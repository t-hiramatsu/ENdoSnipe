ENS.ResourceLinkElementModel = wgp.MapElement.extend({
	defaults : {
		text : null,
		fontSize : 8,
		test : "initText",
		textAnchor : "start",
		linkUrl : "",
		linkType : "basicURL"
	}
});

ENS.ResourceLinkElementView = wgp.MapElementView.extend({
	// 本クラスはテキストエリアを描画する。
	render : function(model) {

		this.model = model;
		var text = model.get("text");

		var positionArray = this.createPositionArray(model);

		// テキストエリアを描画する。
		var position = positionArray[0];
		this.object = this._paper.text(position.x, position.y, text);
		this.object.attr("font-size", model.get("fontSize"));
		this.object.attr("text-anchor", model.get("textAnchor"));
		this.object.attr("fill", model.get("fill"));
		this.object.node.setAttribute("objectid", model.get("objectId"));
		this.object.node.setAttribute("cid", model.cid);

		return this;
	},
	createPositionArray : function(model) {

		// ポジションのリスト
		var positionArray = [];
		// 左上ポジション
		var firstPosition = this.createPosition(model.get("pointX"), model
				.get("pointY"));
		positionArray.push(firstPosition);
		// 右上ポジション
		var secondPosition = this.createPosition(model.get("width"), 0);
		positionArray.push(secondPosition);
		// 左下ポジション
		var thirdPosition = this.createPosition(0, model.get("height"));
		positionArray.push(thirdPosition);
		// 右下ポジション
		var forthPosition = this.createPosition(-1 * model.get("width"), 0);
		positionArray.push(forthPosition);

		return positionArray;
	},
	adjustPosition : function(model) {
		var fontSize = model.get("fontSize");
		var textAnchor = model.get("textAnchor");
		// TODO 揃えに応じたポジション設定
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
	/**
	 * 編集時のイベントを設定する。
	 */
	setEditFunction : function() {

		var instance = this;
		this.object.drag(
		// マウスムーヴ時の処理
		function(dx, dy, x, y, e) {

			var afterX = this.data("x") + dx;
			var afterY = this.data("y") + dy;

			// いずれかの座標が0以下となる場合は移動しない。
			if(afterX < 0 || afterY < 0){
				return;
			}

			this.attr({
				x : afterX,
				y : afterY
			});

			var afterWidth = this.attr("width");
			var afterHeight = this.attr("height");
			var fontSize = this.attr("font-size");

			// マップエリア拡張
			resourceMapListView.childView.enlargeMapArea(
					afterX, afterY, afterWidth, afterHeight + fontSize);


		},
		// ドラッグ開始時の処理
		function(x, y, e) {
			this.data("x", instance.model.get("pointX"));
			this.data("y", instance.model.get("pointY"));
		},
		// ドラッグ終了時の処理
		function(e) {
			instance.model.set("pointX", this.attr("x"), {
				silent : true
			});
			instance.model.set("pointY", this.attr("y"), {
				silent : true
			});
		}

		);
	}
});