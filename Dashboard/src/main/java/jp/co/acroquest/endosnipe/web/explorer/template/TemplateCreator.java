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
package jp.co.acroquest.endosnipe.web.explorer.template;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.web.explorer.dto.MultipleResourceGraphDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.SignalDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.SummarySignalDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.entity.DashboardInfo;
import jp.co.acroquest.endosnipe.web.explorer.entity.MultipleResourceGraphInfo;
import jp.co.acroquest.endosnipe.web.explorer.entity.SignalInfo;
import jp.co.acroquest.endosnipe.web.explorer.service.DashboardService;
import jp.co.acroquest.endosnipe.web.explorer.service.MultipleResourceGraphService;
import jp.co.acroquest.endosnipe.web.explorer.service.SignalService;
import jp.co.acroquest.endosnipe.web.explorer.service.SummarySignalService;
import jp.co.acroquest.endosnipe.web.explorer.template.converter.ResourceConvertUtil;
import jp.co.acroquest.endosnipe.web.explorer.template.converter.TemplateConvertUtil;
import jp.co.acroquest.endosnipe.web.explorer.template.meta.Property;
import jp.co.acroquest.endosnipe.web.explorer.template.meta.Resource;
import jp.co.acroquest.endosnipe.web.explorer.template.meta.Template;
import net.arnx.jsonic.JSON;

/**
 * テンプレートを生成するクラス
 * @author kamo
 *
 */
public class TemplateCreator
{

    private static final String BLANK = "";

    private static final long DASHBOARD_ID = -1;

    private static final Timestamp DASHBOARD_LAST_UPDATE = null;

    private static final long GRAPH_ID = 0;

    private static final long SIGNAL_ID = 0;

    private final MultipleResourceGraphService graphService;

    private final DashboardService dashboardService;

    private final SignalService signalService;

    private final SummarySignalService summarySignalService;

    public TemplateCreator(final MultipleResourceGraphService graphService,
            final DashboardService dashboardService, final SignalService signalService,
            final SummarySignalService summarySignalService)
    {
        this.graphService = graphService;
        this.dashboardService = dashboardService;
        this.signalService = signalService;
        this.summarySignalService = summarySignalService;
    }

    /**
     * テンプレートを生成する
     * @param name テンプレートの名前
     * @param template テンプレートのデータ
     */
    public void create(final String name, final Template template)
    {
        List<Resource> resources = template.getResources();

        for (Resource resource : resources)
        {
            //グラフの作成
            if (Resource.OBJ_NAME_GRAPH.equals(resource.getObjectName()))
            {
                createGraph(name, resource.getProperty());
                continue;
            }

            //シグナルの作成
            if (Resource.OBJ_NAME_SIGNAL.equals(resource.getObjectName()))
            {
                createSignal(name, resource.getProperty());
                continue;
            }
        }

        //テンプレート→Map→JSON変換
        Map<String, Object> templateMap = TemplateConvertUtil.convert(name, template);
        String json = JSON.encode(templateMap);

        //DashboardをDBに登録
        DashboardInfo dashboardInfo = new DashboardInfo();
        dashboardInfo.dashboardId = DASHBOARD_ID;
        dashboardInfo.data = json;
        dashboardInfo.lastUpdate = DASHBOARD_LAST_UPDATE;
        dashboardInfo.name = name;
        List<DashboardInfo> existDashboard = dashboardService.getByName(name);
        if (existDashboard.size() == 0)
        {
            dashboardService.insert(dashboardInfo);
        }
        else
        {
            dashboardService.update(dashboardInfo, true);
        }
    }

    /**
     * テンプレートで定義したグラフを作成する
     * @param name テンプレートの名前
     * @param property グラフのプロパティ
     */
    private void createGraph(final String name, final Property property)
    {
        if (!BLANK.equals(property.getResourceId()))
        {
            return;
        }

        MultipleResourceGraphInfo graphInfo = new MultipleResourceGraphInfo();
        graphInfo.measurementItemIdList_ = BLANK;
        graphInfo.measurementItemPattern_ = property.getTarget();
        graphInfo.multipleResourceGraphId_ = GRAPH_ID;
        graphInfo.multipleResourceGraphName_ = ResourceConvertUtil.getTreeName(name, property);

        MultipleResourceGraphDefinitionDto existDto =
                graphService.getmultipleResourceGraphInfo(graphInfo.multipleResourceGraphName_);
        if (existDto == null)
        {
            graphService.insertMultipleResourceGraphInfo(graphInfo);
        }
        else
        {
            graphService.updatemultipleResourceGraphInfo(graphInfo);
        }

    }

    private void createSignal(final String name, final Property property)
    {
        Property signalProperty = property.getSignal();
        if (!BLANK.equals(signalProperty.getResourceId()))
        {
            return;
        }
        String treeName = ResourceConvertUtil.getTreeName(name, signalProperty);

        SummarySignalDefinitionDto summaryExistDto =
                summarySignalService.getSummarySignalDefinition(treeName);
        if (summaryExistDto == null)
        {
            //TODO: サマリシグナルの場合はDBの書き換えを行わない
            return;
        }

        SignalInfo signalInfo = new SignalInfo();
        signalInfo.escalationPeriod = signalProperty.getPeriod() * 1000;
        signalInfo.level = signalProperty.getLevel();
        signalInfo.matchingPattern = signalProperty.getTarget();
        signalInfo.patternValue = signalProperty.getThreshold();
        signalInfo.signalId = SIGNAL_ID;
        signalInfo.signalName = treeName;

        SignalDefinitionDto existDto = signalService.getSignalInfo(signalInfo.signalName);

        if (existDto == null)
        {
            signalService.insertSignalInfo(signalInfo);
        }
        else
        {
            signalInfo.signalId = existDto.getSignalId();
            signalService.updateSignalInfo(signalInfo);
        }
    }
}
