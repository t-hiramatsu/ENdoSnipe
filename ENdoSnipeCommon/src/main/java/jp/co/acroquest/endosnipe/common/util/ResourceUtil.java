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
package jp.co.acroquest.endosnipe.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * ���\�[�X���������߂̃��[�e�B���e�B�N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class ResourceUtil
{
    private ResourceUtil()
    {

    }

    /**
     * �w�肳�ꂽ�N���X�̃p�b�P�[�W��\�����\�[�X�p�X��Ԃ��܂��B<br />
     * 
     * @param clazz �p�b�P�[�W�̃��\�[�X�p�X���擾����N���X
     * @return ���\�[�X�p�X
     */
    public static String getPackagePath(final Class<?> clazz)
    {
        return "/" + clazz.getPackage().getName().replace('.', '/');
    }

    /**
     * �w�肳�ꂽ�N���X�Ɠ����p�b�P�[�W�ɂ��郊�\�[�X�̐�΃p�X��Ԃ��܂��B<br />
     * 
     * @param clazz �N���X
     * @param name ���\�[�X��
     * @return ��΃p�X
     */
    public static String getAbsolutePath(final Class<?> clazz, final String name)
    {
        String prefix = getPackagePath(clazz);
        return prefix + "/" + name;
    }

    /**
     * ���\�[�X�� {@link File} �I�u�W�F�N�g�Ƃ��Ď擾���܂��B<br />
     * 
     * @param clazz ���\�[�X�Ɠ����p�b�P�[�W�̃N���X�I�u�W�F�N�g
     * @param name ���\�[�X��
     * @return {@link File} �I�u�W�F�N�g
     */
    public static File getResourceAsFile(final Class<?> clazz, final String name)
    {
        String absolutePath = getAbsolutePath(clazz, name);
        try
        {
            URL url = clazz.getResource(absolutePath);
            if (url != null)
            {
                return new File(url.toURI());
            }
            else
            {
                return null;
            }
        }
        catch (URISyntaxException ex)
        {
            return null;
        }
    }

    /**
     * JAR �t�@�C���� MANIFEST.MF ����A�o�[�W�������擾���܂��B
     *
     * @param clazz JAR �t�@�C�����ɑ��݂���N���X
     * @return �o�[�W�����B�o�[�W�������擾�ł��Ȃ��ꍇ�� "(Unknown version)"
     */
    public static String getJarVersion(final Class<?> clazz)
    {
        // Javelin�̃o�[�W�������擾����
        String version = null;
        try
        {
            version = getVersionFromManifest(clazz);
        }
        catch (IOException ex)
        // CHECKSTYLE:OFF
        {
            // Do nothing.
        }
        // CHECKSTYLE:ON
        if (version != null)
        {
            version = "Ver." + version;
        }
        else
        {
            version = "(Unknown version)";
        }
        return version;
    }

    /**
     * JAR �t�@�C���� MANIFEST.MF ����A�o�[�W�������擾���܂��B
     *
     * @param clazz JAR �t�@�C�����ɑ��݂���N���X
     * @return �o�[�W����
     * @throws IOException MANIFEST.MF �̓ǂݍ��݂Ɏ��s�����ꍇ 
     */
    private static String getVersionFromManifest(final Class<?> clazz)
        throws IOException
    {
        // ���̃N���X��JAR�t�@�C���Ɋ܂܂����̂Ɖ��肵�AJAR�t�@�C���̃p�X���܂ނ��̃N���X�̐�΃p�X���擾����;
        URL classUrl = clazz.getResource(clazz.getSimpleName() + ".class");
        String fullPath = classUrl.toExternalForm();

        // jar�t�@�C�����̂��̃N���X�w����A�}�j�t�F�X�g�t�@�C���w��ɍ����ւ���
        String packagePath = clazz.getPackage().getName().replace('.', '/');
        String jar = fullPath.substring(0, fullPath.lastIndexOf(packagePath));
        URL manifestUrl = new URL(jar + "META-INF/MANIFEST.MF");

        // MANIFEST.MF���J���A "Version: " �̒l���擾����
        InputStream is = null;
        Manifest mf = null;
        try
        {
            is = manifestUrl.openStream();
            mf = new Manifest(is);
        }
        finally
        {
            is.close();
        }
        Attributes attributes = mf.getMainAttributes();
        String version = attributes.getValue("Version");

        return version;
    }
}
