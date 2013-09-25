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

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import jp.co.acroquest.endosnipe.report.converter.compressor.CompressOperation;
import jp.co.acroquest.endosnipe.report.converter.compressor.CompressOperator;
import jp.co.acroquest.endosnipe.report.converter.compressor.SamplingCompressor;
import jp.co.acroquest.endosnipe.report.converter.util.calc.CalculatorFactory;
import jp.co.acroquest.endosnipe.report.entity.VmStatusRecord;
import jp.co.dgic.testing.framework.DJUnitTestCase;
import jp.co.acroquest.endosnipe.test.ReporterTestUtil;
import jp.co.acroquest.endosnipe.test.ReporterTestUtilForBean;

/**
 * jp.co.acroquest.endosnipe.report.converter.compressor.SamplingCompressor
 * に対する単体テストコード
 * 
 * @author M.Yoshida
 *
 */
public class SamplingCompressorTest extends DJUnitTestCase 
{
	
	public void testGetValuesByFieldName1()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		SamplingCompressor compressor = new SamplingCompressor();
		List<Double> result 
			= (List<Double>)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class, 
				"getValuesByFieldName", 
				compressor,
				samplingRecords,
				"threadNum");
		
		List<Object> expectsData = new ArrayList<Object>();
		for(Object data : samplingRecords)
		{
			expectsData.add(new Double(((VmStatusRecord)data).getThreadNum()));
		}
		
		assertEquals(expectsData.size(), result.size());
		
		for(int cnt = 0; cnt < expectsData.size(); cnt ++)
		{
			assertEquals(expectsData.get(cnt), result.get(cnt));
		}
	}

	public void testGetValuesByFieldName2()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		SamplingCompressor compressor = new SamplingCompressor();
		List<Double> result 
			= (List<Double>)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class, 
				"getValuesByFieldName", 
				compressor,
				samplingRecords,
				"finalizeObjNum");
		
		List<Object> expectsData = new ArrayList<Object>();
		for(Object data : samplingRecords)
		{
			expectsData.add(new Double(((VmStatusRecord)data).getFinalizeObjNum()));
		}
		
		assertEquals(expectsData.size(), result.size());
		
		for(int cnt = 0; cnt < expectsData.size(); cnt ++)
		{
			assertEquals(expectsData.get(cnt), result.get(cnt));
		}
	}

	public void testCalcTotal1()
	{
		List<Integer> inputData = new ArrayList<Integer>();
		inputData.add(10);
		inputData.add(15);
		inputData.add(20);
		inputData.add(25);
		inputData.add(30);

		SamplingCompressor compressor = new SamplingCompressor();
		Integer result 
			= (Integer)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"calcTotal",
				compressor,
				inputData,
				CalculatorFactory.createCalculator(Integer.class));
		
		assertEquals(100, result.intValue());
	}

	public void testCalcTotal2()
	{
		List<Double> inputData = new ArrayList<Double>();
		inputData.add(20.4);
		inputData.add(13.6);
		inputData.add(43.1);
		inputData.add(29.8);
		inputData.add(10.4);
		
		SamplingCompressor compressor = new SamplingCompressor();
		Double result 
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"calcTotal",
				compressor,
				inputData,
				CalculatorFactory.createCalculator(Double.class));
		
		assertEquals(117.3, result.doubleValue(), 0.04);
	}
	
	public void testCalcTotal3()
	{
		List<BigDecimal> inputData = new ArrayList<BigDecimal>();
		inputData.add(new BigDecimal(20.4));
		inputData.add(new BigDecimal(13.6));
		inputData.add(new BigDecimal(43.1));
		inputData.add(new BigDecimal(29.8));
		inputData.add(new BigDecimal(10.4));
		
		SamplingCompressor compressor = new SamplingCompressor();
		BigDecimal result 
			= (BigDecimal)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"calcTotal",
				compressor,
				inputData,
				CalculatorFactory.createCalculator(BigDecimal.class));
		
		assertEquals(
			(new BigDecimal(117.3)).setScale(1, RoundingMode.HALF_EVEN), 
			result.setScale(1, RoundingMode.HALF_EVEN));
	}
	
	public void testCalcTotal4()
	{
		List<Double> inputData = new ArrayList<Double>();
		
		SamplingCompressor compressor = new SamplingCompressor();
		Double result 
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"calcTotal",
				compressor,
				inputData,
				CalculatorFactory.createCalculator(Double.class));
		
		assertEquals(0, result.doubleValue(), 0);
	}
	
	public void testCalcTotal5()
	{
		List<Double> inputData = new ArrayList<Double>();
		inputData.add(20.4);
		
		SamplingCompressor compressor = new SamplingCompressor();
		Double result 
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"calcTotal",
				compressor,
				inputData,
				CalculatorFactory.createCalculator(Double.class));
		
		assertEquals(20.4, result.doubleValue(), 0);
	}
	
	public void testCalcProductSum1()
	{
		List<Integer> inputData = new ArrayList<Integer>();
		inputData.add(10);
		inputData.add(15);
		inputData.add(20);
		inputData.add(25);
		inputData.add(30);

		SamplingCompressor compressor = new SamplingCompressor();
		Integer result 
			= (Integer)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"calcProductSum",
				compressor,
				inputData,
				new Integer(4),
				CalculatorFactory.createCalculator(Integer.class));
		
		assertEquals(400, result.intValue());
	}
	
	public void testCalcProductSum2()
	{
		List<Double> inputData = new ArrayList<Double>();
		inputData.add(20.4);
		inputData.add(13.6);
		inputData.add(43.1);
		inputData.add(29.8);
		inputData.add(10.4);
		
		SamplingCompressor compressor = new SamplingCompressor();
		Double result 
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"calcProductSum",
				compressor,
				inputData,
				new Double(3.5),
				CalculatorFactory.createCalculator(Double.class));
		
		assertEquals(410.55, result.doubleValue(), 0.004);
	}
	
	public void testCalcProductSum3()
	{
		List<BigDecimal> inputData = new ArrayList<BigDecimal>();
		inputData.add(new BigDecimal(20.4));
		inputData.add(new BigDecimal(13.6));
		inputData.add(new BigDecimal(43.1));
		inputData.add(new BigDecimal(29.8));
		inputData.add(new BigDecimal(10.4));
		
		SamplingCompressor compressor = new SamplingCompressor();
		BigDecimal result 
			= (BigDecimal)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"calcProductSum",
				compressor,
				inputData,
				new BigDecimal(3.5),
				CalculatorFactory.createCalculator(BigDecimal.class));
		
		assertEquals(
			(new BigDecimal(410.55)).setScale(2, RoundingMode.HALF_EVEN), 
			result.setScale(2, RoundingMode.HALF_EVEN));
	}
	
	public void testCalcProductSum4()
	{
		List<Double> inputData = new ArrayList<Double>();
		
		SamplingCompressor compressor = new SamplingCompressor();
		Double result 
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"calcProductSum",
				compressor,
				inputData,
				new Double(3.5),
				CalculatorFactory.createCalculator(Double.class));
		
		assertEquals(0, result.doubleValue(), 0);
	}
	
	public void testCalcProductSum5()
	{
		List<Double> inputData = new ArrayList<Double>();
		inputData.add(20.4);
		
		SamplingCompressor compressor = new SamplingCompressor();
		Double result 
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"calcProductSum",
				compressor,
				inputData,
				new Double(3.5),
				CalculatorFactory.createCalculator(Double.class));
		
		assertEquals(71.4, result.doubleValue(), 0.04);
	}
	
	public void testGetCompressedValue1()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
			
			samplingRecords = samplingRecords.subList(0, 10);
			
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		CompressOperation operation 
			= new CompressOperation("threadNum",CompressOperator.SIMPLE_AVERAGE);
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		Double result
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"getCompressedValue",
				compressor,
				samplingRecords,
				operation,
				VmStatusRecord.class,
				50000L);
		
		assertEquals(5.5, result.doubleValue(),0.04);
	}
	
	public void testGetCompressedValue2()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
			
			samplingRecords = samplingRecords.subList(0, 10);
			
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		CompressOperation operation 
			= new CompressOperation("threadNum",CompressOperator.TOTAL);
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		Double result
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"getCompressedValue",
				compressor,
				samplingRecords,
				operation,
				VmStatusRecord.class,
				50000L);
		
		assertEquals(55, result.doubleValue(),0.4);
	}

	public void testGetCompressedValue3()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
			
			samplingRecords = samplingRecords.subList(0, 10);
			
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		CompressOperation operation 
			= new CompressOperation("threadNum",CompressOperator.TIME_AVERAGE);
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		Double result
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"getCompressedValue",
				compressor,
				samplingRecords,
				operation,
				VmStatusRecord.class,
				50000L);
		
		assertEquals(5.5, result.doubleValue(),0.04);
	}

	public void testGetCompressedValue4()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
			
			samplingRecords = samplingRecords.subList(0, 10);
			
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		CompressOperation operation 
			= new CompressOperation("threadNum",CompressOperator.SIMPLE_AVERAGE);
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		Double result
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"getCompressedValue",
				compressor,
				samplingRecords,
				operation,
				VmStatusRecord.class,
				100000L);
		
		assertEquals(5.5, result.doubleValue(),0.04);
	}

	public void testGetCompressedValue5()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
			
			samplingRecords = samplingRecords.subList(0, 10);
			
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		CompressOperation operation 
			= new CompressOperation("threadNum",CompressOperator.TOTAL);
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		Double result
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"getCompressedValue",
				compressor,
				samplingRecords,
				operation,
				VmStatusRecord.class,
				100000L);
		
		assertEquals(55, result.doubleValue(),0.4);
	}

	public void testGetCompressedValue6()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
			
			samplingRecords = samplingRecords.subList(0, 10);
			
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		CompressOperation operation 
			= new CompressOperation("threadNum",CompressOperator.TIME_AVERAGE);
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		Double result
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"getCompressedValue",
				compressor,
				samplingRecords,
				operation,
				VmStatusRecord.class,
				100000L);
		
		assertEquals(2.75, result.doubleValue(),0.04);
	}

	public void testGetMinValueFromSampleList1()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
			
			samplingRecords = samplingRecords.subList(10, 20);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		
		Double result
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"getMinValueFromSampleList",
				compressor,
				samplingRecords,
				"gcStopTime",
				VmStatusRecord.class);
		
		assertEquals(16.7, result.doubleValue(),0.04);
	}
	
	public void testGetMinValueFromSampleList2()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
			
			samplingRecords = samplingRecords.subList(60, 70);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		
		Double result
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"getMinValueFromSampleList",
				compressor,
				samplingRecords,
				"gcStopTime",
				VmStatusRecord.class);
		
		assertEquals(7.1, result.doubleValue(),0.04);
	}

	public void testGetMinValueFromSampleList3()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords = new ArrayList<VmStatusRecord>();
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		
		Double result
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"getMinValueFromSampleList",
				compressor,
				samplingRecords,
				"gcStopTime",
				VmStatusRecord.class);
		
		assertEquals(0, result.doubleValue(),0);
	}
	
	public void testGetMinValueFromSampleList4()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
			
			samplingRecords = samplingRecords.subList(40, 41);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		
		Double result
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"getMinValueFromSampleList",
				compressor,
				samplingRecords,
				"gcStopTime",
				VmStatusRecord.class);
		
		assertEquals(12.5, result.doubleValue(),0.04);
	}


	public void testGetMaxValueFromSampleList1()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
			
			samplingRecords = samplingRecords.subList(10, 20);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		
		Double result
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"getMaxValueFromSampleList",
				compressor,
				samplingRecords,
				"gcStopTime",
				VmStatusRecord.class);
		
		assertEquals(18.5, result.doubleValue(),0.04);
	}
	
	public void testGetMaxValueFromSampleList2()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
			
			samplingRecords = samplingRecords.subList(60, 70);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		
		Double result
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"getMaxValueFromSampleList",
				compressor,
				samplingRecords,
				"gcStopTime",
				VmStatusRecord.class);
		
		assertEquals(9.3, result.doubleValue(),0.04);
	}

	public void testGetMaxValueFromSampleList3()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords = new ArrayList<VmStatusRecord>();
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		
		Double result
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"getMaxValueFromSampleList",
				compressor,
				samplingRecords,
				"gcStopTime",
				VmStatusRecord.class);
		
		assertEquals(0, result.doubleValue(),0);
	}
	
	public void testGetMaxValueFromSampleList4()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
			
			samplingRecords = samplingRecords.subList(40, 41);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		
		Double result
			= (Double)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"getMaxValueFromSampleList",
				compressor,
				samplingRecords,
				"gcStopTime",
				VmStatusRecord.class);
		
		assertEquals(12.5, result.doubleValue(),0.04);
	}

	public void testCreateCompressedSample1()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
			
			samplingRecords = samplingRecords.subList(40, 50);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		List<CompressOperation> operations = new ArrayList<CompressOperation>();
		operations.add(new CompressOperation("threadNum",CompressOperator.SIMPLE_AVERAGE));
		operations.add(new CompressOperation("gcStopTime",CompressOperator.TOTAL));
		operations.add(new CompressOperation("vmThroughput",CompressOperator.TIME_AVERAGE));

		long startTime = 0;
		long endTime = 0;
		
		try 
		{
			startTime = parseStringForTimestamp("2009/09/07 10:03:32").getTime();
			endTime   = parseStringForTimestamp("2009/09/07 10:04:22").getTime();
		} 
		catch (ParseException e) 
		{
			fail(e.getMessage());
		}
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		VmStatusRecord result
			= (VmStatusRecord)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"createCompressedSample",
				compressor,
				samplingRecords,
				startTime,
				endTime,
				"measurementTime",
				operations,
				VmStatusRecord.class);
		
		String expectData = "2009/09/07 10:03:32,45.5,50,41,116,12.5,10.7,0.964,1,0.92,10,40";
		
		try 
		{
			VmStatusRecord expects 
				= (VmStatusRecord)ReporterTestUtilForBean.createEntity(
						VmStatusRecord.class, 
						VM_STATUS_RECORD_FIELDS, 
						expectData);

			ReporterTestUtilForBean.assertEntityEquals(expects, result, VM_STATUS_RECORD_FIELDS, 0.0004);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
	}
	
	public void testCreateCompressedSample2()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
			
			samplingRecords = samplingRecords.subList(80, 90);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		List<CompressOperation> operations = new ArrayList<CompressOperation>();
		operations.add(new CompressOperation("threadNum",CompressOperator.TOTAL));
		operations.add(new CompressOperation("gcStopTime",CompressOperator.TIME_AVERAGE));
		operations.add(new CompressOperation("vmThroughput",CompressOperator.SIMPLE_AVERAGE));

		long startTime = 0;
		long endTime = 0;
		
		try 
		{
			startTime = parseStringForTimestamp("2009/09/07 10:05:52").getTime();
			endTime   = parseStringForTimestamp("2009/09/07 10:07:52").getTime();
		} 
		catch (ParseException e) 
		{
			fail(e.getMessage());
		}
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		VmStatusRecord result
			= (VmStatusRecord)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"createCompressedSample",
				compressor,
				samplingRecords,
				startTime,
				endTime,
				"measurementTime",
				operations,
				VmStatusRecord.class);
		
		String expectData = "2009/09/07 10:05:52,855,90,81,10.9791667,31.3,21.4,0.475,0.61,0.34,45,40";
		
		try 
		{
			VmStatusRecord expects 
				= (VmStatusRecord)ReporterTestUtilForBean.createEntity(
						VmStatusRecord.class, 
						VM_STATUS_RECORD_FIELDS, 
						expectData);

			ReporterTestUtilForBean.assertEntityEquals(expects, result, VM_STATUS_RECORD_FIELDS, 0.0004);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
	}
	
	public void testCreateCompressedSample3()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			List<VmStatusRecord> tempList;
			
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
			
			tempList = samplingRecords.subList(60, 65);
			tempList.addAll((List<VmStatusRecord>)samplingRecords.subList(75, 80));
			samplingRecords = tempList;
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		List<CompressOperation> operations = new ArrayList<CompressOperation>();
		operations.add(new CompressOperation("threadNum",CompressOperator.TIME_AVERAGE));
		operations.add(new CompressOperation("gcStopTime",CompressOperator.SIMPLE_AVERAGE));
		operations.add(new CompressOperation("vmThroughput",CompressOperator.TOTAL));

		long startTime = 0;
		long endTime = 0;
		
		try 
		{
			startTime = parseStringForTimestamp("2009/09/07 10:05:12").getTime();
			endTime   = parseStringForTimestamp("2009/09/07 10:06:52").getTime();
		} 
		catch (ParseException e) 
		{
			fail(e.getMessage());
		}
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		VmStatusRecord result
			= (VmStatusRecord)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"createCompressedSample",
				compressor,
				samplingRecords,
				startTime,
				endTime,
				"measurementTime",
				operations,
				VmStatusRecord.class);
		
		String expectData = "2009/09/07 10:05:12,35.25,80,61,13.1,20.3,7.7,8.5,1,0.64,5,40";
		
		try 
		{
			VmStatusRecord expects 
				= (VmStatusRecord)ReporterTestUtilForBean.createEntity(
						VmStatusRecord.class, 
						VM_STATUS_RECORD_FIELDS, 
						expectData);

			ReporterTestUtilForBean.assertEntityEquals(expects, result, VM_STATUS_RECORD_FIELDS, 0.0004);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
	}
	
	public void testCreateCompressedSample4()
	{
		List<VmStatusRecord> samplingRecords = new ArrayList<VmStatusRecord>();
		try
		{
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		List<CompressOperation> operations = new ArrayList<CompressOperation>();
		operations.add(new CompressOperation("threadNum",CompressOperator.TIME_AVERAGE));
		operations.add(new CompressOperation("gcStopTime",CompressOperator.SIMPLE_AVERAGE));
		operations.add(new CompressOperation("vmThroughput",CompressOperator.TOTAL));

		long startTime = 0;
		long endTime = 0;
		
		try 
		{
			startTime = parseStringForTimestamp("2009/09/07 10:05:12").getTime();
			endTime   = parseStringForTimestamp("2009/09/07 10:06:52").getTime();
		} 
		catch (ParseException e) 
		{
			fail(e.getMessage());
		}
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		VmStatusRecord result
			= (VmStatusRecord)ReporterTestUtil.invokeNonAccessibleMethod(
				SamplingCompressor.class,
				"createCompressedSample",
				compressor,
				samplingRecords,
				startTime,
				endTime,
				"measurementTime",
				operations,
				VmStatusRecord.class);
		
		String expectData = "2009/09/07 10:05:12,0,0,0,0,0,0,0,0,0,0,0";
		
		try 
		{
			VmStatusRecord expects 
				= (VmStatusRecord)ReporterTestUtilForBean.createEntity(
						VmStatusRecord.class, 
						VM_STATUS_RECORD_FIELDS, 
						expectData);

			ReporterTestUtilForBean.assertEntityEquals(expects, result, VM_STATUS_RECORD_FIELDS, 0.0004);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
	}
	
	public void testCompressSamplingList1()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		List<CompressOperation> operations = new ArrayList<CompressOperation>();
		operations.add(new CompressOperation("threadNum",CompressOperator.SIMPLE_AVERAGE));
		operations.add(new CompressOperation("gcStopTime",CompressOperator.TOTAL));
		operations.add(new CompressOperation("vmThroughput",CompressOperator.TIME_AVERAGE));

		Timestamp startTime = null;
		Timestamp endTime   = null;
		
		try 
		{
			startTime = parseStringForTimestamp("2009/09/07 10:00:00");
			endTime   = parseStringForTimestamp("2009/09/07 10:10:00");
		} 
		catch (ParseException e) 
		{
			fail(e.getMessage());
		}
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		ReporterTestUtil.setNonAccessibleField(
			SamplingCompressor.class, "samplingMax_", compressor, 10L);

		List result = null;
		try 
		{
			result = compressor.compressSamplingList(
					samplingRecords, 
					startTime, 
					endTime, 
					"measurementTime", 
					operations, 
					VmStatusRecord.class);
		} 
		catch (Exception e1) 
		{
			fail(e1.getMessage());
		}
		
		String[] expectData = 
		{
			"2009/09/07 10:00:00,5.5,10,1,196,20.5,18.7,0.470833333,0.61,0.52,90,40",
			"2009/09/07 10:01:00,16.5,22,11,208.8,18.5,16.3,0.675,0.73,0.62,70,40",
			"2009/09/07 10:02:00,28.5,34,23,180,16.1,13.9,0.795,0.85,0.74,46,40",
			"2009/09/07 10:03:00,40.5,46,35,151.2,13.7,11.5,0.915,0.97,0.86,22,40",
			"2009/09/07 10:04:00,52.5,58,47,122.4,11.3,9.1,0.9975,1,0.98,3,40",
			"2009/09/07 10:05:00,64.5,70,59,97.5,9.3,7.1,0.9925,1,0.94,3,40",
			"2009/09/07 10:06:00,76.5,82,71,197.4,22.5,10.4,0.745,0.91,0.58,25,40",
			"2009/09/07 10:07:00,88.5,94,83,355.8,35.7,23.6,0.385,0.55,0.22,49,40",
			"2009/09/07 10:08:00,97.5,100,95,237.3,42.3,36.8,0.0575,0.19,0.04,73,40",
			"2009/09/07 10:09:00,0,0,0,0,0,0,0,0,0,0,0"
		};
		
		try 
		{
			List expects
				= ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, expectData);
			
			ReporterTestUtilForBean.assertEntitiesEquals(expects, result, VM_STATUS_RECORD_FIELDS, 0.0004);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
	}
	
	public void testCompressSamplingList2()
	{
		List<VmStatusRecord> samplingRecords = null;
		try
		{
			samplingRecords
				= (List<VmStatusRecord>)ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, VM_STATUS_RECORD_DATA);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
		
		List<CompressOperation> operations = new ArrayList<CompressOperation>();
		operations.add(new CompressOperation("threadNum",CompressOperator.SIMPLE_AVERAGE));
		operations.add(new CompressOperation("gcStopTime",CompressOperator.TOTAL));
		operations.add(new CompressOperation("vmThroughput",CompressOperator.TIME_AVERAGE));

		Timestamp startTime = null;
		Timestamp endTime   = null;
		
		try 
		{
			startTime = parseStringForTimestamp("2009/09/07 10:00:00");
			endTime   = parseStringForTimestamp("2009/09/07 10:10:00");
		} 
		catch (ParseException e) 
		{
			fail(e.getMessage());
		}
		
		SamplingCompressor compressor = new SamplingCompressor(5000);
		ReporterTestUtil.setNonAccessibleField(
			SamplingCompressor.class, "samplingMax_", compressor, 5L);

		List result = null;
		try 
		{
			result = compressor.compressSamplingList(
					samplingRecords, 
					startTime, 
					endTime, 
					"measurementTime", 
					operations, 
					VmStatusRecord.class);
		} 
		catch (Exception e1) 
		{
			fail(e1.getMessage());
		}
		
		String[] expectData = 
		{
			"2009/09/07 10:00:00,11.5,22,1,404.8,20.5,16.3,0.57291666,0.73,0.52,90,40",
			"2009/09/07 10:02:00,34.5,46,23,331.2,16.1,11.5,0.855,0.97,0.74,46,40",
			"2009/09/07 10:04:00,58.5,70,47,219.9,11.3,7.1,0.995,1,0.94,3,40",
			"2009/09/07 10:06:00,82.5,94,71,553.2,35.7,10.4,0.565,0.91,0.22,25,40",
			"2009/09/07 10:08:00,97.5,100,95,237.3,42.3,36.8,0.02875,0.19,0.04,73,40"
		};
		
		try 
		{
			List expects
				= ReporterTestUtilForBean.createEntities(
					VmStatusRecord.class, VM_STATUS_RECORD_FIELDS, expectData);
			
			ReporterTestUtilForBean.assertEntitiesEquals(expects, result, VM_STATUS_RECORD_FIELDS, 0.00004);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
	}
	
	
	
	
	private Timestamp parseStringForTimestamp(String timeStr) throws ParseException
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return new Timestamp(format.parse(timeStr).getTime());
	}
	
	
	private static final String[] VM_STATUS_RECORD_FIELDS
		= {
		"measurementTime", "threadNum", "threadNumMax", "threadNumMin",
		"gcStopTime", "gcStopTimeMax", "gcStopTimeMin", "vmThroughput",
		"vmThroughputMax", "vmThroughputMin", "finalizeObjNum", "loadedClassNum"
	  };

	private static final String[] VM_STATUS_RECORD_DATA
		= {
		"2009/09/07 10:00:12,1,0,0,20.5,0,0,0.52,0,0,90,40", 
		"2009/09/07 10:00:17,2,0,0,20.3,0,0,0.53,0,0,88,40", 
		"2009/09/07 10:00:22,3,0,0,20.1,0,0,0.54,0,0,86,40", 
		"2009/09/07 10:00:27,4,0,0,19.9,0,0,0.55,0,0,84,40", 
		"2009/09/07 10:00:32,5,0,0,19.7,0,0,0.56,0,0,82,40", 
		"2009/09/07 10:00:37,6,0,0,19.5,0,0,0.57,0,0,80,40", 
		"2009/09/07 10:00:42,7,0,0,19.3,0,0,0.58,0,0,78,40", 
		"2009/09/07 10:00:47,8,0,0,19.1,0,0,0.59,0,0,76,40", 
		"2009/09/07 10:00:52,9,0,0,18.9,0,0,0.6,0,0,74,40", 
		"2009/09/07 10:00:57,10,0,0,18.7,0,0,0.61,0,0,72,40", 
		"2009/09/07 10:01:02,11,0,0,18.5,0,0,0.62,0,0,70,40", 
		"2009/09/07 10:01:07,12,0,0,18.3,0,0,0.63,0,0,68,40", 
		"2009/09/07 10:01:12,13,0,0,18.1,0,0,0.64,0,0,66,40", 
		"2009/09/07 10:01:17,14,0,0,17.9,0,0,0.65,0,0,64,40", 
		"2009/09/07 10:01:22,15,0,0,17.7,0,0,0.66,0,0,62,40", 
		"2009/09/07 10:01:27,16,0,0,17.5,0,0,0.67,0,0,60,40", 
		"2009/09/07 10:01:32,17,0,0,17.3,0,0,0.68,0,0,58,40", 
		"2009/09/07 10:01:37,18,0,0,17.1,0,0,0.69,0,0,56,40", 
		"2009/09/07 10:01:42,19,0,0,16.9,0,0,0.7,0,0,54,40", 
		"2009/09/07 10:01:47,20,0,0,16.7,0,0,0.71,0,0,52,40", 
		"2009/09/07 10:01:52,21,0,0,16.5,0,0,0.72,0,0,50,40", 
		"2009/09/07 10:01:57,22,0,0,16.3,0,0,0.73,0,0,48,40", 
		"2009/09/07 10:02:02,23,0,0,16.1,0,0,0.74,0,0,46,40", 
		"2009/09/07 10:02:07,24,0,0,15.9,0,0,0.75,0,0,44,40", 
		"2009/09/07 10:02:12,25,0,0,15.7,0,0,0.76,0,0,42,40", 
		"2009/09/07 10:02:17,26,0,0,15.5,0,0,0.77,0,0,40,40", 
		"2009/09/07 10:02:22,27,0,0,15.3,0,0,0.78,0,0,38,40", 
		"2009/09/07 10:02:27,28,0,0,15.1,0,0,0.79,0,0,36,40", 
		"2009/09/07 10:02:32,29,0,0,14.9,0,0,0.8,0,0,34,40", 
		"2009/09/07 10:02:37,30,0,0,14.7,0,0,0.81,0,0,32,40", 
		"2009/09/07 10:02:42,31,0,0,14.5,0,0,0.82,0,0,30,40", 
		"2009/09/07 10:02:47,32,0,0,14.3,0,0,0.83,0,0,28,40", 
		"2009/09/07 10:02:52,33,0,0,14.1,0,0,0.84,0,0,26,40", 
		"2009/09/07 10:02:57,34,0,0,13.9,0,0,0.85,0,0,24,40", 
		"2009/09/07 10:03:02,35,0,0,13.7,0,0,0.86,0,0,22,40", 
		"2009/09/07 10:03:07,36,0,0,13.5,0,0,0.87,0,0,20,40", 
		"2009/09/07 10:03:12,37,0,0,13.3,0,0,0.88,0,0,18,40", 
		"2009/09/07 10:03:17,38,0,0,13.1,0,0,0.89,0,0,16,40", 
		"2009/09/07 10:03:22,39,0,0,12.9,0,0,0.9,0,0,14,40", 
		"2009/09/07 10:03:27,40,0,0,12.7,0,0,0.91,0,0,12,40", 
		"2009/09/07 10:03:32,41,0,0,12.5,0,0,0.92,0,0,10,40", 
		"2009/09/07 10:03:37,42,0,0,12.3,0,0,0.93,0,0,8,40", 
		"2009/09/07 10:03:42,43,0,0,12.1,0,0,0.94,0,0,7,40", 
		"2009/09/07 10:03:47,44,0,0,11.9,0,0,0.95,0,0,6,40", 
		"2009/09/07 10:03:52,45,0,0,11.7,0,0,0.96,0,0,5,40", 
		"2009/09/07 10:03:57,46,0,0,11.5,0,0,0.97,0,0,4,40", 
		"2009/09/07 10:04:02,47,0,0,11.3,0,0,0.98,0,0,3,40", 
		"2009/09/07 10:04:07,48,0,0,11.1,0,0,0.99,0,0,2,40", 
		"2009/09/07 10:04:12,49,0,0,10.9,0,0,1,0,0,1,40", 
		"2009/09/07 10:04:17,50,0,0,10.7,0,0,1,0,0,1,40", 
		"2009/09/07 10:04:22,51,0,0,10.5,0,0,1,0,0,0,40", 
		"2009/09/07 10:04:27,52,0,0,10.3,0,0,1,0,0,0,40", 
		"2009/09/07 10:04:32,53,0,0,10.1,0,0,1,0,0,0,40", 
		"2009/09/07 10:04:37,54,0,0,9.9,0,0,1,0,0,0,40", 
		"2009/09/07 10:04:42,55,0,0,9.7,0,0,1,0,0,0,40", 
		"2009/09/07 10:04:47,56,0,0,9.5,0,0,1,0,0,0,40", 
		"2009/09/07 10:04:52,57,0,0,9.3,0,0,1,0,0,0,40", 
		"2009/09/07 10:04:57,58,0,0,9.1,0,0,1,0,0,0,40", 
		"2009/09/07 10:05:02,59,0,0,8.9,0,0,1,0,0,3,40", 
		"2009/09/07 10:05:07,60,0,0,8.7,0,0,1,0,0,4,40", 
		"2009/09/07 10:05:12,61,0,0,8.5,0,0,1,0,0,5,40", 
		"2009/09/07 10:05:17,62,0,0,8.3,0,0,1,0,0,7,40", 
		"2009/09/07 10:05:22,63,0,0,8.1,0,0,1,0,0,9,40", 
		"2009/09/07 10:05:27,64,0,0,7.9,0,0,1,0,0,11,40", 
		"2009/09/07 10:05:32,65,0,0,7.7,0,0,1,0,0,13,40", 
		"2009/09/07 10:05:37,66,0,0,7.5,0,0,1,0,0,15,40", 
		"2009/09/07 10:05:42,67,0,0,7.3,0,0,1,0,0,17,40", 
		"2009/09/07 10:05:47,68,0,0,7.1,0,0,1,0,0,19,40", 
		"2009/09/07 10:05:52,69,0,0,8.2,0,0,0.97,0,0,21,40", 
		"2009/09/07 10:05:57,70,0,0,9.3,0,0,0.94,0,0,23,40", 
		"2009/09/07 10:06:02,71,0,0,10.4,0,0,0.91,0,0,25,40", 
		"2009/09/07 10:06:07,72,0,0,11.5,0,0,0.88,0,0,27,40", 
		"2009/09/07 10:06:12,73,0,0,12.6,0,0,0.85,0,0,29,40", 
		"2009/09/07 10:06:17,74,0,0,13.7,0,0,0.82,0,0,31,40", 
		"2009/09/07 10:06:22,75,0,0,14.8,0,0,0.79,0,0,33,40", 
		"2009/09/07 10:06:27,76,0,0,15.9,0,0,0.76,0,0,35,40", 
		"2009/09/07 10:06:32,77,0,0,17,0,0,0.73,0,0,37,40", 
		"2009/09/07 10:06:37,78,0,0,18.1,0,0,0.7,0,0,39,40", 
		"2009/09/07 10:06:42,79,0,0,19.2,0,0,0.67,0,0,41,40", 
		"2009/09/07 10:06:47,80,0,0,20.3,0,0,0.64,0,0,43,40", 
		"2009/09/07 10:06:52,81,0,0,21.4,0,0,0.61,0,0,45,40", 
		"2009/09/07 10:06:57,82,0,0,22.5,0,0,0.58,0,0,47,40", 
		"2009/09/07 10:07:02,83,0,0,23.6,0,0,0.55,0,0,49,40", 
		"2009/09/07 10:07:07,84,0,0,24.7,0,0,0.52,0,0,51,40", 
		"2009/09/07 10:07:12,85,0,0,25.8,0,0,0.49,0,0,53,40", 
		"2009/09/07 10:07:17,86,0,0,26.9,0,0,0.46,0,0,55,40", 
		"2009/09/07 10:07:22,87,0,0,28,0,0,0.43,0,0,57,40", 
		"2009/09/07 10:07:27,88,0,0,29.1,0,0,0.4,0,0,59,40", 
		"2009/09/07 10:07:32,89,0,0,30.2,0,0,0.37,0,0,61,40", 
		"2009/09/07 10:07:37,90,0,0,31.3,0,0,0.34,0,0,63,40", 
		"2009/09/07 10:07:42,91,0,0,32.4,0,0,0.31,0,0,65,40", 
		"2009/09/07 10:07:47,92,0,0,33.5,0,0,0.28,0,0,67,40", 
		"2009/09/07 10:07:52,93,0,0,34.6,0,0,0.25,0,0,69,40", 
		"2009/09/07 10:07:57,94,0,0,35.7,0,0,0.22,0,0,71,40", 
		"2009/09/07 10:08:02,95,0,0,36.8,0,0,0.19,0,0,73,40", 
		"2009/09/07 10:08:07,96,0,0,37.9,0,0,0.16,0,0,75,40", 
		"2009/09/07 10:08:12,97,0,0,39,0,0,0.13,0,0,77,40", 
		"2009/09/07 10:08:17,98,0,0,40.1,0,0,0.1,0,0,79,40", 
		"2009/09/07 10:08:22,99,0,0,41.2,0,0,0.07,0,0,81,40", 
		"2009/09/07 10:08:26,100,0,0,42.3,0,0,0.04,0,0,83,40"
	  	};
	
}
