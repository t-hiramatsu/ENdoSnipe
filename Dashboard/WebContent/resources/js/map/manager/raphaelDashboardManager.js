function raphaelDashboardManager(dashboardView) {

	/**
	 * 関連するダッシュボードビュー
	 */
	this.dashboardView_ = dashboardView;

	/**
	 * リサイズ後と前で符号が変わらない。
	 *
	 * @private
	 */
	this.CALCU_NOTHING = 0;
	/**
	 * リサイズ後と前の倍率の符号が反転する。
	 *
	 * @private
	 */
	this.NEGATIVE_POSITION = 1;
	/**
	 * リサイズ後の値が0。
	 *
	 * @private
	 */
	this.ZERO_POSITION = 2;
	/**
	 * getBBoxの中にオブジェクトが収まっている
	 */
	this.IS_INTERVAL = 1;
	/**
	 *
	 * 収まっていない
	 */
	this.IS_NOT_INTERVAL = -1;

	/**
	 * ダッシュボードに対する編集可否フラグ
	 */
	this.CAN_EDITE_FLAG = true;

	/**
	 * 選択したビューのリスト
	 */
	this.selectViewList_ = {};

	/**
	 * コピー対象のエレメントの一時保存領域のリスト
	 */
	this.tempCopyElementList_ = {};

	/**
	 * z index dashboard
	 */
	this.zIndexMap_ = [];
	this.zIndeMaxNum_ = [];
	for ( var count = 0; count < raphaelMapConstants.MAP_LAYER_NUM; count++) {
		this.zIndexMap_[count] = {};
		this.zIndeMaxNum_[count] = 0;
	}

};

raphaelDashboardManager.prototype.createElementEventFunction = function(
		dashboardElementStr, dashboardElementType, dashboardAreaViewItem, event) {

	var elementId = this.createNewElementId();
	var basicWidth = null;
	var basicHeight = null;
	if (raphaelMapConstants.CONNECT_LINE_TYPE_NAME != dashboardElementType
			&& raphaelMapConstants.CONNECT_CURVE_LINE_TYPE_NAME != dashboardElementType) {
		basicWidth = raphaelMapConstants.DEFAULT_SETTING[dashboardElementType].basicWidth;
		basicHeight = raphaelMapConstants.DEFAULT_SETTING[dashboardElementType].basicHeight;
	}

	var eventElementProperty = {};

	// 線またはコネクト線でない場合
	if (raphaelMapConstants.LINE_TYPE_NAME != dashboardElementType
			&& raphaelMapConstants.CONNECT_LINE_TYPE_NAME != dashboardElementType
			&& raphaelMapConstants.CURVE_LINE_TYPE_NAME != dashboardElementType
			&& raphaelMapConstants.CONNECT_CURVE_LINE_TYPE_NAME != dashboardElementType) {

		eventElementProperty.pointX = event.pageX
				- $("#" + dashboardAreaViewItem.divId).offset()["left"]
				- (basicWidth / 2);
		eventElementProperty.pointY = event.pageY
				- $("#" + dashboardAreaViewItem.divId).offset()["top"]
				- (basicHeight / 2);
		eventElementProperty.width = basicWidth;
		eventElementProperty.height = basicHeight;
		eventElementProperty.objectId = elementId;

	} else {
		eventElementProperty.pointX = event.pageX
				- $("#" + dashboardAreaViewItem.divId).offset()["left"];
		eventElementProperty.pointY = event.pageY
				- $("#" + dashboardAreaViewItem.divId).offset()["top"];
		eventElementProperty.width = basicWidth;
		eventElementProperty.height = basicHeight;
		eventElementProperty.objectId = elementId;
	}

	var dashboardElementObject = this.createElementFunction(dashboardElementStr,
			dashboardElementType, dashboardAreaViewItem, eventElementProperty, true);

	// move to object to front.
	this._addFront(dashboardElementObject);
	return dashboardElementObject;
};

/**
 * create element function object.
 *
 * @param dashboardElementStr
 * @param dashboardElementType
 * @param elementProperty
 * @param eventSettingFlag
 * @returns {dashboardElement} created element object.
 */
