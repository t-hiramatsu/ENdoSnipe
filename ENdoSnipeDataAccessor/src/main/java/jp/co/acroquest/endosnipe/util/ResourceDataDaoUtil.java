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
package jp.co.acroquest.endosnipe.util;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.common.entity.MeasurementData;
import jp.co.acroquest.endosnipe.common.entity.MeasurementDetail;
import jp.co.acroquest.endosnipe.common.entity.ResourceData;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.data.LogMessageCodes;
import jp.co.acroquest.endosnipe.data.dao.JavelinMeasurementItemDao;
import jp.co.acroquest.endosnipe.data.dao.MeasurementValueDao;
import jp.co.acroquest.endosnipe.data.db.DBManager;
import jp.co.acroquest.endosnipe.data.entity.JavelinMeasurementItem;
import jp.co.acroquest.endosnipe.data.entity.MeasurementValue;

/**
 * �f�[�^�x�[�X�̌v���l���� {@link ResourceData} �ł��Ƃ肷�邽�߂̃��[�e�B���e�B�N���X�B
 *
 * @author sakamoto
 */
public class ResourceDataDaoUtil
{
    /** ���K�[ */
    private static final ENdoSnipeLogger LOGGER =
        ENdoSnipeLogger.getLogger(ResourceDataDaoUtil.class);

    /** �f�[�^�x�[�X�����L�[�Ƃ���A�O��f�[�^��}�������e�[�u���C���f�b�N�X��ێ�����}�b�v */
    private static Map<String, Integer> prevTableIndexMap__ =
            new ConcurrentHashMap<String, Integer>();

    /** �f�[�^�x�[�X�����L�[�Ƃ���A�n��ԍ��Ƃ��̍ŏI�}�������̃}�b�v��ێ�����}�b�v */
    private static Map<String, Map<Integer, Timestamp>> measurementItemUpdatedMap__ =
            new ConcurrentHashMap<String, Map<Integer, Timestamp>>();

    /** �f�[�^�x�[�X�����L�[�Ƃ���Ameasurement_type��item_name��":"�ŋ�؂��ĘA��������������L�[�Ƃ���Aitem_id�̃}�b�v��ێ�����}�b�v*/
    private static Map<String, Map<String, Integer>> measurementItemIdMap__ =
            new ConcurrentHashMap<String, Map<String, Integer>>();
    
    /** �P�T�Ԃ̓��� */
    public static final int DAY_OF_WEEK = 7;

    /** �P�N�Ԃ̍ő���� */
    private static final int DAY_OF_YEAR_MAX = 366;

    /** �p�[�e�B�V���j���O���ꂽ�e�[�u���̐��i�Ō�̃e�[�u����7���ȏ�̃f�[�^������j */
    public static final int PARTITION_TABLE_COUNT = DAY_OF_YEAR_MAX / DAY_OF_WEEK;

    /** JAVELIN_MEASUREMENT_ITEM�e�[�u����LAST_INSERTED�t�B�[���h���X�V����Ԋu�i�~���b�j */
    private static final int ITEM_UPDATE_INTERVAL = 24 * 60 * 60 * 1000;

    /** �o�b�`�̃T�C�Y */
    private static final int DEF_BATCH_SIZE = 100;

    /** itemId�̃L���b�V���T�C�Y */
    private static final int DEF_ITEMID_CACHE_SIZE = 50000;

    /** MEASUREMENT_VALUE �e�[�u���� truncate ����R�[���o�b�N���\�b�h */
    private static final RotateCallback MEASUREMENT_ROTATE_CALLBACK = new RotateCallback() {
        /**
         * {@inheritDoc}
         */
        public String getTableType()
        {
            return "MEASUREMENT_VALUE";
        }

        /**
         * {@inheritDoc}
         */
        public void truncate(final String database, final int tableIndex, final int year)
            throws SQLException
        {
            MeasurementValueDao.truncate(database, tableIndex, year);
        }
    };

    /**
     * �f�t�H���g�R���X�g���N�^���B�����܂��B
     */
    private ResourceDataDaoUtil()
    {
        // Do nothing.
    }

