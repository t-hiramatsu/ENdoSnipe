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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.RequestBody;
import jp.co.acroquest.endosnipe.communicator.entity.ResourceItemConverter;
import jp.co.acroquest.endosnipe.communicator.entity.ResponseBody;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;

/**
 * �d���Ɋւ����{�@�\��񋟂��邽�߂̃��[�e�B���e�B�N���X�ł��B<br />
 * 
 * @author eriguchi
 */
public final class TelegramUtil implements TelegramConstants
{
	private static final ENdoSnipeLogger LOGGER = 
			ENdoSnipeLogger.getLogger(TelegramUtil.class);

	/** ���M�d���̍ő咷 */
	public static final int TELEGRAM_LENGTH_MAX = 10 * 1024 * 1024;

	/** ��M�d���̍ő咷 */
	public static final int TELEGRAM_READ_LENGTH_MAX = 1 * 1024 * 1024;

	/** short����byte�z��ւ̕ϊ����ɕK�v�ȃo�C�g�� */
	private static final int SHORT_BYTE_SWITCH_LENGTH = 2;

	/** int����byte�z��ւ̕ϊ����ɕK�v�ȃo�C�g�� */
	private static final int INT_BYTE_SWITCH_LENGTH = 4;

	/** long����byte�z��ւ̕ϊ����ɕK�v�ȃo�C�g�� */
	private static final int LONG_BYTE_SWITCH_LENGTH = 8;

	/** float����byte�z��ւ̕ϊ����ɕK�v�ȃo�C�g�� */
	private static final int FLOAT_BYTE_SWITCH_LENGTH = 4;

	/** double����byte�z��ւ̕ϊ����ɕK�v�ȃo�C�g�� */
	private static final int DOUBLE_BYTE_SWITCH_LENGTH = 8;

	/** �w�b�_�̒��� */
	public static final int TELEGRAM_HEADER_LENGTH = 18;

	/** ���s�����B */
	public static final String NEW_LINE = System.getProperty("line.separator");

	/** ������̕����R�[�h */
	private static final String CHAR_CODE = "UTF-8";

	private static final long TELEGRAM_ID_START = 1;

	/** �d�� ID */
	private static final AtomicLong TELEGRAM_ID = new AtomicLong(TELEGRAM_ID_START);

	private TelegramUtil()
	{
		// Do nothing.
	}

	/**
	 * �d�� ID �𐶐����܂��B
	 * 
	 * @return �d�� ID
	 */
	public static long generateTelegramId()
	{
		// �d�� ID ���ő�l�ɒB�����ꍇ�́A�l��߂�
		TELEGRAM_ID.compareAndSet(Long.MAX_VALUE, TELEGRAM_ID_START);
		return TELEGRAM_ID.getAndIncrement();
	}

	/**
	 * ��������o�C�g�z��i�S�o�C�g�����񒷁{UTF8�j�ɕϊ����܂��B<br />
	 * 
	 * @param text
	 *            ������
	 * @return �o�C�g�z��
	 */
	public static byte[] stringToByteArray(final String text)
	{
		byte[] textArray = new byte[0];
		try
		{
			textArray = text.getBytes(CHAR_CODE);
		}
		catch (UnsupportedEncodingException ex)
		{
			SystemLogger.getInstance().warn(ex);
		}
		byte[] lengthArray = intToByteArray(textArray.length);

		// �����񒷂ƕ��������������
		return combineTwoByteArray(lengthArray, textArray);
	}

