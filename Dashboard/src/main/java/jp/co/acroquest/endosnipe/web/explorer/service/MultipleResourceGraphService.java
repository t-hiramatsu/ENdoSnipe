package jp.co.acroquest.endosnipe.web.explorer.service;

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
import jp.co.acroquest.endosnipe.web.explorer.constants.LogMessageCodes;
import jp.co.acroquest.endosnipe.web.explorer.constants.MultipleResourceGraphConstants;
import jp.co.acroquest.endosnipe.web.explorer.constants.TreeMenuConstants;
import jp.co.acroquest.endosnipe.web.explorer.dao.MultipleResourceGraphInfoDao;
import jp.co.acroquest.endosnipe.web.explorer.dto.MultipleResourceGraphDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.TreeMenuDto;
import jp.co.acroquest.endosnipe.web.explorer.entity.MultipleResourceGraphInfo;
import jp.co.acroquest.endosnipe.web.explorer.manager.ConnectionClient;
import jp.co.acroquest.endosnipe.web.explorer.manager.DatabaseManager;
import jp.co.acroquest.endosnipe.web.explorer.manager.EventManager;
import jp.co.acroquest.endosnipe.web.explorer.manager.ResourceSender;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wgp.manager.WgpDataManager;

/**
 * 複数グラフ定義のサービスクラス。
 * 
 * @author pin
 * 
 */
@Service("multipleResourceGraphService")
public class MultipleResourceGraphService
{

    /** ロガー。 */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(DashboardService.class);

    /**
     * 複数グラフ情報Dao。
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
     * すべての複数グラフデータを返す。
     * 
     * @return 複数グラフデータ一覧
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
            definitionDtoList.add(multipleResourceGraphDto);
        }

        sendGetAllStateRequest(multipleResourceGraphList);
        return definitionDtoList;
    }

    /**
     * 複数グラフの全状態を取得するリクエストを生成する。
     * 
     * @param multipleResourceGraphList
     *            複数グラフ定義情報一覧
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
     * 複数グラフ情報を取得する電文を作成する。
     * 
     * @param multipleResourceGraphInfoList
     *            複数グラフ定義情報のリスト
     * @return 複数グラフ情報を取得する電文
     */
    private Telegram createAllStateTelegram(
            final List<MultipleResourceGraphInfo> multipleResourceGraphInfoList)
    {
        Telegram telegram = new Telegram();

        Header requestHeader = new Header();
        requestHeader.setByteTelegramKind(TelegramConstants.BYTE_TELEGRAM_KIND_MUL_RES_GRAPH_DEFINITION);
        requestHeader.setByteRequestKind(TelegramConstants.BYTE_REQUEST_KIND_REQUEST);

        int dtoCount = multipleResourceGraphInfoList.size();

        // 複数グラフ定義情報のID
        Body multipleResourceGraphIdBody = new Body();
        multipleResourceGraphIdBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        multipleResourceGraphIdBody.setStrItemName(TelegramConstants.ITEMNAME_MUL_RES_GRAPH_ID);
        multipleResourceGraphIdBody.setByteItemMode(ItemType.ITEMTYPE_LONG);
        multipleResourceGraphIdBody.setIntLoopCount(dtoCount);
        Long[] multipleResourceGraphIds = new Long[dtoCount];

        // 複数グラフ定義情報名
        Body multipleResourceGraphNameBody = new Body();
        multipleResourceGraphNameBody.setStrObjName(TelegramConstants.OBJECTNAME_RESOURCEALARM);
        multipleResourceGraphNameBody.setStrItemName(TelegramConstants.ITEMNAME_SIGNAL_ID);
        multipleResourceGraphNameBody.setByteItemMode(ItemType.ITEMTYPE_STRING);
        multipleResourceGraphNameBody.setIntLoopCount(dtoCount);
        String[] multipleResourceGraphNames = new String[dtoCount];

        for (int cnt = 0; cnt < dtoCount; cnt++)
        {
            MultipleResourceGraphInfo multipleResourceGraphInfo =
                    multipleResourceGraphInfoList.get(cnt);
            multipleResourceGraphNames[cnt] = multipleResourceGraphInfo.multipleResourceGraphName_;
            multipleResourceGraphIds[cnt] =
                    Long.valueOf(multipleResourceGraphInfo.multipleResourceGraphId_);
        }
        multipleResourceGraphIdBody.setObjItemValueArr(multipleResourceGraphIds);
        multipleResourceGraphNameBody.setObjItemValueArr(multipleResourceGraphNames);

        Body[] requestBodys = { multipleResourceGraphIdBody, multipleResourceGraphNameBody };

        telegram.setObjHeader(requestHeader);
        telegram.setObjBody(requestBodys);

        return telegram;
    }

