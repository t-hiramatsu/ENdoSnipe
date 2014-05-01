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
package jp.co.acroquest.endosnipe.javelin.converter.interval.monitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;
import jp.co.acroquest.endosnipe.javelin.conf.JavelinMessages;
import jp.co.acroquest.endosnipe.javelin.event.CommonEvent;
import jp.co.acroquest.endosnipe.javelin.event.IntervalErrorEvent;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * ���\�b�h�̌Ăяo���Ԋu���v�����A臒l�𒴉߂���΁A�A���[���𔭂��܂��B<br />
 * �t���[�����[�N�̏��������A�V�X�e���N�������ɂP�񂾂����s����Ηǂ����\�b�h���A
 * �p�ɂɌĂяo����Ă��Ȃ����m�F���邱�Ƃ��ł��܂��B<br />
 * �Ăяo�����m�F�����ꍇ�́A�A���[���𔭕񂷂邽�߁A�ُ��BottleneckEye�Ō��o���鎖���\�ł��B<br />
 *
 * ���̃N���X�� IntervalMonitor �Ƃ͈قȂ�A�������\�b�h�̒��ł��A
 * ���\�b�h�Ăяo�����̈������������ꍇ�ɃJ�E���g�A�b�v���܂��B<br />
 *
 * @author sakamoto
 */
public class IntervalPerArgumentsMonitor
{
    private static final String KEY_CONFIG_ERROR =
            "javelin.conf.AbstractConversionConfig.AnIllegalConfigurationLabel";

    private static final String TOKEN_THRESHOLD = ",";

    private static final String TOKEN_INTERVAL = "=";

    private static final String TOKEN_CLASS_AND_METHOD = "#";

    private static final int INDEX_CLASS_AND_METHOD = 0;

    private static final int INDEX_INTERVAL = 1;

    private static final int SUBINDEX_CLASS = 0;

    private static final int SUBINDEX_METHD = 1;

    private static Map<CallKeyWithArguments, Long> callIntervalMap__ =
            new ConcurrentHashMap<CallKeyWithArguments, Long>();

    private static Map<CallKey, Long> thresholdMap__ = new ConcurrentHashMap<CallKey, Long>();

    private static JavelinConfig config__;

    /** ���\�b�h�Ăяo���Ԋu�v������\���t���O */
    private static ThreadLocal<Boolean> isTracing__;

    static
    {
        init();
        isTracing__ = new ThreadLocal<Boolean>() {
            protected Boolean initialValue()
            {
                return Boolean.FALSE;
            }
        };

    }

    /**
     * �C���X�^���X����h�~����R���X�g���N�^�ł��B
     */
    private IntervalPerArgumentsMonitor()
    {
        // Do Nothing
    }

    private static void init()
    {
        config__ = new JavelinConfig();
        String threshold = config__.getIntervalPerArgsThreshold();

        parse(threshold);
    }

    /**
     * ���\�b�h���s���O�ɌĂ΂�܂��B<br />
     *
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param args ���\�b�h�̈���
     */
    public static void preProcess(final String className, final String methodName,
            final Object... args)
    {
        // ���\�b�h�Ăяo���Ԋu�v�����̏ꍇ�A�������s��Ȃ��B
        if (isTracing__.get().booleanValue() == true)
        {
            return;
        }

        isTracing__.set(Boolean.TRUE);
        call(className, methodName, args);
    }

    /**
     * ���\�b�h�ďo����ɁA���\�b�h�v������\���t���O��OFF�ɂ��܂��B<br />
     */
    public static void postProcess()
    {
        isTracing__.set(Boolean.FALSE);
    }

    /**
     * ��O�������ɁA���\�b�h�v������\���t���O��OFF�ɂ��܂��B<br />
     */
    public static void postProcessException()
    {
        isTracing__.set(Boolean.FALSE);
    }

    /**
     * javelin.properties �ɋL�q���ꂽ�ݒ�l���p�[�X���A
     * �`�F�b�N�Ώۂ̃N���X�A���\�b�h��臒l���擾���܂��B<br />
     */
    private static void parse(String threshold)
    {
        String[] thresholds = threshold.split(TOKEN_THRESHOLD);
        for (String config : thresholds)
        {
            String[] values = config.split(TOKEN_INTERVAL);

            if (values.length != 2)
            {
                // �`�F�b�N�ΏۃN���X�A���\�b�h�����臒l���������w�肳��Ă��Ȃ��ꍇ�A
                // ���O�ɐݒ�G���[���L�^���܂��B
                String message = JavelinMessages.getMessage(KEY_CONFIG_ERROR, config);
                SystemLogger.getInstance().warn(message);

                // ���̐ݒ�̏����Ɉڍs���܂��B
                continue;
            }

            String intervalConfig = values[INDEX_INTERVAL];

            long interval;
            try
            {
                interval = Long.valueOf(intervalConfig);
            }
            catch (NumberFormatException ex)
            {
                // 臒l�̐ݒ肪long�l�Ƃ��ĉ��ߕs�\�ȏꍇ�A���O�ɐݒ�G���[���L�^���܂��B
                String message = JavelinMessages.getMessage(KEY_CONFIG_ERROR, config);
                SystemLogger.getInstance().warn(message, ex);

                // ���̐ݒ�̏����Ɉڍs���܂��B
                continue;
            }

            String[] names = values[INDEX_CLASS_AND_METHOD].split(TOKEN_CLASS_AND_METHOD);

            if (names.length == 2)
            {
                String className = names[SUBINDEX_CLASS];
                String methodName = names[SUBINDEX_METHD];

                CallKey key = new CallKey(className, methodName);

                thresholdMap__.put(key, interval);
            }
            else
            {
                // �N���X�A���\�b�h���������w�肳��Ă��Ȃ��ꍇ�̓��O�ɐݒ�G���[���L�^���܂��B
                String message = JavelinMessages.getMessage(KEY_CONFIG_ERROR, config);
                SystemLogger.getInstance().warn(message);
            }
        }
    }

    /**
     * ���\�b�h�̌Ăяo���Ԋu�̔�����s���܂��B<br />
     * �O��Ăяo��������̌o�ߎ��Ԃ��~���b�P�ʂŋ��߁A
     * �ݒ�t�@�C���ɋL�q���ꂽ臒l��������Ă���΁A�A���[���̔�����s���܂��B
     *
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param args ���\�b�h�̈���
     */
    private static void call(final String className, final String methodName,
            final Object... args)
    {
        try
        {
            CallKey key = new CallKey(className, methodName);
            CallKeyWithArguments keyWithArguments = new CallKeyWithArguments(key, args);
            long presentTime = System.currentTimeMillis();
            Long lastCallTime = callIntervalMap__.get(keyWithArguments);

            if (lastCallTime != null && config__.isIntervalMonitor())
            {
                long diff = presentTime - lastCallTime.longValue();
                Long thresholdLong = thresholdMap__.get(key);
                if (thresholdLong != null)
                {
                    long threshold = thresholdLong.longValue();

                    // 臒l���ݒ肳��Ă���A���ۂ̌Ăяo���Ԋu��臒l��������Ă���΁A
                    // �A���[���𔭕�ݒ���s���܂��B
                    if (threshold > 0 && threshold > diff)
                    {
                        CommonEvent event = new IntervalErrorEvent();
                        event.addParam(EventConstants.PARAM_INTERVALERROR_THRESHOLD,
                                       String.valueOf(threshold));
                        event.addParam(EventConstants.PARAM_INTERVALERROR_ACTUAL_INTERVAL,
                                       String.valueOf(diff));
                        event.addParam(EventConstants.PARAM_INTERVALERROR_CLASSNAME, className);
                        event.addParam(EventConstants.PARAM_INTERVALERROR_METHODNAME, methodName);
                        for (int index = 0; index < args.length; index++)
                        {
                            String paramIndex = String.valueOf(index + 1);
                            event.addParam(EventConstants.PARAM_INTERVALERROR_ARGUMENTS + paramIndex,
                                           String.valueOf(args[index]));
                        }
                        StackTraceElement[] elements = ThreadUtil.getCurrentStackTrace();
                        String stackTrace = ThreadUtil.getStackTrace(elements);
                        event.addParam(EventConstants.PARAM_INTERVALERROR_STACKTRACE, stackTrace);
                        StatsJavelinRecorder.addEvent(event);
                    }
                }
            }

            // ���񔻒�p�ɍŐV�̌Ăяo���������L�^���Ă����܂��B
            callIntervalMap__.put(keyWithArguments, presentTime);
        }
        catch (Exception ex)
        {
            //��O�������̓��O�ɋL�^���Ă����܂��B
            SystemLogger.getInstance().warn(ex);
        }
    }

}
