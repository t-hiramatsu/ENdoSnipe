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
package jp.co.acroquest.endosnipe.communicator;

import jp.co.acroquest.endosnipe.communicator.entity.ConnectNotifyData;

/**
 * �R�~���j�P�[�V�����N���C�A���g�̂��߂̃C���^�[�t�F�[�X�ł��B<br />
 * 
 * @author y-komori
 */
public interface CommunicationClient extends AbstractCommunicator
{
    /**
     * �R�~���j�P�[�V�����N���C�A���g�����������܂��B<br />
     * 
     * @param hostName �ڑ���z�X�g���܂���IP�A�h���X
     * @param port �ڑ���|�[�g�ԍ�
     */
    void init(String hostName, int port);

    /**
     * �T�[�o�֐ڑ����܂��B<br />
     * 
     * �{���\�b�h���Ăяo���O�ɁA{@link #init(String, int)} ���\�b�h���Ăяo���Ă��������B<br />
     * 
     * @param connectNotify �ڑ�������ɑ��M����ڑ��ʒm
     */
    void connect(ConnectNotifyData connectNotify);

    /**
     * �T�[�o����ؒf���܂��B<br />
     */
    void disconnect();

    /**
     * �R�~���j�P�[�V�����N���C�A���g���I�����܂��B<br />
     * 
     * �{���\�b�h�́A{@link #disconnect()} ���\�b�h�̌�ɌĂяo���Ă��������B<br />
     */
    void shutdown();

    /**
     * �ڑ��� IP �A�h���X��Ԃ��܂��B<br />
     *
     * @return �ڑ��� IP �A�h���X
     */
    String getIpAddress();
}
