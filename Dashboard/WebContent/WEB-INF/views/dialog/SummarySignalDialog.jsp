<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%-- <link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/Dialog/SummarySignalDialog.css"
	type="text/css" media="all"> --%>
</head>
<body>
	<div id="persArea"></div>
	<input id="treeData" type="hidden" value='${treeData}' />
	<div id="summarySignalDialog" title="Summary Signal Definition"
		style="display: none">
		<div id="signalNameArea" class="dialogContentArea"
			style="display: none">
			<div id="summarySignalIdValue" class="dialogValue">
				<input type="text" id="summarySignalId" class="summarySignalValue"
					value="" />
			</div>
		</div>
		<div id="summarySignalNameArea" class="dialogContentArea">
			<div id="summarySignalNameItem" class="dialogItem">Signal Name
				:</div>
			<div id="summarySignalNameValue" class="dialogValue">
				<input type="text" id="summarySignalName" class="summarySignalValue"
					value="new signal" />
				<input type="hidden" id="beforesummarySignalName"
					class="summarySignalValue" value="" />
			</div>
		</div>
		<div id="summarySignalTermArea" class="dialogContentArea">
			<div id="summarySignalTermItem" class="dialogItem">Signal Type
				:</div>

			<div id="summarySignalValue" class="dialogValue">
				<input type="radio" name="summarySignal_type" id="and" value="and"
					checked />
				AND &nbsp;
				<!-- checked  -->
				<input type="radio" name="summarySignal_type" id="or" value="or" />
				OR &nbsp;
			</div>
		</div>
		<div id="summarySignalList" class="dialogContentArea">
			<table style='width: 400px; height: 250 px;'>
				<tr>
					<td style='width: 200px;'>Signal List:<br /> <select
						style='width: 150px; height: 250px;' multiple="multiple"
						id='summarySignalList1Box' class="dialogValue">

					</select>
					</td>
					<td
						style='width: 50px; height: 250px; text-align: center; vertical-align: middle;'>
						<input type='button' id='btnAdd' value='    Add >    ' /> <br />
						<input type='button' id='btnRemove' value=' < Remove ' /></td>
					<td style='width: 220px;'>Selected Signal List:<br /> <select
						style='width: 150px; height: 250px;' multiple="multiple"
						id='summarySignalList2Box' class="dialogValue">

					</select>
					</td>
				</tr>
			</table>
		</div>
		</div>
</body>
</html>