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
package jp.co.acroquest.endosnipe.javelin.event;

import jp.co.acroquest.endosnipe.common.event.EventConstants;

/**
 * SQL��Full Scan�C�x���g�B
 * 
 * @author iida
 */
public class FullScanEvent extends CommonEvent
{
    /** Full Scan���s���Ă���e�[�u�����̃Z�b�g�̕�����\�� */
    protected String tableNames_;

    /**
     * {@inheritDoc}
     */
    @Override
    public void addParam(String key, String value)
    {
        super.addParam(key, value);
        if (EventConstants.PARAM_FULL_SCAN_TABLE_NAME.equals(key))
        {
            this.setTableNames(value);
        }
    }

    /**
     * �R���X�g���N�^�B
     */
    public FullScanEvent()
    {
        super();
        this.setName(EventConstants.NAME_FULL_SCAN);
    }
    
    /**
     * Full Scan���s���Ă���e�[�u�����̃Z�b�g���Z�b�g���܂��B
     * 
     * @param tableNames �e�[�u�����̃Z�b�g
     */
    private void setTableNames(String tableNames)
    {
        this.tableNames_ = tableNames;
    }
}
