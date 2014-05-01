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
package jp.co.acroquest.endosnipe.javelin.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.acroquest.endosnipe.common.config.JavelinConfig;
import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.jmx.JMXManager;
import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.javelin.converter.leak.monitor.ClassHistogramMonitor;
import jp.co.acroquest.endosnipe.javelin.converter.leak.monitor.SunClassHistogramMonitor;
import jp.co.acroquest.endosnipe.javelin.converter.net.NetMonitorConstants;
import jp.co.acroquest.endosnipe.javelin.resource.jmx.MBeanCollectorInitializer;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxCpuArrayGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxCpuIoWaitGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxCpuSystemGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxCpuTimeIoWaitGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxCpuTimeSysGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxCpuTimeTotalGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxCpuTotalGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxMajfltGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxMemFreeGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxMemTotalGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxNumThreadsGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxPageInGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxPageOutGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxProcFileInputGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxProcFileOutputGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxProcParser;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxRSSGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxSwapFreeGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxSwapTotalGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxSystemFileInputGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxSystemFileOutputGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.LinuxVSizeGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.ProcFdCountGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.ProcParser;
import jp.co.acroquest.endosnipe.javelin.resource.proc.SolarisProcParser;
import jp.co.acroquest.endosnipe.javelin.resource.proc.SysFdCountGetter;
import jp.co.acroquest.endosnipe.javelin.resource.proc.WindowsProcParser;
import jp.co.acroquest.endosnipe.javelin.resource.sun.CpuTimeGetter;
import jp.co.acroquest.endosnipe.javelin.resource.sun.PhysicalMemoryCapacityGetter;
import jp.co.acroquest.endosnipe.javelin.resource.sun.PhysicalMemoryFreeGetter;
import jp.co.acroquest.endosnipe.javelin.resource.sun.SwapSpaceCapacityGetter;
import jp.co.acroquest.endosnipe.javelin.resource.sun.SwapSpaceFreeGetter;
import jp.co.acroquest.endosnipe.javelin.resource.sun.VirutalMemorySizeGetter;

/**
 * ���\�[�X�������W����N���X�B
 * 
 * getMultiResourcegetterMap�AgetResourceGroupGetterList�́A ���t�@�N�^���A���̂܂܌��J���Ȃ��l�ɂ���ׂ��ł����A���Ή��ł��B
 * 
 * @author eriguchi
 * @author ochiai
 */
public class ResourceCollector implements TelegramConstants
{
	/** �x���_�[��IBM�ł��邱�Ƃ�\�������� */
	public static final String VENDER_IBM = "IBM";

	/** �x���_�[��BEA�ł��邱�Ƃ�\�������� */
	public static final String VENDER_BEA = "BEA";

	/** OS��Linux�ł��邱�Ƃ�\�������� */
	private static final String OS_LINUX = "Linux";

	/** �x���_�[��Oracle�ł��邱�Ƃ�\�������� */
	public static final String VENDER_ORACLE = "Oracle";

	/** OS��Windows�ł��邱�Ƃ�\�������� */
	private static final String OS_WINDOWS = "Windows";

	/** OS��Solaris�ł��邱�Ƃ�\�������� */
	private static final String OS_SOLARIS = "SunOS";

	/** ���\�[�X�擾�I�u�W�F�N�g�̃}�b�v */
	private final Map<String, ResourceGetter> resourceGetterMap_;

	/** ���\�[�X�擾�I�u�W�F�N�g�̃}�b�v */
	private final Map<String, MultiResourceGetter> multiResourceGetterMap_;

	/** �O���[�v�����ꂽ�������\�[�X�擾�I�u�W�F�N�g�̃��X�g */
	private final List<ResourceGroupGetter> resourceGroupGetterList_;

	private static ResourceCollector instance__ = new ResourceCollector();

	/** Windows �܂��� Linux �Ń��\�[�X�����擾���邽�߂ɗp���� */
	private ProcParser procParser_ = null;

