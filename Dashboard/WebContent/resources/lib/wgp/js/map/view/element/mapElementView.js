wgp.MapElementView = Backbone.View.extend({
	// 本クラスはRaphaelを用いて描画する各マップ要素の基底ビュークラス
	initialize : function(argument) {
		_.bindAll();
		this._paper = argument.paper;
		if (this._paper == null) {
			alert("paper is not exist");
			return;
		}
		this.render(argument.model);
	},
	render : function(model) {
		// renderメソッドは各継承先にてオーバーライドする。

		// 基底ビュークラスの共通的な処理
		// モデルをビュー自身に設定する。
		this.model = model;
		return this;
	},
	registerModelEvent : function() {

		// When Model Change
		this.model.on('change', this.change, this);

		// WHen Model Remove
		this.model.on('remove', this.remove, this);
	},
	change : function() {
		// changeメソッドは各継承先にてオーバーライドする。
	},
	remove : function() {
		// removeメソッドは各継承先にてオーバーライドする。
		this.object.remove();
	},
	createPosition : function(x, y, width, height, value) {
		return {
			x : x,
			y : y,
			width : width,
			height : height,
			initValue : value,
			URL : null,
		};
	},
	relateContextMenu : function(menuId, option){
		var cid = this.model.cid;
		var targetTag = $(this._paper.canvas).find("[cid='"+ cid +"']");
		contextMenuCreator.createContextMenuSelector(targetTag, menuId, option);
	},
	getElementSize : function() {
		return $.extend(trye, {}, {
			x : this.model.get("pointX"),
			y : this.model.get("pointY"),
			width : this.model.get("width"),
			height : this.model.get("height")
		});
	},
	moveElement : function(moveX, moveY) {

		// 対象オブジェクトにフレームが存在する場合は、フレームを合成させて移動を行う
		if (this.frame) {
			_.each(this.frame, function(frameElement, index) {
				frameElement.element.translate(moveX, moveY);
				frameElement.x = frameElement.x + moveX;
				frameElement.y = frameElement.y + moveY;
			});
		}
		this._moveElementObject(moveX, moveY);
		this.model.set("pointX", this.model.get("pointX") + moveX);
		this.model.set("pointY", this.model.get("pointY") + moveY);

		if (this.cx) {
			this.cx = this.cx + moveX;
		}

		if (this.cy) {
			this.cy = this.cy + moveY;
		}

		// TODO 移動対象のオブジェクトが他オブジェクトとconnectしている場合、線を再描画する
		// this.refactorConnectLine();
	},
	_moveElementObject : function(moveX, moveY) {
		this.moveX = this.moveX + moveX;
		this.moveY = this.moveX + moveY;
		var transValue = "t" + this.moveX_ + "," + this.moveY_;
		this.object.transform(transValue);
	},
	resize : function(ratioX, ratioY, ellipsePosition) {
		// resize element
		var paths = this.object.attr("path");
		var originX = this.model.get("pointX") - this.moveX_;
		var originY = this.model.get("pointY") - this.moveY_;
		_.each(paths, function(path, index) {
			if (path.length == 3) {
				path[1] = originX + (path[1] - originX) * ratioX;
				path[2] = originY + (path[2] - originY) * ratioY;
			}
		});
		this.object.attr("path", paths);

		// move element
		var afterWidth = this.model.get("width") * ratioX;
		var afterHeight = this.model.get("height") * ratioY;
		var moveX;
		var moveY;
		var ellipseMoveX;
		var ellipseMoveY;
		if (ellipsePosition == raphaelMapConstants.LEFT_UPPER
				|| ellipsePosition == raphaelMapConstants.LEFT_UNDER) {
			moveX = this.model.get("width") - afterWidth;
			ellipseMoveX = moveX;
		} else {
			moveX = 0;
			ellipseMoveX = afterWidth - this.model.get("width");
		}
		if (ellipsePosition == raphaelMapConstants.LEFT_UPPER
				|| ellipsePosition == raphaelMapConstants.RIGHT_UPPER) {
			moveY = this.model.get("height") - afterHeight;
			ellipseMoveY = moveY;
		} else {
			moveY = 0;
			ellipseMoveY = afterHeight - this.model.get("height");
		}
		this._moveElementObject(moveX, moveY);

		this.model.set("pointX", this.model.get("pointX") + moveX);
		this.model.set("pointY", this.model.get("pointY") + moveY);
		this.model.set("width", afterWidth);
		this.model.set("height", afterHeight);

		this.resizeFrame(ellipseMoveX, ellipseMoveY, ellipsePosition);

		// TODO 移動対象のオブジェクトが他オブジェクトとconnectしている場合、線を再描画する
		// this.refactorConnectLine();
	},
	resizeFrame : function(moveX, moveY, ellipsePosition){
		if (this.frame == null){
			return;
		}

		if (ellipsePosition == raphaelMapConstants.LEFT_UPPER
				|| ellipsePosition == raphaelMapConstants.LEFT_UNDER) {
			this.frame[raphaelMapConstants.LEFT_UPPER].object.translate(moveX, 0);
			this.frame[raphaelMapConstants.LEFT_UNDER].object.translate(moveX, 0);
		} else {
			this.frame[raphaelMapConstants.RIGHT_UNDER].object.translate(moveX, 0);
			this.frame[raphaelMapConstants.RIGHT_UPPER].object.translate(moveX, 0);
		}
		if (ellipsePosition == raphaelMapConstants.LEFT_UPPER
				|| ellipsePosition == raphaelMapConstants.RIGHT_UPPER) {
			this.frame[raphaelMapConstants.LEFT_UPPER].object.translate(0, moveY);
			this.frame[raphaelMapConstants.RIGHT_UPPER].object.translate(0, moveY);
		} else {
			this.frame[raphaelMapConstants.RIGHT_UNDER].object.translate(0, moveY);
			this.frame[raphaelMapConstants.LEFT_UNDER].object.translate(0, moveY);
		}
	},
	setFrame : function(){
		if (!this.frame) {
			var paper = this.object.paper;
			var RADIUS_SIZE = raphaelMapConstants.RADIUS_SIZE;

			var pointX = this.model.get("pointX");
			var pointY = this.model.get("pointY");
			var width = this.model.get("width");
			var height = this.model.get("height");

			var frameLeftUpper = new ellipseSmall(pointX - (RADIUS_SIZE / 2),
					pointY - (RADIUS_SIZE / 2), RADIUS_SIZE, RADIUS_SIZE, paper);
			this._setFrameId(frameLeftUpper, raphaelMapConstants.LEFT_UPPER);

			var frameLeftBottom = new ellipseSmall(pointX - (RADIUS_SIZE / 2),
					pointY + height - (RADIUS_SIZE / 2), RADIUS_SIZE,
					RADIUS_SIZE, paper);
			this._setFrameId(frameLeftBottom, raphaelMapConstants.LEFT_UNDER);

			var frameRightUpper = new ellipseSmall(pointX + this.width
					- (RADIUS_SIZE / 2), pointY - (RADIUS_SIZE / 2), RADIUS_SIZE,
					RADIUS_SIZE, paper);
			this._setFrameId(frameRightUpper, raphaelMapConstants.RIGHT_UPPER);

			var frameRightBottom = new ellipseSmall(pointX + this.width
					- (RADIUS_SIZE / 2), pointY + this.height - (RADIUS_SIZE / 2),
					RADIUS_SIZE, RADIUS_SIZE, paper);
			this._setFrameId(frameRightBottom, raphaelMapConstants.RIGHT_UNDER);

			this.frame = {};
			this.frame[raphaelMapConstants.LEFT_UPPER] = frameLeftUpper;
			this.frame[raphaelMapConstants.LEFT_UNDER] = frameLeftBottom;
			this.frame[raphaelMapConstants.RIGHT_UPPER] = frameRightUpper;
			this.frame[raphaelMapConstants.RIGHT_UNDER] = frameRightBottom;
		} else {
			_.each(this.frame, function(frameElement, index) {
				frameElement.object.show();
			});
		}
	},
	_setFrameId : function(frameElement, ellipsePosition){
		frameElement.parentElement_ = this;
		frameElement.object.node.setAttribute(raphaelMapConstants.OBJECT_ID_NAME,
				this.model.get("objectId"));
		frameElement.object.node.setAttribute(
				raphaelMapConstants.SMALL_ELLIPSE_POSITION, ellipsePosition);
	},
	getFrontNode : function(){
		return this.object;
	},
	getBottomNode : function(){
		return this.object;
	},
	moveFront : function(target){
		$(target.node).after(this.object.node);
	},
	moveBack : function(target){
		$(target.node).before(this.object.node);
	},
	resetFrame : function(){
		if (this.frame) {
			this.frame[raphaelMapConstants.LEFT_UPPER].object.remove();
			this.frame[raphaelMapConstants.LEFT_UNDER].object.remove();
			this.frame[raphaelMapConstants.RIGHT_UPPER].object.remove();
			this.frame[raphaelMapConstants.RIGHT_UNDER].object.remove();
			delete this.frame;
		}
		this.setFrame();
	},
	hide : function(){
		this.element.hide();
		this.hideFrame();
	},
	hideFrame : function(){
		if(!this.frame){
			return;
		}

		_.each(this.frame, function(frameElement, index){
			frameElement.object.hide();
		});
	},
	getObjectId : function(){
		return this.model.get("objectId");
	}
});