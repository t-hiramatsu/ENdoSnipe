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
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.perfdoctor.Messages;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRule;
import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;
import jp.co.acroquest.endosnipe.perfdoctor.WarningUnitUtil;

/**
 * PerformanceDoctor���[���̒��ۃN���X�B
 * ���[���J���҂́A���̃N���X���p�����ă��[�����쐬����B
 * 
 * @author tanimoto
 * 
 */
public abstract class AbstractRule implements PerformanceRule
{
    private static final ENdoSnipeLogger LOGGER             =
                                                              ENdoSnipeLogger.getLogger(AbstractRule.class);

    /** ���[��ID */
    public String                        id;

    /** ���[�����L�����ǂ��� */
    public boolean                       active;

    /** ���[���̖�背�x�� */
    public String                        level;

    /** ���[����L���ɂ���duration��臒l */
    public long                          durationThreshold;

    /** �G���[���N����JavelinLogElement�̃��X�g */
    private List<JavelinLogElement>      errorJavelinLogElementList_;

    /** �G���[�̈������X�g�B�v�f���͕K��javelinLogElementList_�ƈ�v����B */
    private List<Object[]>               argsList_;

    /** ���o�����x���̃��X�g�B */
    private List<WarningUnit>            warningList_;

    /** duration臒l�Ƃ��ė��p���镶����𒊏o����Strategy�B */
    private ThresholdStrategy            thresholdStrategy_ = new DefaultThresholdStrategy();

    /**
     * ���[���̔��菈�����s���B<br>
     * �������A���[���������ȏꍇ�A������null�ł���ꍇ�ɂ͔��肻�̂��̂��s�킸�A�v�f��0��List��Ԃ��B
     * �܂��Aduration��臒l��������Ă���JavelinLogElement�ɂ��Ă͔��菈�����s��Ȃ��B
     * 
     * @param javelinLogElementList JavelinLogElement�̃��X�g
     * @return �x�����j�b�g�̈ꗗ��\�����X�g
     */
    public List<WarningUnit> judge(final List<JavelinLogElement> javelinLogElementList)
    {
        if (this.active == false)
        {
            return new ArrayList<WarningUnit>(0);
        }

        List<JavelinLogElement> targetList = new ArrayList<JavelinLogElement>();
        for (JavelinLogElement javelinLogElement : javelinLogElementList)
        {

            String durationString =
                                    getThresholdStrategy().extractDurationThreshold(javelinLogElement);

            // TAT�̒l���擾�ł��Ȃ������ꍇ�́A0�Ƃ��Ĉ���
            if (durationString == null)
            {
                durationString = "0";
            }

            long duration = 0;
            try
            {
                duration = Long.parseLong(durationString);
            }
            //durationString��long�^�ɕϊ��ł��Ȃ������񂾂����ꍇ�͔�����s��Ȃ��B
            catch (NumberFormatException exception)
            {
                targetList.add(javelinLogElement);
                continue;
            }

            if (this.durationThreshold <= duration)
            {
                targetList.add(javelinLogElement);
            }
        }

        this.errorJavelinLogElementList_ = new ArrayList<JavelinLogElement>();
        this.argsList_ = new ArrayList<Object[]>();
        this.warningList_ = new ArrayList<WarningUnit>();

        doJudge(targetList);

        List<WarningUnit> warningList = createWarningUnitList();
        return warningList;
    }

    /**
     * �ǉ����ꂽ�G���[���A�x�����j�b�g�̈ꗗ��\�����X�g���쐬���܂��B<br />
     * 
     * @return �x�����j�b�g�ꗗ
     */
    protected List<WarningUnit> createWarningUnitList()
    {
        List<WarningUnit> warningList = new ArrayList<WarningUnit>(this.warningList_);
        return warningList;
    }

    /**
     * ���[���̐ݒ�l�ɑ΂��ď��������s���܂��B<br />
     *
     * �{���\�b�h�́A�t�B�[���h�̒l���Z�b�g���ꂽ��AdoJudge���Ă΂��O�ɌĂяo����܂��B<br />
     * ���[���N���X�����҂́A�K�v�ł���Ζ{���\�b�h���I�[�o�[���C�h���Ă��������B<br />
     */
    public void init()
    {
        // Do Nothing.
    }

