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
package jp.co.acroquest.endosnipe.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.util.SQLUtil;
import jp.co.acroquest.endosnipe.data.db.DBManager;
import jp.co.acroquest.endosnipe.data.db.H2DBUtil;
import jp.co.acroquest.endosnipe.data.db.SQLExecutor;
import jp.co.acroquest.endosnipe.util.ResourceDataDaoUtil;

/**
 * ENdoSnipe �p�f�[�^�x�[�X�����������邽�߂̃N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class DBInitializer
{
	private static final String DDL_PATH = "/ddl/ENdoSnipe.ddl";

	private static final String POSTGRES_DDL_PATH = "/ddl/ENdoSnipe_PostgreSQL.ddl";

	private static final String DDL_SEQUENCE_PATH = "/ddl/ENdoSnipeSequence.ddl";

	private static final String H2_FUNC_PATH = "/func/h2_func.sql";

	private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(
			DBInitializer.class);

	/** PostgreSQL�f�[�^�x�[�X�̃h���C�o�N���X���� */
	private static final String POSTGRES_DRIVER = "org.postgresql.Driver";

	/** PostgreSQL�ڑ��pURI�̃v���t�B�N�X */
	private static final String POSTGRES_URI_PREFIX = "jdbc:postgresql://";

	private DBInitializer()
	{
		// Do nothing.
	}

	/**
	 * �ڑ����ꂽ�f�[�^�x�[�X���������ς݂��ǂ����𒲂ׂ܂��B<br />
	 * 
	 * @param con
	 *            �R�l�N�V����
	 * @return �������ς݂̏ꍇ�� <code>true</code>�A�����łȂ��ꍇ�� <code>false</code>�B
	 */
	public static boolean isInitialized(final Connection con)
	{
		if (con == null)
		{
			return false;
		}

		boolean initialized = false;
		String sql = null;

		if (DBManager.isDefaultDb())
		{
			sql = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='TABLE'";
		}
		else
		{
			sql = "SELECT last_value FROM SEQ_LOG_ID";
		}
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next() == true)
			{
				initialized = true;
			}
		}
		catch (SQLException ex)
		{
			// ����������Ă��Ȃ��ꍇ�ɗ�O���������邽�߁A
			// INFO�ȏ�ł̏o�͂Ƃ���B
			if (LOGGER.isInfoEnabled())
			{
				LOGGER
						.log(LogMessageCodes.DB_ACCESS_ERROR, ex, ex
								.getMessage());
			}
		}
		finally
		{
			SQLUtil.closeResultSet(rs);
			SQLUtil.closeStatement(stmt);
		}
		return initialized;
	}

	/**
	 * �ڑ����ꂽ�f�[�^�x�[�X�̏��������s���܂��B<br />
	 * �f�[�^�x�[�X���������ς݂̏ꍇ�͉����s���܂���B<br />
	 * 
	 * @param con
	 *            �R�l�N�V����
	 * @throws SQLException
	 *             SQL���s�Ɏ��s�����ꍇ
	 * @throws IOException
	 *             ���o�̓G���[�����������ꍇ
	 */
	public static void initialize(final Connection con) throws SQLException,
			IOException
	{
		if (isInitialized(con) == true)
		{
			return;
		}

		H2DBUtil.executeDDL(con, DDL_SEQUENCE_PATH);
		if (DBManager.isDefaultDb())
		{
			H2DBUtil.executeDDL(con, DDL_PATH);
		}
		else
		{
			H2DBUtil.executeDDL(con, POSTGRES_DDL_PATH);

			// ���݂̃f�[�^��}������e�[�u���C���f�b�N�X
			int startIndex = ResourceDataDaoUtil.getTableIndexToInsert(null);

			// ���N�̐���
			Calendar currentYearCalendar = Calendar.getInstance();
			int currentYear = currentYearCalendar.get(Calendar.YEAR);

			// �p�[�e�B�V���j���O�p�e�[�u�����쐬����iMEASUREMENT_VALUE�AJAVELIN_LOG�e�[�u���j
			for (int index = 1;
				index <= ResourceDataDaoUtil.PARTITION_TABLE_COUNT; index++)
			{
				int year;
				if (index < startIndex)
				{
					// ���݂̃f�[�^��}������e�[�u�������O�̃e�[�u�����쐬����ꍇ�A
					// CHECK����ɓ���鎞���͈̔͂͗��N�̒l������
					year = currentYear + 1;
				}
				else
				{
					// ���݂̃f�[�^��}������e�[�u���ȍ~�̃e�[�u�����쐬����ꍇ�A
					// CHECK����ɓ���鎞���͈͍̔͂��N�̒l������
					year = currentYear;
				}

				createMeasurementValueTable(con, index, year);
				createJavelinLogTable(con, index, year);
			}
		}
		// MEASUREMENT_INFO�e�[�u�����폜�����̂ŁA�����f�[�^���������Ȃ�
		// initMeasurementInfoFromTsv(con, TSV_MEASUREMENT_INFO_PATH);

	}

	/**
	 * �ڑ��̓x�ɕK�v�ȏ��������s���܂��B
	 * 
	 * @param con
	 *            �R�l�N�V����
	 * @throws SQLException
	 *             SQL���s�Ɏ��s�����ꍇ
	 * @throws IOException
	 *             ���o�̓G���[�����������ꍇ
	 */
	public static void reinitialize(Connection con) throws SQLException,
			IOException
	{
		initialize(con);
		if (DBManager.isDefaultDb())
		{
			H2DBUtil.executeDDL(con, H2_FUNC_PATH);
		}
		// MEASUREMENT_INFO�e�[�u�����폜�����̂ŁA�����f�[�^���������Ȃ�
		// initMeasurementInfoFromTsv(con, TSV_MEASUREMENT_INFO_PATH);
	}

	// MEASUREMENT_INFO�e�[�u�����폜�����̂ŁA�����f�[�^���������Ȃ�
	// /**
	// * �N���X�p�X��ɑ��݂��� TSV �t�@�C����ǂݍ��݁A MEASUREMENT_INFO �e�[�u���Ƀf�[�^��}�����܂��B<br />
	// *
	// * @param con �R�l�N�V����
	// * @param path �ǂݍ��� TSV �t�@�C���̃p�X
	// * @throws IOException ���o�̓G���[�����������ꍇ
	// * @throws SQLException SQL�G���[�����������ꍇ
	// */
	// public static void initMeasurementInfoFromTsv(final Connection con, final
	// String path)
	// throws IOException,
	// SQLException
	// {
	// final int COLUMN_COUNT = 4;
	//
	// if (path == null)
	// {
	// throw new IllegalArgumentException("schemaPath can't be null");
	// }
	//
	// try
	// {
	// MeasurementInfoDao.deleteAllWithoutJMX(con);
	// }
	// catch (SQLException sqle)
	// {
	// // ����������Ă��Ȃ��ꍇ�ɗ�O���������邽�߁A
	// // INFO�ȏ�ł̏o�͂Ƃ���B
	// if (LOGGER.isInfoEnabled())
	// {
	// LOGGER.log(LogMessageCodes.DB_ACCESS_ERROR, sqle, sqle.getMessage());
	// }
	// }
	//        
	// BufferedReader reader = null;
	// try
	// {
	// InputStream is = H2DBUtil.class.getResourceAsStream(path);
	// reader = new BufferedReader(new InputStreamReader(is));
	//
	//
	// String line;
	// while ((line = reader.readLine()) != null)
	// {
	// String[] items = line.split("\t", COLUMN_COUNT);
	// if (items.length == COLUMN_COUNT)
	// {
	// // CHECKSTYLE:OFF
	// long measurementType = Long.parseLong(items[0]);
	// String itemName = items[1];
	// String displayName = items[2];
	// String description = items[3];
	// // CHECKSTYLE:ON
	// MeasurementInfo measurementInfo =
	// new MeasurementInfo(measurementType, itemName, displayName, description);
	// MeasurementInfoDao.insert(con, measurementInfo);
	// }
	// }
	// }
	// finally
	// {
	// reader.close();
	// }
	// }

	/**
	 * CHECK ���񖼂𐶐����܂��B
	 * 
	 * @param tableName
	 *            �C���f�b�N�X���܂߂��e�[�u����
	 * @param column
	 *            �J������
	 * @return CHECK ����
	 */
	public static String createCheckConstraintName(final String tableName,
			final String column)
	{
		return tableName + "_" + column + "_check";
	}

	/**
	 * �p�[�e�B�V���j���O���ꂽ�X�̃e�[�u���ɑ΂��Đݒ肷�� CHECK ����̕��𐶐����܂��B
	 * 
	 * @param column
	 *            ������������J������
	 * @param tableIndex
	 *            �e�[�u���C���f�b�N�X
	 * @param year
	 *            �N
	 * @return CHECK ����̕�
	 */
	public static String createCheckConstraintText(final String column,
			final int tableIndex, final int year)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, 0, 1, 0, 0, 0);
		calendar.add(Calendar.DATE, (tableIndex - 1)
				* ResourceDataDaoUtil.DAY_OF_WEEK);
		Date startDate = calendar.getTime();
		if (tableIndex == ResourceDataDaoUtil.PARTITION_TABLE_COUNT)
		{
			// �Ō�̃e�[�u���ł́A�e�[�u���Ɋi�[����I�����t�͂��̔N��12/31�i�Ȃ̂ŏI�����͗��N��1/1�j
			calendar.set(year + 1, 0, 1, 0, 0, 0);
		}
		else
		{
			calendar.add(Calendar.DATE, ResourceDataDaoUtil.DAY_OF_WEEK);
		}
		Date endDate = calendar.getTime();
		String checkConstraint = String.format(
				"CHECK ('%2$tY/%2$tm/%2$td' <= %1$s"
						+ " AND %1$s < '%3$tY/%3$tm/%3$td')", column,
				startDate, endDate);
		return checkConstraint;
	}

	/**
	 * MEASUREMENT_VALUE_xx �e�[�u�����쐬���܂��B
	 * 
	 * @param con
	 *            �f�[�^�x�[�X�R�l�N�V����
	 * @param index
	 *            �쐬����e�[�u���̃C���f�b�N�X
	 * @param year
	 *            ����
	 * @throws SQLException
	 *             �e�[�u���쐬���ɗ�O�����������ꍇ
	 */
	private static void createMeasurementValueTable(final Connection con,
			int index, int year) throws SQLException
	{
		// MEASUREMENT_VALUE_xx �e�[�u�����쐬����
		String tableName = String.format("%s_%02d",
				TableNames.MEASUREMENT_VALUE, index);
		String checkConstraintName = createCheckConstraintName(tableName,
				"MEASUREMENT_TIME");
		String checkConstraint = createCheckConstraintText("MEASUREMENT_TIME",
				index, year);
		String createMeasurementValueSql = String.format(
				"CREATE TABLE %s (CONSTRAINT %s %s) INHERITS (%s)",
				tableName, checkConstraintName, checkConstraint,
				TableNames.MEASUREMENT_VALUE);
		SQLExecutor.executeSQL(con, createMeasurementValueSql, null);

		// MEASUREMENT_VALUE_xx �e�[�u���ɃC���f�b�N�X������
		String createIndexSql = String
				.format(
						"CREATE INDEX IDX_%1$s_MEASUREMENT_TIME" +
						" ON %1$s (MEASUREMENT_TIME)",
						tableName);
		SQLExecutor.executeSQL(con, createIndexSql, null);
	}

	/**
	 * JAVELIN_LOG_xx �e�[�u�����쐬���܂��B
	 * 
	 * @param con
	 *            �f�[�^�x�[�X�R�l�N�V����
	 * @param index
	 *            �쐬����e�[�u���̃C���f�b�N�X
	 * @param year
	 *            ����
	 * @throws SQLException
	 *             �e�[�u���쐬���ɗ�O�����������ꍇ
	 */
	private static void createJavelinLogTable(final Connection con, int index,
			int year) throws SQLException
	{
		// JAVELIN_LOG_xx �e�[�u�����쐬����
		String tableName = String.format("%s_%02d", TableNames.JAVELIN_LOG,
				index);
		String checkConstraintName = createCheckConstraintName(tableName,
				"END_TIME");
		String checkConstraint = createCheckConstraintText("END_TIME", index,
				year);
		String createJavelinLogSql = String.format(
				"CREATE TABLE %s (CONSTRAINT %s %s) INHERITS (%s)", tableName,
				checkConstraintName, checkConstraint, TableNames.JAVELIN_LOG);
		SQLExecutor.executeSQL(con, createJavelinLogSql, null);

		// JAVELIN_LOG_xx �e�[�u���ɃC���f�b�N�X������
		String createStartTimeIndexSql = String.format(
				"CREATE INDEX IDX_%1$s_START_TIME ON %1$s (START_TIME)",
				tableName);
		String createEndTimeIndexSql = String.format(
				"CREATE INDEX IDX_%1$s_END_TIME ON %1$s (END_TIME)", tableName);
		SQLExecutor.executeSQL(con, createStartTimeIndexSql, null);
		SQLExecutor.executeSQL(con, createEndTimeIndexSql, null);
	}

	/**
	 * �w�肳�ꂽ���O�̃f�[�^�x�[�X���쐬����B
	 * 
	 * @param dbName
	 *            �f�[�^�x�[�X��
	 * @return �f�[�^�x�[�X�̍쐬�ɐ��������� <code>true</code>�A�����łȂ��ꍇ��<code>false</code>
	 */
	public static boolean createDatabase(String dbName)
	{
		if (DBManager.isDefaultDb() == true)
		{
			// H2�̏ꍇ�͉������Ȃ�
			return false;
		}
		return createPostgresDatabase(dbName);
	}

	/**
	 * �w�肳�ꂽ���O�̃f�[�^�x�[�X��PostgreSQL�ō쐬����B
	 * 
	 * @param dbName
	 *            �f�[�^�x�[�X��
	 * @return �f�[�^�x�[�X�̍쐬�ɐ��������� <code>true</code>�A�����łȂ��ꍇ��<code>false</code>
	 */
	private static boolean createPostgresDatabase(String dbName)
	{
		try
		{
			Class.forName(POSTGRES_DRIVER);
		}
		catch (ClassNotFoundException ex)
		{
			return false;
		}

		String uri = POSTGRES_URI_PREFIX + DBManager.getHostName() + ":"
				+ DBManager.getPort() + "/";
		Connection conn = null;
		Statement state = null;

		// �f�[�^�x�[�X���쐬���A���̌��ʂ�Ԃ�
		try
		{
			conn = DriverManager.getConnection(uri, DBManager.getUserName(),
					DBManager.getPassword());

			state = conn.createStatement();

			// �f�[�^�x�[�X����""�Ŋ���
			dbName = "\"" + dbName + "\"";

			return state.execute("CREATE DATABASE " + dbName + ";");
		}
		catch (SQLException sqlex)
		{
			return false;
		}
		finally
		{
			SQLUtil.closeStatement(state);
			SQLUtil.closeConnection(conn);
		}
	}

}
