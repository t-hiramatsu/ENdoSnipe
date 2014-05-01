<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<div id="persArea"></div>
	<input id="treeData" type="hidden" value='${treeData}' />
	<div id="threadDumpDialog" title="Detail of ThreadDump Result"
		style="display: none">

		<div id="threadDumpPatternValueArea" class="dialogContentArea">
			<div id="threadDumpPatternValue" class="dialogValue">

				<table id="threadDumpDetailResult"
					style='width: 1150px; height: 770px; font-size: 14px; background-color: black;'
					border=1 cellpadding=2 cellspacing=0>
					<tr>
						<td style='width: 120px;'><b>Time</b><br /></td>
						<td id="threadDumpTime" style="word-break: break-all;"></td>
					</tr>
					<tr>
						<td style='width: 150px;' valign="top"><b>Detail</b><br /></td>
						<td id="threadDump" valign="top" style="word-break: break-all;"></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>
</html>