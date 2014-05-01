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
package jp.co.acroquest.endosnipe.common.jmx;

import java.util.HashMap;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;

/**
 * ���[�U���ݒ肵��JMX�����Ǘ����邽�߂̃N���X�ł��B<br />
 * 
 * @author y_asazuma
 */
public class JMXManager
{
    /** �C���X�^���X */
    private static JMXManager manager__ = new JMXManager();

    /** ���[�U���w�肵��JMX���ڂƌv���l��ʂ̃}�b�v(DataCollector�p) */
    private final Map<String, Map<String, Long>> measurmentTypeMap_;

    /** JMX�̎擾�l�̂����A�����������l */
    public static final String[] JMX_RATIO_ITEMNAME_ARRAY = {"HitRatio"};

    /** �������t���O */
    private boolean initFlag_ = false;

    private NotifyJMXItem callBack_ = null;

    /**
     * �C���X�^���X����h���R���X�g���N�^
     */
    private JMXManager()
    {
        this.measurmentTypeMap_ = new HashMap<String, Map<String, Long>>(0);
    }

    /**
     * �C���X�^���X���擾���܂��B
     * 
     * @return JMXManager�C���X�^���X
     */
    public static JMXManager getInstance()
    {
        return manager__;
    }

    /**
     * JMX���ڂ̕ύX��ʒm����R�[���o�b�N���\�b�h��ݒ肵�܂��B
     * 
     * @param callBack �R�[���o�b�N���\�b�h
     */
    public void setCallBack(final NotifyJMXItem callBack)
    {
        callBack_ = callBack;
    }

    /**
     * ����������������܂ŃX���b�h��ҋ@�����܂��B<br />
     * ������������ɕK��{@link #initCompleted()}���Ăяo���ĉ������B
     */
    public void waitInitialize()
    {
        while (!initFlag_)
        {
            try
            {
                wait();
            }
            catch (Exception e)
            {
                SystemLogger.getInstance().warn(e);
            }
        }
    }

    /**
     * �������̊�����ʒm���A<br />
     * �������҂��X���b�h������ꍇ�͂��ׂčĊJ�����܂��B
     */
    public synchronized void initCompleted()
    {
        this.initFlag_ = true;
        notifyAll();
    }

    /**
     * JMX���ڂƌv���l��ʂ̃}�b�v���擾���܂��B
     * @param dbName DB��
     * @return JMX���ڂƌv���l��ʂ̃}�b�v
     */
    public Map<String, Long> getMeasurmentTypeMap(final String dbName)
    {
        return measurmentTypeMap_.get(dbName);
    }

    /**
     * JMX���ڂƌv���l��ʂ̃}�b�v���擾���܂��B
     * 
     * @param dbName �f�[�^�x�[�X��
     * @param map JMX���ڂƌv���l��ʂ̃}�b�v
     */
    public void setMeasurementTypeMap(final String dbName, final Map<String, Long> map)
    {
        this.measurmentTypeMap_.put(dbName, map);
    }

    /**
     * JMX���ڂɑ΂���v���l��ʂ��}�b�s���O���܂��B
     * 
     * @param dbName �f�[�^�x�[�X��
     * @param objectName �I�u�W�F�N�g��
     * @param objDispName �I�u�W�F�N�g�\����
     * 
     * @return �v���l���ID<br />���ɍ��ڂ����݂��Ă���ꍇ��-1
     */
    public long addMeasurementType(final String dbName, final String objectName,
            final String objDispName)
    {
        long retID = -1L;

        Map<String, Long> map = this.measurmentTypeMap_.get(dbName);
        if (map == null)
        {
            map = new HashMap<String, Long>(1);
        }

        if (!map.containsKey(objectName))
        {
            if (callBack_ != null)
            {
                retID = this.callBack_.addItem(dbName, objectName, objDispName);
            }
            map.put(objectName, Long.valueOf(retID));
            this.measurmentTypeMap_.put(dbName, map);
        }

        return retID;
    }
}
