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
package jp.co.acroquest.endosnipe.javelin.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;

/**
 * �p�����[�^�ڍ׉����s���N���X�B
 * 
 * @author kato
 */
public class DetailStringBuilder
{
    private static final int DEFAULT_DEPTH = 1;

    private static final String VALUE_HEADER = "[";

    private static final String VALUE_FOOTER = "]";

    private static final String KEY_VALUE_SEPARATOR = " = ";

    private static final String VALUE_SEPARATOR = " , ";

    private static final String DEFAULT_VALUE = "????";

    private static final String NULL_VALUE = "null";

    private static final String INNER_CLASS_SEPARATOR_CHAR = "$";

    private static final String OBJECT_ID_SEPARATOR = ":";

    private static final String CLASS_NAME_SEPARATOR = "@";

    // ���o�͑ΏۂƂ���N���X�Q
    private static final Set<String> PRINT_CLASS_SET = new HashSet<String>();
    static
    {
        PRINT_CLASS_SET.add("Short");
        PRINT_CLASS_SET.add("Integer");
        PRINT_CLASS_SET.add("Long");
        PRINT_CLASS_SET.add("String");
        PRINT_CLASS_SET.add("Boolean");
        PRINT_CLASS_SET.add("Byte");
        PRINT_CLASS_SET.add("Character");
        PRINT_CLASS_SET.add("Float");
        PRINT_CLASS_SET.add("Double");
    }

    private DetailStringBuilder()
    {

    }

    /**
     * Object�̏��o�͂��s�� 
     * ���͂��ꂽ�[�x���ɂ��킹�A�t�B�[���h��H�邩���̏�ŏo�͂��邩���肷��
     * 
     * @param object �o�͑ΏۃI�u�W�F�N�g
     * @return �o�͌���
     */
    public static String buildDetailString(final Object object)
    {
        // �o�͐[�x1�ŌĂяo��
        String detailString = buildDetailString(object, DEFAULT_DEPTH);

        return detailString;
    }

    /**
     * Object�̏��o�͂��s�� 
     * ���͂��ꂽ�[�x���ɂ��킹�A�t�B�[���h��H�邩���̏�ŏo�͂��邩���肷��
     * 
     * @param object �o�͑ΏۃI�u�W�F�N�g
     * @param detailDepth �ݒ�[�x
     * @return �o�͌���
     */
    protected static String buildDetailString(final Object object, final int detailDepth)
    {
        // null�̏ꍇ��"null"�Əo�͂���B
        if (object == null)
        {
            return NULL_VALUE;
        }

        // String�̏ꍇ�͂��̂܂܏o�͂���B
        if (object instanceof String)
        {
            return (String)object;
        }

        //�擪�Ƀw�b�_�����������
        String detailString = toDetailString(object, detailDepth, 0);
        StringBuilder detailBuilder = new StringBuilder();
        detailBuilder.append(object.getClass().getName());
        detailBuilder.append(CLASS_NAME_SEPARATOR);
        detailBuilder.append(Integer.toHexString(System.identityHashCode(object)));
        detailBuilder.append(OBJECT_ID_SEPARATOR);
        detailBuilder.append(detailString);
        return detailBuilder.toString();

    }

    /**
     * Object�̏ڍו����񉻂��s�� 
     * ���͂��ꂽ�[�x���ɂ��킹�A�t�B�[���h��H�邩���̏�ŏo�͂��邩���肷��
     * 
     * @param object �o�͑ΏۃI�u�W�F�N�g
     * @param detailDepth  �o�͐[�x
     * @param currentDepth ���ݐ[�x
     * @return �o�͌���
     */
    protected static String toDetailString(final Object object, final int detailDepth,
            final int currentDepth)
    {
        // ���݂̊K�w�̐[�����ݒ�l�ȏ�̏ꍇ�AtoString�̌��ʂ�Ԃ�
        if (currentDepth >= detailDepth)
        {
            return buildString(object);
        }

        // ���o�͑ΏۂƂȂ�I�u�W�F�N�g�̏ꍇ�AtoString�̌��ʂ�Ԃ�
        if (isPrintable(object))
        {
            return buildString(object);
        }

        Class<?> clazz = object.getClass();
        // �z��̏ꍇ�A�z���p�����ŏo�͂��s��
        if (clazz.isArray())
        {
            return toStringArrayObject(object, detailDepth, currentDepth);
        }
        // �R���N�V�����̏ꍇ�A�R���N�V������p�����ŏo�͂��s��
        if (object instanceof Collection)
        {
            Collection<?> collectionObject = (Collection<?>)object;
            return toStringCollectionObject(collectionObject, detailDepth, currentDepth);
        }
        // Map�̏ꍇ�AMap��p�����ŏo�͂��s��
        if (object instanceof Map<?, ?>)
        {
            Map<?, ?> mapObject = (Map<?, ?>)object;
            return toStringMapObject(mapObject, detailDepth, currentDepth);
        }

        Field[] fields = clazz.getDeclaredFields();
        try
        {
            AccessibleObject.setAccessible(fields, true);
        }
        catch (SecurityException scex)
        {
            return buildString(object);
        }

        StringBuilder builder = new StringBuilder();
        builder.append(VALUE_HEADER);
        boolean separatorFlag = false;
        for (int i = 0; i < fields.length; i++)
        {
            Field field = fields[i];

            boolean printableFlag = isPrintable(field);

            if (printableFlag)
            {
                if (separatorFlag)
                {
                    builder.append(VALUE_SEPARATOR);
                }
                String fieldName = field.getName();
                builder.append(fieldName).append(KEY_VALUE_SEPARATOR);

                Object fieldValue = null;
                try
                {
                    fieldValue = field.get(object);
                    builder.append(toDetailString(fieldValue, detailDepth, currentDepth + 1));

                }
                // �G���[�����������ꍇ�̓f�t�H���g������Ƃ���
                catch (IllegalAccessException iaex)
                {
                    builder.append(DEFAULT_VALUE);
                }
                separatorFlag = true;
            }
        }
        builder.append(VALUE_FOOTER);
        return builder.toString();
    }

