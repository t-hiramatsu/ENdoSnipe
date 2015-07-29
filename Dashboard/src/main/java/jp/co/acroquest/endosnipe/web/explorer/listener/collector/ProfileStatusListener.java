/*
 * Copyright (c) 2004-2014 Acroquest Technology Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  Acroquest Technology Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.web.explorer.listener.collector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.util.StringUtil;
import jp.co.acroquest.endosnipe.communicator.AbstractTelegramListener;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.ResponseBody;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.web.explorer.entity.ClassModel;
import jp.co.acroquest.endosnipe.web.explorer.entity.MethodModel;
import jp.co.acroquest.endosnipe.web.explorer.entity.StructureModel;
import jp.co.acroquest.endosnipe.web.explorer.manager.EventManager;
import jp.co.acroquest.endosnipe.web.explorer.manager.ProfileSender;
import jp.co.acroquest.endosnipe.web.explorer.manager.ProfilerManager;

import org.wgp.manager.WgpDataManager;

/**
 * DataCollectorからの状態取得応答を受け、クライアントに計測情報を返すためのリスナです。
 * 
 * @author hiramatsu
 */
public class ProfileStatusListener extends AbstractTelegramListener
{

    /** レスポンスのオブジェクト名をセパレータで分割時のクラス名のインデックス */
    private static final int INDEX_CLASSNAME = 0;

    /** レスポンスのオブジェクト名をセパレータで分割時のメソッド名のインデックス */
    private static final int INDEX_METHODNAME = 1;

    /** レスポンスのオブジェクト名をクラス名とメソッド名に分割するセパレータ */
    private static final String CLASSMETHOD_SEPARATOR = "###CLASSMETHOD_SEPARATOR###";

    /** 単位変換に用いる定数1000 */
    private static final int MILLIS = 1000;

    /**
     * デフォルトコンストラクタ
     */
    public ProfileStatusListener()
    {

    }

    @Override
    protected Telegram doReceiveTelegram(final Telegram telegram)
    {
        ClassModel[] classModels = createFromTelegram(telegram, new StructureModel());

        EventManager eventManager = EventManager.getInstance();
        WgpDataManager dataManager = eventManager.getWgpDataManager();
        ProfileSender profileSender = eventManager.getProfileSender();
        if (dataManager == null || profileSender == null)
        {
            return null;
        }
        Body[] bodies = telegram.getObjBody();
        String agentName = null;
        if (bodies != null && bodies.length > 0)
        {
            Body lastBody = bodies[bodies.length - 1];
            if (lastBody.getStrObjName().equals("agentName"))
            {
                agentName = lastBody.getStrItemName();
            }
        }

        profileSender.send(classModels, agentName);
        ProfilerManager manager = ProfilerManager.getInstance();
        manager.setProfilerData(agentName, classModels);
        return null;
    }

    @Override
    protected byte getByteRequestKind()
    {
        return TelegramConstants.BYTE_REQUEST_KIND_RESPONSE;
    }

    @Override
    protected byte getByteTelegramKind()
    {
        return TelegramConstants.BYTE_TELEGRAM_KIND_GET;
    }