raphaelDashboardManager.prototype.createElementFunction = function(dashboardElementStr,
		dashboardElementType, elementProperty, eventSettingFlag) {
	if (raphaelMapConstants.POLYGON_TYPE_NAME == dashboardElementType) {
		return this.createElement(dashboardElementStr, elementProperty);

	} else if (raphaelMapConstants.LINE_TYPE_NAME == dashboardElementType) {
		return this.createElementLine(dashboardElementStr,elementProperty,
			eventSettingFlag);

	} else if (raphaelMapConstants.CONNECT_LINE_TYPE_NAME == dashboardElementType) {
		return this.createElementConnectLine(dashboardElementStr, elementProperty,
			eventSettingFlag);

	} else if (raphaelMapConstants.CONNECT_CURVE_LINE_TYPE_NAME == dashboardElementType) {
		return this.createElementConnectLine(dashboardElementStr, elementProperty,
			eventSettingFlag);

	} else if (raphaelMapConstants.TEXTAREA_TYPE_NAME == dashboardElementType) {
		return this.createElementText(dashboardElementStr, elementProperty, eventSettingFlag);

	} else if (raphaelMapConstants.IMAGE_TYPE_NAME == dashboardElementType) {
		return this.createElement(dashboardElementStr, elementProperty);

	} else if (raphaelMapConstants.CURVE_LINE_TYPE_NAME == dashboardElementType) {
		return this.createElementLine(dashboardElementStr, elementProperty,
			eventSettingFlag);
	}
};

raphaelDashboardManager.prototype.createElement = function(dashboardElementStr,
		elementProperty) {

	var paper = this.dashboardView_.paper;
	var dashboardElement = eval("new " + dashboardElementStr
			+ "(elementProperty, paper)");
//	if (elementProperty.objectId) {
//		this.elementList_[elementProperty.objectId] = dashboardElement;
//	}

//	dashboardElement.setEventFunction(dashboardAreaViewItem);
	return dashboardElement;
};

raphaelDashboardManager.prototype.createElementLine = function(dashboardElementStr,
		paper, elementProperty, eventSettingFlag) {
	var dashboardElement = eval("new " + dashboardElementStr
			+ "(elementProperty, paper)");
//	if (elementProperty.objectId) {
//		this.viewList_[elementProperty.objectId] = dashboardElement;
//	}

	// イベント設定不要の場合は処理終了
	if (!eventSettingFlag && eventSettingFlag == undefined) {
		return null;
	}

	var pointX = elementProperty.pointX;
	var pointY = elementProperty.pointY;
	var dragAreaFunction = null;
	dragAreaFunction = function(event) {
		var newPointX = event.pageX
				- $("#" + dashboardAreaViewItem.divId).offset()["left"];
		var newPointY = event.pageY
				- $("#" + dashboardAreaViewItem.divId).offset()["top"];

		var width = newPointX - pointX;
		var height = newPointY - pointY;

		dashboardElement.resize(pointX, pointY, width, height);
		var clickAreaFunction = null;
		clickAreaFunction = function(event) {
			dashboardAreaViewItem.entity.canvas.removeEventListener("mousemove",
					dragAreaFunction);
			dashboardAreaViewItem.entity.canvas.removeEventListener("click",
					clickAreaFunction);
			dashboardElement.setEventFunction(dashboardAreaViewItem);
		};

		// TODO イベントが不必要に何度もバインドされる。
		dashboardAreaViewItem.entity.canvas.addEventListener("click",
				clickAreaFunction);
		return dashboardElement;
	};

	dashboardAreaViewItem.entity.canvas.addEventListener("mousemove",
			dragAreaFunction);
	return null;
};

