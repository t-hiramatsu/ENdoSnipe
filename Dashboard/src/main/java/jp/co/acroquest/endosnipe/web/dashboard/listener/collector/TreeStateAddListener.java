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
import jp.co.acroquest.endosnipe.web.dashboard.manager.ResourceSender;

/**
 * listener class for tree node adding
 * 
 * @author heinminn
 *
 */
public class TreeStateAddListener extends AbstractTelegramListener
{

    /**
     * constructor of this class
     */
    public TreeStateAddListener()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Telegram doReceiveTelegram(final Telegram telegram)
    {
        System.out.println("hello TreeAddListener");
        Body[] resourceAlarmBodys = telegram.getObjBody();

        //EventManager eventManager = EventManager.getInstance();
        ResourceSender resourceSender = new ResourceSender();
        System.out.println(resourceSender);
        for (Body body : resourceAlarmBodys)
        {
            List<TreeMenuDto> treeMenuDtoList = new ArrayList<TreeMenuDto>();
            String itemNameInTelegram = body.getStrItemName();
            System.out.println(itemNameInTelegram);
            if (TelegramConstants.ITEMNAME_TREE_ADD.equals(itemNameInTelegram))
            {
                Object[] measurementItemValues = body.getObjItemValueArr();
                for (Object itemValues : measurementItemValues)
                {
                    TreeMenuDto treeMenuDto = new TreeMenuDto();
                    String measurementItemName = (String)itemValues;
                    System.out.println(measurementItemName);
                    int tempIndex = measurementItemName.lastIndexOf("/");

                    String treeId = measurementItemName;
                    String parentTreeId = measurementItemName.substring(0, tempIndex);
                    String data =
                            measurementItemName.substring(tempIndex, measurementItemName.length());
                    String type = "target";
                    String icon = "leaf";

                    treeMenuDto.setId(treeId);
                    treeMenuDto.setData(data);
                    treeMenuDto.setIcon(icon);
                    treeMenuDto.setParentTreeId(parentTreeId);
                    treeMenuDto.setTreeId(treeId);
                    treeMenuDto.setType(type);

                    treeMenuDtoList.add(treeMenuDto);
                }

                String type = "add";

                resourceSender.send(treeMenuDtoList, type);
            }

        }

        return null;
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
        return TelegramConstants.BYTE_TELEGRAM_KIND_TREE_ADD_DEFINITION;
    }
}