    /**
     * 電文からクラスの配列を作成します。<br />
     *
     * 電文内のメソッドは、引数の <code>structureModel</code> に追加／セットされます。<br />
     *
     * @param telegram 電文
     * @param structureModel 電文に含まれるクラス、メソッドを追加する
     * @return クラスの配列（電文に含まれるクラス、メソッドのみ要素に存在する）
     */
    public ClassModel[] createFromTelegram(final Telegram telegram,
            final StructureModel structureModel)
    {
        // アラームがあがってきたクラスを、クラス名をキーにして保持するマップ
        Map<String, ClassModel> alarmClassMap = new LinkedHashMap<String, ClassModel>();

        Body[] objBody = telegram.getObjBody();
        String classMethodName = null;
        for (int index = 0; index < objBody.length; index++)
        {
            ResponseBody responseBody = (ResponseBody)objBody[index];

            if (responseBody.getStrObjName() == null)
            {
                continue;
            }

            // 全て呼び出す元を設定用
            // データをMethodModelに設定する
            String itemName = ((ResponseBody)objBody[index]).getStrItemName();
            Object[] itemValueArray = ((ResponseBody)objBody[index]).getObjItemValueArr();

            // 説明の配列長が0であればスキップする
            if (itemValueArray.length == 0)
            {
                continue;
            }

            // 対象名より、クラス名、メソッド名を取得する
            if (responseBody.getStrObjName().length() > 0)
            {
                classMethodName = responseBody.getStrObjName();
            }
            if (classMethodName == null)
            {
                continue;
            }

            List<String> classMethodNameList =
                    StringUtil.split(classMethodName, CLASSMETHOD_SEPARATOR);
            String fullClassName = "unknown";
            String methodName = "unknown";
            if (classMethodNameList.size() > INDEX_METHODNAME)
            {
                fullClassName = classMethodNameList.get(INDEX_CLASSNAME);
                methodName = classMethodNameList.get(INDEX_METHODNAME);
            }

            ClassModel classModel = alarmClassMap.get(fullClassName);
            if (classModel == null)
            {
                classModel = new ClassModel(fullClassName);
                alarmClassMap.put(fullClassName, classModel);
            }
            MethodModel methodModel = classModel.getMethod(methodName);
            if (methodModel == null)
            {
                methodModel = new MethodModel(methodName);
                methodModel.setFullClassName(fullClassName);
                classModel.addMethodModel(methodModel);
            }
            convertToMethodModel(responseBody, itemName, classModel, methodModel, itemValueArray[0]);
            methodModel = null;
        }

        // StructureModel にメソッドを追加する
        List<MethodModel> deleteMethodsFromAlarm = new ArrayList<MethodModel>();
        Date date = new Date();
        for (Map.Entry<String, ClassModel> entry : alarmClassMap.entrySet())
        {
            ClassModel alarmClassModel = entry.getValue();
            String fullClassName = alarmClassModel.getFullClassName();
            ClassModel structureClassModel = structureModel.getClassModel(fullClassName);
            if (structureClassModel == null)
            {
                structureClassModel = new ClassModel(fullClassName);
                structureModel.addClassModel(structureClassModel);
            }
            else
            {
                alarmClassModel.addCallerClasses(structureClassModel.getCallerClasses());
            }
            structureClassModel.addCallerClasses(alarmClassModel.getCallerClasses());

            for (MethodModel alarmMethod : alarmClassModel.getMethods())
            {
                alarmMethod.setDate(date);
                MethodModel structureMethodModel = new MethodModel(alarmMethod);
                MethodModel deletedMethod =
                        structureClassModel.addMethodModel(structureMethodModel);
                if (deletedMethod != null)
                {
                    // 上限を超えてメソッドが削除されたため、
                    // 削除されたメソッドを、アラームをあげるメソッド一覧から削除し、画面からも消す。
                    // ただし、アラームをあげるメソッド一覧から削除するのは、このループを抜けてからにする。
                    // 理由は、 ConcurrentModificationException を発生させる恐れがあるため。
                    deleteMethodsFromAlarm.add(deletedMethod);
                    //                    removeInvocationModelFromAllTabs(deletedMethod);
                }
            }
        }

        // 削除されたメソッドを、アラームをあげるメソッド一覧から削除する
        for (MethodModel deletedMethod : deleteMethodsFromAlarm)
        {
            ClassModel classModel = alarmClassMap.get(deletedMethod.getFullClassName());
            if (classModel != null)
            {
                classModel.removeMethodModel(deletedMethod);
                if (classModel.getMethods().size() == 0)
                {
                    alarmClassMap.remove(classModel.getFullClassName());
                }
            }
        }

        Collection<ClassModel> classModelList = alarmClassMap.values();
        return classModelList.toArray(new ClassModel[classModelList.size()]);
    }

