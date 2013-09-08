ENS.perfDoctorView = wgp.AbstractView.extend({
	tableColNames : [ "Time", "Description", "Level", "Class Name",
			"Method Name", "Detail", "Download", "Logfile", "detailResult" ],
	initialize : function(argument, treeSettings) {

		this.tableColModel = this.createTableColModel();

		var appView = new ENS.AppView();
		this.treeSettings = treeSettings;
		appView.addView(this, treeSettings.treeId
				+ ENS.URL.PERFDOCTOR_POSTFIX_ID);

		var dualSliderId = this.id + "_dualSlider";
		// dual slider area (add div and css, and make slider)
		$("#" + this.id).append('<div id="' + dualSliderId + '"></div>');
		$('#' + dualSliderId).css(ENS.nodeinfo.parent.css.dualSliderArea);
		$('#' + dualSliderId).css(ENS.nodeinfo.parent.css.dualSliderArea);
		this.dualSliderView = new ENS.DualSliderView({
			id : dualSliderId,
			rootView : this
		});

		// 空のテーブルを作成
		this.render();

		var startTime = new Date(new Date().getTime() - argument.term * 1000);
		var endTime = new Date();
		appView.getTermPerfDoctorData([ treeSettings.treeId ], startTime,
				endTime, argument.maxLineNum);

		this.id = argument.id;

		this.dualSliderView.setScaleMovedEvent(function(from, to) {
			var appView = new ENS.AppView();
			var startTime = new Date(new Date().getTime() - from);
			var endTime = new Date(new Date().getTime() - to);
			appView.getTermPerfDoctorData([ treeSettings.treeId ], startTime,
					endTime, 100);
		});
	},
	render : function() {
		$("#" + this.id).append('<div id="journalDiv"></div>');
		$("#journalDiv").css({
			"margin-left" : 5
		});
		$("#journalDiv").append('<table id="journalTable"></table>');
		$("#journalDiv").append('<div id="journalPager"></table>');
		var height = "auto";

		$("#journalTable").jqGrid({
			datatype : "local",
			data : "",
			colModel : this.tableColModel,
			colNames : this.tableColNames,
			caption : "Diagnosis of " + this.treeSettings.id,
			pager : "journalPager",
			rowNum : 20,
			rowList : [ 20, 50, 100 ],
			pgbuttons : true,
			pginput : true,
			height : height,
			width : 880,
			sortname : "date",
			sortorder : "desc",
			viewrecords : true,
			rownumbers : true,
			shrinkToFit : false,
			cellEdit : true,
			cmTemplate: { title: false }
		});
		$("#journalTable").filterToolbar({
			defaultSearch : 'cn'
		});
		$("#journalDiv").css('font-size', '0.8em');
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

		$("#journalTable").clearGridData().setGridParam({
			data : tableViewData
		}).trigger("reloadGrid");
	},
	createTableColModel : function() {
		var tableColModel = [ {
			name : "date",
			width : 85
		}, {
			name : "description",
			width : 310
		}, {
			name : "level",
			width : 55
		}, {
			name : "className",
			width : 120
		}, {
			name : "methodName",
			width : 120
		}, {
			name : "detailInfo",
			// width : 140,
			width : 50,
			formatter : ENS.Utility.makeAnchor,
			editoptions : {
				"onclick" : "ENS.perfDoctor.dialog",
				"linkName" : "Detail"
			}
		}, {
			name : "detail",
			width : 80,
			formatter : ENS.Utility.makeAnchor,
			editoptions : {
				"onclick" : "ENS.perfDoctor.download",
				"linkName" : "Download"
			}
		}, {
			name : "logFileName",
			width : 0,
			hidden : true
		}, {
			name : "detailResult",
			width : 0,
			hidden : true
		} ];

		return tableColModel;
	},
	makeAnchor : function(cellValue, options, rowObject) {
		var selectValueList = options.colModel.editoptions;
		var val = rowObject.value;
		var rowId = options.rowId;
		var onclick = selectValueList.onclick;
		return '<a href="javascript:void(0)" onclick="'
				+ selectValueList.onclick + ';">' + 'DL' + '</a>';
	}
});

ENS.perfDoctor.download = function(id) {
	var rowData = $("#journalTable").getRowData(id);
	var fileName = rowData.logFileName;
	$("input#fileName").val(fileName);
	$('#jvnLogBtn').click();
};

ENS.perfDoctor.dialog = function(id) {
	var rowData = $("#journalTable").getRowData(id);
	
	$("#perDocTime").empty();
	$("#perDocTime").append(rowData.date);

	$("#perDocDescription").empty();
	$("#perDocDescription").append(rowData.description);

	$("#perDocLevel").empty();
	$("#perDocLevel").append(rowData.level);

	$("#perDocClassName").empty();
	$("#perDocClassName").append(rowData.className);

	$("#perDocMethodName").empty();
	$("#perDocMethodName").append(rowData.methodName);

	$("#perDocLogFileName").empty();
	$("#perDocLogFileName").append(rowData.logFileName);

	var changed = rowData.detailResult;
	changed = changed.replace(/>/g, "&gt;").replace(/</g, "&lt;");
	changed = changed.replace(/%/gi, "<br/>");
	$("#perDocDetail").empty();
	$("#perDocDetail").append(changed);
	$("#performanceDoctorDialog").dialog({
		modal : true,
		width : 1200,
		height : 800,
	});
};
