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
package jp.co.acroquest.endosnipe.common.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.common.util.StreamUtil;

/**
 * javelin �̐ݒ�t�@�C���փA�N�Z�X���邽�߂̃��[�e�B���e�B�N���X�ł��B<br />
 * �{�N���X�́A�l���͂��߂Ď擾���悤�Ƃ����Ƃ��igetter���\�b�h���͂��߂ČĂ񂾂Ƃ��j
 * �Ƀt�@�C������ݒ�����[�h���܂��B<br />
 *
 * @author sakamoto
 */
public class JavelinConfigUtil
{
    /** Javelin�I�v�V�����L�[1 */
    private static final String JAVELIN_OPTION_KEY_1 = JavelinConfig.JAVELIN_PREFIX + "property";

    /** Javelin�I�v�V�����L�[2 */
    private static final String JAVELIN_OPTION_KEY_2 = JavelinConfig.JAVELIN_PREFIX + "properties";

    /** �ݒ�t�@�C����1 */
    private final String fileName1_;

    /** �ݒ�t�@�C����2 */
    private final String fileName2_;

    /** Javelin �v���p�e�B */
    private Properties properties_;

    /** �{�N���X�̃C���X�^���X */
    private static JavelinConfigUtil instance__ = new JavelinConfigUtil();

    /** Javelin���sJar�t�@�C���̑��݃f�B���N�g�� */
    private static String absoluteJarDirectory__;

    /** PropertyFile�̃p�X */
    private String propertyFilePath_;

    /** PropertyFile�̑��݃f�B���N�g�� */
    private String propertyFileDirectory_;

    /** Boolean�̒l��ێ�����B */
    private Map<String, Boolean> booleanMap_ = new ConcurrentHashMap<String, Boolean>();

    /** Long�̒l��ێ�����B */
    private Map<String, Long> longMap_ = new ConcurrentHashMap<String, Long>();

    /** Double�̒l��ێ�����B */
    private Map<String, Double> doubleMap_ = new ConcurrentHashMap<String, Double>();

    /** Integer�̒l��ێ�����B */
    private Map<String, Integer> intMap_ = new ConcurrentHashMap<String, Integer>();

    private JavelinConfigUtil()
    {
        this.fileName1_ = System.getProperty(JAVELIN_OPTION_KEY_1);
        this.fileName2_ = System.getProperty(JAVELIN_OPTION_KEY_2);
    }

    /**
     * {@link JavelinConfigUtil} �̃C���X�^���X���擾���܂��B<br />
     *
     * @return {@link JavelinConfigUtil} �̃C���X�^���X
     */
    public static JavelinConfigUtil getInstance()
    {
        return instance__;
    }

    /** �C�x���g���x���Ƃ��ē��͉\�Ȓl */
    private static final String[] EVENT_LEVELS = {"ERROR", "WARN", "INFO"};

    /** ���O���x���Ƃ��ē��͉\�Ȓl */
    private static final String[] LOG_LEVELS = {"FATAL", "ERROR", "WARN", "INFO", "DEBUG"};

    /**
     * �ݒ�t�@�C����ǂݍ��݂܂��B<br />
     */
    private void load()
    {
        this.properties_ = new Properties();

        String fileName = null;

        if (getFileName1() != null)
        {
            fileName = getFileName1();
        }
        else if (getFileName2() != null)
        {
            fileName = getFileName2();
        }
        else
        {
            fileName = "../conf/javelin.properties";
        }

        this.propertyFilePath_ = convertRelPathFromJartoAbsPath(fileName);

        if (this.propertyFilePath_ != null)
        {
            File file = null;
            InputStream stream = null;
            try
            {
                file = new File(this.propertyFilePath_);
                if (!file.exists())
                {
                    System.err.println("�v���p�e�B�t�@�C�������݂��܂���B" + "(" + file.getAbsolutePath() + ")");
                    return;
                }
                stream = ConfigPreprocessor.process(file);
                this.properties_.load(stream);

                // �ݒ�t�@�C���i*.conf�j�̂���f�B���N�g�����擾����
                File optionFile = new File(this.propertyFilePath_);
                File optionPath = optionFile.getParentFile();
                if (optionPath != null)
                {
                    setPropertyFileDirectory(optionPath.getAbsolutePath());
                    properties_.setProperty(JAVELIN_OPTION_KEY_1, optionPath.getAbsolutePath());
                }
            }
            catch (Exception ex)
            {
                if (file != null)
                {
                    System.err.println("�v���p�e�B�t�@�C���̓ǂݍ��݂Ɏ��s���܂����B" + "(" + file.getAbsolutePath()
                            + ")");
                }
            }
            finally
            {
                StreamUtil.closeStream(stream);
            }
        }
        else
        {
            System.err.println("�K�v�ȃv���p�e�B(-Djavelin.property)���w�肳��Ă��܂���B");
        }
    }

