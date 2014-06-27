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
package jp.co.acroquest.endosnipe.web.explorer.listener.collector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.web.explorer.constants.TreeMenuConstants;
import jp.co.acroquest.endosnipe.web.explorer.dto.SummarySignalDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.TreeMenuDto;
import jp.co.acroquest.endosnipe.web.explorer.manager.EventManager;
import jp.co.acroquest.endosnipe.web.explorer.manager.ResourceSender;
import jp.co.acroquest.endosnipe.web.explorer.util.TreeMenuUtil;

import org.wgp.manager.WgpDataManager;

/**
 * Accept telegram from the DataCollector to Explorer
 * @author pin
 *
 */
public class SummarySignalStateChangeListener extends AbstractTelegramListener
{
    /**
     * コンストラクタです。
     */
    public SummarySignalStateChangeListener()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Telegram doReceiveTelegram(final Telegram telegram)
    {
        List<TreeMenuDto> summarySignalTreeMenuDtoList = new ArrayList<TreeMenuDto>();
        List<SummarySignalDefinitionDto> summarySignalDtoList =
                new ArrayList<SummarySignalDefinitionDto>();
        Body[] resourceBodys = telegram.getObjBody();
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
            if (TelegramConstants.OBJECTNAME_SUMMARY_SIGNAL_CHANGE.equals(objectNameInTelegram) == false)
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
            if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_NAME.equals(itemNameInTelegram))
            {
                treeIds = getStringValues(loopCount, measurementItemValues);
            }
            else if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_CHILDLIST.equals(itemNameInTelegram))
            {
                childList = getStringValues(loopCount, measurementItemValues);
            }
            else if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_STATUS.equals(itemNameInTelegram))
            {
                summarySignalState = getIntValues(loopCount, measurementItemValues);
            }
            else if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_ERROR.equals(itemNameInTelegram))
            {
                errorMessage = getStringValues(loopCount, measurementItemValues);
            }
        }

        for (int cnt = 0; cnt < treeIds.length; cnt++)
        {
            String treeId = treeIds[cnt];
            TreeMenuDto treeMenu = new TreeMenuDto();
            String summarySignalName = treeIds[cnt];
            int terminateParentTreeIndex = summarySignalName.lastIndexOf("/");
            String parentTreeId = summarySignalName.substring(0, terminateParentTreeIndex);
            String summarySignalDisplayName =
                    summarySignalName.substring(terminateParentTreeIndex + 1);
            treeMenu.setId(TreeMenuUtil.getCannonicalId(summarySignalName));
            treeMenu.setTreeId(summarySignalName);
            treeMenu.setParentTreeId(parentTreeId);
            treeMenu.setData(summarySignalDisplayName);
            treeMenu.setType(TreeMenuConstants.TREE_MENU_TYPE_SUMMARY_SIGNAL);
            treeMenu.setMessage(errorMessage[cnt]);
            treeMenu.setIcon("signal_" + summarySignalState[cnt]);

            summarySignalTreeMenuDtoList.add(treeMenu);
            SummarySignalDefinitionDto sumDto = new SummarySignalDefinitionDto();
            sumDto.setSummarySignalId(summarySignalId[cnt].intValue());
            sumDto.setSummarySignalName(treeId);
            sumDto.setSignalList(Arrays.asList(childList));
            sumDto.setSummarySignalStatus(summarySignalState[cnt]);
            sumDto.setMessage(errorMessage[cnt]);
            summarySignalDtoList.add(sumDto);
        }

        EventManager eventManager = EventManager.getInstance();
        WgpDataManager dataManager = eventManager.getWgpDataManager();
        ResourceSender resourceSender = eventManager.getResourceSender();
        if (dataManager == null || resourceSender == null)
        {
            return null;
        }
        if (type != null && type.length > 0)
        {
            if (type[0].equals(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_ADD))
            {
                resourceSender.send(summarySignalTreeMenuDtoList, "add");
            }
            else if (type[0].equals(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_DELETE))
            {
                if (summarySignalTreeMenuDtoList.size() > 1)
                {
                    List<TreeMenuDto> deleteSummarySignalTreeMenuDtoList =
                            new ArrayList<TreeMenuDto>();
                    deleteSummarySignalTreeMenuDtoList.add(summarySignalTreeMenuDtoList.get(0));

                    resourceSender.send(deleteSummarySignalTreeMenuDtoList, "delete");
                    summarySignalTreeMenuDtoList.remove(0);
                    resourceSender.send(summarySignalTreeMenuDtoList, "update");
                }
                else
                {
                    resourceSender.send(summarySignalTreeMenuDtoList, "delete");
                }
            }
            else if (type[0].equals(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_UPDATE)
                    || type[0].equals(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_CHANGE_STATE))
            {
                resourceSender.send(summarySignalTreeMenuDtoList, "update");
            }
            else if (type[0].equals(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_ALL))
            {
                resourceSender.send(summarySignalTreeMenuDtoList, "update");
            }
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

    /**
     * 電文の配列をLong型にして返します。
     * @param loopCount ループ回数
     * @param telegramValuesOfobject 電文の値オブジェクト
     * @return Long型の電文の値配列
     */
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
