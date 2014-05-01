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
package jp.co.acroquest.endosnipe.javelin.bean;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;

/**
 * �v���Ώۂ��珜�O����N���X�A���\�b�h���Ď�����N���X�ł��B<br />
 * @author fujii
 *
 */
public class ExcludeMonitor
{
    /** �v���Ώۏ��O���ǂ������肷�邽�߂̉񐔂�臒l */
    private static int autoExcludeThresholdCount__;

    /** �v���Ώۏ��O���ǂ������肷�邽�߂̎��Ԃ�臒l */
    private static int autoExcludeThresholdTime__;

    static
    {
        JavelinConfig config = new JavelinConfig();
        autoExcludeThresholdCount__ = config.getAutoExcludeThresholdCount();
        autoExcludeThresholdTime__  = config.getAutoExcludeThresholdTime();
    }

    /**
     * �C���X�^���X����h���A�v���C�x�[�g�R���X�g���N�^�ł��B<br />
     */
    private ExcludeMonitor()
    {
        // Do Nothing.
    }

    /**
     * �����ŗ^����ꂽ�N���X���A���\�b�h���������Ōv���Ώۂ��珜�O����Ă��邩���肵�܂��B<br />
     * 
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     * @return ���������ɂ��v���Ώۂ��珜�O����Ă���ꍇ�A<code>true</code>
     */
    public static boolean isExcludePreffered(final Invocation invocation)
    {
        if (invocation == null)
        {
            return false;
        }
        
        return invocation.isExcludePreffered();
    }

    /**
     * �����ŗ^����ꂽInvocation�Ŏ������Ăяo�����A
     * �����I�Ɏ��W�����v���Ώۏ��O���X�g�ɒǉ����܂��B<br />
     * 
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     */
    public static void addExcludePreffered(final Invocation invocation)
    {
        if (invocation == null)
        {
            return;
        }

        invocation.setExcludePreffered(true);
    }

    /**
     * �����ŗ^����ꂽInvocation�Ŏ������Ăяo�����A
     * �����I�Ɏ��W�����v���Ώۏ��O���X�g����폜���܂��B<br />
     * 
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     */
    public static void removeExcludePreferred(Invocation invocation)
    {
        if (invocation == null)
        {
            return;
        }

        invocation.setExcludePreffered(false);
    }

    /**
     * CallTreeMeasurement�����O�o�͑Ώۂ��珜�O���邩�ǂ������肵�܂��B<br />
     * 
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     */
    public static void judgeExclude(final Invocation invocation)
    {
        long totalTime = invocation.getTotal();
        if (autoExcludeThresholdTime__ > 0 
                && invocation.getCount() >= autoExcludeThresholdCount__
                && totalTime < autoExcludeThresholdTime__)
        {
            addExcludePreffered(invocation);
        }
    }

    /**
     * �����ŗ^����ꂽ�N���X���A���\�b�h���������I�Ɏ��W�����v���Ώۂł��邩�𔻒肵�܂��B<br />
     * 
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     * @return ���O�o�͂��珜�O����Ă���ꍇ�A<code>true</code>
     */
    public static boolean isTargetPreferred(final Invocation invocation)
    {
        if (invocation == null)
        {
            return false;
        }
        
        return invocation.isTargetPreferred();
    }

    /**
     * �����ŗ^����ꂽ�N���X���A���\�b�h���������I�Ɏ��W�����v���Ώۃ��X�g�ɒǉ����܂��B<br />
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     */
    public static void addTargetPreferred(final Invocation invocation)
    {
        if (invocation == null)
        {
            return;
        }

        invocation.setTargetPreferred(true);
    }

    /**
     * �����ŗ^����ꂽ�N���X���A���\�b�h���������I�Ɏ��W�����v���Ώۂ��珜�O���܂��B<br />
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     */
    public static void removeTargetPreferred(final Invocation invocation)
    {
        if (invocation == null)
        {
            return;
        }

        invocation.setTargetPreferred(false);
    }

    /**
     * �����ŗ^����ꂽ�N���X���A���\�b�h�����v���Ώۂ��珜�O���邩�ǂ����𔻒肵�܂��B<br />
     * 
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     * @return ���O�o�͂��珜�O����Ă���ꍇ�A<code>true</code>
     */
    public static boolean isExclude(final Invocation invocation)
    {
        if (invocation == null)
        {
            return false;
        }
        
        return invocation.isExclude();
    }

    /**
     * �����ŗ^����ꂽ�N���X���A���\�b�h�����v���Ώۏ��O���X�g�ɒǉ����܂��B<br />
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     */
    public static void addExclude(final Invocation invocation)
    {
        if (invocation == null)
        {
            return;
        }

        invocation.setExclude(true);
    }

    /**
     * �����ŗ^����ꂽ�N���X���A���\�b�h�����v���Ώۏ��O���X�g���珜�O���܂��B<br />
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     */
    public static void removeExclude(final Invocation invocation)
    {
        if (invocation == null)
        {
            return;
        }

        invocation.setExclude(false);
    }
    
    /**
     * �����ŗ^����ꂽInvocation�Ŏ������Ăяo�����A�v���Ώۂł��邩�𔻒肵�܂��B<br />
     * 
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     * @return ���O�o�͂��珜�O����Ă���ꍇ�A<code>true</code>
     */
    public static boolean isTarget(final Invocation invocation)
    {
        if (invocation == null)
        {
            return true;
        }

        return invocation.isTarget();
    }


    /**
     * �����ŗ^����ꂽ�N���X���A���\�b�h�����v���Ώۃ��X�g�ɒǉ����܂��B<br />
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     */
    public static void addTarget(final Invocation invocation)
    {
        if (invocation == null)
        {
            return;
        }

        invocation.setTarget(true);
    }

    /**
     * �����ŗ^����ꂽ�N���X���A���\�b�h�����v���Ώۂ��珜�O���܂��B<br />
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     */
    public static void removeTarget(final Invocation invocation)
    {
        if (invocation == null)
        {
            return;
        }
        
        invocation.setTarget(false);
    }

    /**
     * �����ŗ^����ꂽ�N���X�̃��\�b�h���v���Ώۂ��ۂ���Ԃ��܂��B<br />
     *
     * �v���ΏۂƎw�肳��Ă���A�܂���
     *
     * @param invocation {@link Invocation}�I�u�W�F�N�g
     * @return �v���Ώۂ̏ꍇ�� <code>true</code> �A�v���ΏۂłȂ��ꍇ�� <code>false</code>
     */
    public static boolean isMeasurementTarget(final Invocation invocation)
    {
        if(invocation == null)
        {
            return true;
        }
        
        boolean isTarget = invocation.isTarget();
        if (isTarget)
        {
            return true;
        }
        
        boolean isExclude = invocation.isExclude();
        if (isExclude)
        {
            return false;
        }
        
        boolean isTargetPreferred = invocation.isTargetPreferred();
        if (isTargetPreferred)
        {
            return true;
        }
        
        boolean isExcludePreferred = invocation.isExcludePreffered();
        return (isExcludePreferred == false);
    }
}
