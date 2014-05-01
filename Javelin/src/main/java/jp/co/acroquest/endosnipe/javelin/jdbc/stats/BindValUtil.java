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
package jp.co.acroquest.endosnipe.javelin.jdbc.stats;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jp.co.acroquest.endosnipe.javelin.jdbc.common.JdbcJavelinConfig;
import jp.co.acroquest.endosnipe.javelin.util.StatsUtil;

/**
 * �o�C���h�����𑀍삷�邽�߂̃��[�e�B���e�B�B
 * 
 * @author eriguchi
 */
public class BindValUtil
{
    /**
     * �C���X�^���X����h�~����R���X�g���N�^�B
     */
    private BindValUtil()
    {

    }

    /**
     * �o�C���h������ۑ�����B(Object)
     * 
     * @param bindValList �o�C���h�����̃}�b�v���i�[�������X�g�B
     * @param bindValListIndex bindValList�ł̈ʒu�B
     * @param index bindVal�̈ʒu�B
     * @param bindVal �o�C���h�����B
     * @param isLimitLength �o�C���h�����𕶎��񂩂���ۂɕ����񒷂𐧌����邩�ǂ����B
     */
    public static void recordBindVal(List<Map<Integer, Object>> bindValList, int bindValListIndex,
            int index, Object bindVal, boolean isLimitLength)
    {
        JdbcJavelinConfig config = JdbcJavelinRecorder.getConfig();
        if (config.isRecordBindVal() == false && config.isRecordExecPlan() == false)
        {
            return;
        }

        int limitLength;
        if (isLimitLength)
        {
            limitLength = (int)config.getJdbcStringLimitLength();
        }
        else
        {
            limitLength = -1;
        }

        String bindValStr = StatsUtil.toStr(bindVal, limitLength);
        recordBindVal(bindValList, bindValListIndex, index, bindValStr);
    }

    /**
     * �o�C���h������ۑ�����B(byte)
     * 
     * @param bindValList �o�C���h�����̃}�b�v���i�[�������X�g�B
     * @param bindValListIndex bindValList�ł̈ʒu�B
     * @param index bindVal�̈ʒu�B
     * @param bindVal �o�C���h�����B
     */
    public static void recordBindVal(List<Map<Integer, Object>> bindValList, int bindValListIndex,
            int index, byte bindVal)
    {
        JdbcJavelinConfig config = JdbcJavelinRecorder.getConfig();
        if (config.isRecordBindVal() == false && config.isRecordExecPlan() == false)
        {
            return;
        }

        String bindValStr = StatsUtil.toStr(bindVal);
        recordBindVal(bindValList, bindValListIndex, index, bindValStr);
    }

    /**
     * �o�C���h������ۑ�����B(byte[])
     * 
     * @param bindValList �o�C���h�����̃}�b�v���i�[�������X�g�B
     * @param bindValListIndex bindValList�ł̈ʒu�B
     * @param index bindVal�̈ʒu�B
     * @param bindVal �o�C���h�����B
     */
    public static void recordBindVal(List<Map<Integer, Object>> bindValList, int bindValListIndex,
            int index, byte[] bindVal)
    {
        JdbcJavelinConfig config = JdbcJavelinRecorder.getConfig();
        if (config.isRecordBindVal() == false && config.isRecordExecPlan() == false)
        {
            return;
        }
        
        String bindValStr = StatsUtil.toStr(bindVal);
        recordBindVal(bindValList, bindValListIndex, index, bindValStr);
    }

    /**
     * �o�C���h������ۑ�����B
     * 
     * @param bindValList �o�C���h�����̃}�b�v���i�[�������X�g�B
     * @param bindValListIndex bindValList�ł̈ʒu�B
     * @param index bindVal�̈ʒu�B
     * @param bindValStr �o�C���h�����B
     */
    public static void recordBindVal(List<Map<Integer, Object>> bindValList, int bindValListIndex,
            int index, String bindValStr)
    {
        Map<Integer, Object> map = getBindValMap(bindValList, bindValListIndex);
        map.put(Integer.valueOf(index), bindValStr);
    }

    /**
     * �o�C���h�����̃}�b�v���擾����B
     * 
     * @param bindValList �o�C���h�����̃}�b�v���i�[�������X�g�B
     * @param bindValListIndex bindValList�ł̈ʒu�B
     * @return �o�C���h�����̃}�b�v�B
     */
    private static Map<Integer, Object> getBindValMap(List<Map<Integer, Object>> bindValList,
            int bindValListIndex)
    {
        Map<Integer, Object> map;
        if (bindValList.size() <= bindValListIndex)
        {
            map = new TreeMap<Integer, Object>();
            bindValList.add(map);
        }
        else
        {
            map = bindValList.get(bindValListIndex);
        }
        return map;
    };
}
