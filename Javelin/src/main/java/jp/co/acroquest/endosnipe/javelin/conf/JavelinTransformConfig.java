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
package jp.co.acroquest.endosnipe.javelin.conf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import jp.co.acroquest.endosnipe.javelin.common.JavassistUtil;
import jp.co.smg.endosnipe.javassist.ClassPool;
import jp.co.smg.endosnipe.javassist.CtClass;

/**
 * Javelin�̃R�[�h���ߍ��ݐݒ�B
 * 
 * @author yamasaki
 */
public class JavelinTransformConfig
{
    /** �R���o�[�^�̐ݒ胊�X�g */
    private final List<ConverterConfig> converterConfigList_ = new ArrayList<ConverterConfig>();

    /** Include�̐ݒ胊�X�g */
    private final List<IncludeConversionConfig> includeConfigList_ =
            new ArrayList<IncludeConversionConfig>();

    /** Exclude�̐ݒ胊�X�g */
    private final List<ExcludeConversionConfig> excludeConfigList_ =
            new ArrayList<ExcludeConversionConfig>();

    /** �ݒ�t�@�C���̃��[�_ */
    private BufferedReader reader_;

    /**
     * �ݒ�t�@�C����ǂݍ��ށB
     * @param includeStream Include�t�@�C���p���̓X�g���[��
     * @param excludeStream Exclude�t�@�C���p���̓X�g���[��
     * @throws IOException �t�@�C���ǂݍ��ݎ��ɔ���������o�͗�O
     */
    public void readConfig(final InputStream includeStream, final InputStream excludeStream)
        throws IOException
    {
        this.reader_ = new BufferedReader(new InputStreamReader(includeStream));

        String line;

        line = readLine();
        while (line != null)
        {
            if (line.startsWith(ConverterConfig.PREFIX))
            {
                ConverterConfig config = new ConverterConfig();
                config.readConfig(line);
                this.converterConfigList_.add(config);
            }
            else
            {
                IncludeConversionConfig config = new IncludeConversionConfig();
                config.readConfig(line);
                this.includeConfigList_.add(config);
            }

            line = readLine();
        }

        this.reader_ = new BufferedReader(new InputStreamReader(excludeStream));

        line = readLine();
        while (line != null)
        {
            ExcludeConversionConfig config = new ExcludeConversionConfig();
            config.readConfig(line);
            this.excludeConfigList_.add(config);

            line = readLine();
        }
    }

    /**
     * �R�[�h���ߍ��ݑΏۂ��珜�O����邩�ǂ�����Ԃ��B
     * @param className �N���X��
     * @return �R�[�h���ߍ��ݑΏۂ��珜�O����邩�ǂ���
     */
    public boolean isExcludeClass(final String className)
    {
        boolean result = false;

        for (ExcludeConversionConfig config : this.excludeConfigList_)
        {
            Matcher matcher = config.getClassNamePattern().matcher(className);
            if (matcher.matches() && ".*".equals(config.getMethodNamePattern()))
            {
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * �R�[�h���ߍ��ݑΏۂ��珜�O�����N���X�̃��X�g��Ԃ��B
     * @param className �N���X��
     * @return �R�[�h���ߍ��ݑΏۂ��珜�O�����N���X�̃��X�g
     */
    public List<ExcludeConversionConfig> matchesToExclude(final String className)
    {
        List<ExcludeConversionConfig> list = new ArrayList<ExcludeConversionConfig>();

        for (ExcludeConversionConfig config : this.excludeConfigList_)
        {
            Matcher matcher = config.getClassNamePattern().matcher(className);
            if (matcher.matches())
            {
                list.add(config);
            }
        }

        return list;
    }

    /**
     * �R�[�h���ߍ��ݑΏۃN���X�̃��X�g��Ԃ��B
     * @param className �N���X��
     * @param ctClass CtClass
     * @param pool ClassPool
     * @return �R�[�h���ߍ��ݑΏۃN���X�̃��X�g
     */
    public List<IncludeConversionConfig> matchesToInclude(final String className,
            final CtClass ctClass, final ClassPool pool)
    {
        List<IncludeConversionConfig> list = new ArrayList<IncludeConversionConfig>();

        for (IncludeConversionConfig config : this.includeConfigList_)
        {
            if (config.isInheritance())
            {
                boolean isInherited =
                        JavassistUtil.isInherited(ctClass, pool, config.getClassName());
                if (isInherited)
                {
                    list.add(config);
                }
            }
            else
            {
                Matcher matcher = config.getClassNamePattern().matcher(className);
                if (matcher.matches())
                {
                    list.add(config);
                }
            }
        }

        return list;
    }

    /**
     * �R���o�[�^�̃N���X���̃��X�g��Ԃ��B
     * @param converterName �R���o�[�^��
     * @return �R���o�[�^�̃N���X���̃��X�g
     */
    public List<String> getConverterClassNames(final String converterName)
    {
        for (ConverterConfig config : this.converterConfigList_)
        {
            if (config.getName().equals(converterName))
            {
                return Arrays.asList(config.getConverterNames());
            }
        }

        return Arrays.asList(new String[]{converterName});
    }

    /**
     * �R���o�[�^���̃��X�g��Ԃ��B
     * @return �R���o�[�^���̃��X�g
     */
    public List<String> getConverterNames()
    {
        List<String> converterNameList = new ArrayList<String>();
        for (ConverterConfig config : this.converterConfigList_)
        {
            converterNameList.add(config.getName());
        }

        return converterNameList;
    }

    /**
     * �R���o�[�^�̐ݒ胊�X�g�AInclude�̐ݒ胊�X�g�AExclude�̐ݒ胊�X�g��Ԃ��B
     * @return �R���o�[�^�̐ݒ胊�X�g�AInclude�̐ݒ胊�X�g�AExclude�̐ݒ胊�X�g
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder(super.toString());

        builder.append(this.converterConfigList_.toString());
        builder.append(this.includeConfigList_.toString());
        builder.append(this.excludeConfigList_.toString());

        return builder.toString();
    }

    /**
     * 1�s�ǂݍ��݁A�R�����g�s�̏ꍇ�A���̍s��ǂݍ��ށB
     * @return �R�����g�ȊO�̍s
     * @throws IOException ���o�͗�O
     */
    private String readLine()
        throws IOException
    {
        String line = this.reader_.readLine();
        while (line != null && (line.startsWith("#") || line.length() == 0))
        {
            line = this.reader_.readLine();
        }

        return line;
    }
}
