//ENS.MulResNodeInfoParentView = ENS.NodeInfoParentView
//		.extend({
//			initialize : function(argument, treeSettings) {
//				ENS.nodeinfo.viewList = [];
//
//				this.viewtype = wgp.constants.VIEW_TYPE.VIEW;
//				this.graphIds = [];
//				this.treeSettings = treeSettings;
//				var treeListView = new wgp.TreeView({
//					id : "tree",
//					rootView : this
//				});
//				var appView = new ENS.AppView();
//				appView.addView(treeListView, "tree");
//
//				this.divId = this.$el.attr("id");
//				var id = argument["ids"];
//				this.maxId = 0;
//				this.viewList = {};
//
//				this.graphPerPage = 12;
//				this.noOfPage = 0;
//				this.moveScale = [];
//
//				// 全ての子ノードのパスを取得し、グラフを表示する
//				this.getChildTargetNodes(treeSettings.id);
//
//				// dual slider area (add div and css, and make slider)
//				$("#" + this.$el.attr("id")).append(
//						'<div id="' + id.dualSliderArea + '"></div>');
//				$('#' + id.dualSliderArea).css(
//						ENS.nodeinfo.parent.css.dualSliderArea);
//				$('#' + id.dualSliderArea).css(
//						ENS.nodeinfo.parent.css.dualSliderArea);
//				this.dualSliderView = new ENS.DualSliderView({
//					id : id.dualSliderArea,
//					rootView : this
//				});
//
//				var instance = this;
//
//				this.dualSliderView.setScaleMovedEvent(function(from, to) {
//					instance.moveScale["from"] = from;
//					instance.moveScale["to"] = to;
//					var viewList = ENS.nodeinfo.viewList;
//
//					for ( var key in viewList) {
//						var fromHour = from / 60 / 60 / 1000;
//						var ins = viewList[key];
//						// 15秒毎にグラフが更新されるので、グラフ内に収まるデータ数は、4 × 60 × 表示期間(h) となる
//						ins.graphMaxNumber = 4 * 60 * fromHour;
//						// グラフの表示期間の幅を更新する
//						ins.updateDisplaySpan(from, to);
//						// グラフの表示データを更新する
//						ins.updateGraphData(key, from, to);
//
//						if ($("#tempDiv").length > 0) {
//							$(".dygraph-title").width(
//									($("#tempDiv").width() * 0.977) - 67);
//						} else {
//							$(".dygraph-title").width(
//									($("#" + ins.$el.attr("id") + "_ensgraph")
//											.width() - 87));
//						}
//					}
//				});
//
//				$("#" + this.$el.attr("id")).append(
//						'<div class="clearFloat"></div>');
//
//				this.render();
//			}
//			
//		});