    /**
     * �f�[�^��}������e�[�u���̖��O��Ԃ��܂��B
     *
     * @param date �}������f�[�^�̓��t
     * @param tableNameBase �e�[�u�����̃x�[�X
     * @return �e�[�u����
     */
    public static String getTableNameToInsert(final Date date, final String tableNameBase)
    {
        String tableName = null;
        if (DBManager.isDefaultDb())
        {
            tableName = tableNameBase;
        }
        else
        {
            // H2�ȊO�̃f�[�^�x�[�X�̏ꍇ�́A�p�[�e�B�V���j���O�������s��
            int weekOfYear = getTableIndexToInsert(date);
            tableName = getTableName(tableNameBase, weekOfYear);
        }
        return tableName;
    }

    /**
     * ���e�[�u���̖��O��Ԃ��܂��B
     *
     * @param tableNameBase �e�[�u�����̃x�[�X
     * @param index �e�[�u���C���f�b�N�X
     * @return ���e�[�u���̖��O
     */
    public static String getTableName(final String tableNameBase, final int index)
    {
        String tableName = String.format("%s_%02d", tableNameBase, Integer.valueOf(index));
        return tableName;
    }

    /**
     * �f�[�^��}������e�[�u���̃C���f�b�N�X��Ԃ��܂��B
     *
     * @param date �}������f�[�^�̓��t�i <code>null</code> �̏ꍇ�͌��݂̓��t�j
     * @return �e�[�u���C���f�b�N�X
     */
    public static int getTableIndexToInsert(final Date date)
    {
        Calendar calendar = Calendar.getInstance();
        if (date != null)
        {
            calendar.setTime(date);
        }
        int index = (calendar.get(Calendar.DAY_OF_YEAR) - 1) / DAY_OF_WEEK + 1;
        if (index > PARTITION_TABLE_COUNT)
        {
            index = PARTITION_TABLE_COUNT;
        }
        return index;
    }

