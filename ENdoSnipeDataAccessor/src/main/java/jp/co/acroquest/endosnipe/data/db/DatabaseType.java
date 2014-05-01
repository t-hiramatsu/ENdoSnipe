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
package jp.co.acroquest.endosnipe.data.db;

/**
 * �f�[�^�x�[�X�̎�ނ�\���񋓑́B<br /> 
 *
 * @author sakamoto
 */
public enum DatabaseType
{
    /** H2 */
    H2("h2"),

    /** PostgreSQL */
    POSTGRES("postgres");

    /** �f�[�^�x�[�X�̎�ނ�\�� ID */
    private final String id_;

    /**
     * �f�[�^�x�[�X�̎�ނ�\�����ڂ𐶐����܂��B<br />
     *
     * @param id �f�[�^�x�[�X�̎�ނ�\�� ID
     */
    private DatabaseType(final String id)
    {
        this.id_ = id;
    }

    /**
     * �f�[�^�x�[�X�̎�ނ�\�� ID ��Ԃ��܂��B<br />
     *
     * @return ID ������
     */
    public String getId()
    {
        return this.id_;
    }

    /**
     * �f�[�^�x�[�X�̎�ނ�\�� ID ����A�I�u�W�F�N�g��Ԃ��܂��B<br />
     *
     * @param id �f�[�^�x�[�X�̎�ނ�\�� ID
     * @return ID �ɑΉ������ނ����݂���ꍇ�̓I�u�W�F�N�g�A���݂��Ȃ��ꍇ�� <code>null</code>
     */
    public static DatabaseType fromId(final String id)
    {
        for (DatabaseType databaseType : values())
        {
            if (databaseType.getId().equals(id))
            {
                return databaseType;
            }
        }
        return null;
    }
}
