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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.util.IOUtil;

/**
 * Javelin�̃V�X�e�����K�[�B<br>
 * 
 * @author eriguchi
 */
public class SystemLogger
{
    /** ��������Ԃ�\���t���O */
    private static volatile boolean initialized__ = false;

    /** �G���[���O�o�͓����̃t�H�[�}�b�g */
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss.SSS";

    /** ���s���� */
    public static final String NEW_LINE = System.getProperty("line.separator");

    /** �V�X�e�����O�t�@�C���̊g���q */
    private static final String EXTENTION = ".log";

    /** �V�X�e�����O�t�@�C�����̃t�H�[�}�b�g(���t�t�H�[�}�b�g(�~��(sec)�܂ŕ\��) */
    private static final String LOG_FILE_FORMAT =
            "jvn_sys_{0,date,yyyy_MM_dd_HHmmss_SSS}" + EXTENTION;

    /** �V�X�e�����O�t�@�C���̍ő吔 */
    private int systemLogNumMax_;

    /** �V�X�e�����O�t�@�C���̍ő�T�C�Y */
    private int systemLogSizeMax_;

    /** �V�X�e�����O�̃��O���x�� */
    private LogLevel systemLogLevel_ = LogLevel.WARN;

    /** �{�N���X�̃C���X�^���X */
    private static SystemLogger instance__ = new SystemLogger();

    /** �������񂾕����� */
    private long writeCount_ = 0;

    /** �V�X�e�����O�t�@�C���̏o�͐�f�B���N�g���̃p�X */
    private String logPath_;

    /** �V�X�e�����O�t�@�C���̃t�@�C���� */
    private String logFileName_;

    /** �V�X�e�����O�o�͗pThreadPoolExecutor�B */
    private final ThreadPoolExecutor executor_ =
            new ThreadPoolExecutor(1, 1, 1, TimeUnit.MILLISECONDS,
                                   new ArrayBlockingQueue<Runnable>(1000), new ThreadFactory() {
                public Thread newThread(final Runnable runnable)
                {
                    Thread thread =
                            new Thread(runnable, "Javelin-SystemLogger");
                    thread.setDaemon(true);
                    return thread;
                }
            }, new ThreadPoolExecutor.CallerRunsPolicy());

    private SystemLogger()
    {
        // Do nothing.
    }

    /**
     * �{�N���X�̃C���X�^���X���擾���܂��B<br />
     * 
     * @return �C���X�^���X
     */
    public static SystemLogger getInstance()
    {
        return instance__;
    }

    /**
     * �V�X�e�����O�t�@�C�����𐶐����܂��B<br />
     * �t�@�C�����̃t�H�[�}�b�g�͈ȉ��̂Ƃ���ł��B<br>
     * jvn_sys_yyyyMMddHHmmssSSS.log
     * 
     * @return �V�X�e�����O�t�@�C����
     */
    private String createLogFileName()
    {
        return MessageFormat.format(LOG_FILE_FORMAT, new Date());
    }

    /**
     * ���O���b�Z�[�W���t�H�[�}�b�g���܂��B<br />
     * 
     * @param level ���O���x��
     * @param threadName �X���b�h���B
     * @param message ���O���b�Z�[�W
     * @param throwable ��O�I�u�W�F�N�g
     * @return �t�H�[�}�b�g�������O���b�Z�[�W
     */
    private String formatMessage(final LogLevel level, final String threadName,
            final String message, final Throwable throwable)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateMessage = dateFormat.format(new Date());

        StringBuffer messageBuffer = new StringBuffer();

        messageBuffer.append(dateMessage);
        messageBuffer.append(" [");
        messageBuffer.append(level.getLevelStr());
        messageBuffer.append("] ");
        messageBuffer.append("[");
        messageBuffer.append(threadName);
        messageBuffer.append("] ");
        messageBuffer.append("[Javelin] ");

        if (message != null)
        {
            messageBuffer.append(message);
        }
        if (throwable != null)
        {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);

            throwable.printStackTrace(printWriter);
            String stackTrace = stringWriter.toString();

