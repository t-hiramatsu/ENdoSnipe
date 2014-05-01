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
package jp.co.acroquest.endosnipe.collector;

import jp.co.acroquest.endosnipe.common.logger.CommonLogMessageCodes;

/**
 * ���O���b�Z�[�W�̂��߂̒萔�N���X�ł��B<br />
 * 
 * @author y-komori
 */
public interface LogMessageCodes extends CommonLogMessageCodes
{
    // -------------------------------------------------------------------------
    // ��{���상�b�Z�[�W�R�[�h (00xx)
    // -------------------------------------------------------------------------
    String ENDOSNIPE_DATA_COLLECTOR_STARTING = "IEDC0001";

    String ENDOSNIPE_DATA_COLLECTOR_STARTED = "IEDC0002";

    String ENDOSNIPE_DATA_COLLECTOR_STOPPING = "IEDC0003";

    String ENDOSNIPE_DATA_COLLECTOR_STOPPED = "IEDC0004";

    String JAVELIN_DATA_LOGGER_STARTED = "IEDC0005";

    String JAVELIN_DATA_LOGGER_STOPPING = "IEDC0006";

    String JAVELIN_DATA_LOGGER_STOPPED = "IEDC0007";

    String JAVELIN_CONNECTED = "IEDC0008";

    String JAVELIN_DISCONNECTED = "IEDC0009";

    String JAVELIN_ALREADY_CONNECTED = "WEDC0010";

    String MAKING_DIR_FAILED = "EEDC0011";

    String WRITING_TEMPFILE_FAILED = "EEDC0012";

    String DATABASE_BASE_DIR = "IEDC0013";

    String DATABASE_PARAMETER = "IEDC0014";

    String IO_EXCEPTION_OCCURED = "EEDC0015";

    String DATA_COLLECTOR_SERVICE_STARTING = "IEDC0016";

    String DATA_COLLECTOR_SERVICE_STARTED = "IEDC0017";

    String DATA_COLLECTOR_SERVICE_STOPPING = "IEDC0018";

    String DATA_COLLECTOR_SERVICE_STOPPED = "IEDC0019";

    String DATA_COLLECTOR_ALREADY_STARTING = "EEDC0020";

    String ERROR_OCCURED_ON_STARTING = "EEDC0021";

    /** �V�X�e���ŗ\�����Ȃ��G���[�����������ꍇ */
    String SYSTEM_UNKNOW_ERROR = "EEDC0023";

    // -------------------------------------------------------------------------
    // �d����M���b�Z�[�W�R�[�h (01xx)
    // -------------------------------------------------------------------------
    /** Javelin���O�ʒm�d����M */
    String JVN_FILE_NOTIFY_RECEIVED = "DEDC0101";

    /** ���\�[�X�ʒm�����d����M */
    String RESOURCE_NOTIFY_RECEIVED = "DEDC0102";

    /** �V�O�i����Ԏ擾�v���d����M */
    String SIGNAL_STATE_NOTIFY_RECEIVED = "DEDC0103";

    /** �V�O�i����`�ύX�v���d����M */
    String SIGNAL_DEFINITION_CHANGE_NOTIFY_RECEIVED = "DEDC0104";

    // -------------------------------------------------------------------------
    // �L���[�֘A���b�Z�[�W�R�[�h (02xx)
    // -------------------------------------------------------------------------
    /** �L���[�Ƀf�[�^��ǉ����� */
    String QUEUE_OFFERED = "DEDC0201";

    /** �L���[����f�[�^�����o���� */
    String QUEUE_TAKEN = "DEDC0202";

    /** �A���[���ʒm�L���[����ꂽ */
    String ALARM_QUEUE_FULL = "WEDC0211";

    // -------------------------------------------------------------------------
    // �f�[�^�x�[�X�֘A���b�Z�[�W�R�[�h (03xx)
    // -------------------------------------------------------------------------
    /** �f�[�^�x�[�X�A�N�Z�X���ɃG���[���������� */
    String DATABASE_ACCESS_ERROR = "EEDC0301";

    /** �z�X�g��񂪌�����Ȃ� */
    String CANNOT_FIND_HOST_INFO = "EEDC0302";

    /** Javelin���O�e�[�u�����[�e�[�g���{ */
    String JAVELINLOG_ROTATE = "DEDC0303";

    /** Javelin���O�e�[�u�����[�e�[�g���s */
    String JAVELINLOG_ROTATE_FAIL = "WEDC0304";

    /** �v���f�[�^�e�[�u�����[�e�[�g���{ */
    String MEASURELOG_ROTATE = "DEDC0305";

    /** �v���f�[�^�e�[�u�����[�e�[�g���s */
    String MEASURELOG_ROTATE_FAIL = "WEDC0306";

    /** ���łɃf�[�^�x�[�X���g�p����Ă��� */
    String DATABASE_ALREADY_USED = "EEDC0307";

    // -------------------------------------------------------------------------
    // DataCollector�֘A���b�Z�[�W�R�[�h (04xx)
    // -------------------------------------------------------------------------
    /** �v���p�e�B�t�@�C����������Ȃ� */
    String CANNOT_FIND_PROPERTY = "EEDC0401";

