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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Oracle�̎��s�v��̒������s���܂��B
 * 
 * @author iida
 */
public class OracleExecutePlanChecker extends AbstractExecutePlanChecker<Map<String, String>>
{
    /** �t���X�L�������s��ꂽ���Ƃ����������� */
    private static final String FULL_SCAN = "TABLE ACCESS FULL";

    /** ���s�v�撆�̖��߂�\����̃w�b�_�[ */
    private static final String OPERATION = "Operation";

    /** �e�[�u������\����̃w�b�_�[ */
    private static final String TABLE_NAME = "Name";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Map<String, String>> parseExecutePlanList(final String executePlan)
    {
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();

        int startIndex = executePlan.indexOf("--------------------------------------------------");
        if (startIndex < 0)
        {
            return mapList;
        }

        // ���s�ŋ�؂�
        String[] lines = executePlan.split("[\r\n]{1,}");

        // �w�b�_�s�̉�́�Map�̃L�[���擾����
        List<String> header = new ArrayList<String>();
        String line;
        int index = 0;
        for (index = 0; index < lines.length; index++)
        {
            line = lines[index];

            if (line.contains("|"))
            {
                String[] columns = line.split("\\|");
                for (String column : columns)
                {
                    column = column.trim();
                    if (column.length() > 0)
                    {
                        header.add(column);
                    }
                }

                break;
            }
        }

        if (index >= lines.length)
        {
            // �w�b�_��������Ȃ�����
            return mapList;
        }

        // �f�[�^���̌��������X�g�ւ̒ǉ�
        for (int dataIndex = index + 1; dataIndex < lines.length; dataIndex++)
        {
            String dataLine = lines[dataIndex];
            if (dataLine.contains("|"))
            {
                // �w�b�_������ɑΉ�����Map���쐬���A���X�g�ɒǉ�����
                Map<String, String> map = new HashMap<String, String>();
                String[] columns = dataLine.split("\\|");
                // TODO StringUtils#split���g�p���Ă����������AString#split�ɕύX�����B
                // �{���̈Ӑ}�ʂ�ƂȂ��Ă��邩�ǂ������m�F����K�v�L��B
                for (int colIndex = 0, listIndex = 0; colIndex < columns.length; colIndex++)
                {
                    if (columns[colIndex] == null)
                    {
                        break;
                    }

                    String column = columns[colIndex].trim();
                    if (column.length() > 0)
                    {
                        map.put(header.get(listIndex), column);
                    }

                    if (0 < columns[colIndex].length())
                    {
                        listIndex++;
                    }
                }
                mapList.add(map);
            }
        }

        return mapList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getFullScanTableNameSet(final String executePlan, final String excludeString)
    {
        List<Map<String, String>> mapList = this.parseExecutePlanList(executePlan);

        Set<String> fullScanTableSet = new HashSet<String>();

        for (Map<String, String> map : mapList)
        {
            String operation = map.get(OPERATION);
            if (operation == null)
            {
                // ����ȓ����������肱���ɂ͓��B���Ȃ��B
                return fullScanTableSet;
            }

            int fullAccessIndex = operation.indexOf(FULL_SCAN);
            // FULL_SCAN�̃L�[���[�h���܂܂Ȃ��ꍇ
            if (fullAccessIndex < 0)
            {
                continue;
            }

            String tableName = map.get(TABLE_NAME);

            // ���O�ΏۂŖ����ꍇ�̂݁A�ۑ�����B
            Pattern excludePattern = null;
            if (excludeString != null)
            {
                excludePattern = Pattern.compile(excludeString);
            }
            if (super.isExclude(tableName, excludePattern) == false)
            {
                fullScanTableSet.add(tableName);
            }
        }
        return fullScanTableSet;
    }
}
