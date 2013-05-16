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
															.match(/[\\\/]/)) {
														alert("Don't use '/'or'\\' in 'Report Name'.");
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

													$("#" + option.dialogId)
															.dialog("close");
													if (!ins.op_[okObj]) {
														return;
													}
													if (!ins.op_[okObj][ins.op_[okName]]) {
														return;
													}
													ins.op_[okObj][ins.op_[okName]]
															(event, ins.op_);
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
									modal : true,
									width : 350
								});
			},
			createDatepicker_ : function() {
				var dates = $(
						'#jquery-ui-datepicker-from, #jquery-ui-datepicker-to')
						.datetimepicker(
								{
									changeMonth : true,
									numberOfMonths : 2,
									showCurrentAtPos : 1,
									onSelect : function(selectedDate) {
										var option = this.id == 'jquery-ui-datepicker-from' ? 'minDate'
												: 'maxDate', instance = jQuery(
												this).data('datetimepicker'), date = jQuery.datetimepicker
												.parseDate(
														instance.settings.dateFormat
																|| jQuery.datetimepicker._defaults.dateFormat,
														selectedDate,
														instance.settings);

										dates.not(this).datetimepicker(
												'option', option, date);
									}
								});

				$("#jquery-ui-datepicker-from, #jquery-ui-datepicker-to")
						.datetimepicker("option", "showOn", 'both');

			}
		});