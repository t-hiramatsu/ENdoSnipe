ENS.reportView = wgp.AbstractView.extend({
	tableColModel : [ {
		name : "no",
		width : 50
	}, {
		name : "name",
		width : 300
	}, {
		name : "fmTime",
		width : 165
	}, {
		name : "toTime",
		width : 165
	}, {
		name : "term",
		width : 100
	}, {
		name : "download",
		width : 60
	} ],
	tableColNames : [ "No", "レポート名", "開始日", "終了日", "期間(日)", "DL" ],
	initialize : function(argument, treeSettings) {
		var appView = new ENS.AppView();
		this.treeSettings = treeSettings;
		appView.addView(this, treeSettings.treeId + ENS.URL.PERFDOCTOR_POSTFIX_ID);

		// 空のテーブルを作成
		this.render();
		
		this.getReportList();
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
	getReportList : function() {
		
	}
});