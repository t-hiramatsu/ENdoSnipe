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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.collector.config.DataCollectorConfig;
import jp.co.acroquest.endosnipe.collector.config.RotateConfig;
import jp.co.acroquest.endosnipe.collector.data.JavelinConnectionData;
import jp.co.acroquest.endosnipe.collector.data.JavelinData;
import jp.co.acroquest.endosnipe.collector.data.JavelinLogData;
import jp.co.acroquest.endosnipe.collector.data.JavelinMeasurementData;
import jp.co.acroquest.endosnipe.collector.log.JavelinLogUtil;
import jp.co.acroquest.endosnipe.collector.manager.SignalStateManager;
import jp.co.acroquest.endosnipe.collector.notification.AlarmEntry;
import jp.co.acroquest.endosnipe.collector.processor.AlarmData;
import jp.co.acroquest.endosnipe.collector.processor.AlarmProcessor;
import jp.co.acroquest.endosnipe.collector.processor.AlarmThresholdProcessor;
import jp.co.acroquest.endosnipe.collector.request.CommunicationClientRepository;
import jp.co.acroquest.endosnipe.collector.util.CollectorTelegramUtil;
import jp.co.acroquest.endosnipe.common.Constants;
import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.entity.MeasurementData;
import jp.co.acroquest.endosnipe.common.entity.MeasurementDetail;
import jp.co.acroquest.endosnipe.common.entity.ResourceData;
import jp.co.acroquest.endosnipe.common.jmx.JMXManager;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogAccessor;
import jp.co.acroquest.endosnipe.common.util.ResourceDataUtil;
import jp.co.acroquest.endosnipe.common.util.StreamUtil;
import jp.co.acroquest.endosnipe.communicator.accessor.ResourceNotifyAccessor;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.data.dao.JavelinLogDao;
import jp.co.acroquest.endosnipe.data.dao.JavelinMeasurementItemDao;
import jp.co.acroquest.endosnipe.data.db.DBManager;
import jp.co.acroquest.endosnipe.data.dto.SignalDefinitionDto;
import jp.co.acroquest.endosnipe.data.entity.JavelinLog;
import jp.co.acroquest.endosnipe.data.entity.JavelinMeasurementItem;
import jp.co.acroquest.endosnipe.data.util.AccumulatedValuesDefinition;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;
import jp.co.acroquest.endosnipe.javelin.parser.ParseException;
import jp.co.acroquest.endosnipe.util.InsertResult;
import jp.co.acroquest.endosnipe.util.ResourceDataDaoUtil;
import jp.co.acroquest.endosnipe.util.RotateCallback;

/**
 * {@link JavelinData} ���f�[�^�x�[�X�֊i�[���邽�߂̃N���X�ł��B<br />
 *
 * @author y-komori
 * @author ochiai
 */
public class JavelinDataLogger implements Runnable, LogMessageCodes
{

    private static final String JVN_LOG_ENCODING = "UTF-8";

    private static final ENdoSnipeLogger LOGGER =
                                                  ENdoSnipeLogger.getLogger(JavelinDataLogger.class);

    private static final int TREE_TELEGRAM_DTO_COUNT = 3;

    private final JavelinDataQueue queue_ = new JavelinDataQueue();

    /** �ݒ� */
    private final DataCollectorConfig config_;

    /** �f�[�^�x�[�X�����L�[�Ɏ��A���[�e�[�g�ݒ��ێ�����}�b�v */
    private final Map<String, RotateConfig> rotateConfigMap_;

    /** �f�t�H���g�̃��[�e�[�Ɛݒ� */
    private RotateConfig defaultRotateConfig_;

    private final CommunicationClientRepository clientRepository_;

    private volatile boolean isRunnning_;

    /** �O��̌v���l */
    private final Map<String, ResourceData> prevResourceDataMap_ =
                                                                   new HashMap<String, ResourceData>();

    /** �O��̌v���l(�ώZ�������ɒ���������) */
    private final Map<String, ResourceData> prevConvertedResourceDataMap_ =
                                                                            new HashMap<String, ResourceData>();

    /** �f�[�^�x�[�X�����L�[�ɂ����A�O��f�[�^��}�������e�[�u���C���f�b�N�X��ێ�����}�b�v */
    private static Map<String, Integer> prevTableIndexMap__ =
                                                              new ConcurrentHashMap<String, Integer>();

    /**
     * Javelin����ڑ����ꂽ�Ƃ��̃C�x���g�B
     * �ڑ��f�[�^���󂯎�������ɃZ�b�g����A�ڑ��O�́A�S�Ă�0�̃f�[�^���������ލۂɗp������B
     * �������܂ꂽ��A���̃t�B�[���h��null�ɖ߂����B
     */
    private JavelinConnectionData connectionData_ = null;

    /** 臒l���菈�����s����`��ێ������}�b�v */
    private final Map<String, AlarmProcessor> processorMap_ =
                                                              new ConcurrentHashMap<String, AlarmProcessor>();

    /** 臒l���x���i����j */
    public static final int NORMAL_ALARM_LEVEL = 0;

    /** 臒l���x���i�Ď���~���j */
    public static final int STOP_ALARM_LEVEL = -1;

    /** JAVELIN_LOG �e�[�u���� truncate ����R�[���o�b�N���\�b�h */
    private final RotateCallback javelinRotateCallback_ = new RotateCallback() {
        /**
         * {@inheritDoc}
         */
        public String getTableType()
        {
            return "JAVELIN_LOG";
        }

        /**
         * {@inheritDoc}
         */
        public void truncate(final String database, final int tableIndex, final int year)
            throws SQLException
        {
            JavelinLogDao.truncate(database, tableIndex, year);
        }
    };

    /**
     * ���������s���܂��B
     *
     * @param config {@link DataCollectorConfig} �I�u�W�F�N�g
     * @param clientRepository {@link CommunicationClientRepository} �I�u�W�F�N�g
     * @param signalDefinitionMap 臒l�����`���̃}�b�v
     */
    public JavelinDataLogger(final DataCollectorConfig config,
            final CommunicationClientRepository clientRepository,
            final Map<Long, SignalDefinitionDto> signalDefinitionMap)
    {
        this.rotateConfigMap_ = new HashMap<String, RotateConfig>();
        this.config_ = config;
        this.clientRepository_ = clientRepository;
        SignalStateManager signalStateManager = SignalStateManager.getInstance();
        signalStateManager.setSignalDeifinitionMap(signalDefinitionMap);
    }

