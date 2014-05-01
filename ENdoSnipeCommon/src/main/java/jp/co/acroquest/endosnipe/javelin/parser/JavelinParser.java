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
package jp.co.acroquest.endosnipe.javelin.parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.common.parser.JavelinConstants;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogAccessor;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogColumnNum;
import jp.co.acroquest.endosnipe.common.parser.JavelinLogConstants;
import jp.co.acroquest.endosnipe.common.util.CSVTokenizer;
import jp.co.acroquest.endosnipe.common.util.NormalDateFormatter;
import jp.co.acroquest.endosnipe.javelin.JavelinLogUtil;

/**
 * Javelin���O��JavelinLogElement�Ƀp�[�X����B JavelinConverter����؂�o���č쐬�����B
 * 
 * @author eriguchi
 */
public class JavelinParser
{
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(JavelinParser.class);

    /** �ڍ׃^�O�̐ړ��� */
    public static final String DETAIL_TAG_PREFIX = "<<javelin.";

    private static final int DETAIL_START_TAG_LENGTH = DETAIL_TAG_PREFIX.length();

    /** �ڍ׃^�O�̐ڔ��� */
    public static final String DETAIL_TAG_START_END_STR = "_START>>";

    /**
     * ��O�ł��邩�ǂ����������^�O�B
     */
    public static final String JAVELIN_EXCEPTION = "<<javelin.Exception>>";

    /** �ȉ��A���샍�O�̏ڍ׏��𕪗ނ��邽�߂̃^�O */
    public static final String TAG_TYPE_ARGS = "Args";

    /** ���샍�O�̏ڍ׏��𕪗ނ���^�O(JMXInfo) */
    public static final String TAG_TYPE_JMXINFO = "JMXInfo";

    /** ���샍�O�̏ڍ׏��𕪗ނ���^�O(ExtraInfo) */
    public static final String TAG_TYPE_EXTRAINFO = "ExtraInfo";

    /** ���샍�O�̏ڍ׏��𕪗ނ���^�O(EventInfo) */
    public static final String TAG_TYPE_EVENTINFO = "EventInfo";

    /** ���샍�O�̏ڍ׏��𕪗ނ���^�O(StackTrace) */
    public static final String TAG_TYPE_STACKTRACE = "StackTrace";

    /** ���샍�O�̏ڍ׏��𕪗ނ���^�O(ReturnValue) */
    public static final String TAG_TYPE_RETURN_VAL = "ReturnValue";

    /** ���샍�O�̏ڍ׏��𕪗ނ���^�O(FieldValue) */
    public static final String TAG_TYPE_FIELD_VAL = "FieldValue";

    /** �I�u�W�F�N�g�P�ʂ̃V�[�P���X�ɂ��邩���w�肷��v���p�e�B�̖��O */
    public static final String PROP_JAVELINCONV_OBJECT = "javelinConv.object";

    /** PROP_JAVELINCONV_OBJECT�v���p�e�B�̃f�t�H���g�l */
    public static final String PROP_JAVELINCONV_OBJECT_DEFAULT = "false";

    private static final String MESSAGE_FORMAT_ERROR =
            Messages.getString("0000_actionLogError_actionLogFileFormatError"); //$NON-NLS-1$

    /**
     * �G���[�o�͗p�X�g���[���B
     */
    private final PrintStream errorStream_ = System.err;

    /** logFileName_�̖��O�����t�@�C�� */
    private File logFile_;

    /** ���O�t�@�C���̖��O�B�N�����A�����ɓn���ꂽ�܂܂̕�����ł��� */
    private final String logFileName_;

    /**
     * Javelin ���O�擾�I�u�W�F�N�g�B<br />
     */
    private final JavelinLogAccessor logAccessor_;

    /** logFile_���A�s�����J�E���g���Ȃ���ǂݍ��ނ��߂̃��[�_�[ */
    private LineNumberReader logBufferedReader_;

    /** ��ǂ݂������O�v�f�̊�{��� */
    private String nextBaseInfo_ = "";

    /** ���ݓǂݍ��ݒ��̍s�ԍ� */
    private int logFileLine_;

    /**
     * �ǂݍ��݃t�@�C�����t�@�C���̏I�[�ɓ��B�������ǂ����B
     */
    private boolean isEOF_ = false;

    /**
     * �t�@�C�������w�肵�ăp�[�T���쐬���܂��B<br />
     * 
     * @param fileName
     *            �p�[�X�Ώۂ̃t�@�C����
     */
    public JavelinParser(final String fileName)
    {
        this.logFileName_ = fileName;
        this.logAccessor_ = null;
    }

