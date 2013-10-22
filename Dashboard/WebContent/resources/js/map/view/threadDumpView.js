ENS.threadDumpView = wgp.AbstractView.extend({
	tableColNames : [ "Time", "Thread Dump" ],
	initialize : function(argument, treeSettings) {

		this.tableColModel = this.createTableColModel();

		var appView = new ENS.AppView();
		this.appView = appView;
		this.treeSettings = treeSettings;
		this.argument = argument;
		appView.addView(this, treeSettings.treeId
				+ ENS.URL.JVN_LOG_NOTIFY_POSTFIX_ID);

		/*
		 * appView.addView(this, treeSettings.treeId +
		 * ENS.URL.THREADDUMP_POSTFIX_ID);
		 */

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
		this.id = argument.id;
		this.argument.term = argument.term;

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
		$("#button").on("click", function() {
			var settings = {
				data : {
					threadDump : ""
				},
				url : ENS.URL.THREAD_DUMP_CLICK
			};
			// 非同期通信でシグナル削除依頼電文を送信する。
			var ajaxHandler = new wgp.AjaxHandler();
			ajaxHandler.requestServerAsync(settings);
		});

	},
	render : function() {
		var id = this.id;
		$("#" + this.id).append(
				"<input type='button' id='button' value='get Thread Dump'>");
		$("#button").css({
			"margin-left" : 750,
			"width" : 150
		});
		$("#" + id).append('<div id="threadDumpDiv"></div>');
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
			rowNum : 1,
			rowList : [ 1 ],
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
		var term = this.argument.term;
		var treeId = this.treeSettings.treeId;
		var maxLineNum = this.argument.maxLineNum;
		/*$("#button").on("click", function() {
			var settings = {
				data : {
					threadDump : ""
				},
				url : ENS.URL.THREAD_DUMP_CLICK
			};
			alert(JSON.stringify(settings));
			// 非同期通信でシグナル削除依頼電文を送信する。
			var ajaxHandler = new wgp.AjaxHandler();
			ajaxHandler.requestServerAsync(settings);
			
			 * var startTime = new Date(new Date().getTime() - term * 1000); var
			 * endTime = new Date(); var appView = new ENS.AppView();
			 * 
			 * appView.getTermThreadDumpData([ treeId ], startTime, endTime,
			 * maxLineNum);
			 * 
			 * this.dualSliderView.setScaleMovedEvent(function( from, to) { var
			 * appView = new ENS.AppView(); var startTime = new Date(new
			 * Date().getTime() - from); var endTime = new Date(new
			 * Date().getTime() - to); appView.getTermThreadDumpData([ treeId ],
			 * startTime, endTime, 100); });
			 
		});*/

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

		$("#threadDumpTable").clearGridData().setGridParam({
			data : tableViewData
		}).trigger("reloadGrid");
	},
	createTableColModel : function() {
		var tableColModel = [ {
			name : "date",
			width : 200
		}, {
			name : "threadDumpInfo",
			width : 680
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
