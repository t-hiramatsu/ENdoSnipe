ENS.schedulingReportDialog.rowId = {};

ENS.schedulingReportView = wgp.AbstractView
		.extend({
			tableColNames : [ "Id", "Report Name", "Report Target", "Schedule",
					"Time", "Day", "Date", "" ],
			initialize : function(argument, treeSettings) {
				this.createTabelColModel();
				var appView = new ENS.AppView();
				this.treeSettings = treeSettings;
				var treeId = treeSettings.id;
				appView.addView(this, treeId + ENS.URL.PERFDOCTOR_POSTFIX_ID);

				// 空のテーブルを作成
				var dualSliderId = this.id + "_dualSlider";
				// dual slider area (add div and css, and make slider)
				$("#" + this.id)
						.append('<div id="' + dualSliderId + '"></div>');
				$('#' + dualSliderId).css(
						ENS.nodeinfo.parent.css.dualSliderArea);
				$('#' + dualSliderId).css(
						ENS.nodeinfo.parent.css.dualSliderArea);
				this.dualSliderView = new ENS.DualSliderView({
					id : dualSliderId,
					rootView : this
				});
				this.getAllScheReportList_();
				this.render();

			},
			render : function() {
				$("#" + this.id).append('<div id="reportDiv"></div>');
				$("#reportDiv").css({
					"margin-top" : 20,
					"margin-left" : 15
				});
				$("#reportDiv").append('<table id="scheduleReportTable"></table>');
				$("#reportDiv").append('<div id="reportPager"></table>');
				var height = "auto";

				$("#scheduleReportTable").jqGrid(
						{
							datatype : "local",
							data : "",
							colModel : this.tableColModel,
							colNames : this.tableColNames,
							caption : "Report List of "
									+ this.treeSettings.parentTreeId + "/"
									+ this.treeSettings.data,
							pager : "reportPager",
							rowList : [ 20, 50, 100 ],
							pgbuttons : true,
							pginput : true,
							height : height,
							viewrecords : true,
							rownumbers : true
						});
				$("#scheduleReportTable").filterToolbar({
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
					width : 0,
					hidden : true
				}, {
					name : "reportName",
					width : 180
				}, {
					name : "targetMeasurementName",
					width : 300
				}, {
					name : "term",
					width : 90
				}, {
					name : "time",
					width : 90
				}, {
					name : "day",
					width : 90
				}, {
					name : "date",
					width : 50
				}, {
					name : "",
					width : 80,
					formatter : ENS.Utility.makeAnchor1,
					editoptions : {
						"onclick1" : "ENS.report.updateSchedulingReport",
						"updateLink" : "Update",
						"onclick2" : "ENS.report.deleteSchedulingReport",
						"deleteLink" : "Delete"
					}

				} ];

			},

			reloadTable : function() {
				var tableViewData = [];
				var instance = this;
				_.each(this.collection.models, function(model, index) {
					tableViewData.push(instance._parseModel(model));
				});

				$("#scheduleReportTable").clearGridData().setGridParam({
					data : tableViewData
				}).trigger("reloadGrid");
			},
			getAllScheReportList_ : function() {

				// レポート出力定義を取得する
				// Ajax通信用の設定
				var settings = {
					url : ENS.tree.SCHEDULING_REPORT_SELECT_ALL_URL
				};

				// 非同期通信でデータを送信する
				var ajaxHandler = new wgp.AjaxHandler();
				settings[wgp.ConnectionConstants.SUCCESS_CALL_OBJECT_KEY] = this;
				settings[wgp.ConnectionConstants.SUCCESS_CALL_FUNCTION_KEY] = "callbackGetAllScheReportList_";
				ajaxHandler.requestServerAsync(settings);
			},
			callbackGetAllScheReportList_ : function(reportList) {
				var tableViewData = [];
				var instance = this;
				_.each(reportList, function(report, index) {
					// レポート名についている余計なパスを除外する
					var reportPath = report.reportName;
					var reportPathSplitList = reportPath.split("/");
					var reportPathLength = reportPathSplitList.length;
					var reportName = reportPathSplitList[reportPathLength - 1];
					report.reportName = reportName;

					// 開始日と終了日のフォーマットを変更する
					var timeDate = new Date(report.time);
					var timeStr = instance.getDateString(timeDate);
					report.time = timeStr;

					tableViewData.push(report);
				});

				$("#scheduleReportTable").clearGridData().setGridParam({
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
					url : ENS.tree.REPORT_SELECT_BY_SCHEDULING_URL
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
					// レポート名についている余計なパスを除外する
					var reportPath = report.reportName;
					var reportPathSplitList = reportPath.split("/");
					var reportPathLength = reportPathSplitList.length;
					var reportName = reportPathSplitList[reportPathLength - 1];
					report.reportName = reportName;

					// 開始日と終了日のフォーマットを変更する
					var timeDate = new Date(report.time);
					var timeStr = instance.getDateString(timeDate);
					report.time = timeStr;

					tableViewData.push(report);
				});

				$("#scheduleReportTable").clearGridData().setGridParam({
					data : tableViewData
				}).trigger("reloadGrid");
			},
			getDateString : function(time) {
				var hour = time.getHours();
				var minute = time.getMinutes();

				if (hour < 10) {
					hour = "0" + hour;
				}
				if (minute < 10) {
					minute = "0" + minute;
				}

				return hour + ":" + minute;
			}

		});

