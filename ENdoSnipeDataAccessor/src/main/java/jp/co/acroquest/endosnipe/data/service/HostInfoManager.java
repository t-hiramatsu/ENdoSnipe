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
import jp.co.acroquest.endosnipe.data.LogMessageCodes;
import jp.co.acroquest.endosnipe.data.TableNames;
import jp.co.acroquest.endosnipe.data.dao.HostInfoDao;
import jp.co.acroquest.endosnipe.data.entity.HostInfo;

import org.seasar.framework.util.StringUtil;

/**
 * �f�[�^�x�[�X�̃z�X�g���Ɋւ���Ǘ����s�����߂̃N���X�ł��B<br />
 * �z�X�g���Ɋւ���A�N�Z�X�́ADAO �𒼐ڎg�p�����ɖ{�N���X���g�p���܂��B<br />
 * 
 * @author y-komori
 */
public class HostInfoManager implements LogMessageCodes, TableNames
{
    private static final ENdoSnipeLogger LOGGER =
            ENdoSnipeLogger.getLogger(HostInfoManager.class);

    private HostInfoManager()
    {
        // Do nothing.
    }

    /**
     * �w�肳�ꂽ�f�[�^�x�[�X�̃z�X�g�����擾���܂��B<br />
     * 
     * @param database �f�[�^�x�[�X
     * @param log �z�X�g��񂪎擾�ł��Ȃ��ꍇ�ɃG���[���e�����O�ɏo�͂���ꍇ�� <code>true</code>
     * @return �z�X�g���B������Ȃ��ꍇ�� <code>null</code>�B
     */
    public static HostInfo getHostInfo(final String database, final boolean log)
    {
        HostInfo hostInfo = null;
        try
        {
            List<HostInfo> hostInfoList = HostInfoDao.selectAll(database);
            if (hostInfoList.size() == 1)
            {
                hostInfo = hostInfoList.get(0);
            }
        }
        catch (SQLException ex)
        {
            if (log)
            {
                LOGGER.log(DB_ACCESS_ERROR, ex, ex.getMessage());
            }
        }
        return hostInfo;
    }

    /**
     * �z�X�g���� DB �ɓo�^���܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @param hostName �z�X�g���i <code>null</code> ���j
     * @param ipAddress IP�A�h���X�i <code>null</code> �͕s�j
     * @param port �|�[�g�ԍ�
     * @param description �f�[�^�x�[�X�̐���
     */
    public static synchronized void registerHostInfo(final String database, final String hostName,
            final String ipAddress, final int port, final String description)
    {
        try
        {
            // �w�肳�ꂽ�z�X�g����DB�ɓo�^����Ă���z�X�g�����r���A
            // �ύX������΍X�V����
            HostInfo oldHostInfo = getHostInfo(database, false);
            if (oldHostInfo != null)
            {
                if (StringUtil.equals(hostName, oldHostInfo.hostName)
                        && StringUtil.equals(ipAddress, oldHostInfo.ipAddress)
                        && port == oldHostInfo.port
                        && StringUtil.equals(description, oldHostInfo.description))
                {
                    // �z�X�g��񂪈�v�����̂ŉ������Ȃ�
                    return;
                }
            }
            // �ύX����������A��U�폜���Ă���o�^���Ȃ���
            HostInfoDao.deleteAll(database);

            // �z�X�g��񂪍X�V����Ă��邽�߁A�ēo�^���s��
            HostInfo newHostInfo = new HostInfo();
            newHostInfo.hostName = hostName;
            newHostInfo.ipAddress = ipAddress;
            newHostInfo.port = port;
            newHostInfo.description = description;
            HostInfoDao.insert(database, newHostInfo);
            LOGGER.log(HOST_REGISTERED, hostName, ipAddress, port);
        }
        catch (SQLException ex)
        {
            LOGGER.log(DB_ACCESS_ERROR, ex, ex.getMessage());
        }
    }
}
