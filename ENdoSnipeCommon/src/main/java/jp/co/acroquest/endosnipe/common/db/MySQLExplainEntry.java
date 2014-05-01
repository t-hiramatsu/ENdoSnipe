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
package jp.co.acroquest.endosnipe.common.db;

/**
 * MySQL�̎��s�v���1�v�f�B
 * 
 * http://dev.mysql.com/doc/refman/5.1/ja/explain.html
 * 
 * @author eriguchi
 *
 */
public class MySQLExplainEntry
{
    /** SELECT���ʎq�B�N�G�����ɂ����邱�� SELECT�̏����ԍ��B */
    private String id_;

    /** SELECT�߂̎�ށB */
    private String selectType_;

    /** ���ʂ𓾂邽�߂ɎQ�Ƃ���e�[�u���B  */
    private String table_;

    /** �����^�B */
    private String type_;

    /** ���̃e�[�u�����̃��R�[�h�̌����� MySQL �Ŏg�p�\�ȃC���f�b�N�X�B */
    private String possibleKeys_;

    /** MySQL �����ۂɎg�p�����肵���L�[�i�C���f�b�N�X�j�B  */
    private String key_;

    /** MySQL �����ۂɎg�p�����肵���L�[�̒����B key�� NULL�̏ꍇ�A���̒����� NULL�ɂȂ�B*/
    private String keyLen_;

    /** �e�[�u�����烌�R�[�h��I������ۂ� key�ƂƂ��Ɏg�p�����J�����܂��͒萔�B  */
    private String ref_;

    /** �N�G���̎��s�ɍۂ��Ē��ׂ�K�v������� MySQL �ɂ���Ĕ��肳�ꂽ���R�[�h�̐��B */
    private int rows_;

    /** MySQL �łǂ̂悤�ɃN�G������������邩�Ɋւ���ǉ���񂪋L�ڂ����B */
    private String extra_;

    /**
     * �R���X�g���N�^�B
     */
    public MySQLExplainEntry()
    {
    }

    /**
     * SELECT���ʎq���擾����B
     * @return SELECT���ʎq�B
     */
    public String getId()
    {
        return id_;
    }

    /**
     * SELECT�߂̎�ނ��擾����B
     * @return SELECT�߂̎�ށB
     */
    public String getSelectType()
    {
        return selectType_;
    }

    /**
     * ���ʂ𓾂邽�߂ɎQ�Ƃ���e�[�u�����擾����B
     * @return ���ʂ𓾂邽�߂ɎQ�Ƃ���e�[�u���B
     */
    public String getTable()
    {
        return table_;
    }

    /**
     * �����^���擾����B
     * @return �����^�B
     */

    public String getType()
    {
        return type_;
    }

    /**
     * �g�p�\�ȃC���f�b�N�X���擾����B
     * @return �g�p�\�ȃC���f�b�N�X�B
     */
    public String getPossibleKeys()
    {
        return possibleKeys_;
    }

    /**
     * ���ۂɎg�p�����肵���L�[�i�C���f�b�N�X�j���擾����B
     * @return ���ۂɎg�p�����肵���L�[�i�C���f�b�N�X�j�B
     */
    public String getKey()
    {
        return key_;
    }

    /**
     * ���ۂɎg�p�����肵���L�[�̒������擾����B
     * @return ���ۂɎg�p�����肵���L�[�̒����B
     */
    public String getKeyLen()
    {
        return keyLen_;
    }

    /**
     * �e�[�u�����烌�R�[�h��I������ۂ� key�ƂƂ��Ɏg�p�����J�����܂��͒萔���擾����B
     * @return �e�[�u�����烌�R�[�h��I������ۂ� key�ƂƂ��Ɏg�p�����J�����܂��͒萔�B
     */
    public String getRef()
    {
        return ref_;
    }

    /**
     * ���R�[�h�̐����擾����B
     * @return ���R�[�h�̐��B
     */
    public int getRows()
    {
        return rows_;
    }

    /**
     * �ǉ������擾����B
     * @return �ǉ����B
     */
    public String getExtra()
    {
        return extra_;
    }

    /**
     * 
     * @param id SELECT���ʎq�B�N�G�����ɂ����邱�� SELECT�̏����ԍ��B
     */
    public void setId(final String id)
    {
        id_ = id;
    }

    /**
     * 
     * @param selectType SELECT�߂̎�ށB
     */
    public void setSelectType(final String selectType)
    {
        selectType_ = selectType;
    }

    /**
     * 
     * @param table ���ʂ𓾂邽�߂ɎQ�Ƃ���e�[�u���B
     */
    public void setTable(final String table)
    {
        table_ = table;
    }

    /**
     * 
     * @param type �����^�B
     */
    public void setType(final String type)
    {
        type_ = type;
    }

    /**
     * 
     * @param possibleKeys ���̃e�[�u�����̃��R�[�h�̌����� MySQL �Ŏg�p�\�ȃC���f�b�N�X�B
     */
    public void setPossibleKeys(final String possibleKeys)
    {
        possibleKeys_ = possibleKeys;
    }

    /**
     * 
     * @param key MySQL �����ۂɎg�p�����肵���L�[�i�C���f�b�N�X�j�B
     */
    public void setKey(final String key)
    {
        key_ = key;
    }

    /**
     * 
     * 
     * @param keyLen MySQL �����ۂɎg�p�����肵���L�[�̒����B key�� NULL�̏ꍇ�A���̒����� NULL�ɂȂ�B
     */
    public void setKeyLen(final String keyLen)
    {
        keyLen_ = keyLen;
    }

    /**
     * 
     * @param ref �e�[�u�����烌�R�[�h��I������ۂ� key�ƂƂ��Ɏg�p�����J�����܂��͒萔�B
     */
    public void setRef(final String ref)
    {
        ref_ = ref;
    }

    /**
     * 
     * @param rows �N�G���̎��s�ɍۂ��Ē��ׂ�K�v������� MySQL �ɂ���Ĕ��肳�ꂽ���R�[�h�̐��B
     */
    public void setRows(final int rows)
    {
        rows_ = rows;
    }

    /**
     * 
     * @param extra MySQL �łǂ̂悤�ɃN�G������������邩�Ɋւ���ǉ���񂪋L�ڂ����B
     */
    public void setExtra(final String extra)
    {
        extra_ = extra;
    }
}
