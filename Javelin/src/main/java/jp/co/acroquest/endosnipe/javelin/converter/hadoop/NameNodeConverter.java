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
package jp.co.acroquest.endosnipe.javelin.converter.hadoop;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.javassist.CannotCompileException;
import jp.co.acroquest.endosnipe.javassist.CtBehavior;
import jp.co.acroquest.endosnipe.javassist.CtClass;
import jp.co.acroquest.endosnipe.javassist.CtMethod;
import jp.co.acroquest.endosnipe.javassist.NotFoundException;
import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.resource.ResourceCollector;
import jp.co.acroquest.endosnipe.javelin.resource.hadoop.HadoopNameNodeGetter;

/**
 * NameNode用のコンバータ
 * 
 * @author eriguchi
 */
public class NameNodeConverter extends AbstractConverter 
{
	/** コンバータ名 */
	private static final String CONVERTER_NAME = "NameNodeConverter";

	/**
	 * {@inheritDoc}
	 */
	public void init() 
	{
		ResourceCollector.getInstance().addMultiResource(
				TelegramConstants.ITEMNAME_HADOOP_NAMENODE,
				new HadoopNameNodeGetter());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void convertImpl() throws CannotCompileException, NotFoundException,
			IOException 
			{
		List<CtBehavior> behaviorList = getMatcheDeclaredBehavior();
		CtClass ctClass = getCtClass();
		if (behaviorList.size() > 0) 
		{
			// インターフェースの追加
			CtClass accessorClass = getClassPool().get(
					FSNamesystemAccessor.class.getName());
			ctClass.addInterface(accessorClass);
			
			String resolveReceiverName = "this.dnsToSwitchMapping";		
			String getDatanodesReceiverName = "";
			CtMethod[] ctMethods = ctClass.getMethods();
			for (CtMethod ctMethod: ctMethods) 
			{
				if ("getBlockManager".equals(ctMethod.getName()))
				{
					resolveReceiverName = "((" + DatanodeManagerAccessor.class.getCanonicalName() + ")getBlockManager().getDatanodeManager())";
					getDatanodesReceiverName = ".getBlockManager().getDatanodeManager()";
					break;
				}
			}
			String constantsClassName = "org.apache.hadoop.hdfs.protocol.FSConstants";
			String datanodeDescriptorName = "org.apache.hadoop.hdfs.server.namenode.DatanodeDescriptor";
			Collection<String> refClasses = ctClass.getRefClasses();
			if (refClasses.contains("org.apache.hadoop.hdfs.protocol.HdfsConstants")) 
			{
				constantsClassName = "org.apache.hadoop.hdfs.protocol.HdfsConstants";
			}
			if (refClasses.contains("org.apache.hadoop.hdfs.server.blockmanagement.DatanodeDescriptor"))
			{
				datanodeDescriptorName = "org.apache.hadoop.hdfs.server.blockmanagement.DatanodeDescriptor";
			}
			
			CtMethod getServerInfoMethod = CtMethod.make("" + //
					"public java.util.List resolve(java.util.List names){ " + //
					"        return " + resolveReceiverName + 
					".resolve(names);" + //
					"}", ctClass);
			ctClass.addMethod(getServerInfoMethod);

			CtMethod getHLCapacityRemainingMethod = CtMethod.make("" + //
					"public long getHLCapacityRemaining(){ " + //
					"        return this.getCapacityRemaining();" + //
					"}", ctClass);
			ctClass.addMethod(getHLCapacityRemainingMethod);

			CtMethod getHLCapacityUsedMethod = CtMethod.make("" + //
					"public long getHLCapacityUsed(){ " + //
					"        return this.getCapacityUsed();" + //
					"}", ctClass);
			ctClass.addMethod(getHLCapacityUsedMethod);

			CtMethod getDfsNodeInfoMethod = CtMethod
					.make(
							""+ //
							"public java.util.Map getDfsNodeInfo() {"+ //
							""+ //
							"	java.util.Map info = new java.util.HashMap();"+ //
							"	java.util.List aliveNodeList = this"+ //
								getDatanodesReceiverName+
							"			.getDatanodeListForReport(" + constantsClassName + ".DatanodeReportType.LIVE);"+ //
							"	for (java.util.Iterator iterator = aliveNodeList.iterator(); iterator.hasNext();) {"+ //
							"       " + datanodeDescriptorName + " node = (" + datanodeDescriptorName + ")iterator.next();"+ //
							"		jp.co.acroquest.endosnipe.javelin.converter.hadoop.DfsNodeInfo nodeInfo = new jp.co.acroquest.endosnipe.javelin.converter.hadoop.DfsNodeInfo();"+ //
							"		nodeInfo.setCapacity(node.getCapacity());" + //
							"		nodeInfo.setDfsUsed(node.getDfsUsed());" + //
							"		nodeInfo.setHostName(node.getHostName());" + //
							"" + //
							"		info.put(node.getHostName(), nodeInfo);" + //
							"	}" + //
							"" + //
							"	return info;" + //
							"}", ctClass);
			ctClass.addMethod(getDfsNodeInfoMethod);
		}

		for (CtBehavior ctBehavior : behaviorList) 
		{
			convertBehavior(ctBehavior);
		}

		setNewClassfileBuffer(ctClass.toBytecode());
	}

	/**
	 * メソッドの振る舞いを修正する。
	 * 
	 * @param ctBehavior
	 *            CtBehavior
	 * @throws CannotCompileException
	 *             コンパイルできない場合
	 * @throws NotFoundException
	 *             クラスが見つからない場合
	 */
	private void convertBehavior(final CtBehavior ctBehavior)
			throws CannotCompileException, NotFoundException 
			{
		ctBehavior.insertAfter(HadoopMeasurementInfo.class.getCanonicalName()
				+ ".getInstance().setFsNamesystem(this);");

		// 処理結果をログに出力する。
		logModifiedMethod(CONVERTER_NAME, ctBehavior);
	}

}
