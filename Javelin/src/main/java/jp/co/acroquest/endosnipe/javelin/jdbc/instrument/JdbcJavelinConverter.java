/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2012 Acroquest Technology Co.,Ltd.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package jp.co.acroquest.endosnipe.javelin.jdbc.instrument;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.jdbc.common.JdbcJavelinMessages;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.DBProcessor;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.JdbcJavelinConnection;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.JdbcJavelinRecorder;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.ClassPool;
import jp.co.smg.endosnipe.javassist.CtBehavior;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.CtField;
import jp.co.smg.endosnipe.javassist.CtMethod;
import jp.co.smg.endosnipe.javassist.Modifier;
import jp.co.smg.endosnipe.javassist.NotFoundException;

/**
 * JdbcJavelin�̕ϊ��p�N���X
 * @author acroquest
 */
public class JdbcJavelinConverter
{
    /**
     * �R���X�g���N�^
     */
    private JdbcJavelinConverter()
    {
        
    }
    
    /**
     * BCI�ɂ�setXXX�ɖ��ߍ��ރR�[�h�i�p�^�[��A�j
     * 1.PreparedStatement�I�u�W�F�N�g�̃o�C���h�ϐ��ێ��t�B�[���h�iList�j���擾����
     * 2.TreeMap��������΁A�쐬����List�ɓo�^����
     * 3.�o�C���h�ϐ���TreeMap�ɓo�^����B���̍�StatsUtil#toStr��String�^�ɕϊ�����B
     * �@�܂��ݒ�l�ɂĕ����񒷐�������B
     */
    protected static final String BCI_METHOD_A =
            "if(this != null) {                                                                 \n"
                    + "jp.co.acroquest.endosnipe.javelin.jdbc.stats.BindValUtil.recordBindVal(\n"
                    + "    this.getJdbcJavelinBindVal(), this.jdbcJavelinBindValIndex_,         \n"
                    + "    $1, $2, true);                                                       \n"
                    + "}                                                                        \n";

    /**
     * BCI�ɂ�setXXX�ɖ��ߍ��ރR�[�h�i�p�^�[��B�j
     * 1.PreparedStatement�I�u�W�F�N�g�̃o�C���h�ϐ��ێ��t�B�[���h�iList�j���擾����
     * 2.TreeMap��������΁A�쐬����List�ɓo�^����
     * 3.�o�C���h�ϐ���TreeMap�ɓo�^����B���̍�StatsUtil#toStr��String�^�ɕϊ�����B
     */
    protected static final String BCI_METHOD_B =
            "if(this != null) {                                                                 \n"
                    + "jp.co.acroquest.endosnipe.javelin.jdbc.stats.BindValUtil.recordBindVal(\n"
                    + "    this.getJdbcJavelinBindVal(), this.jdbcJavelinBindValIndex_,         \n"
                    + "    $1, $2, false);                                                      \n"
                    + "}                                                                        \n";

    /**
     * BCI�ɂ�setXXX�ɖ��ߍ��ރR�[�h�i�p�^�[��C�j
     * 1.PreparedStatement�I�u�W�F�N�g�̃o�C���h�ϐ��ێ��t�B�[���h�iList�j���擾����
     * 2.TreeMap��������΁A�쐬����List�ɓo�^����
     * 3.�o�C���h�ϐ���TreeMap�ɓo�^����B���̍�String#valueOf��String�^�ɕϊ�����B
     */
    protected static final String BCI_METHOD_C =
            "if(this != null) {                                                                 \n"
                    + "jp.co.acroquest.endosnipe.javelin.jdbc.stats.BindValUtil.recordBindVal(\n"
                    + "    this.getJdbcJavelinBindVal(), this.jdbcJavelinBindValIndex_,         \n"
                    + "    $1, java.lang.String.valueOf($2));                                   \n"
                    + "}                                                                        \n";

    /**
     * BCI�ɂ�setXXX�ɖ��ߍ��ރR�[�h�i�p�^�[��D�j
     * 1.PreparedStatement�I�u�W�F�N�g�̃o�C���h�ϐ��ێ��t�B�[���h�iList�j���擾����
     * 2.TreeMap��������΁A�쐬����List�ɓo�^����
     * 3.�o�C���h�ϐ���TreeMap�ɓo�^����B���̍�"byte[length]:FFFF..."�ɕϊ�����B
     */
    protected static final String BCI_METHOD_D =
            "if(this != null) {                                                                 \n"
                    + "jp.co.acroquest.endosnipe.javelin.jdbc.stats.BindValUtil.recordBindVal(\n"
                    + "    this.getJdbcJavelinBindVal(), this.jdbcJavelinBindValIndex_,         \n"
                    + "    $1, $2);                                                             \n"
                    + "}                                                                        \n";

