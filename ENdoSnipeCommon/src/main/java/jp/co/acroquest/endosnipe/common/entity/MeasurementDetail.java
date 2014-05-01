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
 * �v���f�[�^���i�[���邽�߂̃G���e�B�e�B�ł��B
 * 
 * @author fujii
 */
public class MeasurementDetail implements Cloneable
{
	/** �v���l */
	public String value;

	/** �n��̕\���� */
	public String displayName;

	/** �v���l���n�񖼂����ꍇ�i�R���N�V�������Ȃǁj�̌n�� ID �B */
	public int itemId;

	/** �v���l�n�񖼏́B */
	public String itemName;

	/** �v���l�̕\���^ */
	public int displayType;

	/**
	 * �v�� No.�B<br/>
	 * �����Ɍv�����ꂽ�v���l�Q��R�Â��邽�߂� ID �B<br/>
	 * �����Ɍv�����ꂽ�v���l�� MESUREMENT_ID �������l�ƂȂ�܂��B
	 */
	public long itemNum;

	/**
	 * �v���l��ʁB<br/>
	 * �v���l�̎�ʂ�\���l�B<br/>
	 * �v���l�̕\�����̂ɂ��Ă� MESUREMENT_INFO �e�[�u�����Q�Ƃ��܂��B
	 */
	public int type;

	/** �v���l�n�񖼏́B */
	public String typeItemName;

	/**
	 * �v���l ID �B<br/>
	 * �v���l����ӂɎ��ʂ��� ID �B
	 */
	public long valueId;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("DisplayName=");
		builder.append(this.displayName);
		builder.append(",Value=");
		builder.append(this.value);
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MeasurementDetail clone()
	{
		MeasurementDetail cloneObj = null;
		try
		{
			cloneObj = (MeasurementDetail) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			cloneObj = new MeasurementDetail();
		}

		cloneObj.displayName = this.displayName;
		cloneObj.itemId = this.itemId;
		cloneObj.itemName = this.itemName;
		cloneObj.itemNum = this.itemNum;
		cloneObj.type = this.type;
		cloneObj.typeItemName = this.typeItemName;
		cloneObj.value = this.value;
		cloneObj.valueId = this.valueId;
		cloneObj.displayType = this.displayType;

		return cloneObj;
	}
}
