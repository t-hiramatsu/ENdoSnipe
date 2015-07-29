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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jp.co.acroquest.endosnipe.web.explorer.dto.JvnFileSearchResultDto;
import jp.co.acroquest.endosnipe.web.explorer.service.JvnFileDownloadService;
import net.arnx.jsonic.JSON;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    /** ファイルダウンロード用の日付のフォーマット。 */
    private static final String FILE_DATE_FORMAT = "yyyyMMddHHmmss";

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

    /**
     * JVNログファイルを検索する。
     * @param start 開始時刻
     * @param end 終了時刻
     * @param agentName エージェント名
     * @param res レスポンス
     * @return Jvnファイル一覧
     * @throws Exception 例外が発生した場合
     */
    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<JvnFileSearchResultDto> search(
            @RequestParam(value = "agentName") final String agentName,
            @RequestParam(value = "start", required = false) final String start,
            @RequestParam(value = "end", required = false) final String end,
            final HttpServletResponse res)
        throws Exception
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        Timestamp startTime = null;
        Timestamp endTime = null;

        if (!StringUtils.isEmpty(start))
        {
            startTime = new Timestamp(dateFormat.parse(start).getTime());
        }

        if (!StringUtils.isEmpty(end))
        {
            endTime = new Timestamp(dateFormat.parse(end).getTime());
        }

        return this.jvnFileDownloadService_.getJavelinLog(startTime, endTime, agentName);
    }

    /**
     * Jvnファイルをダウンロードする。
     * @param logIds ログID（JSON）
     * @param res レスポンス
     * @throws Exception 例外が発生した場合
     */
    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public void download(@RequestParam(value = "logIds") final String logIds,
            final HttpServletResponse res)
        throws Exception
    {
        List<String> logIdStrList = JSON.decode(logIds);
        List<Long> logIdList = new ArrayList<Long>();
        for (String logIdStr : logIdStrList)
        {
            logIdList.add(Long.valueOf(logIdStr));
        }
        DateFormat dateFormat = new SimpleDateFormat(FILE_DATE_FORMAT);
        String fileName = "jvn_" + dateFormat.format(new Date()) + ".zip";
        res.setContentType("application/zip;charset=UTF8");
        res.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        res.setHeader("Content-Transfer-Encoding", "binary");

        OutputStream os = res.getOutputStream();
        this.jvnFileDownloadService_.writeFile(os, logIdList);
    }
}
