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
package jp.co.acroquest.test.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.acroquest.endosnipe.common.config.ConfigPreprocessor;
import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.config.JavelinConfigUtil;
import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import jp.co.acroquest.endosnipe.javelin.CallTreeRecorder;
import jp.co.acroquest.endosnipe.javelin.conf.ExcludeConversionConfig;
import jp.co.acroquest.endosnipe.javelin.conf.IncludeConversionConfig;
import jp.co.acroquest.endosnipe.javelin.conf.JavelinTransformConfig;
import jp.co.acroquest.endosnipe.javelin.converter.Converter;
import jp.co.acroquest.endosnipe.javelin.resource.MultiResourceGetter;
import jp.co.acroquest.endosnipe.javelin.resource.ResourceCollector;
import jp.co.acroquest.endosnipe.javelin.resource.ResourceGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.ProcParser;
import jp.co.acroquest.endosnipe.javelin.testutil.PrivateAccessor;
import jp.co.dgic.testing.common.virtualmock.MockObjectManager;
import jp.co.smg.endosnipe.javassist.ClassPool;
import jp.co.smg.endosnipe.javassist.CtClass;
import junit.framework.Assert;

/**
 * Javelin�̃e�X�g�R�[�h�ɗp���郆�[�e�B���e�B���\�b�h�Q�N���X
 * 
 * @author M.Yoshida
 */
public class JavelinTestUtil
{
    private static Map<String, String>      propertyMap__ ;
    private static Map<String, Set<String>> convertedClass__;
    private static JavelinConfig            jvnConfig__;
    private static JavelinTransformConfig   jvnTransformConfig__;
    private static Map<String, Class<?>>    resourceGetterClass__;
    
    
    static
    {
        propertyMap__ = new HashMap<String, String>(); 
        propertyMap__.put("javelin.call.tree.enable", "isCallTreeEnabled");
        propertyMap__.put("javelin.call.tree.max", "getCallTreeMax");
        propertyMap__.put("javelin.timeout.monitor", "isTimeoutMonitor");
        propertyMap__.put("javelin.log4j.printstack.level", "getLog4jPrintStackLevel");
        propertyMap__.put("javelin.bytecode.exclude.length", "getBytecodeLengthMax");
        propertyMap__.put("javelin.bytecode.exclude.controlCount", "getBytecodeControlCountMax");
        propertyMap__.put("javelin.bytecode.exclude.policy", "getByteCodeExcludePolicy");
        propertyMap__.put("javelin.autoExcludeThreshold.count","getAutoExcludeThresholdCount");
        propertyMap__.put("javelin.autoExcludeThreshold.time","getAutoExcludeThresholdTime");
        propertyMap__.put("javelin.thread.dump.cpu", "getThreadDumpCpu");
        propertyMap__.put("javelin.thread.dump.threadnum", "getThreadDumpThreadNum");
        propertyMap__.put("javelin.thread.dump.monitor", "isThreadDump");
        propertyMap__.put("javelin.leak.collectionSizeThreshold", "getCollectionSizeThreshold");
        propertyMap__.put("javelin.thread.blocktime.threshold", "getBlockTimeThreshold");
        propertyMap__.put("javelin.leak.collectionSizeOut", "isLeakCollectionSizePrint");
        propertyMap__.put("javelin.fullgc.threshold", "getFullGCThreshold");
        convertedClass__ = new HashMap<String, Set<String>>();
        jvnConfig__ = null;
    }

    /**
     * ���\�[�X�擾�N���X�����B
     */
    private static void initResource()
    {
        if (resourceGetterClass__ != null)
        {
            return;
        }

        resourceGetterClass__ = new HashMap<String, Class<?>>();
        Map<String, ResourceGetter> resourceMap = new HashMap<String, ResourceGetter>();
        Map<String, MultiResourceGetter> multiResourceMap =
            new HashMap<String, MultiResourceGetter>();
        ProcParser procParser = ResourceCollector.createProcParser();
        ResourceCollector.setResouceGetters(resourceMap, multiResourceMap, procParser);
        for (Map.Entry<String, ResourceGetter> entry : resourceMap.entrySet())
        {
            resourceGetterClass__.put(entry.getKey(), entry.getValue().getClass());
        }
    }

