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
package jp.co.acroquest.endosnipe.perfdoctor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogAccessor;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;
import jp.co.acroquest.endosnipe.javelin.parser.ParseException;
import jp.co.acroquest.endosnipe.perfdoctor.exception.RuleCreateException;
import jp.co.acroquest.endosnipe.perfdoctor.exception.RuleNotFoundException;
import jp.co.acroquest.endosnipe.perfdoctor.rule.RuleManager;

/**
 * �p�t�H�[�}���X�h�N�^�[
 * 
 * @author eriguchi
 */
public class PerfDoctor
{
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(PerfDoctor.class);

    /**
     * �p�t�H�[�}���X�̔�������{���郋�[���̃��X�g�B
     */
    private List<PerformanceRule>        ruleList_;

    /**
     * �f�t�H���g�R���X�g���N�^�B
     */
    public PerfDoctor()
    {
        // Do nothing.
    }

    /**
     * �I�u�W�F�N�g�����������܂��B<br />
     *
     * @throws RuleNotFoundException �t�@�C����������Ȃ��ꍇ
     * @throws RuleCreateException ���[���̓ǂݍ��݂Ɏ��s�����ꍇ
     */
    public void init()
        throws RuleNotFoundException,
            RuleCreateException
    {
        this.ruleList_ = RuleManager.getInstance().getActiveRules();
    }

    /**
     * ���[���̌���Ԃ��܂��B<br />
     *
     * @return ���[���̌�
     */
    public int getRuleCount()
    {
        if (this.ruleList_ == null)
        {
            return 0;
        }
        return this.ruleList_.size();
    }

    /**
     * �A�N�e�B�u�ȃ��[���Ƀf�t�H���g���[�����R�s�[���ăt�@�C�����쐬���A
     * �I�u�W�F�N�g�����������܂��B<br />
     *
     * @throws RuleNotFoundException �t�@�C����������Ȃ��ꍇ
     * @throws RuleCreateException ���[���̓ǂݍ��݂Ɏ��s�����ꍇ
     */
    public void copyDefaultToActiveRule()
        throws RuleNotFoundException,
            RuleCreateException
    {
        RuleManager manager = RuleManager.getInstance();
        manager.commit();
        init();
    }

    /**
     * �w�肵��Javelin���O�t�@�C����ǂݍ��݁A
     * ��͂��s���B
     * ��͂������ʁA�x���ƂȂ������̂�Ԃ��B
     * 
     * @param elementList ���O�t�@�C���̓��e
     * @return �x���ƂȂ������ʂ̗v�f�̃��X�g
     */
    @SuppressWarnings("deprecation")
    public List<WarningUnit> judgeJavelinLog(final List<JavelinLogElement> elementList)
    {
        List<WarningUnit> result = new ArrayList<WarningUnit>();

        if (elementList == null)
        {
            return result;
        }

        for (PerformanceRule rule : this.ruleList_)
        {
            try
            {
                List<WarningUnit> warningList = null;
                warningList = rule.judge(elementList);
                if (warningList != null)
                {
                    result.addAll(warningList);
                }
            }
            catch (RuntimeException ex)
            {
                LOGGER.error(Messages.getMessage(PerfConstants.PERF_DOCTOR_RUNTIME_EXCEPTION), ex);
            }
        }

        return result;
    }

    /**
     * ���O�t�@�C���� {@link JavelinLogElement} �̃��X�g�ɕϊ����܂��B<br />
     *
     * @param logAccessor Javelin ���O�f�[�^�擾�I�u�W�F�N�g
     * @return �ϊ��������X�g
     */
    public List<JavelinLogElement> parseJavelinLogFile(final JavelinLogAccessor logAccessor)
    {
        JavelinParser javelinParser = new JavelinParser(logAccessor);
        return parseJavelinLogFileInternal(javelinParser);
    }

    /**
     * ���O�t�@�C���� {@link JavelinLogElement} �̃��X�g�ɕϊ����܂��B<br />
     *
     * @param javelinParser Javelin ���O�p�[�T
     * @return �ϊ��������X�g
     */
    @SuppressWarnings("deprecation")
    private List<JavelinLogElement> parseJavelinLogFileInternal(final JavelinParser javelinParser)
    {
        List<JavelinLogElement> elementList = null;
        try
        {
            // �p�[�T������������B
            javelinParser.init();

            // ��v�f���擾���A�p�t�H�[�}���X�̃��[���ᔽ��
            // ���X�g�Ɋi�[����B
            elementList = new ArrayList<JavelinLogElement>();
            JavelinLogElement javelinLogElement;
            while ((javelinLogElement = javelinParser.nextElement()) != null)
            {
                elementList.add(javelinLogElement);
            }

            JavelinParser.initDetailInfo(elementList);
        }
        catch (ParseException pe)
        {
            LOGGER.error(pe.getMessage(), pe);
        }
        catch (IOException ex)
        {
            LOGGER.error(ex.getMessage(), ex);
        }
        finally
        {
            try
            {
                javelinParser.close();
            }
            catch (IOException ioe)
            {
                LOGGER.error(ioe.getMessage(), ioe);
            }
        }
        return elementList;
    }
}
