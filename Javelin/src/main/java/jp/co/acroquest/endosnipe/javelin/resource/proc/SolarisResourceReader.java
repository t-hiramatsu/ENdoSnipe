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
package jp.co.acroquest.endosnipe.javelin.resource.proc;


import jp.co.acroquest.endosnipe.common.config.JavelinConfigUtil;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;

/**
 * Solaris�̃��\�[�X����JNI�o�R�Ŏ擾����N���X.
 * 
 * libkstat, /proc�t�@�C���V�X�e���𗘗p���ă��\�[�X�����擾����B
 * OpenSolaris2009.06(32bit)�œ���m�F�ς݁B
 * 
 * @author hashimoto
 */
public class SolarisResourceReader
{
    // dll �t�@�C�������[�h����
    static
    {
        SystemLogger logger = SystemLogger.getInstance();

        // ���C�u���������[�h���܂�
        JavelinConfigUtil javelinConfigUtil = JavelinConfigUtil.getInstance();

        String libraryPrefix = "./libresource_reader_solaris_";
        String libraryPostfix = ".so";
        
        // CPU arch
        String arch = System.getProperty("os.arch");
        
        // CPU bit��
        String bit = System.getProperty("sun.arch.data.model");
        if (bit == null || bit.length() == 0)
        {
            logger.warn("you have to set \"sun.arch.data.model\" system properties.");
            bit = "";
        }
        
        String libPath = libraryPrefix + arch + "_" + bit + libraryPostfix;
        libPath = javelinConfigUtil.convertRelPathFromJartoAbsPath(libPath);

        if (logger.isDebugEnabled())
        {
            logger.debug("loading so for read system resource : " + libPath);
        }
        
        try 
        {
            System.load(libPath);
        }
        catch (SecurityException se)
        {
            logger.error("Can't load so library : " + libPath, se);
        }
        catch (UnsatisfiedLinkError ule)
        {
            logger.error("Can't load so library : " + libPath, ule);
        }
    }
    
    /**
     * �V�K�N�G���[���쐬
     * @return �쐬�ɐ��������� true
     */
    private native boolean openQuery();

    /**
     * �v��
     * @return �v���ɐ���������true
     */
    private native boolean collectQueryData();

    /**
     * �V�X�e����CPU���ԁiSystem�j���擾
     * @return �擾�����l(nano second)
     */
    public native long getSystemCPUSys();

    /**
     * �V�X�e����CPU���ԁiUser�j���擾
     * @return �擾�����l(nano second)
     */
    public native long getSystemCPUUser();

    /**
     * �V�X�e����CPU���ԁiTotal�j���擾
     * @return �擾�����l(nano second)
     */
    public native long getSystemCPUTotal();

    /**
     * �����������i�t���[�j���擾
     * @return �擾�����l(byte)
     */
    public native long getSystemMemoryFree();

    /**
     * �����������i�ő�j���擾
     * @return �擾�����l(byte)
     */
    public native long getSystemMemoryTotal();

    /**
     * �X���b�v�i�g�p���j���擾
     * @return �擾�����l(byte)
     */
    public native long getSystemSwapFree();

    /**
     * �X���b�v�i�ő�j���擾
     * @return �擾�����l(byte)
     */
    public native long getSystemSwapTotal();


    /**
     * �y�[�W�C�����擾
     * @return �擾�����l
     */
    public native long getSystemPageIn();

    /**
     * �y�[�W�A�E�g���擾
     * @return �擾�����l
     */
    public native long getSystemPageOut();

    /**
     * �v���Z�X��CPU����(User)���擾
     * @return �擾�����l(nano second)
     */
    public native long getProcessCPUUser();

    /**
     * �v���Z�X��CPU����(System)���擾
     * @return �擾�����l(nano second)
     */
    public native long getProcessCPUSys();

    /**
     * ���W���[�t�H�[���g���擾
     * @return �擾�����l
     */
    public native long getProcessMajFlt();

    /**
     * ���z�������g�p�ʂ��擾
     * @return �擾�����l(byte)
     */
    public native long getProcessMemoryVirtual();

    /**
     * �����������g�p�ʂ��擾
     * @return �擾�����l(byte)
     */
    public native long getProcessMemoryPhysical();

    /**
     * �X���b�h�����擾
     * @return �擾�����l
     */
    public native int getNumThreads();

    /**
     * �N�G���[�̎g�p���I��
     * @return �I���ɐ��������� true
     */
    private native boolean closeQuery();

    /**
     * �R���X�g���N�^
     */
    public SolarisResourceReader()
    {
    }
    
    /**
     * �V�X�e�����\�[�X�擾�����̏��������\�b�h
     * @return �������ɐ���������true
     */
    public boolean init()
    {
        boolean result = openQuery();
        return result;
    }
    
    /**
     * ���t���b�V�����܂��B
     * @return ������true/�ُ펞false
     */
    public boolean refresh()
    {
        boolean result = collectQueryData();
        return result;
    }

    /**
     * �V�X�e�����\�[�X�擾�����̏I�����\�b�h
     * @return �I���ɐ���������true
     */
    public boolean destroy()
    {
        closeQuery();
        return true;
    }
    
}
