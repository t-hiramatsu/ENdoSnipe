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
 * JMX�v���l���擾���邽�߂̃N���X���όn��p�}�b�v�ɓo�^���܂��B
 *
 * @author y_asazuma
 */
public class MBeanCollectorInitializer
{
    /** Javelin�ŊĎ�����JMX�̐ݒ�t�@�C�� */
    private static final String JMX_PROP                        = "../conf/jmx.properties";

    /** �ݒ�t�@�C����ObjectName��\���L�[ */
    private static final String PROP_PREFIX_OBJECTNAME          = "objectName.";

    /** �ݒ�t�@�C����attribute��\���L�[ */
    private static final String PROP_PREFIX_ATTRIBUTE           = "attribute.";

    /** ����(������)�̈ʒu */
    private static final int    ATTRIBUTE_NAME_POSITION         = 0;

    /** ����(���ڌ^)�̈ʒu */
    private static final int    ATTRIBUTE_ITEM_TYPE_POSITION    = 1;

    /**
     * �R���X�g���N�^
     */
    private MBeanCollectorInitializer()
    {
        
    };
    
    /**
     * ���\�[�X�擾�C���X�^���X���}�b�v�ɓo�^���܂��B
     *
     * @param multiResourceMap ���\�[�X�擾�C���X�^���X��o�^����}�b�v�i�όn��p�j
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

            // PREFIX��"objectName."�łȂ��ꍇ�͓ǂݔ�΂�
            if (propKey.startsWith(PROP_PREFIX_OBJECTNAME) == false)
            {
                continue;
            }

            // �ݒ�t�@�C������I�u�W�F�N�g�̒�`���擾����
            String objectName = objectStr;

            String id = propKey.substring(PROP_PREFIX_OBJECTNAME.length());
            String attrListStr = properties.getProperty(PROP_PREFIX_ATTRIBUTE + id);

            // �ݒ�t�@�C�����瑮���̒�`���擾����
            // �ϐ�attrListStr�̒��g�͈ȉ��̌`���ɂȂ��Ă���
            //   <attribute n1>,<attribute n2>,...
            String[] attrList = attrListStr.split(",");
            for (String attrStr : attrList)
            {
                String attrName = attrStr;

                // �擾����JMX�̌v���l�̐ݒ�����o�͂���
                StringBuilder sb = new StringBuilder();
                sb.append("(JMX mesuerment) ");
                sb.append("ObjectName[").append(objectName).append("] ");
                sb.append("attribute[").append(attrName).append("] ");
                SystemLogger.getInstance().info(sb.toString());

                try
                {
                    // JMX�̌v���l���擾����N���X�����������Ēǉ�����
                    getters.addMBeanValueGetter(new MBeanValueGetter(objectName, attrName));
                }
                catch (MalformedObjectNameException ex)
                {
                    SystemLogger.getInstance().warn(ex);
                }
            }
        }

        // �όn��p�̃��\�[�X�擾�Ƃ���JMX�̌v���l��o�^����
        multiResourceMap.put(TelegramConstants.ITEMNAME_JMX, getters);
    }
}
