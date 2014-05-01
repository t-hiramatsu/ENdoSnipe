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
package jp.co.acroquest.endosnipe.javelin.communicate.telegram.util;

import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.RequestBody;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;

/**
 * �d���쐬�Ɋւ��郆�[�e�B���e�B�N���X
 * @author fujii
 *
 */
public class CreateTelegramUtil
{
    /**
     * �d�����쐬����B
     * @param header �w�b�_
     * @param body Body
     * @return �d��
     */
    public static Telegram createTelegram(final Header header, final Body[] body)
    {
        Telegram telegram = new Telegram();
        telegram.setObjHeader(header);
        telegram.setObjBody(body);
        return telegram;

    }

    /**
     * �w�b�_���쐬����B
     * @param requestKind �d���������
     * @param telegramKind �d�����
     * @return
     */
    public static Header createHeader(final byte requestKind, final byte telegramKind)
    {
        Header header = new Header();
        header.setByteRequestKind(requestKind);
        header.setByteTelegramKind(telegramKind);

        return header;
    }

    /**
     * �d����Body���쐬����B
     * @param objectName �I�u�W�F�N�g��
     * @param itemName �A�C�e����
     * @param itemType �^
     * @param loopCount �A�C�e����
     * @param objArr �v���l
     * 
     * @return Body �d����Body
     */
    public static RequestBody createBodyValue(final String objectName, final String itemName,
            final ItemType itemType, final int loopCount, final Object[] objArr)
    {
        RequestBody body = new RequestBody();
        body.setStrObjName(objectName);
        body.setStrItemName(itemName);
        body.setByteItemMode(itemType);
        body.setIntLoopCount(loopCount);
        body.setObjItemValueArr(objArr);

        return body;
    }
}
