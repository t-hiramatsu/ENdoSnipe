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
package jp.co.acroquest.endosnipe.communicator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.util.IOUtil;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.ResponseBody;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;

/**
 * �d���𐶐����邽�߂̃��[�e�B���e�B�N���X�ł��B<br />
 * 
 * @author y-komori
 */
public final class TelegramCreator implements TelegramConstants
{
	private static JavelinConfig javelinConfig__ = new JavelinConfig();

	private TelegramCreator()
	{
		// Do nothing.
	}

	/**
	 * ��̉����d���𐶐����܂��B<br />
	 * 
	 * @param telegramKind
	 *			�d�����
	 * @param requestKind
	 *			�v���������
	 * @return �d��
	 */
	public static Telegram createEmptyTelegram(final byte telegramKind, final byte requestKind)
	{
		Header header = new Header();
		header.setByteTelegramKind(telegramKind);
		header.setByteRequestKind(requestKind);

		Telegram telegram = new Telegram();
		telegram.setObjHeader(header);
		telegram.setObjBody(new Body[0]);
		return telegram;
	}

	/**
	 * Javelin���O�ʒm�d���𐶐����܂��B<br />
	 * Javelin���O���̂��̂�d���ɓY�t���܂��B
	 * 
	 * @param jvnFileName
	 *			Javelin���O�t�@�C����
	 * @param javelinLogContent
	 *			Javelin���O
	 * @param telegramId
	 *			�d�� ID
	 * @param itemName
	 *          ���ږ�
	 * @return ���O�ʒm�d��
	 */
	public static Telegram createJvnFileNotifyTelegram(final String jvnFileName,
			final String javelinLogContent, final long telegramId, final String itemName)
	{
		Telegram telegram =	createJvnLogDownloadTelegram(
		                             BYTE_REQUEST_KIND_NOTIFY, 
		                             new Object[]{jvnFileName},
		                             new Object[]{javelinLogContent}, 
									 telegramId, 
									 itemName);

		return telegram;
	}

	/**
	 * JVN���O�d���𐶐����܂��B<br />
	 * 
	 * @param requestKind
	 *			�v���������
	 * @param jvnFileNames
	 *			Javelin���O�t�@�C����
	 * @return JVN���O�d��
	 */
	public static Telegram createJvnLogDownloadTelegram(final byte requestKind,
			final Object[] jvnFileNames)
	{
		return createJvnLogDownloadTelegram(requestKind, jvnFileNames, null, 0, null);
	}

