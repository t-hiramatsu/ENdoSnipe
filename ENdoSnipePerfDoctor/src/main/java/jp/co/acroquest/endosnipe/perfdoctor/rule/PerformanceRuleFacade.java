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
package jp.co.acroquest.endosnipe.perfdoctor.rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRule;
import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;

/**
 * PerformanceRule�̃t�@�T�[�h�N���X�B<br>
 * �uERROR�v�uWARN�v�uINFO�v��PerformanceRule��ێ����A���Ɏ��s����B
 * @author tanimoto
 *
 */
public class PerformanceRuleFacade implements PerformanceRule
{
    private PerformanceRule errorRule_;

    private PerformanceRule warnRule_;

    private PerformanceRule infoRule_;

    private String          id_;

    private String          level_;

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        this.infoRule_.init();
        this.warnRule_.init();
        this.errorRule_.init();
    }

    /**
     * {@inheritDoc}<br>
     * �uINFO�v�uWARN�v�uERROR�v�̏��Ŕ�����s���A
     * �����JavelinLogElement�ɂ��āA��ł�荂�����x���̌����ł�臒l�𒴂����ꍇ�ɂ�
     * ���ʂ̓��x���̍������̂ɏ㏑�������B<br>
     * ���Ƃ��Γ����JavelinLogElement��INFO�AWARN�Ŗ������o�����ꍇ�́A
     * �ŏ��ɋL�^����INFO�̌��ʂ�WARN�ɂ��㏑�������B
     */
    public List<WarningUnit> judge(final List<JavelinLogElement> javelinLogElementList)
    {
        PerformanceRule[] rules = {this.infoRule_, this.warnRule_, this.errorRule_};

        // �e���x���̐f�f���ʂ�\�����X�g���i�[���郊�X�g�B
        List<List<WarningUnit>> resultList = new ArrayList<List<WarningUnit>>();

        // �e���x���̐f�f���ʂ𓾂�B
        for (PerformanceRule rule : rules)
        {
            if (rule == null)
            {
                continue;
            }

            List<WarningUnit> result = rule.judge(javelinLogElementList);

            if (result != null && result.isEmpty() == false)
            {
                resultList.add(result);
            }
        }

        if (resultList.isEmpty() == true)
        {
            return new ArrayList<WarningUnit>();
        }

        // ���ʂ��܂Ƃ߂�Map�B
        // �l��WarningUnit�A�L�[�́u�t�@�C�����v�Ɓu��������JavelinLogElement�̊J�n�s�v��A������������B
        Map<String, WarningUnit> totalMap = new HashMap<String, WarningUnit>();

        // �Ⴂ���x���̌��ʂ��珇�ɋL�^����B
        for (List<WarningUnit> result : resultList)
        {
            for (WarningUnit unit : result)
            {
                // �ォ�瓯���s�ł�荂���x���̌x��������������㏑�������B
                String unitID = unit.getUnitId();
                totalMap.put(unitID, unit);
            }
        }

        Collection<WarningUnit> value = totalMap.values();
        return new ArrayList<WarningUnit>(value);
    }

    /**
     * {@inheritDoc}
     */
    public String getId()
    {
        return this.id_;
    }

    /**
     * {@inheritDoc}
     */
    public String getLevel()
    {
        return this.level_;
    }

    /**
     * errorRule��ݒ肷��B
     * @param errorRule errorRule
     */
    public void setErrorRule(final PerformanceRule errorRule)
    {
        this.errorRule_ = errorRule;
    }

    /**
     * infoRule��ݒ肷��B
     * @param infoRule infoRule
     */
    public void setInfoRule(final PerformanceRule infoRule)
    {
        this.infoRule_ = infoRule;
    }

    /**
     * warnRule��ݒ肷��B
     * @param warnRule warnRule
     */
    public void setWarnRule(final PerformanceRule warnRule)
    {
        this.warnRule_ = warnRule;
    }
}
