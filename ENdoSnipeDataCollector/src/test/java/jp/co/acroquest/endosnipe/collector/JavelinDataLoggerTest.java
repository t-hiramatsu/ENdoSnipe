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
package jp.co.acroquest.endosnipe.collector;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.co.acroquest.endosnipe.collector.config.ConfigurationReader;
import jp.co.acroquest.endosnipe.collector.config.DataCollectorConfig;
import jp.co.acroquest.endosnipe.collector.config.RotateConfig;
import jp.co.acroquest.endosnipe.collector.data.JavelinConnectionData;
import jp.co.acroquest.endosnipe.collector.data.JavelinMeasurementData;
import jp.co.acroquest.endosnipe.collector.exception.InitializeException;
import jp.co.acroquest.endosnipe.collector.request.CommunicationClientRepository;
import jp.co.acroquest.endosnipe.common.entity.MeasurementData;
import jp.co.acroquest.endosnipe.common.entity.ResourceData;
import jp.co.acroquest.endosnipe.common.util.ResourceUtil;
import jp.co.acroquest.endosnipe.communicator.TelegramSender;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.impl.CommunicationClientImpl;
import jp.co.acroquest.endosnipe.data.dao.MeasurementInfoDao;
import jp.co.acroquest.endosnipe.data.dto.SignalDefinitionDto;
import jp.co.acroquest.endosnipe.data.entity.MeasurementInfo;
import jp.co.acroquest.endosnipe.util.ResourceDataDaoUtil;
import jp.co.dgic.testing.common.virtualmock.MockObjectManager;
import jp.co.dgic.testing.framework.DJUnitTestCase;

/**
 * {@link JavelinDataLogger} �̂��߂̃e�X�g�N���X�ł��B<br />
 * 
 * @author iida
 */
