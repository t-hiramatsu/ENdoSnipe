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
package jp.co.acroquest.endosnipe.javelin.communicate.telegram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.ResponseBody;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.javelin.common.ConfigUpdater;

/**
 * �T�[�o�v���p�e�B�擾�v�������N���X�B
 * 
 * @author tsukano
 */
public class GetPropertyRequestTelegramListener implements TelegramListener, TelegramConstants
{
    /**
     * {@inheritDoc}
     */
    public Telegram receiveTelegram(final Telegram telegram)
    {
        Header header = telegram.getObjHeader();
        if (header.getByteTelegramKind() == BYTE_TELEGRAM_KIND_GET_PROPERTY
                && header.getByteRequestKind() == BYTE_REQUEST_KIND_REQUEST)
        {
            long telegramId = header.getId();
            Telegram response = createPropertyResponse(telegramId, BYTE_TELEGRAM_KIND_GET_PROPERTY);
            return response;
        }
        return null;
    }

    /**
     * �w�肳�ꂽ�d����ʂ�p���āA�{�̂ɍX�V�\�Ȑݒ�l�ꗗ���������d�����쐬����
     *
     * @param telegramId �d�� ID
     * @param telegramKind �d�����
     * @return �w�肳�ꂽ�d����ʂɑΉ����A�{�̂ɍX�V�\�Ȑݒ�l�ꗗ���������d��
     */
    public static Telegram createPropertyResponse(final long telegramId, final byte telegramKind)
    {
        // �����d��
        Telegram telegram = new Telegram();

        // �����d���̃w�b�_���쐬����
        Header header = new Header();
        header.setId(telegramId);
        header.setByteTelegramKind(telegramKind);
        header.setByteRequestKind(BYTE_REQUEST_KIND_RESPONSE);
        telegram.setObjHeader(header);

        // �����d���ɓ����{�f�B�̃��X�g���쐬����
        List<Body> list = new ArrayList<Body>();
        Map<String, String> map = ConfigUpdater.getUpdatableConfig();
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries)
        {
            String key = entry.getKey();
            String value = entry.getValue();
            ResponseBody body = new ResponseBody();
            body.setStrObjName(key);
            body.setStrItemName(value);
            body.setObjItemValueArr(new Object[0]);

            list.add(body);
        }
        Body[] bodyList = list.toArray(new Body[list.size()]);
        telegram.setObjBody(bodyList);

        return telegram;
    }
}
