ENS.AppView = wgp.AppView.extend({
	// Term data methods.
	initialize : function() {
		this.viewType = wgp.constants.VIEW_TYPE.CONTROL;
		this.collections = {};
		this.syncIdList = {};
		var ins = this;
		ENS.AppView = function() {
			return ins;
		};
	},
	getTermData : function(syncIdList, startTime, endTime) {
		var instance = this;
		this.stopSyncData(syncIdList);
		var url = wgp.common.getContextPath()
				+ wgp.constants.URL.GET_TERM_DATA;
		var dataMap = {
			startTime : startTime.getTime(),
			endTime : endTime.getTime(),
			dataGroupIdList : syncIdList
		};
		var settings = {
			url : url,
			data : {
				data : JSON.stringify(dataMap)
			}
		};
		this.onSearch(settings);
	},
	getTermPerfDoctorData : function(syncIdList, startTime, endTime, maxLineNum) {
		var instance = this;
		this.stopSyncData(syncIdList);
		var url = wgp.common.getContextPath()
				+ ENS.URL.TERM_PERFDOCTOR_DATA_URL;
		var dataMap = {
			startTime : startTime.getTime(),
			endTime : endTime.getTime(),
			dataGroupIdList : syncIdList,
			maxLineNum : maxLineNum
		};
		var settings = {
			url : url,
			data : {
				data : JSON.stringify(dataMap)
			}
		};
		this.onSearch(settings);
	},
	getTermThreadDumpData : function(syncIdList, startTime, endTime, maxLineNum) {
		var instance = this;
		this.stopSyncData(syncIdList);
		var url = wgp.common.getContextPath()
				+ ENS.URL.TERM_THREADDUMP_DATA_URL;
		var dataMap = {
			startTime : startTime.getTime(),
			endTime : endTime.getTime(),
			dataGroupIdList : syncIdList,
			maxLineNum : maxLineNum
		};
		var settings = {
			url : url,
			data : {
				data : JSON.stringify(dataMap)
			}
		};
		this.onSearch(settings);
	},
	_add : function(addCollection, addData) {
		if (addCollection.get(addData.id) != null) {
			console.log('Collection already Exists');
		} else {
			var model = new addCollection.model(addData.updateData);
			
			// 追加するmodelがツリーデータ以外であれば、そのままcollectionに追加する
			var treeId = model.get("treeId");
			if (treeId === undefined) {
				addCollection.add(model);
				return;
			}
			
			// リアルタイムでノードを追加する
			this._addRealtimeNode(model, addCollection);
		}
	},
	_addRealtimeNode : function(model, addCollection) {
		// 追加するmodelがツリーデータであれば、
		// 追加するmodelの親ノードを再帰的に見ていき、
		// ツリーとして表示されているノードに出会ったら、そのノードノードの1つ下の子ノードを追加する
		var parentTreeId = model.get("parentTreeId");
		
		var treeModels = this.collections.tree.models;
		var treeModelsLength = treeModels.length;
		var treeModelsIndex;
		var treeModel;
		
		// 追加する末端ノードの親ノードが存在する場合は、ノードのtypeをtarget、iconをleafとして追加するため、
		// modelに変更を加えることなくcollectionに追加する。
		for (treeModelsIndex = 0; treeModelsIndex < treeModelsLength; treeModelsIndex++) {
			treeModel = treeModels[treeModelsIndex];
			if (parentTreeId == treeModel.get("treeId")) {
				addCollection.add(model);
				return;
			}
		}
		
		var treeId = model.get("treeId");
		var treeIdSplitList = treeId.split("/");
		var splitListLength = treeIdSplitList.length;
		
		// 末端ノード以外のノードの親が見つかった時は、そのノードのtypeをgroup、iconをcenterとして追加する。
		for (var splitIndex = splitListLength - 1; splitIndex >= 0; splitIndex--) {
			var tmpParentId = "";
			
			for (var count = 1; count < splitIndex; count++) {
				var addNodeId = "/" + treeIdSplitList[count];
				tmpParentId += addNodeId;
			}
			
			for (treeModelsIndex = 0; treeModelsIndex < treeModelsLength; treeModelsIndex++) {
				treeModel = treeModels[treeModelsIndex];
				
				// 親のノードが存在して、そのノードがOpen状態である場合、もしくは、
				// 最後まで親ノードが存在しなかった時に、ノード追加を行う
				if (tmpParentId == treeModel.get("treeId")　|| tmpParentId === "") {

					// 親ノードがOpenかCloseかを調べ、Openである場合にノード追加を行う
					var elem = document.getElementById(tmpParentId);
					var parentLiTag = $(elem).parent("li");
					var nodeClass = parentLiTag.attr('class');
					
					if(tmpParentId !== "" && nodeClass !== undefined && nodeClass.indexOf("jstree-closed") != -1) {
						return;
					}

					var showName = treeIdSplitList[count];
					var addTreeId = tmpParentId + "/" + showName;
					var treeOption = {
						id : addTreeId,
						data : showName,
						treeId : addTreeId,
						parentTreeId : tmpParentId,
						icon : "center",
						type : ENS.tree.type.GROUP
					};
					
					addCollection.add(treeOption);
					return;
				}
			}
		}
	}
});