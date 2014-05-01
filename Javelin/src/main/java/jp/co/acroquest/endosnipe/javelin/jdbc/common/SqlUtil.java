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
package jp.co.acroquest.endosnipe.javelin.jdbc.common;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.JdbcJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.jdbc.stats.JdbcJavelinStatement;

/**
 * SQL���𑀍삷�郆�[�e�B���e�B�B
 * 
 * @author eriguchi
 */
public class SqlUtil
{
    private static final int DML_KEY_STR_LENGTH = 6;

    /**
     * �C���X�^���X��������邽�߂̃R���X�g���N�^�B
     */
    private SqlUtil()
    {
        // Do Nothing
        return;
    }

    /**
     * sql��啶���ɕύX���A�擪�ɂ���R�����g����������B
     * @param sql �R�����g�������s��SQL
     * @return �������s����SQL
     */
    public static String removeHeadComment(final String sql)
    {
        String rawSql = sql;
        while (true)
        {
            rawSql = rawSql.trim();
            // �R�����g(--)�ł��邩�𒲂ׁA���s�܂ł���菜��
            if (rawSql.startsWith("--"))
            {
                rawSql = rawSql.substring(rawSql.indexOf('\n') + 1);
                continue;
            }

            // �R�����g(/* �` */)�ł��邩�𒲂ׁA/* �` */ �܂ł���菜���B
            if (rawSql.startsWith("/*"))
            {
                int nextEnd = rawSql.indexOf("*/", JdbcJavelinRecorder.COMMENT_HEADER_LENGTH);
                // ����"*/"���o������ꏊ��T���A�R�����g���폜���A�������J��Ԃ��B
                // ������Ȃ�������Anull��Ԃ��B

                if (nextEnd != JdbcJavelinRecorder.NOT_FOUND)
                {
                    rawSql = rawSql.substring(nextEnd + JdbcJavelinRecorder.COMMENT_FOOTER_LENGTH);
                    continue;
                }
                return null;
            }
            break;
        }
        return rawSql;
    }

    /**
     * SQL��DML�ł��邩�`�F�b�N����B
     * @param sql SQL��
     * @return SQL��DML�ł����true���A����ȊO��false��Ԃ��B
     */
    public static boolean checkDml(final String sql)
    {
        String rawSql = null;
        // SQL���̑O�̃R�����g��S�Ď�菜���܂Ń��[�v�B
        // �R�����g���s���ȏꍇ�Ɍ���false���Ԃ����B
        rawSql = SqlUtil.removeHeadComment(sql);
        if (rawSql == null)
        {
            return false;
        }

        // DML�Ȃ�true��Ԃ�
        if(rawSql.length() < DML_KEY_STR_LENGTH)
        {
            return false;
        }
        
        String keyStr = rawSql.substring(0, DML_KEY_STR_LENGTH);
        if (keyStr.equalsIgnoreCase("SELECT") || keyStr.equalsIgnoreCase("UPDATE")
                || keyStr.equalsIgnoreCase("INSERT") || keyStr.equalsIgnoreCase("DELETE"))
        {
            return true;
        }
        return false;
    }

    /**
     * �Z�~�R�����ŋ�؂�ꂽSQL�����Z�~�R�����ŕ�������B
     *
     * @param sql ����SQL��
     * @return �������ꂽSQL��
     */
    public static List<String> splitSqlStatement(String sql)
    {
        List<String> sqllist = new ArrayList<String>();
        // �V���O���N�H�[�e�[�V�����̒��ɂ����true
        boolean singlequoteFlag = false;
        // �_�u���N�H�[�e�[�V�����̒��ɂ���ꍇ��true
        boolean doublequoteFlag = false;

        for (int index = 0; index < sql.length(); index++)
        {
            char checkchar = sql.charAt(index);

            if (checkchar == '\'')
            {
                // �V���O���N�H�[�e�[�V�����������Ƃ��́A
                // �_�u���N�H�[�e�[�V�����̒��łȂ����
                // �V���O���N�H�[�e�[�V�����t���O��؂�ւ���B
                if (doublequoteFlag == false)
                {
                    singlequoteFlag = !singlequoteFlag;
                }
            }
            else if (checkchar == '"')
            {
                // �_�u���N�H�[�e�[�V�����������Ƃ��́A
                // �V���O���N�H�[�e�[�V�����̒��łȂ����
                // �_�u���N�H�[�e�[�V�����t���O��؂�ւ���B
                if (singlequoteFlag == false)
                {
                    doublequoteFlag = !doublequoteFlag;
                }
            }
            else if (checkchar == ';')
            {
                // �Z�~�R�����������Ƃ��́A
                // �V���O���N�H�[�e�[�V�����̒��ł��_�u���N�H�[�e�[�V�����̒��ł��Ȃ��ꍇ��
                // ��������B
                if (singlequoteFlag == false && doublequoteFlag == false)
                {
                    String sqlOne = sql.substring(0, index);
                    sqllist.add(sqlOne);
                    sql = sql.substring(index + 1);
                    index = -1;
                }
            }
        }

        // �c���ǉ�����
        sqllist.add(sql);

        // �󔒂̗v�f���폜����
        Iterator<String> iterator = sqllist.iterator();
        while (iterator.hasNext())
        {
            String sqlOne = iterator.next();
            sqlOne = sqlOne.trim();
            if (sqlOne.length() == 0)
            {
                iterator.remove();
            }
        }

        return sqllist;
    }