    /**
     * ���[�e�[�g�ݒ��ǉ����܂��B
     *
     * @param rotateConfig ���[�e�[�g�ݒ�
     */
    public void addRotateConfig(final RotateConfig rotateConfig)
    {
        this.rotateConfigMap_.put(rotateConfig.getDatabase(), rotateConfig);
    }

    /**
     * �f�t�H���g�̃��[�e�[�g�ݒ��ݒ肵�܂��B
     *
     * @param rotateConfig ���[�e�[�g�ݒ�
     */
    public void setDefaultRotateConfig(final RotateConfig rotateConfig)
    {
        this.defaultRotateConfig_ = rotateConfig;
    }

    /**
     * �A���[���f�[�^������L���[��Ԃ��܂��B
     *
     * @return �L���[
     */
    public JavelinDataQueue getQueue()
    {
        return this.queue_;
    }

    /**
     * {@inheritDoc}
     */
    public void run()
    {
        while (true)
        {
            // �I���`�F�b�N
            if (this.isRunnning_ == false && this.queue_.size() == 0)
            {
                break;
            }

            // �L���[����f�[�^�����o��
            JavelinData data = this.queue_.take();

            if (data != null)
            {
                // �f�[�^�x�[�X�֏�������
                logJavelinData(data);
            }
        }

        LOGGER.log(JAVELIN_DATA_LOGGER_STOPPED);
    }

    /**
     * �X���b�h���~���܂��B<br />
     *
     * �L���[�ɏ������ނׂ��f�[�^���c���Ă���ꍇ�A
     * ���ׂăf�[�^�x�[�X�ɏ�������ł���I�����܂��B<br />
     */
    public synchronized void stop()
    {
        LOGGER.log(JAVELIN_DATA_LOGGER_STOPPING);
        this.isRunnning_ = false;
    }

    /**
     * ���������܂��B<br />
     *
     * @param rotateConfigList �G�[�W�F���g���̃��[�e�[�g�ݒ�̃��X�g
     */
    protected synchronized void init(final List<RotateConfig> rotateConfigList)
    {
        LOGGER.log(JAVELIN_DATA_LOGGER_STARTED);

        this.rotateConfigMap_.clear();
        for (RotateConfig rotateConfig : rotateConfigList)
        {
            addRotateConfig(rotateConfig);
        }

        this.isRunnning_ = true;
    }

    /**
     * Javelin ���O���������݂܂��B<br />
     *
     * @param data Javelin ���O
     */
    private void logJavelinData(final JavelinData data)
    {
        if (data instanceof JavelinConnectionData)
        {
            JavelinConnectionData connectionData = (JavelinConnectionData)data;
            String database = connectionData.getDatabaseName();

            if (connectionData.isConnectionData())
            {
                // �ڑ��C�x���g�̏ꍇ�A
                // �����ێ����Ă����A����̃f�[�^���o�����ɁA�v���l���S��0�̃f�[�^��������B
                this.connectionData_ = connectionData;
            }
            else if (connectionData.isConnectionData() == false)
            {
                // �ؒf�C�x���g�̏ꍇ�A�v���l���S��0�̃f�[�^��������B
                // �������A�ڑ��シ���ɐؒf�����ꍇ�͑O��̃f�[�^�������̂ŁA�����͍s��Ȃ��B
                ResourceData resourceData = this.prevConvertedResourceDataMap_.get(database);
                if (resourceData != null)
                {
                    long measurementTime = connectionData.measurementTime;
                    ResourceData allZeroData =
                                               ResourceDataUtil.createAllZeroResourceData(resourceData,
                                                                                          measurementTime,
                                                                                          false);
                    logResourceData(database, allZeroData, true);
                }
            }
        }
        else if (data instanceof JavelinLogData)
        {
            // Javelin ���O�̏ꍇ
            JavelinLogData logData = (JavelinLogData)data;
            String database = data.getDatabaseName();
            logJavelinLogData(database, logData);

            // Javelin���O�̔��菈��
            alarmJavelinLogData(database, logData);
        }
        else if (data instanceof JavelinMeasurementData)
        {
            // �v���l�f�[�^�̏ꍇ
            ResourceData resourceData = ((JavelinMeasurementData)data).getResourceData();
            String database = data.getDatabaseName();

            if (resourceData != null && resourceData.getMeasurementMap() != null)
            {

                // �ڑ���̍ŏ��̃f�[�^�̏ꍇ�A�ڑ���\���i�v���l���S��0�́j�f�[�^�𒼑O�ɉ�����B
                if (this.connectionData_ != null
                        && resourceData.getMeasurementMap().isEmpty() == false)
                {
                    long measurementTime = this.connectionData_.measurementTime;
                    ResourceData allZeroData =
                                               ResourceDataUtil.createAllZeroResourceData(resourceData,
                                                                                          measurementTime,
                                                                                          true);
                    logResourceData(database, allZeroData, true);
                    this.connectionData_ = null;
                }

                logResourceData(database, resourceData);
            }
        }
    }

