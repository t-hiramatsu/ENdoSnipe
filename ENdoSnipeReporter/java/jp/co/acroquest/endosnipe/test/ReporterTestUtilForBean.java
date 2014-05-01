package jp.co.acroquest.endosnipe.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.locale.converters.SqlTimestampLocaleConverter;

public class ReporterTestUtilForBean 
{
	
	
	
	/**
	 * JavaBeanとして定義したエンティティクラスのインスタンスリストを生成する。
	 * 生成するインスタンスには、パラメータに指定されたデータを設定する。
	 * 
	 * @param clazz     生成するインスタンス
	 * @param fieldList データを設定するフィールド
	 * @param datarows  設定するデータ(CSV形式)
	 * @return データを設定したインスタンスリスト
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static List<? extends Object> createEntities(
		Class<? extends Object> clazz, String[] fieldList, String[] datarows) 
		throws InstantiationException, IllegalAccessException, InvocationTargetException
	{
		List<Object> resultList = new ArrayList<Object>();
		
		for(String datarow : datarows)
		{
			Object entity = createEntity(clazz, fieldList, datarow);
			resultList.add(entity);
		}
		
		return resultList;
	}
	
	/**
	 * JavaBeanとして定義したエンティティクラスのインスタンスを生成する。
	 * 生成するインスタンスには、パラメータに指定されたデータを設定する。
	 * 
	 * @param clazz     生成するインスタンス
	 * @param fieldList データを設定するフィールド
	 * @param datarow   設定するデータ(CSV形式)
	 * @return データを設定したインスタンス
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object createEntity(
		Class<? extends Object> clazz, String[] fieldList, String datarow) 
		throws InstantiationException, IllegalAccessException, InvocationTargetException
	{
		SqlTimestampLocaleConverter converter 
			= new SqlTimestampLocaleConverter(Locale.JAPANESE, "yyyy/MM/dd HH:mm:ss");
		
		ConvertUtils.register(converter, Timestamp.class);
		
		Object instance = clazz.newInstance();
        String[] dataElements = datarow.split(",");
        
        
        for(int cnt = 0; cnt < fieldList.length; cnt++)
        {
        	BeanUtils.setProperty(instance, fieldList[cnt], dataElements[cnt]);
        }
        
        ConvertUtils.deregister(Timestamp.class);
        
        return instance;
	}
	
	/**
	 * リストの要素数、およびリスト内のエンティティが全て同等のエンティティか判定する。
	 * 
     * @param expect  　　　　予測値
     * @param actual 　　　　 実際値
     * @param checkTarget チェック対象のフィールドリスト
     * @param delta   　　　　　等価とみなす浮動小数点の誤差範囲
	 */
	public static void assertEntitiesEquals(
		List<? extends Object> expect, 
		List<? extends Object> actual,
		String[]               checkTarget,
		double                 delta) 
	{
		Assert.assertEquals(expect.size(), actual.size());
		
		for(int cnt = 0; cnt < expect.size(); cnt ++)
		{
			assertEntityEquals(
				expect.get(cnt), actual.get(cnt), checkTarget, delta);
		}
	}
	
    /**
     * 各エンティティ同士のフィールドが等しいか否かを判定する。
     * 
     * @param expect  　　　　予測値
     * @param actual 　　　　 実際値
     * @param checkTarget チェック対象のフィールドリスト
     * @param delta   　　　　　等価とみなす浮動小数点の誤差範囲
     */
    public static void assertEntityEquals(
    	Object expect, Object actual, String[] checkTarget, double delta)
    {
        if (expect == null || actual == null)
        {
            Assert.fail("expect or actual is NULL !!");
        }

        Assert.assertEquals(expect.getClass(), actual.getClass());

        for (String target : checkTarget)
        {
        	Object expectField;
			Object actualField;
			try 
			{
				expectField = PropertyUtils.getProperty(expect, target);
				actualField = PropertyUtils.getProperty(actual, target);
			} 
			catch (Exception e) 
			{
				throw new RuntimeException(e);
			}
        	
        	Class<?> fieldType = actualField.getClass();
        	
        	if(Float.class.equals(fieldType) || Float.TYPE.equals(fieldType))
        	{
        		Assert.assertEquals(((Float)expectField).floatValue(),
        				            ((Float)actualField).floatValue(),
        				            (float)delta);
        	}
        	else if(Double.class.equals(fieldType) || Double.TYPE.equals(fieldType))
        	{
        		Assert.assertEquals(((Double)expectField).doubleValue() ,
    			                    ((Double)actualField).doubleValue(),
              			            delta);
        	}
        	else
        	{
            	Assert.assertEquals(expectField, actualField);
        	}
        }
    }
}
