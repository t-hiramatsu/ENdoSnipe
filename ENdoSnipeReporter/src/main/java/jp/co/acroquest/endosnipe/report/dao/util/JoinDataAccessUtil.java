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
package jp.co.acroquest.endosnipe.report.dao.util;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.data.dao.MeasurementValueDao;
import jp.co.acroquest.endosnipe.data.dto.MeasurementValueDto;

/**
 * �����e�[�u�����������ăf�[�^���擾���邽�߂̃��[�e�B���e�B�N���X
 * 
 * @author M.Yoshida
 */
public class JoinDataAccessUtil
{
    /**
     * �C���X�^���X����h�~���邽�߂̃R���X�g���N�^
     */
    private JoinDataAccessUtil()
    {
    }

    /**
     * �f�[�^�̍��ږ��ƌv�����Ԃ��w�肵�āA�Y�����镽�σ��X�|���X�^�C�����擾����B�B
     * 
     * @param database   �A�N�Z�X�Ώۂ̃f�[�^�x�[�X��
     * @param start      ���������i�J�n�����j
     * @param end        ���������i�I�������j
     * @param cntSumList ���v���s�񐔂��L�^����Ă���f�[�^���X�g
     * @return �����ɍ��v�����f�[�^�̃��X�g�B�w�肵�����ږ������݂��Ȃ��ꍇ��null�B�����ɍ��v����v���l�����݂��Ȃ��ꍇ�͋󃊃X�g��Ԃ��B
     * @throws SQLException �f�[�^�x�[�X�G���[������
     */
    public static List<MeasurementValueDto> getMearsumentValueAverageList(String database,
            Timestamp start, Timestamp end, List<MeasurementValueDto> cntSumList)
        throws SQLException
    {
        List<MeasurementValueDto> aveList =
                                            MeasurementValueDao.selectByTermAndMeasurementTypeWithName(
                                                                                                       database,
                                                                                                       start,
                                                                                                       end,
                                                                                                       TelegramConstants.ITEMNAME_PROCESS_RESPONSE_TIME_AVERAGE);
        List<MeasurementValueDto> cntList =
                                            MeasurementValueDao.selectByTermAndMeasurementTypeWithName(
                                                                                                       database,
                                                                                                       start,
                                                                                                       end,
                                                                                                       TelegramConstants.ITEMNAME_PROCESS_RESPONSE_TOTAL_COUNT);

        Map<String, Map<Long, MeasurementValueDto>> aveMap = classifiedItemMapFromList(aveList);

        Map<String, Map<Long, MeasurementValueDto>> cntMap = classifiedItemMapFromList(cntList);

        List<MeasurementValueDto> resultList = new ArrayList<MeasurementValueDto>();

        for (MeasurementValueDto elem : cntSumList)
        {
            MeasurementValueDto aveElem = new MeasurementValueDto();
            Map<Long, MeasurementValueDto> aveSamplingMap = aveMap.get(elem.measurementItemName);
            Map<Long, MeasurementValueDto> cntSamplingMap = cntMap.get(elem.measurementItemName);

            BigDecimal totalRuntime = new BigDecimal(0);
            for (Long measureNum : aveSamplingMap.keySet())
            {
                BigDecimal averageNum = new BigDecimal(aveSamplingMap.get(measureNum).value);
                BigDecimal cntNum = new BigDecimal(cntSamplingMap.get(measureNum).value);

                totalRuntime = totalRuntime.add(averageNum.multiply(cntNum));
            }

            BigDecimal totalAverage = new BigDecimal(0);

            if (Integer.parseInt(elem.value) != 0)
            {
                totalAverage = totalRuntime.divideToIntegralValue(new BigDecimal(elem.value));
            }
            aveElem.measurementItemId = elem.measurementItemId;
            aveElem.measurementItemName = elem.measurementItemName;
            aveElem.value = totalAverage + "";

            resultList.add(aveElem);
        }

        return resultList;
    }

    /**
     * �f�[�^���X�g����A�n�񖼁A�v���ԍ����L�[�Ƃ���}�b�v�ɕϊ�����B
     * 
     * @param dataList �f�[�^���X�g
     * @return �ϊ����ꂽ�}�b�v
     */
    private static Map<String, Map<Long, MeasurementValueDto>> classifiedItemMapFromList(
            List<MeasurementValueDto> dataList)
    {
        Map<String, Map<Long, MeasurementValueDto>> resultMap =
                                                                new HashMap<String, Map<Long, MeasurementValueDto>>();

        for (MeasurementValueDto elem : dataList)
        {
            Map<Long, MeasurementValueDto> elemMap = resultMap.get(elem.measurementItemName);

            if (elemMap == null)
            {
                elemMap = new HashMap<Long, MeasurementValueDto>();
                resultMap.put(elem.measurementItemName, elemMap);
            }

            elemMap.put(elem.measurementTime.getTime(), elem);
        }

        return resultMap;
    }
}