    /**
     * {@link ResourceData} ���f�[�^�x�[�X�ɓo�^���܂��B<br />
     *
     * {@link ResourceData} ���ێ�����z�X�g��񂪌v���Ώۃz�X�g���e�[�u���ɑ��݂��Ȃ��ꍇ�́A
     * �f�[�^�x�[�X�ɓo�^�����A�G���[���O���o�͂��܂��B<br />
     *
     *�@�Y������v���l�̍��ځi�n��j�� Javelin �v�����ڃe�[�u���ɑ��݂��Ȃ��ꍇ�́A
     * �Y�����郌�R�[�h�� Javelin �v�����ڃe�[�u���ɑ}�����܂��B<br />
     *
     *�@�v���l��ʂ��v���l���e�[�u���ɑ��݂��Ȃ��ꍇ�́A
     * �Y�����郌�R�[�h���v���l���e�[�u���ɑ}�����܂��B<br />
     *
     * �}���Ώۂ̃e�[�u�����O��}��������ς�����ꍇ�A���[�e�[�g�������s���܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param resourceData �o�^����f�[�^
     * @param rotatePeriod ���[�e�[�g����
     * @param rotatePeriodUnit ���[�e�[�g���Ԃ̒P�ʁi Calendar �N���X�� DAY �܂��� MONTH �̒l�j
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void insert(final String database, final ResourceData resourceData,
            final int rotatePeriod, final int rotatePeriodUnit)
        throws SQLException
    {
        insert(database, resourceData, rotatePeriod, rotatePeriodUnit, DEF_BATCH_SIZE,
               DEF_ITEMID_CACHE_SIZE);
    }

    /**
     * {@link ResourceData} ���f�[�^�x�[�X�ɓo�^���܂��B<br />
     *
     * {@link ResourceData} ���ێ�����z�X�g��񂪌v���Ώۃz�X�g���e�[�u���ɑ��݂��Ȃ��ꍇ�́A
     * �f�[�^�x�[�X�ɓo�^�����A�G���[���O���o�͂��܂��B<br />
     *
     *�@�Y������v���l�̍��ځi�n��j�� Javelin �v�����ڃe�[�u���ɑ��݂��Ȃ��ꍇ�́A
     * �Y�����郌�R�[�h�� Javelin �v�����ڃe�[�u���ɑ}�����܂��B<br />
     *
     *�@�v���l��ʂ��v���l���e�[�u���ɑ��݂��Ȃ��ꍇ�́A
     * �Y�����郌�R�[�h���v���l���e�[�u���ɑ}�����܂��B<br />
     *
     * �}���Ώۂ̃e�[�u�����O��}��������ς�����ꍇ�A���[�e�[�g�������s���܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param resourceData �o�^����f�[�^
     * @param rotatePeriod ���[�e�[�g����
     * @param rotatePeriodUnit ���[�e�[�g���Ԃ̒P�ʁi Calendar �N���X�� DAY �܂��� MONTH �̒l�j
     * @param insertUnit insertUnit
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static void insert(final String database, final ResourceData resourceData,
            final int rotatePeriod, final int rotatePeriodUnit, final int insertUnit)
        throws SQLException
    {
        insert(database, resourceData, rotatePeriod, rotatePeriodUnit, insertUnit,
               DEF_ITEMID_CACHE_SIZE);
    }        
    
    /**
     * {@link ResourceData} ���f�[�^�x�[�X�ɓo�^���܂��B<br />
     *
     * {@link ResourceData} ���ێ�����z�X�g��񂪌v���Ώۃz�X�g���e�[�u���ɑ��݂��Ȃ��ꍇ�́A
     * �f�[�^�x�[�X�ɓo�^�����A�G���[���O���o�͂��܂��B<br />
     *
     *�@�Y������v���l�̍��ځi�n��j�� Javelin �v�����ڃe�[�u���ɑ��݂��Ȃ��ꍇ�́A
     * �Y�����郌�R�[�h�� Javelin �v�����ڃe�[�u���ɑ}�����܂��B<br />
     *
     *�@�v���l��ʂ��v���l���e�[�u���ɑ��݂��Ȃ��ꍇ�́A
     * �Y�����郌�R�[�h���v���l���e�[�u���ɑ}�����܂��B<br />
     *
     * �}���Ώۂ̃e�[�u�����O��}��������ς�����ꍇ�A���[�e�[�g�������s���܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param resourceData �o�^����f�[�^
     * @param rotatePeriod ���[�e�[�g����
     * @param rotatePeriodUnit ���[�e�[�g���Ԃ̒P�ʁi Calendar �N���X�� DAY �܂��� MONTH �̒l�j
     * @param batchUnit batchUnit
     * @param itemIdCacheSize itemIdCacheSize
     * @return �C���T�[�g����
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    public static InsertResult insert(final String database, final ResourceData resourceData,
        final int rotatePeriod, final int rotatePeriodUnit, final int batchUnit,
        final int itemIdCacheSize)
        throws SQLException
    {
        InsertResult result = new InsertResult();
        MeasurementValue baseMeasurementValue = new MeasurementValue();
        baseMeasurementValue.measurementTime = new Timestamp(resourceData.measurementTime);

        if (DBManager.isDefaultDb() == false)
        {
            // H2�ȊO�̃f�[�^�x�[�X�̏ꍇ�́A�p�[�e�B�V���j���O�������s��
            partitioning(database, rotatePeriod, rotatePeriodUnit, batchUnit,
					baseMeasurementValue);
        }
        Map<String, Integer> itemMap = measurementItemIdMap__.get(database);
        if (itemMap == null)
        {
            itemMap = loadMeasurementItemIdMap(database, itemIdCacheSize);
            measurementItemIdMap__.put(database, itemMap);
        }
        
        String prevTableName = null;
        
        Map<String, Timestamp> updateTargetMap = new HashMap<String, Timestamp>();
        Map<Integer, Timestamp> updatedMap = getUpdatedMap(database);
        List<MeasurementValue> updateValueList = new ArrayList<MeasurementValue>();
        for (MeasurementData measurementData : resourceData.getMeasurementMap().values())
        {
            for (MeasurementDetail detail : measurementData.getMeasurementDetailMap().values())
            {
                String measurementItemName;
                if (detail.itemName != null && detail.itemName.length() > 0)
                {
                    if (measurementData.itemName != null && measurementData.itemName.length() > 0)
                    {
                        measurementItemName = measurementData.itemName + "/" + detail.itemName;
                    }
                    else
                    {
                        measurementItemName = detail.itemName;
                    }
                }
                else
                {
                    measurementItemName = measurementData.itemName;
                }
                
                if (measurementItemName.startsWith("/") == false)
                {
                    measurementItemName = "/" + measurementItemName;
                }
                
                MeasurementValue measurementValue = new MeasurementValue();
                measurementValue.measurementTime = baseMeasurementValue.measurementTime;
                measurementValue.value  = baseMeasurementValue.value;
                
                int itemId;
                String itemMapKey = measurementItemName;
                itemId = getItemId(database, itemMap, detail, itemMapKey);
                if (itemId != -1)
                {
                    measurementValue.measurementItemId = itemId;
                }
                else
                {
                    result.setCacheMissCount(result.getCacheMissCount() + 1);
                    
                    // �n�� Javelin �v�����ڃe�[�u���ɓo�^����Ă��Ȃ��ꍇ�͒ǉ�����
                    measurementValue.measurementItemId =
                            insertJavelinMeasurementItem(database,
                                                         measurementItemName,
                                                         measurementValue.measurementTime);
                }

                int overflowCount =
                    addItemIdToCache(itemMap, itemMapKey, measurementValue, itemIdCacheSize);
                result.setCacheOverflowCount(result.getCacheOverflowCount() + overflowCount);
                measurementValue.value = detail.value;
                String tableName =
                    MeasurementValueDao.getTableNameToInsert(measurementValue.measurementTime);
                if ((prevTableName != null && prevTableName.endsWith(tableName) == false)
                    || updateValueList.size() > batchUnit)
                {
                    int insertCount =
                        MeasurementValueDao.insertBatch(database, prevTableName, updateValueList);
                    result.setInsertCount(result.getInsertCount() + insertCount);
                    updateValueList = new ArrayList<MeasurementValue>();
                }

                updateValueList.add(measurementValue);

                // �O��JAVELIN_MEASUREMENT_ITEM�e�[�u����LAST_INSERTED�t�B�[���h�X�V����
                // �����Ԃ��o�߂����ꍇ�ɁALAST_INSERTED�t�B�[���h���X�V����ΏۂɊ܂߂�B
                Timestamp beforeTime = updatedMap.get(measurementValue.measurementItemId);
                updatedMap.put(
                		measurementValue.measurementItemId,
                		measurementValue.measurementTime);
                if (beforeTime == null
                    || measurementValue.measurementTime.getTime() > beforeTime.getTime()
                        + ITEM_UPDATE_INTERVAL)
                {
                    updateTargetMap.put(measurementItemName, measurementValue.measurementTime);
                }
                prevTableName = tableName;
             }
        }
        
        if (prevTableName != null && updateValueList.size() > 0)
        {
            int insertCount =
                MeasurementValueDao.insertBatch(database, prevTableName, updateValueList);
            result.setInsertCount(result.getInsertCount()
                                  + insertCount);
        }

        // LAST_INSERTED�t�B�[���h���X�V����K�v������n��ɑ΂��čX�V�����{����B
        if (updateTargetMap.size() > 0)
        {
            JavelinMeasurementItemDao.updateLastInserted(database, updateTargetMap);
        }
        return result;
    }

	private static void partitioning(final String database,
			final int rotatePeriod, final int rotatePeriodUnit,
			final int batchUnit, MeasurementValue baseMeasurementValue)
			throws SQLException
	{
		Integer tableIndex =
		        getTableIndexToInsert(baseMeasurementValue.measurementTime);
		Integer prevTableIndex = prevTableIndexMap__.get(database);
		if (tableIndex.equals(prevTableIndex) == false)
		{
		    Timestamp[] range = MeasurementValueDao.getTerm(database);
		    if (range.length == 2
		            && (range[1] == null
		            		|| range[1].before(baseMeasurementValue.measurementTime)))
		    {
		        // �O��̑}���f�[�^�ƍ���̑}���f�[�^�ő}����e�[�u�����قȂ�ꍇ�ɁA���[�e�[�g�������s��
		        // �������A���ł�DB�ɓ����Ă���f�[�^�̂����A�ŐV�̃f�[�^�����Â��f�[�^�������Ă����ꍇ�̓��[�e�[�g�������Ȃ�
		        boolean truncateCurrent = (prevTableIndex != null);
		        rotateTable(database, tableIndex, baseMeasurementValue.measurementTime,
		                    rotatePeriod, rotatePeriodUnit, truncateCurrent,
		                    MEASUREMENT_ROTATE_CALLBACK);
		        prevTableIndexMap__.put(database, tableIndex);
		    }
		    deleteOldMeasurementItems(database,
		    		baseMeasurementValue.measurementTime, rotatePeriod,
		                              rotatePeriodUnit, batchUnit);
		}
	}

    private static LinkedHashMap<String, Integer> loadMeasurementItemIdMap(String database,
        int itemIdCacheSize)
        throws SQLException
    {
        LinkedHashMap<String, Integer> itemIdMap = new LinkedHashMap<String, Integer>();
        List<JavelinMeasurementItem> itemList =
            JavelinMeasurementItemDao.selectAll(database, itemIdCacheSize);
        for (JavelinMeasurementItem item : itemList)
        {
            itemIdMap.put(item.itemName, item.measurementItemId);
        }
        
        return itemIdMap;
    }

    private static int getItemId(final String database, Map<String, Integer> itemMap,
        MeasurementDetail detail, String itemMapKey)
        throws SQLException
    {
        synchronized (itemMap)
        {
            int itemId;
            if (itemMap.containsKey(itemMapKey))
            {
                itemId = itemMap.get(itemMapKey);
            }
            else
            {
                String displayName = detail.displayName;
                
                // ���łɌv�����ڂ��e�[�u���ɓo�^����Ă��邩�m�F����B
                // ��r����ۂ́A���s�𔼊p�X�y�[�X�ɕϊ�������ԂŔ�r����B
                displayName = displayName.replaceAll("\\r\\n", " ");
                displayName = displayName.replaceAll("\\r", " ");
                displayName = displayName.replaceAll("\\n", " ");
                
                itemId =
                    JavelinMeasurementItemDao.selectMeasurementItemIdFromItemName(database,
                                                                                  displayName);
            }
            return itemId;
        }
    }

    private static int addItemIdToCache(Map<String, Integer> itemMap, String itemMapKey,
        MeasurementValue measurementValue, final int itemIdCacheSize)
    {
        int overflowCount = 0;
        synchronized (itemMap)
        {
            itemMap.put(itemMapKey, measurementValue.measurementItemId);
            if (itemMap.size() > itemIdCacheSize)
            {
                overflowCount = 1;
                
                // �ŏ��̗v�f���폜����B
                Iterator<Entry<String, Integer>> iterator = itemMap.entrySet().iterator();
                if (iterator.hasNext())
                {
                    iterator.next();
                    iterator.remove();
                }
            }
        }
        return overflowCount;
    }
    
    /**
     * ���[�e�[�g�����{���܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param tableIndex �f�[�^���}�����ꂽ�e�[�u���C���f�b�N�X
     * @param date �}������f�[�^�̎���
     * @param rotatePeriod ���[�e�[�g����
     * @param rotatePeriodUnit ���[�e�[�g���Ԃ̒P�ʁiCalendar.DATE or Calendar.MONTH�j
     * @param truncateCurrent ���݂̃f�[�^�̑}���Ώۂ� truncate ����ꍇ�� <code>true</code>
     * @param rotateCallback truncate �������s���R�[���o�b�N���\�b�h
     * @throws SQLException truncate ���s���ɗ�O�����������ꍇ
     */
    public static void rotateTable(final String database, final Integer tableIndex,
            final Timestamp date, final int rotatePeriod, final int rotatePeriodUnit,
            final boolean truncateCurrent, final RotateCallback rotateCallback)
        throws SQLException
    {
        Calendar calendarToInsert = Calendar.getInstance();
        calendarToInsert.setTime(date);
        int yearToInsert = calendarToInsert.get(Calendar.YEAR);

        if (truncateCurrent)
        {
            rotateCallback.truncate(database, tableIndex, yearToInsert);
        }

        Calendar deleteTime = getBeforeDate(date, rotatePeriod, rotatePeriodUnit);
        // remainStartIndex�ȍ~�i���̒l���܂ށj�̃C���f�b�N�X�̃e�[�u����truncate���Ȃ�
        int remainStartIndex = getTableIndexToInsert(deleteTime.getTime());
        if (remainStartIndex <= tableIndex)
        {
            // �e�[�u���C���f�b�N�X���������ꍇ�A
            // ���[�e�[�g���Ԃ�1�T�Ԗ����̏ꍇ�Ɩ�1�N�̏ꍇ��2�p�^�[������B
            // ���ꂼ��ɂ���ď������قȂ�B
            // ���[�e�[�g���Ԃ�1�T�Ԗ����̏ꍇ�́A�}���e�[�u���ȊO�̃e�[�u����truncate���Ă悢���A
            // ���[�e�[�g���Ԃ���1�N�̏ꍇ�́A���̃e�[�u����truncate���Ă͂����Ȃ��B
            if (remainStartIndex < tableIndex
                    || isShorterThanOneWeek(rotatePeriod, rotatePeriodUnit))
            {
                // remainStartIndex���O�A��������tableIndex����̃e�[�u����truncate����
                for (int index = 1; index < remainStartIndex; index++)
                {
                    rotateCallback.truncate(database, index, yearToInsert + 1);
                }
                for (int index = tableIndex + 1; index <= PARTITION_TABLE_COUNT; index++)
                {
                    rotateCallback.truncate(database, index, yearToInsert);
                }
            }
        }
        else
        {
            for (int index = tableIndex + 1; index <= remainStartIndex - 1; index++)
            {
                rotateCallback.truncate(database, index, yearToInsert);
            }
        }

        if (LOGGER.isInfoEnabled())
        {
            LOGGER.log(LogMessageCodes.ROTATE_TABLE_PERFORMED, database,
                         rotateCallback.getTableType());
        }
    }

