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
package jp.co.acroquest.endosnipe.javelin.communicate.telegram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.entity.ResourceItem;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.TelegramListener;
import jp.co.acroquest.endosnipe.communicator.accessor.ResourceNotifyAccessor;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.ResponseBody;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.javelin.common.ConfigUpdater;
import jp.co.acroquest.endosnipe.javelin.resource.MultiResourceGetter;
import jp.co.acroquest.endosnipe.javelin.resource.ResourceCollector;
import jp.co.acroquest.endosnipe.javelin.resource.ResourceGroupGetter;

/**
 * �V�X�e�����\�[�X�擾�B
 * 
 * @author Sakamoto
 */
public class SystemResourceTelegramListener implements TelegramListener, TelegramConstants
{

    private final ResourceCollector resourceCollector_;
    
    /** Javelin�̐ݒ�B */
    private final JavelinConfig javelinConfig_ = new JavelinConfig();
    
    /** �V�X�e���̃��\�[�X�f�[�^�̖��O�̃��X�g�B */
    private final Set<String> systemResourceItemNameSet_ = new HashSet<String>();

    /** HadoopAgent����擾���郊�\�[�X���̃��X�g�B */
    private final Set<String> hadoopAgentItemNameSet_ = new HashSet<String>();

    /**
     * �V�X�e�����\�[�X�擾�I�u�W�F�N�g������������B
     */
    public SystemResourceTelegramListener()
    {
        this.resourceCollector_ = ResourceCollector.getInstance();
        
        // �V�X�e���̃��\�[�X�f�[�^�̖��O�̃��X�g������������B
        systemResourceItemNameSet_.add(ITEMNAME_SYSTEM_MEMORY_PHYSICAL_MAX);
        systemResourceItemNameSet_.add(ITEMNAME_SYSTEM_MEMORY_PHYSICAL_FREE);
        systemResourceItemNameSet_.add(ITEMNAME_SYSTEM_CPU_USERMODE_TIME);
        systemResourceItemNameSet_.add(ITEMNAME_SYSTEM_CPU_SYSTEM_TIME);
        systemResourceItemNameSet_.add(ITEMNAME_CPU_ARRAY);
        systemResourceItemNameSet_.add(ITEMNAME_SYSTEM_MEMORY_PAGEIN_COUNT);
        systemResourceItemNameSet_.add(ITEMNAME_SYSTEM_MEMORY_PAGEOUT_COUNT);
        systemResourceItemNameSet_.add(ITEMNAME_SYSTEM_HANDLE_TOTAL_NUMBER);
        systemResourceItemNameSet_.add(ITEMNAME_SYSTEM_MEMORY_SWAP_MAX);
        systemResourceItemNameSet_.add(ITEMNAME_SYSTEM_MEMORY_SWAP_FREE);
        
        // HadoopAgent����擾���郊�\�[�X���̃��X�g������������
        hadoopAgentItemNameSet_.add(ITEMNAME_HADOOP_NAMENODE);
        hadoopAgentItemNameSet_.add(ITEMNAME_HADOOP_JOBTRACKER);
        hadoopAgentItemNameSet_.add(ITEMNAME_HADOOP_DATANODE);
        hadoopAgentItemNameSet_.add(ITEMNAME_HADOOP_TASKTRACKER);
        hadoopAgentItemNameSet_.add(ITEMNAME_HBASE_HMASTER);
        hadoopAgentItemNameSet_.add(ITEMNAME_HBASE_HREGIONSERVER);
    }

