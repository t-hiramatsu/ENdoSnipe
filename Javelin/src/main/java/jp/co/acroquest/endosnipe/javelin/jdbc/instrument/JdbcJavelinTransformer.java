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

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.common.JavassistUtil;
import jp.co.acroquest.endosnipe.javelin.jdbc.common.JdbcJavelinConfig;
import jp.co.acroquest.endosnipe.javelin.jdbc.common.JdbcJavelinMessages;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.JdbcJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.JdbcJavelinStatement;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.ClassPool;
import jp.co.smg.endosnipe.javassist.CtBehavior;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.CtField;
import jp.co.smg.endosnipe.javassist.CtMethod;
import jp.co.smg.endosnipe.javassist.Modifier;
import jp.co.smg.endosnipe.javassist.NotFoundException;

/**
 * Java Instrumentation API�ɂ��Ajavaagent�Ƃ��ăN���X�̕ϊ����s��.
 *
 * @author acroquest
 */
public class JdbcJavelinTransformer implements ClassFileTransformer
{
    /** �s��pool��Map */
    private static Map<ClassLoader, ClassPool> loaderPoolMap__ =
            new HashMap<ClassLoader, ClassPool>();

    /** ���s�v��擾�pPreparedStatement�t�B�[���h��. */
    public static final String STMTFORPLAN_FIELD_NAME = "stmtForPlan_";

    /** ���s�v��擾�pPreparedStatement�t�B�[���h�錾. */
    private static final String STMTFORPLAN_FIELD_DEF =
            "public jp.co.acroquest.endosnipe.javelin.jdbc.instrument.PreparedStatementPair[] "
                    + STMTFORPLAN_FIELD_NAME + ";";

    /** ���s�v��擾�p�R�[�h. */
    private static final String STMTFORPLAN_GETTER_METHOD_DEF =
            "public jp.co.acroquest.endosnipe.javelin."
            + "jdbc.instrument.PreparedStatementPair[] getStmtForPlan() " 
            + "{return this." 
            + STMTFORPLAN_FIELD_NAME + "; }";
    
    /** ���s�v��ݒ�p�R�[�h. */
    private static final String STMTFORPLAN_SETTER_METHOD_DEF =
              "public void setStmtForPlan("
            + "jp.co.acroquest.endosnipe.javelin.jdbc.instrument.PreparedStatementPair[] stmts) " 
            + "{this." 
            + STMTFORPLAN_FIELD_NAME + " = stmts;}";

    /** ���s�v��擾�pPreparedStatement�p�����^�ݒ�I���t���O�t�B�[���h��. */
    protected static final String FLAGFORPLANSTMT_FIELD_NAME = "flagForPlanStmt_";

    /** ���s�v��擾�pPreparedStatement�p�����^�ݒ�I���t���O�t�B�[���h�錾. */
    private static final String FLAGFORPLANSTMT_FIELD_DEF =
            "private boolean " + FLAGFORPLANSTMT_FIELD_NAME + " = false;";

    /** SQL�擾�p�R�[�h. */
    private static final String SQL_GETTER_METHOD_DEF =
            "public java.util.List getJdbcJavelinSql() " + "{return this.jdbcJavelinSql_;}";

    /** SQL�擾�p�R�[�h */
    private static final String SQL_GETTER_INTERFACE_DEF =
            "public java.util.List getJdbcJavelinSql();";

    /** ���s����SQL��ۑ�����. */
    public static final String SQL_FIELD_NAME = "jdbcJavelinSql_";

    /** ���s����SQL��ۑ�����t�B�[���h�̒�`. */
    private static final String SQL_FIELD_DEF =
            "public java.util.List " + SQL_FIELD_NAME + " = new java.util.ArrayList();";

    /** �o�C���h�ϐ��擾�p�R�[�h */
    private static final String BINDVAL_GETTER_INTERFACE_DEF =
            "public java.util.List getJdbcJavelinBindVal();";

    /** �o�C���h�ϐ��擾�p�R�[�h. */
    private static final String BINDVAL_GETTER_METHOD_DEF =
            "public java.util.List getJdbcJavelinBindVal()" + "{return this.jdbcJavelinBindVal_;}";

    /** �o�C���h�ϐ���ۑ�����t�B�[���h. */
    private static final String BINDVAL_FIELD_NAME = "jdbcJavelinBindVal_";

    /** �o�C���h�ϐ���ۑ�����t�B�[���h�̒�`. */
    private static final String BINDVAL_FIELD_DEF =
            "public java.util.List jdbcJavelinBindVal_ = " + "new java.util.ArrayList();";


    /** �o�C���h�ϐ��擾�p�R�[�h. */
    private static final String BINDVAL_INDEX_GETTER_METHOD_DEF =
            "public int getJdbcJavelinBindIndex()" + "{return this.jdbcJavelinBindValIndex_;}";

    /** �o�C���h�ϐ�index�� */
    public static final String BINDVAL_IDX_FIELD_NAME = "jdbcJavelinBindValIndex_";

    /** �o�C���h�ϐ�index��` */
    private static final String BINDVAL_IDX_FIELD_DEF = "public int jdbcJavelinBindValIndex_ = 0;";

    /** ���\�b�h�̃p�����^�̌^����菜�����߂̐��K�\�� */
    private static final Pattern PARAM_TYPE_PATTERN = Pattern.compile("[A-Za-z\\.]+[\\[\\]]* ()");

    /** substring�J�n�ʒu */
    private static final int SUBSTRING_BEGIN_INDEX = 5;
    
    /**
     * �R���X�g���N�^.
     */
    public JdbcJavelinTransformer()
    {
        // �������Ȃ�
    }