raphaelDashboardManager.prototype.createElementConnectLine = function(dashboardElementStr,
		dashboardAreaViewItem, elementProperty, eventSettingFlag) {
	var instance = this;

	// イベント設定不要の場合はすぐにつなげて処理終了
	if (!eventSettingFlag && eventSettingFlag == undefined) {
		var dashboardElement = eval("new " + dashboardElementStr
				+ "(elementProperty, dashboardAreaViewItem.entity, dashboardAreaViewItem)");

		if (elementProperty.objectId) {
			instance.elementList_[elementProperty.objectId] = dashboardElement;
		}

		// コネクト線は奥に配置する。
		$
				.each(
						this.viewList_,
						function(index, otherElement) {
							if (otherElement.objectType_ != raphaelMapConstants.CONNECT_LINE_TYPE_NAME
									&& otherElement.objectType_ != raphaelMapConstants.CONNECT_CURVE_LINE_TYPE_NAME) {
								otherElement.object
										.insertAfter(dashboardElement.object);
							}
						});
		return dashboardElement;
	}

	var pointX = elementProperty.pointX;
	var pointY = elementProperty.pointY;

	// クリックされた場所より接続元のエレメントを検索する。
	var connectFromElement = null;
	_.each(instance.elementList_, function(dashboardElement, index) {
		if (dashboardElement.object.isPointInside(pointX, pointY)) {
			connectFromElement = dashboardElement;
		}
	});

	// 接続元が正しく認識できた場合に接続先用のイベントをバインド
	if (connectFromElement) {
		var clickToElementFunction = null;
		clickToElementFunction = function(event) {

			pointX = event.pageX
					- $("#" + dashboardAreaViewItem.divId).offset()["left"];
			pointY = event.pageY
					- $("#" + dashboardAreaViewItem.divId).offset()["top"];

			// クリックされた場所より接続元のエレメントを検索する。
			var connectToElement = null;
			_.each(instance.elementList_, function(dashboardElement, index) {
				if (dashboardElement.object.isPointInside(pointX, pointY)) {
					connectToElement = dashboardElement;
				}
			});

			if (connectToElement
					&& connectFromElement.objectId != connectToElement.objectId) {
				var elementPropertyTemp = {
					connectFromElementId : connectFromElement.objectId,
					connectToElementId : connectToElement.objectId,
					objectId : elementProperty.objectId
				};
				elementPropertyTemp.objectId;

				var dashboardElement = eval("new "
						+ dashboardElementStr
						+ "(elementPropertyTemp, dashboardAreaViewItem.entity, dashboardAreaViewItem)");
				if (elementProperty.objectId) {
					instance.elementList_[elementProperty.objectId] = dashboardElement;
				}
				return dashboardElement;
			}

			dashboardAreaViewItem.entity.canvas.removeEventListener("click",
					clickToElementFunction);
		};

		dashboardAreaViewItem.entity.canvas.addEventListener("click",
				clickToElementFunction);
	}
	return null;
};

raphaelDashboardManager.prototype.createElementText = function(dashboardElementStr,
		elementProperty) {
	var paper = this.dashboardView_.paper;
	var dashboardElement = eval("new " + dashboardElementStr
			+ "(elementProperty, paper)");
//	if (elementProperty.objectId) {
//		this.elementList_[elementProperty.objectId] = dashboardElement;
//	}

//	dashboardElement.setEventFunction(dashboardAreaViewItem);
	return dashboardElement;
};

/**
 * execute move or resize or etc event. this method return Command of executed
 * event.
 *
 * @param type
 *            event type.
 * @param event
 *            event object.
 * @param options
 *            other option values.
 * @returns {Command} Command object executed event.
 */
