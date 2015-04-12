package jp.co.acroquest.endosnipe.javelin.converter.infinispan;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.javassist.CannotCompileException;
import jp.co.acroquest.endosnipe.javassist.ClassPool;
import jp.co.acroquest.endosnipe.javassist.CtBehavior;
import jp.co.acroquest.endosnipe.javassist.CtClass;
import jp.co.acroquest.endosnipe.javassist.CtField;
import jp.co.acroquest.endosnipe.javassist.CtMethod;
import jp.co.acroquest.endosnipe.javassist.CtNewMethod;
import jp.co.acroquest.endosnipe.javassist.NotFoundException;
import jp.co.acroquest.endosnipe.javassist.expr.ExprEditor;
import jp.co.acroquest.endosnipe.javassist.expr.MethodCall;
import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.infinispan.monitor.MapReduceTaskAccessor;
import jp.co.acroquest.endosnipe.javelin.converter.infinispan.monitor.MapReduceTaskMonitor;
import jp.co.acroquest.endosnipe.javelin.resource.ResourceCollector;

/**
 * mapreduceのジョブ、タスク情報を取得するためのコードを埋め込む
 * 
 * @author hiramatsu
 *
 */
public class MapReduceTaskConverter extends AbstractConverter
{

    private CtClass throwableClass_;

    private static boolean converted__;

    private static final String MAP_REDUCE_TASK = "org.infinispan.distexec.mapreduce.MapReduceTask";

    private static final String RPC_MANAGER = "org.infinispan.remoting.rpc.RpcManager";

    private static final String MAP_REDUCE_COMMAND =
        "org.infinispan.commands.read.MapReduceCommand";

    private static final String MAP_REDUCE_MANAGER =
        "org.infinispan.distexec.mapreduce.MapReduceManager";

    @Override
    public void convertImpl()
        throws CannotCompileException,
            NotFoundException,
            IOException
    {
        CtClass ctClass = getCtClass();
        ClassPool pool = getClassPool();
        if (!converted__ && ctClass.getName().equals(MAP_REDUCE_TASK))
        {
            converted__ = true;
            CtClass jobInfoIf = pool.get(MapReduceTaskAccessor.class.getName());
            ctClass.addInterface(jobInfoIf);
            CtField mapReduceJobId = CtField.make("java.lang.String mapReduceJobId_;", ctClass);
            ctClass.addField(mapReduceJobId);
            CtField taskCount = CtField.make("int taskCount_;", ctClass);
            ctClass.addField(taskCount);
            CtField mapReduceTaskIdMap =
                CtField.make("java.util.Map mapReduceTaskIdMap_ = " + "new "
                    + ConcurrentHashMap.class.getName() + "();", ctClass);
            ctClass.addField(mapReduceTaskIdMap);
            CtMethod getJobId =
                CtNewMethod.make("public String getJobId() {return mapReduceJobId_;}", ctClass);
            ctClass.addMethod(getJobId);
            CtMethod setJobId =
                CtNewMethod.make("public void setJobId(String jobId){mapReduceJobId_ = jobId;}",
                                 ctClass);
            ctClass.addMethod(setJobId);
            CtMethod getMapperName =
                CtNewMethod.make("public String getMapperName(){"
                    + "return mapper.getClass().getName();}", ctClass);
            ctClass.addMethod(getMapperName);
            CtMethod putTaskId =
                CtNewMethod.make("public void putTaskId(String address, String taskId){"
                    + "mapReduceTaskIdMap_.put(address, taskId);}", ctClass);
            ctClass.addMethod(putTaskId);
            CtMethod getSizeOfTaskIdMap =
                CtNewMethod.make("public int getSizeOfTaskIdMap(){"
                    + "return mapReduceTaskIdMap_.size();}", ctClass);
            ctClass.addMethod(getSizeOfTaskIdMap);
            CtMethod getMapReduceTaskIdMap =
                CtNewMethod.make("public java.util.Map getMapReduceTaskIdMap(){"
                    + "return mapReduceTaskIdMap_;}", ctClass);
            ctClass.addMethod(getMapReduceTaskIdMap);
            CtMethod getTaskCount =
                CtNewMethod.make("public int getTaskCount(){" + "return ++taskCount_;}", ctClass);
            ctClass.addMethod(getTaskCount);
        }
        List<CtBehavior> behaviorList = getMatcheDeclaredBehavior();
        for (CtBehavior ctBehavior : behaviorList)
        {
            convertMethod(ctBehavior);
        }

        setNewClassfileBuffer(ctClass.toBytecode());
    }

