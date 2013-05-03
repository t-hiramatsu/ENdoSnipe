// シグナルダイアログの設定項目を格納しておくためのリスト
ENS.tree.signalDefinitionList = [];

ENS.treeView = wgp.TreeView
		.extend({
			/**
			 * ツリー要素をクリックした際の、別ペインとの
			 * 連携処理を行う。
			 * targetId {String} 別ペインのID
			 */
			setClickEvent : function(targetId) {
				var instance = this;
				this.treeCollection = {};
				this.maxId = 0;
				this.targetId = targetId;
				$("#" + this.$el.attr("id")).mousedown(function(event) {
					var whichValue = event.which;
					if (whichValue != 1) {
						return;
					}
					var target = event.target;
					if ("A" == target.tagName) {
						var treeId = $(target).attr("id");
						var treeModel = instance.collection.get(treeId);
						instance.clickModel(treeModel);
					}
				});
			},
			/**
			 * クリックされたツリー要素のモデルに応じて処理を行う。
			 * treeModel {Backbone.Model}
			 */
			clickModel : function(treeModel) {
				if (this.childView) {
					var tmpAppView = new wgp.AppView();
					tmpAppView.removeView(this.childView);
					this.childView = null;
				}
				$("#" + this.targetId).children().remove();
				var targetContentId = this.targetId + "_content";
				$("#" + this.targetId).append(
						"<div id ='" + targetContentId + "'></div>");

				var dataId = treeModel.get("id");

				var viewSettings = null;
				$.each(wgp.constants.VIEW_SETTINGS, function(index, value) {
					if (dataId.match(index)) {
						viewSettings = value;
						return false;
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
					id : targetContentId
				});
				var treeSettings = treeModel.attributes;
				// 動的に生成するオブジェクトを切り替える必要があるため、やむを得ずeval()を使う
				this.childView = eval("new " + viewClassName
						+ "(viewSettings, treeSettings)");
			},
			render : function(renderType, treeModel) {
				if (renderType == wgp.constants.RENDER_TYPE.ADD) {
					var parentTreeId = treeModel.get("parentTreeId");
					var idAttribute = treeModel.idAttribute;
					var targetTag;
					if (parentTreeId !== null && parentTreeId !== undefined) {
						targetTag = this.getTreeNode(parentTreeId, idAttribute);
					}

					$("#" + this.$el.attr("id")).jstree("create_node",
							$(targetTag), "last",
							this.createTreeData(treeModel));
				} else {
					wgp.TreeView.prototype.render.call(this, renderType,
							treeModel);
				}
			},
			renderAll : function() {
				// View jsTree
				var settings = this.treeOption;
				settings = $.extend(true, settings, {
					json_data : {
						data : this.createJSONData()
					}
				});
				$("#" + this.$el.attr("id")).jstree(settings);

				// シグナル定義を全取得する
				this.getAllSignal_();
			},
			createTreeData : function(treeModel) {
				var returnData = wgp.TreeView.prototype.createTreeData.call(
						this, treeModel);
				var titleData = returnData.data;
				if (titleData.title.indexOf("&#47;") >= 0) {
					titleData.title = titleData.title.split("&#47;").join("/");
				}
				if (treeModel.get("icon")) {
					returnData.data.icon = treeModel.get("icon");
				}
				return returnData;
			},
			addContextMenu : function(contextOption) {
				// contextOptionの中身
				// "menu_id" : 表示するコンテキストメニュータグのID名,
				// "menu_name" : 表示するメニュー名
				// "showParam" : コンテキストメニュー表示制限,
				// "executeClass" : メニュー選択時の実行クラス。
				// "children" : 子要素のコンテキストオプション
				var instance = this;
				this.contextCollection = new Backbone.Collection();
				this.addContextCollection_(contextOption);
				// コンテキストメニュー用のタグを生成する。
				var menuId = this.$el.attr("id") + "_contextManu";
				var tmpClickTarget = null;
				var settingOptions = {
					onShow : function(event, target) {
						var clickTarget = event.target;
						var tagName = clickTarget.tagName;
						if (tagName != "A") {
							return false;
						}
						var clickTargetId = $(clickTarget).attr("id");
						instance.checkShowContext_(clickTargetId);
						tmpClickTarget = clickTarget;
					},
					onSelect : function(event, target) {
						var clickTarget = event.target;
						var id = $(clickTarget).attr("id");
						var targetOption = instance.getContextOption_(id);
						if (targetOption == null) {
							return;
						}
						var dialogId = targetOption.get("executeOption").dialogId;

						if (dialogId == ENS.tree.SIGNAL_DIALOG) {
							instance.signalOnSelect_(event, target, menuId, tmpClickTarget);
						} else if (dialogId == ENS.tree.REPORT_DIALOG) {
							instance.reportOnSelect_(event, target, menuId, tmpClickTarget);
						}
					}
				};
				contextMenuCreator.initializeContextMenu(menuId, contextOption);
				contextMenuCreator.createContextMenu(this.$el.attr("id"),
						menuId, settingOptions);
			},
			pushOkFunction : function(event, option) {

			},
			pushCancelFunction : function(event, option) {

			},
			onComplete : function(type) {
				if (type == wgp.constants.syncType.SEARCH) {
					this.renderAll();
				} else {
					this.updateSignal_(this.collection.models);
				}
			},
			signalOnSelect_ : function(event, target, menuId, tmpClickTarget) {
				var clickTarget = event.target;
				var id = $(clickTarget).attr("id");
				var targetOption = this.getContextOption_(id);

				var executeOption = targetOption.get("executeOption");
				executeOption.treeId = $(tmpClickTarget).attr("id");
				executeOption.displayName = $(tmpClickTarget).text();

				// 削除が選択された場合はそのノードを削除する
				if (id == ENS.tree.DELETE_SIGNAL_TYPE) {
					if (confirm("Are you sure you want to delete it?")) {
						this.deleteSignal_(executeOption);
					}
					return;
				}

				// 開くViewのクラス
				var executeClass = targetOption.get("executeClass");
				if (executeClass) {
					if (id == ENS.tree.ADD_SIGNAL_TYPE) {
						// Matching Patternにデフォルトのツリー階層を入力する
						$("#matchingPattern").val(executeOption.treeId);
					} else if (id == ENS.tree.EDIT_SIGNAL_TYPE) {
						var signalDefinition = ENS.tree.signalDefinitionList[executeOption.treeId];
						this.inputSignalDialog_(signalDefinition);
					}

					// set execute class and function, if push ok
					// button.
					executeOption.okObject = this;
					executeOption.okFunctionName = "signalPushOkFunction";
					executeOption.cancelObject = this;
					executeOption.cancelFunctionName = "pushCancelFunction";
					eval("new " + executeClass + "(executeOption)");
				}
			},
			reportOnSelect_ : function(event, target, menuId, tmpClickTarget) {
				var clickTarget = event.target;
				var id = $(clickTarget).attr("id");
				var targetOption = this.getContextOption_(id);
				if (targetOption == null) {
					return;
				}

				var executeOption = targetOption.get("executeOption");
				executeOption.treeId = $(tmpClickTarget).attr("id");
				executeOption.displayName = $(tmpClickTarget).text();

				// 削除が選択された場合はそのノードを削除する
				if (id == ENS.tree.DELETE_SIGNAL_TYPE) {
					if (confirm("Are you sure you want to delete it?")) {
						this.deleteSignal_(executeOption);
					}
					return;
				}

				// 開くViewのクラス
				var executeClass = targetOption.get("executeClass");
				if (executeClass) {
					if (id == ENS.tree.OUTPUT_REPORT_TYPE) {
						// Target Measurement Nameにツリー階層を入力する
						$("#targetName").val(executeOption.treeId);
					}

					// set execute class and function, if push ok
					// button.
					executeOption.okObject = this;
					executeOption.okFunctionName = "reportPushOkFunction";
					executeOption.cancelObject = this;
					executeOption.cancelFunctionName = "pushCancelFunction";
					eval("new " + executeClass + "(executeOption)");
				}
			},
			/** 閾値判定の定義を入力した後に実行するメソッド。 */
			signalPushOkFunction : function(event, option) {
				// リアルタイム通信を止める
				var idList = _.keys(ENS.tree.signalDefinitionList);
				var appView = new ENS.AppView();
				appView.stopSyncData(idList);

				// add tree data for signal
				var treeId = option.treeId;
				var signalName = $("#signalName").val();
				var signalFullName = treeId + "/" + signalName;

				// Ajax通信のコールバック関数名
				var callbackFunction = "";
				// Ajax通信の送信先URL
				var url = "";
				// サーバに送信するデータ
				var sendData;
				// Ajax通信のコールバック関数と送信先URLをシグナルタイプによって決める
				if (option.signalType == ENS.tree.ADD_SIGNAL_TYPE) {
					sendData = this.createSendAddData_(signalFullName);
					callbackFunction = "callbackAddSignal_";
					url = ENS.tree.SIGNAL_ADD_URL;
				} else if (option.signalType == ENS.tree.EDIT_SIGNAL_TYPE) {
					sendData = this.createSendEditData_(signalFullName,
							signalName);
					callbackFunction = "callbackEditSignal_";
					url = ENS.tree.SIGNAL_EDIT_URL;
				}

				// Ajax通信用の設定
				var settings = {
					data : sendData,
					url : url
				};

				// 非同期通信でデータを送信する
				var ajaxHandler = new wgp.AjaxHandler();
				settings[wgp.ConnectionConstants.SUCCESS_CALL_OBJECT_KEY] = this;
				settings[wgp.ConnectionConstants.SUCCESS_CALL_FUNCTION_KEY] = callbackFunction;
				ajaxHandler.requestServerAsync(settings);

				this.clearSignalDialog_();
			},
			/** レポート出力の定義を入力した後に実行するメソッド。 */
			reportPushOkFunction : function(event, option) {

			},
			updateSignal_ : function(signalDefinitionList) {
				var instance = this;
				var removeOptionList = [];
				var addOptionList = [];
				var tmpList = [];
				_
						.each(
								signalDefinitionList,
								function(signalDefinition, signalIndex) {
									var signalName = signalDefinition.signalName;
									var signalNameSplitList = signalName
											.split("/");
									var signalNameSplitListLength = signalNameSplitList.length;

									// 新しいID
									var newTreeId = "";
									// 親ノードのパス
									var parentTreeId = "";
									for ( var index = 1; index < signalNameSplitListLength; index++) {
										var nameSplit = signalNameSplitList[index];

										if (index == signalNameSplitListLength - 1) {
											newTreeId += ENS.tree.SIGNAL_PREFIX_ID;
										} else {
											parentTreeId += "/";
											parentTreeId += nameSplit;

											newTreeId += "/";
										}
										newTreeId += nameSplit;
									}

									var showName = signalNameSplitList[signalNameSplitListLength - 1];
									var removeOption = {
										id : signalName
									};
									removeOptionList.push(removeOption);

									// シグナルアイコンを取得する
									var icon = instance
											.getIcon(signalDefinition);

									var addOption = {
										id : newTreeId,
										data : showName,
										parentTreeId : parentTreeId,
										icon : icon
									};
									addOptionList.push(addOption);

									// 更新時に、ツリー階層に他のノードがなく、ツリーが閉じてしまう現象をなくすために、
									// 一時的なノードを作成する
									var tmpTreeOption = {
										id : parentTreeId + "/tmp",
										data : "",
										parentTreeId : parentTreeId
									};
									tmpList.push(tmpTreeOption);

									ENS.tree.signalDefinitionList[newTreeId] = signalDefinition;
								});

				// 更新時に、ツリー階層に他のノードがなく、ツリーが閉じてしまう現象をなくすために
				// 最初に一時的なノードを追加している
				this.collection.add(tmpList);
				this.collection.remove(removeOptionList);
				this.collection.add(addOptionList);
				this.collection.remove(tmpList);

			},
			createSendAddData_ : function(signalFullName) {
				// シグナル定義を作成する
				var signalDefinition = {
					signalId : $("#signalId").val(),
					signalName : signalFullName,
					matchingPattern : $("#matchingPattern").val(),
					level : $("#signalPatternValue select").val(),
					patternValue : this.createPatternValue_(),
					escalationPeriod : $("#escalationPeriod").val()
				};

				var sendData = {
					signalDefinition : JSON.stringify(signalDefinition)
				};

				return sendData;
			},
			createSendEditData_ : function(signalFullName, signalNodeName) {
				// 親ノードのパス
				var targetTreeId = "";
				// 編集前のノードID（削除対象）
				var deleteNodeId = "";

				var nameSplitList = signalFullName.split("/");
				var nameSplitListLength = nameSplitList.length;
				// 親ノードへのパスと、新規ノードのパスを作成する
				for ( var index = 1; index < nameSplitListLength; index++) {
					// 編集前のノードはパスする
					if (index == nameSplitListLength - 2) {
						deleteNodeId = nameSplitList[index];
						continue;
					}
					var nameSplit = nameSplitList[index];

					if (index != nameSplitListLength - 1) {
						targetTreeId += "/";
						targetTreeId += nameSplit;
					}
				}
				// 編集前のノードID（削除対象）
				var deleteTreeId = targetTreeId + "/" + deleteNodeId;
				// 編集後のノードID（更新対象）
				var updateTreeId = targetTreeId + "/" + signalNodeName;

				// シグナル定義を作成する
				var signalDefinition = {
					signalId : $("#signalId").val(),
					signalName : updateTreeId,
					matchingPattern : $("#matchingPattern").val(),
					level : $("#signalPatternValue select").val(),
					patternValue : this.createPatternValue_(),
					escalationPeriod : $("#escalationPeriod").val()
				};

				var sendData = {
					signalDefinition : JSON.stringify(signalDefinition),
					oldSignalId : deleteTreeId
				};

				return sendData;
			},
			createPatternValue_ : function() {
				var level = $("#signalPatternValue select").val();
				// 各レベルの閾値をカンマ区切りの文字列としてサーバに送る
				var patternValue = "";
				for ( var num = 1; num <= level; num++) {
					patternValue += $("#patternValue_" + num).val();
					if (num != level) {
						patternValue += ",";
					}
				}

				return patternValue;
			},
			checkShowContext_ : function(id) {
				$.each(this.contextCollection.models, function(index, value) {
					var menuId = value.get("menu_id");
					$("#" + menuId).show();
					var showParam = value.get("showParam");
					if (!showParam) {
						return true;
					}
					var reg = new RegExp(showParam, "i");
					if (!id.match(reg)) {
						$("#" + menuId).hide();
					}
				});
			},
			addContextCollection_ : function(contextOption) {
				var instance = this;
				jQuery.each(contextOption, function(index, target) {
					instance.contextCollection.push(target);
					var children = target.children;
					if (children != null && children.length !== 0) {
						instance.addContextCollection_(children);
					}
				});
			},
			getContextOption_ : function(id) {
				var optionList = this.contextCollection.where({
					menu_id : id
				});
				if (optionList == null) {
					return;
				}
				if (optionList.length === 0) {
					return;
				}
				return optionList[0];
			},
			deleteSignal_ : function(executeOption) {
				// リアルタイム通信を止める
				var idList = _.keys(ENS.tree.signalDefinitionList);
				var appView = new ENS.AppView();
				appView.stopSyncData(idList);

				// 削除するノードのデータ
				var deleteSignalDefinition = ENS.tree.signalDefinitionList[executeOption.treeId];

				// Ajax通信の送信先URL
				var callbackFunction = "callbackDeleteSignal_";
				var url = ENS.tree.SIGNAL_DELETE_URL;

				// Ajax通信用の設定
				var settings = {
					data : {
						signalDefinition : JSON
								.stringify(deleteSignalDefinition)
					},
					url : url
				};

				// 非同期通信でデータを送信する
				var ajaxHandler = new wgp.AjaxHandler();
				settings[wgp.ConnectionConstants.SUCCESS_CALL_OBJECT_KEY] = this;
				settings[wgp.ConnectionConstants.SUCCESS_CALL_FUNCTION_KEY] = callbackFunction;
				ajaxHandler.requestServerAsync(settings);
			},
			getAllSignal_ : function() {
				// シグナル定義を取得する
				// Ajax通信用の設定
				var settings = {
					url : ENS.tree.SIGNAL_SELECT_ALL_URL
				};

				// 非同期通信でデータを送信する
				var ajaxHandler = new wgp.AjaxHandler();
				settings[wgp.ConnectionConstants.SUCCESS_CALL_OBJECT_KEY] = this;
				settings[wgp.ConnectionConstants.SUCCESS_CALL_FUNCTION_KEY] = "callbackGetAllSignal_";
				ajaxHandler.requestServerAsync(settings);
			},
			callbackGetAllSignal_ : function(signalDefinitionList) {
				// ツリーのシグナルを更新する
				this.updateSignal_(signalDefinitionList);

				// 各シグナルに対してリアルタイム更新を行う
				var idList = _.keys(ENS.tree.signalDefinitionList);
				var appView = new ENS.AppView();
				appView.syncData(idList);
			},
			callbackAddSignal_ : function(signalDefinition) {
				var signalId = signalDefinition.signalId;
				if (signalId === 0) {
					alert("This signalName is already exist."
							+ "\nPlease change signal name and try again.");
					return;
				}
				var signalName = signalDefinition.signalName;

				var nameSplitList = signalName.split("/");
				var nameSplitListLength = nameSplitList.length;

				var showName = nameSplitList[nameSplitListLength - 1];
				// 親ノードのパス
				var targetTreeId = "";
				// 新規ノードのパス
				var signalTreeId = "";
				// 親ノードへのパスと、新規ノードのパスを作成する
				for ( var index = 1; index < nameSplitListLength; index++) {
					var nameSplit = nameSplitList[index];

					if (index == nameSplitListLength - 1) {
						signalTreeId += ENS.tree.SIGNAL_PREFIX_ID;
					} else {
						targetTreeId += "/";
						targetTreeId += nameSplit;

						signalTreeId += "/";
					}
					signalTreeId += nameSplit;
				}

				// クライアント側にデータを保持する
				ENS.tree.signalDefinitionList[signalTreeId] = signalDefinition;

				// シグナルアイコンを取得する
				var icon = this.getIcon(signalDefinition);

				var treeOption = {
					id : signalTreeId,
					data : showName,
					parentTreeId : targetTreeId,
					icon : icon
				};
				this.collection.add([ treeOption ]);

				// 新しいシグナルに対してリアルタイム更新を行う
				var idList = _.keys(ENS.tree.signalDefinitionList);
				var appView = new ENS.AppView();
				appView.syncData(idList);
			},
			callbackEditSignal_ : function(data) {
				var signalDefinition = data.signalDefinition;
				var signalName = signalDefinition.signalName;

				var nameSplitList = signalName.split("/");
				var nameSplitListLength = nameSplitList.length;

				var showName = nameSplitList[nameSplitListLength - 1];
				// 親ノードのパス
				var targetTreeId = "";
				// 新規ノードのパス
				var signalTreeId = "";
				// 親ノードへのパスと、新規ノードのパスを作成する
				for ( var index = 1; index < nameSplitListLength; index++) {
					var nameSplit = nameSplitList[index];

					if (index == nameSplitListLength - 1) {
						signalTreeId += ENS.tree.SIGNAL_PREFIX_ID;
					} else {
						targetTreeId += "/";
						targetTreeId += nameSplit;

						signalTreeId += "/";
					}
					signalTreeId += nameSplit;
				}
				// 編集前のノードID（削除対象）
				var oldSignalId = data.oldSignalId;

				// クライアント側にデータを保持する
				ENS.tree.signalDefinitionList[signalTreeId] = signalDefinition;

				// シグナルアイコンを取得する
				var icon = this.getIcon(signalDefinition);

				// 更新時に、ツリー階層に他のノードがなく、ツリーが閉じてしまう現象をなくすために、
				// 一時的なノードを作成する
				var tmpTreeOption = {
					id : targetTreeId + "/tmp",
					data : "",
					parentTreeId : targetTreeId
				};
				this.collection.add([ tmpTreeOption ]);

				// 削除するノードのオプション
				var deleteTreeOption = {
					id : oldSignalId
				};
				this.collection.remove([ deleteTreeOption ]);

				// 追加するノードのオプション
				var treeOption = {
					id : signalTreeId,
					data : showName,
					parentTreeId : targetTreeId,
					icon : icon
				};
				this.collection.add([ treeOption ]);

				// 一時的なノードを削除する
				this.collection.remove([ tmpTreeOption ]);

				// 新しいシグナルに対してリアルタイム更新を行う
				var idList = _.keys(ENS.tree.signalDefinitionList);
				var appView = new ENS.AppView();
				appView.syncData(idList);
			},
			callbackDeleteSignal_ : function(data) {
				var signalName = data.signalName;

				var nameSplitList = signalName.split("/");
				var nameSplitListLength = nameSplitList.length;

				var showName = nameSplitList[nameSplitListLength - 1];
				// 親ノードのパス
				var targetTreeId = "";
				for ( var index = 1; index < nameSplitListLength; index++) {
					var nameSplit = nameSplitList[index];

					if (index != nameSplitListLength - 1) {
						targetTreeId += "/";
						targetTreeId += nameSplit;
					}
				}

				// クライアント側のデータを空にする
				ENS.tree.signalDefinitionList[signalName] = null;

				// 削除するノードのオプション
				var deleteTreeOption = {
					id : targetTreeId + ENS.tree.SIGNAL_PREFIX_ID + showName,
					data : showName,
					parentTreeId : targetTreeId,
					icon : ENS.tree.SIGNAL_ICON
				};
				this.collection.remove([ deleteTreeOption ]);

				// 新しいシグナルに対してリアルタイム更新を行う
				var idList = _.keys(ENS.tree.signalDefinitionList);
				var appView = new ENS.AppView();
				appView.syncData(idList);
			},
			/**
			 * シグナルダイアログをクリアする。
			 */
			clearSignalDialog_ : function() {
				$("#signalId").val("");
				$("#signalName").val("new Signal");
				$("#matchingPattern").val("");
				$("#signalPatternValue select").val("3");
				$("#patternValue_1").val("");
				$("#patternValue_2").val("");
				$("#patternValue_3").val("");
				$("#patternValue_4").val("");
				$("#patternValue_5").val("");
				$("#escalationPeriod").val("");
			},
			inputSignalDialog_ : function(signalDefinition) {
				var treeIdList = signalDefinition.signalName.split("/");
				var signalName = treeIdList[treeIdList.length - 1];

				var patternValueList = signalDefinition.patternValue;
				var patternValueSplit = patternValueList.split(",");

				// レベル別の閾値を入力する
				_.each(patternValueSplit, function(data, index) {
					var patternValue = patternValueSplit[index];
					$("#patternValue_" + (index + 1)).val(patternValue);
				});

				// 各入力項目を入力する
				$("#signalId").val(signalDefinition.signalId);
				$("#signalName").val(signalName);
				$("#matchingPattern").val(signalDefinition.matchingPattern);
				$("#signalPatternValue select").val(signalDefinition.level);
				$("#escalationPeriod").val(signalDefinition.escalationPeriod);
			},
			getIcon : function(signalDefinition) {
				var icon = "";
				var level = signalDefinition.level;
				var signalValue = signalDefinition.signalValue;

				if (signalValue === undefined) {
					return ENS.tree.SIGNAL_ICON_0;
				}

				if (level == 3) {
					if (signalValue == 1) {
						icon = ENS.tree.SIGNAL_ICON_3;
					} else if (signalValue == 2) {
						icon = ENS.tree.SIGNAL_ICON_4;
					} else if (signalValue == 3) {
						icon = ENS.tree.SIGNAL_ICON_5;
					} else {
						icon = ENS.tree.SIGNAL_ICON_0;
					}
				} else if (level == 5) {
					if (signalValue == 1) {
						icon = ENS.tree.SIGNAL_ICON_1;
					} else if (signalValue == 2) {
						icon = ENS.tree.SIGNAL_ICON_2;
					} else if (signalValue == 3) {
						icon = ENS.tree.SIGNAL_ICON_3;
					} else if (signalValue == 4) {
						icon = ENS.tree.SIGNAL_ICON_4;
					} else if (signalValue == 5) {
						icon = ENS.tree.SIGNAL_ICON_5;
					} else {
						icon = ENS.tree.SIGNAL_ICON_0;
					}
				} else {
					icon = ENS.tree.SIGNAL_ICON_0;
				}

				return icon;
			},
			/**
			 * ツリーの表示状態を復元する。
			 * selectTreeId {String} 選択しているツリーのID
			 * openNodes {String[]} 展開しているツリーのID配列
			 */
			restoreDisplayState : function(selectTreeId, openNodes){

				// ツリーの展開状態を復元する。
				if(openNodes && openNodes.length > 0){
					this.openNodes(openNodes, "id");
				}

				// ツリーの選択状態を復元する。
				if(selectTreeId){
					this.selectNode(selectTreeId, "id");
				}
			}
		});