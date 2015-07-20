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
package jp.co.acroquest.endosnipe.web.explorer.service;

import static jp.co.acroquest.endosnipe.web.explorer.util.CsvUtil.CSV_DELIMITER;
import static jp.co.acroquest.endosnipe.web.explorer.util.CsvUtil.CSV_LINE;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.communicator.CommunicationClient;
import jp.co.acroquest.endosnipe.communicator.TelegramCreator;
import jp.co.acroquest.endosnipe.communicator.TelegramCreator.UpdateInvocationParam;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.web.explorer.dto.MethodModelDto;
import jp.co.acroquest.endosnipe.web.explorer.entity.ClassModel;
import jp.co.acroquest.endosnipe.web.explorer.entity.InvocationInfo;
import jp.co.acroquest.endosnipe.web.explorer.entity.MethodModel;
import jp.co.acroquest.endosnipe.web.explorer.manager.ConnectionClient;
import jp.co.acroquest.endosnipe.web.explorer.util.CsvUtil;

import org.springframework.stereotype.Service;

/**
 * プロファイラ機能のサービスクラス
 * 
 * @author hiramatsu
 */
@Service
public class ProfilerService
{
    /**
     * デフォルトコンストラクタ
     */
    public ProfilerService()
    {

    }

    /**
     * 状態取得要求電文を作成し、送信する。
     * 
     * @param agentName 電文送信先Javelinのエージェント名
     */
    public void getStatus(final String agentName)
    {
        Telegram telegram = TelegramCreator.createProfileGetTelegram(agentName);
        ConnectionClient connectionClient = ConnectionClient.getInstance();
        List<CommunicationClient> clientList = connectionClient.getClientList();
        for (CommunicationClient communicationClient : clientList)
        {
            communicationClient.sendTelegram(telegram);
        }
    }

    /**
     * リセット要求電文を作成し、送信する。
     * 
     * @param agentName 電文送信先Javelinのエージェント名
     */
    public void resetProfile(final String agentName)
    {
        Telegram telegram = TelegramCreator.createProfileResetTelegram(agentName);
        ConnectionClient connectionClient = ConnectionClient.getInstance();
        List<CommunicationClient> clientList = connectionClient.getClientList();
        for (CommunicationClient communicationClient : clientList)
        {
            communicationClient.sendTelegram(telegram);
        }
    }

    /**
     * 計測対象更新要求電文を作成し、送信する。
     * 
     * @param agentName 電文送信先Javelinのエージェント名
     * @param invocations 更新する計測対象メソッドのリスト
     */
    public void updateTarget(final String agentName, final List<InvocationInfo> invocations)
    {
        List<UpdateInvocationParam> invocationParamList = new ArrayList<UpdateInvocationParam>();
        for (InvocationInfo invocation : invocations)
        {
            UpdateInvocationParam invocationParam =
                    new UpdateInvocationParam(invocation.getClassName(),
                                              invocation.getMethodName(),
                                              invocation.isResponseGraphOutput(),
                                              invocation.isTarget(),
                                              invocation.getAlarmThreshold(),
                                              invocation.getAlarmCpuThreshold());
            invocationParamList.add(invocationParam);
        }
        ConnectionClient connectionClient = ConnectionClient.getInstance();
        List<CommunicationClient> clientList = connectionClient.getClientList();
        for (CommunicationClient communicationClient : clientList)
        {
            Telegram telegram =
                    TelegramCreator.createUpdateInvocationTelegram(invocationParamList.toArray(new UpdateInvocationParam[invocationParamList.size()]),
                                                                   agentName);
            communicationClient.sendTelegram(telegram);
        }
    }

    /**
     * ProfilerデータからCSVのデータを作成する。
     * @param classModels Profilerのデータ
     * @return CSVデータ
     */
    public String createCsvData(final ClassModel[] classModels)
    {
        StringBuilder builder = new StringBuilder();
        appendCsvHeader(builder);
        if (classModels == null)
        {
            return builder.toString();
        }

        for (ClassModel classModel : classModels)
        {
            for (MethodModel methodModel : classModel.getMethods())
            {
                MethodModelDto methodModelDto = methodModel.convertToDto();
                appendCsvLine(builder, methodModelDto);
            }
        }
        return builder.toString();
    }

