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
package jp.co.acroquest.endosnipe.web.dashboard.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jp.co.acroquest.endosnipe.common.entity.MeasurementData;
import jp.co.acroquest.endosnipe.common.entity.MeasurementDetail;
import jp.co.acroquest.endosnipe.common.entity.ResourceData;
import jp.co.acroquest.endosnipe.web.dashboard.dto.MeasurementValueDto;
import jp.co.acroquest.endosnipe.web.dashboard.dto.MultipleMeasurementValueDto;
import jp.co.acroquest.endosnipe.web.dashboard.dto.TreeMenuDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.wgp.manager.MessageInboundManager;
import org.wgp.manager.WgpDataManager;
import org.wgp.servlet.WgpMessageInbound;

/**
 * リソース通知を行うクラス。
 *
 * @author fujii
 *
 */
@Service
@Scope("singleton")
public class ResourceSender
{
    /** WgpDataManager */
    @Autowired
    private WgpDataManager wgpDataManager;

    /**
     * コンストラクタです。
     */
    public ResourceSender()
    {
        // Do Nothing.
    }

    /**
     * リソース情報をクライアントに送信する。
     * @param resourceData リソース情報
     */
    public void send(final ResourceData resourceData)
    {
        MessageInboundManager inboundManager = MessageInboundManager.getInstance();
        List<WgpMessageInbound> inboundList = inboundManager.getMessageInboundList();

        for (WgpMessageInbound inbound : inboundList)
        {
            sendWgpData(resourceData, this.wgpDataManager, inbound);
        }
    }

    /**
     * ツリーの状態値をWGP用のデータに変換し、送信する。
     *
     * @param treeMenuDtoList 送信するツリーのデータ
     * @param type データ送信の種類(追加:add, 更新:update, 削除:delete)
     */
    public void send(final List<TreeMenuDto> treeMenuDtoList, final String type)
    {
        sendWgpTreeData(treeMenuDtoList, this.wgpDataManager, type);
    }

    /**
     * WGP用のデータに変換し、送信する。
     *
     * @param resourceData 送信するデータ元
     * @param dataManager WGPオブジェクト
     * @param inbound クライアント
     */
    private void sendWgpData(final ResourceData resourceData,
            final WgpDataManager dataManager, final WgpMessageInbound inbound)
    {
        Set<String> listeners = inbound.getListeners();
        Map<String, MeasurementData> measurementMap = resourceData.getMeasurementMap();
        long measurementTime = resourceData.measurementTime;
        Map<String, MultipleMeasurementValueDto> multiDataMap = new HashMap<String, MultipleMeasurementValueDto>();
        Map<String, MeasurementValueDto> singleDataMap = new HashMap<String, MeasurementValueDto>();
        for (Entry<String, MeasurementData> measurementDataEntry : measurementMap.entrySet())
        {
            MeasurementData measurementData = measurementDataEntry.getValue();
            String measurementItemName = measurementDataEntry.getKey();
            String observateGroupId = searchObservateGroupId(listeners,
                    measurementItemName);
            if (observateGroupId == null)
            {
                continue;
            }
            Map<String, MeasurementDetail> measurementDetailMap = measurementData.getMeasurementDetailMap();

            if (measurementDetailMap.size() == 1)
            {
                // グラフの場合にキーがないのはおかしい。
                Iterator<MeasurementDetail> valIterator = measurementDetailMap.values().iterator();
                MeasurementDetail measurementDetail = valIterator.next();
                String value = String.valueOf(measurementDetail.value);

                String[] measurementArray = null;
                if (observateGroupId.indexOf("|") != -1
                        || observateGroupId.indexOf("(") != observateGroupId.lastIndexOf("("))
                {
                    measurementArray = (observateGroupId.substring(
                            observateGroupId.indexOf("(") + 1,
                            observateGroupId.lastIndexOf(")"))).split("\\|");
                }
                List<String> measurementDataList = new ArrayList<String>();
                if (measurementArray != null)
                {
                    measurementDataList.addAll(Arrays.asList(measurementArray));
                }
                else
                {
                    measurementDataList.add(observateGroupId);
                }
                for (int index = 0; index < measurementDataList.size(); index++)
                {
                    String matchData = measurementDataList.get(index);

                    // 完全一致する場合は、単数系列取得扱いとして、値を返す。
                    // 完全一致しない場合は、複数系列取得扱いとして、値を返す。
                    if (matchData.equals(measurementItemName)
                            && measurementDataList.size() == 1)
                    {
                        MeasurementValueDto singleData = singleDataMap.get(observateGroupId);
                        if (singleData == null)
                        {
                            singleData = new MeasurementValueDto();
                            singleData.setMeasurementItemId(0);
                            singleData.setMeasurementItemName(measurementItemName);
                            singleData.setMeasurementTime(measurementTime);
                            singleData.setMeasurementValue(value);
                            singleDataMap.put(matchData, singleData);
                        }
                        singleData.setMeasurementValue(value);
                    }
                    else
                    {
                        MultipleMeasurementValueDto multiData = multiDataMap.get(observateGroupId);
                        if (multiData == null)
                        {
                            multiData = new MultipleMeasurementValueDto();
                            multiData.setMeasurementTime(measurementTime);
                            multiData.setSearchCondition(observateGroupId);
                            multiDataMap.put(observateGroupId, multiData);
                        }
                        Map<String, String> measurmentDataMap = multiData.getMeasurementValue();
                        measurmentDataMap.put(measurementItemName, value);
                    }
                }
            }
        }

        // 単数系列の送信。
        for (Entry<String, MeasurementValueDto> multiDataEntry : singleDataMap.entrySet())
        {
            MeasurementValueDto valueDto = multiDataEntry.getValue();
            dataManager.setData(valueDto.getMeasurementItemName(),
                    String.valueOf(measurementTime), valueDto);
        }

        // 複数系列の送信。
        for (Entry<String, MultipleMeasurementValueDto> multiDataEntry : multiDataMap.entrySet())
        {
            MultipleMeasurementValueDto multiValueDto = multiDataEntry.getValue();
            Map<String, String> multiValueMap = multiValueDto.getMeasurementValue();
            for (Entry<String, String> unitValueEntry : multiValueMap.entrySet())
            {
                String measurementItemName = unitValueEntry.getKey();
                String measurementValue = unitValueEntry.getValue();
                MeasurementValueDto valueDto = new MeasurementValueDto();

                // 単数系列型にデータを成形し、idを時間+測定項目名として送信する。
                valueDto.setMeasurementItemId(0);
                valueDto.setMeasurementItemName(measurementItemName);
                valueDto.setMeasurementTime(measurementTime);
                valueDto.setMeasurementValue(measurementValue);
                String dataId = String.valueOf(measurementTime) + "_"
                        + measurementItemName;
                dataManager.setData(multiValueDto.getSearchCondition(), dataId,
                        valueDto);
            }
        }
    }

