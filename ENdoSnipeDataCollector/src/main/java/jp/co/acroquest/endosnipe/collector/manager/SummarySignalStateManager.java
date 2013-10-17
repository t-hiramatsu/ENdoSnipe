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
package jp.co.acroquest.endosnipe.collector.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.collector.config.DataCollectorConfig;
import jp.co.acroquest.endosnipe.collector.data.SignalStateNode;
import jp.co.acroquest.endosnipe.collector.util.SignalSummarizer;
import jp.co.acroquest.endosnipe.data.dao.SummarySignalDefinitionDao;
import jp.co.acroquest.endosnipe.data.dto.SummarySignalDefinitionDto;
import jp.co.acroquest.endosnipe.data.entity.SummarySignalDefinition;

public class SummarySignalStateManager
{
    private static SummarySignalStateManager instance__ = new SummarySignalStateManager();

    private Map<Long, SummarySignalDefinitionDto> summarySignalDefinitionMap_ =
        new ConcurrentHashMap<Long, SummarySignalDefinitionDto>();

    private static Map<String, SignalStateNode> parChildMap =
        new HashMap<String, SignalStateNode>();

    protected SummarySignalDefinitionDao summarySignalDefinitionDao =
        new SummarySignalDefinitionDao();

    /** アラームを出すかどうか判定するために保持し続けるデータ */
    private final Set<String> alarmChildList_ = new TreeSet<String>();

    /** アラームを出すかどうか判定するために保持し続けるデータ */
    private final Set<String> alarmSummaryList_ = new TreeSet<String>();

    private Set changeSummarySignalListSet;

    private DataCollectorConfig config_;

    public String dataBaseName;

    public void setSummarySignalDefinitionMap(
        final Map<Long, SummarySignalDefinitionDto> summarySignalDefinitionMap)
    {
        summarySignalDefinitionMap_ = summarySignalDefinitionMap;
    }

    public static SummarySignalStateManager getInstance()
    {
        return instance__;
    }

    public Map<Long, SummarySignalDefinitionDto> getSummarySignalDefinitionMap()
    {
        return summarySignalDefinitionMap_;
    }

    public static void setInstance(final SummarySignalStateManager instance)
    {
        instance__ = instance;
    }

    public void addChildAlarmData(final String signalId)
    {
        this.alarmChildList_.add(signalId);
    }

    public void addSummaryAlarmData(final String summarySignalId)
    {
        this.alarmSummaryList_.add(summarySignalId);
    }

    public Map<String, SignalStateNode> getParChildMap()
    {
        return parChildMap;
    }

    public Set<String> getAlarmSummaryList()
    {
        return alarmSummaryList_;
    }

    public Set<String> getAlarmChildList()
    {
        return alarmChildList_;
    }

    public void addSignalDefinition(final long summarySignalId,
        final SummarySignalDefinitionDto summarySignalDefinition)
    {
        if (this.summarySignalDefinitionMap_ == null)
        {
            this.summarySignalDefinitionMap_ =
                new ConcurrentHashMap<Long, SummarySignalDefinitionDto>();
        }
        this.summarySignalDefinitionMap_.put(summarySignalId, summarySignalDefinition);
    }

    public SummarySignalDefinitionDto removeSummarySignalDefinition(final int summarySignalId)
    {
        if (this.summarySignalDefinitionMap_ == null)
        {
            return null;
        }
        return this.summarySignalDefinitionMap_.remove(summarySignalId);
    }

    // getSummarySignalDefinitionMap

    // pulic Map<SummarySignalDefinition> summarySignalDefinitionMap()
    /*   public Map<Long, SummarySignalDefinitionDto> SummarySignalDefinitionMap()
       {
           String databaseName = config_.getDatabaseName();
           try
           {
               List<SummarySignalDefinition> summarSignalDefinition =
                   summarySignalDefinitionDao.selectAll(databaseName);
           }
           catch (SQLException ex)
           {
               ex.printStackTrace();
           }
           return null;
       }*/

