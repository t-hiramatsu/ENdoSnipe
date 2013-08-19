<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/Dialog/MultipleResourceGraphDialog.css"
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
				<input type="text" id="multipleResourceGraphId" class="multipleResourceGraphValue"
					value="" />
			</div>
		</div>
		<div id="multipleResourceGraphNameArea" class="dialogContentArea">
			<div id="multipleResourceGraphNameItem" class="dialogItem">Multiple Resource Graph Name
				:</div>
			<div id="multipleResourceGraphValue" class="dialogValue">
				<input type="text" id="multipleResourceGraphName" class="multipleResourceGraphValue"
					value="new Multiple Resource Graph" /> <input type="hidden"
					id="beforemultipleResourceGraphName" class="multipleResourceGraphValue" value="" />
			</div>
		</div>

		<div id="multipleResourceGraphPatternValueArea" class="dialogContentArea">
			<div id="multipleResourceGraphPatternValueValue" class="dialogValue">
				<table style='width: 400px; height: 250px;'>
				<tr><td colspan="3">
				<div id="multipleResourceGraphPatternGraphRadio" class="dialogValue">
				<input  style='margin-left: 0px' type="radio" name="multiple_Resource_Graph_Resource" id="multipleResourceGraphSelection" value="GraphSelection" checked /><!-- checked  -->Graph Selection
				</div>
				</td></tr>
				<tr>
						<td style='width: 200px;'>Graph Lists:<br /> <select
							style='width: 150px; height: 250px;' multiple="multiple"
							id='multipleResourceGraphLstBox1'>
							<!-- <option value="graph1">/default/127.0.0.1/agent_000/common/fundamental/time/acquired</option>
							<option value="graph2">/default/127.0.0.1/agent_000/javelin/javelin/converter/methods/converted</option>
							<option value="graph3">/default/127.0.0.1/agent_000/javelin/javelin/converter/methods/excluded</option>
							<option value="graph1">/default/127.0.0.1/agent_000/common/fundamental/time/acquired</option>
							<option value="graph2">/default/127.0.0.1/agent_000/javelin/javelin/converter/methods/converted</option>
							<option value="graph3">/default/127.0.0.1/agent_000/javelin/javelin/converter/methods/excluded</option>
							<option value="graph1">/default/127.0.0.1/agent_000/common/fundamental/time/acquired</option>
							<option value="graph2">/default/127.0.0.1/agent_000/javelin/javelin/converter/methods/converted</option>
							<option value="graph3">/default/127.0.0.1/agent_000/javelin/javelin/converter/methods/excluded</option>
							<option value="graph1">/default/127.0.0.1/agent_000/common/fundamental/time/acquired</option>
							<option value="graph2">/default/127.0.0.1/agent_000/javelin/javelin/converter/methods/converted</option>
							<option value="graph3">/default/127.0.0.1/agent_000/javelin/javelin/converter/methods/excluded</option>
							<option value="graph1">/default/127.0.0.1/agent_000/common/fundamental/time/acquired</option>
							<option value="graph2">/default/127.0.0.1/agent_000/javelin/javelin/converter/methods/converted</option>
							<option value="graph3">/default/127.0.0.1/agent_000/javelin/javelin/converter/methods/excluded</option>
							<option value="graph1">/default/127.0.0.1/agent_000/common/fundamental/time/acquired</option>
							<option value="graph2">/default/127.0.0.1/agent_000/javelin/javelin/converter/methods/converted</option>
							<option value="graph3">/default/127.0.0.1/agent_000/javelin/javelin/converter/methods/excluded</option>
						 -->
						</select></td>
						<td
							style='width: 50px; height: 250px; text-align: center; vertical-align: middle;'>
							<input type='button' id='btnAddGraph' value='    Add >    ' /> <br />
							<input type='button' id='btnRemoveGraph' value=' < Remove ' />
						</td>
						<td style='width: 150px;'>Selected Graphs:<br /> <select
							style='width: 150px; height: 250px;' multiple="multiple"
							id='multipleResourceGraphLstBox2'>
							<!-- <option value="graph3">/default/127.0.0.1/agent_000/javelin/javelin/converter/methods/excluded</option>
							<option value="graph1">/default/127.0.0.1/agent_000/common/fundamental/time/acquired</option>
						 -->
						</select></td>
					</tr>
					<tr><td colspan="3">
					<div id="multipleResourceGraphlink" class="dialogValue">
				<!-- 	<a href="#">1</a>&nbsp;<a href="#">2</a>&nbsp;<a href="#">3</a>&nbsp;<a href="#">4</a>&nbsp;<a href="#">5</a>&nbsp;<a href="#">...</a> --></div></td></tr>
				
				<tr><td colspan="3">
				<div id="multipleResourceGraphPatternTextRadio" class="dialogValue">
				<input style='margin-left: 0px' type="radio" name="multiple_Resource_Graph_Resource" id="multipleResourceGraphRegExpression" value="RegExpression" />Regular Expression
				</div>
				</td></tr>
				
				<tr><td colspan="2">	
			<div id="multipleResourceGraphPatternTextValue" class="dialogValue">
				<input type="text" id="multipleResourceGraphItems" class="multipleResourceGraphItemsValue"/> <!--  disabled="disabled" -->
			
		</div></td><td>
		<div id="multipleResourceGraphPatternTextButton" class="dialogValue">
		<input type='button' id='btnAddGraphText' value='         Add            ' /> <br /></div></td></tr>
				
				
				</table>
			</div>
		</div>
	</div>
</body>
</html>