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
import java.util.Arrays;
import java.util.List;

import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.web.dashboard.constants.ResponseConstants;
import jp.co.acroquest.endosnipe.web.dashboard.constants.SignalConstants;
import jp.co.acroquest.endosnipe.web.dashboard.constants.TreeMenuConstants;
import jp.co.acroquest.endosnipe.web.dashboard.controller.SummarySignalController;
import jp.co.acroquest.endosnipe.web.dashboard.dto.ResponseDto;
import jp.co.acroquest.endosnipe.web.dashboard.dto.SummarySignalDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.dto.TreeMenuDto;
import jp.co.acroquest.endosnipe.web.dashboard.manager.EventManager;
import jp.co.acroquest.endosnipe.web.dashboard.manager.ResourceSender;

import org.wgp.manager.WgpDataManager;

public class SummarySignalAllStateListener extends AbstractTelegramListener
{
    /**
     * コンストラクタです。
     */
    public SummarySignalAllStateListener()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Telegram doReceiveTelegram(final Telegram telegram)
    {
        List<TreeMenuDto> summarySignalTreeMenuDtoList = new ArrayList<TreeMenuDto>();

        Body[] resourceBodys = telegram.getObjBody();

        // DataCollectorから送信された電文から、
        // Dashboardのクライアントに通知するためのイベントを作成する。
        Long[] summarySignalId = null;
        String[] treeIds = null;
        String[] childList = null;
        int[] summarySignalState = null;
        String[] errorMessage = null;
        String[] type = null;

        for (Body body : resourceBodys)
        {
            String objectNameInTelegram = body.getStrObjName();
            String itemNameInTelegram = body.getStrItemName();
            if (TelegramConstants.OBJECTNAME_SUMMARY_SIGNAL_AllSTATE.equals(objectNameInTelegram) == false)
            {
                continue;
            }

            int loopCount = body.getIntLoopCount();
            Object[] measurementItemValues = body.getObjItemValueArr();

            if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_TYPE.equals(itemNameInTelegram))
            {
                type = getStringValues(loopCount, measurementItemValues);
            }

            if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_ID.equals(itemNameInTelegram))
            {
                summarySignalId = getLongValues(loopCount, measurementItemValues);
            }

            // 計測IDの項目に対する処理
            if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_NAME.equals(itemNameInTelegram))
            {
                treeIds = getStringValues(loopCount, measurementItemValues);
            }
            // アラームレベルの項目に対する処理
            else if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_CHILDLIST.equals(itemNameInTelegram))
            {
                childList = getStringValues(loopCount, measurementItemValues);
            }
            // アラームレベルの項目に対する処理
            else if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_STATUS.equals(itemNameInTelegram))
            {
                summarySignalState = getIntValues(loopCount, measurementItemValues);
            }
            else if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_ERROR.equals(itemNameInTelegram))
            {
                errorMessage = getStringValues(loopCount, measurementItemValues);
            }

        }
        //        TreeMenuDto summarySignalTree = new TreeMenuDto();
        //        summarySignalTree.setId(treeIds[0]);
        //        summarySignalTree.setTreeId(treeIds[0]);
        //        summarySignalTree.setType("signal");
        //        summarySignalTree.setIcon(SignalConstants.SIGNAL_ICON_STOP);
        //        summarySignalTreeMenuDtoList.add(summarySignalTree);

        for (int cnt = 0; cnt < treeIds.length; cnt++)
        {
            String treeId = treeIds[cnt];

            TreeMenuDto treeMenu = new TreeMenuDto();

            String summarySignalName = treeIds[cnt];

            // 複数グラフ名から親階層のツリーID名を取得する。
            // ※一番右のスラッシュ区切りまでを親階層とする。
            int terminateParentTreeIndex = summarySignalName.lastIndexOf("/");
            String parentTreeId = summarySignalName.substring(0, terminateParentTreeIndex);

            // 複数グラフ表示名を取得する。
            // ※一番右のスラッシュ区切り以降を表示名とする。
            String summarySignalDisplayName =
                    summarySignalName.substring(terminateParentTreeIndex + 1);
            treeMenu.setId(summarySignalName);
            treeMenu.setTreeId(summarySignalName);
            treeMenu.setParentTreeId(parentTreeId);
            treeMenu.setData(summarySignalDisplayName);
            treeMenu.setType(TreeMenuConstants.TREE_MENU_TYPE_SUMMARY_SIGNAL);
            treeMenu.setIcon(SignalConstants.SIGNAL_ICON_STOP);
            summarySignalTreeMenuDtoList.add(treeMenu);

            //            SummarySignalDefinition sumDto = new SummarySignalDefinition();
            //            sumDto.setSummarySignalId(summarySignalId[0]);
            //            sumDto.setSummarySignalName(treeId);
            //            sumDto.setSignalList(Arrays.asList(childList));
            //            sumDto.setSummarySignalStatus(summarySignalState[0]);
            //            sumDto.setErrorMessage(errorMessage[0]);

            SummarySignalDefinitionDto sumDto = new SummarySignalDefinitionDto();
            sumDto.setSummarySignalId(summarySignalId[0].intValue());
            sumDto.setSummarySignalName(treeId);
            sumDto.setSignalList(Arrays.asList(childList));
            sumDto.setSummarySignalStatus(summarySignalState[0]);
            sumDto.setMessage(errorMessage[0]);

            ResponseDto responseDto = new ResponseDto();
            responseDto.setData(sumDto);
            responseDto.setResult(ResponseConstants.RESULT_SUCCESS);

            SummarySignalController.responseData_ = responseDto;
            if (!errorMessage[0].equals(""))
            {
                return null;
            }
        }

        // シグナルの状態更新を通知する。
        EventManager eventManager = EventManager.getInstance();
        WgpDataManager dataManager = eventManager.getWgpDataManager();
        ResourceSender resourceSender = eventManager.getResourceSender();
        if (dataManager == null || resourceSender == null)
        {
            return null;
        }

        if (type[0].equals(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_ADD))
        {
            resourceSender.send(summarySignalTreeMenuDtoList, "add");
        }
        else if (type[0].equals(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_ALL))
        {
            resourceSender.send(summarySignalTreeMenuDtoList, "delete");
        }
        else if (type[0].equals(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_UPDATE))
        {
            resourceSender.send(summarySignalTreeMenuDtoList, "update");
        }
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

    private Long[] getLongValues(final int loopCount, final Object[] telegramValuesOfobject)
    {
        Long[] telegramValues = new Long[loopCount];
        for (int cnt = 0; cnt < telegramValuesOfobject.length; cnt++)
        {
            if (cnt >= loopCount)
            {
                break;
            }
            Long value = (Long)telegramValuesOfobject[cnt];
            telegramValues[cnt] = value.longValue();
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
        return TelegramConstants.BYTE_TELEGRAM_KIND_ADD_STATE_CHANGE_SUMMARYSIGNAL_DEFINITION;
    }

}
