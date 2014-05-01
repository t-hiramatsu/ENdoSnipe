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

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.parser.JavelinConstants;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogColumnNum;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogConstants;
import jp.co.acroquest.endosnipe.javelin.JavelinLogUtil;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;
import jp.co.acroquest.endosnipe.perfdoctor.rule.SingleElementRule;

/**
 * �v���Z�X�ɂ�����GC�̎��s�񐔂��A臒l�𒴂������Ƃ����o����Rule�B</br>
 * 
 * Performance Doctor�ɏo�͂�����e</br> <li>臒l�Ɏw�肵�����s�񐔂��z�����ꍇ�A臒l���o�͂���B
 * 
 * @author S.Kimura
 * @author fujii
 */
public class GCCountRule extends SingleElementRule
{
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(GCCountRule.class);

    /** �x���Ɣ��f����GC���s�p�x��臒l(�P��:��/�b) */
    public int                           threshold;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doJudgeElement(final JavelinLogElement element)
    {
        // Call���O����GC���s�񐔍����ACPU�������ԍ������擾���A
        // �wGC���s�񐔍����^CPU�������ԍ����x���wGC���s�p�x�x���Z�o�A臒l�Ƃ̔�r���s��

        // ��ʂ��`�F�b�N����BCall�ȊO�ł���Ύ��̗v�f�ցB

        String type = element.getBaseInfo().get(JavelinLogColumnNum.ID);
        boolean isReturn = JavelinConstants.MSG_CALL.equals(type);
        if (isReturn == false)
        {
            return;
        }

        // JMX�����擾���A����ɂ��̒�����getCheckParamName()�̖��O�ɑΉ�����l���擾����B
        Map<String, String> jmxInfoMap =
                                         JavelinLogUtil.parseDetailInfo(element,
                                                                        JavelinParser.TAG_TYPE_JMXINFO);

        int gcCount =
                      getIntValue(jmxInfoMap,
                                  JavelinLogConstants.JMXPARAM_GARBAGECOLLECTOR_COLLECTION_COUNT_DELTA);
        double gcTime =
                        getDoubleValue(jmxInfoMap,
                                       JavelinLogConstants.JMXPARAM_GARBAGECOLLECTOR_COLLECTION_TIME_DELTA);

        if (gcCount >= this.threshold)
        {
            addError(element, this.threshold, gcCount, gcTime);
        }
    }

    /**
     * Map����key�ɑΉ�����l��int�l�Ƃ��Ď擾���܂��B<br />
     * 
     * @param jmxInfoMap �����Ώ�Map
     * @param key �擾�L�[
     * @return �Ή�����l
     */
    @SuppressWarnings("deprecation")
    protected int getIntValue(final Map<String, String> jmxInfoMap, final String key)
    {
        String valueStr = jmxInfoMap.get(key);

        int value = 0;
        if (valueStr == null)
        {
            return 0;
        }
        try
        {
            value = Integer.parseInt(valueStr);
        }
        catch (NumberFormatException ex)
        {
            LOGGER.warn(ex);
        }
        return value;
    }

    /**
     * Map����key�ɑΉ�����l��Double�l�Ƃ��Ď擾���܂��B<br />
     * 
     * @param jmxInfoMap �����Ώ�Map
     * @param key �擾�L�[
     * @return �Ή�����l
     */
    @SuppressWarnings("deprecation")
    protected double getDoubleValue(final Map<String, String> jmxInfoMap, final String key)
    {
        String valueStr = jmxInfoMap.get(key);

        double value = 0;
        if (valueStr == null)
        {
            return 0.0;
        }
        try
        {
            value = Double.parseDouble(valueStr);
        }
        catch (NumberFormatException ex)
        {
            LOGGER.warn(ex);
        }
        return value;
    }
}