raphaelDashboardManager.prototype.executeEvent = function(type, event, options) {
	if (type == raphaelMapConstants.EVENT_TYPE_MOVE_START) {
		return this.startMoveElement(event, options);
	} else if (type == raphaelMapConstants.EVENT_TYPE_MOVE) {
		return this.moveElement(event, options);
	} else if (type == raphaelMapConstants.EVENT_TYPE_MOVE_END) {
		return this.endMoveElement(event, options);
	} else if (type == raphaelMapConstants.EVENT_TYPE_SELECT) {
		return this.selectElementRange(event);
	} else if (type == raphaelMapConstants.EVENT_TYPE_RELEASE_SELECT) {
		return this.releaseSelectMapElement(event, options);
	} else if (type == raphaelMapConstants.EVENT_TYPE_RESIZE_START) {
		return this.startResizeElement(event, options);
	} else if (type == raphaelMapConstants.EVENT_TYPE_RESIZE) {
		return this.resizeElement(event, options);
	} else if (type == raphaelMapConstants.EVENT_TYPE_RESIZE_END) {
		return this.endResizeElement(event, options);
	} else if (type == raphaelMapConstants.EVENT_TYPE_COPY) {
		return this.copyDashboardElement();
	} else if (type == raphaelMapConstants.EVENT_TYPE_CUT) {
		return this.cutDashboardElement();
	} else if (type == raphaelMapConstants.EVENT_TYPE_DELETE) {
		return this.deleteDashboardElement();
	} else if (type == raphaelMapConstants.EVENT_TYPE_PASTE) {
		return this.pasteDashboardElement(event, options);
	}
	return null;
};

raphaelDashboardManager.prototype.selectElementRange = function(event) {
	// クリックされたオブジェクトを取得する。
	// グループ化されている場合には、グループ化されているオブジェクトを
	// 取得する。
	var mouseButton = event.button;
	var mouseCheck = this.checkMouseButtonDouble();
	// 既にマウスが押されている場合は処理をしない
	if (mouseCheck) {
		if (!this.isSameMouseButton(mouseButton)) {
			return;
		}
	}
	this.setOnMouseButton(mouseButton);

	var clickObjectId = $(event.target)[0]
		.getAttribute(raphaelMapConstants.OBJECT_ID_NAME);

	var clickView = this.getViewFromId(clickObjectId);
	if (!clickView) {
		// 選択解除して自身の選択を行う。
		_.each(this.selectViewList_, function(releaseSelectView, index) {
			releaseSelectView.hideFrame();
		});

		this.selectViewList_ = {};
		return;
	}

	// 以下の状態を取得し、フラグとして保持する。
	// 既に自分自身が選択されている。
	// 他のオブジェクトが選択されている。
	// Ctrlがクリック済み。
	// 右クリックで操作された。
	var isSelected = false;
	var isOtherObjectSelected = false;
	var isOnCtrlKey = false;
	// 選択オブジェクトが選択済みかどうか判定する。
	_.each(this.selectViewList_, function(selectView, index) {
		if (clickView.getObjectId() == selectView.getObjectId()) {
			isSelected = true;
		}
		isOtherObjectSelected = true;
	});

	if (event.ctrlKey) {
		isOnCtrlKey = true;
	}

	if (event.which == 3) {
		isOnRightMouseButton = true;
	}

	// それぞれの処理を振り分ける。
	if (isOnCtrlKey) {
		var instance = this;
		// コネクト線が選択リストにあれば除外する。
		_.each(this.selectViewList_, function(selectView, index) {
			if (selectView.objectType_ == raphaelMapConstants.CONNECT_LINE_TYPE_NAME) {
				delete instance.selectViewList_[selectView.getObjectId()];
			}
		});

		// 既に選択+他も選択
		if (isSelected) {
			clickView.hideFrame();
			delete this.selectViewList_[clickView.getObjectId()];
		} else {
			this.selectViewList_[clickView.getObjectId()] = clickObject;
			clickView.setFrame();
		}
	} else {
		// ノーマルクリック
		// 既に選択
		if (isSelected) {
			// 何もしない。
		} else if (isOtherObjectSelected) {
			// 選択解除して自身の選択を行う。
			_.each(this.selectViewList_, function(releaseSelectView, index) {
				releaseSelectView.hideFrame();
			});

			this.selectViewList_ = {};
			this.selectViewList_[clickView.getObjectId()] = clickView;
			clickView.setFrame();
		} else {
			this.selectViewList_[clickView.getObjectId()] = clickView;
			clickView.setFrame();
		}
	}
};

raphaelDashboardManager.prototype.releaseSelectMapElement = function(){
	_.each(this.selectViewList_, function(selectView, index){
		selectView.hideFrame();
	});
	this.selectViewList_ = {};
};

