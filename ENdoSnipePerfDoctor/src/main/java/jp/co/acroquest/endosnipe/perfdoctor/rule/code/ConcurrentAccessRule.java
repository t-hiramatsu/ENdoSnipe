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
package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.parser.JavelinConstants;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogColumnNum;
import jp.co.acroquest.endosnipe.javelin.JavelinLogUtil;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;
import jp.co.acroquest.endosnipe.perfdoctor.rule.SingleElementRule;

import org.apache.commons.lang.StringUtils;

/**
 * �����X���b�h����̕���A�N�Z�X
 *�@ENdoSnipeVer.4.0�ɂĒǉ��������[��
 *  
 * @author akita
 *
 */
public class ConcurrentAccessRule extends SingleElementRule implements JavelinConstants
{
    /**
    * EVENT���O����javelin.concurrent.identifier�̓��e�����݂���ꍇ�A�x�����o���B
    * 
    * @param element {@link JavelinLogElement}�I�u�W�F�N�g
    * 
    */
    @Override
    public void doJudgeElement(final JavelinLogElement element)
    {
        //���ʎ���"Event"�łȂ��ꍇ�A�������I������B
        List<String> baseInfo = element.getBaseInfo();
        String type = baseInfo.get(JavelinLogColumnNum.ID);
        if (MSG_EVENT.equals(type) == false)
        {
            return;
        }
        String eventName = baseInfo.get(JavelinLogColumnNum.EVENT_NAME);
        String eventLevel = baseInfo.get(JavelinLogColumnNum.EVENT_LEVEL);

        if (StringUtils.equals(this.level, eventLevel) == false)
        {
            return;
        }

        if (EventConstants.NAME_CONCURRENT_ACCESS.equals(eventName) == false)
        {
            return;
        }

        Map<String, String> eventInfoMap =
                                           JavelinLogUtil.parseDetailInfo(element,
                                                                          JavelinParser.TAG_TYPE_EVENTINFO);

        String identifer = eventInfoMap.get(EventConstants.PARAM_CONCURRENT_IDENTIFIER);

        if (identifer == null || "".equals(identifer))
        {
            return;
        }

        int count = 0;
        StringBuilder builder = new StringBuilder();
        while (true)
        {
            String thread = eventInfoMap.get(EventConstants.PARAM_CONCURRENT_THREAD + "." + count);

            if (thread == null)
            {
                break;
            }
            if (count != 0)
            {
                builder.append(",");
            }
            builder.append(thread);
            count++;
        }

        String stackTrace = eventInfoMap.get(EventConstants.PARAM_CONCURRENT_STACKTRACE + ".0");
        //identifer�̓��e�����݂���ꍇ�A�x�����o���B
        addError(true, stackTrace, element, identifer, builder.toString());
    }
}
