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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.acroquest.endosnipe.report.converter.util.calc.Calculator;
import jp.co.acroquest.endosnipe.report.converter.util.calc.CalculatorFactory;
import jp.co.acroquest.endosnipe.report.util.ReporterConfigAccessor;
import jp.co.acroquest.endosnipe.report.converter.compressor.CompressOperation;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * 多数のサンプリングデータを圧縮し、Excelに表示可能なデータに変換するクラス。
 * 
 * @author M.Yoshida
 */
@SuppressWarnings("unchecked")
public class SamplingCompressor 
{
	private static final String MAX_VALUE_PROPERTY_SUFFIX_ = "Max";
	
	private static final String MIN_VALUE_PROPERTY_SUFFIX_ = "Min";
	
	private static final String SAMPLING_MAX_NUM_KEY_      = "reporter.report.maxSamples";
	
	/** サンプリンググループを生成する際の最小計測時間 */
	private long minLimitSamplingTerm_ = 5000;

	/** 生データのサンプリング周期[sec] */
	private long rawSamplingTerm_ = 5;
	
	/** 圧縮後のサンプリング数の最大数 */
	private long samplingMax_ = 0;
	
	private static long SEC_PER_MILLIS = 1000;
	
	/** デフォルトの圧縮後サンプリング数 */
	private static long DEFAULT_SAMPLING_MAX = 200;
	
	/**
	 * コンストラクタ（デフォルト）
	 */
	public SamplingCompressor()
	{
		this.samplingMax_ = Long.parseLong(
				ReporterConfigAccessor.getProperty(SAMPLING_MAX_NUM_KEY_));
		
		if(this.samplingMax_ <= 0)
		{
			this.samplingMax_ = DEFAULT_SAMPLING_MAX;
		}
		
	}

	/**
	 * コンストラクタ
	 * 
	 * @param minTerm サンプリンググループ生成の際の最小計測時間
	 */
	public SamplingCompressor(long minTerm)
	{
		super();
		this.minLimitSamplingTerm_ = minTerm;
	}
	
	/**
	 * 指定のサンプリングデータを、一定数以下になるよう圧縮する。
	 * 指定したフィールドに関しては、圧縮したデータより、最大、最小、平均値を算出・補完設定する。
	 * 
	 * @param samplingList     サンプリングデータ。measureTimeFieldで指定したフィールドによりソートされていることを前提とする。
	 * @param startTime        圧縮時の計測開始時刻
	 * @param endTime          圧縮時の計測終了時刻
	 * @param measureTimeField 計測時刻を格納しているフィールド
	 * @param operation        圧縮時の操作
	 * @param clazz            サンプリングデータのClass情報
	 * @return 圧縮したサンプリングデータのリスト
	 * @throws IllegalAccessException アクセスできないフィールドにアクセスした場合
	 * @throws InvocationTargetException 予期しないインスタンスを指定した場合
	 * @throws NoSuchMethodException 指定のフィールドが存在しない場合
	 * @throws NoSuchFieldException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 */
	public List compressSamplingList(
			List samplingList, 
			Timestamp startTime,
			Timestamp endTime,
			String    measureTimeField,
			List<CompressOperation> operation,
			Class     clazz)
		throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, SecurityException, InstantiationException, NoSuchFieldException
	{
		// サンプリンググループを作る際の1グループ当たりの「計測期間」を算出する。
		// 算出した結果が、計測時間が「最小計測時間」を下回った場合は、「最小計測時間」に合わせる。
		long samplingTerm = (endTime.getTime() - startTime.getTime()) / this.samplingMax_;
		
		if(samplingTerm < this.minLimitSamplingTerm_)
		{
			samplingTerm = this.minLimitSamplingTerm_;
		}
		
		long nowStartTime = startTime.getTime();
		int  sampleIndex = 0;
		
		List compressedList = new ArrayList();
		
		while(nowStartTime < endTime.getTime())
		{
			// 現計測期間内に処理するサンプルデータを抽出する。
			List samplingGroup = new ArrayList();
			for(int cnt = sampleIndex ; cnt < samplingList.size(); cnt ++)
			{
				Object sampleData = samplingList.get(cnt);
				Date measureTime = (Date)PropertyUtils.getProperty(sampleData, measureTimeField);

				if(nowStartTime > measureTime.getTime())
				{
					sampleIndex ++;
					continue;
				}
				
				if(nowStartTime + samplingTerm <= measureTime.getTime())
				{
					sampleIndex = cnt;
					break;
				}
				
				samplingGroup.add(sampleData);
			}
			
			// 圧縮データを生成する。
			Object compressedData 
				= createCompressedSample(
						samplingGroup,
						nowStartTime, 
						nowStartTime + samplingTerm,
						measureTimeField, 
						operation,
						clazz);
			
			compressedList.add(compressedData);
			
			nowStartTime += samplingTerm;
		}
		
		return compressedList;
	}
	
