package jp.co.acroquest.endosnipe.report.dao.util;

import java.util.List;

import jp.co.acroquest.endosnipe.report.dao.util.JoinDataAccessUtil;
import jp.co.dgic.testing.framework.DJUnitTestCase;
import jp.co.acroquest.endosnipe.data.dao.MeasurementValueDao;
import jp.co.acroquest.endosnipe.data.dto.MeasurementValueDto;
import jp.co.acroquest.endosnipe.test.ReporterTestUtil;

/**
 * JoinDataAccessUtil�N���X�̃e�X�g�N���X
 * 
 * @author kimura
 */
public class JoinDataAccessUtilTest extends DJUnitTestCase
{

    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();
    }

    /**
     * �v���l�̕��ϒl���o�͂���e�X�g���\�b�h
     */
    public void testGetMearsumentValueAverageList()
    {
        String[] averageData =
                               new String[]{
                                       "13, 1, 2009/08/04 21:13:23, 31, 4, 600, turnAroundTime, ���X�|���X�^�C���̕��ϒl, jp.co.acroquest.gms.journal.JournalManager#onEvent",
                                       "39, 2, 2009/08/04 21:13:29, 31, 4, 75, turnAroundTime, ���X�|���X�^�C���̕��ϒl, jp.co.acroquest.gms.journal.JournalManager#onEvent",
                                       "61, 3, 2009/08/04 21:13:41, 31, 4, 570, turnAroundTime, ���X�|���X�^�C���̕��ϒl, jp.co.acroquest.gms.journal.JournalManager#onEvent",
                                       "17, 1, 2009/08/04 21:13:23, 31, 5, 600, turnAroundTime, ���X�|���X�^�C���̕��ϒl, jp.co.acroquest.gms.nameconv.NamingConverter#convert",
                                       "43, 2, 2009/08/04 21:13:29, 31, 5, 800, turnAroundTime, ���X�|���X�^�C���̕��ϒl, jp.co.acroquest.gms.nameconv.NamingConverter#convert",
                                       "5, 1, 2009/08/04 21:13:23, 31, 2, 610, turnAroundTime, ���X�|���X�^�C���̕��ϒl, jp.co.acroquest.gms.ncligw.buffer.DataBuffer#publish",
                                       "31, 2, 2009/08/04 21:13:29, 31, 2, 905, turnAroundTime, ���X�|���X�^�C���̕��ϒl, jp.co.acroquest.gms.ncligw.buffer.DataBuffer#publish",
                                       "53, 3, 2009/08/04 21:13:41, 31, 2, 120, turnAroundTime, ���X�|���X�^�C���̕��ϒl, jp.co.acroquest.gms.ncligw.buffer.DataBuffer#publish",
                                       "69, 4, 2009/08/04 21:13:47, 31, 2, 700, turnAroundTime, ���X�|���X�^�C���̕��ϒl, jp.co.acroquest.gms.ncligw.buffer.DataBuffer#publish",
                                       "9, 1, 2009/08/04 21:13:23, 31, 3, 900, turnAroundTime, ���X�|���X�^�C���̕��ϒl, jp.co.acroquest.gms.snmpgw.dataaccessor.GetRequestProcessor#process",
                                       "35, 2, 2009/08/04 21:13:29, 31, 3, 783, turnAroundTime, ���X�|���X�^�C���̕��ϒl, jp.co.acroquest.gms.snmpgw.dataaccessor.GetRequestProcessor#process",
                                       "57, 3, 2009/08/04 21:13:41, 31, 3, 180, turnAroundTime, ���X�|���X�^�C���̕��ϒl, jp.co.acroquest.gms.snmpgw.dataaccessor.GetRequestProcessor#process",
                                       "1, 1, 2009/08/04 21:13:23, 31, 1, 95, turnAroundTime, ���X�|���X�^�C���̕��ϒl, jp.co.acroquest.mc.event.io.ListIOStream#print",
                                       "27, 2, 2009/08/04 21:13:29, 31, 1, 50, turnAroundTime, ���X�|���X�^�C���̕��ϒl, jp.co.acroquest.mc.event.io.ListIOStream#print",
                                       "65, 4, 2009/08/04 21:13:47, 31, 1, 500, turnAroundTime, ���X�|���X�^�C���̕��ϒl, jp.co.acroquest.mc.event.io.ListIOStream#print"};

        String[] countData =
                             new String[]{
                                     "14, 1, 2009/08/04 21:13:23, 32, 9, 3, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.journal.JournalManager#onEvent",
                                     "40, 2, 2009/08/04 21:13:29, 32, 9, 10, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.journal.JournalManager#onEvent",
                                     "62, 3, 2009/08/04 21:13:41, 32, 9, 2, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.journal.JournalManager#onEvent",
                                     "18, 1, 2009/08/04 21:13:23, 32, 10, 1, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.nameconv.NamingConverter#convert",
                                     "44, 2, 2009/08/04 21:13:29, 32, 10, 1, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.nameconv.NamingConverter#convert",
                                     "6, 1, 2009/08/04 21:13:23, 32, 7, 5, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.ncligw.buffer.DataBuffer#publish",
                                     "32, 2, 2009/08/04 21:13:29, 32, 7, 4, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.ncligw.buffer.DataBuffer#publish",
                                     "54, 3, 2009/08/04 21:13:41, 32, 7, 8, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.ncligw.buffer.DataBuffer#publish",
                                     "70, 4, 2009/08/04 21:13:47, 32, 7, 1, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.ncligw.buffer.DataBuffer#publish",
                                     "10, 1, 2009/08/04 21:13:23, 32, 8, 2, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.snmpgw.dataaccessor.GetRequestProcessor#process",
                                     "36, 2, 2009/08/04 21:13:29, 32, 8, 8, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.snmpgw.dataaccessor.GetRequestProcessor#process",
                                     "58, 3, 2009/08/04 21:13:41, 32, 8, 4, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.snmpgw.dataaccessor.GetRequestProcessor#process",
                                     "2, 1, 2009/08/04 21:13:23, 32, 6, 7, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.mc.event.io.ListIOStream#print",
                                     "28, 2, 2009/08/04 21:13:29, 32, 6, 9, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.mc.event.io.ListIOStream#print",
                                     "66, 4, 2009/08/04 21:13:47, 32, 6, 4, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.mc.event.io.ListIOStream#print"};

        addReturnValue(MeasurementValueDao.class, "selectByTermAndMeasurementTypeWithName",
                       ReporterTestUtil.createMeasurementValueDtoEntities(averageData));
        addReturnValue(MeasurementValueDao.class, "selectByTermAndMeasurementTypeWithName",
                       ReporterTestUtil.createMeasurementValueDtoEntities(countData));

        List<?> totalCntList =
                               ReporterTestUtil.createMeasurementValueDtoEntities(new String[]{
                                       "62, 3, 2009/08/04 21:13:41, 32, 9, 15, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.journal.JournalManager#onEvent",
                                       "18, 1, 2009/08/04 21:13:23, 32, 10, 2, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.nameconv.NamingConverter#convert",
                                       "70, 4, 2009/08/04 21:13:47, 32, 7, 18, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.ncligw.buffer.DataBuffer#publish",
                                       "10, 1, 2009/08/04 21:13:23, 32, 8, 14, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.snmpgw.dataaccessor.GetRequestProcessor#process",
                                       "66, 4, 2009/08/04 21:13:47, 32, 6, 20, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.mc.event.io.ListIOStream#print"});

        List<?> averageList = null;

        try
        {
            averageList =
                          JoinDataAccessUtil.getMearsumentValueAverageList(
                                                                           "endosnipe",
                                                                           ReporterTestUtil.convertString2TimeStamp("2009/08/04 21:10:10"),
                                                                           ReporterTestUtil.convertString2TimeStamp("2009/08/04 21:20:10"),
                                                                           (List<MeasurementValueDto>)totalCntList);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }

        List<?> expectList =
                             ReporterTestUtil.createMeasurementValueDtoEntities(new String[]{
                                     "14, 1, 2009/08/04 21:13:23, 32, 9, 246, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.journal.JournalManager#onEvent",
                                     "18, 1, 2009/08/04 21:13:23, 32, 10, 700, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.nameconv.NamingConverter#convert",
                                     "6, 1, 2009/08/04 21:13:23, 32, 7, 462, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.ncligw.buffer.DataBuffer#publish",
                                     "10, 1, 2009/08/04 21:13:23, 32, 8, 627, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.gms.snmpgw.dataaccessor.GetRequestProcessor#process",
                                     "2, 1, 2009/08/04 21:13:23, 32, 6, 155, turnAroundTimeCount, ���X�|���X��, jp.co.acroquest.mc.event.io.ListIOStream#print",});

        ReporterTestUtil.assertMeasurementValueDto((List<Object>)expectList,
                                                   (List<Object>)averageList);

    }

}
