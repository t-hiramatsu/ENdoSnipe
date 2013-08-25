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
ENS.ResourceGraphElementView = wgp.DygraphElementView
		.extend({
			initialize : function(argument, treeSettings) {
				this.isRealTime = true;
				this._initData(argument, treeSettings);

				var appView = new ENS.AppView();
				appView.addView(this, argument.graphId);

				this.registerCollectionEvent();

				if (!this.noTermData) {
					appView.getTermData([ this.graphId ], this.timeStart,
							this.timeEnd);
				}

				var realTag = $("#" + this.$el.attr("id"));
				if (this.width == null) {
					this.width = realTag.width();
				} else {
					realTag.width(this.width);
				}
				if (this.height == null) {
					this.height = realTag.height();
				} else {
					realTag.height(this.height);
				}

				$("#" + this.$el.attr("id")).attr("class", "graphbox");
				$("#" + this.$el.attr("id")).css({
					margin : "10px",
					float : "left"
				});

				this.render();

				this.windowResize();
				this.addDragEvent();
			},
			_initData : function(argument, treeSettings) {
				this.viewType = wgp.constants.VIEW_TYPE.VIEW;
				this.collection = new ENS.ResourceGraphCollection();
				this.cid = argument["cid"];
				this.parentId = argument["parentId"];
				this.term = argument.term;
				this.noTermData = argument.noTermData;
				this.graphId = argument["graphId"];

				if (argument["title"].indexOf(":") != -1) {
					var splitTitle = argument["title"].split(":");
					this.title = splitTitle[0];
					this.labelY = splitTitle[1];
				} else {
					this.title = argument["title"];
					this.labelY = "value";
				}
				this.width = argument["width"];
				this.height = argument["height"];
				this.labelX = "time";
				this.rootView = argument["rootView"];
				this.graphHeight = this.height
						- ENS.nodeinfo.GRAPH_HEIGHT_MARGIN;
				this.dateWindow = argument["dateWindow"];
				this.maxId = 0;
				// 15秒に一度の更新なので、1時間だと4×60のデータがグラフ内に入る
				this.graphMaxNumber = 4 * 60;
				this.maxValue = 1;// argument.maxValue;
				this.timeStart = new Date(new Date().getTime() - this.term
						* 1000);
				this.timeEnd = new Date();
				this.timeFrom = 0;
				this.siblingNode = argument["siblingNode"];
				this.fromScale = undefined;
				this.toScale = undefined;
				this.noOfGraph = argument["displayNo"];

				var graphId = this.$el.attr("id") + "_ensgraph";

				this.maximumButton = "maximum_button_" + graphId;

				this.normalButton = "normalization_button_" + graphId;

				this.maximumButtonImg = "<div id ='" + this.maximumButton
						+ "' class = '" + this.maximumButton
						+ "' style='z-index: -10;padding-top : 4px;'><img "
						+ "src='./resources/images/maximum_button.png' "
						+ "style='width: 18px; height: 18px;'></img></div>";

				this.normalButtonImg = "<div id ='" + this.normalButton
						+ "' class = '" + this.normalButton
						+ "' style='z-index: -10;padding-top : 4px;'><img "
						+ "src='./resources/images/normalization_button.png' "
						+ "style='width: 18px; height:18px;'></img></div>";
			},
			render : function() {
				var instance = this;
				var graphPath = this.graphId;
				var graphId = this.$el.attr("id") + "_ensgraph";
				var graphdiv = $("<div id='" + graphId + "'><div>");
				$("#" + this.$el.attr("id")).append(graphdiv);

				var labelId = this.$el.attr("id") + "_enslabel";
				var labeldiv = $("<div id='" + labelId
						+ "' class='ensLabel'><div>");
				$("#" + this.$el.attr("id")).append(labeldiv);
				var labelDom = document.getElementById(labelId);

				var data = this.getData();
				var optionSettings = {
					valueRange : [ 0, this.maxValue * 1.1 ],
					title : this.title,
					xlabel : this.labelX,
					ylabel : this.labelY,
					axisLabelColor : "#000000",
					labelsDivStyles : {
						background : "none repeat scroll 0 0 #000000"
					},
					dateWindow : this.dateWindow,
					axisLabelFontSize : 10,
					titleHeight : 22
				};

				this.attributes = undefined;
				var attributes = this.getAttributes(ENS.ResourceGraphAttribute);

				optionSettings = $.extend(true, optionSettings, attributes);
				optionSettings.labelsDiv = labelDom;

				optionSettings.title = optionSettings.title.split("&#47;")
						.join("/");
				var tmpTitle = optionSettings.title;
				var isShort = false;
				if (optionSettings.title.length > ENS.nodeinfo.GRAPH_TITLE_LENGTH) {
					optionSettings.title = optionSettings.title.substring(0,
							ENS.nodeinfo.GRAPH_TITLE_LENGTH)
							+ "......";
					isShort = true;
				}

				var element = document.getElementById(graphId);
				this.entity = new Dygraph(element, data, optionSettings);
				this.entity.resize(this.width, this.graphHeight);
				$("#" + graphId).height(this.height);

				if (this.labelY == "%") {
					this.getGraphObject().updateOptions({
						valueRange : [ 0, 105 ]
					});
				} else {
					this.getGraphObject().updateOptions({
						valueRange : [ 0, this.maxValue * 1.1 ]
					});
				}

				if ($("#" + this.maximumButton).length > 0) {
					$("#" + this.maximumButton).remove();
				}

				var childElem = $("#" + graphId + "").children("div");
				$(childElem).append(this.maximumButtonImg);
				$("#" + this.maximumButton).css("padding-left",
						($("#" + graphId).width() - 20));
				var minButton = this.normalButton, maxButton = this.maximumButton;

				$(element)
						.click(
								function(e) {
									var offsetLeft = $("#" + graphId).offset().left;
									var offsetTop = $("#" + graphId).offset().top;
									var position = {
										x : Math.floor(e.clientX - offsetLeft),
										y : Math.floor(e.clientY - offsetTop)
									}

									if (position.x > ($("#" + graphId).width() - 20)
											&& position.x < ($("#" + graphId)
													.width() - 7)
											&& position.y > 4
											&& position.y < 19) {
										if ($("." + maxButton).length > 0) {
											instance.addMaximizeEvent(
													offsetLeft, offsetTop);
										}
									}
								});

				this.getGraphObject().updateOptions({
					dateWindow : this.dateWindow,
					axisLabelFontSize : 10,
					titleHeight : 22
				});

				this.mouseEvent(graphId, isShort, tmpTitle, optionSettings);

			},
			mouseEvent : function(graphId, isShort, tmpTitle, optionSettings) {
				var graphPath = this.graphId;
				$("#" + graphId).mouseover(function(event) {
					var target = event.target;
					$("#" + graphId).attr("title", graphPath);
					if (!isShort) {
						return;
					}
					if ($(target).hasClass("dygraph-title")) {
						$(target).text(tmpTitle);
						$(target).parent("div").css('z-index', "1");
					}
				});
				$("#" + graphId).mouseout(function(event) {
					$("#" + graphId).removeAttr("title");
					if (!isShort) {
						return;
					}
					var target = event.target;
					if ($(target).hasClass("dygraph-title")) {
						$(target).text(optionSettings.title);
						$(target).parent("div").css('z-index', "0");
					}
				});
			},
			onAdd : function(graphModel) {

				if (this.isRealTime) {
					if (this.collection.length > this.graphMaxNumber) {
						this.collection
								.shift(wgp.constants.BACKBONE_EVENT.SILENT);
					}
					this.data = this.getData();
					var tempStart;
					var tempEnd;
					var time = new Date();
					if (this.timeFrom === 0) {
						tempEnd = time;
						tempStart = new Date(new Date().getTime() - this.term
								* 1000);
					} else {
						tempEnd = time;
						tempStart = new Date(time.getTime() - this.timeFrom);
					}

					var updateOption;
					if (this.labelY == "%") {
						updateOption = {
							valueRange : [ 0, 105 ],
							'file' : this.data
						};
					} else {
						updateOption = {
							'file' : this.data,
							'valueRange' : [ 0, this.maxValue * 1.1 ]
						};
					}
					if (this.data.length !== 0) {
						updateOption['dateWindow'] = [ tempStart, tempEnd ];
					}
					this.entity.updateOptions(updateOption);

					var graphId = this.$el.attr("id") + "_ensgraph";

					if ($("#tempDiv").length > 0) {
						if (this.tempGraphId != undefined) {
							this.tempEntity.updateOptions(updateOption);
						}
						$(".dygraph-title").width(
								($("#tempDiv").width() * 0.977) - 67);
					} else {
						$(".dygraph-title").width(
								($("#" + graphId).width() - 87));
					}
				}
			},
			addCollection : function(dataArray) {
				if (dataArray != null) {
					var instance = this;
					_.each(dataArray, function(data, index) {
						var model = new instance.collection.model({
							dataId : instance.maxId,
							data : data
						});
						instance.collection.add(model,
								wgp.constants.BACKBONE_EVENT.SILENT);
						instance.maxId++;
					});
				}
			},
			_getTermData : function() {
				this.data = this.getData();

				if (this.data.length !== 0) {
					this.maxValue = this.getMaxValue(this.data);
				}
				var updateOption;
				if (this.labelY == "%") {
					updateOption = {
						valueRange : [ 0, 105 ],
						'file' : this.data
					};
				} else {
					updateOption = {
						valueRange : [ 0, this.maxValue * 1.1 ],
						'file' : this.data
					};
				}

				this.entity.updateOptions(updateOption);
				if ($("#tempDiv").length > 0) {
					if (this.tempGraphId != undefined) {
						this.tempEntity.updateOptions(updateOption);
					}
				}

				var tmpAppView = new ENS.AppView();
				tmpAppView.syncData([ this.graphId ]);
			},
			onComplete : function(syncType) {
				if (syncType == wgp.constants.syncType.SEARCH) {
					this._getTermData();
				}
				var graphId = this.$el.attr("id") + "_ensgraph";

				if ($("#tempDiv").length > 0) {
					$(".dygraph-title").width(
							($("#tempDiv").width() * 0.977) - 67);
				} else {

					$(".dygraph-title").width(($("#" + graphId).width() - 87));
				}
			},
			getData : function() {

				var data = [];
				var instance = this;
				data.push([ new Date(0), null, null, null, null, null, null,
						null, null, null, null, null, null, null, null, null ]);
				_.each(this.collection.models, function(model, index) {
					data.push(instance._parseModel(model));
				});
				return data;
			},
			getMaxValue : function(dataList) {
				var maxValue = 0;

				_.each(dataList, function(data, index) {
					var value = data[1];

					if (value) {
						if (value > maxValue) {
							maxValue = value;
						}
					}
				});

				if (maxValue === 0) {
					maxValue = 1;
				}

				return maxValue;
			},
			getRegisterId : function() {
				return this.graphId;
			},
			getGraphObject : function() {
				return this.entity;
			},
			updateDisplaySpan : function(from, to) {
				var startDate = new Date().getTime() - from;
				var endDate = new Date().getTime() - to;

				if ($("#tempDiv").length > 0) {
					if (this.tempGraphId != undefined) {
						this.tempEntity.updateOptions({
							dateWindow : [ startDate, endDate ]
						});
					}
				}

				this.getGraphObject().updateOptions({
					dateWindow : [ startDate, endDate ]
				});

				this.fromScale = startDate;
				this.toScale = endDate;

			},
			updateGraphData : function(graphId, from, to) {
				if (to === 0) {
					this.isRealTime = true;
				} else {
					this.isRealTime = false;
				}

				var startTime = new Date(new Date().getTime() - from);
				var endTime = new Date(new Date().getTime() - to);
				this.timeStart = startTime;
				this.timeEnd = endTime;
				this.timeFrom = from;
				appView.getTermData([ graphId ], startTime, endTime);
			},
			_parseModel : function(model) {
				var timeString = model.get("measurementTime");
				var time = parseInt(timeString, 10);
				var date = new Date(time);
				var valueString = model.get("measurementValue");
				var value = parseFloat(valueString);
				if (this.maxValue < value) {
					this.maxValue = value;
				}
				return [ date, value ];
			},
			change : function() {
				// TODO 実装内容検討
			},
			remove : function() {
				this.destroy();
				$("#" + this.$el.attr("id")).remove();
			},
			move : function(pointX, pointY){
				var divArea = $("#" + this.$el.attr("id"));
				var offsetParent = divArea.offsetParent().offset();
				divArea.offset({
					top : pointY + offsetParent.top,
					left : pointX + offsetParent.left
				});
			},
			resize : function(changeWidth, changeHeight) {

				this.width = this.width + changeWidth;
				this.height = this.height + changeHeight;

				this.graphHeight = this.height
						- ENS.nodeinfo.GRAPH_HEIGHT_MARGIN;
				this.entity.resize(this.width, this.graphHeight);
			},
			setModelPosition : function(property){

				this.model.set({
					pointX : property.pointX,
					pointY : property.pointY,
					width  : property.width,
					height : property.height
				},{
					silent : true
				});
				this.move(property.pointX, property.pointY);
				this.resize(this.width - property.width, this.height - property.height);
			},
			relateContextMenu : function(menuId, option) {
				contextMenuCreator.createContextMenu(this.$el.attr("id"),
						menuId, option);
			},
			/**
			 * 運用時のイベントを設定する。
			 */
			setOperateFunction : function() {

			},
			/**
			 * 編集時のイベントを設定する。
			 */
			setEditFunction : function() {

				var divArea = $("#" + this.$el.attr("id"));
				var instance = this;
				divArea.draggable({
					grid : [16, 16],
					scroll : true,
					drag : function(e, ui) {
						var position = ui.position;
						var positionLeft = position.left;
						var positionTop = position.top;

						var afterWidth = $(e.target).width();
						var afterHeight = $(e.target).height();

						// グラフ分のマップエリア拡張
						resourceMapListView.childView.enlargeMapArea(
								positionLeft, positionTop, afterWidth,
								afterHeight + 10);
					},
					stop : function(e, ui) {
						var position = ui.position;
						var positionLeft = position.left;
						var positionTop = position.top;

						var parentOffset = $(e.target).parent().offset();
						if (position["left"] < 0) {
							$(e.target).offset({
								left : parentOffset.left
							});

							// 再取得
							positionLeft = $(e.target).position().left;
						}

						if (position["top"] < 0) {
							$(e.target).offset({
								top : parentOffset.top
							});

							// 再取得
							positionTop = $(e.target).position().top;
						}

						instance.model.set("pointX", positionLeft, {
							silent : true
						});
						instance.model.set("pointY", positionTop, {
							silent : true
						});
					}
				});

				var beforeWidth = 0;
				var beforeHeight = 0;
				divArea.resizable({
					grid : [16, 16],
					start : function(e, ui) {
						beforeWidth = $(e.target).width();
						beforeHeight = $(e.target).height();

						var parentOffset = $(e.target).parent().offset();
						var position = ui.position;
						$(e.target).offset({
							top : parentOffset.top + position.top,
							left : parentOffset.left + position.left
						});
					},
					resize : function(e, ui) {
						var position = ui.position;
						var positionLeft = position.left;
						var positionTop = position.top;

						var afterWidth = $(e.target).width();
						var afterHeight = $(e.target).height();

						// グラフ分のマップエリア拡張
						resourceMapListView.childView.enlargeMapArea(
								positionLeft, positionTop, afterWidth,
								afterHeight + 10);
					},
					stop : function(e, ui) {
						var afterWidth = $(e.target).width();
						var afterHeight = $(e.target).height();

						var changeWidth = afterWidth - beforeWidth;
						var changeHeight = afterHeight - beforeHeight;

						instance.resize(changeWidth, changeHeight);
						instance.model.set("width", afterWidth, {
							silent : true
						});
						instance.model.set("height", afterHeight, {
							silent : true
						});
					}
				});
			},
			addDragEvent : function() {
				var divArea = $("#" + this.$el.attr("id") + "_ensgraph");
				this.createDragEvent(divArea);
				if (this.tempGraphId != undefined) {
					this.createDragEvent($("#tempDiv_ensgraph"));
				}
			},
			createDragEvent : function(divArea) {
				var instance = this;
				$(divArea)
						.bind(
								'mousedown',
								function(e) {
									$(divArea).bind('mousemove', function(e) {
									});

									$(divArea)
											.bind(
													'mouseup',
													function() {
														$(divArea).unbind(
																'mousemove');
														if ($("#tempDiv").length > 0) {
															$(".dygraph-title")
																	.width(
																			($(
																					"#tempDiv")
																					.width() * 0.977) - 67);
														} else {
															$(".dygraph-title")
																	.width(
																			($(
																					"#"
																							+ instance.$el
																									.attr("id")
																							+ "_ensgraph")
																					.width() - 87));
														}
													});
								});
			},
			windowResize : function() {
				var instance = this;

				var rtime = new Date(1, 1, 2013, 07, 00, 00);
				var timeout = false;
				var delta = 100;
				$(window).resize(function() {
					rtime = new Date();
					if (timeout === false) {
						timeout = true;
						setTimeout(resizeend, delta);
					}
				});

				function resizeend() {
					if (new Date() - rtime < delta) {
						setTimeout(resizeend, delta);
					} else {
						timeout = false;

						var graphId = instance.$el.attr("id") + "_ensgraph";

						if ($("#tempDiv").length > 0) {

							instance.resizeState = true;

							if ($("#" + instance.normalButton).length <= 0) {

								var childElem = $("#" + graphId + "").children(
										"div");
								$(childElem).append(instance.normalButtonImg);
								$("#" + instance.normalButton).show();

								$("#" + instance.normalButton).css(
										"padding-left",
										$("#tempDiv").width() * 0.977);
							}

							$(".dygraph-title").width(
									($("#tempDiv").width() * 0.977) - 67);
						} else {

							if ($("#" + instance.maximumButton).length <= 0) {

								var childElem = $("#" + graphId + "").children(
										"div");
								$(childElem).append(instance.maximumButtonImg);

								$("#" + instance.maximumButton).css(
										"padding-left",
										($("#" + graphId).width() - 20));

							}

							$(".dygraph-title").width(
									($("#" + graphId).width() - 87));
						}
					}
				}

			},

			addMaximizeEvent : function(offsetLeft, offsetTop) {
				var instance = this;

				var divArea = $("#" + this.$el.attr("id"));
				var tempDiv = this.$el.attr("id").substring(0, 29);
				this.tempGraphId = this.$el.attr("id");

				$("#" + tempDiv).append(
						"<div id='tempDiv' class='graphbox'></div>");
				$("#tempDiv")
						.append(
								"<div id='tempDiv_ensgraph' style='height: 200px; width: 260px;'></div>");
				$("#tempDiv").append(
						"<div id='tempDiv_enslabel'class='ensLabel'></div>");
				var data = this.getData();

				var optionSettings = {
					title : this.title,
					xlabel : this.labelX,
					ylabel : this.labelY,
					axisLabelColor : "#000000",
					labelsDivStyles : {
						background : "none repeat scroll 0 0 #000000"
					}
				};

				var attributes = this.getAttributes(ENS.ResourceGraphAttribute);
				var labelDom = document.getElementById("tempDiv_enslabel");

				optionSettings = $.extend(true, optionSettings, attributes);
				optionSettings.labelsDiv = labelDom;

				optionSettings.title = optionSettings.title.split("&#47;")
						.join("/");

				var tmpTitle = optionSettings.title;
				var isShort = false;
				if (optionSettings.title.length > ENS.nodeinfo.GRAPH_TITLE_LENGTH) {
					optionSettings.title = optionSettings.title.substring(0,
							ENS.nodeinfo.GRAPH_TITLE_LENGTH)
							+ "......";
					isShort = true;
				}

				this.tempEntity = new Dygraph(document
						.getElementById("tempDiv_ensgraph"), data,
						optionSettings);

				this.tempEntity.updateOptions({
					dateWindow : this.dateWindow,
					axisLabelFontSize : 10,
					titleHeight : 22
				});

				if (this.fromScale != undefined && this.toScale != undefined) {
					this.tempEntity.updateOptions({
						dateWindow : [ this.fromScale, this.toScale ]
					});
				}

				this.mouseEvent("tempDiv_ensgraph", isShort, tmpTitle,
						optionSettings);

				var graphId = this.$el.attr("id") + "_ensgraph";

				$("#" + graphId).hide();
				$(divArea).hide();

				var graphWidth = parseInt($("#contents_area").width() * 0.9);
				var areaHeight = $("#contents_area_content").height();
				var graphHeight = parseInt(areaHeight * 0.9);

				var resizeStyle = new Array();

				resizeStyle["width"] = graphWidth;
				resizeStyle["height"] = graphHeight;

				$("#tempDiv").css(resizeStyle);
				this.tempEntity.resize(graphWidth, graphHeight);
				this.addDragEvent();

				var divNo = parseInt(this.$el.attr("id").substring(30,
						this.$el.attr("id").length), 10);
				var divCut = this.$el.attr("id").substring(0, 30);

				var hiddenGraph = new Array();

				var start = divNo - (divNo % 12);
				var end = 12 - (divNo % 12);
				var j = divNo - start;

				hiddenGraph[j] = this.$el.attr("id");

				if (this.noOfGraph != 1) {

					if ((divNo + end) > this.noOfGraph) {
						end = this.noOfGraph - divNo;
					}

					var graphPoint = 0;
					for ( var i = start; i < divNo; i++) {
						if (graphPoint == j) {
							graphPoint++;
						}
						hiddenGraph[graphPoint++] = divCut + i;
						$("#" + divCut + i).hide();
						$("#" + divCut + i + "_ensgraph").hide();
					}
					for ( var i = (divNo + 1); i < divNo + end; i++) {
						if (graphPoint == j) {
							graphPoint++;
						}

						hiddenGraph[graphPoint++] = divCut + i;
						$("#" + divCut + i).hide();
						$("#" + divCut + i + "_ensgraph").hide();
					}
				}

				this.hiddenGraph = hiddenGraph;

				$(".dygraph-title").width(($("#tempDiv").width() * 0.977) - 67);
				var childElem = $("#tempDiv_ensgraph").children("div");
				$(childElem).append(this.normalButtonImg);

				$("#" + this.normalButton).css("padding-left",
						$("#tempDiv").width() * 0.977);
				var minButton = this.normalButton;

				$("#tempDiv")
						.click(
								function(e) {
									var offsetLeft = $("#tempDiv_ensgraph")
											.offset().left;
									var offsetTop = $("#tempDiv_ensgraph")
											.offset().top;
									var position = {
										x : Math.floor(e.clientX - offsetLeft),
										y : Math.floor(e.clientY - offsetTop)
									}
									if (position.x > ($("#tempDiv_ensgraph")
											.width() - 20)
											&& position.x < ($(
													"#tempDiv_ensgraph")
													.width() - 7)
											&& position.y > 3
											&& position.y < 21) {
										if ($("." + minButton).length > 0) {
											instance.addNormalizeEvent();
										}
									}
								});

			},
			addNormalizeEvent : function() {
				$("#tempDiv").remove();
				this.tempGraphId = undefined;

				var divArea = $("#" + this.$el.attr("id"));
				var graphId = this.$el.attr("id") + "_ensgraph";

				if (this.resizeState == true) {

					this.resizeState == false;
					if ($("#" + this.maximumButton).length <= 0) {
						var childElem = $("#" + graphId + "").children("div");
						$(childElem).append(this.maximumButtonImg);
					}
					$("#" + this.maximumButton).css("padding-left",
							($("#" + graphId).width() - 20));
				}

				$(".dygraph-title").width(($("#" + graphId).width() - 87));

				if (this.hiddenGraph.length > 0) {
					var startValue = parseInt(this.hiddenGraph[0].substring(30,
							this.hiddenGraph[0].length), 10);
					for ( var i = 0; i < this.hiddenGraph.length; i++) {
						$("#" + this.hiddenGraph[i]).show();
						var graph = this.hiddenGraph[i] + "_ensgraph";
						$("#" + graph).show();
						var ins = ENS.nodeinfo.viewList[this.siblingNode[startValue
								+ i]];
						ins.entity.resize(ins.width, ins.graphHeight);
						if ($("#" + ins.maximumButton).length <= 0) {
							var childElem = $("#" + graph + "").children("div");
							$(childElem).append(ins.maximumButtonImg);

							$("#" + ins.maximumButton).css("padding-left",
									($("#" + graph).width() - 20));
						}
						$(".dygraph-title")
								.width(($("#" + graph).width() - 87));
					}
				}
			},
			isResizable : function(){
				return true;
			},
			setAttributes : function(elementAttributeList){
				//TODO
			}
		});