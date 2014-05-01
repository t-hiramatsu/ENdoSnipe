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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * DB2�̎��s�v��̒������s���܂��B
 * 
 * @author ochiai
 */
public class DB2ExecutePlanChecker extends AbstractExecutePlanChecker<Map<String, String>>
{
    /** �t���X�L�������s��ꂽ���Ƃ�����������B */
    private static final String FULL_SCAN = "TBSCAN";

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getFullScanTableNameSet(final String executePlan, final String excludeString)
    {
        // �t���X�L���������������e�[�u���̖��O���܂Ƃ߂�Set�B
        Set<String> fullScanTableSet = new HashSet<String>();

        // �ŏ��Ƀt���X�L��������������ʒu���擾����B
        int fullAccessIndex = executePlan.indexOf(FULL_SCAN);
        // �t���X�L����������������胋�[�v����B
        while (fullAccessIndex >= 0)
        {
            // �t���X�L���������������e�[�u���̖��O��o�^����B
            String tableName = getTableName(executePlan, fullAccessIndex);

            // ���O�ΏۂŖ����ꍇ�̂݁A�ۑ�����B
            Pattern excludePattern = null;
            if (excludeString != null)
            {
                excludePattern = Pattern.compile(excludeString);
            }
            if (isExclude(tableName, excludePattern) == false)
            {
                fullScanTableSet.add(tableName);
            }

            // ���̃t���X�L������T���B
            fullAccessIndex = executePlan.indexOf(FULL_SCAN, fullAccessIndex + 1);
        }

        return fullScanTableSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Map<String, String>> parseExecutePlanList(final String executePlan)
    {
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        return mapList;
    }

    /**
     * �t���X�L���������������e�[�u���������s�v�悩��擾����B
     * 
     * @param plan ���s�v��̕�����B
     * @param index "TBSCAN"�����񂪌��������ʒu�B
     * @return �e�[�u�����B
     */
    private String getTableName(final String plan, final int index)
    {
        String tableName = "";

        int tableIndex = index + FULL_SCAN.length() + 1;
        int spaceIndex = plan.indexOf(",", tableIndex);
        if (spaceIndex < 0)
        {
            tableName = plan.substring(tableIndex);
        }
        else
        {
            tableName = plan.substring(tableIndex, spaceIndex);
        }
        tableName = tableName.trim();

        return tableName;
    }
}