    /**
     * JVN�f�[�^�ɑ΂��āA�C�x���g�𑗐M���܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @param logData {@link JavelinLogData}�I�u�W�F�N�g
     */
    void alarmJavelinLogData(final String database, final JavelinLogData logData)
    {
        JavelinLogAccessor accesor = new JavelinLogAccessor() {
            @Override
            public InputStream getInputStream()
                throws IOException
            {
                byte[] jvnLog = logData.getContents().getBytes(JVN_LOG_ENCODING);
                return new ByteArrayInputStream(jvnLog);
            }
        };
        JavelinParser parser = new JavelinParser(accesor);
        try
        {
            parser.init();
        }
        catch (ParseException ex)
        {
            String message = ex.getMessage();
            LOGGER.log(LogMessageCodes.FAIL_PARSE_JVN_DATA, ex, message);
            return;
        }

        while (true)
        {
            try
            {
                JavelinLogElement element = parser.nextElement();
                if (element == null)
                {
                    break;
                }

                // DataCollector���Q�Ƃ���e������Z�b�g����
                element.setIpAddress(logData.getIpAddress());
                element.setPort(logData.getPort());
                element.setDatabaseName(logData.getDatabaseName());
                element.setAlarmThreshold(logData.getAlarmThreshold());
                element.setCpuAlarmThreshold(logData.getCpuAlarmThreshold());
                element.setLogFileName(logData.getLogFileName());

                String clientId = logData.getClientId();
                if (clientId == null || clientId.equals(""))
                {
                    clientId =
                               JavelinClient.createClientId(logData.getIpAddress(),
                                                            logData.getPort());
                }
            }
            catch (IOException ex)
            {
                String message = ex.getMessage();
                LOGGER.log(LogMessageCodes.FAIL_PARSE_JVN_DATA, ex, message);
            }
            catch (ParseException ex)
            {
                String message = ex.getMessage();
                LOGGER.log(LogMessageCodes.FAIL_PARSE_JVN_DATA, ex, message);
            }
        }
    }

    /**
     * �w�肳�ꂽ�f�[�^���f�[�^�x�[�X�ɓo�^���܂��B<br />
     * @param database �f�[�^�x�[�X��
     * @param resourceData �o�^����f�[�^
     */
    private void logResourceData(final String database, final ResourceData resourceData)
    {
        this.logResourceData(database, resourceData, false);
    }

    /**
     * �w�肳�ꂽ�f�[�^���f�[�^�x�[�X�ɓo�^���܂��B<br />
     * @param database �f�[�^�x�[�X��
     * @param resourceData �o�^����f�[�^
     * @param isConnectionData �ڑ��E�ؒf�f�[�^���ǂ���
     */
    private void logResourceData(final String database, final ResourceData resourceData,
            final boolean isConnectionData)
    {
        if (resourceData.getMeasurementMap().isEmpty())
        {
            return;
        }
        RotateConfig rotateConfig = this.rotateConfigMap_.get(database);
        if (rotateConfig == null)
        {
            rotateConfig = defaultRotateConfig_;
        }
        int rotatePeriod = rotateConfig.getMeasureRotatePeriod();
        int rotatePeriodUnit = rotateConfig.getMeasureUnitByCalendar();

        try
        {
            // �ϐ��n��ŐV���ȃf�[�^��������Ă���ꍇ�A�O���t�̎n�܂��\���f�[�^��ǉ�����B
            String prevDataKey = resourceData.clientId;
            ResourceData prevData = this.prevConvertedResourceDataMap_.get(prevDataKey);
            if (prevData != null)
            {
                ResourceData additionalData =
                                              ResourceDataUtil.createAdditionalPreviousData(prevData,
                                                                                            resourceData);

                if (additionalData.getMeasurementMap().size() > 0)
                {
                    insertMeasurementData(database, additionalData, rotatePeriod, rotatePeriodUnit);
                }
            }

            ResourceData convertedResourceData = resourceData;
            if (isConnectionData == false)
            {
                // �ώZ�l�������Ă���ꍇ�A�����ɂ���
                convertedResourceData =
                                        accumulatedValueParser(this.prevResourceDataMap_.get(database),
                                                               resourceData);
            }

            // CPU�g�p�����v�Z���A�f�[�^�ɉ�����B
            this.calculateAndAddCpuUsageData(database, convertedResourceData);

            // �J�o���b�W���v�Z���A�f�[�^�ɉ�����B
            this.calculateAndAddCoverageData(database, convertedResourceData);

            // �����̒l�ɑ΂��āA���l�������邱�ƂŐ��x��ۏ؂���B
            this.convertJmxRatioData(database, convertedResourceData);

            insertMeasurementData(database, convertedResourceData, rotatePeriod, rotatePeriodUnit);

            alarmThresholdExceedance(database, convertedResourceData,
                                     this.prevConvertedResourceDataMap_.get(prevDataKey));

            if (isConnectionData == false)
            {
                if (resourceData.getMeasurementMap() != null
                        && resourceData.getMeasurementMap().size() != 0)
                {
                    this.prevResourceDataMap_.put(database, resourceData);
                    this.prevConvertedResourceDataMap_.put(prevDataKey, convertedResourceData);
                }

                notifyResource(convertedResourceData);

            }
        }
        catch (SQLException ex)
        {
            LOGGER.log(DATABASE_ACCESS_ERROR, ex, ex.getMessage());
        }
    }

    private void insertMeasurementData(final String database,
            final ResourceData convertedResourceData, final int rotatePeriod,
            final int rotatePeriodUnit)
        throws SQLException
    {
        long startTime = System.currentTimeMillis();
        InsertResult result =
                              ResourceDataDaoUtil.insert(database, convertedResourceData,
                                                         rotatePeriod, rotatePeriodUnit,
                                                         config_.getBatchSize(),
                                                         config_.getItemIdCacheSize());
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        if (result.getInsertCount() != 0)
        {
            // IEDC0022=�f�[�^�x�[�X�ɑ���l��o�^���܂����B �f�[�^�x�[�X��:{0}�A�o�ߎ���:{1}�A�o�^����:{2}�A�L���b�V���q�b�g����:{3}�A�L���b�V�����ӂ��:{4}
            int cacheHitCount = result.getInsertCount() - result.getCacheMissCount();
            LOGGER.log("IEDC0022", database, elapsedTime, result.getInsertCount(), cacheHitCount,
                       result.getCacheOverflowCount());
        }

        // TODO ���ۂ̒l���擾���đ�����邱��
        int measurementItemId = 0;
        String measurementItemName = "";
        String lastInserted = "";

        Telegram telegram =
                            createAddTreeNodeTelegram(measurementItemId, measurementItemName,
                                                      lastInserted);
        // TODO �쐬����Telegram��Dashboard��Send���鏈������������
    }

