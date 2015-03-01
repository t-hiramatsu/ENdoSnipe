/*******************************************************************************
 * ENdoSnipe 5.3 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Acroquest Technology Co.,Ltd.
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
package jp.co.acroquest.endosnipe.web.explorer.entity;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 画面に表示するコンポーネント。<br />
 *
 * @author hiramatsu
 */
public class ComponentModel extends AbstractModel implements Iterable<InvocationModel>
{

    /** 制約の変更 */
    public static final String P_CONSTRAINT = "_constraint";

    /** クラス名の変更 */
    public static final String P_CLASS_NAME = "_className";

    /** メソッド名の変更 */
    private static final String P_INVOCATION = "_invocation";

    /** 子モデルの変更を識別するための文字列。 */
    public static final String P_CHILDREN = "_children";

    /** 接続元コネクションの変更 */
    public static final String P_SOURCE_CONNECTION = "_source_connection";

    /** 接続先コネクションの変更 */
    public static final String P_TARGET_CONNECTION = "_target_connection";

    /** 警告の閾値の変更 */
    public static final String P_EXCEEDED_THRESHOLD_ALARM = "exceededThresholdMethodName";

    /** 警告の閾値 */
    private String exceededThresholdMethodName_ = "";

    /** クラス名 */
    private String componentName_;

    /** Invocationのリスト */
    private final SortedSet<InvocationModel> invocationList_;

    /** Invocationのマップ */
    private final Map<String, InvocationModel> invocationMap_ =
            new TreeMap<String, InvocationModel>();

    /** 制約 */
    private Rectangle constraint_; // 制約

    /** コンポーネントタイプ */
    private ComponentType componentType_;

    /** このモデルから伸びているコネクションのリスト */
    private final List<AbstractConnectionModel> sourceConnections_ =
            new ArrayList<AbstractConnectionModel>();

    /** このモデルに向かって張られているコネクションのリスト */
    private final List<AbstractConnectionModel> targetConnections_ =
            new ArrayList<AbstractConnectionModel>();

    /** このモデルがユーザによって位置を指定されている場合は <code>true</code> */
    private boolean positionSpecified_ = false;

    /** このモデルがユーザによって削除されている場合は <code>true</code> */
    private boolean isDeleted_ = false;

