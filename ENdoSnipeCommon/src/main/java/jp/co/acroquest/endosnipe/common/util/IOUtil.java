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
package jp.co.acroquest.endosnipe.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;

/**
 * �t�@�C�� I/O �Ɋւ��郆�[�e�B���e�B�N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class IOUtil
{
    private static final int BUFFER_SIZE = 1024;

    private IOUtil()
    {
    }

    /**
     * ���̓X�g���[������o�̓X�g���[���փR�s�[���܂��B<br />
     * 
     * @param input ���̓X�g���[��
     * @param output �o�̓X�g���[��
     * @return �R�s�[�����o�C�g��
     * @throws IOException ���o�̓G���[�����������ꍇ
     */
    public static long copy(final InputStream input, final OutputStream output)
        throws IOException
    {
        byte[] buffer = new byte[BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer)))
        {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * {@link Reader} ���� {@link Writer} �փR�s�[���܂��B<br />
     * 
     * @param input {@link Reader} �I�u�W�F�N�g
     * @param output {@link Writer} �I�u�W�F�N�g
     * @return �R�s�[�����o�C�g��
     * @throws IOException ���o�̓G���[�����������ꍇ
     */
    public static long copy(final Reader input, final Writer output)
        throws IOException
    {
        return copy(input, output, -1);
    }

    /**
     * {@link Reader} ���� {@link Writer} �֍ő�T�C�Y���w�肵�ăR�s�[���܂��B<br />
     * 
     * @param input {@link Reader} �I�u�W�F�N�g
     * @param output {@link Writer} �I�u�W�F�N�g
     * @param maxBytes �ő�o�C�g��
     * @return �R�s�[�����o�C�g��
     * @throws IOException ���o�̓G���[�����������ꍇ
     */
    public static long copy(final Reader input, final Writer output, final int maxBytes)
        throws IOException
    {
        char[] buffer = new char[BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (count < maxBytes && (-1 != input.read(buffer)))
        {
        	n = input.read(buffer);
            if (count + n > maxBytes)
            {
                n = (int)(maxBytes - count);
            }

            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * �t�@�C����ǂݍ���ŕ�����ŕԂ��܂��B<br />
     * 
     * @param fileName �t�@�C����
     * @return �ǂݍ��񂾕�����
     */
    public static String readFileToString(final String fileName)
    {
        return readFileToString(fileName, -1);
    }

    /**
     * �ő�o�C�g�����w�肵�ăt�@�C����ǂݍ��݁A������ŕԂ��܂��B<br />
     * 
     * @param fileName �t�@�C����
     * @param maxBytes �ő�o�C�g��
     * @return �ǂݍ��񂾕�����
     */
    public static String readFileToString(final String fileName, final int maxBytes)
    {
        String content = "";
        Reader input = null;
        StringWriter output = null;
        try
        {
            input = new FileReader(fileName);
            output = new StringWriter();
            copy(input, output, maxBytes);
        }
        catch (FileNotFoundException fnfe)
        {
            SystemLogger.getInstance().warn(fnfe);
        }
        catch (IOException ioe)
        {
            SystemLogger.getInstance().warn(ioe);
        }
        finally
        {
            if (input != null)
            {
                try
                {
                    input.close();
                }
                catch (IOException ioe)
                {
                    SystemLogger.getInstance().warn(ioe);
                }
            }
        }

        if (output != null)
        {
            content = output.toString();
        }
        return content;
    }

    /**
     * �f�B���N�g�����쐬���܂��B<br />
     * �e�f�B���N�g�������݂��Ȃ��ꍇ�A�����ɍ쐬���܂��B<br />
     * 
     * @param dirPath �쐬����f�B���N�g���̃p�X
     * @return ���������ꍇ�� <code>true</code>�A���s�����ꍇ�� <code>false</code>
     */
    public static boolean createDirs(final String dirPath)
    {
        File file = new File(dirPath);
        if (file.exists() == false)
        {
            return file.mkdirs();
        }

        return false;
    }

    /**
     * �g���q���w�肵�ăf�B���N�g���z���̃t�@�C����񋓂��܂��B<br />
     * 
     * @param dirPath �f�B���N�g���̃p�X
     * @param extention �g���q
     * @return �񋓂����t�@�C���B���݂��Ȃ��ꍇ�� <code>null</code>�B
     */
    public static File[] listFile(final String dirPath, final String extention)
    {
        File dir = new File(dirPath);
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(final File dir, final String name)
            {
                if (name != null && name.endsWith(extention))
                {
                    return true;
                }
                return false;
            }
        });

        if (files == null)
        {
            return null;
        }
        Arrays.sort(files);

        return files;
    }

    /**
     * �w�肵���f�B���N�g���z���ɂ�����w�肵���g���q�̃t�@�C�����`�F�b�N���A
     * �ő吔�𒴂��Ă���ꍇ�͍폜���܂��B<br />
     * 
     * @param maxFileCount �ő�t�@�C����
     * @param dirPath �f�B���N�g���̃p�X
     * @param extention �g���q
     * @return �폜�ɐ��������ꍇtrue
     */
    public static boolean removeFiles(final int maxFileCount, final String dirPath,
            final String extention)
    {
        File[] files = listFile(dirPath, extention);

        boolean success = false;
        if (files == null)
        {
            return success;
        }

        for (int index = files.length; index > maxFileCount; index--)
        {
            success = files[files.length - index].delete();
        }

        return success;
    }

    /**
     * �w�肳�ꂽ�t�@�C���� ZIP ���k���܂��B<br />
     * 
     * @param zStream �o�͐�X�g���[��
     * @param file ���k����t�@�C��
     * @throws IOException ���o�̓G���[�����������ꍇ
     */
    public static void zipFile(final ZipOutputStream zStream, final File file)
        throws IOException
    {
        FileInputStream fileInputStream = null;
        try
        {
            fileInputStream = new FileInputStream(file);
            ZipEntry target = new ZipEntry(file.getName());
            zStream.putNextEntry(target);
            copy(fileInputStream, zStream);
            zStream.closeEntry();
        }
        catch (IOException ex)
        {
            StreamUtil.closeStream(fileInputStream);
            throw ex;
        }
        finally
        {
            StreamUtil.closeStream(fileInputStream);
        }
    }

    /**
     * OS �W���̃e���|�����f�B���N�g����\�� {@link File} �I�u�W�F�N�g��Ԃ��܂��B<br />
     * �e���|�����f�B���N�g�������݂��Ȃ��ꍇ�́A�����I�ɍ쐬���܂��B<br />
     * 
     * @return �e���|�����f�B���N�g����\�� {@link File} �I�u�W�F�N�g
     */
    public static File getTmpDirFile()
    {
        String tmpPath = System.getProperty("java.io.tmpdir");
        File tmpFile = new File(tmpPath);
        if (tmpFile.exists() == false)
        {
            boolean success = tmpFile.mkdirs();

            if(!success)
            {
                return null;
            }
        }
        return tmpFile;
    }
}
