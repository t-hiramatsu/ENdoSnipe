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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.seasar.framework.message.MessageFormatter;
import org.seasar.framework.util.AssertionUtil;

/**
 * ���O�o�͂�񋟂���N���X�ł��B<br />
 * �{�N���X�ł́AEclipse �v���O�C�����ł��邩�ǂ������������肵�āA
 * ���O�o�͐�������I�ɐ؂�ւ��܂��B<br />
 * <ul>
 * <li>Eclipse ���łȂ��ꍇ�Alog4j �𗘗p���ă��O�o�͂��܂��B</li>
 * <li>Eclipse ���̏ꍇ�A�G���[���O�E�r���[�փ��O�o�͂��܂��B
 * (�G���[���O�E�r���[�� Eclipse for RCP/Plug-in Developers �Ŏg�p�ł��܂�)</li>
 * </ul>
 * 
 * �{�N���X�𗘗p���ă��O�o�͂��s���ɂ́A�ȉ��̂悤�ɂ��Ă��������B
 * 
 * <h2>ENdoSnipeLogger �C���X�^���X�̎擾���@</h2>
 * <p>
 * �ȉ��̂悤�ɒ萔�Ƃ��ėp�ӂ��Ă��������B������ {@link Class} �I�u�W�F�N�g��
 * ���K�[���g�p����N���X�� {@link Class} �I�u�W�F�N�g�ŁAlog4j �̃J�e�S����
 * ���Ďg�p���܂��B
 * </p>
 * 
 * <pre>
 * private static final ENdoSnipeLogger LOGGER =
 *   ENdoSnipeLogger.getLogger(OutputAction.class);
 * </pre>
 * 
 * <p>
 * �Ȃ��AEclipse �v���O�C�����Ŏg�p�����\��������ꍇ�A
 * �ȉ��̂悤�ɑ�2������ {@link PluginProvider} �� static �C���X�^���X��n���Ă��������B
 * ����́A�G���[���O�E�r���[�ɔ����v���O�C�����o�͂���ۂɎg�p���܂��B
 * </p>
 * 
 * <pre>
 * private static final ENdoSnipeLogger LOGGER =
 *   ENdoSnipeLogger.getLogger(OutputAction.class, ArrowVisionPluginProvider.INSTANCE);
 * </pre>
 * </li>
 * 
 * <h2>���b�Z�[�W�R�[�h���g�p�������O�o�͕��@</h2>
 * <p>
 * �ȉ��̊e���\�b�h�ł́A���b�Z�[�W�R�[�h���g�p�������O�o�͂��s�����Ƃ��ł��܂��B
 * </p>
 * <ul>
 *   <li>{@link #log(String, Object...)}</li>
 *   <li>{@link #log(String, Throwable, Object...)}</li>
 * </ul>
 * <ol>
 * <li>
 *   �T�u�V�X�e�� ID �����߂�B<br />
 *   3 �����̑啶���A���t�@�x�b�g�ŃT�u�V�X�e�� ID �����߂܂��B<br />
 *   �T�u�V�X�e�� ID �̒P�ʂŁA���b�Z�[�W���\�[�X�t�@�C�����쐬���邱�ƂɂȂ�܂��B<br />
 *   (��) ENdoSnipeDataCollector �� <code>EDC</code>
 * </li>
 * <li>
 *   ���b�Z�[�W���\�[�X�t�@�C�����쐬����B<br />
 *   <code><i>�T�u�V�X�e�� ID</i>Messages</code>�Ƃ����o���h�����̂�
 *   ���b�Z�[�W���\�[�X�t�@�C�����쐬���܂��B<br />
 *   �t�@�C���̓N���X�p�X�̃��[�g(�ʏ�� src/main/resources ����)�ɍ쐬���Ă��������B<br />
 *   (��) <code>EDCMessages_ja.properties</code>
 * </li>
 * <li>
 *   ���b�Z�[�W���L�q����B<br />
 *   <p>�ȉ��̌`���Ń��b�Z�[�W���\�[�X�t�@�C���Ƀ��b�Z�[�W��`��ǉ����܂��B</p>
 * 
 *   <i>���b�Z�[�W�R�[�h</i>=<i>���b�Z�[�W</i>
 * 
 *   <p>���b�Z�[�W�R�[�h�͈ȉ��̋K���Ō��肵�܂��B</p>
 * 
 *   <i>���O���x��</i><i>�T�u�V�X�e�� ID</i><i>�G���[�ԍ�</i>
 * 
 *   <p>
 *   �ŏ��� 1 �����̓��O���x���ŁA(F,E,W,I,D,T) �̂����ꂩ�ł��B<br />
 *   ���̂� 3 �����̓T�u�V�X�e�� ID ���w�肵�܂��B<br />
 *   �Ō�� 4 ���̐����̓T�u�V�X�e�����ň�ӂȃG���[�ԍ���\���܂��B<br />
 *   </p>
 * 
 *   (��) <code>IEDC0001=ENdoSnipe DataCollector ���J�n���܂�.</code>
 * 
 *   <p>
 *   ���b�Z�[�W�Ɉ������w�肷��ꍇ�A{0}�A{1}�E�E�E�̂悤�Ƀv���[�X�z���_���w��ł��܂��B<br />
 *   </p>
 * 
 *   (��) <code>IEDC0008=Javelin �ɐڑ����܂���.(�ڑ���: {0}:{1})</code>
 * </li>
 * </ol>
 * 
 * @author y-komori
 * @author nagai
 */
