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
 * ENdoSnipeReporter�v���W�F�N�g�̃e�X�g���[�e�B���e�B�N���X
 * 
 * @author kimura
 */
public class ReporterTestUtil
{

    protected static final String      DB_NAME                             = "endosnipedb";

    private static Map<String, Method> parseMethodMap;

    private static String[]            JAVELIN_LOG_FIELD_LIST              =
                                                                             {"logId", "sessionId",
            "sequenceId", "javelinLog", "logFileName", "startTime", "endTime", "sessionDesc",
            "logType", "calleeName", "calleeSignature", "calleeClass", "calleeFieldType",
            "calleeObjectId", "callerName", "callerSignature", "callerClass", "callerObjectId",
            "eventLevel", "elapsedTime", "modifier", "threadName", "threadClass", "threadObjectId"};

    private static Set<String>         JAVELIN_LOG_EXCLUDE                 = new HashSet<String>();

    private static final String[]      MEASUREMENT_VALUE_FIELD_LIST        =
                                                                             {"measurementValueId",
            "measurementNum", "measurementTime", "measurementType", "measurementItemId", "value"};

    private static Set<String>         MEASUREMENT_VALUE_EXCLUDE           = new HashSet<String>();

    private static final String[]      JAVELIN_MEASUREMENT_ITEM_FIELD_LIST =
                                                                             {"measurementItemId",
            "measurementType", "itemName"                                    };

    private static Set<String>         JAVELIN_MEASUREMENT_ITEM_EXCLUDE    = new HashSet<String>();

    private static final String[]      MEASUREMENT_INFO_FIELD_LIST         =
                                                                             {"measurementType_",
            "itemName_", "displayName_", "description_"                      };

    private static Set<String>         MEASUREMENT_INFO_EXCLUDE            = new HashSet<String>();

    private static final String[]      MEASUREMENT_VALUE_DTO_FIELD_LIST    =
                                                                             {"measurementValueId",
            "measurementNum", "measurementTime", "measurementType", "measurementItemId", "value",
            "measurementTypeItemName", "measurementTypeDisplayName", "measurementItemName"};

    private static Set<String>         MEASUREMENT_VALUE_DTO_EXCLUDE       = new HashSet<String>();

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
    // JavelinLog�e�[�u���Ώۃ��[�e�B���e�B
    // -------------------------------------------------------------------------------

    /**
     * ���JavelinLog�G���e�B�e�B���X�g�������������m�F����B
     * 
     * @param expects �\���l
     * @param actuals ���ےl
     */
    public static void assertJavelinLog(List<Object> expects, List<Object> actuals)
    {
        Collections.sort(expects, new Comparator<Object>() {
            public int compare(Object log1, Object log2)
            {
                if (((JavelinLog)log1).logId > ((JavelinLog)log2).logId)
                {
                    return 1;
                }
                else if (((JavelinLog)log1).logId == ((JavelinLog)log2).logId)
                {
                    return 0;
                }

                return -1;
            }
        });

        Collections.sort(actuals, new Comparator<Object>() {
            public int compare(Object log1, Object log2)
            {
                if (((JavelinLog)log1).logId > ((JavelinLog)log2).logId)
                {
                    return 1;
                }
                else if (((JavelinLog)log1).logId == ((JavelinLog)log2).logId)
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
     * JavelinLog�̃G���e�B�e�B���X�g�𐶐�����B
     * 
     * @param datarows  �G���e�B�e�B���X�g�ɐݒ肷��CSV
     * @return�@�G���e�B�e�B���X�g
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
            javelinLogEntities =
                                 createEntityList(JavelinLog.class, JAVELIN_LOG_FIELD_LIST,
                                                  datarows);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        return javelinLogEntities;

    }

    /**
     * �w�肵���t�@�C���̃p�X����f�[�^��ǂݍ��݁AJavelinLog�f�[�^�𐶐�����B
     * 
     * @param jvnLogFilePath JavelinLog�̃p�X
     * @return �f�[�^�ɃA�N�Z�X���邽�߂̃X�g���[��
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
    // MeasurementValue �e�[�u���Ώۃ��[�e�B���e�B
    // -------------------------------------------------------------------------------

    /**
     * ���MeasurementValue�G���e�B�e�B���X�g�������������m�F����B
     * 
     * @param expects �\���l
     * @param actuals ���ےl
     */
    public static void assertMeasurementValue(List<Object> expects, List<Object> actuals)
    {
        Collections.sort(expects, new Comparator<Object>() {
            public int compare(Object log1, Object log2)
            {
                if (((MeasurementValue)log1).measurementItemId > ((MeasurementValue)log2).measurementItemId)
                {
                    return 1;
                }
                else if (((MeasurementValue)log1).measurementItemId == ((MeasurementValue)log2).measurementItemId)
                {
                    return 0;
                }

                return -1;
            }
        });

        Collections.sort(actuals, new Comparator<Object>() {
            public int compare(Object log1, Object log2)
            {
                if (((MeasurementValue)log1).measurementItemId > ((MeasurementValue)log2).measurementItemId)
                {
                    return 1;
                }
                else if (((MeasurementValue)log1).measurementItemId == ((MeasurementValue)log2).measurementItemId)
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
     * MeasurementValue�̃G���e�B�e�B���X�g�𐶐�����B
     * 
     * @param datarows  �G���e�B�e�B���X�g�ɐݒ肷��CSV
     * @return�@�G���e�B�e�B���X�g
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
            measurementValueEntities =
                                       createEntityList(MeasurementValue.class,
                                                        MEASUREMENT_VALUE_FIELD_LIST, datarows);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        return measurementValueEntities;
    }

    /**
     * MeasurementValueDto�̃G���e�B�e�B��CSV�`���̃f�[�^���琶������B
     * 
     * @param datarows CSV�`���̃f�[�^���X�g
     * @return ���������f�[�^�C���X�^���X�̃��X�g
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
            measurementValueDtoEntities =
                                          createEntityList(MeasurementValueDto.class,
                                                           MEASUREMENT_VALUE_DTO_FIELD_LIST,
                                                           datarows);
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

                MeasurementValueDto obj1 = (MeasurementValueDto)log1;
                MeasurementValueDto obj2 = (MeasurementValueDto)log2;

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

                MeasurementValueDto obj1 = (MeasurementValueDto)log1;
                MeasurementValueDto obj2 = (MeasurementValueDto)log2;

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
    // JavelinMeasurementItem �e�[�u���Ώۃ��[�e�B���e�B
    // -------------------------------------------------------------------------------

    /**
     * ���JavelinMeasurementItem�G���e�B�e�B���X�g�������������m�F����B
     * 
     * @param expects �\���l
     * @param actuals ���ےl
     */
    public static void assertJavelinMeasurementItem(List<Object> expects, List<Object> actuals)
    {
        Collections.sort(expects, new Comparator<Object>() {
            public int compare(Object log1, Object log2)
            {
                if (((JavelinMeasurementItem)log1).measurementItemId > ((JavelinMeasurementItem)log2).measurementItemId)
                {
                    return 1;
                }
                else if (((JavelinMeasurementItem)log1).measurementItemId == ((JavelinMeasurementItem)log2).measurementItemId)
                {
                    return 0;
                }

                return -1;
            }
        });

        Collections.sort(actuals, new Comparator<Object>() {
            public int compare(Object log1, Object log2)
            {
                if (((JavelinMeasurementItem)log1).measurementItemId > ((JavelinMeasurementItem)log2).measurementItemId)
                {
                    return 1;
                }
                else if (((JavelinMeasurementItem)log1).measurementItemId == ((JavelinMeasurementItem)log2).measurementItemId)
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
     * JavelinMeasurementItem�̃G���e�B�e�B���X�g�𐶐�����B
     * 
     * @param datarows  �G���e�B�e�B���X�g�ɐݒ肷��CSV
     * @return�@�G���e�B�e�B���X�g
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
            javelinMeasurementItemEntities =
                                             createEntityList(JavelinMeasurementItem.class,
                                                              JAVELIN_MEASUREMENT_ITEM_FIELD_LIST,
                                                              datarows);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        return javelinMeasurementItemEntities;
    }

    // -------------------------------------------------------------------------------
    // MeasurementInfo �e�[�u���Ώۃ��[�e�B���e�B
    // -------------------------------------------------------------------------------

    /**
     * ���MeasurementInfo�G���e�B�e�B���X�g�������������m�F����B
     * 
     * @param expects �\���l
     * @param actuals ���ےl
     */
    public static void assertMeasurementInfo(List<Object> expects, List<Object> actuals)
    {
        Collections.sort(expects, new Comparator<Object>() {
            public int compare(Object log1, Object log2)
            {
                if (((MeasurementInfo)log1).getMeasurementType() > ((MeasurementInfo)log2).getMeasurementType())
                {
                    return 1;
                }
                else if (((MeasurementInfo)log1).getMeasurementType() == ((MeasurementInfo)log2).getMeasurementType())
                {
                    return 0;
                }

                return -1;
            }
        });

        Collections.sort(actuals, new Comparator<Object>() {
            public int compare(Object log1, Object log2)
            {
                if (((MeasurementInfo)log1).getMeasurementType() > ((MeasurementInfo)log2).getMeasurementType())
                {
                    return 1;
                }
                else if (((MeasurementInfo)log1).getMeasurementType() == ((MeasurementInfo)log2).getMeasurementType())
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
     * MeasurementInfo�̃G���e�B�e�B���X�g�𐶐�����B
     * 
     * @param datarows  �G���e�B�e�B���X�g�ɐݒ肷��CSV
     * @return�@�G���e�B�e�B���X�g
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

                Object infoEntity =
                                    MeasurementInfo.class.getConstructors()[0].newInstance(
                                                                                           mTypeVal,
                                                                                           iNameVal,
                                                                                           dNameVal,
                                                                                           descrVal);
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
    // ���ʃ��[�e�B���e�B
    // -------------------------------------------------------------------------------

    /**
     * �e�G���e�B�e�B�̃��X�g�Ɋi�[���ꂽ�l�����ꂼ�ꓙ�������ۂ��𔻒肷��B
     * 
     * @param expects  �\���l
     * @param actuals  ���ےl
     * @param exclude  �`�F�b�N���珜�O����t�B�[���h�̖��O
     * @throws Exception
     */
    public static void assertEntitiesEquals(List<Object> expects, List<Object> actuals,
            Set<String> exclude)
        throws Exception
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
     * �e�G���e�B�e�B���m�̃t�B�[���h�����������ۂ��𔻒肷��B
     * 
     * @param expect  �\���l
     * @param actual  ���ےl
     * @param exclude �`�F�b�N���s��Ȃ��t�B�[���h�̃Z�b�g
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

                if (((BigDecimal)expectFieldValue).scale() > ((BigDecimal)actualFieldValue).scale())
                {
                    actualFieldValue =
                                       ((BigDecimal)actualFieldValue).setScale(((BigDecimal)expectFieldValue).scale());
                }
                else
                {
                    expectFieldValue =
                                       ((BigDecimal)expectFieldValue).setScale(((BigDecimal)actualFieldValue).scale());
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
     * CSV�`���ɒ�`���ꂽ�f�[�^���A�w�肵���N���X�̃C���X�^���X�ɕϊ�����B
     * �������Astatic final�錾���ꂽ�ϐ����w�肷�邱�Ƃ͏o���Ȃ��B
     * 
     * @param clazz     �ΏۂƂȂ�N���X
     * @param fieldList �ݒ�ΏۂƂ���t�B�[���h
     * @param datarows  �ݒ肷��f�[�^(CSV�`��)�̃��X�g
     * @return�@���������N���X�C���X�^���X�̃��X�g
     * @throws Exception
     */
    public static List<Object> createEntityList(Class<?> clazz, String[] fieldList,
            String[] datarows)
        throws Exception
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
     * CSV�`���ɒ�`���ꂽ�f�[�^���A�w�肵���N���X�̃C���X�^���X�ɕϊ�����B
     * �������Astatic final�錾���ꂽ�ϐ����w�肷�邱�Ƃ͏o���Ȃ��B
     * 
     * @param clazz     �ΏۂƂȂ�N���X
     * @param fieldList �ݒ�ΏۂƂ���t�B�[���h
     * @param datarows�@ �ݒ肷��f�[�^(CSV�`��)
     * @return�@���������N���X�C���X�^���X
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
     * ������\���ŕ\���ꂽ�f�[�^���A�w�肵���t�B�[���h�ɐݒ�\�ȃI�u�W�F�N�g�ɕϊ�����B
     * 
     * @param field �l�̐ݒ�ΏۂƂȂ�t�B�[���h
     * @param data  �Ώۃt�B�[���h�ɐݒ肷��f�[�^�̕�����\��
     * @return      �^�ϊ����ꂽ�ݒ�l
     * @throws Exception
     */
    public static Object parseString(Field field, String data)
        throws Exception
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

    public static Timestamp convertString2TimeStamp(String dateString)
        throws ParseException
    {
        return new Timestamp(
                             new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateString).getTime());

    }

    /**
     * private�t�B�[���h�ȂǁA�A�N�Z�X�ł��Ȃ��t�B�[���h�ɑ΂��āA�l��ݒ肷��B
     * static private�ɑ΂��ẴA�N�Z�X�͕s�\�B
     * 
     * @param clazz     �ݒ�Ώۂ̃N���X�̃N���X���
     * @param fieldName �ݒ�Ώۂ̃t�B�[���h��
     * @param instance  �ݒ�Ώۂ̃C���X�^���X
     * @param value     �ݒ肷��l
     */
    public static void setNonAccessibleField(
        Class<?> clazz, String fieldName, Object instance, Object value)
    {
        if (instance != null && clazz.equals(instance.getClass()) == false)
        {
            throw new RuntimeException("�ݒ�ΏۃN���X�̃C���X�^���X����v���܂���");
        }
        
        try
        {
            Field targetField = clazz.getDeclaredField(fieldName);
            targetField.setAccessible(true);
            targetField.set(instance, value);
        }
        catch(Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * private�t�B�[���h�ȂǁA�A�N�Z�X�ł��Ȃ��t�B�[���h�̒l���擾����B
     * 
     * @param clazz     �ݒ�ΏۃN���X�̃N���X���
     * @param fieldName �ݒ�Ώۂ̃t�B�[���h��
     * @param instance  �ݒ�Ώۂ̃C���X�^���X
     * @return          �ݒ肷��l
     */
    public static Object getNonAccessibleField(
        Class<?> clazz, String fieldName, Object instance)
    {
        if (instance != null && clazz.equals(instance.getClass()) == false)
        {
            throw new RuntimeException("�擾�ΏۃN���X�̃C���X�^���X����v���܂���");
        }
        
        Object result = null;
        try
        {
            Field targetField = clazz.getDeclaredField(fieldName);
            targetField.setAccessible(true);
            result = targetField.get(instance);
        }
        catch(Exception ex)
        {
            throw new RuntimeException(ex);
        }
        
        return result;
    }
    
    /**
     * private���\�b�h�ȂǁA���ڃA�N�Z�X�ł��Ȃ����\�b�h�����s����B
     * 
     * @param clazz      �Ώۂ̃��\�b�h����`����Ă���N���X�̃N���X���
     * @param methodName �Ăяo���Ώۂ̃��\�b�h�̖���
     * @param instance   ���\�b�h���Ăяo���C���X�^���X�Bstatic���\�b�h�̏ꍇ��null�B
     * @param params     ���\�b�h�Ɏw�肷��p�����[�^(�v���~�e�B�u�^�̏ꍇ�̓��b�p�[�N���X���g�p����)
     * @return�@�w�肵�����\�b�h�̌ďo����
     */
    public static Object invokeNonAccessibleMethod(
        Class<?> clazz, String methodName, Object instance, Object ... params)
    {
        List<Class<?>> targetParamTypes = new ArrayList<Class<?>>();
        
        if(params != null)
        {
            for(Object param : params)
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
     * �w�肵�������ɁA�^���Ή����郁�\�b�h���擾����B
     * 
     * @param clazz      ���\�b�h����������^
     * @param methodName ���\�b�h�̖��O
     * @param params     ���\�b�h�ɓK�p�������
     * @return �K���������\�b�h�B���t����Ȃ������ꍇ��null�B
     */
    private static Method getParamTypesMatchMethod(Class<?> clazz, String methodName, Object ... params)
    {
        Method[] methods = clazz.getDeclaredMethods();
        
        for(Method method : methods)
        {
            if(!method.getName().equals(methodName))
            {
                continue;
            }
            
            Class<?>[] paramTypes = method.getParameterTypes();

            if(paramTypes.length != params.length)
            {
                continue;
            }
            
            boolean matchParamType = true;
            for(int index = 0; index < params.length; index ++)
            {
            	if(paramTypes[index].isPrimitive() == true)
            	{
            		Class<?> wrapperType = getWrapperClass(paramTypes[index]);
            		if(!wrapperType.isInstance(params[index]))
            		{
            			matchParamType = false;
            			break;
            		}
            	}
            	else
            	{
	                if(!paramTypes[index].isInstance(params[index]))
	                {
	                    matchParamType = false;
	                    break;
	                }
            	}
            }
            
            if(matchParamType)
            {
                return method;
            }
        }
        
        return null;
    }

    /**
     * �w�肵���v���~�e�B�u�^�ɑΉ����郉�b�p�[�N���X���擾����B
     * 
     * @param clazz �v���~�e�B�u�^
     * @return�@�p�����[�^�ɑΉ����郉�b�p�[�N���X�B�p�����[�^�Ƀv���~�e�B�u�^���K�p����Ă��Ȃ����null�B
     */
    private static Class<? extends Object> getWrapperClass(Class<?> clazz)
    {
    	if(clazz.isPrimitive() == false)
    	{
    		return null;
    	}
    	
    	// ��U�v���~�e�B�u�^�̔z��𐶐���AArray�N���X���g���ėv�f���擾���邱�Ƃ�
    	// ���b�p�[�N���X�ɕϊ����ꂽ�f�[�^���擾�ł���B
    	Object array = Array.newInstance(clazz, 1);
    	Object wrapper = Array.get(array, 0);
    	
    	return wrapper.getClass();
    }

    
}