    public void addSummarySignalDefinition(
        final SummarySignalDefinitionDto summarySignalDefinitionDto)
    {

        long summarySignalId = 0;
        String summarySignalName = summarySignalDefinitionDto.getSummarySignalName();

        summarySignalDefinitionDto.setSummarySignalStatus(-1);
        // this.createAllSummarySignalMapValue();
        boolean isDuplicate = this.checkDuplicate(summarySignalName);
        summarySignalDefinitionDto.summarySignalStatus = 0;
        summarySignalDefinitionDto.errorMessage = "";
        if (isDuplicate == true)
        {
            summarySignalDefinitionDto.errorMessage = "Summary signal name cannot be duplicate";
        }
        else
        {
            try
            {
                this.calculatePriority(summarySignalDefinitionDto,
                                       summarySignalDefinitionDto.getSignalList());

                SummarySignalDefinition summarySignalDefinition =
                    this.convertSummarySignalDefinition(summarySignalDefinitionDto);

                summarySignalDefinitionDao.insert(this.dataBaseName, summarySignalDefinition);
                summarySignalId =
                    summarySignalDefinitionDao.selectSequenceNum(this.dataBaseName,
                                                                 summarySignalDefinition);
                summarySignalDefinitionDto.summarySignalId = summarySignalId;
                this.summarySignalDefinitionMap_.put(summarySignalId, summarySignalDefinitionDto);
                // return summarySignalDefinitionDto;
                createAllSummarySignalMapValue();

            }
            catch (SQLException ex)
            {
                // TODO �����������ꂽ catch �u���b�N
                ex.printStackTrace();
            }
            // return summarySignalDefinitionDto;
        }
    }

    private void calculatePriority(final SummarySignalDefinitionDto summarySignalDefinitionDto,
        final List<String> signalList)
        throws SQLException
    {
        List<SummarySignalDefinition> summarySignalList = null;
        Map<String, SummarySignalDefinition> summarySignalDefinition = null;
        SummarySignalDefinition summaryDefinition = new SummarySignalDefinition();
        int childMaxPriority = 0;
        try
        {
            summarySignalDefinition = summarySignalDefinitionDao.selectAllSignalName(dataBaseName);
        }
        catch (SQLException ex)
        {
            // TODO �����������ꂽ catch �u���b�N
            ex.printStackTrace();
        }

        if (summarySignalDefinition != null && summarySignalDefinition.size() > 0)
        {
            for (String signalName : signalList)
            {
                if (summarySignalDefinition.containsKey(signalName))
                {
                    summaryDefinition = summarySignalDefinition.get(signalName);
                    int priority = summaryDefinition.getPriority();
                    if (childMaxPriority < priority)
                    {
                        childMaxPriority = priority;
                    }
                    System.out.println(priority);
                }
            }
        }
        if (childMaxPriority >= summarySignalDefinitionDto.getPriority())
        {
            summarySignalDefinitionDto.priority = childMaxPriority + 1;
        }
        //        String summarySignalName = summarySignalDefinitionDto.getSummarySignalName();
        //        SignalStateNode signalStateNode = new SignalStateNode();
        //        signalStateNode = parChildMap.get(summarySignalName);
        //        if (signalStateNode != null && signalStateNode.getParentListSet() != null
        //            && signalStateNode.getParentListSet().size() > 0)
        //        {
        //            this.reCalculatePriority(summarySignalDefinitionDto, signalStateNode.getParentListSet());
        //        }
    }

    private void reCalculatePriority(final SummarySignalDefinitionDto childName,
        final Set<String> parentListSet)
        throws SQLException
    {
        if (parentListSet == null || parentListSet.size() == 0)
        {
            return;
        }
        else
        {
            for (String parentSummarySignal : parentListSet)
            {
                // if(childName.getPriority() >= parentSummarySignal.)
                if (childName.priority >= parChildMap.get(parentSummarySignal).priority)
                {
                    parChildMap.get(parentSummarySignal).priority = childName.priority + 1;
                    System.out.println("parent priority "
                        + parChildMap.get(parentSummarySignal).priority);
                    SummarySignalDefinition parentDef = new SummarySignalDefinition();
                    parentDef.summarySignalName = parentSummarySignal;
                    parentDef.summarySignalId =
                        parChildMap.get(parentDef.summarySignalName).getNodeId();

                    parentDef.priority = parChildMap.get(parentSummarySignal).priority;
                    SummarySignalDefinitionDto parendDto =
                        new SummarySignalDefinitionDto(parentDef);
                    summarySignalDefinitionDao.update(dataBaseName, parendDto.summarySignalId,
                                                      parendDto, true);
                    //                    long parentId =
                    //                        summarySignalDefinitionDao
                    //                            .selectSequenceNum(parentSummarySignal, parentDef);
                    this.summarySignalDefinitionMap_.get(parendDto.summarySignalId).priority =
                        parendDto.priority;
                    reCalculatePriority(parendDto,
                                        parChildMap.get(parentSummarySignal).parentListSet);
                }
            }
        }

    }