ENS.report.updateSchedulingReport = function(rowId) {

	var rowData = $("#scheduleReportTable").jqGrid('getRowData', rowId);
	ENS.schedulingReportDialog.rowId = rowId;
	var term = rowData.term;
	var timeSplit = rowData.time.split(":");

	$("#schedulingReportName").val(rowData.reportName);
	$("#schedulingReportTargetName").val(rowData.targetMeasurementName);
	// シグナル名の表示名称は自身より親のツリー構造を除外した値を指定する。

	var viewClassName = new ENS.SchedulingReportDialogView({
		dialogId : ENS.tree.REPORT_SCHEDULE_DIALOG,
		signalType : ENS.tree.EDIT_SCHEDULE_TYPE
	});

	if (term == "WEEKLY") {

		$("#schedulingReportWeekly").attr('checked', 'checked');
		$("#schedulingReportWeekly").change();
		$('#schedulingReportSelectedWeeklyId option').each(function() {
			if (this.value == rowData.day) {
				$(this).prop("selected", "selected");
				return;
			}
		});

	} else if (term == "MONTHLY") {

		$("#schedulingReportMonthly").attr('checked', 'checked');
		$("#schedulingReportMonthly").change();
		$('#schedulingReportSelectedMonthlyId option').each(function() {
			if (this.value == rowData.date) {
				$(this).prop("selected", "selected");
				return;
			}
		});
	}
	$('#schedulingReportSelectedTimeId option').each(function() {
		if (this.value == timeSplit[0]) {
			$(this).prop("selected", "selected");
			return;
		}
	});

	$('#schedulingReportSelectedTimeMinuteId option').each(function() {
		if (this.value == timeSplit[1]) {
			$(this).prop("selected", "selected");
			return;
		}
	});

};
ENS.report.deleteSchedulingReport = function(rowId) {
	var grid = $("#scheduleReportTable");
	var rowData = $("#scheduleReportTable").jqGrid('getRowData', rowId);
	grid.delRowData(rowId);
	var settings = {
		
		data : {
			reportId : rowData.reportId
		},
		url : ENS.tree.REPORT_SCHEDULE_DELETE_BY_ID_URL
	};
	var ajaxHandler = new wgp.AjaxHandler();
	settings[wgp.ConnectionConstants.SUCCESS_CALL_OBJECT_KEY] = this;
	ajaxHandler.requestServerAsync(settings);
};
ENS.report.callbackDownload = function(response) {
	alert(response);
};