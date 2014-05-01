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
package jp.co.acroquest.endosnipe.report;

import java.io.IOException;
import java.util.List;

import jp.co.acroquest.endosnipe.collector.config.AgentSetting;
import jp.co.acroquest.endosnipe.collector.config.ConfigurationReader;
import jp.co.acroquest.endosnipe.collector.config.DataCollectorConfig;
import jp.co.acroquest.endosnipe.collector.exception.InitializeException;
import jp.co.acroquest.endosnipe.common.util.PathUtil;

public class ConfigLoader {
	/** �ݒ�t�@�C���̃p�X */
	private static final String DEF_COLLECTOR_PROPERTY = "../../../conf/collector.properties";

	public static DataCollectorConfig loadConfig() throws InitializeException {
		// �ݒ�t�@�C���̃p�X���΃p�X�ɕϊ�����
		String jarPath = PathUtil.getJarDir(ConfigLoader.class);
		String fileName = jarPath + DEF_COLLECTOR_PROPERTY;

		DataCollectorConfig config = null;
		try {
			config = ConfigurationReader.load(fileName);
		} catch (IOException ex) {
			throw new InitializeException("�v���p�e�B�t�@�C����������܂���B",
					ConfigurationReader.getAbsoluteFilePath());
		}
		List<AgentSetting> agentList = config.getAgentSettingList();
		if (agentList == null || agentList.size() == 0) {
			throw new InitializeException("�f�[�^�x�[�X�ݒ��񂪌�����܂���B",
					ConfigurationReader.getAbsoluteFilePath());
		}

		return config;
	}
}