raphaelDashboardManager.prototype.startMoveElement = function(event, options) {
	// 初期位置を設定する。
	this.movedPointX = 0;
	this.movedPointY = 0;
};

raphaelDashboardManager.prototype.moveElement = function(event, options) {

	// ドラッグされた大きさを計測する。
	var moveX = 0;
	if(Math.abs(options.deltaX - this.movedPointX) > ENS.dashboard.moveSpan){
		moveX = options.deltaX - this.movedPointX;
		this.movedPointX = options.deltaX;
	}
	var moveY = 0;
	if(Math.abs(options.deltaY - this.movedPointY) > ENS.dashboard.moveSpan){
		moveY = options.deltaY - this.movedPointY;
		this.movedPointY = options.deltaY;
	}

	// 複数選択されている場合は、全てのオブジェクトを合成する
	// 子要素があればすべて動かす。
	var arguments = {
		moveX : moveX,
		moveY : moveY
	};
	var moveCommand = new raphaelDashboardMoveCommand(this.selectViewList_,
			arguments);
	moveCommand.execute();
};

raphaelDashboardManager.prototype.endMoveElement = function(event, options) {
	var argument = {
		moveX : this.movedPointX,
		moveY : this.movedPointY
	};
	var command = new raphaelDashboardMoveCommand(this.selectViewList_, argument);
	this.movedPointX = 0;
	this.movedPointY = 0;
	return command;
};
/**
 * <p>
 * [概 要] オブジェクトのリサイズ開始処理を行う。
 * </p>
 * <p>
 * [詳 細] 選択したオブジェクトに対して、リサイズ開始処理を行う。
 * </p>
 * <p>
 * [備 考] なし
 * </p>
 *
 *
 *
 * @param {object}
 *            target クリック対象のオブジェクト
 * @param {int}
 *            framePostion クリックしている円の位置
 * @param {double}
 *            x 移動前の初期位置(X軸)
 * @param {double}
 *            y 移動前の初期位置(Y軸)
 * @param {Event}
 *            event イベントオブジェクト
 * @public
 */
raphaelDashboardManager.prototype.startResizeElement = function(event, options) {
	// start
	// 初期位置を設定する。
	var target = event.target;
	this.movedPointX = 0;
	this.movedPointY = 0;
	var ellipsePosition = $(target).attr(
			raphaelMapConstants.SMALL_ELLIPSE_POSITION);
	this.ellipsePosition = parseInt(ellipsePosition);
	var objectId = $(target).attr(raphaelMapConstants.OBJECT_ID_NAME);
	var elementView = this.getViewFromId(objectId);

	var elementWidth = elementView.getWidth();
	var elementHeight = elementView.getHeight();

	if (this.ellipsePosition == raphaelMapConstants.LEFT_UPPER
			|| this.ellipsePosition == raphaelMapConstants.LEFT_UNDER) {
		this.sizeX = -1 * elementWidth;
		this.initSizeX = -1 * elementWidth;
	} else {
		this.sizeX = elementWidth;
		this.initSizeX = elementWidth;
	}

	if (this.ellipsePosition == raphaelMapConstants.LEFT_UPPER
			|| this.ellipsePosition == raphaelMapConstants.RIGHT_UPPER) {
		this.sizeY = -1 * elementHeight;
		this.initSizeY = -1 * elementHeight;
	} else {
		this.sizeY = elementHeight;
		this.initSizeY = elementHeight;
	}
};

raphaelDashboardManager.prototype.resizeElement = function(target, options) {

	// 移動量を算出し、円を移動する。
	var moveX = 0;
	var moveY = 0;

	if(Math.abs(options.deltaX - this.movedPointX) >= ENS.dashboard.resizeSpan){
		moveX = options.deltaX - this.movedPointX;
		this.movedPointX = options.deltaX;
		this.sizeX = this.sizeX + moveX;
	}

	if(Math.abs(options.deltaY - this.movedPointY) >= ENS.dashboard.resizeSpan){
		moveY = options.deltaY - this.movedPointY;
		this.movedPointY = options.deltaY;
		this.sizeY = this.sizeY + moveY;
	}

	var ratioX = (this.sizeX + moveX) / this.sizeX;
	var ratioY = (this.sizeY + moveY) / this.sizeY;
	if (ratioX == 0) {
		return;
	} else if (ratioY == 0) {
		return;
	}

	var instance = this;
	_.each(this.selectViewList_, function(selectView, index) {
		selectView.resize(ratioX, ratioY, instance.ellipsePosition);
	});
};

