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
package jp.co.acroquest.endosnipe.perfdoctor.exception;

import jp.co.acroquest.endosnipe.perfdoctor.Messages;

/**
 * ���[���̍쐬���s��O�B
 * @author tanimoto
 *
 */
public class RuleCreateException extends Exception
{
    private static final long serialVersionUID = 1L;

    private String[]          messages_;

    /**
     * �R���X�g���N�^�B
     * @param messageId ���b�Z�[�WID
     * @param args ���b�Z�[�W����
     */
    public RuleCreateException(final String messageId, final Object[] args)
    {
        super(Messages.getMessage(messageId, args));
    }

    /**
     * �R���X�g���N�^�B
     * �����̃��b�Z�[�W��ʒm���邱�Ƃ��ł���B
     * @param messageId ���b�Z�[�WID
     * @param args ���b�Z�[�W����
     * @param messages ��ʂɓ`�B���郁�b�Z�[�W�ꗗ
     */
    public RuleCreateException(final String messageId, final Object[] args, final String[] messages)
    {
        super(Messages.getMessage(messageId, args));
        if (messages == null)
        {
        	this.messages_ = null;
        }
        else 
        {
        	this.messages_ = (String[]) messages.clone();
        }
    }

    /**
     * ���b�Z�[�W�ꗗ���擾����B
     * @return ���b�Z�[�W�ꗗ
     */
    public String[] getMessages()
    {
    	if (this.messages_ == null)
    	{
    		return null;
    	}
    	else 
    	{
    		return (String[]) this.messages_.clone();
    	}
    }
}
