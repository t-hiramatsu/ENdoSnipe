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
package jp.co.acroquest.endosnipe.data.service;

import java.sql.SQLException;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.data.dao.AbstractDaoTest;
import jp.co.acroquest.endosnipe.data.dao.HostInfoDao;
import jp.co.acroquest.endosnipe.data.entity.HostInfo;

/**
 * ホスト情報管理機能に対するテストクラス
 * 
 * @author M.Yoshida
 *
 */
public class HostInfoManagerTest extends AbstractDaoTest
{

    protected void setUp()
        throws Exception
    {
        super.setUp();
    }
    
    
    public void testGetHostInfo1()
    {
        // 条件
        // <-- DBにデータが無い -->
        
        // 実施
        HostInfo result = HostInfoManager.getHostInfo(DB_NAME, true);
        
        // 結果確認
        assertNull(result);
        
        assertCalled(ENdoSnipeLogger.class, "log");
        int logCalled = getCallCount(ENdoSnipeLogger.class, "log");
        
        // エラーログが出力されないことを確認する。
        assertFalse(getArgument(ENdoSnipeLogger.class, "log", logCalled - 1, 1) instanceof SQLException);
    }

    public void testGetHostInfo2()
    {
        // 条件
        // <-- DBにデータが無い -->
        
        // 実施
        HostInfo result = HostInfoManager.getHostInfo(DB_NAME, false);
        
        // 結果確認
        assertNull(result);

        assertCalled(ENdoSnipeLogger.class, "log");
        int logCalled = getCallCount(ENdoSnipeLogger.class, "log");
        
        assertFalse("EEDA0103".equals(getArgument(ENdoSnipeLogger.class, "log", logCalled - 1, 0)));
    }

