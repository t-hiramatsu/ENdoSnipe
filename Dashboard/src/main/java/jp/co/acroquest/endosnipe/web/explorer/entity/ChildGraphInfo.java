/*
 * Copyright (c) 2004-2014 Acroquest Technology Co., Ltd. All Rights Reserved.
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
package jp.co.acroquest.endosnipe.web.explorer.entity;

/**
 * 複数グラフのツリー上の子要素を表すオブジェクト。
 * @author fujii
 *
 */
public class ChildGraphInfo
{
    /** 子要素のID */
    public String child;

    /** 孫要素を保持するかどうか。(true:保持する、false:保持しない) */
    public boolean grandChild;

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "ChildGraphInfo [child=" + child + ", grandChild=" + grandChild + "]";
    }
}
