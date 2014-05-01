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

import java.lang.reflect.Field;
import java.util.List;

import jp.co.acroquest.endosnipe.perfdoctor.PerfConstants;
import jp.co.acroquest.endosnipe.perfdoctor.PerformanceRule;
import jp.co.acroquest.endosnipe.perfdoctor.exception.RuleCreateException;
import jp.co.acroquest.endosnipe.perfdoctor.rule.def.PropertyDef;
import jp.co.acroquest.endosnipe.perfdoctor.rule.def.RuleDef;
import jp.co.acroquest.endosnipe.perfdoctor.rule.def.RuleLevelDef;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;

/**
 * ���[���̃C���X�^���X�������s�����[�e�B���e�B�N���X�B
 * @author tanimoto
 *
 */
public class RuleInstanceUtil
{
    private static final String ERROR = "ERROR";

    private static final String WARN  = "WARN";

    private static final String INFO  = "INFO";

    /**
     * ���[���̃C���X�^���X���쐬���A�v���p�e�B��ݒ肷��B<br>
     * ��`���ꂽ���x���̐������C���X�^���X���쐬����B
     * ruleDef���ɂ��郋�[�������s���ł��邽�߂ɁA
     * �C���X�^���X�����Ɏ��s�����ꍇ�ɂ́ARuleCreateException���X���[����B
     * @param ruleDef ���[����`
     * @return ���[���̃C���X�^���X
     * @throws RuleCreateException ���[���̃C���X�^���X�쐬���A�l�̐ݒ�Ɏ��s�����ꍇ�ɔ�������B
     */
    public static PerformanceRule createRuleInstance(final RuleDef ruleDef)
        throws RuleCreateException
    {
        if (checkEnabled(ruleDef.getEnabled()) == false)
        {
            return null;
        }

        List<RuleLevelDef> ruleLevelDefs = ruleDef.getRuleLevelDefs();
        if (ruleLevelDefs == null)
        {
            return null;
        }

        PerformanceRule rule = createRuleFacade(ruleDef);

        return rule;
    }

    /**
     * ���[���̃t�@�T�[�h���쐬����B<br>
     * @param ruleDef ���[����`
     * @return ���[���̃t�@�T�[�h
     * @throws RuleCreateException ���[���̃C���X�^���X�쐬���A�l�̐ݒ�Ɏ��s�����ꍇ�ɔ�������B
     */
    protected static PerformanceRule createRuleFacade(final RuleDef ruleDef)
        throws RuleCreateException
    {
        PerformanceRuleFacade ruleFacade = new PerformanceRuleFacade();

        List<RuleLevelDef> ruleLevelDefs = ruleDef.getRuleLevelDefs();
        for (RuleLevelDef ruleLevelDef : ruleLevelDefs)
        {
            if (checkEnabled(ruleLevelDef.getEnabled()) == false)
            {
                continue;
            }

            PerformanceRule rule = createNewInstance(ruleDef.getClassName());
            String level = ruleLevelDef.getLevel();
            if (ERROR.equalsIgnoreCase(level))
            {
                ruleFacade.setErrorRule(rule);
            }
            else if (WARN.equalsIgnoreCase(level))
            {
                ruleFacade.setWarnRule(rule);
            }
            else if (INFO.equalsIgnoreCase(level))
            {
                ruleFacade.setInfoRule(rule);
            }
            else
            {
                continue;
            }

            setValue(rule, "id", ruleDef.getId());
            setValue(rule, "active", "true");
            setValue(rule, "level", level.toUpperCase());
            setValue(rule, "durationThreshold", ruleLevelDef.getDurationThreshold());

            if (ruleLevelDef.getPropertyDefs() != null)
            {
                for (PropertyDef propertyDef : ruleLevelDef.getPropertyDefs())
                {
                    setValue(rule, propertyDef.getName(), propertyDef.getValue());
                }
            }

            rule.init();
        }
        return ruleFacade;
    }

