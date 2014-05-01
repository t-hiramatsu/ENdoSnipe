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
package jp.co.acroquest.endosnipe.data.entity;

import java.sql.Timestamp;

/**
 * Javelin �v���l���A�[�J�C�u�e�[�u���ɑ΂���G���e�B�e�B�N���X�ł��B<br />
 *
 * @author y-sakamoto
 */
public class ArchivedValue
{

    /**
     * �v���l ID �B<br />
     *
     * �v���l����ӂɎ��ʂ��� ID �B
     */
    public long measurementValueId;

    /**
     * �v�� No.�@�B<br />
     *
     * �����Ɍv�����ꂽ�v���l�Q��R�Â��邽�߂� ID �B<br />
     *
     * �����Ɍv�����ꂽ�v���l�� MESUREMENT_ID �������l�ƂȂ�܂��B<br />
     */
    public long measurementNum;

    /**
     * �v���f�[�^���擾�����z�X�g�� ID �B<br />
     */
    public int hostId;

    /**
     * �v�������B<br />
     */
    public Timestamp measurementTime;

    /**
     * �v���l��ʁB<br />
     *
     * �v���l�̎�ʂ�\���l�B<br />
     *
     * �v���l�̕\�����̂ɂ��Ă� MESUREMENT_INFO �e�[�u�����Q�Ƃ��܂��B
     */
    public int measurementType;

    /**
     * �v���l���n�񖼂����ꍇ�i�R���N�V�������Ȃǁj�̌n�� ID �B<br />
     */
    public int measurementItemId;

    /**
     * �v���l�B<br />
     *
     * ���ۂ̌v���l�B
     */
    public Number value;

}