    /**
     * ���[�e�[�g���Ԃ�1�T�Ԗ������ǂ����𔻒肵�܂��B
     *
     * @param rotatePeriod ���[�e�[�g����
     * @param rotatePeriodUnit ���[�e�[�g���Ԃ̒P��
     * @return 1�T�ԁi7���j�����̏ꍇ�� <code>true</code> �A1�T�Ԉȏ�̏ꍇ�� <code>false</code>
     */
    private static boolean isShorterThanOneWeek(final int rotatePeriod, final int rotatePeriodUnit)
    {
        boolean shorter = false;
        if (rotatePeriodUnit == Calendar.MONTH)
        {
            shorter = (rotatePeriod == 0);
        }
        else if (rotatePeriodUnit == Calendar.DATE)
        {
            shorter = (rotatePeriod < DAY_OF_WEEK);
        }
        return shorter;
    }

    /**
     * �Â��n����iITEM�j���폜���܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param date �}������f�[�^�̎���
     * @param rotatePeriod ���[�e�[�g����
     * @param rotatePeriodUnit ���[�e�[�g���Ԃ̒P�ʁiCalendar.DATE or Calendar.MONTH�j
     */
    private static void deleteOldMeasurementItems(final String database, final Timestamp date,
        final int rotatePeriod, final int rotatePeriodUnit, final int insertUnit)
    {
        List<Integer> deleteIdList = new ArrayList<Integer>();
        
        long deleteTime = getBeforeDate(date, rotatePeriod, rotatePeriodUnit).getTimeInMillis();
        Map<Integer, Timestamp> updatedMap = getUpdatedMap(database);
        Iterator<Map.Entry<Integer, Timestamp>> iterator = updatedMap.entrySet().iterator();
        int removedItems = 0;
        while (iterator.hasNext())
        {
            Map.Entry<Integer, Timestamp> entry = iterator.next();
            if (entry.getValue().getTime() < deleteTime)
            {
                // �ŏI�X�V���������[�e�[�g���Ԃ��O�̏ꍇ�́A
                // ����MEASUREMENT_ITEM���폜����
                deleteIdList.add(entry.getKey());

                if (deleteIdList.size() >= insertUnit)
                {
                    try
                    {
                        JavelinMeasurementItemDao.deleteByMeasurementItemId(database, deleteIdList);
                        removedItems += deleteIdList.size();
                        deleteIdList.clear();
                    }
                    catch (SQLException ex)
                    {
                        // �폜�Ɏ��s�����ꍇ�͂܂�ITEM���g�p����Ă��邽�߁A
                        // Map������폜���Ȃ�
                    }
                }
            }
        }
        if (deleteIdList.size() > 0)
        {
            try
            {
                JavelinMeasurementItemDao.deleteByMeasurementItemId(database, deleteIdList);
                removedItems += deleteIdList.size();
                deleteIdList.clear();
            }
            catch (SQLException ex)
            {
                // �폜�Ɏ��s�����ꍇ�͂܂�ITEM���g�p����Ă��邽�߁A
                // Map������폜���Ȃ�
            }
        }

        
        if (LOGGER.isInfoEnabled())
        {
            LOGGER.log(LogMessageCodes.NO_NEEDED_SERIES_REMOVED, database,
                         Integer.valueOf(removedItems));
        }
    }

