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
package jp.co.acroquest.endosnipe.common.parser;

/**
 * Javelin�Ŏg�p����萔�C���^�[�t�F�[�X�ł��B<br />
 * 
 * @author eriguchi
 */
public interface JavelinConstants
{
    /**
     * ���������ŁA���샍�O�̎�ނ���ʂ��邽�߂Ɏg�p����"Call"��ID�B <br />
     */
    int ID_CALL = 0;

    /**
     * ���������ŁA���샍�O�̎�ނ���ʂ��邽�߂Ɏg�p����"Return"��ID�B <br />
     */
    int ID_RETURN = 1;

    /**
     * ���������ŁA���샍�O�̎�ނ���ʂ��邽�߂Ɏg�p����"Read"��ID�B <br />
     */
    int ID_FIELD_READ = 2;

    /**
     * ���������ŁA���샍�O�̎�ނ���ʂ��邽�߂Ɏg�p����"Write"��ID�B <br />
     */
    int ID_FIELD_WRITE = 3;

    /**
     * ���������ŁA���샍�O�̎�ނ���ʂ��邽�߂Ɏg�p����"Catch"��ID�B <br />
     */
    int ID_CATCH = 4;

    /**
     * ���������ŁA���샍�O�̎�ނ���ʂ��邽�߂Ɏg�p����"Throw"��ID�B <br />
     */
    int ID_THROW = 5;

    /**
     * ���������ŁA���샍�O�̎�ނ���ʂ��邽�߂Ɏg�p����"Event"��ID�B <br />
     */
    int ID_EVENT = 6;

    /**
     * ���샍�O�ɏo�͂���"Return"��\��������B<br />
     */
    String MSG_RETURN = "Return";

    /**
     * ���샍�O�ɏo�͂���"Call"��\��������B<br />
     */
    String MSG_CALL = "Call  ";

    /**
     * ���샍�O�ɏo�͂���"Read"��\��������B<br />
     */
    String MSG_FIELD_READ = "Read  ";

    /**
     * ���샍�O�ɏo�͂���"Write"��\��������B<br />
     */
    String MSG_FIELD_WRITE = "Write ";

    /**
     * ���샍�O�ɏo�͂���"Catch"��\��������B<br />
     */
    String MSG_CATCH = "Catch ";

    /**
     * ���샍�O�ɏo�͂���"Throw"��\��������B<br />
     */
    String MSG_THROW = "Throw ";

    /**
     * ���샍�O�ɏo�͂���"Throw"��\��������B<br />
     */
    String MSG_EVENT = "Event ";
}
