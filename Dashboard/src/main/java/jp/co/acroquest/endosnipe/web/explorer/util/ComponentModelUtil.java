/*
 * Copyright (c) 2004-2009 SMG Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  SMG Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.web.explorer.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.acroquest.endosnipe.web.explorer.entity.AbstractConnectionModel;
import jp.co.acroquest.endosnipe.web.explorer.entity.ArrowConnectionModel;
import jp.co.acroquest.endosnipe.web.explorer.entity.ClassModel;
import jp.co.acroquest.endosnipe.web.explorer.entity.ComponentModel;
import jp.co.acroquest.endosnipe.web.explorer.entity.ComponentType;
import jp.co.acroquest.endosnipe.web.explorer.entity.ContentsModel;
import jp.co.acroquest.endosnipe.web.explorer.entity.InvocationModel;
import jp.co.acroquest.endosnipe.web.explorer.entity.MethodModel;

/**
 * 構造モデルからビューモデルへの変換を行うユーティリティクラスです。<br />
 *
 * @author sakamoto
 *
 * @see ClassModel
 * @see MethodModel
 * @see ComponentModel
 * @see InvocationModel
 */
public class ComponentModelUtil
{

    /**
     * コンストラクタを隠蔽します。<br />
     */
    private ComponentModelUtil()
    {
        // Do nothing.
    }

    /**
     * {@link MethodModel} から　{@link InvocationModel} を作成します。<br />
     *
     * {@link InvocationModel} に対し、 {@link ComponentModel} のセットは行いません。<br />
     *
     * @param methodModel {@link MethodModel} オブジェクト
     * @return {@link InvocationModel} オブジェクト
     */
    public static InvocationModel convertToInvocationModel(final MethodModel methodModel)
    {
        InvocationModel invocationModel = new InvocationModel();
        invocationModel.setClassName(methodModel.getFullClassName());
        invocationModel.setMethodName(methodModel.getMethodName());
        invocationModel.setDate(new Date());
        invocationModel.setMinimum(methodModel.getMinimum());
        invocationModel.setMaximum(methodModel.getMaximum());
        invocationModel.setTotal(methodModel.getTotal());
        invocationModel.setCount(methodModel.getCallCount());
        invocationModel.setCpuMinimum(methodModel.getCpuMinimum());
        invocationModel.setCpuMaximum(methodModel.getCpuMaximum());
        invocationModel.setCpuTotal(methodModel.getCpuTotal());
        invocationModel.setUserMinimum(methodModel.getUserMinimum());
        invocationModel.setUserMaximum(methodModel.getUserMaximum());
        invocationModel.setUserTotal(methodModel.getUserTotal());
        invocationModel.setThrowableCount(methodModel.getThrowableCount());
        invocationModel.setThrowableList(methodModel.getThrowableList());
        invocationModel.setTarget(methodModel.isTarget());
        invocationModel.setHttpStatusCount(methodModel.getHttpStatusCount());
        return invocationModel;
    }

    /**
     * クラス情報から、クラスを表す {@link InvocationModel} を作成します。<br />
     *
     * @param classModel クラス
     * @return クラスを表す {@link InvocationModel} オブジェクト
     */
    public static InvocationModel convertToInvocationModel(final ClassModel classModel)
    {
        if (classModel == null)
        {
            return null;
        }

        InvocationModel invocationModel = new InvocationModel();
        invocationModel.setClassName(classModel.getPackageName());
        invocationModel.setMethodName(classModel.getFullClassName());
        invocationModel.setDate(new Date());
        long maximum = 0;
        long total = 0;
        long callCount = 0;
        boolean targetClass = false;
        for (MethodModel methodModel : classModel.getMethods())
        {
            if (maximum < methodModel.getMaximum())
            {
                maximum = methodModel.getMaximum();
            }
            total += methodModel.getTotal();
            callCount += methodModel.getCallCount();
            targetClass |= methodModel.isTarget();
        }
        invocationModel.setMaximum(maximum);
        invocationModel.setTotal(total);
        invocationModel.setCount(callCount);
        invocationModel.setTarget(targetClass);
        return invocationModel;
    }

    /**
     * 指定したメソッドを含む {@link ComponentModel} を返します。<br />
     *
     * @param contentsModel {@link ContentsModel} オブジェクト
     * @param methodModel {@link MethodModel} オブジェクト
     * @return {@link ComponentModel} オブジェクト
     */
    public static ComponentModel getComponentModel(final ContentsModel contentsModel,
            final MethodModel methodModel)
    {
        List<ComponentModel> componentModelList = contentsModel.getChildren();
        for (ComponentModel componentModel : componentModelList)
        {
            ComponentType componentType = componentModel.getComponentType();
            String componentName = componentModel.getComponentName();
            if (componentType == ComponentType.PACKAGE)
            {
                if (methodModel.getPackageName().startsWith(componentName))
                {
                    return componentModel;
                }
            }
            else
            {
                if (componentName.equals(methodModel.getFullClassName()))
                {
                    return componentModel;
                }
            }
        }
        return null;
    }

    /**
     * 指定したクラスの、もしくは指定したクラスを含む {@link ComponentModel} を返します。<br />
     *
     * @param contentsModel {@link ContentsModel} オブジェクト
     * @param fullClassName パッケージ名を含むクラス名
     * @return 見つかった場合は {@link ComponentModel} オブジェクト、見つからない場合は <code>null</code>
     */
    public static ComponentModel getComponentModel(final ContentsModel contentsModel,
            final String fullClassName)
    {
        List<ComponentModel> componentModelList = contentsModel.getChildren();
        for (ComponentModel componentModel : componentModelList)
        {
            ComponentType componentType = componentModel.getComponentType();
            if (componentType == ComponentType.PACKAGE)
            {
                for (InvocationModel invocationModel : componentModel.getInvocationList())
                {
                    if (invocationModel.getMethodName().equals(fullClassName))
                    {
                        return componentModel;
                    }
                }
            }
            else
            {
                if (componentModel.getComponentName().equals(fullClassName))
                {
                    return componentModel;
                }
            }
        }
        return null;
    }

