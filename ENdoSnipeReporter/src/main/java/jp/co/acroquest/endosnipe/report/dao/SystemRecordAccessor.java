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

import jp.co.acroquest.endosnipe.report.dao.util.PercentageDataUtil;
import jp.co.acroquest.endosnipe.report.entity.ReportItemValue;
import jp.co.acroquest.endosnipe.report.entity.SystemResourceRecord;
import jp.co.acroquest.endosnipe.common.Constants;
import jp.co.acroquest.endosnipe.report.dao.ReportDao;

/**
 * �V�X�e���̃f�[�^�擾�ƃ��|�[�g���ϊ����s���A�N�Z�T�B
 * 
 * @author eriguchi
 */
public class SystemRecordAccessor
{
    /**
     * ���Ԃ��w�肵�A���̊��ԓ��ł̃V�X�e�����\�[�X(CPU�^������)�g�p�󋵂̃��|�[�g�f�[�^���擾����B
     * 
     * @param database  �f�[�^�x�[�X���B
     * @param startTime ��������(�J�n����)�B
     * @param endTime   ��������(�I������)�B
     * @return �V�X�e�����\�[�X�g�p�󋵂̃��|�[�g�f�[�^�B
     * @throws SQLException �f�[�^�擾���ɗ�O�����������ꍇ
     */
    public List<SystemResourceRecord> findSystemResourceStaticsByTerm(String database,
            Timestamp startTime, Timestamp endTime) throws SQLException
    {
        List<SystemResourceRecord> result = new ArrayList<SystemResourceRecord>();

        // �f�[�^�x�[�X����l���擾����
        List<ReportItemValue> cpuUsageTotalValues;
        List<ReportItemValue> cpuUsageSysValues;
        List<ReportItemValue> physicalMemValues;
        List<ReportItemValue> swapMemMaxValues;
        List<ReportItemValue> swapMemNowValues;
        List<ReportItemValue> pageInValues;
        List<ReportItemValue> pageOutValues;
        List<ReportItemValue> fdCountValues;

        cpuUsageTotalValues = ReportDao.selectAverage(database, startTime,
                endTime, Constants.ITEMNAME_SYSTEM_CPU_TOTAL_USAGE);
        cpuUsageSysValues = ReportDao.selectAverage(database, startTime,
                endTime, Constants.ITEMNAME_SYSTEM_CPU_SYSTEM_USAGE);

        // CPU�g�p���̃f�[�^��DB�ɂ���A�擾�ł����ꍇ�́A�����ɕϊ��������s���B
        // �擾�ł��Ȃ������ꍇ�́A���̃f�[�^����v�Z���邱�Ƃɂ���čĎ擾����B
        if (cpuUsageTotalValues != null && cpuUsageSysValues != null
                && 0 < cpuUsageTotalValues.size()
                && 0 < cpuUsageSysValues.size())
        {
            cpuUsageTotalValues = PercentageDataUtil
                    .reconstitutePercentageData(cpuUsageTotalValues);
            cpuUsageSysValues = PercentageDataUtil
                    .reconstitutePercentageData(cpuUsageSysValues);
        }
        else
        {
            cpuUsageTotalValues = selectCpuUsage(database, startTime,
                    endTime, Constants.ITEMNAME_SYSTEM_CPU_USERMODE_TIME);
            cpuUsageSysValues = selectCpuUsage(database, startTime,
                    endTime, Constants.ITEMNAME_SYSTEM_CPU_SYSTEM_TIME);
        }
        
        
        physicalMemValues =
                selectMemoryUsage(database, startTime, endTime,
                                  Constants.ITEMNAME_SYSTEM_MEMORY_PHYSICAL_MAX,
                                  Constants.ITEMNAME_SYSTEM_MEMORY_PHYSICAL_FREE);
        
        swapMemMaxValues = ReportDao.selectAverage(database, startTime, endTime,
                Constants.ITEMNAME_SYSTEM_MEMORY_SWAP_MAX);
        swapMemNowValues = ReportDao.selectAverage(database, startTime, endTime,
                Constants.ITEMNAME_SYSTEM_MEMORY_SWAP_FREE);

        pageInValues = ReportDao.selectAverage(database, startTime, endTime,
                                Constants.ITEMNAME_SYSTEM_MEMORY_PAGEIN_COUNT);
        pageOutValues = ReportDao.selectAverage(database, startTime, endTime,
                                               Constants.ITEMNAME_SYSTEM_MEMORY_PAGEOUT_COUNT);
        fdCountValues = ReportDao.selectAverage(database, startTime,
                                                endTime, Constants.ITEMNAME_SYSTEM_HANDLE_TOTAL_NUMBER);

        for (int index = 0; index < cpuUsageTotalValues.size(); index++)
        {
            SystemResourceRecord record = new SystemResourceRecord();

            ReportItemValue cpuUsageTotal = cpuUsageTotalValues.get(index);
            ReportItemValue cpuUsageSys = cpuUsageSysValues.get(index);
            ReportItemValue physicalMem = physicalMemValues.get(index);
            ReportItemValue swapMemMax = swapMemMaxValues.get(index);
            ReportItemValue swapMemNow = swapMemNowValues.get(index);
            ReportItemValue pageInValue = pageInValues.get(index);
            ReportItemValue pageOutValue = pageOutValues.get(index);
            ReportItemValue fdCount = fdCountValues.get(index);
            
            if(cpuUsageTotal != null)
            {
	            record.setMeasurementTime(cpuUsageTotal.measurementTime);
	            record.setCpuUsage(cpuUsageTotal.summaryValue.doubleValue());
	            record.setCpuUsageMax(cpuUsageTotal.maxValue.doubleValue());
	            record.setCpuUsageMin(cpuUsageTotal.minValue.doubleValue());
           }
            if(cpuUsageSys != null)
            {
                record.setMeasurementTime(cpuUsageSys.measurementTime);
                record.setSysCpuUsage(cpuUsageSys.summaryValue.doubleValue());
                record.setSysCpuUsageMax(cpuUsageSys.maxValue.doubleValue());
                record.setSysCpuUsageMin(cpuUsageSys.minValue.doubleValue());
           }
            if(physicalMem != null)
            {
                record.setMeasurementTime(physicalMem.measurementTime);
                record.setPhysicalMemoryMax(physicalMem.limitValue.doubleValue());
                record.setPhysicalMemoryUse(physicalMem.summaryValue.doubleValue());
                record.setPhysicalMemoryUseMax(physicalMem.maxValue.doubleValue());
                record.setPhysicalMemoryUseMin(physicalMem.minValue.doubleValue());
           }
            if (swapMemMax != null && swapMemNow != null)
            {
                record.setMeasurementTime(swapMemMax.measurementTime);
                record.setSwapMemoryMax(swapMemMax.maxValue.doubleValue());
                record.setSwapMemoryUse(swapMemNow.summaryValue.doubleValue());
                record.setSwapMemoryUseMax(swapMemNow.maxValue.doubleValue());
                record.setSwapMemoryUseMin(swapMemNow.minValue.doubleValue());
            }
            if(pageInValue != null)
            {
                record.setMeasurementTime(pageInValue.measurementTime);
                record.setPageIn(pageInValue.summaryValue.doubleValue());
                record.setPageInMax(pageInValue.maxValue.doubleValue());
                record.setPageInMin(pageInValue.minValue.doubleValue());
            }
            if(pageOutValue != null)
            {
                record.setMeasurementTime(pageOutValue.measurementTime);
                record.setPageOut(pageOutValue.summaryValue.doubleValue());
                record.setPageOutMax(pageOutValue.maxValue.doubleValue());
                record.setPageOutMin(pageOutValue.minValue.doubleValue());
            }
            if (fdCount != null)
            {
                record.setFdCount(fdCount.summaryValue.longValue());
                record.setFdCountMax(fdCount.maxValue.longValue());
                record.setFdCountMin(fdCount.minValue.longValue());
            }
            result.add(record);
        }

        return result;
    }

