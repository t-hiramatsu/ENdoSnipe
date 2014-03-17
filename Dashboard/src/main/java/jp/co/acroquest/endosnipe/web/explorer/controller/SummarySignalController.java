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
package jp.co.acroquest.endosnipe.web.explorer.controller;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.web.explorer.dto.ResponseDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.SignalDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.SummarySignalDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.manager.ResourceSender;
import jp.co.acroquest.endosnipe.web.explorer.service.SignalService;
import jp.co.acroquest.endosnipe.web.explorer.service.SummarySignalService;
import net.arnx.jsonic.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wgp.manager.WgpDataManager;

/**
 * Controller of Summary Signal
 * @author pin
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

    /** service of signal*/
    @Autowired
    protected SignalService signalService_;

    /** controller of summary signal */
    public SummarySignalController summarySignal_;

    /**
    * constructor
    */
    public SummarySignalController()
    {
        summarySignal_ = this;
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
        summarySignalDefinitionDto = summarySignalService_.getAllSummarySignals();

        return summarySignalDefinitionDto;

    }

    /**
     * change the status of all summary signal
     *
     */
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    @ResponseBody
    public void changeStatus()
    {
        summarySignalService_.getAllSummarySignalDefinition(null);
    }

    /**
     * Add the new summarySignal.
     *
     * @param summarySignalDefinition
     *            JSON data of summarySignalDefinition
     * @return data of new summarySignalDefinition
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto add(
            @RequestParam(value = "summarySignalDefinition") final String summarySignalDefinition)
    {
        SummarySignalDefinitionDto summarySignalDefinitionDto =
                JSON.decode(summarySignalDefinition, SummarySignalDefinitionDto.class);

        //名前が重複するサマリシグナルを取得する
        SummarySignalDefinitionDto existDto =
                this.summarySignalService_.getSummarySignalDefinition(summarySignalDefinitionDto.summarySignalName_);

        //重複する場合はエラーメッセージをセットしてレスポンスを返す。
        if (existDto != null)
        {
            ResponseDto responseDto = new ResponseDto();
            responseDto.setResult("failure");
            responseDto.setMessage("\"" + summarySignalDefinitionDto.summarySignalName_
                    + "\" is already exists.");
            return responseDto;
        }

        //重複しない場合は電文を送信し、成功のレスポンスを返す。
        this.summarySignalService_.insertSummarySignalDefinition(summarySignalDefinitionDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResult("success");
        return responseDto;
    }

    /**
     * Delete the selected summarySignal.
     *
     * @param summarySignalName
     *            JSON name of summarySignal
     * @return data of deleted summarySignal
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto delete(
            @RequestParam(value = "summarySignalName") final String summarySignalName)
    {
        this.summarySignalService_.deleteSummarySignalDefinition(summarySignalName);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResult("success");
        return responseDto;

    }

    /**
     * Get the selected SummarySignal data.
     * 
     * @param summarySignalName : Name of SummarySignal
     * @return data of updated SummarySignal
     */
    @RequestMapping(value = "/getDefinition", method = RequestMethod.POST)
    @ResponseBody
    public SummarySignalDefinitionDto get(
            @RequestParam(value = "summarySignalName") final String summarySignalName)

    {
        SummarySignalDefinitionDto summarySignalDefinition =
                this.summarySignalService_.getSummarySignalDefinition(summarySignalName);
        return summarySignalDefinition;
    }

    /**
     * Edit the selected summarySignal.
     *
     * @param summarySignalDefinition
     *            JSON data of summarySignal
     * @return data of updated summarySignal
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto edit(
            @RequestParam(value = "summarySignalDefinition") final String summarySignalDefinition)
    {
        ResponseDto responseDto = new ResponseDto();
        SummarySignalDefinitionDto summarySignalDefinitionDto =
                JSON.decode(summarySignalDefinition, SummarySignalDefinitionDto.class);
        this.summarySignalService_.updateSummarySignalDefinition(summarySignalDefinitionDto);
        responseDto.setResult("success");
        return responseDto;
    }
}
