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
package jp.co.acroquest.endosnipe.web.explorer.dto;

import java.util.List;

/**
 * リソース一覧のDTO
 * @author kamo
 *
 */
public class DashboardDto
{
    /** ダッシュボードの横幅 */
    private int dashboardWidth_;

    /** ダッシュボードの高さ */
    private int dashboardHeight_;

    /** リソース一覧 */
    private List<ResourceDto> resources_;

    /** 背景 */
    private ResourceDto background_;

    /**
     * コンストラクタ
     */
    public DashboardDto()
    {
    }

    /**
     * リソース一覧を取得する
     * @return リソース一覧
     */
    public List<ResourceDto> getResources()
    {
        return resources_;
    }

    /**
     * リソース一覧を設定する
     * @param resources リソース一覧
     */
    public void setResources(final List<ResourceDto> resources)
    {
        this.resources_ = resources;
    }

    /**
     * ダッシュボードの横幅を取得する
     * @return ダッシュボードの横幅
     */
    public int getDashboardWidth()
    {
        return dashboardWidth_;
    }

    /**
     * ダッシュボードの横幅を設定する
     * @param dashboardWidth ダッシュボードの横幅
     */
    public void setDashboardWidth(final int dashboardWidth)
    {
        dashboardWidth_ = dashboardWidth;
    }

    /**
     * ダッシュボードの高さを取得する
     * @return ダッシュボードの高さ
     */
    public int getDashboardHeight()
    {
        return dashboardHeight_;
    }

    /**
     * ダッシュボードの高さを設定する
     * @param dashboardHeight ダッシュボードの高さ
     */
    public void setDashboardHeight(final int dashboardHeight)
    {
        dashboardHeight_ = dashboardHeight;
    }

    /**
     * 背景を取得する
     * @return 背景
     */
    public ResourceDto getBackground()
    {
        return background_;
    }

    /**
     * 背景を設定する
     * @param background 背景
     */
    public void setBackground(final ResourceDto background)
    {
        background_ = background;
    }

}
