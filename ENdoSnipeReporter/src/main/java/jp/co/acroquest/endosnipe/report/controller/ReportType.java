/*
 * Copyright (c) 2004-2009 SMG Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  SMG Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.report.controller;

/**
 * ���|�[�g�́u��ށv�������񋓌^
 * ��ޖ��Ƀv���p�e�B�L�[�𓱂����߂́uID�v�����B
 * 
 * @author M.Yoshida
 *
 */
public enum ReportType
{
    /** �V�X�e�����\�[�X�g�p���ڃ��|�[�g */
    SYSTEM("reporter.report.type.system"),
    /** �v���Z�X���\�[�X�g�p���ڃ��|�[�g */
    PROCESS("reporter.report.type.process"),
    /** �f�[�^���o�̓��|�[�g */
    DATA_IO("reporter.report.type.dataIO"),
    /** VM��ԃ��|�[�g */
    VM_STATUS("reporter.report.type.vmStatus"),
    /** �I�u�W�F�N�g�����|�[�g */
    OBJECT("reporter.report.type.object"),
    /** ���X�|���X�^�C���T�}�����|�[�g */
    RESPONSE_SUMMARY("reporter.report.type.responseSummary"),
    /** �A�N�Z�X�Ώەʃ��X�|���X�^�C�����|�[�g */
    RESPONSE_LIST("reporter.report.type.responseList"),
    /** �A�v���P�[�V�������|�[�g */
    APPLICATION("reporter.report.type.application"),
    /** �A�v���P�[�V�������|�[�g��Commons Pool�̃T�C�Y�̃��|�[�g */
    SERVER_POOL("reporter.report.type.serverPool"),
    /** �A�v���P�[�V�������|�[�g��AP�T�[�o�̃��[�J�X���b�h���̃��|�[�g */
    POOL_SIZE("reporter.report.type.poolSize"),
    /** Javelin���|�[�g */
    JAVELIN("reporter.report.type.javelin"),
    /** �C�x���g��ʖ��̃C�x���g�����񐔃��|�[�g */
    EVENT("reporter.report.type.javelin.event"),
    /** PerformanceDoctor���|�[�g */
    PERF_DOCTOR("reporter.report.type.perfDoctor"),
    /** ���X�|���X�^�C���T�}�����|�[�g */
    ITEM("reporter.report.type.item"),
    /** ���σT�}���̃��|�[�g */
    OBJECT_AVERAGE("reporter.report.type.object.average"),
    /** �ώZ�T�}���O���t�̃��|�[�g */
    OBJECT_TOTAL("reporter.report.type.object.total"),
    /** �T�}�����|�[�g */
    SUMMARY("reporter.report.type.summary");


    /** ID */
    private String id_;

    /**
     * �R���X�g���N�^
     * @param id
     */
    private ReportType(String id)
    {
        this.id_ = id;
    }

    /**
     * ���|�[�g�̎�ނɑΉ�����uID�v���擾����B
     * 
     * @return ID
     */
    public String getId()
    {
        return this.id_;
    }
}
