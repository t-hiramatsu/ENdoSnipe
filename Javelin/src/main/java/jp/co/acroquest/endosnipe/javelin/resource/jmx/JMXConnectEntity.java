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
package jp.co.acroquest.endosnipe.javelin.resource.jmx;

import java.util.ArrayList;
import java.util.List;

/**
 * JMXの接続を行うためのエンティティです。
 * @author fujii
 *
 */
public class JMXConnectEntity
{
    /** ID */
    private String                 id_;

    /** JMXの接続先URL */
    private String                 url_;

    /** ユーザ */
    private String                 user_;

    /** パスワード */
    private String                 password_;

    /** 接続対象リスト */
    private List<MBeanValueGetter> resourceList_ = new ArrayList<MBeanValueGetter>();

    /**
     * IDを取得する。
     * @return ID
     */
    public String getId()
    {
        return id_;
    }

    /**
     * IDを設定する。
     * @param id ID
     */
    public void setId(String id)
    {
        id_ = id;
    }

    /**
     * JMXの接続先URLを取得する。
     * @return JMXの接続先URL
     */
    public String getUrl()
    {
        return url_;
    }

    /**
     * JMXの接続先URLを設定する。
     * @param url JMXの接続先URL
     */
    public void setUrl(String url)
    {
        url_ = url;
    }

    /**
     * ユーザを取得する。
     * @return ユーザ
     */
    public String getUser()
    {
        return user_;
    }

    /**
     * ユーザを設定する。
     * @param user ユーザ
     */
    public void setUser(String user)
    {
        user_ = user;
    }

    /**
     * パスワードを取得する。
     * @return パスワード
     */
    public String getPassword()
    {
        return password_;
    }

    /**
     * パスワードを設定する。
     * @param password パスワード
     */
    public void setPassword(String password)
    {
        password_ = password;
    }

    /**
     * 接続対象リストを取得する。
     * @return 接続対象リスト
     */
    public List<MBeanValueGetter> getResourceList()
    {
        return resourceList_;
    }

    /**
     * 接続対象をリストに追加する。
     * @param resource 接続対象
     */
    public void addResource(MBeanValueGetter resource)
    {
        resourceList_.add(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "JMXConnectEntity [id=" + id_ + ", url=" + url_ + ", user=" + user_
                + ", password=" + password_ + ", resourceList=" + resourceList_ + "]";
    }

}