	/**
	 * 指定のサンプリングデータを圧縮し、ひとつのサンプリングデータに変換する。
	 * 指定したフィールドに関しては、圧縮したデータより、最大、最小、平均値を算出・補完設定する。
	 * 
	 * @param rawSamples   サンプリングデータ
	 * @param targetFields 補完を行うフィールド
	 * @return 圧縮を行ったサンプリングデータ
	 * @throws IllegalAccessException アクセスできないフィールドにアクセスした場合
	 * @throws InvocationTargetException 予期しないインスタンスを指定した場合
	 * @throws NoSuchMethodException 指定のフィールドが存在しない場合
	 * @throws InstantiationException 
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	private Object createCompressedSample(
			List      rawSamples, 
			long      startTime,
			long      endTime,
			String    measureTimeField,
			List<CompressOperation> operation,
			Class     clazz) 
		throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException, SecurityException, NoSuchFieldException
	{
		Object returnSample;
		
		if(rawSamples.size() < 1)
		{
			returnSample = clazz.newInstance();
		}
		else
		{
			returnSample = rawSamples.get(0);
		}

		BeanUtils.setProperty(returnSample, measureTimeField, new Timestamp(startTime));

		for(CompressOperation ope : operation)
		{
			Object maxObj = getMaxValueFromSampleList(rawSamples, ope.getCompressField(), clazz);
			Object minObj = getMinValueFromSampleList(rawSamples, ope.getCompressField(), clazz);
			Object compressedObj
				= getCompressedValue(rawSamples, 
						             ope,
						             clazz,
						             endTime - startTime);
			
			PropertyUtils.setProperty(
				returnSample, ope.getCompressField() + MAX_VALUE_PROPERTY_SUFFIX_, maxObj);
			PropertyUtils.setProperty(
				returnSample, ope.getCompressField() + MIN_VALUE_PROPERTY_SUFFIX_, minObj);
			PropertyUtils.setProperty(
				returnSample, ope.getCompressField(), compressedObj);
		}
		
		return returnSample;
	}

	/**
	 * サンプリングデータのリストから、指定したフィールドの「最大値」を算出する。
	 * 
	 * @param rawSamples  サンプリングデータのリスト
	 * @param targetField 最大値を求めるフィールド
	 * @return 算出した最大値
	 * @throws IllegalAccessException アクセスできないフィールドにアクセスした場合
	 * @throws InvocationTargetException 予期しないインスタンスを指定した場合
	 * @throws NoSuchMethodException 指定のフィールドが存在しない場合
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 * @throws InstantiationException 
	 */
	private Object getMaxValueFromSampleList(
		List rawSamples, String targetField, Class clazz)
		throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, InstantiationException
	{
		if(rawSamples.size() < 1)
		{
			Calculator calculator = getFieldCalculator(clazz, targetField);
			return calculator.immediate("0");
		}
		
		List values = getValuesByFieldName(rawSamples, targetField);
		
		if(!(values.get(0) instanceof Comparable))
		{
			return values.get(0);
		}
		
		Comparable maxValue = (Comparable)values.get(0);
		for(Object value : values)
		{
			if(maxValue.compareTo(value) < 0)
			{
				maxValue = (Comparable)value;
			}
		}
		
		return maxValue;
	}
	
	/**
	 * サンプリングデータのリストから、指定したフィールドの「最小値」を算出する。
	 * 
	 * @param rawSamples  サンプリングデータのリスト
	 * @param targetField 最小値を求めるフィールド
	 * @return 算出した最小値
	 * @throws IllegalAccessException アクセスできないフィールドにアクセスした場合
	 * @throws InvocationTargetException 予期しないインスタンスを指定した場合
	 * @throws NoSuchMethodException 指定のフィールドが存在しない場合
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 * @throws InstantiationException 
	 */
	private Object getMinValueFromSampleList(
		List rawSamples, String targetField, Class clazz) 
		throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, InstantiationException
	{
		if(rawSamples.size() < 1)
		{
			Calculator calculator = getFieldCalculator(clazz, targetField);
			return calculator.immediate("0");
		}
		
		List values = getValuesByFieldName(rawSamples, targetField);
		
		if(!(values.get(0) instanceof Comparable))
		{
			return values.get(0);
		}
		
		Comparable minValue = (Comparable)values.get(0);
		for(Object value : values)
		{
			if(minValue.compareTo(value) > 0)
			{
				minValue = (Comparable)value;
			}
		}
		
		return minValue;
	}
	
