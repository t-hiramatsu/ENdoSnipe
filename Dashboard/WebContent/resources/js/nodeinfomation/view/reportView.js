ENS.reportView = wgp.AbstractView
		.extend({
			tableColNames : [ "No", "レポート名", "レポート対象", "開始日", "終了日", "DL" ],
			initialize : function(argument, treeSettings) {
				var appView = new ENS.AppView();
				this.treeSettings = treeSettings;
				var treeId = treeSettings.id;
				appView.addView(this, treeId + ENS.URL.PERFDOCTOR_POSTFIX_ID);

				// 空のテーブルを作成
				this.createTabelColModel();
				this.render();

				var reportName = treeSettings.parentTreeId + "/"
						+ treeSettings.data;

				this.getReportList(reportName);
			},
			render : function() {
				$("#" + this.id).append('<div id="reportDiv"></div>');
				$("#reportDiv").css({
					"margin-left" : 5
				});
				$("#reportDiv").append('<table id="reportTable"></table>');
				$("#reportDiv").append('<div id="reportPager"></table>');
				var height = "auto";

				$("#reportTable").jqGrid({
					datatype : "local",
					data : "",
					colModel : this.tableColModel,
					colNames : this.tableColNames,
					caption : "Diagnosis of " + this.treeSettings.id,
					pager : "reportPager",
					rowList : [ 10, 50, 500 ],
					pgbuttons : false,
					pginput : false,
					height : height
				});
				$("#reportTable").filterToolbar({
					defaultSearch : 'cn'
				});
				$("#reportDiv").css('font-size', '0.8em');
			},
			_parseModel : function(model) {
				var tableData = model.attributes;

				return tableData;
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
			onComplete : function(element) {
				if (element == wgp.constants.syncType.SEARCH) {
					appView.syncData();
				}
				this.reloadTable();
			},
			createTabelColModel : function() {
				this.tableColModel = [ {
					name : "reportId",
					width : 30
				}, {
					name : "reportName",
					width : 260
				}, {
					name : "targetMeasurementName",
					width : 260
				}, {
					name : "reportTermFrom",
					width : 115
				}, {
					name : "reportTermTo",
					width : 115
				}, {
					name : "download",
					width : 60,
					formatter : ENS.Utility.makeAnchor,
					editoptions : {
						"onclick" : "ENS.report.download",
						"linkName" : "DL"
					}
				} ];
			},
			reloadTable : function() {
				var tableViewData = [];
				var instance = this;
				_.each(this.collection.models, function(model, index) {
					tableViewData.push(instance._parseModel(model));
				});

				$("#reportTable").clearGridData().setGridParam({
					data : tableViewData
				}).trigger("reloadGrid");
			},
			getReportList : function(reportName) {

				// レポート出力定義を取得する
				// Ajax通信用の設定
				var settings = {
					data : {
						reportName : reportName
					},
					url : ENS.tree.REPORT_SELECT_BY_REPORT_NAME_URL
				};

				// 非同期通信でデータを送信する
				var ajaxHandler = new wgp.AjaxHandler();
				settings[wgp.ConnectionConstants.SUCCESS_CALL_OBJECT_KEY] = this;
				settings[wgp.ConnectionConstants.SUCCESS_CALL_FUNCTION_KEY] = "callbackGetReportList";
				ajaxHandler.requestServerAsync(settings);
			},
			callbackGetReportList : function(reportList) {
				var tableViewData = [];
				var instance = this;
				_.each(reportList, function(report, index) {
					var fmTimeDate = new Date(report.reportTermFrom);
					var toTimeDate = new Date(report.reportTermTo);

					var fmTimeStr = instance.getDateString(fmTimeDate);
					var toTimeStr = instance.getDateString(toTimeDate);

					report.reportTermFrom = fmTimeStr;
					report.reportTermTo = toTimeStr;

					tableViewData.push(report);
				});

				$("#reportTable").clearGridData().setGridParam({
					data : tableViewData
				}).trigger("reloadGrid");
			},
			getDateString : function(date) {
				// 年月日の取得
				var month = date.getMonth() + 1;
				var day = date.getDate();
				var hour = date.getHours();
				var minute = date.getMinutes();
				var second = date.getSeconds();

				// 1桁を2桁に変換する
				if (month < 10) {
					month = "0" + month;
				}
				if (day < 10) {
					day = "0" + day;
				}
				if (hour < 10) {
					hour = "0" + hour;
				}
				if (minute < 10) {
					minute = "0" + minute;
				}
				if (second < 10) {
					second = "0" + second;
				}

				// 整形して返却
				return date.getFullYear() + "-" + month + "-" + day + " "
						+ hour + ":" + minute + ":" + second;
			}
		});

ENS.report.download = function(id) {
	var event = {
		"file_name" : "20130512_133200-20130514_133200.zip"
	};
	
	$("input#reportId").val(id);
	$('#btn').click();/*function() {
		$("#reportId").val(id);
		var formElem = $("form#reportDownload")[0];
		formElem.submit();
    });*/
};

ENS.report.callbackDownload = function() {

};