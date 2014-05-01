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
package jp.co.acroquest.endosnipe.javelin.converter.file;

/**
 * �t�@�C��I/O�̃��j�^�����O�Ɏg�p����萔�̏W���B
 * 
 * @author yamasaki
 */
public interface FileMonitorConstants
{
    /** �X���b�h���̃l�b�g���[�N��M�ʂ�ۑ��E�Q�Ƃ���ۂ̃L�[�B */
    String KEY_FILE_THREAD_READ_LENGTH = "file.currentFileReadLength";

    /** �X���b�h���̃l�b�g���[�N���M�ʂ�ۑ��E�Q�Ƃ���ۂ̃L�[�B */
    String KEY_FILE_THREAD_WRITE_LENGTH = "file.currentFileWriteLength";

    /** �v���Z�X�S�̂ł̃l�b�g���[�N��M�ʂ�ۑ��E�Q�Ƃ���ۂ̃L�[�B */
    String KEY_FILE_READ_LENGTH = "io.fileReadLength";

    /** �v���Z�X�S�̂ł̃l�b�g���[�N���M�ʂ�ۑ��E�Q�Ƃ���ۂ̃L�[�B */
    String KEY_FILE_WRITE_LENGTH = "io.fileWriteLength";
}