public abstract class ENdoSnipeLogger
{
    private static final Map<Class<?>, ENdoSnipeLogger> LOGGERS =
            new HashMap<Class<?>, ENdoSnipeLogger>();

    private static boolean initialized__;

    private static boolean isEclipseAvailable__;

    private static boolean useSystemLogger__;

    /**
     * {@link ENdoSnipeLogger} �̈Öق̃R���X�g���N�^�ł��B<br />
     */
    protected ENdoSnipeLogger()
    {
        // Do nothing.
    }

    /**
     * ���O�o�͂� {@link SystemLogger} ���g�p���邩�ǂ�����ݒ肵�܂��B<br />
     * Javelin �����ł� {@link SystemLogger} ���g�p���ă��O�o�͂��s�����߁A
     * �ŏ��ɖ{���\�b�h���g�p���� {@link SystemLogger} ���g�p����悤�ɂ��Ă��������B<br />
     * {@link SystemLogger} ���g�p����悤�ɐݒ肵���ꍇ�A�ȍ~��
     * {@link ENdoSnipeLogger} �ɂ��o�͂͂��ׂ� {@link SystemLogger} �ɂ���čs���܂��B
     * @param useSystemLogger {@link SystemLogger} ���g�p����ꍇ�A<code>true</code>
     */
    public static synchronized void setSystemLoggerMode(final boolean useSystemLogger)
    {
        useSystemLogger__ = useSystemLogger;
    }

    /**
     * {@link ENdoSnipeLogger} ��Ԃ��܂��B<br />
     * 
     * @param clazz {@link Class} �I�u�W�F�N�g
     * @return {@link ENdoSnipeLogger} �I�u�W�F�N�g
     */
    public static synchronized ENdoSnipeLogger getLogger(final Class<?> clazz)
    {
        return getLogger(clazz, null);
    }

    /**
     * {@link ENdoSnipeLogger} ��Ԃ��܂��B<br />
     * 
     * @param clazz {@link Class} �I�u�W�F�N�g
     * @param provider {@link PluginProvider} �I�u�W�F�N�g
     * @return {@link ENdoSnipeLogger} �I�u�W�F�N�g
     */
    public static synchronized ENdoSnipeLogger getLogger(final Class<?> clazz,
            final PluginProvider provider)
    {
        if (!initialized__)
        {
            initialize();
        }
        ENdoSnipeLogger logger = LOGGERS.get(clazz);
        if (logger == null)
        {
            logger = createENdoSnpeLogger(clazz, provider);
            LOGGERS.put(clazz, logger);
        }

        return logger;
    }

    /**
     * {@link ENdoSnipeLogger} �����������܂��B<br />
     */
    protected static synchronized void initialize()
    {
        initialized__ = true;
    }

    /**
     * ���\�[�X���J�����܂��B<br />
     */
    public static synchronized void dispose()
    {
        LogFactory.releaseAll();
        LOGGERS.clear();
        initialized__ = false;
        isEclipseAvailable__ = false;
    }

    private static ENdoSnipeLogger createENdoSnpeLogger(final Class<?> clazz,
            final PluginProvider provider)
    {
        if (useSystemLogger__ == true)
        {
            // SystemLogger �𗘗p����ꍇ
            return new SystemENdoSnipeLogger();
        }
        else
        {
            // �ǂ���ł��Ȃ��ꍇ
            return new Log4jENdoSnipeLogger(clazz);
        }
    }

    /**
     * XML �t�@�C���`���̃R���t�B�O���[�V������ǉ����܂��B<br />
     * �R���t�B�O���[�V�����t�@�C���̏����� log4j �ɏ����܂��B
     * 
     * @param config �R���t�B�O���[�V�����t�@�C���� {@link URL}
     */
    public void addXmlConfig(final URL config)
    {
        AssertionUtil.assertNotNull("config", config);
        DOMConfigurator.configure(config);
    }

