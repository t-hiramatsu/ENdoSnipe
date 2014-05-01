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
package jp.co.acroquest.endosnipe.collector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * �f�[�^�x�[�X�̏����Ǘ�����N���X�ł��B<br />
 * 
 * @author fujii
 *
 */
public class DataBaseManager
{
    /** {@link DataBaseManager}�̃V���O���g���C���X�^���X */
    private static final DataBaseManager MANAGER = new DataBaseManager();

    /** DB�̃p�X�Ɛڑ����ۑ������}�b�v */
    private final Map<String, String> dbMap_ = new ConcurrentHashMap<String, String>();

    /**
     * �C���X�^���X����j�~����v���C�x�[�g�R���X�g���N�^�B
     */
    private DataBaseManager()
    {
        // Do Nothing.
    }

    /**
     * �V���O���g���C���X�^���X���擾���܂��B<br />
     * 
     * @return �C���X�^���X
     */
    public static DataBaseManager getInstance()
    {
        return MANAGER;
    }

    /**
     * DB�̕ۑ���ƃz�X�g����Map�Ƃ��ĕۑ����܂��B<br />
     * 
     * @param dbName DB��
     * @param hostInfo �z�X�g���
     */
    public void addDbInfo(final String dbName, final String hostInfo)
    {
        this.dbMap_.put(dbName, hostInfo);
    }

    /**
     * �z�X�g�����擾���܂��B<br />
     * 
     * @param dbName DB��
     * @return �z�X�g���
     */
    public String getHostInfo(final String dbName)
    {
        return this.dbMap_.get(dbName);
    }

    /**
     * DB�̃z�X�g�����폜���܂��B<br />
     * 
     * @param dbName �f�[�^�x�[�X��
     */
    public void removeDbInfo(final String dbName)
    {
        this.dbMap_.remove(dbName);
    }

}
