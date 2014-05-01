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
package jp.co.acroquest.endosnipe.perfdoctor.rule;

import java.util.Map;

import jp.co.acroquest.endosnipe.common.parser.JavelinLogColumnNum;
import jp.co.acroquest.endosnipe.javelin.JavelinLogUtil;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;

/**
 * �P���{@link JavelinLogElement}���́A �P��̒l��臒l�Ɣ�r���邱�ƂŃ`�F�b�N����V���v���ȃ��[���̂��߂̊�ꃋ�[��
 * �ȉ��̂R�̗v�f���w�肵�Ďg�p����B
 * 
 * JavelinLogElement�̎�ʁiID�j ��񕪗ރ^�O ���ؑΏےl�̖���
 * 
 * �G���[�o�͎��̈����ɂ́u臒l�v�Ɓu�����l�v���w��\
 * 
 * @author S.Kimura
 * 
 */
public abstract class AbstractSingleValueLimitRule extends SingleElementRule
{
    /** 臒l */
    public long threshold;

    /**
     * �Ώےl��臒l�ȏゾ�����ꍇ�̓G���[���o�͂��� �Ώےl�͎w�肳�ꂽID�A�^�O�A���̂��w�肵�Ď擾����
     * 
     * @param element
     *            ���ؑΏ�LogElement
     */
    @Override
    protected void doJudgeElement(final JavelinLogElement element)
    {
        // ��ʂ��`�F�b�N
        String type = element.getBaseInfo().get(JavelinLogColumnNum.ID);
        String targetType = getTargetID();

        boolean isTarget = targetType.equals(type);

        // �w�肳�ꂽ��ʈȊO�������ꍇ�͌��؏I��
        if (false == isTarget)
        {
            return;
        }

        // ��񕪗ރ^�O���w�肵�āA�����擾
        String targetInfoTag = getTargetInfoTag();
        Map<String, String> targetInfoMap = JavelinLogUtil.parseDetailInfo(element, targetInfoTag);

        // �w���񂪎擾�ł��Ȃ������ꍇ�͌��؏I��
        if (targetInfoMap == null)
        {
            return;
        }

        // ���ؑΏےl���擾
        String targetValueName = getTargetValueName();
        String targetValueStr = targetInfoMap.get(targetValueName);

        // �擾�ł��Ȃ������ꍇ�͌��؏I��
        if (targetValueStr == null)
        {
            return;
        }

        long targetValueLong = Long.parseLong(targetValueStr);

        if (targetValueLong >= this.threshold)
        {
            addError(element, this.threshold, targetValueLong);
        }
    }

    /**
     * �ΏۂƂ��郍�O��ʂ�Ԃ��B
     * 
     * @return �Ώۃ��O���
     */
    protected abstract String getTargetID();

    /**
     * �ΏۂƂ����񕪗ރ^�O���擾����
     * 
     * @return �Ώۏ�񕪗ރ^�O
     */
    protected abstract String getTargetInfoTag();

    /**
     * �ΏۂƂ���l�̖��̂��擾����
     * 
     * @return �Ώےl����
     */
    protected abstract String getTargetValueName();
}
