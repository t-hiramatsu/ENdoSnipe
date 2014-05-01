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

ENS.SignalElementView = ENS.ShapeElementView.extend({
	render : function(model) {

		// 継承元のrenderメソッド実行
		wgp.MapElementView.prototype.render.apply(this, [ model ]);

		// 状態画像とシグナル名の情報はresourceTreeから取得
		var treeModel = resourceTreeView.ensTreeView.collection.get(model.get("resourceId"));
		var icon = ENS.tree.SIGNAL_ICON_STOP;
		var text = model.get("text");
		if(treeModel){
			icon = treeModel.get("icon");
			text = treeModel.get("data");
		}

		var elementProperty = {
			objectId : model.get("objectId"),
			pointX : model.get("pointX"),
			pointY : model.get("pointY"),
			width : model.get("width"),
			height : model.get("height"),
			URL : wgp.common.getContextPath() + ENS.SignalElementURL[icon]
		};

		// 状態を表す画像を描画する。
		var imageElement = new image(elementProperty, this._paper);
		imageElement.object.node.setAttribute("cid", model.cid);
		imageElement.object.node.setAttribute('class',
				raphaelMapConstants.CLASS_MAP_ELEMENT);
		this.addElement(imageElement);

		var textElementProperty = {
			objectId : model.get("objectId"),
			pointX : elementProperty.pointX + elementProperty.width / 2,
			pointY : elementProperty.pointY + elementProperty.height + 15,
			fontSize : ENS.dashboard.fontSize,
			textAnchor : "middle",
			text : text,
			fill : ENS.dashboard.fontColor
		};

		// シグナル名を表す文字列を描画する。
		var textElement = new textField(textElementProperty, this._paper);
		textElement.object.node.setAttribute("cid", model.cid);
		this.addElement(textElement);

		var elementAttrList_ = model.get("elementAttrList");
		this.setAttributes(elementAttrList_);
		return this;
	},
	/**
	 * 引数のモデルを基にシグナルを更新する。
	 * ※ツリーからの連携時も呼び出される。
	 */
	update : function(model) {

		// 状態画像の情報はresourceTreeから取得
		var treeModel = resourceTreeView.ensTreeView.collection.get(model.get("resourceId"));
		var icon = ENS.tree.SIGNAL_ICON_STOP;
		var text = model.get("text");

		if(treeModel){
			icon = treeModel.get("icon");
			text = treeModel.get("data");
		}

		var elementProperty = {
			pointX : model.get("pointX"),
			pointY : model.get("pointY"),
			width : model.get("width"),
			height : model.get("height"),
			URL : wgp.common.getContextPath() + ENS.SignalElementURL[icon]
		};

		// シグナル画像を再度設定する。
		this.getElement(0).setProperty(elementProperty);

		// シグナル名を再度設定する。
		this.getElement(1).object.attr("text", text);
	},
	setOperateFunction : function() {
	},
	setEditFunction : function() {
	},
	setFrame : function(){
		this.getElement(0).setFrame();
	},
	hideFrame : function(){
		this.getElement(0).hideFrame();
	},
	moveElement : function(moveX, moveY){
		var imageElement = this.getElement(0);
		imageElement.moveElement(moveX, moveY);

		this.alignText();
		this.updateModelPosition();
	},
	resize : function(ratioX, ratioY, ellipsePosition){
		var imageElement = this.getElement(0);
		imageElement.resize(ratioX, ratioY, ellipsePosition);

		this.alignText();
		this.updateModelPosition();
	},
	updateModelPosition : function(){
		var imageElement = this.getElement(0);
		var textElement = this.getElement(1);
		this.model.set({
			pointX : imageElement.x,
			pointY : imageElement.y,
			width  : imageElement.width,
			height : imageElement.height
		},{
			silent : true
		});

		// ダッシュボード領域の拡張
		this.dashboardView.enlargeDashboardArea(
			imageElement.x,
			textElement.y,
			imageElement.width,
			textElement.height);

	},
	alignText : function(){
		var imageElement = this.getElement(0);
		var textElement = this.getElement(1);

		textElement.object.attr("x", imageElement.x + imageElement.width / 2);
		textElement.object.attr("y", imageElement.y + imageElement.height + 15);
		textElement.x = imageElement.x + imageElement.width / 2;
		textElement.y = imageElement.y + imageElement.height + 15;
	}
});