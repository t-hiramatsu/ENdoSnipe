/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Acroquest Technology Co.,Ltd.
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
package jp.co.acroquest.endosnipe.collector.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jp.co.acroquest.endosnipe.data.dao.MulResourceGraphDefinitionDao;
import jp.co.acroquest.endosnipe.data.entity.MultipleResourceGraphInfo;

/**
 * {@link MulResourceGraphUtil} のための Util です。
 * @author pin
 *
 */
public class MulResourceGraphUtil
{
    /**
     * 
     */
    private MulResourceGraphUtil()
    {
        // nothing
    }

    /**
     * 
     * @param databaseName for put data
     * @param measurementNameList updated mearsurement list
     * @param operation : add or delete
     */
    public static void checkMatchPattern(final String databaseName,
        final List<String> measurementNameList, final String operation)
    {
        try
        {
            for (String measurementName : measurementNameList)
            {

                Map<Long, String> itemMap = new TreeMap<Long, String>();
                List<MultipleResourceGraphInfo> result =
                    MulResourceGraphDefinitionDao.selectMatchPattern(databaseName, measurementName);

                for (MultipleResourceGraphInfo matchResult : result)
                {
                    String updateList =
                        createUpdateMeasurementItemList(matchResult.measurementItemIdList_,
                                                        measurementName, operation);
                    itemMap.put(matchResult.multipleResourceGraphId_, updateList);
                    System.out.println(matchResult.multipleResourceGraphName_);
                }
                if (itemMap.size() > 0)
                {
                    MulResourceGraphDefinitionDao.updateItemListById(databaseName, itemMap);
                }

            }
        }
        catch (SQLException ex)
        {
            // TODO 自動生成された catch ブロック
            ex.printStackTrace();
        }
    }

    /**
     * update the measurementItemList depend on operation
     * @param itemList measurementItemList
     * @param updateItem measurementItem
     * @param operation : add or delete
     * @return String 
     */
    public static String createUpdateMeasurementItemList(final String itemList,
        final String updateItem, final String operation)
    {
        String updateList = null;

        if ("delete".equalsIgnoreCase(operation))
        {
            List<String> list = new ArrayList<String>(Arrays.asList(itemList.split(",")));
            if (list.indexOf(updateItem) == -1)
            {
                return itemList;
            }
            list.remove(list.indexOf(updateItem));
            if (list.size() > 0)
            {
                updateList = list.get(0);
            }
            for (int index = 1; index < list.size(); index++)
            {
                updateList += "," + list.get(index);
            }
        }
        else if ("add".equalsIgnoreCase(operation))
        {
            updateList = itemList + "," + updateItem;
        }
        return updateList;
    }

}
