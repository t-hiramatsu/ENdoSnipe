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
package jp.co.acroquest.endosnipe.perfdoctor.rule;

/**
 * �V���A���C�Y�����ꂽ���[�����i�[����N���X�B<br />
 * 
 * @author sakamoto
 */
public class SerializedRules
{

	/** ruleSetConfigMap_�̓��e���ꎞ�I�ɕۑ����Ă����z�� */
	private final byte[] ruleSetConfigMapData_;

	/** ruleSetMap_�̓��e���ꎞ�I�ɕۑ����Ă����z��B */
	private final byte[] ruleSetMapData_;

	/**
	 * �V���A���C�Y�����ꂽ���[�����i�[����I�u�W�F�N�g�𐶐����܂��B<br />
	 * 
	 * @param ruleSetConfigMapData
	 *            ruleSetConfigMap ���V���A���C�Y�������f�[�^
	 * @param ruleSetMapData
	 *            ruleSetMap ���V���A���C�Y�������f�[�^
	 */
	public SerializedRules(final byte[] ruleSetConfigMapData,
			final byte[] ruleSetMapData)
	{
		if (ruleSetConfigMapData == null)
		{
			this.ruleSetConfigMapData_ = null;
		}
		else
		{
			this.ruleSetConfigMapData_ = (byte[]) ruleSetConfigMapData.clone();
		}

		if (ruleSetMapData == null)
		{
			this.ruleSetMapData_ = null;
		}
		else
		{
			this.ruleSetMapData_ = (byte[]) ruleSetMapData.clone();
		}
	}

	/**
	 * ruleSetConfigMap ���V���A���C�Y�������f�[�^��Ԃ��܂��B<br />
	 * 
	 * @return ruleSetConfigMap ���V���A���C�Y�������f�[�^
	 */
	public byte[] getRuleSetConfigMapData()
	{
		if (ruleSetConfigMapData_ == null)
		{
			return null;
		}
		return (byte[]) ruleSetConfigMapData_.clone();
	}

	/**
	 * ruleSetMap ���V���A���C�Y�������f�[�^��Ԃ��܂��B<br />
	 * 
	 * @return ruleSetMap ���V���A���C�Y�������f�[�^
	 */
	public byte[] getRuleMapData()
	{
		if (ruleSetMapData_ == null)
		{
			return null;
		}
		return (byte[]) ruleSetMapData_.clone();
	}
}
