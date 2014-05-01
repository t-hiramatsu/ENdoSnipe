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
package jp.co.acroquest.endosnipe.collector.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import jp.co.acroquest.endosnipe.collector.Bootstrap;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.util.PathUtil;

/**
 * ��ʕ\�������Ǘ�����N���X�B
 * @author acroquest
 *
 */
public class DisplayNameManager
{
    /** singleton�C���X�^���X */
    private static DisplayNameManager manager__ = new DisplayNameManager();

    /** ����ϊ��p�}�b�v */
    private final HashMap<String, String> convMap_;

    /** �v���p�e�B�t�@�C���̃v���t�B�N�X */
    private static final String PREFIX = "../conf/displayname_";

    /** �v���p�e�B�t�@�C���̊g���q */
    private static final String EXTENSION = ".properties";

    /** �R�����g���� */
    private static final String COMMENT = "#";

    /**
     * �C���X�^���X����h��private�R���X�g���N�^
     */
    private DisplayNameManager()
    {
        convMap_ = new HashMap<String, String>(0);
    }

    /**
     * �C���X�^���X���擾���܂��B
     * 
     * @return �C���X�^���X
     */
    public static DisplayNameManager getManager()
    {
        return manager__;
    }

    /**
     * ����ϊ��p�̃v���p�e�B�t�@�C����ǂݍ��݁A�ϊ��}�b�v�����������܂��B<br />
     * �����Ŏw�肳�ꂽ����R�[�h�ɍ��킹��<br />
     * �v���p�e�B�t�@�C�� displayname_xx.properties ��ǂݍ��݂܂��B
     * 
     * @param lang �w�茾��
     */
    public void init(final String lang)
    {
        // ���O�o�͗p�C���X�^���X
        final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(Bootstrap.class);

        if (lang == null || "".equals(lang))
        {
            LOGGER.error("�\�����ꂪ�ݒ肳��Ă��܂���B");
            return;
        }

        // �v���p�e�B�t�@�C���̐�΃p�X���擾
        String filename = PathUtil.getJarDir(Bootstrap.class) + PREFIX + lang + EXTENSION;
        File file = new File(filename);
        if (!file.exists())
        {
            LOGGER.error("�\�����ݒ�t�@�C�������݂��܂���B");
            return;
        }
        else
        {
            LOGGER.info("�\�����ݒ�t�@�C�� " + filename + " ��ǂݍ��݂܂��B");
        }

        // �v���p�e�B�t�@�C���̓ǂݍ���
        BufferedReader in = null;
        try
        {
            in =
                 new BufferedReader(new InputStreamReader(new FileInputStream(file), "Windows-31J"));

            String line = "";
            // �P�s���ǂݍ��݉��
            while (null != (line = in.readLine()))
            {
                // �R�����g�͓ǂݔ�΂�
                if (line.startsWith(COMMENT))
                {
                    continue;
                }

                // �C�R�[���ŕ���
                String[] elements = line.split("=");
                if (elements.length != 2)
                {
                    continue;
                }

                LOGGER.info("���ږ�[" + elements[0] + "] : �\����[" + elements[1] + "]");
                this.convMap_.put(elements[0], elements[1]);
            }

            in.close();
        }
        catch (IOException e)
        {
            LOGGER.error("�\�����ݒ�t�@�C���̓ǂݍ��݂Ɏ��s���܂����B");
        }

        return;
    }

    /**
     * �\�����ϊ��}�b�v���擾���܂��B
     * 
     * @return �\�����ϊ��}�b�v
     */
    public HashMap<String, String> getConvMap()
    {
        return convMap_;
    }
}