	private ResourceCollector()
	{
		Map<String, ResourceGetter> resourceMap = new HashMap<String, ResourceGetter>();
		Map<String, MultiResourceGetter> mResourceMap = new HashMap<String, MultiResourceGetter>();

		try
		{
			this.procParser_ = createProcParser();
			SystemLogger.getInstance().info("ProcParser not found. Default parser selected.");
			setResouceGetters(resourceMap, mResourceMap, this.procParser_);
		}
		catch (Throwable th)
		{
			SystemLogger.getInstance().warn(th);
		}

		this.resourceGroupGetterList_ = new ArrayList<ResourceGroupGetter>();
		this.resourceGroupGetterList_.add(new TurnAroundTimeGroupGetter());

		this.resourceGetterMap_ = resourceMap;
		this.multiResourceGetterMap_ = mResourceMap;

	}

	/**
	 * {@link ProcParser} �C���X�^���X�𐶐����܂��B
	 * 
	 * @return ���������ꍇ�̓C���X�^���X�A�Ή����Ă��Ȃ�OS�̏ꍇ�� <code>null</code>
	 */
	public static ProcParser createProcParser()
	{
		ProcParser procParser = null;
		if (System.getProperty("os.name").contains(OS_LINUX))
		{
			procParser = new LinuxProcParser();
		}
		else if (System.getProperty("os.name").contains(OS_WINDOWS))
		{
			procParser = new WindowsProcParser();
		}
		else if (System.getProperty("os.name").contains(OS_SOLARIS))
		{
			procParser = new SolarisProcParser();
		}
		if (procParser != null && procParser.init() == false)
		{
			procParser = null;
		}

		return procParser;
	}

