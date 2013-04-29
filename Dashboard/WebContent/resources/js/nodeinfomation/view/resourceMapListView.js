ENS.ResourceMapListView = wgp.TreeView.extend({
	defaults : {},
	initialize : function(argument){
		_.bindAll();

		// コレクションの初期化
		this.collection = new TreeModelList();

		// コレクション用イベントの登録
		this.registerCollectionEvent();
		
		// Ajax通信機能の初期化
		var ajaxHandler = new wgp.AjaxHandler();
		this.ajaxHandler = ajaxHandler;

		var contextMenuId = "ResourceMapListMenu";
		this.contextMenuId = contextMenuId;

		// ツリーの初期化
		$("#" + this.$el.attr("id")).children().remove();
		
		// 継承元の初期化メソッド実行
		this.__proto__.__proto__.initialize.apply(this, [argument]);

		this.targetId = argument["targetId"];

		// マップ一覧の要素がクリックされた際に紐付くマップを表示する。
		var instance = this;
		$("#" + this.$el.attr("id")).mousedown(function(event) {
			$("#" + instance.$el.attr("id") + " A").removeClass("jstree-clicked");

			// 左クリック時のみ処理
			if(event.button != 0){
				return;
			}

			var target = event.target;
			if ("A" == target.tagName) {
				var treeId = $(target).attr("id");
				var treeModel = instance.collection.get(treeId);
				instance.clickModel(treeModel);
				$(target).addClass("jstree-clicked");
			}
		});

		// ツリー構築後の処理をバインド
		$("#" + this.$el.attr("id")).bind("loaded.jstree", function(){
			instance.createContextMenuTag();
			instance.relateContextMenu();
		});

		// ツリー情報をサーバから読み込む
		this.onLoad();
	},
	clickModel : function(treeModel) {
		if (this.childView) {
			var tmpAppView = new wgp.AppView();
			tmpAppView.removeView(this.childView);
			this.childView = null;
		}
		$("#" + this.targetId).children().remove();

		var treeSettings = treeModel.attributes;
		this.childView = new ENS.ResourceMapView({
			id : this.targetId,
			mapId : treeModel.get("id")
		});
	},
	onLoad : function() {
		var instance = this;

		var setting = {
			data : {},
			url : wgp.common.getContextPath() + "/map/getAll"
		}

		var result = this.ajaxHandler.requestServerSync(setting);
		var mapListTree = $.parseJSON(result)["map"];
		instance.collection = new TreeModelList(mapListTree, {
			model : wgp.TreeModel
		});
		this.renderAll();
	},
	onCreate : function() {
		var instance = this;
		var createMapDialog = $("<div title='Create new Map'></div>");
		createMapDialog.append("<p> Please enter new Map name</p>");

		var mapNameLabel = $("<label for='mapName'>Map Name</label>");
		var mapNameText = 
			$("<input type='text' name='mapName' id='mapName' class='text ui-widget-content ui-corner-all'>");
		createMapDialog.append(mapNameLabel);
		createMapDialog.append(mapNameText);

		createMapDialog.dialog({
			autoOpen: false,
			height: 300,
			width: 350,
			modal: true,
			buttons : {
				"OK" : function(){

					if(mapNameText.val().length == 0){
						alert("Map Name is require");
						return;
					}
					
					var setting = {
						data : {
							name : mapNameText.val(),
							data : "{}"
						},
						url : wgp.common.getContextPath() + "/map/insert"
					}
					instance.ajaxHandler.requestServerSync(setting);
					createMapDialog.dialog("close");

					// マップ一覧を再描画する。
					instance.onLoad();
				},
				"CANCEL" : function(){
					createMapDialog.dialog("close");
				}
			},
			close : function(event){
				createMapDialog.remove();
			}
		});

		createMapDialog.dialog("open");
	},
	onRemove : function() {
		var instance = this;
		var removeMapDialog = $("<div title='Remove this Map'></div>");
		removeMapDialog.append("<p>That you to remove this map Sure?</p>");

		removeMapDialog.dialog({
			autoOpen: false,
			height: 300,
			width: 300,
			modal: true,
			buttons : {
				"YES" : function(){
					var setting = {
						data : {
							name : mapNameText.val(),
							data : "{}"
						},
						url : wgp.common.getContextPath() + "/map/insert"
					}
					instance.ajaxHandler.requestServerSync(setting);
					removeMapDialog.dialog("close");

					// マップ一覧を再描画する。
					instance.onLoad();
				},
				"NO" : function(){
					removeMapDialog.dialog("close");
				}
			},
			close : function(event){
				removeMapDialog.remove();
			}
		});
	},
	createContextMenuTag : function(){

		// メニューの定義がない場合にのみメニュー用のタグを作成する。
		if($("#" + this.contextMenuId).length == 0){

			var contextMenu0 = new contextMenu("Remove", "Remove");
			var contextMenuArray = [ contextMenu0 ];
			contextMenuCreator.initializeContextMenu(this.contextMenuId, contextMenuArray);
		}
	},
	relateContextMenu : function(){
		var instance = this;
		var option = {
			onShow: function(event, target){
			},
			onSelect : function(event, target){
				var treeId = $(target).attr("id");
				var treeModel = instance.collection.get(treeId);

				if(event.currentTarget.id == "Remove"){
				}
			}
		};

		var targetTag = $("#" + this.$el.attr("id") + " A");
		var menuId = this.contextMenuId;
		contextMenuCreator.createContextMenuSelector(targetTag, menuId, option);
	}
});