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
package jp.co.acroquest.endosnipe.javelin.converter.util;

import jp.co.acroquest.endosnipe.javelin.CallTree;
import jp.co.acroquest.endosnipe.javelin.CallTreeRecorder;
import jp.co.acroquest.endosnipe.javelin.SystemStatusManager;

/**
 * �X�g���[���̎�M�ʁ^���M�ʊĎ��p�̃��[�e�B���e�B�N���X
 * 
 * @author kimura
 *
 */
public class StreamMonitorUtil
{
    /**
     * �R���X�g���N�^
     */
    private StreamMonitorUtil()
    {
        // Do Nothing.
    }

    /**
     * �~�ϑΏۂ̎��ʎq�Ɖ��Z�ʂ��w�肵�A�ݎZ����B
     * �ݎZ�Ώۂ̓X���b�h���ł̒l�ƁA�v���Z�X�S�̂Ƃ��Ă̒l�̂Q�B
     * 
     * @param recordAmount          ���Z�����l
     * @param recordTargetThreadKey �X���b�h�̗ݎZ�l�ɉ��Z����ۂ̎��ʎq
     * @param recordTargetKey       �v���Z�X�S�̗̂ݎZ�l�ɉ��Z����ۂ̎��ʎq
     */
    public static void recordStreamAmount(final long recordAmount,
            final String recordTargetThreadKey, final String recordTargetKey)
    {
        CallTree tree = CallTreeRecorder.getInstance().getCallTree();

        long oldSize;
        long newSize;
        Object value;

        oldSize = 0;
        value = tree.getLoggingValue(recordTargetThreadKey);
        if (value != null)
        {
            oldSize = (Long)value;
        }
        newSize = oldSize + recordAmount;
        tree.setLoggingValue(recordTargetThreadKey, newSize);

        synchronized (SystemStatusManager.class)
        {
            oldSize = 0;
            value = SystemStatusManager.getValue(recordTargetKey);
            if (value != null)
            {
                oldSize = (Long)value;
            }
            newSize = oldSize + recordAmount;
            SystemStatusManager.setValue(recordTargetKey, newSize);
        }
    }
}
