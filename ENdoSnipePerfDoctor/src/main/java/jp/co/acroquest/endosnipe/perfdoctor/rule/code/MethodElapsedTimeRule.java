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

import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.parser.JavelinConstants;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogColumnNum;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogConstants;
import jp.co.acroquest.endosnipe.javelin.JavelinLogUtil;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;
import jp.co.acroquest.endosnipe.perfdoctor.rule.SingleElementRule;

/**
 * ���\�b�h�̎��������ԃ��[��
 * 
 * @author Sakamoto
 * 
 */
public class MethodElapsedTimeRule extends SingleElementRule
{

    /** �x���Ɣ��f����臒l�̃f�t�H���g�l�B */
    private static final int DEFAULT_THRESHOLD = 5000;

    /**
     * ���������Ԃ�臒l�B���̒l�ɒB�����ۂɌx���𐶐�����B
     */
    public long              threshold         = DEFAULT_THRESHOLD;

    /**
     * CALL���O���̃��\�b�h�̎��������Ԃ̒l�𒲍����A 臒l�ɒB����̂ł���Όx������B
     * 
     * @param javelinLogElement
     *            ���O�̗v�f
     * 
     */
    @Override
    public void doJudgeElement(final JavelinLogElement javelinLogElement)
    {
        // ���O�̎�ʂ��`�F�b�N����BCall�ȊO�ł���Ύ��̗v�f�ցB
        List<String> baseInfo = javelinLogElement.getBaseInfo();
        String type = baseInfo.get(JavelinLogColumnNum.ID);
        boolean isCall = JavelinConstants.MSG_CALL.equals(type);

        if (isCall == false)
        {
            return;
        }

        // SQL���s�͏��O����
        String className = baseInfo.get(JavelinLogColumnNum.CALL_CALLEE_CLASS);
        if (isSqlExec(className) == true)
        {
            return;
        }

        // ���\�b�h�̎��������Ԃ̒l�𓾂�B
        Map<String, String> extraInfo =
                                        JavelinLogUtil.parseDetailInfo(javelinLogElement,
                                                                       JavelinParser.TAG_TYPE_EXTRAINFO);
        String durationString = extraInfo.get(JavelinLogConstants.EXTRAPARAM_ELAPSEDTIME);

        // ���������Ԃ�������Ύ��̗v�f�ɐi��
        if (durationString == null)
        {
            return;
        }

        double duration = Double.parseDouble(durationString);

        // �������o�l��臒l�ɒB����̂ł���΁A�x�����o���B
        if (duration >= this.threshold)
        {
            addError(javelinLogElement, this.threshold, duration);
        }
    }

}
