/*
 * Copyright (c) 2004-2015 Acroquest Technology Co., Ltd. All Rights Reserved.
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
package jp.co.acroquest.endosnipe.web.explorer.manager;

import java.util.HashMap;
import java.util.Map;

import jp.co.acroquest.endosnipe.web.explorer.entity.ClassModel;

/**
 * プロファイラのデータを管理するシングルトンクラスです。
 * @author fujii
 *
 */
public class ProfilerManager
{
    /** シングルトンインスタンス */
    private static ProfilerManager instance__ = new ProfilerManager();

    /** プロファイラデータを保持するマップ */
    private final Map<String, ClassModel[]> classModelMap_ = new HashMap<String, ClassModel[]>();

    /**
     * インスタンス化を阻止するプライベートコンストラクタです。
     */
    private ProfilerManager()
    {
        // Do Nothing.
    }

    /**
     * シングルトンインスタンスを取得します。
     * @return {@link ProfilerManager}オブジェクト
     */
    public static ProfilerManager getInstance()
    {
        return instance__;
    }

    /**
     * プロファイルのデータを設定する。
     * @param agentName エージェント名(クラスタ名/ホスト名/エージェント名)
     * @param classModels プロファイルのデータ
     */
    public synchronized void setProfilerData(final String agentName, final ClassModel[] classModels)
    {
        classModelMap_.put(agentName, classModels);
    }

    /**
     * プロファイラデータを取得する。
     * @param agentName エージェント名(クラスタ名/ホスト名/エージェント名)
     * @return プロファイルのデータ
     */
    public synchronized ClassModel[] getProfilerData(final String agentName)
    {
        return classModelMap_.get(agentName);
    }
}
