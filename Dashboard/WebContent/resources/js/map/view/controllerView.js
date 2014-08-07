ENS.controllerView = wgp.AbstractView
		.extend({
			tableColNames : [ "Property", "Current Value", "Update Value", "Property Detail"],
			initialize : function(argument, treeSettings) {
				var instance = this;
				this.tableMargin = 20;
				this.tableWidth = parseInt($("#" + this.id).width()
						- this.tableMargin * 4);
				this.tableColModel = this.createTableColModel();

				var appView = new ENS.AppView();
				this.treeSettings = treeSettings;
				appView.addView(this, treeSettings.treeId
						+ ENS.URL.CONTROLLER_POSTFIX_ID);

				// 空のテーブルを作成
				this.render();

				$("#controllerReloadButton").on("click", function() {
					instance.collection.models = [];
					ENS.tree.agentName = instance.treeSettings.treeId;
					var settings = {
						data : {
							agentName : ENS.tree.agentName
						},
						url : ENS.tree.CONTROLLER_RELOAD
					};
					var ajaxHandler = new wgp.AjaxHandler();
					ajaxHandler.requestServerAsync(settings);
				});

				$("#controllerClearButton").on("click", function() {
					var rowIDs = $("#controllerTable").getDataIDs();
					$.each(rowIDs, function(i, item) {
						$("#controllerTable").setCell(item, 'updateValue', undefined, "", "");
					});
				});

				$("#controllerUpdateButton").on("click", function() {
					instance.collection.models = [];
					ENS.tree.agentName = instance.treeSettings.treeId;
					var changedCells = JSON.stringify($("#controllerTable").getChangedCells());
					var settings = {
							data : {
								agentName : ENS.tree.agentName,
								propertyList : changedCells
							},
							url : ENS.tree.CONTROLLER_UPDATE
					};
					var ajaxHandler = new wgp.AjaxHandler();
					ajaxHandler.requestServerAsync(settings);
				});

				this.id = argument.id;
			},
			render : function() {
				$("#" + this.id).append('<div id="controllerDiv"></div>');
				$("#controllerDiv").css({
					"margin-left" : 5
				});
				$("#controllerDiv")
						.append(
								"<input type='button' id='controllerReloadButton' value='reload'>");
				$("#controllerDiv").append(
						"<input type='button' id='controllerClearButton' value='clear'>");
				$("#controllerDiv")
						.append(
								"<input type='button' id='controllerUpdateButton' value='update'>");
				$("#controllerDiv").append('<table id="controllerTable"></table>');
				$("#controllerDiv").append('<div id="controllerPager"></table>');
				var height = "auto";

				$("#controllerTable").jqGrid(
						{
							datatype : "local",
							data : "",
							colModel : this.tableColModel,
							colNames : this.tableColNames,
							caption : "Properties of " + this.treeSettings.id,
							pager : "controllerPager",
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
				$("#controllerTable").filterToolbar({
					defaultSearch : 'cn'
				});
				$("#controllerDiv").css('font-size', '0.8em');
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

				$("#controllerTable").clearGridData().setGridParam({
					data : tableViewData
				}).trigger("reloadGrid");
			},
			createTableColModel : function() {
				var tableColModel = [
						{
							name : "property",
							width : parseInt(this.tableWidth * 0.25),
							key : true
						},
						{
							name : "currentValue",
							width : parseInt(this.tableWidth * 0.15)
						},
						{
							name : "updateValue",
							width : parseInt(this.tableWidth * 0.15),
							editable : true,
							edittype : "text"
						},
						{
							name : "propertyDetail",
							width : parseInt(this.tableWidth * 0.45)
						}];
				return tableColModel;
			}
		});