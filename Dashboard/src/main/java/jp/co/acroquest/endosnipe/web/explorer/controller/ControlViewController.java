/*
 * Copyright (c) 2004-2014 Acroquest Technology Co., Ltd. All Rights Reserved.
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

import java.util.Arrays;

import jp.co.acroquest.endosnipe.communicator.PropertyEntry;
import jp.co.acroquest.endosnipe.web.explorer.service.ControllerService;
import net.arnx.jsonic.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * コントロール機能のコントローラクラス
 * 
 * @author hiramatsu
 */
@Controller
@RequestMapping("/propertyController")
public class ControlViewController
{
    /** コントロール機能用サービス */
    @Autowired
    protected ControllerService controllerService_;

    /**
     * デフォルトコンストラクタ
     */
    public ControlViewController()
    {

    }

    /**
     * 「再ロード」
     * 
     * @param agentName 操作対象のエージェント名
     */
    @RequestMapping(value = "/reload", method = RequestMethod.POST)
    @ResponseBody
    public void reload(@RequestParam(value = "agentName") final String agentName)
    {
        controllerService_.getProperty(agentName);
    }

    /**
     * 「更新」
     * 
     * @param agentName 通知対象エージェント名
     * @param propertyList 更新する設定情報
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(@RequestParam(value = "agentName") final String agentName,
            @RequestParam(value = "propertyList") final String propertyList)
    {
        PropertyEntry[] propertyArray = JSON.decode(propertyList, PropertyEntry[].class);
        controllerService_.updateProperty(Arrays.asList(propertyArray), agentName);
    }
}
