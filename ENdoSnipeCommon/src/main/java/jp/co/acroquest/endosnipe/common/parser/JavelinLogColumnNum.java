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
package jp.co.acroquest.endosnipe.common.parser;

/**
 * ���샍�O�̊e���ʎq�ɂ������{����\���萔�C���^�[�t�F�[�X�ł��B<br />
 * 
 * @author kameda
 */
public interface JavelinLogColumnNum
{
    /** ���ʎq */
    int ID = 0;

    /** CALL���O�̃��O�o�͎��� */
    int CALL_TIME = 1;

    /** CALL���O�̌Ăяo���惁�\�b�h�� */
    int CALL_CALLEE_METHOD = 2;

    /** CALL���O�̌Ăяo����N���X�� */
    int CALL_CALLEE_CLASS = 3;

    /** CALL���O�̌Ăяo����I�u�W�F�N�gID */
    int CALL_CALLEE_OBJECTID = 4;

    /** CALL���O�̌Ăяo�������\�b�h�� */
    int CALL_CALLER_METHOD = 5;

    /** CALL���O�̌Ăяo�����N���X�� */
    int CALL_CALLER_CLASS = 6;

    /** CALL���O�̌Ăяo�����I�u�W�F�N�gID */
    int CALL_CALLER_OBJECTID = 7;

    /** CALL���O�̌Ăяo���惁�\�b�h�̃��f�B�t�@�C�A */
    int CALL_CALLEE_METHOD_MODIFIER = 8;

    /** CALL���O�̃X���b�hID */
    int CALL_THREADID = 9;

    /** RETURN���O�̃��O�o�͎��� */
    int RETURN_TIME = 1;

    /** RETURN���O�̌Ăяo���惁�\�b�h�� */
    int RETURN_CALLEE_METHOD = 2;

    /** RETURN���O�̌Ăяo����N���X�� */
    int RETURN_CALLEE_CLASS = 3;

    /** RETURN���O�̌Ăяo����I�u�W�F�N�gID */
    int RETURN_CALLEE_OBJECTID = 4;

    /** RETURN���O�̌Ăяo�������\�b�h�� */
    int RETURN_CALLER_METHOD = 5;

    /** RETURN���O�̌Ăяo�����N���X�� */
    int RETURN_CALLER_CLASS = 6;

    /** RETURN���O�̌Ăяo�����I�u�W�F�N�gID */
    int RETURN_CALLER_OBJECTID = 7;

    /** RETURN���O�̌Ăяo���惁�\�b�h�̃��f�B�t�@�C�A */
    int RETURN_CALLEE_METHOD_MODIFIER = 8;

    /** RETURN���O�̃X���b�hID */
    int RETURN_THREADID = 9;

    /** THROW���O�̃��O�o�͎��� */
    int THROW_TIME = 1;

    /** THROW���O�̗�O�N���X�� */
    int THROW_EX_CLASS = 2;

    /** THROW���O�̗�O�I�u�W�F�N�gID */
    int THROW_EX_OBJECTID = 3;

    /** THROW���O��throw�����\�b�h�� */
    int THROW_THROWER_METHOD = 4;

    /** THROW���O��throw���N���X�� */
    int THROW_THROWER_CLASS = 5;

    /** THROW���O��throw���I�u�W�F�N�gID */
    int THROW_THROWER_OBJECTID = 6;

    /** THROW���O�̃X���b�hID */
    int THROW_THREADID = 7;

    /** CATCH���O�̃��O�o�͎��� */
    int CATCH_TIME = 1;

    /** CATCH���O�̗�O�N���X�� */
    int CATCH_EX_CLASS = 2;

    /** CATCH���O�̗�O�I�u�W�F�N�gID */
    int CATCH_EX_OBJECTID = 3;

    /** CATCH���O��catch�惁�\�b�h�� */
    int CATCH_CATCHER_METHOD = 4;

    /** CATCH���O��catch��N���X�� */
    int CATCH_CATCHER_CLASS = 5;

    /** CATCH���O��catch��I�u�W�F�N�gID */
    int CATCH_CATCHER_OBJECTID = 6;

    /** CATCH���O�̃X���b�hID */
    int CATCH_THREADID = 7;

    /** READ,WRITE���O�̃��O�o�͎��� */
    int READ_WRITE_TIME = 1;

    /** READ,WRITE���O�̃A�N�Z�X��t�B�[���h�� */
    int READ_WRITE_ACCESSEE_FIELD = 2;

    /** READ,WRITE���O�̃A�N�Z�X��N���X�� */
    int READ_WRITE_ACCESSEE_CLASS = 3;

    /** READ,WRITE���O�̃A�N�Z�X��I�u�W�F�N�gID */
    int READ_WRITE_ACCESSEE_OBJECTID = 4;

    /** READ,WRITE���O�̃A�N�Z�X�����\�b�h�� */
    int READ_WRITE_ACCESSOR_METHOD = 5;

    /** READ,WRITE���O�̃A�N�Z�X���N���X�� */
    int READ_WRITE_ACCESSOR_CLASS = 6;

    /** READ,WRITE���O�̃A�N�Z�X���I�u�W�F�N�gID */
    int READ_WRITE_ACCESSOR_OBJECTID = 7;

    /** READ,WRITE���O�̃A�N�Z�X��t�B�[���h�̌^ */
    int READ_WRITE_CALLEE_METHOD_MODIFIER = 8;

    /** READ,WRITE���O�̃X���b�hID */
    int READ_WRITE_THREADID = 9;

    /** EVENT���O�̃��O�o�͎��� */
    int EVENT_TIME = 1;

    /** EVENT���O�̃C�x���g�� */
    int EVENT_NAME = 2;

    /** EVENT���O�̔����������\�b�h�� */
    int EVENT_METHOD = 3;

    /** EVENT���O�̔��������N���X�� */
    int EVENT_CLASS = 4;

    /** EVENT���O�̔��������N���X�� */
    int EVENT_LEVEL = 5;

    /** EVENT���O�̃X���b�hID */
    int EVENT_THREADID = 6;
}