    /**
     * �c���[�m�[�h�ǉ��̓d�����쐬����B
     * 
     * @return
     */
    private Telegram createAddTreeNodeTelegram(final int measurementItemId,
            final String measurementItemName, final String lastInserted)
    {
        Telegram telegram = new Telegram();

        Header requestHeader = new Header();
        requestHeader.setByteTelegramKind(TelegramConstants.BYTE_TELEGRAM_KIND_TREE_DEFINITION);
        requestHeader.setByteRequestKind(TelegramConstants.BYTE_REQUEST_KIND_NOTIFY);

        // �ǉ����ꂽ�c���[�m�[�h���
        Body treeBody = new Body();

        treeBody.setStrObjName(TelegramConstants.OBJECTNAME_TREE_CHANGE);
        treeBody.setStrItemName(TelegramConstants.ITEMNAME_TREE_ADD);
        treeBody.setByteItemMode(ItemType.ITEMTYPE_STRING);

        // �f�[�^�����Z�b�g����
        treeBody.setIntLoopCount(TREE_TELEGRAM_DTO_COUNT);

        String[] treeDefObj =
                              {String.valueOf(measurementItemId), measurementItemName, lastInserted};

        // Value���Z�b�g����
        treeBody.setObjItemValueArr(treeDefObj);

        Body[] requestBodys = {treeBody};

        telegram.setObjHeader(requestHeader);
        telegram.setObjBody(requestBodys);

        return telegram;
    }

    /**
     * ���ڊ����l���v�����Ă���l�ɑ΂��Ĉ��l�������A�ۑ�����B
     * @param database 
     * 
     * @param resourceData �ԊґΏۂ̒l�B
     */
    private void convertJmxRatioData(final String database, final ResourceData resourceData)
        throws SQLException
    {
        // �ϊ��Ώۂ̒l����肷��B
        List<JavelinMeasurementItem> itemList = JavelinMeasurementItemDao.selectAll(database);
        Set<String> jmxTypeSet = new HashSet<String>();
        for (JavelinMeasurementItem item : itemList)
        {
            // jmx�̑����ʂ𔻕ʂ���B
            if (item.itemName.indexOf("/jmx/") >= 0)
            {
                jmxTypeSet.add(item.itemName);
            }
        }

        // jmx���Ȃ���Ή������Ȃ��B
        if (jmxTypeSet.size() == 0)
        {
            return;
        }

        // �ϊ��Ώۂ̒l�����o���B
        // ���l��������B
        for (String itemName : jmxTypeSet)
        {
            Map<String, MeasurementDetail> detailMap = getMultiDetailValue(resourceData, itemName);
            if (detailMap == null)
            {
                continue;
            }

            // ���ꂼ���key�l�ɑ΂��Ċ������������̂����邩�{������B
            Set<Entry<String, MeasurementDetail>> datailEntrySet = detailMap.entrySet();
            for (Entry<String, MeasurementDetail> detailEntry : datailEntrySet)
            {
                String name = detailEntry.getKey();
                boolean isRatio = false;
                for (String ratioKey : JMXManager.JMX_RATIO_ITEMNAME_ARRAY)
                {
                    if (!name.contains(ratioKey))
                    {
                        continue;
                    }

                    isRatio = true;
                    break;
                }

                if (!isRatio)
                {
                    continue;
                }

                MeasurementDetail detail = detailMap.get(name);
                detail.value =
                               String.valueOf(Double.valueOf(detail.value)
                                       * ResourceDataUtil.PERCENT_CONST
                                       * ResourceDataUtil.PERCENTAGE_DATA_MAGNIFICATION);
            }
        }
    }

    private void notifyResource(final ResourceData convertedResourceData)
    {
        Telegram resourceTelegram =
                                    ResourceNotifyAccessor.getResourceTelgram(convertedResourceData);

        String clientId = convertedResourceData.clientId;
        if (clientId == null || clientId.equals(""))
        {
            clientId =
                       JavelinClient.createClientId(convertedResourceData.ipAddress,
                                                    convertedResourceData.portNum);
        }

        this.clientRepository_.sendTelegramToClient(clientId, resourceTelegram);
    }

    /**
     * �w�肳�ꂽ���\�[�X�f�[�^�̒l��p���ăJ�o���b�W���v�Z���A��������̃f�[�^�ɒǉ����܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @param resourceData �o�^����f�[�^
     * @throws SQLException
     */
    private void calculateAndAddCoverageData(final String database, final ResourceData resourceData)
        throws SQLException
    {
        Map<String, Integer> measurementTypeMap = makeMeasurementTypeMap(database);

        // �J�o���b�W�̌v�Z�ɕK�v�Ȓl���擾����B
        long calledMethodCount =
                                 getSingleDetailValue(resourceData, measurementTypeMap,
                                                      Constants.ITEMNAME_CALLEDMETHODCOUNT);
        long convertedMethodCount =
                                    getSingleDetailValue(resourceData, measurementTypeMap,
                                                         Constants.ITEMNAME_CONVERTEDMETHOD);

        // �l���擾�ł��Ȃ��ꍇ�A�f�[�^�̒ǉ��͍s��Ȃ��B
        if (calledMethodCount < 0 || convertedMethodCount < 0)
        {
            return;
        }

        double coverage = 0.0;
        if (convertedMethodCount > 0)
        {
            coverage =
                       (double)calledMethodCount / convertedMethodCount
                               * ResourceDataUtil.PERCENT_CONST;
        }

        // �J�o���b�W�̒l���������f�[�^���쐬����B
        MeasurementData coverageData =
                                       createCpuUsageMeasurementData(measurementTypeMap,
                                                                     Constants.ITEMNAME_COVERAGE,
                                                                     coverage);

        // �쐬�����f�[�^���A���̃f�[�^�̓�����Map�ɒǉ�����B
        resourceData.getMeasurementMap().put(Constants.ITEMNAME_COVERAGE, coverageData);
    }

