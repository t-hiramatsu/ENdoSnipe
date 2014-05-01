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
import java.util.Map;

import jp.co.acroquest.endosnipe.web.explorer.dto.ThreadDumpDto;
import jp.co.acroquest.endosnipe.web.explorer.form.TermDataForm;
import jp.co.acroquest.endosnipe.web.explorer.service.ThreadDumpService;
import net.arnx.jsonic.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This is controller class for
 * threadDump
 * @author khinewai
 *
 */
@Controller
@RequestMapping("/threadDump")
public class ThreadDumpController
{
    /**
     * this is default constructor class
     */
    public ThreadDumpController()
    {

    }

    /**
     * connection with threadDumpService
     */
    @Autowired
    protected ThreadDumpService threadDumpService_;

    /**
     *  This mapping is work when user click  tree node
     * 
     * @param data get threadDump data from client side
     * @return threadDump data get from dataBase
     */
    @RequestMapping(value = "/getThreadDump", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, List<ThreadDumpDto>> getThreadDumpData(
            @RequestParam(value = "data") final String data)
    {
        TermDataForm termDataForm = JSON.decode(data, TermDataForm.class);
        Map<String, List<ThreadDumpDto>> responseDataList =
                threadDumpService_.getTermThreadDumpData(termDataForm);
        return responseDataList;
    }

    /**
     * This mapping is work when user click 
     * "get Thread Dump " button
     * @param agentName is for the selected tree path
     */
    @RequestMapping(value = "/addThreadDump", method = RequestMethod.POST)
    @ResponseBody
    public void addThreadDumpData(@RequestParam(value = "threadDump") final String agentName)
    {
        threadDumpService_.createThreadDump(agentName);
    }

    /**
     * This mapping is work when user get 
     * notifying message from telegram
     * @param data is agent name get from client side
     * @return threadDump data related with agent
     */
    @RequestMapping(value = "/selectAllAgent", method = RequestMethod.POST)
    @ResponseBody
    public List<ThreadDumpDto> getAllAgent(@RequestParam(value = "threadDump") final String data)
    {
        List<ThreadDumpDto> result = new ArrayList<ThreadDumpDto>();
        result = threadDumpService_.getAllAgentData(data);
        return result;
    }
}
