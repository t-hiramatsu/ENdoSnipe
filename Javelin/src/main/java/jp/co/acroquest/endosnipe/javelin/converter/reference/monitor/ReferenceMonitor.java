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
package jp.co.acroquest.endosnipe.javelin.converter.reference.monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;
import jp.co.acroquest.endosnipe.javelin.util.StatsUtil;

/**
 * ReferenceMonitorConverter�ɂ��ϊ����ꂽ�N���X�̃I�u�W�F�N�g��ێ�����N���X�B
 * 
 * @author eriguchi
 */
public class ReferenceMonitor
{
    /** �Q�Ƃ�ێ�����}�b�v�B�L�[���N���X���A�l���Ώۂ̎Q�Ƃ����}�b�v */
    private static Map<String, Map<Object, Object>> referenceMap__;

    static
    {
        referenceMap__ = new ConcurrentHashMap<String, Map<Object, Object>>();
    }

    /**
     * �C���X�^���X����h�~���邽�߂̃R���X�g���N�^�B
     */
    private ReferenceMonitor()
    {
        // Do Nothing
    }

    /**
     * �I�u�W�F�N�g��ǉ�����B
     * 
     * @param className �N���X���B
     * @param obj �ǉ�����I�u�W�F�N�g�B
     */
    public static void add(String className, Object obj)
    {
        if (referenceMap__.containsKey(className) == false)
        {
            Map<Object, Object> synchronizedMap =
                    Collections.synchronizedMap(new WeakHashMap<Object, Object>());
            referenceMap__.put(className, synchronizedMap);
        }

        Map<Object, Object> weakHashMap = referenceMap__.get(className);
        weakHashMap.put(obj, null);
    }

    /**
     * �w�肵���N���X�̃I�u�W�F�N�g�̃��X�g���擾����B
     * 
     * @param className �N���X���B
     * @return �w�肵���N���X�̃I�u�W�F�N�g�̃��X�g�B
     */
    public static List<Object> get(String className)
    {
        List<Object> resultList = new ArrayList<Object>();
        Map<Object, Object> map = referenceMap__.get(className);
        if (map == null)
        {
            return resultList;
        }

        synchronized (map)
        {
            for (Object obj : map.keySet())
            {
                resultList.add(obj);
            }
        }

        return resultList;
    }

    /**
     * �I�u�W�F�N�g���폜����B
     * 
     * @param className �N���X���B
     * @param obj �폜����I�u�W�F�N�g�B
     */
    public static void remove(String className, Object obj)
    {
        if (referenceMap__.containsKey(className) == false)
        {
            Map<Object, Object> synchronizedMap =
                    Collections.synchronizedMap(new WeakHashMap<Object, Object>());
            referenceMap__.put(className, synchronizedMap);
        }

        Map<Object, Object> weakHashMap = referenceMap__.get(className);
        weakHashMap.remove(obj);
    }
    
    /**
     * �Q�Ƃ��Ă���I�u�W�F�N�g�̃_���v�C�x���g�𐶐�����B
     *
     * @param className �N���X��
     * @return �_���v�C�x���g
     */
    public static CommonEvent createDumpEvent(String className)
    {
        CommonEvent referenceDumpEvent = new CommonEvent();
        referenceDumpEvent.setName(EventConstants.NAME_REFERENCE_DUMP);

        Map<Object, Object> map = referenceMap__.get(className);
        if (map == null)
        {
            return referenceDumpEvent;
        }

        synchronized (map)
        {
            int count = 0;
            for (Object obj : map.keySet())
            {
                JavelinConfig config = new JavelinConfig();
                String dump;
                
                if (obj == null)
                {
                    continue;
                }
                
                if (isHttpSession(obj))
                {
                    // HttpSession�̏ꍇ�͂�����
                    if (config.isHttpSessionDetail())
                    {
                        int detailDepth = config.getHttpSessionDetailDepth();
                        dump = StatsUtil.buildDetailString(obj, detailDepth);
                    }
                    else
                    {
                        dump = StatsUtil.toStr(obj, config.getStringLimitLength());
                    }
                }
                else
                {
                    if (config.isArgsDetail())
                    {
                        int argsDetailDepth = config.getArgsDetailDepth();
                        dump = StatsUtil.buildDetailString(obj, argsDetailDepth);
                    }
                    else
                    {
                        dump = StatsUtil.toStr(obj, config.getStringLimitLength());
                    }
                }

                String key = EventConstants.PARAM_REFERENCE_DUMP + "." + count;
                referenceDumpEvent.addParam(key, dump);
                count++;
            }
        }        
        
        return referenceDumpEvent;
    }

    /**
     * HttpSession�̃C���X�^���X���ǂ����𔻒肷��B
     * @param obj ����Ώ�
     * @return HttpSession�̃C���X�^���X���ǂ���
     */
    private static boolean isHttpSession(Object obj)
    {
        boolean result = false;
        
        Class<? extends Object> clazz = obj.getClass();
        try
        {
            ClassLoader loader = clazz.getClassLoader();
            Class<?> sessionClazz = loader.loadClass("javax.servlet.http.HttpSession");
            if (sessionClazz.isInstance(obj))
            {
                result = true;
            }
        }
        catch (ClassNotFoundException ex)
        {
            result = false;
        }        
        
        return result;
    }
}