    /**
     * Map�̕�����o�͂��s���B
     * [key1 = value1 , key2 = value2 ..... keyn = valuen]�̌`���ŏo�͂���
     * 
     * @param mapObject �ΏۂƂȂ�Map
     * @param detailDepth �o�͐[�x
     * @param currentDepth ���ݐ[�x
     * @return Map�̕�����\��
     */
    protected static String toStringMapObject(final Map<?, ?> mapObject, final int detailDepth,
            final int currentDepth)
    {
        // �z��null�̎���"null"��Ԃ��B
        if (mapObject == null)
        {
            return NULL_VALUE;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(VALUE_HEADER);

        Object[] keys = mapObject.keySet().toArray();
        int length = keys.length;

        for (int i = 0; i < length; i++)
        {
            if (i > 0)
            {
                builder.append(VALUE_SEPARATOR);
            }

            Object item = mapObject.get(keys[i]);
            builder.append(buildString(keys[i])).append(KEY_VALUE_SEPARATOR);

            if (item == null)
            {
                builder.append(NULL_VALUE);
            }
            else
            {
                builder.append(toDetailString(item, detailDepth, currentDepth + 1));
            }
        }

        builder.append(VALUE_FOOTER);
        return builder.toString();
    }

    /**
     * �z��I�u�W�F�N�g�̃��O�o�͂��s��
     * 
     * @param array �o�͑Ώہi�z��j
     * @param detailDepth �o�͐[�x
     * @param currentDepth ���ݐ[�x
     * @return �o�͕�����
     */
    protected static String toStringArrayObject(final Object array, final int detailDepth,
            final int currentDepth)
    {
        // �z��null�̎���"null"��Ԃ��B
        if (array == null)
        {
            return NULL_VALUE;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(VALUE_HEADER);
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++)
        {
            Object item = Array.get(array, i);
            if (i > 0)
            {
                builder.append(VALUE_SEPARATOR);
            }

            if (item == null)
            {
                builder.append(NULL_VALUE);
            }
            else
            {
                builder.append(toDetailString(item, detailDepth, currentDepth + 1));
            }
        }
        builder.append(VALUE_FOOTER);
        return builder.toString();
    }

    /**
     * �R���N�V�����I�u�W�F�N�g�̃��O�o�͂��s��
     * 
     * @param collection �o�͑Ώہi�z��j
     * @param detailDepth �o�͐[�x
     * @param currentDepth ���ݐ[�x
     * @return �o�͕�����
     */
    protected static String toStringCollectionObject(final Collection<?> collection,
            final int detailDepth, final int currentDepth)
    {
        // �R���N�V������null�̎���"null"��Ԃ��B
        if (collection == null)
        {
            return NULL_VALUE;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(VALUE_HEADER);

        boolean separatorFlag = false;
        for (Object item : collection)
        {
            if (separatorFlag)
            {
                builder.append(VALUE_SEPARATOR);
            }

            if (item == null)
            {
                builder.append(NULL_VALUE);
            }
            else
            {
                builder.append(toDetailString(item, detailDepth, currentDepth + 1));
            }
            separatorFlag = true;
        }

        builder.append(VALUE_FOOTER);
        return builder.toString();
    }

    /**
     * ToString�̌��ʂ�Ԃ�
     * 
     * @param object �ϊ��Ώ�
     * @return ToString�̌���
     */
    public static String buildString(final Object object)
    {
        // �I�u�W�F�N�g��null�̎���"null"��Ԃ��B
        if (object == null)
        {
            return NULL_VALUE;
        }

        // toString�͗�O�𔭐������邱�Ƃ����邽�߁A��������
        // "????"�Ƃ����������Ԃ��悤�ɂ���B
        try
        {
            return object.toString();
        }
        catch (Throwable th)
        {
            return DEFAULT_VALUE;
        }
    }

    /**
     * �o�͑Ώۂ̃I�u�W�F�N�g��������s��
     * 
     * @param object ����Ώ�
     * @return ���茋��
     */
    protected static boolean isPrintable(final Object object)
    {
        if (object == null)
        {
            return true;
        }

        // Field�I�u�W�F�N�g�̏ꍇ�A�C���i�[�N���X�Atransient�t�B�[���h�Astatic�t�B�[���h
        // �̎��ɂ͏o�͂��s��Ȃ��B
        if (object instanceof Field)
        {
            Field field = (Field)object;
            if (field.getType().getName().indexOf(INNER_CLASS_SEPARATOR_CHAR) != -1)
            {
                return false;
            }
            if (Modifier.isTransient(field.getModifiers()))
            {
                return false;
            }
            if (Modifier.isStatic(field.getModifiers()))
            {
                return false;
            }
            return true;
        }

        // Field�I�u�W�F�N�g�ł͂Ȃ��ꍇ�A�v���~�e�B�u�^�܂��̓v���~�e�B�u�^�̃��b�p�[�N���X
        // �̎��ɂ͑��o�͑ΏۂƂ���B
        Class<?> clazz = object.getClass();

        if (clazz.isPrimitive())
        {
            return true;
        }

        try
        {
            String className = clazz.getSimpleName();
            if (PRINT_CLASS_SET.contains(className))
            {
                return true;
            }
        }
        catch (Exception ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        return false;
    }

}