raphaelDashboardManager.prototype.endResizeElement = function(event, options) {
	this.movedPointX = 0;
	this.movedPointY = 0;
	var ratioX = this.sizeX / this.initSizeX;
	var ratioY = this.sizeY / this.initSizeY;
	var argument = {
		ratioX : ratioX,
		ratioY : ratioY,
		ellipsePosition : this.ellipsePosition
	};
	var command = new raphaelDashboardResizeCommand(this.selectViewList_, argument);
	return command;
};

/**
 * オブジェクトのリサイズを行う。
 *
 *
 * @param {object}
 *            target リサイズ対象のオブジェクト
 * @param {float}
 *            scaleX X軸方向の倍率
 * @param {float}
 *            scaleY Y軸方向の倍率
 * @param {ellipseSmalle}
 *            ellipseTarget
 * @param {Position}
 *            squarePosition チェック対象領域
 * @param {int}
 *            ellipsePosition クリックしたリサイズ円位置
 * @private
 */
raphaelDashboardManager.prototype.resizeChildAll = function(target, scaleX, scaleY,
		ellipseTarget, squarePosition, framePostion) {
	var objectType = target.objectType_;
	target.resize(scaleX, scaleY, ellipseTarget.fulcrumX,
			ellipseTarget.fulcrumY, framePostion);

	// 反転が必要なオブジェクトに関しては、反転しているかチェックを行う。
	// if (!raphaelMapConstants.HAS_RESIZE_LIMIT_OBJECT_LIST[objectType]) {
	// target.checkObjectPosition(ellipseTarget.fulcrumX,
	// ellipseTarget.fulcrumY, framePostion);
	// }

	if (!target.childrenList_) {
		return;
	}

	_.each(target.childrenList_, function(childObject, idx) {
		childObject.resize(scaleX, scaleY, ellipseTarget.fulcrumX,
				ellipseTarget.fulcrumY, ellipsePosition);
		if (!constants.HAS_RESIZE_LIMIT_OBJECT_LIST[objectType]) {
			childObject.checkObjectPosition(ellipseTarget.fulcrumX,
					ellipseTarget.fulcrumY, squarePosition);
		}
	});
};

raphaelDashboardManager.prototype.getViewList = function(){
	return this.dashboardView_.viewCollection;
};

raphaelDashboardManager.prototype.getViewFromId = function(objectId) {
	var targetView = this.getViewList()[objectId];
	return targetView;
};

/**
 * 右クリックの抑制
 *
 * @private
 *
 */
raphaelDashboardManager.prototype.checkMouseButtonRight = function() {
	if (this.onMouseButton_ == constants.MOUSE_BUTTON_RIGHT) {
		return false;
	}
	return true;
};

/**
 * マウスボタンが既に押されているかの判定
 *
 * @private
 *
 */
raphaelDashboardManager.prototype.checkMouseButtonDouble = function(value) {
	if (this.onMouseButton_ == null) {
		return false;
	} else {
		return true;
	}
};

raphaelDashboardManager.prototype.isSameMouseButton = function(value) {
	if (value == this.onMouseButton_) {
		return true;
	}
	return false;
};

/**
 * <p>
 * [概 要] クリックされたボタンを登録する。
 * </p>
 * <p>
 * [詳 細] 登録ボタンが無ければボタン種別を登録する。
 * </p>
 * <p>
 * [備 考] なし
 * </p>
 *
 *
 * @param {Integer}
 *            value 押下されたマウスボタンの種別
 * @public
 */
