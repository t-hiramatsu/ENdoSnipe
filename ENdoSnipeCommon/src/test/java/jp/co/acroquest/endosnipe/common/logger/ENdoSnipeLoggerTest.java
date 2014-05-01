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
package jp.co.acroquest.endosnipe.common.logger;

import junit.framework.TestCase;

public class ENdoSnipeLoggerTest extends TestCase
{
    public void testLog()
    {
        ENdoSnipeLogger logger = ENdoSnipeLogger.getLogger(ENdoSnipeLoggerTest.class);
        logger.error("ErrorLogTest");
    }

    /**
     * ENdoSnipeLogger#createMessage(Object)���\�b�h�̃e�X�g�P�[�X�ł��B<br />
     * ������null���w�肵���ꍇ�A�\�����ʃG���[�����������Ƃ������b�Z�[�W���Ԃ���鎖���m�F���܂��B<br />
     */
    public void testCreateMessage_MessageIsNull()
    {
        // ����
        ENdoSnipeLogger logger = ENdoSnipeLogger.getLogger(ENdoSnipeLoggerTest.class);

        // ���{�E����
        try
        {
            String result = logger.createMessage(null);
            assertEquals(result, "[EECM0005]�\�����ʃG���[���������܂���.");
        }
        catch (Exception ex)
        {
            assertTrue(false);
        }
    }

    /**
     * ENdoSnipeLogger#createMessage(Object)���\�b�h�̃e�X�g�P�[�X�ł��B<br />
     * �����ɋ󕶎����w�肵���ꍇ�A""���Ԃ���鎖���m�F���܂��B<br />
     */
    public void testCreateMessage_MessageIsBlank()
    {
        // ����
        ENdoSnipeLogger logger = ENdoSnipeLogger.getLogger(ENdoSnipeLoggerTest.class);

        // ���{�E����
        try
        {
            String result = logger.createMessage("");
            assertEquals(result, "");
        }
        catch (Exception ex)
        {
            assertTrue(false);
        }
    }

    /**
     * ENdoSnipeLogger#createMessage(Object)���\�b�h�̃e�X�g�P�[�X�ł��B<br />
     * ������String�^���w�肵���ꍇ�A���̕����񂪂��̂܂ܕԂ���鎖���m�F���܂��B<br />
     */
    public void testCreateMessage_MessageIsNotNull()
    {
        // ����
        ENdoSnipeLogger logger = ENdoSnipeLogger.getLogger(ENdoSnipeLoggerTest.class);

        // ���{�E����
        try
        {
            String result = logger.createMessage("String");
            assertEquals(result, "String");
        }
        catch (Exception ex)
        {
            assertTrue(false);
        }
    }

    /**
     * ENdoSnipeLogger#createMessage(Object)���\�b�h�̃e�X�g�P�[�X�ł��B<br />
     * ������Object�^���w�肵���ꍇ�A����toString()�̌��ʂ��Ԃ���鎖���m�F���܂��B<br />
     */
    public void testCreateMessage_Object()
    {
        // ����
        ENdoSnipeLogger logger = ENdoSnipeLogger.getLogger(ENdoSnipeLoggerTest.class);
        Object messageObject = new Object() {
            @Override
            public String toString()
            {
                return "Object#toString() is Called.";
            }
        };

        // ���{�E����
        try
        {
            String result = logger.createMessage(messageObject);
            assertEquals(result, "Object#toString() is Called.");
        }
        catch (Exception ex)
        {
            assertTrue(false);
        }
    }
}
