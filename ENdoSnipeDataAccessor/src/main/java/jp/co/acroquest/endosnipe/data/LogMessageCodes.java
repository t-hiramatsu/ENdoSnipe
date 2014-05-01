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

import jp.co.acroquest.endosnipe.common.logger.CommonLogMessageCodes;

/**
 * ENdoSnipe DataAccessor �̂��߂̃��b�Z�[�W�R�[�h�ł��B<br />
 * 
 * @author y-komori
 */
public interface LogMessageCodes extends CommonLogMessageCodes
{
    // -------------------------------------------------------------------------
    // �f�[�^�A�N�Z�X���b�Z�[�W�R�[�h (01xx)
    // -------------------------------------------------------------------------
    /** �f�[�^�x�[�X������ */
    String DB_INITIALIZED = "IEDA0100";

    /** �z�X�g�p�e�[�u���������� */
    String HOST_TABLE_INITIALIZED = "IEDA0101";

    /** �z�X�g���o�^ */
    String HOST_REGISTERED = "IEDA0102";

    /** �f�[�^�x�[�X�A�N�Z�X���s */
    String DB_ACCESS_ERROR = "EEDA0103";

    /** �R�l�N�V�����ڑ� */
    String DB_CONNECTED = "TEDA0104";

    /** �R�l�N�V�����ؒf */
    String DB_DICONNECTED = "TEDA0105";

    /** �R�l�N�V�����S�ؒf���ɃA�N�e�B�u�R�l�N�V�������c���Ă��� */
    String ACTIVE_CONNECTIONS_REMAINED = "WEDA0106";

    /** ���[�e�[�g�����{���� */
    String ROTATE_TABLE_PERFORMED = "IEDA0107";

    /** �s�v�Ȍn������폜���� */
    String NO_NEEDED_SERIES_REMOVED = "IEDA0108";
}