    /**
     * �w�肳�ꂽ���\�[�X�f�[�^�̒l��p����CPU�g�p�����v�Z���A��������̃f�[�^�ɒǉ����܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @param resourceData �o�^����f�[�^
     * @throws SQLException
     */
    private void calculateAndAddCpuUsageData(final String database, final ResourceData resourceData)
        throws SQLException
    {
        Map<String, Integer> measurementTypeMap = makeMeasurementTypeMap(database);

        // CPU�g�p���̌v�Z�ɕK�v�Ȓl���擾����B
        long processorCount =
                              getSingleDetailValue(resourceData, measurementTypeMap,
                                                   Constants.ITEMNAME_SYSTEM_CPU_PROCESSOR_COUNT);
        long javaUpTime =
                          getSingleDetailValue(resourceData, measurementTypeMap,
                                               Constants.ITEMNAME_JAVAUPTIME);
        long sysCputimeTotal =
                               getSingleDetailValue(resourceData, measurementTypeMap,
                                                    Constants.ITEMNAME_SYSTEM_CPU_USERMODE_TIME);
        long sysCputimeSys =
                             getSingleDetailValue(resourceData, measurementTypeMap,
                                                  Constants.ITEMNAME_SYSTEM_CPU_SYSTEM_TIME);
        long sysCputimeIoWait =
                                getSingleDetailValue(resourceData, measurementTypeMap,
                                                     Constants.ITEMNAME_SYSTEM_CPU_IOWAIT_TIME);

        long procCputimeTotal =
                                getSingleDetailValue(resourceData, measurementTypeMap,
                                                     Constants.ITEMNAME_PROCESS_CPU_TOTAL_TIME);
        long procCputimeSys =
                              getSingleDetailValue(resourceData, measurementTypeMap,
                                                   Constants.ITEMNAME_PROCESS_CPU_SYSTEM_TIME);
        long procCputimeIoWait =
                                 getSingleDetailValue(resourceData, measurementTypeMap,
                                                      Constants.ITEMNAME_PROCESS_CPU_IOWAIT_TIME);

        Map<String, MeasurementData> measurementMap = resourceData.getMeasurementMap();

        // �V�X�e����CPU�g�p���̌v�Z�ɕK�v�Ȓl���擾�ł��Ă���ꍇ�A���̃f�[�^��ǉ�����B
        if (-1 < sysCputimeTotal && -1 < sysCputimeSys && -1 < processorCount && -1 < javaUpTime)
        {
            double sysCpuusageTotal =
                                      ResourceDataUtil.calcCPUUsage(sysCputimeTotal, javaUpTime,
                                                                    processorCount);
            double sysCpuusageSys =
                                    ResourceDataUtil.calcCPUUsage(sysCputimeSys, javaUpTime,
                                                                  processorCount);
            double sysCpuusageIoWait =
                                       ResourceDataUtil.calcCPUUsage(sysCputimeIoWait, javaUpTime,
                                                                     processorCount);

            MeasurementData sysCpuusageTotalData =
                                                   createCpuUsageMeasurementData(measurementTypeMap,
                                                                                 Constants.ITEMNAME_SYSTEM_CPU_TOTAL_USAGE,
                                                                                 sysCpuusageTotal);
            MeasurementData sysCpuusageSysData =
                                                 createCpuUsageMeasurementData(measurementTypeMap,
                                                                               Constants.ITEMNAME_SYSTEM_CPU_SYSTEM_USAGE,
                                                                               sysCpuusageSys);

            MeasurementData sysCpuusageIoWaitData =
                                                    createCpuUsageMeasurementData(measurementTypeMap,
                                                                                  Constants.ITEMNAME_SYSTEM_CPU_IOWAIT_USAGE,
                                                                                  sysCpuusageIoWait);

            measurementMap.put(Constants.ITEMNAME_SYSTEM_CPU_TOTAL_USAGE, sysCpuusageTotalData);
            measurementMap.put(Constants.ITEMNAME_SYSTEM_CPU_SYSTEM_USAGE, sysCpuusageSysData);
            measurementMap.put(Constants.ITEMNAME_SYSTEM_CPU_IOWAIT_USAGE, sysCpuusageIoWaitData);
        }

        // �v���Z�X��CPU�g�p���̌v�Z�ɕK�v�Ȓl���擾�ł��Ă���ꍇ�A���̃f�[�^��ǉ�����B
        if (-1 < procCputimeTotal && -1 < procCputimeSys && -1 < processorCount && -1 < javaUpTime)
        {
            double procCpuusageTotal =
                                       ResourceDataUtil.calcCPUUsage(procCputimeTotal, javaUpTime,
                                                                     processorCount);
            double procCpuusageSys =
                                     ResourceDataUtil.calcCPUUsage(procCputimeSys, javaUpTime,
                                                                   processorCount);

            double procCpuusageIoWait =
                                        ResourceDataUtil.calcCPUUsage(procCputimeIoWait,
                                                                      javaUpTime, processorCount);

            MeasurementData procCpuusageTotalData =
                                                    createCpuUsageMeasurementData(measurementTypeMap,
                                                                                  Constants.ITEMNAME_PROCESS_CPU_TOTAL_USAGE,
                                                                                  procCpuusageTotal);
            MeasurementData procCpuusageSysData =
                                                  createCpuUsageMeasurementData(measurementTypeMap,
                                                                                Constants.ITEMNAME_PROCESS_CPU_SYSTEM_USAGE,
                                                                                procCpuusageSys);
            MeasurementData procCpuusageIoWaitData =
                                                     createCpuUsageMeasurementData(measurementTypeMap,
                                                                                   Constants.ITEMNAME_PROCESS_CPU_IOWAIT_USAGE,
                                                                                   procCpuusageIoWait);

            measurementMap.put(Constants.ITEMNAME_PROCESS_CPU_TOTAL_USAGE, procCpuusageTotalData);
            measurementMap.put(Constants.ITEMNAME_PROCESS_CPU_SYSTEM_USAGE, procCpuusageSysData);
            measurementMap.put(Constants.ITEMNAME_PROCESS_CPU_IOWAIT_USAGE, procCpuusageIoWaitData);
        }
    }

