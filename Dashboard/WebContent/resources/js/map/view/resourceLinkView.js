ENS.ResourceLinkElementModel = wgp.MapElement.extend({
	defaults : {
		text : null,
		fontSize : 8,
		textAnchor : "start",
		linkUrl : "",
		linkType : "basicURL"
	}
});

ENS.ResourceLinkElementView = ENS.ShapeElementView.extend({
	render : function(model){

		// 継承元のrenderメソッド実行
		wgp.MapElementView.prototype.render.apply(this, [ model ]);

		var shapeName = "textField";
		var elementProperty = model.attributes;
		var elementAttrList_ = model.get("elementAttrList");

		// テキストを描画する。
		this.elementList_ = [];
		var element =
			this.mapView.mapManager.createElementText(
				shapeName,
				elementProperty);
		element.object.node.setAttribute("cid", model.cid);
		this.addElement(element);
		this.setAttributes(elementAttrList_);
	},
	/**
	 * 運用時のイベントを設定する。
	 */
	setOperateFunction : function() {

		var instance = this;
		this.object.dblclick(
		// ダブルクリック時の画面遷移処理
		function(event) {

			var linkUrl = instance.model.get("linkUrl");
			var linkType = instance.model.get("linkType");

			// マップリンクの場合はリンク先が存在する場合に遷移を行う。
			if (linkType == "mapLinkURL") {
				var targetMapLink = resourceMapListView.collection.get(linkUrl);

				// ツリー要素をクリックしてイベントを発生させる。
				if (targetMapLink) {
					resourceMapListView.clickModel(targetMapLink);
				}
			}
		});
	},
	getWidth : function(){
		return this.getElement(0).object.getBBox()["width"];
	},
	getHeight : function(){
		return this.getElement(0).object.getBBox()["height"];
	}
});