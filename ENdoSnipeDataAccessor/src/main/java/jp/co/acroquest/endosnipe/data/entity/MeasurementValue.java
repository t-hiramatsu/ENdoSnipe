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
 * Javelin �v���l�e�[�u���ɑ΂���G���e�B�e�B�N���X�ł��B<br /> 
 *
 * @author y-sakamoto
 */
public class MeasurementValue
{

    /**
     * �v�������B<br />
     */
    public Timestamp measurementTime;

    /**
     * �v���l���n�񖼂����ꍇ�i�R���N�V�������Ȃǁj�̌n�� ID �B<br />
     */
    public int measurementItemId;

    /**
     * �v���l�B<br />
     * ���ۂ̌v���l�B
     */
    public String value;

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append("measurementTime=");
        builder.append(this.measurementTime);
        builder.append(",measurementItemId=");
        builder.append(this.measurementItemId);
        builder.append(",value=");
        builder.append(this.value);
        builder.append(")");
        return builder.toString();
    }
}
