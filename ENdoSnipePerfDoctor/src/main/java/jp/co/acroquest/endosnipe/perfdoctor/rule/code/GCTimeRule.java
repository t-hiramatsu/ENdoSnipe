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

import java.util.Map;

import jp.co.acroquest.endosnipe.common.parser.JavelinConstants;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogColumnNum;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogConstants;
import jp.co.acroquest.endosnipe.javelin.JavelinLogUtil;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;
import jp.co.acroquest.endosnipe.perfdoctor.rule.SingleElementRule;

/**
 * �v���Z�X�ɂ�����GC�̎��s���Ԃ��A臒l�𒴂������Ƃ����o����Rule�B</br>
 * 
 * Performance Doctor�ɏo�͂�����e</br> <li>臒l�Ɏw�肵�����s���Ԃ��z�����ꍇ�A臒l�Ǝ��s�p�x���o�͂���B
 * 
 * @author fujii
 */
public class GCTimeRule extends SingleElementRule
{
    /** �x���Ɣ��f����GC���s���Ԃ�臒l(�P��:�~���b) */
    public double            threshold;

    /** �~���b����b�ւ̒P�ʕϊ��萔 */
    private static final int CONVERT_TO_SEC = 1000;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doJudgeElement(final JavelinLogElement element)
    {
        // Return���O����GC���s���ԍ������擾���A臒l�Ƃ̔�r���s���B
        // CPU�������ԍ������擾���A�wGC���s�񐔍����^CPU�������ԍ����x���wGC���s�p�x�x���Z�o
        // ��ʂ��`�F�b�N����BCall�ȊO�ł���Ύ��̗v�f�ցB

        String type = element.getBaseInfo().get(JavelinLogColumnNum.ID);
        boolean isCall = JavelinConstants.MSG_CALL.equals(type);
        if (isCall == false)
        {
            return;
        }

        // JMX�����擾���A����ɂ��̒�����getCheckParamName()�̖��O�ɑΉ�����l���擾����B
        Map<String, String> jmxInfoMap =
                                         JavelinLogUtil.parseDetailInfo(element,
                                                                        JavelinParser.TAG_TYPE_JMXINFO);

        double gcTime =
                        getDoubleValue(jmxInfoMap,
                                       JavelinLogConstants.JMXPARAM_GARBAGECOLLECTOR_COLLECTION_TIME_DELTA);
        double gcCount =
                         getDoubleValue(jmxInfoMap,
                                        JavelinLogConstants.JMXPARAM_GARBAGECOLLECTOR_COLLECTION_COUNT_DELTA);

        int duration = getDuration(element);
        if (duration == 0)
        {
            return;
        }
        // 1000�������邱�Ƃɂ��P�ʂ��u��/sec�v�ɂ���
        double gcFrequency = CONVERT_TO_SEC * gcCount / duration;
        if (gcTime >= this.threshold)
        {
            addError(element, this.threshold, gcTime, gcFrequency);
        }
    }

    /**
     * duration���擾����B
     * 
     * @param element
     *            JavelinLogElement
     * @return duration
     */
    private int getDuration(final JavelinLogElement element)
    {
        // ExtraInfo�̓��e��\��Map���擾����B
        Map<String, String> map =
                                  JavelinLogUtil.parseDetailInfo(element,
                                                                 JavelinParser.TAG_TYPE_EXTRAINFO);

        // ���\�b�h��TAT�̒l�𓾂�B
        String durationString = map.get(JavelinLogConstants.EXTRAPARAM_DURATION);
        if (durationString == null)
        {
            return 0;
        }

        int duration = Integer.parseInt(durationString);
        return duration;
    }

    /**
     * Map����key�ɑΉ�����l��Double�^�Ƃ��Ď擾����B
     * 
     * @param jmxInfoMap
     *            �����Ώ�Map
     * @param key
     *            �擾�L�[
     * @return �Ή�����l
     */
    protected double getDoubleValue(final Map<String, String> jmxInfoMap, final String key)
    {
        String valueStr = jmxInfoMap.get(key);

        if (valueStr == null)
        {
            return 0.0;
        }
        double value = Double.parseDouble(valueStr);

        return value;
    }
}
