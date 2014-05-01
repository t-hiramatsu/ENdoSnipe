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
package jp.co.acroquest.endosnipe.javelin.resource.sun;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import jp.co.acroquest.endosnipe.common.entity.ResourceItem;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;

/**
 * Tomcat�̃��[�J�X���b�h�̍ő吔�Ɖғ�����Ԃ��N���X
 * 
 * @author fujii
 * 
 */
public class TomcatPoolMonitor
{
    /** ���[�J�X���b�h�̃I�u�W�F�N�g�� */
    private static final String THREAD_POOL_STR = "*:type=ThreadPool,*";

    /**
     * �v���C�x�[�g�R���X�g���N�^
     */
    private TomcatPoolMonitor()
    {
        // Do Nothing.
    }

    /**
     * Tomcat�̃��[�J�X���b�h�̍ő吔�Ɖғ����𐔂���B
     * @return Tomcat�̃��[�J�X���b�h�̍ő吔�Ɖғ����̃��X�g
     */
    public static List<ResourceItem> getThreadCount()
    {
        // MBean�T�[�o���擾����B
        MBeanServer mBeanServer = getMBeanServer();

        if (mBeanServer == null)
        {
            return new ArrayList<ResourceItem>();
        }

        List<ResourceItem> list = new ArrayList<ResourceItem>();
        ObjectName queryObjectName = null;

        // ���[�J�X���b�h����ObjectName���擾����B
        try
        {
            queryObjectName = new ObjectName(THREAD_POOL_STR);
        }
        catch (MalformedObjectNameException ex)
        {
            SystemLogger.getInstance().warn(ex);
            return new ArrayList<ResourceItem>();
        }
        Set<?> set = mBeanServer.queryMBeans(queryObjectName, null);
        Iterator<?> iterator = set.iterator();
        List<ObjectName> threadPools = new ArrayList<ObjectName>();
        while (iterator.hasNext())
        {
            ObjectInstance oi = (ObjectInstance)iterator.next();
            threadPools.add(oi.getObjectName());
        }
        
        String poolPrefix = "/process/pool/";
        // �e�|�[�g���Ƃ̃��[�J�X���b�h�̍ő吔�A�ғ������擾����B
        for (ObjectName objectName : threadPools)
        {
            ResourceItem maxThreadEntry = new ResourceItem();
            ResourceItem currentThreadEntry = new ResourceItem();
            ResourceItem waitThreadEntry = new ResourceItem();
            try
            {
                String name = objectName.getKeyProperty("name");

                // ���[�J�X���b�h���̍ő吔���擾����B
                Number maxThreads = (Number)mBeanServer.getAttribute(objectName, "maxThreads");
                maxThreadEntry.setName(poolPrefix + name + "_max");
                maxThreadEntry.setValue(String.valueOf(maxThreads));

                // ���[�J�X���b�h���̉ғ������擾����B
                Number currentServerPool =
                        (Number)mBeanServer.getAttribute(objectName, "currentThreadsBusy");
                currentThreadEntry.setName(poolPrefix + name + "_current");
                currentThreadEntry.setValue(String.valueOf(currentServerPool));

                // �ҋ@���̃��[�J�X���b�h�����擾����B
                Number waitServerPool =
                        (Number)mBeanServer.getAttribute(objectName, "currentThreadCount");
                waitThreadEntry.setName(poolPrefix + name + "_wait");
                waitThreadEntry.setValue(String.valueOf(waitServerPool));

                list.add(maxThreadEntry);
                list.add(currentThreadEntry);
                list.add(waitThreadEntry);

            }
            catch (Exception ex)
            {
                SystemLogger.getInstance().warn(ex);
            }
        }
        return list;
    }

    /**
     * MBeanServer���擾����B
     * @return MBeanServer
     */
    private static synchronized MBeanServer getMBeanServer()
    {
        List<?> mbServers = MBeanServerFactory.findMBeanServer(null);
        ObjectName queryObjectName = null;
        try
        {
            queryObjectName = new ObjectName(THREAD_POOL_STR);
        }
        catch (MalformedObjectNameException ex)
        {
            SystemLogger.getInstance().warn(ex);
        }
        for (Object obj : mbServers)
        {
            MBeanServer server = (MBeanServer)obj;
            Set<?> set = server.queryMBeans(queryObjectName, null);
            if (set.size() != 0)
            {
                return server;
            }
        }
        return null;

    }
}
