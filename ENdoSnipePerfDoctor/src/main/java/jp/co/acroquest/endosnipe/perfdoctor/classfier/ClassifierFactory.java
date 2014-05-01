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
package jp.co.acroquest.endosnipe.perfdoctor.classfier;

import java.util.List;

import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;

/**
 * �󂯎�������X�g�̃T�C�Y����ɁA�e���[���Ɋ�Â���WarningUnit�̕��ފ�𐶐�����B<br />
 * ���X�g�̃T�C�Y��10�ȉ��̂Ƃ��́ASimpleClassifier�𐶐����A<br />
 * ���X�g�̃T�C�Y��10�����傫���Ƃ��́AKmeansClassifier�𐶐�����B<br />
 * 
 * @author fujii
 * 
 */
public class ClassifierFactory
{
    /** ���ފ�̐��� */
    private static ClassifierFactory factory__        = new ClassifierFactory();

    /** �t�B���^��؂�ւ��� */
    private static final int         FILTER_THRESHOLD = 10;

    /**
     * ClassfierFactory�I�u�W�F�N�g��Ԃ��B
     * 
     * @return ClassfierFactory�I�u�W�F�N�g
     */
    public static ClassifierFactory getInstance()
    {
        return factory__;
    }

    /**
     * ���X�g�̑傫���ɂ���āA�قȂ镪�ފ���擾����B
     * @param list WariningUnit�̃��X�g
     * @return �t�B���^�[
     */
    public Classifier getClassifier(final List<WarningUnit> list)
    {
        if (list.size() <= FILTER_THRESHOLD)
        {
            return new SimpleClassifier();
        }
        return new KmeansClassifier();
    }
}
