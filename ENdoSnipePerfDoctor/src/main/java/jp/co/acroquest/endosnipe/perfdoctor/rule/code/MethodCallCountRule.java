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
package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.acroquest.endosnipe.common.parser.JavelinConstants;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogColumnNum;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.Messages;
import jp.co.acroquest.endosnipe.perfdoctor.PerfConstants;
import jp.co.acroquest.endosnipe.perfdoctor.rule.AbstractRule;

/**
 * ���\�b�h�̌Ăяo���񐔂�臒l�𒴂����ꍇ�Ɍx����\�����郋�[���B
 * 
 * @author tooru
 * 
 */
public class MethodCallCountRule extends AbstractRule
{
    /** �x���Ɣ��f����OR/UNION�񐔂̃f�t�H���g�l�B */
    private static final int DEFAULT_THRESHOLD = 1000;

    /**
     * �Ăяo���񐔂�臒l�B���̒l�𒴂����ۂɌx���𐶐�����B
     */
    public int               threshold         = DEFAULT_THRESHOLD;

    /**
     * �N���X�A���\�b�h�A�X���b�hID���ɌĂяo���񐔂��W�v���A 臒l�𒴂����ۂɌx������B
     * 
     * @param javelinLogElementList
     *            ���O�f�[�^�B
     */
    @Override
    public void doJudge(final List<JavelinLogElement> javelinLogElementList)
    {

        /**
         * �Ăяo���񐔂��L�^����}�b�v�B �N���X��#���\�b�h��#�X���b�hID���L�[�Ƃ��A�Ăяo���񐔂�l�Ƃ���B
         */
        Map<String, Integer> callCountMap = new HashMap<String, Integer>();

        /**
         * �Ăяo���ꂽ�񐔂�臒l�𒴂������\�b�h���L�^����}�b�v�B
         * �N���X��#���\�b�h��#�X���b�hID���L�[�Ƃ��A臒l�𒴂����Ƃ���JavelinLogElement��l�Ƃ���B
         */
        Map<String, JavelinLogElement> overThresholdMethodMap =
                                                                new HashMap<String, JavelinLogElement>();

        // ���\�b�h���Ɂu�Ăяo���񐔁v���J�E���g����B
        for (JavelinLogElement javelinLogElement : javelinLogElementList)
        {
            try
            {
                doJudgeSingleElement(javelinLogElement, callCountMap, overThresholdMethodMap);
            }
            catch (RuntimeException ex)
            {
                log(Messages.getMessage(PerfConstants.PERF_DOCTOR_RUNTIME_EXCEPTION),
                    javelinLogElement, ex);
            }
        }

        // 臒l�𒴂������̂�����ꍇ�ɂ͌x�����o���B
        addErrorElements(callCountMap, overThresholdMethodMap);
    }

    /**
     * 臒l�𒴂������̂�����ꍇ�ɂ͌x�����o���B
     * 
     * @param callCountMap
     *            �Ăяo���񐔂��L�^����}�b�v�B
     * @param overThresholdMethodMap
     *            �Ăяo���ꂽ�񐔂�臒l�𒴂������\�b�h���L�^����}�b�v�B
     */
    private void addErrorElements(final Map<String, Integer> callCountMap,
            final Map<String, JavelinLogElement> overThresholdMethodMap)
    {
        Set<Map.Entry<String, JavelinLogElement>> entries = overThresholdMethodMap.entrySet();

        for (Map.Entry<String, JavelinLogElement> entry : entries)
        {
            JavelinLogElement element = entry.getValue();

            String key = entry.getKey();
            int count = callCountMap.get(key);

            addError(key, element, this.threshold, count);
        }
    }

    /**
     * �e���\�b�h�̌Ăяo���񐔂��J�E���g����B 臒l�𒴂������̂�����ꍇ�ɂ͋L�^����B
     * 
     * @param javelinLogElement
     *            ���O�f�[�^�B
     * @param callCountMap
     *            �Ăяo���񐔂��L�^����}�b�v�B
     * @param overThresholdMethodMap
     *            �Ăяo���ꂽ�񐔂�臒l�𒴂������\�b�h���L�^����}�b�v�B
     */
    private void doJudgeSingleElement(final JavelinLogElement javelinLogElement,
            final Map<String, Integer> callCountMap,
            final Map<String, JavelinLogElement> overThresholdMethodMap)
    {
        List<String> baseInfo = javelinLogElement.getBaseInfo();

        // Call���O�݂̂�ΏۂƂ���B
        String id = baseInfo.get(JavelinLogColumnNum.ID);
        if (JavelinConstants.MSG_CALL.equals(id) == false)
        {
            return;
        }

        // Map�̃L�[���쐬����B
        String className = baseInfo.get(JavelinLogColumnNum.CALL_CALLEE_CLASS);
        String methodName = baseInfo.get(JavelinLogColumnNum.CALL_CALLEE_METHOD);
        String threadId = baseInfo.get(JavelinLogColumnNum.CALL_THREADID);

        // SQL���s�͏��O����
        if (isSqlExec(className) == true)
        {
            return;
        }

        String key = className + "#" + methodName + "#" + threadId;

        // �J�E���g��1��������B
        // �܂���x�����s����Ă��Ȃ��ꍇ�ɂ͐V���ɃI�u�W�F�N�g�����A�l��1�ɂ���B
        Integer count = callCountMap.get(key);
        if (count == null)
        {
            count = Integer.valueOf(1);
        }
        else
        {
            count = Integer.valueOf(count.intValue() + 1);
        }
        callCountMap.put(key, count);

        // �����J�E���g��臒l�ɒB����̂ł���΁AoverThresholdMethodMap_�ɋL�^����B
        if (count.intValue() == this.threshold)
        {
            overThresholdMethodMap.put(key, javelinLogElement);
        }
    }
}
