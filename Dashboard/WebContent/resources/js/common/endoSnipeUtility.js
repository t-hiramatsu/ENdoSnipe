/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2012 Acroquest Technology Co.,Ltd.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
ENS.Utility = {};
ENS.Utility.makeLogo = function(id, title) {
	var idName = 'logo';
	$("#" + id)
			.append('<div id="' + idName + '" class="contentHeader" ></div>');
	$('#' + idName).append('<h1>' + title + '</h1>');
	$('#' + idName).append('<div class="logo"></div>');
};

ENS.Utility.makeAnchor = function(cellValue, options, rowObject) {
	var selectValueList = options.colModel.editoptions;
	var val = rowObject.value;
	var rowId = options.rowId;
	var onclick = selectValueList.onclick;
	var linkName = selectValueList.linkName;
	return '<a href="javascript:void(0)" onclick="' + onclick + '(\'' + rowId
			+ '\');">' + linkName + '</a>';
};
ENS.Utility.makeAnchor1 = function(cellValue, options, rowObject) {
	var selectValueList = options.colModel.editoptions;
	var val = rowObject.value;
	var rowId = options.rowId;
	var onclick1 = selectValueList.onclick1;
	var updateLink = selectValueList.updateLink;
	var onclick2 = selectValueList.onclick2;
	var deleteLink = selectValueList.deleteLink;
	return '<a href="javascript:void(0)" id="update" onclick="' + onclick1
			+ '(\'' + rowId + '\');">' + updateLink + '</a>' + '<br>'
			+ '<a href="javascript:void(0)" id="delete" onclick="' + onclick2
			+ '(\'' + rowId + '\');">' + deleteLink + '</a>';
};

ENS.Utility.makeAnchor2 = function(cellValue, options, rowObject) {
	var selectValueList = options.colModel.editoptions;
	var val = rowObject.value;
	var rowId = options.rowId;
	var logFile = rowObject.logFileName;
	var logDetail = rowObject.detailResult;
	var onclick = selectValueList.onclick;
	var linkName = selectValueList.linkName;
	if (logFile == null || logDetail == null) {
		return '<font color=gray>' + linkName + '</font>';
	}
	return '<a href="javascript:void(0)" onclick="' + onclick + '(\'' + rowId
			+ '\');">' + linkName + '</a>';

};

ENS.Utility.getHashAndQuery = function() {
	var result = {};
	
	var hash = location.hash.slice(1);
	var query = {};
	if (!hash) {
		result["hash"] = [];
		result["query"] = query;
		return result;
	}
	var params = hash.split("?");
	result["hash"] = params[0].split("/");
	if (params.length !== 1) {
		var queries = params[1].split("&");
		_.each(queries, function(value, key){
			var keyValue = value.split("=");
			query[keyValue[0]] = keyValue[1];
		});
	}
	result["query"] = query;
	return result;
}