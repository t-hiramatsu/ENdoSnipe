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

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.report.dao.util.PercentageDataUtil;
import jp.co.acroquest.endosnipe.report.entity.ProcessResourceRecord;
import jp.co.acroquest.endosnipe.report.entity.ReportItemValue;
import jp.co.acroquest.endosnipe.common.Constants;
import jp.co.acroquest.endosnipe.report.dao.ReportDao;

/**
 * CPU／メモリのデータ取得とレポート情報変換を行うアクセサ。
 * 
 * @author akiba
 */
public class CpuAndMemoryRecordAccessor
{
    /**
     * 期間を指定し、その期間内でのプロセスリソース(CPU／メモリ)使用状況のレポートデータを取得する。
     * 
     * @param database  データベース名。
     * @param startTime 検索条件(開始時刻)。
     * @param endTime   検索条件(終了時刻)。
     * @return プロセスリソース使用状況のレポートデータ。
     * @throws SQLException データ取得時に例外が発生した場合
     */
    public List<ProcessResourceRecord> findSystemResourceStaticsByTerm(String database,
            Timestamp startTime, Timestamp endTime) throws SQLException
    {
        List<ProcessResourceRecord> result = new ArrayList<ProcessResourceRecord>();

        // データベースから値を取得する
        
        // CPU使用率（プロセス）
        List<ReportItemValue> cpuUsageTotalValues;
        List<ReportItemValue> cpuUsageSysValues;
        // ヒープメモリ使用量
        List<ReportItemValue> heapMemMaxValues;
        List<ReportItemValue> heapMemNowValues;
        // 非ヒープメモリ使用量
        List<ReportItemValue> nonHeapMemMaxValues;
        List<ReportItemValue> nonHeapMemNowValues;
        // メジャーフォールト数
        List<ReportItemValue> majorFaultValues;
        // 仮想マシンメモリ量
        List<ReportItemValue> vmMemMaxValues;
        List<ReportItemValue> vmMemNowValues;
        // プロセスのメモリ使用量
        List<ReportItemValue> virtualMemValue;
        List<ReportItemValue> physicalMemValues;
        // プロセスのファイル記述子／ハンドル数
        List<ReportItemValue> fdCountValues;

        cpuUsageTotalValues = ReportDao.selectAverage(database, startTime,
                endTime, Constants.ITEMNAME_PROCESS_CPU_TOTAL_USAGE);
        cpuUsageSysValues = ReportDao.selectAverage(database, startTime,
                endTime, Constants.ITEMNAME_PROCESS_CPU_SYSTEM_USAGE);

        // CPU使用率のデータがDBにあり、取得できた場合は、それらに変換処理を行う。
        // 取得できなかった場合は、他のデータから計算することによって再取得する。
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
                    endTime, Constants.ITEMNAME_PROCESS_CPU_TOTAL_TIME);
            cpuUsageSysValues = selectCpuUsage(database, startTime,
                    endTime, Constants.ITEMNAME_PROCESS_CPU_SYSTEM_TIME);
        }
        
        heapMemNowValues = ReportDao.selectAverage(database, startTime,
                endTime, Constants.ITEMNAME_JAVAPROCESS_MEMORY_HEAP_USED);
        heapMemMaxValues = ReportDao.selectAverage(database, startTime,
                endTime, Constants.ITEMNAME_JAVAPROCESS_MEMORY_HEAP_MAX);
        nonHeapMemNowValues = ReportDao.selectAverage(database, startTime,
                endTime, Constants.ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_USED);
        nonHeapMemMaxValues = ReportDao.selectAverage(database, startTime,
                endTime, Constants.ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_MAX);
        majorFaultValues = ReportDao.selectAverage(database, startTime,
                endTime, Constants.ITEMNAME_PROCESS_MEMORY_MAJORFAULT_COUNT);
        vmMemNowValues = selectMemoryUsage(database, startTime,endTime,
        		Constants.ITEMNAME_PROCESS_MEMORY_VIRTUALMACHINE_MAX,
        		Constants.ITEMNAME_PROCESS_MEMORY_VIRTUALMACHINE_FREE);
        vmMemMaxValues = ReportDao.selectAverage(database, startTime,
        		endTime, Constants.ITEMNAME_PROCESS_MEMORY_VIRTUALMACHINE_MAX);
        virtualMemValue = ReportDao.selectAverage(database, startTime,
                endTime, Constants.ITEMNAME_PROCESS_MEMORY_VIRTUAL_USED);
        physicalMemValues = ReportDao.selectAverage(database, startTime,
                endTime, Constants.ITEMNAME_PROCESS_MEMORY_PHYSICAL_USED);
        fdCountValues = ReportDao.selectAverage(database, startTime,
                endTime, Constants.ITEMNAME_PROCESS_HANDLE_TOTAL_NUMBER);

        for (int index = 0; index < cpuUsageTotalValues.size(); index++)
        {

            ProcessResourceRecord record = new ProcessResourceRecord();
            ReportItemValue cpuUsageTotal = cpuUsageTotalValues.get(index);
            ReportItemValue cpuUsageSys = cpuUsageSysValues.get(index);
            ReportItemValue heapMemNow = heapMemNowValues.get(index);
            ReportItemValue heapMemMax = heapMemMaxValues.get(index);
            ReportItemValue nonHeapMemNow = nonHeapMemNowValues.get(index);
            ReportItemValue nonHeapMemMax = nonHeapMemMaxValues.get(index);
            ReportItemValue majorFault = majorFaultValues.get(index);
            ReportItemValue vmMemNow = vmMemNowValues.get(index);
            ReportItemValue vmMemMax = vmMemMaxValues.get(index);
            ReportItemValue virtualMem = virtualMemValue.get(index);
            ReportItemValue physicalMem = physicalMemValues.get(index);
            ReportItemValue fdCount = fdCountValues.get(index);
            
            if (cpuUsageTotal != null)
            {
	            record.setMeasurementTime(cpuUsageTotal.measurementTime);
	            record.setCpuUsage(cpuUsageTotal.summaryValue.doubleValue());
	            record.setCpuUsageMax(cpuUsageTotal.maxValue.doubleValue());
	            record.setCpuUsageMin(cpuUsageTotal.minValue.doubleValue());
	            record.setCpuUsageSys(cpuUsageSys.summaryValue.doubleValue());
                record.setCpuUsageSysMax(cpuUsageSys.maxValue.doubleValue());
                record.setCpuUsageSysMin(cpuUsageSys.minValue.doubleValue());
	            record.setHeapMemoryNow(heapMemNow.summaryValue.doubleValue());
	            record.setHeapMemoryNowMax(heapMemNow.maxValue.doubleValue());
	            record.setHeapMemoryNowMin(heapMemNow.minValue.doubleValue());
	            record.setHeapMemoryMax(heapMemMax.maxValue.doubleValue());
	            record.setNonHeapMemoryNow(nonHeapMemNow.summaryValue.doubleValue());
	            record.setNonHeapMemoryNowMax(nonHeapMemNow.maxValue.doubleValue());
	            record.setNonHeapMemoryNowMin(nonHeapMemNow.minValue.doubleValue());
	            record.setNonHeapMemoryMax(nonHeapMemMax.maxValue.doubleValue());
	            record.setMajorFault(majorFault.summaryValue.doubleValue());
	            record.setMajorFaultMax(majorFault.maxValue.doubleValue());
	            record.setMajorFaultMin(majorFault.minValue.doubleValue());
	            record.setVmMemoryNow(vmMemNow.summaryValue.doubleValue());
	            record.setVmMemoryNowMax(vmMemNow.maxValue.doubleValue());
	            record.setVmMemoryNowMin(vmMemNow.minValue.doubleValue());
	            record.setVmMemoryMax(vmMemMax.maxValue.doubleValue());
	            record.setVirtualMem(virtualMem.summaryValue.doubleValue());
	            record.setVirtualMemMax(virtualMem.maxValue.doubleValue());
	            record.setVirtualMemMin(virtualMem.minValue.doubleValue());
	            record.setPhysicalMem(physicalMem.summaryValue.doubleValue());
                record.setPhysicalMemMax(physicalMem.maxValue.doubleValue());
                record.setPhysicalMemMin(physicalMem.minValue.doubleValue());

	            if (fdCount != null)
	            {
	                record.setFdCount(fdCount.summaryValue.longValue());
	                record.setFdCountMax(fdCount.maxValue.longValue());
	                record.setFdCountMin(fdCount.minValue.longValue());
	            }
           }

            result.add(record);
        }

        return result;
    }
    
    /**
     * DBからCPU時間を取得し、CPU使用率を割り出す。
     * 
     * @param database  データベース。
     * @param startTime 検索条件(開始時刻)。
     * @param endTime   検索条件(終了時刻)。
     * @param cpuItemName CPU時間のitemName。
     * @return CPU使用率のリスト。
     * @throws SQLException データベースからの検索時にエラーが発生した場合。
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

	private double calcCpuUsage(long cpuValueLong,
			long upTimeValueLong, long procCntValueLong)
	{
		if (upTimeValueLong == 0 || procCntValueLong == 0)
		{
			return 0.0;
		}

		return cpuValueLong / (upTimeValueLong * 10000.0 * procCntValueLong);
	}

    /**
     * DBからメモリの全体と空き容量を取得し、メモリ使用量を割り出す。
     * 
     * @param database  データベース。
     * @param startTime 検索条件(開始時刻)。
     * @param endTime   検索条件(終了時刻)。
     * @param itemNameCapacity 全体を取得するための項目名称。
     * @param itemNameFree 空きを取得するための項目名称。
     * @return メモリ使用量のリスト。
     * @throws SQLException データベースからの検索時にエラーが発生した場合。
     */
    private List<ReportItemValue> selectMemoryUsage(String database, Timestamp startTime,
            Timestamp endTime, String itemNameCapacity, String itemNameFree)
        throws SQLException
    {
        // 全体
        List<ReportItemValue> memCapacityValues = ReportDao.selectAverage(database, startTime,
                endTime, itemNameCapacity);

        // 空き
        List<ReportItemValue> memFreeValues = ReportDao.selectAverage(database, startTime,
                endTime, itemNameFree);

        // 全体から空きを引いた値を使用量としてリストに追加する
        List<ReportItemValue> memUsageValues = new ArrayList<ReportItemValue>();
        for (int index = 0; index < memCapacityValues.size(); index++)
        {
        	ReportItemValue memCapacity = memCapacityValues.get(index);
        	ReportItemValue memFree = memFreeValues.get(index);

            // value以外はcapacityの値をそのまま詰める
        	ReportItemValue memUsage = new ReportItemValue();
            memUsage.measurementTime = memCapacity.measurementTime;
            memUsage.index = memCapacity.index;
            memUsage.itemName = memCapacity.itemName;
            
            double capacity = memCapacity.maxValue.doubleValue();
            
            // 平均値の計算
            double summaryFree = memFree.summaryValue.doubleValue();
            double summaryUsage = capacity - summaryFree;
            memUsage.summaryValue = new BigDecimal(summaryUsage);
            
            // 最小値の計算
            double maxFree = memFree.maxValue.doubleValue();
            double minUsage = capacity - maxFree;
            memUsage.minValue = new BigDecimal(minUsage);
            
            // 最大値の計算
            double minFree = memFree.minValue.doubleValue();
            double maxUsage = capacity - minFree;
            memUsage.maxValue = new BigDecimal(maxUsage);
            
            memUsageValues.add(memUsage);
        }

        return memUsageValues;
    }
}
