ENS.multipleResourceGraphDialog = {};
ENS.tree.measurementDefinitionList = [];
ENS.multipleResourceGraphDialog.isFirst = true;
ENS.tree.doRender = true;

ENS.MultipleResourceGraphDefinitionDialogView = ENS.DialogView.extend({
	initialize : function(option) {
		var ins = this;
		this.op_ = option;
		this.id = option.dialogId;
		this.signalType = option.signalType;
		var treeId = option.treeId;
		this.measurementDefinitionList = [];
		var okName = "okFunctionName";
		var okObj = "okObject";
		var cName = "cancelFunctionName";
		var cObj = "cancelObject";
		//this.getAllMeasurementItem_();
		this.getAllMeasurement_();
		$("#" + option.dialogId).dialog({
			
			buttons : [ {
				text : "OK",
				click : function(event) {

					// シグナル名が空の時はアラートを表示し、再入力を求める。
					var multipleResourceGraphName = $("#multipleResourceGraphName").val();
					if (multipleResourceGraphName === "") {
						alert("Please input 'Summary Graph Name'.");
						return;
					} else if (multipleResourceGraphName.match(/[\\\/]/)) {
						alert("Don't use '/'or'\\' in 'Summary Graph Name'.");
						return;
					}

					$("#" + option.dialogId).dialog("close");
					if (!ins.op_[okObj]) {
						return;
					}
					if (!ins.op_[okObj][ins.op_[okName]]) {
						return;
					}
					ins.op_[okObj][ins.op_[okName]](event, ins.op_);
				}
			}, {
				text : "Cancel",
				click : function(event) {
					$("#" + option.dialogId).dialog("close");
					if (!ins.op_[cObj]) {
						return;
					}
					if (!ins.op_[cObj][ins.op_[cName]]) {
						return;
					}
					ins.op_[cObj][ins.op_[cName]](event, ins.op_);
				}
			} ],
			modal : true,
			width : 350
			
		});
	
		
		/*$("#multipleResourceGraphLstBox1").hover(function (e)
				{
			
				     var $target = $(e.target); 
				     if($target.is('option')){
				    	// $target.attr("id",$target.val());
				         alert($target.attr("id"));//Will alert id if it has id attribute
				         $($target.attr("id")).attr()
				       //  alert($target.text());//Will alert the text of the option
				       //  alert($target.val());//Will alert the value of the option
				     }
				});*/
		$("#multipleResourceGraphSelection").attr('checked', 'checked');
		$('#multipleResourceGraphLstBox1').removeAttr("disabled");
		$('#multipleResourceGraphItems').attr("disabled",true);
		
		if (ENS.multipleResourceGraphDialog.isFirst === true) {
			
			$("#multipleResourceGraphSelection, #multipleResourceGraphRegExpression")
			.change(function(event) {
					/*	$('#multipleResourceGraphPatternTextValue').removeAttr("disabled");*/
			if ($('#multipleResourceGraphSelection').attr("checked")) {
				
				//$('#multipleResourceGraphLstBox1').disabled= true;
				$('#multipleResourceGraphLstBox1').removeAttr("disabled");
				$('#multipleResourceGraphItems').attr("disabled",true);
				//alert("a");
				
			}
			
			else if($('#multipleResourceGraphRegExpression').attr("checked")) {
				//$('#multipleResourceGraphPatternTextValue').hidden= true;
				$('#multipleResourceGraphItems').removeAttr("disabled");
				$('#multipleResourceGraphLstBox1').attr("disabled",true);
			//alert("b");
			}
			
					});
		
			$('#btnAddGraph').click(function(e) {
				
				var selectedOpts = $('#multipleResourceGraphLstBox1 option:selected');
				if (selectedOpts.length === 0) {
					alert("Nothing to move.");
					e.preventDefault();
				}
				$('#multipleResourceMultipleLstBox2').append($(selectedOpts).clone());
				$(selectedOpts).remove();
				e.preventDefault();
			});
			
			$('#btnRemoveGraph').click(function(e) {
				var selectedOpts = $('#multipleResourceMultipleLstBox2 option:selected');
				if (selectedOpts.length === 0) {
					alert("Nothing to move.");
					e.preventDefault();
				}
				$('#multipleResourceGraphLstBox1').append($(selectedOpts).clone());
				$(selectedOpts).remove();
				e.preventDefault();
			});
			
			$('#btnAddGraphText').click(function(e) {
				
				if($('#multipleResourceGraphRegExpression').attr("checked"))
					{
					var multipleResourceGraphItems = $("#multipleResourceGraphItems").val();
					if (multipleResourceGraphItems === "") {
						alert("Please input 'Multiple Resource Graph Items'.");
						return;
					} 
					
					
					
					
					//alert("button");
					}
				/*var multipleResourceGraphName = $("#multipleResourceGraphName").val();
				if (multipleResourceGraphName === "") {
					alert("Please input 'Summary Graph Name'.");
					return;
				} else if (multipleResourceGraphName.match(/[\\\/]/)) {
					alert("Don't use '/'or'\\' in 'Summary Graph Name'.");
					return;
				}*/

			});
			
			ENS.multipleResourceGraphDialog.isFirst = false;
		}
	},

getAllMeasurement_ : function() {
	// シグナル定義を取得する
	// Ajax通信用の設定
	var settings = {
		url : ENS.tree.MEASUREMENT_ITEM_SELECT_ALL_URL
	};
	$('#multipleResourceGraphLstBox1').empty();
	$('#multipleResourceGraphLstBox2').empty();
	// 非同期通信でデータを送信する
	var ajaxHandler = new wgp.AjaxHandler();
	settings[wgp.ConnectionConstants.SUCCESS_CALL_OBJECT_KEY] = this;
	settings[wgp.ConnectionConstants.SUCCESS_CALL_FUNCTION_KEY] = "callbackGetAllMeasurement_";
	ajaxHandler.requestServerAsync(settings);
},
callbackGetAllMeasurement_ : function(measurementDefinitionList) {
/*	var instance = this;
	var addOptionList = [];
	
	
	_.each(measurementDefinitionList, function(measurementDefinition, index) {
			//addOptionList.push(signalDefinition);
		var measurementDataList=measurementDefinition.measurementItemName;
		//var measurementDataList=measurementDefinition.measurementItemName.signalName;
		var measurementData=measurementDataList[measurementDataList.length-1];
		$('#multipleResourceGraphLstBox1').append("<option value='" + measurementData  + "'>" + measurementData + "</option>");
		*/
		
		var instance = this;
		var addOptionList = [];
		
		
		/*_.each(measurementDefinitionList, function(signalDefinition, index) {
				//addOptionList.push(signalDefinition);
			var signalDataList=signalDefinition.signalName;
			var signalData=signalDataList[signalDataList.length-1];
			$('#multipleResourceGraphLstBox1').append("<option value='" + signalDefinition.signalName  + "'>" + signalDefinition.signalName + "</option>");
			$('#multipleResourceGraphLstBox1').append("<option value='" + signalDefinition.signalName  + "'>" + signalDefinition.signalName + "</option>");
			$('#multipleResourceGraphLstBox1').append("<option value='" + signalDefinition.signalName  + "'>" + signalDefinition.signalName + "</option>");
			
	});*/
		
		_.each(measurementDefinitionList, function(javelinMeasurementItem, index) {
			//addOptionList.push(signalDefinition);
		$('#multipleResourceGraphLstBox1').append("<option value='" + javelinMeasurementItem.itemName  + "'>" + javelinMeasurementItem.itemName + "</option>");
				
});

}
}
);