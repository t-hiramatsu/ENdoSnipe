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
package jp.co.acroquest.endosnipe.perfdoctor.rule.dbaccess;

import java.util.List;

import jp.co.acroquest.endosnipe.common.parser.JavelinConstants;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogColumnNum;
import jp.co.acroquest.endosnipe.javelin.JavelinLogUtil;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.rule.SQLThresholdStrategy;
import jp.co.acroquest.endosnipe.perfdoctor.rule.SingleElementRule;

/**
 * DB �A�N�Z�X�Ɋւ��锻����s�����߂̊��N���X�B
 * 
 * @author y-komori
 */
public abstract class AbstractDbAccessRule extends SingleElementRule
{
    /**
     * �R���X�g���N�^�B
     */
    public AbstractDbAccessRule()
    {
        this.setThresholdStrategy(new SQLThresholdStrategy());
    }

    /**
     * @param element JavelinLogElement
     * @see jp.co.acroquest.endosnipe.perfdoctor.rule.SingleElementRule#doJudgeElement
     * (jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement)
     */
    @Override
    protected void doJudgeElement(final JavelinLogElement element)
    {
        List<String> baseInfoList = element.getBaseInfo();

        // �N���X���ɁASQL���s��\�������񂪏�����Ă��邩�ǂ����𒲂ׂ�B
        // ������Ă��Ȃ��ꍇ�A�������I������B
        String className = baseInfoList.get(JavelinLogColumnNum.CALL_CALLEE_CLASS);
        if (!super.isSqlExec(className))
        {
            return;
        }

        // ���O�̎�ʂ��`�F�b�N����BCall�ȊO�ł���Ώ������I������B
        String type = baseInfoList.get(JavelinLogColumnNum.ID);
        boolean isCall = JavelinConstants.MSG_CALL.equals(type);

        if (isCall == false)
        {
            return;
        }

        // JavelinLogElement�̖`������SQL���擾����B
        String sqlStatement = baseInfoList.get(JavelinLogColumnNum.CALL_CALLEE_METHOD);

        // SQL�̔�����s���B
        // args�̒���[VALUE]������ꍇ�́A���̒l���p����B
        String[] args = JavelinLogUtil.getArgs(element);
        String bindVal = null;
        for (int i = 0; i < args.length; i++)
        {
            bindVal = JavelinLogUtil.getArgContent(args[i], "[VALUE]");
            if (bindVal != null)
            {
                break;
            }
        }

        if (sqlStatement != null)
        {
            doJudgeContent(element, sqlStatement, bindVal);
        }

    }

    /**
     * �R���e���g�̔�����s���B<br />
     * {@link #getTagName()} ���\�b�h�̕Ԃ��^�O�Ɉ�v����R���e���g�̔�����s���B
     * 
     * @param element {@link JavelinLogElement} �I�u�W�F�N�g
     * @param content �R���e���g
     * @param bindVal ����
     */
    protected abstract void doJudgeContent(JavelinLogElement element, String content, String bindVal);

    /**
     * �^�O���̂�Ԃ��B<br />
     * <code>[Time]</code> �̌`���Ń^�O���̂�Ԃ��B<br />
     * �T�u�N���X�Ŏ������Ă��������B
     * 
     * @return �^�O����
     */
    protected abstract String getTagName();
}
