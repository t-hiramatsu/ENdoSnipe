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
package jp.co.acroquest.endosnipe.report.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.report.entity.ReportItemValue;
import jp.co.acroquest.endosnipe.report.entity.VmStatusRecord;
import jp.co.acroquest.endosnipe.common.Constants;
import jp.co.acroquest.endosnipe.report.dao.ReportDao;

/**
 * VM��Ԍv���f�[�^�̃��|�[�g�o�͏����擾����A�N�Z�T�N���X�B
 * 
 * @author akiba
 */
public class VmStatusRecordAccessor
{
    /**
     * ���Ԃ��w�肵�A���̊��ԓ��ł�VM��Ԃ̃��|�[�g�f�[�^���擾����B<br/>
     * �擾����f�[�^�͈ȉ��̒ʂ�B<br/>
     * <ul>
     * 	<li>�X���b�h��</li>
     *  <li>GC��~����</li>
     *  <li>VM�X���[�v�b�g</li>
     *  <li>�t�@�C�i���C�Y�҂��I�u�W�F�N�g��</li>
     *  <li>�N���X���[�h��</li>
     * </ul>
     * 
     * @param database �f�[�^�x�[�X���B
     * @param startTime ��������(�J�n����)�B
     * @param endTime ��������(�I������)�B
     * @return VM��Ԃ̃��|�[�g�f�[�^�B
     * @throws SQLException �f�[�^�擾���ɗ�O�����������ꍇ
     */
    public List<VmStatusRecord> findVmStatusStaticsByTerm(String database, Timestamp startTime,
            Timestamp endTime) throws SQLException
    {
        List<VmStatusRecord> result = new ArrayList<VmStatusRecord>();

        // �f�[�^�x�[�X����l���擾����
        List<ReportItemValue> nativeThreadNumValues;
        List<ReportItemValue> threadNumValues;
        List<ReportItemValue> gcStopTimeValues;
        List<ReportItemValue> vmThroughputValues;
        List<ReportItemValue> finalizeObjNumValues;
        List<ReportItemValue> totalLoadedClassNumValues;
        List<ReportItemValue> loadedClassNumValues;
        

        // �X���b�h��(Native)
        nativeThreadNumValues = ReportDao.selectAverage(database, startTime,
                endTime, Constants.ITEMNAME_PROCESS_THREAD_TOTAL_COUNT);
        // �X���b�h��(Java)
    	threadNumValues = ReportDao.selectAverage(database, startTime,
    			endTime, Constants.ITEMNAME_JAVAPROCESS_THREAD_TOTAL_COUNT);
    	gcStopTimeValues = ReportDao.selectAverage(database, startTime,
    			endTime, Constants.ITEMNAME_JAVAPROCESS_GC_TIME_TOTAL);
    	vmThroughputValues = selectVMThroughput(database, startTime, endTime);
    	finalizeObjNumValues = ReportDao.selectAverage(database, startTime,
    			endTime, Constants.ITEMNAME_JAVAPROCESS_GC_FINALIZEQUEUE_COUNT);
    	// ���[�h���ꂽ�N���X�̍��v��
    	totalLoadedClassNumValues = ReportDao.selectAverage(database, startTime,
                endTime, Constants.ITEMNAME_JAVAPROCESS_CLASSLOADER_CLASS_TOTAL);
    	// ���݃��[�h����Ă���N���X�̐�
    	loadedClassNumValues = ReportDao.selectAverage(database, startTime,
    			endTime, Constants.ITEMNAME_JAVAPROCESS_CLASSLOADER_CLASS_CURRENT);

        for (int index = 0; index < threadNumValues.size(); index++)
        {
            VmStatusRecord record = new VmStatusRecord();

            ReportItemValue nativeThreadNum = nativeThreadNumValues.get(index);
            ReportItemValue threadNum = threadNumValues.get(index);
            ReportItemValue gcStopTime = gcStopTimeValues.get(index);
            ReportItemValue vmThroughput = vmThroughputValues.get(index);
            ReportItemValue finalizeObjNum = finalizeObjNumValues.get(index);
            ReportItemValue totalLoadedClassNum = totalLoadedClassNumValues.get(index);
            ReportItemValue loadedClassNum = loadedClassNumValues.get(index);

            if (threadNum != null)
            {
            	record.setMeasurementTime(threadNum.measurementTime);
            	record.setNativeThreadNum(nativeThreadNum.summaryValue.doubleValue());
                record.setNativeThreadNumMax(nativeThreadNum.maxValue.doubleValue());
                record.setNativeThreadNumMin(nativeThreadNum.minValue.doubleValue());
            	record.setThreadNum(threadNum.summaryValue.doubleValue());
            	record.setThreadNumMax(threadNum.maxValue.doubleValue());
            	record.setThreadNumMin(threadNum.minValue.doubleValue());
            	record.setGcStopTime(gcStopTime.summaryValue.doubleValue());
            	record.setGcStopTimeMax(gcStopTime.maxValue.doubleValue());
            	record.setGcStopTimeMin(gcStopTime.minValue.doubleValue());
            	record.setVmThroughput(vmThroughput.summaryValue.doubleValue());
            	record.setVmThroughputMax(vmThroughput.maxValue.doubleValue());
            	record.setVmThroughputMin(vmThroughput.minValue.doubleValue());
            	record.setFinalizeObjNum(finalizeObjNum.summaryValue.doubleValue());
            	record.setFinalizeObjNumMax(finalizeObjNum.maxValue.doubleValue());
            	record.setFinalizeObjNumMin(finalizeObjNum.minValue.doubleValue());
            	record.setTotalLoadedClassNum(totalLoadedClassNum.summaryValue.doubleValue());
                record.setTotalLoadedClassNumMax(totalLoadedClassNum.maxValue.doubleValue());
                record.setTotalLoadedClassNumMin(totalLoadedClassNum.minValue.doubleValue());
            	record.setLoadedClassNum(loadedClassNum.summaryValue.doubleValue());
            	record.setLoadedClassNumMax(loadedClassNum.maxValue.doubleValue());
            	record.setLoadedClassNumMin(loadedClassNum.minValue.doubleValue());
            }
            
            result.add(record);
        }

        return result;
    }
    
