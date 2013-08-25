<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../include/ext/javaScriptInclude.jsp"%>
<%@ include file="../include/MapListInclude.jsp"%>
<%@ include file="common/Header.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/common/common.css"
	type="text/css" media="all">
</head>
<title>ENdoSnipe Map</title>
<body id="main" oncontextmenu="return false;" onload="self.focus();">
	<div id="persArea"></div>
	<input id="treeData" type="hidden" value='${treeData}' />
	<form:form modelAttribute="mapListForm">
		<!-- マップモード(運用or編集) -->
		<form:hidden path="mapMode" />

		<!-- ResourceTreeViewの展開状態 -->
		<form:hidden path="treeViewOpenNodeData" />

		<!-- 表示しているツリー名 -->
		<form:hidden path="displayTreeArea" />

		<!-- 選択しているResourceTreeViewのID -->
		<form:hidden path="selectedTreeId" />

		<!-- 選択しているResourceMapListViewのID -->
		<form:hidden path="selectedMapListId" />
	</form:form>
	<script>
		var appView = new ENS.AppView();
	</script>
	<script src="<%=request.getContextPath()%>/resources/js/common/user.js" type="text/javaScript"></script>
	<script type="text/javascript">

		// マップモードの取得。
		var mapMode = "";
		if($("#mapMode").val() == ENS.map.mode.OPERATE){
			mapMode = ENS.map.mode.OPERATE;
		}else{
			mapMode = ENS.map.mode.EDIT;
			$("#mapMode").attr("value", ENS.map.mode.EDIT);
		}

		var perspectiveView

		// 運用モード
		// ・マップエリア
		// メニューエリア
		if(mapMode == ENS.map.mode.OPERATE){

			var viewArea1 = {
				width : 1200,
				height : 800,
				rowspan : 1,
				colspan : 1
			};

			var viewArea2 = {
				width : 1200,
				height : 50,
				rowspan : 1,
				colspan : 1
			};

			var table = [ [ new wgp.PerspectiveModel(viewArea1)],
						[new wgp.PerspectiveModel(viewArea2)]];
			perspectiveView = new wgp.PerspectiveView({
				id : "persArea",
				collection : table,
				minimum : false,
				close : false
			});

			// TODO WGPを改修し、barを非表示にする。
			$('#persArea_bar_1_0').hide();

			perspectiveView.dropView("persArea_drop_0_0", "contents_area", "MapView");

			// リソースリスト・マップリスト用の領域を別に用意する。
			var treeArea = $("<div id='tree_area'></div>");
			treeArea.appendTo("body");
			treeArea.hide();

			var listArea = $("<div id='list_area'><div>");
			listArea.appendTo("body");
			listArea.hide();

		// 編集モード
		// ・ツリーエリア(リソースリスト、マップリスト)
		// ・マップエリア
		// ・メニューエリア
		}else {

			var viewArea1 = {
				width : 300,
				height : 800,
				rowspan : 2,
				colspan : 1
			};
			var viewArea2 = {
				width : 900,
				height : 750,
				rowspan : 1,
				colspan : 1
			};
			var viewArea3 = {
				width : 900,
				height : 50,
				rowspan : 1,
				colspan : 1
			};

			var table = [ [ new wgp.PerspectiveModel(viewArea1),
					new wgp.PerspectiveModel(viewArea2) ],
					[new wgp.PerspectiveModel(viewArea3)]];
			perspectiveView = new wgp.PerspectiveView({
				id : "persArea",
				collection : table,
				minimum : false,
				close : false
			});

			// TODO WGPを改修し、barを非表示にする。
			$('#persArea_bar_1_0').hide();

			perspectiveView.dropView("persArea_drop_0_0", "tree_area", "GraphTree");
			perspectiveView.dropView("persArea_drop_0_0", "list_area", "MapList");
			perspectiveView.dropView("persArea_drop_0_1", "contents_area", "MapView");

		}

		// リソースツリーの生成
		var resourceTreeView = new ENS.ResourceTreeView();

		if(mapMode == ENS.map.mode.EDIT){
			resourceTreeView.setEditFunction();

			$("#tree_area")
			.click(
					function() {
						if ($("[id$='mapreduce/task']") != undefined) {

							var elem = $("[id$='mapreduce/task']");

							$("#tree_area").jstree("delete_node",
									elem);
						}
					});

			appView.stopSyncData([wgp.constants.TREE.DATA_ID]);

			// リソースツリーの展開状態・選択状態の復元用データを取得
			var resourceTreeOpenNodes = null;
			var resourceTreeOpenNodesStr = $("#treeViewOpenNodeData").val();
			if(resourceTreeOpenNodesStr > 0){
				resourceTreeOpenNodes = JSON.parse(resourceTreeOpenNodesStr);
			}
			var resourceTreeSelect = null;
			var resourceTreeSelectStr = $("#selectedTreeId").val();
			if(resourceTreeSelectStr.length > 0){
				resourceTreeSelect = resourceTreeSelectStr;
			}

			// リソースツリー構築時の処理をバインド
			$("#" + resourceTreeView.$el.attr("id")).bind("loaded.jstree", function(){
				// 表示状態復元処理を設定
				resourceTreeView.restoreDisplayState(resourceTreeSelect, resourceTreeOpenNodes);
			});

		}

		// マップ一覧ツリーの生成
		var resourceMapListView = new ENS.ResourceMapListView({
			id : "list_area",
			targetId : "contents_area",
			themeUrl : wgp.common.getContextPath()
			+ "/resources/css/jsTree/style.css"
		});

		// マップ一覧ツリーの選択状態の復元用データを取得
		var resourceMapListSelect = null;
		var resourceMapListSelectStr = $("#selectedMapListId").val();
		if(resourceMapListSelectStr.length > 0){
			resourceMapListSelect = resourceMapListSelectStr;
		}

		// マップ一覧ツリー構築後の処理をバインド
		$("#" + resourceMapListView.$el.attr("id")).bind("loaded.jstree", function(){
			resourceMapListView.restoreDisplayState(resourceMapListSelect, null);
		});

		// マップ一覧のモード毎の固有処理を設定
		// ・編集モードの場合は編集イベントを設定する。
		// ・運用モードの場合はダイアログを生成する。
		if(ENS.map.mode.EDIT == mapMode){
			resourceMapListView.setEditFunction();

		}else {
			$("#list_area").dialog({
				title : "Map List",
				autoOpen : false,
				modal : true
			});
		}

		var menuModelArray = [];

		var changeModeMenuModel;
		if(mapMode == ENS.map.mode.OPERATE){
			changeModeMenuModel = new ENS.mapMenuModel({
				width : 25,
				height : 25,
				styleClass : 'map_menu_icon',
				src : '<%=request.getContextPath()%>/resources/images/map/operateModeIcon.png',
				alt : 'This is the current operation mode. Please click if you want to edit the map.',
				onclick : (function(event){
					$("#mapMode").val(ENS.map.mode.EDIT);
					$("#mapListForm").attr("action", "<%=request.getContextPath()%>/map/mapList");
					saveDisplayState();
					$("#mapListForm").submit();
				})
			});

		}else if(mapMode == ENS.map.mode.EDIT){
			changeModeMenuModel = new ENS.mapMenuModel({
				width : 25,
				height : 25,
				styleClass : 'map_menu_icon',
				src : '<%=request.getContextPath()%>/resources/images/map/editModeIcon.png',
				alt : 'This is the current edit mode. Please click to go back to the operation mode.',
				onclick : (function(event){
					$("#mapMode").val(ENS.map.mode.OPERATE);
					$("#mapListForm").attr("action", "<%=request.getContextPath()%>/map/mapList");
					saveDisplayState();
					this.resourceMapListView.saveOperation();
					$("#mapListForm").submit();
				})
			});
		}
		menuModelArray.push(changeModeMenuModel);

		// 編集モード用のメニューアイコンの設定を行う。
		if(mapMode == ENS.map.mode.EDIT){

			var createMapMenuModel = new ENS.mapMenuModel({
				width : 25,
				height : 25,
				styleClass : 'map_menu_icon',
				src : '<%=request.getContextPath()%>/resources/images/map/createIcon.png',
				alt : 'Please click if you want to create a new map.',
				onclick : (function(event){
					resourceMapListView.onCreate();
				})
			});
			menuModelArray.push(createMapMenuModel);

			var saveMapMenuModel = new ENS.mapMenuModel({
				width : 25,
				height : 25,
				styleClass : 'map_menu_icon',
				src : '<%=request.getContextPath()%>/resources/images/map/saveIcon.png',
				alt : 'Please click if you want to save the map.',
				onclick : (function(event){
					if(resourceMapListView.childView){
						var selectedId = $("#" + resourceMapListView.id).find(".jstree-clicked")[0].id;
						var treeModel = resourceMapListView.collection.where({id : selectedId})[0];
						resourceMapListView.childView.onSave(treeModel);
					}else{
						console.log("please select a map");
					}
				})
			});
			menuModelArray.push(saveMapMenuModel);

			var linkMapMenuModel = new ENS.mapMenuModel({
				width : 25,
				height : 25,
				styleClass : 'map_menu_icon',
				src : '<%=request.getContextPath()%>/resources/images/map/createLinkIcon.png',
				alt : 'Please click if you want to add map link.',
				onclick : (function(event){
					if(resourceMapListView.childView){
						var selectedId = $("#" + resourceMapListView.id).find(".jstree-clicked")[0].id;
						var treeModel = resourceMapListView.collection.where({id : selectedId})[0];
						var mapView = resourceMapListView.childView;
						mapView.setClickAddElementEvent(
							"ENS.ResourceLinkElementView",
							raphaelMapConstants.TEXTAREA_ELEMENT_NAME,
							raphaelMapConstants.TEXTAREA_TYPE_NAME);
						$("[alt='createLink']").addClass("map_menu_icon-active");
					}else{
						console.log("please select a map");
					}
				})
			});
			menuModelArray.push(linkMapMenuModel);

			var rectangleMenuModel = new ENS.mapMenuModel({
				width : 25,
				height : 25,
				styleClass : 'map_menu_icon',
				src : '<%=request.getContextPath()%>/resources/images/map/rectangleIcon.png',
				alt : 'Please click if you want to add this object.',
				onclick : (function(event){
					if(resourceMapListView.childView){
						var mapView = resourceMapListView.childView;
						mapView.setClickAddElementEvent(
							"ENS.ShapeElementView",
							raphaelMapConstants.RECTANGLE_ELEMENT_NAME,
							raphaelMapConstants.POLYGON_TYPE_NAME);
					}
				})
			});
			menuModelArray.push(rectangleMenuModel);

			var ellipseMenuModel = new ENS.mapMenuModel({
				width : 25,
				height : 25,
				styleClass : 'map_menu_icon',
				src : '<%=request.getContextPath()%>/resources/images/map/ellipseIcon.png',
				alt : 'Please click if you want to add this object.',
				onclick : (function(event){
					if(resourceMapListView.childView){
						var mapView = resourceMapListView.childView;
						mapView.setClickAddElementEvent(
							"ENS.ShapeElementView",
							raphaelMapConstants.ELLIPSE_ELEMENT_NAME,
							raphaelMapConstants.POLYGON_TYPE_NAME);
					}
				})
			});
			menuModelArray.push(ellipseMenuModel);

			var textMenuModel = new ENS.mapMenuModel({
				width : 25,
				height : 25,
				styleClass : 'map_menu_icon',
				src : '<%=request.getContextPath()%>/resources/images/map/textIcon.png',
				alt : 'Please click if you want to add this object.',
				onclick : (function(event){
					if(resourceMapListView.childView){
						var mapView = resourceMapListView.childView;
						mapView.setClickAddElementEvent(
							"ENS.TextBoxElementView",
							raphaelMapConstants.TEXTAREA_ELEMENT_NAME,
							raphaelMapConstants.TEXTAREA_TYPE_NAME);
					}
				})
			});
			menuModelArray.push(textMenuModel);
		}

		// メニューアイコンを生成する。
		var menuView = new ENS.mapMenuView({
				id : "persArea_drop_1_0",
				collection : menuModelArray
			},
			{}
		);

		// メニューアイコンにツールチップを付ける。
		$(".map_menu_icon").tooltip({
			content : function(){
				return $(this).attr("alt");
			},
			items : "[alt]",
			tooltipClass : "tooltip"
		});

	/**
	 * 画面遷移を行う際に、画面の状態を引き継ぐ。
	 */
	function saveDisplayState(){
		var treeIdAttribute = "id";
		var selectedMapListId = resourceMapListView.getSelectedNodeId(treeIdAttribute);
		$("#selectedMapListId").val(selectedMapListId);
	}

	// リソース一覧又はマップ一覧タブの選択状態があれば選択する。
	var displayTreeArea = $("#displayTreeArea").val();
	$("#persArea_drop_0_0").find(".tab_menu").find("[href='"+ displayTreeArea +"']").click();

	</script>
	<input type="hidden" id="context" value="<%=request.getContextPath()%>" />
</body>
</html>