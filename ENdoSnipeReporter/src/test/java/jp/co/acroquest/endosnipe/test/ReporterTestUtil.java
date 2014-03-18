package jp.co.acroquest.endosnipe.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.acroquest.endosnipe.data.dto.MeasurementValueDto;
import jp.co.acroquest.endosnipe.data.entity.JavelinLog;
import jp.co.acroquest.endosnipe.data.entity.JavelinMeasurementItem;
import jp.co.acroquest.endosnipe.data.entity.MeasurementInfo;
import jp.co.acroquest.endosnipe.data.entity.MeasurementValue;
import junit.framework.Assert;

/**
 * ENdoSnipeReporterプロジェクトのテストユーティリティクラス
 * 
 * @author kimura
 */
public class ReporterTestUtil
{

	protected static final String DB_NAME = "endosnipedb";

	private static Map<String, Method> parseMethodMap;

	private static String[] JAVELIN_LOG_FIELD_LIST = { "logId", "sessionId", "sequenceId",
		"javelinLog", "logFileName", "startTime", "endTime", "sessionDesc", "logType",
		"calleeName", "calleeSignature", "calleeClass", "calleeFieldType", "calleeObjectId",
		"callerName", "callerSignature", "callerClass", "callerObjectId", "eventLevel",
		"elapsedTime", "modifier", "threadName", "threadClass", "threadObjectId" };

	private static Set<String> JAVELIN_LOG_EXCLUDE = new HashSet<String>();

	private static final String[] MEASUREMENT_VALUE_FIELD_LIST = { "measurementValueId",
		"measurementNum", "measurementTime", "measurementType", "measurementItemId", "value" };

	private static Set<String> MEASUREMENT_VALUE_EXCLUDE = new HashSet<String>();

	private static final String[] JAVELIN_MEASUREMENT_ITEM_FIELD_LIST = { "measurementItemId",
		"measurementType", "itemName" };

	private static Set<String> JAVELIN_MEASUREMENT_ITEM_EXCLUDE = new HashSet<String>();

	private static final String[] MEASUREMENT_INFO_FIELD_LIST = { "measurementType_", "itemName_",
		"displayName_", "description_" };

	private static Set<String> MEASUREMENT_INFO_EXCLUDE = new HashSet<String>();

	private static final String[] MEASUREMENT_VALUE_DTO_FIELD_LIST = { "measurementTime",
		"measurementItemId", "measurementTypeItemName", "measurementTypeDisplayName",
		"measurementItemName" };

	private static Set<String> MEASUREMENT_VALUE_DTO_EXCLUDE = new HashSet<String>();

	static
	{
		parseMethodMap = new HashMap<String, Method>();
		try
		{
			parseMethodMap.put("boolean", Boolean.class.getMethod("parseBoolean", String.class));
			parseMethodMap.put("byte", Byte.class.getMethod("parseByte", String.class));
			parseMethodMap.put("double", Double.class.getMethod("parseDouble", String.class));
			parseMethodMap.put("float", Float.class.getMethod("parseFloat", String.class));
			parseMethodMap.put("int", Integer.class.getMethod("parseInt", String.class));
			parseMethodMap.put("long", Long.class.getMethod("parseLong", String.class));
			parseMethodMap.put("short", Short.class.getMethod("parseShort", String.class));
		}
		catch (SecurityException ex)
		{
		}
		catch (NoSuchMethodException ex)
		{
		}

		JAVELIN_LOG_EXCLUDE.add("logId");
		JAVELIN_LOG_EXCLUDE.add("javelinLog");

		MEASUREMENT_VALUE_EXCLUDE.add("measurementValueId");
		JAVELIN_MEASUREMENT_ITEM_EXCLUDE.add("measurementItemId");
		MEASUREMENT_VALUE_DTO_EXCLUDE.add("measurementValueId");
		MEASUREMENT_VALUE_DTO_EXCLUDE.add("measurementNum");
		MEASUREMENT_VALUE_DTO_EXCLUDE.add("measurementTime");
		MEASUREMENT_VALUE_DTO_EXCLUDE.add("measurementType");
		MEASUREMENT_VALUE_DTO_EXCLUDE.add("measurementItemId");
		MEASUREMENT_VALUE_DTO_EXCLUDE.add("measurementTypeItemName");
		MEASUREMENT_VALUE_DTO_EXCLUDE.add("measurementTypeDisplayName");
	}