	/**
	 * 指定したデータリストから「圧縮値」を求める
	 * 
	 * @param rawSamples     サンプリングされた元データ
	 * @param operation      圧縮値を求める際の演算情報      
	 * @param clazz          元データのクラス情報
	 * @param compressedTerm 圧縮するデータの計測期間
	 * @return 算出された圧縮値
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException 
	 */
	private Object getCompressedValue(
		List rawSamples, CompressOperation operation, Class clazz, long compressedTerm) throws SecurityException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException
	{
		Calculator calculator = getFieldCalculator(clazz, operation.getCompressField());

		if(rawSamples.size() < 1)
		{
			return calculator.immediate("0");
		}
		
		List values = getValuesByFieldName(rawSamples, operation.getCompressField());
		
		switch(operation.getOperation())
		{
			case SIMPLE_AVERAGE :
				Object totalValue = calcTotal(values, calculator);
				return calculator.div(totalValue, calculator.immediate(String.valueOf(values.size())));
			case TOTAL :
				return calcTotal(values, calculator);
			case TIME_AVERAGE : 
				Object multipler = calculator.immediate(String.valueOf(this.rawSamplingTerm_));
				Object productSumValue = calcProductSum(values, multipler, calculator);
				Object samplingTerm = calculator.immediate(String.valueOf(compressedTerm));
				samplingTerm = calculator.div(samplingTerm, calculator.immediate(String.valueOf(SEC_PER_MILLIS)));
				return calculator.div(productSumValue, samplingTerm);
		}
		
		return null;
	}
	
	/**
	 * 指定したデータのリストから「合計値」を求める
	 * 
	 * @param values データのリスト
	 * @param calculator 計算機クラス
	 * @return 合計値
	 */
	private Object calcTotal(List values, Calculator calculator)
	{
		if(values.size() < 1)
		{
			return calculator.immediate("0");
		}
		
		if(values.size() == 1)
		{
			return values.get(0);
		}
		
		Object result = values.get(0);
		
		for(int cnt = 1; cnt < values.size(); cnt ++)
		{
			result = calculator.add(result, values.get(cnt));
		}
		
		return result;
	}

	/**
	 * 指定したデータのリストから、固定の倍数をかけた「積和」結果を求める
	 * 
	 * @param values    データのリスト
	 * @param multipler 積を求めるときに用いる倍数
	 * @param calculator 計算機クラス
	 * @return 積和結果
	 */
	private Object calcProductSum(List values, Object multipler, Calculator calculator)
	{
		if(values.size() < 1)
		{
			return calculator.immediate("0");
		}
		
		if(values.size() == 1)
		{
			return calculator.mul(values.get(0), multipler);
		}
		
		Object result = calculator.mul(values.get(0), multipler);
		
		for(int cnt = 1; cnt < values.size(); cnt ++)
		{
			Object adder = calculator.mul(values.get(cnt), multipler);
			result = calculator.add(result, adder);
		}
		
		return result;
	}
	
	
	/**
	 * 指定のJavaBeanを要素として持つリストから、指定のフィールドのデータのみを抜き出した
	 * リストを生成する。
	 * 
	 * @param rawSamples  JavaBeanが入ったリスト
	 * @param targetField 抜き出すフィールドのフィールド名
	 * @return 抜き出したデータのリスト
	 * @throws IllegalAccessException アクセスできないフィールドにアクセスした場合
	 * @throws InvocationTargetException 予期しないインスタンスを指定した場合
	 * @throws NoSuchMethodException 指定のフィールドが存在しない場合
	 */
	private List getValuesByFieldName(List rawSamples, String targetField) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		List values = new ArrayList();
		
		for(Object sample : rawSamples)
		{
			values.add(PropertyUtils.getProperty(sample, targetField));
		}
		
		return values;
	}
	
	/**
	 * 指定クラスのフィールドの値を計算するための計算機クラスインスタンスを取得する。
	 * 
	 * @param clazz     フィールドが属するクラス
	 * @param fieldName フィールドの名称
	 * @return フィールドの値を計算するための計算機クラス
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws Exception 指定のフィールドがクラスに存在しないなどのエラー
	 */
	private Calculator getFieldCalculator(Class clazz, String fieldName) throws SecurityException, NoSuchFieldException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException 
	{
		Object dummy = clazz.newInstance();
		Object dummyObj = PropertyUtils.getProperty(dummy, fieldName);
		
		return CalculatorFactory.createCalculator(dummyObj.getClass());
	}
	
	
}