raphaelDashboardManager.prototype.setOnMouseButton = function(value) {
	if (this.onMouseButton_ == null) {
		this.onMouseButton_ = value;
	}
};

/**
 * <p>
 * [概 要] クリックされたボタンをクリアする。
 * </p>
 * <p>
 * [詳 細] 登録ボタンと同じ値であればクリアする。
 * </p>
 * <p>
 * [備 考] なし
 * </p>
 *
 *
 * @param {Integer}
 *            value 押下されたマウスボタンの種別
 * @public
 */
raphaelDashboardManager.prototype.clearOnMouseButton = function(value) {
	if (this.onMouseButton_ == value) {
		this.onMouseButton_ = null;
	}
};

raphaelDashboardManager.prototype.createNewElementId = function() {

	var maxObjectId = 0;
	_.each(this.viewList_, function(tempElement, index) {
		if (tempElement.objectId > maxObjectId) {
			maxObjectId = tempElement.objectId;
		}
	});

	return maxObjectId + 1;
};

/**
 * 任意のタグからそこにひもづくオブジェクトを取得する。
 */
raphaelDashboardManager.prototype.getObjectFromTag = function(tag) {
	if ($.isArray(tag)) {
		tag = tag[0];
	}
	var tagName = tag.tagName;
	tagName = tagName.toLowerCase();
	if (!$.inArray(tagName, this.SVG_TAG_LIST)) {
		return null;
	}
	var objectId = $(tag).attr(raphaelMapConstants.OBJECT_ID_KEY_NAME);
	if (objectId == null) {
		return null;
	}
	objectId = parseInt(objectId);
	var object = this.getViewFromId(objectId);
	return object;
};

/**
 * copy to dashboard element object.
 */
raphaelDashboardManager.prototype.copyDashboardElement = function() {
	var instance = this;
	_.each(this.selectElementList_, function(selectElement, index) {
		instance.tempCopyElementList_[index] = selectElement;
	});
};

/**
 * cut dashboard element object.
 */
raphaelDashboardManager.prototype.cutDashboardElement = function() {
	// 現在選択中のエレメント情報を登録する。
	this.tempCopyElementList_ = {};
	var objectIdArray = new Array();
	var instance = this;
	_.each(this.selectElementList_, function(selectElement, index) {
		instance.tempCopyElementList_[index] = selectElement;
		objectIdArray.push(index);
	});

	// execute command
	var executeCommand = new raphaelDashboardDeleteCommand(this.selectElementList_, null);
	executeCommand.execute();

	this.selectElementList_ = {};
	return executeCommand;
};

raphaelDashboardManager.prototype.deleteDashboardElement = function(){
	var executeCommand = new raphaelDashboardDeleteCommand(this.selectElementList_, null);
	executeCommand.execute();
	this.selectElementList_ = {};
	return executeCommand;
};

raphaelDashboardManager.prototype.pasteDashboardElement = function(event, option) {
	var target = event.target;
	var tagName = target.tagName;
	var svgTag = null;
	if (tagName == "SVG") {
		svgTag = target;
	} else {
		svgTag = $(target).parents("svg");
	}

	// calculate.
	var argument = {
		top : event.screenY - $(svgTag).top(),
		left : event.screenX - $(svgTag).left(),
		type : option.type,
		manager : this
	};
	var pasteCommand = new raphaelDashboardPasteCommand(instance.tempCopyElementList_, argument);
	pasteCommand.execute();

	var dashboardElementBBox = this.getDashboardElementBBox(instance.tempCopyElementList_);

	_.each(instance.tempCopyElementList_, function(dashboardElement, index) {

		// エレメント名を取得
		var dashboardElementStr = dashboardElement.elementName_;

		// タイプを取得
		var dashboardElementType = dashboardElement.objectType_;

		// 位置、大きさの情報を取得
		var elementProperty = {};
		if (raphaelMapConstants.PASTE_TYPE_CONTEXT == pasteType) {

			var pointX = event.pageX
					- $("#" + dashboardAreaViewItem.divId).offset()["left"] + this.x
					- dashboardElementBBox.x;

			var pointY = event.pageY
					- $("#" + dashboardAreaViewItem.divId).offset()["top"] + this.y
					- dashboardElementBBox.y;

			elementProperty = {
				pointX : pointX,
				pointY : pointY,
				width : this.width,
				height : this.height,
				objectId : instance.createNewElementId()
			};

		} else if (raphaelMapConstants.PASTE_TYPE_SHORTCUT == pasteType) {
			elementProperty = {
				pointX : this.x + 10,
				pointY : this.y + 10,
				width : this.width,
				height : this.height,
				objectId : instance.createNewElementId()
			};
		}

		// 属性の情報を追加
		var attributes = dashboardElement.getAttributes();
		_.each(attributes, function(attribute, index) {
			elementProperty[index] = attribute;
		});
		instance.createElementFunction(dashboardElementStr, dashboardElementType,
				dashboardAreaViewItem, elementProperty, true);
	});
};