    /**
     * �w�肳�ꂽ��������A�w�肵�����Ԃ����O�̎�����Ԃ��܂��B
     *
     * @param baseTime ����
     * @param period ���ԁi���̒l�j
     * @param unit �P�ʁiCalendar�N���X�̃C���f�b�N�X�j
     * @return Calendar�C���X�^���X
     */
    private static Calendar getBeforeDate(final Timestamp baseTime,
                                          final int period,
                                          final int unit)
    {
        Calendar deleteTimeCalendar = Calendar.getInstance();
        deleteTimeCalendar.setTime(baseTime);
        deleteTimeCalendar.add(unit, -1 * period);
        return deleteTimeCalendar;
    }

    /**
     * Javelin �v�����ڃe�[�u���Ƀ��R�[�h��ǉ����܂��B<br />
     *
     * @param database �f�[�^�x�[�X��
     * @param measurementType �v���l���
     * @param itemName ���ږ���
     * @param lastInserted �v���f�[�^�ŏI�}������
     * @return �}���������R�[�h�̌v������ ID
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    private static int insertJavelinMeasurementItem(final String database,
                                                    final String itemName,
                                                    final Timestamp lastInserted)
        throws SQLException
    {
        JavelinMeasurementItem item = new JavelinMeasurementItem();
        item.itemName = itemName;
        item.lastInserted = lastInserted;
        JavelinMeasurementItemDao.insert(database, item);

        // �}���������R�[�h�̌v������ ID����������ہA�v�����ڂɉ��s�������Ă���ꍇ�́A
        // ���s�𔼊p�X�y�[�X�ɕύX���Ă��猟������
        String parsedItemName = itemName.replaceAll("\\r\\n", " ");
        parsedItemName = parsedItemName.replaceAll("\\r", " ");
        parsedItemName = parsedItemName.replaceAll("\\n", " ");
        int measurementItemId =
            JavelinMeasurementItemDao.selectMeasurementItemIdFromItemName(database, parsedItemName);

        return measurementItemId;
    }

    /**
     * �w�肳�ꂽ�f�[�^�x�[�X�ɑΉ�����A�n�񖈂̍ŏI�X�V�����̃}�b�v��Ԃ��܂��B<br />
     *
     * �����}�b�v�����݂��Ȃ��ꍇ�͐V���ɍ쐬���܂��B
     * ���̂Ƃ��A�f�[�^�x�[�X�ɑ��݂���n�񖈂̍ŏI�X�V���������擾���܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @return �n�񖈂̍ŏI�X�V�����̃}�b�v
     */
    private static Map<Integer, Timestamp> getUpdatedMap(String database)
    {
        Map<Integer, Timestamp> updatedMap = measurementItemUpdatedMap__.get(database);
        if (updatedMap == null)
        {
            updatedMap = new ConcurrentHashMap<Integer, Timestamp>();
            measurementItemUpdatedMap__.put(database, updatedMap);

            // �f�[�^�x�[�X�ɑ��݂���n�񖈂̍ŏI�X�V���������擾����
            try
            {
                List<JavelinMeasurementItem> itemList =
                    JavelinMeasurementItemDao.selectAll(database);
                for (JavelinMeasurementItem item : itemList)
                {
                    updatedMap.put(item.measurementItemId, item.lastInserted);
                }
            }
            catch (SQLException ex)
            {
                LOGGER.log(LogMessageCodes.DB_ACCESS_ERROR, database, ex.getMessage());
            }
        }
        return updatedMap;
    }
}
