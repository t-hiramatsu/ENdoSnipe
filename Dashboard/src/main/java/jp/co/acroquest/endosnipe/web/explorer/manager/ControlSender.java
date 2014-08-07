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
package jp.co.acroquest.endosnipe.web.explorer.manager;

import java.util.HashMap;
import java.util.List;

import jp.co.acroquest.endosnipe.communicator.PropertyEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.wgp.manager.MessageInboundManager;
import org.wgp.manager.WgpDataManager;
import org.wgp.servlet.WgpMessageInbound;

/**
 * コントロールビューへの通知を行うクラス。
 *
 * @author hiramatsu
 *
 */
@Service
@Scope("singleton")
public class ControlSender
{
    /** WgpDataManager */
    @Autowired
    private WgpDataManager wgpDataManager;

    /**
     * コンストラクタです。
     */
    public ControlSender()
    {
        // Do Nothing.
    }

    /**
     * プロパティ情報をクライアントに送信する。
     * @param propertyData プロパティ情報
     * @param agentName 通知元Javelinエージェント名
     */
    public void send(final List<PropertyEntry> propertyData, final String agentName)
    {
        MessageInboundManager inboundManager = MessageInboundManager.getInstance();
        List<WgpMessageInbound> inboundList = inboundManager.getMessageInboundList();

        for (WgpMessageInbound inbound : inboundList)
        {
            sendWgpData(propertyData, this.wgpDataManager, inbound, agentName);
        }
    }

    /**
     * WGP用のデータに変換し、送信する。
     *
     * @param propertyData プロパティ情報
     * @param dataManager WGPオブジェクト
     * @param inbound クライアント
     * @param agentName 通知元Javelinエージェント名
     */
    private void sendWgpData(final List<PropertyEntry> propertyData,
            final WgpDataManager dataManager, final WgpMessageInbound inbound,
            final String agentName)
    {
        String dataGroupId = agentName + "/propertyController";
        dataManager.initDataMap(dataGroupId, new HashMap<String, Object>());
        for (PropertyEntry propertyEntry : propertyData)
        {
            String objectId = propertyEntry.getProperty();
            dataManager.setData(dataGroupId, objectId, propertyEntry);
        }
    }
}