    /**
     * �p�[�T���쐬���܂��B<br />
     * 
     * @param logAccessor
     *            Javelin ���O�擾�I�u�W�F�N�g
     */
    public JavelinParser(final JavelinLogAccessor logAccessor)
    {
        this.logFileName_ = logAccessor.getFileName();
        this.logAccessor_ = logAccessor;
    }

    /**
     * ���������܂��B<br />
     * 
     * �p�[�X�Ώۂ̃t�@�C�����J���A�s�ԍ������������܂��B
     * 
     * @throws ParseException
     *             �t�@�C�������݂��Ȃ��ꍇ�A�f�B���N�g���̏ꍇ�A �ǂݍ��݌������Ȃ��ꍇ
     */
    public void init()
        throws ParseException
    {
        // �t�@�C���̊J�n�s�ԍ���1�ɏ���������B
        this.logFileLine_ = 1;

        Reader logReader = null;
        try
        {
            if (this.logAccessor_ != null)
            {
                // //////// �p�[�X�Ώۂ����O�擾�I�u�W�F�N�g�ɂ���ꍇ //////////
                InputStream input = this.logAccessor_.getInputStream();
                logReader = new InputStreamReader(input);
            }
            else
            {
                // //////// �p�[�X�Ώۂ��t�@�C���̏ꍇ //////////

                this.logFile_ = new File(this.logFileName_);

                // ���O�t�@�C�������݂��Ȃ��A�������̓t�@�C���łȂ��ꍇ�́A
                // �G���[���o�͂��Ď��s��Ԃ��B
                if (this.logFile_.exists() == false || this.logFile_.isFile() == false)
                {
                    String message =
                            MessageFormat.format(Messages.getString("0001_notExist"),
                                                 new Object[]{this.logFileName_});
                    this.printError(message);
                    throw new ParseException(message);
                }
                // �t�@�C���̓ǂݍ��݌������Ȃ��ꍇ�́A�G���[���o�͂��Ď��s��Ԃ��B
                else if (this.logFile_.canRead() == false)
                {
                    String message =
                            MessageFormat.format(Messages.getString("0002_unreadable"),
                                                 new Object[]{this.logFileName_});
                    this.printError(message);
                    throw new ParseException(message);
                }
                logReader = new FileReader(this.logFile_);
            }

            // ���샍�O�̓��e��ǂލ���Reader�𐶐�
            this.logBufferedReader_ = new LineNumberReader(logReader);
        }
        catch (IOException exp)
        {
            String message =
                    MessageFormat.format(Messages.getString("0002_unreadable"),
                                         new Object[]{this.logFileName_});
            this.printError(message);
            throw new ParseException(message, exp);
        }

    }

    /**
     * �p�[�X�Ώۂ̃t�@�C�����N���[�Y����B �I�����ɂ͕K�����̃��\�b�h���ĂԂ��ƁB
     * 
     * @throws IOException
     *             �p�[�X�Ώۂ̃t�@�C���̃N���[�Y�Ɏ��s�����ꍇ�B
     */
    public void close()
        throws IOException
    {
        if (this.logBufferedReader_ != null)
        {
            this.logBufferedReader_.close();
        }

    }

    /**
     * �G���[���b�Z�[�W��\������B
     * 
     * @param message
     *            �G���[���b�Z�[�W
     */
    private void printError(final String message)
    {
        this.errorStream_.println(message);
    }

