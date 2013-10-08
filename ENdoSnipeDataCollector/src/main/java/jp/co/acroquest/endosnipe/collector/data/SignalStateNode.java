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
package jp.co.acroquest.endosnipe.collector.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SignalStateNode
{
    public String name;

    public long nodeId;

    public int priority;

    public Set<String> parentListSet = new HashSet<String>();

    public List<String> childList;

    public int currentStatus;

    public int signalType;

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(final int priority)
    {
        this.priority = priority;
    }

    public Set<String> getParentListSet()
    {
        return parentListSet;
    }

    public void setParentListSet(final Set<String> parentListSet)
    {
        this.parentListSet = parentListSet;
    }

    public List<String> getChildList()
    {
        return childList;
    }

    public void setChildList(final List<String> childList)
    {
        this.childList = childList;
    }

    public int getCurrentStatus()
    {
        return currentStatus;
    }

    public void setCurrentStatus(final int currentStatus)
    {
        this.currentStatus = currentStatus;
    }

    public int getSignalType()
    {
        return signalType;
    }

    public void setSignalType(final int signalType)
    {
        this.signalType = signalType;
    }

    public long getNodeId()
    {
        return nodeId;
    }

    public void setNodeId(final long nodeId)
    {
        this.nodeId = nodeId;
    }

    @Override
    public String toString()
    {
        return "SignalStateNode [name=" + name + ",\n nodeId=" + nodeId + ",\n priority="
            + priority + ",\n parentListSet=" + parentListSet + ",\n childList=" + childList
            + ",\n currentStatus=" + currentStatus + ",\n signalType=" + signalType + "]";
    }

}
