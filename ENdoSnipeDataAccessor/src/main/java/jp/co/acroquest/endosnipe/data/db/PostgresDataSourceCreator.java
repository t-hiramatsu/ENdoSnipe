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
package jp.co.acroquest.endosnipe.data.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.util.SQLUtil;
import jp.co.acroquest.endosnipe.data.DBInitializer;
import jp.co.acroquest.endosnipe.data.LogMessageCodes;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;

/**
 * PostgreSQL�p�̃f�[�^�\�[�X�쐬�N���X�ł��B<br />
 * 
 * @author fujii
 *
 */
public class PostgresDataSourceCreator extends AbstractDataSourceCreator implements LogMessageCodes
{
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(
                                                      PostgresDataSourceCreator.class);

    /** PostgreSQL�f�[�^�x�[�X�̃h���C�o�N���X���� */
    private static final String           POSTGRES_DRIVER = "org.postgresql.Driver";

    /** PostgreSQL�ڑ��pURI�̃v���t�B�N�X */
    private static final String           POSTGRES_URI_PREFIX = "jdbc:postgresql://";

    /**
     * {@inheritDoc}
     */
    public DataSource createPoolingDataSource(String dbname, boolean connectOnlyExists)
        throws SQLException
    {
        try
        {
            String driverClass = DBManager.getDriverClass();
            Class.forName(driverClass);
            String uri = createDatabaseURI(DBManager.getHostName(), DBManager.getPort(), dbname);
            String userName = DBManager.getUserName();
            String password = DBManager.getPassword();
            ConnectionFactory connectionFactory =
                new DriverManagerConnectionFactory(uri, userName, password);

            // �w�肳�ꂽ�f�[�^�x�[�X�������݂����AconnectOnlyExists��false�̏ꍇ�A
            // �w�肳�ꂽ���O�̃f�[�^�x�[�X���쐬����
            if (!connectOnlyExists)
            {
                if(!(this.existsDatabase(dbname)))
                {
                    // �f�[�^�x�[�X�쐬�Ɏ��s���Ă��������Ȃ�
                    DBInitializer.createDatabase(dbname);
                }
            }

            // �f�[�^�x�[�X���ɑΉ����� StackObjectPool ���擾����B
            // �������݂��Ȃ���΁A��������B
            ConnectionManager manager = ConnectionManager.getInstance();
            ObjectPool connectionPool = manager.getConnectionPool(uri);
            if (connectionPool == null)
            {
                connectionPool = manager.createNewConnectionPool(uri);
            }

            new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false,
                                          true);
            return new PoolingDataSource(connectionPool);
        }
        catch (Throwable ex)
        {
            LOGGER.log(EXCEPTION_OCCURED, ex, ex.getMessage());
            throw new SQLException(ex.getMessage());
        }
    }

    /**
     * �f�[�^�x�[�X�ڑ��p URI �𐶐����܂��B<br />
     *
     * @param host �z�X�g��
     * @param port �|�[�g�ԍ�
     * @param dbName �f�[�^�x�[�X�̖���
     * 
     * @return URL �ڑ�������
     */
    protected String createDatabaseURI(final String host, final String port, final String dbName)
    {
        String dbNameLocal = dbName;

        if (dbNameLocal == null)
        {
            dbNameLocal = "";
        }
        String connectStrBase = "jdbc:postgresql://" + host + ":" + port + "/" + dbNameLocal;
        return connectStrBase;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTarget()
    {
        return !DBManager.isDefaultDb();
    }

    /**
     * {@inheritDoc}
     */
    public String getSequenceSql(String sequenceName)
    {
        return "SELECT last_value FROM " + sequenceName;
    }

    /**
     * {@inheritDoc}
     */
    public boolean existsDatabase(String dbName)
    {
        try
        {
            Class.forName(POSTGRES_DRIVER);
        }
        catch(ClassNotFoundException e)
        {
            return false;
        }

        String uri = POSTGRES_URI_PREFIX + DBManager.getHostName() + ":" 
                                         + DBManager.getPort() + "/";
        Connection conn = null;
        Statement state = null;
        ResultSet res = null;

        // ���ڃA�N�Z�X����DB�ꗗ���擾
        try
        {
            conn = DriverManager.getConnection( uri,
                                                DBManager.getUserName(),
                                                DBManager.getPassword() );

            state = conn.createStatement();
            res = state.executeQuery(
                         "SELECT datname FROM pg_database WHERE datistemplate = false;");

            // �₢���킹���ʊm�F
            while (res.next())
            {
                String datname = res.getString(1);
                // DB���͑啶���E����������ʂ����ɔ��肷��
                if (datname.equalsIgnoreCase(dbName))
                {
                    return true;
                }
            }
        }
        catch(SQLException e)
        {
            return false;
        }
        finally
        {
            SQLUtil.closeResultSet(res);
            SQLUtil.closeStatement(state);
            SQLUtil.closeConnection(conn);
        }

        return false;
    }
}
