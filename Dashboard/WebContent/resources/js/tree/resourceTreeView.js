ENS.ResourceTreeView = ENS.treeManager
		.extend({

			/**
			 * ツリー連携は不要であるため空でオーバーライドする。
			 */
			setTreeCooperation : function(){
			},
			/**
			 * 編集用のイベントを設定する。
			 */
			setEditFunction : function() {
				this.createContextMenuTag_();
				this.relateContextMenu_();
			},
			/**
			 * コンテキストメニューを生成する。
			 */
			createContextMenuTag_ : function() {

				// メニューの定義がない場合にのみメニュー用のタグを作成する。
				this.contextMenuId = this.cid + "_contextMenu";
				if ($("#" + this.contextMenuId).length == 0) {

					var contextMenu0 = new contextMenu("addGraph", "Add Graph");
					var contextMenu1 = new contextMenu("addSignal","Add Signal");
					var contextMenuArray = [ contextMenu0, contextMenu1 ];
					contextMenuCreator.initializeContextMenu(
							this.contextMenuId, contextMenuArray);
				}
			},
			relateContextMenu_ : function() {
				var instance = this;
				var clickTarget = null;
				var zIndex = 1;
				var option = {
					target_id : "#" + this.ensTreeView.$el.attr("id"),

					// コンテキストメニューの表示条件を指定する。
					// 以下の条件に全て該当する要素を右クリックした場合に表示する。
					// ①Aタグである。
					// ②要素がシグナルかグラフである。
					onShow : function(event, target) {
						var tagName = event.target.tagName;
						if (tagName != "A") {
							return false;
						}

						clickTarget = $(event.target);
						var treeId = clickTarget.attr("id");

						var treeModel = instance.ensTreeView.collection.get(treeId);
						var treeType = treeModel.get("type");

						// シグナルかグラフかによって表示するメニューを変更する。
						// シグナルの場合
						if (ENS.tree.type.SIGNAL == treeType ||
								ENS.tree.type.SUMMARYSIGNAL == treeType) {
							$("#" + instance.contextMenuId + " #addGraph")
									.hide();
							$("#" + instance.contextMenuId + " #addSignal")
									.show();

							// グラフの場合
						} else if (ENS.tree.type.TARGET == treeType ||
								ENS.tree.type.MULTIPLERESOURCEGRAPH == treeType) {
							$("#" + instance.contextMenuId + " #addGraph")
									.show();
							$("#" + instance.contextMenuId + " #addSignal")
									.hide();

							// シグナルでもグラフでもなければ表示しない。
						} else {
							return false;
						}

					},
					onSelect : function(event, target) {
						var treeId = clickTarget.attr("id");
						var treeModel = instance.ensTreeView.collection.get(treeId);

						// TODO マップが選択されていない場合はメッセージを表示して処理を中止する。
						if (!window.resourceDashboardListView.childView) {
							return;
						}

						var offsetX = $("#" + window.resourceDashboardListView.childView.$el.attr("id"))
								.offset()["left"];
						var offsetY = $("#" + window.resourceDashboardListView.childView.$el.attr("id"))
								.offset()["top"];
						var resourceModel = new wgp.DashboardElement();

						// グラフを追加する場合
						if (event.currentTarget.id == "addGraph") {

							// 同一グラフが既にマップ上に存在する場合
							var graphModel = window.resourceDashboardListView.childView.collection.get(treeId);
							if(graphModel != null){
								alert("Cannot add the graph. Because the graph has already existed in the dashboard.");
								return;
							}

							var treeType = treeModel.get("type");
							var graphObjectName = "";
							if(ENS.tree.type.MULTIPLERESOURCEGRAPH == treeType){
								graphObjectName = "ENS.MultipleResourceGraphElementView";
							}else{
								graphObjectName = "ENS.ResourceGraphElementView";
							}

							resourceModel.set({
								resourceId : treeId,
								objectName : graphObjectName,
								pointX : 50 + offsetX,
								pointY : 50 + offsetY,
								width : 300,
								height : 300,
								zIndex : zIndex
							});

							// グラフ追加イベント
							window.resourceDashboardListView.childView.changedFlag = true;

							// シグナルを追加する場合
						} else if (event.currentTarget.id == "addSignal") {

							var treeModel = instance.ensTreeView.collection.get(treeId);
							var treeIcon = treeModel.get("icon");
							var treeText = treeModel.get("data");

							var signalModel = window.resourceDashboardListView.childView.collection
									.get(treeId);
							// 同一シグナルが既にマップ上に存在する場合
							if (signalModel != null) {
								alert("Cannot add the signal. Because the signal has already existed in the dashboard.");
								return false;
							}

							resourceModel.set({
								resourceId : treeId,
								objectName : "ENS.SignalElementView",
								pointX : 50,
								pointY : 50,
								width : 50,
								height : 50,
								stateId : "normal",
								linkId : "test",
								stateId : treeIcon,
								text : treeText,
								elementAttrList : [{
								},
								{
									fontSize : 16,
									textAnchor : "middle",
									fill : ENS.dashboard.fontColor,
								}]
							});

							// シグナル追加イベント
							window.resourceDashboardListView.childView.changedFlag = true;
						}

						window.resourceDashboardListView.childView.collection.add(resourceModel);
						zIndex++;
					}
				};

				var targetTag = $("#" + this.ensTreeView.$el.attr("id"));
				var menuId = this.contextMenuId;
				contextMenuCreator.createContextMenuSelector(targetTag, menuId,
						option);

			},
			/**
			 * ツリー要素の基となるコレクションが変更された場合に、 別ペインへ状態変更を伝搬する。
			 */
			onChange : function(treeModel) {
				if (window.resourceDashboardListView != null
						&& resourceDashboardListView.childView != null) {
					var childView = resourceDashboardListView.childView;

					var dashboardElementModel = childView.collection.where({resourceId : treeModel.id});

					// 伝搬対象のビューがマップに存在しなければ処理終了
					if (!dashboardElementModel){
						return;
					}

					_.each(dashboardElementModel, function(model, index){
						var dashboardElementView = childView.viewCollection[model.id];
						if (!dashboardElementView) {
							return;
						}
						// モデルのチェンジイベントを発行する。
						dashboardElementView.model
								.trigger("change", dashboardElementView.model);
					});
				}
			}
		});