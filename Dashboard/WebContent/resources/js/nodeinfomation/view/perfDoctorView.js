ENS.perfDoctorView = wgp.AbstractView.extend({
	tableColNames : [ "時刻", "概要", "重要度", "クラス", "メソッド", "ダウンロード", "ログファイル" ],
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
			rowList : [ 100, 1000, 10000 ],
			pgbuttons : false,
			pginput : false,
			height : height
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
			width : 95
		}, {
			name : "description",
			width : 290
		}, {
			name : "level",
			width : 65
		}, {
			name : "className",
			width : 150
		}, {
			name : "methodName",
			width : 100
		}, {
			name : "detail",
//			width : 140,
			width : 65,
			formatter : ENS.Utility.makeAnchor,
			editoptions : {
				"onclick" : "ENS.perfDoctor.download",
				"linkName" : "DL"
			}
		}, {
			name : "logFileName",
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
		// return '<a href="./JvnDownloadServlet?agent_id=' +
		// "agent_000"/*getAgentId()*/
		// + '&log_file_name=' + "test"/*alarm_data.file_name*/ + '"
		// target="_blank">DL</a>';
//		return '<a href="javascript:void(0)" onclick="'
//				+ selectValueList.onclick + ';">' + 'DL' + '</a>';
		return '<a href="javascript:void(0)" onclick="'
		+ selectValueList.onclick + ';">' + 'DL' + '</a>';
	}// ,
	// download : function() {
	// var event = {
	// "agent_id" : 42,
	// "log_file_name" : "logFileName"
	// };
	// jQuery.post('/DashBoard/JvnDownloadServlet',event,this.callbackDownload);
	// },
	// callbackDownload : function() {
	//		
	// }
});

//function download() {
//	var event = {
//		"agent_id" : "1"/*getAgentId()*/,
//		"log_file_name" : "javelin_2013_05_09_164114_175_00000.jvn"
//	};
//	jQuery.post('/Dashboard/JvnDownloadServlet', event, callbackDownload);
//}
ENS.perfDoctor.download = function(id) {
	var rowData = $("#journalTable").getRowData(id);
	var fileName = rowData.logFileName;
	$("input#fileName").val(fileName);
	$('#jvnLogBtn').click();
}

function callbackDownload(responce) {
	alert("download!");
}
