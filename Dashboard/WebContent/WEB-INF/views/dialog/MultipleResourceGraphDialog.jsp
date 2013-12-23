<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/dialog/MultipleResourceGraphDialog.css"
	type="text/css" media="all">
</head>
<body>
	<div id="persArea"></div>
	<input id="treeData" type="hidden" value='${treeData}' />
	<div id="multipleResourceGraphDialog" title="Multiple Resource Graph"
		style="display: none">
		<div id="multipleResourceGraphNameArea" class="dialogContentArea"
			style="display: none">
			<div id="multipleResourceGraphIdValue" class="dialogValue">
				<input type="text" id="multipleResourceGraphId"
					class="multipleResourceGraphValue" value="" />
			</div>
		</div>
		<div id="multipleResourceGraphNameArea" class="dialogContentArea">
			<div id="multipleResourceGraphNameItem" class="dialogItem">Multiple
				Resource Graph Name :</div>
			<div id="multipleResourceGraphValue" class="dialogValue">
				<input type="text" id="multipleResourceGraphName"
					class="multipleResourceGraphValue"
					value="new Multiple Resource Graph" style='width: 300px;' /> <input
					type="hidden" id="beforemultipleResourceGraphName"
					class="multipleResourceGraphValue" value="" />
			</div>
		</div>

		<div id="multipleResourceGraphPatternValueArea"
			class="dialogContentArea">
			<div id="multipleResourceGraphPatternValueValue" class="dialogValue">
				<table style='width: 500px; height: auto;'>
					<tr class="selectPlace">
						<td colspan="3">
							<div id="multipleResourceGraphPatternGraphRadio"
								class="dialogValue">
								<input style='margin-left: 0px' type="radio"
									name="multiple_Resource_Graph_Resource"
									id="multipleResourceGraphSelection" value="GraphSelection"
									checked /> Graph Selection
							</div>
						</td>
					</tr>
					<tr class="selectPlace">
						<td style='width: 250px;'>Graph Lists:<br /> <select
							style='width: 200px; height: 250px;' multiple="multiple"
							id='multipleResourceGraphLstBox1' class="dialogValue">

						</select></td>
						<td
							style='width: 50px; height: 250px; text-align: center; vertical-align: middle;'>
							<input type='button' id='btnAddGraph' value='    Add >    ' /> <br />
							<input type='button' id='btnRemoveGraph' value=' < Remove ' />
						</td>
						<td style='width: 250px;'>Selected Graphs:<br /> <select
							style='width: 200px; height: 250px;' multiple="multiple"
							id='multipleResourceGraphLstBox2' class="dialogValue">

						</select></td>
					</tr>
					<tr class="selectPlace">
						<td colspan="3">
							<div id='pagingMeasurement' class='pagination'
								style='padding-left: 10px;'></div>
						</td>
					</tr>

					<tr class="regPlace">
						<td colspan="3">
							<div id="multipleResourceGraphPatternTextRadio"
								class="dialogValue">
								<input style='margin-left: 0px' type="radio"
									name="multiple_Resource_Graph_Resource"
									id="multipleResourceGraphRegExpression" value="RegExpression" />Regular
								Expression
							</div>
						</td>
					</tr>

					<tr class="regPlace">
						<td colspan="2">
							<div id="multipleResourceGraphPatternTextValue"
								class="dialogValue">
								<input type="text" id="multipleResourceGraphItems"
									class="multipleResourceGraphItemsValue" style='width: 300px;' />

							</div>
						</td>
					</tr>

				</table>
			</div>
		</div>

		<select id="hiddenIdList" style="display: none;" class="dialogValue">
		</select>
	</div>
</body>
</html>