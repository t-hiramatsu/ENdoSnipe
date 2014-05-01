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

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import jp.co.acroquest.endosnipe.common.entity.DisplayType;
import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.entity.ResourceItem;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;

/**
 * �w�肳�ꂽ���O��MBean����v���l���擾����N���X�ł��B
 *
 * @author y_asazuma
 */
public class MBeanValueGetter
{
    /** MBeanServer */
    private static MBeanServer server__ = ManagementFactory.getPlatformMBeanServer();

    /** JMX��ObjectName�I�u�W�F�N�g */
    private final ObjectName   objectName_;

    /** JMX��attribute�Ɏw�肷�镶���� */
    private final String       attribute_;

    /** �I�u�W�F�N�g���̃��X�g */
    private List<ObjectName>   objectNameList_;

    /** �O����s����MBean�� */
    private Integer            lastMBeanCount_;

    /**
     * JMX�̌v���l���擾���邽�߂̃N���X�����������܂��B
     *
     * @param name JMX��ObjectName�Ɏw�肷�镶����
     * @param attribute JMX��attribute�Ɏw�肷�镶����
     *
     * @throws MalformedObjectNameException ������̌`���������ObjectName���w�肵���ꍇ
     */
    public MBeanValueGetter(final String name, final String attribute)
        throws MalformedObjectNameException
    {
        this.objectName_ = new ObjectName(name);
        this.attribute_ = attribute;
        // �I�u�W�F�N�g���̐��K�\���ƕ�����̐��K�\������v�����邽�߂ɁA* �� (.*), ( �� \(, ) �� \) �ɕϊ�����B
        String regularObjectName =
                                   name.replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)").replaceAll("\\*",
                                                                                                         "(\\.\\*)");
    }

    /**
     * JMX�v���l���擾���܂��B<br />
     *
     * @return JMX�v���l�̃��X�g
     */
    public List<ResourceItem> getValue()
    {
        List<ResourceItem> returnList = new ArrayList<ResourceItem>();
        try
        {
            Integer mBeanCount = server__.getMBeanCount();
            if (this.lastMBeanCount_ == null
                    || mBeanCount.intValue() != this.lastMBeanCount_.intValue())
            {
                this.objectNameList_ = createObjectNameList(this.objectName_);
                this.lastMBeanCount_ = mBeanCount;
            }

            // �Y������I�u�W�F�N�g�����Ƃɑ����l���擾����B
            for (ObjectName objectName : this.objectNameList_)
            {
                if (this.attribute_.equals("*"))
                {
                    try
                    {
                        server__.getMBeanInfo(objectName).getAttributes();
                    }
                    catch (IntrospectionException ex)
                    {
                        // TODO �����������ꂽ catch �u���b�N
                        ex.printStackTrace();
                    }
                }
                // JMX�̌v���l���擾����
                Object value = server__.getAttribute(objectName, this.attribute_);
                
                // �n�񖼂�g�ݗ��Ă�B
                String targetObjectName = createItemName(objectName, this.attribute_);
                
                ResourceItem retValue =
                                        createResourceItem(targetObjectName, String.valueOf(value),
                                                           ItemType.ITEMTYPE_STRING);
                returnList.add(retValue);
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
        return returnList;
    }

    /***
     * ObjectName�Ƒ���������A�n�񖼂��쐬����B
     * 
     * @param objectName �n�񖼂��擾������ObjectName�B
     * @param attributeName �n�񖼂��擾�������������B
     * @return�@�n�񖼁B
     */
    private String createItemName(ObjectName objectName, String attributeName)
    {
        StringBuilder itemNameBuilder = new StringBuilder();
        itemNameBuilder.append("/jmx/");
        itemNameBuilder.append(objectName.getDomain());

        // ObjectName�𕶎��񉻂����ۂ̏����Ńv���p�e�B�����Ԃ悤�ɁA������TreeMap�Ɋi�[����B
        Hashtable<?, ?> keyPropertyList = objectName.getKeyPropertyList();
        String canonicalName = objectName.toString();
        Map<Integer, String> keyOrderMap = new TreeMap<Integer, String>();
        for (Object key : keyPropertyList.keySet())
        {
            String keyStr = key.toString();
            keyOrderMap.put(Integer.valueOf(canonicalName.indexOf(keyStr)), key.toString());
        }
        
        // �쐬���������ɏ]���ăv���p�e�B��ǉ�����B
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
     * �����Ŏw�肵���I�u�W�F�N�g���i���K�\���j�ƈ�v����I�u�W�F�N�g���ꗗ���쐬����B
     * 
     * @param objectName �擾����ΏۂƂȂ�I�u�W�F�N�g���i���K�\���j
     * @return �I�u�W�F�N�g���ꗗ
     */
    private List<ObjectName> createObjectNameList(ObjectName objectName)
    {
        List<ObjectName> objectNameList = new ArrayList<ObjectName>();
        Set<?> set = server__.queryMBeans(objectName, null);
        Iterator<?> iterator = set.iterator();
        while (iterator.hasNext())
        {
            ObjectInstance oi = (ObjectInstance)iterator.next();
            objectNameList.add(oi.getObjectName());
        }
        return objectNameList;
    }

    /**
     * JMX��ObjectName�Ɏw�肷�镶������擾����
     * @return JMX��ObjectName�Ɏw�肷�镶����
     */
    public String getName()
    {
        return this.objectName_.toString();
    }

    /**
     * JMX��attribute�Ɏw�肷�镶������擾����
     * @return JMX��attribute�Ɏw�肷�镶����
     */
    public String getAttribute()
    {
        return this.attribute_;
    }

    /**
     * �v���l�̌^���擾���܂��B
     *
     * @return �v���l�̌^
     */
    public ItemType getItemType()
    {
        return ItemType.ITEMTYPE_STRING;
    }

    /**
     * ResourceItem���쐬����
     * 
     * @param name Item Name
     * @return JMX�̒l���擾�������ʂ� ResourceItem �̌`���ŕԂ�
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
}
