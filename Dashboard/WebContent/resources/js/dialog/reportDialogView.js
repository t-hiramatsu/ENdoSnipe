ENS.ReportDialogView = ENS.DialogView
		.extend({
			initialize : function(option) {
				var ins = this;
				this.op_ = option;
				this.id = option.dialogId;
				this.signalType = option.signalType;
				var treeId = option.treeId;
				this.signalDefinitionList = [];
				var okName = "okFunctionName";
				var okObj = "okObject";
				var cName = "cancelFunctionName";
				var cObj = "cancelObject";

				this.createDatepicker_();

				$("#" + option.dialogId)
						.dialog(
								{
									buttons : [
											{
												text : "OK",
												click : function(event) {

													// レポート名が空の時はアラートを表示し、再入力を求める。
													var reportName = $(
															"#reportName")
															.val();
													if (reportName === "") {
														alert("Please input 'Report Name'.");
														return;
													} else if (reportName
															.match(/[\\\/\:\;\|\,\*\?/"\<\>]/)) {
														alert("Don't use '/\\:;|,*?\"<>' in 'Report Name'.");
														return;
													}

													// レポート名が空の時はアラートを表示し、再入力を求める。
													var termFrom = $(
															"#jquery-ui-datepicker-from")
															.val();
													var termTo = $(
															"#jquery-ui-datepicker-to")
															.val();
													if (termFrom === ""
															|| termTo === "") {
														alert("Please input 'Report Term'.");
														return;
													}

													var fromDateFormat = termFrom
															.match(/\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) ([01][0-9]|2[0-3]):([0-5][0-9])/);
													var toDateFormat = termTo
															.match(/\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) ([01][0-9]|2[0-3]):([0-5][0-9])/);

													if (fromDateFormat === null
															|| toDateFormat === null) {
														alert("'Report Term' is invalid format.");
														return;
													}

													// 開始時間が終了時間よりも後になっている場合は再入力を求める
													if (termFrom >= termTo) {
														alert("'From Time' should be earlier than the 'To Time'.");
														return;
													}

													if (!ins.op_[okObj]) {
														$("#" + option.dialogId)
																.dialog("close");
														return;
													}
													if (!ins.op_[okObj][ins.op_[okName]]) {
														$("#" + option.dialogId)
																.dialog("close");
														return;
													}
													ins.op_[okObj][ins.op_[okName]]
															(event, ins.op_);

													$("#" + option.dialogId)
															.dialog("close");
												}
											},
											{
												text : "Cancel",
												click : function(event) {
													$("#" + option.dialogId)
															.dialog("close");
													if (!ins.op_[cObj]) {
														return;
													}
													if (!ins.op_[cObj][ins.op_[cName]]) {
														return;
													}
													ins.op_[cObj][ins.op_[cName]]
															(event, ins.op_);
												}
											} ],
									close : function(event) {
										if (!ins.op_[cObj]) {
											return;
										}
										if (!ins.op_[cObj][ins.op_[cName]]) {
											return;
										}
										ins.op_[cObj][ins.op_[cName]](event,
												ins.op_);
									},
									modal : true,
									width : 350
								});
			},
			createDatepicker_ : function() {
				var startDateTextBox = $('#jquery-ui-datepicker-from');
				var endDateTextBox = $('#jquery-ui-datepicker-to');
				
				startDateTextBox.datetimepicker({ 
					numberOfMonths: 2,
					onClose: function(dateText, inst) {
						if (endDateTextBox.val() !== '') {
							var testStartDate = startDateTextBox.datetimepicker('getDate');
							var testEndDate = endDateTextBox.datetimepicker('getDate');
							if (testStartDate > testEndDate)
								endDateTextBox.datetimepicker('setDate', testStartDate);
						}
						else {
							endDateTextBox.val(dateText);
						}
					},
					onSelect: function (selectedDateTime){
						endDateTextBox.datetimepicker('option', 'minDate', startDateTextBox.datetimepicker('getDate') );
					}
				});
				endDateTextBox.datetimepicker({ 
					numberOfMonths: 2,
					onClose: function(dateText, inst) {
						if (startDateTextBox.val() !== '') {
							var testStartDate = startDateTextBox.datetimepicker('getDate');
							var testEndDate = endDateTextBox.datetimepicker('getDate');
							if (testStartDate > testEndDate)
								startDateTextBox.datetimepicker('setDate', testEndDate);
						}
						else {
							startDateTextBox.val(dateText);
						}
					},
					onSelect: function (selectedDateTime){
						startDateTextBox.datetimepicker('option', 'maxDate', endDateTextBox.datetimepicker('getDate') );
					}
				});
			}
		});