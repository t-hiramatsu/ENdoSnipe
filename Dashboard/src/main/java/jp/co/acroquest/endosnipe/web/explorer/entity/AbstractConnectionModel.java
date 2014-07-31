/*
 * Copyright (c) 2004-2008 SMG Co., Ltd. All Rights Reserved.
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

public abstract class AbstractConnectionModel
{
    private ComponentModel source_;

    private ComponentModel target_;

    /**
     * このコネクションの根元 をsource に接続します。<br />
     */
    public void attachSource()
    {
        // このコネクションが既に接続されている場合は何もしない
        if (!this.source_.getModelSourceConnections().contains(this))
        {
            this.source_.addSourceConnection(this);
        }
    }

    /**
     * このコネクションの先端を target に接続します。<br />
     */
    public void attachTarget()
    {
        // このコネクションが既に接続されている場合は何もしない
        if (!this.target_.getModelTargetConnections().contains(this))
        {
            this.target_.addTargetConnection(this);
        }
    }

    /**
     * このコネクションの根元を source から取り外します。<br />
     */
    public void detachSource()
    {
        this.source_.removeSourceConnection(this);
    }

    /**
     * このコネクションの先端を target から取り外します。<br />
     */
    public void detachTarget()
    {
        this.target_.removeTargetConnection(this);
    }

    public ComponentModel getSource()
    {
        return this.source_;
    }

    public ComponentModel getTarget()
    {
        return this.target_;
    }

    public void setSource(final ComponentModel model)
    {
        this.source_ = model;
    }

    public void setTarget(final ComponentModel model)
    {
        this.target_ = model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return getSource().getComponentName() + "->" + getTarget().getComponentName();
    }
}
