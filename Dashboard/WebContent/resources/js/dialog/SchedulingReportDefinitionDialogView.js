ENS.schedulingReportDialogIsFirst = true;
ENS.SchedulingReportDialogView = ENS.DialogView
		.extend({
			initialize : function(option) {
				var ins = this;
				this.op_ = option;
				this.id = option.dialogId;
				this.signalType = option.signalType;
				var treeId = option.treeId;
				var weeklyList = [ "Monday", "Tuesday", "Wednesday",
						"Thursday", "Friday", "Saturday", "Sunday" ];
				var okName = "okFunctionName";
				var okObj = "okObject";
				var cName = "cancelFunctionName";
				var cObj = "cancelObject";
				var instance = this;
				$("#" + option.dialogId)
						.dialog(
								{
									buttons : [
											{
												text : "OK",
												click : function(event) {

													var schedulingReportName = $(
															"#schedulingReportName")
															.val();
													if (schedulingReportName === "") {
														alert("Please input 'Scheduling Report Name'.");
														return;
													} else if (schedulingReportName
															.match(/[\\\/]/)) {
														alert("Don't use '/'or'\\' in 'Scheduling Report Name'.");
														return;
													}
													if (option.signalType == ENS.tree.EDIT_SCHEDULE_TYPE) {

														var reportFullName = $(
																"#schedulingReportTargetName")
																.val()
																+ "/"
																+ $(
																		"#schedulingReportName")
																		.val();
														var schedulingReportDefinition = {
															reportId : $("#reportId").val(),
															reportName : reportFullName,
															targetMeasurementName : $(
																	"#schedulingReportTargetName")
																	.val(),
															term : $(
																	'input[name=scheduling_report_type]:checked')
																	.val(),
															time : $(
																	"#schedulingReportSelectedTimeId")
																	.val()
																	+ ":"
																	+ $(
																			"#schedulingReportSelectedTimeMinuteId")
																			.val(),
															day : $(
																	"#schedulingReportSelectedWeeklyId")
																	.val(),
															date : $(
																	"#schedulingReportSelectedMonthlyId")
																	.val()
														};
														var callbackFunction = "";
														// Ajax�ʐM�̑��M��URL
														var url = "";
														var sendData = {
															schedulingReportDefinition : JSON
																	.stringify(schedulingReportDefinition)
														};
														callbackFunction = "callbackEditSchedulingReport_";
														url = ENS.tree.SCHEDULE_EDIT_URL;
														var settings = {
															data : sendData,
															url : url
														};
														/* alert(url); */
														/* alert(JSON.stringify(sendData)); */
														// �񓯊�ʐM�Ńf�[�^�𑗐M����
														var ajaxHandler = new wgp.AjaxHandler();
														settings[wgp.ConnectionConstants.SUCCESS_CALL_OBJECT_KEY] = this;
														settings[wgp.ConnectionConstants.SUCCESS_CALL_FUNCTION_KEY] = callbackFunction;
														ajaxHandler
																.requestServerAsync(settings);

														var rowData = $(
																"#reportTable")
																.jqGrid(
																		'getRowData',
																		ENS.schedulingReportDialog.rowId);
														
														rowData.term = schedulingReportDefinition.term;
														rowData.reportName = schedulingReportDefinition.reportName;
														rowData.targetMeasurementName = schedulingReportDefinition.targetMeasurementName;
														rowData.time = schedulingReportDefinition.time;
														if(rowData.term == "WEEKLY")
															{
															rowData.date = "";
															rowData.day = schedulingReportDefinition.day;
															
															}
														else if(rowData.term == "MONTHLY")
															{
															rowData.day = "";
															rowData.date = schedulingReportDefinition.date;
															}
														else if(rowData.term == "DAILY")
															{
															rowData.day = "";
															rowData.date = "";
															}											
														$("#reportTable")
																.jqGrid(
																		'setRowData',
																		ENS.schedulingReportDialog.rowId,
																		rowData);
													}
													/* alert("hi"); */
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
													$("#" + option.dialogId)
													.dialog("close");
													/*
													 * var
													 * schedulingReportSelectedTimeMinuteId = $(
													 * "#schedulingReportSelectedTimeMinuteId")
													 * .val(); if
													 * (schedulingReportSelectedTimeMinuteId
													 * === "") { alert("Please
													 * input 'Report Term'.");
													 * return; } alert("hi");
													 */
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
									width : 550
								});
				$("#schedulingReportDaily").attr('checked', 'checked');
				/* $('#schedulingReportSetTermChoosingItem').empty(); */
				$('#schedulingReportDayAndDate').empty();
				$('#schedulingReportTime').empty();
				instance.createTime_();
				var index;
				if (ENS.schedulingReportDialogIsFirst === true) {
					$(
							"#schedulingReportDaily, #schedulingReportWeekly, #schedulingReportMonthly")
							.change(
									function(event) {
										/*
										 * $(
										 * '#schedulingReportSetTermChoosingItem')
										 * .empty();
										 */
										$('#schedulingReportDayAndDate')
												.empty();
										$('#schedulingReportTime').empty();
										if ($('#schedulingReportDaily').attr(
												"checked")) {
											instance.createTime_();
										} else if ($('#schedulingReportWeekly')
												.attr("checked")) {

											/*
											 * $(
											 * '#schedulingReportSetTermChoosingItem')
											 */
											$('#schedulingReportDayAndDate')
													.append(
															"Day:&nbsp;&nbsp;&nbsp;<select id=schedulingReportSelectedWeeklyId style=width:100px name=schedulingReportSelectedWeeklyId><option value=>Day</option></select>");
											_
													.each(
															weeklyList,
															function(
																	DayDefinition,
																	index) {
																$(
																		'#schedulingReportSelectedWeeklyId')
																		.append(
																				"<option value='"
																						+ DayDefinition
																						+ "'>"
																						+ DayDefinition
																						+ "</option>");

															});
											instance.createTime_();
										} else if ($('#schedulingReportMonthly')
												.attr("checked")) {

											/*
											 * $(
											 * '#schedulingReportSetTermChoosingItem')
											 */
											$('#schedulingReportDayAndDate')
													.append(
															"Date:&nbsp;&nbsp;<select id=schedulingReportSelectedMonthlyId style=width:100px name=schedulingReportSelectedMonthlyId><option value=>Date</option></select>");
											for (index = 1; index < 32; index++) {

												$(
														'#schedulingReportSelectedMonthlyId')
														.append(
																"<option value='"
																		+ index
																		+ "'>"
																		+ index
																		+ "</option>");

											}
											instance.createTime_();
										}

									});

					ENS.schedulingReportDialogIsFirst = false;
				}
			},

			createTime_ : function() {
				var index;
				/* $('#schedulingReportSetTermChoosingItem') */
				$('#schedulingReportTime')
						.append(
								"Time:&nbsp;<select id=schedulingReportSelectedTimeId name=schedulingReportSelectedTimeId><option value=>Hours</option></select>");
				for (index = 0; index < 24; index++) {
					if (index < 10) {
						$('#schedulingReportSelectedTimeId').append(
								"<option value='" + 0 + index + "'>" + 0
										+ index + "</option>");
					} else {
						$('#schedulingReportSelectedTimeId').append(
								"<option value='" + index + "'>" + index
										+ "</option>");

					}
				}

				/* $('#schedulingReportSetTermChoosingItem') */
				$('#schedulingReportTime')
						.append(
								"&nbsp;:&nbsp;<select id=schedulingReportSelectedTimeMinuteId name=schedulingReportSelectedTimeMinuteId><option value=>Minutes</select>");
				for (index = 0; index < 60; index++) {
					if (index < 10) {
						$('#schedulingReportSelectedTimeMinuteId').append(
								"<option >" + 0 + index + "</option>");
					} else {
						$('#schedulingReportSelectedTimeMinuteId').append(
								"<option>" + index + "</option>");
					}

				}
			},
			callbackEditSchedulingReport_ : function(responseDto) {
				alert("addSchedulingReport");
				var result = responseDto.result;

				// �ǉ�����Ɏ��s�����ꍇ�̓��b�Z�[�W��\������B
				if (result === "fail") {
					var message = responseDto.message;
					alert(message);
					return;
				}
				var schedulingReportDefinition = responseDto.data;
				var reportId = schedulingReportDefinition.reportId;
			}

		});