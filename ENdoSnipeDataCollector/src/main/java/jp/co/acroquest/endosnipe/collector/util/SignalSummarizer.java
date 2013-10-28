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

/**
 * SummarySignalの summarizer of signal
 * @author pin
 *
 */
public class SignalSummarizer
{
    private static SignalSummarizer instance__ = new SignalSummarizer();

    /**pair of parent and child 。 */
    private static Map<String, SignalStateNode> parChildMap__ = null;

    /**dataBase Name */
    public String dataBaseName_;

    /**
     * {@link SignalSummarizer}インスタンスを取得する。
     * @return {@link SignalSummarizer}インスタンス
     */
    public static SignalSummarizer getInstance()
    {
        return instance__;
    }

    /**
     * calculate the status of parent summary signal <br/>
     * according to the change child signal
     * @param alarmDataList child signal 
     * @return SummarySignalDefinitionDto list
     */
    public List<SummarySignalDefinitionDto> calculateSummarySignalState(
        final List<String> alarmDataList)
    {

        Set<String> summarySignalDefinitionDtoList = new TreeSet<String>();
        parChildMap__ = SummarySignalStateManager.getInstance().getParChildMap();
        Set<String> parentListSet = new TreeSet<String>();

        for (String changeNode : alarmDataList)
        {
            if (parChildMap__.get(changeNode) != null
                && parChildMap__.get(changeNode).getParentListSet() != null
                && parChildMap__.get(changeNode).getParentListSet().size() > 0)
            {
                parentListSet.addAll(parChildMap__.get(changeNode).getParentListSet());
            }
        }

        while (parentListSet.size() != 0)
        {
            Set<String> smalletPriorityParent = getSmalletPriorityParent(parentListSet);
            Set<String> changeSummarySignalList = new TreeSet<String>();
            for (String parent : smalletPriorityParent)
            {
                if (parChildMap__.get(parent).getSignalType() == 1)
                {
                    //OR cause
                    parChildMap__.get(parent).setCurrentStatus(1);
                    SummarySignalStateManager.getInstance().addSummaryAlarmData(parent);
                    SummarySignalStateManager.getInstance().getSummarySignalDefinitionMap()
                        .get(parChildMap__.get(parent).nodeId).summarySignalStatus_ = 1;
                    summarySignalDefinitionDtoList.add(parent);
                    changeSummarySignalList.add(parent);
                    parentListSet.remove(parent);

                }
                else if (parChildMap__.get(parent).getSignalType() == 0)
                {
                    //AND cause
                    List<String> childList = parChildMap__.get(parent).childList;

                    boolean flagAnd = true;
                    for (String child : childList)
                    {

                        if (!SummarySignalStateManager.getInstance().getAlarmChildList()
                            .contains(child)
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
                        parChildMap__.get(parent).setCurrentStatus(1);
                        SummarySignalStateManager.getInstance().addSummaryAlarmData(parent);
                        SummarySignalStateManager.getInstance().getSummarySignalDefinitionMap()
                            .get(parChildMap__.get(parent).nodeId).summarySignalStatus_ = 1;
                        summarySignalDefinitionDtoList.add(parent);
                        changeSummarySignalList.add(parent);
                        parentListSet.remove(parent);
                    }
                    else
                    {
                        parentListSet.remove(parent);
                    }
                }
                for (String changeNode : changeSummarySignalList)
                {

                    parentListSet.addAll(parChildMap__.get(changeNode).parentListSet);
                }

            }
        }
        return createAlarmSummarySignalList(summarySignalDefinitionDtoList, null);
    }

    /**
     * create summary signal definition name list to object list
     * @param summarySignalDefinitionDtoList name list
     * @param currentSignal current change summary signal
     * @return SummarySignalDefinitionDto list
     */
    public List<SummarySignalDefinitionDto> createAlarmSummarySignalList(
        final Set<String> summarySignalDefinitionDtoList,
        final SummarySignalDefinitionDto currentSignal)
    {
        List<SummarySignalDefinitionDto> summarySignalList =
            new ArrayList<SummarySignalDefinitionDto>();

        for (String name : summarySignalDefinitionDtoList)
        {
            SummarySignalDefinitionDto summaryDto = new SummarySignalDefinitionDto();
            SignalStateNode nodeData = parChildMap__.get(name);
            summaryDto.setSummarySignalName(nodeData.getName());
            summaryDto.setSummarySignalId(nodeData.getNodeId());
            summaryDto.setSummarySignalStatus(nodeData.getCurrentStatus());
            summaryDto.setSignalList(nodeData.getChildList());
            if (currentSignal != null && name.equals(currentSignal.getSummarySignalName()))
            {
                summaryDto.setErrorMessage(currentSignal.getErrorMessage());
            }
            else
            {
                summaryDto.setErrorMessage("");
            }
            summarySignalList.add(summaryDto);
        }
        return summarySignalList;
    }

    /**
     * calcualte the smallest parent among the parents
     * @param parentListSet name list
     * @return smallest priority parent list
     */
    public Set<String> getSmalletPriorityParent(final Set<String> parentListSet)
    {
        Set<String> smallestPrioritySummary = new TreeSet<String>();

        int maxPriority = calculateMinPriority(parentListSet);

        for (String parent : parentListSet)
        {
            if (parChildMap__.get(parent).priority == maxPriority)
            {
                smallestPrioritySummary.add(parent);
            }
        }
        return smallestPrioritySummary;
    }

    /**
     * calculate the minimum priority among the parents
     * @param parentListSet name list
     * @return priority number
     */
    private int calculateMinPriority(final Set<String> parentListSet)
    {
        List<String> parentList = new ArrayList<String>(parentListSet);
        int min = 1;
        if (parentList != null && parentList.size() > 0)
        {
            min = parChildMap__.get(parentList.get(0)).priority;

            for (int count = 1; count < parentList.size(); count++)
            {
                if (min > parChildMap__.get(parentList.get(count)).priority)
                {
                    min = parChildMap__.get(parentList.get(count)).priority;
                }
            }
        }

        return min;
    }

    /**
     * calculate the state of summary signal when add,update and delete process
     * @param summarySignalDefinitionList summary signal list
     * @param process kind of telegram
     * @return SummarySignalDefinitionDto list
     */
    public List<SummarySignalDefinitionDto> calculateChangeSummarySignalState(
        final List<SummarySignalDefinitionDto> summarySignalDefinitionList, final String process)
    {
        Set<String> summarySignalDefinitionDtoList = new TreeSet<String>();
        parChildMap__ = SummarySignalStateManager.getInstance().getParChildMap();
        Set<String> parentListSet = new TreeSet<String>();
        Set<String> parentUpdate = new TreeSet<String>();
        for (SummarySignalDefinitionDto summarySignal : summarySignalDefinitionList)
        {
            boolean changeFlag = false;
            if (SummarySignalStateManager.getInstance().getAlarmSummaryList()
                .contains(summarySignal.getSummarySignalName()))
            {
                changeFlag = true;

            }
            if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_DELETE.equals(process))
            {

                SummarySignalStateManager.getInstance().getAlarmSummaryList()
                    .remove(summarySignal.summarySignalName_);
                if (changeFlag)
                {
                    parentListSet
                        .addAll(parChildMap__.get(summarySignal.getSummarySignalName()).parentListSet);
                }
            }
            if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_ADD.equals(process))
            {
                parentListSet.add(summarySignal.getSummarySignalName());
            }

            if (TelegramConstants.ITEMNAME_SUMMARY_SIGNAL_UPDATE.equals(process))
            {
                List<String> childList =
                    parChildMap__.get(summarySignal.getSummarySignalName()).childList;

                if (childList != null && childList.size() == 0)
                {
                    if (changeFlag)
                    {
                        summarySignalDefinitionDtoList.add(summarySignal.getSummarySignalName());
                        SummarySignalStateManager.getInstance().getAlarmSummaryList()
                            .remove(summarySignal.getSummarySignalName());
                        parChildMap__.get(summarySignal.getSummarySignalName())
                            .setCurrentStatus(-1);
                        SummarySignalStateManager.getInstance().getSummarySignalDefinitionMap()
                            .get(parChildMap__.get(summarySignal.getSummarySignalName()).nodeId).summarySignalStatus_ =
                            -1;
                        changeFlag = false;
                    }
                }
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
                Set<String> changeSummarySignalList = new TreeSet<String>();
                for (String parent : smalletPriorityParent)
                {

                    if (SummarySignalStateManager.getInstance().getAlarmSummaryList()
                        .contains(parent))
                    {
                        changeFlag = true;
                    }
                    SummarySignalStateManager.getInstance().getAlarmSummaryList().remove(parent);
                    parChildMap__.get(parent).setCurrentStatus(-1);
                    SummarySignalStateManager.getInstance().getSummarySignalDefinitionMap()
                        .get(parChildMap__.get(parent).nodeId).summarySignalStatus_ = -1;
                    if (parChildMap__.get(parent).getSignalType() == 1)
                    {
                        //OR cause
                        List<String> childDataList = parChildMap__.get(parent).childList;
                        for (String child : childDataList)
                        {
                            if (SummarySignalStateManager.getInstance().getAlarmSummaryList()
                                .contains(child)
                                || SummarySignalStateManager.getInstance().getAlarmChildList()
                                    .contains(child))
                            {
                                parChildMap__.get(parent).setCurrentStatus(1);
                                summarySignal.setSummarySignalStatus(1);
                                SummarySignalStateManager.getInstance().getAlarmSummaryList()
                                    .add(parent);
                                SummarySignalStateManager.getInstance().addSummaryAlarmData(parent);
                                SummarySignalStateManager.getInstance()
                                    .getSummarySignalDefinitionMap()
                                    .get(parChildMap__.get(parent).nodeId).summarySignalStatus_ = 1;
                                summarySignalDefinitionDtoList.add(parent);
                                changeSummarySignalList.add(parent);
                                break;
                            }
                        }
                        parentListSet.remove(parent);
                    }
                    else if (parChildMap__.get(parent).getSignalType() == 0)
                    {
                        //AND cause
                        List<String> childDataList = parChildMap__.get(parent).childList;

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
                            parChildMap__.get(parent).setCurrentStatus(1);
                            summarySignal.setSummarySignalStatus(1);
                            SummarySignalStateManager.getInstance().addSummaryAlarmData(parent);
                            SummarySignalStateManager.getInstance().getSummarySignalDefinitionMap()
                                .get(parChildMap__.get(parent).nodeId).summarySignalStatus_ = 1;
                            SummarySignalStateManager.getInstance().addSummaryAlarmData(parent);
                            summarySignalDefinitionDtoList.add(parent);
                            changeSummarySignalList.add(parent);
                        }
                        parentListSet.remove(parent);
                    }
                    if ((parChildMap__.get(parent).getCurrentStatus() == -1) && changeFlag)
                    {
                        summarySignalDefinitionDtoList.add(parent);
                        changeSummarySignalList.add(parent);
                        changeFlag = false;
                    }
                    for (String changeNode : changeSummarySignalList)
                    {
                        parentListSet.addAll(parChildMap__.get(changeNode).parentListSet);
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
                SummarySignalStateManager.getInstance().getAlarmSummaryList()
                    .remove(summarySignal.getSummarySignalName());
            }

        }

        return createAlarmSummarySignalList(summarySignalDefinitionDtoList,
                                            summarySignalDefinitionList.get(0));
    }

}
