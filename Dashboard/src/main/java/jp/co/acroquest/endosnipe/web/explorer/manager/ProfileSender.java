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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import jp.co.acroquest.endosnipe.web.explorer.entity.ClassModel;
import jp.co.acroquest.endosnipe.web.explorer.entity.MethodModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.wgp.manager.MessageInboundManager;
import org.wgp.manager.WgpDataManager;
import org.wgp.servlet.WgpMessageInbound;

/**
 * プロファイル通知を行うクラス。
 *
 * @author hiramatsu
 *
 */
@Service
@Scope("singleton")
public class ProfileSender
{
    /** WgpDataManager */
    @Autowired
    private WgpDataManager wgpDataManager;

    /**
     * コンストラクタです。
     */
    public ProfileSender()
    {
        // Do Nothing.
    }

    /**
     * プロファイル情報をクライアントに送信する。
     * @param profileData プロファイル情報
     */
    public void send(final ClassModel[] profileData, final String agentName)
    {
        MessageInboundManager inboundManager = MessageInboundManager.getInstance();
        List<WgpMessageInbound> inboundList = inboundManager.getMessageInboundList();

        for (WgpMessageInbound inbound : inboundList)
        {
            sendWgpData(profileData, this.wgpDataManager, inbound, agentName);
        }
    }

    /**
     * WGP用のデータに変換し、送信する。
     *
     * @param profileData 送信するデータ元
     * @param dataManager WGPオブジェクト
     * @param inbound クライアント
     */
    private void sendWgpData(final ClassModel[] profileData, final WgpDataManager dataManager,
            final WgpMessageInbound inbound, final String agentName)
    {
        String dataGroupId = agentName + "/profiler";
        dataManager.initDataMap(dataGroupId, new HashMap<String, Object>());
        for (ClassModel classModel : profileData)
        {
            for (MethodModel methodModel : classModel.getMethods())
            {
                String objectId =
                        classModel.getFullClassName() + "##" + methodModel.getMethodName();
                dataManager.setData(dataGroupId, objectId, methodModel.convertToDto());
                //                if (dataManager.getData(dataGroupId, objectId) == null)
                //                {
                //                    dataManager.setData(dataGroupId, objectId, methodModel.convertToDto());
                //                }
                //                else
                //                {
                //                    dataManager.updateData(dataGroupId, objectId, methodModel.convertToDto());
                //                }
            }
        }

        //        MeasurementValueDto dto = new MeasurementValueDto();
        //        dto.setMeasurementItemId(123);
        //        dto.setMeasurementItemName("aaa");
        //        dto.setMeasurementTime(1000);
        //        dto.setMeasurementValue("aaa");
        //        MeasurementValueDto dto2 = new MeasurementValueDto();
        //        dto2.setMeasurementItemId(123);
        //        dto2.setMeasurementItemName("aaa");
        //        dto2.setMeasurementTime(1000);
        //        dto2.setMeasurementValue("aaa");
        //        List<MeasurementValueDto> dtoes = new ArrayList<MeasurementValueDto>();
        //        dtoes.add(dto);
        //        dtoes.add(dto2);
        //        idBuilder.setLength(idBuilder.length() - 2);
        //        Map map = new HashMap();
        //        map.put("dto", dto);
        //        map.put("dto2", dto2);
        //        dataManager.setData("profiler", idBuilder.toString(), dtoes);
    }

    /**
     * 計測対象が含まれている監視対象グループを全て取得する。
     *
     * @param listeners
     *            監視対象リスナ
     * @param itemName
     *            計測項目名
     * @return 計測対象が含まれている監視対象グループのリスト
     */
    private List<String> searchObservateGroupIdList(final Set<String> listeners,
            final String itemName)
    {
        List<String> groupIdList = new ArrayList<String>();
        for (String groupId : listeners)
        {
            // 正規表現で一致する場合、または完全一致する場合
            if (itemName.matches(groupId) || itemName.equals(groupId))
            {
                groupIdList.add(groupId);

            }
        }
        return groupIdList;
    }
}
