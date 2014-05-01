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
package jp.co.acroquest.endosnipe.perfdoctor.rule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.perfdoctor.exception.RuleCreateException;
import jp.co.acroquest.endosnipe.perfdoctor.exception.RuleNotFoundException;
import jp.co.acroquest.endosnipe.perfdoctor.rule.def.RuleSetDef;

/**
 * ���[����`�̒ǉ��A�ύX�A�폜�A�Q�Ƃ��s���N���X�BXML�t�@�C���𗘗p����B
 * @author tanimoto
 *
 */
public class XmlRuleDefAccessor implements RuleDefAccessor
{
    private static final ENdoSnipeLogger LOGGER         =
                                                          ENdoSnipeLogger.getLogger(RuleDefAccessor.class);

    /** �����R�[�hUTF-8��\�������� */
    private static final String          ENCODING_UTF_8 = "utf-8";

    /** �f�[�^��XML�ɖ߂��ۂ̐�����s���I�u�W�F�N�g */
    private Marshaller                   marshaller_;

    /** �f�[�^��XML����擾����ۂ̐�����s���I�u�W�F�N�g */
    private Unmarshaller                 unmarshaller_;

    /**
     * �R���X�g���N�^�Bmarshaller/unmarshaller������������B
     */
    public XmlRuleDefAccessor()
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(RuleSetDef.class);
            this.marshaller_ = context.createMarshaller();
            this.marshaller_.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            this.unmarshaller_ = context.createUnmarshaller();
        }
        catch (JAXBException ex)
        {
            // ignore
            ex.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}<br>
     * XML�t�@�C������`��ǂݍ��ށB
     * @param fileName �ǂݍ��ރt�@�C���̖��O
     * @return ���[���Z�b�g��`(RuleSetDef)
     * @throws RuleNotFoundException �t�@�C����������Ȃ��ꍇ
     * @throws RuleCreateException �ǂݍ��݂Ɏ��s�����ꍇ
     */
    @SuppressWarnings("deprecation")
    public RuleSetDef findRuleSet(final String fileName)
        throws RuleNotFoundException,
            RuleCreateException
    {
        URL url = createURL(fileName);
        if (url == null)
        {
            throw new RuleNotFoundException("ResourceNotFoundMessage", new Object[]{fileName});
        }

        InputStream stream = null;
        try
        {
            stream = url.openStream();
            return (RuleSetDef)this.unmarshaller_.unmarshal(stream);
        }
        catch (IOException ex)
        {
            throw new RuleCreateException("ResourceReadError", new Object[]{url});
        }
        catch (JAXBException ex)
        {
            throw new RuleCreateException("ResourceReadError", new Object[]{url});
        }
        finally
        {
            if (stream != null)
            {
                try
                {
                    stream.close();
                }
                catch (IOException ex)
                {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
    }

    /**
     * �t�@�C�������URL���쐬����B<br>
     * �����t�@�C�������݂����ꍇ�́Afile://����n�܂�URL���쐬���A<br>
     * �����łȂ��ꍇ�̓N���X���[�_�[���t�@�C����T����URL���쐬����B<br>
     * �t�@�C����������Ȃ��ꍇ��null��Ԃ��B
     * @param fileName �t�@�C����
     * @return URL
     */
    @SuppressWarnings("deprecation")
    protected URL createURL(final String fileName)
    {
        File file = new File(fileName);

        URL url = null;
        if (file.exists() && file.isFile())
        {
            try
            {
                url = new URL("file", "", file.getAbsolutePath());
            }
            catch (MalformedURLException ex)
            {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
        else
        {
            url = XmlRuleDefAccessor.class.getResource(fileName);
        }

        return url;
    }

    /**
     * {@inheritDoc}<br>
     * XML�t�@�C���ɒ�`���������ށB
     * @param ruleSetDef ���[���Z�b�g��`(RuleSetDef)
     * @param fileName �������ރt�@�C���̖��O
     */
    @SuppressWarnings("deprecation")
    public void updateRuleSet(final RuleSetDef ruleSetDef, final String fileName)
    {
        File file = new File(fileName);
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;

        try
        {
            fileOutputStream = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, ENCODING_UTF_8);
            this.marshaller_.marshal(ruleSetDef, outputStreamWriter);
        }
        catch (JAXBException ex)
        {
            LOGGER.error(ex.getMessage(), ex);
        }
        catch (UnsupportedEncodingException ex)
        {
            LOGGER.error(ex.getMessage(), ex);
        }
        catch (FileNotFoundException ex)
        {
            LOGGER.error(ex.getMessage(), ex);
        }
        finally
        {
            if (outputStreamWriter != null)
            {
                try
                {
                    outputStreamWriter.close();
                }
                catch (IOException ex)
                {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }

        return;
    }
}
