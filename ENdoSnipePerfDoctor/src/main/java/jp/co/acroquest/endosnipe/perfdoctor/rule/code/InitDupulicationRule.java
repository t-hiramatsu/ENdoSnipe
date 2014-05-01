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
package jp.co.acroquest.endosnipe.perfdoctor.rule.code;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.parser.JavelinConstants;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogColumnNum;
import jp.co.acroquest.endosnipe.javelin.JavelinLogUtil;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinLogElement;
import jp.co.acroquest.endosnipe.javelin.parser.JavelinParser;
import jp.co.acroquest.endosnipe.perfdoctor.rule.SingleElementRule;

/**
 * �������𕡐���s���Ă��Ȃ����`�F�b�N���郋�[���ł��B<br />
 *
 * �ʏ�A�N���X���ƃ��\�b�h�����w�肵�Ȃ��ꍇ�́A
 * ���� {@link InitDupulicationRule} �ɓo�^����Ă��Ȃ��N���X�E���\�b�h��臒l������������̂��ׂĂ��A
 * IntervalError �Ƃ��ďo�͂��܂��B<br />
 *
 * @author fujii
 * @author sakamoto
 */
public class InitDupulicationRule extends SingleElementRule implements JavelinConstants
{
    /** ���K�[ */
    private static final ENdoSnipeLogger         LOGGER                =
                                                                         ENdoSnipeLogger.getLogger(InitDupulicationRule.class);

    private static final String                  ID_LEVEL_SEPARATOR    = ":";

    /** 臒l */
    public long                                  threshold;

    /** �N���X���i�J���}��؂�ŕ����w��\�^���\�b�h���ƑΉ�����j */
    public String                                classNameList;

    /** ���\�b�h���i�J���}��؂�ŕ����w��\�^�N���X���ƑΉ�����j */
    public String                                methodNameList;

    /** �uID + ":" + ���x���v���L�[�A���\�b�h�ꗗ��l�Ɏ��}�b�v */
    private static Map<String, ClassMethodPairs> classMethodPairsMap__ =
                                                                         new ConcurrentHashMap<String, ClassMethodPairs>();

