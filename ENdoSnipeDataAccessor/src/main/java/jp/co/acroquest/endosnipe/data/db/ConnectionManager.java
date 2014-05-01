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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.sql.DataSource;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.util.SQLUtil;
import jp.co.acroquest.endosnipe.data.DBInitializer;
import jp.co.acroquest.endosnipe.data.LogMessageCodes;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.StackObjectPool;
import org.seasar.framework.util.StringUtil;

/**
 * �f�[�^�x�[�X�R�l�N�V�������Ǘ����邽�߂̃N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class ConnectionManager implements LogMessageCodes
{
    /** ���K�[ */
    private static final ENdoSnipeLogger   LOGGER = ENdoSnipeLogger.getLogger(
                                                ConnectionManager.class);

    /** ConnectionManager�C���X�^���X�ێ��p�ϐ� */
    private static ConnectionManager       instance__;

    /** �Ǘ����ɓ����Ă���DataSource�̃��X�g */
    private final List<DataSourceEntry>    dataSourceList_;

    /** �f�[�^�x�[�X�����L�[�ɂ����R�l�N�V�����v�[���I�u�W�F�N�g�̃}�b�v */
    private final Map<String, ObjectPool>  connectionPoolMap_;

    /** �������ς݂̃f�[�^�\�[�X */
    private final Set<String>  initializedDatabaseSet_;
    
    /** �f�[�^�\�[�X�̃��X�g */
    private static List<DataSourceCreator> dataSouceCreatorList__;

    static
    {
        dataSouceCreatorList__ = new ArrayList<DataSourceCreator>();
        dataSouceCreatorList__.add(new H2DataSourceCreator());
        dataSouceCreatorList__.add(new PostgresDataSourceCreator());
    }

    /**
     * �C���X�^���X����h�~���邽�߂̃R���X�g���N�^
     */
    private ConnectionManager()
    {
        this.dataSourceList_ = new ArrayList<DataSourceEntry>();
        this.connectionPoolMap_ = new ConcurrentHashMap<String, ObjectPool>();
        this.initializedDatabaseSet_ = new CopyOnWriteArraySet<String>();
    }

    /**
     * {@link ConnectionManager} �̃C���X�^���X��Ԃ��܂��B<br />
     * 
     * @return �C���X�^���X
     */
    public static synchronized ConnectionManager getInstance()
    {
        if (instance__ == null)
        {
            instance__ = new ConnectionManager();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run()
                {
                    instance__.closeAll();
                }
            });
        }
        return instance__;
    }

    /**
     * �f�[�^�x�[�X�R�l�N�V�������擾���܂��B<br />
     * 
     * @param dbname �f�[�^�x�[�X��
     * @param connectOnlyExists �f�[�^�x�[�X�����݂���Ƃ��̂ݐڑ�����ꍇ�� <code>true</code> �A
     *                          ���݂��Ȃ��Ƃ��Ƀf�[�^�x�[�X�𐶐�����ꍇ�� <code>false</code>
     * @return {@link Connection} �I�u�W�F�N�g
     * @throws SQLException �R�l�N�V�������擾�ł��Ȃ������ꍇ
     */
    public synchronized Connection getConnection(final String dbname,
            final boolean connectOnlyExists)
        throws SQLException
    {
        return getConnection(dbname, connectOnlyExists, true);
    }
    
    /**
     * �f�[�^�x�[�X�R�l�N�V�������擾���܂��B<br />
     * 
     * @param dbname �f�[�^�x�[�X��
     * @param connectOnlyExists �f�[�^�x�[�X�����݂���Ƃ��̂ݐڑ�����ꍇ�� <code>true</code> �A
     *                          ���݂��Ȃ��Ƃ��Ƀf�[�^�x�[�X�𐶐�����ꍇ�� <code>false</code>
     * @param initialize        �f�[�^�x�[�X�����������邩�ǂ����B
     * @return {@link Connection} �I�u�W�F�N�g
     * @throws SQLException �R�l�N�V�������擾�ł��Ȃ������ꍇ
     */
    public synchronized Connection getConnection(final String dbname,
        final boolean connectOnlyExists, final boolean initialize)
        throws SQLException
    {
        if (dbname == null)
        {
            throw new IllegalArgumentException("dbname can't be null");
        }

        DataSource ds = getDataSource(dbname);
        if (ds != null)
        {
            Connection connection = getConnection(ds, dbname);
            if(this.initializedDatabaseSet_.contains(dbname) == false && initialize)
            {
                initialize(dbname, initialize, connection);
            }
            return connection;
        }

        ds = createPoolingDataSource(dbname, connectOnlyExists);

        Connection conn = getConnection(ds, dbname);

        // �R�l�N�V�����擾�ɐ�������΁A DataSource ��o�^����
        registDataSource(dbname, ds);
        
        initialize(dbname, initialize, conn);

        return conn;
    }

    private void initialize(final String dbname, final boolean initialize, Connection conn)
        throws SQLException
    {
        try
        {
            // �K�v�ɉ����ăf�[�^�x�[�X�̏��������s��
            boolean isInitialized = DBInitializer.isInitialized(conn);
            if (isInitialized)
            {
                this.initializedDatabaseSet_.add(dbname);
            }
            else if (initialize == true && isInitialized == false)
            {
                DBInitializer.initialize(conn);
                LOGGER.log(DB_INITIALIZED, dbname);
                this.initializedDatabaseSet_.add(dbname);
            }
            
            DBInitializer.reinitialize(conn);
        }
        catch (Exception ex)
        {
            LOGGER.log(EXCEPTION_OCCURED, ex, ex.getMessage());
            SQLException sqlex = new SQLException();
            sqlex.initCause(ex);
            throw sqlex;
        }
    }

    /**
     * �f�[�^�x�[�X�̃x�[�X�f�B���N�g����ݒ肵�܂��B<br />
     *
     * �f�[�^�x�[�X���ύX���ꂽ�ꍇ�́A�R�l�N�V�����v�[�����N���A���܂��B<br />
     *
     * @param baseDir �x�[�X�f�B���N�g���B <code>null</code> ���w�肵���ꍇ�� ~�i�z�[���f�B���N�g���j
     */
    public synchronized void setBaseDir(final String baseDir)
    {
        boolean changeBaseDir = !StringUtil.equals(baseDir, DBManager.getDbDir());

        if (baseDir != null)
        {
            DBManager.setDbDir(baseDir);
        }
        else
        {
            DBManager.setDbDir("~");
        }
        for (DataSourceCreator creator : dataSouceCreatorList__)
        {
            creator.setBaseDir(DBManager.getDbDir());
        }

        if (changeBaseDir)
        {
            closeAll();
        }
    }

    /**
     * �f�[�^�x�[�X�����݂��邩�ǂ����𒲂ׂ܂��B<br />
     *
     * @param dbname �f�[�^�x�[�X��
     * @return �f�[�^�x�[�X�����݂���ꍇ�� <code>true</code> �A���݂��Ȃ��ꍇ�� <code>false</code>
     */
    public boolean existsDatabase(final String dbname)
    {
        // �R�l�N�V���������邩�ǂ����ŁADB�̑��݂𔻒f����
        Connection con = null;
        boolean exist = false;
        try
        {
            con = getConnection(dbname, true);
            exist = true;
        }
        catch (SQLException ex)
        {
            exist = false;
        }
        finally
        {
            SQLUtil.closeConnection(con);
        }
        return exist;
    }

    /**
     * �A�C�h�����̃R�l�N�V���������ׂăN���[�Y���܂��B<br />
     * �g�p���̃R�l�N�V�����̓N���[�Y����܂���̂Œ��ӂ��Ă��������B
     */
    public void closeAll()
    {
        try
        {
            for (ObjectPool connectionPool : this.connectionPoolMap_.values())
            {
                connectionPool.clear();
            }
            int numActive = getNumActive();
            if (numActive > 0)
            {
                LOGGER.log(ACTIVE_CONNECTIONS_REMAINED, numActive);
            }
            this.connectionPoolMap_.clear();
            this.dataSourceList_.clear();
        }
        catch (Exception ex)
        {
            LOGGER.log(EXCEPTION_OCCURED, ex, ex.getMessage());
        }
    }

    /**
     * �R�l�N�V�������擾���܂��B<br />
     *
     * @param dataSource �f�[�^�\�[�X
     * @param dbName {@link ConnectionWrapper} �ɐݒ肷��f�[�^�x�[�X��
     * @return {@link Connection} �I�u�W�F�N�g
     * @throws SQLException �R�l�N�V�������擾�ł��Ȃ������ꍇ
     */
    protected Connection getConnection(final DataSource dataSource, final String dbName)
        throws SQLException
    {
        Connection conn = dataSource.getConnection();
        ConnectionWrapper wrapper = new ConnectionWrapper(conn, dbName);
        LOGGER.log(DB_CONNECTED, dbName);
        return wrapper;
    }

    /**
     * �w�肳�ꂽ�f�[�^�x�[�X�̃f�[�^�\�[�X���擾���܂��B<br />
     *
     * @param dbname �f�[�^�x�[�X��
     * @return �f�[�^�\�[�X
     */
    protected DataSource getDataSource(final String dbname)
    {
        for (DataSourceEntry entry : this.dataSourceList_)
        {
            if (entry.getDbname().equals(dbname))
            {
                return entry.getDataSource();
            }
        }
        return null;
    }

    /**
     * �f�[�^�\�[�X�����X�g�ɓo�^���܂��B<br />
     *
     * @param dbname �f�[�^�x�[�X��
     * @param dataSource �f�[�^�\�[�X
     */
    protected void registDataSource(final String dbname, final DataSource dataSource)
    {
        if (getDataSource(dbname) == null)
        {
            DataSourceEntry entry = new DataSourceEntry(dbname, dataSource);
            this.dataSourceList_.add(entry);
        }
    }

    /**
     * {@link DataSource} ���쐬���܂��B<br />
     *
     * @param dbname �f�[�^�x�[�X��
     * @param connectOnlyExists �f�[�^�x�[�X�����݂���Ƃ��̂ݐڑ�����ꍇ�� <code>true</code> �A
     *                          ���݂��Ȃ��Ƃ��Ƀf�[�^�x�[�X�𐶐�����ꍇ�� <code>false</code>
     * @return {@link DataSource}
     * @throws SQLException �f�[�^�\�[�X�쐬���ɗ�O�����������ꍇ
     */
    protected DataSource createPoolingDataSource(final String dbname,
        final boolean connectOnlyExists)
        throws SQLException
    {
        DataSourceCreator creator = getDataSourceCreator();
        return creator.createPoolingDataSource(dbname, connectOnlyExists);
    }

    /**
     * �f�[�^�x�[�X���� {@link DataSource} ��R�Â��ĊǗ����邽�߂̃G���g���N���X�ł��B<br />
     * 
     * @author y-komori
     */
    private static class DataSourceEntry
    {
        private final String     dbname_;

        private final DataSource dataSource_;

        /**
         * {@link DataSourceEntry} ���\�z���܂��B<br />
         * 
         * @param dbname �f�[�^�x�[�X��

         * @param dataSource {@link DataSource} �I�u�W�F�N�g
         */
        public DataSourceEntry(final String dbname, final DataSource dataSource)
        {
            this.dbname_ = dbname;
            this.dataSource_ = dataSource;
        }

        /**
         * �f�[�^�x�[�X����Ԃ��܂��B<br />
         * 
         * @return �f�[�^�x�[�X��

         */
        public String getDbname()
        {
            return this.dbname_;
        }

        /**
         * �f�[�^�\�[�X��Ԃ��܂��B<br />
         * 
         * @return �f�[�^�\�[�X
         */
        public DataSource getDataSource()
        {
            return this.dataSource_;
        }
    }

    /**
     * �v�[���̒�����g�p���Ă���I�u�W�F�N�g�̐���Ԃ��܂��B<br />
     *
     * @return �g�p���Ă���I�u�W�F�N�g�̐�
     */
    private int getNumActive()
    {
        int numActive = 0;
        for (ObjectPool objectPool : this.connectionPoolMap_.values())
        {
            numActive += objectPool.getNumActive();
        }
        return numActive;
    }

    /**
     * �R�l�N�V�����v�[������擾���܂��B
     * @param key �L�[
     * @return �R�l�N�V�����v�[��
     */
    public ObjectPool getConnectionPool(final String key)
    {
        return this.connectionPoolMap_.get(key);
    }

    /**
     * �R�l�N�V�����v�[������V�K�쐬���܂��B
     * @param key �L�[
     * @return �R�l�N�V�����v�[��
     */
    public ObjectPool createNewConnectionPool(String key)
    {
        ObjectPool connectionPool = this.connectionPoolMap_.get(key);
        if (connectionPool == null)
        {
            connectionPool = new StackObjectPool();
            this.connectionPoolMap_.put(key, connectionPool);
        }
        return connectionPool;
    }

    /**
     * �f�[�^�\�[�X�쐬�I�u�W�F�N�g���擾���܂��B<br />
     * 
     * @return �f�[�^�\�[�X�쐬�I�u�W�F�N�g
     */
    public synchronized DataSourceCreator getDataSourceCreator()
    {
        for (DataSourceCreator creator : dataSouceCreatorList__)
        {
            if (creator.isTarget())
            {
                return creator;
            }
        }
        return null;
    }

    /**
     * �V�[�P���X�ԍ����擾����SQL���擾���܂��B
     * @param sequenceName �V�[�P���X�ԍ�
     * @return �V�[�P���X�ԍ��擾SQL
     */
    public String getSequenceSql(String sequenceName)
    {
        DataSourceCreator creator = getDataSourceCreator();
        return creator.getSequenceSql(sequenceName);
    }
}
