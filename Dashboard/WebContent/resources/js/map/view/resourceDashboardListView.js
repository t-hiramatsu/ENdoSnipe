ENS.ResourceDashboardListView = wgp.TreeView
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

				// ダッシュボード一覧の要素がクリックされた際に紐付くダッシュボードを表示する。
				var instance = this;
				$("#" + this.$el.attr("id")).mousedown(function(event) {

					// 左クリック時のみ処理
					if (event.button != 0) {
						return;
					}

					var target = event.target;
					var treeId = null;

					// ツリーのアンカータグクリック時
					if ("A" == target.tagName) {
						treeId = $(target).attr("id");

					// ツリーのアイコンタグクリック時
					}else if("INS" == target.tagName){
						treeId = $(target).parent().attr("id");
					}

					if(treeId){
						var treeModel = instance.collection.get(treeId);
						instance.clickModel(treeModel);
					}
				});

				// ツリー情報をサーバから読み込む
				this.onLoad();
			},
			clickModel : function(treeModel) {
				if (this.childView) {
					this.saveOperation();
					var tmpAppView = new wgp.AppView();
					tmpAppView.removeView(this.childView);
					this.childView.destroy();
				}

				var treeSettings = treeModel.attributes;
				var resourceDashboardView = new ENS.ResourceDashboardView({
					id : this.targetId,
					dashboardId : treeModel.get("id")
				});

				this.childView = resourceDashboardView;

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
					url : wgp.common.getContextPath() + "/dashboard/getAll"
				}

				var result = this.ajaxHandler.requestServerSync(setting);
				var dashboardListTree = $.parseJSON(result)["dashboard"];
				instance.collection = new TreeModelList(dashboardListTree, {
					model : wgp.TreeModel
				});
				this.renderAll();
			},
			onCreate : function() {
				var instance = this;
				var createDashboardDialog = $("<div title='Create new Dashboard'></div>");
				createDashboardDialog.append("<p> Please enter new Dashboard Name</p>");

				var dashboardNameLabel = $("<label for='dashboardName'>Dashboard Name</label>");
				var dashboardNameText = $("<input type='text' name='dashboardName' id='dashboardName' class='text'>");
				createDashboardDialog.append(dashboardNameLabel);
				createDashboardDialog.append(dashboardNameText);

				createDashboardDialog.dialog({
					autoOpen : false,
					height : 300,
					width : 350,
					modal : true,
					buttons : {
						"OK" : function() {

							if (dashboardNameText.val().length == 0) {
								alert("Dashboard Name is required.");
								return;
							}

							var setting = {
								data : {
									name : dashboardNameText.val(),
									data : "{}"
								},
								url : wgp.common.getContextPath()
										+ "/dashboard/insert"
							}

							var telegram = instance.ajaxHandler.requestServerSync(setting);
							var returnData = $.parseJSON(telegram);

							var result = returnData.result;
							if(result == "fail"){
								alert(returnData.message);

							}else{
								createDashboardDialog.dialog("close");

								// ダッシュボード一覧を再描画する。
								instance.onLoad();
							}
						},
						"CANCEL" : function() {
							createDashboardDialog.dialog("close");
						}
					},
					close : function(event) {
						createDashboardDialog.remove();
					}
				});

				createDashboardDialog.dialog("open");
			},
			onRemove : function(treeModel) {
				var instance = this;
				var removeDashboardDialog = $("<div title='Remove this Dashboard'></div>");
				removeDashboardDialog
						.append("<p>That you to remove this dashboard Sure?</p>");
				removeDashboardDialog.dialog({
					autoOpen : false,
					height : 300,
					width : 350,
					modal : true,
					buttons : {
						"OK" : function() {
							var setting = {
								data : {
									dashboardId : treeModel.id
								},
								url : wgp.common.getContextPath()
										+ "/dashboard/removeById"
							}
							var telegram = instance.ajaxHandler.requestServerSync(setting);
							var returnData = $.parseJSON(telegram);
							if(returnData.result == "fail"){
								alert(returnData.message);
								return;
							}
							removeDashboardDialog.dialog("close");

							// ダッシュボードの表示内容を全て消去する。
							$("#" + instance.targetId).children().remove();

							// ビューの関連付けを削除する。
							instance.childView = null;

							// ダッシュボード一覧を再描画する。
							instance.onLoad();
						},
						"Cancel" : function() {
							removeDashboardDialog.dialog("close");
						}
					},
					close : function(event) {
						removeDashboardDialog.remove();
					}
				});

				removeDashboardDialog.dialog("open");
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

				// ダッシュボードを表示する。
				var treeModel = this.collection.get(selectTreeId);
				if(treeModel){
					this.clickModel(treeModel);
				}
			},
						/**
			 * 操作した内容を保存するか確認する。
			 */
			saveOperation : function(){
				var instance = this;
				if(!instance.childView){
					return;
				}

				var changedFlag = instance.childView.changedFlag;

				if(changedFlag)
				{
					if(window.confirm("Save change?"))
					{
						var selectedId = $("#" + instance.id).find(".jstree-clicked")[0].id;
						var treeModel = instance.collection.where({id : selectedId})[0];
						instance.childView.onSave(treeModel);
					}
					instance.childView.changedFlag = false;
				}
			}
		});