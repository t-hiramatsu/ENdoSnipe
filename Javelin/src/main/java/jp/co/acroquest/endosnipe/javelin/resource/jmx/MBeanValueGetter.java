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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import jp.co.acroquest.endosnipe.common.entity.DisplayType;
import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.entity.ResourceItem;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;

/**
 * 指定された名前のMBeanから計測値を取得するクラスです。
 *
 * @author y_asazuma
 */
public class MBeanValueGetter
{
    private MBeanServer           server_;

    private MBeanServerConnection connection_;

    /** JMXのObjectNameオブジェクト */
    private final ObjectName      objectName_;

    /** JMXのattributeに指定する文字列 */
    private final String          attribute_;

    /** オブジェクト名のリスト */
    private List<ObjectName>      objectNameList_;

    /** 前回実行時のMBean数 */
    private Integer               lastMBeanCount_;

    /** リモート接続を行うかどうかを表すフラグ */
    private boolean               remoteEnable_;

    /** ID */
    private String                id_;

    /** 数値を表す型のセット */
    private static Set<String>    numberTypeSet__;

    static
    {
        numberTypeSet__ = new CopyOnWriteArraySet<String>();
        numberTypeSet__.add(Short.class.getName());
        numberTypeSet__.add(Integer.class.getName());
        numberTypeSet__.add(Long.class.getName());
        numberTypeSet__.add(Float.class.getName());
        numberTypeSet__.add(Double.class.getName());
    }

    /**
     * JMXの計測値を取得するためのクラスを初期化します。
     *
     * @param server {@link MBeanServer}オブジェクト（ローカル接続の場合のみ指定）
     * @param connection {@link MBeanServerConnection}オブジェクト（リモート接続の場合のみ指定）
     * @param name JMXのObjectNameに指定する文字列
     * @param attribute JMXのattributeに指定する文字列
     * @param remoteEnable リモート接続を行うかどうかを表すフラグ
     * @param id ID
     *
     * @throws MalformedObjectNameException 文字列の形式が誤ったObjectNameを指定した場合
     */
    public MBeanValueGetter(final MBeanServer server, final MBeanServerConnection connection,
            final String name, final String attribute, final boolean remoteEnable, String id)
        throws MalformedObjectNameException
    {
        this.server_ = server;
        this.connection_ = connection;
        this.objectName_ = new ObjectName(name);
        this.attribute_ = attribute;
        this.remoteEnable_ = remoteEnable;
        this.id_ = id;
        // オブジェクト名の正規表現と文字列の正規表現を一致させるために、* → (.*), ( → \(, ) → \) に変換する。
        // String regularObjectName =
        //   name.replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)").replaceAll("\\*",
        //   "(\\.\\*)");
    }