    /**
     * ���샍�O�����v�f���ɑΉ�����L�q���擾����B �I���ʒu�܂œǂݍ��ݏI����Ă���ꍇ�́Anull��Ԃ��B
     * 
     * @return ���샍�O�ɂ������v�f���̃��O������
     * @throws IOException
     *             ���o�͗�O������
     * @throws ParseException
     *             �p�[�X�G���[������
     */
    public JavelinLogElement nextElement()
        throws IOException,
            ParseException
    {
        // �t�@�C���̊J�n�s���L�^����
        int startLogLine = this.logFileLine_;

        // �I���ʒu�܂œǂݍ��ݏI����Ă���ꍇ�́Anull��Ԃ��B
        if (this.nextBaseInfo_ == null)
        {
            return null;
        }

        // Javelin���샍�O�̗v�f�𐶐�
        JavelinLogElement javelinLogElement = new JavelinLogElement();

        // ���O�t�@�C�����̐ݒ�
        javelinLogElement.setLogFileName(this.logFileName_);

        // ��{���̎��o��
        List<String> baseInfoList = this.getBaseInfoList();

        if (baseInfoList == null)
        {
            return null;
        }

        // ��{���̃Z�b�g
        javelinLogElement.setBaseInfo(baseInfoList);

        // �ڍ׏��̎��o��
        boolean hasDetailInfo = this.getDetailInfo(javelinLogElement);
        while (hasDetailInfo == true)
        {
            // �ڍ׏��̎��o��
            hasDetailInfo = this.getDetailInfo(javelinLogElement);
        }

        // ��s��ǂݔ�΂��B���̂Ƃ��A��s���ڍ׏��Ɋ܂ށB
        while ("".equals(this.nextBaseInfo_))
        {
            this.nextBaseInfo_ = this.logBufferedReader_.readLine();
        }

        this.logFileLine_ = this.logBufferedReader_.getLineNumber();

        // �t�@�C���̏I���s���L�^����
        int endLogLine = this.logFileLine_ - 1;
        if (this.isEOF_)
        {
            endLogLine++;
        }

        // �J�n�A�I���̍s�ԍ���ݒ肷��B
        javelinLogElement.setStartLogLine(startLogLine);
        javelinLogElement.setEndLogLine(endLogLine);

        return javelinLogElement;
    }

    /**
     * ��{���̍s����CSV�Ő؂蕪�������X�g��Ԃ�
     * 
     * @return ��{���̃��X�g
     * @throws IOException
     *             �t�@�C���̓ǂݍ��݂Ɏ��s�����ꍇ
     */
    public List<String> getBaseInfoList()
        throws IOException
    {
        String baseInfoString;

        // �t�B�[���h�Ɋ�{��񂪕ێ�����Ă���ꍇ
        if (this.nextBaseInfo_.length() > 0)
        {
            // �t�B�[���h�����{���̎��o��
            baseInfoString = this.nextBaseInfo_;
            this.nextBaseInfo_ = null;
        }
        else
        {
            // ���샍�O�����{���̎��o��
            String line = this.logBufferedReader_.readLine();
            if (line == null)
            {
                return null;
            }
            baseInfoString = line;
        }

        // CSV�ɂ���{���̐؂蕪��
        CSVTokenizer csvTokenizer = new CSVTokenizer(baseInfoString);

        // �S�Ă̊�{���ɂ��āACSV�Ő؂�o���A
        // ��{���̃��X�g�ɒǉ�
        List<String> baseInfoList = new ArrayList<String>();
        boolean hasMoreBaseInfo = csvTokenizer.hasMoreTokens();
        while (hasMoreBaseInfo == true)
        {
            String baseInfo = csvTokenizer.nextToken();
            baseInfoList.add(baseInfo);

            hasMoreBaseInfo = csvTokenizer.hasMoreTokens();
        }

        return baseInfoList;
    }