	/**
	 * 4�o�C�g�̕����񒷁{UTF8�̃o�C�g�z�񂩂當������쐬���܂��B<br />
	 * 
	 * @param buffer
	 *            �o�C�g�z��
	 * @return ������
	 */
	public static String byteArrayToString(final ByteBuffer buffer)
	{
		String strResult = "";

		// �����񒷂��擾����
		int intbyteArrLength = buffer.getInt();

		if (intbyteArrLength > TELEGRAM_READ_LENGTH_MAX)
		{
			intbyteArrLength = TELEGRAM_READ_LENGTH_MAX;
			LOGGER.log("WECC0101", intbyteArrLength);
		}

		try
		{
			byte[] byteSoruceArr = new byte[intbyteArrLength];
			buffer.get(byteSoruceArr);

			strResult = new String(byteSoruceArr, 0, intbyteArrLength,
					CHAR_CODE);
		}
		catch (UnsupportedEncodingException uee)
		// CHECKSTYLE:OFF
		{
			// Do nothing.
		}
		// CHECKSTYLE:ON
		catch (Throwable th)
		{
			LOGGER.log("WECC0102", th);
		}

		return strResult;
	}

	/**
	 * �Q�o�C�g�����t�������o�C�g�z��ɕϊ����܂��B<br />
	 * 
	 * @param value
	 *            �l
	 * @return �o�C�g�z��
	 */
	public static byte[] shortToByteArray(final short value)
	{
		ByteBuffer buffer = ByteBuffer.allocate(SHORT_BYTE_SWITCH_LENGTH);
		buffer.putShort(value);
		return buffer.array();
	}

	/**
	 * �S�o�C�g�����t�������o�C�g�z��ɕϊ����܂��B<br />
	 * 
	 * @param value
	 *            �l
	 * @return �o�C�g�z��
	 */
	public static byte[] intToByteArray(final int value)
	{
		ByteBuffer buffer = ByteBuffer.allocate(INT_BYTE_SWITCH_LENGTH);
		buffer.putInt(value);
		return buffer.array();
	}

	/**
	 * �W�o�C�g�����t�������o�C�g�z��ɕϊ����܂��B<br />
	 * 
	 * @param value
	 *            �l
	 * @return �o�C�g�z��
	 */
	public static byte[] longToByteArray(final long value)
	{
		ByteBuffer buffer = ByteBuffer.allocate(LONG_BYTE_SWITCH_LENGTH);
		buffer.putLong(value);
		return buffer.array();
	}

	/**
	 * �S�o�C�g�����t�������o�C�g�z��ɕϊ����܂��B<br />
	 * 
	 * @param value
	 *            �l
	 * @return �o�C�g�z��
	 */
	public static byte[] floatToByteArray(final float value)
	{
		ByteBuffer buffer = ByteBuffer.allocate(FLOAT_BYTE_SWITCH_LENGTH);
		buffer.putFloat(value);
		return buffer.array();
	}

	/**
	 * �W�o�C�g�����t�������o�C�g�z��ɕϊ����܂��B<br />
	 * 
	 * @param value
	 *            �l
	 * @return �o�C�g�z��
	 */
	public static byte[] doubleToByteArray(final double value)
	{
		ByteBuffer buffer = ByteBuffer.allocate(DOUBLE_BYTE_SWITCH_LENGTH);
		buffer.putDouble(value);
		return buffer.array();
	}

