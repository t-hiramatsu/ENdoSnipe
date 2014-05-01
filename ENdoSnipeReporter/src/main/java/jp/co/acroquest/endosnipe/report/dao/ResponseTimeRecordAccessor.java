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
package jp.co.acroquest.endosnipe.report.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.report.entity.ReportItemValue;
import jp.co.acroquest.endosnipe.report.entity.ResponseTimeRecord;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.data.dto.MeasurementValueDto;
import jp.co.acroquest.endosnipe.report.dao.ReportDao;

/**
 * ���X�|���X�^�C�����|�[�g�p�̃f�[�^�ɃA�N�Z�X���邽�߂�DAO�B
 * DB����擾�����f�[�^�̉��H���s���A���|�[�g�ɏo�͂ł���f�[�^�`���ɕϊ�����B
 * 
 * @author M.Yoshida
 */
public class ResponseTimeRecordAccessor
{
    /**
     * ���Ԃ��w�肵�A���̊��ԓ��ł̃��X�|���X�^�C�����|�[�g�̃f�[�^���擾����B
     * 
     * @param database  �f�[�^�x�[�X��
     * @param startTime ��������(�J�n����)
     * @param endTime   ��������(�I������)
     * @return ���X�|���X�^�C�����|�[�g�̃f�[�^
     * @throws SQLException �f�[�^�擾���ɃG���[�����������ꍇ
     */
    public List<ResponseTimeRecord> findResponseStatisticsByTerm(String database,
            Timestamp startTime, Timestamp endTime) throws SQLException
    {
        List<ResponseTimeRecord> result = new ArrayList<ResponseTimeRecord>();

        List<ReportItemValue> minValues = ReportDao.selectAverage(database, startTime, endTime,
                TelegramConstants.ITEMNAME_PROCESS_RESPONSE_TIME_MIN);

        List<ReportItemValue> maxValues = ReportDao.selectAverage(database, startTime, endTime,
                TelegramConstants.ITEMNAME_PROCESS_RESPONSE_TIME_MAX);

        List<ReportItemValue> cntValues = ReportDao.selectAverage(database, startTime, endTime,
                TelegramConstants.ITEMNAME_PROCESS_RESPONSE_TOTAL_COUNT);

        List<ReportItemValue> aveValues = ReportDao.selectAverage(database, startTime, endTime,
                TelegramConstants.ITEMNAME_PROCESS_RESPONSE_TIME_AVERAGE);

//        for (String accessedTarget : cntMap.keySet())
//        {
//            ResponseTimeRecord resultElem = new ResponseTimeRecord();
//            resultElem.setAccessTarget(accessedTarget);
//            resultElem.setAccessCount(cntMap.get(accessedTarget).value.longValue());
//            resultElem.setAveResponseTime(aveMap.get(accessedTarget).value.longValue());
//            resultElem.setMaxResponseTime(maxMap.get(accessedTarget).value.longValue());
//            resultElem.setMinResponseTime(minMap.get(accessedTarget).value.longValue());
//
//            result.add(resultElem);
//        }

        return result;
    }

    /**
     * ���X�g�f�[�^���A�n�񖼏́i�A�N�Z�X�Ώۃ��\�b�h�^URL)���L�[�Ƃ���}�b�v�`���̃f�[�^�ɕϊ�����B
     * 
     * @param list ���X�g�f�[�^
     * @return �ϊ���̃}�b�v�f�[�^
     */
    private Map<String, MeasurementValueDto> convertListToItemKeyMap(List<MeasurementValueDto> list)
    {
        Map<String, MeasurementValueDto> resultMap = new HashMap<String, MeasurementValueDto>();

        for (MeasurementValueDto elem : list)
        {
            resultMap.put(elem.measurementItemName, elem);
        }

        return resultMap;
    }

}