raphaelDashboardManager.prototype.getDashboardElementBBox = function(dashboardElementList) {
	var returnBox = {};
	_.each(dashboardElementList, function(dashboardElement, index) {

		var x = 0;
		var y = 0;
		var width = 0;
		var height = 0;

		if (this.width > 0) {
			x = this.x;
			width = this.x + this.width;
		} else {
			x = this.x + this.width;
			width = this.x - this.width;
		}

		if (this.height > 0) {
			y = this.y;
			height = this.y + this.height;
		} else {
			y = this.y + this.height;
			height = this.y - this.height;
		}

		if (!returnBox["x"] || returnBox["x"] > x) {
			returnBox["x"] = x;
		}

		if (!returnBox["y"] || returnBox["y"] > y) {
			returnBox["y"] = y;
		}

		if (!returnBox["width"] || returnBox["width"] < width) {
			returnBox["width"] = width;
		}

		if (!returnBox["height"] || returnBox["height"] < height) {
			returnBox["height"] = height;
		}
	});

	return returnBox;
};

/**
 * add object to front.
 *
 * @param dashboardElementObject
 *            {dashboardElement} dashboardElement object.
 */
raphaelDashboardManager.prototype._addFront = function(dashboardElementObject) {
	// get element kind and adding layer num
	var elementKind = dashboardElementObject.elementName_;
	var layerNum = raphaelMapConstants.MAP_LAYER_JOIN_OBJECT[elementKind];
	if (layerNum == null) {
		layerNum = raphaelMapConstants.MAP_LAYER_NUM - 1;
	}
	// add new zindex and register
	var nextIndex = this.zIndeMaxNum_[layerNum] + 1;
	this.zIndeMaxNum_[layerNum] = nextIndex;
	dashboardElementObject.setZIndex(nextIndex);
	this.zIndexMap_[layerNum][nextIndex] = dashboardElementObject;

	// move object
	// already exist own layer
	var frontObject = null;
	for ( var count = nextIndex - 1; count >= 0; count--) {
		frontObject = this.zIndexMap_[layerNum][count];
		if (frontObject != null) {
			break;
		}
	}

	// if not exist element on own layer, get bottom object in next layer
	if (frontObject) {
		var raphaelObject = frontObject.getFrontNode();
		dashboardElementObject.moveFront(raphaelObject);
	} else {
		var backendObject = this._getBackendObject(layerNum + 1);
		if (backendObject == null) {
			return;
		}
		var raphaelObject = backendObject.getBottomNode();
		dashboardElementObject.moveBack(raphaelObject);
	}
};

/**
 * get backend object from upper start layer
 *
 * @param layer
 *            layer num
 */
raphaelDashboardManager.prototype._getBackendObject = function(layer) {
	if (layer >= this.zIndexMap_.length) {
		return null;
	}
	var maxIndex = this.zIndeMaxNum_[layer];
	for ( var count = 0; count <= maxIndex; count++) {
		backendObject = this.zIndexMap_[layer][count];
		if (backendObject != null) {
			return backendObject;
		}
	}
	return this._getBackendObject(layer + 1);
};