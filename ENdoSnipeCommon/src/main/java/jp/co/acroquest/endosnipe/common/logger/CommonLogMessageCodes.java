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
package jp.co.acroquest.endosnipe.common.logger;

/**
 * ���ʃ��O�̂��߂̃��b�Z�[�W�R�[�h�ł��B<br />
 * 
 * @author y-komori
 */
public interface CommonLogMessageCodes
{
    /**
     * ��O�����������ꍇ�̃��b�Z�[�W�R�[�h�ł��B
     * <dl>
     * <dt><b>�l :</b></dt>
     * <dd>{@value}</dd>
     * </dl>
     */
    String EXCEPTION_OCCURED = "EECM0000";

    /**
     * ��O�����������ꍇ�̃��b�Z�[�W�R�[�h�ł��B
     * <dl>
     * <dt><b>�l :</b></dt>
     * <dd>{@value}</dd>
     * </dl>
     */
    String EXCEPTION_OCCURED_WITH_RESASON = "EECM0001";

    /**
     * �p�����[�^�� <code>null</code> �ł����Ă͂����Ȃ��ꍇ�̃��b�Z�[�W�R�[�h�ł��B
     * <dl>
     * <dt><b>�l :</b></dt>
     * <dd>{@value}</dd>
     * </dl>
     */
    String CANT_BE_NULL = "EECM0002";

    /**
     * �p�����[�^���󕶎���ł����Ă͂����Ȃ��ꍇ�̃��b�Z�[�W�R�[�h�ł��B
     * <dl>
     * <dt><b>�l :</b></dt>
     * <dd>{@value}</dd>
     * </dl>
     */
    String CANT_BE_EMPTY_STRING = "EECM0003";

    /**
     * �^���z�肵�Ă���^�Ɉ�v���Ȃ��ꍇ�̃��b�Z�[�W�R�[�h�ł��B
     * <dl>
     * <dt><b>�l :</b></dt>
     * <dd>{@value}</dd>
     * </dl>
     */
    String TYPE_MISS_MATCH = "EECM0004";

    /**
     * �\�����ʃG���[�����������ꍇ�̃��b�Z�[�W�R�[�h�ł��B
     * <dl>
     * <dt><b>�l :</b></dt>
     * <dd>{@value}</dd>
     * </dl>
     */
    String UNEXPECTED_ERROR = "EECM0005";

    /**
     * Eclipse 3.4 �ȍ~���K�v�ȋ@�\��ǂݍ��񂾏ꍇ�̃��b�Z�[�W�R�[�h�ł��B
     * <dl>
     * <dt><b>�l :</b></dt>
     * <dd>{@value}</dd>
     * </dl>
     */
    String ECLIPSE_3_4_IS_REQUIRED = "WECM0011";

    /**
     * �N���b�v�{�[�h�ɃR�s�[����f�[�^���傫������ꍇ�̃��b�Z�[�W�R�[�h�ł��B
     * <dl>
     * <dt><b>�l :</b></dt>
     * <dd>{@value}</dd>
     * </dl>
     */
    String TOO_LARGE_COPY_TO_CLIPBOARD = "EECM0012";
}
