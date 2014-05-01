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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.perfdoctor.Messages;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRule;
import jp.co.acroquest.endosnipe.perfdoctor.exception.RuleCreateException;
import jp.co.acroquest.endosnipe.perfdoctor.rule.def.RuleDef;
import jp.co.acroquest.endosnipe.perfdoctor.rule.def.RuleSetConfig;
import jp.co.acroquest.endosnipe.perfdoctor.rule.def.RuleSetDef;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SerializationUtils;

/**
 * ���[���̊Ǘ��i�ǉ��A�ύX�A�폜�A�Q�Ɓj���s���N���X�B
 * 
 * @author tanimoto
 * 
 */
public class RuleManager
{
    private static final ENdoSnipeLogger   LOGGER                     =
                                                                        ENdoSnipeLogger.getLogger(RuleManager.class);

    /** ���[����`�t�@�C���̓ǂݍ��݂Ɏ��s�����ۂɓ��������O�ɓn��������B */
    private static final String            RULE_CREATE_ERROR          = "RuleCreateError";

    /** �f�t�H���g�̃��[���Z�b�g��ID�B */
    public static final String             DEFAULT_RULESET_ID         = "PERFDOCTOR_DEFAULT";

    /** �f�t�H���g�̃��[���Z�b�g�̖��O�B */
    private static final String            DEFAULT_RULESET_NAME       =
                                                                        Messages.getMessage("endosnipe.perfdoctor.rule.RuleManager.DefaultRuleSetName");

    /** �f�t�H���g�̃��[���Z�b�g�̃t�@�C�����B */
    private static final String            DEFAULT_RULESET_FILE       = "/perfdoctor_rule.xml";

    /** Java�p�̃f�t�H���g�̃��[���Z�b�g��ID�B */
    public static final String             DEFAULT_JAVA_RULESET_ID    = "PERFDOCTOR_JAVA_DEFAULT";

    /** Java�p�̃f�t�H���g�̃��[���Z�b�g�̖��O�B */
    private static final String            DEFAULT_JAVA_RULESET_NAME  =
                                                                        Messages.getMessage("endosnipe.perfdoctor.rule.RuleManager.DefaultJavaRuleSetName");

    /** Java�p�̃f�t�H���g�̃��[���Z�b�g�̃t�@�C�����B */
    private static final String            DEFAULT_JAVA_RULESET_FILE  = "/perfdoctor_Java_rule.xml";

    /** DB�p�̃f�t�H���g�̃��[���Z�b�g��ID�B */
    public static final String             DEFAULT_DB_RULESET_ID      = "PERFDOCTOR_DB_DEFAULT";

    /** DB�p�̃f�t�H���g�̃��[���Z�b�g�̖��O�B */
    private static final String            DEFAULT_DB_RULESET_NAME    =
                                                                        Messages.getMessage("endosnipe.perfdoctor.rule.RuleManager.DefaultDBRuleSetName");

    /** DB�p�̃f�t�H���g�̃��[���Z�b�g�̃t�@�C�����B */
    private static final String            DEFAULT_DB_RULESET_FILE    = "/perfdoctor_DB_rule.xml";

    /** HP-UX�p�̃f�t�H���g�̃��[���Z�b�g��ID�B */
    public static final String             DEFAULT_HP_UX_RULESET_ID   = "PERFDOCTOR_HP_UX_DEFAULT";

    /** HP-UX�p�̃f�t�H���g�̃��[���Z�b�g�̖��O�B */
    private static final String            DEFAULT_HP_UX_RULESET_NAME =
                                                                        Messages.getMessage("endosnipe.perfdoctor.rule.RuleManager.DefaultHPUXRuleSetName");

    /** �@HP-UX�p�̃f�t�H���g�̃��[���Z�b�g�̃t�@�C�����B */
    private static final String            DEFAULT_HP_UX_RULESET_FILE =
                                                                        "/perfdoctor_HP_UX_rule.xml";

    /** ���[���̊Ǘ����s���C���X�^���X�B */
    private static RuleManager             instance__;

    /** ���[����`�̃C���^�t�F�[�X�B */
    private final RuleDefAccessor          accessor_                  = new XmlRuleDefAccessor();

    /** ���X�i�[�̃Z�b�g */
    private final Set<RuleChangeListener>  listenerSet_               =
                                                                        new LinkedHashSet<RuleChangeListener>();