    /**
     * ����������.
     * �ݒ��ǂݍ���.
     */
    public void init()
    {
        // JdbcJavelin�֘A�̐ݒ�l��javelin.conf�t�@�C������Ǎ���
        JdbcJavelinConfig config = new JdbcJavelinConfig();
        JdbcJavelinRecorder.setJdbcJavelinConfig(config);

        // JDBC�̐ݒ�l���擾����
        boolean recordExecPlan = config.isRecordExecPlan();
        boolean fullScanMonitor = config.isFullScanMonitor();
        boolean recordDuplJdbcCall = config.isRecordDuplJdbcCall();
        boolean recordBindVal = config.isRecordBindVal();
        long execPlanThreshold = config.getExecPlanThreshold();
        long jdbcStringLimitLength = config.getJdbcStringLimitLength();
        boolean allowSqlTraceForOracle = config.isAllowSqlTraceForOracle();
        boolean verbosePlanForPostgres = config.isVerbosePlanForPostgres();
        int queryLimitCount = config.getRecordStatementNumMax();
        boolean recordStackTrace = config.isRecordStackTrace();
        int recordStackTraceThreshold = config.getRecordStackTraceThreshold();

        // JDBC�̐ݒ�l��W���o�͂���
        PrintStream out = System.out;
        String key =
                "javelin.jdbc.instrument.JdbcJavelinTransformer.PropertiesRelatedWithJDBCJavelin";
        String message = JdbcJavelinMessages.getMessage(key);
        out.println(">>>> " + message);
        out.println("\tjavelin.jdbc.recordExecPlan               : " + recordExecPlan);
        out.println("\tjavelin.jdbc.fullScan.monitor             : " + fullScanMonitor);
        out.println("\tjavelin.jdbc.recordDuplJdbcCall           : " + recordDuplJdbcCall);
        out.println("\tjavelin.jdbc.recordBindVal                : " + recordBindVal);
        out.println("\tjavelin.jdbc.execPlanThreshold            : " + execPlanThreshold);
        out.println("\tjavelin.jdbc.stringLimitLength            : " + jdbcStringLimitLength);
        out.println("\tjavelin.jdbc.oracle.allowSqlTrace         : " + allowSqlTraceForOracle);
        out.println("\tjavelin.jdbc.postgres.verbosePlan         : " + verbosePlanForPostgres);
        out.println("\tjavelin.jdbc.record.statement.num.maximum : " + queryLimitCount);
        out.println("\tjavelin.jdbc.record.stackTrace            : " + recordStackTrace);
        out.println("\tjavelin.jdbc.record.stackTraceThreshold   : " + recordStackTraceThreshold);
        out.println("<<<<");
    }

    /**
     * �R�[�h���ߍ��ݑΏۃN���X���ǂ����𔻒肵�A
     * �ΏۃN���X�ɑ΂��ăR�[�h�𖄂ߍ��ށB
     * @param loader �N���X���[�_
     * @param className �N���X��
     * @param classBeingRedefined �Ē�`�����N���X
     * @param protectionDomain �ی�̈�
     * @param classfileBuffer �N���X�t�@�C���̃o�b�t�@
     * @throws IllegalClassFormatException �s���ȃN���X�̃t�H�[�}�b�g
     * @return �ϊ���̃N���X
     */
    public byte[] transform(final ClassLoader loader, final String className,
            final Class<?> classBeingRedefined, final ProtectionDomain protectionDomain,
            final byte[] classfileBuffer)
        throws IllegalClassFormatException
    {
        // �N���X�����ߍ��߂Ȃ��`���ł���ꍇ��
        // IllegalClassFormatException�𓊂���
        try
        {
            ClassPool pool;
            if (loader instanceof URLClassLoader)
            {
                pool = loaderPoolMap__.get(loader);
                if (pool == null)
                {
                    pool = new ClassPool(ClassPool.getDefault());
                    appendLoaderClassPath(pool, (URLClassLoader)loader);

                    loaderPoolMap__.put(loader, pool);
                }
            }
            else
            {
                pool = ClassPool.getDefault();
            }

            // Interface�́uStatement�v��]������
            CtClass statement = createModifiedStatement(pool);
            if ("java/sql/Statement".equals(className))
            {
                return statement.toBytecode();
            }

            // �o�C���h�ϐ��o�͑Ή�
            // Interface�́uPreparedStatementStatement�v��]������
            CtClass pStatement = createModifiedPreparedStatement(pool);
            if ("java/sql/PreparedStatement".equals(className))
            {
                return pStatement.toBytecode();
            }

            // �v���Ώۂ���A�]���N���X�����
            ByteArrayInputStream stream = new ByteArrayInputStream(classfileBuffer);

            // CtClass�擾�̌��܂育�ƁB
            // �N���X�Ƀ��\�b�h�𖄂ߍ��߂�悤�ɂ���B
            // �����N���X�������S�C�����łȂ������ꍇ�́A
            // �o�C�g�z�񂩂�CtClass���쐬����B
            CtClass ctClass = null;
            try
            {
                // ClassPool���Ǘ�����N���X���̃Z�p���[�^��
                // Java�̊��S�C�����p�ɕϊ�����
                ctClass = pool.get(className.replaceAll("/", "."));
                if (ctClass.isFrozen() == true)
                {
                    ctClass.defrost();
                    ctClass.stopPruning(true);
                }
                else
                {
                    ctClass = null;
                }
            }
            catch (Exception ex)
            {
                // �������Ȃ�
                SystemLogger.getInstance().warn(ex);
            }
            if (ctClass == null)
            {
                ctClass = pool.makeClass(stream);
                ctClass.defrost();
                ctClass.stopPruning(true);
            }

            return transform(className, classfileBuffer, pool, ctClass);
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
            IllegalClassFormatException e = new IllegalClassFormatException();
            e.initCause(ex);
            throw e;
        }
    }

