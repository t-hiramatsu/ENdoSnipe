<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/dialog/PerfDoctorDialog.css"
	type="text/css" media="all">
</head>
<body>
	<div id="persArea"></div>
	<input id="treeData" type="hidden" value='${treeData}' />
	<div id="performanceDoctorDialog" title="Detail of Diagnotics Result"
		style="display: none">
		
		<div id="performanceDoctorPatternValueArea" class="dialogContentArea">
			<div id="performanceDoctorPatternValue" class="dialogValue">
				<table style='width: 570px; height: 370px;' border=1 cellpadding=0 cellspacing=0>
						<tr>
						<td style='width: 120px;'><b>Time</b><br />
					</td>
					<td id="perDocTime" style='width: 450px;'>2013/07/18 14:20:35 </td></tr>
					<tr>
						<td style='width: 150px;'><b>Description</b> <br />
					</td>
					<td id="perDocDescription">There is not field name in the table.</td> 	</tr>
					<tr>
						<td style='width: 150px;'>Level<br />
					</td>
					<td id="perDocLevel">ERROR</td>	</tr>
					<tr>
						<td style='width: 150px;'>Class Name <br />
					</td>
					<td id="perDocClassName">jdbc:postgresql://localhost:5432/ENdoSnipe-db</td></tr>
					<tr>
						<td style='width: 150px;'>Method Name<br />
					</td>
					<td id="perDocMethodName">select * from emp where empname="JOHN"</td>	</tr>
					<tr>
						<td style='width: 150px;'>Log File Name <br />
					</td>
					<td id="perDocLogFileName">20130512_133200-20130514_133200.jvn	</td></tr>
						<tr>
						<td style='width: 150px;' valign="top">Log Detail <br />
					</td>
					<td id="perDocDetail" valign="top">
				   <c:out value="Event, 2013/06/25 13:26:32, FullScan,select * from emp where empname=JOHN<br/><<javelin.EventInfo_START>></br>javelin.jdbc.fullScan.tableName=dept,emp<br/>javelin.jdbc.fullScan.duration=241
					<br/>javelin.jdbc.fullScan.stackTrace=[STACKTRACE] Get a stacktrace."></c:out>
					<!-- Event, 2013/06/25 13:26:32, "FullScan","select * from emp where empname="JOHN"<br/>&lt;&lt;javelin.EventInfo_START&gt;&gt;</br>javelin.jdbc.fullScan.tableName=dept,emp<br/>javelin.jdbc.fullScan.duration=241
					<br/>javelin.jdbc.fullScan.stackTrace=[STACKTRACE] Get a stacktrace. --></td>	
					</tr>
				
				</table>
			</div>
		</div>
	</div>
</body>
</html>