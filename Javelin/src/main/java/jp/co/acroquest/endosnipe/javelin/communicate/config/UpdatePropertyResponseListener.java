package jp.co.acroquest.endosnipe.javelin.communicate.config;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;

/**
 *  サーバプロパティ設定更新の応答を受信して、結果を保持するリスナ。
 * 
 * @author eriguchi
 */
public class UpdatePropertyResponseListener extends AbstractTelegramListener
{
    private List<PropertyEntry> propertyInfoList_;

    /** タイムアウトに使用するオブジェクト。 */
    private Object timeoutObject_;

    /**
     * コンストラクタ。
     * 
     * @param timeoutObject 呼び出し元との待ち合わせに使うオブジェクト。
     */
    public UpdatePropertyResponseListener(Object timeoutObject)
    {
        this.timeoutObject_ = timeoutObject;
    }

    /**
     * 受信したプロパティ設定のリストを取得する。
     * 
     * @return 受信したプロパティ設定のリスト。未受信の場合、nullを返す。
     */
    public List<PropertyEntry> getPropertyInfoList()
    {
        return propertyInfoList_;
    }

    /**
     * サーバプロパティ設定更新の応答を受信して、結果を保持する。
     *　@param telegram 応答電文。 
     * @return nullを返す。
     */
    @Override
    protected Telegram doReceiveTelegram(Telegram telegram)
    {
        this.propertyInfoList_ = parsePropertyInfoList(telegram);

        synchronized (this.timeoutObject_)
        {
            this.timeoutObject_.notifyAll();
        }
        return null;
    }

    /**
     * 要求応答種別（応答）を返す。
     * @return 要求応答種別（応答）。
     */
    @Override
    protected byte getByteRequestKind()
    {
        return TelegramConstants.BYTE_REQUEST_KIND_RESPONSE;
    }

    /**
     * サーバプロパティ設定更新の種別を返す。
     * @return サーバプロパティ設定更新の種別。
     */
    @Override
    protected byte getByteTelegramKind()
    {
        return TelegramConstants.BYTE_TELEGRAM_KIND_UPDATE_PROPERTY;
    }

    /**
     * 電文からテーブルに表示するプロパティ情報を作成する
     * 
     * @param telegram 受信電文
     * @return 表示するプロパティ情報
     */
    List<PropertyEntry> parsePropertyInfoList(final Telegram telegram)
    {
        List<PropertyEntry> propertyInfoList = new ArrayList<PropertyEntry>();

        // 受信電文からテーブルに表示するデータを作成する
        Body[] bodyList = telegram.getObjBody();

        if (bodyList == null)
        {
            return propertyInfoList;
        }

        for (Body body : bodyList)
        {
            String propertyName = body.getStrObjName();
            String propertyDetail = "";
            String propertyValue = body.getStrItemName();

            if (propertyName == null || "".equals(propertyName))
            {
                continue;
            }

            PropertyEntry entry = new PropertyEntry();
            entry.setProperty(propertyName);
            entry.setPropertyDetail(propertyDetail);
            entry.setCurrentValue(propertyValue);
            propertyInfoList.add(entry);
        }
        return propertyInfoList;
    }
}
