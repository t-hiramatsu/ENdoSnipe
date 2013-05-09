package jp.co.acroquest.endosnipe.report;

import java.io.IOException;
import java.util.List;

import jp.co.acroquest.endosnipe.collector.config.AgentSetting;
import jp.co.acroquest.endosnipe.collector.config.ConfigurationReader;
import jp.co.acroquest.endosnipe.collector.config.DataCollectorConfig;
import jp.co.acroquest.endosnipe.collector.exception.InitializeException;
import jp.co.acroquest.endosnipe.common.util.PathUtil;

public class ConfigLoader
{
    /** 設定ファイルのパス */
    private static final String DEF_COLLECTOR_PROPERTY = "../conf/collector.properties";

    public static DataCollectorConfig loadConfig()
        throws InitializeException
    {
        // 設定ファイルのパスを絶対パスに変換する
        String jarPath = PathUtil.getJarDir(ConfigLoader.class);
        String fileName = jarPath + DEF_COLLECTOR_PROPERTY;

        DataCollectorConfig config = null;
        try
        {
            config = ConfigurationReader.load(fileName);
        }
        catch (IOException ex)
        {
            throw new InitializeException("プロパティファイルが見つかりません。",
                                          ConfigurationReader.getAbsoluteFilePath());
        }
        List<AgentSetting> agentList = config.getAgentSettingList();
        if (agentList == null || agentList.size() == 0)
        {
            throw new InitializeException("データベース設定情報が見つかりません。",
                                          ConfigurationReader.getAbsoluteFilePath());
        }

        return config;
    }
}
