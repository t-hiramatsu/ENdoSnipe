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
package jp.co.acroquest.endosnipe.web.explorer.template.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.web.explorer.template.meta.Resource;
import jp.co.acroquest.endosnipe.web.explorer.template.meta.Template;

/**
 * テンプレートをマップに変換するユーティリティクラス
 * @author kamo
 *
 */
public class TemplateConvertUtil
{

    private static final String KEY_DASHBOARD_WIDTH = "dashboardWidth";

    private static final String KEY_DASHBOARD_HEIGHT = "dashboardHeight";

    private static final String KEY_BACKGROUND = "background";

    private static final String KEY_RESOURCES = "resources";

    protected static final String TEMPLATE_PREFIX = "/default/template";

    protected static final String SLASH = "/";

    private static final String WILDCARD = "%";

    /**
     * テンプレートをマップに変換する
     * @param name テンプレート名
     * @param template テンプレートオブジェクト
     * @return 変換したマップ
     */
    public static Map<String, Object> convert(final String name, final Template template)
    {
        Map<String, Object> result = new HashMap<String, Object>();

        //固定値をセット
        result.put(KEY_DASHBOARD_WIDTH, template.getWidth());
        result.put(KEY_DASHBOARD_HEIGHT, template.getHeight());

        //背景をセット
        Map<String, Object> background =
                BackgroundCounvertUtil.convert(template.getBackground(), template.getWidth(),
                                               template.getHeight());
        result.put(KEY_BACKGROUND, background);

        //リソース群をセット
        List<Map<String, Object>> resources = new ArrayList<Map<String, Object>>();
        for (Resource resource : template.getResources())
        {
            resources.add(ResourceConvertUtil.convert(name, resource));
        }
        result.put(KEY_RESOURCES, resources);

        return result;
    }

    /**
     * 指定したテンプレート名の配下全てを表す正規表現文字列を取得する
     * @param name テンプレート名
     * @return テンプレートの配下全てを表す正規表現文字列
     */
    public static String getTreeName(final String name)
    {
        return TEMPLATE_PREFIX + SLASH + name + SLASH + WILDCARD;
    }
}
