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

import jp.co.acroquest.endosnipe.common.parser.JavelinConstants;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogConstants;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;
import jp.co.acroquest.endosnipe.perfdoctor.rule.AbstractSingleValueLimitRule;

/**
 * �P��̏����ӂ�̃l�b�g���[�N�o�͗ʂ�臒l�ȏ�ɂȂ����ꍇ�ɏo�͂��郋�[��
 * 
 * @author S.Kimura
 */
public class NetworkOutputRule extends AbstractSingleValueLimitRule
{
    /**
     * RETURN���O��������ʂ�Ԃ��B
     * 
     * @return RETURN���O���
     */
    @Override
    protected String getTargetID()
    {
        return JavelinConstants.MSG_CALL;
    }

    /**
     * ���o�͏����������^�O��Ԃ��B
     * 
     * @return ���o�͏��^�O
     */
    @Override
    protected String getTargetInfoTag()
    {
        return JavelinParser.TAG_TYPE_EXTRAINFO;
    }

    /**
     * �l�b�g���[�N�o�͗ʂ������p�����[�^���̂�Ԃ��B
     * 
     * @return �l�b�g���[�N�o�͗ʃp�����[�^����
     */
    @Override
    protected String getTargetValueName()
    {
        return JavelinLogConstants.IOPARAM_NETWORK_OUTPUT;
    }
}