    /**
     * item_name��measurement_type�̑Ή���\���}�b�v�𐶐����A�Ԃ��܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @return item_name���L�[�Ameasurement_type��l�Ƃ����}�b�v
     * @throws SQLException DB�A�N�Z�X���ɔ���������O
     */
    private Map<String, Integer> makeMeasurementTypeMap(final String database)
        throws SQLException
    {
        // measure_info �e�[�u���̒u������
        List<JavelinMeasurementItem> measurementItemList;
        measurementItemList = JavelinMeasurementItemDao.selectAll(database);
        Map<String, Integer> measurementTypeMap = new HashMap<String, Integer>();
        for (JavelinMeasurementItem info : measurementItemList)
        {
            measurementTypeMap.put(info.itemName, info.measurementItemId);
        }
        return measurementTypeMap;
    }

    /**
     * �w�肳�ꂽ���\�[�X�f�[�^�́A�w�肳�ꂽmeasurementType�����f�[�^�̒l��Ԃ��܂��B<br />
     * �������A�f�[�^�������Ă��Ȃ��ꍇ��A�����n��̂��̂��w�肵���ꍇ�́A-1��Ԃ��܂��B<br />
     *
     * @param resourceData ���\�[�X�f�[�^
     * @param measurementTypeMap item_name��measurement_type�̑Ή���\���}�b�v
     * @param itemName itemName
     * @return �w�肳�ꂽ�f�[�^�̒l
     */
    private long getSingleDetailValue(final ResourceData resourceData,
            final Map<String, Integer> measurementTypeMap, final String itemName)
    {
        long value = -1;
        Map<String, MeasurementData> measurementMap = resourceData.getMeasurementMap();
        MeasurementData measurementData = measurementMap.get(itemName);
        if (measurementData != null)
        {
            Map<String, MeasurementDetail> measurementDetailMap;
            measurementDetailMap = measurementData.getMeasurementDetailMap();
            MeasurementDetail measurementDetail;
            measurementDetail = measurementDetailMap.get(MeasurementData.SINGLE_DETAIL_KEY);
            if (measurementDetail != null)
            {
                value = Long.valueOf(measurementDetail.value).longValue();
            }
        }
        return value;
    }

    /**
     * �w�肳�ꂽ���\�[�X�f�[�^�́A�w�肳�ꂽmeasurementType�����f�[�^�̒l��double�^�ŕԂ��܂��B<br />
     * �������A�f�[�^�������Ă��Ȃ��ꍇ��A�����n��̂��̂��w�肵���ꍇ�́A-1��Ԃ��܂��B<br />
     *
     * @param resourceData ���\�[�X�f�[�^
     * @param measurementTypeMap item_name��measurement_type�̑Ή���\���}�b�v
     * @param itemName itemName
     * @return �w�肳�ꂽ�f�[�^�̒l
     */
    private Map<String, MeasurementDetail> getMultiDetailValue(final ResourceData resourceData,
            final String itemName)
    {
        Map<String, MeasurementData> measurementMap = resourceData.getMeasurementMap();
        MeasurementData measurementData = measurementMap.get(itemName);
        Map<String, MeasurementDetail> measurementDetailMap = null;
        if (measurementData != null)
        {
            measurementDetailMap = measurementData.getMeasurementDetailMap();
        }
        return measurementDetailMap;
    }

    /**
     * CPU�g�p���̃f�[�^�ɑ΂��āA�w�肳�ꂽitemName��value��MeasurementDetail�Ƃ��Ď��A<br />
     * MeasurementData�𐶐����A�Ԃ��܂��B<br />
     *
     * @param measurementTypeMap item_name��measurement_type�̑Ή���\���}�b�v
     * @param itemName itemName
     * @param cpuUsage �f�[�^�̒l
     * @return MeasurementData
     */
    private MeasurementData createCpuUsageMeasurementData(
            final Map<String, Integer> measurementTypeMap, final String itemName,
            final double cpuUsage)
    {
        MeasurementDetail measurementDetail = new MeasurementDetail();
        // �����_�ȉ��̒l���ێ����邽�߁A���̔{�����|����B
        measurementDetail.value =
                                  String.valueOf(cpuUsage
                                          * ResourceDataUtil.PERCENTAGE_DATA_MAGNIFICATION);
        measurementDetail.displayName = "";

        MeasurementData measurementData = new MeasurementData();
        measurementData.itemName = itemName;
        Integer typeObj = measurementTypeMap.get(itemName);
        if (typeObj != null)
        {
            measurementData.measurementType = typeObj.intValue();
        }
        else
        {
            measurementData.measurementType = -1;
        }
        measurementData.valueType = TelegramConstants.BYTE_ITEMMODE_KIND_STRING;
        measurementData.getMeasurementDetailMap().put(MeasurementData.SINGLE_DETAIL_KEY,
                                                      measurementDetail);

        return measurementData;
    }

    /**
     * 臒l�̒��߁A�����ŃA���[�����o���܂��B
     * @param database DB��
     * @param currentResourceData �擾�������\�[�X�l�i�ώZ�l�͍����l�ɕϊ��ς݂Ƃ���j
     * @param prevResourceData �O��擾�������\�[�X�l�i�ώZ�l�͍����l�ɕϊ��ς݂Ƃ���j
     */
    private void alarmThresholdExceedance(final String database,
            final ResourceData currentResourceData, final ResourceData prevResourceData)
    {
        SignalStateManager signalStateManager = SignalStateManager.getInstance();
        List<AlarmEntry> alarmEntryList = new ArrayList<AlarmEntry>();

        Map<Long, SignalDefinitionDto> signalDefinitionMap =
                                                             signalStateManager.getSignalDeifinitionMap();

        for (Entry<Long, SignalDefinitionDto> signalDefinitionEntry : signalDefinitionMap.entrySet())
        {
            SignalDefinitionDto signalDefinition = signalDefinitionEntry.getValue();
            String itemName = signalDefinition.getMatchingPattern();
            String signalName = signalDefinition.getSignalName();

            //���݂̃A���[���ʒm�󋵂��擾
            AlarmData currentAlarmData = signalStateManager.getAlarmData(signalName);
            if (currentAlarmData == null)
            {
                currentAlarmData = new AlarmData();
                signalStateManager.addAlarmData(signalName, currentAlarmData);
            }
            AlarmProcessor processor = getAlarmProcessor(signalDefinition);

            if (processor == null)
            {
                continue;
            }
            AlarmEntry alarmEntry =
                                    processor.calculateAlarmLevel(currentResourceData,
                                                                  prevResourceData,
                                                                  signalDefinition,
                                                                  currentAlarmData);

            if (alarmEntry == null)
            {
                continue;
            }
            // DataCollector���Q�Ƃ�������Z�b�g����
            alarmEntry.setIpAddress(currentResourceData.ipAddress);
            alarmEntry.setPort(currentResourceData.portNum);
            alarmEntry.setDatabaseName(database);

            signalStateManager.addAlarmData(itemName, currentAlarmData);

            // �A���[���ʒm����
            if (alarmEntry.isSendAlarm())
            {
                alarmEntryList.add(alarmEntry);
            }
        }

        // 臒l���߃A���[�����N���C�A���g�ɒʒm����B
        if (alarmEntryList != null && alarmEntryList.size() != 0)
        {
            String clientId = currentResourceData.clientId;
            if (clientId == null || clientId.equals(""))
            {
                clientId =
                           JavelinClient.createClientId(currentResourceData.ipAddress,
                                                        currentResourceData.portNum);
            }
            Telegram alarmTelegram = CollectorTelegramUtil.createAlarmTelegram(alarmEntryList);
            this.clientRepository_.sendTelegramToClient(clientId, alarmTelegram);
        }
    }

