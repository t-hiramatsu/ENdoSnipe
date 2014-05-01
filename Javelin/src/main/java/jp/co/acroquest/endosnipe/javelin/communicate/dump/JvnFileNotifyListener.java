package jp.co.acroquest.endosnipe.javelin.communicate.dump;

import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.accessor.JvnFileNotifyAccessor;
import jp.co.acroquest.endosnipe.communicator.accessor.JvnFileNotifyAccessor.JvnFileEntry;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;

/**
 * Jvnログ通知を受け取る。
 * @author eriguchi
 */
public class JvnFileNotifyListener extends AbstractTelegramListener implements TelegramListener
{
    /** タイムアウトに使用するオブジェクト。 */
    private Object timeoutObject_;

    private JvnFileEntry[] entries_;

    /**
     * リスナを作成する。
     * @param timeoutObject タイムアウト待ちをするオブジェクト。
     */
    public JvnFileNotifyListener(Object timeoutObject)
    {
        this.timeoutObject_ = timeoutObject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Telegram doReceiveTelegram(final Telegram telegram)
    {
        this.entries_ = JvnFileNotifyAccessor.getJvnFileEntries(telegram);

        synchronized (this.timeoutObject_)
        {
            this.timeoutObject_.notifyAll();
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
        return TelegramConstants.BYTE_TELEGRAM_KIND_JVN_FILE;
    }

    /**
     * 結果を取得する。
     * 
     * @return 結果のJvnログ。
     */
    public JvnFileEntry[] getEntries()
    {
        return entries_;
    }

}
