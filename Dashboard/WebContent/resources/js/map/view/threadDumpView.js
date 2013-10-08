ENS.threadDumpView = wgp.AbstractView.extend({
	tableColNames : [ "Time", "Thread Dump" ],
	initialize : function(argument, treeSettings) {

		this.tableColModel = this.createTableColModel();

		var appView = new ENS.AppView();
		this.treeSettings = treeSettings;
		appView.addView(this, treeSettings.treeId
				+ ENS.URL.THREADDUMP_POSTFIX_ID);

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
		appView.getTermThreadDumpData([ treeSettings.treeId ], startTime,
				endTime, argument.maxLineNum);

		this.id = argument.id;

		
		this.dualSliderView.setScaleMovedEvent(function(from, to) {
			var appView = new ENS.AppView();
			var startTime = new Date(new Date().getTime() - from);
			var endTime = new Date(new Date().getTime() - to);
			appView.getTermThreadDumpData([ treeSettings.treeId ], startTime,
					endTime, 100);
		});
	},
	render : function() {
		var id= this.id;
		$("#"+this.id).append("<input type='button' id='button' value='get Thread Dump'>");
		$("#button").css({
			"margin-left":750,
			"width": 150
		});
		$("#"+id).append('<div id="threadDumpDiv"></div>');
		$("#threadDumpDiv").css({
			"margin-left" : 5
		});
		$("#threadDumpDiv").append('<table id="threadDumpTable"></table>');
		$("#threadDumpDiv").append('<div id="threadDumpPager"></table>');
		var height = "auto";

		$("#threadDumpTable").jqGrid({
			datatype : "local",
			data : "",
			colModel : this.tableColModel,
			colNames : this.tableColNames,
			caption : "Diagnosis of " + this.treeSettings.id,
			pager : "threadDumpPager",
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
			shrinkToFit : false
		});
		$("#threadDumpTable").filterToolbar({
			defaultSearch : 'cn'
		});
		$("#threadDumpDiv").css('font-size', '0.8em');
		$("#button").on("click", function(){
			alert("this is table");
		/*	$("#"+id).append('<div id="threadDumpDiv"></div>');
			$("#threadDumpDiv").css({
				"margin-left" : 5
			});
			$("#threadDumpDiv").append('<table id="threadDumpTable"></table>');
			$("#threadDumpDiv").append('<div id="threadDumpPager"></table>');
			var height = "auto";

			$("#threadDumpTable").jqGrid({
				datatype : "local",
				data : "",
				colModel : this.tableColModel,
				colNames : this.tableColNames,
				caption : "Diagnosis of " + this.treeSettings.id,
				pager : "threadDumpPager",
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
				shrinkToFit : false
			});
			$("#threadDumpTable").filterToolbar({
				defaultSearch : 'cn'
			});
			$("#threadDumpDiv").css('font-size', '0.8em');*/
		})
	/*	$("#" + this.id).append('<div id="threadDumpDiv"></div>');
		$("#threadDumpDiv").css({
			"margin-left" : 5
		});
		$("#threadDumpDiv").append('<table id="threadDumpTable"></table>');
		$("#threadDumpDiv").append('<div id="threadDumpPager"></table>');
		var height = "auto";

		$("#threadDumpTable").jqGrid({
			datatype : "local",
			data : "",
			colModel : this.tableColModel,
			colNames : this.tableColNames,
			caption : "Diagnosis of " + this.treeSettings.id,
			pager : "threadDumpPager",
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
			shrinkToFit : false
		});
		$("#threadDumpTable").filterToolbar({
			defaultSearch : 'cn'
		});
		$("#threadDumpDiv").css('font-size', '0.8em');*/
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
			width : 200
		}, {
			name : "threadDump",
			width : 680
		}];

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