	/**
	 * JVN���O�d���𐶐����܂��B<br />
	 * ���O�t�@�C���̓��e��null�̏ꍇ�̓t�@�C������ǂݍ��݂܂��B
	 * 
	 * @param requestKind
	 *			�v���������
	 * @param jvnFileNames
	 *			Javelin���O�t�@�C����
	 * @param jvnFileContents
	 *			Javelin���O�t�@�C���̓��e
	 * @param telegramId
	 *			�d�� ID
	 * @param itemName
	 *          ���ږ�
	 * @return JVN���O�d��
	 */
	public static Telegram createJvnLogDownloadTelegram(final byte requestKind,
			final Object[] jvnFileNames, Object[] jvnFileContents, 
			final long telegramId, String itemName)
	{
		final JavelinConfig JAVELINCONFIG = new JavelinConfig();

		// �d���w�b�_�����
		Header objHeader = new Header();
		objHeader.setId(telegramId);
		objHeader.setByteRequestKind(requestKind);
		objHeader.setByteTelegramKind(BYTE_TELEGRAM_KIND_JVN_FILE);

		// �d���{�̂����
		ResponseBody jvnFileNameBody =
				TelegramUtil.createResponseBody(
	                                OBJECTNAME_JVN_FILE, 
	                                ITEMNAME_JVN_FILE_NAME,
									ItemType.ITEMTYPE_STRING, 
									jvnFileNames);

		if (jvnFileContents == null)
		{
			jvnFileContents = new String[jvnFileNames.length];
		}

		if (jvnFileNames.length != jvnFileContents.length)
		{
			throw new IllegalArgumentException();
		}

		for (int index = 0; index < jvnFileNames.length; index++)
		{
			if (jvnFileContents[index] == null)
			{
				Object objJvnFileName = jvnFileNames[index];
				if (objJvnFileName instanceof String == false)
				{
					continue;
				}
				String jvnFileName = (String)objJvnFileName;
				String jvnFileContent =
						IOUtil.readFileToString(
		                          JAVELINCONFIG.getJavelinFileDir()
		                        + File.separator + jvnFileName, 
		                        JAVELINCONFIG.getJvnDownloadMax());
				jvnFileContents[index] = jvnFileContent;
			}
		}
		ResponseBody jvnFileContentBody =
				TelegramUtil.createResponseBody(
                                OBJECTNAME_JVN_FILE, ITEMNAME_JVN_FILE_CONTENT, 
                                ItemType.ITEMTYPE_STRING, jvnFileContents);

		Object[] itemNames = new Object[jvnFileNames.length];
		for (int i = 0; i < itemNames.length; i++)
		{
			itemNames[i] = itemName;
		}
		ResponseBody jvnItemNameBody =
				TelegramUtil.createResponseBody(
                                OBJECTNAME_JVN_FILE, ITEMNAME_JVN_ITEM_NAME, 
                                ItemType.ITEMTYPE_STRING, itemNames);

		// 臒l�ݒ��d���ɒǉ�����B
		ResponseBody thresholdBody =
				TelegramUtil.createResponseBody(
                                OBJECTNAME_JVN_FILE, 
                                ITEMNAME_ALARM_THRESHOLD, 
                                ItemType.ITEMTYPE_LONG, 
                                new Long[]{javelinConfig__.getAlarmThreshold()});
		
		ResponseBody cpuThresholdBody =
				TelegramUtil.createResponseBody(
                                OBJECTNAME_JVN_FILE, 
                                ITEMNAME_ALARM_CPU_THRESHOLD, 
                                ItemType.ITEMTYPE_LONG, 
                                new Long[]{javelinConfig__.getAlarmCpuThreashold()});

		// �d���I�u�W�F�N�g��ݒ肷��
		Telegram objTelegram = new Telegram();
		objTelegram.setObjHeader(objHeader);
		objTelegram.setObjBody(new ResponseBody[]{jvnFileNameBody, jvnFileContentBody,
				thresholdBody, cpuThresholdBody, jvnItemNameBody});

		return objTelegram;
	}

	/**
	 * JVN���O�擾�����d���𐶐����܂��B<br />
	 * 
	 * @param jvnFileNames
	 *			JVN���O�t�@�C�����̃��X�g (<code>null</code> �̏ꍇ�� <code>null</code> ��Ԃ�)
	 * @return �d���I�u�W�F�N�g
	 */
	public static Telegram createJvnLogListTelegram(final String[] jvnFileNames)
	{
		// �t�@�C�����̃��X�g��null�̂Ƃ��Anull��Ԃ��B
		if (jvnFileNames == null)
		{
			return null;
		}
		// �d���w�b�_�����
		Header objHeader = new Header();
		objHeader.setByteRequestKind(BYTE_REQUEST_KIND_RESPONSE);
		objHeader.setByteTelegramKind(BYTE_TELEGRAM_KIND_JVN_FILE_LIST);

		// �d���{�̂����
		ResponseBody body =
				TelegramUtil.createResponseBody(
                                        "jvnFile", 
                                        "jvnFileName", 
                                        ItemType.ITEMTYPE_STRING,
                    					jvnFileNames);

		// �d���I�u�W�F�N�g��ݒ肷��
		Telegram objTelegram = new Telegram();
		objTelegram.setObjHeader(objHeader);
		objTelegram.setObjBody(new ResponseBody[]{body});

		return objTelegram;
	}

	/**
	 * �N���X�폜�v���d���𐶐����܂��B<br />
	 * 
	 * @param classNameList
	 *			�N���X���̃��X�g
	 * @return �d���I�u�W�F�N�g
	 */
	public static Telegram createRemoveClassTelegram(final List<String> classNameList)
	{
		Header objHeader = new Header();
		objHeader.setByteTelegramKind(BYTE_TELEGRAM_KIND_REMOVE_CLASS);
		objHeader.setByteRequestKind(BYTE_REQUEST_KIND_REQUEST);

		Telegram objOutputTelegram = new Telegram();
		objOutputTelegram.setObjHeader(objHeader);

		List<Body> bodies = new ArrayList<Body>();
		if (classNameList != null)
		{
			for (String className : classNameList)
			{
				Body body = new Body();
				body.setStrObjName(className);
				body.setByteItemMode(ItemType.ITEMTYPE_STRING);
				body.setStrItemName(ITEMNAME_CLASSTOREMOVE);
				body.setIntLoopCount(0);
				body.setObjItemValueArr(new Object[0]);
				bodies.add(body);
			}
		}

		objOutputTelegram.setObjBody(bodies.toArray(new Body[bodies.size()]));
		return objOutputTelegram;
	}

