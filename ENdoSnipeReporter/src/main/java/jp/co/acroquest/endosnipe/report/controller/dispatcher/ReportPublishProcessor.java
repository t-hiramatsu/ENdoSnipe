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
package jp.co.acroquest.endosnipe.report.controller.dispatcher;

import jp.co.acroquest.endosnipe.report.controller.ReportProcessReturnContainer;
import jp.co.acroquest.endosnipe.report.controller.ReportSearchCondition;

/**
 * ���|�[�g�����v���Z�b�T�̃C���^�t�F�[�X�B
 * ���|�[�g�����v���Z�b�T�́A�{�C���^�t�F�[�X��implements���č쐬����B
 * 
 * @author M.Yoshida
 */
public interface ReportPublishProcessor
{
    /**
     * ���|�[�g�o�͏������s���B
     * 
     * @param cond ���|�[�g�o�͂̍ۂ̏���
     * @throws InterruptedException ���荞�ݔ�����
     * @return ���|�[�g�o�͂̌���
     */
    ReportProcessReturnContainer publish(ReportSearchCondition cond)
        throws InterruptedException;
}