    /**
     * 複数グラフ定義をDBに登録する。
     * 
     * @param multipleResourceGraphInfo
     *            複数グラフ定義
     * @return 複数グラフ定義のDTOオブジェクト
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

        // 各クライアントに複数グラフ定義の追加を送信する。
        sendmultipleResourceGraphDefinition(multipleResourceGraphDefinitionDto, "add");

        return multipleResourceGraphDefinitionDto;
    }

    /**
     * 複数グラフ定義をDBから取得する。
     * @param multipleResourceGraphName 複数グラフ名
     * @return 複数グラフ定義のDTOオブジェクト
     */
    public MultipleResourceGraphDefinitionDto getmultipleResourceGraphInfo(
            final String multipleResourceGraphName)
    {
        MultipleResourceGraphInfo multipleResourceGraphInfo =
                multipleResourceGraphDao.selectByName(multipleResourceGraphName);

        //指定した名前の複数グラフが存在しない場合はnullを返す
        if (multipleResourceGraphInfo == null)
        {
            return null;
        }

        MultipleResourceGraphDefinitionDto defitionDto =
                this.convertmultipleResourceGraphDto(multipleResourceGraphInfo);
        return defitionDto;
    }

    /**
     * 複数グラフ定義を更新する。
     * 
     * @param multipleResourceGraphInfo
     *            複数グラフ定義
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
        // 各クライアントに複数グラフ定義の変更を送信する。
        sendmultipleResourceGraphDefinition(multipleResourceGraphDefinitionDto, "update");

        return multipleResourceGraphDefinitionDto;
    }

    /**
     * 複数グラフ名に該当する複数グラフ定義をDBから削除する。
     *
     * @param multipleResourceGraphName
     *            複数グラフ名
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

        // 各クライアントに複数グラフ定義の削除を送信する。
        sendmultipleResourceGraphDefinition(multipleResourceGraphDefinitionDto, "delete");
    }

    /**
     * MultipleResourceGraphDefinitionDtoオブジェクトをMultipleResourceGraphInfoオブジェクトに変換する。
     * 
     * @param definitionDto
     *            MultipleResourceGraphDefinitionDtoオブジェクト
     * 
     * @return MultipleResourceGraphInfo data of MultipleResourceGraph
     */
    public MultipleResourceGraphInfo convertMultipleResourceGraphInfo(
            final MultipleResourceGraphDefinitionDto definitionDto)
    {
        MultipleResourceGraphInfo multipleResourceGraphInfo = new MultipleResourceGraphInfo();

        multipleResourceGraphInfo.multipleResourceGraphId_ =
                definitionDto.getMultipleResourceGraphId();
        multipleResourceGraphInfo.multipleResourceGraphName_ =
                definitionDto.getMultipleResourceGraphName();
        multipleResourceGraphInfo.measurementItemIdList_ = definitionDto.getMeasurementItemIdList();
        multipleResourceGraphInfo.measurementItemPattern_ =
                definitionDto.getMeasurementItemPattern();
        return multipleResourceGraphInfo;
    }

    /**
     * multipleResourceGraphInfoオブジェクトをMultipleResourceGraphDefinitionDtoオブジェクトに変換する。
     * 
     * @param multipleResourceGraphInfo
     *            multipleResourceGraphInfoオブジェクト
     * @return MultipleResourceGraphDefinitionDtoオブジェクト
     */
    private MultipleResourceGraphDefinitionDto convertmultipleResourceGraphDto(
            final MultipleResourceGraphInfo multipleResourceGraphInfo)
    {

        MultipleResourceGraphDefinitionDto definitionDto = new MultipleResourceGraphDefinitionDto();

        definitionDto.setMultipleResourceGraphId(multipleResourceGraphInfo.multipleResourceGraphId_);
        definitionDto.setMultipleResourceGraphName(multipleResourceGraphInfo.multipleResourceGraphName_);
        definitionDto.setMeasurementItemIdList(multipleResourceGraphInfo.measurementItemIdList_);
        definitionDto.setMeasurementItemPattern(multipleResourceGraphInfo.measurementItemPattern_);
        return definitionDto;
    }