    /**
     * DB����GC��~���Ԃ��擾���AVM�X���[�v�b�g������o���B
     * 
     * @param database  �f�[�^�x�[�X�B
     * @param startTime ��������(�J�n����)�B
     * @param endTime   ��������(�I������)�B
     * @return VM�X���[�v�b�g�̃��X�g�B
     * @throws SQLException �f�[�^�x�[�X����̌������ɃG���[�����������ꍇ�B
     */
    private List<ReportItemValue> selectVMThroughput(String database, Timestamp startTime,
            Timestamp endTime)
        throws SQLException
    {
    	List<ReportItemValue> gcTotalTimeValues = ReportDao.selectAverage(database,
    			startTime, endTime, Constants.ITEMNAME_JAVAPROCESS_GC_TIME_TOTAL);
    	List<ReportItemValue>  upTimeValues =
            ReportDao.selectAverage(database, startTime, endTime, Constants.ITEMNAME_JAVAUPTIME);
   	
        List<ReportItemValue> dtoList = new ArrayList<ReportItemValue>();

        for(int index = 0; index < gcTotalTimeValues.size(); index++)
        {
			ReportItemValue gcTotalTimeValue = gcTotalTimeValues.get(index);
			ReportItemValue upTimeValue = upTimeValues.get(index);
			if (gcTotalTimeValue == null)
			{
				continue;
			}
			else
			{
				double gcTotalTime = gcTotalTimeValue.summaryValue.doubleValue();
				long uptime = upTimeValue.summaryValue.longValue();
				double vmThroughput;
				if (0 < uptime)
				{
				    vmThroughput = (1 - gcTotalTime / uptime) * 100;
				}
				else
				{
				    vmThroughput = 100.0;
				}

				ReportItemValue dto = new ReportItemValue();
				dto.measurementTime = gcTotalTimeValue.measurementTime;
				dto.summaryValue = vmThroughput;
				dto.maxValue = vmThroughput;
				dto.minValue = vmThroughput;

				dtoList.add(dto);
			}
        }

        return dtoList;
    }

}
