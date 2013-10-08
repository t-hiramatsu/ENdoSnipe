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
package jp.co.acroquest.endosnipe.collector.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import jp.co.acroquest.endosnipe.collector.data.SignalStateNode;
import jp.co.acroquest.endosnipe.collector.manager.SummarySignalStateManager;

public class SignalSummarizer
{
    private static SignalSummarizer instance__ = new SignalSummarizer();

    private static Map<String, SignalStateNode> parChildMap = null;

    public String dataBaseName;

    public static SignalSummarizer getInstance()
    {
        return instance__;
    }

    public static void setInstance(final SignalSummarizer instance)
    {
        instance__ = instance;
    }

    public void calculateSummarySignalState(final List<String> alarmDataList)
    {

        Set<String> summarySignalDefinitionDtoList = new TreeSet<String>();
        parChildMap = SummarySignalStateManager.getInstance().getParChildMap();
        Set<String> parentListSet = new TreeSet<String>();

        for (String changeNode : alarmDataList)
        {
            parentListSet.addAll(parChildMap.get(changeNode).getParentListSet());
        }
        Set<String> smalletPriorityParent = getSmalletPriorityParent(parentListSet);

        while (parentListSet.size() != 0)
        {
            Set<String> changeSummarySignalList = new TreeSet<String>();
            for (String parent : smalletPriorityParent)
            {
                if (parChildMap.get(parent).getSignalType() == 1)
                {
                    //OR cause
                    parChildMap.get(parent).setCurrentStatus(1);
                    changeSummarySignalList.add(parent);
                    parentListSet.remove(parent);

                }
                else if (parChildMap.get(parent).getSignalType() == 0)
                {
                    //AND cause
                    List<String> childList = parChildMap.get(parent).childList;

                    boolean flagAnd = true;
                    for (String child : childList)
                    {
                        if (!alarmDataList.contains(child)
                            && !changeSummarySignalList.contains(child))
                        {
                            flagAnd = false;
                            break;
                        }
                    }

                    if (flagAnd)
                    {
                        parChildMap.get(parent).setCurrentStatus(1);
                        changeSummarySignalList.add(parent);
                        parentListSet.remove(parent);
                    }
                }
                for (String changeNode : changeSummarySignalList)
                {
                    parentListSet.clear();
                    parentListSet.addAll(changeSummarySignalList);
                }

            }
        }

    }

    public Set<String> getSmalletPriorityParent(final Set<String> parentListSet)
    {
        Set<String> smallestPrioritySummary = new TreeSet<String>();

        int maxPriority = calculateMinPriority(parentListSet);

        for (String parent : parentListSet)
        {
            if (parChildMap.get(parent).priority == maxPriority)
            {
                smallestPrioritySummary.add(parent);
            }
        }
        return smallestPrioritySummary;
    }

    private int calculateMinPriority(final Set<String> parentListSet)
    {
        List<String> parentList = new ArrayList<String>(parentListSet);
        int min = 1;
        if (parentList != null && parentList.size() > 0)
        {
            min = parChildMap.get(parentList.get(0)).priority;

            for (int count = 1; count < parentList.size(); count++)
            {
                if (min > parChildMap.get(parentList.get(count)).priority)
                {
                    min = parChildMap.get(parentList.get(count)).priority;
                }
            }
        }

        return min;
    }
}