    /**
     * 臒l���菈�����s���I�u�W�F�N�g���擾����B
     * @param signalDefinitionDto 臒l�����`���
     * @return 臒l���菈�����s���I�u�W�F�N�g
     */
    private AlarmProcessor getAlarmProcessor(final SignalDefinitionDto signalDefinitionDto)
    {
        // ���݂́A臒l���߂̃A���[�����肵���Ȃ����A���胍�W�b�N��ǉ����邽�߂ɁA
        // �����ɂ�SignalDefinitionDto��ݒ肷��B

        String key = "default";
        AlarmProcessor processor = this.processorMap_.get(key);

        if (processor == null)
        {
            processor = new AlarmThresholdProcessor();
            this.processorMap_.put(key, processor);
        }

        return processor;
    }

    /**
     * JVN���O�f�[�^��ۑ����܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @param logData {@link JavelinLogData}�I�u�W�F�N�g
     */
    private void logJavelinLogData(final String database, final JavelinLogData logData)
    {
        RotateConfig rotateConfig = this.rotateConfigMap_.get(database);
        if (rotateConfig == null)
        {
            rotateConfig = defaultRotateConfig_;
        }
        int rotatePeriod = rotateConfig.getMeasureRotatePeriod();
        int rotatePeriodUnit = rotateConfig.getMeasureUnitByCalendar();

        JavelinLog javelinLog = createJavelinLog(logData);
        try
        {
            if (DBManager.isDefaultDb() == false)
            {
                // H2�ȊO�̃f�[�^�x�[�X�̏ꍇ�́A�p�[�e�B�V���j���O�������s��
                Integer tableIndex = ResourceDataDaoUtil.getTableIndexToInsert(javelinLog.endTime);
                Integer prevTableIndex = prevTableIndexMap__.get(database);
                if (tableIndex.equals(prevTableIndex) == false)
                {
                    Timestamp[] range = JavelinLogDao.getLogTerm(database);
                    if (range.length == 2
                            && (range[1] == null || range[1].before(javelinLog.endTime)))
                    {
                        // �O��̑}���f�[�^�ƍ���̑}���f�[�^�ő}����e�[�u�����قȂ�ꍇ�ɁA���[�e�[�g�������s��
                        // �������A���ł�DB�ɓ����Ă���f�[�^�̂����A�ŐV�̃f�[�^�����Â��f�[�^�������Ă����ꍇ�̓��[�e�[�g�������Ȃ�
                        boolean truncateCurrent = (prevTableIndex != null);
                        ResourceDataDaoUtil.rotateTable(database, tableIndex, javelinLog.endTime,
                                                        rotatePeriod, rotatePeriodUnit,
                                                        truncateCurrent,
                                                        this.javelinRotateCallback_);
                        prevTableIndexMap__.put(database, tableIndex);
                    }
                }
            }
            JavelinLogDao.insert(database, javelinLog);
        }
        catch (SQLException ex)
        {
            LOGGER.log(DATABASE_ACCESS_ERROR, ex, ex.getMessage());
        }
        StreamUtil.closeStream(javelinLog.javelinLog);

        // �ꎞ�t�@�C��������ꍇ�͍폜���Ă���
        logData.deleteFile();
    }

    /**
     * Javelin ���O���A�f�[�^�x�[�X�ɏ������ތ`�ɕϊ����܂��B<br />
     *
     * @param javelinLogData Javelin ���O
     * @return �f�[�^�x�[�X�ɏ������� Javelin ���O�I�u�W�F�N�g�B
     *         �z�X�g��񂪎擾�ł��Ȃ��ꍇ�� <code>hostId</code> �� <code>-1</code>
     */
    protected JavelinLog createJavelinLog(final JavelinLogData javelinLogData)
    {
        JavelinLog javelinLog = new JavelinLog();
        javelinLog.logFileName = javelinLogData.getLogFileName();

        javelinLog.javelinLog = createContentInputStream(javelinLogData);

        javelinLog.measurementItemName = javelinLogData.getAgentName();

        BufferedReader reader =
                                StreamUtil.getBufferedReader(createContentInputStream(javelinLogData));
        try
        {
            String line = reader.readLine();
            List<String> elemList = JavelinLogUtil.csvTokenizeHeader(line);

            // �t�@�C����1�s�ڂ���͂��Ċe�푮����ݒ肷��
            JavelinLogUtil.parse(javelinLog, elemList);

            // duration��ǂݍ��ށB
            while ((line = reader.readLine()) != null)
            {
                int beginIndex = line.indexOf(JavelinLogUtil.DURATION_KEY);
                if (beginIndex != 0 || line.length() < JavelinLogUtil.DURATION_KEY.length())
                {
                    continue;
                }
                String durationStr = line.substring(JavelinLogUtil.DURATION_KEY.length());
                JavelinLogUtil.setDuration(javelinLog, durationStr);
                break;
            }
        }
        catch (IOException ex)
        {
            LOGGER.log(IO_EXCEPTION_OCCURED, ex, ex.getMessage());
        }
        finally
        {
            StreamUtil.closeStream(reader);
        }

        return javelinLog;
    }

