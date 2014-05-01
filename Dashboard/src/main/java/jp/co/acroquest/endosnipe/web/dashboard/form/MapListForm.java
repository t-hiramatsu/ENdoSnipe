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
package jp.co.acroquest.endosnipe.web.dashboard.form;

/**
 * マップの情報を持つクラス。
 * 
 * @author miyasaka
 *
 */
public class MapListForm
{
    /** マップモード(運用or編集) */
    private String mapMode_;

    /** ResourceTreeViewの展開状態 */
    private String treeViewOpenNodeData_;

    /** 表示しているツリー名 */
    private String displayTreeArea_;

    /** 選択しているResourceTreeViewのID */
    private String selectedTreeId_;

    /** 選択しているResourceMapListViewのID */
    private String selectedMapListId_;

    /**
     * コンストラクタ。
     */
    public MapListForm()
    {

    }

    /**
     * マップモードを取得する。
     * 
     * @return マップモード
     */
    public String getMapMode()
    {
        return mapMode_;
    }

    /**
     * マップモードを設定する。
     * 
     * @param mapMode マップモード
     */
    public void setMapMode(final String mapMode)
    {
        this.mapMode_ = mapMode;
    }

    /**
     * ResourceTreeViewの展開状態を取得する。
     * 
     * @return ResourceTreeViewの展開状態
     */
    public String getTreeViewOpenNodeData()
    {
        return treeViewOpenNodeData_;
    }

    /**
     * ResourceTreeViewの展開状態を設定する。
     * 
     * @param treeViewOpenNodeData ResourceTreeViewの展開状態
     */
    public void setTreeViewOpenNodeData(final String treeViewOpenNodeData)
    {
        this.treeViewOpenNodeData_ = treeViewOpenNodeData;
    }

    /**
     * 表示しているツリー名を取得する。
     * 
     * @return 表示しているツリー名
     */
    public String getDisplayTreeArea()
    {
        return displayTreeArea_;
    }

    /**
     * 表示しているツリー名を設定する。
     * 
     * @param displayTreeArea 表示しているツリー名
     */
    public void setDisplayTreeArea(final String displayTreeArea)
    {
        this.displayTreeArea_ = displayTreeArea;
    }

    /**
     * 選択しているResourceTreeViewのIDを取得する。
     * 
     * @return 選択しているResourceTreeViewのID
     */
    public String getSelectedTreeId()
    {
        return selectedTreeId_;
    }

    /**
     * 選択しているResourceTreeViewのID
     * 
     * @param selectedTreeId 選択しているResourceTreeViewのID
     */
    public void setSelectedTreeId(final String selectedTreeId)
    {
        this.selectedTreeId_ = selectedTreeId;
    }

    /**
     * 選択しているResourceMapListViewのIDを取得する。
     * 
     * @return 選択しているResourceMapListViewのID
     */
    public String getSelectedMapListId()
    {
        return selectedMapListId_;
    }

    /**
     * 選択しているResourceMapListViewのIDを設定する。
     * 
     * @param selectedMapListId 選択しているResourceMapListViewのID
     */
    public void setSelectedMapListId(final String selectedMapListId)
    {
        this.selectedMapListId_ = selectedMapListId;
    }

}
