/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Acroquest Technology Co.,Ltd.
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
package jp.co.acroquest.endosnipe.collector.processor;

import jp.co.acroquest.endosnipe.collector.notification.AlarmEntry;
import jp.co.acroquest.endosnipe.common.entity.ResourceData;
import jp.co.acroquest.endosnipe.data.dto.SignalDefinitionDto;

/**
 * 臒l���߂̃A���[�������𔻒肷�鏈���̃C���^�t�F�[�X
 * @author ochiai
 *
 */
public interface AlarmProcessor
{
    /** �p�[�Z���g���v�Z����Ƃ��̒萔�i100�j */
    int PERCENT_CONST = 100;

    /**
     * �A���[�����x�����v�Z����
     * @param currentResourceData ���݂̃��\�[�X�f�[�^
     * @param prevResourceData ��O�̃��\�[�X�f�[�^
     * @param signalDefinition 臒l�����`���
     * @param alarmData �A���[���ʒm�̗L�����v�Z���邽�߂̃f�[�^�B���̃��\�b�h�̒��ōX�V����
     * @return �A���[���ʒm�̗L���ƁA�ʒm����A���[���ɕ\�������������ꂽAlarmEntry
     */
    AlarmEntry calculateAlarmLevel(ResourceData currentResourceData, ResourceData prevResourceData,
            SignalDefinitionDto signalDefinition, AlarmData alarmData);
}
