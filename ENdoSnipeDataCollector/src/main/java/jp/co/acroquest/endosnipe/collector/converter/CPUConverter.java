/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Acroquest Technology Co.,Ltd.
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
package jp.co.acroquest.endosnipe.collector.converter;

import jp.co.acroquest.endosnipe.common.util.ResourceDataUtil;

/**
 * calculate CPU usage
 * 
 * @author Khine Wai
 *
 */

public class CPUConverter
{
    /**
     * default constructor of CUPConverter class
     */
    private CPUConverter()
    {

    }

    /**
     * calculate CPU time
     * 
     * @param cpuTime is cpu time get from JavelinDataLogger
     * @param measurementInterval is measurementInterval time
     * @param processorCount is number of cup processor count
     * @return cpuUsage for cpuUsage
     */

    public static double calcCPUUsage(final long cpuTime, final long measurementInterval,
            final long processorCount)
    {
        double cpuUsage = 0.0;
        if (measurementInterval * processorCount > 0)
        {
            cpuUsage =
                       (double)cpuTime
                               / (measurementInterval * ResourceDataUtil.NANO_TO_MILI * processorCount)
                               * ResourceDataUtil.PERCENT_CONST;
            // パフォーマンスカウンタの仕様上、CPU使用率が100％を超えたり、0％を下回ることがあるため、
            // 最大100％、最小0％に丸める。（#2006）
            cpuUsage = Math.min(cpuUsage, ResourceDataUtil.MAX_CPU_RATE);
            cpuUsage = Math.max(cpuUsage, ResourceDataUtil.MIN_CPU_RATE);
        }
        return cpuUsage;
    }
}
