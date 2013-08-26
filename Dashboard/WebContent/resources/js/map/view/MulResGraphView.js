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

ENS.MultipleResourceGraphElementView = ENS.ResourceGraphElementView
		.extend({
			initialize : function(argument, treeSettings) {
				this.isRealTime = true;
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

				var graphIds="(";
				var appView = new ENS.AppView();
				this.getMeasurementTargetNodes(argument.graphId);
			
				for(index=0;index<ENS.tree.measurementDefinitionList.length;index++)
					{
					
					if(index==ENS.tree.measurementDefinitionList.length-1)
						{
						 graphIds=graphIds+ENS.tree.measurementDefinitionList[index]+")";
						}
					else
						{
					 graphIds=graphIds+ENS.tree.measurementDefinitionList[index]+"|";
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

				this.render();

				this.windowResize();
				this.addDragEvent();
			},
					// /////////////////////modify//////////////////////
			getMeasurementTargetNodes : function(parentId) {
				
				var settings = {
						url : ENS.tree.MULTIPLE_RESOURCE_GRAPH_GET_URL,
						data : {
							multipleResourceGraphName : parentId
						}
					}

					var ajaxHandler = new wgp.AjaxHandler();
					var result = ajaxHandler.requestServerSync(settings);
					var multipleResourceGraphDefinition = JSON.parse(result);

					ENS.tree.measurementDefinitionList = multipleResourceGraphDefinition.measurementItemIdList.split(",");

			},
			keysByValue : function (data)
			{
				var arrayData=[];
				for(var key in data)
					{
					if(data.hasOwnProperty(key))
						{
						arrayData.push([key,data[key]]);
						}
					}
				
				arrayData.sort(function (data1,data2){
					var value1=data1[1],value2=data2[1];
					return value2.length-value1.length;
				});
				for(var index=0,length=arrayData.length;index<length;index++)
					{
					arrayData[index]=arrayData[index][0];
					}
				return arrayData;
			},
			// /////////////////////modify//////////////////////
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
								
				var dataMap={};
				var keys=this.keysByValue(data);
				
				var top;
				if(keys.length>5)
					{
					top=5;
					}
				else
					{
					top=keys.length;
					}
				for(var indexKey=0;indexKey<top;indexKey++)
					{
					var value=data[keys[indexKey]];
					_.each(value,function(valueData,index)
							{
						if(dataMap[valueData[0]]==undefined)
							{
							dataMap[valueData[0]]=valueData;
							}
						else
							{
							var list=dataMap[valueData[0]];
							list.push(valueData[1]);
							dataMap[valueData[0]]=list;
							}
							});
					}
				
				var dataFinal=[];
				$.map(dataMap,function(value,key){
					
					dataFinal.push(value);
				});
				
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

				$(element)
						.click(
								function(e) {
									var offsetLeft = $("#" + graphId).offset().left;
									var offsetTop = $("#" + graphId).offset().top;
									var position = {
										x : Math.floor(e.clientX - offsetLeft),
										y : Math.floor(e.clientY - offsetTop)
									};

									if (position.x > ($("#" + graphId).width() - 20)
											&& position.x < ($("#" + graphId)
													.width())
											&& position.y > 0
											&& position.y < 28) {
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
				
				$(".dygraph-title").width($("#" + graphId).width() - this.titleButtonSpace);

				this.mouseEvent(graphId, isShort, tmpTitle, optionSettings);

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

				var measurementListMap = {};
				var data = [];
				var instance = this;
				var measurementItemName;
				var measurmentListm={};
				data.push([ new Date(0), null, null, null, null, null, null,
						null, null, null, null, null, null, null, null, null ]);
				_.each(this.collection.models, function(model, index) {
					
					measurementItemName = model.get("measurementItemName");
					data.push(instance._parseModel(model));
					measurementListMap[measurementItemName] =data;
				});
				
				 return measurementListMap;
				
			}
		});