    /**
     * �ݒ�t�@�C����ǂݍ��݂܂��B<br />
     * @param relPath �ݒ�t�@�C��(jar�t�@�C������̑��΃p�X)
     * @return �ǂݍ��񂾐ݒ�
     */
    public static Properties loadProperties(final String relPath)
    {
        Properties properties = new Properties();

        String fileName = convertRelPathFromJartoAbsPath(relPath);

        File file = null;
        InputStream stream = null;
        try
        {
            file = new File(fileName);
            if (!file.exists())
            {
                System.err.println("�v���p�e�B�t�@�C�������݂��܂���B" + "(" + file.getAbsolutePath() + ")");
            }
            stream = ConfigPreprocessor.process(file);
            properties.load(stream);
        }
        catch (Exception ex)
        {
            if (file != null)
            {
                System.err.println("�v���p�e�B�t�@�C���̓ǂݍ��݂Ɏ��s���܂����B" + "(" + file.getAbsolutePath() + ")");
            }
        }
        finally
        {
            StreamUtil.closeStream(stream);
        }
        return properties;
    }

    /**
     * ���O���x���̎擾�ɂ����āA�w�肳�ꂽ�L�[�ɑΉ�����l��Ԃ��܂��B<br />
     * �����ݒ肪�s���Ă��Ȃ��Ƃ��A�ݒ�l���ُ�̏ꍇ�ɂ́A�f�t�H���g�l��Ԃ��܂��B<br />
     *
     * @param key �L�[
     * @param defaultValue �f�t�H���g�l
     * @return �l
     */
    public String getLogLevel(final String key, final String defaultValue)
    {
        return getLevel(key, defaultValue, LOG_LEVELS);
    }

    /**
     * �C�x���g�̃��x���̎擾�ɂ����āA�w�肳�ꂽ�L�[�ɑΉ�����l��Ԃ��܂��B<br />
     * �����ݒ肪�s���Ă��Ȃ��Ƃ��A�ݒ�l���ُ�̏ꍇ�ɂ́A�f�t�H���g�l��Ԃ��܂��B<br />
     *
     * @param key �L�[
     * @param defaultValue �f�t�H���g�l
     * @return �l
     */
    public String getEventLevel(final String key, final String defaultValue)
    {
        return getLevel(key, defaultValue, EVENT_LEVELS);
    }

    /**
     * ���x���̎擾�ɂ����āA�w�肳�ꂽ�L�[�ɑΉ�����l��Ԃ��܂��B<br />
     * �����ݒ肪�s���Ă��Ȃ��Ƃ��A�ݒ�l���ُ�̏ꍇ�ɂ́A�f�t�H���g�l��Ԃ��܂��B<br />
     *
     * @param key �L�[
     * @param defaultValue �f�t�H���g�l
     * @param levelArray ���x���Ƃ��ē��͉\�Ȓl�̔z��
     * 
     * @return �l
     */
    private String getLevel(final String key, final String defaultValue, final String[] levelArray)
    {

    	String value = null;
        synchronized (this)
        {
        	value = this.properties_.getProperty(key);
	        if (value == null)
	        {
	            value = defaultValue.toUpperCase();
	            this.properties_.put(key, value);
	            return value;
	        }
        }
        value = value.toUpperCase();
        for (int num = 0; num < levelArray.length; num++)
        {
            if (levelArray[num].equals(value))
            {
                return value;
            }
        }
        System.out.println(key + "�ɕs���Ȓl�����͂���܂����B�f�t�H���g�l(" + defaultValue.toUpperCase() + ")���g�p���܂��B");
        synchronized (this)
        {
        	this.properties_.put(key, defaultValue.toUpperCase());
        }
        return defaultValue;
    }

    /**
     * �w�肳�ꂽ�L�[�ɑΉ�����l��Ԃ��܂��B<br />
     * �����ݒ肪�s���Ă��Ȃ��Ƃ��ɂ́A�f�t�H���g�l��Ԃ��܂��B<br />
     *
     * @param key �L�[
     * @param defaultValue �f�t�H���g�l
     * @return �l
     */
    public String getString(final String key, final String defaultValue)
    {
    	String value = null;
        synchronized (this)
        {
            if (this.properties_ == null)
            {
                load();
            }
            value = this.properties_.getProperty(key);
        }
        if (value == null)
        {
            value = defaultValue;
        }
        return value;
    }

