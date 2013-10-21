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
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.data.dto.SummarySignalDefinitionDto;

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

    public List<SummarySignalDefinitionDto> calculateSummarySignalState(
        final List<String> alarmDataList)
    {

        Set<String> summarySignalDefinitionDtoList = new TreeSet<String>();
        parChildMap = SummarySignalStateManager.getInstance().getParChildMap();
        Set<String> parentListSet = new TreeSet<String>();

        for (String changeNode : alarmDataList)
        {
            if (parChildMap.get(changeNode) != null
                && parChildMap.get(changeNode).getParentListSet() != null
                && parChildMap.get(changeNode).getParentListSet().size() > 0)
            {
                parentListSet.addAll(parChildMap.get(changeNode).getParentListSet());
            }
        }

        while (parentListSet.size() != 0)
        {
            Set<String> smalletPriorityParent = getSmalletPriorityParent(parentListSet);
            Set<String> changeSummarySignalList = new TreeSet<String>();
            for (String parent : smalletPriorityParent)
            {
                if (parChildMap.get(parent).getSignalType() == 1)
                {
                    //OR cause
                    parChildMap.get(parent).setCurrentStatus(1);
                    SummarySignalStateManager.getInstance().addSummaryAlarmData(parent);
                    SummarySignalStateManager.getInstance().getSummarySignalDefinitionMap()
                        .get(parChildMap.get(parent).nodeId).summarySignalStatus = 1;
                    summarySignalDefinitionDtoList.add(parent);
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
                            && !changeSummarySignalList.contains(child)
                            && !SummarySignalStateManager.getInstance().getAlarmSummaryList()
                                .contains(child))
                        {
                            flagAnd = false;
                            break;
                        }
                    }

                    if (flagAnd)
                    {
                        parChildMap.get(parent).setCurrentStatus(1);
                        SummarySignalStateManager.getInstance().addSummaryAlarmData(parent);
                        SummarySignalStateManager.getInstance().getSummarySignalDefinitionMap()
                            .get(parChildMap.get(parent).nodeId).summarySignalStatus = 1;
                        summarySignalDefinitionDtoList.add(parent);
                        changeSummarySignalList.add(parent);
                        parentListSet.remove(parent);
                    }
                    else
                    {
                        parentListSet.remove(parent);
                    }
                }
                parentListSet.clear();
                for (String changeNode : changeSummarySignalList)
                {

                    parentListSet.addAll(parChildMap.get(changeNode).parentListSet);
                }

            }
        }
        return createAlarmSummarySignalList(summarySignalDefinitionDtoList);
    }

    public List<SummarySignalDefinitionDto> createAlarmSummarySignalList(
        final Set<String> summarySignalDefinitionDtoList)
    {
        List<SummarySignalDefinitionDto> summarySignalList =
            new ArrayList<SummarySignalDefinitionDto>();

        for (String name : summarySignalDefinitionDtoList)
        {
            SummarySignalDefinitionDto summaryDto = new SummarySignalDefinitionDto();
            SignalStateNode nodeData = parChildMap.get(name);
            summaryDto.setSummarySignalName(nodeData.getName());
            summaryDto.setSummarySignalId(nodeData.getNodeId());
            summaryDto.setSummarySignalStatus(nodeData.getCurrentStatus());
            summaryDto.setSignalList(nodeData.getChildList());
            summaryDto.setErrorMessage("");
            summarySignalList.add(summaryDto);
        }
        return summarySignalList;
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

    public List<SummarySignalDefinitionDto> calculateChangeSummarySignalState(
        final List<SummarySignalDefinitionDto> summarySignalDefinitionList, final String process)
    {
        Set<String> summarySignalDefinitionDtoList = new TreeSet<String>();
        parChildMap = SummarySignalStateManager.getInstance().getParChildMap();
        Set<String> parentListSet = new TreeSet<String>();

        Set<String> parentUpdate = new TreeSet<String>();
        for (SummarySignalDefinitionDto summarySignal : summarySignalDefinitionList)
        {

            boolean changeFlag = false;
            if (SummarySignalStateManager.getInstance().getAlarmSummaryList()
                .contains(summarySignal.getSummarySignalName()))
            {
                changeFlag = true;

                if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_DELETE.equals(process))
                {
                    SummarySignalStateManager.getInstance().getAlarmSummaryList()
                        .remove(summarySignal.summarySignalName);
                    parentListSet
                        .addAll(parChildMap.get(summarySignal.getSummarySignalName()).parentListSet);
                }
            }
            //            SummarySignalStateManager.getInstance().getAlarmSummaryList()
            //                .remove(summarySignal.summarySignalName);

            if (!TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_DELETE.equals(process))
            {
                List<String> childList =
                    parChildMap.get(summarySignal.getSummarySignalName()).childList;

                for (String changeNode : childList)
                {
                    if (SummarySignalStateManager.getInstance().getAlarmSummaryList()
                        .contains(changeNode)
                        || SummarySignalStateManager.getInstance().getAlarmChildList()
                            .contains(changeNode))
                    {
                        parentListSet.add(summarySignal.getSummarySignalName());
                        break;
                    }
                    else if (changeFlag)
                    {
                        parentListSet.add(summarySignal.getSummarySignalName());

                        break;
                    }

                }
            }
            while (parentListSet.size() != 0)
            {
                Set<String> smalletPriorityParent = getSmalletPriorityParent(parentListSet);
                System.out.println("smallest parent " + smalletPriorityParent);
                System.out.println("parentlist " + parentListSet);
                //System.out.println("parentlist " + parentListSet);
                Set<String> changeSummarySignalList = new TreeSet<String>();
                for (String parent : smalletPriorityParent)
                {

                    if (SummarySignalStateManager.getInstance().getAlarmSummaryList()
                        .contains(parent))
                    {
                        changeFlag = true;
                    }
                    SummarySignalStateManager.getInstance().getAlarmSummaryList().remove(parent);
                    parChildMap.get(parent).setCurrentStatus(-1);
                    SummarySignalStateManager.getInstance().getSummarySignalDefinitionMap()
                        .get(parChildMap.get(parent).nodeId).summarySignalStatus = -1;

                    if (parChildMap.get(parent).getSignalType() == 1)
                    {
                        //OR cause
                        List<String> childDataList = parChildMap.get(parent).childList;
                        for (String child : childDataList)
                        {

                            if (SummarySignalStateManager.getInstance().getAlarmSummaryList()
                                .contains(child)
                                || SummarySignalStateManager.getInstance().getAlarmChildList()
                                    .contains(child))
                            {
                                parChildMap.get(parent).setCurrentStatus(1);
                                summarySignal.setSummarySignalStatus(1);
                                SummarySignalStateManager.getInstance().getAlarmSummaryList()
                                    .add(parent);
                                SummarySignalStateManager.getInstance().addSummaryAlarmData(parent);
                                SummarySignalStateManager.getInstance()
                                    .getSummarySignalDefinitionMap()
                                    .get(parChildMap.get(parent).nodeId).summarySignalStatus = 1;
                                summarySignalDefinitionDtoList.add(parent);
                                changeSummarySignalList.add(parent);

                                break;
                            }
                        }

                        parentListSet.remove(parent);

                    }
                    else if (parChildMap.get(parent).getSignalType() == 0)
                    {
                        //AND cause
                        List<String> childDataList = parChildMap.get(parent).childList;

                        boolean flagAnd = true;
                        for (String child : childDataList)
                        {

                            if (!SummarySignalStateManager.getInstance().getAlarmSummaryList()
                                .contains(child)
                                && !changeSummarySignalList.contains(child)
                                && !SummarySignalStateManager.getInstance().getAlarmChildList()
                                    .contains(child))
                            {
                                flagAnd = false;
                                break;
                            }
                        }

                        if (flagAnd)
                        {
                            parChildMap.get(parent).setCurrentStatus(1);
                            summarySignal.setSummarySignalStatus(1);
                            SummarySignalStateManager.getInstance().addSummaryAlarmData(parent);
                            SummarySignalStateManager.getInstance().getSummarySignalDefinitionMap()
                                .get(parChildMap.get(parent).nodeId).summarySignalStatus = 1;
                            SummarySignalStateManager.getInstance().addSummaryAlarmData(parent);
                            summarySignalDefinitionDtoList.add(parent);
                            changeSummarySignalList.add(parent);
                            parentListSet.remove(parent);
                        }
                        else
                        {
                            parentListSet.remove(parent);
                        }
                    }
                    parentListSet.clear();
                    if ((parChildMap.get(parent).getCurrentStatus() == -1) && changeFlag)
                    {
                        summarySignalDefinitionDtoList.add(parent);
                        changeSummarySignalList.add(parent);
                        changeFlag = false;
                    }
                    for (String changeNode : changeSummarySignalList)
                    {

                        parentListSet.addAll(parChildMap.get(changeNode).parentListSet);
                    }

                    parentUpdate.add(parent);

                }

            }
            if (parentUpdate.contains(summarySignal.getSummarySignalName()))
            {
                parentUpdate.remove(summarySignal.getSummarySignalName());
            }
            if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_DELETE.equals(process))
            {
                SummarySignalStateManager.getInstance()
                    .removeSummarySignalDefinition((int)summarySignal.getSummarySignalId());
                SummarySignalStateManager.getInstance().getParChildMap()
                    .remove(summarySignal.getSummarySignalName());
                //SummarySignalStateManager.getInstance().createAllSummarySignalMapValue();
                SummarySignalStateManager.getInstance().getAlarmSummaryList()
                    .remove(summarySignal.getSummarySignalName());
            }

        }

        //        if (process.equals(TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_UPDATE))
        //        {
        //            summarySignalDefinitionList
        //                .addAll(createAlarmSummarySignalList(summarySignalDefinitionDtoList));
        //        }
        return createAlarmSummarySignalList(summarySignalDefinitionDtoList);
    }

    public List<SummarySignalDefinitionDto> calculateChangeParentSummarySignalState(
        final List<SummarySignalDefinitionDto> summarySignalDefinitionList)
    {

        Set<String> summarySignalDefinitionDtoList = new TreeSet<String>();
        parChildMap = SummarySignalStateManager.getInstance().getParChildMap();
        Set<String> parentListSet = new TreeSet<String>();
        Set<String> parentUpdate = new TreeSet<String>();
        for (SummarySignalDefinitionDto summarySignal : summarySignalDefinitionList)
        {

            boolean changeFlag = false;
            if (SummarySignalStateManager.getInstance().getAlarmSummaryList()
                .contains(summarySignal.getSummarySignalName()))
            {
                changeFlag = true;

                parentListSet
                    .addAll(parChildMap.get(summarySignal.getSummarySignalName()).parentListSet);
            }
            while (parentListSet.size() != 0)
            {
                Set<String> smalletPriorityParent = getSmalletPriorityParent(parentListSet);
                System.out.println("smallest parent " + smalletPriorityParent);
                System.out.println("parentlist " + parentListSet);
                //System.out.println("parentlist " + parentListSet);
                Set<String> changeSummarySignalList = new TreeSet<String>();
                for (String parent : smalletPriorityParent)
                {
                    SummarySignalStateManager.getInstance().getAlarmSummaryList().remove(parent);
                    parChildMap.get(parent).setCurrentStatus(-1);
                    SummarySignalStateManager.getInstance().getSummarySignalDefinitionMap()
                        .get(parChildMap.get(parent).nodeId).summarySignalStatus = -1;

                    if (parChildMap.get(parent).getSignalType() == 1)
                    {
                        //OR cause
                        List<String> childList = parChildMap.get(parent).childList;

                        for (String changeNode : childList)
                        {
                            if (SummarySignalStateManager.getInstance().getAlarmSummaryList()
                                .contains(changeNode)
                                || SummarySignalStateManager.getInstance().getAlarmChildList()
                                    .contains(changeNode))
                            {
                                parChildMap.get(parent).setCurrentStatus(1);
                                summarySignal.setSummarySignalStatus(1);
                                SummarySignalStateManager.getInstance().getAlarmSummaryList()
                                    .add(parent);
                                SummarySignalStateManager.getInstance().addSummaryAlarmData(parent);
                                SummarySignalStateManager.getInstance()
                                    .getSummarySignalDefinitionMap()
                                    .get(parChildMap.get(parent).nodeId).summarySignalStatus = 1;
                                summarySignalDefinitionDtoList.add(parent);
                                changeSummarySignalList.add(parent);
                                break;
                            }
                        }

                        parentListSet.remove(parent);

                    }
                    else if (parChildMap.get(parent).getSignalType() == 0)
                    {
                        //AND cause
                        List<String> childDataList = parChildMap.get(parent).childList;

                        boolean flagAnd = true;
                        for (String child : childDataList)
                        {

                            if (!SummarySignalStateManager.getInstance().getAlarmSummaryList()
                                .contains(child)
                                && !changeSummarySignalList.contains(child)
                                && !SummarySignalStateManager.getInstance().getAlarmChildList()
                                    .contains(child))
                            {
                                flagAnd = false;
                                break;
                            }
                        }

                        if (flagAnd)
                        {
                            parChildMap.get(parent).setCurrentStatus(1);
                            summarySignal.setSummarySignalStatus(1);
                            SummarySignalStateManager.getInstance().addSummaryAlarmData(parent);
                            SummarySignalStateManager.getInstance().getSummarySignalDefinitionMap()
                                .get(parChildMap.get(parent).nodeId).summarySignalStatus = 1;
                            SummarySignalStateManager.getInstance().addSummaryAlarmData(parent);
                            summarySignalDefinitionDtoList.add(parent);
                            changeSummarySignalList.add(parent);
                            parentListSet.remove(parent);
                        }
                        else
                        {
                            parentListSet.remove(parent);
                        }
                    }
                    parentListSet.clear();
                    if (changeFlag)
                    {
                        summarySignalDefinitionDtoList.add(parent);
                        // changeSummarySignalList.add(parent);
                    }
                    for (String changeNode : changeSummarySignalList)
                    {

                        parentListSet.addAll(parChildMap.get(changeNode).parentListSet);
                    }

                }

            }
            SummarySignalStateManager.getInstance()
                .removeSummarySignalDefinition((int)summarySignal.getSummarySignalId());
            SummarySignalStateManager.getInstance().createAllSummarySignalMapValue();
            SummarySignalStateManager.getInstance().getAlarmSummaryList()
                .remove(summarySignal.getSummarySignalName());
        }

        return createAlarmSummarySignalList(summarySignalDefinitionDtoList);
    }
}
