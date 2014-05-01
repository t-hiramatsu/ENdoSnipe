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
package jp.co.acroquest.endosnipe.communicator.entity;

/**
 * �ڑ��ʒm���̃f�[�^�N���X�ł��B
 * 
 * @author matsuoka
 */
public class ConnectNotifyData
{
    /** �ڑ���ʁFJavelin */
    public static final int KIND_JAVELIN = 0;

    /** �ڑ���ʁF����N���C�A���g (BottleneckEye/WebDashboard) */
    public static final int KIND_CONTROLLER = 1;

    /** �ڑ��ړI�F���\�[�X�̎擾 */
    public static final int PURPOSE_GET_RESOURCE = 0;

    /** �ڑ��ړI�F�f�[�^�x�[�X���̎擾 */
    public static final int PURPOSE_GET_DATABASE = 1;

    /** �ڑ���� */
    private int kind_ = -1;

    /** �G�[�W�F���g�� */
    private String agentName_;

    /** �ڑ��ړI */
    private int purpose_ = 0;

    /**
     * �ڑ���ʂ��擾���܂��B
     * @return �ڑ����
     */
    public int getKind()
    {
        return kind_;
    }

    /**
     * �ڑ���ʂ�ݒ肵�܂��B
     * @param kind �ڑ����
     */
    public void setKind(int kind)
    {
        kind_ = kind;
    }

    /**
     * �G�[�W�F���g�����擾���܂��B
     * @return DB��
     */
    public String getAgentName()
    {
        return agentName_;
    }

    /**
     * �G�[�W�F���g����ݒ肵�܂��B
     * @param agentName �G�[�W�F���g��
     */
    public void setAgentName(String agentName)
    {
        agentName_ = agentName;
    }

    /**
     * �ڑ��ړI���擾���܂��B
     * @return �ڑ��ړI
     */
    public int getPurpose()
    {
        return purpose_;
    }

    /**
     * �ڑ��ړI��ݒ肵�܂��B
     * @param purpose �ڑ��ړI
     */
    public void setPurpose(int purpose)
    {
        purpose_ = purpose;
    }

}