    /**
     * {@inheritDoc}
     */
    public Telegram receiveTelegram(final Telegram telegram)
    {
        //�@�N���C�A���g�̏������������ǂ�����\���t���O
        boolean isInitializing = false;
        Telegram responseTelegram = null;
        Header header = telegram.getObjHeader();
        if (header.getByteTelegramKind() == BYTE_TELEGRAM_KIND_RESOURCENOTIFY
                && header.getByteRequestKind() == BYTE_REQUEST_KIND_REQUEST)
        {
            List<Body> responseBodyList = new ArrayList<Body>();

            // ������ǉ�����B
            long currentTime = System.currentTimeMillis();
            ResponseBody timeBody = ResourceNotifyAccessor.makeTimeBody(currentTime);
            responseBodyList.add(timeBody);
            
            Map<String, MultiResourceGetter> mrgMap = new HashMap<String, MultiResourceGetter>();

            // ProcParser �� load����
            Body[] objBodies = telegram.getObjBody();
            this.resourceCollector_.load();
            
         // �ݒ�X�V�v��������Δ��f����B
            ConfigUpdater.executeScheduledRequest();
            
            for (Body body : objBodies)
            {
                String objectName = body.getStrObjName();
                String itemName = body.getStrItemName();
                if (OBJECTNAME_RESOURCE.equals(objectName) == true)
                {
                    if (ITEMNAME_INITIALIZE.equals(itemName))
                    {
                        isInitializing = true;
                        continue;
                    }

                    if (javelinConfig_.getCollectSystemResources()
                            || systemResourceItemNameSet_.contains(itemName) == false)
                    {
                        Number value = this.resourceCollector_.getResource(itemName);
                        ItemType itemType = this.resourceCollector_.getResourceType(itemName);
                        
                        if (value != null)
                        {
                            ResponseBody responseBody =
                                ResourceNotifyAccessor.makeResourceResponseBody(itemName,
                                                                           value, itemType);
                            responseBodyList.add(responseBody);
                        }
                    }

                    ItemType multiItemType = this.resourceCollector_.getMultiResourceType(itemName);
                    MultiResourceGetter multiGetter =
                            this.resourceCollector_.getMultiResourceGetterMap().get(itemName);
                    List<ResourceItem> entries = null;
                    if (multiGetter != null)
                    {
                        try
                        {
                            entries = multiGetter.getValues();
                        }
                        catch (Throwable th)
                        {
                            SystemLogger.getInstance().debug(th);
                        }
                    }

                    if (entries != null)
                    {
                        addToBodyList(entries, responseBodyList, objectName, itemName,
                                      multiItemType);
                    }

                    // ResourceGroupGetter��������擾����
                    List<ResourceGroupGetter> resourceGroupGetterList =
                            this.resourceCollector_.getResourceGroupGetterList();
                    for (ResourceGroupGetter group : resourceGroupGetterList)
                    {
                        Set<String> itemNames = group.getItemNameSet();
                        // ResourceGroupGetter���n��f�[�^�������Ă���ꍇ
                        if (itemNames.contains(itemName))
                        {
                            // ResourceGroupGetter������MultiResourceGetter��
                            // �n��f�[�^�擾�p�̃e���|�����}�b�v�ɓo�^����Ă��Ȃ��ꍇ�A
                            // �e���|�����}�b�v�ɓo�^����
                            if (!mrgMap.containsKey(itemName))
                            {
                                Map<String, MultiResourceGetter> map = group.getResourceGroup();
                                mrgMap.putAll(map);
                            }

                            // MultiResourceGetter���擾����
                            MultiResourceGetter mg = mrgMap.get(itemName);
                            entries = mg.getValues();
                            ItemType mgItemType = mg.getItemType();
                            if (entries != null)
                            {
                                addToBodyList(entries, responseBodyList, objectName, itemName,
                                              mgItemType);
                            }
                        }
                    }
                }
            }
            
            // �N���C�A���g�̏������������ɂ́A��̓d����Ԃ��A
            // ����ȊO�̏ꍇ��ResourceCollector����擾�������ʂ�Ԃ��B
            if (isInitializing == true)
            {
                responseBodyList = new ArrayList<Body>();
            }
            else
            {
                addSingleResourceItem(responseBodyList);
                addMultiResourceItem(responseBodyList, OBJECTNAME_RESOURCE);
                addResourceGroupItem(responseBodyList, OBJECTNAME_RESOURCE);
            }
            responseTelegram = ResourceNotifyAccessor.makeResponseTelegram(responseBodyList);    
        }

        return responseTelegram;
    }

