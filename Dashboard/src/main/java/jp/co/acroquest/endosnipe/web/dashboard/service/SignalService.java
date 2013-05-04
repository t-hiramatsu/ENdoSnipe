package jp.co.acroquest.endosnipe.web.dashboard.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.data.dao.JavelinMeasurementItemDao;
import jp.co.acroquest.endosnipe.web.dashboard.constants.LogMessageCodes;
import jp.co.acroquest.endosnipe.web.dashboard.dao.SignalInfoDao;
import jp.co.acroquest.endosnipe.web.dashboard.dto.SignalDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.entity.SignalInfo;
import jp.co.acroquest.endosnipe.web.dashboard.manager.DatabaseManager;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * シグナル定義のサービスクラス。
 *
 * @author miyasaka
 *
 */
@Service
public class SignalService
{
    /** ロガー。 */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(MapService.class);

    @Autowired
    protected SignalInfoDao signalInfoDao;

    /**
     * すべてのシグナルデータを返す。
     *
     * @return シグナルデータ一覧
     */
    public List<SignalDefinitionDto> getAllSignal()
    {
        List<SignalInfo> signalList = null;
        try
        {
            signalList = signalInfoDao.selectAll();
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

            return new ArrayList<SignalDefinitionDto>();
        }

        // TODO signalMapがモックなので、DataCollectorに問い合わせて取得するように変更する
        Map<Integer, Integer> signalMap = new HashMap<Integer, Integer>();
        for (SignalInfo signalInfo : signalList)
        {
            SignalDefinitionDto signalDto = this.convertSignalDto(signalInfo);
            int signalId = signalDto.getSignalId();

            signalMap.put(signalId, 1 + (int)(Math.random() * signalInfo.level));
        }

        List<SignalDefinitionDto> definitionDtoList = new ArrayList<SignalDefinitionDto>();

        for (SignalInfo signalInfo : signalList)
        {
            SignalDefinitionDto signalDto = this.convertSignalDto(signalInfo);
            int signalId = signalDto.getSignalId();
            Integer signalValue = signalMap.get(signalId);
            signalDto.setSignalValue(signalValue);
            definitionDtoList.add(signalDto);
        }

        return definitionDtoList;
    }

    /**
     * シグナル定義をDBに登録する。
     *
     * @param signalInfo
     *            シグナル定義
     * @return シグナル定義のDTOオブジェクト
     */
    @Transactional
    public SignalDefinitionDto insertSignalInfo(final SignalInfo signalInfo)
    {
        int signalId = 0;
        try
        {
            signalInfoDao.insert(signalInfo);
            signalId = signalInfoDao.selectSequenceNum(signalInfo);
        }
        catch (DuplicateKeyException dkEx)
        {
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, dkEx, dkEx.getMessage());
            return new SignalDefinitionDto();
        }

        SignalDefinitionDto signalDefinitionDto = this.convertSignalDto(signalInfo);
        signalDefinitionDto.setSignalId(signalId);

        // TODO signalValueがモックなので、DataCollectorに問い合わせて取得するように変更する
        signalDefinitionDto.setSignalValue(1 + (int)(Math.random() * signalInfo.level));

        return signalDefinitionDto;
    }

    /**
     * シグナル定義を更新する。
     *
     * @param signalInfo シグナル定義
     */
    public SignalDefinitionDto updateSignalInfo(final SignalInfo signalInfo)
    {
        try
        {
            SignalInfo beforeSignalInfo = signalInfoDao.selectById(signalInfo.signalId);
            if (beforeSignalInfo == null || signalInfo == null)
            {
                return new SignalDefinitionDto();
            }

            signalInfoDao.update(signalInfo);

            String beforeItemName = beforeSignalInfo.signalName;
            String afterItemName = signalInfo.signalName;

            this.updateMeasurementItemName(beforeItemName, afterItemName);
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

            return new SignalDefinitionDto();
        }
        catch (SQLException sqlEx)
        {
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            return new SignalDefinitionDto();
        }

        SignalDefinitionDto signalDefinitionDto = this.convertSignalDto(signalInfo);

        // TODO signalValueがモックなので、DataCollectorに問い合わせて取得するように変更する
        signalDefinitionDto.setSignalValue(1 + (int)(Math.random() * signalInfo.level));

        return signalDefinitionDto;
    }

    /**
     * シグナル定義をDBから削除する。
     *
     * @param signalInfo
     *            シグナル定義
     */
    public void deleteSignalInfo(final SignalInfo signalInfo)
    {
        try
        {
            signalInfoDao.delete(signalInfo);
            this.deleteMeasurementItem(signalInfo.signalName);
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
    }

    /**
     * SignalDefinitionDtoオブジェクトをSignalInfoオブジェクトに変換する。
     *
     * @param definitionDto
     *            SignalDefinitionDtoオブジェクト
     *
     * @return SignalInfoオブジェクト
     */
    public SignalInfo convertSignalInfo(final SignalDefinitionDto definitionDto)
    {
        SignalInfo signalInfo = new SignalInfo();

        signalInfo.signalId = definitionDto.getSignalId();
        signalInfo.signalName = definitionDto.getSignalName();
        signalInfo.matchingPattern = definitionDto.getMatchingPattern();
        signalInfo.level = definitionDto.getLevel();
        signalInfo.patternValue = definitionDto.getPatternValue();
        signalInfo.escalationPeriod = definitionDto.getEscalationPeriod();

        return signalInfo;
    }

    /**
     * SignalInfoオブジェクトをSignalDefinitionDtoオブジェクトに変換する。
     *
     * @param signalInfo
     *            SignalInfoオブジェクト
     * @return SignalDefinitionDtoオブジェクト
     */
    private SignalDefinitionDto convertSignalDto(final SignalInfo signalInfo)
    {

        SignalDefinitionDto definitionDto = new SignalDefinitionDto();

        definitionDto.setSignalId(signalInfo.signalId);
        definitionDto.setSignalName(signalInfo.signalName);
        definitionDto.setMatchingPattern(signalInfo.matchingPattern);
        definitionDto.setLevel(signalInfo.level);
        definitionDto.setPatternValue(signalInfo.patternValue);
        definitionDto.setEscalationPeriod(signalInfo.escalationPeriod);

        return definitionDto;
    }

    /**
     * javelin_measurement_itemテーブルのMEASUREMENT_ITEM_NAMEを更新する。
     *
     * @param beforeItemName
     *            更新前のMEASUREMENT_ITEM_NAME
     * @param afterItemName
     *            更新前のMEASUREMENT_ITEM_NAME
     * @throws SQLException
     *             SQL 実行時に例外が発生した場合
     */
    private void updateMeasurementItemName(final String beforeItemName, final String afterItemName)
        throws SQLException
    {
        DatabaseManager dbMmanager = DatabaseManager.getInstance();
        String dbName = dbMmanager.getDataBaseName(1);

        JavelinMeasurementItemDao.updateMeasurementItemName(dbName, beforeItemName, afterItemName);
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

}
