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

import java.util.HashMap;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.parser.JavelinConstants;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.javelin.CallTree;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.CallTreeRecorder;
import jp.co.acroquest.endosnipe.javelin.bean.FastInteger;

/**
 * �C�x���g��ʖ��̃C�x���g�������𐔂���N���X�B<br />
 * 
 * @author Sakamoto
 */
public class JavelinEventCounter implements JavelinConstants
{
	private long poolStorePeriod_;

	/** �C�x���g�����L�[�ɂ����C�x���g�����񐔂̃}�b�v */
	private Map<String, FastInteger> eventCountMap_;

	private Map<String, FastInteger> prevEventCountMap_;

	private Map<String, String> eventPageNameMap_;

	private long lastClearTime_;

	private static final JavelinEventCounter INSTANCE = new JavelinEventCounter();

	/**
	 * �R���X�g���N�^���B�����܂��B<br />
	 */
	private JavelinEventCounter()
	{
		this.eventCountMap_ = new HashMap<String, FastInteger>();
		this.prevEventCountMap_ = new HashMap<String, FastInteger>();
		this.lastClearTime_ = System.currentTimeMillis();
		JavelinConfig config = new JavelinConfig();
		this.poolStorePeriod_ = config.getTatKeepTime();
	}

	/**
	 * ���̃N���X�̃C���X�^���X��Ԃ��܂��B<br />
	 * 
	 * @return �C���X�^���X
	 */
	public static JavelinEventCounter getInstance()
	{
		return INSTANCE;
	}

	/**
	 * �C�x���g�~�ϊ��Ԃ��Z�b�g���܂��B<br />
	 * 
	 * �C�x���g�ǉ����ɁA���łɂ��̒l�𒴂��ăC�x���g��~�ς���Ă����ꍇ�A �~�ς����C�x���g���������N���A���܂��B<br />
	 * 
	 * @param period
	 *            ���ԁi�~���b�j
	 */
	public void setPoolStorePeriod(final long period)
	{
		this.poolStorePeriod_ = period;
	}

	/**
	 * �C�x���g��ǉ����܂��B<br />
	 * 
	 * �O��v�[�����N���A������������C�x���g�~�ϊ��Ԃ��߂��Ă���ꍇ�́A �v�[�����N���A������ɃC�x���g��ǉ����܂��B<br />
	 * 
	 * @param event
	 *            Javelin �C�x���g
	 */
	public synchronized void addEvent(final CommonEvent event)
	{
		clearOldEvents();
		String pageName = null;
		CallTree callTree = CallTreeRecorder.getInstance().getCallTree();
		if (callTree != null)
		{
			CallTreeNode rootNode = callTree.getRootNode();
			if (rootNode != null)
			{
				pageName = rootNode.getInvocation().getRootInvocationManagerKey()
						.replace("/", "&#47;");
			}
		}

		FastInteger count = this.eventCountMap_.get(event.getName());
		if (count == null)
		{
			count = new FastInteger();
			this.eventCountMap_.put((pageName == null ? "/event/" + event.getName()
					: TelegramConstants.PREFIX_PROCESS_RESPONSE_EVENT.replace("page", pageName)
							+ "/" + event.getName()), count);
		}
		count.increment();
	}

	/**
	 * �C�x���g��ʖ��̃C�x���g���������擾���܂��B<br />
	 * 
	 * �擾��A�C�x���g�������̓N���A����܂��B<br />
	 * 
	 * @return �C�x���g�������̃}�b�v
	 */
	public synchronized Map<String, FastInteger> takeEventCount()
	{
		Map<String, FastInteger> eventCountMapCopy = new HashMap<String, FastInteger>(
				this.eventCountMap_);
		addZeroCount(eventCountMapCopy);
		this.prevEventCountMap_ = this.eventCountMap_;
		this.eventCountMap_ = new HashMap<String, FastInteger>();
		this.lastClearTime_ = System.currentTimeMillis();
		return eventCountMapCopy;
	}

	/**
	 * �O�񔭐����Ă����C�x���g�̂����A����͔������Ȃ������C�x���g�̔������� <code>0</code> �ɂ��܂��B<br />
	 * 
	 * �C�x���g���������Ȃ������ꍇ�̓N���C�A���g���ɔ�������ʒm���܂��񂪁A �O��C�x���g���������Ă����ꍇ�A <code>0</code> ��ǉ����邱�Ƃɂ��A �O���t�\����
	 * <code>0</code> ��\���ł���悤�ɂȂ�܂��B<br />
	 * 
	 * @param currentCount
	 *            ���݂̔�����
	 */
	private void addZeroCount(Map<String, FastInteger> currentCount)
	{
		for (Map.Entry<String, FastInteger> entry : this.prevEventCountMap_.entrySet())
		{
			if (!currentCount.containsKey(entry.getKey()) && entry.getValue().getValue() != 0)
			{
				// ������ 0 ��ǉ�����
				currentCount.put(entry.getKey(), new FastInteger());
			}
		}
	}

	/**
	 * �C�x���g�~�ϊ��Ԃ𒴂����C�x���g���N���A���܂��B<br />
	 */
	private void clearOldEvents()
	{
		long nowTime = System.currentTimeMillis();
		if (nowTime > this.lastClearTime_ + this.poolStorePeriod_)
		{
			this.eventCountMap_.clear();
			this.lastClearTime_ = nowTime;
		}
	}
}