    /**
     * �ڍ׏��̓ǂݍ��݂��s��
     * 
     * @param logElement
     *            Javelin���O�̗v�f
     * @return �ڍ׏��擾�̌��ʁB��ꂽ�Ƃ��́Atrue�B���Ȃ������Ƃ��́Afalse�B
     * @throws ParseException
     *             �ڍ׃��O�̃t�H�[�}�b�g���ُ�Ȃ��߂Ƀp�[�X�Ɏ��s�����ꍇ�B
     * @throws IOException
     *             �ڍ׃��O�̓ǂݍ��݂Ɏ��s�����ꍇ
     */
    public boolean getDetailInfo(final JavelinLogElement logElement)
        throws ParseException,
            IOException
    {
        boolean result = false;

        String nextInfoLine = this.logBufferedReader_.readLine();

        if (nextInfoLine == null)
        {
            this.isEOF_ = true;
        }
        else if (nextInfoLine != null && nextInfoLine.startsWith(DETAIL_TAG_PREFIX) == true)
        {
            result = true;

            // �ڍ׏�񂪗�O���Ӗ�����"javelin.Exception"�ł��邩���`�F�b�N
            boolean isJavelinExceptionTag = nextInfoLine.indexOf(JAVELIN_EXCEPTION) >= 0;
            if (isJavelinExceptionTag == true)
            {
                // �ڍ׏�� "javelin.Exception"���Z�b�g
                String detailTagType = JAVELIN_EXCEPTION;
                String detailTagData = JAVELIN_EXCEPTION;
                logElement.setDetailInfo(detailTagType, detailTagData);
                return true;
            }

            // �J�n�^�O�̖������������`�ɂȂ��Ă��邩�`�F�b�N
            // �������Ȃ��ꍇ�́A�G���[�o�͂��Ē��f�B
            boolean isRightEnd = nextInfoLine.endsWith(DETAIL_TAG_START_END_STR);
            if (isRightEnd == false)
            {
                // ���샍�O�̌��݂̍s�ԍ���ێ�����
                this.logFileLine_ = this.logBufferedReader_.getLineNumber();

                String message =
                        this.createLogParseErrorMsg(MESSAGE_FORMAT_ERROR,
                                                    Messages.getString("0000_actionLogError_beginningTabError"),
                                                    this.logFileLine_, null);
                this.printError(message);
                return false;
            }

            int endPos = nextInfoLine.length() - DETAIL_TAG_START_END_STR.length();
            // �ڍ׏��̃^�O�^�C�v���̎擾
            String detailTagType = nextInfoLine.substring(DETAIL_START_TAG_LENGTH, endPos);

            // �ڍ׏���ۑ�����StringBuffer
            StringBuffer detailInfoBuffer = new StringBuffer();

            // �ڍ׏��̒��g����s���ǂݍ��ށB
            // �t�@�C���̍Ō�܂œǂ�ł��I���^�O������Ȃ��ꍇ�́A�G���[�o�͂��Ē��f�B
            String detailInfoLine = this.logBufferedReader_.readLine();
            while (detailInfoLine != null && detailInfoLine.startsWith(DETAIL_TAG_PREFIX) == false)
            {
                detailInfoBuffer.append(detailInfoLine);
                detailInfoBuffer.append(System.getProperty("line.separator"));
                detailInfoLine = this.logBufferedReader_.readLine();
                if (detailInfoLine == null)
                {
                    String message =
                            this.createLogParseErrorMsg(MESSAGE_FORMAT_ERROR,
                                                        Messages.getString("0000_actionLogError_noFinalTab"),
                                                        this.logBufferedReader_.getLineNumber(),
                                                        null);
                    this.printError(message);
                    return false;
                }
            }

            logElement.setDetailInfo(detailTagType, detailInfoBuffer.toString());
        }
        else
        {
            // ��{��񂪘A�������ꍇ�ɒʂ鏈��
            // �ǂݍ��񂾎��̍s���A���̊�{����\���t�B�[���h�ɃZ�b�g
            this.nextBaseInfo_ = nextInfoLine;
        }
        return result;
    }

    /**
     * ���O�̃p�[�X�G���[���b�Z�[�W���쐬����B
     * 
     * @param message
     * @param cause
     * @param lineNum
     * @param log
     */
    private String createLogParseErrorMsg(String message, final String cause, final int lineNum,
            String log)
    {
        if (log == null)
        {
            log = "";
        }

        message =
                MessageFormat.format(Messages.getString("0000_actionLogError"), new Object[]{
                        message, cause, this.logFile_.getName(), lineNum, log});
        return message;
    }