    /**
     * JMX計測値を取得します。<br />
     *
     * @return JMX計測値のリスト
     */
    public List<ResourceItem> getValue()
    {
        List<ResourceItem> returnList = new ArrayList<ResourceItem>();
        try
        {
            Integer mBeanCount = getMBeanCount();
            if (this.lastMBeanCount_ == null
                    || mBeanCount.intValue() != this.lastMBeanCount_.intValue())
            {
                this.objectNameList_ = createObjectNameList(this.objectName_);
                this.lastMBeanCount_ = mBeanCount;
            }

            // 該当するオブジェクト名ごとに属性値を取得する。
            for (ObjectName objectName : this.objectNameList_)
            {
                List<String> attributeList = new ArrayList<String>();
                if (this.attribute_.equals("*"))
                {
                    try
                    {
                        attributeList = getAttributes(objectName);
                    }
                    catch (IntrospectionException ex)
                    {
                        SystemLogger.getInstance().warn(ex);
                    }
                }
                else
                {
                    attributeList.add(this.attribute_);
                }

                // 系列名を組み立てる。
                for (String attribute : attributeList)
                {
                    // JMXの計測値を取得する
                    Object value = getAttribute(objectName, attribute);
                    String targetObjectName = createItemName(objectName, attribute);

                    ResourceItem retValue =
                                            createResourceItem(targetObjectName,
                                                               String.valueOf(value),
                                                               ItemType.ITEMTYPE_STRING);
                    returnList.add(retValue);
                }
            }
        }
        catch (AttributeNotFoundException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (InstanceNotFoundException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (MBeanException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (ReflectionException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (IOException ex)
        {
            SystemLogger.getInstance().warn(ex);
            MBeanCollectorInitializer.recconect(this.id_);
        }
        return returnList;
    }

    private Object getAttribute(ObjectName objectName, String attribute)
        throws AttributeNotFoundException,
            MBeanException,
            InstanceNotFoundException,
            ReflectionException,
            IOException
    {
        Object value = null;
        if (server_ != null)
        {
            value = server_.getAttribute(objectName, attribute);
        }
        else if (connection_ != null && remoteEnable_)
        {
            value = connection_.getAttribute(objectName, attribute);
        }
        return value;
    }

    private List<String> getAttributes(ObjectName objectName)
        throws ReflectionException,
            IntrospectionException,
            InstanceNotFoundException,
            IOException
    {
        MBeanAttributeInfo[] attributeInfos = null;
        if (server_ != null)
        {
            attributeInfos = server_.getMBeanInfo(objectName).getAttributes();
        }
        else if (connection_ != null && remoteEnable_)
        {
            attributeInfos = connection_.getMBeanInfo(objectName).getAttributes();
        }
        List<String> attributeList = new ArrayList<String>();
        if (attributeInfos == null)
        {
            return attributeList;
        }
        for (MBeanAttributeInfo attributeInfo : attributeInfos)
        {
            String attribute = attributeInfo.getName();
            String attributetype = attributeInfo.getType();
            if (isNumber(attributetype))
            {
                attributeList.add(attribute);
            }
        }
        return attributeList;
    }

    /**
     * MBean数を取得する。
     * @return MBean数
     * @throws IOException MBean数取得時に切断していた場合。
     */
    private Integer getMBeanCount()
        throws IOException
    {
        Integer mBeanCount = null;
        if (server_ != null)
        {
            mBeanCount = server_.getMBeanCount();
        }
        else if (connection_ != null && remoteEnable_)
        {
            mBeanCount = connection_.getMBeanCount();
        }
        return mBeanCount;
    }

    /***
     * ObjectNameと属性名から、系列名を作成する。
     * 
     * @param objectName 系列名を取得したいObjectName。
     * @param attributeName 系列名を取得したい属性名。
     * @return　系列名。
     */
    private String createItemName(ObjectName objectName, String attributeName)
    {
        StringBuilder itemNameBuilder = new StringBuilder();
        itemNameBuilder.append("/jmx/");
        itemNameBuilder.append(objectName.getDomain());

        // ObjectNameを文字列化した際の順序でプロパティが並ぶように、順序をTreeMapに格納する。
        Hashtable<?, ?> keyPropertyList = objectName.getKeyPropertyList();
        String canonicalName = objectName.toString();
        Map<Integer, String> keyOrderMap = new TreeMap<Integer, String>();
        for (Object key : keyPropertyList.keySet())
        {
            String keyStr = key.toString();
            keyOrderMap.put(Integer.valueOf(canonicalName.indexOf(keyStr)), key.toString());
        }

        // 作成した順序に従ってプロパティを追加する。
        for (String key : keyOrderMap.values())
        {
            itemNameBuilder.append("/");
            itemNameBuilder.append(keyPropertyList.get(key).toString().replace("\"", ""));
        }
        itemNameBuilder.append("/");
        itemNameBuilder.append(attributeName);
        String itemName = itemNameBuilder.toString();
        return itemName;
    }

    /**
     * 引数で指定したオブジェクト名（正規表現）と一致するオブジェクト名一覧を作成する。
     * 
     * @param objectName 取得する対象となるオブジェクト名（正規表現）
     * @return オブジェクト名一覧
     * @throws IOException MBean数取得時に切断していた場合。
     */
    private List<ObjectName> createObjectNameList(ObjectName objectName)
        throws IOException
    {
        List<ObjectName> objectNameList = new ArrayList<ObjectName>();
        Set<?> set = null;
        if (server_ != null)
        {
            set = server_.queryMBeans(objectName, null);
        }
        else if (connection_ != null && remoteEnable_)
        {
            set = connection_.queryMBeans(objectName, null);
        }
        else
        {
            return objectNameList;
        }
        Iterator<?> iterator = set.iterator();
        while (iterator.hasNext())
        {
            ObjectInstance oi = (ObjectInstance)iterator.next();
            objectNameList.add(oi.getObjectName());
        }
        return objectNameList;
    }

    /**
     * JMXのObjectNameに指定する文字列を取得する
     * @return JMXのObjectNameに指定する文字列
     */
    public String getName()
    {
        return this.objectName_.toString();
    }

    /**
     * JMXのattributeに指定する文字列を取得する
     * @return JMXのattributeに指定する文字列
     */
    public String getAttribute()
    {
        return this.attribute_;
    }

    /**
     * 計測値の型を取得します。
     *
     * @return 計測値の型
     */
    public ItemType getItemType()
    {
        return ItemType.ITEMTYPE_STRING;
    }

    /**
     * ResourceItemを作成する
     * 
     * @param name Item Name
     * @return JMXの値を取得した結果を ResourceItem の形式で返す
     */
    private ResourceItem createResourceItem(String name, String value, ItemType itemType)
    {
        ResourceItem retValue = new ResourceItem();

        retValue.setValue(value);
        retValue.setItemType(itemType);
        retValue.setObjectName(name);
        retValue.setName(name);
        retValue.setObjectDisplayNeme(name);
        retValue.setDisplayName(name);
        retValue.setDisplayType(DisplayType.DISPLAYTYPE_NORMAL);

        return retValue;
    }

    /**
     * {@link MBeanServerConnection}を設定する。
     * @param connection MBeanServerConnectionオブジェクト
     */
    public void setConnection(MBeanServerConnection connection)
    {
        this.connection_ = connection;
    }

    private boolean isNumber(String type)
    {
        return numberTypeSet__.contains(type);
    }
}
