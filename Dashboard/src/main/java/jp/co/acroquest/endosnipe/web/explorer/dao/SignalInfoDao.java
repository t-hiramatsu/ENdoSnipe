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
package jp.co.acroquest.endosnipe.web.explorer.dao;

import java.util.List;

import jp.co.acroquest.endosnipe.web.explorer.entity.SignalInfo;

/**
 * {@link SignalInfo} のための DAO のインターフェースです。
 *
 * @author miyasaka
 *
 */
public interface SignalInfoDao
{

    /**
     * 指定されたデータベースのシグナル定義を全て取得します。<br />
     *
     * シグナル定義が登録されていない場合は空のリストを返します。<br />
     *
     * @return シグナル定義のリスト
     */
    List<SignalInfo> selectAll();

    /**
     * シグナル定義情報を取得する。
     *
     * @param signalId
     *            シグナルID
     * @return シグナル定義
     */
    SignalInfo selectById(long signalId);

    /**
     * シグナル定義情報を取得する。
     * @param signalName シグナル名
     * @return シグナル定義
     */
    SignalInfo selectByName(String signalName);

    /**
     * {@link SignalInfo} オブジェクトを挿入します。<br />
     *
     * @param signalInfo
     *            対象オジェクト
     */
    void insert(final SignalInfo signalInfo);

    /**
     * シグナル定義を更新する。
     *
     * @param signalInfo
     *            シグナル定義
     */
    void update(final SignalInfo signalInfo);

    /**
     * 指定されたシグナル名に該当するシグナル情報をDBから削除する。
     *
     * @param signalName
     *            シグナル名
     */
    void delete(final String signalName);

    /**
     * すべてのレコードを削除します。<br />
     */
    void deleteAll();

    /**
     * 直前のシーケンス情報を取得する。<br />
     * 
     * @param signalInfo
     *            シグナル情報
     * @return 直前のシーケンス番号
     */
    int selectSequenceNum(final SignalInfo signalInfo);
}