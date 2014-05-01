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

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.parser.JavelinConstants;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogColumnNum;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogConstants;
import jp.co.acroquest.endosnipe.javelin.JavelinLogUtil;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;

/**
 * �p�t�H�[�}���X�h�N�^�[�Ŏg�p���郆�[�e�B���e�B
 * 
 * @author eriguchi
 * @author tanimoto
 * 
 */
public class WarningUnitUtil
{
    private static final ENdoSnipeLogger LOGGER              =
                                                               ENdoSnipeLogger.getLogger(WarningUnitUtil.class);

    // ���\�[�X�o���h����
    private static final String          PERFDOCTOR_MESSAGES = "PerfDoctorMessages";

    // ���\�[�X�o���h�����烁�b�Z�[�W���擾����ۂ�
    // ���[��ID�̌��ɂ���suffix
    private static final String          MESSAGE_SUFFIX      = "_message";

    /** ���t�̃t�H�[�}�b�g */
    private static final String          DATE_FORMAT         = "yyyy/M/d HH:mm:ss.SSS";

    /**
     * �C���X�^���X����h�~���邽�߂̃v���C�x�[�g�R���X�g���N�^�ł��B<br />
     * 
     */
    private WarningUnitUtil()
    {
        // Do Nothing.
    }

    /**
     * �x���𐶐����܂��B<br />
     * WarningUnit�̐����ɂ͕K�����̃��\�b�h�𗘗p���Ă��������B<br />
     * 
     * @param unitId
     *            �x����ID
     * @param rule
     *            ���胋�[��
     * @param javelinLogElement
     *            {@link JavelinLogElement}�I�u�W�F�N�g
     * @param isDescend
     *            �t�B���^���ɍ~���ɕ��ׂ邩�ǂ�����\���t���O
     * @param args
     *            ���b�Z�[�W�̈���
     * @return �x���B
     */
    public static WarningUnit createWarningUnit(final String unitId, final PerformanceRule rule,
            final JavelinLogElement javelinLogElement, final boolean isDescend, final Object[] args)
    {
        return createWarningUnit(false, "", unitId, rule, javelinLogElement, isDescend, args);
    }

    /**
     * �x���𐶐����܂��B<br />
     * WarningUnit�̐����ɂ͕K�����̃��\�b�h�𗘗p���Ă��������B<br />
     * 
     * @param isEvent
     *            �C�x���g�ɂ��WarningUnit���ǂ���
     * @param stackTrace
     *            �X�^�b�N�g���[�X
     * @param unitId
     *            �x����ID
     * @param rule
     *            ���胋�[��
     * @param javelinLogElement
     *            {@link JavelinLogElement}�I�u�W�F�N�g
     * @param isDescend
     *            �t�B���^���ɍ~���ɕ��ׂ邩�ǂ�����\���t���O
     * @param args
     *            ���b�Z�[�W�̈���
     * @return �x���B
     */
    public static WarningUnit createWarningUnit(final boolean isEvent, final String stackTrace,
            final String unitId, final PerformanceRule rule,
            final JavelinLogElement javelinLogElement, final boolean isDescend, final Object[] args)
    {
        String id = rule.getId();
        String message = getMessage(id + MESSAGE_SUFFIX, args);
        String className = getClassName(javelinLogElement);
        String methodName = getMethodName(javelinLogElement);
        String level = rule.getLevel();
        String logFileName = javelinLogElement.getLogFileName();
        int startLogLine = javelinLogElement.getStartLogLine();

        // call�̃��O�̂�endTime���͂���A���̑��̃��O��endTime��startTime�ɂ��킹��B
        long startTime = calculateStartTime(javelinLogElement);
        long endTime = startTime;

        List<?> baseInfo = javelinLogElement.getBaseInfo();
        String baseInfoId = (String)baseInfo.get(JavelinLogColumnNum.ID);
        if (JavelinConstants.MSG_CALL.equals(baseInfoId) == true)
        {
            endTime = calculateEndTime(javelinLogElement, startTime);
        }

        return new WarningUnit(unitId, id, message, className, methodName, level, logFileName,
                               startLogLine, startTime, endTime, isDescend, isEvent, stackTrace,
                               args);
    }

    /**
     * �x�����o�͂��郍�O�̊J�n���Ԃ�long�l���v�Z����B<br>
     * "yyyy/mm/dd hh:mm:ss.SSS" �̌`���̎����f�[�^��long�l�ɕϊ����āA�������v�Z����B<br>
     * 
     * @param javelinLogElement
     *            JavelinLogElement
     * @return �����f�[�^��long�l
     */
    @SuppressWarnings("deprecation")
    private static long calculateStartTime(final JavelinLogElement javelinLogElement)
    {
        List<String> baseInfoList = javelinLogElement.getBaseInfo();
        if (baseInfoList == null || baseInfoList.size() <= JavelinLogColumnNum.CALL_TIME)
        {
            String text = Messages.getMessage("endosnipe.perfdoctor.rule.DateGetter.ErrorLabel");
            LOGGER.error(text);
            return 0;
        }

        String startTimeStr = baseInfoList.get(JavelinLogColumnNum.CALL_TIME);
        if (startTimeStr == null)
        {
            String text = Messages.getMessage("endosnipe.perfdoctor.rule.DateGetter.ErrorLabel");
            LOGGER.error(text);
            return 0;
        }
        // SimleDateFormat�𗘗p���A�������擾����B
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        formatter.setLenient(true);
        try
        {
            Date startDate = formatter.parse(startTimeStr);
            return startDate.getTime();
        }
        catch (ParseException ex1)
        {
            String text = Messages.getMessage("endosnipe.perfdoctor.rule.DateGetter.ErrorLabel");
            LOGGER.error(text);
        }
        return 0;
    }

