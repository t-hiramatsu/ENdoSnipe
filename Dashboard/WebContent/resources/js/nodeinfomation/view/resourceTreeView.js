ENS.ResourceTreeView = wgp.TreeView.extend({
	defaults : {},
	initialize : function(argument){

		// 継承元の初期化メソッド実行
		this.__proto__.__proto__.initialize.apply(this, [argument]);

		this.targetId = argument["targetId"];
		this.mode = argument["mode"];

		var instance = this;

		// コンテキストメニュー用のID
		var contextMenuId = this.cid + "_contextMenu";
		this.contextMenuId = contextMenuId;

		// リソース表示モードの場合はクリック時にリソースを表示する。
		if("display" === this.mode){
			$("#" + this.$el.attr("id")).mousedown(function(event) {
				var target = event.target;
				if ("A" == target.tagName) {
					var treeId = $(target).attr("id");
					var treeModel = instance.collection.get(treeId);
					instance.clickModel(treeModel);
				}
			});

		// リソース配置モードの場合はドラッグ時にリソースを配置する。
		}else if("arrangement" === this.mode){

			// マップモードを取得する。
			var mapMode = $("#mapMode").val();
			if(ENS.map.mode.EDIT == mapMode){
				this.setEditFunction();
			}
		}

		// ツリー構築時の処理をバインド
		$("#" + this.$el.attr("id")).bind("loaded.jstree", function(){
			instance.restoreDisplayState();
		});
	},
	clickModel : function(treeModel) {
		if (this.childView) {
			var tmpAppView = new wgp.AppView();
			tmpAppView.removeView(this.childView);
			this.childView = null;
		}
		$("#" + this.targetId).children().remove();
	
		var viewSettingName = treeModel.get("viewSettingName");
	
		var viewSettings = null;
		$.each(wgp.constants.VIEW_SETTINGS, function(index, value) {
			if (viewSettingName === index) {
				viewSettings = value;
			}
		});
		if (viewSettings == null) {
			viewSettings = wgp.constants.VIEW_SETTINGS["default"];
			if (viewSettings == null) {
				return;
			}
		}
	
		var viewClassName = viewSettings.viewClassName;
		$.extend(true, viewSettings, {
			id : this.targetId
		});
		var treeSettings = treeModel.attributes;
		this.childView = eval("new " + viewClassName
				+ "(viewSettings, treeSettings)");
	},
	setEditFunction : function(){
		this.createContextMenuTag();
		this.relateContextMenu();
	},
	createContextMenuTag : function(){

		// メニューの定義がない場合にのみメニュー用のタグを作成する。
		if($("#" + this.contextMenuId).length == 0){

			var contextMenu0 = new contextMenu("addGraph", "Add Graph");
			var contextMenu1 = new contextMenu("addSignal", "Add Signal");
			var contextMenuArray = [ contextMenu0, contextMenu1 ];
			contextMenuCreator.initializeContextMenu(this.contextMenuId, contextMenuArray);
		}
	},
	relateContextMenu : function(){
		var instance = this;
		var clickTarget = null;
		var zIndex = 1;
		var option = {
			target_id : "#" + this.$el.attr("id"),

			// コンテキストメニューの表示条件を指定する。
			//　以下の条件に全て該当する要素を右クリックした場合に表示する。
			// ①Aタグである。
			// ②要素がシグナルかグラフである。
			onShow: function(event, target){
				var tagName = event.target.tagName;
				if(tagName != "A"){
					return false;
				}

				clickTarget = $(event.target);
				var treeId = clickTarget.attr("id");

				// シグナルかグラフかによって表示するメニューを変更する。
				// シグナルの場合
				if(treeId.startsWith("Signal-")){
					$("#" + instance.contextMenuId + " #addGraph").hide();
					$("#" + instance.contextMenuId + " addSignal").show();

				// グラフの場合
				}else if(instance.collection.where({parentTreeId : treeId}).length == 0){
					$("#" + instance.contextMenuId + " #addGraph").show();
					$("#" + instance.contextMenuId + " #addSignal").hide();

				// シグナルでもグラフでもなければ表示しない。
				}else{
					return false;					
				}

			},
			onSelect : function(event, target){
				var treeId = clickTarget.attr("id");
				var treeModel = instance.collection.get(treeId);

				// TODO マップが選択されていない場合はメッセージを表示して処理を中止する。
				if(!instance.childView){
					return;
				}

				var offsetX = $("#" + instance.childView.$el.attr("id")).offset()["left"];
				var offsetY = $("#" + instance.childView.$el.attr("id")).offset()["top"];
				var resourceModel = new wgp.MapElement();

				// グラフを追加する場合
				if(event.currentTarget.id == "addGraph"){
					resourceModel.set({
						objectId : treeId,
						objectName : "ENS.ResourceGraphElementView",
						pointX : 50 + offsetX,
						pointY : 50 + offsetY,
						width : 300,
						height : 300,
						zIndex : zIndex
					});

				// シグナルを追加する場合
				}else if(event.currentTarget.id == "addSignal"){
					resourceModel.set({
						objectId : treeId,
						objectName : "ENS.ResourceStateElementView",
						pointX : 50,
						pointY : 50,
						width : 50,
						height : 50,
						stateId : "normal",
						linkId : "test"
					});
				}

				instance.childView.collection.add(resourceModel);
				zIndex++;
			}
		};

		var targetTag = $("#" + this.$el.attr("id"));
		var menuId = this.contextMenuId;
		contextMenuCreator.createContextMenuSelector(targetTag, menuId, option);

	},
	// 画面に保持している状態を復元する。
	restoreDisplayState : function(){
		var openNodeDataStr = $("#treeViewOpenNodeData").val();
		if(openNodeDataStr.length > 0){
			var　openNodeData = JSON.parse($("#treeViewOpenNodeData").val());

			// 保持している展開状態を基に展開する。
			this.openNodes(openNodeData, "id");
		}

		var selectedTreeId = $("#selectedTreeId").val();
		if(selectedTreeId.length > 0){
			this.selectNode(selectedTreeId, "id");
		}
	},
	// 展開状態のノードのIDを全て取得する。
	getOpenNodes : function(idAttribute){
		var openNodeData = [];
		var openNodeList = $("#" + this.$el.attr("id")).find("li.jstree-open");
		_.each(openNodeList, function(openNode, index){
			var openNodeAnchor = $(openNode).children("a");
			openNodeData.push( openNodeAnchor.attr(idAttribute) );
		});

		return openNodeData;
	},
	// IDを基にツリーを展開する。
	openNode : function(treeId, idAttribute){
		var treeNode = this.getTreeNode(treeId, idAttribute);
		if(treeNode.length > 0){
			$("#" + this.$el.attr("id")).jstree("open_node", treeNode.parent());
		}
	},
	// IDを基にツリーを展開する。(複数)
	openNodes : function(treeIdArray, idAttribute){
		var instance = this;
		_.each(treeIdArray, function(treeId, index){
			instance.openNode(treeId, idAttribute);
		});
	},
	// 選択中のツリーのノードを取得する。
	getSelectedNode : function(){
		var selectedNode = $("#" + this.$el.attr("id")).jstree("get_selected");
		return selectedNode
	},
	// 選択中のツリーのノードIDを取得する。
	getSelectedNodeId : function(idAttribute){
		var selectedNode = this.getSelectedNode();
		if(selectedNode.length > 0){
			return selectedNode.children("a").attr(idAttribute);
		}else{
			return "";
		}
	},
	// ツリーのノードを選択する。
	selectNode : function(treeId, idAttribute){
		var selectTreeNode = this.getTreeNode(treeId, idAttribute);
		if(selectTreeNode.length > 0){
			selectTreeNode.click();
		}
	}
});