    private SummarySignalDefinition convertSummarySignalDefinition(
        final SummarySignalDefinitionDto definitionDto)
    {
        SummarySignalDefinition summarySignalInfo = new SummarySignalDefinition();

        summarySignalInfo.summarySignalId = definitionDto.getSummarySignalId();
        summarySignalInfo.summarySignalName = definitionDto.getSummarySignalName();
        summarySignalInfo.summarySignalType = definitionDto.getSummmarySignalType();
        summarySignalInfo.signalList = definitionDto.getSignalList();
        summarySignalInfo.errorMessage = definitionDto.getErrorMessage();
        summarySignalInfo.priority = definitionDto.getPriority();
        summarySignalInfo.summarySignalStatus = definitionDto.getSummarySignalStatus();
        //summarySignalInfo.priority = 1;
        //   summarySignalInfo.errorMessage = definitionDto.getErrorMessage();
        return summarySignalInfo;
    }

    private boolean checkDuplicate(final String summarySignalName)
    {
        List<SummarySignalDefinition> summarySignalDefinition = null;
        try
        {
            summarySignalDefinition =
                summarySignalDefinitionDao.selectByName(this.dataBaseName, summarySignalName);
        }
        catch (SQLException ex)
        {
            // TODO �����������ꂽ catch �u���b�N
            // ex.printStackTrace();
        }
        if (summarySignalDefinition != null && summarySignalDefinition.size() > 0)
        {
            return true;
        }

        return false;
    }

    public void setDataBaseName(final String dataBase)
    {
        System.out.println(dataBase);
        this.dataBaseName = dataBase;
    }

    public void createAllSummarySignalMapValue()
    {
        parChildMap.clear();
        List<String> childData = new ArrayList<String>();
        //  List<String> childDataList = new ArrayList<String>();
        try
        {
            List<SummarySignalDefinition> summarySignalList =
                summarySignalDefinitionDao.selectAllByPriority(this.dataBaseName);
            for (SummarySignalDefinition summarySignal : summarySignalList)
            {
                childData = summarySignal.getSignalList();
                String currentSignalName = summarySignal.getSummarySignalName();

                if (!parChildMap.containsKey(currentSignalName))
                {
                    SignalStateNode signalStateNode = new SignalStateNode();
                    signalStateNode.name = summarySignal.getSummarySignalName();
                    signalStateNode.priority = summarySignal.getPriority();
                    signalStateNode.childList = summarySignal.getSignalList();
                    signalStateNode.signalType = summarySignal.getSummarySignalType();
                    signalStateNode.nodeId = summarySignal.getSummarySignalId();
                    parChildMap.put(currentSignalName, signalStateNode);
                }
                else
                {
                    parChildMap.get(currentSignalName).name = summarySignal.getSummarySignalName();
                    parChildMap.get(currentSignalName).priority = summarySignal.getPriority();
                    parChildMap.get(currentSignalName).childList = summarySignal.getSignalList();
                    parChildMap.get(currentSignalName).nodeId = summarySignal.getSummarySignalId();
                    parChildMap.get(currentSignalName).signalType =
                        summarySignal.getSummarySignalType();
                    // parChildMap.get(currentSignalName).getParentListSet().add(currentSignalName);
                }
                for (String child : childData)
                {
                    if (!parChildMap.containsKey(child))
                    {
                        SignalStateNode signalState = new SignalStateNode();
                        signalState.name = child;
                        signalState.priority = 0;
                        signalState.parentListSet.add(currentSignalName);
                        signalState.signalType = -1;
                        signalState.nodeId = -1;
                        parChildMap.put(child, signalState);
                    }
                    else
                    {
                        SignalStateNode signalStateNodes = new SignalStateNode();
                        parChildMap.get(child).getParentListSet().add(currentSignalName);
                        //   signalStateNodes = parChildMap.get(child);
                        //  signalStateNode.parentListSet.add()
                        //  parChildMap.put(currentSignalName, signalStateNode);
                    }
                }
            }

            /////////////tempory

            System.out.println("//////////////////size of map : " + parChildMap.size());
            Set<String> datakey = parChildMap.keySet();
            Iterator<String> node = datakey.iterator();
            while (node.hasNext())
            {
                String st = node.next();
                SignalStateNode nodedata = parChildMap.get(st);
                System.out.println(st);
                System.out.println(nodedata.toString());
                System.out.println();
            }
            /////////////tempory

        }
        catch (SQLException ex)
        {
            // TODO �����������ꂽ catch �u���b�N
            ex.printStackTrace();
        }
    }