    /**
     * �I���������v�Z����B<br>
     * �J�n����+duration�Ōv�Z���邪�A��O�����������ꍇ�́A�J�n������Ԃ��B
     * 
     * @param javelinLogElement
     *            JavelinLogElement
     * @param startTime
     *            �J�n����
     * @return
     */
    @SuppressWarnings("deprecation")
    private static long calculateEndTime(final JavelinLogElement javelinLogElement,
            final long startTime)
    {
        Map<String, String> extraInfoMap;
        extraInfoMap =
                       JavelinLogUtil.parseDetailInfo(javelinLogElement,
                                                      JavelinParser.TAG_TYPE_EXTRAINFO);

        // Duration��o�^����Map��������Ȃ��ꍇ�͊J�n������Ԃ��B
        if (extraInfoMap == null)
        {
            String text = Messages.getMessage("endosnipe.perfdoctor.rule.DateGetter.ErrorLabel");
            LOGGER.error(text);
            return startTime;
        }

        // Duration��������Ȃ��ꍇ�͊J�n������Ԃ��B
        String durationStr = extraInfoMap.get(JavelinLogConstants.EXTRAPARAM_DURATION);
        if (durationStr == null)
        {
            String text = Messages.getMessage("endosnipe.perfdoctor.rule.DateGetter.ErrorLabel");
            LOGGER.error(text);
            return startTime;
        }

        try
        {
            return startTime + Long.valueOf(durationStr);
        }
        catch (NumberFormatException ex)
        {
            String text = Messages.getMessage("endosnipe.perfdoctor.rule.DateGetter.ErrorLabel");
            LOGGER.error(text);
            return startTime;
        }
    }

    /**
     * JavelinLogElement����N���X�����擾����B
     * 
     * @param element
     *            {@link JavelinLogElement}�I�u�W�F�N�g
     * @return �N���X��
     */
    private static String getClassName(final JavelinLogElement element)
    {
        List<?> baseInfo = element.getBaseInfo();

        String id = (String)baseInfo.get(JavelinLogColumnNum.ID);
        if (JavelinConstants.MSG_CALL.equals(id) == true)
        {
            return (String)baseInfo.get(JavelinLogColumnNum.CALL_CALLEE_CLASS);
        }
        else if (JavelinConstants.MSG_RETURN.equals(id) == true)
        {
            return (String)baseInfo.get(JavelinLogColumnNum.RETURN_CALLEE_CLASS);
        }
        else if (JavelinConstants.MSG_THROW.equals(id) == true)
        {
            return (String)baseInfo.get(JavelinLogColumnNum.THROW_THROWER_CLASS);
        }
        else if (JavelinConstants.MSG_EVENT.equals(id) == true)
        {
            return (String)baseInfo.get(JavelinLogColumnNum.EVENT_CLASS);
        }

        return "unknown";
    }

    /**
     * JavelinLogElement���烁�\�b�h�����擾����B
     * 
     * @param element
     *            {@link JavelinLogElement}�I�u�W�F�N�g
     * @return ���\�b�h��
     */
    private static String getMethodName(final JavelinLogElement element)
    {
        List<?> baseInfo = element.getBaseInfo();

        String id = (String)baseInfo.get(JavelinLogColumnNum.ID);
        if (JavelinConstants.MSG_CALL.equals(id) == true)
        {
            return (String)baseInfo.get(JavelinLogColumnNum.CALL_CALLEE_METHOD);
        }
        else if (JavelinConstants.MSG_RETURN.equals(id) == true)
        {
            return (String)baseInfo.get(JavelinLogColumnNum.RETURN_CALLEE_METHOD);
        }
        else if (JavelinConstants.MSG_THROW.equals(id) == true)
        {
            return (String)baseInfo.get(JavelinLogColumnNum.THROW_THROWER_METHOD);
        }
        else if (JavelinConstants.MSG_EVENT.equals(id) == true)
        {
            return (String)baseInfo.get(JavelinLogColumnNum.EVENT_METHOD);
        }

        return "unknown";
    }

    /**
     * ���b�Z�[�W���擾����B
     * 
     * @param messageId
     *            ���b�Z�[�WID�B
     * @param args
     *            ���b�Z�[�W�̈����B
     * @return ���b�Z�[�W�B
     */
    @SuppressWarnings("deprecation")
    static String getMessage(final String messageId, final Object[] args)
    {
        ResourceBundle bundle = ResourceBundle.getBundle(PERFDOCTOR_MESSAGES);

        try
        {
            String pattern = bundle.getString(messageId);
            MessageFormat format = new MessageFormat(pattern);
            String message = format.format(args);

            return message;
        }
        catch (MissingResourceException mre)
        {
            LOGGER.error(mre.getMessage(), mre);
        }

        return "";
    }
}
