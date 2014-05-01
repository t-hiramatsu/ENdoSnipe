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

import java.sql.Timestamp;

/**
 * �I�u�W�F�N�g���|�[�g�ɏo�͂���P���R�[�h���̏���ێ�����G���e�B�e�B�B
 * 
 * @author akiba
 */
public class ObjectRecord
{
    /** �v������ */
    private Timestamp measurementTime_;
    
    /** List�̃T�C�Y[�v�f��]�i��Ԋ��ԕ��ρj */
    private long      listSize_;
    
    /** List�̃T�C�Y[�v�f��]�i��Ԋ��ԍő�j */
    private long      listSizeMax_;
    
    /** List�̃T�C�Y[�v�f��]�i��Ԋ��ԍŏ��j */
    private long      listSizeMin_;

    /** Queue�̃T�C�Y[�v�f��]�i��Ԋ��ԕ��ρj */
    private long      queueSize_;
    
    /** Queue�̃T�C�Y[�v�f��]�i��Ԋ��ԍő�j */
    private long      queueSizeMax_;
    
    /** Queue�̃T�C�Y[�v�f��]�i��Ԋ��ԍŏ��j */
    private long      queueSizeMin_;

    /** Set�̃T�C�Y[�v�f��]�i��Ԋ��ԕ��ρj */
    private long      setSize_;
    
    /** Set�̃T�C�Y[�v�f��]�i��Ԋ��ԍő�j */
    private long      setSizeMax_;
    
    /** Set�̃T�C�Y[�v�f��]�i��Ԋ��ԍŏ��j */
    private long      setSizeMin_;

    /** Map�̃T�C�Y[�v�f��]�i��Ԋ��ԕ��ρj */
    private long      mapSize_;
    
    /** Map�̃T�C�Y[�v�f��]�i��Ԋ��ԍő�j */
    private long      mapSizeMax_;
    
    /** Map�̃T�C�Y[�v�f��]�i��Ԋ��ԍŏ��j */
    private long      mapSizeMin_;

    /** �I�u�W�F�N�g�T�C�Y[�v�f��]�i��Ԋ��ԕ��ρj */
    private long      objectSize_;
    
    /** �I�u�W�F�N�g�T�C�Y[�v�f��]�i��Ԋ��ԍő�j */
    private long      objectSizeMax_;
    
    /** �I�u�W�F�N�g�T�C�Y[�v�f��]�i��Ԋ��ԍŏ��j */
    private long      objectSizeMin_;

    /** �I�u�W�F�N�g��[�v�f��]�i��Ԋ��ԕ��ρj */
    private long      objectNum_;
    
    /** �I�u�W�F�N�g��[�v�f��]�i��Ԋ��ԍő�j */
    private long      objectNumMax_;
    
    /** �I�u�W�F�N�g��[�v�f��]�i��Ԋ��ԍŏ��j */
    private long      objectNumMin_;

	/**
	 * @return the measurementTime_
	 */
	public Timestamp getMeasurementTime() {
		return measurementTime_;
	}

	/**
	 * @param measurementTime the measurementTime_ to set
	 */
	public void setMeasurementTime(Timestamp measurementTime) {
		this.measurementTime_ = measurementTime;
	}

	/**
	 * @return the listSize_
	 */
	public long getListSize() {
		return listSize_;
	}

	/**
	 * @param listSize the listSize_ to set
	 */
	public void setListSize(long listSize) {
		this.listSize_ = listSize;
	}

	/**
	 * @return the listSizeMax_
	 */
	public long getListSizeMax() {
		return listSizeMax_;
	}

	/**
	 * @param listSizeMax the listSizeMax_ to set
	 */
	public void setListSizeMax(long listSizeMax) {
		this.listSizeMax_ = listSizeMax;
	}

	/**
	 * @return the listSizeMin_
	 */
	public long getListSizeMin() {
		return listSizeMin_;
	}

	/**
	 * @param listSizeMin the listSizeMin_ to set
	 */
	public void setListSizeMin(long listSizeMin) {
		this.listSizeMin_ = listSizeMin;
	}

	/**
	 * @return the queueSize_
	 */
	public long getQueueSize() {
		return queueSize_;
	}

	/**
	 * @param queueSize the queueSize_ to set
	 */
	public void setQueueSize(long queueSize) {
		this.queueSize_ = queueSize;
	}

