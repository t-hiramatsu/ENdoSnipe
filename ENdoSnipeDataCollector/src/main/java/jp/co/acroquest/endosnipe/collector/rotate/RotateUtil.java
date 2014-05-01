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
package jp.co.acroquest.endosnipe.collector.rotate;

import java.util.Calendar;

import jp.co.acroquest.endosnipe.collector.config.PeriodUnit;

/**
 * ���[�e�[�g�p���[�e�B���e�B�N���X�B
 *
 * @author sakamoto
 */
public class RotateUtil
{

    /**
     * �R���X�g���N�^���B�����܂��B
     */
    private RotateUtil()
    {
        // Do nothing.
    }

    /**
     * �ێ����Ԃ̒P�ʂ�Calendar�N���X�̃C���f�b�N�X�ɕϊ����܂��B
     * 
     * @param unit �ێ����Ԃ̒P��
     * @return Calendar�N���X�̃C���f�b�N�X
     */
    public static int convertUnit(final PeriodUnit unit)
    {
        int result;
        switch (unit)
        {
        case MONTH:
            result = Calendar.MONTH;
            break;
        case DAY:
            result = Calendar.DATE;
            break;
        default:
            result = Calendar.DATE;
            break;
        }
        return result;
    }

    /**
     * ���ݎ�������A�w�肵�����Ԃ����O�̎�����Ԃ��܂��B
     *
     * @param unit �P�ʁiCalendar�N���X�̃C���f�b�N�X�j
     * @param period ���ԁi���̒l�j
     * @return Calendar�C���X�^���X
     */
    public static Calendar getBeforeDate(final int unit, final int period)
    {
        Calendar deleteTimeCalendar = Calendar.getInstance();
        deleteTimeCalendar.add(unit, -1 * period);
        return deleteTimeCalendar;
    }
}
