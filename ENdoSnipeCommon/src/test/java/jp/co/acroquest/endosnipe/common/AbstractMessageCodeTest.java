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
package jp.co.acroquest.endosnipe.common;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import junit.framework.TestCase;

import org.seasar.framework.message.MessageFormatter;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.StringUtil;

/**
 * ���b�Z�[�W�R�[�h�p�萔�N���X�ƃv���p�e�B�t�@�C���̑Ή����`�F�b�N����
 * �e�X�g�N���X�̂��߂̊��N���X�ł��B<br />
 * 
 * @author y-komori
 */
public abstract class AbstractMessageCodeTest extends TestCase
{
    /** �e�X�g�ΏۃN���X */
    private Class<?> messageCodeClass_;

    /** �e�X�g�Ώۃo���h���� */
    private String resourceBundleName_;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp()
        throws Exception
    {
        String testClassName = getClass().getName();
        String targetClassName =
                testClassName.substring(0, testClassName.length() - "Test".length());
        messageCodeClass_ = Class.forName(targetClassName);
        resourceBundleName_ = getResourceBundleName();
    }

    /**
     * �萔�N���X�ɒ�`����Ă���萔�ɑΉ����郁�b�Z�[�W���v���p�e�B�t�@�C��
     * �ɓo�^����Ă��邩�ǂ������e�X�g���܂��B<br />
     */
    public void testConstants()
    {
        Field[] fields = messageCodeClass_.getDeclaredFields();
        for (Field field : fields)
        {
            if (field.getDeclaringClass() == messageCodeClass_)
            {
                String code = FieldUtil.getString(field);
                String message = MessageFormatter.getSimpleMessage(code, null);
                assertFalse(code + " is not found in " + resourceBundleName_ + ".",
                            StringUtil.isEmpty(message));
            }
        }
    }

    /**
     * �v���p�e�B�t�@�C���ɓo�^����Ă���L�[�����ׂĒ萔�N���X��
     * �ɓo�^����Ă��邩�ǂ������e�X�g���܂��B<br />
     */
    public void testMessages()
    {
        Map<String, String> constantMap = new HashMap<String, String>();

        Field[] fields = messageCodeClass_.getDeclaredFields();
        for (Field field : fields)
        {
            constantMap.put(FieldUtil.getString(field), field.getName());
        }

        ResourceBundle bundle = ResourceBundle.getBundle(resourceBundleName_);
        assertNotNull("1", bundle);

        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements())
        {
            String key = keys.nextElement();
            assertTrue(key + " is not found in " + messageCodeClass_.getName(),
                       constantMap.containsKey(key));
        }
    }

    /**
     * �e�X�g�Ώۂ̃��\�[�X�o���h������Ԃ��܂��B<br />
     * 
     * @return �e�X�g�Ώۂ̃��\�[�X�o���h����
     */
    abstract protected String getResourceBundleName();
}
