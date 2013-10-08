/*
 * Copyright (c) 2004-2013 Acroquest Technology Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  Acroquest Technology Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.web.dashboard.controller;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.web.dashboard.dto.ResponseDto;
import jp.co.acroquest.endosnipe.web.dashboard.dto.SignalDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.dto.SummarySignalDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.entity.SummarySignalInfo;
import jp.co.acroquest.endosnipe.web.dashboard.manager.ResourceSender;
import jp.co.acroquest.endosnipe.web.dashboard.service.SignalService;
import jp.co.acroquest.endosnipe.web.dashboard.service.SummarySignalService;
import net.arnx.jsonic.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wgp.manager.WgpDataManager;

/**
 * /summarySignal/getAllDefinition
 * @author khinewai
 *
 */
@Controller
@RequestMapping("/summarySignal")
public class SummarySignalController
{
    /** Wgpのデータを管理するクラス。 */
    @Autowired
    protected WgpDataManager wgpDataManager_;

    /** リソースを送信するクラスのオブジェクト。 */
    @Autowired
    protected ResourceSender resourceSender_;

    @Autowired
    protected SummarySignalService summarySignalService_;

    @Autowired
    protected SignalService signalService_;

    public static ResponseDto responseData_ = null;

    public static List<SummarySignalDefinitionDto> summarySignalDefinitionDto_ =
            new ArrayList<SummarySignalDefinitionDto>();

    public static SummarySignalController summarySignal__;

    /**
    * コンストラクタ。
    */
    public SummarySignalController()
    {
        summarySignal__ = this;
    }

    /**
     * 機関データを取得する。
     *
     * @return 期間データ
     */
    @RequestMapping(value = "/getAllDefinition", method = RequestMethod.POST)
    @ResponseBody
    public List<SignalDefinitionDto> getTermData()
    {

        List<SignalDefinitionDto> summarySignalAllList;
        List<SignalDefinitionDto> signalAllList;
        signalAllList = signalService_.getAllSignal();
        summarySignalAllList = summarySignalService_.getAllSummarySignal();
        signalAllList.addAll(summarySignalAllList);
        return signalAllList;
    }

    /**
     * 機関データを取得する。
     *
     * @return 期間データ
     */
    @RequestMapping(value = "/getAllDefinitions", method = RequestMethod.POST)
    @ResponseBody
    public List<SummarySignalDefinitionDto> getAllDefinition()
    {

        List<SummarySignalDefinitionDto> summarySignalDefinitionDto =
                new ArrayList<SummarySignalDefinitionDto>();
        summarySignalService_.getAllSummarySignals();

        while (summarySignalDefinitionDto_.size() == 0)
        {
            //
        }
        summarySignalDefinitionDto.addAll(summarySignalDefinitionDto_);
        SummarySignalController.summarySignalDefinitionDto_.clear();
        return summarySignalDefinitionDto;

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto add(
            @RequestParam(value = "summarySignalDefinition") final String summarySignalDefinition)
    {
        ResponseDto responseDto = new ResponseDto();
        SummarySignalDefinitionDto summarySignalDefinitionDto =
                JSON.decode(summarySignalDefinition, SummarySignalDefinitionDto.class);
        this.summarySignalService_.insertSummarySignalDefinition(summarySignalDefinitionDto);

        while (responseData_ == null)
        {
            //
        }
        responseDto = responseData_;
        responseData_ = null;
        return responseDto;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto delete(
            @RequestParam(value = "summarySignalName") final String summarySignalName)
    {
        //        System.out.println(summarySignalNames);
        //        String summarySignalName =
        //                "/default/127.0.0.1/agent_000/process/thread/native/native summary signal";
        this.summarySignalService_.deleteSummarySignalDefinition(summarySignalName);
        ResponseDto responseDto = new ResponseDto();
        while (responseData_ == null)
        {
            //
        }
        responseDto = responseData_;
        responseData_ = null;
        // responseData_.setData(summarySignalDefinitionDto);
        //responseData_.setResult(ResponseConstants.RESULT_SUCCESS);
        return responseDto;

    }

    @RequestMapping(value = "/getDefinition", method = RequestMethod.POST)
    @ResponseBody
    public SummarySignalDefinitionDto get(
            @RequestParam(value = "summarySignalName") final String summarySignalName)

    {
        SummarySignalDefinitionDto summarySignalDefinition =
                this.summarySignalService_.getSummarySignalDefinition(summarySignalName);
        return summarySignalDefinition;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto edit(
            @RequestParam(value = "summarySignalDefinition") final String summarySignalDefinition)
    {
        ResponseDto responseDto = new ResponseDto();
        SummarySignalDefinitionDto summarySignalDefinitionDto =
                JSON.decode(summarySignalDefinition, SummarySignalDefinitionDto.class);

        long summarySignalId = summarySignalDefinitionDto.getSummarySignalId();
        String summarySignalName = summarySignalDefinitionDto.getSummarySignalName();
        SummarySignalInfo summarySignalInfo =
                this.summarySignalService_.convertSummarySignalInfo(summarySignalDefinitionDto);

        // DBに登録されている定義を更新する
        /*  SignalDefinitionDto updatedDefinitionDto =*/
        this.summarySignalService_.updateSummarySignalDefinition(summarySignalDefinitionDto);
        while (responseData_ == null)
        {
            //
        }
        responseDto = responseData_;
        responseData_ = null;
        return responseDto;
    }
}
