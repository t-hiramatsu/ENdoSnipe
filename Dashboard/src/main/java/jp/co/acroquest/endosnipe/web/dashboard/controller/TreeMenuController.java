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

import java.util.List;

import jp.co.acroquest.endosnipe.web.dashboard.manager.ResourceSender;
import jp.co.acroquest.endosnipe.web.dashboard.service.TreeMenuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wgp.manager.WgpDataManager;

/**
 * ツリーメニューのコントローラクラス。
 * 
 * @author miyasaka
 *
 */
@Controller
@RequestMapping("/tree")
public class TreeMenuController
{
    /** Wgpのデータを管理するクラス。 */
    @Autowired
    protected WgpDataManager wgpDataManager_;

    /** リソースを送信するクラスのオブジェクト。 */
    @Autowired
    protected ResourceSender resourceSender_;

    /** ツリーメニューに関する操作を行うクラスのオブジェクト。 */
    @Autowired
    protected TreeMenuService treeMenuService;

    /**
     * コンストラクタ。
     */
    public TreeMenuController()
    {

    }

    /**
     * 指定された親ノードの直下にある子要素のパスの一覧を取得する。
     *
     * @param parentTreeId
     *            親ノードのID
     * @return 指定された親ノードの直下にある子要素のパスの一覧
     */
    @RequestMapping(value = "/getDirectlChildNode", method = RequestMethod.POST)
    @ResponseBody
    public List<String> getDirectlChildNodes(
            @RequestParam(value = "parentTreeId") final String parentTreeId)
    {
        List<String> menuDtos = this.treeMenuService.getDirectChildNodes(parentTreeId);

        return menuDtos;
    }
}