    /**
     * ���\�[�X���̎擾���\�b�h�̕Ԃ�l���U�����A�C�ӂ̃��\�[�X�����擾�ł���悤�ɂ���B
     * 
     * @param resourceKey ���\�[�X������ӂɒ�߂�L�[(TelegramConstants�̃����o)
     * @param value       �Ԃ�l�i�U������j
     * @throws Exception  �����̓r���ɃG���[�����������ꍇ
     */
    public static void camouflageResourceInfo(String resourceKey, Number value) throws Exception
    {
        initResource();
        Class<?> resGetter = resourceGetterClass__.get(resourceKey);
        
        MockObjectManager.setReturnValueAtAllTimes(resGetter, "getValue", value);
    }
    
    /**
     * �e�X�g�p�ɍ쐬����Javelin�ݒ�t�@�C����ǂݍ��݁AJavelin�������Q�Ƃ���ۂ̐ݒ�l���U������B
     *
     * @param baseClass �f�B���N�g����N���X
     * @param fileName �U������ݒ�l�������ꂽ�t�@�C��
     * @throws Exception �����̍Œ��G���[�����������ꍇ
     */
    public static void camouflageJavelinConfig(Class<?> baseClass, String fileName) throws Exception
    {
        PrivateAccessor.setField(JavelinConfig.class, "isInitialized__", false);
        PrivateAccessor.setField(JavelinConfigUtil.getInstance(), "properties_", null);
        PrivateAccessor.setField(ConfigPreprocessor.class, "canonicalFiles__", new HashMap<Integer, File>());
        jvnTransformConfig__ = null;
        String configPath = getAbsolutePath(baseClass, fileName);
        MockObjectManager.setReturnValueAtAllTimes(JavelinConfigUtil.class, "getFileName1", configPath);
        JavelinConfigUtil.getInstance().update();
        JavelinConfig config = new JavelinConfig();
        jvnConfig__ = config;
    }
    
    /**
     * Javelin�������Q�Ƃ���ۂ̐ݒ�l���U������B���łɋU���̐ݒ肪����ꍇ�́A
     * ���̃��\�b�h�̎��s�ɂ��U���l���㏑������B
     * 
     * @param propKey Javelin��Config�t�@�C���ɋL�ڂ����L�[
     * @param value   �L�[�ɐݒ肳���l�i�U���l�j
     */
    public static void camouflageJavelinConfig(String propKey, Object value)
    {
        String camouflageMethod = propertyMap__.get(propKey);
        
        if (camouflageMethod == null)
        {
            return ;
        }
        
        MockObjectManager.setReturnValueAtAllTimes(JavelinConfig.class, camouflageMethod, value);
    }

    /**
     * �\�ߔz�u���Ă���ݒ�t�@�C���̃f�[�^���擾����B
     * �{���\�b�h���Ăяo���O�ɁAcamouflageJavelinConfig()���\�b�h���Ăяo���K�v������B
     * 
     * @return �ݒ�t�@�C���̃f�[�^�������Ă���I�u�W�F�N�g
     * @throws IOException �t�@�C���̓Ǎ��Ɏ��s�����ꍇ
     */
    public static JavelinTransformConfig readTransformConfig() throws IOException
    {
        if(jvnConfig__ == null)
        {
            return null;
        }
        
        if(jvnTransformConfig__ != null)
        {
            return jvnTransformConfig__;
        }
        
        JavelinTransformConfig transformConfig_ = new JavelinTransformConfig();

        InputStream includeStream = ConfigPreprocessor.process(new File(jvnConfig__.getInclude()));
        InputStream excludeStream = ConfigPreprocessor.process(new File(jvnConfig__.getExclude()));

        try
        {
            transformConfig_.readConfig(includeStream, excludeStream);
        }
        finally
        {
            includeStream.close();
            excludeStream.close();
        }
        
        jvnTransformConfig__ = transformConfig_;
        
        return transformConfig_;
    }

