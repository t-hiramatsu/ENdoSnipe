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
 * Javelinログを記録する
 * @author tanimoto
 * 
 */
public class JrubyRecorder
{
    /** JRubyがRubyクラスの先頭につける文字列 */
    private static final String CLASS_NAME_HEADER = "#<";

    /** JRubyが作成するメソッド呼び出しの引数の最小数 */
    private static final int JRUBY_ARG_NUMS = 3;

    /**
     * デフォルトコンストラクタ
     */
    private JrubyRecorder()
    {
        // Do Nothing.
    }

    /** Javelinの設定ファイル */
    private static JavelinConfig config__ = new JavelinConfig();

    /**
     * 前処理。
     * 
     * @param className クラス名
     * @param methodName メソッド名
     * @param args 引数
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
     * 後処理（本処理成功時）。
     * 
     * @param className クラス名
     * @param methodName メソッド名
     * @param args 引数
     * @param retValue 戻り値
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
     * 後処理（本処理失敗時）。
     * 
     * @param className クラス名
     * @param methodName メソッド名
     * @param args 引数
     * @param cause 例外の原因
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
     * 設定クラスを読み込む
     * 
     * @param config Javelinの設定ファイル
     */
    public static void setJavelinConfig(final JavelinConfig config)
    {
        JrubyRecorder.config__ = config;
    }

    /**
     * 引数から、JRuby内部用の引数を除外します。
     * @param args 引数
     * @return JRuby内部用の引数を除外した引数
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
     * JRubyが生成したクラス名を、元のrbファイル名に変換します。<br>
     * JRubyが生成したクラス名の形式でない場合は、引数のクラス名をそのまま返します。<br>
     * <br>
     * 例）<br>
     * opt.testpj_minus_1_dot_2_dot_0.app.controllers.test_class<br>
     * → /opt/testpj-1.2.0/app/controllers/test_class.rb<br>
     * @param className JRubyが生成したメソッド名
     * @return 元のrbファイル名
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
     * JRubyが生成したメソッド名を、元のrbファイルに記述されたメソッド名に変換します。<br>
     * JRubyが生成したメソッド名の形式でない場合は、引数のメソッド名をそのまま返します。<br>
     * <br>
     * 例）<br>
     * method__19$RUBY$render_flash_messages<br>
     * → render_flash_messages<br>
     * @param methodName JRubyが生成したメソッド名
     * @param args 引数
     * @return 元のrbファイルに記述されたメソッド名
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
     * Rubyでのクラス名を取得します。
     * @param obj オブジェクト
     * @return Rubyでのクラス名
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
     * JRuby内で扱うクラス名表記を、元のRubyクラス名に変換します。<br>
     * JRuby内のクラス名表記でない場合は、引数の表記をそのまま返します。<br>
     * <br>
     * 例）<br>
     * #<ActionView::Base:0x3f156b><br>
     * → ActionView::Base<br>
     * #Class<#:<ActionView::Base:0x3f156b>><br>
     * → ActionView::Base<br>
     * @param className JRuby内で扱うクラス名表記
     * @return 元のRubyクラス名
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
