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
 ******************************************************************************/package jp.co.acroquest.endosnipe.javelin.converter.jruby;

import java.lang.reflect.Method;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.StatsJavelinRecorder;

/**
 * Javelin���O���L�^����
 * @author tanimoto
 * 
 */
public class JrubyRecorder
{
    /** JRuby��Ruby�N���X�̐擪�ɂ��镶���� */
    private static final String CLASS_NAME_HEADER = "#<";

    /** JRuby���쐬���郁�\�b�h�Ăяo���̈����̍ŏ��� */
    private static final int JRUBY_ARG_NUMS = 3;

    /**
     * �f�t�H���g�R���X�g���N�^
     */
    private JrubyRecorder()
    {
        // Do Nothing.
    }

    /** Javelin�̐ݒ�t�@�C�� */
    private static JavelinConfig config__ = new JavelinConfig();

    /**
     * �O�����B
     * 
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param args ����
     */
    public static void preProcess(String className, String methodName, final Object[] args)
    {
        try
        {
            Object[] origArgs = omitArgs(args);
            className = toSimpleFileName(className);
            methodName = toSimpleMethodName(methodName, args);
            StatsJavelinRecorder.preProcess(className, methodName, origArgs, config__, true);
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().warn(th);
        }
    }

    /**
     * �㏈���i�{�����������j�B
     * 
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param args ����
     * @param retValue �߂�l
     */
    public static void postProcessOK(String className, String methodName, final Object[] args,
            final Object retValue)
    {
        try
        {
            className = toSimpleFileName(className);
            methodName = toSimpleMethodName(methodName, args);
            StatsJavelinRecorder.postProcess(className, methodName, retValue, config__);
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().warn(th);
        }
    }

    /**
     * �㏈���i�{�������s���j�B
     * 
     * @param className �N���X��
     * @param methodName ���\�b�h��
     * @param args ����
     * @param cause ��O�̌���
     */
    public static void postProcessNG(String className, String methodName, final Object[] args,
            final Throwable cause)
    {
        try
        {
            className = toSimpleFileName(className);
            methodName = toSimpleMethodName(methodName, args);
            StatsJavelinRecorder.postProcess(className, methodName, cause, config__, true);
        }
        catch (Throwable th)
        {
            SystemLogger.getInstance().warn(th);
        }
    }

    /**
     * �ݒ�N���X��ǂݍ���
     * 
     * @param config Javelin�̐ݒ�t�@�C��
     */
    public static void setJavelinConfig(final JavelinConfig config)
    {
        JrubyRecorder.config__ = config;
    }

    /**
     * ��������AJRuby�����p�̈��������O���܂��B
     * @param args ����
     * @return JRuby�����p�̈��������O��������
     */
    protected static Object[] omitArgs(Object[] args)
    {
        if (args == null || args.length < JRUBY_ARG_NUMS)
        {
            return args;
        }

        int newSize = args.length - JRUBY_ARG_NUMS;
        Object[] result = new Object[newSize];
        if (newSize > 0)
        {
            System.arraycopy(args, 2, result, 0, args.length - JRUBY_ARG_NUMS);
        }

        return result;
    }

    /**
     * JRuby�����������N���X�����A����rb�t�@�C�����ɕϊ����܂��B<br>
     * JRuby�����������N���X���̌`���łȂ��ꍇ�́A�����̃N���X�������̂܂ܕԂ��܂��B<br>
     * <br>
     * ��j<br>
     * opt.testpj_minus_1_dot_2_dot_0.app.controllers.test_class<br>
     * �� /opt/testpj-1.2.0/app/controllers/test_class.rb<br>
     * @param className JRuby�������������\�b�h��
     * @return ����rb�t�@�C����
     */
    protected static String toSimpleFileName(String className)
    {
        if (className == null)
        {
            return null;
        }

        className = className.replaceAll("/", "\\");
        className = className.replaceAll("\\.", "/");
        className = className.replaceAll("$_dot__dot_", "..");
        className = className.replaceAll("_dot_", ".");
        className = className.replaceAll("_minus_", "-");
        className = className.replaceAll("_3a_", ":");
        className = "/" + className + ".rb";

        return className;
    }

    /**
     * JRuby�������������\�b�h�����A����rb�t�@�C���ɋL�q���ꂽ���\�b�h���ɕϊ����܂��B<br>
     * JRuby�������������\�b�h���̌`���łȂ��ꍇ�́A�����̃��\�b�h�������̂܂ܕԂ��܂��B<br>
     * <br>
     * ��j<br>
     * method__19$RUBY$render_flash_messages<br>
     * �� render_flash_messages<br>
     * @param methodName JRuby�������������\�b�h��
     * @param args ����
     * @return ����rb�t�@�C���ɋL�q���ꂽ���\�b�h��
     */
    protected static String toSimpleMethodName(String methodName, final Object[] args)
    {
        StringBuilder builder = new StringBuilder();

        if (args != null && args.length >= JRUBY_ARG_NUMS && args[1] != null)
        {
            String rubyClassName = getRubyClassName(args[1]);
            if (rubyClassName != null)
            {
                builder.append(rubyClassName).append(":");
            }
        }

        if (methodName != null)
        {
            int index = methodName.lastIndexOf('$');
            if (index > 0)
            {
                methodName = methodName.substring(index + 1);
                methodName = methodName.replaceAll("_p_", "?");
                methodName = methodName.replaceAll("_equal_", "=");
                methodName = methodName.replaceAll("_aref_", "[]");

                builder.append(methodName);
            }
        }

        return new String(builder);
    }

    /**
     * Ruby�ł̃N���X�����擾���܂��B
     * @param obj �I�u�W�F�N�g
     * @return Ruby�ł̃N���X��
     */
    private static String getRubyClassName(Object obj)
    {
        Class<?> clazz = obj.getClass();

        if ("org.jruby.RubyClass".equals(clazz.getName()))
        {
            return obj.toString();
        }
        else if ("org.jruby.RubyObject".equals(clazz.getName()))
        {
            try
            {
                Method method = clazz.getMethod("getMetaClass");
                Object result = method.invoke(obj);
                String className = toSimpleClassName(result.toString());
                return className;
            }
            catch (Throwable th)
            {
                SystemLogger.getInstance().error(
                                                 "RubyObject invocation Error - "
                                                         + clazz.toString() + "#getMetaClass", th);
            }
        }
        else
        {
            SystemLogger.getInstance().warn("Unknown Ruby Type - " + clazz.toString());
        }

        return null;
    }

    /**
     * JRuby���ň����N���X���\�L���A����Ruby�N���X���ɕϊ����܂��B<br>
     * JRuby���̃N���X���\�L�łȂ��ꍇ�́A�����̕\�L�����̂܂ܕԂ��܂��B<br>
     * <br>
     * ��j<br>
     * #<ActionView::Base:0x3f156b><br>
     * �� ActionView::Base<br>
     * #Class<#:<ActionView::Base:0x3f156b>><br>
     * �� ActionView::Base<br>
     * @param className JRuby���ň����N���X���\�L
     * @return ����Ruby�N���X��
     */
    protected static String toSimpleClassName(String className)
    {
        if (className == null || className.startsWith(CLASS_NAME_HEADER) == false)
        {
            return className;
        }

        int classEnd = className.lastIndexOf(":");
        int classStart = className.lastIndexOf(CLASS_NAME_HEADER);
        if (classEnd < 0 || classEnd < classStart + CLASS_NAME_HEADER.length())
        {
            return className;
        }

        className = className.substring(classStart + CLASS_NAME_HEADER.length(), classEnd);

        return className;
    }
}
