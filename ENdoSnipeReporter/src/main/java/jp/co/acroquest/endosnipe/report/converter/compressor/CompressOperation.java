/*
 * Copyright (c) 2004-2009 SMG Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  SMG Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.report.converter.compressor;

import jp.co.acroquest.endosnipe.report.converter.compressor.CompressOperator;

/**
 * �T���v�����O�̈��k���s���ۂɁACompressor�Ɉ��k�����̕��@��
 * �w�肷�邽�߂̃G���e�B�e�B�I�u�W�F�N�g
 * 
 * @author M.Yoshida
 */
public class CompressOperation 
{
	/** ���k���̌v���l���Z�Ώۃt�B�[���h */
	private String           compressField_;

	/** ���k���̉��Z���� */
	private CompressOperator operation_;

	/**
	 * �R���X�g���N�^�B
	 * @param compressField �v���l���Z�Ώۃt�B�[���h
	 * @param operation     ���Z����
	 */
	public CompressOperation(String compressField,
			                 CompressOperator operation)
	{
		this.compressField_ = compressField;
		this.operation_     = operation;
	}

	public String getCompressField() {
		return compressField_;
	}

	public void setCompressField(String compressField) {
		this.compressField_ = compressField;
	}

	public CompressOperator getOperation() {
		return operation_;
	}

	public void setOperation(CompressOperator operation) {
		this.operation_ = operation;
	}
}
