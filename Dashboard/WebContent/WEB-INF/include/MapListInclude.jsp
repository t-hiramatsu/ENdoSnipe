<meta charset="UTF-8" />
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/nodeinfomation/nodeStyles.css"
	type="text/css" media="all">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/lib/slider/ui.slider.extras.css"
	type="text/css" media="all">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/lib/pagination/pagination.css"
	type="text/css" media="all">

<%-- libraries --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/lib/pagination/jquery.pagination.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/lib/slider/selectToUISlider.jQuery.js">
</script>

<%-- common static value --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/common/constants.js">
</script>

<%-- dual slider --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/common/dualSliderView.js">
</script>

<%-- utility --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/common/endoSnipeUtility.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/common/ensAppView.js">
</script>

<%-- tree --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/tree/ensTreeView.js">
</script>

<%-- tree --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/tree/resourceTreeView.js">
</script>

<%-- resource map --%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/map/map.css"
	type="text/css" media="all">

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/element/textField.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/resourceMapView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/resourceMapListView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/resourceMapMenuView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/shapeView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/signalView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/resourceLinkView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/textboxView.js">
</script>

<%-- nodeInfo graph --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/nodeInfoParentView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/resourceGraphView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/model/resourceGraphModel.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/manager/raphaelMapManager.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/common/command.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/command/raphaelMapMoveCommand.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/command/raphaelMapDeleteCommand.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/command/raphaelMapResizeCommand.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/command/raphaelMapPasteCommand.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/dialog/mapElementPropertyView.js">