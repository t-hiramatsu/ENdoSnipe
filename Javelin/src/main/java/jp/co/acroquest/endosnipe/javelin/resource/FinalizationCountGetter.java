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

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.entity.ItemType;

/**
 * �t�@�C�i���C�Y�҂��I�u�W�F�N�g�����擾����N���X�B
 * 
 * @author tsukano
 */
public class FinalizationCountGetter extends AbstractResourceGetter
{
    /**
     * {@inheritDoc}
     */
    public Number getValue()
    {
        MemoryMXBean bean = ManagementFactory.getMemoryMXBean();
        return bean.getObjectPendingFinalizationCount();
    }

    /**
     * {@inheritDoc}
     */
    public ItemType getItemType()
    {
        return ItemType.ITEMTYPE_INT;
    }

    /**
     * JavelinConfig�ɐݒ肳�ꂽenable�l��Ԃ��B
     * @return enable�l
     */
    @Override
    public boolean isEnable()
    {
        JavelinConfig config = new JavelinConfig();
        return config.isFinalizationCount();
    }
}