    /**
     * ���[���̔��菈�����s���܂��B<br />
     * 
     * @param javelinLogElementList JavelinLogElement�̃��X�g
     */
    public abstract void doJudge(List<JavelinLogElement> javelinLogElementList);

    /**
     * �G���[��ǉ����܂��B<br />
     * ���̃��\�b�h�̓C�x���g�ȊO�̌x���𔭐������邽�߂ɗ��p���郁�\�b�h�ł��B<br />
     * �C�x���g�ɂ��x���𔭐�������ꍇ�ɂ͗��p���Ȃ��ł��������B<br />
     * 
     * @param element {@link JavelinLogElement}�I�u�W�F�N�g
     * @param args ���b�Z�[�W�̈���
     */
    protected synchronized void addError(final JavelinLogElement element, final Object... args)
    {
        addError(element, true, args);
    }

    /**
     * �G���[��ǉ����܂��B<br />
     * 
     * @param element {@link JavelinLogElement}�I�u�W�F�N�g
     * @param isDescend �t�B���^���ɍ~���ɕ��ׂ邩�ǂ�����\���t���O
     * @param args ���b�Z�[�W�̈���
     */
    protected synchronized void addError(final JavelinLogElement element, final boolean isDescend,
            final Object... args)
    {
        String unitId = element.getLogFileName() + "#" + element.getStartLogLine();
        addError(unitId, element, isDescend, args);
    }

    /**
     * �G���[��ǉ����܂��B<br />
     * 
     * @param unitId �x����ID
     * @param element {@link JavelinLogElement}�I�u�W�F�N�g
     * @param args ���b�Z�[�W�̈���
     */
    protected synchronized void addError(final String unitId, final JavelinLogElement element,
            final Object... args)
    {
        addError(unitId, element, true, args);
    }

    /**
     * �G���[��ǉ����܂��B<br />
     * 
     * @param unitId �x����ID
     * @param element {@link JavelinLogElement}�I�u�W�F�N�g
     * @param isDescend �t�B���^���ɍ~���ɕ��ׂ邩�ǂ�����\���t���O
     * @param args ���b�Z�[�W�̈���
     */
    protected synchronized void addError(final String unitId, final JavelinLogElement element,
            final boolean isDescend, final Object[] args)
    {
        this.errorJavelinLogElementList_.add(element);
        this.argsList_.add(args);

        WarningUnit unit =
                           WarningUnitUtil.createWarningUnit(unitId, this, element, isDescend, args);
        this.warningList_.add(unit);
    }

    /**
     * �G���[��ǉ����܂��B<br />
     * �C�x���g�ɂ��x���𔭐�������ꍇ�ɂ́A���̃��\�b�h�𗘗p���Ă��������B 
     * 
     * @param isEvent �C�x���g�ɂ��x���ł��邩�ǂ����B
     * @param stackTrace �X�^�b�N�g���[�X
     * @param element {@link JavelinLogElement}�I�u�W�F�N�g
     * @param isDescend �t�B���^���ɍ~���ɕ��ׂ邩�ǂ�����\���t���O
     * @param args ���b�Z�[�W�̈���
     */
    protected synchronized void addError(final boolean isEvent, final String stackTrace,
            final JavelinLogElement element, final boolean isDescend, final Object... args)
    {
        String unitId = element.getLogFileName() + "#" + element.getStartLogLine();
        addError(isEvent, stackTrace, unitId, element, isDescend, args);
    }

    /**
     * �G���[��ǉ����܂��B<br />
     * �C�x���g�ɂ��x���𔭐�������ꍇ�ɂ́A���̃��\�b�h�𗘗p���Ă��������B 
     * 
     * @param isEvent �C�x���g�ɂ��x���ł��邩�ǂ����B
     * @param stackTrace �X�^�b�N�g���[�X
     * @param element {@link JavelinLogElement}�I�u�W�F�N�g
     * @param args ���b�Z�[�W�̈���
     */
    protected synchronized void addError(final boolean isEvent, final String stackTrace,
            final JavelinLogElement element, final Object... args)
    {
        addError(isEvent, stackTrace, element, true, args);
    }

