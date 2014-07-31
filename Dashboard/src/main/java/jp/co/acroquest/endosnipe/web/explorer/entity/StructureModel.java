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
package jp.co.acroquest.endosnipe.web.explorer.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * クラスの構造を表すクラス。<br />
 *
 * @see ClassModel
 * @see MethodModel
 *
 * @author sakamoto
 */
public class StructureModel
{
    /** 構造の名前 */
    private final String structureName_;

    /** クラス一覧（キーはパッケージ名を含むクラス名） */
    private final Map<String, ClassModel> classes_;

    private final Queue<String> classNameQueue_;

    /** エラーレベルの閾値 */
    private long alarmThreshold_;

    /** 警告レベルの閾値 */
    private long warningThreshold_;

    /** 保持するComponentModelの最大数。 */
    public static final int CLASS_COUNT_MAX = 1000;

    /**
     * システムの構造を表すオブジェクトを作成します。<br />
     *
     * @param structureName 構造の名前
     */
    public StructureModel(final String structureName)
    {
        this.structureName_ = structureName;
        this.classes_ = new ConcurrentHashMap<String, ClassModel>();
        this.classNameQueue_ = new LinkedList<String>();
    }

    /**
     * システムの構造を表すオブジェクトを作成します。<br />
     */
    public StructureModel()
    {
        this.structureName_ = "";
        this.classes_ = new ConcurrentHashMap<String, ClassModel>();
        this.classNameQueue_ = new LinkedList<String>();
    }

    /**
     * クラスモデルをマージします。<br />
     *
     * @param classModel クラスモデル
     */
    public void mergeClassModel(final ClassModel classModel)
    {
        ClassModel existedClassModel = this.classes_.get(classModel.getClassName());
        if (existedClassModel != null)
        {
            for (String callerClass : classModel.getCallerClasses())
            {
                existedClassModel.addCallerClass(callerClass);
            }
            for (MethodModel methodModel : classModel.getMethods())
            {
                existedClassModel.addMethodModel(methodModel);
            }
        }
        else
        {
            addClassModel(classModel);
        }
    }

    /**
     * メソッドを追加します。<br />
     *
     * 必要であれば、 {@link ClassModel} を作成します。<br />
     * すでに指定されたメソッドが存在する場合は、置き換えます。<br />
     *
     * @param methodModel メソッド
     */
    public void addMethodModel(final MethodModel methodModel)
    {
        String fullClassName = methodModel.getFullClassName();
        ClassModel classModel = this.classes_.get(fullClassName);
        if (classModel == null)
        {
            classModel = new ClassModel(methodModel.getPackageName(), methodModel.getClassName());
            addClassModel(classModel);
        }
        classModel.addMethodModel(methodModel);
    }

    /**
     * クラスを追加します。<br />
     *
     * すでに指定されたクラスが存在する場合は、置き換えます。<br />
     *
     * @param classModel クラス
     */
    public void addClassModel(final ClassModel classModel)
    {
        if (this.classNameQueue_.size() >= CLASS_COUNT_MAX)
        {
            String classNameToRemove = this.classNameQueue_.poll();
            this.classes_.remove(classNameToRemove);
        }
        this.classes_.put(classModel.getFullClassName(), classModel);
        this.classNameQueue_.offer(classModel.getFullClassName());
    }

    /**
     * 指定されたクラスを返します。<br />
     *
     * @param fullClassName パッケージ名を含めたクラス名
     * @return 指定されたクラスが存在する場合は {@link ClassModel} インスタンス、存在しない場合は <code>null</code>
     */
    public ClassModel getClassModel(final String fullClassName)
    {
        return this.classes_.get(fullClassName);
    }

    /**
     * 指定されたパッケージ配下にあるクラスのリストを返します。<br />
     *
     * @param packageName パッケージ名
     * @return 指定されたパッケージ配下にあるクラスのリスト
     */
    public List<ClassModel> getClassModelsIn(final String packageName)
    {
        List<ClassModel> classesInPackage = new ArrayList<ClassModel>();
        for (ClassModel classModel : this.classes_.values())
        {
            if (classModel.getFullClassName().startsWith(packageName))
            {
                classesInPackage.add(classModel);
            }
        }
        return classesInPackage;
    }

    /**
     * クラスの数を返します。<br />
     *
     * @return クラス数
     */
    public int getClassModelSize()
    {
        return this.classes_.size();
    }

    /**
     * クラスをクリアします。<br />
     */
    public void clear()
    {
        this.classes_.clear();
        this.classNameQueue_.clear();
    }

    /**
     * 構造の名前を返します。<br />
     *
     * @return 構造の名前
     */
    public String getStructureName()
    {
        return this.structureName_;
    }

    /**
     * このシステムに存在するクラスの一覧を返します。<br />
     *
     * @return クラス一覧
     */
    public Collection<ClassModel> getClasses()
    {
        return Collections.unmodifiableCollection(this.classes_.values());
    }

    /**
     * モデルからクラスを削除します。<br />
     *
     * @param className 削除するクラス名
     */
    public void removeClassModel(final String className)
    {
        this.classes_.remove(className);
    }

    /**
     * エラーレベルの閾値をセットします。<br />
     *
     * @param alarmThreshold 閾値
     */
    public void setAlarmThreshold(final long alarmThreshold)
    {
        this.alarmThreshold_ = alarmThreshold;
    }

    /**
     * エラーレベルの閾値を返します。<br />
     *
     * @return 閾値
     */
    public long getAlarmThreshold()
    {
        return this.alarmThreshold_;
    }

    /**
     * 警告レベルの閾値をセットします。<br />
     *
     * @param warningThreshold 閾値
     */
    public void setWarningThreshold(final long warningThreshold)
    {
        this.warningThreshold_ = warningThreshold;
    }

    /**
     * 警告レベルの閾値を返します。<br />
     *
     * @return 閾値
     */
    public long getWarningThreshold()
    {
        return this.warningThreshold_;
    }

    /**
     * メソッドが存在しないクラスを削除します。<br />
     */
    public void removeEmptyClass()
    {
        Iterator<Map.Entry<String, ClassModel>> entryIterator = this.classes_.entrySet().iterator();
        while (entryIterator.hasNext())
        {
            Map.Entry<String, ClassModel> entry = entryIterator.next();
            if (entry.getValue().getMethods().size() == 0)
            {
                entryIterator.remove();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        String structureName = "";
        if (this.structureName_.length() > 0)
        {
            structureName = this.structureName_;
        }
        return structureName + this.classes_.keySet();
    }

}