    /**
     * DB����ݐ�CPU���Ԃ��擾���ACPU�g�p��������o���B
     * 
     * @param database  �f�[�^�x�[�X�B
     * @param startTime ��������(�J�n����)�B
     * @param endTime   ��������(�I������)�B
     * @param cpuItemName TODO
     * @return CPU�g�p���̃��X�g�B
     * @throws SQLException �f�[�^�x�[�X����̌������ɃG���[�����������ꍇ�B
     */
    private List<ReportItemValue> selectCpuUsage(String database, Timestamp startTime,
            Timestamp endTime, String cpuItemName)
        throws SQLException
    {
    	List<ReportItemValue> cpuTimeValues =
                ReportDao.selectAverage(database, startTime, endTime, cpuItemName);
    	List<ReportItemValue> upTimeValues =
                ReportDao.selectAverage(database, startTime, endTime, Constants.ITEMNAME_JAVAUPTIME);
        List<ReportItemValue> procCntValues =
                ReportDao.selectAverage(database, startTime, endTime,
                                        Constants.ITEMNAME_SYSTEM_CPU_PROCESSOR_COUNT);

        List<ReportItemValue> dtoList = new ArrayList<ReportItemValue>();

        for(int index = 0; index < cpuTimeValues.size(); index++)
        {
			ReportItemValue cpuValue = cpuTimeValues.get(index);
			ReportItemValue upTimeValue = upTimeValues.get(index);
			ReportItemValue procCntValue = procCntValues.get(index);
			if (cpuValue == null || upTimeValue == null || procCntValue == null)
			{
				continue;
			}
			else
			{
				ReportItemValue dto = new ReportItemValue();
				dto.measurementTime = cpuValue.measurementTime;
				dto.summaryValue = calcCpuUsage(cpuValue.summaryValue
						.longValue(), upTimeValue.summaryValue.longValue(),
						procCntValue.summaryValue.longValue());
				dto.maxValue =
                        calcCpuUsage(cpuValue.maxValue.longValue(),
                                     upTimeValue.maxValue.longValue(),
                                     procCntValue.maxValue.longValue());
                dto.minValue =
                        calcCpuUsage(cpuValue.minValue.longValue(),
                                     upTimeValue.minValue.longValue(),
                                     procCntValue.minValue.longValue());

				dtoList.add(dto);
			}
        }


        return dtoList;
    }

