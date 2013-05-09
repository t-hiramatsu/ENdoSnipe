package jp.co.acroquest.endosnipe.web.dashboard.dto;

/**
 * PerfDoctorテーブル用のDtoクラス。
 * 
 * @author miyasaka
 *
 */
public class PerfDoctorTableDto
{

    /**
     * コンストラクタ。
     */
    public PerfDoctorTableDto()
    {

    }

    /** 日付。 */
    private String date_;

    /** 概要。 */
    private String description_;

    /** レベル。 */
    private String level_;

    /** クラス名。 */
    private String className_;

    /** メソッド名。 */
    private String methodName_;

    /** 詳細。 */
    private String detail_;

    /**
     * 日付を取得する。
     * 
     * @return 日付
     */
    public String getDate()
    {
        return date_;
    }

    /**
     * 日付を設定する。
     * 
     * @param date 日付
     */
    public void setDate(final String date)
    {
        this.date_ = date;
    }

    /**
     * 概要を取得する。
     * 
     * @return 概要
     */
    public String getDescription()
    {
        return description_;
    }

    /**
     * 概要を設定する。
     * 
     * @param description 概要
     */
    public void setDescription(final String description)
    {
        this.description_ = description;
    }

    /**
     * レベルを取得する。
     * 
     * @return レベル
     */
    public String getLevel()
    {
        return level_;
    }

    /**
     * レベルを設定する。
     * 
     * @param level レベル
     */
    public void setLevel(final String level)
    {
        this.level_ = level;
    }

    /**
     * クラス名を取得する。
     * 
     * @return クラス名
     */
    public String getClassName()
    {
        return className_;
    }

    /**
     * クラス名を設定する。
     * 
     * @param className クラス名
     */
    public void setClassName(final String className)
    {
        this.className_ = className;
    }

    /**
     * メソッド名を取得する。
     * 
     * @return メソッド名
     */
    public String getMethodName()
    {
        return methodName_;
    }

    /**
     * メソッド名を設定する。
     * 
     * @param methodName メソッド名
     */
    public void setMethodName(final String methodName)
    {
        this.methodName_ = methodName;
    }

    /**
     * 詳細を取得する。
     * 
     * @return 詳細
     */
    public String getDetail()
    {
        return detail_;
    }

    /**
     * 詳細を設定する。
     * 
     * @param detail 詳細
     */
    public void setDetail(final String detail)
    {
        this.detail_ = detail;
    }
}
