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
package jp.co.acroquest.endosnipe.javelin.bean.proc;

/**
 *�@/proc/meminfo�̓��e��ێ�����Bean
 * 
 * @author eriguchi
 */
public class MemInfo
{
    /** �V�X�e���S�̂̃������ő�l */
    private long memTotal_;
    
    /** �V�X�e���S�̂̋󂫃����� */
    private long memFree_;
    
    /** �V�X�e���S�̂̃o�b�t�@ */ 
    private long bufferes_;

    /** �V�X�e���S�̂̃L���b�V�� */ 
    private long cached_;
    
    /** �V�X�e���S�̂̃X���b�v�ő�� */ 
    private long swapTotal_;
    
    /** �V�X�e���S�̂̃X���b�v�󂫗e�� */ 
    private long swapFree_;
    
    /** �V�X�e���S�̂̉��z�������g�p�� */ 
    private long vmallocTotal_;
    
    /**
     * �V�X�e���S�̂̃������ő�l���擾���܂��B
     * @return �V�X�e���S�̂̃������ő�l
     */
    public long getMemTotal()
    {
        return memTotal_;
    }
    
    /**
     * �V�X�e���S�̂̃������ő�l��ݒ肵�܂��B
     * @param memTotal �V�X�e���S�̂̃������ő�l
     */
    public void setMemTotal(long memTotal)
    {
        memTotal_ = memTotal;
    }
    
    /**
     * �V�X�e���S�̂̋󂫃��������擾���܂��B
     * @return �V�X�e���S�̂̋󂫃�����
     */
    public long getMemFree()
    {
        return memFree_;
    }
    
    /** 
     * �V�X�e���S�̂̋󂫃�������ݒ肵�܂��B
     * @param memFree �V�X�e���S�̂̋󂫃�����
     */
    public void setMemFree(long memFree)
    {
        memFree_ = memFree;
    }
    
    /**
     * �V�X�e���S�̂̃o�b�t�@���擾���܂��B   
     * @return �V�X�e���S�̂̃o�b�t�@
     */
    public long getBufferes()
    {
        return bufferes_;
    }
    
    /**
     * �V�X�e���S�̂̃o�b�t�@��ݒ肵�܂��B
     * @param bufferes �V�X�e���S�̂̃o�b�t�@
     */
    public void setBufferes(long bufferes)
    {
        bufferes_ = bufferes;
    }
    
    /**
     * �V�X�e���S�̂̃L���b�V�����擾���܂��B
     * @return �V�X�e���S�̂̃L���b�V��
     */
    public long getCached()
    {
        return cached_;
    }
    
    /**
     * �V�X�e���S�̂̃L���b�V����ݒ肵�܂��B
     * @param cached �V�X�e���S�̂̃L���b�V��
     */
    public void setCached(long cached)
    {
        cached_ = cached;
    }
    
    /**
     * �V�X�e���S�̂̃X���b�v�ő�ʂ��擾���܂��B
     * @return �V�X�e���S�̂̃X���b�v�ő��
     */
    public long getSwapTotal()
    {
        return swapTotal_;
    }
    
    /**
     * �V�X�e���S�̂̃X���b�v�ő�ʂ�ݒ肵�܂��B
     * @param swapTotal �V�X�e���S�̂̃X���b�v�ő��
     */
    public void setSwapTotal(long swapTotal)
    {
        swapTotal_ = swapTotal;
    }
    
    /**
     * �V�X�e���S�̂̃X���b�v�󂫗e�ʂ��擾���܂��B
     * @return �V�X�e���S�̂̃X���b�v�󂫗e��
     */
    public long getSwapFree()
    {
        return swapFree_;
    }
    
    /**
     * �V�X�e���S�̂̃X���b�v�󂫗e�ʂ�ݒ肵�܂��B
     * @param swapFree �V�X�e���S�̂̃X���b�v�󂫗e��
     */
    public void setSwapFree(long swapFree)
    {
        swapFree_ = swapFree;
    }
    
    /**
     * �V�X�e���S�̂̉��z�������g�p�ʂ��擾���܂��B
     * @return �V�X�e���S�̂̉��z�������g�p��
     */
    public long getVmallocTotal()
    {
        return vmallocTotal_;
    }
    
    /**
     * �V�X�e���S�̂̉��z�������g�p�ʂ�ݒ肵�܂��B
     * @param vmallocTotal �V�X�e���S�̂̉��z�������g�p��
     */
    public void setVmallocTotal(long vmallocTotal)
    {
        vmallocTotal_ = vmallocTotal;
    }
    
}
