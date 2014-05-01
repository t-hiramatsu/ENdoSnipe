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
package jp.co.acroquest.endosnipe.report.entity;

import java.util.List;

import jp.co.acroquest.endosnipe.report.converter.compressor.CompressOperator;
import jp.co.acroquest.endosnipe.report.entity.ItemRecord;

/**
 * �n�񂲂ƂɃO���t���o�͂���ꍇ�̃f�[�^��
 * �ێ�����N���X�ł��B
 * 
 * @author ochiai
 */
public class ItemData
{
	/** �n�� */
    private String itemName_;
    
	/** ���k���@ */
    private CompressOperator operator_;
    
    /** ��Ԋ��ԕ��ρ@*/
    private List<ItemRecord> records_;
    
    /**
	 * @return the itemName_
	 */
	public String getItemName()
	{
		return itemName_;
	}

	/**
	 * @param itemName the itemName_ to set
	 */
	public void setItemName(String itemName)
	{
		this.itemName_ = itemName;
	}
	
    /**
	 * @return the operator_
	 */
	public CompressOperator getOperator()
	{
		return operator_;
	}

	/**
	 * @param operator the operator_ to set
	 */
	public void setOperator(CompressOperator operator)
	{
		this.operator_ = operator;
	}
	
	/**
	 * @return the records_
	 */
	public List<ItemRecord> getRecords()
	{
		return records_;
	}

	/**
	 * @param records the records_ to set
	 */
	public void setRecords(List<ItemRecord> records)
	{
		this.records_ = records;
	}
}
