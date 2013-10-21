ENS.tree.summarySignalDefinitionList = [];
ENS.SummarySignalDefinitionDialogView = ENS.DialogView
		.extend({
			initialize : function(option) {
				var ins = this;
				this.op_ = option;
				this.id = option.dialogId;
				this.summarySignalType = option.summarySignalType;
				var treeId = option.treeId;
				this.summarySignalDefinitionList = [];
				var okName = "okFunctionName";
				var okObj = "okObject";
				var cName = "cancelFunctionName";
				var cObj = "cancelObject";

				$("#summarySignalList1Box").empty();
				this.getAllSignalData_();

				// シグナル追加時のみシグナル名の変更を許可する。
				if (this.summarySignalType == ENS.tree.ADD_SUMMARYSIGNAL_TYPE) {
					$("#summarySignalName").prop("disabled", false);
				} else {
					//var summarySignalName = $("#summarySignalName").val();
					//alert(summarySignalName);
					//var signalName = $("#summarySignalName").val();
					//alert(signalName);
					$("#summarySignalName").prop("disabled", true);
				}
				$("#" + option.dialogId).dialog({
					buttons : [ {
						text : "OK",
						click : function(event) {

							// シグナル名が空の時はアラートを表示し、再入力を求める。
							var signalName = $("#summarySignalName").val();
							if (signalName === "") {
								alert("Please input 'Summary Signal Name'.");
								return;
							} else if (signalName.match(/[\\\/]/)) {
								alert("Don't use '/'or'\\' in 'Summary Signal Name'.");
								return;
							}

							// マッチングパターンが空の時はアラーとを表示し、再入力を求める。

							if (!ins.op_[okObj]) {
								$("#" + option.dialogId).dialog("close");
								return;
							}
							if (!ins.op_[okObj][ins.op_[okName]]) {
								$("#" + option.dialogId).dialog("close");
								return;
							}
							ins.op_[okObj][ins.op_[okName]](event, ins.op_);

							$("#" + option.dialogId).dialog("close");
						}
					}, {
						text : "Cancel",
						click : function(event) {
							$("#" + option.dialogId).dialog("close");
							if (!ins.op_[cObj]) {
								return;
							}
							if (!ins.op_[cObj][ins.op_[cName]]) {
								return;
							}
							ins.op_[cObj][ins.op_[cName]](event, ins.op_);
						}
					} ],
					close : function(event) {
						if (!ins.op_[cObj]) {
							return;
						}
						if (!ins.op_[cObj][ins.op_[cName]]) {
							return;
						}
						ins.op_[cObj][ins.op_[cName]](event, ins.op_);
					},
					modal : true,
					width : 450
				});
				var instance = this;
				$("#btnAdd").click(function(e) {
					instance.addList(e);
				});
				$("#btnRemove").click(function(e) {
					instance.removeList(e);
				});
			},
			addList : function(e) {
				var selectedOpts = $('#summarySignalList1Box option:selected');
				if (selectedOpts.length == 0) {
					alert("Nothing to move.");
					e.preventDefault();
				}
				$('#summarySignalList2Box').append($(selectedOpts).clone());
				$("select option").attr("title", "");
				$("select option").each(function(i) {
					this.title = this.value;
				});
				$(selectedOpts).remove();
				e.preventDefault();
			},
			removeList : function(e) {
				var selectedOpts = $('#summarySignalList2Box option:selected');
				if (selectedOpts.length == 0) {
					alert("Nothing to move.");
					e.preventDefault();
				}
				$('#summarySignalList1Box').append($(selectedOpts).clone());
				$(selectedOpts).remove();
				e.preventDefault();
			},
			getAllSignalData_ : function() {
				var settings = {
					url : ENS.tree.SUMMARYSIGNAL_ITEM_SELECT_ALL_URL
				};
				var ajaxHandler = new wgp.AjaxHandler();
				settings[wgp.ConnectionConstants.SUCCESS_CALL_OBJECT_KEY] = this;
				settings[wgp.ConnectionConstants.SUCCESS_CALL_FUNCTION_KEY] = "callbackGetAllSummarySignal_";
				ajaxHandler.requestServerAsync(settings);
			},
			callbackGetAllSummarySignal_ : function(summarySignalDefinitionList) {
				var instance = this;
				var addOptionList = [];
				_.each(summarySignalDefinitionList, function(
						summarySignalDefinition, index) {
					var signalName = summarySignalDefinition.signalName
					var lastIndexOf = signalName.lastIndexOf("/");
					var signalSubName = signalName.substr(lastIndexOf + 1);
					$('#summarySignalList1Box').append(
							"<option value='"
									+ summarySignalDefinition.signalName + "'>"
									+ signalSubName + "</option>");

				});
				$("select option").attr("title", "");
				$("select option").each(function(i) {
					this.title = this.value;
				});
				/*
				 * $("select option").attr("title", ""); $("select
				 * option").each(function(i) { this.title = this.text; });
				 */

				// Attach a tooltip to select elements
			}
		});