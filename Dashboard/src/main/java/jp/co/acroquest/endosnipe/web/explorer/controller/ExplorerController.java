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

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import jp.co.acroquest.endosnipe.communicator.CommunicationClient;
import jp.co.acroquest.endosnipe.web.explorer.manager.ConnectionClient;
import jp.co.acroquest.endosnipe.web.explorer.manager.EventManager;
import jp.co.acroquest.endosnipe.web.explorer.manager.ProfileSender;
import jp.co.acroquest.endosnipe.web.explorer.manager.ResourceSender;
import jp.co.acroquest.endosnipe.web.explorer.service.TreeMenuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.wgp.manager.WgpDataManager;

/**
 * Explorer用のコントローラークラスです。
 * 
 * @author miyasaka
 *
 */
@Controller
public class ExplorerController
{
    /** ツリーメニューに関する操作を行うクラスのオブジェクト。 */
    @Autowired
    protected TreeMenuService treeMenuService;

    /** WGPのデータを扱うクラスのオブジェクト。 */
    @Autowired
    protected WgpDataManager wgpDataManager;

    /** リソース送信クラスのオブジェクト。 */
    @Autowired
    protected ResourceSender resourceSender;

    /** メソッド情報送信クラスのオブジェクト */
    @Autowired
    protected ProfileSender profileSender;

    /**
     * コンストラクタ。
     */
    public ExplorerController()
    {

    }

    /**
     * Simply selects the home view to render by returning its name.
     * 
     * @param locale local情報
     * @param model モデル
     * @param request HTTPサーブレットリクエスト
     * @return 表示するjspファイルの名前
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView initialize(final Locale locale, final Model model,
            final HttpServletRequest request)
    {
        // TODO ServletContextから取得できないため、初期化時に設定する。
        EventManager eventManager = EventManager.getInstance();
        eventManager.setWgpDataManager(wgpDataManager);
        eventManager.setResourceSender(resourceSender);
        eventManager.setProfileSender(profileSender);

        ConnectionClient connectionClient = ConnectionClient.getInstance();
        List<CommunicationClient> clientList = connectionClient.getClientList();
        boolean isConnected = false;
        int connect = 0;
        for (int clientNumber = 0; clientNumber < clientList.size(); clientNumber++)
        {
            isConnected = clientList.get(clientNumber).isConnected();

            if (isConnected == true)
            {
                connect = 0;
            }
            else
            {
                connect = -1;
                break;
            }
        }
        ModelAndView modelAndView = new ModelAndView("Explorer");
        modelAndView.addObject("connect", connect);
        return modelAndView;
    }
}
