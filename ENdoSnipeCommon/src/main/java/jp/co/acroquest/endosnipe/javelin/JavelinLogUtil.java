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
package jp.co.acroquest.endosnipe.javelin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;

/**
 * JavelinLogElement�𑀍삷�郆�[�e�B���e�B�B
 * 
 * @author eriguchi
 * 
 */
public class JavelinLogUtil
{
    /**
     * �C���X�^���X����j�~����v���C�x�[�g�R���X�g���N�^�B
     */
    private JavelinLogUtil()
    {
        // Do Nothing.
    }

    /**
     * JavleinLogElement����������擾����B
     * 
     * @param javelinLogElement
     *            Javelin���O�̗v�f�B
     * @return �����B
     */
    public static String[] getArgs(final JavelinLogElement javelinLogElement)
    {
        if (javelinLogElement == null)
        {
            return new String[0];
        }

        String[] args = javelinLogElement.getArgs();
        if (args == null)
        {
            String argsStr = javelinLogElement
                    .getDetailInfo(JavelinParser.TAG_TYPE_ARGS);
            args = extractArgs(argsStr);
            javelinLogElement.setArgs(args);
        }
        return args;
    }

    private static String[] extractArgs(final String argsStr)
    {
        if (argsStr == null)
        {
            return new String[0];
        }

        int argsTempLen = "args[0] = ".length();
        if (argsTempLen >= argsStr.length())
        {
            return new String[0];
        }

        String argsTemp = argsStr.substring(argsTempLen);
        String[] args = argsTemp.split("args\\[[0-9]+\\] = ");
        return args;
    }

    /**
     * JavelinLogElement�����̕����񂩂�������擾����B
     * 
     * @param logElementString
     *            JavelinLogElement�����̕�����B
     * @return �����B
     */
    public static String[] getArgs(final String logElementString)
    {
        String argsStart = JavelinParser.DETAIL_TAG_PREFIX
                + JavelinParser.TAG_TYPE_ARGS
                + JavelinParser.DETAIL_TAG_START_END_STR;
        int argsStartIndex = logElementString.indexOf(argsStart);
        if (argsStartIndex < 0)
        {
            return new String[0];
        }
        int argsContentStartIndex = argsStartIndex + argsStart.length();
        String argsEnd = JavelinParser.DETAIL_TAG_PREFIX
                + JavelinParser.TAG_TYPE_ARGS;
        int argsContentEndIndex = logElementString.indexOf(argsEnd,
                argsContentStartIndex);
        if (argsContentEndIndex <= argsContentStartIndex)
        {
            return new String[0];
        }
        String argsStr = logElementString.substring(argsContentStartIndex,
                argsContentEndIndex).trim();
        String[] args = extractArgs(argsStr);
        return args;
    }

    /**
     * �ڍ׈�������^�O����菜�������e�̕������擾����B<br />
     * �y��z<br />
     * �ڍ׈����� <code>"[Time] xxxx"</code> �ƂȂ��Ă���ꍇ�A<code>arg</code> ��
     * <code>[Time]</code> ���w�肵�Ė{���\�b�h���Ăяo���ƁA<code>xxx</code> �̕������Ԃ����B
     * 
     * @param arg
     *            ����
     * @param argName
     *            �^�O����
     * @return ���e�����B�^�O�����݂��Ȃ��ꍇ�� <code>null</code>
     */
    public static String getArgContent(final String arg, final String argName)
    {
        if (arg.startsWith(argName))
        {
            return arg.substring(argName.length() + 1, arg.length()).trim();
        }
        else
        {
            return null;
        }
    }

    private static final String LINE_SEPERATOR = System
            .getProperty("line.separator");

    /**
     * JavelinLogElement �Ɏw�肳�ꂽ�^�O�����݂��邩�`�F�b�N���܂��B
     * 
     * @param javelinLogElement
     *            Javelin���O�̗v�f
     * @param tagName
     *            ���݃`�F�b�N����^�O
     * @return ���݂���ꍇ�� <code>true</code> �A���݂��Ȃ��ꍇ�� <code>false</code>
     */
    public static boolean isExistTag(final JavelinLogElement javelinLogElement,
            final String tagName)
    {
        if (javelinLogElement != null)
        {
            if (javelinLogElement.getDetailInfo(tagName) != null)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * JavleinLogElement����JMX����ێ������}�b�v���擾����B
     * 
     * @param javelinLogElement
     *            Javelin���O�̗v�f�B
     * @param paramName
     *            �p�����[�^��
     * 
     * @return �}�b�v
     */
    public static Map<String, String> parseDetailInfo(
            final JavelinLogElement javelinLogElement, final String paramName)
    {
        if (javelinLogElement == null)
        {
            return new HashMap<String, String>();
        }

        String jmxInfoStr = javelinLogElement.getDetailInfo(paramName);
        if (jmxInfoStr == null)
        {
            return new HashMap<String, String>();
        }

        Map<String, String> map = createJmxInfoMap(jmxInfoStr);

        return map;
    }

    /**
     * �v���p�e�B�`���ŋL�q���ꂽ�����񂩂�A�v���p�e�B�̃L�[�ƒl�ɕ��������}�b�v���쐬���܂��B<br />
     * 
     * @param propStr
     *            �v���p�e�B�`���ŋL�q���ꂽ������
     * @return �v���p�e�B�̃L�[�ƒl�ɕ��������}�b�v
     */
    public static Map<String, String> createJmxInfoMap(final String propStr)
    {
        Map<String, String> map = new LinkedHashMap<String, String>();
        String now = propStr;
        int equalPos = propStr.indexOf('=');
        int retPos = propStr.indexOf(LINE_SEPERATOR);

        while (now != null && now.equals("") == false && equalPos != -1)
        {
            int nextEqualPos = now.indexOf('=', retPos + 1);
            int nextRetPos = now.indexOf(LINE_SEPERATOR, retPos + 1);
            if ((retPos < nextEqualPos && nextEqualPos < nextRetPos)
                    || nextRetPos == -1 || nextEqualPos == -1)
            {
                String keyValue = "";
                if (nextEqualPos == -1 || retPos == -1)
                {
                    keyValue = now.trim();
                }
                else
                {
                    keyValue = now.substring(0, retPos);
                }
                if (equalPos > 0 && keyValue.length() >= equalPos + 2)
                {
                    String key = keyValue.substring(0, equalPos - 1);
                    String value = keyValue.substring(equalPos + 2);
                    map.put(key, value);
                }
                if (retPos == -1)
                {
                    break;
                }
                now = now.substring(retPos + LINE_SEPERATOR.length());
                equalPos = now.indexOf('=');
                retPos = now.indexOf(LINE_SEPERATOR);
            }
            else
            {
                retPos = nextRetPos;
            }
        }
        return map;
    }
}
