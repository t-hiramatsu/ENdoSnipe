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

    /** service of summary signal*/
    @Autowired
    protected SummarySignalService summarySignalService_;

    @Autowired
    protected SignalService signalService_;

    /**response data for summary signal data */
    public static ResponseDto responseData__ = null;

    /** response data for summary signal as the list*/
    public static List<SummarySignalDefinitionDto> summarySignalDefinitionDto__ =
            new ArrayList<SummarySignalDefinitionDto>();

    /** controller of summary signal */
    public SummarySignalController summarySignal__;

    /**
    * constructor
    */
    public SummarySignalController()
    {
        summarySignal__ = this;
    }

    /**
     * get all data of signal data and summary signal data to display in List box
     *
     * @return List<SignalDefinitionDto> data of signals
     */
    @RequestMapping(value = "/getAllDefinition", method = RequestMethod.POST)
    @ResponseBody
    public List<SignalDefinitionDto> getAllSummarySigAndSigData()
    {

        List<SignalDefinitionDto> summarySignalAllList;
        List<SignalDefinitionDto> signalAllList;
        signalAllList = signalService_.getAllSignal();
        summarySignalAllList = summarySignalService_.getAllSummarySignal();
        signalAllList.addAll(summarySignalAllList);
        return signalAllList;
    }

    /**
     * get all data of summary signals to show on the tree node
     *
     * @return  List<SummarySignalDefinitionDto> summary signal list
     */
    @RequestMapping(value = "/getAllDefinitions", method = RequestMethod.POST)
    @ResponseBody
    public List<SummarySignalDefinitionDto> getAllDefinition()
    {

        List<SummarySignalDefinitionDto> summarySignalDefinitionDto =
                new ArrayList<SummarySignalDefinitionDto>();
        summarySignalService_.getAllSummarySignals();

        while (summarySignalDefinitionDto__.size() == 0)
        {
            //
        }

        summarySignalDefinitionDto.addAll(summarySignalDefinitionDto__);
        SummarySignalController.summarySignalDefinitionDto__.clear();
        System.out.println(summarySignalDefinitionDto.size());
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

        while (responseData__ == null)
        {
            //
        }
        responseDto = responseData__;
        responseData__ = null;
        return responseDto;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto delete(
            @RequestParam(value = "summarySignalName") final String summarySignalName)
    {
        this.summarySignalService_.deleteSummarySignalDefinition(summarySignalName);
        ResponseDto responseDto = new ResponseDto();
        while (responseData__ == null)
        {
            //
        }
        responseDto = responseData__;
        responseData__ = null;
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

        this.summarySignalService_.updateSummarySignalDefinition(summarySignalDefinitionDto);
        while (responseData__ == null)
        {
            //
        }
        responseDto = responseData__;
        responseData__ = null;
        return responseDto;
    }
}