    /**
     * ツリー状態をWGP用のデータに変換し、送信する。
     *
     * @param treeMenuDtoList 送信するデータ元
     * @param dataManager WGPオブジェクト
     * @param type データ送信の種類(追加:add, 更新:update)
     */
    private void sendWgpTreeData(final List<TreeMenuDto> treeMenuDtoList,
            final WgpDataManager dataManager, final String type)
    {
        for (TreeMenuDto treeMenuDto : treeMenuDtoList)
        {
            String signalId = treeMenuDto.getId();
            if ("add".equals(type))
            {
                dataManager.setData("tree", signalId, treeMenuDto);
            }
            else if ("update".equals(type))
            {
                dataManager.updateData("tree", signalId, treeMenuDto);
            }
            else if ("delete".equals(type))
            {
                dataManager.removeData("tree", signalId);
            }
        }
    }

    /**
     * 監視対象グループに計測対象が含まれているかどうか。リスナに保持したグループIDの前方一致で判定する。
     *
     * @param listeners
     *            監視対象リスナ
     * @param itemName
     *            計測項目名
     * @return 監視対象グループに計測対象が含まれている場合true、含まれていない場合falseを返す。
     */
    private String searchObservateGroupId(final Set<String> listeners,
            final String itemName)
    {
        for (String groupId : listeners)
        {
            String[] measurementArray = null;
            if (groupId.indexOf("|") != -1
                    || groupId.indexOf("(") != groupId.lastIndexOf("("))
            {
                measurementArray = (groupId.substring(groupId.indexOf("(") + 1,
                        groupId.lastIndexOf(")"))).split("\\|");
            }

            List<String> measurementDataList = new ArrayList<String>();
            if (measurementArray != null)
            {
                measurementDataList.addAll(Arrays.asList(measurementArray));
            }
            else
            {
                if ((groupId.charAt(0) == '(')
                        && (groupId.charAt((groupId.length() - 1)) == ')'))
                {
                    measurementDataList.add(groupId.substring(
                            groupId.indexOf("(") + 1, groupId.lastIndexOf(")")));
                }
                else
                {
                    measurementDataList.add(groupId);
                }
            }
            for (int index = 0; index < measurementDataList.size(); index++)
            {
                String matchData = measurementDataList.get(index);

                if (checkPattern(matchData, itemName))
                {
                    return groupId;
                }

            }
        }
        return null;
    }

    private boolean checkPattern(final String groupId, final String itemName)
    {
        String pattern = groupId.replace("%", ".*");
        if (itemName.matches(pattern) || itemName.equals(groupId))
        {
            return true;
        }
        return false;
    }

}
