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
package jp.co.acroquest.endosnipe.common.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * 1�s��CSV�`���̃f�[�^����͂��A���ꂼ��̍��ڂɕ�������N���X�B
 * CSV�`���ɑΉ����� java.util.StringTokenizer �̂悤�Ȃ��́B
 *
 *�|�|�|�|�|�|�|�|�|�|�|�|�|�d�l�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|
 *�@�E�Z�p���[�^�́A���p�J���}(,)��p����B
 *
 *�@�E�e�v�f�́A���p�̃_�u���N�H�[�e�[�V����(")�ň͂�ł��͂܂Ȃ��Ă��ǂ��B
 *�@�@�������A
 *�@�@�@���@�v�f���ɔ��p�J���}���܂ޏꍇ�́A�_�u���N�H�[�e�[�V������
 *�@�@�@�@�@�͂܂Ȃ��Ă͂Ȃ�Ȃ��B
 *
 *�@�E�_�u���N�H�[�e�[�V�����𕶎���̈ꕔ�Ƃ��ĔF��������ꍇ�́A
 *�@�@�A�������_�u���N�H�[�e�[�V������p����B
 *�@�@�P�Ƃ̃_�u���N�H�[�e�[�V�������������ꍇ�̓���͕ۏ؂��Ȃ��B
 *
 *�@�E�Z�p���[�^�̗����ɂ́A�X�y�[�X����ꂽ�ꍇ�́A
 *�@�@�X�y�[�X�Ƃ��ĔF�������B
 * @author unknown
 */
public class CSVTokenizer implements Enumeration<String>
{
    private final String source_; // �ΏۂƂȂ镶����

    private int currentPosition_; // ���̓ǂݏo���ʒu

    private final int maxPosition_;

    /**
     * CSV �`���� line ����͂��� CSVTokenizer �̃C���X�^���X��
     * �쐬����B
     *
     * @param line CSV�`���̕�����  ���s�R�[�h���܂܂Ȃ��B
     */
    public CSVTokenizer(final String line)
    {
        source_ = line.trim();
        currentPosition_ = 0;
        maxPosition_ = source_.length();
    }

    /**
     * ���̃J���}������ʒu��Ԃ��B
     * �J���}���c���Ă��Ȃ��ꍇ�� nextComma() == maxPosition �ƂȂ�B
     * �܂��Ō�̍��ڂ���̏ꍇ�� nextComma() == maxPosition �ƂȂ�B
     *
     * @param ind �������J�n����ʒu
     * @return ���̃J���}������ʒu�B�J���}���Ȃ��ꍇ�́A�������
     * �����̒l�ƂȂ�B
     */
    private int nextComma(int ind)
    {
        boolean inquote = false;
        while (ind < maxPosition_)
        {
            char ch = source_.charAt(ind);
            if (!inquote && ch == ',')
            {
                break;
            }
            else if ('"' == ch)
            {
                inquote = !inquote; // ""�̏����������OK
            }
            ind++;
        }
        return ind;
    }

    /**
     * �܂܂�Ă��鍀�ڂ̐���Ԃ��B
     *
     * @return �܂܂�Ă��鍀�ڂ̐�
     */
    public int countTokens()
    {
        int i = 0;
        int ret = 1;
        while ((i = nextComma(i)) < maxPosition_)
        {
            i++;
            ret++;
        }
        return ret;
    }

    /**
     * ���̍��ڂ̕������Ԃ��B
     *
     * @return ���̍���
     */
    public String nextToken()
    {
        // ">=" �ł͖����̍��ڂ𐳂��������ł��Ȃ��B
        // �����̍��ڂ���i�J���}��1�s���I���j�ꍇ�A��O����������
        // ���܂��̂ŁB
        if (currentPosition_ > maxPosition_)
        {
            throw new NoSuchElementException(toString() + "#nextToken");
        }

        int st = currentPosition_;
        currentPosition_ = nextComma(currentPosition_);

        StringBuffer strb = new StringBuffer();
        boolean inquote = false;

        while (st < currentPosition_)
        {
            char ch = source_.charAt(st++);
            if (ch == '"')
            {
                // quote�̊O�ł���΁A���ł�quote�̒��ɓ���
                if (inquote == false)
                {
                    inquote = true;
                }
                // quote���ł���A���̎��̕�����"�ł���΁A1��������"�Ƃ��Ĉ���
                // "���P�ƂŌ��ꂽ�Ƃ��͉������Ȃ�
                else if ((st < currentPosition_) && (source_.charAt(st) == '"'))
                {
                    strb.append(ch);
                    st++;
                }
                // ����ȊO�ł����quote���o��B
                else
                {
                    inquote = false;
                }
            }
            else
            {
                strb.append(ch);
            }
        }
        currentPosition_++;
        return new String(strb);
    }

    /**
     * <code>nextToken</code>���\�b�h�Ɠ����ŁA
     * ���̍��ڂ̕������Ԃ��B<br>
     * �������Ԓl�́AString�^�ł͂Ȃ��AObject�^�ł���B<br>
     * java.util.Enumeration���������Ă��邽�߁A���̃��\�b�h��
     * ����B
     *
     * @return ���̍���
     * @see java.util.Enumeration
     */
    public String nextElement()
    {
        return nextToken();
    }

    /**
     * �܂����ڂ��c���Ă��邩�ǂ������ׂ�B
     *
     * @return �܂����ڂ��̂����Ă���Ȃ�true
     */
    public boolean hasMoreTokens()
    {
        // "<=" �łȂ��A"<" ���Ɩ����̍��ڂ𐳂��������ł��Ȃ��B
        return (nextComma(currentPosition_) <= maxPosition_);
    }

    /**
     * <code>hasMoreTokens</code>���\�b�h�Ɠ����ŁA
     * �܂����ڂ��c���Ă��邩�ǂ������ׂ�B<br>
     * java.util.Enumeration���������Ă��邽�߁A���̃��\�b�h��
     * ����B
     *
     * @return �܂����ڂ��̂����Ă���Ȃ�true
     * @see java.util.Enumeration
     */
    public boolean hasMoreElements()
    {
        return hasMoreTokens();
    }

    /**
     * �C���X�^���X�̕�����\����Ԃ��B
     *
     * @return �C���X�^���X�̕�����\���B
     */
    @Override
    public String toString()
    {
        return "CSVTokenizer(\"" + source_ + "\")";
    }
}