            messageBuffer.append(NEW_LINE);
            messageBuffer.append(stackTrace);
        }
        messageBuffer.append(NEW_LINE);
        String buildMessage = messageBuffer.toString();
        return buildMessage;
    }

    /**
     * ���O���o�͂��܂��B<br />
     * 
     * @param message �G���[���b�Z�[�W
     * @param throwable ��O�I�u�W�F�N�g
     */
    private void log(final LogLevel level, final String message, final Throwable throwable)
    {
        if (level.getLevel() < SystemLogger.this.systemLogLevel_.getLevel())
        {
            return;
        }

        Thread currentThread = Thread.currentThread();
        final String THREAD_NAME = currentThread.getName() + "(" + currentThread.getId() + ")";
        executor_.execute(new Runnable() {
            public void run()
            {
                if (initialized__ == false)
                {
                    return;
                }

                String formattedMessage = formatMessage(level, THREAD_NAME, message, throwable);

                String logPath = SystemLogger.this.logPath_;
                if (logPath == null)
                {
                    System.err.println(formattedMessage);
                    return;
                }

                if (SystemLogger.this.logFileName_ == null)
                {
                    SystemLogger.this.logFileName_ = createLogFileName();
                }

                OutputStreamWriter writer = null;
                try
                {
                    // �e�f�B���N�g�����쐬����B
                    IOUtil.createDirs(logPath_);

                    FileOutputStream fileOutputStream =
                            new FileOutputStream(logPath_ + File.separator + logFileName_, true);
                    writer = new OutputStreamWriter(fileOutputStream);

                    writer.write(formattedMessage);
                    writeCount_ += formattedMessage.length();
                }
                catch (Exception ex)
                {
                    // �o�͂ł��Ȃ������ꍇ�͕W���G���[�ɏo�͂���B�B
                    String errMessage =
                            "Javelin���s�G���[�o�̓t�@�C���ւ̏������݂��s���Ȃ��������߁A�W���G���[�o�͂��g�p���܂��B" + NEW_LINE
                            + "(javelin.error.log=" + logPath + File.separator
                            + logFileName_ + ")";
                    System.err.println(errMessage);
                    ex.printStackTrace();
                    System.err.println(formattedMessage);
                }
                finally
                {
                    try
                    {
                        if (writer != null)
                        {
                            writer.close();
                        }
                    }
                    catch (IOException ioe)
                    {
                        ioe.printStackTrace();
                    }
                }

                // ���[�e�[�g���K�v�ȏꍇ�̓��[�e�[�g����B
                if (writeCount_ > systemLogSizeMax_)
                {
                    File logFile =
                            new File(logPath + File.separator + SystemLogger.this.logFileName_);
                    if (logFile.length() > systemLogSizeMax_)
                    {
                        logFileName_ = createLogFileName();
                        boolean success = IOUtil.removeFiles(
                                         systemLogNumMax_-1, logPath, EXTENTION);
                        
                        if(!success)
                        {
                            getInstance().warn(
                               "Failed to delete files under the direcotry. Directory:" + logPath_);
                        }
                        
                        writeCount_ = 0;
                    }
                }
            }
        });
    }

    /**
     * FATAL ���x���̃��O���o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     */
    public void fatal(final String message)
    {
        this.fatal(message, null);
    }

    /**
     * FATAL ���x���̃��O���o�͂��܂��B<br />
     * 
     * @param throwable ��O�I�u�W�F�N�g
     */
    public void fatal(final Throwable throwable)
    {
        this.fatal(null, throwable);
    }

    /**
     * FATAL ���x���̃��O���o�͂��܂��B<br />
     *
     * @param message ���b�Z�[�W
     * @param throwable ��O�I�u�W�F�N�g
     */
    public void fatal(final String message, final Throwable throwable)
    {
        this.log(LogLevel.FATAL, message, throwable);
    }

    /**
     * ERROR ���x���̃��O���o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     */
    public void error(final String message)
    {
        this.error(message, null);
    }

    /**
     * ERROR ���x���̃��O���o�͂��܂��B<br />
     * 
     * @param throwable ��O�I�u�W�F�N�g
     */
    public void error(final Throwable throwable)
    {
        this.error(null, throwable);
    }

    /**
     * ERROR ���x���̃��O���o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     * @param throwable ��O�I�u�W�F�N�g
     */
    public void error(final String message, final Throwable throwable)
    {
        this.log(LogLevel.ERROR, message, throwable);
    }

    /**
     * WARN ���x���̃��O���o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     */
    public void warn(final String message)
    {
        this.warn(message, null);
    }

    /**
     * WARN ���x���̃��O���o�͂��܂��B<br />
     * 
     * @param throwable ��O�I�u�W�F�N�g
     */
    public void warn(final Throwable throwable)
    {
        this.warn(null, throwable);
    }

    /**
     * WARN ���x���̃��O���o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     * @param throwable ��O�I�u�W�F�N�g
     */
    public void warn(final String message, final Throwable throwable)
    {
        this.log(LogLevel.WARN, message, throwable);
    }

    /**
     * INFO ���x���̃��O���o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     */
    public void info(final String message)
    {
        this.info(message, null);
    }

    /**
     * INFO ���x���̃��O���o�͂��܂��B<br />
     * 
     * @param throwable ��O�I�u�W�F�N�g
     */
    public void info(final Throwable throwable)
    {
        this.info(null, throwable);
    }

    /**
     * INFO ���x���̃��O���o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     * @param throwable ��O�I�u�W�F�N�g
     */
    public void info(final String message, final Throwable throwable)
    {
        this.log(LogLevel.INFO, message, throwable);
    }

    /**
     * DEBUG ���x���̃��O���o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     */
    public void debug(final String message)
    {
        this.debug(message, null);
    }

    /**
     * DEBUG ���x���̃��O���o�͂��܂��B<br />
     * 
     * @param throwable ��O�I�u�W�F�N�g
     */
    public void debug(final Throwable throwable)
    {
        this.debug(null, throwable);
    }

    /**
     * DEBUG ���x���̃��O���o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     * @param throwable ��O�I�u�W�F�N�g
     */
    public void debug(final String message, final Throwable throwable)
    {
        this.log(LogLevel.DEBUG, message, throwable);
    }

    /**
     * DEBUG ���x�����L�����ǂ�����Ԃ��܂��B<br />
     * 
     * @return �L���ł���ꍇ�A<code>true</code>
     */
    public boolean isDebugEnabled()
    {
        if (LogLevel.DEBUG.getLevel() < this.systemLogLevel_.getLevel())
        {
            return false;
        }
        return true;
    }

    /**
     * INFO ���x�����L�����ǂ�����Ԃ��܂��B<br />
     * 
     * @return �L���ł���ꍇ�A<code>true</code>
     */
    public boolean isInfoEnabled()
    {
        if (LogLevel.INFO.getLevel() < this.systemLogLevel_.getLevel())
        {
            return false;
        }
        return true;
    }

    /**
     * WARN ���x�����L�����ǂ�����Ԃ��܂��B<br />
     * 
     * @return �L���ł���ꍇ�A<code>true</code>
     */
    public boolean isWarnEnabled()
    {
        if (LogLevel.WARN.getLevel() < this.systemLogLevel_.getLevel())
        {
            return false;
        }
        return true;
    }

    /**
     * ERROR ���x�����L�����ǂ�����Ԃ��܂��B<br />
     * 
     * @return �L���ł���ꍇ�A<code>true</code>
     */
    public boolean isErrorEnabled()
    {
        if (LogLevel.ERROR.getLevel() < this.systemLogLevel_.getLevel())
        {
            return false;
        }
        return true;
    }

    /**
     * FATAL ���x�����L�����ǂ�����Ԃ��܂��B<br />
     * 
     * @return �L���ł���ꍇ�A<code>true</code>
     */
    public boolean isFatalEnabled()
    {
        if (LogLevel.FATAL.getLevel() < this.systemLogLevel_.getLevel())
        {
            return false;
        }
        return true;
    }

    /**
     * �V�X�e�����O�����������܂��B<br />
     * 
     * @param config �������p�����[�^�I�u�W�F�N�g
     */
    public static synchronized void initSystemLog(final JavelinConfig config)
    {
        SystemLogger instance = getInstance();
        instance.init(config);

        initialized__ = true;
    }

    private void init(final JavelinConfig config)
    {
        this.logPath_ = config.getSystemLog();
        this.systemLogNumMax_ = config.getSystemLogNumMax();
        this.systemLogSizeMax_ = config.getSystemLogSizeMax();
        this.systemLogLevel_ = toLogLevel(config.getSystemLogLevel());

        // �N�����Ƀ��O�t�@�C������������ꍇ�͍폜����B
        boolean success = IOUtil.removeFiles(this.systemLogNumMax_ - 1, this.logPath_, EXTENTION);

        if(!success)
        {
            getInstance().warn(
                   "Failed to delete files under the direcotry. Directory:" + this.logPath_);
        }
    }

    /**
     * {@link SystemLogger} ���������ς݂ł��邩�ǂ�����Ԃ��܂��B<br />
     * 
     * @return �������ς݂̏ꍇ�� <code>true</code>�B
     */
    public static boolean isInitialized()
    {
        return initialized__;
    }

    /**
     * ���O���x���̕������ {@link LogLevel} �I�u�W�F�N�g�ɕϊ����܂��B<br />
     * 
     * @param logLevelStr ���O���x���̕�����B
     * @return LogLevel {@link LogLevel} �I�u�W�F�N�g
     */
    private static LogLevel toLogLevel(final String logLevelStr)
    {
        if ("DEBUG".equals(logLevelStr))
        {
            return LogLevel.DEBUG;
        }
        else if ("INFO".equals(logLevelStr))
        {
            return LogLevel.INFO;
        }
        else if ("WARN".equals(logLevelStr))
        {
            return LogLevel.WARN;
        }
        else if ("ERROR".equals(logLevelStr))
        {
            return LogLevel.ERROR;
        }
        else if ("FATAL".equals(logLevelStr))
        {
            return LogLevel.FATAL;
        }
        return LogLevel.WARN;
    }
}
