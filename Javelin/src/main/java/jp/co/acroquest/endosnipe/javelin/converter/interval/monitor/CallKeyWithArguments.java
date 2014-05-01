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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * クラス名、メソッド名、引数の値をキーにします。<br />
 *
 * @author sakamoto
 */
class CallKeyWithArguments
{
    private final CallKey callKey_;

    private final List<String> args_;

    /** 不変オブジェクト＋高速化のため、ハッシュコードはあらかじめ計算しておく */
    private final int hashCode_;

    /**
     * キーを生成します。<br />
     *
     * @param callKey クラス名とメソッド名
     * @param args 引数の値
     */
    CallKeyWithArguments(final CallKey callKey, final Object... args)
    {
        List<String> argsList = new ArrayList<String>(args.length);
        for (Object element : args)
        {
            argsList.add(element.toString());
        }

        final int PRIME = 31;
        this.callKey_ = callKey;
        this.args_ = Collections.unmodifiableList(argsList);
        this.hashCode_ = callKey.hashCode() + this.args_.hashCode() * PRIME;
    }

    /**
     * クラス名とメソッド名を返します。<br />
     *
     * @return クラス名とメソッド名
     */
    CallKey getCallKey()
    {
        return this.callKey_;
    }

    /**
     * 引数の値を返します。<br />
     *
     * @return 引数の値
     */
    List<String> getArgs()
    {
        return this.args_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof CallKeyWithArguments)
        {
            CallKeyWithArguments keyWithArguments = (CallKeyWithArguments)obj;
            if (keyWithArguments.getCallKey().equals(getCallKey())
                    && keyWithArguments.getArgs().equals(getArgs()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return this.hashCode_;
    }
}