    private void addResourceGroupItem(List<Body> responseBodyList, String objectName)
    {
        List<ResourceItem> entries = null;
        // ResourceGroupGetter��������擾����
        List<ResourceGroupGetter> rggList = this.resourceCollector_.getResourceGroupGetterList();
        for (ResourceGroupGetter group : rggList)
        {
            for (Map.Entry<String, MultiResourceGetter> entry : group.getResourceGroup().entrySet())
            {
                String itemName = entry.getKey();
                MultiResourceGetter multiResourceGetter = entry.getValue();
                entries = multiResourceGetter.getValues();
                ItemType mgItemType = multiResourceGetter.getItemType();
                if (entries != null)
                {
                    addToBodyList(entries, responseBodyList, objectName, itemName,
                                                   mgItemType);
                }

            }
        }
    }

    private void addMultiResourceItem(List<Body> responseBodyList, String objectName)
    {
        for (String itemName : this.resourceCollector_.getMultiResourceItemId())
        {
                // �����n��������ږ�
                ItemType multiItemType = this.resourceCollector_.getMultiResourceType(itemName);
                MultiResourceGetter multiGetter =
                        this.resourceCollector_.getMultiResourceGetterMap().get(itemName);
                List<ResourceItem> entries = null;
                if (multiGetter != null)
                {
                    try
                    {
                        entries = multiGetter.getValues();
                    }
                    catch (Throwable th)
                    {
                        SystemLogger.getInstance().debug(th);
                    }
                }

                if (entries != null)
                {
                    addToBodyList(entries, responseBodyList, objectName, itemName,
                                  multiItemType);
                }
        }
    }

    private void addSingleResourceItem(List<Body> responseBodyList)
    {
        for (String itemName : this.resourceCollector_.getResourceItemId())
        {
        
            // �V�X�e�����\�[�X�̎擾���ڂł���ꍇ
            // ���v���p�e�B�Ŏ擾����w�肪�Ȃ���Ă���ꍇ�̂ݎ��s����
            if (javelinConfig_.getCollectSystemResources()
                    || systemResourceItemNameSet_.contains(itemName) == false)
            {
                Number value = this.resourceCollector_.getResource(itemName);
                ItemType itemType = this.resourceCollector_.getResourceType(itemName);
                
                if (value != null)
                {
                    ResponseBody responseBody =
                        ResourceNotifyAccessor.makeResourceResponseBody(itemName,
                                                                   value, itemType);
                    responseBodyList.add(responseBody);
                }
            }
        }
    }

    /**
     * �����Ɏw�肵�����X�g��HadoopGetter����n��f�[�^��ݒ肷��B
     * 
     * @param entries
     * @param responseBodyList
     * @param objectName
     * @param itemName
     * @param itemType
     */
    private void addToBodyList(final List<ResourceItem> entries,
            final List<Body> responseBodyList, final String objectName, final String itemName,
            final ItemType itemType)
    {
        String[] values = new String[entries.size()];
        String[] names = new String[entries.size()];

        for (int index = 0; index < values.length; index++)
        {
            values[index] = entries.get(index).getValue();
            names[index] = entries.get(index).getName();
            
            // �l��ǉ�����B
            ResponseBody valueBody = new ResponseBody();
            valueBody.setStrObjName(objectName);
            valueBody.setStrItemName(names[index]);
            valueBody.setIntLoopCount(1);
            valueBody.setByteItemMode(itemType);
            valueBody.setObjItemValueArr(new Object[] { values[index] });
            responseBodyList.add(valueBody);

            // �n�񖼂�ǉ�����B
            ResponseBody nameBody = new ResponseBody();
            nameBody.setStrObjName(objectName);
            nameBody.setStrItemName(names[index] + "-name");
            nameBody.setIntLoopCount(1);
            nameBody.setByteItemMode(ItemType.ITEMTYPE_STRING);
            nameBody.setObjItemValueArr(new Object[] { names[index] });
            responseBodyList.add(nameBody);
        }
    }
}