    /**
     * ���̃I�u�W�F�N�g�ɃZ�b�g����Ă���N���X�E���\�b�h���}�b�v�ɓo�^���܂��B<br />
     *
     * �N���X�E���\�b�h���o�^����Ă��Ȃ� {@link InitDupulicationRule} �I�u�W�F�N�g�́A
     * �}�b�v�ɓo�^����Ă��Ȃ��N���X�E���\�b�h�� IntervalError �Ƃ��ďo�͂��܂��B<br />
     */
    @Override
    public void init()
    {
        if (this.classNameList != null && this.methodNameList != null)
        {
            String key = getId() + ID_LEVEL_SEPARATOR + getLevel();
            ClassMethodPairs pairs = new ClassMethodPairs(this.classNameList, this.methodNameList);
            classMethodPairsMap__.put(key, pairs);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void doJudgeElement(final JavelinLogElement element)
    {
        // ���ʎq��"Event"�łȂ��ꍇ�́A�������Ȃ��B
        String type = element.getBaseInfo().get(JavelinLogColumnNum.ID);
        boolean isEvent = MSG_EVENT.equals(type);

        if (isEvent == false)
        {
            return;
        }

        String eventName = element.getBaseInfo().get(JavelinLogColumnNum.EVENT_NAME);

        // �C�x���g���� "IntervalError" �̏ꍇ�A���o���s���B
        if (EventConstants.NAME_INTERVALERROR.equals(eventName) == false)
        {
            return;
        }

        Map<String, String> eventInfoMap =
                                           JavelinLogUtil.parseDetailInfo(element,
                                                                          JavelinParser.TAG_TYPE_EVENTINFO);
        String actual = eventInfoMap.get(EventConstants.PARAM_INTERVALERROR_ACTUAL_INTERVAL);

        // ���ۂɂ����������Ԃ�臒l�ȉ��̏ꍇ�́A�������I������B
        long actualTime = Long.MAX_VALUE;
        try
        {
            actualTime = Long.parseLong(actual);
        }
        catch (NumberFormatException ex)
        {
            LOGGER.warn(ex);
        }
        if (actualTime > this.threshold)
        {
            return;
        }

        classMethodMatching(element, eventInfoMap, actualTime);
    }

    /**
     * �N���X���ƃ��\�b�h�����}�b�`���O���AIntervalError ���o�͂��܂��B<br />
     *
     * @param element {@link JavelinLogElement} �I�u�W�F�N�g
     * @param eventInfoMap �C�x���g���
     * @param actualTime ���ۂɂ����������ԁi�~���b�j
     */
    private void classMethodMatching(final JavelinLogElement element,
            final Map<String, String> eventInfoMap, final long actualTime)
    {
        String eventClassName = eventInfoMap.get(EventConstants.PARAM_INTERVALERROR_CLASSNAME);
        String eventMethodName = eventInfoMap.get(EventConstants.PARAM_INTERVALERROR_METHODNAME);

        if (this.classNameList == null || this.methodNameList == null)
        {
            // ���[���ŃN���X�������\�b�h�����w�肳��Ă��Ȃ���΁A
            // �}�b�v�ɓo�^����Ă��Ȃ��N���X�E���\�b�h�̏ꍇ�� IntervalError ���o�͂���B
            for (Map.Entry<String, ClassMethodPairs> entry : classMethodPairsMap__.entrySet())
            {
                ClassMethodPairs pairs = entry.getValue();
                if (pairs.contains(eventClassName, eventMethodName))
                {
                    // �}�b�v�ɓo�^����Ă���ꍇ�́A�������Ȃ��B
                    return;
                }
            }
            // �}�b�v�ɓo�^����Ă��Ȃ������̂ŁAIntervalError ���o�͂���B
            String stackTrace = eventInfoMap.get(EventConstants.PARAM_INTERVALERROR_STACKTRACE);
            addError(true, stackTrace, element, false, new Object[]{this.threshold, actualTime,
                    eventClassName, eventMethodName});
            return;
        }

        String[] classArray = this.classNameList.split(",");
        String[] methodArray = this.methodNameList.split(",");
        int repeatTime = Math.min(classArray.length, methodArray.length);

        for (int num = 0; num < repeatTime; num++)
        {
            // �N���X���A���\�b�h�������X�g�ƈ�v�����Ƃ��̂ݏo�͂���B
            if (classArray[num].equals(eventClassName) && methodArray[num].equals(eventMethodName))
            {
                String stackTrace = eventInfoMap.get(EventConstants.PARAM_INTERVALERROR_STACKTRACE);
                addError(true, stackTrace, element, false, new Object[]{this.threshold, actualTime,
                        eventClassName, eventMethodName});
                return;
            }
        }
    }

    /**
     * ���\�b�h�̃��X�g��ێ�����N���X�B<br />
     *
     * �����ꂩ�� {@link InitDupulicationRule} �I�u�W�F�N�g�̃t�B�[���h�Ɏ����Ă���N���X�E���\�b�h�́A
     * ���̃N���X�ɓo�^����܂��B<br />
     *
     * @author Sakamoto
     */
    private static class ClassMethodPairs
    {
        private final Set<String>   classAndMethodSet_;

        private static final String SEPARATOR = "###";

        /**
         * �o�^���郁�\�b�h���w�肵�ăI�u�W�F�N�g�����������܂��B<br />
         *
         * @param classNameList �N���X�����J���}�ŋ�؂���������
         * @param methodNameList ���\�b�h�����J���}�ŋ�؂���������
         */
        public ClassMethodPairs(final String classNameList, final String methodNameList)
        {
            String[] classNameArray = classNameList.split(",");
            String[] methodNameArray = methodNameList.split(",");
            int count = Math.min(classNameArray.length, methodNameArray.length);
            this.classAndMethodSet_ = new HashSet<String>();
            for (int index = 0; index < count; index++)
            {
                String className = classNameArray[index];
                String methodName = methodNameArray[index];
                this.classAndMethodSet_.add(className + SEPARATOR + methodName);
            }
        }

        /**
         * �w�肳�ꂽ�N���X�̃��\�b�h���A���̃I�u�W�F�N�g�ɓo�^����Ă��邩�ǂ����𒲂ׂ܂��B<br />
         *
         * @param className �������郁�\�b�h�̃N���X��
         * @param methodName �������郁�\�b�h
         * @return �o�^����Ă���ꍇ�� <code>true</code> �A�o�^����Ă��Ȃ��ꍇ�� <code>false</code>
         */
        public boolean contains(final String className, final String methodName)
        {
            return this.classAndMethodSet_.contains(className + SEPARATOR + methodName);
        }
    }
}
