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
package jp.co.acroquest.endosnipe.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.util.SQLUtil;
import jp.co.acroquest.endosnipe.data.TableNames;
import jp.co.acroquest.endosnipe.data.entity.MeasurementInfo;

/**
 * {@link MeasurementInfo} �̂��߂� DAO �ł��B
 * 
 * @author y-sakamoto
 */
public class MeasurementInfoDao extends AbstractDao implements TableNames
{
	private static final int MEASUREMENT_TYPE_INDEX = 1;

	private static final int ITEM_NAME_INDEX = 2;

	private static final int DISPLAY_NAME_INDEX = 3;

	private static final int DESCRIPTION_INDEX = 4;

	private static final Map<String, List<MeasurementInfo>>
		MEASUREMENT_INFO_CACHE = new HashMap<String, List<MeasurementInfo>>();

	// /** DB�ύX��ʒm���邽�߂̃R�[���o�b�N���\�b�h */
	// private static final NotifyJMXItem callBack_;
	//
	// static
	// {
	// callBack_ = new NotifyJMXItem() {
	// public long addItem(String dbName, String objectName, String dispName)
	// {
	// long type = 256L;
	//
	// try
	// {
	// List<MeasurementInfo> cachedList = getMeasurementCacheList(dbName);
	//
	// MeasurementInfo measurementInfo = null;
	// for (MeasurementInfo cachedItem : cachedList)
	// {
	// if (cachedItem.getItemName().equals(objectName))
	// {
	// measurementInfo = cachedItem;
	// type = cachedItem.getMeasurementType();
	// break;
	// }
	// else if (cachedItem.getMeasurementType() >= type)
	// {
	// type = cachedItem.getMeasurementType() + 1;
	// }
	// }
	// if (measurementInfo == null)
	// {
	// MeasurementInfo mInfo = new MeasurementInfo(type, objectName, dispName,
	// "");
	// insert(dbName, mInfo);
	// }
	// }
	// catch (SQLException e)
	// {
	// SystemLogger.getInstance().warn(e);
	// return -1L;
	// }
	//
	// return type;
	// }
	// };
	//
	// JMXManager.getInstance().setCallBack(callBack_);
	// }

	/**
	 * Call�o�b�N�I�u�W�F�N�g��o�^���邽�߂́A���������\�b�h�ł��B<br />
	 */
	public static void initialize()
	{
		return;
	}

	/**
	 * {@link MeasurementInfo} �I�u�W�F�N�g��}�����܂��B<br />
	 * 
	 * @param database
	 *            �}����f�[�^�x�[�X��
	 * @param measurementInfo
	 *            �ΏۃI�u�W�F�N�g
	 * @throws SQLException
	 *             SQL ���s���ɗ�O�����������ꍇ
	 */
	public static void insert(final String database,
			final MeasurementInfo measurementInfo) throws SQLException
	{
		Connection conn = null;
		try
		{
			conn = getConnection(database);
			insert(conn, measurementInfo);
		}
		finally
		{
			SQLUtil.closeConnection(conn);
		}
		MEASUREMENT_INFO_CACHE.remove(database);
	}

	/**
	 * {@link MeasurementInfo} �I�u�W�F�N�g��}�����܂��B<br />
	 * 
	 * @param conn
	 *            �R�l�N�V����
	 * @param measurementInfo
	 *            �ΏۃI�u�W�F�N�g
	 * @throws SQLException
	 *             SQL ���s���ɗ�O�����������ꍇ
	 */
	public static void insert(final Connection conn,
			final MeasurementInfo measurementInfo) throws SQLException
	{
		PreparedStatement pstmt = null;
		try
		{
			String sql = "insert into " + MEASUREMENT_INFO + " ("
					+ "MEASUREMENT_TYPE, " + "ITEM_NAME, " + "DISPLAY_NAME, "
					+ "DESCRIPTION" + ")" + " values (?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			PreparedStatement delegated = getDelegatingStatement(pstmt);
			delegated.setLong(MEASUREMENT_TYPE_INDEX, measurementInfo
					.getMeasurementType());
			delegated.setString(ITEM_NAME_INDEX, measurementInfo.getItemName());
			delegated.setString(DISPLAY_NAME_INDEX, measurementInfo
					.getDisplayName());
			delegated.setString(DESCRIPTION_INDEX, measurementInfo
					.getDescription());

			pstmt.execute();
		}
		finally
		{
			SQLUtil.closeStatement(pstmt);
		}
	}

	/**
	 * ���ׂẴ��R�[�h���擾���܂��B
	 * 
	 * @param database
	 *            �f�[�^�x�[�X��
	 * @return {@link MeasurementInfo} �I�u�W�F�N�g�̃��X�g
	 * @throws SQLException
	 *             SQL ���s���ɗ�O�����������ꍇ
	 */
	public static List<MeasurementInfo> selectAll(final String database)
			throws SQLException
	{
		cacheFromDatabase(database);
		List<MeasurementInfo> result = MEASUREMENT_INFO_CACHE.get(database);
		return Collections.unmodifiableList(result);
	}

