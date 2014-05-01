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
 * �����̃T���v�����O�f�[�^�����k���AExcel�ɕ\���\�ȃf�[�^�ɕϊ�����N���X�B
 * 
 * @author M.Yoshida
 */
@SuppressWarnings("unchecked")
public class SamplingCompressor 
{
	private static final String MAX_VALUE_PROPERTY_SUFFIX_ = "Max";
	
	private static final String MIN_VALUE_PROPERTY_SUFFIX_ = "Min";
	
	private static final String SAMPLING_MAX_NUM_KEY_      = "reporter.report.maxSamples";
	
	/** �T���v�����O�O���[�v�𐶐�����ۂ̍ŏ��v������ */
	private long minLimitSamplingTerm_ = 5000;

	/** ���f�[�^�̃T���v�����O����[sec] */
	private long rawSamplingTerm_ = 5;
	
	/** ���k��̃T���v�����O���̍ő吔 */
	private long samplingMax_ = 0;
	
	private static long SEC_PER_MILLIS = 1000;
	
	/** �f�t�H���g�̈��k��T���v�����O�� */
	private static long DEFAULT_SAMPLING_MAX = 200;
	
	/**
	 * �R���X�g���N�^�i�f�t�H���g�j
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
	 * �R���X�g���N�^
	 * 
	 * @param minTerm �T���v�����O�O���[�v�����̍ۂ̍ŏ��v������
	 */
	public SamplingCompressor(long minTerm)
	{
		super();
		this.minLimitSamplingTerm_ = minTerm;
	}
	
	/**
	 * �w��̃T���v�����O�f�[�^���A��萔�ȉ��ɂȂ�悤���k����B
	 * �w�肵���t�B�[���h�Ɋւ��ẮA���k�����f�[�^���A�ő�A�ŏ��A���ϒl���Z�o�E�⊮�ݒ肷��B
	 * 
	 * @param samplingList     �T���v�����O�f�[�^�BmeasureTimeField�Ŏw�肵���t�B�[���h�ɂ��\�[�g����Ă��邱�Ƃ�O��Ƃ���B
	 * @param startTime        ���k���̌v���J�n����
	 * @param endTime          ���k���̌v���I������
	 * @param measureTimeField �v���������i�[���Ă���t�B�[���h
	 * @param operation        ���k���̑���
	 * @param clazz            �T���v�����O�f�[�^��Class���
	 * @return ���k�����T���v�����O�f�[�^�̃��X�g
	 * @throws IllegalAccessException �A�N�Z�X�ł��Ȃ��t�B�[���h�ɃA�N�Z�X�����ꍇ
	 * @throws InvocationTargetException �\�����Ȃ��C���X�^���X���w�肵���ꍇ
	 * @throws NoSuchMethodException �w��̃t�B�[���h�����݂��Ȃ��ꍇ
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
		// �T���v�����O�O���[�v�����ۂ�1�O���[�v������́u�v�����ԁv���Z�o����B
		// �Z�o�������ʂ��A�v�����Ԃ��u�ŏ��v�����ԁv����������ꍇ�́A�u�ŏ��v�����ԁv�ɍ��킹��B
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
			// ���v�����ԓ��ɏ�������T���v���f�[�^�𒊏o����B
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
			
			// ���k�f�[�^�𐶐�����B
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
	 * �w��̃T���v�����O�f�[�^�����k���A�ЂƂ̃T���v�����O�f�[�^�ɕϊ�����B
	 * �w�肵���t�B�[���h�Ɋւ��ẮA���k�����f�[�^���A�ő�A�ŏ��A���ϒl���Z�o�E�⊮�ݒ肷��B
	 * 
	 * @param rawSamples   �T���v�����O�f�[�^
	 * @param targetFields �⊮���s���t�B�[���h
	 * @return ���k���s�����T���v�����O�f�[�^
	 * @throws IllegalAccessException �A�N�Z�X�ł��Ȃ��t�B�[���h�ɃA�N�Z�X�����ꍇ
	 * @throws InvocationTargetException �\�����Ȃ��C���X�^���X���w�肵���ꍇ
	 * @throws NoSuchMethodException �w��̃t�B�[���h�����݂��Ȃ��ꍇ
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
	 * �T���v�����O�f�[�^�̃��X�g����A�w�肵���t�B�[���h�́u�ő�l�v���Z�o����B
	 * 
	 * @param rawSamples  �T���v�����O�f�[�^�̃��X�g
	 * @param targetField �ő�l�����߂�t�B�[���h
	 * @return �Z�o�����ő�l
	 * @throws IllegalAccessException �A�N�Z�X�ł��Ȃ��t�B�[���h�ɃA�N�Z�X�����ꍇ
	 * @throws InvocationTargetException �\�����Ȃ��C���X�^���X���w�肵���ꍇ
	 * @throws NoSuchMethodException �w��̃t�B�[���h�����݂��Ȃ��ꍇ
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
	 * �T���v�����O�f�[�^�̃��X�g����A�w�肵���t�B�[���h�́u�ŏ��l�v���Z�o����B
	 * 
	 * @param rawSamples  �T���v�����O�f�[�^�̃��X�g
	 * @param targetField �ŏ��l�����߂�t�B�[���h
	 * @return �Z�o�����ŏ��l
	 * @throws IllegalAccessException �A�N�Z�X�ł��Ȃ��t�B�[���h�ɃA�N�Z�X�����ꍇ
	 * @throws InvocationTargetException �\�����Ȃ��C���X�^���X���w�肵���ꍇ
	 * @throws NoSuchMethodException �w��̃t�B�[���h�����݂��Ȃ��ꍇ
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
	 * �w�肵���f�[�^���X�g����u���k�l�v�����߂�
	 * 
	 * @param rawSamples     �T���v�����O���ꂽ���f�[�^
	 * @param operation      ���k�l�����߂�ۂ̉��Z���      
	 * @param clazz          ���f�[�^�̃N���X���
	 * @param compressedTerm ���k����f�[�^�̌v������
	 * @return �Z�o���ꂽ���k�l
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
	 * �w�肵���f�[�^�̃��X�g����u���v�l�v�����߂�
	 * 
	 * @param values �f�[�^�̃��X�g
	 * @param calculator �v�Z�@�N���X
	 * @return ���v�l
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
	 * �w�肵���f�[�^�̃��X�g����A�Œ�̔{�����������u�Ϙa�v���ʂ����߂�
	 * 
	 * @param values    �f�[�^�̃��X�g
	 * @param multipler �ς����߂�Ƃ��ɗp����{��
	 * @param calculator �v�Z�@�N���X
	 * @return �Ϙa����
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
	 * �w���JavaBean��v�f�Ƃ��Ď����X�g����A�w��̃t�B�[���h�̃f�[�^�݂̂𔲂��o����
	 * ���X�g�𐶐�����B
	 * 
	 * @param rawSamples  JavaBean�����������X�g
	 * @param targetField �����o���t�B�[���h�̃t�B�[���h��
	 * @return �����o�����f�[�^�̃��X�g
	 * @throws IllegalAccessException �A�N�Z�X�ł��Ȃ��t�B�[���h�ɃA�N�Z�X�����ꍇ
	 * @throws InvocationTargetException �\�����Ȃ��C���X�^���X���w�肵���ꍇ
	 * @throws NoSuchMethodException �w��̃t�B�[���h�����݂��Ȃ��ꍇ
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
	 * �w��N���X�̃t�B�[���h�̒l���v�Z���邽�߂̌v�Z�@�N���X�C���X�^���X���擾����B
	 * 
	 * @param clazz     �t�B�[���h��������N���X
	 * @param fieldName �t�B�[���h�̖���
	 * @return �t�B�[���h�̒l���v�Z���邽�߂̌v�Z�@�N���X
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws Exception �w��̃t�B�[���h���N���X�ɑ��݂��Ȃ��Ȃǂ̃G���[
	 */
	private Calculator getFieldCalculator(Class clazz, String fieldName) throws SecurityException, NoSuchFieldException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException 
	{
		Object dummy = clazz.newInstance();
		Object dummyObj = PropertyUtils.getProperty(dummy, fieldName);
		
		return CalculatorFactory.createCalculator(dummyObj.getClass());
	}
	
	
}
