/*
 * Copyright (c) 2004-2015 Acroquest Technology Co., Ltd. All Rights Reserved.
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

import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import jp.co.acroquest.endosnipe.web.explorer.service.JvnFileDownloadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * JVNファイルダウンロード機能のコントローラクラス
 * @author kawasaki
 *
 */
@Controller
@RequestMapping("/jvnFileDownload")
public class JvnFileDownloadController
{
    /** 日付のフォーマット。 */
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    /**
     * connection with threadDumpService
     */
    @Autowired
    protected JvnFileDownloadService jvnFileDownloadService_;

    /**
     * デフォルトコンストラクタ
     */
    public JvnFileDownloadController()
    {

    }

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public void download(@RequestParam(value = "start") final String start,
            @RequestParam(value = "end") final String end, final HttpServletResponse res)
        throws Exception
    {
        String fileName = "jvn_" + start + "_" + end + ".zip";
        res.setContentType("application/octet-stream;charset=UTF8");
        res.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        res.setHeader("Content-Transfer-Encoding", "binary");

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Timestamp startTime = new Timestamp(dateFormat.parse(start).getTime());
        Timestamp endTime = new Timestamp(dateFormat.parse(end).getTime());

        OutputStream os = res.getOutputStream();
        this.jvnFileDownloadService_.writeFile(os, startTime, endTime);
    }
}
