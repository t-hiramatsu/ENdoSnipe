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
package jp.co.acroquest.endosnipe.data;

/**
 * �e�[�u�����̂��`����C���^�[�t�F�[�X�ł��B<br />
 * 
 * @author y-komori
 */
public interface TableNames
{
    /** �y�H�Ώۃz�X�g��� */
    String HOST_INFO                = "HOST_INFO";

    /** �v���l��� */
    String MEASUREMENT_INFO         = "MEASUREMENT_INFO";

    /** Javelin ���O */
    String JAVELIN_LOG              = "JAVELIN_LOG";

    /** Javelin �v������ */
    String JAVELIN_MEASUREMENT_ITEM = "JAVELIN_MEASUREMENT_ITEM";

    /** Javelin �v���l */
    String MEASUREMENT_VALUE        = "MEASUREMENT_VALUE";

    /** Javelin �v���l�A�[�J�C�u */
    String ARCHIVED_VALUE           = "ARCHIVED_VALUE";

    /** ���O ID �̒l�𐶐�����V�[�P���X���B */
    String SEQ_LOG_ID               = "SEQ_LOG_ID";

    /** �Z�b�V���� ID �̒l�𐶐�����V�[�P���X���B */
    String SEQ_SESSION_ID           = "SEQ_SESSION_ID";

    /** �z�X�g ID �̒l�𐶐�����V�[�P���X���B */
    String SEQ_HOST_ID              = "SEQ_HOST_ID";

    /** �v������ ID �̒l�𐶐�����V�[�P���X���B */
    String SEQ_MEASUREMENT_ITEM_ID  = "SEQ_MEASUREMENT_ITEM_ID";

    /** �v�� No. �̒l�𐶐�����V�[�P���X���B */
    String SEQ_MEASUREMENT_NUM      = "SEQ_MEASUREMENT_NUM";

    /** �V�O�i����`�e�[�u���B */
    String SIGNAL_DEFINITION        = "SIGNAL_DEFINITION";
}
