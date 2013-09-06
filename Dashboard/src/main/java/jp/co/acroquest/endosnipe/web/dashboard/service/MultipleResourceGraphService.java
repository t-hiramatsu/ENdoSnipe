package jp.co.acroquest.endosnipe.web.dashboard.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.entity.ItemType;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.communicator.CommunicationClient;
import jp.co.acroquest.endosnipe.communicator.entity.Body;
import jp.co.acroquest.endosnipe.communicator.entity.Header;
import jp.co.acroquest.endosnipe.communicator.entity.Telegram;
import jp.co.acroquest.endosnipe.communicator.entity.TelegramConstants;
import jp.co.acroquest.endosnipe.data.dao.JavelinMeasurementItemDao;
import jp.co.acroquest.endosnipe.data.entity.JavelinMeasurementItem;
import jp.co.acroquest.endosnipe.web.dashboard.constants.LogMessageCodes;
import jp.co.acroquest.endosnipe.web.dashboard.constants.TreeMenuConstants;
import jp.co.acroquest.endosnipe.web.dashboard.dao.MultipleResourceGraphInfoDao;
import jp.co.acroquest.endosnipe.web.dashboard.dto.MultipleResourceGraphDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.dto.TreeMenuDto;
import jp.co.acroquest.endosnipe.web.dashboard.entity.MultipleResourceGraphInfo;
import jp.co.acroquest.endosnipe.web.dashboard.manager.ConnectionClient;
import jp.co.acroquest.endosnipe.web.dashboard.manager.DatabaseManager;
import jp.co.acroquest.endosnipe.web.dashboard.manager.EventManager;
import jp.co.acroquest.endosnipe.web.dashboard.manager.ResourceSender;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wgp.manager.WgpDataManager;

/**
 * シグナル定義のサービスクラス。
 * 
 * @author pin
 * 
 */
@Service
public class MultipleResourceGraphService
{

    /** ロガー。 */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(MapService.class);

    /**
     * シグナル情報Dao
     */
    @Autowired
    protected MultipleResourceGraphInfoDao multipleResourceGraphDao;

    /**
     * コンストラクタ
     */
    public MultipleResourceGraphService()
    {

    }

    /**
     * すべてのシグナルデータを返す。
     * 
     * @return シグナルデータ一覧
     */
    public List<MultipleResourceGraphDefinitionDto> getAllMultipleResourceGraph()
    {
        List<MultipleResourceGraphInfo> multipleResourceGraphList = null;
        try
        {
            multipleResourceGraphList = multipleResourceGraphDao.selectAll();
        }
        catch (PersistenceException pEx)
        {
            Throwable cause = pEx.getCause();
            if (cause instanceof SQLException)
            {
                SQLException sqlEx = (SQLException)cause;
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            }
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, pEx, pEx.getMessage());

            return new ArrayList<MultipleResourceGraphDefinitionDto>();
        }

        List<MultipleResourceGraphDefinitionDto> definitionDtoList =
                new ArrayList<MultipleResourceGraphDefinitionDto>();

        for (MultipleResourceGraphInfo multipleResourceGraphInfo : multipleResourceGraphList)
        {
            MultipleResourceGraphDefinitionDto multipleResourceGraphDto =
                    this.convertmultipleResourceGraphDto(multipleResourceGraphInfo);
            // 初期状態にはデフォルト値を設定とする。
            // signalDto.setSignalValue(DEFAULT_SIGNAL_STATE);
            definitionDtoList.add(multipleResourceGraphDto);
        }