	/**
	 * ���\�[�X�擾�C���X�^���X���}�b�v�ɓo�^���܂��B
	 * 
	 * @param resourceMap
	 *            ���\�[�X�擾�C���X�^���X��o�^����}�b�v
	 * @param multiResourceMap
	 *            ���\�[�X�擾�C���X�^���X��o�^����}�b�v�i�όn��p�j
	 * @param procParser
	 *            ���\�[�X�擾�x�[�X�C���X�^���X
	 */
	public static void setResouceGetters(Map<String, ResourceGetter> resourceMap,
			Map<String, MultiResourceGetter> multiResourceMap, ProcParser procParser)
	{
		ClassHistogramMonitor historgramMonitor = null;
		String vendor = System.getProperty("java.vendor");
		if (vendor != null)
		{
			resourceMap.put(ITEMNAME_PROCESS_CPU_TOTAL_TIME, new CpuTimeGetter());
			resourceMap.put(ITEMNAME_PROCESS_MEMORY_PHYSICAL_MAX,
					new PhysicalMemoryCapacityGetter());
			resourceMap.put(ITEMNAME_PROCESS_MEMORY_PHYSICAL_FREE, new PhysicalMemoryFreeGetter());
			resourceMap.put(ITEMNAME_SYSTEM_MEMORY_SWAP_MAX, new SwapSpaceCapacityGetter());
			resourceMap.put(ITEMNAME_SYSTEM_MEMORY_SWAP_FREE, new SwapSpaceFreeGetter());
			resourceMap.put(ITEMNAME_SYSTEM_MEMORY_VIRTUAL_USED, new VirutalMemorySizeGetter());
			multiResourceMap.put(ITEMNAME_SERVER_POOL, new TomcatPoolCounter());
			historgramMonitor = new SunClassHistogramMonitor();
		}

		resourceMap.put(ITEMNAME_ACQUIREDTIME, new TimeGetter());
		resourceMap.put(ITEMNAME_JAVAUPTIME, new JavaUpTimeGetter());
		resourceMap.put(ITEMNAME_SYSTEM_CPU_PROCESSOR_COUNT, new ProcessorCountGetter());
		resourceMap.put(ITEMNAME_PROCESS_MEMORY_VIRTUALMACHINE_MAX,
				new VirtualMachineCapacityGetter());
		VirtualMachineFreeGetter virturalMachineFreeGetter = new VirtualMachineFreeGetter();
		resourceMap.put(ITEMNAME_PROCESS_MEMORY_VIRTUALMACHINE_FREE, virturalMachineFreeGetter);

		resourceMap.put(ITEMNAME_JAVAPROCESS_MEMORY_HEAP_COMMIT, new HeapMemoryCommittedGetter());
		resourceMap.put(ITEMNAME_JAVAPROCESS_MEMORY_HEAP_USED, new HeapMemoryUsedGetter());
		resourceMap.put(ITEMNAME_JAVAPROCESS_MEMORY_HEAP_MAX, new HeapMemoryMaxGetter());
		resourceMap.put(ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_MAX, new NonHeapMemoryMaxGetter());
		NonHeapMemoryCommittedGetter nonHeapMemoryCommitted = new NonHeapMemoryCommittedGetter();
		resourceMap.put(ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_COMMIT, nonHeapMemoryCommitted);
		resourceMap.put(ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_USED, new NonHeapMemoryUsedGetter());
		resourceMap.put(ITEMNAME_JAVAPROCESS_GC_FINALIZEQUEUE_COUNT, new FinalizationCountGetter());

		resourceMap.put(ITEMNAME_JAVAPROCESS_THREAD_TOTAL_COUNT, new ThreadCountGetter());
		resourceMap.put(ITEMNAME_JAVAPROCESS_GC_TIME_TOTAL, new GCTotalTimeGetter());

		resourceMap.put(ITEMNAME_CONVERTEDMETHOD, new ConvertedMethodCountGetter());
		resourceMap.put(ITEMNAME_EXCLUDEDMETHOD, new ExcludedMethodCountGetter());

		SystemStatusValueGetter networkReadMonitor = new SystemStatusValueGetter(
				NetMonitorConstants.KEY_NETWORK_READ_LENGTH);
		SystemStatusValueGetter networkWriteMonitor = new SystemStatusValueGetter(
				NetMonitorConstants.KEY_NETWORK_WRITE_LENGTH);
		resourceMap.put(ITEMNAME_NETWORKINPUTSIZEOFPROCESS, networkReadMonitor);
		resourceMap.put(ITEMNAME_NETWORKOUTPUTSIZEOFPROCESS, networkWriteMonitor);

		LoadedClassTotalCountGetter loadedClassTotalCountGetter = new LoadedClassTotalCountGetter();
		resourceMap.put(ITEMNAME_JAVAPROCESS_CLASSLOADER_CLASS_TOTAL, loadedClassTotalCountGetter);
		LoadedClassCountGetter loadedClassCountGetter = new LoadedClassCountGetter();
		resourceMap.put(ITEMNAME_JAVAPROCESS_CLASSLOADER_CLASS_CURRENT, loadedClassCountGetter);
		resourceMap.put(ITEMNAME_CALLEDMETHODCOUNT, new CalledMethodCountGetter());

		if (procParser != null)
		{
			resourceMap.put(ITEMNAME_SYSTEM_MEMORY_PHYSICAL_MAX,
					new LinuxMemTotalGetter(procParser));
			resourceMap.put(ITEMNAME_SYSTEM_MEMORY_PHYSICAL_FREE,
					new LinuxMemFreeGetter(procParser));
			resourceMap.put(ITEMNAME_SYSTEM_MEMORY_SWAP_MAX, new LinuxSwapTotalGetter(procParser));
			resourceMap.put(ITEMNAME_SYSTEM_MEMORY_SWAP_FREE, new LinuxSwapFreeGetter(procParser));
			resourceMap.put(ITEMNAME_SYSTEM_CPU_USERMODE_TIME, new LinuxCpuTotalGetter(procParser));
			resourceMap.put(ITEMNAME_SYSTEM_CPU_SYSTEM_TIME, new LinuxCpuSystemGetter(procParser));
			resourceMap.put(ITEMNAME_SYSTEM_CPU_IOWAIT_TIME, new LinuxCpuIoWaitGetter(procParser));
			resourceMap.put(ITEMNAME_CPU_ARRAY, new LinuxCpuArrayGetter(procParser));
			resourceMap.put(ITEMNAME_SYSTEM_MEMORY_PAGEIN_COUNT, new LinuxPageInGetter(procParser));
			resourceMap.put(ITEMNAME_SYSTEM_MEMORY_PAGEOUT_COUNT,
					new LinuxPageOutGetter(procParser));
			LinuxCpuTimeSysGetter linuxCpuTimeSysGetter = new LinuxCpuTimeSysGetter(procParser);
			resourceMap.put(ITEMNAME_PROCESS_CPU_SYSTEM_TIME, linuxCpuTimeSysGetter);
			resourceMap.put(ITEMNAME_PROCESS_CPU_IOWAIT_TIME, new LinuxCpuTimeIoWaitGetter(
					procParser));
			resourceMap.put(ITEMNAME_PROCESS_CPU_TOTAL_TIME,
					new LinuxCpuTimeTotalGetter(procParser));
			resourceMap.put(ITEMNAME_PROCESS_MEMORY_VIRTUAL_USED, new LinuxVSizeGetter(procParser));
			resourceMap.put(ITEMNAME_PROCESS_MEMORY_PHYSICAL_USED, new LinuxRSSGetter(procParser));
			resourceMap.put(ITEMNAME_PROCESS_THREAD_TOTAL_COUNT, new LinuxNumThreadsGetter(
					procParser));
			resourceMap.put(ITEMNAME_PROCESS_MEMORY_MAJORFAULT_COUNT, new LinuxMajfltGetter(
					procParser));
			ProcFdCountGetter procFdCountGetter = new ProcFdCountGetter(procParser);
			resourceMap.put(ITEMNAME_PROCESS_HANDLE_TOTAL_NUMBER, procFdCountGetter);
			resourceMap.put(ITEMNAME_SYSTEM_HANDLE_TOTAL_NUMBER, new SysFdCountGetter(procParser));

			LinuxProcFileInputGetter pInputGetter = new LinuxProcFileInputGetter(procParser);
			resourceMap.put(ITEMNAME_FILEINPUTSIZEOFPROCESS, pInputGetter);
			LinuxProcFileOutputGetter pOutputGetter = new LinuxProcFileOutputGetter(procParser);
			resourceMap.put(ITEMNAME_FILEOUTPUTSIZEOFPROCESS, pOutputGetter);
			LinuxSystemFileInputGetter sInputGetter = new LinuxSystemFileInputGetter(procParser);
			resourceMap.put(ITEMNAME_FILEINPUTSIZEOFSYSTEM, sInputGetter);
			LinuxSystemFileOutputGetter sOutputGetter = new LinuxSystemFileOutputGetter(procParser);
			resourceMap.put(ITEMNAME_FILEOUTPUTSIZEOFSYSTEM, sOutputGetter);
		}

		// JMX�̃��\�[�X�f�[�^���擾���邩�ǂ���
		JavelinConfig config = new JavelinConfig();
		if (config.getCollectJmxResources())
		{
			try
			{
				MBeanCollectorInitializer.init(multiResourceMap);
			}
			catch (Exception e)
			{
				SystemLogger.getInstance().warn(e);
			}
			JMXManager.getInstance().initCompleted();
		}

		multiResourceMap.put(ITEMNAME_JAVAPROCESS_COLLECTION_LIST_COUNT, new ListCountGetter());
		multiResourceMap.put(ITEMNAME_JAVAPROCESS_COLLECTION_QUEUE_COUNT, new QueueCountGetter());
		multiResourceMap.put(ITEMNAME_JAVAPROCESS_COLLECTION_SET_COUNT, new SetCountGetter());
		multiResourceMap.put(ITEMNAME_JAVAPROCESS_COLLECTION_MAP_COUNT, new MapCountGetter());

		if (historgramMonitor != null)
		{
			multiResourceMap.put(ITEMNAME_JAVAPROCESS_COLLECTION_HISTOGRAM_SIZE,
					new ClassHistogramSizeGetter(historgramMonitor));
			multiResourceMap.put(ITEMNAME_JAVAPROCESS_COLLECTION_HISTOGRAM_COUNT,
					new ClassHistogramCountGetter(historgramMonitor));
		}

		multiResourceMap.put(ITEMNAME_POOL_SIZE, new PoolSizeGetter());
		multiResourceMap.put(ITEMNAME_NODECOUNT, new CallTreeNodeCountGetter());
		multiResourceMap.put(ITEMNAME_EVENT_COUNT, new EventCountGetter());

	}

