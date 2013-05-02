<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<div id="persArea"></div>
	<input id="treeData" type="hidden" value='${treeData}' />
	<div id="reportDialog" title="Output Report" style="display: none">
		<div id="reportNameArea" class="dialogContentArea"
			style="display: none">
			<div id="reportIdValue" class="dialogValue">
				<input type="text" id="reportId" class="reportValue" value="" />
			</div>
		</div>
		<div id="reportNameArea" class="dialogContentArea">
			<div id="reportNameItem" class="dialogItem">Report Name :</div>
			<div id="reportNameValue" class="dialogValue">
				<input type="text" id="reportName" class="reportValue"
					value="new report" />
			</div>
		</div>
		<div id="reportTargetNameArea" class="dialogContentArea">
			<div id="reportTargetNameItem" class="dialogItem">Target
				Measurement Name :</div>
			<div id="reportTargetNameValue" class="dialogValue">
				<input type="text" id="targetName" class="reportValue" value=""
					readonly="readonly" />
			</div>
		</div>
		<div id="reportSetTermArea" class="dialogContentArea">
			<div id="reportSetTermItem" class="dialogItem">Report Term :</div>
			<div id="reportSetTermValue" class="dialogValue">
				From<br /> <input name="jquery-ui-datepicker-from"
					class="hasDatepicker" id="jquery-ui-datepicker-from" type="text" /><br />To<br />
				<input name="jquery-ui-datepicker-to" class="hasDatepicker"
					id="jquery-ui-datepicker-to" type="text" />
			</div>
		</div>
		<div class="clearFloat"></div>
	</div>
</body>
</html>