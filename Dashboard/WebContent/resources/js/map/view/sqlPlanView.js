ENS.sqlPlanView = wgp.AbstractView.extend({
	tableColNamesList : {
		"sqlStatement" : [ "sqlStatement" ],
		"sqlExecutePlan" : [ "gettingPlanTime", "executionPlan" ],
		"stackTrace" : [ "stackTrace" ]
	},
	tableColModelsList : {
		"sqlStatement" : [ {
			name : "SQL Statement"
		} ],
		"sqlExecutePlan" : [ {
			name : "Time"
		}, {
			name : "Plan"
		} ],
		"stackTrace" : [ {
			name : "Stack Trace"
		} ]
	},
	initialize : function(argument, treeSettings) {
		var appView = new ENS.AppView();
		this.treeSettings = treeSettings;
		var treeId = treeSettings.id;
		appView.addView(this, treeId);

		this.tableMargin = 20;
		this.tableWidth = $("#" + this.id).width() - this.tableMargin * 2;

		// 空のテーブルを作成
		this.render();

		this.getSqlPlan(treeId);
	},
	render : function() {
		var divId = "sqlPlanDiv";
		$("#" + this.id).append('<div id="' + divId + '"></div>');
		$("#" + divId).css({
			"margin-top" : 20,
			"margin-left" : 15
		});

		// 各テーブルを作成する
		this.createSqlStatementTabel(divId);
		this.createSqlExecutePlanTable(divId);
		this.createStackTraceTable(divId);

	},
	onAdd : function(element) {
		// Do nothing
	},
	onChange : function(element) {
		// Do nothing
	},
	onRemove : function(element) {
		// Do nothing
	},
	onComplete : function(element) {
		// Do nothing
	},
	createSqlStatementTabel : function(divId) {
		$("#" + divId).append('<table id="sqlStatementTable"></table>');

		$("#sqlStatementTable").jqGrid({
			datatype : "local",
			data : "",
			colModel : this.tableColModelsList["sqlStatement"],
			colNames : this.tableColNamesList["sqlStatement"],
			caption : "SQL Statement",
			height : "auto",
			width : this.tableWidth,
			shrinkToFit : false
		});

		$("#gbox_sqlStatementTable").css({
			"margin-bottom" : this.tableMargin,
		});
	},
	createSqlExecutePlanTable : function(divId) {
		$("#" + divId).append('<table id="sqlExecutePlanTable"></table>');
		$("#" + divId).append('<div id="sqlExecutePlanPager"></table>');

		$("#sqlExecutePlanTable").jqGrid({
			datatype : "local",
			data : "",
			colModel : this.tableColModelsList["sqlExecutePlan"],
			colNames : this.tableColNamesList["sqlExecutePlan"],
			caption : "SQL Execute Plan",
			pager : "sqlExecutePlanPager",
			rowList : [ 1, 3, 5 ],
			pgbuttons : true,
			pginput : true,
			height : "auto",
			width : this.tableWidth,
			sortname : "gettingPlanTime",
			sortorder : "desc",
			viewrecords : true,
			rownumbers : true,
			shrinkToFit : false
		});

		$("#gbox_sqlExecutePlanTable").css({
			"margin-bottom" : this.tableMargin
		});
	},
	createStackTraceTable : function(divId) {
		$("#" + divId).append('<table id="stackTraceTable"></table>');

		$("#stackTraceTable").jqGrid({
			datatype : "local",
			data : "",
			colModel : this.tableColModelsList["stackTrace"],
			colNames : this.tableColNamesList["stackTrace"],
			caption : "Stack Trace",
			height : "auto",
			width : this.tableWidth,
			shrinkToFit : false
		});

		$("#gbox_stackTraceTable").css({
			"margin-bottom" : this.tableMargin
		});
	},
	getSqlPlan : function(itemName) {
		// SQL文、SQL実行計画、スタックトレースを取得する
		// Ajax通信用の設定
		var settings = {
			data : {
				itemName : itemName
			},
			url : ENS.tree.GET_SQL_PLAN
		};

		// 非同期通信でデータを送信する
		var ajaxHandler = new wgp.AjaxHandler();
		settings[wgp.ConnectionConstants.SUCCESS_CALL_OBJECT_KEY] = this;
		settings[wgp.ConnectionConstants.SUCCESS_CALL_FUNCTION_KEY] = "callbackGetSqlPlan";
		ajaxHandler.requestServerAsync(settings);
	},
	callbackGetSqlPlan : function(sqlPlan) {
		var sqlStatement = sqlPlan.sqlStatement;
		var executionPlanList = sqlPlan.executionPlanList;
		var gettingPlanTimeList = sqlPlan.gettingPlanTimeList;
		var stackTrace = sqlPlan.stackTrace;
	}
});