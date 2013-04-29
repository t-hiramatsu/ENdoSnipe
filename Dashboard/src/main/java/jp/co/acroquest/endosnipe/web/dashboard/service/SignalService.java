package jp.co.acroquest.endosnipe.web.dashboard.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.data.dao.JavelinMeasurementItemDao;
import jp.co.acroquest.endosnipe.web.dashboard.constants.LogMessageCodes;
import jp.co.acroquest.endosnipe.web.dashboard.dao.SignalInfoDao;
import jp.co.acroquest.endosnipe.web.dashboard.dto.SignalDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.entity.SignalInfo;
import jp.co.acroquest.endosnipe.web.dashboard.manager.DatabaseManager;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * シグナル定義のサービスクラス。
 * 
 * @author miyasaka
 * 
 */
@Service
public class SignalService {
	/** ロガー。 */
	private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger
			.getLogger(MapService.class);

	@Autowired
	protected SignalInfoDao signalInfoDao;

	/**
	 * すべてのシグナルデータを返す。
	 * 
	 * @return シグナルデータ一覧
	 */
	public List<SignalDefinitionDto> getAllSignal() {
		List<SignalInfo> signalList = null;
		try {
			signalList = signalInfoDao.selectAll();
		} catch (PersistenceException pe) {
			Throwable cause = pe.getCause();
			if (cause instanceof SQLException) {
				SQLException sqlEx = (SQLException) cause;
				LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
				return new ArrayList<SignalDefinitionDto>();
			}
		}

		List<SignalDefinitionDto> definitionDtoList = new ArrayList<SignalDefinitionDto>();

		for (SignalInfo signalInfo : signalList) {
			SignalDefinitionDto signalDto = this.convertSignalDto(signalInfo);
			definitionDtoList.add(signalDto);
		}

		return definitionDtoList;
	}

	/**
	 * シグナル定義をDBに登録する。
	 * 
	 * @param signalInfo
	 *            シグナル定義
	 */
	@Transactional
	public int insertSignalInfo(SignalInfo signalInfo) {
		int signalId = 0;
		try {
			signalInfoDao.insert(signalInfo);
			signalId = signalInfoDao.selectSequenceNum(signalInfo);
		} catch (PersistenceException pe) {
			Throwable cause = pe.getCause();
			if (cause instanceof SQLException) {
				SQLException sqlEx = (SQLException) cause;
				LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
			}
		}

		return signalId;
	}

	/**
	 * シグナル定義を更新する。
	 * 
	 * @param シグナル定義
	 */
	public void updateSignalInfo(final SignalInfo signalInfo) {
		try {
			SignalInfo beforeSignalInfo = signalInfoDao
					.selectById(signalInfo.signalId);
			signalInfoDao.update(signalInfo);

			String beforeItemName = beforeSignalInfo.signalName;
			String afterItemName = signalInfo.signalName;

			this.updateMeasurementItemName(beforeItemName, afterItemName);
		} catch (PersistenceException pEx) {
			Throwable cause = pEx.getCause();
			if (cause instanceof SQLException) {
				SQLException sqlEx = (SQLException) cause;
				LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
			}
		} catch (SQLException sqlEx) {
			LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
		}
	}

	/**
	 * シグナル定義をDBから削除する。
	 * 
	 * @param signalInfo
	 *            シグナル定義
	 */
	public void deleteSignalInfo(final SignalInfo signalInfo) {
		try {
			signalInfoDao.delete(signalInfo);
		} catch (PersistenceException pe) {
			Throwable cause = pe.getCause();
			if (cause instanceof SQLException) {
				SQLException sqlEx = (SQLException) cause;
				LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
			}
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
	public SignalInfo convertSignalInfo(SignalDefinitionDto definitionDto) {
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
	private SignalDefinitionDto convertSignalDto(SignalInfo signalInfo) {

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
	 * javelin_measurement_itemテーブルのmeasurement_item_nameを更新する。
	 * 
	 * @param beforeItemName
	 *            更新前のmeasurement_item_name
	 * @param afterItemName
	 *            更新前のmeasurement_item_name
	 * @throws SQLException
	 *             SQL 実行時に例外が発生した場合
	 */
	private void updateMeasurementItemName(String beforeItemName,
			String afterItemName) throws SQLException {
		DatabaseManager dbMmanager = DatabaseManager.getInstance();
		String dbName = dbMmanager.getDataBaseName(1);

		JavelinMeasurementItemDao.updateMeasurementItemName(dbName,
				beforeItemName, afterItemName);
	}
}
