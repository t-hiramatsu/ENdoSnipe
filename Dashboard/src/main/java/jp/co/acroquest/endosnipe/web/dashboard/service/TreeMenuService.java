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
package jp.co.acroquest.endosnipe.web.dashboard.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.data.dao.JavelinMeasurementItemDao;
import jp.co.acroquest.endosnipe.data.entity.JavelinMeasurementItem;
import jp.co.acroquest.endosnipe.web.dashboard.constants.LogMessageCodes;
import jp.co.acroquest.endosnipe.web.dashboard.constants.TreeMenuConstants;
import jp.co.acroquest.endosnipe.web.dashboard.dto.TreeMenuDto;
import jp.co.acroquest.endosnipe.web.dashboard.manager.DatabaseManager;

import org.springframework.stereotype.Service;

/**
 * 
 * ツリーメニューに関する操作を行うクラスです。
 *
 * @author fujii
 *
 */
@Service
public class TreeMenuService
{
    /** ロガー。 */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(MapService.class);

    /** ツリー階層の区切り文字 */
    private static final String TREE_SEPARATOR = "/";

    /** ツリー階層のセパレートパターン */
    private static final Pattern TREE_SEPARATE_PATTERN = Pattern.compile(TREE_SEPARATOR);

    /** 単位の区切り文字 */
    private static final String UNIT_SEPARATOR = ":";

    /** タイプ：グループ。 */
    private static final String GROUP_TYPE = "group";

    /** タイプ：ターゲット。 */
    private static final String TARGET_TYPE = "target";

    /** アイコン：グループ。 */
    private static final String GROUP_ICON = "center";

    /** アイコン：ターゲット。 */
    private static final String TARGET_ICON = "leaf";

    /**
     * コンストラクタ
     */
    public TreeMenuService()
    {

    }

    /**
     * 初期化を行う。
     * @return 初期描画時のツリーメニュー
     */
    public List<TreeMenuDto> initialize()
    {
        DatabaseManager dbMmanager = DatabaseManager.getInstance();
        // TODO エージェントIDは0固定
        String dbName = dbMmanager.getDataBaseName(1);
        List<JavelinMeasurementItem> javelinMeasurementItemList = null;
        List<TreeMenuDto> treeMenuDtoList = new ArrayList<TreeMenuDto>();
        Map<String, TreeMenuDto> treeMenuMap = new LinkedHashMap<String, TreeMenuDto>();
        try
        {
            javelinMeasurementItemList = JavelinMeasurementItemDao.selectAll(dbName);
        }
        catch (SQLException sqlEx)
        {
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
        }

        if (javelinMeasurementItemList == null)
        {
            return treeMenuDtoList;
        }
        // TODO ツリー階層を作成すること。
        for (JavelinMeasurementItem javelinMeasurementItem : javelinMeasurementItemList)
        {
            String itemName = javelinMeasurementItem.itemName;
            addTreeData(treeMenuMap, itemName);
        }
        treeMenuDtoList = new ArrayList<TreeMenuDto>(treeMenuMap.values());
        return treeMenuDtoList;
    }

    /**
     * ツリーのデータを追加します。
     * @param treeMenuDtoMap ツリーメニュー情報のMap
     * @param itemName 項目名
     */
    private void addTreeData(final Map<String, TreeMenuDto> treeMenuDtoMap, final String itemName)
    {
        String[] itemList = TREE_SEPARATE_PATTERN.split(itemName);
        if (itemList.length < 1)
        {
            return;
        }
        String currentId = TREE_SEPARATOR;
        for (int cnt = 1; cnt < itemList.length; cnt++)
        {
            // ツリーの階層が最下層である場合はターゲット
            // そうではない場合はグループ
            String treeMenuType = "";
            if (cnt + 1 == itemList.length)
            {
                treeMenuType = TreeMenuConstants.TREE_MENU_TYPE_TARGET;
            }
            else
            {
                treeMenuType = TreeMenuConstants.TREE_MENU_TYPE_GROUP;
            }

            addTree(treeMenuDtoMap, currentId, itemList[cnt], treeMenuType);
            currentId += itemList[cnt] + TREE_SEPARATOR;
        }
    }

