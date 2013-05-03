ENS.ResourceTreeView = ENS.treeView
		.extend({
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
					var contextMenu1 = new contextMenu("addSignal",
							"Add Signal");
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
					target_id : "#" + this.$el.attr("id"),

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

						var treeModel = instance.collection.get(treeId);
						var treeIcon = treeModel.get("icon");

						// シグナルかグラフかによって表示するメニューを変更する。
						// シグナルの場合
						if (treeIcon != undefined && treeIcon.startsWith("signal_")) {
							$("#" + instance.contextMenuId + " #addGraph")
									.hide();
							$("#" + instance.contextMenuId + " addSignal")
									.show();

							// グラフの場合
						} else if (instance.collection.where({
							parentTreeId : treeId
						}).length == 0) {
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
						var treeModel = instance.collection.get(treeId);

						// TODO マップが選択されていない場合はメッセージを表示して処理を中止する。
						if (!instance.childView) {
							return;
						}

						var offsetX = $("#" + instance.childView.$el.attr("id"))
								.offset()["left"];
						var offsetY = $("#" + instance.childView.$el.attr("id"))
								.offset()["top"];
						var resourceModel = new wgp.MapElement();

						// グラフを追加する場合
						if (event.currentTarget.id == "addGraph") {
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
						} else if (event.currentTarget.id == "addSignal") {

							var treeModel = instance.collection.get(treeId);
							var treeIcon = treeModel.get("icon");

							resourceModel.set({
								objectId : treeId,
								objectName : "ENS.ResourceStateElementView",
								pointX : 50,
								pointY : 50,
								width : 50,
								height : 50,
								stateId : "normal",
								linkId : "test",
								stateId : treeIcon
							});
						}

						instance.childView.collection.add(resourceModel);
						zIndex++;
					}
				};

				var targetTag = $("#" + this.$el.attr("id"));
				var menuId = this.contextMenuId;
				contextMenuCreator.createContextMenuSelector(targetTag, menuId,
						option);

			},
			/**
			 * ツリー要素の内容が変更された場合に、
			 * 変更内容を連携対象のペインに伝搬する。
			 */
			onChange : function(treeModel){
				ENS.treeView.prototype.onChange.apply(this, [treeModel]);

				if(!this.childView){
					return;
				}

				var id = treeModel.id;

				var treeIcon = treeModel.get("icon");
				if(treeIcon != undefined && treeIcon.startsWith("signal_")){

				}
			}
		});