	/**
	 * Invocation ���X�V���邽�߂̓d���𐶐����܂��B<br />
	 * 
	 * @param invocationParamArray
	 *			Invocation ���X�V������e
	 * @return �d���I�u�W�F�N�g
	 */
	public static Telegram createUpdateInvocationTelegram(
			final UpdateInvocationParam[] invocationParamArray)
	{
		Header objHeader = new Header();
		objHeader.setByteTelegramKind(BYTE_TELEGRAM_KIND_UPDATE_TARGET);
		objHeader.setByteRequestKind(BYTE_REQUEST_KIND_REQUEST);

		Telegram objOutputTelegram = new Telegram();
		objOutputTelegram.setObjHeader(objHeader);

		List<Body> bodies = new ArrayList<Body>();

		// �d���{�̂�ݒ肷��
		if (invocationParamArray != null)
		{
			for (UpdateInvocationParam invocation : invocationParamArray)
			{
				Body body;
				Object[] itemValueArr;
				String objName =
						invocation.getClassName() + CLASSMETHOD_SEPARATOR
								+ invocation.getMethodName();

				if (invocation.isTransactionGraphOutput() != null)
				{
					body = new Body();
					body.setStrObjName(objName);
					body.setByteItemMode(ItemType.ITEMTYPE_STRING);
					body.setStrItemName(ITEMNAME_TRANSACTION_GRAPH);
					body.setIntLoopCount(1);
					itemValueArr = new String[1];
					itemValueArr[0] 
					        = invocation.isTransactionGraphOutput().toString();
					body.setObjItemValueArr(itemValueArr);
					bodies.add(body);
				}

				if (invocation.isTarget() != null)
				{
					body = new Body();
					body.setStrObjName(objName);
					body.setByteItemMode(ItemType.ITEMTYPE_STRING);
					body.setStrItemName(ITEMNAME_TARGET);
					body.setIntLoopCount(1);
					itemValueArr = new String[1];
					itemValueArr[0] = String.valueOf(invocation.isTarget());
					body.setObjItemValueArr(itemValueArr);
					bodies.add(body);
				}

				if (invocation.getAlarmThreshold() != null)
				{
					body = new Body();
					body.setStrObjName(objName);
					body.setByteItemMode(ItemType.ITEMTYPE_LONG);
					body.setStrItemName(ITEMNAME_ALARM_THRESHOLD);
					body.setIntLoopCount(1);
					itemValueArr = new Long[1];
					itemValueArr[0] = invocation.getAlarmThreshold();
					body.setObjItemValueArr(itemValueArr);
					bodies.add(body);
				}

				if (invocation.getAlarmCpuThreshold() != null)
				{
					body = new Body();
					body.setStrObjName(objName);
					body.setByteItemMode(ItemType.ITEMTYPE_LONG);
					body.setStrItemName(ITEMNAME_ALARM_CPU_THRESHOLD);
					body.setIntLoopCount(1);
					itemValueArr = new Long[1];
					itemValueArr[0] = invocation.getAlarmCpuThreshold();
					body.setObjItemValueArr(itemValueArr);
					bodies.add(body);
				}
			}
		}
		objOutputTelegram.setObjBody(bodies.toArray(new Body[bodies.size()]));
		return objOutputTelegram;
	}

	/**
	 * Invocation ���X�V���邽�߂̃p�����^�N���X�B<br />
	 * 
	 * @author sakamoto
	 */
	public static class UpdateInvocationParam
	{
		private final String className_;

		private final String methodName_;

		private final Boolean transactionGraphOutput_;

		private final Boolean target_;

		private final Long alarmThreshold_;

		private final Long alarmCpuThreshold_;

