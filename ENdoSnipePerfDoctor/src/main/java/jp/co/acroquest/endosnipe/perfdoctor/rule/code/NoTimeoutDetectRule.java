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

import java.util.Map;

import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.parser.JavelinConstants;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogColumnNum;
import jp.co.acroquest.endosnipe.javelin.JavelinLogUtil;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;
import jp.co.acroquest.endosnipe.perfdoctor.rule.SingleElementRule;

import org.apache.commons.lang.StringUtils;

/**
 * �\�P�b�g�̃^�C���A�E�g�l�̏��������s���Ă��Ȃ����Ƃ����o���郋�[���ł��B<br />
 * @author fujii
 *
 */
public class NoTimeoutDetectRule extends SingleElementRule implements JavelinConstants
{
    /** ���K�[ */
    private static final ENdoSnipeLogger LOGGER =
                                                  ENdoSnipeLogger.getLogger(NoTimeoutDetectRule.class);

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void doJudgeElement(final JavelinLogElement element)
    {
        // ���ʎq��"Event"�łȂ��ꍇ�́A�������Ȃ��B
        String type = element.getBaseInfo().get(JavelinLogColumnNum.ID);
        boolean isEvent = MSG_EVENT.equals(type);

        if (isEvent == false)
        {
            return;
        }

        String eventName = element.getBaseInfo().get(JavelinLogColumnNum.EVENT_NAME);
        String eventLevel = element.getBaseInfo().get(JavelinLogColumnNum.EVENT_LEVEL);

        if (StringUtils.equals(this.level, eventLevel) == false)
        {
            return;
        }

        // �C�x���g����"NoTimeoutDetected"�̏ꍇ�A���o���s���B
        if (EventConstants.NAME_NOTIMEOUT_DETECTED.equals(eventName) == false)
        {
            return;
        }

        Map<String, String> eventInfoMap =
                                           JavelinLogUtil.parseDetailInfo(element,
                                                                          JavelinParser.TAG_TYPE_EVENTINFO);
        String identifier = eventInfoMap.get(EventConstants.NOTIMEOUT_IDENTIFIER);
        String timeoutStr = eventInfoMap.get(EventConstants.NOTIMEOUT_TIMEOUT);

        if (timeoutStr != null)
        {
            int timeout = 0;
            try
            {
                timeout = Integer.parseInt(timeoutStr);
            }
            catch (NumberFormatException ex)
            {
                LOGGER.warn(ex);
                return;
            }

            String stackTrace = eventInfoMap.get(EventConstants.NOTIMEOUT_STACKTRACE);
            addError(true, stackTrace, element, false, new Object[]{identifier, timeout});
        }
    }

}
