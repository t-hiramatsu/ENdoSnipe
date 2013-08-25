/*
 * Copyright (c) 2004-2013 Acroquest Technology Co., Ltd. All Rights Reserved.
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
package jp.co.acroquest.endosnipe.javelin.communicate.config;

/**
 * リモート設定画面のテーブルの一行分の要素。
 * 
 * @author eriguchi
 */
public class PropertyEntry
{
    /** 項目名 */
    private String property_ = "";

    /** 項目の解説 */
    private String propertyDetail_ = "";

    /** 現在の設定値 */
    private String currentValue_ = "";

    /** 変更後の値 */
    private String updateValue_ = "";

    /**
     * @return the property_
     */
    public String getProperty()
    {
        return this.property_;
    }

    /**
     * @param property the property_ to set
     */
    public void setProperty(final String property)
    {
        this.property_ = property;
    }

    /**
     * @return the propertyDetail_
     */
    public String getPropertyDetail()
    {
        return this.propertyDetail_;
    }

    /**
     * @param propertyDetail the propertyDetail_ to set
     */
    public void setPropertyDetail(final String propertyDetail)
    {
        this.propertyDetail_ = propertyDetail;
    }

    /**
     * @return the currentValue_
     */
    public String getCurrentValue()
    {
        return this.currentValue_;
    }

    /**
     * @param currentValue the currentValue_ to set
     */
    public void setCurrentValue(final String currentValue)
    {
        this.currentValue_ = currentValue;
    }

    /**
     * @return the updateValue_
     */
    public String getUpdateValue()
    {
        return this.updateValue_;
    }

    /**
     * @param updateValue the updateValue_ to set
     */
    public void setUpdateValue(final String updateValue)
    {
        this.updateValue_ = updateValue;
    }

}