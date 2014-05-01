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
package jp.co.acroquest.endosnipe.communicator.accessor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.entity.MeasurementData;
import jp.co.acroquest.endosnipe.common.entity.MeasurementDetail;
import jp.co.acroquest.endosnipe.common.entity.ResourceData;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.MeasurementConstants;
import jp.co.acroquest.endosnipe.communicator.entity.RequestBody;
import jp.co.acroquest.endosnipe.communicator.entity.ResponseBody;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;

import org.apache.log4j.Logger;

/**
 * ���\�[�X�ʒm�d���̂��߂̃A�N�Z�T�N���X�ł��B<br />
 * @author fujii
 */
public class ResourceNotifyAccessor implements TelegramConstants, MeasurementConstants
{
    /** �O���t�̌n�񂪂P�� */
    private static final int           SINGLE_RESOURCE        = 1;

    /** �O���t�̌n�񂪂Q�ȏ� */
    private static final int           MULTI_RESOURCE         = 2;

    /** �O���t�n��̓d����\�����ږ��̐ڔ��� */
    private static final String        NAME_POSTFIX           = "-name";

    /** Javelin�̐ݒ�B */
    private static final JavelinConfig CONFIG                 = new JavelinConfig();

    /** �\�����ϊ��}�b�v */
    private static Map<String, String> convMap__              = new HashMap<String, String>(0);

    /** �u���ϐ�����肷�邽�߂̐��K�\��������B */
    private static final Pattern       VAR_PATTERN            =
                                           Pattern.compile("\\$\\{[A-z0-9][A-z0-9_.-]*\\}");

    /** �v�����ږ�(ID)�̐ړ����B��`�t�@�C������擾����������B */
    private static String              prefixTemplate__;

    /** �v�����ږ�(ID)�ɐړ�����t�^���Ȃ����ڂ̑O����v�p�^�[�����X�g�B */
    private static List<String>        noPrefixPatternList__  = new ArrayList<String>();

    static
    {
        prefixTemplate__ = CONFIG.getItemNamePrefix();
        String tmpNoPrefixPatternStr = CONFIG.getItemNameNoPrefixList();
        if (tmpNoPrefixPatternStr != null)
        {
            String[] tmpNoPrefixPatternArr = tmpNoPrefixPatternStr.split(",");
            for (String pattern : tmpNoPrefixPatternArr)
            {
                // �󕶎���A�󔒂݂̂͏��O����
                if (pattern.trim().length() == 0)
                {
                    continue;
                }
                noPrefixPatternList__.add(pattern.trim());
            }
        }
    }

    /**
     * �v���C�x�[�g�R���X�g���N�^
     */
    private ResourceNotifyAccessor()
    {
        // Do Nothing.
    }

    /**
     * {@link ResourceData}�I�u�W�F�N�g���烊�\�[�X�ʒm�̓d�����쐬���܂��B<br />
     *
     * @param resourceData {@link ResourceData}�I�u�W�F�N�g
     * @return �d�����e
     */
    public static Telegram getResourceTelgram(final ResourceData resourceData)
    {
        List<Body> responseBodyList = new ArrayList<Body>();

        // ������ǉ�����B
        Long currentTime = resourceData.measurementTime;
        ResponseBody timeBody = makeTimeBody(currentTime);
        responseBodyList.add(timeBody);

        Map<String, MeasurementData> measurementDataMap = resourceData.getMeasurementMap();
        for (MeasurementData measurementData : measurementDataMap.values())
        {
            String itemName = measurementData.itemName;
            byte itemType = measurementData.valueType;

            // measurementData�̏ڍׂ�ǉ�����B
            Map<String, MeasurementDetail> measurementDetailMap =
                    measurementData.getMeasurementDetailMap();

            List<String> nameList = new ArrayList<String>();
            List<Object> valueList = new ArrayList<Object>();
            for (MeasurementDetail detail : measurementDetailMap.values())
            {
                String value = detail.value;

                if ("".equals(detail.displayName) == false)
                {
                    nameList.add(detail.displayName);
                    valueList.add(value);
                }
                else
                {
                    ResponseBody body = makeResourceResponseBody(itemName, value,
                                                     ItemType.getItemType(itemType));
                    responseBodyList.add(body);
                }
            }

            if (nameList.size() > 0)
            {
                ResponseBody valueBody = makeResourceResponseBody(itemName, valueList,
                                                 ItemType.getItemType(itemType));
                responseBodyList.add(valueBody);

                // DisplayName�Ή�
                List<String> convNameList = new ArrayList<String>(nameList.size());
                for (String name : nameList)
                {
                    // DisplayName���ʐݒ肳��Ă�����K�p
                    if (convMap__.containsKey(name))
                    {
                        convNameList.add(convMap__.get(name));
                    }
                    else
                    {
                        convNameList.add(name);
                    }
                }

                ResponseBody nameBody = makeResourceResponseBody(itemName + "-name",
                                                                 convNameList,
                                                                 ItemType.ITEMTYPE_STRING);
                responseBodyList.add(nameBody);
            }
        }

        Telegram responseTelegram = makeResponseTelegram(responseBodyList);

        return responseTelegram;
    }

