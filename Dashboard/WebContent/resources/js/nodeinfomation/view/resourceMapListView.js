ENS.ResourceMapListView = wgp.TreeView
		.extend({
			defaults : {},
			initialize : function(argument) {
				_.bindAll();

				// コレクションの初期化
				this.collection = new TreeModelList();

				// コレクション用イベントの登録
				this.registerCollectionEvent();

				// Ajax通信機能の初期化
				var ajaxHandler = new wgp.AjaxHandler();
				this.ajaxHandler = ajaxHandler;

				// コンテキストメニュー用のID
				var contextMenuId = this.cid + "_contextMenu";
				this.contextMenuId = contextMenuId;

				// ツリーの初期化
				$("#" + this.$el.attr("id")).children().remove();

				// 継承元の初期化メソッド実行
				this.__proto__.__proto__.initialize.apply(this, [ argument ]);

				this.targetId = argument["targetId"];

				// マップ一覧の要素がクリックされた際に紐付くマップを表示する。
				var instance = this;
				$("#" + this.$el.attr("id")).mousedown(function(event) {

					// 左クリック時のみ処理
					if (event.button != 0) {
						return;
					}

					var target = event.target;
					if ("A" == target.tagName) {
						var treeId = $(target).attr("id");
						var treeModel = instance.collection.get(treeId);
						instance.clickModel(treeModel);
					}
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
				var resourceMapView = new ENS.ResourceMapView({
					id : this.targetId,
					mapId : treeModel.get("id")
				});

				this.childView = resourceMapView;
				resourceTreeView.childView = resourceMapView;

				// ツリー要素を再選択
				var treeNode = this.getTreeNode(treeModel.id, "id");
				$("#" + this.$el.attr("id")).jstree("deselect_all");
				$("#" + this.$el.attr("id")).jstree("toggle_select", treeNode.parent("li"));
			},
			onLoad : function() {

				// ツリーを消去する。
				$("#" + this.$el.attr("id")).children().remove();

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
				var mapNameText = $("<input type='text' name='mapName' id='mapName' class='text ui-widget-content ui-corner-all'>");
				createMapDialog.append(mapNameLabel);
				createMapDialog.append(mapNameText);

				createMapDialog.dialog({
					autoOpen : false,
					height : 300,
					width : 350,
					modal : true,
					buttons : {
						"OK" : function() {

							if (mapNameText.val().length == 0) {
								alert("Map Name is require");
								return;
							}

							var setting = {
								data : {
									name : mapNameText.val(),
									data : "{}"
								},
								url : wgp.common.getContextPath()
										+ "/map/insert"
							}
							instance.ajaxHandler.requestServerSync(setting);
							createMapDialog.dialog("close");

							// マップ一覧を再描画する。
							instance.onLoad();
						},
						"CANCEL" : function() {
							createMapDialog.dialog("close");
						}
					},
					close : function(event) {
						createMapDialog.remove();
					}
				});

				createMapDialog.dialog("open");
			},
			onRemove : function(treeModel) {
				var instance = this;
				var removeMapDialog = $("<div title='Remove this Map'></div>");
				removeMapDialog
						.append("<p>That you to remove this map Sure?</p>");
				removeMapDialog.dialog({
					autoOpen : false,
					height : 300,
					width : 350,
					modal : true,
					buttons : {
						"YES" : function() {
							var setting = {
								data : {
									mapId : treeModel.id
								},
								url : wgp.common.getContextPath()
										+ "/map/removeById"
							}
							instance.ajaxHandler.requestServerSync(setting);
							removeMapDialog.dialog("close");

							// マップの表示内容を全て消去する。
							$("#" + instance.targetId).children().remove();

							// ビューの関連付けを削除する。
							instance.childView = null;
							resoureceTreeView.childView = null;

							// マップ一覧を再描画する。
							instance.onLoad();
						},
						"NO" : function() {
							removeMapDialog.dialog("close");
						}
					},
					close : function(event) {
						removeMapDialog.remove();
					}
				});

				removeMapDialog.dialog("open");
			},
			setEditFunction : function() {
				this.createContextMenuTag();
				this.relateContextMenu();
			},
			createContextMenuTag : function() {

				// メニューの定義がない場合にのみメニュー用のタグを作成する。
				if ($("#" + this.contextMenuId).length == 0) {

					var contextMenu0 = new contextMenu("Remove", "Remove");
					var contextMenuArray = [ contextMenu0 ];
					contextMenuCreator.initializeContextMenu(
							this.contextMenuId, contextMenuArray);
				}
			},
			relateContextMenu : function() {
				var instance = this;
				var clickTarget = null;
				var option = {
					target_id : "#" + this.$el.attr("id"),
					onShow : function(event, target) {
						var tagName = event.target.tagName;
						if (tagName != "A") {
							return false;
						} else {
							clickTarget = event.target;
							return true;
						}
					},
					onSelect : function(event, target) {
						var treeId = $(clickTarget).attr("id");
						var treeModel = instance.collection.get(treeId);

						if (event.currentTarget.id == "Remove") {
							instance.onRemove(treeModel);
						}
					}
				};

				var targetTag = $("#" + this.$el.attr("id"));
				var menuId = this.contextMenuId;
				contextMenuCreator.createContextMenuSelector(targetTag, menuId,
						option);
			},
			/**
			 * ツリーの表示状態を復元する。
			 * selectTreeId {String} 選択しているツリーのID
			 * openNodes {String[]} 展開しているツリーのID配列
			 */
			restoreDisplayState : function(selectTreeId, openNodes) {

				// ツリーの展開状態を復元する。
				if (openNodes && openNodes.length > 0) {
					this.openNodes(openNodes, "id");
				}

				// ツリーの選択状態を復元する。
				if (selectTreeId) {
					this.selectNode(selectTreeId, "id");
				}

				// マップを表示する。
				var treeModel = this.collection.get(selectTreeId);
				if(treeModel){
					this.clickModel(treeModel);
				}
			}
		});