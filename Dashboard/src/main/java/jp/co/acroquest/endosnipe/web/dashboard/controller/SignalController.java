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
package jp.co.acroquest.endosnipe.web.dashboard.controller;

import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.web.dashboard.dto.SignalDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.entity.SignalInfo;
import jp.co.acroquest.endosnipe.web.dashboard.manager.ResourceSender;
import jp.co.acroquest.endosnipe.web.dashboard.service.SignalService;
import net.arnx.jsonic.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wgp.manager.WgpDataManager;

@Controller
@RequestMapping("/signal")
public class SignalController {
	/** Wgpのデータを管理するクラス。 */
	@Autowired
	protected WgpDataManager wgpDataManager;

	/** リソースを送信するクラスのオブジェクト。 */
	@Autowired
	protected ResourceSender resourceSender;

	/** シグナル定義のサービスクラスのオブジェクト。 */
	@Autowired
	protected SignalService signalService;

	/**
	 * 閾値判定の定義を新規に追加する。
	 * 
	 * @param data
	 *            閾値判定の定義のJSONデータ
	 */
	@RequestMapping(value = "/getAllDefinition", method = RequestMethod.POST)
	@ResponseBody
	public List<SignalDefinitionDto> getAllDefinition() {
		List<SignalDefinitionDto> signalDefinitionDtos = new ArrayList<SignalDefinitionDto>();

		signalDefinitionDtos = signalService.getAllSignal();

		return signalDefinitionDtos;
	}

	/**
	 * 閾値判定の定義を新規に追加する。
	 * 
	 * @param data
	 *            閾値判定の定義のJSONデータ
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public SignalDefinitionDto add(
			@RequestParam(value = "data") final String data) {
		SignalDefinitionDto signalDefinitionDto = JSON.decode(data,
				SignalDefinitionDto.class);

		SignalInfo signalInfo = this.signalService
				.convertSignalInfo(signalDefinitionDto);

		// DBに追加する
		int signalId = this.signalService.insertSignalInfo(signalInfo);
		signalDefinitionDto.setSignalId(signalId);

		return signalDefinitionDto;
	}

	/**
	 * 閾値判定の定義を編集する。
	 * 
	 * @param data
	 *            閾値判定の定義のJSONデータ
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public SignalDefinitionDto edit(
			@RequestParam(value = "data") final String data) {
		SignalDefinitionDto signalDefinitionDto = JSON.decode(data,
				SignalDefinitionDto.class);

		SignalInfo signalInfo = this.signalService
				.convertSignalInfo(signalDefinitionDto);

		// DBに登録されている定義を更新する
		this.signalService.updateSignalInfo(signalInfo);

		return signalDefinitionDto;
	}

	/**
	 * 閾値判定のシグナルを削除する。
	 * 
	 * @param treeId
	 *            ツリーのパス
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public SignalDefinitionDto delete(
			@RequestParam(value = "data") final String data) {
		SignalDefinitionDto signalDefinitionDto = JSON.decode(data,
				SignalDefinitionDto.class);

		SignalInfo signalInfo = this.signalService
				.convertSignalInfo(signalDefinitionDto);
		
		this.signalService.deleteSignalInfo(signalInfo);

		System.out.println("閾値判定のシグナルを削除しました。");

		return signalDefinitionDto;
	}
}
