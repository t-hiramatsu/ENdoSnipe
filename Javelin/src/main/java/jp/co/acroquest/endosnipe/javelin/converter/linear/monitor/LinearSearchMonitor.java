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
package jp.co.acroquest.endosnipe.javelin.converter.linear.monitor;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;
import jp.co.acroquest.endosnipe.javelin.event.LinearSearchEvent;
import jp.co.acroquest.endosnipe.javelin.util.IdentityWeakMap;
import jp.co.acroquest.endosnipe.javelin.util.StatsUtil;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * ���`���������o���郂�j�^�N���X�ł��B<br />
 * 
 * @author fujii
 *
 */
public class LinearSearchMonitor
{
    /** �I�u�W�F�N�g���O��w�肵���C���f�b�N�X��ۑ�����}�b�v */
    private static ThreadLocal<IdentityWeakMap<Object, Integer>> lastIndexMap__;

    /** ���`�����C�x���g���o��ۑ�����}�b�v */
    private static IdentityWeakMap<Object, AtomicInteger> searchCountMap__ =
            new IdentityWeakMap<Object, AtomicInteger>();

    /** LinkedList#indexOf() �Ăяo���񐔂�ۑ�����}�b�v */
    private static IdentityWeakMap<Object, AtomicInteger> indexOfMap__ =
            new IdentityWeakMap<Object, AtomicInteger>();

    /** Javelin�̐ݒ�l */
    private static JavelinConfig config__ = new JavelinConfig();

    /** ���`�������o�̔��蒆���ǂ�����\���t���O */
    private static ThreadLocal<Boolean> isTracing__;

    /** ���`�������o�̔���Ώ� */
    private static ThreadLocal<Object> target__;

    /** ���`�������o���s�����ǂ�����\���t���O */
    private static boolean isMonitor__;

    /** ���`�����ΏۂƂȂ郊�X�g�̃T�C�Y��臒l */
    private static int linearSearchlistSize__;

    /** ���`�����ΏۂƂȂ�A���X�g�ɑ΂�����`�A�N�Z�X�񐔂̊�����臒l */
    private static double listRatio__;

    static
    {
        isTracing__ = new ThreadLocal<Boolean>() {
            protected Boolean initialValue()
            {
                return Boolean.TRUE;
            }
        };

        target__ = new ThreadLocal<Object>();

        lastIndexMap__ = new ThreadLocal<IdentityWeakMap<Object, Integer>>() {
            protected synchronized IdentityWeakMap<Object, Integer> initialValue()
            {
                return new IdentityWeakMap<Object, Integer>();
            }
        };

        isMonitor__ = config__.isLinearSearchMonitor();
        linearSearchlistSize__ = config__.getLinearSearchListSize();
        listRatio__ = config__.getLinearSearchListRatio();
    }

    /**
     * �I�u�W�F�N�g�̃C���X�^���X����j�~����v���C�x�[�g�R���X�g���N�^�ł��B<br />
     */
    private LinearSearchMonitor()
    {
        // Do Nothing.
    }

    /**
     * ���`���������Ă��邩�ǂ��������o���܂��B�i get() �����s�����ƌĂ΂�܂��B�j<br />
     * @param obj �I�u�W�F�N�g
     * @param index �C���f�b�N�X
     */
    public static void postProcess(Object obj, int index)
    {
        // ���`�������o�t���O��OFF�ɂȂ��Ă���ꍇ�͏������s��Ȃ��B
        if (isMonitor__ == false)
        {
            return;
        }
        if (isTracing__.get().booleanValue() == false)
        {
            return;
        }
        
        if (target__.get() == null)
        {
            return;
        }
        target__.set(null);
        
        isTracing__.set(Boolean.FALSE);

        try
        {
            detect(obj, index);
        }
        finally
        {
            isTracing__.set(Boolean.TRUE);
        }
    }

    /**
     * ���`�����̌��o�̃��C���������s���B
     * 
     * @param obj ���o�ΏہB
     * @param index �C���f�b�N�X�B
     */
    private static void detect(Object obj, int index)
    {
        if (obj instanceof List<?>)
        {
            List<?> list = (List<?>)obj;
            int listSize = list.size();
            AtomicInteger countInteger = searchCountMap__.get(list);
            if (countInteger != null)
            {
                int searchCount = countInteger.intValue();
                if (searchCount >= (int)((listSize - 1) * listRatio__))
                {
                    return;
                }
            }
            if (listSize >= linearSearchlistSize__)
            {
                detect(list, index);
            }
        }
    }

    public static void preProcess(Object obj)
    {
        // ���`�������o�t���O��OFF�ɂȂ��Ă���ꍇ�͏������s��Ȃ��B
        if (isMonitor__ == false)
        {
            return;
        }
        if (isTracing__.get().booleanValue() == false)
        {
            return;
        }
        target__.set(obj);
    }
    
    /**
     * ���`���������Ă��邩�ǂ��������o���܂��B�i indexOf() �����s�����ƌĂ΂�܂��B�j<br />
     *
     * @param obj �I�u�W�F�N�g
     * @param search ���������I�u�W�F�N�g
     * @param index indexOf() �̖߂�l
     */
    public static void postProcessIndexOf(Object obj, Object search, int index)
    {
        // ���`�������o�t���O��OFF�ɂȂ��Ă���ꍇ�͏������s��Ȃ��B
        if (isMonitor__ == false)
        {
            return;
        }
        if (isTracing__.get().booleanValue() == false)
        {
            return;
        }
        
        if (target__.get() == null)
        {
            return;
        }
        target__.set(null);
        
        isTracing__.set(Boolean.FALSE);
        try
        {
            if (obj instanceof List<?>)
            {
                List<?> list = (List<?>)obj;
                int listSize = list.size();
                if (listSize >= linearSearchlistSize__)
                {
                    detectIndexOf(list, index);
                }
            }
        }
        finally
        {
            isTracing__.set(Boolean.TRUE);
        }
    }

    /**
     * ���`���������o���܂��B�i get() �Łj<br />
     * �ȉ��̏����𖞂����ꍇ�A���`�����Ƃ��Č��o���܂��B
     * <li>�C���f�b�N�X�̒l�����X�g�T�C�Y�ɑ΂��Ĉ��ȏ�̊���(javelin.properties�ɂ��ݒ�)���P�傫��(������)�B</li>
     * <li>���X�g�Ɏw�肵���C���f�b�N�X���A�O��w�肵���C���f�b�N�X���P�傫��(������)�B</li>
     * <li>�I�u�W�F�N�g�ɑ΂�����`�����������o�ł���B</li>
     * 
     * @param list ���`�������o�ΏۂƂȂ郊�X�g
     * @param index �C���f�b�N�X
     */
    public static void detect(List<?> list, int index)
    {
        IdentityWeakMap<Object, Integer> lastIndexMap = lastIndexMap__.get();
        Integer lastIndex = lastIndexMap.get(list);

        if (lastIndex != null)
        {
            int listSize = list.size();
            int searchCount = 0;

            if (detectLinearSearch(listSize, index, lastIndex.intValue()))
            {
                AtomicInteger searchCountObj;
                synchronized (searchCountMap__)
                {
                    searchCountObj = searchCountMap__.get(list);
                    if (searchCountObj == null)
                    {
                        searchCountMap__.put(list, new AtomicInteger(1));
                        searchCount = 1;
                    }
                    else
                    {
                        searchCount = searchCountObj.incrementAndGet();
                    }
                }
                if (searchCount == (int)((listSize - 1) * listRatio__))
                {
                    // �񓯊������X���b�h�A�N�Z�X�C�x���g�𔭐�������
                    CommonEvent event = createDetectedEvent(list, searchCountObj);
                    StatsJavelinRecorder.addEvent(event);
                }
            }
        }
        // ���o�ʒu��ۑ�����B
        lastIndexMap.put(list, Integer.valueOf(index));
    }

    /**
     * ���`�����ł��邩�ǂ����𔻒肵�܂��B<br />
     * 
     * @param index �������̃��X�g�̃C���f�b�N�X
     * @param lastIndex �O��A�N�Z�X�������X�g�̃C���f�b�N�X
     * @return ���`�����ł���ꍇ�A<code>true</code>
     */
    private static boolean detectLinearSearch(int listSize, int index, int lastIndex)
    {
        if (index == lastIndex + 1)
        {
            return true;
        }
        if (index == lastIndex - 1)
        {
            return true;
        }
        return false;
    }

    /**
     * ���`���������o���܂��B�i indexOf() �Łj<br />
     *
     * @param list ���`�������o�ΏۂƂȂ郊�X�g
     * @param index �C���f�b�N�X
     */
    private static void detectIndexOf(List<?> list, int index)
    {
        int searchCount = 0;

        synchronized (indexOfMap__)
        {
            AtomicInteger searchCountObj = indexOfMap__.get(list);
            if (searchCountObj == null)
            {
                indexOfMap__.put(list, new AtomicInteger(1));
                searchCount = 1;
            }
            else
            {
                searchCount = searchCountObj.incrementAndGet();
            }
        }
        if (searchCount == (int)listRatio__)
        {
            // �񓯊������X���b�h�A�N�Z�X�C�x���g�𔭐�������
            CommonEvent event = createDetectedEvent(list, indexOfMap__.get(list));
            StatsJavelinRecorder.addEvent(event);
        }
    }

    /**
     * ���`�������o�C�x���g���쐬���܂��B<br />
     * [�o�͓��e]<br />
     * ���C�x���g��<br />
     * LinearSearchDetected : ���`���������̃C�x���g��<br />
     * <br />
     * ���p�����[�^<br />
     * linearsearch.size : ���`�������o�������X�g�̃T�C�Y<br />
     * linearsearch.count : ���`�A�N�Z�X��<br />
     * linearsearch.objectID : ���`�������o�������X�g�̃I�u�W�F�N�gID<br />
     * linearsearch.stackTrace : ���`�������o���̃X�^�b�N�g���[�X<br />
     * 
     * @param list ���`���������o�������X�g
     * @param countObj ���`�������s������
     * @return ���`�������o�C�x���g
     */
    public static CommonEvent createDetectedEvent(final List<?> list, final AtomicInteger countObj)
    {
        CommonEvent event = new LinearSearchEvent();
        event.addParam(EventConstants.PARAM_LINEARSEARCH_SIZE, String.valueOf(list.size()));
        if (countObj != null)
        {
            String searchCountStr = String.valueOf(countObj);
            event.addParam(EventConstants.PARAM_LINEARSEARCH_COUNT, searchCountStr);
        }
        else
        {
            event.addParam(EventConstants.PARAM_LINEARSEARCH_COUNT, String.valueOf(0));
        }
        String objectID = StatsUtil.createIdentifier(list);
        event.addParam(EventConstants.PARAM_LINEARSEARCH_OBJECTID, objectID);

        StackTraceElement[] stacktraces = ThreadUtil.getCurrentStackTrace();
        String stackTrace = ThreadUtil.getStackTrace(stacktraces, config__.getTraceDepth());

        event.addParam(EventConstants.PARAM_LINEARSEARCH_STACKTRACE, stackTrace);
        return event;
    }

    /**
     * ���`�������o���s�����ǂ�����ݒ肵�܂��B<br />
     * 
     * @param isMonitor ���`�������o���s���ꍇ�́A<code>true</code>
     */
    public static void setMonitor(boolean isMonitor)
    {
        isMonitor__ = isMonitor;
    }

    /**
     * ���`�������o���s�����X�g��臒l��ݒ肵�܂��B<br />
     * 
     * @param listSize ���`�������o���s�����X�g��臒l
     */
    public static void setLinearSearchListSize(int listSize)
    {
        linearSearchlistSize__ = listSize;
    }

    /**
     * ���`�����ΏۂƂȂ�A���X�g�ɑ΂�����`�A�N�Z�X�񐔂̊�����臒l��ݒ肵�܂��B
     * 
     * @param listRatio ���`�����ΏۂƂȂ�A���X�g�ɑ΂�����`�A�N�Z�X�񐔂̊�����臒l
     */
    public static void setListRatio(double listRatio)
    {
        listRatio__ = listRatio;
    }

}