    /**
     * �p�����[�^�Ŏw�肵���R���o�[�^�ɂ��ϊ����ꂽ�N���X�̃N���X�����擾����B�B
     * 
     * @param converterName �K�p����R���o�[�^�̃N���X(���S���薼)
     * @param targetClass   �R���o�[�^�ɂ��ϊ�����N���X(���S���薼)
     * @return�@�R���o�[�^�ɂ��ϊ����ꂽ�N���X�̃N���X���
     * @throws Exception �G���[�����������ꍇ�i�����Ɉ��炸�j
     */
    public static Class<?> applyMonitor(String converterName, String targetClass) throws Exception
    {
        // ���ɃR���o�[�g�����s�����N���X�̏ꍇ�́A�ϊ���N���X��Ԃ��ďI������B
        Set<String> convertedClassSet = convertedClass__.get(converterName);
        
        if(convertedClassSet != null && convertedClassSet.contains(targetClass) == true)
        {
            return Class.forName(targetClass);
        }
        
        if(convertedClassSet == null)
        {
            convertedClassSet = new HashSet<String>();
            convertedClass__.put(converterName, convertedClassSet);
        }
        
        // �R���o�[�g�ΏۃN���X�̏����擾����B
        ClassPool pool            = ClassPool.getDefault();
        CtClass   targetClassInfo = pool.get(targetClass);
        targetClassInfo.stopPruning(true);
        
        // �ݒ�t�@�C������A�w�肵���R���o�[�^�̓K�p�Ώۂ��ǂ����𔻒肷��B
        JavelinTransformConfig transformConfig = readTransformConfig();
        
        IncludeConversionConfig includeConversionInfo 
            = findMatchConversionConfig(
                 targetClass, converterName, pool, targetClassInfo, transformConfig);
        
        List<ExcludeConversionConfig> excludeConverionInfo 
            = transformConfig.matchesToExclude(targetClass);
        
        if(includeConversionInfo == null)
        {
            return null;
        }
        
        // �K�p����R���o�[�^�𐶐�����B
        Class<?>  convertClassInfo = Class.forName(converterName);
        Converter converter        = (Converter)convertClassInfo.newInstance();

        converter.init();
        
        // �R���o�[�g�����{����B
        byte[] convertedByteCodeBuffer
            = converter.convert(targetClass,
                                null,
                                pool,
                                targetClassInfo,
                                includeConversionInfo,
                                excludeConverionInfo);
        
        // �R���o�[�g���s���Ȃ������ꍇ�͌��̃N���X��Ԃ��B
        if(convertedByteCodeBuffer == null || convertedByteCodeBuffer.length <= 0)
        {
            return Class.forName(targetClass);
        }
        
        ByteArrayInputStream byteCodeStream 
            = new ByteArrayInputStream(convertedByteCodeBuffer);

        targetClassInfo.defrost();
        CtClass convertedClass    = pool.makeClass(byteCodeStream);
        convertedClass.freeze();
        
        convertedClassSet.add(targetClass);
        return convertedClass.toClass();
    }
    
    
    /**
     * �p�����[�^�Ŏw�肵���R���o�[�^�ɂ��ϊ����ꂽ�N���X�̃C���X�^���X���擾����B
     * 
     * @param converterName �K�p����R���o�[�^�̃N���X(���S���薼)
     * @param targetClass   �R���o�[�^�ɂ��ϊ�����N���X(���S���薼)
     * @return�@�R���o�[�^�ɂ��ϊ����ꂽ�N���X�̃C���X�^���X
     * @throws Exception �G���[�����������ꍇ�i�����Ɉ��炸�j
     */
    public static Object createMonitoredObject(String converterName, String targetClass) throws Exception
    {
        Class<?> convertedClass = applyMonitor(converterName, targetClass);
        
        if(convertedClass == null)
        {
            return null;
        }
        
        Object convertedInstance = convertedClass.newInstance();
        return convertedInstance;
    }
    
