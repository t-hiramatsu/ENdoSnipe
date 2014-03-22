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
ENS.GraphControllerView = wgp.AbstractView
		.extend({
			initialize : function(argument, treeSettings) {
				this.OBJ_NAME_GRAPH = "ENS.ResourceGraphElementView";
				this.OBJ_NAME_MULTIPLE_GRAPH = "ENS.MultipleResourceGraphElementView";
				this.OBJ_NAME_SIGNAL = "ENS.SignalElementView";
				this.OBJ_NAME_LINK = "ENS.ResourceLinkElementView";
				this.OBJ_NAME_TEXT = "ENS.TextBoxElementView";
				this.OBJ_NAME_SHAPE = "ENS.ShapeElementView";
				this.DIV_ID_CONTROLLER = "range_controller";

				var $timeControllerDiv = $("<div/>");
				$timeControllerDiv.attr("id", this.DIV_ID_CONTROLLER);
				$timeControllerDiv.css("background-color", "rgba(0, 0, 0, 0.7)");
				$timeControllerDiv.css("padding", "10px");
				$("#"+argument.targetId).append($timeControllerDiv);

				var graphRangeController = new ENS.graphRangeController(argument.targetId);
				// 時間帯変更時のリスナを設定する
				var instance = this;
				graphRangeController.setSearchListener(function(from, to){
					var viewList = resourceDashboardListView.childView.viewCollection;
					for ( var key in viewList) {
						var view = viewList[key];
						var objectName = view.model.attributes.objectName;
						
						// viewがグラフでない場合は何もしない
						if(objectName !== instance.OBJ_NAME_GRAPH && objectName !== instance.OBJ_NAME_MULTIPLE_GRAPH ){
							continue;
						}
						
						var fromHour = from / 60 / 60 / 1000;
						// 15秒毎にグラフが更新されるので、グラフ内に収まるデータ数は、4 × 60 × 表示期間(h) となる
						view.graphMaxNumber = 4 * 60 * fromHour;
						// グラフの表示期間の幅を更新する
						view.updateDisplaySpan(from, to);
						// グラフの表示データを更新する
						view.updateGraphData(view.graphId, from, to);

						if ($("#tempDiv").length > 0) {
							$(".dygraph-title").width(
									($("#tempDiv").width() * 0.977) - 67);
						} else {
							$(".dygraph-title").width(
									($("#" + view.$el.attr("id") + "_ensgraph")
											.width() - 87));
						}
					}
				});

				this.render();
			},

			render : function() {
				console.log('call render');
			},
			onAdd : function(element) {
				console.log('call onAdd');
			},
			onChange : function(element) {
				console.log('called changeModel');
			},
			onRemove : function(element) {
				console.log('called removeModel');
			}
		});
