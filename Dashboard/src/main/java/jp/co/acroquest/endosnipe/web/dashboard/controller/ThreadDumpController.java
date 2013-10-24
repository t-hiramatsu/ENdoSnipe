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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.web.dashboard.dto.ThreadDumpDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.form.TermDataForm;
import jp.co.acroquest.endosnipe.web.dashboard.service.ThreadDumpService;
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
 * 
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

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/getThreadDump", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, List<ThreadDumpDefinitionDto>> getThreadDumpData(
            @RequestParam(value = "data") final String data)
    {
        Map<String, List<ThreadDumpDefinitionDto>> responceDataList =
                new HashMap<String, List<ThreadDumpDefinitionDto>>();

        TermDataForm termDataForm = JSON.decode(data, TermDataForm.class);
        Map<String, List<ThreadDumpDefinitionDto>> responseDataList =
                threadDumpService_.getTermThreadDumpData(termDataForm);
        return responseDataList;
    }

    @RequestMapping(value = "/addThreadDump", method = RequestMethod.POST)
    @ResponseBody
    public void addThreadDumpData(@RequestParam(value = "threadDump") final String data)
    {
        threadDumpService_.createThreadDump();
    }
}