    /**
     * �N���X�p�X��ǉ�����.
     * @param pool �N���X�v�[��
     * @param loader �N���X���[�_
     */
    private void appendLoaderClassPath(final ClassPool pool, final URLClassLoader loader)
    {
        URL[] urls = loader.getURLs();
        for (URL url : urls)
        {
            String value = url.toExternalForm();
            if (value.startsWith("file:"))
            {
                try
                {
                    pool.appendClassPath(value.substring(SUBSTRING_BEGIN_INDEX));
                }
                catch (NotFoundException ex)
                {
                    SystemLogger.getInstance().warn(ex);
                }
            }
        }

        ClassLoader parentLoader = loader.getParent();
        if (parentLoader instanceof URLClassLoader)
        {
            appendLoaderClassPath(pool, (URLClassLoader)parentLoader);
        }
    }

    /**
     * 
     * @param className �N���X��
     * @param classfileBuffer �N���X�t�@�C���o�b�t�@
     * @param pool �N���X�v�[��
     * @param ctClass �����N���X
     * @return �ϊ���̃N���X
     * @throws IllegalClassFormatException �s���ȃN���X�t�H�[�}�b�g
     * @throws NotFoundException �N���X��������Ȃ�
     * @throws CannotCompileException �R���p�C���ł��Ȃ�
     */
    public byte[] transform(final String className, final byte[] classfileBuffer,
            final ClassPool pool, CtClass ctClass)
        throws IllegalClassFormatException,
            NotFoundException,
            CannotCompileException
    {
        // Interface�������
        if (ctClass.isInterface())
        {
            return null;
        }

        if (JavassistUtil.isInherited(ctClass, pool, "java.sql.Connection"))
        {
            ctClass = JdbcJavelinConverter.convertConnection(pool, ctClass);
            try
            {
                return ctClass.toBytecode();
            }
            catch (Exception ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }

        // JDBCJavelin��BCI�ΏۈȊO�N���X�������
        boolean inheritedStatement = JavassistUtil.isInherited(ctClass, pool, "java.sql.Statement");
        if (inheritedStatement == false)
        {
            return null;
        }

        // BCI�Ώ�Statement�ɑ΂���A�ϊ����s��
        boolean inheritedPreparedStatement = 
                JavassistUtil.isInherited(ctClass,
                                          pool,
                                          "java.sql.PreparedStatement");
        if (inheritedPreparedStatement)
        {
            // �o�C���h�ϐ��o�͂̂��ߒǉ�
            ctClass = createModifiedInheritedPreparedStatement(className, pool);
        }
        else
        {
            ctClass = createModifiedInheritedStatement(className, pool);
        }

        // �ϊ����ʂ�߂�
        String tagKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
        String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tagKey);
        String messageKey = "javelin.jdbc.instrument.JdbcJavelinTransformer.BCIClassNameLabel";
        String message = JdbcJavelinMessages.getMessage(messageKey, ctClass.getName());
        SystemLogger.getInstance().info(jdbcJavelinTag + message);

        CtBehavior[] behaviors = ctClass.getDeclaredBehaviors();
        for (int index = 0; index < behaviors.length; index++)
        {
            CtBehavior behaviour = behaviors[index];
            // ���\�b�h�̒�`���Ȃ��ꍇ�A���邢��public�łȂ�
            // (->�C���^�[�t�F�[�X�ɒ�`����Ă��Ȃ�)�ꍇ�͎��s���Ȃ��B
            final int MODIFER = behaviour.getModifiers();
            if (Modifier.isAbstract(MODIFER) || !Modifier.isPublic(MODIFER))
            {
                continue;
            }
            JdbcJavelinConverter.convertStatement(pool, ctClass, behaviour, inheritedStatement,
                                                  inheritedPreparedStatement);
            
        }

        try
        {
            return ctClass.toBytecode();
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        return null;
    }

    /**
     * Statement�����N���X�p��BCI�����B
     *
     * @param className PreparedStatement�����N���X
     * @param pool �N���X�v�[��
     * @return �ύX����PreparedStatement�����N���X
     * @throws NotFoundException className�Ŏw�肳�ꂽ�N���X��������Ȃ��ꍇ
     * @throws CannotCompileException ���\�b�h��ǉ��ł��Ȃ��ꍇ
     */
    private CtClass createModifiedInheritedStatement(String className, final ClassPool pool)
        throws NotFoundException,
            CannotCompileException
    {

        className = className.replace('/', '.');
        CtClass ctClassStatement = pool.get(className);
        CtField ctSQLField;
        CtMethod ctGetSQLField;

        synchronized (this)
        {
            try
            {
                ctClassStatement.getDeclaredField(SQL_FIELD_NAME);
            }
            catch (NotFoundException nofExp)
            {
                // field�����
                ctSQLField = CtField.make(SQL_FIELD_DEF, ctClassStatement);
                // field��ǉ�����
                ctClassStatement.addField(ctSQLField);

                try
                {
                    ctClassStatement.getDeclaredMethod("getJdbcJavelinSql");
                }
                catch (NotFoundException nfe)
                {
                    // Mehtod�����
                    ctGetSQLField = CtMethod.make(SQL_GETTER_METHOD_DEF, ctClassStatement);
                    // Method��ǉ�����
                    ctClassStatement.addMethod(ctGetSQLField);

                    String tagKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
                    String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tagKey);
                    String messageKey = "javelin.jdbc.instrument.JdbcJavelinTransformer."
                                      + "AddJdbcJavelinSqlMessage";
                    String message = JdbcJavelinMessages.getMessage(messageKey, className);
                    SystemLogger.getInstance().info(jdbcJavelinTag + message);
                }
            }
        }

        // ����Statement�C���^�t�F�[�X�����N���X��
        // addBatch()�AclearBatch()�Aexecute()�AexecuteQury()�A
        // executeBatch()�AexecuteUpdate����������Ă��Ȃ���΁A
        // ���ꂼ�ꃁ�\�b�h��ǉ����A�I�[�o�[���C�h����B
        addAddBatchMethodOfStatement(pool, className);
        addClearBatchMethod(pool, className, "");
        addExecuteMethodOfStatement(pool, className);
        addExecuteQueryMethodOfStatement(pool, className);
        addExecuteBatchMethodOfStatement(pool, className);
        addExecuteUpdateMethodOfStatement(pool, className);

        return ctClassStatement;
    }