	/**
	 * @return the queueSizeMax_
	 */
	public long getQueueSizeMax() {
		return queueSizeMax_;
	}

	/**
	 * @param queueSizeMax the queueSizeMax_ to set
	 */
	public void setQueueSizeMax(long queueSizeMax) {
		this.queueSizeMax_ = queueSizeMax;
	}

	/**
	 * @return the queueSizeMin_
	 */
	public long getQueueSizeMin() {
		return queueSizeMin_;
	}

	/**
	 * @param queueSizeMin the queueSizeMin_ to set
	 */
	public void setQueueSizeMin(long queueSizeMin) {
		this.queueSizeMin_ = queueSizeMin;
	}

	/**
	 * @return the setSize_
	 */
	public long getSetSize() {
		return setSize_;
	}

	/**
	 * @param setSize the setSize_ to set
	 */
	public void setSetSize(long setSize) {
		this.setSize_ = setSize;
	}

	/**
	 * @return the setSizeMax_
	 */
	public long getSetSizeMax() {
		return setSizeMax_;
	}

	/**
	 * @param setSizeMax the setSizeMax_ to set
	 */
	public void setSetSizeMax(long setSizeMax) {
		this.setSizeMax_ = setSizeMax;
	}

	/**
	 * @return the setSizeMin_
	 */
	public long getSetSizeMin() {
		return setSizeMin_;
	}

	/**
	 * @param setSizeMin the setSizeMin_ to set
	 */
	public void setSetSizeMin(long setSizeMin) {
		this.setSizeMin_ = setSizeMin;
	}

	/**
	 * @return the mapSize_
	 */
	public long getMapSize() {
		return mapSize_;
	}

	/**
	 * @param mapSize the mapSize_ to set
	 */
	public void setMapSize(long mapSize) {
		this.mapSize_ = mapSize;
	}

	/**
	 * @return the mapSizeMax_
	 */
	public long getMapSizeMax() {
		return mapSizeMax_;
	}

	/**
	 * @param mapSizeMax the mapSizeMax_ to set
	 */
	public void setMapSizeMax(long mapSizeMax) {
		this.mapSizeMax_ = mapSizeMax;
	}

	/**
	 * @return the mapSizeMin_
	 */
	public long getMapSizeMin() {
		return mapSizeMin_;
	}

	/**
	 * @param mapSizeMin the mapSizeMin_ to set
	 */
	public void setMapSizeMin(long mapSizeMin) {
		this.mapSizeMin_ = mapSizeMin;
	}

	/**
	 * @return the objectSize_
	 */
	public long getObjectSize() {
		return objectSize_;
	}

	/**
	 * @param objectSize the objectSize_ to set
	 */
	public void setObjectSize(long objectSize) {
		this.objectSize_ = objectSize;
	}

	/**
	 * @return the objectSizeMax_
	 */
	public long getObjectSizeMax() {
		return objectSizeMax_;
	}

	/**
	 * @param objectSizeMax the objectSizeMax_ to set
	 */
	public void setObjectSizeMax(long objectSizeMax) {
		this.objectSizeMax_ = objectSizeMax;
	}

	/**
	 * @return the objectSizeMin_
	 */
	public long getObjectSizeMin() {
		return objectSizeMin_;
	}

	/**
	 * @param objectSizeMin the objectSizeMin_ to set
	 */
	public void setObjectSizeMin(long objectSizeMin) {
		this.objectSizeMin_ = objectSizeMin;
	}

	/**
	 * @return the objectNum_
	 */
	public long getObjectNum() {
		return objectNum_;
	}

	/**
	 * @param objectNum the objectNum_ to set
	 */
	public void setObjectNum(long objectNum) {
		this.objectNum_ = objectNum;
	}

	/**
	 * @return the objectNumMax_
	 */
	public long getObjectNumMax() {
		return objectNumMax_;
	}

	/**
	 * @param objectNumMax the objectNumMax_ to set
	 */
	public void setObjectNumMax(long objectNumMax) {
		this.objectNumMax_ = objectNumMax;
	}

	/**
	 * @return the objectNumMin_
	 */
	public long getObjectNumMin() {
		return objectNumMin_;
	}

	/**
	 * @param objectNumMin the objectNumMin_ to set
	 */
	public void setObjectNumMin(long objectNumMin) {
		this.objectNumMin_ = objectNumMin;
	}
}
