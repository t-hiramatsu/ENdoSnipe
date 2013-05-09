package jp.co.acroquest.endosnipe.report;

import jp.co.acroquest.endosnipe.report.ReporterPlugin;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * ENdoSnipeReporterをReportPluginとして使用するためのクラス
 * 
 * @author kimura
 */
public class ReporterPlugin extends AbstractUIPlugin
{

    /** プラグインID */
    public static final String    PLUGIN_ID = "ENdoSnipeReport";

    /** プラグインの参照先 */
    private static ReporterPlugin plugin__;

    /**
     * The constructor
     */
    public ReporterPlugin()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(BundleContext context)
        throws Exception
    {
        super.start(context);
        plugin__ = this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop(BundleContext context)
        throws Exception
    {
        plugin__ = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static ReporterPlugin getDefault()
    {
        return plugin__;
    }

}
