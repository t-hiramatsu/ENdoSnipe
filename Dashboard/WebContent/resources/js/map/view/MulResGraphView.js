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

ENS.tree.measurementDefinitionList = [];
ENS.tree.previousKey = [];

ENS.MultipleResourceGraphElementView = ENS.ResourceGraphElementView
		.extend({
			initialize : function(argument, treeSettings) {
				this.isFirstRender = true;
				this.isRealTime = true;
				this.isFirstCreate = true;
				this.count = 0;
				this._initData(argument, treeSettings);
				// %グラフのY軸の最大値
				this.percentGraphMaxYValue = argument.percentGraphMaxYValue;
				// グラフの上限を決める際の、グラフのY値の最大に対する係数
				this.yValueMagnification = argument.yValueMagnification;
				// 最大化グラフの横のマージン
				this.maxGraphSideMargin = argument.maxGraphSideMargin;
				// 最大化グラフの縦のマージン
				this.maxGraphVerticalMargin = argument.maxGraphVerticalMargin;
				// グラフタイトル横のボタン用スペースの大きさ
				this.titleButtonSpace = argument.titleButtonSpace;

				var graphIds = "(";
				var appView = new ENS.AppView();
				this.getMeasurementTargetNodes(argument.graphId);

				for ( var index = 0; index < ENS.tree.measurementDefinitionList.length; index++) {

					if (index == ENS.tree.measurementDefinitionList.length - 1) {
						graphIds = graphIds
								+ ENS.tree.measurementDefinitionList[index]
								+ ")";
					} else {
						graphIds = graphIds
								+ ENS.tree.measurementDefinitionList[index]
								+ "|";
					}

				}
				appView.addView(this, graphIds);

				this.registerCollectionEvent();

				if (!this.noTermData) {

					appView.getTermData([ graphIds ], this.timeStart,
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
				this.windowResize();
				this.addDragEvent();
			},
			getMeasurementTargetNodes : function(parentId) {

				var settings = {
					url : ENS.tree.MULTIPLE_RESOURCE_GRAPH_GET_URL,
					data : {
						multipleResourceGraphName : parentId
					}
				};

				var ajaxHandler = new wgp.AjaxHandler();
				var result = ajaxHandler.requestServerSync(settings);
				var multipleResourceGraphDefinition = JSON.parse(result);

				ENS.tree.measurementDefinitionList = multipleResourceGraphDefinition.measurementItemIdList
						.split(",");

			},
			keysByValue : function(data) {
				var arrayData = [];
				for ( var key in data) {
					if (data.hasOwnProperty(key)) {
						arrayData.push([ key, data[key] ]);
					}
				}

				arrayData.sort(function(data1, data2) {
					var value1 = data1[1], value2 = data2[1];
					return value2.length - value1.length;
				});
				for ( var index = 0, length = arrayData.length; index < length; index++) {
					arrayData[index] = arrayData[index][0];
				}

				return arrayData;
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
				var dataFinal = this.createDataList(data);

				var optionSettings = {
					labels : this.datalabel,
					valueRange : [ 0, this.maxValue * this.yValueMagnification ],
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

				this.entity = new Dygraph(element, dataFinal, optionSettings);
				this.entity.resize(this.width, this.graphHeight);
				$("#" + graphId).height(this.height);

				if (this.labelY == "%") {
					this.getGraphObject().updateOptions({
						valueRange : [ 0, this.percentGraphMaxYValue ]
					});
				} else {
					this.getGraphObject().updateOptions(
							{
								valueRange : [
										0,
										this.maxValue
												* this.yValueMagnification ]
							});
				}
				if ($("#" + this.maximumButton).length > 0) {
					$("#" + this.maximumButton).remove();
				}

				var childElem = $("#" + graphId + "").children("div");
				$(childElem).append(this.maximumButtonImg);
				$("#" + this.maximumButton).css("float", "right");
				var minButton = this.normalButton, maxButton = this.maximumButton;
				$(element).click(
						function(e) {
							var offsetLeft = $("#" + graphId).offset().left;
							var offsetTop = $("#" + graphId).offset().top;
							var position = {
								x : Math.floor(e.clientX - offsetLeft),
								y : Math.floor(e.clientY - offsetTop)
							};

							if (position.x > ($("#" + graphId).width() - 20)
									&& position.x < ($("#" + graphId).width())
									&& position.y > 0 && position.y < 28) {
								if ($("." + maxButton).length > 0) {
									instance.addMaximizeEvent(offsetLeft,
											offsetTop);
								}
							}
						});
				this.getGraphObject().updateOptions({
					dateWindow : this.dateWindow,
					axisLabelFontSize : 10,
					titleHeight : 22
				});

				$(".dygraph-title").width(
						$("#" + graphId).width() - this.titleButtonSpace);
				this.mouseEvent(graphId, isShort, tmpTitle, optionSettings);
			},

			onAdd : function(graphModel) {
				var data = this.getData();
				var len = Object.keys(data).length;

				if (this.isRealTime) {

					if (this.collection.length > this.graphMaxNumber * len) {
						for ( var index = 0; index < len; index++) {
							var removeIndex = (this.graphMaxNumber * index)
									- (this.count * index) - index;
							if (removeIndex < 0) {
								removeIndex = 0;
							}
							this.collection.remove(this.collection
									.at(removeIndex));
						}

						this.count++;
					}
					data = this.getData();
					var dataList = this.createDataList(data);
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
							valueRange : [ 0, this.percentGraphMaxYValue ],
							'file' : dataList
						};
					} else {
						updateOption = {
							'file' : dataList,
							'valueRange' : [ 0,
									this.maxValue * this.yValueMagnification ]
						};
					}
					if (dataList.length !== 0) {
						updateOption['dateWindow'] = [ tempStart, tempEnd ];
					}
					this.entity.updateOptions(updateOption);

					var graphId = this.$el.attr("id") + "_ensgraph";

					if ($("#tempDiv").length > 0) {
						if (this.tempGraphId !== undefined) {
							this.tempEntity.updateOptions(updateOption);
						}

					}
					$(".dygraph-title").width(
							$("#tempDiv").width() - this.titleButtonSpace);
				}
			},
			_getTermData : function() {

				var graphIds = "(";

				for ( var index = 0; index < ENS.tree.measurementDefinitionList.length; index++) {

					if (index == ENS.tree.measurementDefinitionList.length - 1) {
						graphIds = graphIds
								+ ENS.tree.measurementDefinitionList[index]
								+ ")";
					} else {
						graphIds = graphIds
								+ ENS.tree.measurementDefinitionList[index]
								+ "|";
					}

				}

				var data = this.getData();
				var dataList = this.createDataList(data);
				if (dataList.length !== 0) {
					this.maxValue = this.getMaxValue(dataList);
				}
				var updateOption;
				if (this.labelY == "%") {
					updateOption = {
						valueRange : [ 0, this.percentGraphMaxYValue ],
						'file' : dataList
					};
				} else {
					updateOption = {
						valueRange : [ 0,
								this.maxValue * this.yValueMagnification ],
						'file' : dataList
					};
				}

				this.entity.updateOptions(updateOption);
				if ($("#tempDiv").length > 0) {
					if (this.tempGraphId !== undefined) {
						this.tempEntity.updateOptions(updateOption);
					}
				}

				var tmpAppView = new ENS.AppView();
				tmpAppView.syncData([ graphIds ]);
			},
			onComplete : function(syncType) {

				if (this.isFirstRender === true) {
					this.render();
					this.isFirstRender = false;
				}
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
			getMaxValue : function(dataList) {
				var maxValue = 0;

				_.each(dataList, function(data, index) {
					for ( var i = 1; i < data.length; i++) {
						var value = data[i];

						if (value) {
							if (value > maxValue) {
								maxValue = value;
							}
						}
					}
				});

				if (maxValue === 0) {
					maxValue = 1;
				}

				return maxValue;
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
				var graphIds = "(";
				this.getMeasurementTargetNodes(graphId);

				for ( var index = 0; index < ENS.tree.measurementDefinitionList.length; index++) {

					if (index == ENS.tree.measurementDefinitionList.length - 1) {
						graphIds = graphIds
								+ ENS.tree.measurementDefinitionList[index]
								+ ")";
					} else {
						graphIds = graphIds
								+ ENS.tree.measurementDefinitionList[index]
								+ "|";
					}

				}
				appView.getTermData([ graphIds ], startTime, endTime);
			},
			getData : function() {

				var measurementListMap = {};
				var dataMap = [];
				var data = [];
				var instance = this;
				var measurementItemName;
				var measurmentListm = {};

				_
						.each(
								this.collection.models,
								function(model, index) {
									measurementItemName = model
											.get("measurementItemName");

									if (data[measurementItemName] === undefined) {
										data[measurementItemName] = [];
										data[measurementItemName].push([
												new Date(0), null, null, null,
												null, null, null, null, null,
												null, null, null, null, null,
												null, null ]);
									}

									data[measurementItemName].push(instance
											._parseModel(model));
									measurementListMap[measurementItemName] = data[measurementItemName];

								});

				return measurementListMap;

			},
			addMaximizeEvent : function(offsetLeft, offsetTop) {
				var instance = this;

				var divArea = $("#" + this.$el.attr("id"));
				var tempDiv = this.$el.attr("id").substring(0, 29);
				this.tempGraphId = this.$el.attr("id");

				$("#" + tempDiv)
						.append(
								"<div id='tempDiv' class='graphbox' style='margin: 10px;'></div>");
				$("#tempDiv").append("<div id='tempDiv_ensgraph'></div>");
				$("#tempDiv").append(
						"<div id='tempDiv_enslabel'class='ensLabel'></div>");
				var data = this.getData();
				var dataFinal = this.createDataList(data);
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
						.getElementById("tempDiv_ensgraph"), dataFinal,
						optionSettings);

				this.tempEntity.updateOptions({
					dateWindow : this.dateWindow,
					axisLabelFontSize : 10,
					titleHeight : 22
				});

				if (this.fromScale !== undefined && this.toScale !== undefined) {
					this.tempEntity.updateOptions({
						dateWindow : [ this.fromScale, this.toScale ]
					});
				}

				if (this.labelY == "%") {
					this.tempEntity.updateOptions({
						valueRange : [ 0, this.percentGraphMaxYValue ]
					});
				} else {
					this.tempEntity.updateOptions({
						valueRange : [ 0,
								this.maxValue * this.yValueMagnification ]
					});
				}

				this.mouseEvent("tempDiv_ensgraph", isShort, tmpTitle,
						optionSettings);

				var graphId = this.$el.attr("id") + "_ensgraph";

				$("#" + graphId).hide();
				$(divArea).hide();

				var graphWidth = parseInt($("#contents_area").width()
						- this.maxGraphSideMargin, 10);
				var graphHeight = parseInt($("#contents_area").height()
						- this.maxGraphVerticalMargin, 10);

				var resizeStyle = [];

				resizeStyle["width"] = graphWidth;
				resizeStyle["height"] = graphHeight;

				$("#tempDiv").css(resizeStyle);
				this.tempEntity.resize(graphWidth, graphHeight);
				this.addDragEvent();

				var divNo = parseInt(this.$el.attr("id").substring(30,
						this.$el.attr("id").length), 10);
				var divCut = this.$el.attr("id").substring(0, 30);

				var hiddenGraph = [];

				var start = divNo - (divNo % 12);
				var end = 12 - (divNo % 12);
				var j = divNo - start;

				hiddenGraph[j] = this.$el.attr("id");

				if (this.noOfGraph != 1) {

					if ((divNo + end) > this.noOfGraph) {
						end = this.noOfGraph - divNo;
					}

					var graphPoint = 0;
					for ( var cnt1 = start; cnt1 < divNo; cnt1++) {
						if (graphPoint == j) {
							graphPoint++;
						}
						hiddenGraph[graphPoint++] = divCut + cnt1;
						$("#" + divCut + cnt1).hide();
						$("#" + divCut + cnt1 + "_ensgraph").hide();
					}
					for ( var cnt2 = (divNo + 1); cnt2 < divNo + end; cnt2++) {
						if (graphPoint == j) {
							graphPoint++;
						}

						hiddenGraph[graphPoint++] = divCut + cnt2;
						$("#" + divCut + cnt2).hide();
						$("#" + divCut + cnt2 + "_ensgraph").hide();
					}
				}

				this.hiddenGraph = hiddenGraph;

				$(".dygraph-title").width(
						$("#tempDiv").width() - this.titleButtonSpace);
				var childElem = $("#tempDiv_ensgraph").children("div");
				$(childElem).append(this.normalButtonImg);

				$("#" + this.normalButton).css("float", "right");
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
									};
									if (position.x > ($("#tempDiv_ensgraph")
											.width() - 20)
											&& position.x < ($("#tempDiv_ensgraph")
													.width())
											&& position.y > 0
											&& position.y < 28) {
										if ($("." + minButton).length > 0) {
											instance.addNormalizeEvent();
										}
									}
								});
				// ズームアウト時（ダブルクリック）のイベントを設定。
				$("#tempDiv").dblclick(
						function(event) {
							instance.zoomOut(instance.tempEntity);
							$(".dygraph-title").width(
									$("#tempDiv").width()
											- instance.titleButtonSpace);
						});

			},
			createDataList : function(data) {
				var dataMap = {};
				var dataValue = {};
				dataValue = data;
				var keys = this.keysByValue(data);

				var top;
				if (keys.length > 5) {
					top = 5;
				} else {
					top = keys.length;
				}

				if (this.isFirstCreate) {
					ENS.tree.previousKey = keys;
					this.isFirstCreate = false;
				} else {
					var tempKey = ENS.tree.previousKey.slice();
					for ( var keyIndex = 0; keyIndex < top; keyIndex++) {
						var found = false;
						for ( var iKey = 0; iKey < ENS.tree.previousKey.length; iKey++) {
							if (ENS.tree.previousKey[iKey] === keys[keyIndex]) {
								found = true;

							}
						}
						if (!found) {
							tempKey.push(keys[keyIndex]);
						}

					}

					ENS.tree.previousKey = tempKey.slice();

					for ( var keyIndex2 = 0; keyIndex2 < top; keyIndex2++) {
						tempKey.splice(keys[keyIndex2]);
					}

					for ( var tempIndex = 0; tempIndex < tempKey.length; tempIndex++) {
						ENS.tree.previousKey.splice(tempKey[tempIndex]);
					}
				}

				for ( var keyIndex3 = 0; keyIndex3 < top; keyIndex3++) {
					var value = dataValue[ENS.tree.previousKey[keyIndex3]]/* .slice() */;

					_.each(value, function(valueData, index) {
						var valueD = valueData;
						if (dataMap[valueD[0]] === undefined
								|| dataMap[valueD[0]] === null) {
							dataMap[valueD[0]] = valueD;
						} else if (dataMap[valueD[0]] !== undefined
								|| dataMap[valueD[0]] !== null) {

							var list = dataMap[valueD[0]];
							list.push(valueD[1]);
							dataMap[valueD[0]] = list;
						}
					});
				}

				var dataFinal = [];
				$.map(dataMap, function(value, key) {

					dataFinal.push(value);
				});
				return dataFinal;
			}
		});