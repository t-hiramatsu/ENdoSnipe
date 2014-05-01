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
package jp.co.acroquest.endosnipe.javelin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.javelin.bean.Component;

/**
 * �V���A���C�Y�������s���N���X�B<br />
 *
 * @author acroquest
 */
public class MBeanManagerSerializer
{

    /**
     * �R���X�g���N�^���B�����܂��B<br />
     */
    private MBeanManagerSerializer()
    {
        // Do nothing.
    }

    /**
     * �f�V���A���C�Y���s���܂��B<br />
     *
     * @return �f�V�A���C�Y�����R���|�[�l���g�̃}�b�v
     */
    public static Map<String, Component> deserialize()
    {
        JavelinConfig config = new JavelinConfig();
        if (!config.isSetSerializeFile())
        {
            return new HashMap<String, Component>();
        }

        String serializeFile = config.getSerializeFile();
        File file = new File(serializeFile);

        // �t�@�C�������݂��Ȃ��ꍇ�̓f�V���A���C�Y���X�L�b�v����B
        if (!file.exists())
        {
            return new HashMap<String, Component>();
        }

        return deserializeFile(serializeFile);
    }

    /**
     * �f�V���A���C�Y���s���܂��B<br />
     * @param serializeFile �V���A���C�Y�t�@�C��
     * @return �f�V�A���C�Y�����R���|�[�l���g�̃}�b�v
     */
    public static Map<String, Component> deserializeFile(String serializeFile)
    {
        ObjectInputStream inObject = null;
        try
        {
            FileInputStream inFile = new FileInputStream(serializeFile);
            inObject = new ObjectInputStream(inFile);
            Map<String, Component> map = castToMap(inObject.readObject());
            return map;
        }
        catch (Exception e)
        {
            SystemLogger.getInstance().warn("Failed to deserializing MBeanManager. "
                                                    + "Start without it. Deserializing source:"
                                                    + serializeFile, e);
        }
        finally
        {
            if (inObject != null)
            {
                try
                {
                    inObject.close();
                }
                catch (IOException ioe)
                {
                    SystemLogger.getInstance().warn(ioe);
                }
            }
        }

        return new HashMap<String, Component>();
    }

    /**
     * �V���A���C�Y���s���܂��B<br />
     *
     * @param map �V���A���C�Y���s���R���|�[�l���g�̃}�b�v
     */
    public static void serialize(final Map<String, Component> map)
    {
        JavelinConfig config = new JavelinConfig();
        if (config.isSetSerializeFile())
        {
            String serializeFile = config.getSerializeFile();

            try
            {
                FileOutputStream outFile = new FileOutputStream(serializeFile);
                ObjectOutputStream outObject = new ObjectOutputStream(outFile);
                outObject.writeObject(map);
                outObject.close();
                outFile.close();
            }
            catch (Exception e)
            {
                SystemLogger.getInstance().warn("Failed to serializing MBeanManager."
                                                        + "Serializing dest.:" + serializeFile, e);
            }
        }
    }

    /**
     * Map�ւ̃L���X�g���s���B
     * @param object �ΏۃI�u�W�F�N�g
     * @return Map�ɃL���X�g�����I�u�W�F�N�g
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Component> castToMap(final Object object)
    {
        return (Map<String, Component>)object;
    }
}
