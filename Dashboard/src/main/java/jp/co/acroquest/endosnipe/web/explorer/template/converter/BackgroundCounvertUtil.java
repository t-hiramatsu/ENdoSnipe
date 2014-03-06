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

import jp.co.acroquest.endosnipe.web.explorer.template.meta.Background;
import jp.co.acroquest.endosnipe.web.explorer.template.meta.Resource;

/**
 * 背景要素をMapに変換するユーティリティクラス
 * @author kamo
 *
 */
public class BackgroundCounvertUtil extends ResourceConvertUtil
{
    private static final String VALUE_OBJECT_ID = "background";

    private static final String VALUE_OBJECT_NAME = Resource.OBJ_NAME_BACKGROUND;

    private static final int VALUE_X = 0;

    private static final int VALUE_Y = 0;

    private static final int VALUE_DEPTH = 0;

    private static final String VALUE_SHAPE_NAME = "rectangle";

    private static final String KEY_SRC = "src";

    /**
     * Background型データを、情報が追加されたMapに変換する
     * @return　変換したマップ
     */
    public static Map<String, Object> convert(final Background background, final Integer width,
            final Integer height)
    {
        Map<String, Object> result = new HashMap<String, Object>();

        //固定値のセット
        result.put(KEY_OBJECT_ID, VALUE_OBJECT_ID);
        result.put(KEY_OBJECT_NAME, VALUE_OBJECT_NAME);
        result.put(KEY_X, VALUE_X);
        result.put(KEY_Y, VALUE_Y);
        result.put(KEY_DEPTH, VALUE_DEPTH);
        result.put(KEY_SHAPE_NAME, VALUE_SHAPE_NAME);
        result.put(KEY_SHAPE_TYPE, VALUE_SHAPE_TYPE);

        result.put(KEY_W, width);
        result.put(KEY_H, height);

        //テンプレートからの読み込み値をセット
        List<Map<String, Object>> attr = new ArrayList<Map<String, Object>>();
        Map<String, Object> property = new HashMap<String, Object>();

        String objectType = background.getObjectType();
        property.put(KEY_OBJECT_TYPE, objectType);

        if (objectType.equals(Background.OBJECT_TYPE_POLYGON))
        {
            property.put(KEY_FILL, background.getFill());
        }
        else if (objectType.equals(Background.OBJECT_TYPE_IMAGE))
        {
            property.put(KEY_SRC, background.getSrc());
        }
        attr.add(property);
        result.put(KEY_PROPERTIES, attr);

        return result;
    }
}
