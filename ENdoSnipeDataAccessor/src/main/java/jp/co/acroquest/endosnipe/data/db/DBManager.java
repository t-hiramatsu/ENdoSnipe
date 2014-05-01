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
 * DB�̐ڑ�����ۑ�����N���X�ł��B<br />
 * 
 * @author fujii
 *
 */
public class DBManager
{
    /** PostgreSQL�f�[�^�x�[�X�p�̃h���C�o���� */
    private static final String POSTGRES_DIVERNAME = "org.postgresql.Driver";

    /** ����DB�𗘗p���邩�ǂ����B */
    private static boolean      useDefault__       = true;

    /** �z�X�g�� */
    private static String       hostName__;

    /** �|�[�g�ԍ� */
    private static String       port__;

    /** ���[�U�� */
    private static String       userName__;

    /** �p�X���[�h */
    private static String       password__;

    /** �f�[�^�x�[�X�� */
    private static String       dbName__;

    /** �f�[�^�x�[�X��t�H���_ */
    private static String       dbDir__;

    /**
     * �z�X�g�����擾���܂��B<br />
     * 
     * @return �z�X�g��
     */
    public static String getHostName()
    {
        return hostName__;
    }

    /**
     * �z�X�g����ݒ肵�܂��B<br />
     * 
     * @param hostName �z�X�g��
     */
    public static void setHostName(String hostName)
    {
        hostName__ = hostName;
    }

    /**
     * �|�[�g�ԍ����擾���܂��B<br />
     * 
     * @return �|�[�g�ԍ�
     */
    public static String getPort()
    {
        return port__;
    }

    /**
     * �|�[�g�ԍ���ݒ肵�܂��B<br />
     * 
     * @param port �|�[�g�ԍ�
     */
    public static void setPort(String port)
    {
        port__ = port;
    }

    /**
     * �C���X�^���X����j�~����v���C�x�[�g�R���X�g���N�^
     */
    private DBManager()
    {
        // Do Nothing.
    }

    /**
     * �f�t�H���g��DB�𗘗p���邩�ǂ����B<br />
     * 
     * @return �f�t�H���g��DB�𗘗p����ꍇ�A<code>true</code>
     */
    public static boolean isDefaultDb()
    {
        return useDefault__;
    }

    /**
     * JDBC�h���C�o�N���X�����擾���܂��B<br />
     * ���󂱂̃��\�b�h���g�p����̂�PostgreSQL�݂̂̂��߁A�Ԃ�l�͌Œ�Ƃ���B
     * 
     * @return JDBC�h���C�o�̃N���X��
     */
    public static String getDriverClass()
    {
        return POSTGRES_DIVERNAME;
    }

    /**
     * ���[�U�����擾���܂��B<br />
     * 
     * @return ���[�U��
     */
    public static String getUserName()
    {
        return userName__;
    }

    /**
     * �p�X���[�h���擾���܂��B<br />
     * 
     * @return �p�X���[�h
     */
    public static String getPassword()
    {
        return password__;
    }

    /**
     * �f�[�^�x�[�X�����擾���܂��B<br />
     * 
     * @return �f�[�^�x�[�X��
     */
    public static String getDbName()
    {
        return dbName__;
    }

    /**
     * �f�[�^�x�[�X����ݒ肵�܂��B<br />
     * 
     * @param dbName �f�[�^�x�[�X��
     */
    public static void setDbName(String dbName)
    {
        dbName__ = dbName;
    }

    /**
     * �ݒ�����X�V���܂��B<br />
     * 
     * @param useDefault �f�t�H���g��DB�𗘗p���邩�ǂ����B
     * @param dbDir �f�[�^�x�[�X��f�B���N�g��
     * @param host �z�X�g��
     * @param port �|�[�g�ԍ�
     * @param dbName �f�[�^�x�[�X��
     * @param userName ���[�U��
     * @param password �p�X���[�h
     */
    public static synchronized void updateSettings(boolean useDefault, String dbDir, String host,
        String port, String dbName, String userName, String password)
    {
        useDefault__ = useDefault;
        if (useDefault__ == true)
        {
            dbDir__ = dbDir;
        }
        else
        {
            hostName__ = host;
            port__ = port;
            dbName__ = dbName;
            userName__ = userName;
            password__ = password;
        }
    }

    /**
     * �f�[�^�x�[�X��f�B���N�g�����擾����B
     * 
     * @return dbDir
     */
    public static String getDbDir()
    {
        return dbDir__;
    }

    /**
     * �f�[�^�x�[�X��f�B���N�g����ݒ肷��B
     * 
     * @param dbDir �Z�b�g���� dbDir
     */
    public static void setDbDir(String dbDir)
    {
        dbDir__ = dbDir;
    }

    /**
     * �t�B�[���h�ɕύX�����������m�F���܂��B
     * @param useDefault ����DB�𗘗p���邩�ǂ���
     * @param dbDir DB�f�B���N�g����
     * @param host �z�X�g��
     * @param port �|�[�g�ԍ�
     * @param userName ���[�U��
     * @param password �p�X���[�h
     * @return �ύX���������Ƃ�true/�����łȂ��Ƃ�false
     */
    public static boolean isDirty(boolean useDefault, String dbDir, String host,
        String port, String userName, String password)
    {
        if (useDefault__ != useDefault)
        {
            return true;
        }
        if (dbDir__.equals(dbDir) == false)
        {
            return true;
        }
        if (hostName__.equals(host) == false)
        {
            return true;
        }
        if (port__.equals(port) == false)
        {
            return true;
        }
        if (userName__.equals(userName) == false)
        {
            return true;
        }
        if (password__.equals(password) == false)
        {
            return true;
        }

        return false;
    }
}
