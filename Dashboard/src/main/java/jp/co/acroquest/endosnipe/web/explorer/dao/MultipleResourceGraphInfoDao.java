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

import jp.co.acroquest.endosnipe.web.explorer.entity.MultipleResourceGraphInfo;

/**
 * {@link MultipleResourceGraphInfo} のための DAO のインターフェースです。
 *
 * @author pin
 *
 */
public interface MultipleResourceGraphInfoDao
{

    /**
     * Get the all data of MultipleResourceGraph.<br />
     * @return MultipleResourceGraphのリスト
     */
    List<MultipleResourceGraphInfo> selectAll();

    /**
     * Get the MultipleResourceGraph by Id.
     *
     * @param multipleResourceGraphId
     *            MultipleResourceGraph's ID
     * @return MultipleResourceGraph's data
     */
    MultipleResourceGraphInfo selectById(long multipleResourceGraphId);

    /**
     * Get the MultipleResourceGraph by Name.
     *
     * @param multipleResourceGraphName
     *            MultipleResourceGraph's Name
     * @return MultipleResourceGraph's data
     */
    MultipleResourceGraphInfo selectByName(String multipleResourceGraphName);

    /**
     * {@link MultipleResourceGraphInfo} オブジェクトを挿入します。<br />
     *
     * @param multipleResourceGraphInfo
     *            Object to insert
     */
    void insert(final MultipleResourceGraphInfo multipleResourceGraphInfo);

    /**
     * Update the multipleResourceGraph.
     *
     * @param multipleResourceGraphInfo
     *            Object to update
     */
    void update(final MultipleResourceGraphInfo multipleResourceGraphInfo);

    /**
     * Delete the multipleResourceGraph.
     *
     * @param multipleResourceGraphName
     *            Object to delete
     */
    void delete(final String multipleResourceGraphName);

    /**
     * Delete all of data of multipleResourceGraph.<br />
     */
    void deleteAll();

    /**
     * Get the sequence number of multipleResourceGraph.<br />
     * 
     * @param multipleResourceGraphInfo
     *           Object of multipleResourceGraph
     * @return sequence no of multipleResourceGraph
     */
    int selectSequenceNum(final MultipleResourceGraphInfo multipleResourceGraphInfo);
}