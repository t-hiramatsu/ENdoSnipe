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
package jp.co.acroquest.endosnipe.javelin.event;

import jp.co.acroquest.endosnipe.common.event.EventConstants;
import jp.co.acroquest.endosnipe.javelin.util.ThreadUtil;

/**
 * HttpStatusEvent�N���X
 * @author acroquest
 *
 */
public class HttpStatusEvent extends AbstractNameAndParamCheckEvent
{
    /**
     * �R���X�g���N�^
     * @param contextPath �R���e�L�X�g�p�X
     * @param servletPath �T�[�u���b�g�p�X
     * @param status �X�e�[�^�X
     * @param throwable ��O
     * @param stackTraceDepth stacktrace�̐[��
     */
    public HttpStatusEvent(String contextPath, String servletPath, int status,
            Throwable throwable, int stackTraceDepth)
    {
        this.setName(EventConstants.NAME_HTTP_STATUS_ERROR);
        this.addParam(EventConstants.PARAM_HTTP_URL, contextPath + servletPath);
        this.addParam(EventConstants.PARAM_HTTP_STATUS, String.valueOf(status));
        if (throwable != null)
        {
            this.addParam(EventConstants.PARAM_HTTP_THROWABLE_MESSAGE, throwable.getMessage());
            this.addParam(EventConstants.PARAM_HTTP_THROWABLE_STACKTRACE,
                      ThreadUtil.getStackTrace(throwable.getStackTrace(), stackTraceDepth));
        }
    }

}