    /**
     * Javelin���O�̏ڍ׏�������������B
     * 
     * ��̓I�ɂ́ACall���O�ɑ΂��āA���̃��\�b�h�̏����l �iElapsedTime��Pure CPU
     * Time�j���v�Z���AextraInfo�}�b�v�ɓo�^����B
     * 
     * @param logList
     *            Javelin���O
     */
    @SuppressWarnings("deprecation")
    public static void initDetailInfo(final List<JavelinLogElement> logList)
    {
        // �����l���v�Z����l�̃L�[��o�^����
        Map<String, String> pureKeyMap = register();
        // ���\�b�h�Ăяo���X�^�b�N
        Stack<MethodParam> methodCallStack = new Stack<MethodParam>();

        // ���ꂼ���Javelin Call���O�ɑ΂��āA�܂��̓��O�ɋL�q���ꂽ�l���}�b�v�ɓo�^���A
        // �}�b�v����q���\�b�h�̒l�������Ă���
        for (JavelinLogElement targetMethod : logList)
        {
            if (targetMethod == null)
            {
                continue;
            }
            // Call���O�ȊO�͖�������
            String id = targetMethod.getLogIDType();
            if (JavelinConstants.MSG_CALL.equals(id) == false)
            {
                continue;
            }

            MethodParam methodParam = new MethodParam();
            methodParam.setJavelinLogElement(targetMethod);

            // ���݃p�[�X���̃��\�b�h�̊J�n�������擾����
            List<String> baseInfo = targetMethod.getBaseInfo();
            if (baseInfo == null)
            {
                continue;
            }
            String callTimeString = baseInfo.get(JavelinLogColumnNum.CALL_TIME);
            if (callTimeString == null)
            {
                continue;
            }
            try
            {
                methodParam.setStartTime(NormalDateFormatter.parse(callTimeString).getTime());
            }
            catch (java.text.ParseException ex)
            {
                methodParam.setStartTime(0);
            }

            // ���݃p�[�X���̃��\�b�h��Duration Time���擾����
            Map<String, String> extraInfoMap =
                    JavelinLogUtil.parseDetailInfo(targetMethod, JavelinParser.TAG_TYPE_EXTRAINFO);
            String durationString = extraInfoMap.get(JavelinLogConstants.EXTRAPARAM_DURATION);
            if (durationString != null)
            {
                try
                {
                    methodParam.setDuration(Long.parseLong(durationString));
                }
                catch (NumberFormatException ex)
                {
                    LOGGER.error(ex.getMessage(), ex);
                    continue;
                }
            }
            else
            {
                methodParam.setDuration(0);
            }

            // �J�n������Duration Time����A���\�b�h�̏I���������v�Z����
            methodParam.setEndTime(methodParam.getStartTime() + methodParam.getDuration());
            methodParam.setOriginalDataMap(new HashMap<String, Double>());
            methodParam.setPureDataMap(new HashMap<String, Double>());

            putOriginalValue(pureKeyMap, targetMethod, methodParam);

            // ���\�b�h�Ăяo���X�^�b�N�Ɋi�[����Ă��郁�\�b�h�̒��ŁA
            // ���\�b�h�I�����������̃��\�b�h�J�n���������O�̂��̂̏����l���v�Z����
            calcPureValue(methodCallStack, methodParam);

            methodCallStack.push(methodParam);
        }

        // �Ō�܂Ŏc�������\�b�h�̏����l���v�Z����
        while (methodCallStack.size() > 0)
        {
            MethodParam methodParam = methodCallStack.pop();
            if (methodCallStack.size() > 0)
            {
                MethodParam parentMethodParam = methodCallStack.get(methodCallStack.size() - 1);
                parentMethodParam.subtractData(methodParam);
            }
            registerPureDataToJavelinLogElement(methodParam);
        }
    }

    /**
     * �����l�����߂邷�ׂĂ̍��ڂɑ΂��āA���ݒl�i���O�t�@�C���ɋL�q����Ă���l�j���}�b�v�ɓo�^����
     * @param pureKeyMap �����L�[�}�b�v
     * @param targetMethod �Ώۃ��\�b�h
     * @param methodParam ���\�b�h�p�����[�^
     */
    @SuppressWarnings("deprecation")
    private static void putOriginalValue(final Map<String, String> pureKeyMap,
            final JavelinLogElement targetMethod, final MethodParam methodParam)
    {
        // �����l�����߂邷�ׂĂ̍��ڂɑ΂��āA���ݒl�i���O�t�@�C���ɋL�q����Ă���l�j���}�b�v�ɓo�^����
        for (Map.Entry<String, String> entrySet : pureKeyMap.entrySet())
        {
            // �����l���v�Z���錳�ƂȂ�L�[�ƒl
            String detailInformationKey = entrySet.getKey();
            String originalString =
                    JavelinParser.getValueFromExtraInfoOrJmxInfo(targetMethod, detailInformationKey);
            double originalValue;
            if (originalString != null)
            {
                try
                {
                    originalValue = Double.parseDouble(originalString);
                }
                catch (NumberFormatException ex)
                {
                    LOGGER.error(ex.getMessage(), ex);
                    continue;
                }
            }
            else
            {
                originalValue = 0;
            }
            String javelinLogFileParam = entrySet.getValue();
            methodParam.getOriginalDataMap().put(javelinLogFileParam, originalValue);
            methodParam.getPureDataMap().put(javelinLogFileParam, originalValue);
        }
    }