    /**
     * �o�C���h�ϐ���CSV�𐶐�����B
     * 
     * @param bindList �o�C���h�ϐ��̃��X�g�i<code>null</code> ���j
     * @param count �o�C���h�ϐ��̃��X�g���̈ʒu
     * @return �Ή�����SQL�̃o�C���h�ϐ���CSV�B
     *          bindList �� <code>null</code> �Ȃ� <code>null</code> ��Ԃ��B
     */
    public static String getBindValCsv(final List<?> bindList, final int count)
    {
        //�o�C���h�ϐ��擾
        String bindVals = null;
        if (bindList != null)
        {
            try
            {
                // �o�C���h�ϐ���List����A�Ή�����SQL��TreeMap���擾����B
                TreeMap<?, ?> bindMap = (TreeMap<?, ?>)bindList.get(count);
                Object lastKey = bindMap.lastKey();
                if (lastKey != null)
                {
                    // �o�C���h�ϐ��̃J���}��؂蕶����𐶐�����
                    int maxIdx = ((Integer)lastKey).intValue();
                    StringBuffer csv = new StringBuffer();
                    for (int bindIdx = 1; bindIdx <= maxIdx; bindIdx++)
                    {
                        if (csv.length() != 0)
                        {
                            csv.append(",");
                        }
                        Object val = bindMap.get(Integer.valueOf(bindIdx));
                        if (val != null)
                        {
                            csv.append(val.toString());
                        }
                        else
                        {
                            csv.append("(ERROR)");
                        }
                    }
                    bindVals = csv.toString();
                }
            }
            catch (IndexOutOfBoundsException ex)
            {
                // �o�C���h�ϐ��͓o�^����Ă��Ȃ�
                SystemLogger.getInstance().warn(ex);
            }
        }

        return bindVals;
    }

    /**
     * ���t���N�V�����ɂ�PreparedStatement��getJdbcJavelinBindVal���\�b�h���Ăяo���B
     * @param stmt Statement�I�u�W�F�N�g
     * @return �o�C���h�ϐ��̃��X�g
     */
    public static List<?> getJdbcJavelinBindValByRef(final Statement stmt)
    {
        // JdbcJavelinStatement�ł͂Ȃ��ꍇ�Anull��Ԃ�
        if (stmt instanceof JdbcJavelinStatement == false)
        {
            return null;
        }

        JdbcJavelinStatement jStatement = (JdbcJavelinStatement)stmt;

        List<?> ret = jStatement.getJdbcJavelinBindVal();
        return (List<?>)ret;
    }

    /**
     * <code>PreparedStatement</code> ��
     * <code>addBatch</code> ���\�b�h���Ăяo���ꂽ�񐔂�Ԃ��B
     *
     * @param stmt PreparedStatement
     * @return <code>addBatch</code> ���\�b�h���Ăяo���ꂽ��
     * @throws Exception ��O
     */
    public static int getPreparedStatementAddBatchCount(final Statement stmt)
        throws Exception
    {
        JdbcJavelinStatement jStatement = (JdbcJavelinStatement)stmt;
        return jStatement.getJdbcJavelinBindIndex();
    }
}