    /**
     * �w�肵���N���X�̃��\�b�h���A�z�肵�Ă���񐔃R�[������Ă��邩���肷��B
     * 
     * @param clazz      ����Ώۂ̃N���X�̃N���X���
     * @param methodName ����Ώۂ̃��\�b�h��
     * @param num        �Ăяo�����񐔂̊��Ғl
     */
    public static void assertRecordCallNum(Class<?> clazz, String methodName, int num)
    {
        int makeCallNode = MockObjectManager.getCallCount(CallTreeRecorder.class, "addCallTreeNode");
        
        List<CallTreeNode> nodeList = new ArrayList<CallTreeNode>();
        
        for(int cnt = 0; cnt < makeCallNode; cnt ++)
        {
            nodeList.add(
                (CallTreeNode)MockObjectManager.getArgument(
                    CallTreeRecorder.class, "addCallTreeNode",cnt, 2));
        }

        int callCount = 0;
        
        for(CallTreeNode ite : nodeList)
        {
            String calledClass = ite.getInvocation().getClassName();
            String calledMethod = ite.getInvocation().getMethodName();
            
            if(clazz.getName().equals(calledClass) && methodName.equals(calledMethod))
            {
                callCount++;
            }
        }
        
        Assert.assertEquals(num, callCount);
    }
    