	// -------------------------------------------------------------------------------
	// JavelinLogテーブル対象ユーティリティ
	// -------------------------------------------------------------------------------

	/**
	 * 二つのJavelinLogエンティティリストが等しい事を確認する。
	 * 
	 * @param expects 予測値
	 * @param actuals 実際値
	 */
	public static void assertJavelinLog(List<Object> expects, List<Object> actuals)
	{
		Collections.sort(expects, new Comparator<Object>() {
			public int compare(Object log1, Object log2)
			{
				if (((JavelinLog) log1).logId > ((JavelinLog) log2).logId)
				{
					return 1;
				}
				else if (((JavelinLog) log1).logId == ((JavelinLog) log2).logId)
				{
					return 0;
				}

				return -1;
			}
		});

		Collections.sort(actuals, new Comparator<Object>() {
			public int compare(Object log1, Object log2)
			{
				if (((JavelinLog) log1).logId > ((JavelinLog) log2).logId)
				{
					return 1;
				}
				else if (((JavelinLog) log1).logId == ((JavelinLog) log2).logId)
				{
					return 0;
				}

				return -1;
			}
		});

		try
		{
			assertEntitiesEquals(expects, actuals, JAVELIN_LOG_EXCLUDE);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}

	/**
	 * JavelinLogのエンティティリストを生成する。
	 * 
	 * @param datarows  エンティティリストに設定するCSV
	 * @return　エンティティリスト
	 * @throws Exception
	 */
	public static List<Object> createJavelinEntities(String[] datarows)
	{
		if (datarows == null)
		{
			return null;
		}

		List<Object> javelinLogEntities;

		try
		{
			javelinLogEntities = createEntityList(JavelinLog.class, JAVELIN_LOG_FIELD_LIST,
				datarows);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}

		return javelinLogEntities;

	}

	/**
	 * 指定したファイルのパスからデータを読み込み、JavelinLogデータを生成する。
	 * 
	 * @param jvnLogFilePath JavelinLogのパス
	 * @return データにアクセスするためのストリーム
	 */
	public static InputStream convertStreamJavelinFile(String jvnLogFilePath)
	{
		File jvnLogFile = new File(jvnLogFilePath);

		if (jvnLogFile.exists() == false)
		{
			return new ByteArrayInputStream(new byte[0]);
		}

		FileInputStream jvnLogStream = null;

		try
		{
			jvnLogStream = new FileInputStream(jvnLogFile);
		}
		catch (FileNotFoundException ex)
		{
			try
			{
				if (jvnLogStream != null)
				{
					jvnLogStream.close();
				}
			}
			catch (IOException iex)
			{
			}

			return new ByteArrayInputStream(new byte[0]);
		}

		BufferedInputStream bufStream = new BufferedInputStream(jvnLogStream);
		ByteArrayOutputStream byteOStream = new ByteArrayOutputStream();

		while (true)
		{
			int readData = 0;
			try
			{
				readData = bufStream.read();
			}
			catch (IOException ex)
			{
				try
				{
					bufStream.close();
				}
				catch (IOException iex)
				{
				}
				try
				{
					byteOStream.close();
				}
				catch (IOException iex)
				{
				}
				return new ByteArrayInputStream(new byte[0]);
			}

			if (readData == -1)
			{
				break;
			}

			byteOStream.write(readData);
		}

		ByteArrayInputStream retStream = new ByteArrayInputStream(byteOStream.toByteArray());

		try
		{
			bufStream.close();
		}
		catch (IOException ex)
		{
		}

		try
		{
			byteOStream.close();
		}
		catch (IOException ex)
		{
		}

		return retStream;
	}

	// -------------------------------------------------------------------------------
	// MeasurementValue テーブル対象ユーティリティ
	// -------------------------------------------------------------------------------

	/**
	 * 二つのMeasurementValueエンティティリストが等しい事を確認する。
	 * 
	 * @param expects 予測値
	 * @param actuals 実際値
	 */
	public static void assertMeasurementValue(List<Object> expects, List<Object> actuals)
	{
		Collections.sort(expects, new Comparator<Object>() {
			public int compare(Object log1, Object log2)
			{
				if (((MeasurementValue) log1).measurementItemId > ((MeasurementValue) log2).measurementItemId)
				{
					return 1;
				}
				else if (((MeasurementValue) log1).measurementItemId == ((MeasurementValue) log2).measurementItemId)
				{
					return 0;
				}

				return -1;
			}
		});

		Collections.sort(actuals, new Comparator<Object>() {
			public int compare(Object log1, Object log2)
			{
				if (((MeasurementValue) log1).measurementItemId > ((MeasurementValue) log2).measurementItemId)
				{
					return 1;
				}
				else if (((MeasurementValue) log1).measurementItemId == ((MeasurementValue) log2).measurementItemId)
				{
					return 0;
				}

				return -1;
			}
		});

		try
		{
			assertEntitiesEquals(expects, actuals, MEASUREMENT_VALUE_EXCLUDE);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}

	/**
	 * MeasurementValueのエンティティリストを生成する。
	 * 
	 * @param datarows  エンティティリストに設定するCSV
	 * @return　エンティティリスト
	 * @throws Exception
	 */
	public static List<Object> createMeasurementValueEntities(String[] datarows)
	{
		if (datarows == null)
		{
			return null;
		}

		List<Object> measurementValueEntities;

		try
		{
			measurementValueEntities = createEntityList(MeasurementValue.class,
				MEASUREMENT_VALUE_FIELD_LIST, datarows);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}

		return measurementValueEntities;
	}

	/**
	 * MeasurementValueDtoのエンティティをCSV形式のデータから生成する。
	 * 
	 * @param datarows CSV形式のデータリスト
	 * @return 生成したデータインスタンスのリスト
	 */
	public static List<Object> createMeasurementValueDtoEntities(String[] datarows)
	{
		if (datarows == null)
		{
			return null;
		}

		List<Object> measurementValueDtoEntities;

		try
		{
			measurementValueDtoEntities = createEntityList(MeasurementValueDto.class,
				MEASUREMENT_VALUE_DTO_FIELD_LIST, datarows);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}

		return measurementValueDtoEntities;
	}

	public static void assertMeasurementValueDto(List<Object> expects, List<Object> actuals)
	{
		Collections.sort(expects, new Comparator<Object>() {
			public int compare(Object log1, Object log2)
			{

				MeasurementValueDto obj1 = (MeasurementValueDto) log1;
				MeasurementValueDto obj2 = (MeasurementValueDto) log2;

				if (obj1.measurementItemName.compareTo(obj2.measurementItemName) > 0)
				{
					return 1;
				}
				else if (obj1.measurementItemName.compareTo(obj2.measurementItemName) < 0)
				{
					return -1;
				}

				if (Long.parseLong(obj1.value) > Long.parseLong(obj2.value))
				{
					return 1;
				}
				else if (Long.parseLong(obj1.value) < Long.parseLong(obj2.value))
				{
					return -1;
				}

				return 0;
			}
		});

		Collections.sort(actuals, new Comparator<Object>() {
			public int compare(Object log1, Object log2)
			{

				MeasurementValueDto obj1 = (MeasurementValueDto) log1;
				MeasurementValueDto obj2 = (MeasurementValueDto) log2;

				if (obj1.measurementItemName.compareTo(obj2.measurementItemName) > 0)
				{
					return 1;
				}
				else if (obj1.measurementItemName.compareTo(obj2.measurementItemName) < 0)
				{
					return -1;
				}

				if (Long.parseLong(obj1.value) > Long.parseLong(obj2.value))
				{
					return 1;
				}
				else if (Long.parseLong(obj1.value) < Long.parseLong(obj2.value))
				{
					return -1;
				}

				return 0;
			}
		});

		try
		{
			assertEntitiesEquals(expects, actuals, MEASUREMENT_VALUE_DTO_EXCLUDE);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}

	}

	// -------------------------------------------------------------------------------
	// JavelinMeasurementItem テーブル対象ユーティリティ
	// -------------------------------------------------------------------------------

	/**
	 * 二つのJavelinMeasurementItemエンティティリストが等しい事を確認する。
	 * 
	 * @param expects 予測値
	 * @param actuals 実際値
	 */
	public static void assertJavelinMeasurementItem(List<Object> expects, List<Object> actuals)
	{
		Collections.sort(expects, new Comparator<Object>() {
			public int compare(Object log1, Object log2)
			{
				if (((JavelinMeasurementItem) log1).measurementItemId > ((JavelinMeasurementItem) log2).measurementItemId)
				{
					return 1;
				}
				else if (((JavelinMeasurementItem) log1).measurementItemId == ((JavelinMeasurementItem) log2).measurementItemId)
				{
					return 0;
				}

				return -1;
			}
		});

		Collections.sort(actuals, new Comparator<Object>() {
			public int compare(Object log1, Object log2)
			{
				if (((JavelinMeasurementItem) log1).measurementItemId > ((JavelinMeasurementItem) log2).measurementItemId)
				{
					return 1;
				}
				else if (((JavelinMeasurementItem) log1).measurementItemId == ((JavelinMeasurementItem) log2).measurementItemId)
				{
					return 0;
				}

				return -1;
			}
		});

		try
		{
			assertEntitiesEquals(expects, actuals, JAVELIN_MEASUREMENT_ITEM_EXCLUDE);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}

	/**
	 * JavelinMeasurementItemのエンティティリストを生成する。
	 * 
	 * @param datarows  エンティティリストに設定するCSV
	 * @return　エンティティリスト
	 * @throws Exception
	 */
	public static List<Object> createJavelinMeasurementItemEntities(String[] datarows)
	{
		if (datarows == null)
		{
			return null;
		}

		List<Object> javelinMeasurementItemEntities;

		try
		{
			javelinMeasurementItemEntities = createEntityList(JavelinMeasurementItem.class,
				JAVELIN_MEASUREMENT_ITEM_FIELD_LIST, datarows);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}

		return javelinMeasurementItemEntities;
	}

	// -------------------------------------------------------------------------------
	// MeasurementInfo テーブル対象ユーティリティ
	// -------------------------------------------------------------------------------

	/**
	 * 二つのMeasurementInfoエンティティリストが等しい事を確認する。
	 * 
	 * @param expects 予測値
	 * @param actuals 実際値
	 */
	public static void assertMeasurementInfo(List<Object> expects, List<Object> actuals)
	{
		Collections.sort(expects, new Comparator<Object>() {
			public int compare(Object log1, Object log2)
			{
				if (((MeasurementInfo) log1).getMeasurementType() > ((MeasurementInfo) log2)
					.getMeasurementType())
				{
					return 1;
				}
				else if (((MeasurementInfo) log1).getMeasurementType() == ((MeasurementInfo) log2)
					.getMeasurementType())
				{
					return 0;
				}

				return -1;
			}
		});

		Collections.sort(actuals, new Comparator<Object>() {
			public int compare(Object log1, Object log2)
			{
				if (((MeasurementInfo) log1).getMeasurementType() > ((MeasurementInfo) log2)
					.getMeasurementType())
				{
					return 1;
				}
				else if (((MeasurementInfo) log1).getMeasurementType() == ((MeasurementInfo) log2)
					.getMeasurementType())
				{
					return 0;
				}

				return -1;
			}
		});

		try
		{
			assertEntitiesEquals(expects, actuals, MEASUREMENT_INFO_EXCLUDE);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}

	/**
	 * MeasurementInfoのエンティティリストを生成する。
	 * 
	 * @param datarows  エンティティリストに設定するCSV
	 * @return　エンティティリスト
	 * @throws Exception
	 */
	public static List<Object> createMeasurementInfoEntities(String[] datarows)
	{
		if (datarows == null)
		{
			return null;
		}

		List<Object> measurementInfoEntities = new ArrayList<Object>();

		try
		{
			for (String datarow : datarows)
			{
				String[] dataElements = datarow.split(",");

				Field mType = MeasurementInfo.class.getDeclaredField("measurementType_");
				Field iName = MeasurementInfo.class.getDeclaredField("itemName_");
				Field dName = MeasurementInfo.class.getDeclaredField("displayName_");
				Field descr = MeasurementInfo.class.getDeclaredField("description_");
				mType.setAccessible(true);
				iName.setAccessible(true);
				dName.setAccessible(true);
				descr.setAccessible(true);

				Object mTypeVal = parseString(mType, dataElements[0]);
				Object iNameVal = parseString(iName, dataElements[1]);
				Object dNameVal = parseString(dName, dataElements[2]);
				Object descrVal = parseString(descr, dataElements[3]);

				Object infoEntity = MeasurementInfo.class.getConstructors()[0].newInstance(
					mTypeVal, iNameVal, dNameVal, descrVal);
				measurementInfoEntities.add(infoEntity);
			}
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}

		return measurementInfoEntities;
	}

	// -------------------------------------------------------------------------------
	// 共通ユーティリティ
	// -------------------------------------------------------------------------------

	/**
	 * 各エンティティのリストに格納された値がそれぞれ等しいか否かを判定する。
	 * 
	 * @param expects  予測値
	 * @param actuals  実際値
	 * @param exclude  チェックから除外するフィールドの名前
	 * @throws Exception
	 */
	public static void assertEntitiesEquals(List<Object> expects, List<Object> actuals,
		Set<String> exclude) throws Exception
	{
		if (expects.size() != actuals.size())
		{
			Assert.fail("actual datasize different expect datasize !! : expect<" + expects.size()
				+ "> but actual<" + actuals.size() + ">");
		}

		for (int cnt = 0; cnt < expects.size(); cnt++)
		{
			assertEntityEquals(expects.get(cnt), actuals.get(cnt), exclude);
		}
	}

	/**
	 * 各エンティティ同士のフィールドが等しいか否かを判定する。
	 * 
	 * @param expect  予測値
	 * @param actual  実際値
	 * @param exclude チェックを行わないフィールドのセット
	 * @throws Exception 
	 */
	public static void assertEntityEquals(Object expect, Object actual, Set<String> exclude)
		throws Exception
	{
		if (expect == null || actual == null)
		{
			Assert.fail("expect or actual is NULL !!");
		}

		Class<?> chkTargetClass = expect.getClass();
		Field[] chkFields = chkTargetClass.getDeclaredFields();

		Assert.assertEquals(chkTargetClass, actual.getClass());

		for (Field chkField : chkFields)
		{
			if (exclude.contains(chkField.getName()))
			{
				continue;
			}

			if (chkField.isAccessible() == false)
			{
				chkField.setAccessible(true);
			}

			Object expectFieldValue = chkField.get(expect);
			Object actualFieldValue = chkField.get(actual);

			if (Number.class.getName().equals(chkField.getType().getName()))
			{
				expectFieldValue = new BigDecimal(expectFieldValue.toString());
				actualFieldValue = new BigDecimal(actualFieldValue.toString());

				if (((BigDecimal) expectFieldValue).scale() > ((BigDecimal) actualFieldValue)
					.scale())
				{
					actualFieldValue = ((BigDecimal) actualFieldValue)
						.setScale(((BigDecimal) expectFieldValue).scale());
				}
				else
				{
					expectFieldValue = ((BigDecimal) expectFieldValue)
						.setScale(((BigDecimal) actualFieldValue).scale());
				}
			}

			if (expectFieldValue == null && actualFieldValue == null)
			{
				continue;
			}

			if (expectFieldValue == null)
			{
				Assert.assertNull(actualFieldValue);
			}
			else
			{
				Assert.assertNotNull(actualFieldValue);
			}

			Assert.assertEquals(expectFieldValue, actualFieldValue);
		}
	}

	/**
	 * CSV形式に定義されたデータを、指定したクラスのインスタンスに変換する。
	 * ただし、static final宣言された変数を指定することは出来ない。
	 * 
	 * @param clazz     対象となるクラス
	 * @param fieldList 設定対象とするフィールド
	 * @param datarows  設定するデータ(CSV形式)のリスト
	 * @return　生成したクラスインスタンスのリスト
	 * @throws Exception
	 */
	public static List<Object> createEntityList(Class<?> clazz, String[] fieldList,
		String[] datarows) throws Exception
	{
		if (clazz == null || fieldList == null || fieldList.length < 1)
		{
			return null;
		}

		List<Object> entityList = new ArrayList<Object>();

		for (String datarow : datarows)
		{
			Object settingEntity = createEntity(clazz, fieldList, datarow);
			entityList.add(settingEntity);
		}

		return entityList;
	}

	/**
	 * CSV形式に定義されたデータを、指定したクラスのインスタンスに変換する。
	 * ただし、static final宣言された変数を指定することは出来ない。
	 * 
	 * @param clazz     対象となるクラス
	 * @param fieldList 設定対象とするフィールド
	 * @param datarows　 設定するデータ(CSV形式)
	 * @return　生成したクラスインスタンス
	 * @throws Exception
	 */
	public static Object createEntity(Class<?> clazz, String[] fieldList, String datarow)
		throws Exception
	{
		Object entityObj = clazz.newInstance();
		String[] dataElements = datarow.split(",");

		for (int fieldCnt = 0; fieldCnt < fieldList.length; fieldCnt++)
		{
			Field settingField = clazz.getField(fieldList[fieldCnt]);
			String settingData = null;

			if (fieldCnt < dataElements.length)
			{
				settingData = dataElements[fieldCnt].trim();
			}

			Object settingVal = parseString(settingField, settingData);

			if (settingVal != null)
			{
				if (settingField.isAccessible() == false)
				{
					settingField.setAccessible(true);
				}

				settingField.set(entityObj, settingVal);
			}
		}

		return entityObj;
	}

	/**
	 * 文字列表現で表されたデータを、指定したフィールドに設定可能なオブジェクトに変換する。
	 * 
	 * @param field 値の設定対象となるフィールド
	 * @param data  対象フィールドに設定するデータの文字列表現
	 * @return      型変換された設定値
	 * @throws Exception
	 */
	public static Object parseString(Field field, String data) throws Exception
	{
		String fieldTypeName = field.getType().getName();

		if (data == null)
		{
			if (InputStream.class.getName().equals(fieldTypeName))
			{
				return new ByteArrayInputStream(new byte[0]);
			}

			return null;
		}

		if (String.class.getName().equals(fieldTypeName))
		{
			return data;
		}

		if ("".equals(data))
		{
			return null;
		}

		Method parseMethod = parseMethodMap.get(fieldTypeName);

		if (parseMethod != null)
		{
			return parseMethod.invoke(null, data);
		}

		if (Timestamp.class.getName().equals(fieldTypeName))
		{
			DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			return new Timestamp(format.parse(data).getTime());
		}

		if (Date.class.getName().equals(fieldTypeName))
		{
			DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			return format.parse(data);
		}

		if (InputStream.class.getName().equals(fieldTypeName))
		{
			return convertStreamJavelinFile(data);
		}

		if (Number.class.getName().equals(fieldTypeName))
		{
			Method parseNumberMethod;

			if (data.indexOf(".") == -1)
			{
				parseNumberMethod = parseMethodMap.get("long");
			}
			else
			{
				parseNumberMethod = parseMethodMap.get("double");
			}

			return parseNumberMethod.invoke(null, data);
		}

		return null;
	}

	public static Timestamp convertString2TimeStamp(String dateString) throws ParseException
	{
		return new Timestamp(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateString)
			.getTime());

	}

	/**
	 * privateフィールドなど、アクセスできないフィールドに対して、値を設定する。
	 * static privateに対してのアクセスは不可能。
	 * 
	 * @param clazz     設定対象のクラスのクラス情報
	 * @param fieldName 設定対象のフィールド名
	 * @param instance  設定対象のインスタンス
	 * @param value     設定する値
	 */
	public static void setNonAccessibleField(Class<?> clazz, String fieldName, Object instance,
		Object value)
	{
		if (instance != null && clazz.equals(instance.getClass()) == false)
		{
			throw new RuntimeException("設定対象クラスのインスタンスが一致しません");
		}

		try
		{
			Field targetField = clazz.getDeclaredField(fieldName);
			targetField.setAccessible(true);
			targetField.set(instance, value);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}

	/**
	 * privateフィールドなど、アクセスできないフィールドの値を取得する。
	 * 
	 * @param clazz     設定対象クラスのクラス情報
	 * @param fieldName 設定対象のフィールド名
	 * @param instance  設定対象のインスタンス
	 * @return          設定する値
	 */
	public static Object getNonAccessibleField(Class<?> clazz, String fieldName, Object instance)
	{
		if (instance != null && clazz.equals(instance.getClass()) == false)
		{
			throw new RuntimeException("取得対象クラスのインスタンスが一致しません");
		}

		Object result = null;
		try
		{
			Field targetField = clazz.getDeclaredField(fieldName);
			targetField.setAccessible(true);
			result = targetField.get(instance);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}

		return result;
	}

	/**
	 * privateメソッドなど、直接アクセスできないメソッドを実行する。
	 * 
	 * @param clazz      対象のメソッドが定義されているクラスのクラス情報
	 * @param methodName 呼び出す対象のメソッドの名称
	 * @param instance   メソッドを呼び出すインスタンス。staticメソッドの場合はnull。
	 * @param params     メソッドに指定するパラメータ(プリミティブ型の場合はラッパークラスを使用する)
	 * @return　指定したメソッドの呼出結果
	 */
	public static Object invokeNonAccessibleMethod(Class<?> clazz, String methodName,
		Object instance, Object... params)
	{
		List<Class<?>> targetParamTypes = new ArrayList<Class<?>>();

		if (params != null)
		{
			for (Object param : params)
			{
				targetParamTypes.add(param.getClass());
			}
		}
		Object retVal = null;

		try
		{
			Method targetMethod = getParamTypesMatchMethod(clazz, methodName, params);
			targetMethod.setAccessible(true);
			retVal = targetMethod.invoke(instance, params);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}

		return retVal;
	}

	/**
	 * 指定した引数に、型が対応するメソッドを取得する。
	 * 
	 * @param clazz      メソッドを検索する型
	 * @param methodName メソッドの名前
	 * @param params     メソッドに適用する引数
	 * @return 適合したメソッド。見付からなかった場合はnull。
	 */
	private static Method getParamTypesMatchMethod(Class<?> clazz, String methodName,
		Object... params)
	{
		Method[] methods = clazz.getDeclaredMethods();

		for (Method method : methods)
		{
			if (!method.getName().equals(methodName))
			{
				continue;
			}

			Class<?>[] paramTypes = method.getParameterTypes();

			if (paramTypes.length != params.length)
			{
				continue;
			}

			boolean matchParamType = true;
			for (int index = 0; index < params.length; index++)
			{
				if (paramTypes[index].isPrimitive() == true)
				{
					Class<?> wrapperType = getWrapperClass(paramTypes[index]);
					if (!wrapperType.isInstance(params[index]))
					{
						matchParamType = false;
						break;
					}
				}
				else
				{
					if (!paramTypes[index].isInstance(params[index]))
					{
						matchParamType = false;
						break;
					}
				}
			}

			if (matchParamType)
			{
				return method;
			}
		}

		return null;
	}

	/**
	 * 指定したプリミティブ型に対応するラッパークラスを取得する。
	 * 
	 * @param clazz プリミティブ型
	 * @return　パラメータに対応するラッパークラス。パラメータにプリミティブ型が適用されていなければnull。
	 */
	private static Class<? extends Object> getWrapperClass(Class<?> clazz)
	{
		if (clazz.isPrimitive() == false)
		{
			return null;
		}

		// 一旦プリミティブ型の配列を生成後、Arrayクラスを使って要素を取得することで
		// ラッパークラスに変換されたデータを取得できる。
		Object array = Array.newInstance(clazz, 1);
		Object wrapper = Array.get(array, 0);

		return wrapper.getClass();
	}

}
