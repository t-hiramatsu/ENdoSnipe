/*
 * Copyright (c) 2004-2009 SMG Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  SMG Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.report;

import jp.co.acroquest.endosnipe.common.logger.AbstractPluginProvider;
import jp.co.acroquest.endosnipe.report.ReporterPluginProvider;

/**
 * ReportPluginに対応するPluginProvider。
 * 
 * @author M.Yoshida
 */
public class ReporterPluginProvider extends AbstractPluginProvider
{

    /** インスタンス保持用変数 */
    public static final ReporterPluginProvider INSTANCE   = new ReporterPluginProvider();

    /** クラス名称 */
    private static final String                CLASS_NAME =
                                                            "jp.co.acroquest.endosnipe.report.ReporterPlugin";

    /**
     * {@inheritDoc}
     */
    @Override
    protected ClassLoader getClassLoader()
    {
        return ReporterPluginProvider.class.getClassLoader();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getClassName()
    {
        return CLASS_NAME;
    }

}
