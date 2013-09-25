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
package jp.co.acroquest.endosnipe.javelin.communicate.config;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.javelin.communicate.SimpleENdoSnipeClient;

/**
 * 設定変更クライアント
 * 
 * @author eriguchi
 */
public class ConfigUpdateClient extends SimpleENdoSnipeClient
{

    /** 応答の待ち受けリスナ。 */
    protected UpdatePropertyResponseListener listener_;

    /** コンストラクタ 
     * @param threadName スレッド名
     */
    public ConfigUpdateClient(String threadName)
    {
        super(threadName);
        listener_ = new UpdatePropertyResponseListener(timeoutObject_);
        client_.addTelegramListener(listener_);
    }

    /**
     * 更新を実行する。
     * 実行結果は標準出力に出力する。引数が空のリストの場合は、
     * 全プロパティ情報をサーバから取得して標準出力に出力する。
     * 
     * @param list 更新対象のプロパティのリスト。
     */
    public void update(List<PropertyEntry> list)
    {
        Telegram updateRequest = createUpdateTelegram(list);
        client_.sendTelegram(updateRequest);

        // 応答を待つ。
        if (listener_.getPropertyInfoList() == null)
        {
            try
            {
                synchronized (this.timeoutObject_)
                {
                    this.timeoutObject_.wait(TIMEOUT_MILLIS);
                }
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }

            if (listener_.getPropertyInfoList() == null)
            {
                System.out.println("update timeout");
                return;
            }
        }

        List<PropertyEntry> propertyInfoList = listener_.getPropertyInfoList();

        if (list.size() == 0)
        {
            printResult(propertyInfoList);
        }
        else
        {
            checkResult(list, propertyInfoList);
        }
    }

    /**
     * 更新の実行結果を標準出力に出力する。
     * 
     * @param propertyInfoList 実行結果
     */
    private void printResult(List<PropertyEntry> propertyInfoList)
    {
        for (PropertyEntry entry : propertyInfoList)
        {
            System.out.println(entry.getProperty() + "=" + entry.getCurrentValue());
        }
    }

    /**
     * 実行結果が、期待通りかを確認する。
     * 
     * @param expectedList 期待結果 
     * @param acturalList 実際の結果
     */
    private void checkResult(List<PropertyEntry> expectedList, List<PropertyEntry> acturalList)
    {
        for (PropertyEntry entry : acturalList)
        {
            for (PropertyEntry expected : expectedList)
            {
                if (expected.getProperty().equals(entry.getProperty()))
                {
                    if (expected.getUpdateValue().equals(entry.getCurrentValue()))
                    {
                        System.out.println("" + entry.getProperty() + "=" + entry.getCurrentValue()
                            + "(succeeded)");
                    }
                    else
                    {
                        System.out.println("Update failed. : " + entry.getProperty() + "="
                            + entry.getCurrentValue() + "(failed)");
                    }
                    break;
                }
            }
        }
    }

    /**
     * サーバプロパティ設定更新電文を作成する。
     * 
     * @param propertyList 設定変更したいプロパティのリスト。
     * @return　サーバプロパティ設定更新電文。
     */
    private static Telegram createUpdateTelegram(List<PropertyEntry> propertyList)
    {
        Telegram telegram = new Telegram();
        Header header = new Header();
        header.setByteTelegramKind(TelegramConstants.BYTE_TELEGRAM_KIND_UPDATE_PROPERTY);
        header.setByteRequestKind(TelegramConstants.BYTE_REQUEST_KIND_REQUEST);
        telegram.setObjHeader(header);

        List<Body> updatePropertyList = new ArrayList<Body>();

        for (PropertyEntry property : propertyList)
        {
            String updateProperty = property.getProperty();
            String updateValue = property.getUpdateValue();

            if (updateValue == null || "".equals(updateValue))
            {
                continue;
            }

            Body addParam = new Body();
            addParam.setStrObjName(updateProperty);
            addParam.setStrItemName(updateValue);

            updatePropertyList.add(addParam);
        }

        Body[] updatePropertyArray =
            updatePropertyList.toArray(new Body[updatePropertyList.size()]);
        telegram.setObjBody(updatePropertyArray);

        return telegram;
    }

    /**
     * エントリポイント。
     * 
     * @param args <host> <port> [<propertyKey>=<propertyValue]
     */
    public static void main(String[] args)
    {
        if (args.length < 2)
        {
            System.out.println("usage : <Main Class> <host> <port> [<propertyKey>=<propertyValue]");
            return;
        }

        String hostName = args[0];
        int port;
        try
        {
            port = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException nfe)
        {
            nfe.printStackTrace();
            System.out.println("illegal port number : " + args[1]);
            return;
        }

        ConfigUpdateClient client = new ConfigUpdateClient("ConfigUpdateClient");

        List<PropertyEntry> list = new ArrayList<PropertyEntry>();

        for (int index = 2; index < args.length; index++)
        {
            String[] paramStr = args[index].split("=");
            if (paramStr.length < 2)
            {
                continue;
            }

            PropertyEntry propertyEntry = new PropertyEntry();
            String propertyKey = paramStr[0];
            String propertyValue = paramStr[1];
            propertyEntry.setProperty(propertyKey);
            propertyEntry.setUpdateValue(propertyValue);
            list.add(propertyEntry);
        }

        boolean success = client.connect(hostName, port);
        try
        {
            if (success)
            {
                client.update(list);
            }
        }
        finally
        {
            client.disconnect();
        }
    }
}
