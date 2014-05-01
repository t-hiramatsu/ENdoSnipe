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

import java.util.List;

/**
 * ���v�����pMBean�B<br>
 * S2JmxJavelin�Œ~�ς������ɑ΂��ē��v�������s�������ʂ�Ԃ��B<br>
 * ����A�ȉ��̏����擾���邱�Ƃ��\�B
 * <ol>
 * <li>���ϒl�Ń\�[�g�������\�b�h�R�[�����B</li>
 * <li>�ő�l�Ń\�[�g�������\�b�h�R�[�����B</li>
 * <li>�ŏ��l�Ń\�[�g�������\�b�h�R�[�����B</li>
 * <li>��O�̔����񐔂Ń\�[�g�������\�b�h�R�[�����B</li>
 * </ol>
 * 
 * @author yamasaki
 * 
 */
public interface StatisticsMBean
{
    /**
     * ���ϒl�Ń\�[�g�������\�b�h�R�[������Ԃ��܂��B
     * @return ���ϒl�Ń\�[�g�������\�b�h�R�[�����
     */
    List<InvocationMBean> getInvocationListOrderByAverage();

    /**
     * �ő�l�Ń\�[�g�������\�b�h�R�[������Ԃ��܂��B
     * @return �ő�l�Ń\�[�g�������\�b�h�R�[�����
     */
    List<InvocationMBean> getInvocationListOrderByMaximum();

    /**
     * �ŏ��l�Ń\�[�g�������\�b�h�R�[������Ԃ��܂��B
     * @return �ŏ��l�Ń\�[�g�������\�b�h�R�[�����
     */
    List<InvocationMBean> getInvocationListOrderByMinimum();

    /**
     * ��O�̔����񐔂Ń\�[�g�������\�b�h�R�[������Ԃ��܂��B
     * @return ��O�̔����񐔂Ń\�[�g�������\�b�h�R�[�����
     */
    List<InvocationMBean> getInvocationListOrderByThrowableCount();
}
