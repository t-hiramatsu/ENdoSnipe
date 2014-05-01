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

import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * DataSource�쐬�̂��߂̃C���^�t�F�[�X�ł�
 * 
 * @author fujii
 */
public interface DataSourceCreator
{
    /**
     * �x�[�X�f�B���N�g����ݒ肵�܂��B
     * 
     * @param baseDir �x�[�X�f�B���N�g��
     */
    void setBaseDir(String baseDir);

    /**
     * {@link DataSource}�I�u�W�F�N�g���쐬���܂��B<br />
     * 
     * @param dbname �f�[�^�x�[�X��
     * @param connectOnlyExists �R�l�N�V���������݂��Ă��邩�ǂ����B
     * 
     * @return {@link DataSource}
     * @throws SQLException SQL���s���ɗ�O�����������ꍇ
     */
    DataSource createPoolingDataSource(String dbname, boolean connectOnlyExists)
        throws SQLException;

    /**
     * �^�[�Q�b�g�ł��邩�ǂ����B
     * @return �^�[�Q�b�g�ł���ꍇ�A<code>true</code>
     */
    boolean isTarget();

    /**
     * �V�[�P���X�ԍ����擾����SQL���擾���܂��B<br />
     * 
     * @param sequenceName �V�[�P���X��
     * @return �V�[�P���X�ԍ��擾�pSQL
     */
    String getSequenceSql(String sequenceName);

    /**
     * �w�肳�ꂽ�f�[�^�x�[�X�����݂��邩�m�F����B
     * 
     * @param dbName �f�[�^�x�[�X��
     * @return ���݂���ꍇ�� <code>true</code>�A�����łȂ��ꍇ�� <code>false</code>
     */
    boolean existsDatabase(String dbName);

}