    /**
     * BCI�ɂ�setXXX�ɖ��ߍ��ރR�[�h�i�p�^�[��E�j
     * 1.PreparedStatement�I�u�W�F�N�g�̃o�C���h�ϐ��ێ��t�B�[���h�iList�j���擾����
     * 2.TreeMap��������΁A�쐬����List�ɓo�^����
     * 3.�o�C���h�ϐ�"(UNSUPPORTED)"��TreeMap�ɓo�^����B
     */
    protected static final String BCI_METHOD_E =
            "if(this != null) {                                                                 \n"
                    + "jp.co.acroquest.endosnipe.javelin.jdbc.stats.BindValUtil.recordBindVal(\n"
                    + "    this.getJdbcJavelinBindVal(), this.jdbcJavelinBindValIndex_,         \n"
                    + "    $1, \"(UNSUPPORTED)\");                                              \n"
                    + "}                                                                        \n";

    /**
     * BCI�ɂ�setXXX�ɖ��ߍ��ރR�[�h�i�p�^�[��F�j
     * 1.PreparedStatement�I�u�W�F�N�g�̃o�C���h�ϐ��ێ��t�B�[���h�iList�j���擾����
     * 2.TreeMap��������΁A�쐬����List�ɓo�^����
     * 3.�o�C���h�ϐ�"null"��TreeMap�ɓo�^����B���̍�String#valueOf��String�^�ɕϊ�����B
     */
    protected static final String BCI_METHOD_F =
            "if(this != null) {                                                                 \n"
                    + "jp.co.acroquest.endosnipe.javelin.jdbc.stats.BindValUtil.recordBindVal(\n"
                    + "    this.getJdbcJavelinBindVal(), this.jdbcJavelinBindValIndex_,         \n"
                    + "    $1, \"null\");                                                       \n"
                    + "}                                                                        \n";

    /**
     * BCI�ɂ�addBatch�ɖ��ߍ��ރR�[�h�ijdbcJavelinBindValIndex_�̃C���N�������g�j
     */
    protected static final String BCI_METHOD_ADD_BATCH =
            "if(this != null) {                                         \n"
                    + "	this.jdbcJavelinBindValIndex_++;                \n"
                    + "}                                                \n";

    /**
     * BCI�ɂ�clearBatch�ɖ��ߍ��ރR�[�h�ijdbcJavelinBindVal�̃N���A�j
     */
    protected static final String BCI_METHOD_CLEAR_BATCH =
            "if(this != null) {                                         \n"
                    + "	this.getJdbcJavelinBindVal().clear();           \n"
                    + "}                                                \n";

    /**
     * ���s�v��擾�pPreparedStatement��setXXX���邽�߂ɁA
     * �K�������\�b�h���ɒu������u���Ώە�����B
     */
    protected static final String REPLACETARGET_OF_PLANPREPARED = "setXXX";

    /**
     * ���s�v��擾�pPreparedStatement��setXXX���邽�߂̃R�[�h�B
     * setXXX�͓K�������\�b�h���ɒu������B
     */
    protected static final String BCI_METHOD_PLANFORPREPARED_SETXXX =
            "if (this != null && this." + JdbcJavelinTransformer.FLAGFORPLANSTMT_FIELD_NAME
                    + " == false && " + "this." + JdbcJavelinTransformer.STMTFORPLAN_FIELD_NAME
                    + " != null) {\n" + "    int indexOfPreparedStmt = 0;\n"
                    + "    int indexForPlanOfStmt = $1;\n"
                    + "    while (indexForPlanOfStmt > this."
                    + JdbcJavelinTransformer.STMTFORPLAN_FIELD_NAME
                    + "[indexOfPreparedStmt].getBindValCount()) {\n"
                    + "        indexForPlanOfStmt -= this."
                    + JdbcJavelinTransformer.STMTFORPLAN_FIELD_NAME
                    + "[indexOfPreparedStmt].getBindValCount();\n"
                    + "        indexOfPreparedStmt++;\n" + "    }\n" + "    int backup = $1;\n"
                    + "    $1 = indexForPlanOfStmt;\n" + "    this."
                    + JdbcJavelinTransformer.STMTFORPLAN_FIELD_NAME
                    + "[indexOfPreparedStmt].getPreparedStatement()."
                    + REPLACETARGET_OF_PLANPREPARED + "($$);\n" + "    $1 = backup;\n"
                    + "}\n";

    /**
     * ���s�v��擾�pPreparedStatement��setXXX���邽�߂̃R�[�h�B
     * setXXX�͓K�������\�b�h���ɒu������B
     * �ȉ��̃��\�b�h�̏ꍇ�Ɏg�p����B
     * setAsciiStream
     * setBinaryStream
     * setUnicodeStream
     */
    protected static final String BCI_METHOD_PLANFORPREPARED_SETINPUTSTREAM =
            "if (this != null && this." + JdbcJavelinTransformer.FLAGFORPLANSTMT_FIELD_NAME
                    + " == false && " + "this." + JdbcJavelinTransformer.STMTFORPLAN_FIELD_NAME
                    + " != null) {\n" + "    int indexOfPreparedStmt = 0;\n"
                    + "    int indexForPlanOfStmt = $1;\n"
                    + "    while (indexForPlanOfStmt > this."
                    + JdbcJavelinTransformer.STMTFORPLAN_FIELD_NAME
                    + "[indexOfPreparedStmt].getBindValCount()) {\n"
                    + "        indexForPlanOfStmt -= this."
                    + JdbcJavelinTransformer.STMTFORPLAN_FIELD_NAME
                    + "[indexOfPreparedStmt].getBindValCount();\n"
                    + "        indexOfPreparedStmt++;\n" + "    }\n" + "    int backup = $1;\n"
                    + "    $1 = indexForPlanOfStmt;\n" + "    this."
                    + JdbcJavelinTransformer.STMTFORPLAN_FIELD_NAME
                    + "[indexOfPreparedStmt].getPreparedStatement()." + "setXXX"
                    + "($1, (java.io.InputStream)"
                    + "new java.io.ByteArrayInputStream(new byte[0]), $3);\n"
                    + "    $1 = backup;\n" + "}\n";

