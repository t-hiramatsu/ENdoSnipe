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
package jp.co.acroquest.endosnipe.perfdoctor.rule;

import java.util.HashMap;
import java.util.Map;

import jp.co.acroquest.endosnipe.perfdoctor.rule.def.RuleSetConfig;

import org.apache.commons.lang.StringUtils;

/**
 * Eclipse��胋�[���ݒ���擾���郆�[�e�B���e�B�N���X�B
 * TODO �ݒ�̉i����
 * 
 * @author tanimoto
 *
 */
public class RulePreferenceUtil
{
    /** ���[���Z�b�g��ID��\�����ڂ̖��O�B */
    private static final String        CONFIG_RULESET_IDS         = "perfdoctor.ruleSetIds";

    /** ���[���Z�b�g����\�����ڂ̖��O�B */
    private static final String        CONFIG_RULESET_NAME_PREFIX = "perfdoctor.ruleSetName_";

    /** ���[���Z�b�g��`�t�@�C���̖��O��\�����ڂ̖��O�B */
    private static final String        CONFIG_RULESET_FILE_PREFIX = "perfdoctor.ruleSetFile_";

    /** �L���ȃ��[���Z�b�g��\�����ڂ̖��O�B */
    private static final String        CONFIG_ACTIVE_RULESET_ID   = "perfdoctor.activeRuleSetId";

    private static Map<String, String> preferenceMap__            = new HashMap<String, String>();

    /**
     * �v���t�@�����X�X�g�A���烋�[���Z�b�g��`(RuleSetConfig�C���X�^���X)���擾����B
     * @param ruleSetId ���[���Z�b�gID
     * @return ���[���Z�b�g��`�B��`��������Ȃ��ꍇ�ł��A���[���Z�b�g��`��Ԃ��B
     */
    public static RuleSetConfig loadRuleSet(final String ruleSetId)
    {
        String name = preferenceMap__.get(CONFIG_RULESET_NAME_PREFIX + ruleSetId);
        String fileName = preferenceMap__.get(CONFIG_RULESET_FILE_PREFIX + ruleSetId);

        RuleSetConfig config = new RuleSetConfig();
        config.setId(ruleSetId);
        config.setName(name);
        config.setFileName(fileName);

        return config;
    }

    /**
     * �v���t�@�����X�X�g�A�Ƀ��[���Z�b�g��`��ۑ�����B
     * @param config ���[���Z�b�g��`
     */
    public static void saveRuleSet(final RuleSetConfig config)
    {
        String id = config.getId();

        preferenceMap__.put(CONFIG_RULESET_NAME_PREFIX + id, config.getName());
        preferenceMap__.put(CONFIG_RULESET_FILE_PREFIX + id, config.getFileName());
    }

    /**
     * �v���t�@�����X�X�g�A���烋�[���Z�b�gID�ꗗ���擾����B
     * @return ���[���Z�b�gID�ꗗ�B������Ȃ��ꍇ�́A����0�̔z���Ԃ��B
     */
    public static String[] loadRuleSetIds()
    {
        String ids = preferenceMap__.get(CONFIG_RULESET_IDS);

        if (ids == null || ids.length() == 0)
        {
            return new String[0];
        }

        String[] ruleIds = StringUtils.split(ids, ",");
        return ruleIds;
    }

    /**
     * �v���t�@�����X�X�g�A�Ƀ��[���Z�b�gID�ꗗ��ۑ�����B
     * @param ruleSetIds ���[���Z�b�gID�ꗗ
     */
    public static void saveRuleSetIds(final String[] ruleSetIds)
    {
        String ruleIds = StringUtils.join(ruleSetIds, ",");
        preferenceMap__.put(CONFIG_RULESET_IDS, ruleIds);
    }

    /**
     * �v���t�@�����X�X�g�A����A�N�e�B�u�ȃ��[���Z�b�gID���擾����B
     * @return ���[���Z�b�gID�B������Ȃ��ꍇ�́A��̕������Ԃ��B
     */
    public static String loadActiveRuleSetId()
    {
        String str = preferenceMap__.get(CONFIG_ACTIVE_RULESET_ID);

        return str;
    }

    /**
     * �v���t�@�����X�X�g�A�ɃA�N�e�B�u�ȃ��[���Z�b�gID��ۑ�����B
     * @param ruleSetId ���[���Z�b�gID
     */
    public static void saveActiveRuleSetId(final String ruleSetId)
    {
        preferenceMap__.put(CONFIG_ACTIVE_RULESET_ID, ruleSetId);
    }

    private RulePreferenceUtil()
    {
        // Do Nothing.
    }
}