    /**
     * コンストラクタ。
     */
    public ComponentModel()
    {
        Comparator<InvocationModel> comparator = new InvocationModelComparator();
        this.invocationList_ = new TreeSet<InvocationModel>(comparator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getPropertyValue(final Object id)
    {
        if (P_CLASS_NAME.equals(id))
        {
            // プロパティ・ビューに表示するデータを返す
            return this.componentName_;
        }
        if (id instanceof String)
        {
            String idString = (String)id;
            if (idString.startsWith(P_INVOCATION))
            {
                String methodName = idString.substring(P_INVOCATION.length());
                InvocationModel invocationModel = this.invocationMap_.get(methodName);
                return invocationModel.toString();
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPropertySet(final Object id)
    {
        if (id.equals(P_CLASS_NAME))
        {
            return true;
        }

        if (id instanceof String)
        {
            String text = (String)id;
            if (text.startsWith(P_INVOCATION))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPropertyValue(final Object id, final Object value)
    {
        // Do Nothing.
    }

    /**
     * コンポーネント名を取得する。
     * @return コンポーネント名
     */
    public String getComponentName()
    {
        return this.componentName_;
    }

    /**
     * コンポーネント名を設定します。<br />
     *
     * コンポーネントの種類も同時に設定します。<br />
     *
     * @param componentName クラス名
     */
    public void setComponentName(final String componentName)
    {
        this.componentName_ = componentName;
        this.componentType_ = ComponentType.getComponentType(componentName);
        firePropertyChange(P_CLASS_NAME, null, this.componentName_);
    }

    /**
     * コンポーネントの種類を設定します。<br />
     *
     * @param componentType コンポーネントの種類
     */
    public void setComponentType(final ComponentType componentType)
    {
        this.componentType_ = componentType;
    }

    /**
     * Invocationを追加する。
     * @param invocation Invocation
     * @return Invocation リストに変更があった場合は <code>true</code> 、そうでない場合は <code>false</code>
     */
    public boolean addInvocation(final InvocationModel invocation)
    {

        String methodName = invocation.getMethodName();
        InvocationModel oldInvocation = this.invocationMap_.get(methodName);
        this.invocationMap_.put(methodName, invocation);
        invocation.setComponent(this);

        if (oldInvocation != null)
        {
            if (equalsInvocationModel(invocation, oldInvocation))
            {
                return false;
            }
            this.invocationList_.remove(oldInvocation);
        }
        this.invocationList_.add(invocation);

        return true;
    }

    /**
     * Invocation を削除します。
     *
     * @param methodName 削除する Invocation のメソッド名
     */
    public void removeInvocation(final String methodName)
    {
        InvocationModel deletedInvocation = this.invocationMap_.remove(methodName);
        if (deletedInvocation != null)
        {
            this.invocationList_.remove(deletedInvocation);
        }
    }

    /**  
     * View タブ上に表示される項目（メソッド名、最大時間、平均時間、計測対象）で比較します。<br />  
     *   
     * @param invocation1 InvocationModel  
     * @param invocation2 InvocationModel  
     * 
     * @return ２つのメソッド名、最大時間、平均時間、計測対象が同じ場合、<code>true</code>
     */
    private boolean equalsInvocationModel(final InvocationModel invocation1,
            final InvocationModel invocation2)
    {
        String methodName1 = invocation1.getMethodName();
        String methodName2 = invocation2.getMethodName();
        double average1 = invocation1.getAverageDouble();
        double average2 = invocation2.getAverageDouble();
        long maximum1 = invocation1.getMaximum();
        long maximum2 = invocation2.getMaximum();
        long callCount1 = invocation1.getCount();
        long callCount2 = invocation2.getCount();
        boolean equal =
                (methodName1.equals(methodName2) && average1 == average2 && maximum1 == maximum2
                        && callCount1 == callCount2 && invocation1.isTarget() == invocation2.isTarget());
        return equal;
    }

    /**
     * Invocation を削除します。<br />
     */
    public void clearInvocationList()
    {
        this.invocationList_.clear();
        this.invocationMap_.clear();
    }

    /**
     * Invocationの一覧を取得する。
     * @return Invocationの一覧
     */
    public Set<InvocationModel> getInvocationList()
    {
        return Collections.unmodifiableSet(this.invocationList_);
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<InvocationModel> iterator()
    {
        return new InvocationModelIterator(this.invocationList_.iterator());
    }

    /**
     * 制約を取得する。
     * @return 制約
     */
    public Rectangle getConstraint()
    {
        return this.constraint_;
    }

    /**
     * 制約を設定する。
     * @param constraint 制約
     */
    public void setConstraint(final Rectangle constraint)
    {
        this.constraint_ = constraint;
        // 変更の通知
        firePropertyChange(P_CONSTRAINT, null, constraint);
    }

    /**
     * このモデルが接続元となるコネクションモデルを追加する。
     * @param connx コネクションモデル
     */
    public void addSourceConnection(final AbstractConnectionModel connx)
    {
        this.sourceConnections_.add(connx);
        firePropertyChange(P_SOURCE_CONNECTION, null, null);
    }

    /**
     * このモデルが接続先となるコネクションモデルを追加する。
     * @param connx コネクションモデル
     */
    public void addTargetConnection(final AbstractConnectionModel connx)
    {
        this.targetConnections_.add(connx);
        firePropertyChange(P_TARGET_CONNECTION, null, null);
    }

    /**
     * このモデルが接続元となるコネクションモデル一覧を取得する。
     * @return コネクションモデル一覧
     */
    public List<? extends AbstractConnectionModel> getModelSourceConnections()
    {
        return this.sourceConnections_;
    }

    /**
     * このモデルが接続先となるコネクションモデル一覧を取得する。
     * @return コネクションモデル一覧
     */
    public List<? extends AbstractConnectionModel> getModelTargetConnections()
    {
        return this.targetConnections_;
    }

    /**
     * このモデルを接続元とするコネクションを切り離す。
     * @param connx コネクション
     */
    public void removeSourceConnection(final AbstractConnectionModel connx)
    {
        this.sourceConnections_.remove(connx);
        firePropertyChange(P_SOURCE_CONNECTION, null, null);
    }

    /**
     * このモデルを接続先とするコネクションを切り離す。
     * @param connx コネクション
     */
    public void removeTargetConnection(final AbstractConnectionModel connx)
    {
        this.targetConnections_.remove(connx);
        firePropertyChange(P_TARGET_CONNECTION, null, null);
    }

    /**
     * 警告の閾値を設定する。
     * @param exceededThresholdMethodName 警告の閾値
     */
    public void setExceededThresholdAlarm(final String exceededThresholdMethodName)
    {
        if (exceededThresholdMethodName == null)
        {
            this.exceededThresholdMethodName_ = null;
            return;
        }

        String oldMethodName = this.exceededThresholdMethodName_;
        this.exceededThresholdMethodName_ = exceededThresholdMethodName;
        this.firePropertyChange(P_EXCEEDED_THRESHOLD_ALARM, oldMethodName,
                                this.exceededThresholdMethodName_);
    }

    /**
     * コンポーネントタイプを取得する。
     * @return コンポーネントタイプ
     */
    public ComponentType getComponentType()
    {
        return this.componentType_;
    }

    /**
     * このモデルがユーザによって削除されているかどうかを調べる。
     *
     * @return このモデルがユーザによって削除されている場合は <code>true</code>
     */
    public boolean isDeleted()
    {
        return this.isDeleted_;
    }

    /**
     * ユーザによる削除フラグを設定する。
     *
     * @param isDeleted このモデルがユーザによって削除されている場合は <code>true</code>
     */
    public void setDeleted(final boolean isDeleted)
    {
        this.isDeleted_ = isDeleted;
    }

    /**
     * このモデルが、ユーザによって位置が指定されているかどうかを返します。<br />
     *
     * @return 位置が指定されている場合は <code>true</code> 、指定されていない場合は <code>false</code>
     */
    public boolean isPositionSpecified()
    {
        return this.positionSpecified_;
    }

    /**
     * このモデルが、ユーザによって位置が指定されているかどうかをセットします。<br />
     *
     * @param positionSpecified 位置が指定されている場合は <code>true</code> 、指定されていない場合は <code>false</code>
     */
    public void setPositionSpecified(final boolean positionSpecified)
    {
        this.positionSpecified_ = positionSpecified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return getComponentName() + getInvocationList().toString();
    }

    /**
     * remove() 時にリストが変更されたことを通知するイテレータです。<br />
     *
     * @author sakamoto
     */
    private class InvocationModelIterator implements Iterator<InvocationModel>
    {
        private final Iterator<InvocationModel> iterator_;

        public InvocationModelIterator(final Iterator<InvocationModel> iterator)
        {
            this.iterator_ = iterator;
        }

        public boolean hasNext()
        {
            return this.iterator_.hasNext();
        }

        public InvocationModel next()
        {
            return this.iterator_.next();
        }

        public void remove()
        {
            this.iterator_.remove();
            firePropertyChange(P_CONSTRAINT, null, ComponentModel.this);
        }

    }

    /**
     * {@link InvocationModel} を、最大実行時間が大きい順に並べるための比較関数。
     * 最大実行時間が等しい場合は、平均実行時間が大きい順に並べます。
     * 平均実行時間も等しい場合は、メソッド名で昇順に並べます。
     *
     * @author sakamoto
     */
    static class InvocationModelComparator implements Comparator<InvocationModel>, Serializable
    {
        private static final long serialVersionUID = 2963706918076102368L;

        /**
         * {@inheritDoc}
         */
        public int compare(final InvocationModel o1, final InvocationModel o2)
        {
            int ret = Long.signum(o2.getMaximum() - o1.getMaximum());
            if (ret == 0)
            {
                ret = Double.compare(o2.getAverageDouble(), o1.getAverageDouble());
                if (ret == 0)
                {
                    ret = o1.getMethodName().compareTo(o2.getMethodName());
                }
            }
            return ret;
        }
    }
}
