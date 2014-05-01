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
package jp.co.acroquest.endosnipe.communicator.accessor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.ConnectNotifyData;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.ResponseBody;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;

/**
 * Javelin ���O�ʒm�d���̂��߂̃A�N�Z�T�N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class ConnectNotifyAccessor
{
	private ConnectNotifyAccessor()
	{

	}

	/**
	 * �ڑ����ʒm�d��������e�����o���܂��B<br />
	 * �d����ʂ��ڑ����ʒm�ʒm�d���ł͂Ȃ��ꍇ��A���e���s���ł���ꍇ�� <code>null</code> ��Ԃ��܂��B<br />
	 * 
	 * @param telegram
	 *            Javelin �ڑ����ʒm�d��
	 * @return �d�����e
	 */
	public static ConnectNotifyData getConnectNotifyData(final Telegram telegram)
	{
		if (!isConnectNotifyTelegram(telegram))
		{
			return null;
		}

		ConnectNotifyData data = new ConnectNotifyData();

		Body[] bodies = telegram.getObjBody();
		for (Body body : bodies)
		{
			String objectName = body.getStrObjName();
			if (objectName == null)
			{
				continue;
			}

			if (objectName.equals(TelegramConstants.OBJECTNAME_CONNECTINFO))
			{
				getFromConnectInfoObject(body, data);
			}
		}

		if (data.getKind() < 0)
		{
			return null;
		}
		if (data.getAgentName() == null || data.getAgentName().length() == 0)
		{
			return null;
		}

		return data;
	}

	/**
	 * �ڑ����ʒm�d��������e�����o���܂��B<br />
	 * �d����ʂ��ڑ����ʒm�ʒm�d���ł͂Ȃ��ꍇ��A���e���s���ł���ꍇ�� <code>null</code> ��Ԃ��܂��B<br />
	 * 
	 * @param telegram
	 *            Javelin �ڑ����ʒm�d��
	 * @return �d�����e
	 */
	public static Set<String> getDataBaseNameList(final Telegram telegram)
	{
		if (!isDatabaseNameTelegram(telegram))
		{
			return null;
		}

		Set<String> databaseNameList = new HashSet<String>();

		Body[] bodies = telegram.getObjBody();
		for (Body body : bodies)
		{
			String objectName = body.getStrObjName();
			if (objectName == null)
			{
				continue;
			}

			if (objectName.equals(TelegramConstants.OBJECTnAME_DATABASE_NAME))
			{
				String databaseName = getFromDatabaseNameObject(body);
				if (databaseName != null)
				{
					databaseNameList.add(databaseName);
				}
			}
		}

		return databaseNameList;
	}

	/**
	 * �ڑ����ʒm�d�����쐬���܂��B
	 * 
	 * @param data
	 *            �d���ɐݒ肷��l
	 * @return �ڑ����ʒm�d����Ԃ��܂��B
	 */
	public static final Telegram createTelegram(ConnectNotifyData data)
	{
		Telegram telegram = new Telegram();
		telegram.setObjHeader(createHeader());
		telegram.setObjBody(createBodys(data));

		return telegram;
	}

	/**
	 * �ڑ�DB�������ʒm�d�����쐬���܂��B
	 * 
	 * @param databaseNameList
	 *            �d���ɐݒ肷��l
	 * @return �ڑ�DB�ʒm�d����Ԃ��܂��B
	 */
	public static final Telegram createAddDatabaseNameTelegram(
			Set<String> databaseNameList)
	{
		Telegram telegram = new Telegram();
		telegram.setObjHeader(createAddDatabaseNameHeader());
		telegram.setObjBody(createDatabaseNameBodys(databaseNameList));

		return telegram;
	}

	/**
	 * �ڑ�DB�������ʒm�d�����쐬���܂��B
	 * 
	 * @param databaseNameList
	 *            �d���ɐݒ肷��l
	 * @return �ڑ�DB�ʒm�d����Ԃ��܂��B
	 */
	public static final Telegram createDelDatabaseNameTelegram(
			Set<String> databaseNameList)
	{
		Telegram telegram = new Telegram();
		telegram.setObjHeader(createDelDatabaseNameHeader());
		telegram.setObjBody(createDatabaseNameBodys(databaseNameList));

		return telegram;
	}

	/**
	 * �I�u�W�F�N�g����{@link TelegramConstants#OBJECTNAME_CONNECTINFO}�ł��� {@link Body}
	 * ����A{@link ConnectNotifyData}�̒l���擾����B
	 * 
	 * @param body
	 *            �擾��
	 * @param toData
	 *            �擾�����l�̐ݒ��
	 */
	private static void getFromConnectInfoObject(Body body,
			ConnectNotifyData toData)
	{
		String itemName = body.getStrItemName();
		if (itemName == null)
		{
			return;
		}

		Object[] objItemValueArr = body.getObjItemValueArr();
		if (objItemValueArr.length == 0)
		{
			return;
		}

		if (itemName.equals(TelegramConstants.ITEMNAME_CONNECTNOTIFY_KIND))
		{
			toData.setKind((Integer) objItemValueArr[0]);
		}
		else if (itemName
				.equals(TelegramConstants.ITEMNAME_CONNECTNOTIFY_DBNAME))
		{
			toData.setAgentName((String) objItemValueArr[0]);
		}
		else if (itemName
				.equals(TelegramConstants.ITEMNAME_CONNECTNOTIFY_PURPOSE))
		{
			toData.setPurpose((Integer) objItemValueArr[0]);
		}
	}

	/**
	 * �I�u�W�F�N�g����{@link TelegramConstants#OBJECTnAME_DATABASE_NAME}�ł���
	 * {@link Body}����ADB���̈ꗗ���擾����B
	 * 
	 * @param body
	 *            �擾��
	 * @return DB���̈ꗗ
	 */
	private static String getFromDatabaseNameObject(Body body)
	{
		String itemName = body.getStrItemName();
		String databaseName = null;
		if (itemName == null)
		{
			return databaseName;
		}

		Object[] objItemValueArr = body.getObjItemValueArr();
		if (objItemValueArr.length == 0)
		{
			return databaseName;
		}

		if (itemName.equals(TelegramConstants.ITEMNAME_CONNECTNOTIFY_DBNAME))
		{
			return (String) objItemValueArr[0];
		}
		// else if
		// (itemName.equals(TelegramConstants.ITEMNAME_CONNECTNOTIFY_DBNAME))
		// {
		// toData.setDbName((String)objItemValueArr[0]);
		// }
		// else if
		// (itemName.equals(TelegramConstants.ITEMNAME_CONNECTNOTIFY_PURPOSE))
		// {
		// toData.setPurpose((Integer)objItemValueArr[0]);
		// }

		return databaseName;
	}

	/**
	 * �ڑ����ʒm�d���̃w�b�_���쐬���܂��B
	 * 
	 * @return �ڑ����ʒm�d���̃w�b�_��Ԃ��܂��B
	 */
	private static Header createHeader()
	{
		Header header = new Header();
		header
			.setByteTelegramKind(
				TelegramConstants.BYTE_TELEGRAM_KIND_CONNECT_NOTIFY);
		header.setByteRequestKind(TelegramConstants.BYTE_REQUEST_KIND_NOTIFY);
		return header;
	}

	/**
	 * �ڑ����ʒm�d���̃{�f�B���쐬���܂��B
	 * 
	 * @param data
	 *            �{�f�B�ɐݒ肷��l
	 * @return �ڑ����ʒm�d���̃{�f�B��Ԃ��܂��B
	 */
	private static Body[] createBodys(ConnectNotifyData data)
	{
		List<Body> bodyList = new ArrayList<Body>();

		Body bodyKind = new ResponseBody();
		bodyKind.setStrObjName(TelegramConstants.OBJECTNAME_CONNECTINFO);
		bodyKind.setStrItemName(TelegramConstants.ITEMNAME_CONNECTNOTIFY_KIND);
		bodyKind.setIntLoopCount(1);
		bodyKind.setByteItemMode(ItemType.ITEMTYPE_INT);
		bodyKind.setObjItemValueArr(new Object[]
		{ data.getKind() });
		bodyList.add(bodyKind);

		Body bodyDbName = new ResponseBody();
		bodyDbName.setStrObjName(TelegramConstants.OBJECTNAME_CONNECTINFO);
		bodyDbName
				.setStrItemName(TelegramConstants.ITEMNAME_CONNECTNOTIFY_DBNAME);
		bodyDbName.setIntLoopCount(1);
		bodyDbName.setByteItemMode(ItemType.ITEMTYPE_STRING);
		bodyDbName.setObjItemValueArr(new Object[]
		{ data.getAgentName() });
		bodyList.add(bodyDbName);

		Body bodypurpose = new ResponseBody();
		bodypurpose.setStrObjName(TelegramConstants.OBJECTNAME_CONNECTINFO);
		bodypurpose
				.setStrItemName(TelegramConstants.ITEMNAME_CONNECTNOTIFY_PURPOSE);
		bodypurpose.setIntLoopCount(1);
		bodypurpose.setByteItemMode(ItemType.ITEMTYPE_INT);
		bodypurpose.setObjItemValueArr(new Object[]
		{ data.getPurpose() });
		bodyList.add(bodypurpose);

		return bodyList.toArray(new Body[bodyList.size()]);
	}

	/**
	 * �ڑ�DB����񑝉��ʒm�d���̃w�b�_���쐬���܂��B
	 * 
	 * @return �ڑ�DB�����ʒm�d���̃w�b�_��Ԃ��܂��B
	 */
	private static Header createAddDatabaseNameHeader()
	{
		Header header = new Header();
		header
				.setByteTelegramKind(
					TelegramConstants.BYTE_TELEGRAM_KIND_ADD_DATABASE_NAME);
		header.setByteRequestKind(TelegramConstants.BYTE_REQUEST_KIND_NOTIFY);
		return header;
	}

	/**
	 * �ڑ�DB�����ʒm�����d���̃w�b�_���쐬���܂��B
	 * 
	 * @return �ڑ�DB�����ʒm�d���̃w�b�_��Ԃ��܂��B
	 */
	private static Header createDelDatabaseNameHeader()
	{
		Header header = new Header();
		header
				.setByteTelegramKind(
					TelegramConstants.BYTE_TELEGRAM_KIND_DEL_DATABASE_NAME);
		header.setByteRequestKind(TelegramConstants.BYTE_REQUEST_KIND_NOTIFY);
		return header;
	}

	/**
	 * �ڑ�DB�����ʒm�d���̃{�f�B���쐬���܂��B
	 * 
	 * @param data
	 *            �{�f�B�ɐݒ肷��l
	 * @return �ڑ�DB�����ʒm�d���̃{�f�B��Ԃ��܂��B
	 */
	private static Body[] createDatabaseNameBodys(Set<String> databaseNameList)
	{
		List<Body> bodyList = new ArrayList<Body>();

		for (String databaseName : databaseNameList)
		{
			Body bodyDBName = new ResponseBody();
			Object[] valueArr = new Object[]
			{ databaseName };
			bodyDBName
					.setStrObjName(TelegramConstants.OBJECTnAME_DATABASE_NAME);
			bodyDBName
					.setStrItemName(
						TelegramConstants.ITEMNAME_CONNECTNOTIFY_DBNAME);
			bodyDBName.setIntLoopCount(valueArr.length);
			bodyDBName.setByteItemMode(ItemType.ITEMTYPE_STRING);
			bodyDBName.setObjItemValueArr(valueArr);
			bodyList.add(bodyDBName);
		}

		return bodyList.toArray(new Body[bodyList.size()]);
	}

	/**
	 * �d�����ڑ����ʒm�ł��邩���m�F���܂��B
	 * 
	 * @param telegram
	 *            �m�F�Ώۂ̓d��
	 * @return �w�肳�ꂽ�d�����ڑ��ʒm���ł����<code>true</code>��Ԃ��܂��B
	 */
	private static boolean isConnectNotifyTelegram(final Telegram telegram)
	{
		Header header = telegram.getObjHeader();
		if (header.getByteTelegramKind()
				== TelegramConstants.BYTE_TELEGRAM_KIND_CONNECT_NOTIFY)
		{
			return true;
		}
		return false;
	}

	/**
	 * �d����DB���̑������ʒm�ł��邩���m�F���܂��B
	 * 
	 * @param telegram
	 *            �m�F�Ώۂ̓d��
	 * @return �w�肳�ꂽ�d����DB���̑����ʒm���ł����<code>true</code>��Ԃ��܂��B
	 */
	private static boolean isDatabaseNameTelegram(final Telegram telegram)
	{
		Header header = telegram.getObjHeader();
		if (header.getByteTelegramKind()
				== TelegramConstants.BYTE_TELEGRAM_KIND_ADD_DATABASE_NAME
				|| header.getByteTelegramKind()
				== TelegramConstants.BYTE_TELEGRAM_KIND_DEL_DATABASE_NAME)
		{
			return true;
		}
		return false;
	}

	/**
	 * �f�[�^�x�[�X���ƃV�[�P���X�ԍ�����G�[�W�F���g���𐶐�����B
	 * 
	 * @param databaseName
	 *            �f�[�^�x�[�X��
	 * @param sequenceNo
	 *            �V�[�P���X�ԍ�
	 * 
	 * @return �G�[�W�F���g���i�f�[�^�x�[�X�� + "_" + �V�[�P���X�ԍ�)
	 */
	public static String createAgentName(String databaseName, int sequenceNo)
	{
		String agentName = databaseName;
		if (databaseName.endsWith("/"))
		{
			agentName = agentName.substring(0, databaseName.length() - 1);
		}
		agentName = agentName + "_" + createNumberString(sequenceNo);
		return agentName;
	}

	/**
	 * �G�[�W�F���g���𐶐����邽�߂ɁA�V�[�P���X�ԍ����R���̂O���߂ɕϊ�����B
	 * 
	 * @param number
	 *            �V�[�P���X�ԍ�
	 * @return �ϊ������V�[�P���X�ԍ�
	 */
	private static String createNumberString(int number)
	{
		String numStr = String.format("%1$03d", number);
		return numStr;
	}
}