    private static ResponseBody makeResourceResponseBody(String itemName, List<?> nameList,
            ItemType itemType)
    {
        // �l��ǉ�����B
        ResponseBody valueBody = new ResponseBody();
        valueBody.setStrObjName(OBJECTNAME_RESOURCE);
        valueBody.setStrItemName(itemName);
        valueBody.setIntLoopCount(nameList.size());
        valueBody.setByteItemMode(itemType);
        valueBody.setObjItemValueArr(nameList.toArray(new Object[nameList.size()]));

        // DisplayName���ʐݒ肳��Ă�����K�p
        if (convMap__.containsKey(itemName))
        {
            valueBody.setStrObjDispName(convMap__.get(itemName));
        }

        return valueBody;
    }

    /**
     * ��M�����d�����烊�\�[�X�f�[�^���쐬���܂��B<br />
     * �d����ʂ����\�[�X�ʒm�d���łȂ��ꍇ��A�v��������ʂ������łȂ��ꍇ�A<br />
     * ���e���s���ł���ꍇ��<code>null</code>��Ԃ��܂��B<br />
     *
     * @param telegram ���\�[�X�ʒm�d��
     * @param dbName �f�[�^�x�[�X��
     * @param agentName �G�[�W�F���g��
     *
     * @return �d������쐬����{@link ResourceData}�I�u�W�F�N�g
     */
    public static ResourceData createResourceData(final Telegram telegram, 
            String dbName, String agentName)
    {
        if (checkTelegramKind(telegram) == false || checkResponseKind(telegram) == false)
        {
            return null;
        }

        Logger logger = Logger.getLogger(ResourceNotifyAccessor.class);
        logger.debug("ResourceNotifyAccessor.createResourceData() begin");

        ResourceData resourceData = new ResourceData();

        Body[] bodies = telegram.getObjBody();
        int bodyMax = bodies.length;
        int bodyCnt = 0;
        while (bodyCnt < bodyMax)
        {
            Body body = bodies[bodyCnt];
            String objectName = body.getStrObjName();
            String itemName = body.getStrItemName();
            if (TIME_RESOURCE.equals(objectName))
            {
                // ���ږ��������̏ꍇ�ɂ͎�����ݒ肵�A�z�X�gID�̎��ɂ̓z�X�gID��ݒ肷��B
                // ����ȊO�̏ꍇ�ɂ́A�v���l����\���Ƃ��āAResourceData�ɒl��ݒ肷��B
                // ���ړ������t���ꍇ��z�肵�āA�����v�Ŋm�F���Ă���
                if (itemName != null && itemName.endsWith(ITEMNAME_TIME))
                {
                    if (body.getIntLoopCount() != 1)
                    {
                        return null;
                    }

                    // �N���C�A���g�̎����ŃO���t�̃f�[�^��ۑ�����B
                    Date date = new Date();
                    long now = date.getTime();
                    resourceData.measurementTime = now;
                }

                // ���Ԃ̍��ڂ����Ȃ��Ɩ������[�v�ɂȂ�̂ŁA�ǂ���ɂ��Ă��J�E���g�A�b�v����
                bodyCnt++;
            }
            else if (OBJECTNAME_RESOURCE.equals(objectName))
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("ResourceNotifyAccessor.createResourceData: " +
                                 "call addMeasurementData([" + bodyCnt + "]:" + itemName + ")");
                }
                bodyCnt = addMeasurementData(resourceData, bodies, bodyCnt, dbName, agentName);
            }
            else
            {
                if (logger.isDebugEnabled())
                {
                    logger.info("ResourceNotifyAccessor.createResourceData: " +
                                "unknown ObjectName: [" + bodyCnt + "]=" + objectName);
                }
                bodyCnt++;
            }
        }
        return resourceData;
    }

    /**
     * {@link ResourceData}�I�u�W�F�N�g�Ɍv������ǉ����܂��B<br />
     *
     * @param resourceData {@link ResourceData}�I�u�W�F�N�g
     * @param bodies �d���{�̂̔z��
     * @param cnt �Ǎ����̓d���ʒu
     * @param dbName �f�[�^�x�[�X��
     *
     * @return ���̓d���ʒu
     */
    private static int addMeasurementData(ResourceData resourceData, Body[] bodies, int cnt,
            String dbName, String agentName)
    {
        Logger logger = Logger.getLogger(ResourceNotifyAccessor.class);

        MeasurementData data = new MeasurementData();

        // �v���l����ǉ�
        Body measurementBody = bodies[cnt];
        String measurementObjName = measurementBody.getStrObjName();
        String measuremnetItemName = agentName + measurementBody.getStrItemName();

        if (OBJECTNAME_RESOURCE.equals(measurementObjName))
        {
            // �v���l�������肷��B
            data.measurementType = 0;
            data.itemName = measuremnetItemName;
            data.valueType = ItemType.getItemTypeNumber(measurementBody.getByteItemMode());
            
            data.displayName = measurementBody.getStrObjDispName();
            if (isSingleResource(bodies, cnt))
            {
                MeasurementDetail measurementDetail = new MeasurementDetail();
                measurementDetail.displayName = "";
                if (measurementBody.getIntLoopCount() == 1)
                {
                    Object[] valueArray = measurementBody.getObjItemValueArr();
                    data.valueType = ItemType.getItemTypeNumber(ItemType.ITEMTYPE_STRING);
                    measurementDetail.value = String.valueOf(valueArray[0]);
                }
                ItemType itemType = measurementBody.getByteItemMode();
                measurementDetail.valueId = ItemType.getItemTypeNumber(itemType);
                data.addMeasurementDetail(measurementDetail);

                if (logger.isDebugEnabled())
                {
                    logger.debug("ResourceNotifyAccessor.addMeasurementData: " +
                                 "addMeasurementData(" + data + ")");
                }

                resourceData.addMeasurementData(data);
                return (cnt + SINGLE_RESOURCE);
            }
            else
            {
                // �v���l�ڍ�(�v���l�A�\����)��ǉ�
                Body nameBody = bodies[cnt + 1];

                Object[] valueArr = measurementBody.getObjItemValueArr();

                String nameObjName = nameBody.getStrObjName();
                Object[] nameArr = null;
                if (OBJECTNAME_RESOURCE.equals(nameObjName))
                {
                    nameArr = nameBody.getObjItemValueArr();
                }
                
                for (int num = 0; num < valueArr.length; num++)
                {
                    String name = (String)nameArr[num];
                    String value = String.valueOf(valueArr[num]);

                    MeasurementDetail detail = new MeasurementDetail();
                    detail.value = String.valueOf(value);
                    detail.displayName = name;
                    data.addMeasurementDetail(detail);
                }
                data.valueType = ItemType.getItemTypeNumber(ItemType.ITEMTYPE_STRING);

                if (logger.isDebugEnabled())
                {
                    logger.debug("ResourceNotifyAccessor.addMeasurementData: " +
                                 "addMeasurementData(" + data + ")");
                }

                resourceData.addMeasurementData(data);
                return (cnt + MULTI_RESOURCE);
            }
        }
        return (cnt + SINGLE_RESOURCE);
    }

    /**
     * �Ǎ����̓d���^���A�P��̃f�[�^�n�������킷���A�����̃f�[�^�n�������킷���B<br />
     * �P��f�[�^�n��ł���ꍇ�́A�ȉ��̂����ꂩ�𖞂����܂��B<br />
     * <ul>
     * <li>�Ǎ����̓d���{�̂��A�z��̍Ō�ł���B</li>
     * <li>�Ǎ����̓d���̎��̓d���̃I�u�W�F�N�g����"resources"�łȂ��B</li>
     * <li>�Ǎ����̓d���̎��̓d���̍��ږ����A"&lt;�Ǎ����d���̍��ږ�&gt;-name"�łȂ��B</li>
     * </ul>
     *
     * @param bodies �d���{�̂̔z��
     * @param cnt �ǂݍ��ݒ��̓d���̔ԍ�
     * @return �P��f�[�^�n��ł���΁A<code>true</code>
     */
    private static boolean isSingleResource(Body[] bodies, int cnt)
    {
        // �Ǎ����̓d���{�̂��A�z��̍Ō�ł���ꍇ
        if (bodies.length <= (cnt + 1))
        {
            return true;
        }
        Body valueBody = bodies[cnt];
        Body nameBody = bodies[cnt + 1];

        // �Ǎ����̓d���̎��̓d���̃I�u�W�F�N�g����"resources"�ł���A
        // �Ǎ����̓d���̎��̓d���̍��ږ����A"<�Ǎ����d���̍��ږ�>-name"�ł���ꍇ
        if (OBJECTNAME_RESOURCE.equals(nameBody.getStrObjName()))
        {
            String valueItemName = valueBody.getStrItemName();
            String nameItemName = nameBody.getStrItemName();
            if ((valueItemName + NAME_POSTFIX).equals(nameItemName))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * �d����ʂ����\�[�X�ʒm�ł��邩�ǂ���<br />
     *
     * @param telegram �d��
     * @return ���\�[�X�ʒm�d���ł���ꍇ�A<code>true</code>
     */
    private static boolean checkTelegramKind(final Telegram telegram)
    {
        Header header = telegram.getObjHeader();
        if (BYTE_TELEGRAM_KIND_RESOURCENOTIFY == header.getByteTelegramKind())
        {
            return true;
        }
        return false;
    }

    /**
     * �v��������ʂ������ł��邩�ǂ���
     * @param telegram �d��
     * @return true:�v��������ʂ������ł���Afalse:�v��������ʂ������łȂ��B
     */
    private static boolean checkResponseKind(final Telegram telegram)
    {
        Header header = telegram.getObjHeader();
        if (BYTE_REQUEST_KIND_RESPONSE == header.getByteRequestKind())
        {
            return true;
        }
        return false;
    }

    /**
     * �v���{�̂��쐬����B
     *
     * @param itemName ���ږ�
     * @return �v���{��
     */
    public static RequestBody makeResourceRequestBody(final String itemName)
    {
        RequestBody requestBody = new RequestBody();
        requestBody.setStrObjName(OBJECTNAME_RESOURCE);
        requestBody.setStrItemName(itemName);
        return requestBody;
    }

    /**
     * �����{�̂��쐬����B
     *
     * @param itemName ���ږ�
     * @param value �l
     * @param itemType ���ڂ̌^
     * @return �v���{��
     */
    public static ResponseBody makeResourceResponseBody(String itemName, Object value,
            ItemType itemType)
    {
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStrObjName(OBJECTNAME_RESOURCE);
        responseBody.setStrItemName(itemName);
        responseBody.setIntLoopCount(1);
        responseBody.setByteItemMode(itemType);
        responseBody.setObjItemValueArr(new Object[]{value});

        // DisplayName���ʐݒ肳��Ă�����K�p
        if (convMap__.containsKey(itemName))
        {
            responseBody.setStrObjDispName(convMap__.get(itemName));
        }

        return responseBody;
    }

    /**
     * �����d�����쐬����B
     * 
     * @param responseBodyList �����d���ɋl�߂�Body�̃��X�g�B
     * @return �쐬���������d��(Telegram)�B
     */
    public static Telegram makeResponseTelegram(List<Body> responseBodyList)
    {
        Header responseHeader = new Header();
        responseHeader.setByteTelegramKind(BYTE_TELEGRAM_KIND_RESOURCENOTIFY);
        responseHeader.setByteRequestKind(BYTE_REQUEST_KIND_RESPONSE);

        Telegram responseTelegram = new Telegram();
        responseTelegram.setObjHeader(responseHeader);

        Body[] objBodies = responseBodyList.toArray(new Body[responseBodyList.size()]);

        // strItemName�ɁA�z�X�g���Ȃǂ̐ړ�����t�^����
        //  - �N���X�^���͋����I�ɕt�^����B
        //  - ���O�ΏۂƂȂ镶����Ŏn�܂���̂ɂ͕t�^���Ȃ�
        //  - �t�^����ړ����̓t�H�[�}�b�g���`�ł���

        String prefix = getPrefixStr();

        for (Body body : objBodies)
        {
            String itemName = body.getStrItemName();
            boolean needPrefix = isPrefixNeeded(itemName);
            if (needPrefix == true)
            {
                String concreteItemName = appendPrefix(prefix, itemName);
                
                // �ړ����ƍ��ږ��̊ԂɃX���b�V��������Ȃ��P�[�X���������
                body.setStrItemName(concreteItemName);
            }
            else
            {
                // �ړ����ƍ��ږ��̊ԂɃX���b�V��������Ȃ��P�[�X���������
                body.setStrItemName(itemName);
            }
        }

        responseTelegram.setObjBody(objBodies);

        return responseTelegram;
    }

    private static String appendPrefix(String prefix, String itemName)
    {
        StringBuilder builder = new StringBuilder(prefix);
        
        if (prefix.length() > 0 && itemName.length() > 0
                && prefix.endsWith("/") == false && itemName.startsWith("/") == false)
        {
            builder.append("/");
        }
        String concreteItemName = builder.append(itemName).toString();
        return concreteItemName;
    }

    /**
     * �ړ�����t�^����K�v�����邩�ǂ������肷��B<br/>
     * javelin.properties�ɒ�`�����A���O�Ώۂ�ItemName�ɑO����v����ꍇ�́A
     * �ړ�����t�^���Ȃ��B
     * 
     * @param itemName ����Ώۂ̍��ږ��́B
     * @return �ړ�����t�^����ꍇ��true�A�t�^���Ȃ��ꍇ��false�B
     */
    private static boolean isPrefixNeeded(String itemName)
    {
        if (itemName == null)
        {
            return false;
        }

        boolean isPrefixNeeded = true;
        for (String pattern : noPrefixPatternList__)
        {
            if (itemName.startsWith(pattern) == true)
            {
                isPrefixNeeded = false;
                break;
            }
        }
        return isPrefixNeeded;
    }

    /**
     * ��`����擾��������������ɁA�ړ����ƂȂ镶����𐶐�����B<br/>
     * <p>
     * ��`�́Ajavelin.properties �� javelin.resource.itemName.prefix �ɋL�q����B
     * </p>
     * <p>
     * ��`���ɂ͒u���ϐ����܂߂邱�Ƃ��ł���B<br/>
     * �u���ϐ��̏����� ${(�V�X�e���v���p�e�B��)} �ł���B
     * �������A�V�X�e���v���p�e�B���Ɏw��ł��镶���͉p�����A
     * ���p�n�C�t���A���p�A���_�[�X�R�A�A���p�s���I�h�݂̂Ƃ���B
     * 
     * @return ���������ړ����B
     */
    private static String getPrefixStr()
    {
        Matcher varMatcher = VAR_PATTERN.matcher(prefixTemplate__);

        // ��`����擾���������񒆂ɁA�ϐ������݂���ꍇ�͒��o����
        List<String> varList = new ArrayList<String>();
        int startIndex = 0;
        while (varMatcher.find(startIndex))
        {
            int beginIndex = varMatcher.start();
            int endIndex = varMatcher.end();
            if (endIndex <= beginIndex + 1)
            {
                startIndex = endIndex;
                continue;
            }
            String varStr = prefixTemplate__.substring(beginIndex + 2, endIndex - 1);
            varList.add(varStr);
            startIndex = endIndex;
        }

        // �ϐ����Ȃ���΂��̂܂܂̕������Ԃ�
        if (varList.size() == 0)
        {
            return prefixTemplate__;
        }

        // �ϐ������́A�V�X�e���v���p�e�B����l���擾���Ēu������
        String tempPrefix = prefixTemplate__;
        for (String propName : varList)
        {
            String propValue = System.getProperty(propName);
            if (propValue != null)
            {
                tempPrefix = tempPrefix.replaceAll("\\$\\{" + propName + "\\}", propValue);
            }
        }

        return tempPrefix;
    }

    /**
     * ���ԍ��ڂ�Body���쐬���܂��B
     * 
     * @param time �����B
     * @return �쐬����Body�B
     */
    public static ResponseBody makeTimeBody(long time)
    {
        Long currentTimeLong = Long.valueOf(time);
        ResponseBody timeBody = new ResponseBody();
        timeBody.setStrObjName(TIME_RESOURCE);
        timeBody.setStrItemName(ITEMNAME_TIME);
        timeBody.setIntLoopCount(1);
        timeBody.setByteItemMode(ItemType.ITEMTYPE_LONG);
        timeBody.setObjItemValueArr(new Object[]{currentTimeLong});
        return timeBody;
    }


    /**
     * �\�����ϊ��}�b�v��ݒ肵�܂��B
     *
     * @param convMap �\�����ϊ��}�b�v
     */
    public static void setConvMap(Map<String, String> convMap)
    {
        convMap__ = convMap;
    }

}
