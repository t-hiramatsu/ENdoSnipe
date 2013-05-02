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

public class MapListForm
{
    /** マップモード(運用or編集) */
    private String mapMode;

    /** ResourceTreeViewの展開状態 */
    private String treeViewOpenNodeData;

    /** 表示しているツリー名 */
    private String displayTreeArea;

    /** 選択しているResourceTreeViewのID */
    private String selectedTreeId;

    /** 選択しているResourceMapListViewのID */
    private String selectedMapListId;

    public String getMapMode()
    {
        return mapMode;
    }

    public void setMapMode(final String mapMode)
    {
        this.mapMode = mapMode;
    }

    public String getTreeViewOpenNodeData()
    {
        return treeViewOpenNodeData;
    }

    public void setTreeViewOpenNodeData(final String treeViewOpenNodeData)
    {
        this.treeViewOpenNodeData = treeViewOpenNodeData;
    }

    public String getDisplayTreeArea()
    {
        return displayTreeArea;
    }

    public void setDisplayTreeArea(final String displayTreeArea)
    {
        this.displayTreeArea = displayTreeArea;
    }

    public String getSelectedTreeId()
    {
        return selectedTreeId;
    }

    public void setSelectedTreeId(final String selectedTreeId)
    {
        this.selectedTreeId = selectedTreeId;
    }

    public String getSelectedMapListId()
    {
        return selectedMapListId;
    }

    public void setSelectedMapListId(final String selectedMapListId)
    {
        this.selectedMapListId = selectedMapListId;
    }

}