    /**
     * �G���[��ǉ����܂��B<br />
     * �C�x���g�ɂ��x���𔭐�������ꍇ�ɂ́A���̃��\�b�h�𗘗p���Ă��������B 
     * 
     * @param isEvent �C�x���g�ɂ��x���ł��邩�ǂ����B
     * @param stackTrace �X�^�b�N�g���[�X
     * @param unitId �x����ID
     * @param element {@link JavelinLogElement}�I�u�W�F�N�g
     * @param isDescend �t�B���^���ɍ~���ɕ��ׂ邩�ǂ�����\���t���O
     * @param args ���b�Z�[�W�̈���
     */
    protected synchronized void addError(final boolean isEvent, final String stackTrace,
            final String unitId, final JavelinLogElement element, final boolean isDescend,
            final Object[] args)
    {
        this.errorJavelinLogElementList_.add(element);
        this.argsList_.add(args);

        WarningUnit unit =
                           WarningUnitUtil.createWarningUnit(isEvent, stackTrace, unitId, this,
                                                             element, isDescend, args);
        this.warningList_.add(unit);
    }

    /**
     * �G���[��ǉ����܂��B<br />
     * �C�x���g�ɂ��x���𔭐�������ꍇ�ɂ́A���̃��\�b�h�𗘗p���Ă��������B 
     *
     * @param warningUnitList �G���[�̃��X�g
     */
    protected synchronized void addError(final List<WarningUnit> warningUnitList)
    {
        this.warningList_.addAll(warningUnitList);
    }

    /**
     * �G���[��ǉ����܂��B<br />
     * 
     * @param messageId ���b�Z�[�WID
     * @param args ���b�Z�[�W�̈����B
     */
    protected synchronized void addValidationError(final String messageId, final Object... args)
    {
        // TODO: �����ǉ�
    }

    /**
     * ���[��ID���擾���܂��B<br />
     * 
     * @return ���[��ID
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * ���[���̖�背�x�����擾���܂��B<br />
     * 
     * @return ���[���̖�背�x��
     */
    public String getLevel()
    {
        return this.level;
    }

    /**
     * duration臒l�Ƃ��ė��p���镶����𒊏o����Strategy���擾���܂��B<br />
     * 
     * @return duration臒l�Ƃ��ė��p���镶����𒊏o����Strategy�B
     */
    public ThresholdStrategy getThresholdStrategy()
    {
        return this.thresholdStrategy_;
    }

    /**
     * duration臒l�Ƃ��ė��p���镶����𒊏o����Strategy��ݒ肷��B
     * 
     * @param thresholdStrategy duration臒l�Ƃ��ė��p���镶����𒊏o����Strategy�B
     */
    public void setThresholdStrategy(final ThresholdStrategy thresholdStrategy)
    {
        this.thresholdStrategy_ = thresholdStrategy;
    }

    /**
     * ���[���N���X�Ŕ���������O�������O�ɏo�͂���
     * @param message �o�͂��郁�b�Z�[�W
     * @param element ���b�Z�[�W�Ɋ֘A����JavelinLogElement
     * @param throwable �o�͂���Throwable
     */
    @SuppressWarnings("deprecation")
    protected void log(final String message, final JavelinLogElement element,
            final Throwable throwable)
    {
        String text = "";

        if (message == null)
        {
            text = Messages.getMessage("endosnipe.perfdoctor.rule.AbstractRule.RuleLabel", this.id);
        }
        else if (element == null)
        {
            text =
                   Messages.getMessage("endosnipe.perfdoctor.rule.AbstractRule.NoElementLabel",
                                       message, this.id);
        }
        else
        {
            text =
                   Messages.getMessage("endosnipe.perfdoctor.rule.AbstractRule.ElementLabels",
                                       message, this.id, element.getLogFileName(),
                                       element.getStartLogLine());
        }
        LOGGER.error(text);
    }

    /**
     * SQL���s��\�����ǂ����𒲂ׂ�B
     * 
     * @param className �N���X��
     * @return SQL���s��\���Ȃ� <code>true</code>
     */
    public boolean isSqlExec(final String className)
    {
        return className.startsWith("jdbc:");
    }
}