    /**
     * 受信した応答データの値をクラスモデルまたはメソッドモデルにセットします。<br />
     *
     * @param responseBody 応答データ
     * @param itemName 項目名
     * @param classModel 値をセットするクラスモデル
     * @param methodModel 値をセットするメソッドモデル
     * @param value セットする値
     */
    private static void convertToMethodModel(final ResponseBody responseBody,
            final String itemName, final ClassModel classModel, final MethodModel methodModel,
            final Object value)
    {
        if (value instanceof Long)
        {
            long longValue = (Long)value;
            if (TelegramConstants.ITEMNAME_CALL_COUNT.equals(itemName))
            {
                longValue = getValidValue(longValue);
                methodModel.setCallCount(longValue);
            }
            else if (TelegramConstants.ITEMNAME_CURRENT_INTERVAL.equals(itemName))
            {
                longValue = getValidValue(longValue);
                methodModel.setCurrent(longValue);
            }
            else if (TelegramConstants.ITEMNAME_TOTAL_INTERVAL.equals(itemName))
            {
                longValue = getValidValue(longValue);
                methodModel.setTotal(longValue);
            }
            else if (TelegramConstants.ITEMNAME_MAXIMUM_INTERVAL.equals(itemName))
            {
                longValue = getValidValue(longValue);
                methodModel.setMaximum(longValue);
            }
            else if (TelegramConstants.ITEMNAME_MINIMUM_INTERVAL.equals(itemName))
            {
                longValue = getValidValue(longValue);
                methodModel.setMinimum(longValue);
            }
            else if (TelegramConstants.ITEMNAME_TOTAL_CPU_INTERVAL.equals(itemName))
            {
                longValue = getValidMegaValue(longValue);
                methodModel.setCpuTotal(longValue);
            }
            else if (TelegramConstants.ITEMNAME_MAXIMUM_CPU_INTERVAL.equals(itemName))
            {
                longValue = getValidMegaValue(longValue);
                methodModel.setCpuMaximum(longValue);
            }
            else if (TelegramConstants.ITEMNAME_MINIMUM_CPU_INTERVAL.equals(itemName))
            {
                longValue = getValidMegaValue(longValue);
                methodModel.setCpuMinimum(longValue);
            }
            else if (TelegramConstants.ITEMNAME_TOTAL_USER_INTERVAL.equals(itemName))
            {
                longValue = getValidMegaValue(longValue);
                methodModel.setUserTotal(longValue);
            }
            else if (TelegramConstants.ITEMNAME_MAXIMUM_USER_INTERVAL.equals(itemName))
            {
                longValue = getValidMegaValue(longValue);
                methodModel.setUserMaximum(longValue);
            }
            else if (TelegramConstants.ITEMNAME_MINIMUM_USER_INTERVAL.equals(itemName))
            {
                longValue = getValidMegaValue(longValue);
                methodModel.setUserMinimum(longValue);
            }
            else if (TelegramConstants.ITEMNAME_JAVAPROCESS_EXCEPTION_OCCURENCE_COUNT.equals(itemName))
            {
                longValue = getValidValue(longValue);
                methodModel.setThrowableCount(longValue);
            }
            else if (TelegramConstants.ITEMNAME_JAVAPROCESS_METHOD_STALL_COUNT.equals(itemName))
            {
                longValue = getValidValue(longValue);
                methodModel.setMethodStallCount(longValue);
            }
            else if (TelegramConstants.ITEMNAME_ALARM_THRESHOLD.equals(itemName))
            {
                longValue = getValidValue(longValue);
                methodModel.setAlarmThreshold(longValue);
            }
            else if (TelegramConstants.ITEMNAME_ALARM_CPU_THRESHOLD.equals(itemName))
            {
                longValue = getValidValue(longValue);
                methodModel.setAlarmCpuThreshold(longValue);
            }
        }
        else if (value instanceof String)
        {
            if (TelegramConstants.ITEMNAME_ALL_CALLER_NAMES.equals(itemName))
            {
                // メソッドの呼び出し元クラス名
                Object[] callerClassNames = responseBody.getObjItemValueArr();
                for (Object callerClassName : callerClassNames)
                {
                    if (!classModel.getClassName().equals(callerClassName))
                    {
                        // 自分への呼び出しでなければ追加する
                        classModel.addCallerClass((String)callerClassName);
                    }
                }
            }
            else if (TelegramConstants.ITEMNAME_TARGET.equals(itemName))
            {
                methodModel.setTarget(Boolean.valueOf((String)value));
            }
            else if (TelegramConstants.ITEMNAME_TRANSACTION_GRAPH.equals(itemName))
            {
                methodModel.setTransactionGraph(Boolean.valueOf((String)value));
            }
        }
    }

    /**
     * 引数を判定し、正の値であればその値を、負の値であれば-1を返す
     * 
     * @param longValue 判定する入力値
     * @return longValueが正であればlongValue、負であれば-1
     */
    private static long getValidValue(final long longValue)
    {
        if (longValue < 0)
        {
            return -1;
        }
        return longValue;
    }

    /**
     * 引数を判定し、正の値であれば単位をメガに変換した値を、負の値であれば-1を返す
     * 
     * @param longValue 判定する入力値
     * @return longValueが正であればlongValueを1000000で割った値、負であれば-1
     */
    private static long getValidMegaValue(long longValue)
    {
        if (longValue < 0)
        {
            return -1;
        }
        longValue /= (MILLIS * MILLIS);
        return longValue;
    }

}