    /**
     * PreparedStatement�����N���X�p��BCI�����B
     *
     * @param className PreparedStatement�����N���X
     * @param pool �N���X�v�[��
     * @return �ύX����PreparedStatement�����N���X
     * @throws NotFoundException className�Ŏw�肳�ꂽ�N���X��������Ȃ��ꍇ
     * @throws CannotCompileException ���\�b�h��ǉ��ł��Ȃ��ꍇ
     */
    private CtClass createModifiedInheritedPreparedStatement(String className, final ClassPool pool)
        throws NotFoundException,
            CannotCompileException
    {

        className = className.replace('/', '.');
        CtClass ctClassStatement = pool.get(className);
        CtField ctSQLField;
        CtMethod ctGetSQLField;

        synchronized (this)
        {
            try
            {
                ctClassStatement.getDeclaredField(SQL_FIELD_NAME);
            }
            catch (NotFoundException nofExp)
            {
                // field�����
                ctSQLField = CtField.make(SQL_FIELD_DEF, ctClassStatement);
                // field��ǉ�����
                ctClassStatement.addField(ctSQLField);

                try
                {
                    ctClassStatement.getDeclaredMethod("getJdbcJavelinSql");
                }
                catch (NotFoundException nfe)
                {
                    // Mehtod�����
                    ctGetSQLField = CtMethod.make(SQL_GETTER_METHOD_DEF, ctClassStatement);
                    // Method��ǉ�����
                    ctClassStatement.addMethod(ctGetSQLField);

                    String tagKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
                    String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tagKey);
                    String messageKey = "javelin.jdbc.instrument.JdbcJavelinTransformer."
                                      + "AddJdbcJavelinSqlMessage";
                    String message = JdbcJavelinMessages.getMessage(messageKey, className);
                    SystemLogger.getInstance().info(jdbcJavelinTag + message);
                }
            }

            try
            {
                ctClassStatement.getDeclaredField(BINDVAL_FIELD_NAME);
            }
            catch (NotFoundException nofExp)
            {
                // field�����
                ctSQLField = CtField.make(BINDVAL_FIELD_DEF, ctClassStatement);
                // field��ǉ�����
                ctClassStatement.addField(ctSQLField);

                ctSQLField = CtField.make(BINDVAL_IDX_FIELD_DEF, ctClassStatement);
                // field��ǉ�����
                ctClassStatement.addField(ctSQLField);
                
                try
                {
                    ctClassStatement.getDeclaredMethod("getJdbcJavelinBindIndex");
                }
                catch (NotFoundException nfe)
                {
                    // Mehtod�����
                    CtMethod sqlIndexField =
                                             CtMethod.make(BINDVAL_INDEX_GETTER_METHOD_DEF,
                                                           ctClassStatement);
                    ctClassStatement.addMethod(sqlIndexField);
                }
                

                try
                {
                    ctClassStatement.getDeclaredMethod("getJdbcJavelinBindVal");
                }
                catch (NotFoundException nfe)
                {
                    // Mehtod�����
                    ctGetSQLField = CtMethod.make(BINDVAL_GETTER_METHOD_DEF, ctClassStatement);
                    // Method��ǉ�����
                    ctClassStatement.addMethod(ctGetSQLField);

                    String tagKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
                    String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tagKey);
                    String messageKey = "javelin.jdbc.instrument.JdbcJavelinTransformer."
                                      + "AddJdbcJavelinBindValMessage";
                    String message = JdbcJavelinMessages.getMessage(messageKey, className);
                    SystemLogger.getInstance().info(jdbcJavelinTag + message);
                }
            }

