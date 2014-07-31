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

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.web.explorer.util.ComponentModelUtil;

/**
 * システム構造の中の 1 クラスを表すクラス。<br />
 *
 * @see StructureModel
 * @see MethodModel
 *
 * @author sakamoto
 */
public class ClassModel
{
    /** パッケージ名 */
    private final String packageName_;

    /** パッケージを含めないクラス名 */
    private final String className_;

    /** メソッド一覧（キーはメソッド名） */
    private final Map<String, MethodModel> methodMap_;

    public Map<String, MethodModel> getMethodMap()
    {
        return methodMap_;
    }

    /** メソッド一覧（合計時間が大きいものからソートされている） */
    private final SortedSet<MethodModel> sortedMethodSet_;

    /** 呼び出し元クラスの集合 */
    private final Set<String> callerClasses_;

    /** 保持するMethodModelの最大数。 */
    public static final int METHOD_COUNT_MAX = 1000;

    /**
     * クラスを表すオブジェクトを作成します。<br />
     *
     * @param packageName パッケージ名
     * @param className パッケージ名を含めないクラス名
     */
    public ClassModel(final String packageName, final String className)
    {
        this.packageName_ = packageName;
        this.className_ = className;
        this.methodMap_ = new ConcurrentHashMap<String, MethodModel>();
        MethodModelComparator comparator = new MethodModelComparator();
        this.sortedMethodSet_ = new TreeSet<MethodModel>(comparator);
        this.callerClasses_ = new HashSet<String>();
    }

    /**
     * クラスを表すオブジェクトを作成します。<br />
     *
     * @param fullClassName パッケージ名を含むクラス名
     */
    public ClassModel(final String fullClassName)
    {
        String[] packageAndClass = ComponentModelUtil.splitPackageAndClass(fullClassName);
        this.packageName_ = packageAndClass[0];
        this.className_ = packageAndClass[1];
        this.methodMap_ = new ConcurrentHashMap<String, MethodModel>();
        MethodModelComparator comparator = new MethodModelComparator();
        this.sortedMethodSet_ = new TreeSet<MethodModel>(comparator);
        this.callerClasses_ = new HashSet<String>();
    }

    /**
     * メソッドを追加します。<br />
     *
     * すでに指定されたメソッドが存在する場合は、置き換えます。<br />
     * また、 1 クラスあたりのメソッド保持制限（ {@link MainCtrl#METHOD_COUNT_MAX} ）を超えた場合は、
     * メソッド実行の合計時間が最も短いメソッドを削除します。
     *
     * @param methodModel メソッド
     * @return 保持制限を超えた場合に削除されたメソッド
     */
    public synchronized MethodModel addMethodModel(final MethodModel methodModel)
    {
        this.methodMap_.put(methodModel.getMethodName(), methodModel);
        this.sortedMethodSet_.remove(methodModel);
        this.sortedMethodSet_.add(methodModel);

        // 最大保持数を超えているかチェックする
        MethodModel methodToRemove = null;
        if (this.sortedMethodSet_.size() > METHOD_COUNT_MAX)
        {
            methodToRemove = this.sortedMethodSet_.last();
            this.methodMap_.remove(methodToRemove.getMethodName());
            this.sortedMethodSet_.remove(methodToRemove);
        }
        return methodToRemove;
    }

    /**
     * メソッドを削除します。<br />
     * 
     * @param methodModel メソッド
     */
    public synchronized void removeMethodModel(final MethodModel methodModel)
    {
        if (this.methodMap_.containsKey(methodModel.getMethodName()))
        {
            MethodModel methodToRemove = this.methodMap_.remove(methodModel.getMethodName());
            this.sortedMethodSet_.remove(methodToRemove);
        }
    }

    /**
     * 呼び出し元クラスを追加します。<br />
     *
     * 指定されたクラスがすでに存在する場合は、何も行いません。<br />
     *
     * @param className クラス名
     */
    public void addCallerClass(final String className)
    {
        this.callerClasses_.add(className);
    }

    /**
     * 呼び出し元クラスを追加します。<br />
     *
     * 指定されたクラスがすでに存在する場合は、何も行いません。<br />
     *
     * @param classNameSet クラス名の集合
     */
    public void addCallerClasses(final Collection<String> classNameSet)
    {
        this.callerClasses_.addAll(classNameSet);
    }

    /**
     * 呼び出し元クラスの集合を返します。<br />
     *
     * @return 呼び出し元クラスの集合
     */
    public Set<String> getCallerClasses()
    {
        return Collections.unmodifiableSet(this.callerClasses_);
    }

    /**
     * パッケージ名を含めたクラス名を返します。<br />
     *
     * @return パッケージ名を含めたクラス名
     */
    public String getFullClassName()
    {
        return ComponentModelUtil.getFullClassName(this.packageName_, this.className_);
    }

    /**
     * パッケージ名を含めないクラス名を返します。<br />
     *
     * @return パッケージ名を含めないクラス名
     */
    public String getClassName()
    {
        return this.className_;
    }

    /**
     * パッケージ名を返します。<br />
     *
     * @return パッケージ名
     */
    public String getPackageName()
    {
        return this.packageName_;
    }

    /**
     * このクラスに存在するメソッド一覧を返します。<br />
     *
     * @return メソッド一覧
     */
    public Collection<MethodModel> getMethods()
    {
        return Collections.unmodifiableCollection(this.sortedMethodSet_);
    }

    /**
     * 指定されたメソッド名のメソッドを返します。<br />
     *
     * @param methodName メソッド名
     * @return メソッドが存在する場合はメソッドオブジェクト、存在しない場合は <code>null</code>
     */
    public synchronized MethodModel getMethod(final String methodName)
    {
        return this.methodMap_.get(methodName);
    }

    /**
     * 指定されたメソッドを残し、他のメソッドを削除します。<br />
     *
     * 引数が <code>null</code> の場合は、すべてのメソッドを削除します。<br />
     *
     * @param methodNameList 残すメソッドのリスト
     */
    public synchronized void retainAll(final Collection<String> methodNameList)
    {
        if (methodNameList != null)
        {
            Iterator<MethodModel> iterator = this.sortedMethodSet_.iterator();
            while (iterator.hasNext())
            {
                MethodModel entry = iterator.next();
                if (!methodNameList.contains(entry.getMethodName()))
                {
                    iterator.remove();
                    this.methodMap_.remove(entry.getMethodName());
                }
            }
        }
        else
        {
            this.methodMap_.clear();
            this.sortedMethodSet_.clear();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return getFullClassName() + this.sortedMethodSet_;
    }

    /**
     * {@link MethodModel} の比較を行うクラス。<br />
     *
     * 総計実行時間の長いメソッドが優先されます。
     *
     * @author sakamoto
     */
    private static class MethodModelComparator implements Comparator<MethodModel>, Serializable
    {
        private static final long serialVersionUID = -6443459326178744224L;

        /**
         * {@inheritDoc}
         */
        public int compare(final MethodModel method1, final MethodModel method2)
        {
            int ret = (int)(method2.getTotal() - method1.getTotal());
            if (ret == 0)
            {
                ret = (int)(method2.getMaximum() - method1.getMaximum());
                if (ret == 0)
                {
                    ret = (int)(method2.getMinimum() - method1.getMinimum());
                    if (ret == 0)
                    {
                        ret = method2.getMethodName().compareTo(method1.getMethodName());
                    }
                }
            }
            return ret;
        }
    }
}
