/*
 * Copyright (c) 2004-2009 SMG Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  SMG Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.report.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import jp.co.acroquest.endosnipe.report.util.ReporterConfigAccessor;

/**
 * ���|�[�g�́u��ށv�ɑΉ��������|�[�g�e���v���[�g���Ǘ�����B
 * ���N�G�X�g�ɉ����āA���|�[�g�e���v���[�g���\�[�X�ɃA�N�Z�X���邽�߂̃p�X��Ԃ��B
 * 
 * @author M.Yoshida
 */
public class TemplateFileManager
{
    private static TemplateFileManager instance__ = null;

    /**
     * �C���X�^���X�h�~�̂��߂̃R���X�g���N�^
     */
    private TemplateFileManager()
    // CHECKSTYLE:OFF
    {
        // Do nothing.
    }
    // CHECKSTYLE:ON

    /**
     * �C���X�^���X���擾����B
     * @return �C���X�^���X�B
     */
    public static TemplateFileManager getInstance()
    {
        if (instance__ == null)
        {
            instance__ = new TemplateFileManager();
        }
        return instance__;
    }

    /**
     * ���|�[�g�́u��ށv�ɑΉ�����e���v���[�g�t�@�C�������\�[�X������o��
     * �e���|�����̈�ɃR�s�[��A�e���|�����t�@�C���ւ̐�΃p�X�𐶐�����B
     * 
     * @param type ���|�[�g�̎��
     * @throws IOException ���o�̓G���[������
     * @return �t�@�C���p�X
     */
    public String getTemplateFile(ReportType type)
        throws IOException
    {
        String templateFileResourcePath =
                                          ReporterConfigAccessor.getReportTemplateResourcePath(type);

        if (templateFileResourcePath == null)
        {
            return null;
        }

        URL fileUrl = getClass().getResource(templateFileResourcePath);
        if (fileUrl == null)
        {
            return null;
        }

        File temporaryTemplate = null;
        BufferedInputStream templateFileStream = null;
        BufferedOutputStream bTempStream = null;
        try
        {
            templateFileStream = new BufferedInputStream(fileUrl.openStream());

            temporaryTemplate = File.createTempFile("tempTemplate", ".xls");

            FileOutputStream temporaryStream = new FileOutputStream(temporaryTemplate);
            bTempStream = new BufferedOutputStream(temporaryStream);

            while (true)
            {
                int data = templateFileStream.read();
                if (data == -1)
                {
                    break;
                }
                bTempStream.write(data);
            }

            bTempStream.flush();
        }
        catch (IOException ioex)
        {
            throw ioex;
        }
        finally
        {
            closeStream(templateFileStream);
            closeStream(bTempStream);
        }

        return temporaryTemplate.getAbsolutePath();
    }

    /**
     * �X�g���[�����N���[�Y���܂��B<br />
     * ������ <code>null</code> �̏ꍇ�͉����s���܂���B
     * 
     * @param stream �X�g���[��

     */
    private void closeStream(final Closeable stream)
    {
        if (stream != null)
        {
            try
            {
                stream.close();
            }
            catch (IOException ex)
            // CHECKSTYLE:OFF
            {
                // Do nothing.
            }
            // CHECKSTYLE:ON
        }
    }
}
