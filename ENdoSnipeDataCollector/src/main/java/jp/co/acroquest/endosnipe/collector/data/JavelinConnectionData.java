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

import java.util.LinkedHashMap;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.entity.MeasurementData;

/**
 * Javelin����̐ڑ����邢�͐ؒf�̃C�x���g��\�� {@link JavelinData} �ł��B<br />
 * 
 * @author iida
 */
public class JavelinConnectionData extends AbstractJavelinData
{
    /** �ڑ��̃^�C�v */
    public static final boolean TYPE_CONNECTION    = true;

    /** �ؒf�̃^�C�v */
    public static final boolean TYPE_DISCONNECTION = false;

    /** ���̃f�[�^���ڑ��̃C�x���g��\�����ǂ����B */
    private final boolean       connectionData_;

    /** �v������ */
    public long                 measurementTime;

    /** �z�X�g�� */
    public String               hostName;

    /** �|�[�g�ԍ� */
    public int                  portNum;

    /**
     * �R���X�g���N�^�B
     * 
     * @param type ���̃f�[�^�̎�ށi�ڑ����ؒf���j
     */
    public JavelinConnectionData(final boolean type)
    {
        this.connectionData_ = type;
    }

    /** �O���t�̌v���f�[�^���i�[����}�b�v(�v���l��ʁA�O���t�̌v���f�[�^) */
    private final Map<Integer, MeasurementData> measurementMap_ 
                    = new LinkedHashMap<Integer, MeasurementData>();

    /**
     * �v���f�[�^��ǉ����܂��B 
     * @param mData �v���f�[�^
     */
    public void addMeasurementData(final MeasurementData mData)
    {
        measurementMap_.put(mData.measurementType, mData);
    }

    /**
     * �v���f�[�^���i�[����}�b�v���擾���܂��B
     * @return �v���f�[�^���i�[����}�b�v
     */
    public Map<Integer, MeasurementData> getMeasuermentMap()
    {
        return this.measurementMap_;
    }

    /**
     * ���̃f�[�^���ڑ��̃C�x���g��\�����ǂ�����Ԃ��܂��B<br>
     * 
     * @return �ڑ��C�x���g�Ȃ��true�A�ؒf�C�x���g�Ȃ��false
     */
    public boolean isConnectionData()
    {
        return this.connectionData_;
    }
}