    private void convertMethod(final CtBehavior ctMethod)
        throws CannotCompileException
    {
        insertProcessesForJob(ctMethod);

        // タスク終了後処理（失敗時）
        insertPostProcessTaskNG(ctMethod);

        // タスク開始前処理
        insertPreProcessTask(ctMethod);

        // タスク終了後処理
        insertPostProcess(ctMethod);

        // 処理結果をログに出力する。
        logModifiedMethod("MapReduceTaskConverter", ctMethod);
    }

    /**
     * ジョブ終了後の処理を埋め込む
     * 
     * @param ctMethod 埋め込む対象のメソッド
     * @throws CannotCompileException
     */
    private void insertPostProcess(final CtBehavior ctMethod)
        throws CannotCompileException
    {
        // for Infinispan 5.1
        if (ctMethod.getDeclaringClass().getName().equals(MAP_REDUCE_TASK)
            && ctMethod.getName().equals("execute"))
        {
            ctMethod.instrument(new ExprEditor() {
                public void edit(MethodCall m)
                    throws CannotCompileException
                {
                    String className = m.getClassName();
                    String methodName = m.getMethodName();
                    if (className.equals(RPC_MANAGER) && methodName.equals("invokeRemotely")
                        || className.equals(MAP_REDUCE_TASK + ".MapReduceFuture")
                        && methodName.equals("get"))
                    {
                        m.replace("{" + "$_ = $proceed($$);"
                            + "java.util.Map map2 = (java.util.Map)$_;"
                            + "String[] keys = new String[0];"
                            + "java.util.Set keySet = map2.keySet();"
                            + "java.util.Map newMap = new java.util.HashMap();"
                            + "java.util.Iterator it = keySet.iterator();"
                            + "while (it.hasNext()) {"
                            + "org.infinispan.remoting.transport.Address key = "
                            + "(org.infinispan.remoting.transport.Address)it.next();"
                            + "newMap.put(key.toString(), " + "Boolean.valueOf"
                            + "(((org.infinispan.remoting.responses.Response)map2.get(key))"
                            + ".isSuccessful()));}" + MapReduceTaskMonitor.class.getName()
                            + ".postProcessTaskForInvokeRemotely(this, newMap);" + "$_ = map2;"
                            + "}");
                    }
                    if (className.equals(MAP_REDUCE_COMMAND) && methodName.equals("perform"))
                    {
                        m.replace("{" + "$_ = $proceed($$);" + MapReduceTaskMonitor.class.getName()
                            + ".postProcessTask(this);}");
                    }
                }
            });
        }

        // for Infinispan 5.3
        if (ctMethod.getDeclaringClass().getName()
            .equals("org.infinispan.distexec.mapreduce.MapReduceTask$MapTaskPart")
            || ctMethod.getDeclaringClass().getName()
                .equals("org.infinispan.distexec.mapreduce.MapReduceTask$ReduceTaskPart")
            || ctMethod.getDeclaringClass().getName()
                .equals("org.infinispan.distexec.mapreduce.MapReduceTask$TaskPart"))
        {
            ctMethod.instrument(new ExprEditor() {
                public void edit(MethodCall m)
                    throws CannotCompileException
                {
                    String className = m.getClassName();
                    String methodName = m.getMethodName();
                    if (className.equals(RPC_MANAGER) && methodName.equals("invokeRemotely")
                        || className.equals("java.util.concurrent.Future")
                        && methodName.equals("get"))
                    {
                        String replaceStr =
                            "{" + "$_ = $proceed($$);" + "java.util.Map map2 = (java.util.Map)$_;"
                                + "java.util.Set keySet = map2.keySet();"
                                + "java.util.Map newMap = new java.util.HashMap();"
                                + "java.util.Iterator it = keySet.iterator();"
                                + "while (it.hasNext()) {"
                                + "org.infinispan.remoting.transport.Address key = "
                                + "(org.infinispan.remoting.transport.Address)it.next();"
                                + "newMap.put(key.toString(), " + "Boolean.valueOf"
                                + "(((org.infinispan.remoting.responses.Response)map2.get(key))"
                                + ".isSuccessful()));}" + MapReduceTaskMonitor.class.getName()
                                + ".postProcessTaskForInvokeRemotely(("
                                + MapReduceTaskAccessor.class.getName() + ")this$0, newMap);"
                                + "$_ = map2;" + "}";
                        SystemLogger.getInstance().warn(replaceStr);
                        m.replace(replaceStr);
                    }
                    if ((methodName.equals("mapAndCombineForLocalReduction")
                        || methodName.equals("mapAndCombineForDistributedReduction") || methodName
                            .equals("reduce"))
                        && className.equals("org.infinispan.distexec.mapreduce.MapReduceManager"))
                    {
                        m.replace("{" + "$_ = $proceed($$);" + MapReduceTaskMonitor.class.getName()
                            + ".postProcessTask((" + MapReduceTaskAccessor.class.getName()
                            + ")this$0);}");
                    }
                }
            });
        }
    }