    public List<SummarySignalDefinition> getAllSummarySignalDefinition()
    {
        List<SummarySignalDefinition> summarySignalList = new ArrayList<SummarySignalDefinition>();
        try
        {
            summarySignalList = summarySignalDefinitionDao.selectAll(this.dataBaseName);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return summarySignalList;
    }

    public SummarySignalDefinitionDto deleteSummarySignalDefinition(final String summarySignalName)
    {
        List<SummarySignalDefinitionDto> sumList = new ArrayList<SummarySignalDefinitionDto>();
        SummarySignalDefinition sumDef = new SummarySignalDefinition();
        SummarySignalDefinitionDto sumDefDto = null;
        try
        {
            int summarySignalId = -1;

            sumDef.summarySignalName = summarySignalName;
            sumDef.errorMessage = "Cannot Delete";
            //            summarySignalId =
            //                summarySignalDefinitionDao.selectSequenceNum(this.dataBaseName, sumDef);
            //            
            summarySignalId = (int)parChildMap.get(summarySignalName).nodeId;
            sumDef.setSummarySignalId(summarySignalId);
            if (summarySignalId != -1)
            {

                summarySignalDefinitionDao.delete(this.dataBaseName, summarySignalName);
                sumDef.errorMessage = "";
            }

            //  removeSummarySignalDefinition(summarySignalId);
            //   createAllSummarySignalMapValue();
            CheckDeleteNode(summarySignalName);
            sumDefDto = new SummarySignalDefinitionDto(sumDef);
            sumList.add(sumDefDto);
            sumList =
                SignalSummarizer.getInstance().calculateChangeParentSummarySignalState(sumList);
            //  sumList.add(sumDefDto);
            removeSummarySignalDefinition((int)sumDefDto.getSummarySignalId());
            createAllSummarySignalMapValue();
            alarmSummaryList_.remove(summarySignalName);
            //   alarmSummaryList_.remove(summarySignalName);
        }
        catch (SQLException ex)
        {
            // TODO �����������ꂽ catch �u���b�N
            ex.printStackTrace();

        }
        finally
        {
            if (sumDefDto == null)
            {
                sumDefDto = new SummarySignalDefinitionDto(sumDef);
                // sumList.add(sumDefDto);
            }

        }
        return sumDefDto;
    }

    public void CheckDeleteNode(final String summarySignalName)
        throws SQLException
    {

        if (this.alarmChildList_.contains(summarySignalName))
        {
            this.alarmChildList_.remove(summarySignalName);
        }
        for (Entry<Long, SummarySignalDefinitionDto> summarySignalDtoList : summarySignalDefinitionMap_
            .entrySet())
        {
            SummarySignalDefinitionDto summarySignalDto = summarySignalDtoList.getValue();

            if (summarySignalDto.getSignalList().contains(summarySignalName))
            {
                summarySignalDto.getSignalList().remove(summarySignalName);
                summarySignalDefinitionMap_.put(summarySignalDto.getSummarySignalId(),
                                                summarySignalDto);
                summarySignalDefinitionDao.update(dataBaseName,
                                                  summarySignalDto.getSummarySignalId(),
                                                  summarySignalDto, false);

            }

        }

        List<String> removeSignal = new ArrayList<String>();
        for (Entry<String, SignalStateNode> summarySignalDtoList : parChildMap.entrySet())
        {
            SignalStateNode summarySignalDto = summarySignalDtoList.getValue();

            if (summarySignalDto.getParentListSet() != null
                && summarySignalDto.getParentListSet().contains(summarySignalName))
            {
                summarySignalDto.getParentListSet().remove(summarySignalName);

            }

            if (summarySignalDto.getChildList() != null
                && summarySignalDto.getChildList().contains(summarySignalName))
            {
                summarySignalDto.getChildList().remove(summarySignalName);

            }

            if (summarySignalDto.getParentListSet() != null
                && summarySignalDto.getChildList() != null
                && summarySignalDto.getParentListSet().size() == 0
                && summarySignalDto.childList.size() == 0
                && !summarySignalDefinitionMap_.containsKey(summarySignalDto.nodeId))
            {
                removeSignal.add(summarySignalDto.name);
            }
        }

        if (removeSignal.size() != 0)
        {
            for (String name : removeSignal)
            {
                parChildMap.remove(name);
            }
        }
        System.out.println(parChildMap);
    }

    public SummarySignalDefinitionDto updateSummarySignalDefinition(
        final SummarySignalDefinitionDto summarySignalDefinitionDto)
    {
        try
        {
            //            SummarySignalDefinition sumDef = new SummarySignalDefinition();
            //            sumDef.summarySignalName = summarySignalDefinitionDto.getSummarySignalName();
            //    long id = summarySignalDefinitionDao.selectSequenceNum(dataBaseName, sumDef);

            summarySignalDefinitionDto.setSummarySignalStatus(-1);

            long id =
                parChildMap.get(summarySignalDefinitionDto.getSummarySignalName()).getNodeId();

            summarySignalDefinitionDto.summarySignalId = id;
            summarySignalDefinitionDto.priority = summarySignalDefinitionMap_.get(id).priority;

            boolean isLoop =
                this.checkLoop(summarySignalDefinitionDto.getSummarySignalName(),
                               summarySignalDefinitionDto.getSummarySignalName(),
                               summarySignalDefinitionDto.getSignalList());
            if (isLoop == true)
            {
                summarySignalDefinitionDto.errorMessage = "It cannot be happen loop";
                // summarySignalDefinitionDto.summarySignalStatus = 2;
            }
            else
            {
                this.calculatePriority(summarySignalDefinitionDto,
                                       summarySignalDefinitionDto.getSignalList());
                summarySignalDefinitionDao.update(dataBaseName,
                                                  summarySignalDefinitionDto.getSummarySignalId(),
                                                  summarySignalDefinitionDto, false);
                this.summarySignalDefinitionMap_.put(summarySignalDefinitionDto
                    .getSummarySignalId(), summarySignalDefinitionDto);
                /*long summarySignalId = 0;
                String summarySignalName = summarySignalDefinitionDto.getSummarySignalName();

                boolean isDuplicate = this.checkDuplicate(summarySignalName);
                if (isDuplicate == true)
                {
                    summarySignalDefinitionDto.summarySignalStatus = 1;
                    summarySignalDefinitionDto.errorMessage = "Summary signal name cannot be duplicate";
                }

                SummarySignalDefinition summarySignalDefinition =
                    this.convertSummarySignalDefinition(summarySignalDefinitionDto);
                return null;*/
                createAllSummarySignalMapValue();
                String summarySignalName = summarySignalDefinitionDto.getSummarySignalName();
                SignalStateNode signalStateNode = new SignalStateNode();
                signalStateNode = parChildMap.get(summarySignalName);
                if (signalStateNode != null && signalStateNode.getParentListSet() != null
                    && signalStateNode.getParentListSet().size() > 0)
                {
                    this.reCalculatePriority(summarySignalDefinitionDto,
                                             signalStateNode.getParentListSet());
                }

            }
        }
        catch (SQLException ex)
        {
            // TODO 自動生成された catch ブロック
            ex.printStackTrace();
        }
        return null;
    }

    private boolean checkLoop(final String updatedSummarySignal,
        final String parentSummarySignalName, final List<String> signalList)
    {
        for (String targetSignal : signalList)
        {
            if (!updatedSummarySignal.equals(targetSignal))
            {
                if (parChildMap.get(targetSignal).childList == null
                    || parChildMap.get(targetSignal).childList.size() == 0)
                {
                    //  return false;
                    continue;
                }
                else
                {
                    boolean result =
                        checkLoop(updatedSummarySignal, targetSignal,
                                  parChildMap.get(targetSignal).childList);
                    if (result)
                    {
                        return true;
                    }
                }
            }
            else
            {
                return true;
            }
        }
        return false;

    }

}
