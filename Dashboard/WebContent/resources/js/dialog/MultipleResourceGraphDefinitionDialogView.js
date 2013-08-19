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
		
		// this.getAllMeasurementItem_();
		this.getAllMeasurement_();
		
		
		
			    $("select option").attr( "title", "" );
			    $("select option").each(function(i){
			      this.title = this.text;
			    });
			

			  // Attach a tooltip to select elements
			// $("select").tooltip({
				  
			   /*
				 * left: "auto", color: "black"
				 */
			 // });

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
	
		
		/*
		 * $("#multipleResourceGraphLstBox1").hover(function (e) {
		 * 
		 * var $target = $(e.target); if($target.is('option')){ //
		 * $target.attr("id",$target.val()); alert($target.attr("id"));//Will
		 * alert id if it has id attribute $($target.attr("id")).attr() //
		 * alert($target.text());//Will alert the text of the option //
		 * alert($target.val());//Will alert the value of the option } });
		 */
		$("#multipleResourceGraphSelection").attr('checked', 'checked');
		$('#multipleResourceGraphLstBox1').removeAttr("disabled");
		$('#multipleResourceGraphLstBox2').removeAttr("disabled");
		$('#multipleResourceGraphItems').attr("disabled",true);
		
		if (ENS.multipleResourceGraphDialog.isFirst === true) {
			
			$("#multipleResourceGraphSelection, #multipleResourceGraphRegExpression")
			.change(function(event) {
					/* $('#multipleResourceGraphPatternTextValue').removeAttr("disabled"); */
			if ($('#multipleResourceGraphSelection').attr("checked")) {
				
				// $('#multipleResourceGraphLstBox1').disabled= true;
				$('#multipleResourceGraphLstBox1').removeAttr("disabled");
				$('#multipleResourceGraphItems').attr("disabled",true);
				// alert("a");
				
			}
			
			else if($('#multipleResourceGraphRegExpression').attr("checked")) {
				// $('#multipleResourceGraphPatternTextValue').hidden= true;
				$('#multipleResourceGraphItems').removeAttr("disabled");
				$('#multipleResourceGraphLstBox1').attr("disabled",true);
				$('#multipleResourceGraphLstBox2').attr("disabled",true);
			// alert("b");
			}
			
					});
		
		/*	$('#link2').click(function() {
				
				var count=2*10;
				var end;
				if(count>ins.measurementDefinitionList.length)
					{
					end=ins.measurementDefinitionList.length;
					}
				else
					{
					end=count;
					}
				var start=(2-1)*10;
				for(i=start;i<end;i++)
					{
					$('#multipleResourceGraphLstBox1').append("<option value='" + ins.measurementDefinitionList.get(i).itemName  + "'>" + ins.measurementDefinitionList.get(i).itemName + "</option>");
										
					}
			
			});*/

			$('#btnAddGraph').click(function(e) {
				
				var selectedOpts = $('#multipleResourceGraphLstBox1 option:selected');
				if (selectedOpts.length === 0) {
					alert("Nothing to move.");
					e.preventDefault();
				}
				$('#multipleResourceGraphLstBox2').append($(selectedOpts).clone());
				$(selectedOpts).remove();
				e.preventDefault();
			});
			
			$('#btnRemoveGraph').click(function(e) {
				var selectedOpts = $('#multipleResourceGraphLstBox2 option:selected');
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
					ins.inputMulResGraphDialog_();
					// alert("button");
					}
				/*
				 * var multipleResourceGraphName =
				 * $("#multipleResourceGraphName").val(); if
				 * (multipleResourceGraphName === "") { alert("Please input
				 * 'Summary Graph Name'."); return; } else if
				 * (multipleResourceGraphName.match(/[\\\/]/)) { alert("Don't
				 * use '/'or'\\' in 'Summary Graph Name'."); return; }
				 */

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
	//$('#multipleResourceGraphLstBox2').empty();
	// 非同期通信でデータを送信する
	var ajaxHandler = new wgp.AjaxHandler();
	settings[wgp.ConnectionConstants.SUCCESS_CALL_OBJECT_KEY] = this;
	settings[wgp.ConnectionConstants.SUCCESS_CALL_FUNCTION_KEY] = "callbackGetAllMeasurement_";
	ajaxHandler.requestServerAsync(settings);
},
callbackGetAllMeasurement_ : function(measurementDefinitionList) {
/*
 * var instance = this; var addOptionList = [];
 * 
 * 
 * _.each(measurementDefinitionList, function(measurementDefinition, index) {
 * //addOptionList.push(signalDefinition); var
 * measurementDataList=measurementDefinition.measurementItemName; //var
 * measurementDataList=measurementDefinition.measurementItemName.signalName; var
 * measurementData=measurementDataList[measurementDataList.length-1];
 * $('#multipleResourceGraphLstBox1').append("<option value='" +
 * measurementData + "'>" + measurementData + "</option>");
 */
		
		var instance = this;
		var addOptionList = [];
		
		
		/*
		 * _.each(measurementDefinitionList, function(signalDefinition, index) {
		 * //addOptionList.push(signalDefinition); var
		 * signalDataList=signalDefinition.signalName; var
		 * signalData=signalDataList[signalDataList.length-1];
		 * $('#multipleResourceGraphLstBox1').append("<option value='" +
		 * signalDefinition.signalName + "'>" + signalDefinition.signalName + "</option>");
		 * $('#multipleResourceGraphLstBox1').append("<option value='" +
		 * signalDefinition.signalName + "'>" + signalDefinition.signalName + "</option>");
		 * $('#multipleResourceGraphLstBox1').append("<option value='" +
		 * signalDefinition.signalName + "'>" + signalDefinition.signalName + "</option>");
		 * 
		 * });
		 */
		
			var lengthList=measurementDefinitionList.length;
			

				lengthList=lengthList/10;
				
				for(i=1;i<=lengthList;i++)
					{
					$('#multipleResourceGraphlink').append("<a href=# class=dialogValue id=link"+ i +">"+i+"</a>&nbsp;");
					
					}
						
		var count=0;
		_.each(measurementDefinitionList, function(javelinMeasurementItem, index) {
			// addOptionList.push(signalDefinition);
		
			if(count<10)
				{
				$('#multipleResourceGraphLstBox1').append("<option value='" + javelinMeasurementItem.itemName  + "'>" + javelinMeasurementItem.itemName + "</option>");
				
				}
			count++;
			
			
		
});

},
inputMulResGraphDialog_ : function() {
	// Ajax通信用の送信先URL
	
	var settings = {
		url : ENS.tree.MEASUREMENT_ITEM_SELECT_ALL_URL
		
	}

	var ajaxHandler = new wgp.AjaxHandler();
	var result = ajaxHandler.requestServerSync(settings);
	var measurementDefinitionList = JSON.parse(result);
	var multipleResourceGraphItems = $("#multipleResourceGraphItems").val();
	
	_.each(measurementDefinitionList, function(javelinMeasurementItem, index) {
		// addOptionList.push(signalDefinition);
		
	//	/[\\\/]/
		
		//multipleResourceGraphItems.match(javelinMeasurementItem.itemName)
		if(javelinMeasurementItem.itemName.match(multipleResourceGraphItems))
			{
	$('#multipleResourceGraphLstBox2').append("<option value='" + javelinMeasurementItem.itemName  + "'>" + javelinMeasurementItem.itemName + "</option>");
			}
		else
			{
			
			var a=javelinMeasurementItem.itemName;
			}

	});
}


});