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
package jp.co.acroquest.endosnipe.collector.config;

import java.io.File;
import java.io.IOException;

import jp.co.acroquest.endosnipe.collector.exception.InitializeException;
import jp.co.acroquest.endosnipe.common.util.ResourceUtil;
import junit.framework.TestCase;

/**
 * {@link ConfigurationReader} �̂��߂̃e�X�g�N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class ConfigurationReaderTest extends TestCase
{
    /**
     * {@link ConfigurationReader#load(String)} ���\�b�h�̃e�X�g�N���X�ł��B<br />
     */
    public void testLoad()
        throws InitializeException,
            IOException
    {
        File file = ResourceUtil.getResourceAsFile(getClass(), "dataCollector.conf");
        DataCollectorConfig config = ConfigurationReader.load(file.getAbsolutePath());
        assertNotNull("1", config);
        assertEquals("2", 3, config.getAgentSettingList().size());

        for (AgentSetting setting : config.getAgentSettingList())
        {
            System.out.println(setting);
        }
    }

    /**
     * {@link ConfigurationReader#load(String)} ���\�b�h�̃e�X�g�N���X�ł��B<br />
     * ���[���ʒm�̐ݒ� (SMTP settings) �ɂ��āA<br />
     * collector.properties �����ݒ�̏ꍇ�̃f�t�H���g�l���m�F���܂��B<br />
     * �m�F����͈̂ȉ��̃p�����[�^�ł��B<br />
     * <ul>
     * <li>collector.smtp.sendMail</li>
     * <li>collector.smtp.server</li>
     * <li>collector.smtp.encoding</li>
     * <li>collector.smtp.from</li>
     * <li>collector.smtp.to</li>
     * <li>collector.smtp.subject</li>
     * </ul>
     */
    public void testLoad_CheckSMTPSettings_Default()
        throws InitializeException,
            IOException
    {
        // ����
        File file = ResourceUtil.getResourceAsFile(getClass(), "collector_NotDefined.properties");

        // ���{
        DataCollectorConfig config = ConfigurationReader.load(file.getAbsolutePath());

        // ����
        assertEquals(config.isSendMail(), false);
        assertEquals(config.getSmtpServer(), "mail.example.com");
        assertEquals(config.getSmtpEncoding(), "iso-2022-jp");
        assertEquals(config.getSmtpFrom(), "endosnipe@example.com");
        assertEquals(config.getSmtpTo(), "endosnipe@example.com");
        assertEquals(config.getSmtpSubject(), "[javelin] ${eventName} is occurred.");
    }

    /**
     * {@link ConfigurationReader#load(String)} ���\�b�h�̃e�X�g�N���X�ł��B<br />
     * ���[���ʒm�̐ݒ� (SMTP settings) �ɂ��āA<br />
     * collector.properties �ɕs���l���L�q����Ă����ꍇ�̒l���m�F���܂��B<br />
     * �m�F����͈̂ȉ��̃p�����[�^�ł��B<br />
     * <ul>
     * <li>collector.smtp.sendMail</li>
     * </ul>
     */
    public void testLoad_CheckSMTPSettings_WrongDefine()
        throws InitializeException,
            IOException
    {
        // ����
        File file = ResourceUtil.getResourceAsFile(getClass(), "collector_WrongDefine.properties");

        // ���{
        DataCollectorConfig config = ConfigurationReader.load(file.getAbsolutePath());

        // ����
        assertEquals(config.isSendMail(), false);
    }

    /**
     * {@link ConfigurationReader#load(String)} ���\�b�h�̃e�X�g�N���X�ł��B<br />
     * �g���b�v���M�̐ݒ� (SNMP settings) �ɂ��āA<br />
     * collector.properties �����ݒ�̏ꍇ�̃f�t�H���g�l���m�F���܂��B<br />
     * �m�F����͈̂ȉ��̃p�����[�^�ł��B<br />
     * <ul>
     * <li>collector.snmp.sendTrap</li>
     * <li>collector.snmp.managers</li>
     * <li>collector.snmp.trapPort</li>
     * <li>collector.snmp.version</li>
     * <li>collector.snmp.trapCommunity</li>
     * </ul>
     */
    public void testLoad_CheckSNMPSettings_Default()
        throws InitializeException,
            IOException
    {
        // ����
        File file = ResourceUtil.getResourceAsFile(getClass(), "collector_NotDefined.properties");

        // ���{
        DataCollectorConfig config = ConfigurationReader.load(file.getAbsolutePath());

        // ����
        assertEquals(config.isSendTrap(), false);
        assertEquals(config.getManagers(), "localhost");
        assertEquals(config.getTrapPort(), 162);
        assertEquals(config.getVersion(), "v2c");
        assertEquals(config.getTrapCommunity(), "public");
    }

    /**
     * {@link ConfigurationReader#load(String)} ���\�b�h�̃e�X�g�N���X�ł��B<br />
     * �g���b�v���M�̐ݒ� (SNMP settings) �ɂ��āA<br />
     * collector.properties �ɕs���l���L�q����Ă����ꍇ�̒l���m�F���܂��B<br />
     * �m�F����͈̂ȉ��̃p�����[�^�ł��B<br />
     * <ul>
     * <li>collector.snmp.sendTrap</li>
     * </ul>
     */
    public void testLoad_CheckSNMPSettings_WrongDefine()
        throws InitializeException,
            IOException
    {
        // ����
        File file = ResourceUtil.getResourceAsFile(getClass(), "collector_WrongDefine.properties");

        // ���{
        DataCollectorConfig config = ConfigurationReader.load(file.getAbsolutePath());

        // ����
        assertEquals(config.isSendTrap(), false);
    }
}
