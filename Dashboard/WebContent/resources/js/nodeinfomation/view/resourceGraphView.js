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
			},
			render : function() {
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
					}
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
				this.getGraphObject().updateOptions({
					valueRange : [ 0, this.maxValue * 1.1 ],
					dateWindow : this.dateWindow,
					axisLabelFontSize : 10,
					titleHeight : 22
				});
				$("#" + graphId).mouseover(function(event) {
					var target = event.target;
					if ($(target).hasClass("dygraph-title")) {
						$("#" + graphId).attr("title", graphPath);
					}
					if (!isShort) {
						return;
					}
					var target = event.target;
					if ($(target).hasClass("dygraph-title")) {
						$(target).text(tmpTitle);
						$(target).parent("div").css('z-index', "1");
					}
				});
				$("#" + graphId).mouseout(function(event) {
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
						tempStart =  new Date(new Date().getTime() - this.term* 1000);
					} else{
						tempEnd = time;
						tempStart = new Date(time.getTime() - this.timeFrom);
					} 
					var updateOption = {
						'file' : this.data,
						'valueRange': [0, this.maxValue* 1.1]
					};
					if (this.data.length !== 0) {
						updateOption['dateWindow'] = [ tempStart, tempEnd ];
					}
					this.entity.updateOptions(updateOption);
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

				var updateOption = {
					valueRange : [ 0, this.maxValue * 1.1 ],
					'file' : this.data
				};
				this.entity.updateOptions(updateOption);

				var tmpAppView = new ENS.AppView();
				tmpAppView.syncData([ this.graphId ]);
			},
			onComplete : function(syncType) {
				if (syncType == wgp.constants.syncType.SEARCH) {
					this._getTermData();
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
				this.getGraphObject().updateOptions({
					dateWindow : [ startDate, endDate ]
				});

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
			resize : function(changeWidth, changeHeight) {

				this.width = this.width + changeWidth;
				this.height = this.height + changeHeight;

				this.graphHeight = this.height
						- ENS.nodeinfo.GRAPH_HEIGHT_MARGIN;
				this.entity.resize(this.width, this.graphHeight);
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
			}
		});