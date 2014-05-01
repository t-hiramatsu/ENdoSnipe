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
import jp.co.acroquest.endosnipe.javelin.JavelinLogUtil;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.rule.SingleElementRule;

/**
 * ���\�b�h��TAT���[��
 * 
 * @author tooru
 * 
 */
public class MethodTatRule extends SingleElementRule
{

    /** ���\�b�h��TAT��\�������� */
    private static final String DURATION          = "duration";

    /** ExtraInfo��\�������� */
    private static final String EXTRA_INFO        = "ExtraInfo";

    /** Call���O�̊J�n�^�O */
    private static final String CALL_TAG          = JavelinConstants.MSG_CALL;

    /** �x���Ɣ��f����OR/UNION�񐔂̃f�t�H���g�l�B */
    private static final int    DEFAULT_THRESHOLD = 5000;

    /**
     * TAT��臒l�B���̒l�𒴂����ۂɌx���𐶐�����B
     */
    public long                 threshold         = DEFAULT_THRESHOLD;

    /**
     * CALL���O���̃��\�b�h��TAT�̒l�𒲍����A 臒l�𒴂��Ă����ۂɂ͌x������B
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
        boolean isCall = CALL_TAG.equals(type);

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

        // ExtraInfo�̓��e��\��Map���擾����B
        Map<String, String> map = JavelinLogUtil.parseDetailInfo(javelinLogElement, EXTRA_INFO);

        // ���\�b�h��TAT�̒l�𓾂�B
        String durationString = map.get(DURATION);
        long duration = 0;

        duration = Long.parseLong(durationString);

        // �������o�l��臒l�ɒB����̂ł���΁A�x�����o���B
        if (duration >= this.threshold)
        {
            addError(javelinLogElement, this.threshold, duration);
        }
    }

}
