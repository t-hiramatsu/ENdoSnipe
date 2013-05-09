package jp.co.acroquest.endosnipe.report;

/**
 * ログ出力用メッセージを定義する定数クラス
 * 
 * @author kimura
 */
public interface LogIdConstants
{
    /** 例外発生時のログID */
    String EXCEPTION_HAPPENED          = "EERT0001";

    /** レポート出力が停止した際ののログID */
    String REPORT_PUBLISH_STOPPED_WARN = "WERT0002";

    /** レポート生成がキャンセルされた際のログID */
    String REPORT_CANCEL_INFO          = "IERT0003";

    /** コンフィグ読み込み失敗時のログID */
    String READ_FAULT_CONFIG           = "EERT0004";

    /** データ読み込み失敗時のログID */
    String EXCEPTION_IN_READING        = "WERT0005";
}
