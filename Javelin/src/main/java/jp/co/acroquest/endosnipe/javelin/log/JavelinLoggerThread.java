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
package jp.co.acroquest.endosnipe.javelin.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.zip.ZipOutputStream;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.common.util.IOUtil;
import jp.co.acroquest.endosnipe.javelin.converter.concurrent.monitor.ConcurrentAccessMonitor;
import jp.co.acroquest.endosnipe.javelin.converter.leak.monitor.CollectionMonitor;

/**
 * Javelin���O���o�͂���X���b�h�ł��B
 * @author eriguchi
 *
 */
class JavelinLoggerThread extends Thread
{
    /** �t�@�C���ɂ���V�[�P���X�i���o�[ */
    private static int sequenceNumber__ = 0;

    /** zip�t�@�C���ɂ���V�[�P���X�i���o�[ */
    private static int zipSequenceNumber__ = 0;

    /** ���� Javelin ���O�t�@�C�����폜����V�[�P���X�i���o�[ */
    private static int nextDeleteSequenceNumber__ = 0;

    private static final String EXTENTION_JVN = ".jvn";

    private static final String EXTENTION_ZIP = ".zip";

    /** jvn�t�@�C�����̃t�H�[�}�b�g(���t�t�H�[�}�b�g(�~��(sec)�܂ŕ\��) */
    private static final String JVN_FILE_FORMAT =
            "javelin_{0,date,yyyy_MM_dd_HHmmss_SSS}_{1,number,00000}" + EXTENTION_JVN;

    /** zip�t�@�C�����̃t�H�[�}�b�g(���t�t�H�[�}�b�g(�~��(sec)�܂ŕ\��) */
    private static final String ZIP_FILE_FORMAT =
            "{0}" + File.separator + "javelin_{1,date,yyyy_MM_dd_HHmmss_SSS}_{2,number,00000}"
                    + EXTENTION_ZIP;

    private final JavelinConfig javelinConfig_;

    private final BlockingQueue<JavelinLogTask> queue_;

