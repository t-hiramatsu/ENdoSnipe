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
package jp.co.acroquest.endosnipe.javelin.jdbc.instrument;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * PostgreSQL��PreparedStatement�̎��s�v�����邽�߂Ɏg�p����N���X�B
 *
 * @author sakamoto
 */
public class PreparedStatementPair
{

    /** ���s�v��擾�pPreparedStatement */
    private final PreparedStatement pstmtForPlan_;

    /** �o�C���h�ϐ��̐� */
    private final int bindValCount_;

    /** DML�Ȃ�<code>true</code> */
    private final boolean isDml_;

    /**
     * ���s�v��擾�pPreparedStatement���쐬����B
     *
     * @param connect DB�R�l�N�V����
     * @param sql SQL��
     * @param isDml �w�肵��SQL����DML�Ȃ�<code>true</code>
     * @throws SQLException SQL���ɃG���[���������Ƃ�
     */
    public PreparedStatementPair(final Connection connect, final String sql, final boolean isDml)
        throws SQLException
    {
        this.pstmtForPlan_ = connect.prepareStatement(sql);
        this.bindValCount_ = countBindVal(sql);
        this.isDml_ = isDml;
    }

    /**
     * ���s�v��擾�pPreparedStatement��Ԃ��B
     *
     * @return ���s�v��擾�pPreparedStatement
     */
    public PreparedStatement getPreparedStatement()
    {
        return this.pstmtForPlan_;
    }

    /**
     * ���s�v��擾�pPreparedStatement�Ɏg����o�C���h�ϐ��̐���Ԃ��B
     *
     * @return �o�C���h�ϐ��̐�
     */
    public int getBindValCount()
    {
        return this.bindValCount_;
    }

    /**
     * DML���ǂ�����Ԃ��B
     *
     * @return DML�Ȃ�<code>true</code>
     */
    public boolean isDml()
    {
        return this.isDml_;
    }

    /**
     * �w�肳�ꂽSQL���̃o�C���h�ϐ��̐��𐔂���B
     *
     * @param sql SQL��
     * @return �o�C���h�ϐ��̐�
     */
    private int countBindVal(final String sql)
    {
        // �o�C���h�ϐ��̐�
        int count = 0;
        // �V���O���N�H�[�e�[�V�����̒��ɂ����true
        boolean singlequoteFlag = false;
        // �_�u���N�H�[�e�[�V�����̒��ɂ���ꍇ��true
        boolean doublequoteFlag = false;

        for (int index = 0; index < sql.length(); index++)
        {
            char checkchar = sql.charAt(index);

            if (checkchar == '\'')
            {
                // �V���O���N�H�[�e�[�V�����������Ƃ��́A
                // �_�u���N�H�[�e�[�V�����̒��łȂ����
                // �V���O���N�H�[�e�[�V�����t���O��؂�ւ���B
                if (doublequoteFlag == false)
                {
                    singlequoteFlag = !singlequoteFlag;
                }
            }
            else if (checkchar == '"')
            {
                // �_�u���N�H�[�e�[�V�����������Ƃ��́A
                // �V���O���N�H�[�e�[�V�����̒��łȂ����
                // �_�u���N�H�[�e�[�V�����t���O��؂�ւ���B
                if (singlequoteFlag == false)
                {
                    doublequoteFlag = !doublequoteFlag;
                }
            }
            else if (checkchar == '?')
            {
                // �N�G�X�`�����������Ƃ��́A
                // �V���O���N�H�[�e�[�V�����̒��ł��_�u���N�H�[�e�[�V�����̒��ł��Ȃ��ꍇ��
                // �J�E���g����B
                if (singlequoteFlag == false && doublequoteFlag == false)
                {
                    count++;
                }
            }
        }

        return count;
    }

}