    /**
     * CSVヘッダを設定する。
     * @param builder {@link StringBuilder}
     */
    private void appendCsvHeader(final StringBuilder builder)
    {
        builder.append("Response");
        builder.append(CSV_DELIMITER);
        builder.append("Target");
        builder.append(CSV_DELIMITER);
        builder.append("Class Name");
        builder.append(CSV_DELIMITER);
        builder.append("Method Name");
        builder.append(CSV_DELIMITER);
        builder.append("TAT Threshold");
        builder.append(CSV_DELIMITER);
        builder.append("CPU Threshold");
        builder.append(CSV_DELIMITER);
        builder.append("Call Count");
        builder.append(CSV_DELIMITER);
        builder.append("TotalTime");
        builder.append(CSV_DELIMITER);
        builder.append("AverageTime");
        builder.append(CSV_DELIMITER);
        builder.append("MaximumTime");
        builder.append(CSV_DELIMITER);
        builder.append("MinimumTime");
        builder.append(CSV_DELIMITER);
        builder.append("TotalCPUTime");
        builder.append(CSV_DELIMITER);
        builder.append("AverageCPUTime");
        builder.append(CSV_DELIMITER);
        builder.append("MaximumCPUTime");
        builder.append(CSV_DELIMITER);
        builder.append("MinimumCPUTime");
        builder.append(CSV_DELIMITER);
        builder.append("TotalUserTime");
        builder.append(CSV_DELIMITER);
        builder.append("AverageUserTime");
        builder.append(CSV_DELIMITER);
        builder.append("MaximumUserTime");
        builder.append(CSV_DELIMITER);
        builder.append("MinimumUserTime");
        builder.append(CSV_DELIMITER);
        builder.append("ThrowableCount");
        builder.append(CSV_LINE);
    }

    /**
     * メソッドデータからCSVの行データを設定する。
     * @param methodModelDto メソッドデータ
     * @param builder {@link StringBuilder}
     */
    private void appendCsvLine(final StringBuilder builder, final MethodModelDto methodModelDto)
    {
        builder.append(methodModelDto.getTransactionGraph());
        builder.append(CSV_DELIMITER);
        builder.append(methodModelDto.getTarget());
        builder.append(CSV_DELIMITER);
        builder.append(CsvUtil.escaped(methodModelDto.getClassName()));
        builder.append(CSV_DELIMITER);
        builder.append(CsvUtil.escaped(methodModelDto.getMethodName()));
        builder.append(CSV_DELIMITER);
        builder.append(methodModelDto.getAlarmThreshold());
        builder.append(CSV_DELIMITER);
        builder.append(methodModelDto.getAlarmCpuThreshold());
        builder.append(CSV_DELIMITER);
        builder.append(methodModelDto.getCallCount());
        builder.append(CSV_DELIMITER);
        builder.append(methodModelDto.getTotal());
        builder.append(CSV_DELIMITER);
        builder.append(methodModelDto.getAverage());
        builder.append(CSV_DELIMITER);
        builder.append(methodModelDto.getMaximum());
        builder.append(CSV_DELIMITER);
        builder.append(methodModelDto.getMinimum());
        builder.append(CSV_DELIMITER);
        builder.append(methodModelDto.getCpuTotal());
        builder.append(CSV_DELIMITER);
        builder.append(methodModelDto.getCpuAverage());
        builder.append(CSV_DELIMITER);
        builder.append(methodModelDto.getCpuMaximum());
        builder.append(CSV_DELIMITER);
        builder.append(methodModelDto.getCpuMinimum());
        builder.append(CSV_DELIMITER);
        builder.append(methodModelDto.getUserTotal());
        builder.append(CSV_DELIMITER);
        builder.append(methodModelDto.getUserAverage());
        builder.append(CSV_DELIMITER);
        builder.append(methodModelDto.getUserMaximum());
        builder.append(CSV_DELIMITER);
        builder.append(methodModelDto.getUserMinimum());
        builder.append(CSV_DELIMITER);
        builder.append(methodModelDto.getThrowableCount());
        builder.append(CSV_LINE);
    }

}