    /**
     * �w�肳�ꂽ�L�[�ɑΉ����鐔�l��Ԃ��܂��B<br />
     * �����ݒ肪�s���Ă��Ȃ��Ƃ���A�s���Ȓl�����͂���Ă���Ƃ��́A
     * �f�t�H���g�l��Ԃ��܂��B<br />
     *
     * @param key �L�[
     * @param defaultValue �f�t�H���g�l
     * @return �l
     */
    public int getInteger(final String key, final int defaultValue)
    {
        Integer intValue = intMap_.get(key);
        if (intValue == null)
        {
            String value = getString(key, null);
            intValue = Integer.valueOf(defaultValue);
            if (value != null)
            {
                try
                {
                    intValue = Integer.valueOf(value);
                }
                catch (NumberFormatException nfe)
                {
                    System.out.println(key + "�ɕs���Ȓl�����͂���܂����B�f�t�H���g�l(" + intValue + ")���g�p���܂��B");
                    setInteger(key, intValue);
                }
            }

            intMap_.put(key, intValue);
        }

        return intValue.intValue();
    }

    /**
     * �w�肳�ꂽ�L�[�ɑΉ����鐔�l��Ԃ��܂��B<br />
     * �����ݒ肪�s���Ă��Ȃ��Ƃ���s���Ȓl�����͂���Ă���Ƃ��́A
     * �f�t�H���g�l��Ԃ��܂��B<br />
     *
     * @param key �L�[
     * @param defaultValue �f�t�H���g�l
     * @return �l
     */
    public long getLong(final String key, final long defaultValue)
    {
        Long longValue = longMap_.get(key);
        if (longValue == null)
        {
            String value = getString(key, null);
            longValue = Long.valueOf(defaultValue);
            if (value != null)
            {
                try
                {
                    longValue = Long.valueOf(value);
                }
                catch (NumberFormatException nfe)
                {
                    System.out.println(key + "�ɕs���Ȓl�����͂���܂����B�f�t�H���g�l(" + defaultValue + ")���g�p���܂��B");
                    setLong(key, longValue);
                }
            }

            longMap_.put(key, longValue);
        }

        return longValue.longValue();
    }

    /**
     * �w�肳�ꂽ�L�[�ɑΉ����鐔�l��Ԃ��܂��B<br />
     * �����ݒ肪�s���Ă��Ȃ��Ƃ���s���Ȓl�����͂���Ă���Ƃ��́A
     * �f�t�H���g�l��Ԃ��܂��B<br />
     *
     * @param key �L�[
     * @param defaultValue �f�t�H���g�l
     * @return �l
     */
    public double getDouble(final String key, final double defaultValue)
    {
        Double doubleValue = doubleMap_.get(key);
        if (doubleValue == null)
        {
            String value = getString(key, null);
            doubleValue = Double.valueOf(defaultValue);
            if (value != null)
            {
                try
                {
                    doubleValue = Double.valueOf(value);
                }
                catch (NumberFormatException nfe)
                {
                    System.out.println(key + "�ɕs���Ȓl�����͂���܂����B�f�t�H���g�l(" + defaultValue + ")���g�p���܂��B");
                    setDouble(key, doubleValue);
                }
            }

            doubleMap_.put(key, doubleValue);
        }

        return doubleValue.doubleValue();
    }

    /**
     * �w�肳�ꂽ�L�[�ɑΉ�����Boolean�l��Ԃ��܂��B<br />
     * �����ݒ肪�s���Ă��Ȃ���A�s���Ȓl�����͂���Ă���Ƃ��́A
     * �f�t�H���g�l��Ԃ��܂��B
     *
     * @param key �L�[
     * @param defaultValue �f�t�H���g�l
     * @return �l
     */
    public boolean getBoolean(final String key, final boolean defaultValue)
    {
        Boolean value = booleanMap_.get(key);

        if (value == null)
        {
            String valueStr = getString(key, null);

            value = defaultValue;
            if (valueStr != null)
            {
                if ("true".equals(valueStr))
                {
                    value = true;
                }
                else if ("false".equals(valueStr))
                {
                    value = false;
                }
                else
                {
                    System.out.println(key + "�ɕs���Ȓl�����͂���܂����B�f�t�H���g�l(" + defaultValue + ")���g�p���܂��B");
                    setBoolean(key, value);
                }
            }

            booleanMap_.put(key, value);
        }

        return value;
    }