    /**
     * �w�肳�ꂽ���\�b�h�̊J�n���������O�Ɏ��s���������Ă��郁�\�b�h�̏����l���v�Z���A ���̒l�����\�b�h�̃}�b�v�ɓo�^����B
     * 
     * @param methodCallStack
     *            ���̒��ɓo�^����Ă��郁�\�b�h�̏����l���v�Z����
     * @param methodParam
     *            ���̃��\�b�h�̊J�n�������O�Ɏ��s���������Ă��郁�\�b�h�̏����l���v�Z����
     */
    private static void calcPureValue(final Stack<MethodParam> methodCallStack,
            final MethodParam methodParam)
    {
        if (methodCallStack.size() <= 0)
        {
            return;
        }

        // Stack�̒��ŁAmethodParam�Ŏ�����郁�\�b�h�̊J�n���������O��
        // ���s���I�����Ă��郁�\�b�h�̏����l���v�Z����
        long callStartTime = methodParam.getStartTime();
        MethodParam parentMethodParam = methodCallStack.get(methodCallStack.size() - 1);
        while (parentMethodParam.getEndTime() <= callStartTime)
        {
            methodCallStack.pop();
            if (methodCallStack.size() == 0)
            {
                // ���\�b�h�̃}�b�v�ɏ����l��o�^����
                registerPureDataToJavelinLogElement(parentMethodParam);
                break;
            }

            // �@���ݒ��ڂ��Ă��郁�\�b�h�iparentMethodParam�j�̐e���\�b�h�̒l����A
            // ���ڂ��Ă��郁�\�b�h�̒l���������ƂŁA�����l���v�Z����
            MethodParam grandparentMethodParam = methodCallStack.get(methodCallStack.size() - 1);
            grandparentMethodParam.subtractData(parentMethodParam);

            // ���\�b�h�̃}�b�v�ɏ����l��o�^����
            registerPureDataToJavelinLogElement(parentMethodParam);

            parentMethodParam = grandparentMethodParam;
        }
    }

    /**
     * �����l���v�Z����L�[�̑Ή��𐶐�����B
     * 
     * @return �����l���v�Z���錳�ƂȂ�l�̃L�[�ƁA�����l���i�[����L�[�̃}�b�v
     */
    private static Map<String, String> register()
    {
        Map<String, String> pureKeyMap = new HashMap<String, String>();
        pureKeyMap.put(JavelinLogConstants.EXTRAPARAM_DURATION,
                       JavelinLogConstants.EXTRAPARAM_ELAPSEDTIME);
        pureKeyMap.put(JavelinLogConstants.JMXPARAM_THREAD_CURRENT_THREAD_CPU_TIME_DELTA,
                       JavelinLogConstants.EXTRAPARAM_PURECPUTIME);
        pureKeyMap.put(JavelinLogConstants.JMXPARAM_THREAD_CURRENT_THREAD_USER_TIME_DELTA,
                       JavelinLogConstants.EXTRAPARAM_PUREUSERTIME);
        pureKeyMap.put(JavelinLogConstants.JMXPARAM_THREAD_THREADINFO_WAITED_TIME_DELTA,
                       JavelinLogConstants.EXTRAPARAM_PUREWAITEDTIME);
        return pureKeyMap;
    }

    /**
     * JavelinLogElement��ExtraInfo���ɁA�v�Z���������l��ǋL����B
     * 
     * @param methodParam
     *            ���\�b�h
     */
    private static void registerPureDataToJavelinLogElement(final MethodParam methodParam)
    {
        JavelinLogElement parentMethod = methodParam.getJavelinLogElement();
        for (Map.Entry<String, Double> entrySet : methodParam.getPureDataMap().entrySet())
        {
            double value = entrySet.getValue();
            if (value < 0)
            {
                value = 0;
            }

            String extraInfoStr = parentMethod.getDetailInfo(JavelinParser.TAG_TYPE_EXTRAINFO);
            String newExtraInfoStr = extraInfoStr + entrySet.getKey() + " = " + value + "\r\n";

            parentMethod.setDetailInfo(JavelinParser.TAG_TYPE_EXTRAINFO, newExtraInfoStr);
        }
    }

    /**
     * ExtraInfo�܂���JmxInfo����l���擾����B
     * 
     * @param element
     *            ���\�b�h
     * @param key
     *            �l���擾����L�[
     * @return �l�B�l���擾�ł��Ȃ��ꍇ�� <code>null</code>
     */
    private static String getValueFromExtraInfoOrJmxInfo(final JavelinLogElement element,
            final String key)
    {
        Map<String, String> map = JavelinLogUtil.parseDetailInfo(element, TAG_TYPE_EXTRAINFO);
        String pureValueString = map.get(key);
        if (pureValueString == null)
        {
            map = JavelinLogUtil.parseDetailInfo(element, JavelinParser.TAG_TYPE_JMXINFO);
            pureValueString = map.get(key);
        }
        return pureValueString;
    }

}