    /**
     * �����̒l���utrue�v���ǂ����𔻒肷��B<br>
     * org.apache.commons.beanUtils.ConvertUtils�̃f�t�H���g�ϊ����[���ɏ]���āA<br>
     * �������Boolean�ɕϊ����A���̌��ʂ�Ԃ��B<br>
     * �������A�f�t�H���g�l��true�Ƃ��Ĕ��f���邽�߁A�����̒l����null�̏ꍇ��true��Ԃ��B<br>
     * @param value ������
     * @return ������true���ǂ�����Ԃ��B
     */
    protected static boolean checkEnabled(final String value)
    {
        if (StringUtils.isEmpty(value))
        {
            return true;
        }

        Boolean b = (Boolean)ConvertUtils.convert(value, Boolean.TYPE);
        return b.booleanValue();
    }

    /**
     * ���[���N���X�̖��O���烋�[���̃C���X�^���X�𐶐�����B
     * �N���X��������Ȃ��A�������\���N���X�����[���N���X�łȂ��Ȃǂ̗��R��
     * ��O�����������ꍇ�ɂ́ARuleCreateException���X���[����B
     * @param className �N���X��
     * @return ���[���̃C���X�^���X
     * @throws RuleCreateException �C���X�^���X�̐����Ɏ��s�����ꍇ�ɔ�������B
     */
    protected static PerformanceRule createNewInstance(final String className)
        throws RuleCreateException
    {
        PerformanceRule rule;
        try
        {
            Class<?> clazz = Class.forName(className);
            // PerformanceRule�̃C���X�^���X�łȂ���΁AClassCastException�Ɠ��l�̏������s���B
            if (PerformanceRule.class.isAssignableFrom(clazz) == false)
            {
                throw new RuleCreateException(PerfConstants.CLASS_TYPE_ERROR,
                                              new Object[]{className});
            }
            rule = (PerformanceRule)clazz.newInstance();
        }
        catch (ClassNotFoundException ex)
        {
            throw new RuleCreateException(PerfConstants.CLASS_NOT_FOUND, new Object[]{className});
        }
        catch (InstantiationException ex)
        {
            throw new RuleCreateException(PerfConstants.NEW_INSTANCE_ERROR, new Object[]{className});
        }
        catch (IllegalAccessException ex)
        {
            throw new RuleCreateException(PerfConstants.NEW_INSTANCE_ERROR, new Object[]{className});
        }
        catch (ClassCastException ex)
        {
            throw new RuleCreateException(PerfConstants.CLASS_TYPE_ERROR, new Object[]{className});
        }

        return rule;
    }

    /**
     * �l�̐ݒ���s���B
     * @param obj �ݒ�Ώۂ̃I�u�W�F�N�g
     * @param fieldName �t�B�[���h��
     * @param value �l
     * @throws RuleCreateException �l�̐ݒ�Ɏ��s�����ꍇ�ɔ�������B
     */
    protected static void setValue(final Object obj, final String fieldName, final String value)
        throws RuleCreateException
    {
        Class<? extends Object> clazz = obj.getClass();
        Object[] args = new Object[]{clazz.getCanonicalName(), fieldName, value};

        try
        {
            Field field = clazz.getField(fieldName);

            Object convertedValue = ConvertUtils.convert(value, field.getType());
            field.set(obj, convertedValue);
        }
        catch (NoSuchFieldException ex)
        {
            throw new RuleCreateException(PerfConstants.PROPERTY_NOT_FOUND, args);
        }
        catch (SecurityException ex)
        {
            throw new RuleCreateException(PerfConstants.PROPERTY_ERROR, args);
        }
        catch (IllegalArgumentException ex)
        {
            throw new RuleCreateException(PerfConstants.PROPERTY_TYPE_ERROR, args);
        }
        catch (IllegalAccessException ex)
        {
            throw new RuleCreateException(PerfConstants.PROPERTY_ACCESS_ERROR, args);
        }
    }

    /**
     * ���x�����I����(ERROR�AWARN�AINFO)�Ɋ܂܂�Ă��邩�ǂ����m�F����B
     * @param level ���x��
     * @return �I�����Ɋ܂܂�Ă����true�A�����łȂ��ꍇ��false��Ԃ��B
     */
    protected static boolean isValidLevel(final String level)
    {
        if (PerfConstants.LEVEL_ERROR.equalsIgnoreCase(level)
                || PerfConstants.LEVEL_INFO.equalsIgnoreCase(level)
                || PerfConstants.LEVEL_WARN.equalsIgnoreCase(level))
        {
            return true;
        }

        return false;
    }

    private RuleInstanceUtil()
    {
        // Do Nothing.
    }
}