    // �����[���o�b�N�ΏہB
    /**
     * ���p�\��RuleSetConfig��ێ�����Map�B �L�[��RuleSetConfig��ID�A�l��RuleSetConfig�{�́B
     */
    private HashMap<String, RuleSetConfig> ruleSetConfigMap_;

    /** �t�@�C���폜�Ώۃ��[�� */
    private List<RuleSetConfig>            removeList_;

    // �����[���o�b�N�ΏہB
    /**
     * RuleSetDef��ێ�����Map�B �L�[��RuleSetDef��ID�A�l��RuleSetDef�{�́B
     */
    private HashMap<String, RuleSetDef>    ruleSetMap_;

    /**
     * �L���ƂȂ��Ă��郋�[���Z�b�g��ID�B
     */
    private String                         activeRuleSetId_;

    /**
     * �ݒ肪�ύX���ꂽ���[���Z�b�g��ID�̃��X�g�B
     */
    private Set<String>                    dirtyRuleSetIds_;

    /**
     * RuleManager�C���X�^���X�̎擾�B
     * 
     * @return RuleManager�C���X�^���X�B
     */
    public static synchronized RuleManager getInstance()
    {
        if (instance__ == null)
        {
            instance__ = new RuleManager();
        }
        return instance__;
    }

    /**
     * �R���X�g���N�^�B�O������̌Ăяo�����֎~����B
     */
    private RuleManager()
    {
        initialize();
    }

    /**
     * �C���X�^���X�̏��������s���B
     */
    private void initialize()
    {
        this.ruleSetMap_ = new HashMap<String, RuleSetDef>();
        this.ruleSetConfigMap_ = loadConfigurations();
        this.activeRuleSetId_ = loadActiveRuleSetId();
        this.dirtyRuleSetIds_ = new HashSet<String>();
        this.removeList_ = Collections.synchronizedList(new ArrayList<RuleSetConfig>());
    }

    /**
     * ���[���Z�b�g��`��ǂݍ��ށB �v���t�@�����X�X�g�A�Ƀ��[���Z�b�g��ID������ۑ�����Ă��Ȃ��ꍇ�ɂ� �f�t�H���g�̃��[���Z�b�g��`�}�b�v��Ԃ��B
     * 
     * @return ���[���Z�b�g��`�}�b�v�i��`�ǂݍ��ݍς݁j
     */
    private HashMap<String, RuleSetConfig> loadConfigurations()
    {
        HashMap<String, RuleSetConfig> map = createDefaultConfigMap();

        String[] ruleSetIds = RulePreferenceUtil.loadRuleSetIds();
        for (String ruleSetId : ruleSetIds)
        {
            RuleSetConfig config = RulePreferenceUtil.loadRuleSet(ruleSetId);
            map.put(ruleSetId, config);
        }

        return map;
    }

    /**
     * �������[���Z�b�g��`�}�b�v���쐬����B
     * 
     * @return ���[���Z�b�g��`�}�b�v�i�f�t�H���g��`�̂݁j
     */
    private HashMap<String, RuleSetConfig> createDefaultConfigMap()
    {
        HashMap<String, RuleSetConfig> map = new LinkedHashMap<String, RuleSetConfig>();
        RuleSetConfig config = new RuleSetConfig();
        RuleSetConfig hpUxRuleSetConfig = new RuleSetConfig();
        RuleSetConfig javaRuleSetConfig = new RuleSetConfig();
        RuleSetConfig dbRuleSetConfig = new RuleSetConfig();

        // �f�t�H���g�̃��[���Z�b�g���`����B
        config.setId(DEFAULT_RULESET_ID);
        config.setName(DEFAULT_RULESET_NAME);
        config.setFileName(DEFAULT_RULESET_FILE);
        map.put(DEFAULT_RULESET_ID, config);

        // HP_UX�p�̃��[���Z�b�g���`����B
        hpUxRuleSetConfig.setId(DEFAULT_HP_UX_RULESET_ID);
        hpUxRuleSetConfig.setName(DEFAULT_HP_UX_RULESET_NAME);
        hpUxRuleSetConfig.setFileName(DEFAULT_HP_UX_RULESET_FILE);
        map.put(DEFAULT_HP_UX_RULESET_ID, hpUxRuleSetConfig);

        // java�p�̃��[���Z�b�g���`����B
        javaRuleSetConfig.setId(DEFAULT_JAVA_RULESET_ID);
        javaRuleSetConfig.setName(DEFAULT_JAVA_RULESET_NAME);
        javaRuleSetConfig.setFileName(DEFAULT_JAVA_RULESET_FILE);
        map.put(DEFAULT_JAVA_RULESET_ID, javaRuleSetConfig);

        // DB�p�̃��[���Z�b�g���`����B
        dbRuleSetConfig.setId(DEFAULT_DB_RULESET_ID);
        dbRuleSetConfig.setName(DEFAULT_DB_RULESET_NAME);
        dbRuleSetConfig.setFileName(DEFAULT_DB_RULESET_FILE);
        map.put(DEFAULT_DB_RULESET_ID, dbRuleSetConfig);

        return map;
    }