    /**
     * タスク開始前の処理を埋め込む。
     * 
     * @param ctMethod 埋め込み対象のメソッド
     * @throws CannotCompileException
     */
    private void insertPreProcessTask(final CtBehavior ctMethod)
        throws CannotCompileException
    {
        // for Infinispan 5.1
        if (ctMethod.getDeclaringClass().getName().equals(MAP_REDUCE_TASK))
        {
            ctMethod.instrument(new ExprEditor() {
                public void edit(MethodCall m)
                    throws CannotCompileException
                {
                    String methodName = m.getMethodName();
                    String className = m.getClassName();
                    if ((methodName.equals("invokeRemotely") || methodName
                        .equals("invokeRemotelyInFuture")) && className.equals(RPC_MANAGER))
                    {
                        m.replace("{String address = $0.getAddress().toString();"
                            + MapReduceTaskMonitor.class.getName()
                            + ".preProcessTaskForInvokeRemotely(this, address, null);"
                            + "$_ = $proceed($$);}");
                    }
                    else if (methodName.equals("invokeRemotelyInFuture")
                        && className.equals(RPC_MANAGER))
                    {
                        m.replace("{String addressStr = address.toString();"
                            + MapReduceTaskMonitor.class.getName()
                            + ".preProcessTaskForInvokeRemotelyInFuture(this, addressStr, null);"
                            + "$_ = $proceed($$);}");
                    }

                    else if (methodName.equals("perform") && className.equals(MAP_REDUCE_COMMAND))
                    {
                        m.replace("{String address = rpc.getAddress().toString();"
                            + MapReduceTaskMonitor.class.getName()
                            + ".preProcessTask(this, address, null);" + "$_ = $proceed($$);}");
                    }
                }
            });
        }

        // for Infinispan 5.3
        else if (ctMethod.getDeclaringClass().getName()
            .equals("org.infinispan.distexec.mapreduce.MapReduceTask$MapTaskPart")
            || ctMethod.getDeclaringClass().getName()
                .equals("org.infinispan.distexec.mapreduce.MapReduceTask$ReduceTaskPart"))
        {
            final boolean IS_MAP_TASK =
                ctMethod.getDeclaringClass().getName().endsWith("MapTaskPart");
            ctMethod.instrument(new ExprEditor() {
                public void edit(MethodCall m)
                    throws CannotCompileException
                {
                    String methodName = m.getMethodName();
                    String className = m.getClassName();
                    String taskType = IS_MAP_TASK ? "map" : "reduce";
                    if (methodName.startsWith("invokeRemotely") && className.equals(RPC_MANAGER))
                    {
                        m.replace("{String address = getExecutionTarget().toString();"
                            + MapReduceTaskMonitor.class.getName()
                            + ".preProcessTaskForInvokeRemotelyInFuture(("
                            + MapReduceTaskAccessor.class.getName() + ")this$0, address, \""
                            + taskType + "\");" + "$_ = $proceed($$);}");
                    }
                    else if ((methodName.equals("mapAndCombineForLocalReduction")
                        || methodName.equals("mapAndCombineForDistributedReduction") || methodName
                            .equals("reduce"))
                        && className.equals("org.infinispan.distexec.mapreduce.MapReduceManager"))
                    {
                        m.replace("{String address = getAddress().toString();"
                            + MapReduceTaskMonitor.class.getName() + ".preProcessTask(("
                            + MapReduceTaskAccessor.class.getName() + ")this$0, address, \""
                            + taskType + "\");" + "$_ = $proceed($$);}");
                    }
                }
            });
        }
    }