    /**
     * properties �t�@�C���`���̃R���t�B�O���[�V������ǉ����܂��B<br />
     * �R���t�B�O���[�V�����t�@�C���̏����� log4j �ɏ����܂��B
     * 
     * @param config �R���t�B�O���[�V�����t�@�C���� {@link URL}
     */
    public void addPropertyConfig(final URL config)
    {
        AssertionUtil.assertNotNull("config", config);
        PropertyConfigurator.configure(config);
    }

    /**
     * TRACE��񂪏o�͂���邩�ǂ�����Ԃ��܂��B<br />
     * {@link SystemLogger} �����p����Ă���ꍇ�ADEBUG��񂪏o�͂���邩�ǂ�����Ԃ��܂��B
     * 
     * @return TRACE��񂪏o�͂���邩�ǂ���
     */
    public abstract boolean isTraceEnabled();

    /**
     * TRACE�����o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     * @param throwable {@link Throwable} �I�u�W�F�N�g
     * @deprecated �{���\�b�h�͋��R�[�h�̌݊����̂��߂ɗp�ӂ���Ă��܂��B�ւ��ɂ� log() ���\�b�h���g�p���Ă��������B
     */
    @Deprecated
    public abstract void trace(final Object message, final Throwable throwable);

    /**
     * TRACE�����o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     * @deprecated �{���\�b�h�͋��R�[�h�̌݊����̂��߂ɗp�ӂ���Ă��܂��B�ւ��ɂ� log() ���\�b�h���g�p���Ă��������B
     */
    @Deprecated
    public abstract void trace(final Object message);

    /**
     * DEBUG��񂪏o�͂���邩�ǂ�����Ԃ��܂��B<br />
     * 
     * @return DEBUG��񂪏o�͂���邩�ǂ���
     */
    public abstract boolean isDebugEnabled();

    /**
     * DEBUG�����o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     * @param throwable {@link Throwable} �I�u�W�F�N�g
     * @deprecated �{���\�b�h�͋��R�[�h�̌݊����̂��߂ɗp�ӂ���Ă��܂��B�ւ��ɂ� log() ���\�b�h���g�p���Ă��������B
     */
    @Deprecated
    public abstract void debug(final Object message, final Throwable throwable);

    /**
     * DEBUG�����o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     * @deprecated �{���\�b�h�͋��R�[�h�̌݊����̂��߂ɗp�ӂ���Ă��܂��B�ւ��ɂ� log() ���\�b�h���g�p���Ă��������B
     */
    @Deprecated
    public abstract void debug(final Object message);

    /**
     * INFO��񂪏o�͂���邩�ǂ�����Ԃ��܂��B<br />
     * 
     * @return INFO��񂪏o�͂���邩�ǂ���
     */
    public abstract boolean isInfoEnabled();

    /**
     * INFO�����o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     * @param throwable {@link Throwable} �I�u�W�F�N�g
     * @deprecated �{���\�b�h�͋��R�[�h�̌݊����̂��߂ɗp�ӂ���Ă��܂��B�ւ��ɂ� log() ���\�b�h���g�p���Ă��������B
     */
    @Deprecated
    public abstract void info(final Object message, final Throwable throwable);

    /**
     * INFO�����o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     * @deprecated �{���\�b�h�͋��R�[�h�̌݊����̂��߂ɗp�ӂ���Ă��܂��B�ւ��ɂ� log() ���\�b�h���g�p���Ă��������B
     */
    @Deprecated
    public abstract void info(final Object message);

    /**
     * WARN�����o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     * @param throwable {@link Throwable} �I�u�W�F�N�g
     * @deprecated �{���\�b�h�͋��R�[�h�̌݊����̂��߂ɗp�ӂ���Ă��܂��B�ւ��ɂ� log() ���\�b�h���g�p���Ă��������B
     */
    @Deprecated
    public abstract void warn(final Object message, final Throwable throwable);

    /**
     * WARN�����o�͂��܂��B<br />
     * 
     * @param throwable ���b�Z�[�W
     * @deprecated �{���\�b�h�͋��R�[�h�̌݊����̂��߂ɗp�ӂ���Ă��܂��B�ւ��ɂ� log() ���\�b�h���g�p���Ă��������B
     */
    @Deprecated
    public void warn(final Throwable throwable)
    {
        this.warn(throwable.toString(), throwable);
    }

    /**
     * WARN�����o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     * @deprecated �{���\�b�h�͋��R�[�h�̌݊����̂��߂ɗp�ӂ���Ă��܂��B�ւ��ɂ� log() ���\�b�h���g�p���Ă��������B
     */
    @Deprecated
    public abstract void warn(final Object message);

    /**
     * ERROR�����o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     * @param throwable {@link Throwable} �I�u�W�F�N�g
     * @deprecated �{���\�b�h�͋��R�[�h�̌݊����̂��߂ɗp�ӂ���Ă��܂��B�ւ��ɂ� log() ���\�b�h���g�p���Ă��������B
     */
    @Deprecated
    public abstract void error(final Object message, final Throwable throwable);