    public void testGetHostInfo3()
    {
        // 条件
        try
        {
            initializeHostInfoTable(new String[]{"mica, 192.168.252.22, 15002, 補足１２"});
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        // 実施
        HostInfo result = HostInfoManager.getHostInfo(DB_NAME, true);
        
        // 結果確認
        assertNotNull(result);
        
        assertEquals("mica", result.hostName);
        assertEquals("192.168.252.22", result.ipAddress);
        assertEquals(15002, result.port);
        assertEquals("補足１２", result.description);

        assertCalled(ENdoSnipeLogger.class, "log");
        int logCalled = getCallCount(ENdoSnipeLogger.class, "log");
        
        assertFalse("EEDA0103".equals(getArgument(ENdoSnipeLogger.class, "log", logCalled - 1, 0)));
    }

    public void testGetHostInfo4()
    {
        // 条件
        try
        {
            initializeHostInfoTable(new String[]{"mica, 192.168.252.22, 15002, 補足１２"});
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        // 実施
        HostInfo result = HostInfoManager.getHostInfo(DB_NAME, false);
        
        // 結果確認
        assertNotNull(result);
        
        assertEquals("mica", result.hostName);
        assertEquals("192.168.252.22", result.ipAddress);
        assertEquals(15002, result.port);
        assertEquals("補足１２", result.description);

        assertCalled(ENdoSnipeLogger.class, "log");
        int logCalled = getCallCount(ENdoSnipeLogger.class, "log");
        
        assertFalse("EEDA0103".equals(getArgument(ENdoSnipeLogger.class, "log", logCalled - 1, 0)));
    }
    
    public void testRegisterHostInfo1()
    {
        // 条件
        // <-- ホスト情報無し -->
        
        // 実施
        HostInfoManager.registerHostInfo(DB_NAME, "mica", "192.168.252.22", 15002, "補足１２");
        
        // 結果確認
        HostInfo expected = new HostInfo();
        expected.hostName = "mica";
        expected.ipAddress = "192.168.252.22";
        expected.port = 15002;
        expected.description = "補足１２";
        
        assertRegistedDataEqual(expected);
    }

    public void testRegisterHostInfo2()
    {
        // 条件
        try
        {
            initializeHostInfoTable(new String[]{"mica, 192.168.252.22, 15002, 補足１２"});
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        // 実施
        HostInfoManager.registerHostInfo(DB_NAME, "mica", "192.168.252.22", 15002, "補足１２");
        
        // 結果確認
        HostInfo expected = new HostInfo();
        expected.hostName = "mica";
        expected.ipAddress = "192.168.252.22";
        expected.port = 15002;
        expected.description = "補足１２";
        
        assertRegistedDataEqual(expected);
        
        assertNotCalled(HostInfoDao.class, "deleteAll");
    }
    
    public void testRegisterHostInfo3()
    {
        // 条件
        try
        {
            initializeHostInfoTable(new String[]{"mica, 192.168.252.22, 15002, 補足１２"});
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        // 実施
        HostInfoManager.registerHostInfo(DB_NAME, "garnet", "192.168.252.22", 15002, "補足１２");
        
        // 結果確認
        HostInfo expected = new HostInfo();
        expected.hostName = "garnet";
        expected.ipAddress = "192.168.252.22";
        expected.port = 15002;
        expected.description = "補足１２";
        
        assertRegistedDataEqual(expected);
        
        assertCalled(HostInfoDao.class, "deleteAll");
    }

    public void testRegisterHostInfo4()
    {
        // 条件
        try
        {
            initializeHostInfoTable(new String[]{"mica, 192.168.252.22, 15002, 補足１２"});
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        // 実施
        HostInfoManager.registerHostInfo(DB_NAME, "mica", "212.168.252.22", 15002, "補足１２");
        
        // 結果確認
        HostInfo expected = new HostInfo();
        expected.hostName = "mica";
        expected.ipAddress = "212.168.252.22";
        expected.port = 15002;
        expected.description = "補足１２";
        
        assertRegistedDataEqual(expected);
        
        assertCalled(HostInfoDao.class, "deleteAll");
    }

    public void testRegisterHostInfo5()
    {
        // 条件
        try
        {
            initializeHostInfoTable(new String[]{"mica, 192.168.252.22, 15002, 補足１２"});
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        // 実施
        HostInfoManager.registerHostInfo(DB_NAME, "mica", "192.168.252.22", 15432, "補足１２");
        
        // 結果確認
        HostInfo expected = new HostInfo();
        expected.hostName = "mica";
        expected.ipAddress = "192.168.252.22";
        expected.port = 15432;
        expected.description = "補足１２";
        
        assertRegistedDataEqual(expected);
        
        assertCalled(HostInfoDao.class, "deleteAll");
    }
    
    public void testRegisterHostInfo6()
    {
        // 条件
        try
        {
            initializeHostInfoTable(new String[]{"mica, 192.168.252.22, 15002, 補足１２"});
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        // 実施
        HostInfoManager.registerHostInfo(DB_NAME, "mica", "192.168.252.22", 15002, "説明３９");
        
        // 結果確認
        HostInfo expected = new HostInfo();
        expected.hostName = "mica";
        expected.ipAddress = "192.168.252.22";
        expected.port = 15002;
        expected.description = "説明３９";
        
        assertRegistedDataEqual(expected);
        
        assertCalled(HostInfoDao.class, "deleteAll");
    }

    public void testRegisterHostInfo7()
    {
        // 条件
        try
        {
            initializeHostInfoTable(new String[]{"mica, 192.168.252.22, 15002, 補足１２"});
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        // 実施
        HostInfoManager.registerHostInfo(DB_NAME, "", "192.168.252.22", 15002, "説明１２");
        
        // 結果確認
        HostInfo expected = new HostInfo();
        expected.hostName = "";
        expected.ipAddress = "192.168.252.22";
        expected.port = 15002;
        expected.description = "説明１２";
        
        assertRegistedDataEqual(expected);
        
        assertCalled(HostInfoDao.class, "deleteAll");
    }

    public void testRegisterHostInfo8()
    {
        // 条件
        try
        {
            initializeHostInfoTable(new String[]{"mica, 192.168.252.22, 15002, 補足１２"});
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        // 実施
        HostInfoManager.registerHostInfo(DB_NAME, "mica", "", 15002, "説明１２");
        
        // 結果確認
        HostInfo expected = new HostInfo();
        expected.hostName = "mica";
        expected.ipAddress = "";
        expected.port = 15002;
        expected.description = "説明１２";
        
        assertRegistedDataEqual(expected);
        
        assertCalled(HostInfoDao.class, "deleteAll");
    }

    public void testRegisterHostInfo9()
    {
        // 条件
        try
        {
            initializeHostInfoTable(new String[]{", 192.168.252.22, 15002, 補足１２"});
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        // 実施
        HostInfoManager.registerHostInfo(DB_NAME, "mica", "192.168.252.22", 15002, "補足１２");
        
        // 結果確認
        HostInfo expected = new HostInfo();
        expected.hostName = "mica";
        expected.ipAddress = "192.168.252.22";
        expected.port = 15002;
        expected.description = "補足１２";
        
        assertRegistedDataEqual(expected);
        
        assertCalled(HostInfoDao.class, "deleteAll");
    }
    
    public void testRegisterHostInfo10()
    {
        // 条件
        try
        {
            initializeHostInfoTable(new String[]{"mica, 192.168.252.22, 15002, 補足１２"});
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        // 実施
        HostInfoManager.registerHostInfo(DB_NAME, "", "192.168.252.22", 15002, "補足１２");
        
        // 結果確認
        HostInfo expected = new HostInfo();
        expected.hostName = "";
        expected.ipAddress = "192.168.252.22";
        expected.port = 15002;
        expected.description = "補足１２";
        
        assertRegistedDataEqual(expected);
        
        assertCalled(HostInfoDao.class, "deleteAll");
    }
    
    private void assertRegistedDataEqual(HostInfo expect)
    {
        List<HostInfo> actual = null;
        try
        {
            actual = HostInfoDao.selectAll(DB_NAME);
        }
        catch (SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        assertEquals(1, actual.size());
        
        HostInfo actualData = actual.get(0);
        
        assertEquals(expect.hostName, actualData.hostName);
        assertEquals(expect.ipAddress, actualData.ipAddress);
        assertEquals(expect.port, actualData.port);
        assertEquals(expect.description, actualData.description);
    }
    
    private void initializeHostInfoTable(String[] datarows) throws SQLException
    {
        if(datarows == null)
        {
            return;
        }
        
        for(String row : datarows)
        {
            String[] elements = row.split(",");
            
            HostInfo info = new HostInfo();
            info.hostName = elements[0].trim();
            info.ipAddress = elements[1].trim();
            info.port = Integer.parseInt(elements[2].trim());
            info.description = elements[3].trim();
            
            HostInfoDao.insert(DB_NAME, info);
        }
    }
    
    
}