    /**
     * ���݃A�N�e�B�u�ȃ��[���Z�b�gID���擾����B �v���t�@�����X�X�g�A�ɕۑ�����Ă������[���Z�b�g��ID��null�ł��邩�A
     * ����0�ł������ꍇ�ɂ́A�f�t�H���g�̃��[���Z�b�g��ID��Ԃ��B
     * 
     * @return �A�N�e�B�u�ȃ��[���Z�b�gID
     */
    private String loadActiveRuleSetId()
    {
        String str = RulePreferenceUtil.loadActiveRuleSetId();

        if (str == null || str.length() == 0)
        {
            str = DEFAULT_RULESET_ID;
        }

        return str;
    }

    /**
     * ���[���Z�b�g��`(RuleSetConfig�C���X�^���X)�𗘗p�\�ȃ��[���Z�b�g�ɒǉ�����B
     * 
     * @param config
     *            ���[���Z�b�g��`
     */
    public void addRuleSetConfig(final RuleSetConfig config)
    {
        this.ruleSetConfigMap_.put(config.getId(), config);
    }

    /**
     * ���[���Z�b�g��`�ꗗ(���p�\�ȃ��[���ꗗ)���擾����B
     * 
     * @return ���[���Z�b�g��`�ꗗ
     */
    public RuleSetConfig[] getRuleSetConfigs()
    {
        RuleSetConfig[] array = new RuleSetConfig[this.ruleSetConfigMap_.size()];
        return this.ruleSetConfigMap_.values().toArray(array);
    }

    /**
     * ���[���Z�b�g��`�𗘗p�\�ȃ��[���Z�b�g�ꗗ����폜����B
     * 
     * @param id
     *            �폜���郋�[���Z�b�gID
     */
    public synchronized void removeRuleSetConfig(final String id)
    {
        RuleSetConfig removeConfig = this.ruleSetConfigMap_.get(id);
        this.removeList_.add(removeConfig);
        this.ruleSetConfigMap_.remove(id);
    }

    /**
     * ���݃A�N�e�B�u�ȃ��[���Z�b�g��`(RuleSetConfig�C���X�^���X)���擾����B
     * 
     * @return �A�N�e�B�u�ȃ��[���Z�b�g��`
     */
    public RuleSetConfig getActiveRuleSetConfig()
    {
        return this.ruleSetConfigMap_.get(this.activeRuleSetId_);
    }

    /**
     * �A�N�e�B�u�ȃ��[���Z�b�g��ݒ肷��B
     * 
     * @param ruleSetConfig
     *            ���[���Z�b�g��`
     */
    public void setActiveRuleSetConfig(final RuleSetConfig ruleSetConfig)
    {
        this.activeRuleSetId_ = ruleSetConfig.getId();
    }