    /**
     * タスク終了時の処理を埋め込む。
     *
     * @param ctMethod 埋め込み対象のメソッド
     * @throws CannotCompileException
     */
    private void insertPostProcessTaskNG(final CtBehavior ctMethod)
        throws CannotCompileException
    {
        // for Infinispan 5.1
        if (ctMethod.getDeclaringClass().getName().equals(MAP_REDUCE_TASK))
        {
            ctMethod.instrument(new ExprEditor() {
                public void edit(MethodCall m)
                    throws CannotCompileException
                {
                    String className = m.getClassName();
                    String methodName = m.getMethodName();
                    if (className.equals(RPC_MANAGER)
                        && methodName.equals("invokeRemotelyInFuture")
                        || className.equals(RPC_MANAGER) && methodName.equals("invokeRemotely")
                        || className.equals(MAP_REDUCE_TASK + ".MapReduceFuture")
                        && methodName.equals("get") || className.equals(MAP_REDUCE_COMMAND)
                        && methodName.equals("perform"))
                    {

                        m.replace("try{$_ = $proceed($$);}catch(Exception e){"
                            + MapReduceTaskMonitor.class.getName() + ".postProcessNGTask(this);}");
                    }
                }
            });
        }

        // for Infinispan 5.3
        else if (ctMethod.getDeclaringClass().getName()
            .equals("org.infinispan.distexec.mapreduce.MapReduceTask$MapTaskPart")
            || ctMethod.getDeclaringClass().getName()
                .equals("org.infinispan.distexec.mapreduce.MapReduceTask$ReduceTaskPart"))
        {
            ctMethod.instrument(new ExprEditor() {
                public void edit(MethodCall m)
                    throws CannotCompileException
                {
                    String className = m.getClassName();
                    String methodName = m.getMethodName();
                    if (className.equals(MAP_REDUCE_MANAGER)
                        && methodName.equals("mapAndCombineForLocalReduction")
                        || className.equals(MAP_REDUCE_MANAGER)
                        && methodName.equals("mapAndCombineForDistributedReduction")
                        || className.equals(RPC_MANAGER) && methodName.equals("invokeRemotely"))
                    {
                        m.replace("try{$_ = $proceed($$);}catch(Exception e){"
                            + MapReduceTaskMonitor.class.getName() + ".postProcessNGTask(("
                            + MapReduceTaskAccessor.class.getName() + ")this$0);}");
                    }
                }
            });
        }
    }

    /**
     * ジョブ開始時、終了時、失敗時の処理を埋め込む。
     * 
     * @param ctMethod 埋め込み対象のメソッド
     * @throws CannotCompileException
     */
    private void insertProcessesForJob(final CtBehavior ctMethod)
        throws CannotCompileException
    {
        if (ctMethod.getName().equals("execute")
            && ctMethod.getDeclaringClass().getName().equals(MAP_REDUCE_TASK))
        {
            ctMethod.insertBefore(MapReduceTaskMonitor.class.getName() + ".preProcess(this);");
            ctMethod.insertAfter(MapReduceTaskMonitor.class.getName() + ".postProcess(this);");
            ctMethod.addCatch("{" + MapReduceTaskMonitor.class.getName()
                + ".postProcessNG(this, $e); $e.printStackTrace(); throw $e;}", throwableClass_);
        }
    }

    /** (non-Javadoc)
     * @see jp.co.acroquest.endosnipe.javelin.converter.Converter#init()
     */
    public void init()
    {
        try
        {
            throwableClass_ = ClassPool.getDefault().get(Throwable.class.getCanonicalName());
        }
        catch (NotFoundException nfe)
        {
            // 発生しない。
            SystemLogger.getInstance().warn(nfe);
        }
        ResourceCollector.getInstance()
            .addMultiResource(TelegramConstants.ITEMNAME_INFINISPAN_MAPREDUCE,
                              new InfinispanJobGetter());

    }

}
