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
package jp.co.acroquest.endosnipe.web.explorer.dto;

/**
 * リソースグラフ・マルチリソースグラフのオブジェクト
 * @author kamo
 *
 */
public class ResourceDto
{
    /** オブジェクトのID */
    private String objectId_;

    /** オブジェクトの名前 */
    private String objectName_;

    /** 対象のID */
    private String resourceId_;

    /** x座標 */
    private int pointX_;

    /** y座標 */
    private int pointY_;

    /** 横幅 */
    private int width_;

    /** 高さ */
    private int height_;

    /** 深度 */
    private int zIndex_;

    /** 図形名 */
    private String shapeName_;

    /** 図形タイプ */
    private String shapeType_;

    /** オプション */
    private Object elementAttrList_;

    /** 状態ID */
    private String stateId_;

    /**
     * コンストラクタ
     */
    public ResourceDto()
    {
    }

    /**
     * オブジェクトのIDを取得する
     * @return オブジェクトのID
     */
    public String getObjectId()
    {
        return objectId_;
    }

    /**
     * オブジェクトのIDを設定する
     * @param objectId オブジェクトのID
     */
    public void setObjectId(final String objectId)
    {
        this.objectId_ = objectId;
    }

    /**
     * オブジェクトの名前を取得する
     * @return オブジェクトの名前
     */
    public String getObjectName()
    {
        return objectName_;
    }

    /**
     * オブジェクトの名前を設定する
     * @param objectName オブジェクトの名前
     */
    public void setObjectName(final String objectName)
    {
        this.objectName_ = objectName;
    }

    /**
     * 対象のIDを取得する
     * @return 対象のID
     */
    public String getResourceId()
    {
        return resourceId_;
    }

    /**
     * 対象のIDを設定する
     * @param resourceId 対象のID
     */
    public void setResourceId(final String resourceId)
    {
        this.resourceId_ = resourceId;
    }

    /**
     * x座標を取得する
     * @return x座標
     */
    public int getPointX()
    {
        return pointX_;
    }

    /**
     * x座標を設定する
     * @param pointX x座標
     */
    public void setPointX(final int pointX)
    {
        pointX_ = pointX;
    }

    /**
     * y座標を取得する
     * @return y座標
     */
    public int getPointY()
    {
        return pointY_;
    }

    /**
     * y座標を設定する
     * @param pointY y座標
     */
    public void setPointY(final int pointY)
    {
        pointY_ = pointY;
    }

    /**
     * 横幅を取得する
     * @return 横幅
     */
    public int getWidth()
    {
        return width_;
    }

    /**
     * 横幅を設定する
     * @param width 横幅
     */
    public void setWidth(final int width)
    {
        width_ = width;
    }

    /**
     * 高さを取得する
     * @return 高さ
     */
    public int getHeight()
    {
        return height_;
    }

    /**
     * 高さを設定する
     * @param height 高さ
     */
    public void setHeight(final int height)
    {
        height_ = height;
    }

    /**
     * 深度を取得する
     * @return 深度
     */
    public int getzIndex()
    {
        return zIndex_;
    }

    /**
     * 深度を設定する
     * @param zIndex 深度
     */
    public void setzIndex(final int zIndex)
    {
        zIndex_ = zIndex;
    }

    /**
     * 図形名を取得する
     * @return 図形名
     */
    public String getShapeName()
    {
        return shapeName_;
    }

    /**
     * 図形名を設定する
     * @param shapeName 図形名
     */
    public void setShapeName(final String shapeName)
    {
        shapeName_ = shapeName;
    }

    /**
     * 図形タイプを取得する
     * @return 図形タイプ
     */
    public String getShapeType()
    {
        return shapeType_;
    }

    /**
     * 図形タイプを設定する
     * @param shapeType 図形タイプ
     */
    public void setShapeType(final String shapeType)
    {
        shapeType_ = shapeType;
    }

    /**
     * オプションを取得する
     * @return オプション
     */
    public Object getElementAttrList()
    {
        return elementAttrList_;
    }

    /**
     * オプションを設定する
     * @param elementAttrList オプション
     */
    public void setElementAttrList(final Object elementAttrList)
    {
        elementAttrList_ = elementAttrList;
    }

    /**
     * 状態IDを取得する
     * @return 状態ID
     */
    public String getStateId()
    {
        return stateId_;
    }

    /**
     * 状態IDを設定する
     * @param stateId 状態ID
     */
    public void setStateId(final String stateId)
    {
        stateId_ = stateId;
    }

}