    /**
     * ���[���Z�b�g��`�Ȃǂ��v���t�@�����X�X�g�A�Axml�t�@�C���ɕۑ�����B
     */
    @SuppressWarnings("deprecation")
    public synchronized void commit()
    {
        // �A�N�e�B�u�ȃ��[���Z�b�gID�̕ۑ��B
        RulePreferenceUtil.saveActiveRuleSetId(this.activeRuleSetId_);

        // ���[���Z�b�g�ڍ׈ꗗ�̕ۑ��B
        List<String> ruleSetIdList = new ArrayList<String>();
        Collection<RuleSetConfig> ruleSetConfigs = this.ruleSetConfigMap_.values();

        for (RuleSetConfig config : ruleSetConfigs)
        {
            String id = config.getId();
            if (isDefaultRuleSet(id))
            {
                continue;
            }

            RulePreferenceUtil.saveRuleSet(config);

            // 
            ruleSetIdList.add(id);
        }

        // ���[���Z�b�gID�ꗗ�̕ۑ��B
        String[] ruleSetIds = ruleSetIdList.toArray(new String[ruleSetIdList.size()]);
        RulePreferenceUtil.saveRuleSetIds(ruleSetIds);

        // ���[���Z�b�g�̕ۑ��B
        // �ύX�����������[���Z�b�g�̂ݕۑ�����B
        for (String ruleId : this.dirtyRuleSetIds_)
        {
            if (isDefaultRuleSet(ruleId))
            {
                continue;
            }

            RuleSetConfig config = this.ruleSetConfigMap_.get(ruleId);
            if (config == null)
            {
                continue;
            }
            RuleSetDef def = this.ruleSetMap_.get(ruleId);
            this.accessor_.updateRuleSet(def, config.getFileName());
        }

        // ���[���Z�b�g�̕ۑ��B
        // �t�@�C�������݂��Ȃ����[���Z�b�g�ɂ��āA
        // �f�t�H���g�̃��[�������Ƀt�@�C�����쐬����B
        for (RuleSetConfig config : ruleSetConfigs)
        {
            String id = config.getId();
            if (isDefaultRuleSet(id))
            {
                continue;
            }

            File file = new File(config.getFileName());
            if (file.exists() && file.isFile())
            {
                continue;
            }

            File parentFile = file.getParentFile();

            if (parentFile != null && parentFile.exists() == false)
            {
                try
                {
                    parentFile.mkdirs();
                }
                catch (SecurityException ex)
                {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }

            // �f�t�H���g�̃��[�����R�s�[���ĕۑ�����
            try
            {
                RuleSetDef defaultRuleSetClone = new RuleSetDef(getRuleSetDef(DEFAULT_RULESET_ID));
                defaultRuleSetClone.setName(config.getName());
                this.accessor_.updateRuleSet(defaultRuleSetClone, config.getFileName());
            }
            catch (RuleCreateException ex)
            {
                LOGGER.error(ex.getMessage(), ex);
            }
        }

        // ���[���t�@�C�����폜����B
        for (RuleSetConfig config : this.removeList_)
        {
            File file = new File(config.getFileName());
            if (file.exists())
            {
                try
                {
                    file.delete();
                }
                catch (SecurityException ex)
                {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
        this.removeList_ = Collections.synchronizedList(new ArrayList<RuleSetConfig>());
    }

    /**
     * ���[���Z�b�g��`���擾����B<br>
     * �w�肳�ꂽ���[���Z�b�gID�ɑΉ�����ݒ�t�@�C����������Ȃ��ꍇ�́A<br>
     * �f�t�H���g�̃��[���Z�b�g��`���擾����B
     * 
     * @param id
     *            ���[���Z�b�gID
     * @return ���[���Z�b�g��`
     * @throws RuleCreateException
     *             ���[���Z�b�g��`�t�@�C���ǂݍ��݂Ɏ��s�����ꍇ
     */
    public RuleSetDef getRuleSetDef(final String id)
        throws RuleCreateException
    {
        RuleSetDef def = this.ruleSetMap_.get(id);
        if (def != null)
        {
            return def;
        }

        if (id.equals(DEFAULT_RULESET_ID))
        {
            def = this.accessor_.findRuleSet(DEFAULT_RULESET_FILE);
            def.setName(this.ruleSetConfigMap_.get(id).getName());
        }
        else if (id.equals(DEFAULT_HP_UX_RULESET_ID))
        {
            def = this.accessor_.findRuleSet(DEFAULT_HP_UX_RULESET_FILE);
            def.setName(this.ruleSetConfigMap_.get(id).getName());
        }
        else if (id.equals(DEFAULT_JAVA_RULESET_ID))
        {
            def = this.accessor_.findRuleSet(DEFAULT_JAVA_RULESET_FILE);
            def.setName(this.ruleSetConfigMap_.get(id).getName());
        }
        else if (id.equals(DEFAULT_DB_RULESET_ID))
        {
            def = this.accessor_.findRuleSet(DEFAULT_DB_RULESET_FILE);
            def.setName(this.ruleSetConfigMap_.get(id).getName());
        }
        else
        {
            RuleSetConfig config = this.ruleSetConfigMap_.get(id);
            if (config == null)
            {
                throw new RuleCreateException("InvalidRuleSetDef", new Object[]{id});
            }
            String fileName = config.getFileName();
            def = this.accessor_.findRuleSet(fileName);
        }

        this.ruleSetMap_.put(id, def);

        return def;
    }

    /**
     * ���[�����R�s�[���܂��B<br />
     * 
     * @param orgId
     *            �R�s�[�� ID
     * @param dstId
     *            �R�s�[�� ID
     * @param dstName
     *            �R�s�[�惋�[���Z�b�g�̖��O
     * @throws RuleCreateException
     *             ���[���Z�b�g��`�t�@�C���ǂݍ��݂Ɏ��s�����ꍇ
     */
    public void copyRuleSetDef(final String orgId, final String dstId, final String dstName)
        throws RuleCreateException
    {
        RuleSetDef orgDef = getRuleSetDef(orgId);
        RuleSetDef dstDef = new RuleSetDef(orgDef);
        dstDef.setName(dstName);
        this.ruleSetMap_.put(dstId, dstDef);
    }

    /**
     * ���[���Z�b�g��`���ꎞ�I�ɕۑ�����B<br>
     * rollbackRuleSet���\�b�h�����s���ꂽ�ۂɁA���[���Z�b�g��`�������߂����߂ɗ��p����B
     * 
     * @return �V���A���C�Y�����ꂽ���[���f�[�^
     */
    public synchronized SerializedRules saveRuleSet()
    {
        byte[] ruleSetConfigMapData = SerializationUtils.serialize(this.ruleSetConfigMap_);
        byte[] ruleSetMapData = SerializationUtils.serialize(this.ruleSetMap_);
        return new SerializedRules(ruleSetConfigMapData, ruleSetMapData);
    }

    /**
     * ���[���Z�b�g��`�����[���o�b�N����B<br>
     * 
     * @param serializedRules
     *            �V���A���C�Y�����ꂽ���[���f�[�^
     */
    @SuppressWarnings("unchecked")
    public synchronized void rollbackRuleSet(final SerializedRules serializedRules)
    {
        byte[] ruleSetConfigMapData = serializedRules.getRuleSetConfigMapData();
        byte[] ruleMapData = serializedRules.getRuleMapData();
        if (ruleSetConfigMapData == null || ruleSetConfigMapData.length == 0 || ruleMapData == null
                || ruleMapData.length == 0)
        {
            return;
        }

        this.ruleSetConfigMap_ =
                                 (HashMap<String, RuleSetConfig>)SerializationUtils.deserialize(ruleSetConfigMapData);
        this.ruleSetMap_ = (HashMap<String, RuleSetDef>)SerializationUtils.deserialize(ruleMapData);
        this.removeList_ = Collections.synchronizedList(new ArrayList<RuleSetConfig>());
    }

    /**
     * �ύX�����������[���Z�b�gID��ۑ�����B<br>
     * commit���\�b�h�����s���ꂽ�ۂɁA���̃��\�b�h�Ŏw�肵��<br>
     * ���[���Z�b�gID�ɑ΂��郋�[���Z�b�g��`�̂ݕۑ�����B
     * 
     * @param ruleSetId
     *            ���[���Z�b�gID
     */
    public void addDirty(final String ruleSetId)
    {
        this.dirtyRuleSetIds_.add(ruleSetId);
    }

    /**
     * �A�N�e�B�u�ȃ��[���Z�b�g��`���擾����B
     * 
     * @return ���[���Z�b�g��`
     * @throws RuleCreateException
     *             ���[����`�t�@�C���̓ǂݍ��݂Ɏ��s�����ۂɔ�������B
     */
    public RuleSetDef getActiveRuleSetDef()
        throws RuleCreateException
    {
        return getRuleSetDef(this.activeRuleSetId_);
    }

    /**
     * �A�N�e�B�u�ȃ��[���Z�b�g�Ɋ܂܂��A���[���C���X�^���X�̈ꗗ���擾����B �A�N�e�B�u�ȃ��[���Z�b�g���̗v�f���ɂ��郋�[�������s���ł��邽�߂ɁA
     * �C���X�^���X�����Ɏ��s�����ꍇ�ɂ́ARuleCreateException���X���[����B
     * 
     * @return ���[���C���X�^���X�̈ꗗ
     * @throws RuleCreateException
     *             ���[����`�t�@�C���̓ǂݍ��݂Ɏ��s�����ꍇ
     */
    public List<PerformanceRule> getActiveRules()
        throws RuleCreateException
    {
        List<PerformanceRule> ruleList = new ArrayList<PerformanceRule>();
        List<String> errorMessageList = new ArrayList<String>();

        RuleSetDef ruleSetDef = getActiveRuleSetDef();

        for (RuleDef ruleDef : ruleSetDef.getRuleDefs())
        {
            try
            {
                PerformanceRule rule = RuleInstanceUtil.createRuleInstance(ruleDef);
                if (rule != null)
                {
                    ruleList.add(rule);
                }
            }
            catch (RuleCreateException ex)
            {
                errorMessageList.add(ex.getMessage());
            }
        }

        if (errorMessageList.size() > 0)
        {
            String[] messages = errorMessageList.toArray(new String[errorMessageList.size()]);
            throw new RuleCreateException(RULE_CREATE_ERROR, null, messages);
        }

        return ruleList;
    }

    /**
     * ���j�[�N�ȃ��[���Z�b�gID���擾����B
     * 
     * @return ���[���Z�b�gID
     */
    public String createUniqueId()
    {
        String[] ruleSetIds = RulePreferenceUtil.loadRuleSetIds();

        String ruleSetId;
        do
        {
            ruleSetId = UUID.randomUUID().toString();
        }
        while (ArrayUtils.contains(ruleSetIds, ruleSetId));

        return ruleSetId;
    }

    /**
     * �f�t�H���g���[���Z�b�g���A�N�e�B�u�ɂ���B
     */
    public void setActiveRuleSetDefault()
    {
        this.activeRuleSetId_ = RuleManager.DEFAULT_RULESET_ID;
        RuleSetConfig config = this.ruleSetConfigMap_.get(RuleManager.DEFAULT_RULESET_ID);
        setActiveRuleSetConfig(config);
    }

    /**
     * �L���ƂȂ��Ă��郋�[���Z�b�g��ID���擾����B
     * 
     * @return �L���ƂȂ��Ă��郋�[���Z�b�gID
     */
    public String getActiveRuleSetID()
    {
        return this.activeRuleSetId_;
    }

    /**
     * ���[���Z�b�gID���w�肵�āA�L���ƂȂ��Ă��郋�[���Z�b�g��؂芷����B
     * 
     * @param ruleSetID
     *            �L�������郋�[���Z�b�gID
     */
    public void changeActiveRuleSetByID(final String ruleSetID)
    {
        RuleSetConfig config = this.ruleSetConfigMap_.get(ruleSetID);

        if (config != null)
        {
            setActiveRuleSetConfig(config);
        }
    }

    /**
     * ���[���ύX���X�i��ǉ�����B
     * 
     * @param listener
     *            ���X�i
     */
    public void addListener(final RuleChangeListener listener)
    {
        this.listenerSet_.add(listener);
    }

    /**
     * ���[���ύX���X�i���폜����B
     * 
     * @param listener
     *            ���X�i
     */
    public void removeListener(final RuleChangeListener listener)
    {
        this.listenerSet_.remove(listener);
    }

    /**
     * �X�V��ʒm����B
     */
    public void notifyChanged()
    {
        for (RuleChangeListener listener : this.listenerSet_)
        {
            listener.ruleChangePerformed();
        }
    }

    /**
     * �w�肳�ꂽ���[�� ID �̃��[�����f�t�H���g���[�����ǂ������`�F�b�N���܂��B<br />
     * 
     * @param ruleId
     *            ���[�� ID
     * @return �f�t�H���g���[���̏ꍇ�� <code>true</code> �A�f�t�H���g���[���łȂ��ꍇ�� <code>false</code>
     */
    private boolean isDefaultRuleSet(final String ruleId)
    {
        if (DEFAULT_RULESET_ID.equals(ruleId) || DEFAULT_HP_UX_RULESET_ID.equals(ruleId)
                || DEFAULT_JAVA_RULESET_ID.equals(ruleId) || DEFAULT_DB_RULESET_ID.equals(ruleId))
        {
            return true;
        }
        return false;
    }

}