    /**
     * javelin_measurement_itemテーブルのMEASUREMENT_ITEM_NAMEを更新する。
     * @param regExp regExp
     * @throws SQLException
     *             SQL 実行時に例外が発生した場合
     *             
     * @return javelin_measurement_itemのリスト
     */
    public List<JavelinMeasurementItem> getMeasurementItemName(final String regExp)
        throws SQLException
    {
        DatabaseManager dbMmanager = DatabaseManager.getInstance();
        String dbName = dbMmanager.getDataBaseName(1);
        List<JavelinMeasurementItem> measurementItemList = new ArrayList<JavelinMeasurementItem>();
        if (regExp != null && !(regExp.trim().equals("")))
        {
            measurementItemList = JavelinMeasurementItemDao.selectAllByPattern(dbName, regExp);
        }
        else
        {
            measurementItemList = JavelinMeasurementItemDao.selectAll(dbName);
        }
        return measurementItemList;
    }

    /**
     * 同一の複数グラフ名を持つ複数グラフ定義情報がDBに存在するかどうか。
     * @param multipleResourceGraphName 複数グラフ名
     * @return 同一複数グラフ名を保持する複数グラフ定義情報が存在する場合にtrueを返し、存在しない場合にfalseを返す。
     */
    public boolean hasSamemultipleResourceGraphName(final String multipleResourceGraphName)
    {
        MultipleResourceGraphInfo multipleResourceGraphInfo =
                this.multipleResourceGraphDao.selectByName(multipleResourceGraphName);
        if (multipleResourceGraphInfo == null)
        {
            // 同一複数グラフ名を持つ複数グラフ定義情報が存在しない場合
            return false;
        }
        return true;
    }

    /**
     * 同一の複数グラフ名を持つ複数グラフ定義情報がDBに存在するかどうか。<br />
     * ただし、同一の複数グラフIDを保持する場合は、更新対象がDBに定義された複数グラフ定義情報と同一とみなし、falseを返す。
     * @param multipleResourceGraphId 複数グラフID
     * @param multipleResourceGraphName 複数グラフ名
     * @return 同一複数グラフ名を保持する複数グラフ定義情報が存在する場合にtrueを返し、存在しない場合にfalseを返す。
     */
    public boolean hasSameMultipleResourceGraphName(final long multipleResourceGraphId,
            final String multipleResourceGraphName)
    {
        MultipleResourceGraphInfo multipleResourceGraphInfo =
                this.multipleResourceGraphDao.selectByName(multipleResourceGraphName);
        if (multipleResourceGraphInfo == null)
        {
            // 同一複数グラフ名を持つ複数グラフ定義情報が存在しない場合
            return false;
        }
        else if (multipleResourceGraphInfo.multipleResourceGraphId_ == multipleResourceGraphId)
        {
            // 複数グラフ名が一致する複数グラフ定義情報が更新対象自身の場合
            return false;
        }
        return true;
    }

    /**
     * 複数グラフ定義DTOをツリーメニュー用のDTOに変換する。
     * @param multipleResourceGraphDefinitionDto 複数グラフ定義情報
     * @return ツリーメニュー用のDTO
     */
    public TreeMenuDto convertMultipleResourceGraphTreeMenu(
            final MultipleResourceGraphDefinitionDto multipleResourceGraphDefinitionDto)
    {

        TreeMenuDto treeMenu = new TreeMenuDto();

        String multipleResourceGraphName =
                multipleResourceGraphDefinitionDto.getMultipleResourceGraphName();

        // 複数グラフ名から親階層のツリーID名を取得する。
        // ※一番右のスラッシュ区切りまでを親階層とする。
        int terminateParentTreeIndex = multipleResourceGraphName.lastIndexOf("/");
        String parentTreeId = multipleResourceGraphName.substring(0, terminateParentTreeIndex);

        // 複数グラフ表示名を取得する。
        // ※一番右のスラッシュ区切り以降を表示名とする。
        String multipleResourceGraphDisplayName =
                multipleResourceGraphName.substring(terminateParentTreeIndex + 1);

        treeMenu.setId(multipleResourceGraphName);
        treeMenu.setTreeId(multipleResourceGraphName);
        treeMenu.setParentTreeId(parentTreeId);
        treeMenu.setData(multipleResourceGraphDisplayName);
        treeMenu.setType(TreeMenuConstants.TREE_MENU_TYPE_MUL_RESOURCE_GRAPH);
        treeMenu.setIcon(MultipleResourceGraphConstants.MUL_RES_GRAPH_ICON);
        return treeMenu;
    }

    /**
     * 複数グラフ定義情報の追加・更新・削除を各クライアントに送信する。
     * @param multipleResourceGraphDefinitionDto 複数グラフ定義情報
     * @param type 送信タイプ(add:追加 update:変更 delete:削除)
     */
    private void sendmultipleResourceGraphDefinition(
            final MultipleResourceGraphDefinitionDto multipleResourceGraphDefinitionDto,
            final String type)
    {
        // 各クライアントに複数グラフ定義の追加を通知する。
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
