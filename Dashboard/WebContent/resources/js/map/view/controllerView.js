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
					instance._reload();
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
				this._reload();
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
				var height = 600;

				$("#controllerTable").jqGrid(
						{
							datatype : "local",
							data : "",
							colModel : this.tableColModel,
							colNames : this.tableColNames,
							caption : "Properties of " + this.treeSettings.id,
							pager : "controllerPager",
							rowNum : 10000,
							height : height,
							width : this.tableWidth,
							viewrecords : true,
							rownumbers : true,
							shrinkToFit : true,
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
			_reload : function() {
				var instance = this;
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
			},	
			onAdd : function(element) {
			},
			onChange : function(element) {
			},
			onRemove : function(element) {
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
				
				tableViewData.sort(
						function(data1, data2) {
							var order1 = parseInt(data1.sortOrder, 10);
							var order2 = parseInt(data2.sortOrder, 10);
							return order1 - order2;
						}
				);

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
							width : parseInt(this.tableWidth * 0.1)
						},
						{
							name : "updateValue",
							width : parseInt(this.tableWidth * 0.1),
							editable : true,
							edittype : "text"
						},
						{
							name : "propertyDetail",
							width : parseInt(this.tableWidth * 0.55)
						}];
				return tableColModel;
			}
		});