    /**
     * 呼び出し元コンポーネントと接続します。<br />
     *
     * @param methodClassArray 呼び出し元と接続する呼び出し先クラスの配列
     * @param contentsModel コンテンツモデル
     */
    public static void connectCallerClasses(final ClassModel[] methodClassArray,
            final ContentsModel contentsModel)
    {
        for (ClassModel classModel : methodClassArray)
        {
            ComponentModel target = getComponentModel(contentsModel, classModel.getFullClassName());
            for (String callerClass : classModel.getCallerClasses())
            {
                ComponentModel source = getComponentModel(contentsModel, callerClass);
                if (source == null || target == null)
                {
                    continue;
                }
                connectOneCallerClass(source, target);
            }
        }
    }

    /**
     * 呼び出し元コンポーネントと接続します。<br />
     *
     * 指定された呼び出し先クラスに入っているすべてのメソッドの呼び出し元と接続します。<br />
     *
     * @param classModel 呼び出し元と接続する呼び出し先クラス
     * @param contentsModel コンテンツモデル
     */
    public static void connectCallerClasses(final ClassModel classModel,
            final ContentsModel contentsModel)
    {
        ComponentModel target = getComponentModel(contentsModel, classModel.getFullClassName());
        if (target == null)
        {
            // この View 上には接続先コンポーネントが存在しない
            return;
        }

        for (String callerClass : classModel.getCallerClasses())
        {
            ComponentModel source = getComponentModel(contentsModel, callerClass);
            if (source != null)
            {
                connectOneCallerClass(source, target);
            }
        }
    }

    /**
     * 指定されたコンポーネント同士を {@link ArrowConnectionModel} で接続します。<br />
     *
     * すでに接続されている場合、接続先と接続元が等しい場合は、何も行いません。<br />
     *
     * @param source 接続元
     * @param target 接続先
     */
    private static void connectOneCallerClass(final ComponentModel source,
            final ComponentModel target)
    {
        if (source == target)
        {
            return;
        }

        List<? extends AbstractConnectionModel> sourceList = source.getModelSourceConnections();
        for (AbstractConnectionModel model : sourceList)
        {
            if (target.equals(model.getTarget()))
            {
                // すでに接続されている場合は、何もしない
                return;
            }
        }

        ArrowConnectionModel arrow = new ArrowConnectionModel();
        arrow.setSource(source);
        arrow.setTarget(target);
        source.addSourceConnection(arrow);
        target.addTargetConnection(arrow);
    }

    /**
     * パッケージ名を含むクラス名を、パッケージ名とクラス名に分割します。<br />
     *
     * @param fullClassName パッケージ名を含むクラス名
     * @return 1 つ目の要素はパッケージ名、 2 つ目の要素はクラス名
     */
    public static String[] splitPackageAndClass(final String fullClassName)
    {
        String className = fullClassName;
        ComponentType componentType = ComponentType.getComponentType(fullClassName);
        String packageName = "";
        switch (componentType)
        {
        case WEB:
            packageName = ComponentType.WEB_PREFIX;
            break;
        case DATABASE:
            packageName = ComponentType.DATABASE_PREFIX;
            break;
        default:
            // クラスの場合、クラス名をパッケージ名とクラス名に分割する
            int classPosition = fullClassName.lastIndexOf('.');
            if (classPosition >= 0)
            {
                packageName = fullClassName.substring(0, classPosition);
                className = fullClassName.substring(classPosition + 1);
            }
            break;
        }
        return new String[] { packageName, className };
    }

    /**
     * 指定されたコンポーネントに関連する接続を削除します。<br />
     *
     * @param componentModel コンポーネント
     */
    public static void deleteConnection(final ComponentModel componentModel)
    {
        componentModel.setDeleted(true);
        List<AbstractConnectionModel> sources =
                new ArrayList<AbstractConnectionModel>(componentModel.getModelSourceConnections());
        List<AbstractConnectionModel> targets =
                new ArrayList<AbstractConnectionModel>(componentModel.getModelTargetConnections());
        detachConnections(sources);
        detachConnections(targets);
    }

    /**
     * 指定されたリスト内のすべてのコネクションを外します。<br />
     *
     * @param connections コネクションリスト
     */
    public static void detachConnections(final List<AbstractConnectionModel> connections)
    {
        for (AbstractConnectionModel connection : connections)
        {
            connection.detachSource();
            connection.detachTarget();
        }
    }

    /**
     * 指定されたリスト内のすべてのコネクションを繋ぎます。<br />
     *
     * @param connections コネクションリスト 
     */
    public static void attachConnections(final List<AbstractConnectionModel> connections)
    {
        for (AbstractConnectionModel connection : connections)
        {
            connection.attachSource();
            connection.attachTarget();
        }
    }

    /**
     * パッケージ名を含めたクラス名を返します。<br />
     *
     * @param packageName パッケージ名
     * @param className クラス名
     * @return パッケージ名を含めたクラス名
     */
    public static String getFullClassName(final String packageName, final String className)
    {
        String fullClassName;
        if (packageName.equals(ComponentType.WEB_PREFIX)
                || packageName.equals(ComponentType.DATABASE_PREFIX))
        {
            fullClassName = className;
        }
        else if (packageName.length() > 0)
        {
            fullClassName = packageName + "." + className;
        }
        else
        {
            fullClassName = className;
        }
        return fullClassName;
    }

}
