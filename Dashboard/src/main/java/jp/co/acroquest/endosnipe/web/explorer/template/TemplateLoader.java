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
package jp.co.acroquest.endosnipe.web.explorer.template;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXB;

import jp.co.acroquest.endosnipe.web.explorer.template.meta.Resource;
import jp.co.acroquest.endosnipe.web.explorer.template.meta.Template;

/**
 * XML形式のテンプレート定義ファイルを読み込む
 * @author kamo
 *
 */
public class TemplateLoader
{
    /**
     * テンプレート定義ファイルのディレクトリ内全てのテンプレートを読み込む
     * @return keyをファイル名、valueをテンプレートデータとするマップ
     */
    public Map<String, Template> loadAll(final String dirPath)
    {
        Map<String, Template> templates = new HashMap<String, Template>();
        File dir = new File(dirPath);

        for (String path : dir.list())
        {
            Template template = load(dirPath + File.separator + path);
            templates.put(path, template);
        }

        return templates;
    }

    /**
     * パスを指定してテンプレート定義ファイルを読み込む
     * @param path テンプレート定義ファイルのパス
     * @return テンプレートデータ
     */
    public Template load(final String path)
    {
        File file = new File(path);
        Template template = JAXB.unmarshal(file, Template.class);

        //背景が空のときはnullを設定する
        if (template.getBackground().isEmpty())
        {
            template.setBackground(null);
        }

        //リソースが空のときは空のListを設定する
        List<Resource> resources = template.getResources();
        if (resources == null)
        {
            template.setResources(new ArrayList<Resource>());
        }

        return template;
    }
}
