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
package jp.co.acroquest.endosnipe.common.db;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DB�̎��s�v��̒������`������N���X�B
 * 
 * @param <T> ���s�v���1�v�f
 * @author iida
 */
public abstract class AbstractExecutePlanChecker<T>
{
    /** ���s�v��̃v���t�B�b�N�X */
    public static final String PLAN_PREFIX = "[PLAN] ";

    /**
     * �w�肳�ꂽ���s�v��̃��X�g����A���s�v��̕�������쐬���āA�Ԃ��܂��B<br>
     * 
     * @param executePlanList ���s�v��̃��X�g
     * @return ���s�v��̕�����
     */
    public String parseExecutePlan(final List<String> executePlanList)
    {
        String[] args = executePlanList.toArray(new String[executePlanList.size()]);
        String planString = "";

        for (int index = 0; index < args.length; index++)
        {
            String arg = args[index];
            int planIndex = arg.indexOf(PLAN_PREFIX);
            if (planIndex >= 0)
            {
                planString = args[index].substring(planIndex + PLAN_PREFIX.length());
                break;
            }
        }

        return planString;
    }

    /**
     * �w�肳�ꂽ���s�v��̕����񂩂�AMap�̃��X�g���쐬���āA�Ԃ��܂��B<br>
     * 
     * @param executePlan ���s�v��̕�����
     * @return Map�̃��X�g
     */
    public abstract List<T> parseExecutePlanList(String executePlan);

    /**
     * �w�肳�ꂽ���s�v��̃��X�g����AMap�̃��X�g���쐬���āA�Ԃ��܂��B<br>
     * 
     * @param executePlanList ���s�v��̃��X�g
     * @return Map�̃��X�g
     */
    public List<T> parseExecutePlanList(final List<String> executePlanList)
    {
        String executePlan = this.parseExecutePlan(executePlanList);
        return this.parseExecutePlanList(executePlan);
    }

    /**
     * �w�肳�ꂽ���s�v��̕�����𒲍����A���̒���Full Scan���s���Ă���Table�̖��O�̃Z�b�g���쐬���āA�Ԃ��܂��B<br>
     * �������A�w�肳�ꂽ���O�p�^�[���̕�����ƃ}�b�`������̂́A���̃Z�b�g�Ɋ܂܂�܂���B<br>
     * 
     * @param executePlan ���s�v��̕�����
     * @param excludeString ���O�p�^�[���̕�����B
     * @return Full Scan���s���Ă���Table�̖��O�̃Z�b�g
     */
    public abstract Set<String> getFullScanTableNameSet(String executePlan, String excludeString);

    /**
     * �w�肳�ꂽ�e�[�u�������A�w�肳�ꂽ���O�p�^�[���Ƀ}�b�`���邩�ǂ����𔻒肵�A�Ԃ��܂��B<br>
     * �����̒���null�̂��̂�����ꍇ�Afalse��Ԃ��܂��B<br>
     * 
     * @param tableName ������
     * @param excludePattern ���O�p�^�[��
     * @return ���O�p�^�[���Ƀ}�b�`����ꍇ��true�A�����łȂ��ꍇ��false
     */
    protected boolean isExclude(final String tableName, final Pattern excludePattern)
    {
        if (tableName == null || excludePattern == null)
        {
            return false;
        }

        Matcher matcher = excludePattern.matcher(tableName);
        if (matcher.matches())
        {
            return true;
        }

        return false;
    }
}
