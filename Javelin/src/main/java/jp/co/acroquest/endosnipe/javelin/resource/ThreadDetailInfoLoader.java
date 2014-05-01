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
package jp.co.acroquest.endosnipe.javelin.resource;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.javelin.util.DetailThreadInfo;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * スレッド詳細情報をロードする。
 * 
 * @author eriguchi
 *
 */
public class ThreadDetailInfoLoader implements ResourceLoader
{
    /** Javelinの設定。 */
    private final JavelinConfig config_ = new JavelinConfig();

    /** スレッド城塞情報 */
    private DetailThreadInfo info_;

    /**
     * スレッド詳細情報をロードする。
     */
    public void load()
    {
        if (config_.isResourceThreadBlocked() || config_.isResourceThreadRunnable())
        {
            DetailThreadInfo info = ThreadUtil.getThreadStateCount();
            this.info_ = info;
        }
    }

    /**
     * スレッド詳細情報を取得する。
     * 
     * @return スレッド詳細情報
     */
    public DetailThreadInfo getInfo()
    {
        return info_;
    }
}
