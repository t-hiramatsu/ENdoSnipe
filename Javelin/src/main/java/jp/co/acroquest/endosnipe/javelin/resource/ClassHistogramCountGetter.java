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
package jp.co.acroquest.endosnipe.javelin.resource;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.entity.ResourceItem;
import jp.co.acroquest.endosnipe.javelin.converter.leak.monitor.ClassHistogramEntry;
import jp.co.acroquest.endosnipe.javelin.converter.leak.monitor.ClassHistogramMonitor;
import jp.co.acroquest.endosnipe.javelin.converter.leak.monitor.InstanceMonitor;

/**
 * �N���X�q�X�g�O��������A�I�u�W�F�N�g�̐����擾����N���X�B
 * 
 * @author eriguchi
 */
public class ClassHistogramCountGetter implements MultiResourceGetter
{
    private ClassHistogramMonitor historgramMonitor_;

    public ClassHistogramCountGetter(ClassHistogramMonitor historgramMonitor)
    {
        this.historgramMonitor_ = historgramMonitor;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<ResourceItem> getValues()
    {
        List<ClassHistogramEntry> list = historgramMonitor_.getHistogramList();
        List<ClassHistogramEntry> instanceList = InstanceMonitor.getHistogramList();
        list.addAll(instanceList);

        List<ResourceItem> result = convert(list);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public ItemType getItemType()
    {
        return ItemType.ITEMTYPE_INT;
    }

    /**
     * ClassHistogramEntry ���� ResourceEntry �ւ̌^�ϊ����s���܂��B<br />
     *
     * list �� <code>null</code> �̏ꍇ�́A <code>null</code> ��Ԃ��܂��B<br />
     *
     * @param list ClassHistogramEntry�̃��X�g
     * @return ResourceEntry�̃��X�g
     */
    List<ResourceItem> convert(List<ClassHistogramEntry> list)
    {
        if (list == null)
        {
            return null;
        }

        List<ResourceItem> result = new ArrayList<ResourceItem>(list.size());

        for (ClassHistogramEntry histogramEntry : list)
        {
            ResourceItem resourceEntry = new ResourceItem();
            resourceEntry.setName(histogramEntry.getClassName());
            resourceEntry.setValue(String.valueOf(histogramEntry.getInstances()));

            result.add(resourceEntry);
        }
        return result;
    }
}
