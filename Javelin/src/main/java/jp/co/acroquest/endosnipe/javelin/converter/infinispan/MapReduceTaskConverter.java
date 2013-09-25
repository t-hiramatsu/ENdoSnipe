package jp.co.acroquest.endosnipe.javelin.converter.infinispan;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.common.logger.SystemLogger;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.infinispan.monitor.MapReduceTaskAccessor;
import jp.co.acroquest.endosnipe.javelin.converter.infinispan.monitor.MapReduceTaskMonitor;
import jp.co.acroquest.endosnipe.javelin.resource.ResourceCollector;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.ClassPool;
import jp.co.smg.endosnipe.javassist.CtBehavior;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.CtField;
import jp.co.smg.endosnipe.javassist.CtMethod;
import jp.co.smg.endosnipe.javassist.CtNewMethod;
import jp.co.smg.endosnipe.javassist.NotFoundException;
import jp.co.smg.endosnipe.javassist.expr.ExprEditor;
import jp.co.smg.endosnipe.javassist.expr.MethodCall;

/**
 * mapreduceのジョブ、タスク情報を取得するためのコードを埋め込む
 * 
 * @author hiramatsu
 *
 */
public class MapReduceTaskConverter extends AbstractConverter
{

    private CtClass throwableClass_;

    @Override
    public void convertImpl() throws CannotCompileException, NotFoundException,
            IOException
    {
        CtClass ctClass = getCtClass();
        ClassPool pool = getClassPool();

        CtClass jobInfoIf = pool.get(MapReduceTaskAccessor.class.getName());
        ctClass.addInterface(jobInfoIf);
        CtField mapReduceJobId = CtField.make(
                "java.lang.String mapReduceJobId_;", ctClass);
        ctClass.addField(mapReduceJobId);
        CtField mapReduceTaskIdMap = CtField.make(
                "java.util.Map mapReduceTaskIdMap_ = "
                        + "new "
                        + ConcurrentHashMap.class.getName()
                        + "();", ctClass);
        ctClass.addField(mapReduceTaskIdMap);
        CtMethod getJobId = CtNewMethod.make(
                "public String getJobId() {return mapReduceJobId_;}", ctClass);
        ctClass.addMethod(getJobId);
        CtMethod setJobId = CtNewMethod.make(
                "public void setJobId(String jobId){mapReduceJobId_ = jobId;}",
                ctClass);
        ctClass.addMethod(setJobId);
        CtMethod getMapperName = CtNewMethod.make(
                "public String getMapperName(){"
                        + "return mapper.getClass().getName();}", ctClass);
        ctClass.addMethod(getMapperName);
        CtMethod putTaskId = CtNewMethod.make(
                "public void putTaskId(String address, String taskId){"
                        + "mapReduceTaskIdMap_.put(address, taskId);}", ctClass);
        ctClass.addMethod(putTaskId);
        CtMethod getSizeOfTaskIdMap = CtNewMethod.make(
                "public int getSizeOfTaskIdMap(){"
                        + "return mapReduceTaskIdMap_.size();}", ctClass);
        ctClass.addMethod(getSizeOfTaskIdMap);

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
        ctMethod.instrument(new ExprEditor() {
            public void edit(MethodCall m) throws CannotCompileException
            {
                String className = m.getClassName();
                String methodName = m.getMethodName();
                if (className.equals("org.infinispan.remoting.rpc.RpcManager")
                        && methodName.equals("invokeRemotely")
                        || className.equals("org.infinispan.distexec.mapreduce."
                                + "MapReduceTask.MapReduceFuture")
                        && methodName.equals("get"))
                {
                    m.replace("{"
                            +   "$_ = $proceed($$);"
                            +   "java.util.Map map2 = (java.util.Map)$_;"
                            +   "String[] keys = new String[0];"
                            +   "java.util.Set keySet = map2.keySet();"
                            +   "java.util.Map newMap = new java.util.HashMap();"
                            +   "java.util.Iterator it = keySet.iterator();"
                            +   "while (it.hasNext()) {"
                            +     "org.infinispan.remoting.transport.Address key = "
                            +       "(org.infinispan.remoting.transport.Address)it.next();"
                            +     "newMap.put(key.toString(), "
                            +       "Boolean.valueOf"
                            +         "(((org.infinispan.remoting.responses.Response)map2.get(key))"
                            +           ".isSuccessful()));}"
                            +    MapReduceTaskMonitor.class.getName()
                            +     ".postProcessTaskForInvokeRemotely(this, newMap);"
                            +   "$_ = map2;" + "}");
                }
                if (className.equals("org.infinispan.commands.read.MapReduceCommand")
                        && methodName.equals("perform"))
                {
                    m.replace("{" + "$_ = $proceed($$);"
                            + MapReduceTaskMonitor.class.getName()
                            + ".postProcessTask(this);}");
                }
            }
        });
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
        ctMethod.instrument(new ExprEditor() {
            public void edit(MethodCall m) throws CannotCompileException
            {
                String methodName = m.getMethodName();
                String className = m.getClassName();
                if ((methodName.equals("invokeRemotely") 
                        || methodName.equals("invokeRemotelyInFuture"))
                        && className.equals("org.infinispan.remoting.rpc.RpcManager"))
                {

                    m.replace("{String address = $0.getAddress().toString();"
                            + MapReduceTaskMonitor.class.getName()
                            + ".preProcessTaskForInvokeRemotely(this, address);"
                            + "$_ = $proceed($$);}");
                }
                else if (methodName.equals("invokeRemotelyInFuture")
                        && className.equals("org.infinispan.remoting.rpc.RpcManager"))
                {
                    m.replace("{String addressStr = address.toString();"
                            + MapReduceTaskMonitor.class.getName()
                            + ".preProcessTaskForInvokeRemotelyInFuture(this, addressStr);"
                            + "$_ = $proceed($$);}");
                }

                else if (methodName.equals("perform")
                        && className.equals("org.infinispan.commands.read.MapReduceCommand"))
                {
                    m.replace("{String address = rpc.getAddress().toString();"
                            + MapReduceTaskMonitor.class.getName()
                            + ".preProcessTask(this, address);"
                            + "$_ = $proceed($$);}");
                }
            }
        });
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
        ctMethod.instrument(new ExprEditor() {
            public void edit(MethodCall m) throws CannotCompileException
            {
                if (m.getClassName().equals(
                        "org.infinispan.remoting.rpc.RpcManager")
                        && m.getMethodName().equals("invokeRemotelyInFuture")
                        || m.getClassName().equals(
                                "org.infinispan.remoting.rpc.RpcManager")
                        && m.getMethodName().equals("invokeRemotely")
                        || m.getClassName().equals(
                                "org.infinispan.distexec.mapreduce.MapReduceTask.MapReduceFuture")
                        && m.getMethodName().equals("get")
                        || m.getClassName().equals(
                                "org.infinispan.commands.read.MapReduceCommand")
                        && m.getMethodName().equals("perform"))
                {
                    m.replace("try{$_ = $proceed($$);}catch(Exception e){"
                            + MapReduceTaskMonitor.class.getName()
                            + ".postProcessNGTask(this);}");
                }
            }
        });
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
        ctMethod.insertBefore(MapReduceTaskMonitor.class.getName()
                + ".preProcess(this);");
        ctMethod.insertAfter(MapReduceTaskMonitor.class.getName()
                + ".postProcess(this);");
        ctMethod.addCatch("{" + MapReduceTaskMonitor.class.getName()
                + ".postProcessNG(this, $e); $e.printStackTrace(); throw $e;}",
                throwableClass_);
    }

    /** (non-Javadoc)
     * @see jp.co.acroquest.endosnipe.javelin.converter.Converter#init()
     */
    public void init()
    {
        try
        {
            throwableClass_ = ClassPool.getDefault().get(
                    Throwable.class.getCanonicalName());
        }
        catch (NotFoundException nfe)
        {
            // 発生しない。
            SystemLogger.getInstance().warn(nfe);
        }
        ResourceCollector.getInstance().addMultiResource(
                TelegramConstants.ITEMNAME_INFINISPAN_MAPREDUCE,
                new InfinispanJobGetter());

    }

}
