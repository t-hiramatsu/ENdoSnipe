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
package jp.co.acroquest.endosnipe.javelin.event;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Javelin�̋��ʃC�x���g�N���X�B
 * 
 * @author eriguchi
 */
public class CommonEvent
{
    /** �C�x���g�̌x�����x��(INFO) */
    public static final int LEVEL_INFO = 20;

    /** �C�x���g�̌x�����x��(WARN) */
    public static final int LEVEL_WARN = 30;

    /** �C�x���g�̌x�����x��(ERROR) */
    public static final int LEVEL_ERROR = 40;

    /** �C�x���g���������B */
    protected long time_;

    /** �C�x���g���B */
    protected String name_;

    /** �p�����[�^�̃}�b�v�B�B */
    protected Map<String, String> paramMap_;

    /** �C�x���g�̃��x���B */
    protected int level_;

    /**
     *�@�C�x���g�o�͎����A�C�x���g���A�C�x���g���x���̃f�t�H���g�l��ݒ肵�܂��B<br />
     *
     */
    public CommonEvent()
    {
        this.time_ = System.currentTimeMillis();
        this.name_ = "unknown";
        this.paramMap_ = new LinkedHashMap<String, String>();
        this.level_ = LEVEL_WARN;
    }

    /**
     * �C�x���g�����������擾���܂��B<br />
     * 
     * @return �C�x���g��������
     */
    public long getTime()
    {
        return this.time_;
    }

    /**
     * �C�x���g����������ݒ肵�܂��B<br />
     * 
     * @param time �C�x���g��������
     */
    public void setTime(long time)
    {
        this.time_ = time;
    }

    /**
     * �C�x���g�����擾���܂��B<br />
     * 
     * @return �C�x���g��
     */
    public String getName()
    {
        return this.name_;
    }

    /**
     * �C�x���g����ݒ肵�܂��B<br />
     * 
     * @param name �C�x���g��
     */
    public void setName(String name)
    {
        this.name_ = name;
    }

    /**
     * �C�x���g�p�����[�^��ۑ�����}�b�v���擾���܂��B<br />
     * 
     * @return �C�x���g�p�����[�^��ۑ�����}�b�v
     */
    public Map<String, String> getParamMap()
    {
        return this.paramMap_;
    }

    /**
     * �C�x���g�p�����[�^��ݒ肵�܂��B<br />
     * 
     * @param key �L�[
     * @param value �l
     */
    public void addParam(String key, String value)
    {
        this.paramMap_.put(key, value);
    }

    /**
     * �C�x���g�p�����[�^�̃}�b�v����w�肵���L�[�ɑΉ�����l���擾���܂��B<br />
     * 
     * @param key �L�[
     * @return �w�肵���L�[�ɑΉ�����C�x���g�p�����[�^�̃}�b�v����擾�����l
     */
    public String getParam(String key)
    {
        return this.paramMap_.get(key);
    }

    /**
     * �C�x���g���x�����擾���܂��B<br />
     * 
     * @return �C�x���g���x��
     */
    public int getLevel()
    {
        return this.level_;
    }

    /**
     * �C�x���g���x����ݒ肵�܂��B<br />
     * 
     * @param level �C�x���g���x��
     */
    public void setLevel(int level)
    {
        this.level_ = level;
    }

}