            try
            {
                ctClassStatement.getDeclaredField(STMTFORPLAN_FIELD_NAME);
            }
            catch (NotFoundException nofExp)
            {
                // field�����
                ctSQLField = CtField.make(STMTFORPLAN_FIELD_DEF, ctClassStatement);
                // field��ǉ�����
                ctClassStatement.addField(ctSQLField);

                // field�����
                ctSQLField = CtField.make(FLAGFORPLANSTMT_FIELD_DEF, ctClassStatement);
                // field��ǉ�����
                ctClassStatement.addField(ctSQLField);
                
                try
                {
                    ctClassStatement.getDeclaredMethod("getStmtForPlan");
                }
                catch (NotFoundException ex)
                {
                    // �擾�p�̃��\�b�h���쐬����B
                    CtMethod getterMethod = CtMethod.make(STMTFORPLAN_GETTER_METHOD_DEF, 
                                                          ctClassStatement);
                    ctClassStatement.addMethod(getterMethod);

                    CtMethod setterMethod = CtMethod.make(STMTFORPLAN_SETTER_METHOD_DEF, 
                                                          ctClassStatement);
                    ctClassStatement.addMethod(setterMethod);
                }
                
                String tagKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
                String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tagKey);
                String messageKey =
                        "javelin.jdbc.instrument.JdbcJavelinTransformer.AddSomeFieldMessage";
                String message =
                        JdbcJavelinMessages.getMessage(messageKey, className,
                                                       STMTFORPLAN_FIELD_NAME + ","
                                                               + FLAGFORPLANSTMT_FIELD_NAME);
                SystemLogger.getInstance().info(jdbcJavelinTag + message);
            }

            // ����PreparedStatement�C���^�t�F�[�X�����N���X��setXXX���\�b�h����������Ă��Ȃ���΁A
            // setXXX���\�b�h��ǉ����A�I�[�o�[���C�h����B
            String[] setMethodA =
                    new String[]{"setString(int parameterIndex, java.lang.String x)",
                            "setObject(int parameterIndex, java.lang.Object x)",
                            "setObject(int parameterIndex, java.lang.Object x, int targetSqlType)",
                            "setObject(int parameterIndex, java.lang.Object x, int targetSqlType, "
                           + "int scale)"};
            String[] setMethodB =
                    new String[]{"setBigDecimal(int parameterIndex, java.math.BigDecimal x)",
                            "setDate(int parameterIndex, java.sql.Date x)",
                            "setDate(int parameterIndex, java.sql.Date x, java.util.Calendar cal)",
                            "setTime(int parameterIndex, java.sql.Time x)",
                            "setTime(int parameterIndex, java.sql.Time x, java.util.Calendar cal)",
                            "setTimestamp(int parameterIndex, java.sql.Timestamp x)",
                            "setTimestamp(int parameterIndex, java.sql.Timestamp x, "
                           + "java.util.Calendar cal)"};
            String[] setMethodC =
                    new String[]{"setBoolean(int parameterIndex, boolean x)",
                            "setShort(int parameterIndex, short x)",
                            "setInt(int parameterIndex, int x)",
                            "setLong(int parameterIndex, long x)",
                            "setFloat(int parameterIndex, float x)",
                            "setDouble(int parameterIndex, double x)"};
            String[] setMethodD =
                    new String[]{"setByte(int parameterIndex, byte x)",
                            "setBytes(int parameterIndex, byte[] x)"};
            String[] setMethodE =
                    new String[]{"setArray(int parameterIndex, java.sql.Array x)",
                            "setBlob(int parameterIndex, java.sql.Blob x)",
                            "setClob(int parameterIndex, java.sql.Clob x)",
                            "setRef(int parameterIndex, java.sql.Ref x)",
                            "setURL(int parameterIndex, java.net.URL x)"};
            String[] setMethodF =
                    new String[]{"setNull(int parameterIndex, int sqlType)",
                            "setNull(int parameterIndex, int sqlType, java.lang.String typeName)"};
            String[] setMethodG =
                    new String[]{
                            "setAsciiStream(int parameterIndex, java.io.InputStream x, int length)",
                            "setBinaryStream("
                            + "int parameterIndex, java.io.InputStream x, int length)", 
                            "setUnicodeStream("
                            + "int parameterIndex, java.io.InputStream x, int length)", };
            String[] setMethodH =
                    new String[]{"setCharacterStream("
                                + "int parameterIndex, java.io.Reader reader, int length)", };

            // set���\�b�h��super���Ăяo���R�[�h�𖄂ߍ���
            addSetMethodLoop(pool, className, setMethodA);
            addSetMethodLoop(pool, className, setMethodB);
            addSetMethodLoop(pool, className, setMethodC);
            addSetMethodLoop(pool, className, setMethodD);
            addSetMethodLoop(pool, className, setMethodE);
            addSetMethodLoop(pool, className, setMethodF);
            addSetMethodLoop(pool, className, setMethodG);
            addSetMethodLoop(pool, className, setMethodH);

            // ����PreparedStatement�C���^�t�F�[�X�����N���X��
            // addBatch()�Aclose()����������Ă��Ȃ���΁A
            // ���ꂼ�ꃁ�\�b�h��ǉ����A�I�[�o�[���C�h����B
            addAddBatchMethodOfPreparedStatement(pool, className);
            addNonParamMethod(pool, className, "clearBatch",
                              JdbcJavelinConverter.BCI_METHOD_CLEAR_BATCH
                                      + JdbcJavelinConverter.BCI_METHOD_PLANFORPREPARED_CLEARBATCH);
            addCloseMethod(pool, className);
        }
        
        addJvnStatementInterface(ctClassStatement);
        
        return ctClassStatement;
    }

    /**
     * JdbcJavelinStatement�̃C���^�t�F�[�X��ǉ�����B
     * 
     * @param ctClassStatement �ǉ��Ώ�
     */
    private void addJvnStatementInterface(CtClass ctClassStatement)
    {
        try
        {
            ClassPool classPool = ctClassStatement.getClassPool();
            CtClass jvnStatementClass =
                                     classPool.get(JdbcJavelinStatement.class.getCanonicalName());
            boolean hasInterface =
                                   JdbcJavelinConverter.hasInterface(ctClassStatement,
                                                                     jvnStatementClass);
            
            if (hasInterface == false)
            {
                ctClassStatement.addInterface(jvnStatementClass);
            }
        }
        catch (NotFoundException nfe)
        {
            SystemLogger.getInstance().warn(nfe);
        }
    }

    /**
     * Statement�Ƀo�C���h�ϐ��擾���\�b�h��ǉ�����B
     *
     * @param pool �N���X�v�[��
     * @return �ύX����Statement�C���^�t�F�[�X
     * @throws NotFoundException java.sql.Statement��������Ȃ��ꍇ
     * @throws CannotCompileException ���\�b�h��ǉ��ł��Ȃ��ꍇ
     */
    private CtClass createModifiedStatement(final ClassPool pool)
        throws NotFoundException,
            CannotCompileException
    {

        CtClass ctClassStatement = pool.get("java.sql.Statement");
        CtMethod ctGetSQLField;

        try
        {
            ctClassStatement.getDeclaredMethod("getJdbcJavelinSql");
        }
        catch (NotFoundException nofExp)
        {
            // Mehtod�����
            ctGetSQLField = CtMethod.make(SQL_GETTER_INTERFACE_DEF, ctClassStatement);
            // Method��ǉ�����
            ctClassStatement.addMethod(ctGetSQLField);

            String tagKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
            String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tagKey);
            String messageKey =
              "javelin.jdbc.instrument.JdbcJavelinTransformer.AddJdbcJavelinSqlToInterfaceMessage";
            String message = JdbcJavelinMessages.getMessage(messageKey);
            SystemLogger.getInstance().info(jdbcJavelinTag + message);
        }
        return ctClassStatement;
    }

    /**
     * PreparedStatement�Ƀo�C���h�ϐ��擾���\�b�h��ǉ�����B
     *
     * @param pool �N���X�v�[��
     * @return �ύX����PreparedStatement�C���^�t�F�[�X
     * @throws NotFoundException java.sql.PreparedStatement��������Ȃ��ꍇ
     * @throws CannotCompileException ���\�b�h��ǉ��ł��Ȃ��ꍇ
     */
    private CtClass createModifiedPreparedStatement(final ClassPool pool)
        throws NotFoundException,
            CannotCompileException
    {

        CtClass ctClassStatement = pool.get("java.sql.PreparedStatement");
        CtMethod ctGetSQLField;

        try
        {
            ctClassStatement.getDeclaredMethod("getJdbcJavelinBindVal");
        }
        catch (NotFoundException nofExp)
        {
            // Mehtod�����
            ctGetSQLField = CtMethod.make(BINDVAL_GETTER_INTERFACE_DEF, ctClassStatement);
            // Method��ǉ�����
            ctClassStatement.addMethod(ctGetSQLField);

            String tagKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
            String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tagKey);
            String messageKey =
                    "javelin.jdbc.instrument.JdbcJavelinTransformer."
                  + "AddJdbcJavelinBindValToInterfaceMessage";
            String message = JdbcJavelinMessages.getMessage(messageKey);
            SystemLogger.getInstance().info(jdbcJavelinTag + message);
        }
        return ctClassStatement;
    }

    /**
     * ����^�C�v��set���\�b�h�𖄂ߍ��ށB
     * @param pool �N���X�v�[��
     * @param className �N���X��
     * @param setMethods set���\�b�h�ꗗ
     * @throws NotFoundException ���\�b�h��������Ȃ��Ƃ�
     * @throws CannotCompileException �R���p�C���ł��Ȃ��Ƃ�
     */
    private void addSetMethodLoop(final ClassPool pool, final String className,
            final String[] setMethods)
        throws NotFoundException,
            CannotCompileException
    {
        for (String method : setMethods)
        {
            addSetMethod(pool, className, method);
        }
    }

    /**
     * �w�肳�ꂽset���\�b�h�𖄂ߍ��ށB
     * @param pool �N���X�v�[��
     * @param className �N���X��
     * @param method set���\�b�h
     * @throws NotFoundException ���\�b�h��������Ȃ��Ƃ�
     * @throws CannotCompileException �R���p�C���ł��Ȃ��Ƃ�     */
    private void addSetMethod(final ClassPool pool, final String className, final String method)
        throws NotFoundException,
            CannotCompileException
    {
        CtClass ctClassStatement = pool.get(className);

        int bracketIndex = method.indexOf('(');

        // ���\�b�h�̈���
        String paramsAll = method.substring(bracketIndex + 1, method.length() - 1);

        // ���\�b�h�̈����̌^����菜��
        Matcher matcher = PARAM_TYPE_PATTERN.matcher(method);
        String params = matcher.replaceAll("");

        // ���\�b�h�̖��O�����o��
        String methodName = method.substring(0, bracketIndex);

        // ���\�b�h�̌^�����o��
        String[] param = paramsAll.split(",[ \\t\\n]*");
        CtClass[] paramClass = new CtClass[param.length];
        for (int index = 0; index < param.length; index++)
        {
            String paramType = param[index].split("[ \\t\\n]+")[0];
            paramClass[index] = pool.get(paramType);
        }

        try
        {
            ctClassStatement.getDeclaredMethod(methodName, paramClass);
        }
        catch (NotFoundException nfe)
        {
            // Mehtod�����
            StringBuffer methodDef = new StringBuffer();
            methodDef.append("public void ");
            methodDef.append(method);
            methodDef.append(" throws java.sql.SQLException{\n");
            methodDef.append("    super.");
            methodDef.append(params);
            methodDef.append(";};\n");
            CtMethod ctSetMethod = CtMethod.make(methodDef.toString(), ctClassStatement);
            // Method��ǉ�����
            ctClassStatement.addMethod(ctSetMethod);

            String tagKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
            String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tagKey);
            String messageKey =
                    "javelin.jdbc.instrument.JdbcJavelinTransformer.AddSomeMethodMessage";
            String message = JdbcJavelinMessages.getMessage(messageKey, className, methodName);
            SystemLogger.getInstance().info(jdbcJavelinTag + message);
        }
    }

    /**
     * �������Ȃ����\�b�h�𖄂ߍ��ށB
     * @param pool �N���X�v�[��
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param code ���ߍ��ރR�[�h
     * @throws NotFoundException ���\�b�h��������Ȃ��Ƃ�
     * @throws CannotCompileException �R���p�C���ł��Ȃ��Ƃ�
     */
    private void addNonParamMethod(final ClassPool pool, final String className,
            final String methodName, final String code)
        throws CannotCompileException,
            NotFoundException
    {
        CtClass ctClassStatement = pool.get(className);

        try
        {
            ctClassStatement.getDeclaredMethod(methodName);
        }
        catch (NotFoundException nfe)
        {
            // Mehtod�����
            StringBuffer methodDef = new StringBuffer();
            methodDef.append("public void ");
            methodDef.append(methodName);
            methodDef.append("() throws java.sql.SQLException {\n" + "    super." + methodName
                    + "();\n");
            methodDef.append(code);
            methodDef.append("}\n");
            CtMethod ctSetMethod = CtMethod.make(methodDef.toString(), ctClassStatement);
            // Method��ǉ�����
            ctClassStatement.addMethod(ctSetMethod);

            String tagKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
            String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tagKey);
            String messageKey =
                    "javelin.jdbc.instrument.JdbcJavelinTransformer.AddSomeMethodMessage";
            String message = JdbcJavelinMessages.getMessage(messageKey, className, methodName);
            SystemLogger.getInstance().info(jdbcJavelinTag + message);
        }
    }

    /**
     * Statement����������N���X��addBatch���\�b�h�𖄂ߍ��ށB
     * @param pool �N���X�v�[��
     * @param className �N���X��
     * @throws NotFoundException ���\�b�h��������Ȃ��Ƃ�
     * @throws CannotCompileException �R���p�C���ł��Ȃ��Ƃ�
     */
    private void addAddBatchMethodOfStatement(final ClassPool pool, final String className)
        throws NotFoundException,
            CannotCompileException
    {
        CtClass ctClassStatement = pool.get(className);

        // ����addbatch���\�b�h���Ȃ���Βǉ�����
        try
        {
            ctClassStatement.getDeclaredMethod("addBatch");
        }
        catch (NotFoundException nfe)
        {
            // Mehtod�����
            StringBuilder methodDef = new StringBuilder();
            methodDef.append("public void addBatch(java.lang.String sql)"
                    + " throws java.sql.SQLException {\n" + "    super.addBatch(sql);\n}\n");
            CtMethod ctSetMethod = CtMethod.make(methodDef.toString(), ctClassStatement);
            // Method��ǉ�����
            ctClassStatement.addMethod(ctSetMethod);

            String tagKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
            String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tagKey);
            String messageKey =
                    "javelin.jdbc.instrument.JdbcJavelinTransformer.AddSomeMethodMessage";
            String message = JdbcJavelinMessages.getMessage(messageKey, className, "addBatch");
            SystemLogger.getInstance().info(jdbcJavelinTag + message);
        }
    }

    /**
     * PreparedStatement����������N���X��addBatch���\�b�h�𖄂ߍ��ށB
     * @param pool �N���X�v�[��
     * @param className �N���X��
     * @throws NotFoundException ���\�b�h��������Ȃ��Ƃ�
     * @throws CannotCompileException �R���p�C���ł��Ȃ��Ƃ�
     */
    private void addAddBatchMethodOfPreparedStatement(final ClassPool pool, final String className)
        throws NotFoundException,
            CannotCompileException
    {
        addNonParamMethod(pool, className, "addBatch", "");
        //                          JdbcJavelinConverter.BCI_METHOD_ADD_BATCH
        //                          + JdbcJavelinConverter.BCI_METHOD_PLANFORPREPARED_ADDBATCH);
        //        CtClass ctClass = pool.get(className);
        //        CtMethod method = ctClass.getDeclaredMethod("addBatch");
        //        JdbcJavelinConverter.addSqlToFieldStat(ctClass, method);
    }

    /**
     * Statement����������N���X��execute���\�b�h�𖄂ߍ��ށB
     * @param pool �N���X�v�[��
     * @param className �N���X��
     * @throws NotFoundException execute����������Ă��Ȃ��Ƃ��B
     * @throws CannotCompileException �R���p�C���ł��Ȃ��Ƃ�
     */
    private void addExecuteMethodOfStatement(final ClassPool pool, final String className)
        throws NotFoundException,
            CannotCompileException
    {
        CtClass ctClassStatement = pool.get(className);

        // ����execute���\�b�h���Ȃ���Βǉ�����
        try
        {
            ctClassStatement.getDeclaredMethod("execute");
        }
        catch (NotFoundException nfe)
        {
            // Mehtod�����
            StringBuffer methodDef = new StringBuffer();
            methodDef.append("public boolean execute(java.lang.String sql)"
                    + " throws java.sql.SQLException {\n" + "    return super.execute(sql);\n");
            methodDef.append("}\n");
            CtMethod ctSetMethod = CtMethod.make(methodDef.toString(), ctClassStatement);
            // Method��ǉ�����
            ctClassStatement.addMethod(ctSetMethod);

            String tagKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
            String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tagKey);
            String messageKey =
                    "javelin.jdbc.instrument.JdbcJavelinTransformer.AddSomeMethodMessage";
            String message = JdbcJavelinMessages.getMessage(messageKey, className, "execute");
            SystemLogger.getInstance().info(jdbcJavelinTag + message);
        }
    }

    /**
     * Statement����������N���X��executeQuery���\�b�h�𖄂ߍ��ށB
     * @param pool �N���X�v�[��
     * @param className �N���X��
     * @throws NotFoundException executeQuery����������Ă��Ȃ��Ƃ��B
     * @throws CannotCompileException �R���p�C���ł��Ȃ��Ƃ�
     */
    private void addExecuteQueryMethodOfStatement(final ClassPool pool, final String className)
        throws NotFoundException,
            CannotCompileException
    {
        CtClass ctClassStatement = pool.get(className);

        // ����executeQuery���\�b�h���Ȃ���Βǉ�����
        try
        {
            ctClassStatement.getDeclaredMethod("executeQuery");
        }
        catch (NotFoundException nfe)
        {
            // Mehtod�����
            StringBuffer methodDef = new StringBuffer();
            methodDef.append("public java.sql.ResultSet executeQuery(java.lang.String sql)"
                    + " throws java.sql.SQLException {\n" 
                    + "    return super.executeQuery(sql);\n");
            methodDef.append("}\n");

            CtMethod ctSetMethod = CtMethod.make(methodDef.toString(), ctClassStatement);
            // Method��ǉ�����

            ctClassStatement.addMethod(ctSetMethod);

            String tagKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
            String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tagKey);
            String messageKey =
                    "javelin.jdbc.instrument.JdbcJavelinTransformer.AddSomeMethodMessage";
            String message = JdbcJavelinMessages.getMessage(messageKey, className, "executeQuery");
            SystemLogger.getInstance().info(jdbcJavelinTag + message);
        }
    }

    /**
     * Statement����������N���X��executeUpdate���\�b�h�𖄂ߍ��ށB
     * @param pool �N���X�v�[��
     * @param className �N���X��
     * @throws NotFoundException executeUpdate����������Ă��Ȃ��Ƃ��B
     * @throws CannotCompileException �R���p�C���ł��Ȃ��Ƃ�
     */
    private void addExecuteUpdateMethodOfStatement(final ClassPool pool, final String className)
        throws NotFoundException,
            CannotCompileException
    {
        CtClass ctClassStatement = pool.get(className);

        // ����executeUpdate���\�b�h���Ȃ���Βǉ�����
        try
        {
            ctClassStatement.getDeclaredMethod("executeUpdate");
        }
        catch (NotFoundException nfe)
        {
            // Mehtod�����
            StringBuffer methodDef = new StringBuffer();
            methodDef.append("public int executeUpdate(java.lang.String sql)"
                    + " throws java.sql.SQLException {\n"
                    + "    return super.executeUpdate(sql);\n");
            methodDef.append("}\n");
            CtMethod ctSetMethod = CtMethod.make(methodDef.toString(), ctClassStatement);
            // Method��ǉ�����
            ctClassStatement.addMethod(ctSetMethod);

            String tagKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
            String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tagKey);
            String messageKey =
                    "javelin.jdbc.instrument.JdbcJavelinTransformer.AddSomeMethodMessage";
            String message = JdbcJavelinMessages.getMessage(messageKey, className, "executeUpdate");
            SystemLogger.getInstance().info(jdbcJavelinTag + message);
        }
    }

    /**
     * Statement����������N���X��executeBatch���\�b�h�𖄂ߍ��ށB
     * @param pool �N���X�v�[��
     * @param className �N���X��
     * @throws NotFoundException executeBatch����������Ă��Ȃ��Ƃ��B
     * @throws CannotCompileException �R���p�C���ł��Ȃ��Ƃ�
     */
    private void addExecuteBatchMethodOfStatement(final ClassPool pool, final String className)
        throws NotFoundException,
            CannotCompileException
    {
        CtClass ctClassStatement = pool.get(className);

        // ����executeBatch���\�b�h���Ȃ���Βǉ�����
        try
        {
            ctClassStatement.getDeclaredMethod("executeBatch");
        }
        catch (NotFoundException nfe)
        {
            // Mehtod�����
            StringBuffer methodDef = new StringBuffer();
            methodDef.append("public int[] executeBatch()" + " throws java.sql.SQLException {\n"
                    + "    return super.executeBatch();\n");
            methodDef.append("}\n");
            CtMethod ctSetMethod = CtMethod.make(methodDef.toString(), ctClassStatement);
            // Method��ǉ�����
            ctClassStatement.addMethod(ctSetMethod);

            String tagKey = "javelin.jdbc.instrument.JdbcJavelinConverter.JDBCJavelinTag";
            String jdbcJavelinTag = JdbcJavelinMessages.getMessage(tagKey);
            String messageKey =
                    "javelin.jdbc.instrument.JdbcJavelinTransformer.AddSomeMethodMessage";
            String message = JdbcJavelinMessages.getMessage(messageKey, className, "executeBatch");
            SystemLogger.getInstance().info(jdbcJavelinTag + message);
        }
    }

    /**
     * clearBatch���\�b�h�𖄂ߍ��ށB
     * @param pool �N���X�v�[��
     * @param className �N���X��
     * @param code �R�[�h
     * @throws NotFoundException clearBatch����������Ă��Ȃ��Ƃ��B
     * @throws CannotCompileException �R���p�C���ł��Ȃ��Ƃ�
     */
    private void addClearBatchMethod(final ClassPool pool, final String className, final String code)
        throws NotFoundException,
            CannotCompileException
    {
        addNonParamMethod(pool, className, "clearBatch", code);
        CtClass ctClass = pool.get(className);
        CtMethod method = ctClass.getDeclaredMethod("clearBatch");
        JdbcJavelinConverter.delSqlFromField(ctClass, method);
    }

    /**
     * close���\�b�h�𖄂ߍ��ށB
     * @param pool �N���X�v�[��
     * @param className �N���X��
     * @throws NotFoundException close����������Ă��Ȃ��Ƃ��B
     * @throws CannotCompileException �R���p�C���ł��Ȃ��Ƃ�
     */
    private void addCloseMethod(final ClassPool pool, final String className)
        throws NotFoundException,
            CannotCompileException
    {
        addNonParamMethod(pool, className, "close",
                          JdbcJavelinConverter.BCI_METHOD_PLANFORPREPARED_CLOSE);
    }
}