    /**
     * ERROR�����o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     * @deprecated �{���\�b�h�͋��R�[�h�̌݊����̂��߂ɗp�ӂ���Ă��܂��B�ւ��ɂ� log() ���\�b�h���g�p���Ă��������B
     */
    @Deprecated
    public abstract void error(final Object message);

    /**
     * FATAL�����o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     * @param throwable {@link Throwable} �I�u�W�F�N�g
     * @deprecated �{���\�b�h�͋��R�[�h�̌݊����̂��߂ɗp�ӂ���Ă��܂��B�ւ��ɂ� log() ���\�b�h���g�p���Ă��������B
     */
    @Deprecated
    public abstract void fatal(final Object message, final Throwable throwable);

    /**
     * FATAL�����o�͂��܂��B<br />
     * 
     * @param message ���b�Z�[�W
     * @deprecated �{���\�b�h�͋��R�[�h�̌݊����̂��߂ɗp�ӂ���Ă��܂��B�ւ��ɂ� log() ���\�b�h���g�p���Ă��������B
     */
    @Deprecated
    public abstract void fatal(final Object message);

    /**
     * ���O���o�͂��܂��B<br />
     * 
     * @param throwable {@link Throwable} �I�u�W�F�N�g
     * @deprecated �{���\�b�h�͋��R�[�h�̌݊����̂��߂ɗp�ӂ���Ă��܂��B
     * �ւ��� {@link #log(String, Throwable, Object...)} ���\�b�h���g�p���Ă��������B
     */
    @Deprecated
    public void log(final Throwable throwable)
    {
        error(throwable.getClass().getName() + " : " + throwable.getMessage(), throwable);
    }

    /**
     * ���b�Z�[�W�R�[�h���g�p���ă��O���o�͂��܂��B<br />
     * 
     * @param messageCode ���b�Z�[�W�R�[�h
     * @param args ����
     */
    public void log(final String messageCode, final Object... args)
    {
        log(messageCode, null, args);
    }

    /**
     * ���b�Z�[�W�R�[�h���g�p���ă��O���o�͂��܂��B<br />
     * 
     * @param messageCode ���b�Z�[�W�R�[�h
     * @param throwable {@link Throwable} �I�u�W�F�N�g
     * @param args ����
     */
    public void log(final String messageCode, final Throwable throwable, final Object... args)
    {
        char messageType = messageCode.charAt(0);
        if (isEnabledFor(messageType))
        {
            String message = getMessage(messageCode, args);

            switch (messageType)
            {
            case 'T':
                trace(message, throwable);
                break;
            case 'D':
                debug(message, throwable);
                break;
            case 'I':
                info(message, throwable);
                break;
            case 'W':
                warn(message, throwable);
                break;
            case 'E':
                error(message, throwable);
                break;
            case 'F':
                fatal(message, throwable);
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(messageType));
            }
        }
    }

    /**
     * properties�t�@�C���ɋL�q���ꂽ���b�Z�[�W���擾���܂��B<br />
     * 
     * @param messageCode ���b�Z�[�W�̃R�[�h
     * @param args �u�����郁�b�Z�[�W
     * @return ���b�Z�[�W
     */
    protected String getMessage(final String messageCode, final Object... args)
    {
        return MessageFormatter.getMessage(messageCode, args);
    }

    /**
     * �w�肳�ꂽ���b�Z�[�W�^�C�v�����p�\���ǂ������肷��
     * @param messageType ���b�Z�[�W�^�C�v
     * @return �w�肳�ꂽ���b�Z�[�W�^�C�v�����p�\�ȂƂ�true/�����łȂ��Ƃ�false
     */
    protected abstract boolean isEnabledFor(final char messageType);

    /**
     * �I�u�W�F�N�g�̏ڍ׏���Ԃ��܂��B<br />
     * 
     * @param obj �I�u�W�F�N�g
     * @return �ڍ׏��
     */
    public static String getObjectDescription(final Object obj)
    {
        if (obj != null)
        {
            return obj.getClass().getName() + "@" + Integer.toHexString(obj.hashCode());
        }
        else
        {
            return "NULL";
        }
    }

    /**
     * message��toString���Ăяo���B
     * null�̏ꍇ��"Unknown Error"���o�͂���B
     * 
     * @param message ���b�Z�[�W�I�u�W�F�N�g�B
     * @return message��toString���ʁBnull�̏ꍇ��"Unknown Error"
     */
    protected String createMessage(final Object message)
    {
        String messageString;

        if (message != null)
        {
            messageString = message.toString();
        }
        else
        {
            messageString = getMessage(CommonLogMessageCodes.UNEXPECTED_ERROR);
        }
        return messageString;
    }
}
