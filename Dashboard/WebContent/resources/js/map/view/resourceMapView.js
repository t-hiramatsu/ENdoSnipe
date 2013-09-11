ENS.ResourceMapView = wgp.MapView.extend({
	tagName : "div",
	initialize : function(argument) {
		_.bindAll();

		var width = $("#" + this.$el.attr("id")).width();
		var height = $("#" + this.$el.attr("id")).height();
		_.extend(argument, {width : width, height : height});

		var changedFlag = false;
		this.changedFlag = changedFlag;

		var ajaxHandler = new wgp.AjaxHandler();
		this.ajaxHandler = ajaxHandler;

		this.mapMode = $("#mapMode").val();

		var contextMenuId = this.cid + "_contextMenu";
		this.contextMenuId = contextMenuId;

		this.mapId = argument["mapId"];

		// 継承元の初期化メソッド実行
		this.__proto__.__proto__.initialize.apply(this, [argument]);

		// マップ操作用マネージャ
		this.mapManager = new raphaelMapManager(this);

		// 手動でのオブジェクトID割り振り用のカウント変数
		this.maxObjectId = 0;

		// 本クラスのrenderメソッド実行
		this.renderExtend();

		if(this.mapMode == ENS.map.mode.EDIT){
			this.setEditFunction();

		}else{
			this.relateOperateContextMenu();
			this.setOperateFunction();
		}

		// イベント実行可設定
		this.isExecuteEvent_ = true;
	},
	renderExtend : function(){

		if(this.mapMode == ENS.map.mode.EDIT){
			this.createEditContextMenuTag();
		}else {
			this.createOperateContextMenuTag();
		}

		// マップ情報を読み込む。
		this.onLoad();
		return this;
	},
	destroy : function (){
		this.$el.children().remove();
		this.undelegateEvents();
	},
	// 要素が追加された際に、自身の領域に要素のタイプに応じて描画要素を追加する。
	onAdd : function(model){
		var objectName = model.get("objectName");

		// オブジェクトIDが未採番の場合
		var objectId = model.get("objectId");
		if(objectId == null){
			objectId = this.createObjectId();
			model.set({objectId : objectId}, {silent: true});
		}

		// リソースグラフの場合はグラフを描画する。
		var newView;
		if("ENS.ResourceGraphElementView" == objectName){
			newView = this._addGraphDivision(model);
			this.viewCollection[model.id] = newView;

		}else if("ENS.MultipleResourceGraphElementView" == objectName){
			newView = this._addGraphDivision(model);
			this.viewCollection[model.id] = newView;

		}else if("ENS.SignalElementView" == objectName){
			newView = this._addStateElement(model);
			this.viewCollection[model.id] = newView;

		}else if("ENS.ResourceLinkElementView" == objectName){
			newView = this._addLinkElement(model);
			this.viewCollection[model.id] = newView;

		}else if("ENS.TextBoxElementView" == objectName){
			newView = this._addTextboxElement(model);
			this.viewCollection[model.id] = newView;

		}else if("ENS.ShapeElementView" == objectName){
			newView = this._addShapeElement(model);
			this.viewCollection[model.id] = newView;

		}else{

			// 継承元の追加メソッドを実行する。
			this.__proto__.__proto__.onAdd(this, [model]);
		}

		// 編集モードの場合に各種イベントを設定する。
		if(this.mapMode == ENS.map.mode.EDIT){

			// ビューの編集イベントを設定する。
			if(newView && newView.setEditFunction){
				newView.setEditFunction();
			}

			// コンテキストメニューとの関連付け
			this.relateEditContextMenu(model);

		}else{

			// ビューの運用イベントを設定する。
			if(newView && newView.setOperateFunction){
				newView.setOperateFunction();
			}
		}
	},
	_addGraphDivision : function(model) {
		var objectId = model.get("objectId");
		var graphId = model.get("resourceId");
		var width = model.get("width");
		var height = model.get("height");
		var objectName = model.get("objectName");

		var tempId = graphId.split("/");
		var dataId = tempId[tempId.length - 1];
		var treeSettings = {
			data : dataId,
			id : graphId,
			measurementUnit : "",
			parentTreeId : "",
			treeId : ""
		};
		var viewAttribute = {
			id : graphId,
			cid : model.cid,
			rootView : this,
			graphId : graphId,
			title : dataId,
			noTermData : false,
			term : 1800 * 2,
			width : width,
			height : height,
			dateWindow : [ new Date() - 60 * 60 * 1000, new Date() ],
			attributes : {
				xlabel : "Time",
				ylabel : "value",
				labels : [ "time", "PC1", "PC2" ]
			}
		};

		var newDivAreaId = this.$el.attr("id") + "_" + model.cid;
		var newDivArea = $("<div id='" + newDivAreaId + "'></div>");
		newDivArea.css("position","absolute");

		$("#" + this.$el.attr("id")).append(newDivArea);
		newDivArea.width(model.get("width"));
		newDivArea.height(model.get("height"));

		$.extend(true, viewAttribute, {
			id : newDivAreaId
		});
		$.extend(true, viewAttribute, ENS.nodeInfoParentView.viewAttribute);
		var view = null;
		if ("ENS.ResourceGraphElementView" === objectName) {
			view = new ENS.ResourceGraphElementView(viewAttribute, treeSettings);
		} else {
			view = new ENS.MultipleResourceGraphElementView(viewAttribute, treeSettings);
		}
		view.model = model;

		// cidを設定
		$("#" + view.$el.attr("id")).attr("cid", model.cid);

		// 後で移動する。
		var parentOffset = newDivArea.parent().offset();
		newDivArea.offset({
			top : parentOffset["top"]  + model.get("pointY"),
			left :parentOffset["left"] + model.get("pointX")
		});

		return view;
	},/*,
	_addMulResGraphDivision : function(model) {
		var objectId = model.get("objectId");
		var graphId = model.get("resourceId");
		var width = model.get("width");
		var height = model.get("height");

		var tempId = graphId.split("/");
		var dataId = tempId[tempId.length - 1];
		var treeSettings = {
			data : dataId,
			id : graphId,
			measurementUnit : "",
			parentTreeId : "",
			treeId : ""
		};
		var viewAttribute = {
			id : graphId,
			cid : model.cid,
			rootView : this,
			graphId : graphId,
			title : dataId,
			noTermData : false,
			term : 1800 * 2,
			width : width,
			height : height,
			dateWindow : [ new Date() - 60 * 60 * 1000, new Date() ],
			attributes : {
				xlabel : "Time",
				ylabel : "value",
				labels : [ "time", "PC1", "PC2" ]
			}
		};

		var newDivAreaId = this.$el.attr("id") + "_" + model.cid;
		var newDivArea = $("<div id='" + newDivAreaId + "'></div>");
		newDivArea.css("position","absolute");

		$("#" + this.$el.attr("id")).append(newDivArea);
		newDivArea.width(model.get("width"));
		newDivArea.height(model.get("height"));

		$.extend(true, viewAttribute, {
			id : newDivAreaId
		});
		var view =
			new ENS.MulResGraphElementView(viewAttribute, treeSettings);
		view.model = model;

		// cidを設定
		$("#" + view.$el.attr("id")).attr("cid", model.cid);

		// 後で移動する。
		var parentOffset = newDivArea.parent().offset();
		newDivArea.offset({
			top : parentOffset["top"]  + model.get("pointY"),
			left :parentOffset["left"] + model.get("pointX")
		});

		return view;
	}*/
	_addStateElement : function(model){
		var argument = {
			paper : this.paper,
			mapView : this,
			model : model
		};

		var view =
			new ENS.SignalElementView(argument);
		return view;
	},
	_addLinkElement : function(model){
		var argument = {
			paper : this.paper,
			mapView : this,
			model : model
		};

		var view =
			new ENS.ResourceLinkElementView(argument);
		return view;
	},
	_addShapeElement : function(model){
		var argument = {
			paper : this.paper,
			mapView : this,
			model : model
		};

		var view =
			new ENS.ShapeElementView(argument);
		return view;
	},
	_addTextboxElement : function(model){
		var argument = {
			paper : this.paper,
			mapView : this,
			model : model
		};

		var view =
			new ENS.TextBoxElementView(argument);
		return view;
	},
	onSave : function(treeModel){
		var resourceArray = [];
		_.each(this.collection.models, function(model, index){
			resourceArray.push(model.toJSON());
		});

		var mapWidth = this.paper.width;
		var mapHeight = this.paper.height;
		var background = this.backgroundView.model.toJSON();

		var resourceMap = {
			mapWidth : mapWidth,
			mapHeight : mapHeight,
			background : background,
			resources : resourceArray
		}

		var setting = {
			data : {
				mapId : treeModel.get("id"),
				name : treeModel.get("data"),
				data : JSON.stringify(resourceMap)
			},
			url : wgp.common.getContextPath() + "/map/update"
		}
		var telegram = this.ajaxHandler.requestServerSync(setting);
		var returnData = $.parseJSON(telegram);
		this.changedFlag = false;

		var result = returnData.result;
		if(result == "fail"){
			alert(returnData.message);
		}
	},
	onLoad : function(){

		// コレクションをリセット
		this.collection.reset();

		// マップ情報を画面に表示する。
		var mapId = this.mapId;

		if(mapId != undefined && mapId.length > 0){

			// サーバからマップ情報を取得し、結果をコレクションに追加する。
			var telegram = this.getMapData(mapId);
			var returnData = $.parseJSON(telegram);
			if(returnData.result == "fail"){
				alert(returnData.message);
				return;
			}

			var mapData = $.parseJSON(returnData.data.mapData);
			var mapWidth = mapData["mapWidth"];
			var mapHeight = mapData["mapHeight"];
			var background = mapData["background"];
			if(mapWidth && mapHeight){
				this.paper.setSize(mapWidth, mapHeight);
			}

			if(!background){
				background = ENS.map.backgroundSetting;
			}
			var backgroundModel = new wgp.MapElement(background);

			var backgroundArgument = {
				paper : this.paper,
				mapView : this,
				model : backgroundModel
			};

			var backgroundView = new ENS.BackgroundElementView(backgroundArgument);
			this.backgroundView = backgroundView;

			var resources = mapData["resources"];
			var instance = this;
			_.each(resources, function(resource, index){
				var mapElement = new wgp.MapElement(resource);
				instance.collection.add(mapElement);
			});
		}

		// オブジェクトIDの番号の最大値をセット
		var maxObjectIdElement = this.collection.max(function(element){return element.get("objectId")});
		if(maxObjectIdElement){
			this.maxObjectId = maxObjectIdElement.get("objectId") + 1;
		}
	},
	getMapData : function(mapId){
		var setting = {
			data : {
				mapId : mapId
			},
			url : wgp.common.getContextPath() + "/map/getById"
		}
		return this.ajaxHandler.requestServerSync(setting);
	},
	// raphaelPaperをクリックした際に追加するイベントを付加する。
	setClickAddElementEvent : function(viewClassName, name, type){

		if(this.clickAddElementFunction){
			$(this.paper.canvas).off("click", this.clickAddElementFunction);
		}

		if("ENS.ResourceLinkElementView" === viewClassName){
			this.clickAddElementFunction = this.createClickAddLinkEvent(name, type);

		} else if("ENS.TextBoxElementView" == viewClassName){
			this.clickAddElementFunction = this.createClickAddTextboxEvent(name, type);

		} else if("ENS.ShapeElementView" === viewClassName){
			this.clickAddElementFunction = this.createClickAddShapeEvent(name, type);

		} else{
			this.clickAddElementFunction = null;

		}

		if(this.clickAddElementFunction){
			$(this.paper.canvas).on("click", this.clickAddElementFunction);
		}
	},
	createClickAddShapeEvent : function(shapeName, shapeType){
		var instance = this;
		var clickEventFunction = function(event){
			var resourceModel = new wgp.MapElement();

			// オリジナルオブジェクトIDの算出
			var objectId = instance.createObjectId();

			var width = 100;
			var height = 100;
			resourceModel.set({
				objectId : objectId,
				objectName : "ENS.ShapeElementView",
				pointX : event.pageX - $("#" + instance.$el.attr("id")).offset()["left"],
				pointY : event.pageY - $("#" + instance.$el.attr("id")).offset()["top"],
				width : 100,
				height : 100,
				shapeName : shapeName,
				shapeType : shapeType,
				zIndex : 1,
				elementAttrList : [{
					fill : "#FFFFFF",
					stroke : "#000000",
					strokeDasharray : "",
					strokeWidth : 3
				}]
			});
			instance.collection.add(resourceModel);
			$(instance.paper.canvas).off("click", clickEventFunction);
		}

		return clickEventFunction;
	},
	createClickAddTextboxEvent :function(){
		var instance = this;
		var clickEventFunction = function(event){
			var resourceModel = new wgp.MapElement();

			// オリジナルオブジェクトIDの算出
			var objectId = instance.createObjectId();

			var width = 100;
			var height = 100;
			resourceModel.set({
				objectId : objectId,
				objectName : "ENS.TextBoxElementView",
				pointX : event.pageX - $("#" + instance.$el.attr("id")).offset()["left"],
				pointY : event.pageY - $("#" + instance.$el.attr("id")).offset()["top"],
				width : 100,
				height : 100,
				zIndex : 1,
				elementAttrList : [{
					fill : "#FFFFFF",
					stroke : "#000000",
					strokeDasharray : "",
					strokeWidth : 1
				},{
					fontSize : 10,
					textAnchor : "middle",
					fontFamily : "Arial",
					fill : "#000000"
				}]
			});
			instance.collection.add(resourceModel);
			$(instance.paper.canvas).off("click", clickEventFunction);
		}

		return clickEventFunction;
	},
	createClickAddLinkEvent : function(shapeName, shapeType){
		var instance = this;
		var linkName = "";
		var linkUrl = "";

		var clickEventFunction = function(event){

			var resourceModel = new wgp.MapElement();

			var createLinkDialog = $("<div title='Create new Link'></div>");
			createLinkDialog.append("<p> Please enter new Link name</p>");

			var linkNameLabel = $("<label for='linkName'>Link Name：</label>");
			var linkNameText =
				$("<input type='text' name='linkName' id='linkName' class='text'>");

			var linkUrlLabel = $("<label for='linkUrl' style='margin-top: 10px;'>Target Map：</label>");
			var linkUrlSelect = $("<select name='selectMapList' style='margin-top: 10px;'></select>")
			var mapList = resourceMapListView.collection.models;
			_.each(mapList, function(mapListModel, index){
				var linkUrlName = mapListModel.get("data");
				var linkUrlValue = mapListModel.get("id");
				linkUrlSelect.append("<option value='"+ linkUrlValue +"'>" + linkUrlName + "</option>");
			});

			createLinkDialog.append(linkNameLabel);
			createLinkDialog.append(linkNameText);
			createLinkDialog.append("</br>")
			createLinkDialog.append(linkUrlLabel);
			createLinkDialog.append(linkUrlSelect);
			createLinkDialog.dialog({
				autoOpen: false,
				height: 500,
				width: 400,
				modal: true,
				buttons : {
					"OK" : function(){

						if(linkNameText.val().length == 0){
							alert("link Name is require");
							return;
						}
						linkName = linkNameText.val();
						linkUrl = linkUrlSelect.val();
						createLinkDialog.dialog("close");

						// オブジェクトIDの算出
						var objectId = instance.createObjectId();

						resourceModel.set({
							objectId : objectId,
							objectName : "ENS.ResourceLinkElementView",
							text : linkName,
							linkUrl : linkUrl,
							linkType : "mapLinkURL",
							pointX : event.pageX - $("#" + instance.$el.attr("id")).offset()["left"],
							pointY : event.pageY - $("#" + instance.$el.attr("id")).offset()["top"],
							elementAttrList : [{
								fontSize : 28,
								textAnchor : "start",
								fontFamily : "Arial",
								fill : ENS.map.fontColor,
							}],
							zIndex : 1
						});
						instance.collection.add(resourceModel);
						$(instance.paper.canvas).unbind("click", clickEventFunction);

						// リンク追加イベント
						window.resourceMapListView.childView.changedFlag = true;
					},
					"CANCEL" : function(){
						createLinkDialog.dialog("close");
					}
				},
				close : function(event){
					createLinkDialog.remove();
					$(instance.paper.canvas).off("click", clickEventFunction);
					$(".map_menu_icon-active").removeClass("map_menu_icon-active");
				}
			});

			createLinkDialog.dialog("open");
		};

		return clickEventFunction;
	},
	createOperateContextMenuTag : function(){
	},
	createEditContextMenuTag : function(){

		// メニューの定義がない場合にのみメニュー用のタグを作成する。
		if($("#" + this.contextMenuId).length == 0){

			var contextMenu0 = new contextMenu("Remove", "Remove");
			var contextMenu1 = new contextMenu("Properties", "Properties");
			var contextMenuArray = [ contextMenu0 , contextMenu1];
			contextMenuCreator.initializeContextMenu(this.contextMenuId, contextMenuArray);
		}
	},
	relateOperateContextMenu : function(){
	},
	relateEditContextMenu : function(model){
		var instance = this;
		var option = {
			onShow: function(event, target){
			},
			onSelect : function(event, target){
				var cid = $(target).attr("cid");
				var model = instance.collection._byCid[cid];
				if(event.currentTarget.id == "Remove"){
					// 削除時のイベント
					window.resourceMapListView.childView.changedFlag = true;
					var model = instance.collection._byCid[cid];
					instance.collection.remove(model);
				}else if(event.currentTarget.id == "Properties"){
					var targetView = instance.viewCollection[model.id];
					var propertyView = new ENS.MapElementPropertyView(targetView);
				}
			}
		};

		var resourceView = this.viewCollection[model.id];
		resourceView.relateContextMenu(this.contextMenuId, option);
	},
	enlargeMapArea : function(pointX, pointY, width, height){
		var mapWidth = this.paper.width;
		var mapHeight = this.paper.height;
		var changeFlag = false;

		if(pointX + width + ENS.map.extraMapSize > mapWidth){
			mapWidth = pointX + width + ENS.map.extraMapSize;
			changeFlag = true;
		}

		if(pointY + height + ENS.map.extraMapSize > mapHeight){
			mapHeight = pointY + height + ENS.map.extraMapSize;
			changeFlag = true;
		}

		if(changeFlag){
			this.paper.setSize(mapWidth, mapHeight);
			var backWidth = this.backgroundView.getWidth();
			var backHeight = this.backgroundView.getHeight();
			this.backgroundView.resize(
				mapWidth / backWidth,
				mapHeight / backHeight,
				raphaelMapConstants.RIGHT_UNDER
			);
		}

		// ドラッグ時のイベント
		this.changedFlag = true;
	},
	setMapSize : function(mapWidth, mapHeight){
		this.paper.setSize(mapWidth, mapHeight);
		this.changeFlag = true;
	},
	createObjectId : function(){
		this.maxObjectId = this.maxObjectId + 1;
		return this.maxObjectId;
	},
	setEditFunction : function(){

		var draginitEventKey = "draginit ." + raphaelMapConstants.CLASS_MAP_ELEMENT;
		var draginitEventFunctionName = "draginitFunction";

		var dragEventKey = "drag ." + raphaelMapConstants.CLASS_MAP_ELEMENT;
		var dragEventFunctionName = "dragFunction";

		var dragendEventKey = "dragend ." + raphaelMapConstants.CLASS_MAP_ELEMENT;
		var dragendEventFunctionName = "dragendFunction";

		var events = {};
		events[draginitEventKey] = draginitEventFunctionName;
		events[dragEventKey] = dragEventFunctionName;
		events[dragendEventKey] = dragendEventFunctionName;

		this.delegateEvents(events);
	},
	setOperateFunction : function(){

	},
	draginitFunction : function(event, option){
		// target has object id ?
		var target = event.target;
		var objectType = $(target).attr(raphaelMapConstants.OBJECT_TYPE_FIELD_NAME);
		if (objectType == raphaelMapConstants.ELLIPSESMALL_ELEMENT_NAME) {
			// resize event
			this.eventType = raphaelMapConstants.EVENT_TYPE_RESIZE;
			this.mapManager.executeEvent(raphaelMapConstants.EVENT_TYPE_RESIZE_START,
					event, option);
		} else {
			// move event
			var objectId = $(target).attr(raphaelMapConstants.OBJECT_ID_NAME);
			if (objectId) {
				this.eventType = raphaelMapConstants.EVENT_TYPE_MOVE;
				this.mapManager.executeEvent(raphaelMapConstants.EVENT_TYPE_MOVE_START,
						event, option);
				// select event
				this.mapManager.executeEvent(raphaelMapConstants.EVENT_TYPE_SELECT,
						event, option);
			} else {
				this.eventType = raphaelMapConstants.EVENT_TYPE_RELEASE_SELECT;
				this.mapManager.executeEvent(raphaelMapConstants.EVENT_TYPE_RELEASE_SELECT,
						event, option);
			}
		}
	},
	dragFunction : function(event, option){
		// event adjust
		if (!this.isExecuteEvent_) {
			return;
		}
		this.isExecuteEvent_ = false;
		var instance = this;
		window.setTimeout(function() {
			instance.isExecuteEvent_ = true;
		}, raphaelMapConstants.EVENT_EXECUTE_INTERVAL);

		if (this.eventType == raphaelMapConstants.EVENT_TYPE_MOVE) {
			this._dragMove(event, option);
		} else {
			this._dragResize(event, option);
		}
	},
	_dragMove : function(event, option){
		this.mapManager.executeEvent(raphaelMapConstants.EVENT_TYPE_MOVE, event,
				option);
	},
	_dragResize : function(event, option){
		this.mapManager.executeEvent(raphaelMapConstants.EVENT_TYPE_RESIZE, event,
				option);
	},
	dragendFunction : function(event, option){
		if (this.eventType == raphaelMapConstants.EVENT_TYPE_MOVE) {
			this._dragMoveEnd(event, option);
		} else if (this.eventType == raphaelMapConstants.EVENT_TYPE_RESIZE) {
			this._dragResizeEnd(event, option);
		}
	},
	_dragMoveEnd :function(event, option){
		this.mapManager.executeEvent(raphaelMapConstants.EVENT_TYPE_MOVE_END,
			event, option);
	},
	_dragResizeEnd : function(event, option){
		this.mapManager.executeEvent(raphaelMapConstants.EVENT_TYPE_RESIZE_END,
			event, option);
	}
});