		/**
		 * Invocation ���X�V���邽�߂̃p�����^�I�u�W�F�N�g�𐶐����܂��B<br />
		 * 
		 * @param className
		 *			�N���X��
		 * @param methodName
		 *			���\�b�h��
		 * @param transactionGraph
		 *			�g�����U�N�V�����O���t���o�͂��邩�ۂ��i <code>null</code> �Ȃ�ݒ肵�Ȃ��j
		 * @param target
		 *			�v���Ώۂɂ��邩�ۂ��i <code>null</code> �Ȃ�ݒ肵�Ȃ��j
		 * @param alarmThreshold
		 *			�A���[��臒l�i <code>null</code> �Ȃ�ݒ肵�Ȃ��j
		 * @param alarmCpuThreshold
		 *			CPU�A���[��臒l�i <code>null</code> �Ȃ�ݒ肵�Ȃ��j
		 */
		public UpdateInvocationParam(String className, String methodName, 
		        Boolean transactionGraph,
				Boolean target, Long alarmThreshold, Long alarmCpuThreshold)
		{
			this.className_ = className;
			this.methodName_ = methodName;
			this.transactionGraphOutput_ = transactionGraph;
			this.target_ = target;
			this.alarmThreshold_ = alarmThreshold;
			this.alarmCpuThreshold_ = alarmCpuThreshold;
		}

		/**
		 * �N���X����Ԃ��܂��B<br />
		 * 
		 * @return �N���X��
		 */
		public String getClassName()
		{
			return this.className_;
		}

		/**
		 * ���\�b�h����Ԃ��܂��B<br />
		 * 
		 * @return ���\�b�h��
		 */
		public String getMethodName()
		{
			return this.methodName_;
		}

		/**
		 * �g�����U�N�V�����O���t��\�����邩�ۂ���Ԃ��܂��B<br />
		 * 
		 * �ݒ肵�Ȃ��ꍇ�� <code>null</code> ��Ԃ��܂��B<br />
		 * 
		 * @return �g�����U�N�V�����O���t��\������ꍇ�� <code>true</code>
		 */
		public Boolean isTransactionGraphOutput()
		{
			return this.transactionGraphOutput_;
		}

		/**
		 * �v���Ώۂɂ��邩�ۂ���Ԃ��܂��B<br />
		 * 
		 * �ݒ肵�Ȃ��ꍇ�� <code>null</code> ��Ԃ��܂��B<br />
		 * 
		 * @return �v���Ώۂɂ���ꍇ�� <code>true</code>
		 */
		public Boolean isTarget()
		{
			return this.target_;
		}

		/**
		 * �A���[��臒l��Ԃ��܂��B<br />
		 * 
		 * �ݒ肵�Ȃ��ꍇ�� <code>null</code> ��Ԃ��܂��B<br />
		 * 
		 * @return �A���[��臒l�i�~���b�j
		 */
		public Long getAlarmThreshold()
		{
			return this.alarmThreshold_;
		}

		/**
		 * CPU�A���[��臒l��Ԃ��܂��B<br />
		 * 
		 * �ݒ肵�Ȃ��ꍇ�� <code>null</code> ��Ԃ��܂��B<br />
		 * 
		 * @return CPU�A���[��臒l
		 */
		public Long getAlarmCpuThreshold()
		{
			return this.alarmCpuThreshold_;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("{className=");
			builder.append(getClassName());
			builder.append(",methodName=");
			builder.append(getMethodName());
			builder.append("}");
			return builder.toString();
		}
	}

	/**
	 * JVN���O�擾�d�����쐬����B
	 * 
	 * @param requestKind
	 *			�d���v��������ʁB
	 * @param jvnFileNames
	 *			JVN���O�t�@�C�����B
	 * @return JVN���O�擾�d���B
	 */
	public static Telegram createJvnLogTelegram(final byte requestKind,
			final List<String> jvnFileNames)
	{
		Telegram telegram =
				TelegramUtil.createSingleTelegram(
	                                  BYTE_TELEGRAM_KIND_JVN_FILE, requestKind,
	                                  OBJECTNAME_JVN_FILE, ITEMNAME_JVN_FILE_NAME, 
	                                  ItemType.ITEMTYPE_STRING, jvnFileNames);

		return telegram;
	}

