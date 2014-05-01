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

/**
 * �p�t�H�[�}���X�h�N�^�[�̐f�f���ʂ̈ꍀ�ځB
 * 
 * @author eriguchi
 * 
 */
public class WarningUnit
{
    /** �x��ID */
    private final String   unitId_;

    /** ���[��ID */
    private final String   id_;

    /** ���e */
    private final String   description_;

    /** �x���Ώۂ̃N���X�� */
    private final String   className_;

    /** �x���Ώۂ̃��\�b�h�� */
    private final String   methodName_;

    /** �x���̏d�v�x */
    private final String   level_;

    /** �x���Ώۂ̃��O�t�@�C���� */
    private final String   logFileName_;

    /** �x���Ώۂ̃��O�t�@�C���̍s�ԍ� */
    private final int      logFileLineNumber_;

    /** �J�n���� */
    private final long     startTime_;

    /** �I������ */
    private final long     endTime_;

    /** �~���t���O(�t�B���^���ɍ~���ɕ��ׂ邩�ǂ���) */
    private final boolean  isDescend_;

    /** �ϐ����X�g */
    private final Object[] args_;

    /** �X�^�b�N�g���[�X */
    private String         stackTrace_ = "";

    /** �C�x���g�ɂ��x���ł��邩�ǂ����B */
    private boolean        isEvent_    = false;

    /**
     * �R���X�g���N�^�B
     * 
     * @param unitId �x����ID
     * @param id ���[����ID
     * @param description �x���̐����B
     * @param className �N���X���B
     * @param methodName ���\�b�h���B
     * @param level �d�v�x
     * @param logFileName ���O�t�@�C�����B
     * @param logFileLineNumber �s�ԍ��B
     * @param startTime �J�n����
     * @param endTime �I������
     * @param isDescend �x���̗D��x���~���ɂ��邩�ǂ����B
     * @param args 臒l�A���o�l�Ȃǂ̈����B
     */
    WarningUnit(final String unitId, final String id, final String description,
            final String className, final String methodName, final String level,
            final String logFileName, final int logFileLineNumber, final long startTime,
            final long endTime, final boolean isDescend, final Object[] args)
    {
        super();
        this.unitId_ = unitId;
        this.id_ = id;
        this.description_ = description;
        this.className_ = className;
        this.methodName_ = methodName;
        this.level_ = level;
        this.logFileName_ = logFileName;
        this.logFileLineNumber_ = logFileLineNumber;
        this.startTime_ = startTime;
        this.endTime_ = endTime;
        this.isDescend_ = isDescend;
        this.args_ = args;
    }

    /**
     * �R���X�g���N�^�B
     * 
     * @param unitId �x����ID
     * @param id ���[����ID
     * @param description �x���̐����B
     * @param className �N���X���B
     * @param methodName ���\�b�h���B
     * @param level �d�v�x
     * @param logFileName ���O�t�@�C�����B
     * @param logFileLineNumber �s�ԍ��B
     * @param startTime �J�n����
     * @param endTime �I������
     * @param isDescend �x���̗D��x���~���ɂ��邩�ǂ����B
     * @param isEvent �C�x���g�ł��邩�ǂ����B
     * @param stackTrace �X�^�b�N�g���[�X
     * @param args 臒l�A���o�l�Ȃǂ̈����B
     */
    WarningUnit(final String unitId, final String id, final String description,
            final String className, final String methodName, final String level,
            final String logFileName, final int logFileLineNumber, final long startTime,
            final long endTime, final boolean isDescend, final boolean isEvent,
            final String stackTrace, final Object[] args)
    {
        this(unitId, id, description, className, methodName, level, logFileName, logFileLineNumber,
                startTime, endTime, isDescend, args);
        this.isEvent_ = isEvent;
        this.stackTrace_ = stackTrace;
    }

    /**
     * �X�^�b�N�g���[�X���擾���܂��B<br />
     * 
     * @return �X�^�b�N�g���[�X
     */
    public String getStackTrace()
    {
        return stackTrace_;
    }

    /**
     * �C�x���g�ł��邩�ǂ�����Ԃ��܂��B<br />
     * 
     * @return ���̌x���I�u�W�F�N�g���C�x���g�ł���΁A<code>true</code>
     */
    public boolean isEvent()
    {
        return isEvent_;
    }

    /**
     * @return �N���X���B
     */
    public String getClassName()
    {
        return this.className_;
    }

    /**
     * @return �����B�B
     */
    public String getDescription()
    {
        return this.description_;
    }

    /**
     * @return unitId
     */
    public String getUnitId()
    {
        return this.unitId_;
    }

    /**
     * @return ID�B
     */
    public String getId()
    {
        return this.id_;
    }

    /**
     * @return �s�ԍ��B
     */
    public int getLogFileLineNumber()
    {
        return this.logFileLineNumber_;
    }

    /**
     * @return ���O�t�@�C�����B
     */
    public String getLogFileName()
    {
        return this.logFileName_;
    }

    /**
     * @return ���\�b�h���B
     */
    public String getMethodName()
    {
        return this.methodName_;
    }

    /**
     * @return �d�v�x�B
     */
    public String getLevel()
    {
        return this.level_;
    }

    /**
     * �ϐ��̃��X�g��z��Ƃ��ĕԂ��B
     * @return �ϐ����X�g
     */
    public Object[] getArgs()
    {
    	if (args_ == null)
    	{
    		return null;
    	}
    	else
    	{
    		return this.args_.clone();
    	}
    }

    /**
     * �J�n���Ԃ��擾���܂��B<br />
     * 
     * @return �J�n����
     */
    public long getStartTime()
    {
        return this.startTime_;
    }

    /**
     * �I�����Ԃ��擾���܂��B<br />
     * 
     * @return �I������
     */
    public long getEndTime()
    {
        return this.endTime_;
    }

    /**
     * �~���t���O��ݒ肵�܂��B<br />
     * 
     * @return �t�B���^���ɍ~���ɕ��ׂ�Ƃ��ɂ�<code>trud</code>
     */
    public boolean isDescend()
    {
        return this.isDescend_;
    }
}