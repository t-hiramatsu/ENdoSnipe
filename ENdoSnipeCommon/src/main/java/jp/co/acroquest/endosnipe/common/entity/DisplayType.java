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
package jp.co.acroquest.endosnipe.common.entity;

/**
 * �v�������l�̕\�����@��\���񋓑̂ł��B<br />
 * �f�[�^�^�͈ȉ��̃R�[�h�Ŏ����܂��B<br />
 * <br />
 * <table border="1" cellspacing="0">
 * <tr>
 *  <th>�\�����@</th>
 *  <th>���l</th>
 * </tr>
 * <tr>
 *  <td>�s��</td>
 *  <td>-1</td>
 * </tr>
 * <tr>
 *  <td>�v���l�\��</td>
 *  <td>0</td>
 * </tr>
 * <tr>
 *  <td>�����\��</td>
 *  <td>1</td>
 * </tr>
 * </table>
 *
 * @author fujii
 */
public enum DisplayType
{
    /** �s�� */
    DISPLAYTYPE_UNKNOWN, // -1
    /** �v���l�\�� */
    DISPLAYTYPE_NORMAL, // 0
    /** �����\�� */
    DISPLAYTYPE_DIFFERENCE; // 1

    /**
     * ���l����f�[�^�^��Ԃ��B
     *
     * @param n ���l
     * @return �f�[�^�^
     */
    public static DisplayType getDisplayType(final byte n)
    {
        switch (n)
        {
        case -1:
            return DISPLAYTYPE_UNKNOWN;
        case 0:
            return DISPLAYTYPE_NORMAL;
        case 1:
            return DISPLAYTYPE_DIFFERENCE;
        default:
            throw new IllegalArgumentException();
        }
    }

    /**
     * �f�[�^�^�ɉ��������l���擾���܂��B
     *
     * @param type �f�[�^�^
     * @return ���l
     */
    public static byte getDisplayTypeNumber(final DisplayType type)
    {
        switch (type)
        {
        case DISPLAYTYPE_UNKNOWN:
            return -1;
        case DISPLAYTYPE_NORMAL:
            return 0;
        case DISPLAYTYPE_DIFFERENCE:
            return 1;
        default:
            throw new IllegalArgumentException();
        }
    }
}