	/**
	 * �X���b�h�_���v�擾�v���d�����쐬���܂��B
	 * 
	 * @return �X���b�h�_���v�擾�v���d��
	 */
	public static Telegram createThreadDumpRequestTelegram()
	{
		Header objHeader = new Header();
		objHeader.setId(TelegramUtil.generateTelegramId());
		objHeader.setByteTelegramKind(BYTE_TELEGRAM_KIND_GET_DUMP);
		objHeader.setByteRequestKind(BYTE_REQUEST_KIND_REQUEST);
		Body[] bodies = 
		        TelegramUtil.createEmptyRequestBody(OBJECTNAME_DUMP, ITEMNAME_THREADDUMP);

		Telegram requestTelegram = new Telegram();
		requestTelegram.setObjHeader(objHeader);
		requestTelegram.setObjBody(bodies);

		return requestTelegram;
	}

	/**
	 * �q�[�v�_���v�擾�v���d�����쐬���܂��B
	 * 
	 * @return �q�[�v�_���v�擾�v���d��
	 */
	public static Telegram createHeapDumpRequestTelegram()
	{
		Header objHeader = new Header();
		objHeader.setId(TelegramUtil.generateTelegramId());
		objHeader.setByteTelegramKind(BYTE_TELEGRAM_KIND_GET_DUMP);
		objHeader.setByteRequestKind(BYTE_REQUEST_KIND_REQUEST);
		Body[] bodies = 
		        TelegramUtil.createEmptyRequestBody(OBJECTNAME_DUMP, ITEMNAME_HEAPDUMP);

		Telegram objOutputTelegram = new Telegram();
		objOutputTelegram.setObjHeader(objHeader);
		objOutputTelegram.setObjBody(bodies);
		return objOutputTelegram;
	}

	/**
	 * �q�[�v�_���v�擾�����d�����쐬���܂��B
	 * 
	 * @param telegramId
	 *			�d�� ID
	 * @return �q�[�v�_���v�擾�����d��
	 */
	public static Telegram createHeapDumpResponseTelegram(final long telegramId)
	{
		Header objHeader = new Header();
		objHeader.setId(telegramId);
		objHeader.setByteTelegramKind(BYTE_TELEGRAM_KIND_GET_DUMP);
		objHeader.setByteRequestKind(BYTE_REQUEST_KIND_RESPONSE);
		Body[] bodies = 
		        TelegramUtil.createEmptyRequestBody(OBJECTNAME_DUMP, ITEMNAME_HEAPDUMP);

		Telegram objOutputTelegram = new Telegram();
		objOutputTelegram.setObjHeader(objHeader);
		objOutputTelegram.setObjBody(bodies);
		return objOutputTelegram;
	}

	/**
	 * �N���X�q�X�g�O�����擾�v���d�����쐬���܂��B
	 * 
	 * @return �N���X�q�X�g�O�����擾�v���d��
	 */
	public static Telegram createClassHistogramRequestTelegram()
	{
		Header objHeader = new Header();
		objHeader.setId(TelegramUtil.generateTelegramId());
		objHeader.setByteTelegramKind(BYTE_TELEGRAM_KIND_GET_DUMP);
		objHeader.setByteRequestKind(BYTE_REQUEST_KIND_REQUEST);
		Body[] bodies = 
		        TelegramUtil.createEmptyRequestBody(
                                    OBJECTNAME_DUMP, ITEMNAME_CLASSHISTOGRAM);

		Telegram objOutputTelegram = new Telegram();
		objOutputTelegram.setObjHeader(objHeader);
		objOutputTelegram.setObjBody(bodies);
		return objOutputTelegram;
	}

	/**
	 * �N���X�q�X�g�O�����擾�����d�����쐬���܂��B
	 * 
	 * @param telegramId
	 *			�d�� ID
	 * @return �N���X�q�X�g�O�����擾�����d��
	 */
	public static Telegram createClassHistogramResponseTelegram(final long telegramId)
	{
		Header objHeader = new Header();
		objHeader.setId(TelegramUtil.generateTelegramId());
		objHeader.setByteTelegramKind(BYTE_TELEGRAM_KIND_GET_DUMP);
		objHeader.setByteRequestKind(BYTE_REQUEST_KIND_RESPONSE);
		Body[] bodies =
				TelegramUtil.createEmptyRequestBody(
                                    OBJECTNAME_DUMP, ITEMNAME_CLASSHISTOGRAM);

		Telegram objOutputTelegram = new Telegram();
		objOutputTelegram.setObjHeader(objHeader);
		objOutputTelegram.setObjBody(bodies);
		return objOutputTelegram;
	}
}
