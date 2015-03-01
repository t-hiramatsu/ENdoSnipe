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