    /**
     * �w�肳�ꂽ�L�[�ɕ�������Z�b�g���܂��B<br />
     *
     * @param key �L�[
     * @param value �l
     */
    public void setString(final String key, final String value)
    {
        synchronized (this)
        {
            if (this.properties_ == null)
            {
                load();
            }
            this.properties_.setProperty(key, value);
        }
    }

    /**
     * �w�肳�ꂽ�L�[�ɐ��l���Z�b�g���܂��B<br />
     *
     * @param key �L�[
     * @param value �l
     */
    public void setInteger(final String key, final int value)
    {
        String valueString = String.valueOf(value);
        setString(key, valueString);
    }

    /**
     * �w�肳�ꂽ�L�[�ɐ��l���Z�b�g���܂��B<br />
     *
     * @param key �L�[
     * @param value �l
     */
    public void setLong(final String key, final long value)
    {
        String valueString = String.valueOf(value);
        setString(key, valueString);
    }

    /**
     * �w�肳�ꂽ�L�[�ɐ��l���Z�b�g���܂��B<br />
     *
     * @param key �L�[
     * @param value �l
     */
    public void setDouble(final String key, final double value)
    {
        String valueString = String.valueOf(value);
        setString(key, valueString);
    }

    /**
     * �w�肳�ꂽ�L�[��boolean�l���Z�b�g���܂��B<br />
     *
     * @param key �L�[
     * @param value �l
     */
    public void setBoolean(final String key, final boolean value)
    {
        String valueString = String.valueOf(value);
        setString(key, valueString);
    }

    /**
     * �w�肳�ꂽ�L�[���ݒ�ɑ��݂��邩�ǂ����𒲂ׂ܂��B<br />
     *
     * @param key �L�[
     * @return ���݂���ꍇ <code>true</code>�A���݂��Ȃ��ꍇ�A<code>false</code>
     */
    public boolean isKeyExist(final String key)
    {
    	boolean ret = false;
        synchronized (this)
        {
            if (this.properties_ == null)
            {
                load();
            }
            ret = this.properties_.containsKey(key);
        }
        return ret;
    }

    /**
     * �ݒ�t�@�C������Ԃ��܂��B<br />
     *
     * @return �ݒ�t�@�C����
     */
    public String getFileName()
    {
        return getFileName1();
    }

    /**
     * �ݒ�t�@�C������Ԃ��܂��B<br />
     *
     * @return �ݒ�t�@�C����
     */
    private String getFileName1()
    {
        return this.fileName1_;
    }

    /**
     * �ݒ�t�@�C������Ԃ��܂��B<br />
     *
     * @return �ݒ�t�@�C����
     */
    private String getFileName2()
    {
        return this.fileName2_;
    }

    /**
     * Javelin���sJar�t�@�C���̑��݃f�B���N�g����Ԃ��܂��B<br />
     * 
     * @return Javelin���sJar�t�@�C���̑��݃f�B���N�g��
     */
    public String getAbsoluteJarDirectory()
    {
        return this.absoluteJarDirectory__;
    }

    /**
     * Javelin���sJar�t�@�C���̑��݃f�B���N�g����ݒ肵�܂��B<br />
     * 
     * @param absoluteJarDirectory Javelin���sJar�t�@�C���̑��݃f�B���N�g��
     */
    public void setAbsoluteJarDirectory(final String absoluteJarDirectory)
    {
        this.absoluteJarDirectory__ = absoluteJarDirectory;
    }

    /**
     * �v���p�e�B�t�@�C���̑��݃f�B���N�g����Ԃ��܂��B<br />
     * 
     * @return �v���p�e�B�t�@�C���̑��݃f�B���N�g��
     */
    public String getPropertyFileDirectory()
    {
        return this.propertyFileDirectory_;
    }

    /**
     * �v���p�e�B�t�@�C���̑��݃f�B���N�g����ݒ肵�܂��B<br />
     * 
     * @param propertyFileDirectory �v���p�e�B�t�@�C���̑��݃f�B���N�g��
     */
    public void setPropertyFileDirectory(final String propertyFileDirectory)
    {
        this.propertyFileDirectory_ = propertyFileDirectory;
    }

    /**
     * �v���p�e�B�t�@�C���̃p�X��ݒ肵�܂��B<br />
     * 
     * @param propertyFilePath �v���p�e�B�t�@�C���̃p�X
     */
    public void setPropertyFilePath(final String propertyFilePath)
    {
        this.propertyFilePath_ = propertyFilePath;
    }