	/**
	 * �����̃��\�[�X��ǉ����܂��B
	 * 
	 * @param itemName
	 *            ���ږ�
	 * @param multiResourceGetter
	 *            �}���`���\�[�X�Q�b�^�[
	 */
	public void addMultiResource(String itemName, MultiResourceGetter multiResourceGetter)
	{
		synchronized (this.multiResourceGetterMap_)
		{
			this.multiResourceGetterMap_.put(itemName, multiResourceGetter);
		}
	}

	/**
	 * �P�̂̃��\�[�X��ǉ����܂��B
	 * 
	 * @param itemName
	 *            ���ږ�
	 * @param resourceGetter
	 *            ���\�[�X�Q�b�^�[
	 */
	public void addSingleResource(String itemName, ResourceGetter resourceGetter)
	{
		synchronized (this.resourceGetterMap_)
		{
			this.resourceGetterMap_.put(itemName, resourceGetter);
		}
	}

	/**
	 * �C���X�^���X���擾���܂��B
	 * 
	 * @return �C���X�^���X�B
	 */
	public static ResourceCollector getInstance()
	{
		return instance__;
	}

	/**
	 * �w�肵�����\�[�X�����擾���܂��B
	 * 
	 * @param itemName
	 *            ���\�[�X�̖��́B
	 * @return ���\�[�X���B
	 */
	public Number getResource(String itemName)
	{
		ResourceGetter getter = this.resourceGetterMap_.get(itemName);
		Number value = null;
		if (getter != null && getter.isEnable() == true)
		{
			try
			{
				value = getter.getValue();
			}
			catch (Throwable th)
			{
				SystemLogger.getInstance().debug(th);
			}
		}

		return value;
	}