	/**
	 * �w�肳�ꂽ�v���l��ʂ̃��R�[�h���擾���܂��B
	 * 
	 * @param database
	 *            �f�[�^�x�[�X��
	 * @param measurementType
	 *            �v���l���
	 * @return �Y�����郌�R�[�h�����݂���ꍇ�� {@link MeasurementInfo} �I�u�W�F�N�g�A ����ȊO�̏ꍇ��
	 *         <code>null</code>
	 * @throws SQLException
	 *             SQL ���s���ɗ�O�����������ꍇ
	 */
	public static MeasurementInfo selectByMeasurementType(
			final String database, final int measurementType)
			throws SQLException
	{
		cacheFromDatabase(database);
		MeasurementInfo result = null;
		List<MeasurementInfo> cachedList = MEASUREMENT_INFO_CACHE.get(database);
		if (cachedList != null)
		{
			for (MeasurementInfo cachedItem : cachedList)
			{
				if (cachedItem.getMeasurementType() == measurementType)
				{
					result = cachedItem;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * �w�肳�ꂽ���\�[�X�ʒm�d���̍��ږ��ɑΉ����郌�R�[�h���擾���܂��B
	 * 
	 * @param database
	 *            �f�[�^�x�[�X��
	 * @param itemName
	 *            ���\�[�X�ʒm�d���̍��ږ�
	 * @return �Y�����郌�R�[�h�����݂���ꍇ�� {@link MeasurementInfo} �I�u�W�F�N�g�A ����ȊO�̏ꍇ��
	 *         <code>null</code>
	 * @throws SQLException
	 *             SQL ���s���ɗ�O�����������ꍇ
	 */
	public static MeasurementInfo selectByItemName(final String database,
			final String itemName) throws SQLException
	{
		cacheFromDatabase(database);
		MeasurementInfo result = null;
		List<MeasurementInfo> cachedList = MEASUREMENT_INFO_CACHE.get(database);
		if (cachedList != null)
		{
			for (MeasurementInfo cachedItem : cachedList)
			{
				if (itemName.equals(cachedItem.getItemName()))
				{
					result = cachedItem;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * ���ׂẴ��R�[�h���폜���܂��B<br />
	 * 
	 * @param database
	 *            �f�[�^�x�[�X��
	 * @throws SQLException
	 *             SQL ���s���ɗ�O�����������ꍇ
	 */
	public static void deleteAll(final String database) throws SQLException
	{
		deleteAll(database, MEASUREMENT_INFO);
		MEASUREMENT_INFO_CACHE.remove(database);
	}

	/**
	 * ���ׂẴ��R�[�h���폜���܂��B<br />
	 * 
	 * @param conn
	 *            �R�l�N�V����
	 * @throws SQLException
	 *             SQL ���s���ɗ�O�����������ꍇ
	 */
	public static void deleteAll(final Connection conn) throws SQLException
	{
		deleteAll(conn, MEASUREMENT_INFO);
	}

	/**
	 * JMX�n��̃f�[�^�������A�w�肽�e�[�u���̂��ׂẴ��R�[�h���폜���܂��B<br />
	 * 
	 * @param conn
	 *            �R�l�N�V����
	 * @throws SQLException
	 *             SQL ���s���ɗ�O�����������ꍇ
	 */
	public static void deleteAllWithoutJMX(final Connection conn)
			throws SQLException
	{
		Statement stmt = null;
		try
		{
			stmt = conn.createStatement();
			String sql = "delete from "
					+ MEASUREMENT_INFO
					+ " where measurement_type <= 255 or" +
					" measurement_type not in " +
					"(select distinct measurement_type from measurement_value)";
			stmt.execute(sql);
		}
		finally
		{
			SQLUtil.closeStatement(stmt);
		}
	}

	/**
	 * ���R�[�h�̐���Ԃ��܂��B<br />
	 * 
	 * @param database
	 *            �f�[�^�x�[�X��
	 * @return ���R�[�h�̐�
	 * @throws SQLException
	 *             SQL ���s���ɗ�O�����������ꍇ
	 */
	public static int count(final String database) throws SQLException
	{
		List<MeasurementInfo> measurementInfoList = MEASUREMENT_INFO_CACHE
				.get(database);
		if (measurementInfoList == null)
		{
			return 0;
		}
		return measurementInfoList.size();
	}

	/**
	 * �f�[�^�x�[�X�ɂ���f�[�^���L���b�V�����܂��B<br />
	 * 
	 * @param database
	 *            �f�[�^�x�[�X��
	 * @throws SQLException
	 *             SQL ���s���ɗ�O�����������ꍇ
	 */
	private static synchronized void cacheFromDatabase(final String database)
			throws SQLException
	{
		if (MEASUREMENT_INFO_CACHE.containsKey(database))
		{
			return;
		}

		List<MeasurementInfo> result = new ArrayList<MeasurementInfo>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			conn = getConnection(database, true);
			String sql = "select * from " + MEASUREMENT_INFO;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next() == true)
			{
				// CHECKSTYLE:OFF
				long measurementType = rs.getLong(1);
				String itemName = rs.getString(2);
				String displayName = rs.getString(3);
				String description = rs.getString(4);
				// CHECKSTYLE:ON
				MeasurementInfo info = new MeasurementInfo(measurementType,
						itemName, displayName, description);
				result.add(info);
			}
		}
		finally
		{
			SQLUtil.closeResultSet(rs);
			SQLUtil.closeStatement(stmt);
			SQLUtil.closeConnection(conn);
		}
		MEASUREMENT_INFO_CACHE.put(database, result);
	}


	/**
	 * �A�C�e��������ɕ\�������X�V���܂��B
	 * 
	 * @param database
	 *            �f�[�^�x�[�X��
	 * @param displayName
	 *            �\����
	 * @param itemName
	 *            �A�C�e����
	 * @throws SQLException SQL��O
	 */
	public static void updateByItemName(final String database,
			final String displayName, final String itemName)
			throws SQLException
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		try
		{
			conn = getConnection(database);

			String sql = "update MEASUREMENT_INFO" +
					" set DISPLAY_NAME = ? where ITEM_NAME = ?;";
			pstmt = conn.prepareStatement(sql);
			PreparedStatement delegated = getDelegatingStatement(pstmt);
			delegated.setString(1, displayName);
			delegated.setString(2, itemName);

			pstmt.execute();
		}
		finally
		{
			SQLUtil.closeStatement(pstmt);
		}
	}
}
