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
package jp.co.acroquest.endosnipe.data.util;

import jp.co.acroquest.endosnipe.common.Constants;
import jp.co.acroquest.endosnipe.common.entity.DisplayType;

/**
 * �ώZ�Œl���擾���ADB�ɕۑ�����O�ɍ����̒l�ɕϊ�����K�v������v�����ڂ̈ꗗ
 *
 * @author ochiai
 *
 */
public class AccumulatedValuesDefinition implements Constants
{
    /** �ώZ�l�ł���Ƃ������Ƃ𔻒f����suffix�B�n�񖼂̖��������̕�����̏ꍇ�ɁA�ώZ�l�Ƃ݂Ȃ��B */
    private static final String ACCUMULATED_SUFFIX = "(d)";

    private AccumulatedValuesDefinition()
    {
        // do nothing
    }

    /**
     * �w�肵��itemId ���ώZ�Œl���擾����v�����ڂ��ǂ�����Ԃ�
     * @param itemName �v�����ڂ�ID
     * @param displayType �v���l�̕\���^
     * @return �ώZ�Œl���擾����ꍇ true
     */
    public static synchronized boolean isAccumulatedValue(final String itemName,
        final int displayType)
    {
        boolean isAccumulatedItem = itemName.endsWith(ACCUMULATED_SUFFIX);
        if (isAccumulatedItem)
        {
            return true;
        }
        int differenceDisplayType =
            (int)DisplayType.getDisplayTypeNumber(DisplayType.DISPLAYTYPE_DIFFERENCE);
        if (displayType == differenceDisplayType)
        {
            return true;
        }
        return false;
    }

}
