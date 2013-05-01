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
				
				$("#" + option.dialogId).dialog({
					buttons : [ {
						text : "OK",
						click : function(event) {
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