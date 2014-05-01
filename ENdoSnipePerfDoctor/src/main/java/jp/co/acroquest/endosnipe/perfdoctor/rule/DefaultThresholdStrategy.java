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
package jp.co.acroquest.endosnipe.perfdoctor.rule;

import java.util.Map;

import jp.co.acroquest.endosnipe.common.parser.JavelinLogConstants;
import jp.co.acroquest.endosnipe.javelin.JavelinLogUtil;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;

/**
 * JavelinLogElement����AExtraInfo��duration��duration臒l�Ƃ��Ē��o����B
 * 
 * @author eriguchi
 */
public class DefaultThresholdStrategy implements ThresholdStrategy
{
    /**
     * JavelinLogElement����Aduration臒l�Ƃ��ė��p���镶����𒊏o����B
     * 
     * @param javelinLogElement
     *            �l�̒��o���B
     * @return duration臒l������B
     */
    public String extractDurationThreshold(final JavelinLogElement javelinLogElement)
    {
        // ExtraInfo�̓��e��\��Map���擾����B
        Map<String, String> map =
                                  JavelinLogUtil.parseDetailInfo(javelinLogElement,
                                                                 JavelinParser.TAG_TYPE_EXTRAINFO);

        // ���\�b�h��TAT�̒l�𓾂�B
        String durationString = map.get(JavelinLogConstants.EXTRAPARAM_DURATION);
        return durationString;
    }

}
