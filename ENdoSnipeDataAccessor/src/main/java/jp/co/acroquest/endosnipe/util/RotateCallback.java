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
package jp.co.acroquest.endosnipe.util;

import java.sql.SQLException;

/**
 * ���[�e�[�g�������� truncate ����K�v������ꍇ��
 * �R�[���o�b�N����郁�\�b�h���`����C���^�t�F�[�X�B
 *
 * @author sakamoto
 */
public interface RotateCallback
{
    /**
     * �e�[�u���̎�ނ�Ԃ��܂��B
     *
     * @return �e�[�u���̎�ނ�\��������
     */
    String getTableType();

    /**
     * �w�肵���C���f�b�N�X�̃e�[�u���� truncate ���܂��B
     *
     * @param database �f�[�^�x�[�X��
     * @param tableIndex �e�[�u���C���f�b�N�X
     * @param year ���ɂ��̃e�[�u���ɓ����f�[�^�̔N
     * @throws SQLException SQL ���s���ɗ�O�����������ꍇ
     */
    void truncate(String database, int tableIndex, int year) throws SQLException;
}
