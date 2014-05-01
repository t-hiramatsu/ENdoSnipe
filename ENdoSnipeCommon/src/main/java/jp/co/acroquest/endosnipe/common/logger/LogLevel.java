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

/**
 * �V�X�e�����O�̃��O���x����\���N���X�ł��B<br />
 * 
 * @author eriguchi
 */
public class LogLevel
{
    /** FATAL���x����int�l�B */
    public static final int FATAL_INT = 50000;

    /** ERROR���x����int�l�B */
    public static final int ERROR_INT = 40000;

    /** WARN���x����int�l�B */
    public static final int WARN_INT = 30000;

    /** INFO���x����int�l�B */
    public static final int INFO_INT = 20000;

    /** DEBUG���x����int�l�B */
    public static final int DEBUG_INT = 10000;

    /** FATAL���x���B */
    public static final LogLevel FATAL = new LogLevel(FATAL_INT, "FATAL");

    /** ERROR���x���B */
    public static final LogLevel ERROR = new LogLevel(ERROR_INT, "ERROR");

    /** WARN���x���B */
    public static final LogLevel WARN = new LogLevel(WARN_INT, "WARN");

    /** INFO���x���B */
    public static final LogLevel INFO = new LogLevel(INFO_INT, "INFO");

    /** DEBUG���x���B */
    public static final LogLevel DEBUG = new LogLevel(DEBUG_INT, "DEBUG");

    /** ���O���x����int�l�B */
    private final int level_;

    /** ���O���x���̖��́B */
    private final String levelStr_;

    /**
     * {@link LogLevel} ���\�z���܂��B<br />
     * 
     * @param level ���O���x���̒l
     * @param levelStr ���O���x���̖���
     */
    public LogLevel(final int level, final String levelStr)
    {
        this.level_ = level;
        this.levelStr_ = levelStr;
    }

    /**
     * ���O���x���̖��̂��擾���܂��B<br />
     * 
     * @return ���O���x���̖���
     */
    public String getLevelStr()
    {
        return levelStr_;
    }

    /**
     * ���O���x���̒l���擾���܂��B<br />
     * 
     * @return ���O���x����in�l
     */
    public int getLevel()
    {
        return level_;
    }
}
