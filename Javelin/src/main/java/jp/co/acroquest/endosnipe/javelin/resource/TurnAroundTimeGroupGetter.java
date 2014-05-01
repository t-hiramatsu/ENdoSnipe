/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2012 Acroquest Technology Co.,Ltd.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package jp.co.acroquest.endosnipe.javelin.resource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.entity.ResourceItem;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.javelin.RootInvocationManager;
import jp.co.acroquest.endosnipe.javelin.bean.Invocation;
import jp.co.acroquest.endosnipe.javelin.bean.TurnAroundTimeInfo;

/**
 * Turn Around Time�̃O���[�v���擾����N���X�B
 * 
 * @author tsukano
 */
public class TurnAroundTimeGroupGetter implements ResourceGroupGetter, TelegramConstants
{
	/** Javelin��Config */
	private JavelinConfig config_ = new JavelinConfig();

	/**
	 * �R���X�g���N�^
	 */
	public TurnAroundTimeGroupGetter()
	{

	}

	/**
	 * {@inheritDoc}
	 */
	public Set<String> getItemNameSet()
	{
		// javelin.tat.monitor�̒l��false�̎��ɂ́A
		// �O���t�̒l���擾���Ȃ��悤�ɂ���B
		if (this.config_.isTatEnabled() == false)
		{
			return new HashSet<String>();
		}

		Set<String> retVal = new HashSet<String>();
		retVal.add(ITEMNAME_PROCESS_RESPONSE_TIME_AVERAGE);
		retVal.add(ITEMNAME_PROCESS_RESPONSE_TIME_MAX);
		retVal.add(ITEMNAME_PROCESS_RESPONSE_TIME_MIN);
		retVal.add(ITEMNAME_PROCESS_RESPONSE_TOTAL_COUNT);
		retVal.add(ITEMNAME_JAVAPROCESS_EXCEPTION_OCCURENCE_COUNT);
		retVal.add(ITEMNAME_JAVAPROCESS_STALL_OCCURENCE_COUNT);
		return retVal;
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<String, MultiResourceGetter> getResourceGroup()
	{
		long currentTime = System.currentTimeMillis();
		long tatZeroKeepTime = this.config_.getTatZeroKeepTime();

		// Turn Around Time���(���ϒl)
		List<ResourceItem> tatEntryList = new ArrayList<ResourceItem>();
		// Turn Around Time���(�ő�l)
		List<ResourceItem> tatMaxEntryList = new ArrayList<ResourceItem>();
		// Turn Around Time���(�ŏ��l)
		List<ResourceItem> tatMinEntryList = new ArrayList<ResourceItem>();
		// Turn Around Time�Ăяo���񐔏��
		List<ResourceItem> tstCountEntryList = new ArrayList<ResourceItem>();
		// ��O�����񐔏��
		List<ResourceItem> throwableCountEntryList = new ArrayList<ResourceItem>();

		// HTTP�X�e�[�^�X�G���[�����񐔏��
		List<ResourceItem> httpStatusCountEntryList = new ArrayList<ResourceItem>();
		// �X�g�[�����o�񐔏��
		List<ResourceItem> methodStallCountEntryList = new ArrayList<ResourceItem>();

		Invocation[] invocations = RootInvocationManager.getAllRootInvocations();

		for (Invocation invocation : invocations)
		{
			boolean output = invocation.isResponseGraphOutputTarget();
			if (!output)
			{
				continue;
			}
			TurnAroundTimeInfo info = invocation.resetAccumulatedTimeCount();

			// resetAccumulatedTimeCount() �ďo����ɁA�Ăяo���񐔂� 0 �ł�����Ԃ𒲂ׂ�
			long tatCallZeroValueStartTime = invocation.getTatCallZeroValueStartTime();
			if (tatCallZeroValueStartTime != Invocation.TAT_ZERO_KEEP_TIME_NULL_VALUE
					&& currentTime > tatCallZeroValueStartTime + tatZeroKeepTime)
			{
				// �Ăяo���񐔂� 0 �ł��鎞�Ԃ�臒l�𒴂����ꍇ�́A�N���C�A���g�ɒʒm���Ȃ�
				continue;
			}

			// �߂�l�ŕԂ�ResourceEntry���쐬����
			ResourceItem tatEntry = new ResourceItem();
			ResourceItem tatMaxEntry = new ResourceItem();
			ResourceItem tatMinEntry = new ResourceItem();
			ResourceItem tatCountEntry = new ResourceItem();

			// ResourceEntry��Name��ݒ肷��B
			// �X���b�V���͋�؂蕶���̂��߁A�X���b�V��������ꍇ�ɂ͕����Q�Ƃɕϊ�����
			String name = invocation.getRootInvocationManagerKey().replace("/", "&#47;");

			tatEntry.setName(ITEMNAME_PROCESS_RESPONSE_TIME_AVERAGE.replace("total", name));
			tatMaxEntry.setName(ITEMNAME_PROCESS_RESPONSE_TIME_MAX.replace("total", name));
			tatMinEntry.setName(ITEMNAME_PROCESS_RESPONSE_TIME_MIN.replace("total", name));
			tatCountEntry.setName(ITEMNAME_PROCESS_RESPONSE_TOTAL_COUNT.replace("total", name));

			// ResourceEntry��Value��ݒ肷��
			int count = info.getCallCount();
			tatCountEntry.setValue(String.valueOf(count));
			if (count == 0)
			{
				tatEntry.setValue(String.valueOf(Long.valueOf(0)));
			}
			else
			{
				tatEntry.setValue(String.valueOf(info.getTurnAroundTime() / count));
			}
			tatMaxEntry.setValue(String.valueOf(info.getTurnAroundTimeMax()));
			tatMinEntry.setValue(String.valueOf(info.getTurnAroundTimeMin()));

			// ResourceEntry�����X�g�ɐݒ肷��B
			tatEntryList.add(tatEntry);
			tatMaxEntryList.add(tatMaxEntry);
			tatMinEntryList.add(tatMinEntry);
			tstCountEntryList.add(tatCountEntry);

			// ��O��������ݒ�
			Map<String, Integer> throwableCountMap = info.getThrowableCountMap();
			for (Map.Entry<String, Integer> entry : throwableCountMap.entrySet())
			{
				String throwableName = entry.getKey();
				Integer throwableCount = entry.getValue();

				ResourceItem throwableCountEntry = new ResourceItem();
				throwableCountEntry.setName(ITEMNAME_JAVAPROCESS_EXCEPTION_OCCURENCE_COUNT.replace(
						"java", name) + "/" + throwableName);
				throwableCountEntry.setValue(String.valueOf(throwableCount));
				throwableCountEntryList.add(throwableCountEntry);
			}

			// HTTP�G���[��������ݒ�
			Map<String, Integer> httpStatusCountMap = info.getHttpStatusCountMap();
			for (Map.Entry<String, Integer> entry : httpStatusCountMap.entrySet())
			{
				String httpStatusName = entry.getKey();
				Integer httpStatusCount = entry.getValue();

				ResourceItem httpStatusCountEntry = new ResourceItem();
				httpStatusCountEntry.setName(ITEMNAME_JAVAPROCESS_EXCEPTION_OCCURENCE_COUNT
						.replace("java", name) + "/" + httpStatusName);
				httpStatusCountEntry.setValue(String.valueOf(httpStatusCount));
				httpStatusCountEntryList.add(httpStatusCountEntry);
			}

			// �X�g�[�����o����ݒ�
			ResourceItem methodStallCountEntry = new ResourceItem();
			methodStallCountEntry.setName(ITEMNAME_JAVAPROCESS_STALL_OCCURENCE_COUNT.replace(
					"java", name));
			methodStallCountEntry.setValue(String.valueOf(info.getMethodStallCount()));
			methodStallCountEntryList.add(methodStallCountEntry);
		}

		// �߂�l�ƂȂ�MultiResourceGetter�̃��X�g��ݒ肷��
		Map<String, MultiResourceGetter> retVal = new LinkedHashMap<String, MultiResourceGetter>();
		retVal.put(ITEMNAME_PROCESS_RESPONSE_TIME_AVERAGE, new TurnAroundTimeGetter(tatEntryList));
		retVal.put(ITEMNAME_PROCESS_RESPONSE_TIME_MAX, new TurnAroundTimeGetter(tatMaxEntryList));
		retVal.put(ITEMNAME_PROCESS_RESPONSE_TIME_MIN, new TurnAroundTimeGetter(tatMinEntryList));
		retVal.put(ITEMNAME_PROCESS_RESPONSE_TOTAL_COUNT, new TurnAroundTimeCountGetter(
				tstCountEntryList));
		retVal.put(ITEMNAME_JAVAPROCESS_EXCEPTION_OCCURENCE_COUNT, new TurnAroundTimeCountGetter(
				throwableCountEntryList));
		retVal.put(ITEMNAME_JAVAPROCESS_STALL_OCCURENCE_COUNT, new TurnAroundTimeCountGetter(
				methodStallCountEntryList));
		return retVal;
	}
}
