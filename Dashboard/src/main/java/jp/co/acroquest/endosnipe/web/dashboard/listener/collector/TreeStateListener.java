/*
 * Copyright (c) 2004-2013 Acroquest Technology Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  Acroquest Technology Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.web.dashboard.listener.collector;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.web.dashboard.dto.TreeMenuDto;
import jp.co.acroquest.endosnipe.web.dashboard.manager.EventManager;
import jp.co.acroquest.endosnipe.web.dashboard.manager.ResourceSender;

import org.wgp.manager.WgpDataManager;

/**
 * ツリーの状態をクライアントに通知するリスナクラスです。
 * 
 * @author miyasaka
 *
 */
public class TreeStateListener extends AbstractTelegramListener
{
    /**
     * コンストラクタです。
     */
    public TreeStateListener()
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Telegram doReceiveTelegram(final Telegram telegram)
    {
        Body[] resourceAlarmBodys = telegram.getObjBody();

        // DataCollectorから送信された電文から、
        // Dashboardのクライアントに通知するためのイベントを作成する。
        String[] dataList = null;
        String[] treeIdList = null;
        String[] parentTreeIdList = null;
        String[] idList = null;
        String[] typeList = null;
        String[] iconList = null;
        String[] measurementUnitList = null;

        for (Body body : resourceAlarmBodys)
        {
            String objectNameInTelegram = body.getStrObjName();
            String itemNameInTelegram = body.getStrItemName();

            if (TelegramConstants.OBJECTNAME_RESOURCEALARM.equals(objectNameInTelegram) == false)
            {
                continue;
            }

            int loopCount = body.getIntLoopCount();

            Object[] measurementItemValues = body.getObjItemValueArr();
            // ツリー表示名の項目に対する処理
            if (TelegramConstants.ITEMNAME_TREE_DATA.equals(itemNameInTelegram))
            {
                dataList = getStringValues(loopCount, measurementItemValues);
            }
            // ツリーIDの項目に対する処理
            else if (TelegramConstants.ITEMNAME_TREE_TREEID.equals(itemNameInTelegram))
            {
                treeIdList = getStringValues(loopCount, measurementItemValues);
            }
            // 親ツリーIDの項目に対する処理
            else if (TelegramConstants.ITEMNAME_TREE_PARENTTREEID.equals(itemNameInTelegram))
            {
                parentTreeIdList = getStringValues(loopCount, measurementItemValues);
            }
            // IDの項目に対する処理
            else if (TelegramConstants.ITEMNAME_TREE_ID.equals(itemNameInTelegram))
            {
                idList = getStringValues(loopCount, measurementItemValues);
            }
            // タイプの項目に対する処理
            else if (TelegramConstants.ITEMNAME_TREE_TYPE.equals(itemNameInTelegram))
            {
                typeList = getStringValues(loopCount, measurementItemValues);
            }
            // アイコンの項目に対する処理
            else if (TelegramConstants.ITEMNAME_TREE_ICON.equals(itemNameInTelegram))
            {
                iconList = getStringValues(loopCount, measurementItemValues);
            }
            // 計測単位の項目に対する処理
            else if (TelegramConstants.ITEMNAME_TREE_MEASUREMENTUNIT.equals(itemNameInTelegram))
            {
                measurementUnitList = getStringValues(loopCount, measurementItemValues);
            }

        }

        // ツリーメニューのDTOオブジェクトのリスト
        List<TreeMenuDto> treeMenuDtoList = new ArrayList<TreeMenuDto>();

        // ツリーメニューのDTOオブジェクトのリストにデータを追加する
        for (int cnt = 0; cnt < treeIdList.length; cnt++)
        {
            TreeMenuDto treeMenu = new TreeMenuDto();
            treeMenu.setData(dataList[cnt]);
            treeMenu.setTreeId(treeIdList[cnt]);
            treeMenu.setParentTreeId(parentTreeIdList[cnt]);
            treeMenu.setId(idList[cnt]);
            treeMenu.setType(typeList[cnt]);
            treeMenu.setIcon(iconList[cnt]);
            treeMenu.setMeasurementUnit(measurementUnitList[cnt]);

            treeMenuDtoList.add(treeMenu);
        }

        // シグナルの状態更新を通知する。
        EventManager eventManager = EventManager.getInstance();
        WgpDataManager dataManager = eventManager.getWgpDataManager();
        ResourceSender resourceSender = eventManager.getResourceSender();
        if (dataManager == null || resourceSender == null)
        {
            return null;
        }

        // TODO 更新のタイプは固定にしているが、状況によって変更できるようにする
        resourceSender.send(treeMenuDtoList, "add");

        return null;
    }

    /**
     * 電文をString型にして返します。
     * @param loopCount ループ回数
     * @param telegramValuesOfobject 電文の値オブジェクト
     * @return String型の電文の値配列
     */
    private String[] getStringValues(final int loopCount, final Object[] telegramValuesOfobject)
    {
        String[] telegramValues = new String[loopCount];
        for (int cnt = 0; cnt < telegramValuesOfobject.length; cnt++)
        {
            if (cnt >= loopCount)
            {
                break;
            }
            String alarmType = (String)telegramValuesOfobject[cnt];
            telegramValues[cnt] = alarmType;
        }
        return telegramValues;
    }

    /**
     * 電文の配列をInteger型にして返します。
     * @param loopCount ループ回数
     * @param telegramValuesOfobject 電文の値オブジェクト
     * @return Integer型の電文の値配列
     */
    private int[] getIntValues(final int loopCount, final Object[] telegramValuesOfobject)
    {
        int[] telegramValues = new int[loopCount];
        for (int cnt = 0; cnt < telegramValuesOfobject.length; cnt++)
        {
            if (cnt >= loopCount)
            {
                break;
            }
            Integer value = (Integer)telegramValuesOfobject[cnt];
            telegramValues[cnt] = value.intValue();
        }
        return telegramValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte getByteRequestKind()
    {
        return TelegramConstants.BYTE_REQUEST_KIND_NOTIFY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte getByteTelegramKind()
    {
        return TelegramConstants.BYTE_TELEGRAM_KIND_TREE_DEFINITION;
    }
}
