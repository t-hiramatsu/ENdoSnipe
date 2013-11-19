<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../include/ext/javaScriptInclude.jsp"%>
<%@ include file="../include/DashboardInclude.jsp"%>
<%@ include file="common/Header.jsp"%>
<%@ include file="dialog/SignalDialog.jsp"%>
<%@ include file="dialog/SchedulingReportDialog.jsp"%>
<%@ include file="dialog/ReportDialog.jsp"%>
<%@ include file="dialog/PerformanceDoctorDialog.jsp"%>
<%@ include file="dialog/threadDumpDialog.jsp"%>
<%@ include file="dialog/SummarySignalDialog.jsp"%>

<title>ENdoSnipe Dashboard</title>
</head>
<body id="main" oncontextmenu="return false;" onload="self.focus();">

	<script type="text/javascript">
		var viewArea1 = {};
		var viewArea2 = {};

		viewArea1.width = 300;
		viewArea1.height = 800;
		viewArea1.rowspan = 1;
		viewArea1.colspan = 1;

		viewArea2.width = 900;
		viewArea2.height = 800;
		viewArea2.rowspan = 1;
		viewArea2.colspan = 1;

		var table = [ [ new wgp.PerspectiveModel(viewArea1),
				new wgp.PerspectiveModel(viewArea2) ] ];
		var perspectiveView = new wgp.PerspectiveView({
			id : "persArea",
			collection : table,
			minimum : false,
			close : false
		});
		perspectiveView.dropView("persArea_drop_0_0", "tree_area");
		perspectiveView.dropView("persArea_drop_0_1", "contents_area");

		var appView = new ENS.AppView();
	</script>

	<%@ include file="../include/pluginsInclude.jsp"%>
	<script src="<%=request.getContextPath()%>/resources/js/common/user.js"
		type="text/javaScript"></script>

	<script>
		var treeView = new ENS.treeManager();
	</script>
	<input type="hidden" id="context" value="<%=request.getContextPath()%>" />

	<!-- レポートダウンロード用のフォーム -->
	<form method="post" id="reportDownload"
		action="<%=request.getContextPath()%>/report/download">
		<input type="hidden" id="reportId" name="reportId" value="" />
		<div hidden=true>
			<input type="submit" name="btn" id="btn" value="submit">
		</div>
		<div id="connect" style="display: none">${connect}</div>
	</form>
	<script>
		if ($("#connect").text() == "-1") {
			alert("DataCollector cannot be connected");
		}
	</script>

	<!-- Javelinlogダウンロード用のフォーム -->
	<form method="post" id="jvnLogDownload"
		action="<%=request.getContextPath()%>/performanceDoctor/download">
		<input type="hidden" id="fileName" name="fileName" value="" />
		<div hidden=true>
			<input type="submit" name="jvnLogBtn" id="jvnLogBtn" value="submit">
		</div>
	</form>
</body>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/common/common.css"
	type="text/css" media="all">
</html>