    /** �z�X�g��񂪌�����Ȃ� */
    String CANNOT_FIND_HOST = "EEDC0402";

    /** �p�����[�^�̉�͂Ɏ��s */
    String FAIL_TO_READ_PARAMETER = "EEDC0403";

    /** 臒l����ݒ�t�@�C���̎擾�Ɏ��s */
    String CANNOT_FIND_RESOURCE_MONITORING = "WEDC0404";

    /** 臒l����ݒ�t�@�C���̃p�����[�^�����s�� */
    String FAIL_MONITORING_PARAM_NUM = "WEDC0405";

    /** 臒l����ݒ�t�@�C���̃p�����[�^���s�� */
    String FAIL_MONITORING_PARAM_VALUE = "WEDC0406";

    /** �p�����[�^�̉�͂Ɏ��s(�f�t�H���g�l���g�p) */
    String FAIL_READ_PARAMETER_USE_DEFAULT = "WEDC0407";

    /** �G�[�W�F���gID�̎擾�Ɏ��s */
    String FAIL_GET_AGENT_ID = "WEDC0408";

    /** �C�x���g�ʒm�N���X�̏������Ɏ��s */
    String FAIL_CREATE_EVENT_SENDER = "WEDC0411";

    /** 臒l���菈���N���X�̏������Ɏ��s */
    String FAIL_CREATE_ALARM_PROCESSOR = "WEDC0412";

    /** Javelin���O�f�[�^�̉�͂Ɏ��s */
    String FAIL_PARSE_JVN_DATA = "WEDC0413";

    /** ���\�[�X��ԊǗ��̐ݒ�l�o�� */
    String OUTPUT_RESOURCE_MONITORING = "IEDC0414";

    /** �C�x���g�ʒm�X���b�h�J�n */
    String EVENT_NOTIFICATION_THREAD_STARTED = "IEDC0415";

    /** �C�x���g�ʒm�X���b�h�I�� */
    String EVENT_NOTIFICATION_THREAD_STOPPING = "IEDC0416";

    /** ���[���e���v���[�g�����w�� */
    String MAIL_TEMPLATE_NOT_SPECIFYED = "WEDC0417";

    /** ���[���e���v���[�g�ݒ�̎擾�Ɏ��s */
    String FAIL_READ_MAIL_TEMPLATE_CONFIG = "WEDC0418";

    /** Javelin �ʐM�p�I�u�W�F�N�g���擾�ł��Ȃ� */
    String CANNOT_GET_JAVELIN_COMMUNICATION_CLIENT = "EEDC0431";

    /** �ݒ�F���[�����M�Ȃ� */
    String NO_SENDMAIL_CONFIG_MESSAGE = "WEDC0451";

    /** ���M���郁�b�Z�[�W���� */
    String NO_SEND_INFORMATION_MESSAGE = "WEDC0452";

    /** ���[�����M���ɗ\�����ʃG���[������ */
    String SENDING_MAIL_ERROR_MESSAGE = "WEDC0453";

    /** �ݒ�:SMTP�T�[�o���ݒ� */
    String SMTP_SERVER_NOT_SPECIFIED = "WEDC0454";

    /** ���[���e���v���[�g�̓ǂݍ��݂Ɏ��s */
    String FAIL_READ_MAIL_TEMPLATE = "WEDC0455";

    /** SMTP�ݒ�̐ݒ�l�o�� */
    String OUTPUT_SMTP_SETTINGS = "IEDC0456";

    /** �ݒ�FSNMP�̃o�[�W�������Ή��O */
    String INVALID_SNMP_VERSION = "WEDC0461";

    /** SNMP�g���b�v���M���ɗ\�����ʗ�O������ */
    String SENDING_SNMP_ERROR_MESSAGE = "WEDC0462";

    /** �ݒ�FSNMP�̃o�[�W�������Ή��O */
    String OUTPUT_SNMP_SETTINGS = "IEDC0463";

    /** 臒l�����`�e�[�u�����擾�ł��Ȃ��ꍇ */
    String FAIL_READ_SIGNAL_DEFINITION = "EEDC0471";

    /** �t�@�C�������ɂ����āA�\�����Ȃ���O�����������ꍇ */
    String ERROR_FILE_HANDLER = "EEDC0499";

    // -------------------------------------------------------------------------
    // �A���[�������������W�֘A���b�Z�[�W�R�[�h (05xx)
    // -------------------------------------------------------------------------
    /** �A���[���������̏����W���J�n */
    String ALARM_DATA_COLLECT_STARTED = "IEDC0501";

    /** �A���[���������̏��擾������ */
    String ALARM_DATA_COLLECT_COMPLETED = "IEDC0502";

    /** �A���[���������̏����W�����ׂĊ��� */
    String ALARM_DATA_COLLECT_ALL_COMPLETED = "IEDC0503";

    /** �A���[��ID�ɑΉ�������擾��`�����݂��Ȃ� */
    String ALARM_DATA_COLLECT_NOT_DEFINED = "DEDC0504";
}
