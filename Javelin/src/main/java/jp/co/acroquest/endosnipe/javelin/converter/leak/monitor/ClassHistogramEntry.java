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
package jp.co.acroquest.endosnipe.javelin.converter.leak.monitor;

/**
 * �N���X�q�X�g�O�����̈�v�f�B
 * 
 * @author eriguchi
 */
public class ClassHistogramEntry
{
    /** �C���X�^���X���B */
    private int instances_;

    /** �T�C�Y(byte)�B */
    private int bytes_;

    /** �N���X���B */
    private String className_;

    /**
     * �C���X�^���X�����擾����B
     * 
     * @return �C���X�^���X���B
     */
    public int getInstances()
    {
        return instances_;
    }

    /**
     * �C���X�^���X����ݒ肷��B
     * 
     * @param instances �C���X�^���X���B
     */
    public void setInstances(final int instances)
    {
        this.instances_ = instances;
    }

    /**
     * �T�C�Y(byte)���擾����B
     * 
     * @return �T�C�Y(byte)�B
     */
    public int getBytes()
    {
        return bytes_;
    }

    /**
     * �T�C�Y(byte)��ݒ肷��B
     * 
     * @param bytes �T�C�Y(byte)�B
     */
    public void setBytes(final int bytes)
    {
        this.bytes_ = bytes;
    }

    /**
     * �N���X�����擾����B
     * 
     * @return �N���X���B
     */
    public String getClassName()
    {
        return className_;
    }

    /**
     * �N���X����ݒ肷��B
     * 
     * @param className �N���X���B
     */
    public void setClassName(final String className)
    {
        this.className_ = className;
    }
}