    /**
     * Javelin�̐ݒ�l�ƃL���[���Z�b�g���܂��B<br />
     *
     * @param javelinConfig {@link JavelinConfig}�I�u�W�F�N�g
     * @param queue �L���[
     */
    public JavelinLoggerThread(final JavelinConfig javelinConfig,
            final BlockingQueue<JavelinLogTask> queue)
    {
        super();
        setName("S2Javelin-LoggerThread-" + getId());
        setDaemon(true);
        this.javelinConfig_ = javelinConfig;
        this.queue_ = queue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        CollectionMonitor.setTracing(Boolean.FALSE);
        ConcurrentAccessMonitor.setTracing(Boolean.FALSE);
        
        boolean isZipFileMax = this.javelinConfig_.isLogZipMax();
        int jvnFileMax = this.javelinConfig_.getLogJvnMax();
        int zipFileMax = this.javelinConfig_.getLogZipMax();
        if (nextDeleteSequenceNumber__ == 0)
        {
            nextDeleteSequenceNumber__ = jvnFileMax;
        }

        String javelinFileDir = this.javelinConfig_.getJavelinFileDir();

        // jvn���O�o�͐�f�B���N�g�����쐬����B
        File javelinFileDirFile = new File(javelinFileDir);
        if (javelinFileDirFile.exists() == false)
        {
            boolean mkdirs = javelinFileDirFile.mkdirs();
            if (mkdirs == false)
            {
                SystemLogger.getInstance().warn(
                        "mkdir failed: dir name "
                                + javelinFileDirFile.getAbsolutePath());
            }
        }

        while (true)
        {
            try
            {
                JavelinLogTask task;
                try
                {
                    task = this.queue_.take();
                }
                catch (InterruptedException ex)
                {
                    SystemLogger.getInstance().warn(ex);
                    continue;
                }

                // ���O��zip���k�A�t�@�C�����������s���B
                if (sequenceNumber__ > nextDeleteSequenceNumber__)
                {
                    nextDeleteSequenceNumber__ += jvnFileMax;
                    if (isZipFileMax)
                    {
                        zipAndDeleteLogFiles(jvnFileMax, javelinFileDir, EXTENTION_JVN);
                        boolean success = IOUtil.removeFiles(
                                             zipFileMax, javelinFileDir, EXTENTION_ZIP);
                        
                        if(!success)
                        {
                            SystemLogger.getInstance().warn(
                               "Failed to delete files under the direcotry. Directory:" 
                             + javelinFileDir);
                        }
                    }
                    else
                    {
                        IOUtil.removeFiles(jvnFileMax, javelinFileDir, EXTENTION_JVN);
                    }
                }

                StringBuilder stringBuilder = new StringBuilder();

                String jvnFileDir = this.javelinConfig_.getJavelinFileDir();
                String jvnFileName = task.getJvnFileName();
                String jvnFileFullPath = jvnFileDir + File.separator + jvnFileName;

                JavelinLogCallback callback = task.getJavelinLogCallback();
                long telegramId = task.getTelegramId();
                String itemName = task.getItemName();

                // �ċA�I��writer�ɏ������݂��s���B
                JavelinFileGenerator.generateJavelinFileImpl(stringBuilder,
                        task.getTree(), task.getNode(), task.getEndNode(),
                        callback, jvnFileName, jvnFileFullPath, telegramId,
                        itemName);

                // �o�͂��ׂ����b�Z�[�W������΃o�b�t�@�t���b�V��
                if (stringBuilder.length() > 0)
                {
                    JavelinFileGenerator.flushBuffer(stringBuilder,
                            jvnFileName, jvnFileFullPath, callback,
                            this.javelinConfig_, telegramId, itemName);
                }
            }
            catch (Throwable ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }
    }

    /**
     * Javelin���O�t�@�C�����𐶐����܂��B<br />
     *
     * @param date ���t
     * @return jvn�t�@�C����
     */
    public static String createJvnFileName(final Date date)
    {
        String fileName;
        fileName = MessageFormat.format(JVN_FILE_FORMAT, date, (sequenceNumber__++));

        return fileName;
    }

    /**
     * �t�@�C�����𐶐����܂��B<br />
     *
     * @return �t�@�C����
     */
    private String createZipFileName(final Date date)
    {
        String fileName;
        fileName =
                MessageFormat.format(ZIP_FILE_FORMAT, this.javelinConfig_.getJavelinFileDir(),
                                     date, (zipSequenceNumber__++));

        return fileName;
    }

    private void zipAndDeleteLogFiles(final int maxFileCount, final String dirName,
            final String extention)
    {
        File[] files = IOUtil.listFile(dirName, extention);

        if (files == null || files.length == 0)
        {
            return;
        }

        String fileName = createZipFileName(new Date());
        FileOutputStream fileOutputStream;
        ZipOutputStream zStream = null;
        try
        {
            fileOutputStream = new FileOutputStream(fileName);
            zStream = new ZipOutputStream(fileOutputStream);

            for (int index = 0; index < files.length; index++)
            {
                File file = files[index];
                IOUtil.zipFile(zStream, file);
                SystemLogger.getInstance()
                        .debug("zip file name = " + file.getName() + " to "
                                + fileName);

                boolean success = file.delete();
                if (success == false)
                {
                    SystemLogger.getInstance().warn("Remove failed. file name = " + file.getName());
                }
                else
                {
                    if (SystemLogger.getInstance().isDebugEnabled())
                    {
                        SystemLogger.getInstance().debug("Remove file name = " + file.getName());
                    }
                }
            }

            zStream.finish();
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
            if (zStream != null)
            {
                try
                {
                    zStream.close();
                }
                catch (IOException ioe)
                {
                    SystemLogger.getInstance().warn(ioe);
                }
            }
        }
    }

}
