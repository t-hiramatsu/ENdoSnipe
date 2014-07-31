ENS.profilerView = wgp.AbstractView.extend({
	tableColNames : [ "Response", "Target", "Class Name", "Method Name",
			"TAT Threshold", "CPU Threshold", "Call Count", "Total Time",
			"Average Time", "Maximum Time", "Minimum Time", "Total CPU Time",
			"Average CPU Time", "Maximum CPU Time", "Minimum CPU Time",
			"Total User Time", "Average User Time", "Maximum User Time",
			"Minimum User Time", "Throwable Count" ],
	initialize : function(argument, treeSettings) {
		var instance = this;
		this.tableMargin = 20;
		this.tableWidth = parseInt($("#" + this.id).width() - this.tableMargin
				* 4);
		this.tableColModel = this.createTableColModel();

		var appView = new ENS.AppView();
		this.treeSettings = treeSettings;
		appView
				.addView(this, treeSettings.treeId
						+ ENS.URL.PROFILER_POSTFIX_ID);

		// 空のテーブルを作成
		this.render();

		$("#reloadButton").on("click", function() {
			instance.collection.models = [];
			ENS.tree.agentName = instance.treeSettings.treeId;
			var settings = {
				data : {
					agentName : ENS.tree.agentName
				},
				url : ENS.tree.PROFILER_RELOAD
			};
			var ajaxHandler = new wgp.AjaxHandler();
			ajaxHandler.requestServerAsync(settings);
		});

		$("#resetButton").on("click", function() {
			ENS.tree.agentName = instance.treeSettings.treeId;
			var settings = {
				data : {
					agentName : ENS.tree.agentName
				},
				url : ENS.tree.PROFILER_RESET
			};
			var ajaxHandler = new wgp.AjaxHandler();
			ajaxHandler.requestServerAsync(settings);
		});

		$("#updateButton").on(
				"click",
				function() {
					ENS.tree.agentName = instance.treeSettings.treeId;
					var changedCells = JSON.stringify($("#profilerTable")
							.getChangedCells());
					var settings = {
						data : {
							agentName : ENS.tree.agentName,
							invocations : changedCells
						},
						url : ENS.tree.PROFILER_UPDATE
					};
					var ajaxHandler = new wgp.AjaxHandler();
					ajaxHandler.requestServerAsync(settings);
				});

		this.id = argument.id;
	},
	render : function() {
		$("#" + this.id).append('<div id="profilerDiv"></div>');
		$("#profilerDiv").css({
			"margin-left" : 5
		});
		$("#profilerDiv").append(
				"<input type='button' id='reloadButton' value='reload'>");
		$("#profilerDiv").append(
				"<input type='button' id='resetButton' value='reset'>");
		$("#profilerDiv").append(
				"<input type='button' id='updateButton' value='update'>");
		$("#profilerDiv").append('<table id="profilerTable"></table>');
		$("#profilerDiv").append('<div id="profilerPager"></table>');
		var height = "auto";

		$("#profilerTable").jqGrid({
			datatype : "local",
			data : "",
			colModel : this.tableColModel,
			colNames : this.tableColNames,
			caption : "Profile of " + this.treeSettings.id,
			pager : "profilerPager",
			rowNum : 20,
			rowList : [ 20, 50, 100 ],
			pgbuttons : true,
			pginput : true,
			height : height,
			width : this.tableWidth,
			// sortname : "date",
			sortorder : "desc",
			viewrecords : true,
			rownumbers : true,
			shrinkToFit : false,
			cellEdit : true,
			cmTemplate : {
				title : false
			},
			cellsubmit : 'clientArray'
		});
		$("#profilerTable").filterToolbar({
			defaultSearch : 'cn'
		});
		$("#profilerDiv").css('font-size', '0.8em');
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

		$("#profilerTable").clearGridData().setGridParam({
			data : tableViewData
		}).trigger("reloadGrid");
	},
	createTableColModel : function() {
		var tableColModel = [ {
			name : "transactionGraph",
			width : parseInt(this.tableWidth * 0.05),
			editable : true,
			edittype : "select",
			editoptions : {
				value : "true:true; false:false"
			}
		}, {
			name : "target",
			width : parseInt(this.tableWidth * 0.05),
			editable : true,
			edittype : "select",
			editoptions : {
				value : "true:true; false:false"
			}
		}, {
			name : "className",
			width : parseInt(this.tableWidth * 0.2)
		}, {
			name : "methodName",
			width : parseInt(this.tableWidth * 0.2)
		}, {
			name : "alarmThreshold",
			width : parseInt(this.tableWidth * 0.05),
			editable : true,
			edittype : "text",
			editrules : {
				nubmer : true
			}
		}, {
			name : "alarmCpuThreshold",
			// width : 140,
			width : parseInt(this.tableWidth * 0.05),
			editable : true,
			edittype : "text"
		}, {
			name : "callCount",
			width : parseInt(this.tableWidth * 0.05),
			sorttype : "float"
		}, {
			name : "total",
			width : parseInt(this.tableWidth * 0.05),
			sorttype : "float"
		}, {
			name : "average",
			width : parseInt(this.tableWidth * 0.05),
			sorttype : "float"
		}, {
			name : "maximum",
			width : parseInt(this.tableWidth * 0.05),
			sorttype : "float"
		}, {
			name : "minimum",
			width : parseInt(this.tableWidth * 0.05),
			sorttype : "float"
		}, {
			name : "cpuTotal",
			width : parseInt(this.tableWidth * 0.05),
			sorttype : "float"
		}, {
			name : "cpuAverage",
			width : parseInt(this.tableWidth * 0.05),
			sorttype : "float"
		}, {
			name : "cpuMaximum",
			width : parseInt(this.tableWidth * 0.05),
			sorttype : "float"
		}, {
			name : "cpuMinimum",
			width : parseInt(this.tableWidth * 0.05),
			sorttype : "float"
		}, {
			name : "userTotal",
			width : parseInt(this.tableWidth * 0.05),
			sorttype : "float"
		}, {
			name : "userAverage",
			width : parseInt(this.tableWidth * 0.05),
			sorttype : "float"
		}, {
			name : "userMaximum",
			width : parseInt(this.tableWidth * 0.05),
			sorttype : "float"
		}, {
			name : "userMinimum",
			width : parseInt(this.tableWidth * 0.05),
			sorttype : "float"
		}, {
			name : "throwableCount",
			width : parseInt(this.tableWidth * 0.05),
			sorttype : "float"
		} ];

		return tableColModel;
	}
});