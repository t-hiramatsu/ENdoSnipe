ENS.signalElementModel = wgp.MapElement.extend({
	defaults : {
		linkId : null,
		text : ""
	}
});

// 状態ID毎の画像のURL定義
ENS.SignalElementURL = {
	"signal_-1" : "/resources/images/signal/signal_-1.png",
	"signal_0" : "/resources/images/signal/signal_0.png",
	"signal_1" : "/resources/images/signal/signal_1.png",
	"signal_2" : "/resources/images/signal/signal_2.png",
	"signal_3" : "/resources/images/signal/signal_3.png",
	"signal_4" : "/resources/images/signal/signal_4.png",
	"signal_5" : "/resources/images/signal/signal_5.png"
};

ENS.SignalElementView = wgp.MapElementView.extend({
	render : function(model) {

		// 継承元のrenderメソッド実行
		wgp.MapElementView.prototype.render.apply(this, [ model ]);

		// 状態画像の情報はresourceTreeから取得
		var treeModel = resourceTreeView.collection.get(model.id);
		var icon = ENS.tree.SIGNAL_ICON_STOP;
		if(treeModel){
			icon = treeModel.get("icon");
		}

		var elementProperty = {
			pointX : model.get("pointX"),
			pointY : model.get("pointY"),
			width : model.get("width"),
			height : model.get("height"),
			URL : wgp.common.getContextPath() + ENS.SignalElementURL[icon]
		};

		// 状態を表す画像を描画する。
		this.image = new image(elementProperty, this._paper);
		this.image.object.node.setAttribute("cid", model.cid);

		var textElementProperty = {
			pointX : elementProperty.pointX + model.get("width") / 2,
			pointY : elementProperty.pointY + elementProperty.height + 15,
			width : 1,
			height : 1,
			fontSize : ENS.map.fontSize,
			text : model.get("text"),
			fill : ENS.map.fontColor
		};

		this.text = new textArea(textElementProperty, this._paper);
		this.text.textObject.attr("fill", ENS.map.fontColor);

	},
	/**
	 * 引数のモデルを基にシグナルを更新する。
	 * ※ツリーからの連携時も呼び出される。
	 */
	update : function(model) {

		// 状態画像の情報はresourceTreeから取得
		var treeModel = resourceTreeView.collection.get(model.id);
		var icon = ENS.tree.SIGNAL_ICON_STOP;
		if(treeModel){
			icon = treeModel.get("icon");
		}

		var elementProperty = {
			pointX : model.get("pointX"),
			pointY : model.get("pointY"),
			width : model.get("width"),
			height : model.get("height"),
			URL : wgp.common.getContextPath() + ENS.SignalElementURL[icon]
		};

		// 再度設定する。
		this.image.setProperty(elementProperty);
	},
	remove : function(model) {
		this.image.object.remove();
		this.text.textObject.remove();
	},
	setOperateFunction : function() {

	},
	setEditFunction : function() {

		var instance = this;
		this.image.object.drag(
		// マウスムーヴ時の処理
		function(dx, dy, x, y, e) {

			// シグナル表示名のオブジェクトを取得
			var textObject = instance.text.textObject;

			var afterX = this.data("x") + dx;
			var afterY = this.data("y") + dy;
			var afterTextX = textObject.data("x") + dx;
			var afterTextY = textObject.data("y") + dy;

			// いずれかの座標が0以下となる場合は移動しない。
			if(afterX < 0 || afterY < 0 || afterTextX < 0 || afterTextY < 0){
				return;
			}

			this.attr({
				x : afterX,
				y : afterY
			});

			textObject.attr({
				x : afterTextX,
				y : afterTextY
			})

			var afterWidth = this.attr("width");
			var afterHeight = this.attr("height");
			var afterTextWidth = textObject.attr("width");
			var afterTextHeight = textObject.attr("height");

			// シグナル画像分のマップエリア拡張
			resourceMapListView.childView.enlargeMapArea(
					afterX, afterY, afterWidth, afterHeight);

			// シグナル表示名分のマップエリア拡張
			resourceMapListView.childView.enlargeMapArea(
					afterTextX, afterTextY, afterTextWidth, afterTextHeight + ENS.map.fontSize);
		},
		// ドラッグ開始時の処理
		function(x, y, e) {

			var imageObject = instance.image.object;
			var textObject = instance.text.textObject;

			this.data("x", imageObject.attr("x"));
			this.data("y", imageObject.attr("y"));

			textObject.data("x", textObject.attr("x"));
			textObject.data("y", textObject.attr("y"));

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