    /**
     * ���s�v��擾�pPreparedStatement��setXXX���邽�߂̃R�[�h�B
     * setXXX�͓K�������\�b�h���ɒu������B
     * �ȉ��̃��\�b�h�̏ꍇ�Ɏg�p����B
     * setCharacterStream
     */
    protected static final String BCI_METHOD_PLANFORPREPARED_SETREADER =
            "if (this != null && this." + JdbcJavelinTransformer.FLAGFORPLANSTMT_FIELD_NAME
                    + " == false && " + "this." + JdbcJavelinTransformer.STMTFORPLAN_FIELD_NAME
                    + " != null) {\n" + "    int indexOfPreparedStmt = 0;\n"
                    + "    int indexForPlanOfStmt = $1;\n"
                    + "    while (indexForPlanOfStmt > this."
                    + JdbcJavelinTransformer.STMTFORPLAN_FIELD_NAME
                    + "[indexOfPreparedStmt].getBindValCount()) {\n"
                    + "        indexForPlanOfStmt -= this."
                    + JdbcJavelinTransformer.STMTFORPLAN_FIELD_NAME
                    + "[indexOfPreparedStmt].getBindValCount();\n"
                    + "        indexOfPreparedStmt++;\n" + "    }\n" + "    int backup = $1;\n"
                    + "    $1 = indexForPlanOfStmt;\n" + "    this."
                    + JdbcJavelinTransformer.STMTFORPLAN_FIELD_NAME
                    + "[indexOfPreparedStmt].getPreparedStatement()." + "setXXX"
                    + "($1, new java.io.StringReader(\"\"), $3);\n" + "    $1 = backup;\n" + "}\n";

    /**
     * ���s�v��擾�pPreparedStatement��addBatch�̂Ƃ��Ɏ��s�����R�[�h�B
     * �p�����^�ݒ�I���t���O��ON�ɂ���B
     */
    protected static final String BCI_METHOD_PLANFORPREPARED_ADDBATCH =
            "if (this != null && this." + JdbcJavelinTransformer.FLAGFORPLANSTMT_FIELD_NAME
                    + " == false && " + "this." + JdbcJavelinTransformer.STMTFORPLAN_FIELD_NAME
                    + " != null) {\n" + "    this."
                    + JdbcJavelinTransformer.FLAGFORPLANSTMT_FIELD_NAME + " = true;\n" + "}\n";

    /**
     * ���s�v��擾�pPreparedStatement��clearBatch�̂Ƃ��Ɏ��s�����R�[�h�B
     * �p�����^�ݒ�I���t���O��OFF�ɂ���B
     */
    protected static final String BCI_METHOD_PLANFORPREPARED_CLEARBATCH =
            "if (this != null && this." + JdbcJavelinTransformer.FLAGFORPLANSTMT_FIELD_NAME
                    + " == false && " + "this." + JdbcJavelinTransformer.STMTFORPLAN_FIELD_NAME
                    + " != null) {\n" + "    " + JdbcJavelinTransformer.FLAGFORPLANSTMT_FIELD_NAME
                    + " = false;\n" + "}\n";

    /**
     * ���s�v��擾�pPreparedStatement��close�̂Ƃ��Ɏ��s�����R�[�h�B
     * ���s�v��擾�pPreparedStatement��close����B
     */
    protected static final String BCI_METHOD_PLANFORPREPARED_CLOSE =
            "if (this != null && this." + JdbcJavelinTransformer.STMTFORPLAN_FIELD_NAME
                    + " != null) {\n" + "    for (int indexOfPlanStmt = 0; indexOfPlanStmt < this."
                    + JdbcJavelinTransformer.STMTFORPLAN_FIELD_NAME
                    + ".length; indexOfPlanStmt++) {\n" + "        this."
                    + JdbcJavelinTransformer.STMTFORPLAN_FIELD_NAME
                    + "[indexOfPlanStmt].getPreparedStatement().close();\n" + "    }\n" + "}\n";

    /** �ݒ�l�ێ�Bean */
    private static final String JAVELIN_RECORDER_NAME = JdbcJavelinRecorder.class.getName();
    
    /** �����̐� */
    private static final int ARGS = 3;
    
