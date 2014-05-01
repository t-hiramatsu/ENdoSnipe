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
package jp.co.acroquest.endosnipe.javelin.converter.leak.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;

/**
 * DiagnosticCommand��p���ăN���X�q�X�g�O�������擾����B
 * �擾�����q�X�g�O�����͈ȉ��̌`���Ŏ擾�ł��邽�߁A�p�[�X�������s���B
 * 
 * <pre>
 * --------- Detailed Heap Statistics: ---------
 * 46.4% 199k     2067   +199k [C
 * 16.2% 69k      595    +69k java/lang/Class
 * 11.4% 49k     2097    +49k java/lang/String
 *  6.3% 27k       28    +27k [B
 *  4.9% 21k      342    +21k [Ljava/lang/Object;
 *  1.6% 7k      176     +7k [Ljava/lang/String;
 *  1.1% 4k      156     +4k java/util/LinkedHashMap$Entry
 *  1.1% 4k       45     +4k [Ljava/util/HashMap$Entry;
 * </pre>
 * 
 * @author eriguchi
 */
public class OracleClassHistogramMonitor extends ClassHistogramMonitor
{
    /** �N���X�q�X�g�O�����̃w�b�_ */
    private static final String HEAP_HEADER        =
                                                     "--------- Detailed Heap Statistics: ---------";

    /** �R�}���h���s�N���X�̃N���X�� */
    private static final String COMMAND_CLASS_NAME = "com.bea.jvm.DiagnosticCommand";

    /** �N���X�q�X�g�O�������́A�T�C�Y(kbyte)�̃C���f�b�N�X�B */
    private static final int    INDEX_BYTES        = 1;

    /** �N���X�q�X�g�O�������́A�C���X�^���X���̃C���f�b�N�X�B */
    private static final int    INDEX_INSTANCES    = 2;

    /** �N���X�q�X�g�O�������́A�N���X���̃C���f�b�N�X�B */
    private static final int    INDEX_CLASSNAME    = 4;

    /** �N���X�q�X�g�O�����̃J�������B */
    private static final int    HISTOGRAM_COLUMNS  = 5;

    /** �R�}���h���s�p�I�u�W�F�N�g */
    private Object              command_;

    /** �R�}���h���s���\�b�h */
    private Method              executeMethod_;

    public OracleClassHistogramMonitor()
    {
        try
        {
            Class<?> clazz = Class.forName(COMMAND_CLASS_NAME);
            Method getCommandMethod = clazz.getMethod("getDiagnosticCommand", new Class[]{});
            Object command = getCommandMethod.invoke(null);
            Method executeMethod = clazz.getMethod("execute", new Class[]{String.class});
            this.command_ = command;
            this.executeMethod_ = executeMethod;
        }
        catch (ClassNotFoundException cnfe)
        {
            SystemLogger.getInstance().warn(cnfe);
        }
        catch (SecurityException se)
        {
            SystemLogger.getInstance().warn(se);
        }
        catch (NoSuchMethodException nsme)
        {
            SystemLogger.getInstance().warn(nsme);
        }
        catch (IllegalArgumentException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (IllegalAccessException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (InvocationTargetException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
    }

    /**
     * heap_diagnostics�R�}���h�𔭍s���ăN���X�q�X�g�O�������擾����B
     */
    public BufferedReader newReader(boolean classHistoGC)
        throws IOException
    {
        if (this.command_ == null || this.executeMethod_ == null)
        {
            return null;
        }

        BufferedReader reader = null;
        try
        {
            String result = (String)this.executeMethod_.invoke(this.command_, "heap_diagnostics");
            reader = new BufferedReader(new StringReader(result));
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                if (HEAP_HEADER.equals(line))
                {
                    break;
                }
            }
        }
        catch (IllegalArgumentException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (IllegalAccessException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        catch (InvocationTargetException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }

        return reader;
    }

    /**
     * 1�s���p�[�X���āAClassHistogramEntry�𐶐�����B
     * @param splitLine 1�s
     */
    protected ClassHistogramEntry parseEntry(final String[] splitLine)
    {
        if (splitLine.length != HISTOGRAM_COLUMNS)
        {
            return null;
        }

        try
        {
            ClassHistogramEntry entry = new ClassHistogramEntry();
            entry.setInstances(Integer.parseInt(splitLine[INDEX_INSTANCES]));
            entry.setBytes(parseHistogramBytes(splitLine[INDEX_BYTES]));
            entry.setClassName(splitLine[INDEX_CLASSNAME]);
            return entry;
        }
        catch (NumberFormatException nfe)
        {
            SystemLogger.getInstance().debug(nfe);
            return null;
        }
    }

    private int parseHistogramBytes(String bytes)
    {
        if (bytes == null || bytes.length() <= 1)
        {
            return 0;
        }

        int lastIndex = bytes.length() - 1;
        String valueStr = bytes.substring(0, lastIndex);
        int unitSize;
        if (bytes.charAt(lastIndex) == 'k')
        {
            unitSize = 1024;
        }
        else if (bytes.charAt(lastIndex) == 'm')
        {
            unitSize = 1024 * 1024;
        }
        else
        {
            unitSize = 1;
        }

        return Integer.parseInt(valueStr) * unitSize;
    }
}
