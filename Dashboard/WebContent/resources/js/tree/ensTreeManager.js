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
ENS.treeManager = wgp.AbstractView
		.extend({
			initialize : function(argument, treeSettings) {
				this.BATCH_UPDATE_MIN_SIZE = 3;

				// ENdoSnipe用のツリー作成
				this.ensTreeView = new ENS.treeView({
					id : "tree_area",
					targetId : "contents_area",
					themeUrl : wgp.common.getContextPath()
							+ "/resources/css/jsTree/style.css"
				});

				// ツリー連携を追加。
				this.setTreeCooperation();

				appView.addView(this.ensTreeView, wgp.constants.TREE.DATA_ID);
				var websocketClient = new wgp.WebSocketClient(appView,
						"notifyEvent");
				websocketClient.initialize();
				// appView.getTermData([ wgp.constants.TREE.DATA_ID ], new
				// Date(),
				// new Date());

				// クリックイベント作成
				$("#tree_area").click(function() {
					if ($("[id$='mapreduce/task']") !== undefined) {

						var elem = $("[id$='mapreduce/task']");

						$("#tree_area").jstree("delete_node", elem);
					}
				});

				this.getTopNodes();
				this.finishOpenOrClose = false;

				var instance = this;

				$("#tree_area").jstree("mousedown").bind(
						"mousedown.jstree",
						function(event) {
							instance.finishOpenOrClose = false;

							/** 右クリック押下時には処理を行わない。 */
							if (event.which == ENS.tree.CLICK_RIGHT) {
								return;
							}
							/* クリックされたaタグを取得する。 */
							var clickTarget = event.target;
							var parentTag = $(clickTarget).parent();
							if ($(parentTag).hasClass("jstree-leaf")) {
								return true;
							}

							var isOpen;
							if ($(parentTag).hasClass("jstree-open")) {
								isOpen = true;
							} else {
								isOpen = false;
							}

							ENS.tree.addedOtherNodes = [];

							setTimeout(function() {
								instance.handleExpandCollapseTag(clickTarget,
										isOpen);
							}, 0);
							return true;
						});

				$("#tree_area").bind("open_node.jstree close_node.jstree",
						function(e) {
							instance.finishOpenOrClose = true;
						});
			},
			setTreeCooperation : function() {
				this.ensTreeView.setClickEvent("contents_area");
				this.ensTreeView.addContextMenu(ENS.tree.contextOption);
			},
			render : function() {
				console.log('call render');
			},
			onAdd : function(element) {
				console.log('call onAdd');
			},
			onChange : function(element) {
				console.log('called changeModel');
			},
			onRemove : function(element) {
				console.log('called removeModel');
			},
			getTermData : function() {

			},
			destroy : function() {

			},
			handleExpandCollapseTag : function(clickTarget, isOpen) {
				/* Managerかどうか判定する。 */
				var tagName = clickTarget.tagName;
				if (tagName != "INS") {
					return;
				}
				var treeTag = $(clickTarget).siblings("a");
				/* 展開か格納かを判定する。 */
				var parentTag = $(clickTarget).parent();
				var treeTagModel = treeTag[0];
				if (isOpen === true) {
					/* 格納 */
					if (treeTagModel !== undefined) {
						var parentNodeId = treeTagModel.getAttribute("id");
						this.removeChildNodes(parentNodeId);
					}
				} else {
					/* 展開 */
					if (treeTagModel !== undefined) {
						var parentNodeId = treeTagModel.getAttribute("id");
						this.getDirectChildNode(parentNodeId);
					}
				}
			},
			/**
			 * TreeViewのモデルを取得する。
			 */
			getENSTreeModel : function(graphName) {
				var treeModel = this.ensTreeView.collection.get(graphName);
				return treeModel;
			},
			getTopNodes : function() {
				// Ajax通信用の設定
				var settings = {
					url : ENS.tree.GET_TOP_NODES
				};

				// 非同期通信でデータを送信する
				var ajaxHandler = new wgp.AjaxHandler();
				settings[wgp.ConnectionConstants.SUCCESS_CALL_OBJECT_KEY] = this;
				settings[wgp.ConnectionConstants.SUCCESS_CALL_FUNCTION_KEY] = "callbackGetTopNodes";
				ajaxHandler.requestServerAsync(settings);
			},
			callbackGetTopNodes : function(topNodes) {
				this.ensTreeView.collection.add(topNodes);
				this.ensTreeView.renderAll();
			},
			/**
			 * 直下の子要素を取得する。
			 * 
			 * @param parentNodeId
			 *            親ノードのID
			 */
			getDirectChildNode : function(parentNodeId) {
				var sendData = {
					parentTreeId : parentNodeId
				};

				// Ajax通信用の設定
				var settings = {
					data : sendData,
					url : ENS.tree.GET_DIRECT_CHILDLEN_NODE
				};

				// 非同期通信でデータを送信する
				var ajaxHandler = new wgp.AjaxHandler();
				settings[wgp.ConnectionConstants.SUCCESS_CALL_OBJECT_KEY] = this;
				settings[wgp.ConnectionConstants.SUCCESS_CALL_FUNCTION_KEY] = "callbackGetDirectChildNode";
				ajaxHandler.requestServerAsync(settings);
			},
			/**
			 * 直下の子要素取得のコールバック関数。<br>
			 * 
			 * 
			 * @param childNodes
			 *            追加する子ノードの配列
			 */
			callbackGetDirectChildNode : function(childNodes) {
				if (childNodes.length > this.BATCH_UPDATE_MIN_SIZE) {
					// 追加する子ノードが多い場合には、clean_node、__getrollbackなどの実行を最初と最後の要素のみにする。
					this.ensTreeView.collection.add(childNodes.slice(0, 1));
					var args = this.ensTreeView.start_batch();
					this.ensTreeView.collection.add(childNodes.slice(1,
							childNodes.length - 1));
					this.ensTreeView.end_batch(args);
					this.ensTreeView.collection.add(childNodes.slice(
							childNodes.length - 1, childNodes.length));
				} else {
					this.ensTreeView.collection.add(childNodes);
				}
			},
			/**
			 * 直下の子要素を削除する。
			 * 
			 * @param parentNodeId
			 *            親ノードのID
			 */
			removeChildNodes : function(parentNodeId) {
				var removeOptionList = [];

				var models = this.ensTreeView.collection.models;
				var parentRegExp = new RegExp("^" + parentNodeId);
				_
						.each(
								models,
								function(model, index) {
									var type = model.get("type");
									var parentTreeId = model
											.get("parentTreeId");

									// 削除対象は、ディレクトリノードとグラフノードのみとする
									if ((type == ENS.tree.type.GROUP || type == ENS.tree.type.TARGET)
											&& parentTreeId.match(parentRegExp)) {

										var option = {
											id : model.id
										};

										removeOptionList.push(option);
									}
								});

				this.ensTreeView.collection.remove(removeOptionList);

				var elem = document.getElementById(parentNodeId);
				var parentLiTag = $(elem).parent("li");
				if (parentLiTag) {
					if (removeOptionList.length === 0) {
						var nextElem = parentLiTag.next("li");
						if (nextElem.length === 0) {
							if (this.finishOpenOrClose === true) {
								parentLiTag.attr("class",
										"jstree-last jstree-closed");
							} else {
								parentLiTag.attr("class",
										"jstree-last jstree-open");
							}
						} else {
							if (this.finishOpenOrClose === true) {
								parentLiTag.attr("class", "jstree-closed");
							} else {
								parentLiTag.attr("class", "jstree-open");
							}
						}

					} else {
						if (this.finishOpenOrClose === true) {
							parentLiTag.attr("class", "jstree-closed");
						} else {
							parentLiTag.attr("class", "jstree-open");
						}
					}
				}
			}
		});
