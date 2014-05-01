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
import java.net.URL;

/**
 * �p�X���������߂̃��[�e�B���e�B�N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class PathUtil
{
    /** Excel �œǂݍ��߂�t�@�C���p�X�̍ő咷 */
    public static final int MAX_PATH_LENGTH = 218;

    /** �t�@�C���p�X�̍ő咷�𒴂����Ƃ��ɁA�c���p�X�̒����i�g���q���܂ށj */
    public static final int CUT_PATH_LENGTH = 209;

    private PathUtil()
    {

    }

    /**
     * �N���X�p�X�z���ɂ��郊�\�[�X�̃p�X�𐶐����܂��B<br />
     * 
     * @param clazz ���\�[�X�Ɠ����p�b�P�[�W�ɑ��݂���N���X�� {@link Class} �I�u�W�F�N�g
     * @param path ���\�[�X��
     * @return ���\�[�X�p�X
     */
    public static String convertPath(final Class<?> clazz, final String path)
    {
        if (clazz == null || path == null)
        {
            return null;
        }

        String packageName = clazz.getPackage().getName() + '.';
        return packageName.replace('.', '/') + path;
    }

    /**
     * �w�肳�ꂽ�p�X�����΃p�X�ł��邩�ǂ����𒲂ׂ܂��B<br />
     * ���΃p�X�ł��邩�ǂ����́A�ȉ��̂悤�ɂ��Ē��ׂ܂��B<br />
     * �Ȃ��A�w�肳�ꂽ�p�X���� \ �L���� / �ɕϊ�������Œ��ׂ܂��B<br />
     * <ul>
     * <li>Windows���̏ꍇ
     *   <ul>
     *     <li>�ŏ���3������ [A-Za-z]:/ �Ɉ�v���Ă���ΐ�΃p�X
     *     <li>�ŏ���2������ // �Ɉ�v���Ă���ΐ�΃p�X(UNC�p�X�̏ꍇ)
     *     <li>����ȊO�͑��΃p�X
     *   </ul>
     * </li>
     * <li>Windows���ȊO�̏ꍇ
     *   <ul>
     *     <li>�ŏ���1������ / �ł���ΐ�΃p�X
     *     <li>�ŏ���1������ ~ �ł���ΐ�΃p�X
     *     <li>����ȊO�͑��΃p�X
     *   </ul>
     * </li>
     * </ul>
     * 
     * @param path �p�X
     * @return ���΃p�X�ł���ꍇ�� <code>true</code>
     */
    public static boolean isRelativePath(final String path)
    {
        if (path == null)
        {
            return false;
        }

        String normalizedPath = path.replace('\\', '/');
        if (OSUtil.isWindows())
        {
            // Windows ���̏ꍇ
            char drive = normalizedPath.charAt(0);
            if ((('A' <= drive && drive <= 'Z') || ('a' <= drive && drive <= 'z'))
                    && normalizedPath.charAt(1) == ':' && normalizedPath.charAt(2) == '/')
            {
                // �p�X�̐擪�Ƀh���C�u���^�[�����݂���ꍇ
                return false;
            }
            if (normalizedPath.startsWith("//"))
            {
                // UNC �p�X�̏ꍇ
                return false;
            }
            return true;
        }
        else
        {
            // Windows �ȊO�̏ꍇ
            if (normalizedPath.charAt(0) == '/')
            {
                // �p�X�� / ����n�܂�ꍇ
                return false;
            }
            if (normalizedPath.charAt(0) == '~')
            {
                // �p�X�� ~ ����n�܂�ꍇ
                return false;
            }
            return true;
        }
    }

    /**
     * �w�肳�ꂽ�N���X���܂܂�Ă��� Jar �t�@�C���̑��݂���f�B���N�g����Ԃ��܂��B<br />
     * �w��N���X�� Jar �t�@�C���Ɋ܂܂�Ă��Ȃ��ꍇ�A�󕶎����Ԃ��܂��B<br />
     * 
     * @param clazz �����ΏۃN���X�� {@link Class} �I�u�W�F�N�g
     * @return �f�B���N�g���̃p�X
     */
    public static String getJarDir(final Class<?> clazz)
    {
        if (clazz == null)
        {
            return null;
        }

        String classFileName = clazz.getName().replace('.', '/') + ".class";
        URL url = clazz.getClassLoader().getResource(classFileName);
        String urlPath = url.getPath();
        if ("jar".equals(url.getProtocol()) == true)
        {
            String jarPath = urlPath.substring("file:".length(), urlPath.indexOf('!'));
            return normalizeUrlPath(jarPath.substring(0, jarPath.lastIndexOf('/') + 1));
        }
        else
        {
            return "";
        }
    }

    /**
     * �p�X��������g�p����OS�ɍ��킹�Đ��K�����܂��B<br />
     * Windows �ł̓p�X�̍ŏ��� / �����邽�߁A���O���܂��B<br />
     * 
     * @param path �p�X������
     * @return ���K������
     */
    private static String normalizeUrlPath(final String path)
    {
        int startPos = 0;
        if (OSUtil.isWindows())
        {
            // Windows �ł̓p�X�̍ŏ��� / �����邽�߁A���O����
            startPos = 1;
        }
        return path.substring(startPos);
    }

    /**
     * �w�肳�ꂽ�t�@�C������L���ȃt�@�C�����ɂ���B<br />
     * "\", "/", ":" , ",", "*", "?", """, "<", ">", "|", "(", ")","\n" �� "_"�ɒu������B<br />
     * 
     * @param fileName �t�@�C����
     * @return �L���ȃt�@�C����
     */
    public static String getValidFileName(String fileName)
    {
        String removeRegex = "\\\\|/|:|,|\\*|\\?|\"|<|>|\\||\\(|\\)|\n";
        String replaceChar = "_";
        fileName = fileName.replaceAll(removeRegex, replaceChar);
        return fileName;
    }

    /**
     * �w�肳�ꂽ�t�@�C���̐�΃p�X�̒�����L���Ȓ����ɒ��߂��܂��B<br />
     *
     * �p�X�̒����� 218 �o�C�g�𒴂����ꍇ�́A
     * �p�X�̑O�� 209 �o�C�g�� "_" �ƃn�b�V���R�[�h��ǉ����܂��B<br />
     *
     * �t�@�C���������ɑS�p���������݂���ꍇ�́A�t�@�C�����������������N�����\��������܂��B
     *
     * @param absolutePath ��΃p�X
     * @return �������߂��s�����t�@�C����
     */
    public static String getValidLengthPath(final String absolutePath)
    {
        return getValidLengthPath(absolutePath, null);
    }

    /**
     * �w�肳�ꂽ�t�@�C���̐�΃p�X�̒�����L���Ȓ����ɒ��߂��܂��B<br />
     *
     * �p�X�̒����� 218 �o�C�g�𒴂����ꍇ�́A
     * �p�X�̑O�� 209 �o�C�g�� "_" �ƃn�b�V���R�[�h��ǉ����܂��B<br />
     *
     * �t�@�C���������ɑS�p���������݂���ꍇ�́A�t�@�C�����������������N�����\��������܂��B
     *
     * @param absolutePath ��΃p�X
     * @param addition �ǉ�������
     * @return �������߂��s�����t�@�C����
     */
    public static String getValidLengthPath(final String absolutePath, final String addition)
    {
        String fileName = new File(absolutePath).getName();
        String folderPath = new File(absolutePath).getParentFile().getAbsolutePath();

        String newFileName = "";
        int absolutePathLength = absolutePath.getBytes().length;
        if (absolutePathLength > MAX_PATH_LENGTH)
        {
            // �g���q���擾����
            int extensionPos = fileName.lastIndexOf('.');
            String extension = "";
            String fileNameWithoutExtension = fileName;
            if (extensionPos != -1)
            {
                extension = fileName.substring(extensionPos);
                fileNameWithoutExtension = fileName.substring(0, extensionPos);
            }

            // �t�@�C�����Ƃ��Ďc�������i�g���q�͏����j���v�Z����
            int folderLength = absolutePathLength - fileNameWithoutExtension.length();
            int remainLength = CUT_PATH_LENGTH - folderLength;

            if (remainLength > 0)
            {
                newFileName = fileName.substring(0, remainLength) + "_";
            }

            if (addition == null)
            {
                String hashCodeString = Integer.toHexString(absolutePath.hashCode());
                newFileName = newFileName + hashCodeString + extension;
            }
            else
            {
                newFileName = newFileName + addition + extension;
            }
        }
        else
        {
            newFileName = fileName;
        }
        return folderPath + File.separator + newFileName;
    }
}