        sendGetAllStateRequest(multipleResourceGraphList);
        return definitionDtoList;
    }

    /**
     * シグナルの全状態を取得するリクエストを生成する。
     * 
     * @param multipleResourceGraphList
     *            シグナル定義情報一覧
     */
    private void sendGetAllStateRequest(
            final List<MultipleResourceGraphInfo> multipleResourceGraphList)
    {
        ConnectionClient connectionClient = ConnectionClient.getInstance();
        List<CommunicationClient> clientList = connectionClient.getClientList();
        for (CommunicationClient communicationClient : clientList)
        {
            Telegram telegram = createAllStateTelegram(multipleResourceGraphList);
            communicationClient.sendTelegram(telegram);
        }

    }

    /**
     * 全閾値情報を取得する電文を作成する。
     * 
     * @param multipleResourceGraphInfoList
     *            シグナル定義情報のリスト
     * @return 全閾値情報を取得する電文
     */
    private Telegram createAllStateTelegram(
            final List<MultipleResourceGraphInfo> multipleResourceGraphInfoList)
    {
        Telegram telegram = new Telegram();

        Header requestHeader = new Header();
        requestHeader.setByteTelegramKind(TelegramConstants.BYTE_TELEGRAM_KIND_MUL_RES_GRAPH_DEFINITION);
        //  requestHeader.setByteTelegramKind(TelegramConstants.BYTE_TELEGRAM_KIND_ADD_MUL_RES_GRAPH_DEFINITION);
        requestHeader.setByteRequestKind(TelegramConstants.BYTE_REQUEST_KIND_REQUEST);

        int dtoCount = multipleResourceGraphInfoList.size();

        // 閾値判定定義情報のID
        Body multipleResourceGraphIdBody = new Body();
        multipleResourceGraphIdBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        multipleResourceGraphIdBody.setStrItemName(TelegramConstants.ITEMNAME_MUL_RES_GRAPH_ID);
        multipleResourceGraphIdBody.setByteItemMode(ItemType.ITEMTYPE_LONG);
        multipleResourceGraphIdBody.setIntLoopCount(dtoCount);
        Long[] multipleResourceGraphIds = new Long[dtoCount];

        // 閾値判定定義情報名
        Body multipleResourceGraphNameBody = new Body();
        multipleResourceGraphNameBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        multipleResourceGraphNameBody.setStrItemName(TelegramConstants.ITEMNAME_ALARM_ID);
        multipleResourceGraphNameBody.setByteItemMode(ItemType.ITEMTYPE_STRING);
        multipleResourceGraphNameBody.setIntLoopCount(dtoCount);
        String[] multipleResourceGraphNames = new String[dtoCount];

        for (int cnt = 0; cnt < dtoCount; cnt++)
        {
            MultipleResourceGraphInfo multipleResourceGraphInfo =
                    multipleResourceGraphInfoList.get(cnt);
            multipleResourceGraphNames[cnt] = multipleResourceGraphInfo.multipleResourceGraphName;
            multipleResourceGraphIds[cnt] =
                    Long.valueOf(multipleResourceGraphInfo.multipleResourceGraphId);
        }
        multipleResourceGraphIdBody.setObjItemValueArr(multipleResourceGraphIds);
        multipleResourceGraphNameBody.setObjItemValueArr(multipleResourceGraphNames);

        Body[] requestBodys = { multipleResourceGraphIdBody, multipleResourceGraphNameBody };

        telegram.setObjHeader(requestHeader);
        telegram.setObjBody(requestBodys);

        return telegram;
    }

    /**
     * シグナル定義をDBに登録する。
     * 
     * @param multipleResourceGraphInfo
     *            シグナル定義
     * @return シグナル定義のDTOオブジェクト
     */
    @Transactional
    public MultipleResourceGraphDefinitionDto insertMultipleResourceGraphInfo(
            final MultipleResourceGraphInfo multipleResourceGraphInfo)
    {
        int multipleResourceGraphId = 0;
        try
        {
            multipleResourceGraphDao.insert(multipleResourceGraphInfo);
            multipleResourceGraphId =
                    multipleResourceGraphDao.selectSequenceNum(multipleResourceGraphInfo);
        }
        catch (DuplicateKeyException dkEx)
        {
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, dkEx, dkEx.getMessage());
            return new MultipleResourceGraphDefinitionDto();
        }

        MultipleResourceGraphDefinitionDto multipleResourceGraphDefinitionDto =
                this.convertmultipleResourceGraphDto(multipleResourceGraphInfo);
        multipleResourceGraphDefinitionDto.setMultipleResourceGraphId(multipleResourceGraphId);

        // 各クライアントにシグナル定義の追加を送信する。
        sendmultipleResourceGraphDefinition(multipleResourceGraphDefinitionDto, "add");

        return multipleResourceGraphDefinitionDto;
    }

    /**
     * シグナル定義をDBから取得する。
     * @param multipleResourceGraphName シグナル名
     * @return シグナル定義のDTOオブジェクト
     */
    public MultipleResourceGraphDefinitionDto getmultipleResourceGraphInfo(
            final String multipleResourceGraphName)
    {
        MultipleResourceGraphInfo multipleResourceGraphInfo =
                multipleResourceGraphDao.selectByName(multipleResourceGraphName);

        MultipleResourceGraphDefinitionDto defitionDto =
                this.convertmultipleResourceGraphDto(multipleResourceGraphInfo);
        return defitionDto;
    }

    /**
     * シグナル定義を更新する。
     * 
     * @param multipleResourceGraphInfo
     *            シグナル定義
     * @return {@link MultipleResourceGraphDefinitionDto}オブジェクト
     */
    public MultipleResourceGraphDefinitionDto updatemultipleResourceGraphInfo(
            final MultipleResourceGraphInfo multipleResourceGraphInfo)
    {
        try
        {
            multipleResourceGraphDao.update(multipleResourceGraphInfo);
        }
        catch (PersistenceException pEx)
        {
            Throwable cause = pEx.getCause();
            if (cause instanceof SQLException)
            {
                SQLException sqlEx = (SQLException)cause;
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            }
            else
            {
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, pEx, pEx.getMessage());
            }

            return new MultipleResourceGraphDefinitionDto();
        }

        MultipleResourceGraphDefinitionDto multipleResourceGraphDefinitionDto =
                this.convertmultipleResourceGraphDto(multipleResourceGraphInfo);
        // 各クライアントにシグナル定義の変更を送信する。
        sendmultipleResourceGraphDefinition(multipleResourceGraphDefinitionDto, "update");

        return multipleResourceGraphDefinitionDto;
    }

    /**
     * シグナル名に該当するシグナル定義をDBから削除する。
     *
     * @param multipleResourceGraphName
     *            シグナル名
     */
    public void deletemultipleResourceGraphInfo(final String multipleResourceGraphName)
    {
        MultipleResourceGraphDefinitionDto multipleResourceGraphDefinitionDto = null;
        try
        {
            // 削除前に定義情報を取得しておく。
            multipleResourceGraphDefinitionDto =
                    getmultipleResourceGraphInfo(multipleResourceGraphName);

            multipleResourceGraphDao.delete(multipleResourceGraphName);
            this.deleteMeasurementItem(multipleResourceGraphName);
        }
        catch (PersistenceException pEx)
        {
            Throwable cause = pEx.getCause();
            if (cause instanceof SQLException)
            {
                SQLException sqlEx = (SQLException)cause;
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            }
            else
            {
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, pEx, pEx.getMessage());
            }
        }
        catch (SQLException sqlEx)
        {
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
        }

        // 各クライアントにシグナル定義の削除を送信する。
        sendmultipleResourceGraphDefinition(multipleResourceGraphDefinitionDto, "delete");
    }

    /**
     * SignalDefinitionDtoオブジェクトをSignalInfoオブジェクトに変換する。
     * 
     * @param definitionDto
     *            SignalDefinitionDtoオブジェクト
     * 
     * @return SignalInfoオブジェクト
     */
    public MultipleResourceGraphInfo convertMultipleResourceGraphInfo(
            final MultipleResourceGraphDefinitionDto definitionDto)
    {
        MultipleResourceGraphInfo multipleResourceGraphInfo = new MultipleResourceGraphInfo();

        multipleResourceGraphInfo.multipleResourceGraphId =
                definitionDto.getMultipleResourceGraphId();
        multipleResourceGraphInfo.multipleResourceGraphName =
                definitionDto.getMultipleResourceGraphName();
        multipleResourceGraphInfo.measurementItemIdList = definitionDto.getMeasurementItemIdList();

        return multipleResourceGraphInfo;
    }

    /**
     * SignalInfoオブジェクトをSignalDefinitionDtoオブジェクトに変換する。
     * 
     * @param multipleResourceGraphInfo
     *            SignalInfoオブジェクト
     * @return SignalDefinitionDtoオブジェクト
     */
    private MultipleResourceGraphDefinitionDto convertmultipleResourceGraphDto(
            final MultipleResourceGraphInfo multipleResourceGraphInfo)
    {

        MultipleResourceGraphDefinitionDto definitionDto = new MultipleResourceGraphDefinitionDto();

        definitionDto.setMultipleResourceGraphId(multipleResourceGraphInfo.multipleResourceGraphId);
        definitionDto.setMultipleResourceGraphName(multipleResourceGraphInfo.multipleResourceGraphName);
        definitionDto.setMeasurementItemIdList(multipleResourceGraphInfo.measurementItemIdList);

        return definitionDto;
    }

    /**
     * javelin_measurement_itemテーブルのMEASUREMENT_ITEM_NAMEを更新する。
     * 
     * @throws SQLException
     *             SQL 実行時に例外が発生した場合
     *             
     * @return javelin_measurement_itemのリスト
     */
    public List<JavelinMeasurementItem> getMeasurementItemName()
        throws SQLException
    {
        DatabaseManager dbMmanager = DatabaseManager.getInstance();
        String dbName = dbMmanager.getDataBaseName(1);

        List<JavelinMeasurementItem> measurementItemList =
                JavelinMeasurementItemDao.selectAll(dbName);
        return measurementItemList;
    }

    /**
     * javelin_measurement_itemテーブルの指定したMEASUREMENT_ITEM_NAMEのレコードを削除する。
     * 
     * @param itemName
     *            削除するレコードの MEASUREMENT_ITEM_NAME
     * @throws SQLException
     *             SQL 実行時に例外が発生した場合
     */
    private void deleteMeasurementItem(final String itemName)
        throws SQLException
    {
        DatabaseManager dbMmanager = DatabaseManager.getInstance();
        String dbName = dbMmanager.getDataBaseName(1);

        JavelinMeasurementItemDao.deleteByMeasurementItemId(dbName, itemName);
    }

    /**
     * 同一のシグナル名を持つ閾値判定定義情報がDBに存在するかどうか。
     * @param multipleResourceGraphName シグナル名
     * @return 同一シグナル名を保持する閾値判定定義情報が存在する場合にtrueを返し、存在しない場合にfalseを返す。
     */
    public boolean hasSamemultipleResourceGraphName(final String multipleResourceGraphName)
    {
        MultipleResourceGraphInfo multipleResourceGraphInfo =
                this.multipleResourceGraphDao.selectByName(multipleResourceGraphName);
        if (multipleResourceGraphInfo == null)
        {
            // 同一シグナル名を持つ閾値判定定義情報が存在しない場合
            return false;
        }
        return true;
    }

    /**
     * 同一のシグナル名を持つ閾値判定定義情報がDBに存在するかどうか。<br />
     * ただし、同一のシグナルIDを保持する場合は、更新対象がDBに定義された閾値判定定義情報と同一とみなし、falseを返す。
     * @param multipleResourceGraphId シグナルID
     * @param multipleResourceGraphName シグナル名
     * @return 同一シグナル名を保持する閾値判定定義情報が存在する場合にtrueを返し、存在しない場合にfalseを返す。
     */
    public boolean hasSameMultipleResourceGraphName(final long multipleResourceGraphId,
            final String multipleResourceGraphName)
    {
        MultipleResourceGraphInfo multipleResourceGraphInfo =
                this.multipleResourceGraphDao.selectByName(multipleResourceGraphName);
        if (multipleResourceGraphInfo == null)
        {
            // 同一シグナル名を持つ閾値判定定義情報が存在しない場合
            return false;
        }
        else if (multipleResourceGraphInfo.multipleResourceGraphId == multipleResourceGraphId)
        {
            // シグナル名が一致する閾値判定定義情報が更新対象自身の場合
            return false;
        }
        return true;
    }

    /**
     * シグナル定義DTOをツリーメニュー用のDTOに変換する。
     * @param multipleResourceGraphDefinitionDto シグナル定義情報
     * @return ツリーメニュー用のDTO
     */
    public TreeMenuDto convertMultipleResourceGraphTreeMenu(
            final MultipleResourceGraphDefinitionDto multipleResourceGraphDefinitionDto)
    {

        TreeMenuDto treeMenu = new TreeMenuDto();

        String multipleResourceGraphName =
                multipleResourceGraphDefinitionDto.getMultipleResourceGraphName();

        // シグナル名から親階層のツリーID名を取得する。
        // ※一番右のスラッシュ区切りまでを親階層とする。
        int terminateParentTreeIndex = multipleResourceGraphName.lastIndexOf("/");
        String parentTreeId = multipleResourceGraphName.substring(0, terminateParentTreeIndex);

        // シグナル表示名を取得する。
        // ※一番右のスラッシュ区切り以降を表示名とする。
        String multipleResourceGraphDisplayName =
                multipleResourceGraphName.substring(terminateParentTreeIndex + 1);

        treeMenu.setId(multipleResourceGraphName);
        treeMenu.setTreeId(multipleResourceGraphName);
        treeMenu.setParentTreeId(parentTreeId);
        treeMenu.setData(multipleResourceGraphDisplayName);
        treeMenu.setType(TreeMenuConstants.TREE_MENU_TYPE_MUL_RESOURCE_GRAPH);
        treeMenu.setIcon("mulResGraph");
        return treeMenu;
    }

    /**
     * シグナル定義情報の追加・更新・削除を各クライアントに送信する。
     * @param multipleResourceGraphDefinitionDto シグナル定義情報
     * @param type 送信タイプ(add:追加 update:変更 delete:削除)
     */
    private void sendmultipleResourceGraphDefinition(
            final MultipleResourceGraphDefinitionDto multipleResourceGraphDefinitionDto,
            final String type)
    {
        // 各クライアントにシグナル定義の追加を通知する。
        EventManager eventManager = EventManager.getInstance();
        WgpDataManager dataManager = eventManager.getWgpDataManager();
        ResourceSender resourceSender = eventManager.getResourceSender();
        if (dataManager != null && resourceSender != null)
        {
            List<TreeMenuDto> treeMenuDtoList = new ArrayList<TreeMenuDto>();
            treeMenuDtoList.add(this.convertMultipleResourceGraphTreeMenu(multipleResourceGraphDefinitionDto));
            resourceSender.send(treeMenuDtoList, type);
        }
    }

}
