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
	<div id="signalDialog" title="Signal Definition" style="display:none">
		<div id="signalNameArea" class="dialogContentArea" style="display:none">
			<div id="signalIdValue" class="dialogValue">
				<input type="text" id="signalId" class="signalValue" value="" />
			</div>
		</div>
		<div id="signalNameArea" class="dialogContentArea">
			<div id="signalNameItem" class="dialogItem">Signal Name : </div>
			<div id="signalNameValue" class="dialogValue">
				<input type="text" id="signalName" class="signalValue" value="new Signal" />
				<input type="hidden" id="beforeSignalName" class="signalValue" value="" />
			</div>
		</div>
		<div id="signalMatchingPatternArea" class="dialogContentArea">
			<div id="signalMatchingPatternItem" class="dialogItem">Matching Pattern : </div>
			<div id="signalMatchingPatternValue" class="dialogValue">
				<input type="text" id="matchingPattern" class="signalValue" value="" />
			</div>
		</div>
		<div id="signalPatternArea" class="dialogContentArea">
			<div id="signalPatternItem" class="dialogItem">Signal Pattern : </div>
			<div id="signalPatternValue" class="dialogValue">
				<select>
					<option value="3">3 Level</option>
					<option value="5">5 Level</option>
				</select>
			</div>
		</div>
		<div id="signalPatternValueArea" class="dialogContentArea">
			<div id="signalPatternValueItem" class="dialogItem">Threshold Value of each signal levels : </div>
			<div id="signalPatternValueValue" class="dialogValue">
				<div id="signalPatternValue_1" class="dialogPatternValue">
					<div>1 : </div>
					<input type="text" id="patternValue_1" class="signalValue" value="" pattern="^([1-9]\d*|0)(\.\d+)?$" />
				</div>
				<div id="signalPatternValue_2" class="dialogPatternValue">
					<br />
					<div>2 : </div>
					<input type="text" id="patternValue_2" class="signalValue" value="" pattern="^([1-9]\d*|0)(\.\d+)?$" />
				</div>
				<div id="signalPatternValue_3" class="dialogPatternValue">
					<div>3 : </div>
					<input type="text" id="patternValue_3" class="signalValue" value="" pattern="^([1-9]\d*|0)(\.\d+)?$" />
				</div>
				<div id="signalPatternValue_4" class="dialogPatternValue">
					<br />
					<div>4 : </div>
					<input type="text" id="patternValue_4" class="signalValue" value="" pattern="^([1-9]\d*|0)(\.\d+)?$" />
				</div>
				<div id="signalPatternValue_5" class="dialogPatternValue">
					<div>5 : </div>
					<input type="text" id="patternValue_5" class="signalValue" value="" pattern="^([1-9]\d*|0)(\.\d+)?$" />
				</div>
			</div>
		</div>
		<div class="clearFloat"></div>
		<div id="signalEscalationPeriodArea" class="dialogContentArea">
			<div id="signalEscalationPeriodItem" class="dialogItem">Escalation Period : </div>
			<div id="signalEscalationPeriodValue" class="dialogValue">
				<input type="text" id="escalationPeriod" class="signalValue" value="" />
			</div>
			<div id="signalEscalationPeriodUnit">ms</div>
		</div>
	</div>
</body>
</html>