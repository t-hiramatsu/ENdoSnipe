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
package jp.co.acroquest.endosnipe.data.dao;

import java.sql.SQLException;
import java.util.List;

import jp.co.acroquest.endosnipe.data.entity.HostInfo;

/**
 * {@link HostInfoDao} �N���X�̃e�X�g�P�[�X�B<br />
 *
 * @author y-sakamoto
 */
public class HostInfoDaoTest extends AbstractDaoTest
{

    /**
     * @target testInsert_one
     * @test �}��
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public void testInsert_one()
        throws SQLException
    {
        // ����
        HostInfo hostInfo = createHostInfo("localhost", "127.0.0.1", 18000, "Host1");

        // ���s
        HostInfoDao.insert(DB_NAME, hostInfo);

        // ����
        List<HostInfo> actual = HostInfoDao.selectAll(DB_NAME);
        assertEquals(1, actual.size());
        assertEquals("localhost", actual.get(0).hostName);
        assertEquals("127.0.0.1", actual.get(0).ipAddress);
        assertEquals(18000, actual.get(0).port);
        assertEquals("Host1", actual.get(0).description);
    }

    /**
     * @target testSelectAll_exist
     * @test �I��
     *   Condition:: ���R�[�h�����݂���B
     *   Result:: ���ׂẴ��R�[�h���Ԃ邱�ƁB
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public void testSelectAll_exist()
        throws SQLException
    {
        // ����
        HostInfo hostInfo;
        hostInfo = createHostInfo("localhost1", "127.0.0.1", 18000, "Host1");
        HostInfoDao.insert(DB_NAME, hostInfo);
        hostInfo = createHostInfo("localhost2", "127.0.0.2", 18001, "Host2");
        HostInfoDao.insert(DB_NAME, hostInfo);

        // ���s
        List<HostInfo> actual = HostInfoDao.selectAll(DB_NAME);

        // ����
        assertEquals(2, actual.size());
        assertEquals("localhost1", actual.get(0).hostName);
        assertEquals("127.0.0.1", actual.get(0).ipAddress);
        assertEquals(18000, actual.get(0).port);
        assertEquals("Host1", actual.get(0).description);
        assertEquals("localhost2", actual.get(1).hostName);
        assertEquals("127.0.0.2", actual.get(1).ipAddress);
        assertEquals(18001, actual.get(1).port);
        assertEquals("Host2", actual.get(1).description);
    }

    /**
     * @target testSelectAll_notExist
     * @test �I��
     *   Condition:: ���R�[�h�����݂��Ȃ��B
     *   Result:: ���R�[�h���Ԃ�Ȃ����ƁB
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public void testSelectAll_notExist()
        throws SQLException
    {
        // ���s
        List<HostInfo> actual = HostInfoDao.selectAll(DB_NAME);

        // ����
        assertEquals(0, actual.size());
    }

    /**
     * {@link HostInfo} �I�u�W�F�N�g���쐬���܂��B<br />
     *
     * @param hostName �z�X�g��
     * @param ipAddress IP �A�h���X
     * @param port �|�[�g�ԍ�
     * @param description �ڍא���
     * @return {@link HostInfo} �I�u�W�F�N�g
     */
    private HostInfo createHostInfo(String hostName, String ipAddress, int port, String description)
    {
        HostInfo hostInfo = new HostInfo();
        hostInfo.hostName = hostName;
        hostInfo.ipAddress = ipAddress;
        hostInfo.port = port;
        hostInfo.description = description;
        return hostInfo;
    }

}
