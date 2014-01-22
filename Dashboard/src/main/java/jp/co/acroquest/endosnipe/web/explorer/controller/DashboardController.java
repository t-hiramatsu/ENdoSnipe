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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import jp.co.acroquest.endosnipe.web.explorer.dto.ResponseDto;
import jp.co.acroquest.endosnipe.web.explorer.entity.DashboardInfo;
import jp.co.acroquest.endosnipe.web.explorer.form.DashboardListForm;
import jp.co.acroquest.endosnipe.web.explorer.manager.EventManager;
import jp.co.acroquest.endosnipe.web.explorer.manager.ResourceSender;
import jp.co.acroquest.endosnipe.web.explorer.service.DashboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wgp.manager.WgpDataManager;

/**
 * マップ機能のコントローラークラス。
 *
 * @author miyasaka
 *
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController
{
    /** WGPのデータを扱うクラスのオブジェクト。 */
    @Autowired
    protected WgpDataManager wgpDataManager;

    /** リソース送信クラスのオブジェクト。 */
    @Autowired
    protected ResourceSender resourceSender;

    @Autowired
    protected ServletContext servletContext;

    /** マップを表すID */
    private static final String MAP_DATA_ID = "map";

    /** 運用モード */
    private static final String OPERATE_MODE = "OPERATE";

    /** 背景画像パス */
    private static final String BACKGROUND_IMAGE_PATH = "resources/images/user";

    /** マップ機能のサービスクラス。 */
    @Autowired
    protected DashboardService dashboardService_;

    /**
     * コンストラクタ。
     */
    public DashboardController()
    {

    }

    /**
     * Get Map List.
     *
     * @param request HTTPサーブレットリクエスト
     * @param dashboardListForm マップ情報
     * @return 表示するjspファイルの名前
     */
    @RequestMapping(value = "/dashboardList")
    public String initializeMapList(final HttpServletRequest request,
            @ModelAttribute("dashboardListForm") final DashboardListForm dashboardListForm)
    {
        EventManager eventManager = EventManager.getInstance();
        eventManager.setWgpDataManager(wgpDataManager);
        eventManager.setResourceSender(resourceSender);

        // マップモードが設定されていない場合は運用モードを設定する。
        String mapMode = dashboardListForm.getMapMode();
        if (mapMode == null || mapMode.length() == 0)
        {
            dashboardListForm.setMapMode(OPERATE_MODE);
        }
        return "DashboardList";
    }

    /**
     * Get All map Data for Tree.
     *
     * @return 全てのマップ情報
     */
    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, List<Map<String, String>>> getAll()
    {
        Map<String, List<Map<String, String>>> resultMap =
                new HashMap<String, List<Map<String, String>>>();
        List<Map<String, String>> resultList = this.dashboardService_.getAllMap();
        resultMap.put(MAP_DATA_ID, resultList);
        return resultMap;
    }

    /**
     * Insert Map.
     *
     * @param data 登録するマップのデータ
     * @param name 登録するマップ名
     * @return 登録結果
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto insert(@RequestParam(value = "data") final String data,
            @RequestParam(value = "name") final String name)
    {
        DashboardInfo mapInfo = new DashboardInfo();
        mapInfo.data = data;
        mapInfo.name = name;
        return this.dashboardService_.insert(mapInfo);
    }

    /**
     * Update Map.
     *
     * @param mapId 更新するマップのID
     * @param data 更新するマップのデータ
     * @param name 更新するマップ名
     * @return 更新結果
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto update(@RequestParam(value = "mapId") final String mapId,
            @RequestParam(value = "data") final String data,
            @RequestParam(value = "name") final String name)
    {
        DashboardInfo dashboardInfo = new DashboardInfo();
        dashboardInfo.mapId = Long.valueOf(mapId);
        dashboardInfo.data = data;
        dashboardInfo.name = name;
        return this.dashboardService_.update(dashboardInfo);
    }

    /**
     * Get Map.
     *
     * @param mapId 取得するマップのID
     * @return 取得結果
     */
    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto getById(@RequestParam(value = "mapId") final String mapId)
    {
        return this.dashboardService_.getById(Long.valueOf(mapId));
    }

    /**
     * Remove Map
     * @param mapId Target remove mapId
     * @return 削除結果
     */
    @RequestMapping(value = "/removeById", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto removeById(@RequestParam(value = "mapId") final String mapId)
    {
        return this.dashboardService_.removeMapById(Long.valueOf(mapId));
    }

    /**
     * Get Background Image List.
     * @return 背景画像取得結果
     */
    @RequestMapping(value = "/getBackgroundImage", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto getBackgroundImageList()
    {
        String path = "resources/images/user";
        String directoryPath = servletContext.getRealPath(BACKGROUND_IMAGE_PATH);
        return this.dashboardService_.getImageList(directoryPath, BACKGROUND_IMAGE_PATH);
    }
}