    /**
     * DB����ݐ�CPU���Ԃ��擾���ACPU�g�p��������o���B
     * 
     * @param database  �f�[�^�x�[�X�B
     * @param startTime ��������(�J�n����)�B
     * @param endTime   ��������(�I������)�B
     * @param memoryItemName TODO
     * @return CPU�g�p���̃��X�g�B
     * @throws SQLException �f�[�^�x�[�X����̌������ɃG���[�����������ꍇ�B
     */
    private List<ReportItemValue> selectMemoryUsage(String database, Timestamp startTime,
            Timestamp endTime, String memoryItemName, String memoryFreeItemName)
        throws SQLException
    {
        List<ReportItemValue> memoryAllValues =
                ReportDao.selectAverage(database, startTime, endTime, memoryItemName);
        List<ReportItemValue> memoryFreeValues =
                ReportDao.selectAverage(database, startTime, endTime, memoryFreeItemName);

        List<ReportItemValue> dtoList = new ArrayList<ReportItemValue>();

        for(int index = 0; index < memoryAllValues.size(); index++)
        {
            ReportItemValue memoryAllValue = memoryAllValues.get(index);
            ReportItemValue memoryFreeValue = memoryFreeValues.get(index);
            if (memoryAllValue == null || memoryFreeValue == null)
            {
                continue;
            }
            else
            {
                ReportItemValue dto = new ReportItemValue();
                dto.measurementTime = memoryAllValue.measurementTime;
                dto.limitValue = memoryAllValue.summaryValue.longValue();
                dto.summaryValue =
                        Math.max(memoryAllValue.summaryValue.longValue()
                                - memoryFreeValue.summaryValue.longValue(), 0);
                dto.maxValue =
                        Math.max(memoryAllValue.maxValue.longValue()
                                - memoryFreeValue.summaryValue.longValue(), 0);
                dto.minValue =
                        Math.max(memoryAllValue.minValue.longValue()
                                - memoryFreeValue.summaryValue.longValue(), 0);
                dtoList.add(dto);
            }
        }


        return dtoList;
    }
    

    private double calcCpuUsage(long cpuValueLong,
			long upTimeValueLong, long procCntValueLong)
	{
		if (upTimeValueLong == 0 || procCntValueLong == 0)
		{
			return 0.0;
		}

		return cpuValueLong / (upTimeValueLong * 10000.0 * procCntValueLong);
	}

}
