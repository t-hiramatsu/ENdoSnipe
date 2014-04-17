ENS.multipleResourceGraphDialog = {};
ENS.multipleResourceGraphDialog.isFirst = true;
ENS.tree.doRender = true;

ENS.MultipleResourceGraphDefinitionDialogView = ENS.DialogView
		.extend({
			initialize : function(option) {
				var ins = this;
				this.op_ = option;
				this.id = option.dialogId;
				this.signalType = option.signalType;
				var treeId = option.treeId;
				this.measurementDefinitionList = [];
				this.noOfPage = 0;
				this.graphPerPage = 15;
				var okName = "okFunctionName";
				var okObj = "okObject";
				var cName = "cancelFunctionName";
				var cObj = "cancelObject";

				this.getAllMeasurement_();

				$("#" + option.dialogId)
						.dialog(
								{

									buttons : [
											{
												text : "OK",
												click : function(event) {

													// multipleResourceGraphNameが空の時はアラートを表示し、再入力を求める。
													var multipleResourceGraphName =
														$("#multipleResourceGraphName").val();
													if (multipleResourceGraphName === "") {
														alert("Please input 'Summary Graph Name'.");
														return;
													} else if (multipleResourceGraphName
															.match(/[\\\/]/)) {
														alert("Don't use '/'or'\\' in 'Summary Graph Name'.");
														return;
													}

													if ($('#multipleResourceGraphSelection').attr("checked")) {
														var selectedOpts = $('#multipleResourceGraphLstBox2 option');
														if(selectedOpts.length === 0){
															alert("Please select 'Multiple Resource Graph Items'.");
															return;
														}
													}

													if ($('#multipleResourceGraphRegExpression').attr("checked")) {
														if ($("#multipleResourceGraphItems").val() === "") {
															alert("Please input 'Pattern of Multiple Resource Graph Items'.");
															return;
														}
													}

													if (!ins.op_[okObj]) {
														$("#" + option.dialogId)
																.dialog("close");
														return;
													}
													if (!ins.op_[okObj][ins.op_[okName]]) {
														$("#" + option.dialogId)
																.dialog("close");
														return;
													}
													ins.op_[okObj][ins.op_[okName]]
															(event, ins.op_);

													$("#" + option.dialogId)
															.dialog("close");
												}
											},
											{
												text : "Cancel",
												click : function(event) {
													$("#" + option.dialogId)
															.dialog("close");
													if (!ins.op_[cObj]) {
														return;
													}
													if (!ins.op_[cObj][ins.op_[cName]]) {
														return;
													}
													ins.op_[cObj][ins.op_[cName]]
															(event, ins.op_);
												}
											} ],
									close : function(event) {
										if (!ins.op_[cObj]) {
											return;
										}
										if (!ins.op_[cObj][ins.op_[cName]]) {
											return;
										}
										ins.op_[cObj][ins.op_[cName]](event,
												ins.op_);
									},
									modal : true,
									width : 800

								});
				$('#multipleResourceGraphLstBox1').removeAttr("disabled");
				$('#multipleResourceGraphLstBox2').removeAttr("disabled");
				$('#multipleResourceGraphSelection').removeAttr("disabled");
				$("#multipleResourceGraphSelection").attr('checked', 'checked');
				$('#multipleResourceGraphRegExpression').removeAttr("disabled");
				$(".selectPlace").css('display', 'block');
				$(".regPlace").css('display', 'block');
				if (this.signalType == ENS.tree.ADD_MULTIPLE_RESOURCE_GRAPH_TYPE) {
					$('#multipleResourceGraphItems').val(treeId + "/.*");
					$('#multipleResourceGraphItems').attr("disabled", true);

				} else {

					// グラフ系列直接指定時
					if ($("#multipleResourceGraphItems").val() === "") {
						$('#multipleResourceGraphSelection').attr("checked", true);
						this.changeGraphSelection();

					// 正規表現指定時
					} else {
						$('#multipleResourceGraphRegExpression').attr("checked", true);
						this.changeRegExpression();
					}
				}
				if (ENS.multipleResourceGraphDialog.isFirst === true) {

					$(
							"#multipleResourceGraphSelection, #multipleResourceGraphRegExpression")
							.change(
									function(event) {
										if ($('#multipleResourceGraphSelection').attr("checked")) {
											ins.changeGraphSelection();

										}else if ($('#multipleResourceGraphRegExpression').attr("checked")) {
											ins.changeRegExpression();
										}
									});

					$('#btnAddGraph')
							.click(
									function(e) {

										var selectedOpts = $('#multipleResourceGraphLstBox1 option:selected');
										if (selectedOpts.length === 0) {
											alert("Nothing to move.");
											e.preventDefault();
										}
										$('#multipleResourceGraphLstBox2')
												.append($(selectedOpts).clone());
										$(selectedOpts).hide();
										e.preventDefault();
									});

					$('#btnRemoveGraph')
							.click(
									function(e) {
										var selectedOpts = $('#multipleResourceGraphLstBox2 option:selected');
										if (selectedOpts.length === 0) {
											alert("Nothing to move.");
											e.preventDefault();
										}

										$(selectedOpts).remove();
										_.each(selectedOpts, function(selectedOpt, index){
											$("#multipleResourceGraphLstBox1 > option[value='" + selectedOpt.value + "']").show();
										});

										e.preventDefault();
									});

					ENS.multipleResourceGraphDialog.isFirst = false;
				}
			},
			changeGraphSelection : function(){
				// グラフ系列選択時の挙動
				$('#multipleResourceGraphItems').attr("disabled", true);
				$('#multipleResourceGraphLstBox1').removeAttr("disabled");
				$('#multipleResourceGraphLstBox2').removeAttr("disabled");
			},
			changeRegExpression : function(){
				// 正規表現選択時の挙動
				$('#multipleResourceGraphItems').removeAttr("disabled");
				$('#multipleResourceGraphLstBox1').attr("disabled", true);
				$('#multipleResourceGraphLstBox2').attr("disabled", true);
			},
			getAllMeasurement_ : function() {
				// measurementItem List 定義を取得する
				// Ajax通信用の設定
				var settings = {
					data : {
						multipleResourceGraphItems : ""
					},
					url : ENS.tree.MEASUREMENT_ITEM_SELECT_ALL_URL
				};
				$('#hiddenIdList').empty();
				$('#multipleResourceGraphLstBox1').empty();
				// 非同期通信でデータを送信する
				var ajaxHandler = new wgp.AjaxHandler();
				settings[wgp.ConnectionConstants.SUCCESS_CALL_OBJECT_KEY] = this;
				settings[wgp.ConnectionConstants.SUCCESS_CALL_FUNCTION_KEY] = "callbackGetAllMeasurement_";
				ajaxHandler.requestServerAsync(settings);
			},
			callbackGetAllMeasurement_ : function(measurementDefinitionList) {

				var measurementDefList = $(
						"#multipleResourceGraphLstBox2>option").map(function() {
					return $(this).val();

				});
				var instance = this;
				var addOptionList = [];
				_.each(measurementDefinitionList, function(
						javelinMeasurementItem, index) {
					var measurementName = javelinMeasurementItem.itemName;
					var indexArray = $.inArray(measurementName,
							measurementDefList);

					$('#hiddenIdList').append(
						"<option value='"
							+ javelinMeasurementItem.itemName
							+ "'>"
							+ javelinMeasurementItem.itemName
							+ "</option>");
				});
				instance.pagingGraph(measurementDefinitionList);

				$(".tooltipDialogValue option").each(function(i){
					var text = $(this).html();
					$(this).html(text + "<br><span>" + text + "</span>");
					$(this).addClass("tooltip");
				});

			},
			inputMulResGraphDialog_ : function() {

				$("#multipleResourceGraphLstBox2").empty();
				var multipleResourceGraphItems = $(
						"#multipleResourceGraphItems").val();
				var settings = {
					data : {
						multipleResourceGraphItems : multipleResourceGraphItems
					},
					url : ENS.tree.MEASUREMENT_ITEM_SELECT_ALL_URL
				};

				var ajaxHandler = new wgp.AjaxHandler();
				var result = ajaxHandler.requestServerSync(settings);
				var measurementDefinitionList = JSON.parse(result);

				_.each(measurementDefinitionList, function(
						javelinMeasurementItem, index) {
					$('#multipleResourceGraphLstBox2').append(
							"<option value='" + javelinMeasurementItem.itemName
									+ "'>" + javelinMeasurementItem.itemName
									+ "</option>");
				});

				$("select option").attr("title", "");
				$("select option").each(function(i) {
					this.title = this.text;
				});

				// Attach a tooltip to select elements
				$("select").tooltip({
					content : function() {
						return $(this).attr(this.title);
					},
					items : "[alt]",
					tooltipClass : "tooltip"
				});
			},
			pagingGraph : function(measurementDefinitionList) {

				var instance = this;
				var num_entries = $('#hiddenIdList option').length;
				$("#pagingMeasurement").pagination(num_entries, {
					items_per_page : this.graphPerPage,
					num_display_entries : 8,
					current_page : 0,
					num_edge_entries : 2,
					callback : instance.pageselectCallback
				});
			},
			pageselectCallback : function(page_index, jq) {

				$('#multipleResourceGraphLstBox1').empty()

				var items_per_page = this.items_per_page;
				var offset = page_index * items_per_page;
				var new_content = [];
				_.each($('#hiddenIdList option').slice(offset, offset + items_per_page)
						,function(node ,index){
					new_content.push($(node.cloneNode(true))[0]);
				});

				// すでに選択されている系列については削除する。
				var selectedOptionValues =
					_.pluck($('#multipleResourceGraphLstBox2 option'), "value");
				_.each(new_content, function(newContentOption, index){
					var result = _.contains(selectedOptionValues, newContentOption.value);
					if(result){
						$(newContentOption).hide();
					}
					$('#multipleResourceGraphLstBox1').append(newContentOption);
				});

				return false;
			}

		});