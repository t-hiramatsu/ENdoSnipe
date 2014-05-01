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
package jp.co.acroquest.endosnipe.javelin.resource.jmx;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import javax.management.MalformedObjectNameException;

import jp.co.acroquest.endosnipe.common.config.JavelinConfigUtil;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.javelin.resource.MultiResourceGetter;

/**
 * JMX計測値を取得するためのクラスを可変系列用マップに登録します。
 *
 * @author y_asazuma
 */
public class MBeanCollectorInitializer
{
    /** Javelinで監視するJMXの設定ファイル */
    private static final String JMX_PROP                        = "../conf/jmx.properties";

    /** 設定ファイルのObjectNameを表すキー */
    private static final String PROP_PREFIX_OBJECTNAME          = "objectName.";

    /** 設定ファイルのattributeを表すキー */
    private static final String PROP_PREFIX_ATTRIBUTE           = "attribute.";

    /** 属性(属性名)の位置 */
    private static final int    ATTRIBUTE_NAME_POSITION         = 0;

    /** 属性(項目型)の位置 */
    private static final int    ATTRIBUTE_ITEM_TYPE_POSITION    = 1;

    /**
     * コンストラクタ
     */
    private MBeanCollectorInitializer()
    {
        
    };
    
    /**
     * リソース取得インスタンスをマップに登録します。
     *
     * @param multiResourceMap リソース取得インスタンスを登録するマップ（可変系列用）
     */
    public static void init(Map<String, MultiResourceGetter> multiResourceMap)
    {
        Properties properties = JavelinConfigUtil.loadProperties(JMX_PROP);
        if (properties == null)
        {
            SystemLogger.getInstance().warn(JMX_PROP + " is null.");
            return;
        }

        MBeanMultiResourceGetter getters = new MBeanMultiResourceGetter();
        Enumeration<?> enumetarion = properties.propertyNames();
        while (enumetarion.hasMoreElements())
        {
            String propKey = (String)enumetarion.nextElement();
            String objectStr = properties.getProperty(propKey);

            // PREFIXが"objectName."でない場合は読み飛ばす
            if (propKey.startsWith(PROP_PREFIX_OBJECTNAME) == false)
            {
                continue;
            }

            // 設定ファイルからオブジェクトの定義を取得する
            String objectName = objectStr;

            String id = propKey.substring(PROP_PREFIX_OBJECTNAME.length());
            String attrListStr = properties.getProperty(PROP_PREFIX_ATTRIBUTE + id);

            // 設定ファイルから属性の定義を取得する
            // 変数attrListStrの中身は以下の形式になっている
            //   <attribute n1>,<attribute n2>,...
            String[] attrList = attrListStr.split(",");
            for (String attrStr : attrList)
            {
                String attrName = attrStr;

                // 取得するJMXの計測値の設定情報を出力する
                StringBuilder sb = new StringBuilder();
                sb.append("(JMX mesuerment) ");
                sb.append("ObjectName[").append(objectName).append("] ");
                sb.append("attribute[").append(attrName).append("] ");
                SystemLogger.getInstance().info(sb.toString());

                try
                {
                    // JMXの計測値を取得するクラスを初期化して追加する
                    getters.addMBeanValueGetter(new MBeanValueGetter(objectName, attrName));
                }
                catch (MalformedObjectNameException ex)
                {
                    SystemLogger.getInstance().warn(ex);
                }
            }
        }

        // 可変系列用のリソース取得としてJMXの計測値を登録する
        multiResourceMap.put(TelegramConstants.ITEMNAME_JMX, getters);
    }
}