	/**
	 * ���\�[�X�擾�I�u�W�F�N�g�̃}�b�v���擾����B
	 * 
	 * @return ���\�[�X�擾�I�u�W�F�N�g�̃}�b�v
	 */
	public Map<String, ResourceGetter> getResourceGetterMap()
	{
		return Collections.unmodifiableMap(resourceGetterMap_);
	}

	/**
	 * ���\�[�X�擾�I�u�W�F�N�g�̃}�b�v���擾���܂��B
	 * 
	 * @return ���\�[�X�擾�I�u�W�F�N�g�̃}�b�v�B
	 */
	public Map<String, MultiResourceGetter> getMultiResourceGetterMap()
	{
		return multiResourceGetterMap_;
	}

	/**
	 * ���\�[�X�擾�I�u�W�F�N�g�̃��X�g���擾���܂��B
	 * 
	 * @return ���\�[�X�擾�I�u�W�F�N�g�̃}�b�v�B
	 */
	public List<ResourceGroupGetter> getResourceGroupGetterList()
	{
		return resourceGroupGetterList_;
	}

	/**
	 * ��ʂ��擾���܂��B
	 * 
	 * @param itemName
	 *            ���\�[�X�̖��́B
	 * @return ��ʁB
	 */
	public ItemType getResourceType(String itemName)
	{
		ResourceGetter getter = this.resourceGetterMap_.get(itemName);
		if (getter != null && getter.isEnable() == true)
		{
			return getter.getItemType();
		}
		return ItemType.ITEMTYPE_UNKNOWN;
	}

	/**
	 * �����n��̎�ʂ��擾���܂��B
	 * 
	 * @param itemName
	 *            ���\�[�X�̖��́B
	 * @return ��ʁB
	 */
	public ItemType getMultiResourceType(String itemName)
	{
		MultiResourceGetter getter = this.multiResourceGetterMap_.get(itemName);
		if (getter != null)
		{
			return getter.getItemType();
		}
		return ItemType.ITEMTYPE_UNKNOWN;
	}

	/**
	 * �����n���itemID���擾���܂��B
	 * 
	 * @return �����n���itemID�B
	 */
	public Set<String> getMultiResourceItemId()
	{
		return this.multiResourceGetterMap_.keySet();
	}

	/**
	 * �n���itemID���擾���܂��B
	 * 
	 * @return �n���itemID�B
	 */
	public Set<String> getResourceItemId()
	{
		return this.resourceGetterMap_.keySet();
	}

	/**
	 * procParser �� load() ���\�b�h���Ăяo���ď���������
	 */
	public void load()
	{
		if (this.procParser_ != null)
		{
			this.procParser_.load();
		}
	}
}
