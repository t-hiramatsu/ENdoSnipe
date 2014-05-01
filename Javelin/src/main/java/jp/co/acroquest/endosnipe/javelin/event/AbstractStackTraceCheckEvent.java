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
package jp.co.acroquest.endosnipe.javelin.event;


/**
 * �X�^�b�N�g���[�X�̓��e�ɂ���āA�C�x���g�o�̗͂}�����s���C�x���g�N���X�ł��B<br />
 * 
 * @author fujii
 *
 */
abstract class AbstractStackTraceCheckEvent extends CommonEvent
{
    /** ��r�̂��߂̃X�^�b�N�g���[�X�B */
    protected String stackTraceToCompare_;

    /** �C�x���g���ƃX�^�b�N�g���[�X�̃Z�p���[�^ */
    private static final char SEPARATOR = '#';

    /** �X�^�b�N�g���[�X��\���p�����[�^ */
    protected String paramStackTrace_;

    /**
     * �p�����[�^��ۑ�����Map�ɒl��ۑ����܂��B<br />
     * ���̂Ƃ��A�p�����[�^��"stackTrace"(�啶���E�������̔��ʂ͍s��Ȃ�)�ŏI���Ȃ�΁A��r�p�ɃX�^�b�N�g���[�X��ۑ����܂��B<br />
     * 
     * @param key �L�[�B
     * @param value �l�B
     */
    public void addParam(String key, String value)
    {
        if (key.equals(this.paramStackTrace_))
        {
            setStackTraceCompare(value);
        }
        super.addParam(key, value);
    }

    /**
     * �X�^�b�N�g���[�X�Ńn�b�V���R�[�h���v�Z����B
     * 
     * @return �n�b�V���R�[�h�B
     */
    public int hashCode()
    {
        String stackTrace = this.name_ + SEPARATOR + this.stackTraceToCompare_;

        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((stackTrace == null) ? 0 : stackTrace.hashCode());
        return result;
    }

    /**
     * �X�^�b�N�g���[�X�Ŕ�r����B
     * 
     * @param obj ��r�ΏہB
     * @return ��r���ʁB
     */
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        AbstractStackTraceCheckEvent other = (AbstractStackTraceCheckEvent)obj;

        String stackTrace = this.name_ + SEPARATOR + this.stackTraceToCompare_;
        String stackTraceOther = other.getName() + SEPARATOR + other.stackTraceToCompare_;

        return stackTrace.equals(stackTraceOther);
    }

    /**
     * ��r�p�X�^�b�N�g���[�X�l��ݒ肷��B
     * 
     * @param stackTrace �X�^�b�N�g���[�X
     */
    abstract void setStackTraceCompare(String stackTrace);

}