    /**
     * Statement�̃��\�b�h�ɑ΂��Čv���R�[�h�𖄂ߍ��ށB
     *
     * @param pool Statement���܂ރv�[���B 
     * @param ctClass Statement����������N���X
     * @return �ϊ�����
     */
    public static CtClass convertConnection(final ClassPool pool, final CtClass ctClass)
    {
        CtClass jvnConnction;
        try
        {
            jvnConnction = pool.get(JdbcJavelinConnection.class.getCanonicalName());
            boolean hasInterface = hasInterface(ctClass, jvnConnction);
            if (hasInterface == false)
            {
                ctClass.addInterface(jvnConnction);
            }
            
            // ���łɃ��\�b�h��ǉ����Ă���ꍇ�͏������s��Ȃ�
            if (hasBehavior(ctClass, "getJdbcJavelinProcessor"))
            {
                return ctClass;
            }

            CtField procField =
                                CtField.make("private " + DBProcessor.class.getCanonicalName()
                                        + " dbProcessor_;", ctClass);
            ctClass.addField(procField);
            CtMethod procGetMethod =
                    CtMethod.make("public " + DBProcessor.class.getCanonicalName()
                            + " getJdbcJavelinProcessor(){ return dbProcessor_; }", ctClass);
            ctClass.addMethod(procGetMethod);
            CtMethod procSetMethod =
                                     CtMethod.make("public void setJdbcJavelinProcessor("
                                             + DBProcessor.class.getCanonicalName()
                                             + " dbProcessor){ dbProcessor_ = dbProcessor; }",
                                                   ctClass);
            ctClass.addMethod(procSetMethod);

            CtField urlField = CtField.make("private String jdbcUrl_;", ctClass);
            ctClass.addField(urlField);
            CtMethod urlMethod =
                                 CtMethod.make("public String getJdbcUrl(){ return jdbcUrl_; }",
                                               ctClass);
            ctClass.addMethod(urlMethod);
            CtMethod urlSetMethod =
                                    CtMethod.make("public void " 
                                                  + "setJdbcUrl(String jdbcUrl)"
                                                  +"{ jdbcUrl_ = jdbcUrl; }", ctClass);
            ctClass.addMethod(urlSetMethod);
        }
        catch (NotFoundException ex)
        {
            SystemLogger.getInstance().warn(ex);
            return null;
        }
        catch (CannotCompileException ex)
        {
            SystemLogger.getInstance().warn(ex);
            return null;
        }

        
        
        CtBehavior[] behaviors = ctClass.getDeclaredBehaviors();
        for (int index = 0; index < behaviors.length; index++)
        {
            CtBehavior method = behaviors[index];
            // ���\�b�h�̒�`���Ȃ��ꍇ�A���邢��public�łȂ�
            // (->�C���^�[�t�F�[�X�ɒ�`����Ă��Ȃ�)�ꍇ�͎��s���Ȃ��B
            final int MODIFIER = method.getModifiers();
            if (Modifier.isAbstract(MODIFIER) || !Modifier.isPublic(MODIFIER))
            {
                continue;
            }
            // BCI�ΏۃN���X�ujava.sql.Connection�v�ɑ΂��āA�R�[�h�]�����s��
            String methodName = method.getName();
            try
            {
                if ("prepareStatement".equals(methodName))
                {
                    addSqlToFieldCon(ctClass, method);
                }
                else if ("prepareCall".equals(methodName))
                {
                    addSqlToFieldCon(ctClass, method);
                }
            }
            catch (CannotCompileException ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }
        
        try
        {
            return ctClass;
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
            return null;
        }        
    }
    /**
     * Statement�̃��\�b�h�ɑ΂��Čv���R�[�h�𖄂ߍ��ށB
     *
     * @param pool Statement���܂ރv�[���B 
     * @param ctClass Statement����������N���X
     * @param method ���\�b�h
     * @param inheritedStatement Statement�����������N���X��ϊ�����ꍇ�� <code>true</code>
     * @param inheritedPreparedStatement PreparedStatement�����������N���X��ϊ�����ꍇ�� <code>true</code>
     */
    public static void convertStatement(final ClassPool pool, final CtClass ctClass,
            final CtBehavior method, boolean inheritedStatement, boolean inheritedPreparedStatement)
    {
        try
        {
            // BCI�ΏۃN���X�ujava.sql.Connection�v�ɑ΂��āA�R�[�h�]�����s��
            String methodName = method.getName();

            // BCI�ΏۃN���X�ujava.sql.Statement�v�ɑ΂��āA�R�[�h�]�����s��
            if (inheritedStatement)
            {
                if (SystemLogger.getInstance().isDebugEnabled())
                {
                    SystemLogger.getInstance().debug("JDBC JAVELIN:-->Running ctMethodName:"
                                                             + methodName);
                }
                
                if ("execute".equals(methodName) || "executeQuery".equals(methodName) 
                        || "executeUpdate".equals(methodName) || "executeBatch".equals(methodName))
                {
                    convertStatementMethod(pool, ctClass, method);
                }
                else if ("addBatch".equals(methodName))
                {
                    addSqlToFieldStat(ctClass, method);
                }
                else if ("clearBatch".equals(methodName))
                {
                    if (inheritedPreparedStatement == false)
                    {
                        delSqlFromField(ctClass, method);
                    }
                }
            }

            // BCI�ΏۃN���X�ujava.sql.PreparedStatement�v�ɑ΂��āA�R�[�h�]�����s��
            if (inheritedPreparedStatement)
            {
                if (SystemLogger.getInstance().isDebugEnabled())
                {
                    SystemLogger.getInstance().debug("JDBC JAVELIN:-->Running ctMethodName:"
                                                             + methodName);
                }
                if ("setString".equals(methodName) || "setObject".equals(methodName))
                {
                    // �p�^�[��A��setter. �����񒷐������x���ɋ�̓I�Ȑݒ�l������.
                    long jdbcStringLimitLength =
                            JdbcJavelinRecorder.getConfig().getJdbcStringLimitLength();
                    String code = BCI_METHOD_A.replaceAll("BCI_METHOD_A_LENGTH",
                                                    String.valueOf(jdbcStringLimitLength));
                    convertPreparedMethod(ctClass, method, code, BCI_METHOD_PLANFORPREPARED_SETXXX);
                }
                else if ("setBigDecimal".equals(methodName) || "setDate".equals(methodName)
                        || "setTime".equals(methodName) || "setTimestamp".equals(methodName))
                {
                    // �p�^�[��B��setter
                    convertPreparedMethod(ctClass, method, BCI_METHOD_B,
                                          BCI_METHOD_PLANFORPREPARED_SETXXX);
                }
                else if ("setBoolean".equals(methodName) || "setShort".equals(methodName)
                        || "setInt".equals(methodName) || "setLong".equals(methodName)
                        || "setFloat".equals(methodName) || "setDouble".equals(methodName))
                {
                    // �p�^�[��C��setter
                    convertPreparedMethod(ctClass, method, BCI_METHOD_C,
                                          BCI_METHOD_PLANFORPREPARED_SETXXX);
                }
                else if ("setByte".equals(methodName) || "setBytes".equals(methodName))
                {
                    // �p�^�[��D��setter
                    convertPreparedMethod(ctClass, method, BCI_METHOD_D,
                                          BCI_METHOD_PLANFORPREPARED_SETXXX);
                }
                else if ("setArray".equals(methodName) || "setBlob".equals(methodName)
                        || "setClob".equals(methodName) || "setRef".equals(methodName)
                        || "setURL".equals(methodName))
                {
                    // �p�^�[��E��setter
                    convertPreparedMethod(ctClass, method, BCI_METHOD_E,
                                          BCI_METHOD_PLANFORPREPARED_SETXXX);
                }
                else if ("setNull".equals(methodName))
                {
                    // �p�^�[��F��setter
                    convertPreparedMethod(ctClass, method, BCI_METHOD_F,
                                          BCI_METHOD_PLANFORPREPARED_SETXXX);
                }
                else if ("setAsciiStream".equals(methodName)
                        || "setBinaryStream".equals(methodName)
                        || "setUnicodeStream".equals(methodName))
                {
                    // TODO Java6.0 �ŃC���^�[�t�F�[�X�̎d�l���ς�������߁A�����̐���3�ȊO�̂��̂��Ή����K�v�B
                    if (method.getParameterTypes().length == ARGS)
                    {
                        // �p�^�[��G��setter
                        convertPreparedMethod(ctClass, method, BCI_METHOD_E,
                                              BCI_METHOD_PLANFORPREPARED_SETINPUTSTREAM);
                    }
                }
                else if ("setCharacterStream".equals(methodName))
                {
                    // TODO Java6.0 �ŃC���^�[�t�F�[�X�̎d�l���ς�������߁A�����̐���3�ȊO�̂��̂��Ή����K�v�B
                    if (method.getParameterTypes().length == ARGS)
                    {
                        // �p�^�[��H��setter
                        convertPreparedMethod(ctClass, method, BCI_METHOD_E,
                                              BCI_METHOD_PLANFORPREPARED_SETREADER);
                    }
                }
                else if ("addBatch".equals(methodName))
                {
                    convertPreparedMethodAddBatch(ctClass, method);
                }
                else if ("clearBatch".equals(methodName))
                {
                    convertPreparedMethodClearBatch(ctClass, method);
                }
                else if ("close".equals(methodName))
                {
                    convertPreparedMethodClose(ctClass, method);
                }
                if ("execute".equals(methodName) || "executeBatch".equals(methodName)
                        || "executeQuery".equals(methodName) || "executeUpdate".equals(methodName))
                {
                    convertExecuteMethod(ctClass, method);
                }
            }
        }
        catch (Throwable ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
    }

    /**
     * PreparedStatement��execute���\�b�h�ɁA
     * �o�C���h�����ۑ��pArrayList������������ǉ�����B
     * @param ctClass �ϊ��Ώۂ̃N���X�B
     * @param method ���\�b�h�B
     * @throws CannotCompileException javassist���R���p�C���Ɏ��s�����ꍇ�B
     */
    private static void convertExecuteMethod(final CtClass ctClass, final CtBehavior method)
        throws CannotCompileException
    {
        String className = ctClass.getName();
        className = className.substring(className.lastIndexOf('.') + 1);

        // �O�����𖄂ߍ���
        String key = "javelin.jdbc.instrument.JdbcJavelinConverter.ModifiedMethodLabel";
        String message = JdbcJavelinMessages.getMessage(key, className, method.getName());
        SystemLogger.getInstance().info(message);
        method.insertAfter("this.jdbcJavelinBindValIndex_ = 0;" + "this.flagForPlanStmt_ = false;",
                           true);
    }

    /**
     * PreparedStatement�p�Ƀp�^�[���ʂ̃R�[�h�𖄂ߍ���
     * @param ctClass �N���X
     * @param method ���\�b�h
     * @param bindValCode �o�C���h�ϐ��擾�p�R�[�h
     * @param explainCodeTemplate ���s�v��擾�p�R�[�h
     * @throws CannotCompileException �R�[�h���ߍ��݂Ɏ��s�����ꍇ
     */
    public static void convertPreparedMethod(final CtClass ctClass, final CtBehavior method,
            final String bindValCode, final String explainCodeTemplate)
        throws CannotCompileException
    {
        String className = ctClass.getName();
        String methodName = method.getName();
        className = className.substring(className.lastIndexOf('.') + 1);

        // set���\�b�h�̈�����1�Ԗڂ�int�̂Ƃ��̂݁A���s�v��擾�p�����A�o�C���h�����擾������ǉ�����
        try
        {
            CtClass[] paramTypes;
            paramTypes = method.getParameterTypes();
            if (paramTypes.length >= 1 && "int".equals(paramTypes[0].getName()))
            {
                // ���s�v��擾�pPreparedStatement��setXXX��K�������\�b�h���ɕύX����
                String explainCode = explainCodeTemplate.replaceAll(REPLACETARGET_OF_PLANPREPARED,
                                                                    methodName);
                
                // �O�����𖄂ߍ���
                String key = "javelin.jdbc.instrument.JdbcJavelinConverter.ModifiedMethodLabel";
                String message = JdbcJavelinMessages.getMessage(key, className, methodName);
                SystemLogger.getInstance().info(message);

                method.insertBefore(bindValCode + explainCode);
            }
        }
        catch (NotFoundException e)
        {
            SystemLogger.getInstance().warn(e);
        }

    }

    /**
     * PreparedStatement#addBatch�p�ɃR�[�h�𖄂ߍ���
     * 
     * @param ctClass PreparedStatement����������N���X
     * @param method addBatch���\�b�h
     * @throws CannotCompileException �R�[�h���ߍ��݂Ɏ��s�����ꍇ
     */
    public static void convertPreparedMethodAddBatch(final CtClass ctClass, final CtBehavior method)
        throws CannotCompileException
    {
        String className = ctClass.getName();
        className = className.substring(className.lastIndexOf('.') + 1);

        // �O�����𖄂ߍ���
        String key = "javelin.jdbc.instrument.JdbcJavelinConverter.ModifiedMethodLabel";
        String message = JdbcJavelinMessages.getMessage(key, className, method.getName());
        SystemLogger.getInstance().info(message);
        method.insertAfter(BCI_METHOD_ADD_BATCH + BCI_METHOD_PLANFORPREPARED_ADDBATCH);
    }

    /**
     * PreparedStatement#clearBatch�p�ɃR�[�h�𖄂ߍ���
     * 
     * @param ctClass PreparedStatement����������N���X
     * @param method clearBatch���\�b�h
     * @throws CannotCompileException �R�[�h���ߍ��݂Ɏ��s�����ꍇ
     */
    public static void convertPreparedMethodClearBatch(final CtClass ctClass,
            final CtBehavior method)
        throws CannotCompileException
    {
        String className = ctClass.getName();
        className = className.substring(className.lastIndexOf('.') + 1);

        // �O�����𖄂ߍ���
        String key = "javelin.jdbc.instrument.JdbcJavelinConverter.ModifiedMethodLabel";
        String message = JdbcJavelinMessages.getMessage(key, className, method.getName());
        SystemLogger.getInstance().info(message);
        method.insertAfter(BCI_METHOD_CLEAR_BATCH + BCI_METHOD_PLANFORPREPARED_CLEARBATCH);
    }

    /**
     * PreparedStatement#close�p�ɃR�[�h�𖄂ߍ���
     *
     * @param ctClass PreparedStatement����������N���X
     * @param method close���\�b�h
     * @throws CannotCompileException �R�[�h���ߍ��݂Ɏ��s�����ꍇ
     */
    public static void convertPreparedMethodClose(final CtClass ctClass, final CtBehavior method)
        throws CannotCompileException
    {
        String className = ctClass.getName();
        className = className.substring(className.lastIndexOf('.') + 1);

        // �O�����𖄂ߍ���
        String key = "javelin.jdbc.instrument.JdbcJavelinConverter.ModifiedMethodLabel";
        String message = JdbcJavelinMessages.getMessage(key, className, method.getName());
        SystemLogger.getInstance().info(message);
        method.insertAfter(BCI_METHOD_PLANFORPREPARED_CLOSE);
    }

    /**
     * Statement����������N���X�Ɍv���R�[�h�𖄂ߍ��ށB
     *
     * @param pool �N���X�v�[��
     * @param ctClass Statement����������N���X
     * @param method ���ߍ��ݑΏۃ��\�b�h
     * @throws CannotCompileException �R�[�h���ߍ��݂Ɏ��s�����ꍇ
     */
    public static void convertStatementMethod(final ClassPool pool, final CtClass ctClass,
            final CtBehavior method)
        throws CannotCompileException
    {
        // StatsJavelin�ɂ��v���R�[�h�𖄂ߍ��ށiStatement�̕��p�j
        addRecordCode(ctClass, method);
        convertCatch(pool, ctClass, method);

        if (SystemLogger.getInstance().isDebugEnabled())
        {
            String tegKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
            String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tegKey);
            String messageKey = "javelin.jdbc.instrument.JdbcJavelinConverter.ConvertLabel";
            String message = JdbcJavelinMessages.getMessage(messageKey, method.getName());
            SystemLogger.getInstance().debug(jdbcJavelinTag + message);
        }
    }

    /**
     * StatsJavelin�ɂ��v���R�[�h�𖄂ߍ��ށB
     * 
     * @param ctClass �N���X
     * @param method ���\�b�h
     * @throws CannotCompileException ���\�b�h�̊J�n�ʒu�ƏI���ʒu�ɃR�[�h�𖄂ߍ��߂Ȃ������ꍇ
     */
    public static void addRecordCode(final CtClass ctClass, final CtBehavior method)
        throws CannotCompileException
    {
        // �O�������O�R�[�h�����
        StringBuffer callPreProcessCodeBuffer = new StringBuffer();
        callPreProcessCodeBuffer.append(JAVELIN_RECORDER_NAME);
        CtClass[] parameterTypes = null;
        boolean paramZero = false;
        try
        {
            parameterTypes = method.getParameterTypes();
        }
        catch (NotFoundException ex)
        {
            SystemLogger.getInstance().warn("", ex);
        }
        if (parameterTypes != null && (parameterTypes.length > 0))
        {
            callPreProcessCodeBuffer.append(".preProcessParam(");
            callPreProcessCodeBuffer.append("$0");
            callPreProcessCodeBuffer.append(", $args);");
        }
        else
        {
            callPreProcessCodeBuffer.append(".preProcessSQLArgs(");
            callPreProcessCodeBuffer.append("$0");
            callPreProcessCodeBuffer.append(", this.getJdbcJavelinSql().toArray());");
            paramZero = true;
        }
        String callPreProcessCode;
        callPreProcessCode = callPreProcessCodeBuffer.toString();

        // �㏈�����O�R�[�h�����
        StringBuffer callPostProcessCodeBuffer = new StringBuffer();
        callPostProcessCodeBuffer.append(JAVELIN_RECORDER_NAME);
        // Recorder�ɂĎ��s�v��擾��Statemen�A�N���X���A���\�b�h�����K�v
        callPostProcessCodeBuffer.append(".postProcessOK($0");
        if (paramZero == false)
        { // �����̐���postProcessOK�ɓn��
            callPostProcessCodeBuffer.append(", 1"); // ����1�ȏ�
        }
        else
        {
            callPostProcessCodeBuffer.append(", 0"); // ����0
        }

        callPostProcessCodeBuffer.append(");");
        String returnPostProcessCode = callPostProcessCodeBuffer.toString();
        //		JavelinErrorLogger.getInstance().log("modified class:" + className);
        method.insertBefore(callPreProcessCode);
        method.insertAfter(returnPostProcessCode);
    }

    private static void addSqlToFieldCon(final CtClass ctClass, final CtBehavior method)
        throws CannotCompileException
    {
        String addSqlCode =
                JdbcJavelinRecorder.class.getName() + ".postPrepareStatement($0, $1, $_, \""
                        + method.getName() + "\");";
        method.insertAfter(addSqlCode);
        String key = "javelin.jdbc.instrument.JdbcJavelinConverter.ModifiedMethodLabel";
        String message = JdbcJavelinMessages.getMessage(key, ctClass.getName(), method.getName());
        SystemLogger.getInstance().info(message);
        if (SystemLogger.getInstance().isDebugEnabled())
        {
            String tegKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
            String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tegKey);
            String messageKey = "javelin.jdbc.instrument.JdbcJavelinConverter.SQLAddedLabel1";
            String logMessage = JdbcJavelinMessages.getMessage(messageKey, method.getName());
            SystemLogger.getInstance().debug(jdbcJavelinTag + logMessage);
        }
    }