	/**
	 * �d���I�u�W�F�N�g���o�C�g�z��ɕϊ����܂��B<br />
	 * 
	 * @param objTelegram
	 *            �d���I�u�W�F�N�g
	 * @return �o�C�g�z��
	 */
	public static List<byte[]> createTelegram(final Telegram objTelegram)
	{
		Header header = objTelegram.getObjHeader();

		List<byte[]> telegramList = new ArrayList<byte[]>();

		// �{�̃f�[�^�����
		ByteArrayOutputStream byteArrayOutputStream = newByteStream(header);

		if (objTelegram.getObjBody() != null)
		{
			for (Body body : objTelegram.getObjBody())
			{
				// �T�C�Y�𒴂����ꍇ�ɂ́A�����܂łň�U���M����B
				if (byteArrayOutputStream.size() > TELEGRAM_LENGTH_MAX)
				{
					byte[] bytesBody = byteArrayOutputStream.toByteArray();
					int telegramLength = bytesBody.length;
					ByteBuffer outputBuffer = ByteBuffer.wrap(bytesBody);

					// �w�b�_��ϊ�����
					outputBuffer.rewind();
					outputBuffer.putInt(telegramLength);
					telegramList.add(outputBuffer.array());

					// �V�����d���Ƃ��āA�w�b�_��ǉ�����B
					byteArrayOutputStream = newByteStream(header);
				}

				try
				{
					byte[] bytesObjName = stringToByteArray(body
							.getStrObjName());
					byte[] bytesItemName = stringToByteArray(body
							.getStrItemName());
					byte[] bytesObjDispName = stringToByteArray(body
							.getStrObjDispName());
					// �{�̃f�[�^�ɐݒ肷��
					byteArrayOutputStream.write(bytesObjName);
					byteArrayOutputStream.write(bytesItemName);
					byteArrayOutputStream.write(bytesObjDispName);

					Body responseBody = body;
					ItemType itemType = responseBody.getByteItemMode();
					int loopCount = responseBody.getIntLoopCount();
					byte[] itemModeArray = new byte[]
					{ ItemType.getItemTypeNumber(itemType) };
					byte[] loopCountArray = intToByteArray(loopCount);
					byteArrayOutputStream.write(itemModeArray);
					byteArrayOutputStream.write(loopCountArray);

					for (int index = 0; index < loopCount; index++)
					{
						Object obj 
							= responseBody.getObjItemValueArr()[index];
						byte[] value = null;
						switch (itemType)
						{
						case ITEMTYPE_BYTE:
							value = new byte[]
							{ ((Number) obj).byteValue() };
							break;
						case ITEMTYPE_SHORT:
							value = shortToByteArray(((Number) obj)
									.shortValue());
							break;
						case ITEMTYPE_INT:
							value
								= intToByteArray(
									((Number) obj).intValue());
							break;
						case ITEMTYPE_LONG:
							value
								= longToByteArray(
									((Number) obj).longValue());
							break;
						case ITEMTYPE_FLOAT:
							value = floatToByteArray(((Number) obj)
									.floatValue());
							break;
						case ITEMTYPE_DOUBLE:
							value = doubleToByteArray(((Number) obj)
									.doubleValue());
							break;
						case ITEMTYPE_STRING:
							value = stringToByteArray((String) obj);
							break;
						case ITEMTYPE_JMX:
							value = stringToByteArray((String) obj);
							break;
						default:
							return null;
						}
						byteArrayOutputStream.write(value);
					}
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
					continue;
				}
			}
		}

		byte[] bytesBody = byteArrayOutputStream.toByteArray();
		int telegramLength = bytesBody.length;
		ByteBuffer outputBuffer = ByteBuffer.wrap(bytesBody);

		// �w�b�_��ϊ�����
		outputBuffer.rewind();
		outputBuffer.putInt(telegramLength);
		outputBuffer.position(INT_BYTE_SWITCH_LENGTH + LONG_BYTE_SWITCH_LENGTH);
		outputBuffer.put(FINAL_TELEGRAM);
		return Arrays.asList(new byte[][]
		{ outputBuffer.array() });
	}

