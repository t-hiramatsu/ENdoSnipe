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
package jp.co.acroquest.endosnipe.web.explorer.constants;

/**
 * 閾値判定定義情報に関する定数クラスです。
 * @author fujii
 *
 */
public class SignalConstants
{
    /** 成功を表す電文 **/
    public static final int STATE_LEVEL_3 = 3;

    /** 失敗を表す電文 **/
    public static final int STATE_LEVEL_5 = 5;

    /** シグナルアイコン名のヘッダ */
    public static final String SIGNA_ICON_PREFIX = "signal_";

    /** シグナルアイコン名：監視停止中(-1) */
    public static final String SIGNAL_ICON_STOP = "signal_-1";

    /** 操作種別：追加 */
    public static final String OPERATION_TYPE_ADD = "add";

    /** 操作種別：更新 */
    public static final String OPERATION_TYPE_UPDATE = "update";

    /** 操作種別：削除 */
    public static final String OPERATION_TYPE_DELETE = "delete";

    /**
     * インスタンス化を防止するprivateコンストラクタ。
     */
    private SignalConstants()
    {
        // Do Nothing.
    }
}