    /**
     * private�t�B�[���h�ȂǁA�A�N�Z�X�ł��Ȃ��t�B�[���h�ɑ΂��āA�l��ݒ肷��B
     * static private�ɑ΂��ẴA�N�Z�X�͕s�\�B
     * 
     * @param clazz     �ݒ�Ώۂ̃N���X�̃N���X���
     * @param fieldName �ݒ�Ώۂ̃t�B�[���h��
     * @param instance  �ݒ�Ώۂ̃C���X�^���X
     * @param value     �ݒ肷��l
     */
    public static void setNonAccessibleField(
        Class<?> clazz, String fieldName, Object instance, Object value)
    {
        if (instance != null && clazz.equals(instance.getClass()) == false)
        {
            throw new RuntimeException("�ݒ�ΏۃN���X�̃C���X�^���X����v���܂���");
        }
        
        try
        {
            Field targetField = clazz.getDeclaredField(fieldName);
            targetField.setAccessible(true);
            targetField.set(instance, value);
        }
        catch(Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * private�t�B�[���h�ȂǁA�A�N�Z�X�ł��Ȃ��t�B�[���h�̒l���擾����B
     * 
     * @param clazz     �ݒ�ΏۃN���X�̃N���X���
     * @param fieldName �ݒ�Ώۂ̃t�B�[���h��
     * @param instance  �ݒ�Ώۂ̃C���X�^���X
     * @return          �ݒ肷��l
     */
    public static Object getNonAccessibleField(
        Class<?> clazz, String fieldName, Object instance)
    {
        if (instance != null && clazz.equals(instance.getClass()) == false)
        {
            throw new RuntimeException("�擾�ΏۃN���X�̃C���X�^���X����v���܂���");
        }
        
        Object result = null;
        try
        {
            Field targetField = clazz.getDeclaredField(fieldName);
            targetField.setAccessible(true);
            result = targetField.get(instance);
        }
        catch(Exception ex)
        {
            throw new RuntimeException(ex);
        }
        
        return result;
    }
    
    /**
     * private���\�b�h�ȂǁA���ڃA�N�Z�X�ł��Ȃ����\�b�h�����s����B
     * 
     * @param clazz      �Ώۂ̃��\�b�h����`����Ă���N���X�̃N���X���
     * @param methodName �Ăяo���Ώۂ̃��\�b�h�̖���
     * @param instance   ���\�b�h���Ăяo���C���X�^���X�Bstatic���\�b�h�̏ꍇ��null�B
     * @param params     ���\�b�h�Ɏw�肷��p�����[�^(�v���~�e�B�u�^�̏ꍇ�̓��b�p�[�N���X���g�p����)
     * @return�@�w�肵�����\�b�h�̌ďo����
     */
    public static Object invokeNonAccessibleMethod(
        Class<?> clazz, String methodName, Object instance, Object ... params)
    {
        List<Class<?>> targetParamTypes = new ArrayList<Class<?>>();
        
        if(params != null)
        {
            for(Object param : params)
            {
                targetParamTypes.add(param.getClass());
            }
        }
        Object retVal = null;
        
        try
        {
            Method targetMethod = getParamTypesMatchMethod(clazz, methodName, params);
            targetMethod.setAccessible(true);
            retVal = targetMethod.invoke(instance, params);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
        
        return retVal;
    }
    
    /**
     * �w�肵�������ɁA�^���Ή����郁�\�b�h���擾����B
     * 
     * @param clazz      ���\�b�h����������^
     * @param methodName ���\�b�h�̖��O
     * @param params     ���\�b�h�ɓK�p�������
     * @return �K���������\�b�h�B���t����Ȃ������ꍇ��null�B
     */
    private static Method getParamTypesMatchMethod(Class<?> clazz, String methodName, Object ... params)
    {
        Method[] methods = clazz.getDeclaredMethods();
        
        for(Method method : methods)
        {
            if(!method.getName().equals(methodName))
            {
                continue;
            }
            
            Class<?>[] paramTypes = method.getParameterTypes();

            if(paramTypes.length != params.length)
            {
                continue;
            }
            
            boolean matchParamType = true;
            for(int index = 0; index < params.length; index ++)
            {
                if(!paramTypes[index].isInstance(params[index]))
                {
                    matchParamType = false;
                    break;
                }
            }
            
            if(matchParamType)
            {
                return method;
            }
        }
        
        return null;
    }
    
    
    /**
     * �ݒ�t�@�C�����ɂ���include�ݒ��񂩂�A�w�肵���R���o�[�^�^�ΏۃN���X�ƍ��v����ݒ��
     * �擾����B
     * @param convertedTargetName �R���o�[�g�ΏۃN���X
     * @param convertClassName    �R���o�[�^�̃N���X
     * @param pool                �N���X�v�[��
     * @param convertCtClass      �R���o�[�g�ΏۃN���X�̃N���X�t�@�C�����
     * @param transformConfig     �t�@�C������ǂݍ��񂾐ݒ���
     * @return �����ɍ��v����include�ݒ���
     */
    private static IncludeConversionConfig findMatchConversionConfig(
            String    convertedTargetName,
            String    convertClassName,
            ClassPool pool,
            CtClass   convertCtClass,
            JavelinTransformConfig transformConfig)
    {
        List<IncludeConversionConfig> includeClassList = 
            transformConfig.matchesToInclude(convertedTargetName, convertCtClass, pool);
        
        for(IncludeConversionConfig inConfig : includeClassList)
        {
            List<String> converterList = inConfig.getConverterNameList();
            
            for(String converterId : converterList)
            {
                List<String> converterClassList = transformConfig.getConverterClassNames(converterId);
            
                if(converterClassList.contains(convertClassName))
                {
                    return inConfig;
                }
            }
        }
        
        return null;
    }

    /**
     * �w�肳�ꂽ�N���X����̑��΃p�X�Ŏw�肳�ꂽ�t�@�C���܂��̓f�B���N�g�����A��΃p�X�ɕϊ����܂��B
     *
     * @param clazz ��N���X
     * @param relative ���΃p�X
     * @return ��΃p�X
     */
    public static String getAbsolutePath(final Class<?> clazz, final String relative)
    {
        URL url = clazz.getResource(relative);
        String absolutePath = url.getFile().replaceAll("^/+", "");
        return absolutePath;
    }
}