    /**
     * ツリーメニューを追加します。
     * @param treeMenuDtoMap ツリーメニューのMap
     * @param parentId 親ノードのID
     * @param itemName 項目名
     * @param treeMenuType ツリーメニューのタイプ
     */
    private void addTree(final Map<String, TreeMenuDto> treeMenuDtoMap, final String parentId,
            final String itemName, final String treeMenuType)
    {
        String currentItemName = itemName;
        String unitName = "";
        int unitPosition = itemName.indexOf(UNIT_SEPARATOR);
        if (unitPosition > 0)
        {
            unitName = itemName.substring(unitPosition + 1);
        }
        String currentId = parentId + currentItemName;
        TreeMenuDto menuDto = treeMenuDtoMap.get(currentId);
        if (menuDto != null)
        {
            return;
        }
        menuDto = new TreeMenuDto();
        menuDto.setId(currentId);
        menuDto.setTreeId(currentId);
        menuDto.setData(currentItemName);
        menuDto.setMeasurementUnit(unitName);
        menuDto.setParentTreeId(parentId.substring(0, parentId.length() - 1));
        menuDto.setType(treeMenuType);
        treeMenuDtoMap.put(currentId, menuDto);
    }

    /**
     * 指定された親ノードの直下にある子要素のパスの一覧を取得する。
     * 
     * @param parentTreeId 親ノードのID
     * @return 子要素のリスト
     */
    public List<TreeMenuDto> getDirectChildNodes(final String parentTreeId)
    {
        List<TreeMenuDto> directChildPathList = new ArrayList<TreeMenuDto>();

        DatabaseManager dbMmanager = DatabaseManager.getInstance();
        // TODO エージェントIDは0固定
        String dbName = dbMmanager.getDataBaseName(1);
        try
        {
            List<String> itemNameList =
                    JavelinMeasurementItemDao.selectItemNameListByParentItemName(dbName,
                                                                                 parentTreeId);

            int parentPathLength = parentTreeId.split("/").length;

            List<String> childNodeList = new ArrayList<String>();
            int itemNameListLength = itemNameList.size();
            for (int index = 0; index < itemNameListLength; index++)
            {
                String itemName = itemNameList.get(index);
                String[] itemNameSplits = itemName.split("/");

                String childNodeName = itemNameSplits[parentPathLength];
                boolean isContain = childNodeList.contains(childNodeName);
                if (!isContain)
                {
                    childNodeList.add(childNodeName);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(parentTreeId);
                    stringBuilder.append("/");
                    stringBuilder.append(childNodeName);

                    TreeMenuDto treeMenuDto = new TreeMenuDto();

                    if (itemNameSplits.length > parentPathLength + 1)
                    {
                        treeMenuDto.setType(GROUP_TYPE);
                        treeMenuDto.setIcon(GROUP_ICON);
                    }
                    else
                    {
                        treeMenuDto.setType(TARGET_TYPE);
                        treeMenuDto.setIcon(TARGET_ICON);
                    }
                    String treeId = stringBuilder.toString();

                    treeMenuDto.setId(treeId);
                    treeMenuDto.setTreeId(treeId);
                    treeMenuDto.setData(childNodeName);
                    treeMenuDto.setParentTreeId(parentTreeId);

                    directChildPathList.add(treeMenuDto);
                }
            }
        }
        catch (SQLException sqlEx)
        {
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
        }
        return directChildPathList;
    }

    /**
     * 一番階層が上のノードの一覧を取得する。
     * 
     * @return 一番階層が上のノードの一覧
     */
    public List<TreeMenuDto> getTopNodes()
    {
        DatabaseManager dbMmanager = DatabaseManager.getInstance();
        // TODO エージェントIDは0固定
        String dbName = dbMmanager.getDataBaseName(1);

        List<TreeMenuDto> topNodeList = new ArrayList<TreeMenuDto>();
        List<String> topNodeNameList = new ArrayList<String>();
        try
        {
            // 一番階層が上のノードの一覧を取得する
            List<String> itemNameList = JavelinMeasurementItemDao.selectAllItemName(dbName);

            int itemNameListLength = itemNameList.size();

            for (int index = 0; index < itemNameListLength; index++)
            {

                String itemName = itemNameList.get(index);
                String nodeName = itemName.split(TREE_SEPARATOR)[1];
                String topNodeName = TREE_SEPARATOR + itemName.split(TREE_SEPARATOR)[1];

                boolean isContaiin = topNodeNameList.contains(topNodeName);
                if (!isContaiin)
                {
                    topNodeNameList.add(topNodeName);
                    TreeMenuDto treeMenuDto = new TreeMenuDto();

                    treeMenuDto.setType(GROUP_TYPE);
                    treeMenuDto.setIcon(GROUP_ICON);
                    String treeId = topNodeName;

                    treeMenuDto.setId(treeId);
                    treeMenuDto.setTreeId(treeId);
                    treeMenuDto.setData(nodeName);
                    treeMenuDto.setParentTreeId(TREE_SEPARATOR);
                    topNodeList.add(treeMenuDto);
                }
            }
        }
        catch (SQLException sqlEx)
        {
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
        }

        return topNodeList;
    }
}
