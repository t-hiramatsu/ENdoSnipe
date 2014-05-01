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

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;

/**
 * ���\���Ɋւ��郆�[�e�B���e�B�N���X�ł��B<br />
 * 
 * @author eriguchi
 */
public class StatsUtil
{
    /** �ϊ��t�H�[�}�b�g */
    private static final int EXCHANGE_FORMAT = 0xFF;
    
    /** �J�E���g�̍ő吔 */
    private static final int MAX_COUNT = 8;
    
    /**
     * �R���X�g���N�^
     */
    private StatsUtil()
    {
    }

    /**
     * �X���b�h�����ʂ��邽�߂̕�������o�͂���B <br>
     * �t�H�[�}�b�g�F�X���b�h��@�X���b�h�N���X��@�X���b�h�I�u�W�F�N�g��ID<br>
     * 
     * @param currentThread �A�N�Z�X���̃X���b�h
     * @return �X���b�h�����ʂ��邽�߂̕�����
     */
    public static String createThreadIDText(final Thread currentThread)
    {
        StringBuilder threadId = new StringBuilder();
        threadId.append(currentThread.getName());
        threadId.append("@");
        threadId.append(ThreadUtil.getThreadId());
        threadId.append("(" + currentThread.getClass().getName());
        threadId.append("@");
        threadId.append(StatsUtil.getObjectID(currentThread));
        threadId.append(")");

        return threadId.toString();
    }

    /**
     * �X���b�h�����ʂ��邽�߂̕�������o�͂���B 
     * �t�H�[�}�b�g�F�X���b�h��@�X���b�h�N���X��@�X���b�h�I�u�W�F�N�g��ID
     * 
     * @return �X���b�h�����ʂ��邽�߂̕�����
     */
    public static String createThreadIDText()
    {
        Thread currentThread = Thread.currentThread();
        return createThreadIDText(currentThread);
    }

    /**
     * �I�u�W�F�N�gID��16�i�`���̕�����Ƃ��Ď擾����B
     * 
     * @param object �I�u�W�F�N�gID���擾�I�u�W�F�N�g�B
     * @return �I�u�W�F�N�gID�B
     */
    public static String getObjectID(final Object object)
    {
        // ������null�̏ꍇ��"null"��Ԃ��B
        if (object == null)
        {
            return "null";
        }

        return Integer.toHexString(System.identityHashCode(object));
    }

    /**
     * �I�u�W�F�N�g�̎��ʎq���쐬����B
     * @param obj �I�u�W�F�N�g
     * @return �I�u�W�F�N�g�̎��ʎq
     */
    public static String createIdentifier(final Object obj)
    {
        return obj.getClass().getName() + "@" + StatsUtil.getObjectID(obj);
    }

    /**
     * object��toString�ŕ�����ɕϊ�����B
     * 
     * toString�ŗ�O�����������ꍇ�́A
     * �W���G���[�o�͂�object�̃N���X���ƃX�^�b�N�g���[�X���o�͂��A
     * �N���X��@�I�u�W�F�N�gID��Ԃ��B
     * 
     * @param object �I�u�W�F�N�g
     * @return toString��object�𕶎��񉻂������́B
     */
    public static String toStr(final Object object)
    {
        // ������null�̏ꍇ��"null"��Ԃ��B
        if (object == null)
        {
            return "null";
        }

        String result;
        try
        {
            result = object.toString();
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().debug(
                                             "Javelin Exception" + object.getClass().toString()
                                                     + "#toString(): ", th);
            result = object.getClass().toString() + "@" + StatsUtil.getObjectID(object);
        }
        return result;
    }

