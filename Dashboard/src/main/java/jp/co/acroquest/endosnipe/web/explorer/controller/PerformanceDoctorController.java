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
package jp.co.acroquest.endosnipe.web.explorer.controller;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.data.dao.JavelinLogDao;
import jp.co.acroquest.endosnipe.data.dao.PerfDoctorResultDao;
import jp.co.acroquest.endosnipe.data.dto.PerfDoctorResultDto;
import jp.co.acroquest.endosnipe.data.entity.JavelinLog;
import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;
import jp.co.acroquest.endosnipe.web.explorer.config.ConfigurationReader;
import jp.co.acroquest.endosnipe.web.explorer.dto.PerfDoctorTableDto;
import jp.co.acroquest.endosnipe.web.explorer.form.PerfDoctorTermDataForm;
import jp.co.acroquest.endosnipe.web.explorer.manager.DatabaseManager;
import jp.co.acroquest.endosnipe.web.explorer.service.JvnFileEntryJudge;
import jp.co.acroquest.endosnipe.web.explorer.service.PerformanceDoctorService;
import net.arnx.jsonic.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * PerformanceDoctorControllerクラス
 * 
 * @author acroquest
 * 
 */
@Controller
@RequestMapping("/performanceDoctor")
public class PerformanceDoctorController
{
    /** PerformanceDoctorページのID。 */
    private static final String PERFDOCTOR_POSTFIX_ID = "/performanceDoctor";

    /** Performance Doctor用サービス */
    @Autowired
    protected PerformanceDoctorService perfDoctorService_;

    /** ロガー */
    private static final ENdoSnipeLogger LOGGER =
            ENdoSnipeLogger.getLogger(ConfigurationReader.class);

    /**
     * コンストラクタ
     */
    public PerformanceDoctorController()
    {

    }

    /**
     * PerformanceDoctorで表示するための期間データを返す。
     * 
     * @param data
     *            取得する期間データの定義
     * @return PerformanceDoctorにおける期間データ
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/getPerfDoctor", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, List<PerfDoctorTableDto>> getTermPerfDoctorData(
            @RequestParam(value = "data") final String data)
    {
        Map<String, List<PerfDoctorTableDto>> responceDataList =
                new HashMap<String, List<PerfDoctorTableDto>>();
        DatabaseManager dbManager = DatabaseManager.getInstance();
        String dbName = dbManager.getDataBaseName(1);

        PerfDoctorTermDataForm termDataForm = JSON.decode(data, PerfDoctorTermDataForm.class);
        List<String> dataGroupIdList = termDataForm.getDataGroupIdList();

        for (String dataGroupId : dataGroupIdList)
        {
            List<JavelinLog> list = new ArrayList<JavelinLog>();
            Timestamp start = new Timestamp(Long.valueOf(termDataForm.getStartTime()));
            Timestamp end = new Timestamp(Long.valueOf(termDataForm.getEndTime()));

            // 未診断のログを選択。選択したログは既診断に変更。
            try
            {
                list =
                        JavelinLogDao.selectByTermAndName(dbName, start, end, dataGroupId, true,
                                                          true);
                JavelinLogDao.updateDiagnosed(dbName, start, end, dataGroupId);
            }
            catch (SQLException e)
            {
                LOGGER.log(e);
            }

            // PerformanceDoctorでJavelinLogを診断する。
            JvnFileEntryJudge judge = new JvnFileEntryJudge();
            List<WarningUnit> warnings = judge.judge(list, true, true);
            // 診断結果をPerfDoctorResultTableに登録するべく、Dtoに変換
            List<PerfDoctorResultDto> dtoList = new ArrayList<PerfDoctorResultDto>();

            for (WarningUnit warning : warnings)
            {
                PerfDoctorResultDto dto = new PerfDoctorResultDto();
                dto.setOccurrenceTime(new Timestamp(warning.getStartTime()));
                dto.setDescription(warning.getDescription());
                dto.setLevel(warning.getLevel());
                dto.setClassName(warning.getClassName());
                dto.setMethodName(warning.getMethodName());
                dto.setLogFileName(warning.getLogFileName());
                dto.setMeasurementItemName(warning.getMeasurementItemName());
                dtoList.add(dto);
            }
            // 診断結果をPerfDoctorResultTableに登録
            //            PerfDoctorResultDao.insert(dbName, dtoList);
            for (PerfDoctorResultDto dto : dtoList)
            {
                try
                {
                    PerfDoctorResultDao.insert(dbName, dto);
                }
                catch (SQLException ex)
                {
                    LOGGER.log(ex);
                }
            }
            // PerfDoctorResultTableから条件に合致する診断結果を取得（取得時にソートしておく）
            // 各要素をPerformanceDoctorTableDtoに変換して返す。
            List<PerfDoctorResultDto> resultList;
            List<PerfDoctorTableDto> displayList = new ArrayList<PerfDoctorTableDto>();

            try
            {
                resultList =
                        PerfDoctorResultDao.selectByTermAndName(dbName, start, end, dataGroupId);
                for (PerfDoctorResultDto resultDto : resultList)
                {
                    PerfDoctorTableDto tableDto = new PerfDoctorTableDto();
                    tableDto.setClassName(resultDto.getMethodName());
                    tableDto.setMethodName(resultDto.getClassName());
                    tableDto.setDate(resultDto.getOccurrenceTime().toString());
                    tableDto.setDescription(resultDto.getDescription());
                    tableDto.setLevel(resultDto.getLevel());
                    tableDto.setLogFileName(resultDto.getLogFileName());
                    tableDto.setDetailResult(this.perfDoctorService_.getPerfDoctorDetailData(tableDto.getLogFileName()));
                    displayList.add(tableDto);
                }
            }
            catch (SQLException ex)
            {
                LOGGER.log(ex);
            }
            list.clear();
            responceDataList.put(dataGroupId + PERFDOCTOR_POSTFIX_ID, displayList);
        }
        return responceDataList;
    }

    /**
     * ログファイルをダウンロードする。
     * 
     * @param res
     *            {@link HttpServletResponse}オブジェクト
     * @param fileName
     *            ログファイル名
     */
    @RequestMapping(value = "/download", method = RequestMethod.POST)
    @ResponseBody
    public void downloadJvnLog(final HttpServletResponse res,
            @RequestParam(value = "fileName") final String fileName)
    {
        this.perfDoctorService_.doRequest(res, fileName);
    }

}
