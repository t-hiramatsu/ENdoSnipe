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
package jp.co.acroquest.endosnipe.javelin.resource.proc;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.entity.ResourceItem;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.javelin.bean.proc.CpuCoreInfo;
import jp.co.acroquest.endosnipe.javelin.bean.proc.StatInfo;

/**
 * StatInfoからタスク待ちでのCPU使用量を取得する。
 * 
 * @author akita
 */
public class LinuxCpuArrayGetter extends ProcMultiResourceGetter
{
    /**
     * 
     * @param procParser リソース情報取得用
     */
    public LinuxCpuArrayGetter(ProcParser procParser)
    {
        super(procParser);
    }

    /**
     * {@inheritDoc}
     */
    public ItemType getItemType()
    {
        return ItemType.ITEMTYPE_STRING;
    }

    /**
     * {@inheritDoc}
     */
    public List<ResourceItem> getValues()
    {
        StatInfo statInfo = this.getProcParser().getProcInfo().getStatInfo();

        List<ResourceItem> values = new ArrayList<ResourceItem>();
        List<CpuCoreInfo> coreList = statInfo.getCpuArray();
        for (int index = 0; index < coreList.size(); index++)
        {
            CpuCoreInfo coreInfo = coreList.get(index);
            String base = TelegramConstants.ITEMNAME_CPU_ARRAY + "/" + index + "/time";
            values.add(createItem(base, "/user(d)", coreInfo.getCpuUser()));
            values.add(createItem(base, "/system(d)", coreInfo.getCpuSystem()));
            values.add(createItem(base, "/iowait(d)", coreInfo.getCpuIoWait()));
        }

        return values;
    }

    /**
     * 
     * @param base
     * @param name
     * @param value
     * @return
     */
    private ResourceItem createItem(String base, String name, long value)
    {
        ResourceItem item = new ResourceItem();
        item.setName(base + name);
        item.setValue(String.valueOf(value));
        return item;
    }

}