    private static void addSqlToFieldStat(final CtClass ctClass, final CtBehavior method)
        throws CannotCompileException
    {
        try
        {
            CtClass[] parameterTypes = method.getParameterTypes();
            if (!(parameterTypes == null) && (parameterTypes.length > 0))
            {
                // ���s���Ă���SQL����ǉ�����p�R�[�h��ǉ�����iStatement��addBatch���\�b�h�p�j
                String addSqlCode = "if(this != null) this.jdbcJavelinSql_.add($1);";
                method.insertAfter(addSqlCode);

                if (SystemLogger.getInstance().isDebugEnabled())
                {
                    String tagKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
                    String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tagKey);
                    String messageKey =
                            "javelin.jdbc.instrument.JdbcJavelinConverter.SQLAddedLabel2";
                    String logMessage =
                            JdbcJavelinMessages.getMessage(messageKey, method.getName());
                    SystemLogger.getInstance().debug(jdbcJavelinTag + logMessage);
                }
            }
        }
        catch (NotFoundException e)
        {
            SystemLogger.getInstance().warn("", e);
        }
    }

    /**
     * �t�B�[���h����SQL���폜����
     * @param ctClass �N���X
     * @param method ���\�b�h
     * @throws CannotCompileException �R���p�C���s���̃G���[
     */
    public static void delSqlFromField(final CtClass ctClass, final CtBehavior method)
        throws CannotCompileException
    {
        // ���s���Ă���SQL����ǉ�����p�R�[�h��ǉ�����iStatement�̕��j
        String addSqlCode = "if(this != null) this.jdbcJavelinSql_.clear();";
        method.insertAfter(addSqlCode);

        if (SystemLogger.getInstance().isDebugEnabled())
        {
            String tagKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
            String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tagKey);
            String messageKey = "javelin.jdbc.instrument.JdbcJavelinConverter.SQLDeletedLabel3";
            String logMessage = JdbcJavelinMessages.getMessage(messageKey, method.getName());
            SystemLogger.getInstance().debug(jdbcJavelinTag + logMessage);
        }
    }

    /**
     * ���O�擾�R�[�h��Throwable��catch�߂Ƃ��Ēǉ�����
     * @param pool Statement���܂ރv�[��
     * @param ctClass PreparedStatement����������N���X
     * @param behaviour behaviour
     * @throws CannotCompileException �R���p�C�����ł��Ȃ��Ƃ��̗�O
     */
    public static void convertCatch(final ClassPool pool, final CtClass ctClass,
            final CtBehavior behaviour)
        throws CannotCompileException
    {
        try
        {
            CtClass throwable = pool.get("java.lang.Throwable");

            // ���\�b�h�̒�`���Ȃ��ꍇ�͎��s���Ȃ��B
            final int MODIFIER = behaviour.getModifiers();
            if (Modifier.isAbstract(MODIFIER))
            {
                return;
            }

            // JavelinLogger#writeExceptionLog�̌Ăяo���R�[�h���쐬����B
            StringBuffer code = new StringBuffer();

            // �㏈���i��O�ꍇ�j
            code.append(JAVELIN_RECORDER_NAME);
            code.append(".postProcessNG(");
            code.append("$e");
            code.append(");");

            // ��O����throw����B
            code.append("throw $e;");

            // ���O�擾�R�[�h��Throwable��catch�߂Ƃ��Ēǉ�����B
            behaviour.addCatch(code.toString(), throwable);
        }
        catch (NotFoundException nfe)
        {
            SystemLogger.getInstance().warn(nfe);
        }
    }

    /**
     * �w�肵���N���X���w�肵���C���^�[�t�F�[�X���������肷��
     * @param targetClass ���肷��ΏۃN���X
     * @param interfaceClass �m�F����C���^�[�t�F�[�X
     * @return �w�肵���C���^�[�t�F�[�X�����Ƃ�true/�����łȂ��Ƃ�false
     */
    public static boolean hasInterface(CtClass targetClass, CtClass interfaceClass)
    {
        boolean hasInterface = false;

        try
        {
            for (CtClass stmtInterface : targetClass.getInterfaces())
            {
                if (stmtInterface.equals(interfaceClass))
                {
                    hasInterface = true;
                    break;
                }
            }
        }
        catch (NotFoundException ex)
        {
            // �������Ȃ��B
            SystemLogger.getInstance().warn(ex);
        }
        return hasInterface;
    }

    /**
     * �w�肵���N���X�Ɏw�肵�����\�b�h�����݂��邩�ǂ����𒲂ׂ܂��B
     *
     * @param targetClass ���\�b�h�̑��݂𒲂ׂ�N���X
     * @param methodName ���\�b�h��
     * @return ���\�b�h�����݂���ꍇ�� <code>true</code> �A���݂��Ȃ��ꍇ�� <code>false</code>
     */
    public static boolean hasBehavior(final CtClass targetClass, final String methodName)
    {
        boolean method = false;
        CtBehavior[] declaredBehaviors = targetClass.getDeclaredBehaviors();
        for (CtBehavior behavior : declaredBehaviors)
        {
            String behaviorName = behavior.getName();
            if (methodName.equals(behaviorName))
            {
                method = true;
                break;
            }
        }
        return method;
    }

}
