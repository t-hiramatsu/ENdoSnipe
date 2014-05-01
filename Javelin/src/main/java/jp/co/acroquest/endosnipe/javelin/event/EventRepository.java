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
package jp.co.acroquest.endosnipe.javelin.event;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;

/**
 * �������B
 * 
 * @author eriguchi
 */
public class EventRepository
{
    /** �ő�C�x���g���B���̒l�𒴂��ēo�^�����ꍇ�̓C�x���g��o�^���Ȃ��B */
    private static final int EVENT_MAX = 2000;

    /** �C�x���g�̓o�^�B */
    private Map<CommonEvent, Long> eventMap_ = new ConcurrentHashMap<CommonEvent, Long>();

    /** �C�x���g�Ԋu */
    private long interval_ = new JavelinConfig().getEventInterval();

    /** �Ō�ɍX�V���������B */
    private long lastCleanupTime_ = 0;

    /**
     * �C�x���g��Map�Ɋi�[���܂��B<br />
     * 
     * @param event {@link CommonEvent}�I�u�W�F�N�g
     */
    public void putEvent(CommonEvent event)
    {
        if (EVENT_MAX < this.eventMap_.size())
        {
            return;
        }

        long currentTime = System.currentTimeMillis();
        this.eventMap_.put(event, currentTime);
    }

    private void cleanup(long currentTime)
    {
        long interval = this.interval_;
        if (currentTime - lastCleanupTime_ < interval)
        {
            return;
        }

        Set<CommonEvent> removeSet = new HashSet<CommonEvent>();

        for (Map.Entry<CommonEvent, Long> entry : this.eventMap_.entrySet())
        {
            boolean isExpired = currentTime - entry.getValue() >= interval;

            if (isExpired)
            {
                removeSet.add(entry.getKey());
            }
        }

        for (CommonEvent event : removeSet)
        {
            this.eventMap_.remove(event);
        }
        this.lastCleanupTime_ = currentTime;
    }

    /**
     * �C�x���g��Map�Ɋ��Ɋi�[����Ă��邩�ǂ�����Ԃ��܂��B<br />
     * 
     * @param event {@link CommonEvent}�I�u�W�F�N�g
     * @return ���Ɋi�[����Ă���ꍇ�ɁA<code>true</code>
     */
    public boolean containsEvent(CommonEvent event)
    {
        long currentTime = System.currentTimeMillis();
        cleanup(currentTime);
        return this.eventMap_.containsKey(event);
    }

}
