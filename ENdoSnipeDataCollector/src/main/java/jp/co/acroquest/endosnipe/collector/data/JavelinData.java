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

/**
 * Javelin ���O��\���C���^�[�t�F�[�X�ł��B<br />
 * 
 * @author y-komori
 */
public interface JavelinData
{
    /**
     * �f�[�^�x�[�X����Ԃ��܂��B<br />
     *
     * @return �f�[�^�x�[�X��
     */
    String getDatabaseName();

    /**
     * �f�[�^�x�[�X����ݒ肵�܂��B<br />
     *
     * @param databaseName �f�[�^�x�[�X��
     */
    void setDatabaseName(String databaseName);

    /**
     * �z�X�g����Ԃ��܂��B<br />
     * 
     * @return �z�X�g��
     */
    String getHost();

    /**
     * �z�X�g����ݒ肵�܂��B<br />
     * 
     * @param host �z�X�g��
     */
    void setHost(String host);

    /**
     * IP �A�h���X��Ԃ��܂��B<br />
     * 
     * @return IP�A�h���X
     */
    String getIpAddress();

    /**
     * IP �A�h���X��ݒ肵�܂��B<br />
     * 
     * @param ipAddress IP �A�h���X
     */
    void setIpAddress(String ipAddress);

    /**
     * �|�[�g�ԍ���Ԃ��܂��B<br />
     * 
     * @return �|�[�g�ԍ�
     */
    int getPort();

    /**
     * �|�[�g�ԍ���ݒ肵�܂��B<br />
     * 
     * @param port �|�[�g�ԍ�
     */
    void setPort(int port);

    /**
     * �N���C�A���gID��Ԃ��܂��B
     * @return �N���C�A���gID
     */
    String getClientId();

    /**
     * �N���C�A���gID��ݒ肵�܂��B
     * @param clientId �N���C�A���gID
     */
    void setClientId(String clientId);

    /**
     * �d�� ID ��Ԃ��܂��B
     *
     * @return �d�� ID
     */
    long getTelegramId();

    /**
     * �d�� ID ��ݒ肵�܂��B
     *
     * @param telegramId �d�� ID
     */
    void setTelegramId(long telegramId);
}
