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
package jp.co.acroquest.endosnipe.data.entity;

import java.io.InputStream;
import java.sql.Timestamp;

/**
 * Javelin ���O�e�[�u���ɑ΂���G���e�B�e�B�N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class JavelinLog
{
    /**
     * ���O����ӂɎ��ʂ��� ID �B<br/ >
     *
     * ID �̓V�[�P���X�uSEQ_JAVELIN_LOG�v�ɂ���Ĕ��s���܂��B
     */
    public long logId;

    /**
     * �Z�b�V��������ӂɎ��ʂ���ID�B<br />
     *
     * ���O�t�@�C���𕡐����R�[�h�ɕ������Ċi�[����ꍇ�A���� SESSION_ID �����蓖�Ă��܂��B
     * ID �̓V�[�P���X�uSEQ_JAVELIN_SESSION�v�ɂ���Ĕ��s���܂��B
     */
    public long sessionId;

    /**
     * ���O�t�@�C���𕡐����R�[�h�ɕ������Ċi�[����ꍇ�A���Ԃ����ʂ��邽�߂̒l�B<br />
     *
     * 1 ����n�܂�ʔԂƂ��܂��B<br />
     * �������R�[�h�ɕ������Ȃ��ꍇ�� 1 �ŌŒ�Ƃ��܂��B
     */
    public int sequenceId;

    /**
     * Javelin ���O�{�́B<br />
     */
    public InputStream javelinLog;

    /**
     * Javelin ���O�̃t�@�C�����B<br />
     */
    public String logFileName;

    /**
     * �Z�b�V�����̊J�n�����B<br />
     */
    public Timestamp startTime;

    /**
     * �Z�b�V�����̏I�������B<br />
     */
    public Timestamp endTime;

    /**
     * �Z�b�V�����Ɋւ���ڍא����B<br />
     *
     * ��͎��ɃZ�b�V�����֐����������邱�Ƃ�z��B
     */
    public String sessionDesc;

    /**
     * Javelin�̃��O��ʂ�\�����l�B<br />
     *
     * 1�FCALL<br />
     * 2�FRETURN<br />
     * 3�FREAD<br />
     * 4�FWRITE<br />
     * 5�FTHROW<br />
     * 6�FCATCH
     */
    public int logType;

    /**
     * CALL, RETURN �F�Ăяo���ꂽ���\�b�h�̖��O�B<br />
     * READ, WRITE �F�A�N�Z�X���ꂽ�t�B�[���h�̖��O�B
     */
    public String calleeName;

    /**
     * CALL, RETURN �F�Ăяo���ꂽ���\�b�h�̃V�O�l�`���B<br />
     * READ, WRITE �F�A�N�Z�X���ꂽ�t�B�[���h�̃V�O�l�`���B
     */
    public String calleeSignature;

    /**
     * Call, Return �F�Ăяo���ꂽ�N���X�̖��O�B<br />
     * Read, Write �F�A�N�Z�X���ꂽ�N���X�̖��O�B<br />
     * Throw, Catch �F�X���[���ꂽ��O�̃N���X���B<br />
     * �N���X���̓p�b�P�[�W���܂߂����̂Ƃ��܂��B
     */
    public String calleeClass;

    /**
     * �A�N�Z�X��t�B�[���h�̌^�B<br />
     *
     * �N���X���̓p�b�P�[�W���܂߂����̂Ƃ��܂��B
     */
    public String calleeFieldType;

    /**
     * Call, Return �F�Ăяo���ꂽ�I�u�W�F�N�g�̎��ʎq�B<br />
     * Read, Write �F�A�N�Z�X���ꂽ�I�u�W�F�N�g�̎��ʎq�B<br />
     * Throw, Catch �F throw ���ꂽ��O�̃I�u�W�F�N�g�̎��ʎq�B
     */
    public int calleeObjectId;

    /**
     * Call, Return �F�Ăяo�����̃��\�b�h�̖��O�B<br />
     * Read, Write �F�A�N�Z�X�����\�b�h�̖��O�B<br />
     * Throw �F��O�� throw �������\�b�h�̖��O�B<br />
     * Catch �F��O�� catch �������\�b�h�̖��O�B
     */
    public String callerName;

    /**
     * Call, Return �F�Ăяo�����̃��\�b�h�̃V�O�l�`���B<br />
     * Read, Write �F�A�N�Z�X�����\�b�h�̃V�O�l�`���B<br />
     * Throw �F��O�� throw �������\�b�h�̃V�O�l�`���B<br />
     * Catch �F��O�� catch �������\�b�h�̃V�O�l�`���B
     */
    public String callerSignature;

    /**
     * Call, Return �F���\�b�h���Ăяo�����N���X�̖��O�B<br />
     * Read, Write �F�t�B�[���h�ɃA�N�Z�X�����N���X�̖��O�B<br />
     * Throw �F��O���X���[�����N���X�̖��O�B<br />
     * Catch �F��O���L���b�`�����N���X�̖��O�B<br />
     * �N���X���̓p�b�P�[�W���܂߂����̂Ƃ��܂��B
     */
    public String callerClass;

    /**
     * Call, Return �F���\�b�h���Ăяo�����I�u�W�F�N�g�̎��ʎq�B<br />
     * Read, Write �F�A�N�Z�X���I�u�W�F�N�g�̎��ʎq�B<br />
     * Throw �F��O���X���[�����I�u�W�F�N�g�̎��ʎq�B<br />
     * Catch �F��O���L���b�`�����I�u�W�F�N�g�̎��ʎq�B
     */
    public int callerObjectId;

    /**
     * �C�x���g�̌x�����x���B�ȉ��̒l�Ƃ���B<br />
     * INFO�F20�AWARN:30�AERROR:40
     */
    public int eventLevel;

    /**
     * ���\�b�h�̎��s�ɂ����������ԁB<br />
     */
    public long elapsedTime;

    /**
     * �Ăяo���惁�\�b�h�̃��f�B�t�@�C�A�𕶎���Ŋi�[���܂��B<br />
     */
    public String modifier;

    /**
     * ���O�o�͂��Ă���X���b�h�̖��́B<br />
     */
    public String threadName;

    /**
     * ���O�o�͂��Ă��� Thread �I�u�W�F�N�g�̃p�b�P�[�W�����܂߂��N���X���B<br />
     */
    public String threadClass;

    /**
     * ���O�o�͂��Ă���X���b�h�̃I�u�W�F�N�g ID �B<br />
     */
    public int threadObjectId;
    
    /**
     * �v�����ڂ̖��O�B<br />
     */
    public String measurementItemName;

}