    /**
     * �v���p�e�B�t�@�C���̃p�X��Ԃ��܂��B<br />
     * 
     * @return �v���p�e�B�t�@�C���̃p�X
     */
    public String getPropertyFilePath()
    {
        return this.propertyFilePath_;
    }

    /**
     * Javelin���sJar�t�@�C������̑��΃p�X���΃p�X�ɕϊ����܂��B<br />
     * 
     * @param relativePath Javelin���sJar�t�@�C������̑��΃p�X
     * @return ��΃p�X
     */
    public static String convertRelPathFromJartoAbsPath(final String relativePath)
    {
        if (relativePath == null)
        {
            return null;
        }

        File relativeFile = new File(relativePath);
        if (relativeFile.isAbsolute())
        {
            return relativePath;
        }
        File targetPath = new File(absoluteJarDirectory__, relativePath);

        String canonicalPath;
        try
        {
            canonicalPath = targetPath.getCanonicalPath();
        }
        catch (IOException ioe)
        {
            return targetPath.getAbsolutePath();
        }

        return canonicalPath;
    }

    /**
     * �v���p�e�B�t�@�C������̑��΃p�X���΃p�X�ɕϊ����܂��B<br />
     * 
     * @param relativePath �v���p�e�B�t�@�C������̑��΃p�X
     * @return ��΃p�X
     */
    public String convertRelativePathtoAbsolutePath(final String relativePath)
    {
        if (relativePath == null)
        {
            return null;
        }

        File relativeFile = new File(relativePath);
        if (relativeFile.isAbsolute())
        {
            return relativePath;
        }

        File targetPath = new File(this.propertyFileDirectory_, relativePath);

        String canonicalPath;
        try
        {
            canonicalPath = targetPath.getCanonicalPath();
        }
        catch (IOException ioe)
        {
            return targetPath.getAbsolutePath();
        }

        return canonicalPath;
    }

    /**
     * ���ׂĂ̐ݒ�̍X�V�𔽉f���܂��B<br />
     */
    public void update()
    {
        this.longMap_ = new ConcurrentHashMap<String, Long>();
        this.booleanMap_ = new ConcurrentHashMap<String, Boolean>();
        this.intMap_ = new ConcurrentHashMap<String, Integer>();
        this.doubleMap_ = new ConcurrentHashMap<String, Double>();
    }

    /**
     * �w�肵���L�[������Boolean�̒l�̍X�V�𔽉f���܂��B<br />
     * 
     * ������Ƃ��āA�w�肵���L�[������Boolean�̒l��booleanMap_����폜���܂��B
     * �폜���邱�Ƃɂ���āA�w�肳�ꂽ�L�[�����l�����Ɏg�p�����ہA
     * properties����ǂݏo����邽�߁A�X�V�����l�����f����܂��B
     * 
     * @param key �X�V���f�Ώۂ̒l�̃L�[
     */
    public void updateBooleanValue(final String key)
    {
        if (key == null)
        {
            return;
        }
        this.booleanMap_.remove(key);
    }

    /**
     * �w�肵���L�[������Integer�̒l�̍X�V�𔽉f���܂��B<br />
     * 
     * ������Ƃ��āA�w�肵���L�[������Integer�̒l��intMap_����폜���܂��B
     * �폜���邱�Ƃɂ���āA�w�肳�ꂽ�L�[�����l�����Ɏg�p�����ہA
     * properties����ǂݏo����邽�߁A�X�V�����l�����f����܂��B
     * 
     * @param key �X�V���f�Ώۂ̒l�̃L�[
     */
    public void updateIntValue(final String key)
    {
        if (key == null)
        {
            return;
        }
        this.intMap_.remove(key);
    }

    /**
     * �w�肵���L�[������Long�̒l�̍X�V�𔽉f���܂��B<br />
     * 
     * ������Ƃ��āA�w�肵���L�[������Long�̒l��longMap_����폜���܂��B
     * �폜���邱�Ƃɂ���āA�w�肳�ꂽ�L�[�����l�����Ɏg�p�����ہA
     * properties����ǂݏo����邽�߁A�X�V�����l�����f����܂��B
     * 
     * @param key �X�V���f�Ώۂ̒l�̃L�[
     */
    public void updateLongValue(final String key)
    {
        if (key == null)
        {
            return;
        }
        this.longMap_.remove(key);
    }
}