	private static ByteArrayOutputStream newByteStream(Header header)
	{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ByteBuffer headerBuffer = initHeaderByteBuffer(header, HALFWAY_TELEGRAM);
		try
		{
			byteArrayOutputStream.write(headerBuffer.array());
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		return byteArrayOutputStream;
	}

	private static ByteBuffer initHeaderByteBuffer(Header header,
			byte finalTelegram)
	{
		ByteBuffer headerBuffer = ByteBuffer.allocate(TELEGRAM_HEADER_LENGTH);
		headerBuffer.putInt(0); // �d�����͌�œ����B
		headerBuffer.putLong(header.getId());
		headerBuffer.put(finalTelegram);
		headerBuffer.put(header.getByteTelegramKind());
		headerBuffer.put(header.getByteRequestKind());
		return headerBuffer;
	}

	/**
	 * �o�C�g�z���d���I�u�W�F�N�g�ɕϊ����܂��B<br />
	 * 
	 * @param byteTelegramArr
	 *            �o�C�g�z��
	 * @return �d���I�u�W�F�N�g
	 */
	public static Telegram recoveryTelegram(final byte[] byteTelegramArr)
	{
		// �ԋp����p
		Telegram objTelegram = new Telegram();

		if (byteTelegramArr == null)
		{
			return null;
		}

		ByteBuffer telegramBuffer = ByteBuffer.wrap(byteTelegramArr);

		// �܂��AHeader�����擾����
		Header objHeader = new Header();
		objHeader.setIntSize(telegramBuffer.getInt());
		objHeader.setId(telegramBuffer.getLong());
		objHeader.setLastTelegram(telegramBuffer.get());
		objHeader.setByteTelegramKind(telegramBuffer.get());
		objHeader.setByteRequestKind(telegramBuffer.get());
		telegramBuffer.get();
		telegramBuffer.get();
		telegramBuffer.get();

		byte kind = objHeader.getByteRequestKind();
		boolean isResponseBody = 
			(kind == BYTE_REQUEST_KIND_RESPONSE || kind == BYTE_REQUEST_KIND_NOTIFY);

		List<Body> bodyList = new ArrayList<Body>();

		// �{�̂��擾����
		while (telegramBuffer.remaining() > 0)
		{
			Body body;
			String objectName = byteArrayToString(telegramBuffer);
			String itemName = byteArrayToString(telegramBuffer);
			String objDispName = byteArrayToString(telegramBuffer);

			if (isResponseBody)
			{
				body = new ResponseBody();
			}
			else
			{
				body = new RequestBody();
			}

			// ���ڌ^�ݒ�
			body.setByteItemMode(ItemType.getItemType(telegramBuffer.get()));

			// �J��Ԃ��񐔐ݒ�
			body.setIntLoopCount(telegramBuffer.getInt());

			// �l�ݒ�
			Object[] values = new Object[body.getIntLoopCount()];
			for (int index = 0; index < values.length; index++)
			{
				switch (body.getByteItemMode())
				{
				case ITEMTYPE_BYTE:
					values[index] = telegramBuffer.get();
					break;
				case ITEMTYPE_SHORT:
					values[index] = telegramBuffer.getShort();
					break;
				case ITEMTYPE_INT:
					values[index] = telegramBuffer.getInt();
					break;
				case ITEMTYPE_LONG:
					values[index] = telegramBuffer.getLong();
					break;
				case ITEMTYPE_FLOAT:
					values[index] = telegramBuffer.getFloat();
					break;
				case ITEMTYPE_DOUBLE:
					values[index] = telegramBuffer.getDouble();
					break;
				case ITEMTYPE_STRING:
					values[index] = byteArrayToString(telegramBuffer);
					break;
				case ITEMTYPE_JMX:
					String jsonStr = byteArrayToString(telegramBuffer);
					try
					{
						values[index] = ResourceItemConverter.getInstance()
								.decodeFromJSON(jsonStr);
					}
					catch (Exception e)
					{
						SystemLogger.getInstance().warn(e);
					}
					break;
				default:
					return null;
				}
			}
			body.setObjItemValueArr(values);

			body.setStrObjName(objectName);
			body.setStrItemName(itemName);
			body.setStrObjDispName(objDispName);
			bodyList.add(body);
		}

		// �{�̃��X�g�����
		Body[] objBodyArr;
		if (isResponseBody)
		{
			objBodyArr = bodyList.toArray(new ResponseBody[bodyList.size()]);
		}
		else
		{
			objBodyArr = bodyList.toArray(new Body[bodyList.size()]);
		}

		// �񕜂����w�b�_�Ɩ{�̂�d���ɐݒ肷��
		objTelegram.setObjHeader(objHeader);
		objTelegram.setObjBody(objBodyArr);

		return objTelegram;
	}

	/**
	 * �w�肳�ꂽ��ނ̓d�����쐬���܂��B<br />
	 * 
	 * @param telegramKind
	 *            �d�����
	 * @param requestKind
	 *            �v���������
	 * @param objectName
	 *            �I�u�W�F�N�g��
	 * @param itemName
	 *            ���ږ�
	 * @param itemType
	 *            ���ڌ^
	 * @param value
	 *            �l
	 * @return �d��
	 */
	public static Telegram createSingleTelegram(final byte telegramKind,
			final byte requestKind, final String objectName,
			final String itemName, final ItemType itemType, final Object value)
	{
		Header header = new Header();
		header.setByteTelegramKind(telegramKind);
		header.setByteRequestKind(requestKind);

		ResponseBody responseBody = createSingleResponseBody(objectName,
				itemName, itemType, value);

		Telegram telegram = new Telegram();
		telegram.setObjHeader(header);
		telegram.setObjBody(new Body[]
		{ responseBody });
		return telegram;
	}

	/**
	 * �_���v�擾�v���̓d���{�̂��쐬���܂��B
	 * 
	 * @param objName
	 *            �I�u�W�F�N�g��
	 * @param itemName
	 *            ���ږ�
	 * @return �_���v�擾�v���̓d���{��
	 */
	public static Body[] createEmptyRequestBody(final String objName,
			final String itemName)
	{
		Body[] bodies = new Body[1];
		Body objBody = new Body();
		objBody.setStrObjName(objName);
		objBody.setStrItemName(itemName);
		objBody.setIntLoopCount(0);
		objBody.setByteItemMode(ItemType.ITEMTYPE_BYTE);
		objBody.setObjItemValueArr(new Object[0]);
		bodies[0] = objBody;
		return bodies;
	}

	/**
	 * �w�肳�ꂽ��ނ̉������쐬���܂��B<br />
	 * 
	 * @param objectName
	 *            �I�u�W�F�N�g��
	 * @param itemName
	 *            ���ږ�
	 * @param itemType
	 *            ���ڌ^
	 * @param value
	 *            �l
	 * @return ����
	 */
	public static ResponseBody createSingleResponseBody(
			final String objectName, final String itemName,
			final ItemType itemType, final Object value)
	{
		ResponseBody responseBody = new ResponseBody();
		responseBody.setStrObjName(objectName);
		responseBody.setStrItemName(itemName);
		responseBody.setByteItemMode(itemType);
		Object[] itemValues;
		if (value instanceof List<?>)
		{
			List<?> valueList = (List<?>) value;
			itemValues = new Object[valueList.size()];
			for (int index = 0; index < valueList.size(); index++)
			{
				itemValues[index] = valueList.get(index);
			}
		}
		else
		{
			itemValues = new Object[]
			{ value };
		}
		responseBody.setIntLoopCount(itemValues.length);
		responseBody.setObjItemValueArr(itemValues);
		return responseBody;
	}

	/**
	 * �Q�̃o�C�g�z����������܂��B<br />
	 * 
	 * @param bytesFirst
	 *            �ŏ��̃o�C�g�z��
	 * @param bytesSecond
	 *            ���ɂȂ���o�C�g�z��
	 * @return �Q�̃o�C�g�z����Ȃ����o�C�g�͗��
	 */
	private static byte[] combineTwoByteArray(final byte[] bytesFirst,
			final byte[] bytesSecond)
	{
		// �ԋp�p
		byte[] bytesResult = null;

		int byteBeforeArrLength = 0;
		int byteAfterArrLength = 0;

		// �O�� byte[] �̃T�C�Y���擾
		if (bytesFirst != null)
		{
			byteBeforeArrLength = bytesFirst.length;
		}

		// �㕪 byte[] �̃T�C�Y���擾
		if (bytesSecond != null)
		{
			byteAfterArrLength = bytesSecond.length;
		}

		// �ԋp�p byte[] �����
		if (byteBeforeArrLength + byteAfterArrLength > 0)
		{
			bytesResult = new byte[byteBeforeArrLength + byteAfterArrLength];
		}

		// �O�� byte[] ��ԋp�p byte[] �ɐݒ肷��
		if (byteBeforeArrLength > 0)
		{
			System
					.arraycopy(bytesFirst, 0, bytesResult, 0,
							byteBeforeArrLength);
		}

		// �㕪 byte[] ��ԋp�p byte[] �ɐݒ肷��
		if (byteAfterArrLength > 0)
		{
			System.arraycopy(bytesSecond, 0, bytesResult, byteBeforeArrLength,
					byteAfterArrLength);
		}

		// �ԋp����
		return bytesResult;
	}

	/**
	 * ReponseBody���쐬���܂��B<br />
	 * 
	 * @param objName
	 *            �I�u�W�F�N�g��
	 * @param itemName
	 *            ���ږ�
	 * @param itemMode
	 *            ���ڌ^
	 * @param objItemValueArr
	 *            ���ړ��̔z��
	 * @return ReponseBody
	 */
	public static ResponseBody createResponseBody(final String objName,
			final String itemName, final ItemType itemMode,
			final Object[] objItemValueArr)
	{
		ResponseBody body = new ResponseBody();
		body.setStrObjName(objName);
		body.setStrItemName(itemName);
		body.setByteItemMode(itemMode);
		body.setIntLoopCount(objItemValueArr.length);
		body.setObjItemValueArr(objItemValueArr);

		return body;
	}

	/**
	 * �d�����e���V�X�e�����O�ɏo�͂��܂��B<br />
	 * 
	 * @param telegram
	 *            �o�͑Ώۓd��
	 * @param length
	 *            �o�͑Ώۓd����
	 * @return ���O�o�͕�����
	 */
	public static String toPrintStr(final Telegram telegram, final int length)
	{
		StringBuffer receivedBuffer = new StringBuffer();

		Header header = telegram.getObjHeader();
		byte telegramKind = header.getByteTelegramKind();
		byte requestKind = header.getByteRequestKind();

		receivedBuffer.append(NEW_LINE);
		receivedBuffer
				.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		receivedBuffer.append(NEW_LINE);

		receivedBuffer.append("�d�����      :[" + telegramKind + "]");
		receivedBuffer.append(NEW_LINE);
		receivedBuffer.append("�v���������  :[" + requestKind + "]");
		receivedBuffer.append(NEW_LINE);
		receivedBuffer.append("�d����        :[" + length + "]");
		receivedBuffer.append(NEW_LINE);

		Body[] objBody = telegram.getObjBody();

		receivedBuffer.append("�I�u�W�F�N�g��\t���ږ�\t���ڌ^\t�J��Ԃ���\t���ڒl");
		receivedBuffer.append(NEW_LINE);
		for (Body body : objBody)
		{
			String objName = body.getStrObjName();
			String itemName = body.getStrItemName();
			String itemMode = "";
			String loopCount = "";
			StringBuffer itemValue = new StringBuffer();

			if (body instanceof ResponseBody)
			{
				ResponseBody responseBody = (ResponseBody) body;
				itemMode = "[" + responseBody.getByteItemMode() + "]";
				loopCount = "[" + responseBody.getIntLoopCount() + "]";

				Object[] objArr = responseBody.getObjItemValueArr();
				for (Object obj : objArr)
				{
					itemValue.append("[" + obj + "]");
				}
			}
			receivedBuffer.append(objName);
			receivedBuffer.append("\t");
			receivedBuffer.append(itemName);
			receivedBuffer.append("\t");
			receivedBuffer.append(itemMode);
			receivedBuffer.append("\t");
			receivedBuffer.append(loopCount);
			receivedBuffer.append("\t");
			receivedBuffer.append(itemValue);
			receivedBuffer.append(NEW_LINE);
		}

		receivedBuffer
				.append("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		receivedBuffer.append(NEW_LINE);

		String receivedStr = receivedBuffer.toString();

		return receivedStr;
	}
}
