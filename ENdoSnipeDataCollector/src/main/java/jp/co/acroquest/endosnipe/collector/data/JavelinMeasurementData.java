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
package jp.co.acroquest.endosnipe.collector.data;

import jp.co.acroquest.endosnipe.common.entity.ResourceData;

/**
 * Javelin �v���l��\���N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class JavelinMeasurementData extends AbstractJavelinData
{
    private final ResourceData resourceData_;

    /**
     * {@link JavelinMeasurementData} ���\�z���܂��B<br />
     * 
     * @param resourceData �v���l�f�[�^
     */
    public JavelinMeasurementData(final ResourceData resourceData)
    {
        resourceData_ = resourceData;
    }

    /**
     * �v���l�f�[�^��Ԃ��܂��B<br />
     * 
     * @return resourceData �v���l�f�[�^
     */
    public ResourceData getResourceData()
    {
        return resourceData_;
    }
}
