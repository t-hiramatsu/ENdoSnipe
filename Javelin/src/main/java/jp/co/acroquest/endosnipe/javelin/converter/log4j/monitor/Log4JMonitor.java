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
package jp.co.acroquest.endosnipe.javelin.converter.log4j.monitor;

import java.lang.reflect.Field;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;
import jp.co.acroquest.endosnipe.javelin.event.Log4jErrorEvent;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

//import org.apache.log4j.Priority;

/**
 * Log4JConverter����Ăяo�����X�^�b�N�g���[�X�o�͗p�N���X
 * 
 * @author S.Kimura
 */
public class Log4JMonitor
{
    /** Level�I�u�W�F�N�g���̃��x��(int�^)�������t�B�[���h�̖��� */
    private static final String LEVEL_FIELD = "level";

    /** Level�I�u�W�F�N�g���̃��x��(String�^)�������t�B�[���h�̖��� */
    private static final String LEVELSTR_FIELD = "levelStr";

    /** �f�t�H���g�̃X�^�b�N�g���[�X�o��臒l(int�\��) */
    private static final int DEF_PRINT_LEVEL = 40000;

    /** ���x���uDEBUG�v���������O���x���l */
    private static final int DEBUG_LEVEL_INT = 10000;

    /** ���x���uINFO�v���������O���x���l */
    private static final int INFO_LEVEL_INT = 20000;

    /** ���x���uWARN�v���������O���x���l */
    private static final int WARN_LEVEL_INT = 30000;

    /** ���x���uERROR�v���������O���x���l */
    private static final int ERROR_LEVEL_INT = 40000;

    /** ���x���uFATAL�v���������O���x���l */
    private static final int FATAL_LEVEL_INT = 50000;

    /**
     * �C���X�^���X����h�~���邽�߂̃R���X�g���N�^
     */
    private Log4JMonitor()
    {
    }

    /**
     * �w�肳�ꂽ���O���x���̃��b�Z�[�W�o�͎��̃X�^�b�N�g���[�X���o�͂���B
     * 
     * @param levelObj Level�I�u�W�F�N�g
     * @param message ���O���b�Z�[�W
     * @param th ��O
     */
    public static void printLog4JStackTrace(Object levelObj, Object message, Throwable th)
    {
        if (levelObj == null)
        {
            return;
        }

        Integer logLevel = getLevel(levelObj);

        if (logLevel == null)
        {
            return;
        }

        if (getPrintStackLevelInt() <= logLevel.intValue())
        {
            JavelinConfig config = new JavelinConfig();
            int stackTraceDepth = config.getThreadMonitorDepth();

            String levelStr = getLevelStr(levelObj);

            outputLog4jErrorEvent(levelStr, message, th, stackTraceDepth);
        }
    }

    /**
     * �R���t�B�O�ɐݒ肳�ꂽ�o�̓��x��臒l�𐔒l�ɕϊ����ĕԂ�
     * 
     * @return �o�̓��x��臒l�ݒ�iint�^�j
     */
    private static int getPrintStackLevelInt()
    {
        JavelinConfig config = new JavelinConfig();
        String printLevelStr = config.getLog4jPrintStackLevel();

        if ("FATAL".equals(printLevelStr))
        {
            return FATAL_LEVEL_INT;
        }
        else if ("ERROR".equals(printLevelStr))
        {
            return ERROR_LEVEL_INT;
        }
        else if ("WARN".equals(printLevelStr))
        {
            return WARN_LEVEL_INT;
        }
        else if ("INFO".equals(printLevelStr))
        {
            return INFO_LEVEL_INT;
        }
        else if ("DEBUG".equals(printLevelStr))
        {
            return DEBUG_LEVEL_INT;
        }
        else
        {
            return DEF_PRINT_LEVEL;
        }
    }

    /**
     * Level�I�u�W�F�N�g����Integer�^��level�Ƃ������̂̃t�B�[���h���擾����B
     * 
     * @param levelObj Level�I�u�W�F�N�g
     * @return ���O�o�̓��x���i���l�`���j
     */
    private static Integer getLevel(Object levelObj)
    {
        Integer level = null;

        // level�t�B�[���h��ێ����Ă���̂�Level�̃X�[�p�[�N���X�ł���Priority�N���X�̂��߁A
        // �͂��߂���P�i�K�X�[�p�[�N���X�Ƃ��ăt�B�[���h�̎擾���s��
        for (Class<?> cls = levelObj.getClass().getSuperclass(); cls != null; cls =
                cls.getSuperclass())
        {
            try
            {
                Field field = cls.getDeclaredField(LEVEL_FIELD);
                field.setAccessible(true);
                level = (Integer)field.get(levelObj);
                break;
            }
            catch (Exception ex)
            {
                SystemLogger.getInstance().debug(ex);
            }
        }

        return level;
    }

    /**
     * levelStr�I�u�W�F�N�g����String�^��levelStr�Ƃ������̂̃t�B�[���h���擾����B
     * 
     * @param levelObj Level�I�u�W�F�N�g
     * @return ���O�o�̓��x���iString�`���j
     */
    private static String getLevelStr(Object levelObj)
    {
        String levelStr = null;

        // level�t�B�[���h��ێ����Ă���̂�Level�̃X�[�p�[�N���X�ł���Priority�N���X�̂��߁A
        // �͂��߂���P�i�K�X�[�p�[�N���X�Ƃ��ăt�B�[���h�̎擾���s��
        for (Class<?> cls = levelObj.getClass().getSuperclass(); cls != null; cls =
                cls.getSuperclass())
        {
            try
            {
                Field field = cls.getDeclaredField(LEVELSTR_FIELD);
                field.setAccessible(true);
                levelStr = (String)field.get(levelObj);
                break;
            }
            catch (Exception ex)
            {
                SystemLogger.getInstance().debug(ex);
            }
        }

        return levelStr;
    }

    /**
     * Log4j�G���[�C�x���g���o��
     * 
     * @param levelStr ���O���x��
     * @param message ���O���b�Z�[�W
     * @param th ��O
     * @param stackTraceDepth ���O�o�͎��̃X�^�b�N�g���[�X�̐[��
     */
    private static void outputLog4jErrorEvent(String levelStr, Object message,
            Throwable th, int stackTraceDepth)
    {
        CommonEvent event = new Log4jErrorEvent();
        
        String stackTrace =
            ThreadUtil.getStackTrace(ThreadUtil.getCurrentStackTrace(), stackTraceDepth);
        
        event.addParam(EventConstants.PARAM_LOG4JERROR_LOGLEVEL, levelStr);
        if (message != null)
        {
            event.addParam(EventConstants.PARAM_LOG4JERROR_LOGMESSAGE, message.toString());
        }
        event.addParam(EventConstants.PARAM_LOG4JERROR_STACKTRACE, stackTrace);
        
        if(th != null)
        {
            String exStackTrace = ThreadUtil.getStackTrace(th.getStackTrace(), stackTraceDepth);
            event.addParam(EventConstants.PARAM_LOG4JERROR_EXCLASS, th.getClass().getCanonicalName());
            event.addParam(EventConstants.PARAM_LOG4JERROR_EXMESSAGE, th.getMessage());
            event.addParam(EventConstants.PARAM_LOG4JERROR_EXSTACKTRACE, exStackTrace);
        }

        StatsJavelinRecorder.addEvent(event);
    }

}