    /**
     * object��toString�ŕ�����ɕϊ��A�w�蒷�Ő؂�B
     * 
     * toString�ŗ�O�����������ꍇ�́A
     * �W���G���[�o�͂�object�̃N���X���ƃX�^�b�N�g���[�X���o�͂��A
     * �N���X��@�I�u�W�F�N�gID��Ԃ��B
     * �w�蒷�𒴂��Ă���ꍇ�͎w�蒷�Ő؂�A"..."��t�^����B
     * 
     * @param object �����񉻑ΏۃI�u�W�F�N�g
     * @param length ������w�蒷(���̒l���w�肳�ꂽ�ꍇ�͎w�蒷�Ő؂鏈�����s��Ȃ�)
     * @return toString�ŕ�����ɕϊ����A�w�蒷�Ő؂������́B
     */
    public static String toStr(final Object object, final int length)
    {
        // ������null�̏ꍇ��"null"��Ԃ��B
        if (object == null)
        {
            return "null";
        }

        String result;
        try
        {
            result = object.toString();
            if (length == 0)
            {
                result = "";
            }
            else if (length > 0 && result.length() > length)
            {
                result = result.substring(0, length) + "...";
            }
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().debug(
                                             "Javelin Exception" + object.getClass().toString()
                                                     + "#toString(): ", th);
            result = object.getClass().toString() + "@" + StatsUtil.getObjectID(object);
        }

        return result;
    }

    /**
     * �o�C�g���byte[length]:FFFF...�`���ɕϊ��A�w�蒷�Ő؂�B
     * 
     * @param binary �o�C�i��
     * @return �o�C�g���byte[length]:FFFF...�`���ɕϊ��A�w�蒷�Ő؂������́B
     */
    public static String toStr(final byte binary)
    {
        String hex = Integer.toHexString((binary) & EXCHANGE_FORMAT).toUpperCase();
        String result = "byte[1]:" + "00".substring(hex.length()) + hex;
        return result;
    }

    /**
     * �o�C�g���byte[length]:FFFF...�`���ɕϊ�(�ő�Ő擪8�o�C�g��16�i�o��)�B
     * 
     * @param binary �o�C�i��
     * @return �o�C�g���byte[length]:FFFF...�`���ɕϊ�(�ő�Ő擪8�o�C�g��16�i�o��)�������́B
     */
    public static String toStr(final byte[] binary)
    {
        // ������null�̏ꍇ��"null"��Ԃ��B
        if (binary == null)
        {
            return "null";
        }

        if (binary.length == 0)
        {
            return "byte[0]";
        }

        StringBuffer result = new StringBuffer("byte[");
        result.append(binary.length);
        result.append("]:");
        for (int count = 0; count < MAX_COUNT && count < binary.length; count++)
        {
            String hex = Integer.toHexString((binary[count]) & EXCHANGE_FORMAT).toUpperCase();
            result.append("00".substring(hex.length()) + hex);
        }
        if (binary.length > MAX_COUNT)
        {
            result.append("...");
        }
        return result.toString();
    }

    /**
     * Object�̏��o�͂��s��
     * �o�͐[�x�ɂ��킹�A�t�B�[���h��H�邩���̏�ŏo�͂��邩���肷��
     * 
     * @param object       �o�͑ΏۃI�u�W�F�N�g
     * @param detailDepth  �o�͐[�x
     * @return             �o�͌���
     */
    public static String buildDetailString(final Object object, final int detailDepth)
    {
        return DetailStringBuilder.buildDetailString(object, detailDepth);
    }

    /**
     * ToString�̌��ʂ�Ԃ�
     * 
     * @param object �ϊ��Ώ�
     * @return       ToString�̌���
     */
    public static String buildString(final Object object)
    {
        //toString�͗�O�𔭐������邱�Ƃ����邽�߁A��������
        //"????"�Ƃ����������Ԃ��悤�ɂ���B
        return DetailStringBuilder.buildString(object);
    }

    /**
     * �w�肵��������̎w�肵�������܂ł̕�������擾���܂��B
     * @param str ������
     * @param maxLength ����
     * @return �Y��������
     */
    public static String substring(String str, int maxLength)
    {
        if (str == null)
        {
            return str;
        }

        if (maxLength > str.length())
        {
            maxLength = str.length();
        }
        
        return str.substring(0, maxLength);
    }

}