public class JavelinDataLoggerTest extends DJUnitTestCase
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp()
        throws Exception
    {
        super.setUp();
        MockObjectManager.initialize();
    }

    /**
     * ���\�[�X�f�[�^��DB�ւ̏������ݎ��ɁABottleneckEye�ւ̒ʒm���s���鎖���m�F����B
     * @throws Exception ��O�����������ꍇ
     */
    public void testLogResourceData_Connect()
        throws Exception
    {
        // ����
        addReturnValue(JavelinDataLogger.class, "calculateAndAddCpuUsageData");
        addReturnValue(JavelinDataLogger.class, "calculateAndAddCoverageData");
        addReturnValue(ResourceDataDaoUtil.class, "insert");
        addReturnValue(JavelinDataLogger.class, "alarmThresholdExceedance");

        int beforeCount = getCallCount("CommunicationServerImpl", "sendTelegram");

        String database = "database";
        JavelinDataLogger javelinDataLogger = createJavelinDataLogger(database);

        // ���s
        Class<?> cls = javelinDataLogger.getClass();
        Method method = cls.getDeclaredMethod("logResourceData", String.class, ResourceData.class);
        method.setAccessible(true);
        ResourceData resourceData = new ResourceData();
        resourceData.getMeasurementMap().put("/test0", new MeasurementData());
        method.invoke(javelinDataLogger, database, resourceData);

        int afterCount = getCallCount("CommunicationServerImpl", "sendTelegram");

        // ����
        assertEquals(beforeCount + 1, afterCount);

    }

    /**
     * ���\�[�X�f�[�^��DB�ւ̏������ݎ��ɁABottleneckEye�ւ̒ʒm���s���鎖���m�F����B
     * @throws Exception ��O�����������ꍇ
     */
    public void testLogResourceData_disconnect()
        throws Exception
    {
        // ����
        addReturnValue(JavelinDataLogger.class, "calculateAndAddCpuUsageData");
        addReturnValue(JavelinDataLogger.class, "calculateAndAddCoverageData");
        addReturnValue(ResourceDataDaoUtil.class, "insert");
        addReturnValue(JavelinDataLogger.class, "alarmThresholdExceedance");

        int beforeCount = getCallCount("CommunicationServerImpl", "sendTelegram");

        String database = "database";
        JavelinDataLogger javelinDataLogger = createJavelinDataLogger(database);

        // ���s
        Class<?> cls = javelinDataLogger.getClass();
        Method method =
                        cls.getDeclaredMethod("logResourceData", String.class, ResourceData.class,
                                              boolean.class);
        method.setAccessible(true);
        ResourceData resourceData = new ResourceData();
        resourceData.getMeasurementMap().put("/test0", new MeasurementData());
        method.invoke(javelinDataLogger, database, resourceData, true);

        int afterCount = getCallCount("CommunicationServerImpl", "sendTelegram");

        // ����
        assertEquals(beforeCount, afterCount);

    }

    /**
     * �ڑ��d��(�ڑ��J�n�C�x���g)
     * JavelinConnectionData�̐ڑ��J�n�t���O(connectionData_)��true�ɂ��A
     * JavelinDataLogger#logJavelinData�����s����B
     * 
     * [����]
     * CommunicationServerImpl#sendTelegram����x���Ă΂�Ȃ����ƁB
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testLogJavelinData_connect()
        throws Exception
    {
        // ����
        int beforeCount = getCallCount("CommunicationServerImpl", "sendTelegram");

        String database = "database";
        JavelinDataLogger javelinDataLogger = createJavelinDataLogger(database);

        // ���s
        Method method = getLogJavelinData();

        boolean connectionData = true;
        JavelinConnectionData javelinLogData = new JavelinConnectionData(connectionData);

        method.invoke(javelinDataLogger, javelinLogData);

        // ����
        int afterCount = getCallCount("CommunicationServerImpl", "sendTelegram");

        assertEquals(beforeCount, afterCount);

    }

    /**
     * �ڑ��d��(�ؒf�C�x���g/�O��̃f�[�^����)
     * <li>JavelinConnectionData�̐ڑ��J�n�t���O(connectionData_)��false�ɂ���B</li>
     * <li>prevConvertedResourceDataMap_�̕Ԃ�l��ݒ肷��</li>
     * <li>JavelinDataLogger#logJavelinData�����s����B</li>
     * 
     * 
     * [����]
     * CommunicationServerImpl#sendTelegram���Ă΂�Ă��Ȃ����ƁB
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testLogJavelinData_disConnectPrevData()
        throws Exception
    {
        // ����
        addReturnValue(ResourceDataDaoUtil.class, "insert");
        addReturnValue(JavelinDataLogger.class, "alarmThresholdExceedance");
        addReturnValue(JavelinDataLogger.class, "calculateAndAddCpuUsageData");
        addReturnValue(JavelinDataLogger.class, "calculateAndAddCoverageData");
        addReturnValue(MeasurementInfoDao.class, "selectAll", new ArrayList<MeasurementInfo>());
        addReturnValue(ResourceDataDaoUtil.class, "insert");

        int beforeCount = getCallCount("CommunicationServerImpl", "sendTelegram");

        String database = "database";
        JavelinDataLogger javelinDataLogger = createJavelinDataLogger(database);

        // prevConvertedResourceDataMap_���擾����B
        Class<?> cls = javelinDataLogger.getClass();
        Field prevConvertedResourceDataMap = cls.getDeclaredField("prevConvertedResourceDataMap_");
        prevConvertedResourceDataMap.setAccessible(true);

        Map<String, ResourceData> resourceDataMap = new HashMap<String, ResourceData>();
        ResourceData resourceData = new ResourceData();
        resourceData.addMeasurementData(new MeasurementData());
        resourceDataMap.put(database, resourceData);
        prevConvertedResourceDataMap.set(javelinDataLogger, resourceDataMap);

        // ���s
        Method method = getLogJavelinData();

        boolean connectionData = false;
        JavelinConnectionData javelinLogData = new JavelinConnectionData(connectionData);
        javelinLogData.setDatabaseName(database);

        method.invoke(javelinDataLogger, javelinLogData);

        // ����
        int afterCount = getCallCount("CommunicationServerImpl", "sendTelegram");

        assertEquals(beforeCount, afterCount);

    }

    /**
     * �ڑ��d��(�ؒf�C�x���g/�O��̃f�[�^�Ȃ�)
     * <li>JavelinConnectionData�̐ڑ��J�n�t���O(connectionData_)��false�ɂ���B</li>
     * <li>prevConvertedResourceDataMap_�̕Ԃ�l��ݒ肵�Ȃ�</li>
     * <li>JavelinDataLogger#logJavelinData�����s����B</li>
     * 
     * 
     * [����]
     * CommunicationServerImpl#sendTelegram���Ă΂�Ă��Ȃ����ƁB
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testLogJavelinData_disConnectPrevNoData()
        throws Exception
    {
        // ����
        int beforeCount = getCallCount("CommunicationServerImpl", "sendTelegram");

        String database = "database";
        JavelinDataLogger javelinDataLogger = createJavelinDataLogger(database);

        // ���s
        Method method = getLogJavelinData();

        boolean connectionData = false;
        JavelinConnectionData javelinLogData = new JavelinConnectionData(connectionData);

        method.invoke(javelinDataLogger, javelinLogData);

        // ����
        int afterCount = getCallCount("CommunicationServerImpl", "sendTelegram");

        assertEquals(beforeCount, afterCount);

    }

    /**
     * ���\�[�X�ʒm�d��(����ڑ�)
     * <li>JavelinConnectionData��connectionData_�t���O��true�ɂ���B</li>
     * <li>JavelinDataLogger#logJavelinData�ɑ΂��āA
     * JavelinMeasurementData�������ɂ��ČĂяo���B</li>
     * 
     * 
     * [����]
     * CommunicationServerImpl#sendTelegram��2��Ă΂�Ă��邱�ƁB
     * (�����l�p�̓d���A��M�����d���̂����A��M�����d���̂ݑ��M)
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testLogJavelinData_measurementFirstEvent()
        throws Exception
    {
        // ����
        addReturnValue(ResourceDataDaoUtil.class, "insert");
        setReturnValueAtAllTimes(JavelinDataLogger.class, "alarmThresholdExceedance");
        setReturnValueAtAllTimes(JavelinDataLogger.class, "calculateAndAddCpuUsageData");
        setReturnValueAtAllTimes(JavelinDataLogger.class, "calculateAndAddCoverageData");
        addReturnValue(MeasurementInfoDao.class, "selectAll", new ArrayList<MeasurementInfo>());
        addReturnValue(ResourceDataDaoUtil.class, "insert");

        int beforeCount = getCallCount("CommunicationServerImpl", "sendTelegram");

        String database = "database";
        JavelinDataLogger javelinDataLogger = createJavelinDataLogger(database);

        Map<String, ResourceData> resourceDataMap = new HashMap<String, ResourceData>();
        ResourceData resourceData = new ResourceData();
        resourceData.addMeasurementData(new MeasurementData());
        resourceDataMap.put(database, resourceData);

        Method method = getLogJavelinData();

        boolean connectionData = true;
        JavelinConnectionData javelinConnectionData = new JavelinConnectionData(connectionData);
        method.invoke(javelinDataLogger, javelinConnectionData);

        // ���s
        JavelinMeasurementData javelinLogData = new JavelinMeasurementData(resourceData);
        javelinLogData.setDatabaseName(database);

        method.invoke(javelinDataLogger, javelinLogData);

        // ����
        int afterCount = getCallCount("CommunicationServerImpl", "sendTelegram");

        assertEquals(beforeCount + 1, afterCount);

    }

    /**
     * ���\�[�X�ʒm�d��(2��ڈȍ~)
     * <li>JavelinDataLogger#logJavelinData�ɑ΂��āAJavelinConnectionData�������ɂ��ČĂяo���B</li>
     * <li>JavelinDataLogger#logJavelinData�ɑ΂��āAJavelinMeasurementData�������ɂ��ČĂяo���B</li>
     * 
     * 
     * [����]
     * CommunicationServerImpl#sendTelegram��1��Ă΂�Ă��邱�ƁB
     * (��M�����d��)
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testLogJavelinData_testLogResourceData_measurementSecondEvent()
        throws Exception
    {
        // ����
        addReturnValue(ResourceDataDaoUtil.class, "insert");
        setReturnValueAtAllTimes(JavelinDataLogger.class, "alarmThresholdExceedance");
        setReturnValueAtAllTimes(JavelinDataLogger.class, "calculateAndAddCpuUsageData");
        setReturnValueAtAllTimes(JavelinDataLogger.class, "calculateAndAddCoverageData");
        addReturnValue(MeasurementInfoDao.class, "selectAll", new ArrayList<MeasurementInfo>());
        addReturnValue(ResourceDataDaoUtil.class, "insert");

        int beforeCount = getCallCount("CommunicationServerImpl", "sendTelegram");

        String database = "database";
        JavelinDataLogger javelinDataLogger = createJavelinDataLogger(database);

        // prevConvertedResourceDataMap_���擾����B
        Class<?> cls = javelinDataLogger.getClass();
        Field prevConvertedResourceDataMap = cls.getDeclaredField("prevConvertedResourceDataMap_");
        prevConvertedResourceDataMap.setAccessible(true);

        Map<String, ResourceData> resourceDataMap = new HashMap<String, ResourceData>();
        ResourceData resourceData = new ResourceData();
        resourceData.addMeasurementData(new MeasurementData());
        resourceDataMap.put(database, resourceData);
        prevConvertedResourceDataMap.set(javelinDataLogger, resourceDataMap);

        // ���s
        Method method = getLogJavelinData();

        JavelinMeasurementData javelinLogData = new JavelinMeasurementData(resourceData);
        javelinLogData.setDatabaseName(database);

        method.invoke(javelinDataLogger, javelinLogData);

        // ����
        int afterCount = getCallCount("CommunicationServerImpl", "sendTelegram");

        assertEquals(beforeCount + 1, afterCount);

    }

    /**
     * �ΏۊO�̓d��(�ڑ��d���AJavelin���O�ʒm�d���A���\�[�X�ʒm�d���ȊO)
     * <li>JavelinConnectionData���p�������AMockJavelinConnectionData���쐬����B</li>
     * <li>JavelinDataLogger#logJavelinData�����s����B</li>
     * 
     * 
     * [����]
     * CommunicationServerImpl#sendTelegram���Ă΂�Ă��Ȃ����ƁB
     * 
     * @throws Exception ��O�����������ꍇ
     */
    public void testLogJavelinData_testLogResourceData_otherData()
        throws Exception
    {
        // ����
        int beforeCount = getCallCount("CommunicationServerImpl", "sendTelegram");

        String database = "database";
        JavelinDataLogger javelinDataLogger = createJavelinDataLogger(database);
        Method method = getLogJavelinData();

        // ���s
        MockJavelinData javelinData = new MockJavelinData();
        method.invoke(javelinDataLogger, javelinData);

        // ����
        int afterCount = getCallCount("CommunicationServerImpl", "sendTelegram");
        assertEquals(beforeCount, afterCount);

    }

    /**
     * JavelinDataLogger�I�u�W�F�N�g���쐬���܂��B
     * @param dataBase �f�[�^�x�[�X��
     * @return {@link JavelinDataLogger}�I�u�W�F�N�g
     * @throws IOException ���o�͗�O�����������ꍇ
     * @throws InitializeException ���������ɗ�O�����������ꍇ
     */
    private JavelinDataLogger createJavelinDataLogger(final String dataBase)
        throws IOException,
            InitializeException
    {
        File file = ResourceUtil.getResourceAsFile(getClass(), "dataCollector.conf");
        DataCollectorConfig config = ConfigurationReader.load(file.getAbsolutePath());
        JavelinDataLogger javelinDataLogger =
                                              new JavelinDataLogger(
                                                                    config,
                                                                    new ClientRepositoryMock(),
                                                                    new HashMap<Long, SignalDefinitionDto>());
        RotateConfig rotateConfig = new RotateConfig();
        rotateConfig.setDatabase(dataBase);
        javelinDataLogger.addRotateConfig(rotateConfig);
        return javelinDataLogger;
    }

    /**
     * JavelinDataLogger#logJavelinData���擾���܂��B
     * @return JavelinDataLogger#logJavelinData���\�b�h
     * @throws NoSuchMethodException ���\�b�h��������Ȃ��ꍇ
     * @throws ClassNotFoundException �N���X��������Ȃ��ꍇ
     */
    private Method getLogJavelinData()
        throws NoSuchMethodException,
            ClassNotFoundException
    {
        Class<?> cls = Class.forName("jp.co.acroquest.endosnipe.collector.JavelinDataLogger");
        Method[] methods = cls.getDeclaredMethods();
        Method returnMethod = null;
        for (Method tmpMethod : methods)
        {
            if ("logJavelinData".equals(tmpMethod.getName()))
            {
                returnMethod = tmpMethod;
                returnMethod.setAccessible(true);
                return returnMethod;
            }
        }
        return null;
    }

    private static class ClientRepositoryMock implements CommunicationClientRepository
    {
        /**
         * {@inheritDoc}
         */
        public void sendTelegramToClient(final String clientId, final Telegram telegram)
        {

        }

        /**
         * {@inheritDoc}
         */
        public TelegramSender getTelegramSender(final String clientId)
        {
            return new CommunicationClientImpl("Thread-Name");
        }

    }
}