    /**
     * Javelin ���O���e�� {@link InputStream} �ŕԂ��܂��B<br />
     *
     * @param javelinLogData Javelin ���O
     * @return ���O���e�� {@link InputStream}
     */
    protected InputStream createContentInputStream(final JavelinLogData javelinLogData)
    {
        InputStream is = null;
        String contents = javelinLogData.getContents();
        if (contents != null)
        {
            // Javelin ���O�𕶎���ŕێ����Ă���ꍇ
            is = new ByteArrayInputStream(contents.getBytes());
        }
        else
        {
            // Javelin ���O���t�@�C���ŕێ����Ă���ꍇ
            is = StreamUtil.getStream(javelinLogData.getFile());
        }
        return is;
    }

    /**
     * �f�[�^�x�[�X���𐶐����܂��B<br />
     *
     * @param data Javelin ���O
     * @return �f�[�^�x�[�X��
     */
    protected String createDatabaseName(final JavelinData data)
    {
        final int BUFFER = 32;
        StringBuilder builder = new StringBuilder(BUFFER);
        builder.append("javelin_");
        builder.append(data.getHost());
        builder.append("_");
        builder.append(data.getIpAddress());
        builder.append("_");
        builder.append(data.getPort());
        return builder.toString();
    }

    /**
     * �ώZ�l�œ����Ă���l�������l�ɕϊ��������̂�Ԃ�
     * @param prevResourceData �O��̌v���l
     * @param resourceData ����̌v���l
     *
     * @return �ώZ�l�͍����l�ɕς��A�����łȒl�͂��̂܂܂ɃR�s�[����ResourceData
     */
    private static ResourceData accumulatedValueParser(final ResourceData prevResourceData,
            final ResourceData resourceData)
    {
        ResourceData newResourceData = new ResourceData();
        newResourceData.measurementTime = resourceData.measurementTime;
        newResourceData.hostName = resourceData.hostName;
        newResourceData.ipAddress = resourceData.ipAddress;
        newResourceData.portNum = resourceData.portNum;
        newResourceData.clientId = resourceData.clientId;

        // ResourceData ���A�ώZ�l�͍����l�ɕς��A�����łȒl�͂��̂܂܂ɃR�s�[����
        for (MeasurementData measurementData : resourceData.getMeasurementMap().values())
        {
            MeasurementData newMeasurementData = new MeasurementData();
            newMeasurementData.measurementType = measurementData.measurementType;
            newMeasurementData.itemName = measurementData.itemName;
            newMeasurementData.measurementTime = measurementData.measurementTime;
            newMeasurementData.valueType = measurementData.valueType;
            newMeasurementData.displayName = measurementData.displayName;

            // MeasurementData ���A�ώZ�l�͍����l�ɕς��A�����łȒl�͂��̂܂܂ɃR�s�[����
            for (MeasurementDetail detail : measurementData.getMeasurementDetailMap().values())
            {
                MeasurementDetail newMeasurementDetail = new MeasurementDetail();

                newMeasurementDetail.value = detail.value;
                newMeasurementDetail.displayName = detail.displayName;
                newMeasurementDetail.itemId = detail.itemId;
                newMeasurementDetail.itemName = detail.itemName;
                newMeasurementDetail.itemNum = detail.itemNum;
                newMeasurementDetail.type = detail.type;
                newMeasurementDetail.typeItemName = detail.typeItemName;
                newMeasurementDetail.valueId = detail.valueId;

                // �ώZ�l�͍����l�ɕς���
                if (AccumulatedValuesDefinition.isAccumulatedValue(measurementData.itemName,
                                                                   detail.displayType))
                {
                    long prevMeasurementValue =
                                                getPrevValue(prevResourceData, measurementData,
                                                             detail);
                    long resultValue =
                                       Long.valueOf(detail.value).longValue()
                                               - prevMeasurementValue;
                    if (resultValue < 0)
                    {
                        newMeasurementDetail.value = "0";
                    }
                    else
                    {
                        newMeasurementDetail.value = String.valueOf(resultValue);
                    }
                }
                newMeasurementData.addMeasurementDetail(newMeasurementDetail);
            }
            newResourceData.addMeasurementData(newMeasurementData);
        }
        return newResourceData;
    }

    /**
     * measurementData.measurementType �ɑΉ�����1�O�̌v���l�𓾂�
     * @param prevResourceData 1�O�̌v���l
     * @param measurementData �v���l
     * @param defaultValue 1�O�̌v���l�����݂��Ȃ������ꍇ�̒l
     * @return
     */
    private static long getPrevValue(final ResourceData prevResourceData,
            final MeasurementData measurementData, final MeasurementDetail defaultValue)
    {
        if (prevResourceData == null)
        {
            return Long.valueOf(defaultValue.value).longValue();
        }
        Map<String, MeasurementData> measurementMap = prevResourceData.getMeasurementMap();
        if (measurementMap == null)
        {
            return Long.valueOf(defaultValue.value).longValue();
        }
        MeasurementData prevMeasurementData = measurementMap.get(measurementData.itemName);
        if (prevMeasurementData == null)
        {
            return Long.valueOf(defaultValue.value).longValue();
        }
        Map<String, MeasurementDetail> measurementDetailMap =
                                                              prevMeasurementData.getMeasurementDetailMap();
        if (measurementDetailMap == null)
        {
            return Long.valueOf(defaultValue.value).longValue();
        }
        MeasurementDetail measurementDetail = measurementDetailMap.get(defaultValue.displayName);
        if (measurementDetail == null)
        {
            return Long.valueOf(defaultValue.value).longValue();
        }
        long prevMeasurementValue = Long.valueOf(measurementDetail.value).longValue();
        return prevMeasurementValue;
    }

}
