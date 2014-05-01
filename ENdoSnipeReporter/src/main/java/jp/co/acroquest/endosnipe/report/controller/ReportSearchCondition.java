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
package jp.co.acroquest.endosnipe.report.controller;

import java.sql.Timestamp;
import java.util.List;

/**
 * ���|�[�g�o�͎��̍i���ݏ����������I�u�W�F�N�g�B
 * 
 * @author M.Yoshida
 */
public class ReportSearchCondition {

	/** �Ώۃf�[�^�x�[�X���X�g */
	private List<String> databases_;

	/** �J�n���� */
	private Timestamp startDate_;

	/** �I������ */
	private Timestamp endDate_;

	/** �o�̓p�X */
	private String outputFilePath_;

	/** ���|�[�g�o�͑Ώۂ̌v�����ڂ̐e�̍��ږ� */
	private String targetItemName_;

	/** PerformanceDoctor���|�[�g�o�͂ŁA���ꌴ�����i�荞�ނ��ǂ��� */
	private boolean limitSameCause_;

	/** PerformanceDoctor���|�[�g�o�͂ŁA���ꃋ�[���ōi�荞�ނ��ǂ��� */
	private boolean limitBySameRule_;


	/**
	 * @return the databases
	 */
	public List<String> getDatabases() {
		return databases_;
	}

	/**
	 * @return the startDate
	 */
	public Timestamp getStartDate() {
		return startDate_;
	}

	/**
	 * @return the endDate
	 */
	public Timestamp getEndDate() {
		return endDate_;
	}

	/**
	 * @return the outputFilePath
	 */
	public String getOutputFilePath() {
		return outputFilePath_;
	}

	/**
	 * @param databases
	 *            the databases to set
	 */
	public void setDatabases(List<String> databases) {
		databases_ = databases;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Timestamp startDate) {
		startDate_ = startDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Timestamp endDate) {
		endDate_ = endDate;
	}

	/**
	 * @param outputFilePath
	 *            the outputFilePath to set
	 */
	public void setOutputFilePath(String outputFilePath) {
		outputFilePath_ = outputFilePath;
	}

	/**
	 * @return the limitSameCause
	 */
	public boolean getLimitSameCause() {
		return this.limitSameCause_;
	}

	/**
	 * @param limitSameCause
	 *            the limitSameCause to set
	 */
	public void setLimitSameCause(boolean limitSameCause) {
		this.limitSameCause_ = limitSameCause;
	}

	/**
	 * @return the limitBySameRule
	 */
	public boolean getLimitBySameRule() {
		return this.limitBySameRule_;
	}

	/**
	 * @param limitBySameRule
	 *            the limitBySameRule to set
	 */
	public void setLimitBySameRule(boolean limitBySameRule) {
		this.limitBySameRule_ = limitBySameRule;
	}

	/**
	 * ���|�[�g�o�͑Ώۂ̌v�����ڂ̐e�̍��ږ����擾����B
	 * 
	 * @return ���|�[�g�o�͑Ώۂ̌v�����ڂ̐e�̍��ږ�
	 */
	public String getTargetItemName() {
		return targetItemName_;
	}

	/**
	 * ���|�[�g�o�͑Ώۂ̌v�����ڂ̐e�̍��ږ���ݒ肷��B
	 * 
	 * @param targetItemName
	 *            ���|�[�g�o�͑Ώۂ̌v�����ڂ̐e�̍��ږ�
	 */
	public void setTargetItemName(String targetItemName) {
		this.targetItemName_ = targetItemName;
	}

}
