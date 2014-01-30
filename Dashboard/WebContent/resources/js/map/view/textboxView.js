ENS.TextBoxElementView = ENS.ShapeElementView.extend({
	initialize : function(argument){

		this.dashboardView  = argument.dashboardView;

		// 継承元のinitialzeメソッド実行
		wgp.MapElementView.prototype.initialize.apply(this, [ argument ]);
	},
	render : function(model){

		// 継承元のrenderメソッド実行
		wgp.MapElementView.prototype.render.apply(this, [ model ]);

		var elementProperty = {
			objectId : model.get("objectId"),
			pointX : model.get("pointX"),
			pointY : model.get("pointY"),
			width : model.get("width"),
			height : model.get("height")
		};
		var elementAttrList_ = model.get("elementAttrList");

		// 外枠を描画する。
		this.elementList_ = [];
		var boxElement = new rectangle(elementProperty, this._paper);
		boxElement.object.node.setAttribute("cid", model.cid);
		boxElement.object.node.setAttribute('class',
				raphaelMapConstants.CLASS_MAP_ELEMENT);

		var defaultTextProperty = {
			pointX : 0,
			pointY : 0,
			objectId : model.get("objectId")
		};

		var textElement = new textField(defaultTextProperty, this._paper);

		this.addElement(boxElement);
		this.addElement(textElement);
		this.setAttributes(elementAttrList_);
		this.alignText();

		return this;
	},
	alignText : function(){
		var boxElement  = this.getElement(0);
		var textElement = this.getElement(1);
		var textAnchor = textElement.object.attr("text-anchor");

		var boxX = this.model.get("pointX");
		var boxY = this.model.get("pointY");
		var boxWidth = this.model.get("width");
		var textHeight = this.model.get("height");

		var x = null;
		var y = null;
		if(textAnchor == "start"){
			x = boxX;
			y = boxY + (textHeight / 2);

		}else if(textAnchor == "middle"){
			x = boxX + (boxWidth / 2);
			y = boxY + (textHeight / 2);

		}else {
			x = boxX + boxWidth;
			y = boxY + (textHeight / 2);

		}

		textElement.object.attr("x", x);
		textElement.object.attr("y", y);
	},
	setOperateFunction : function() {
	},
	setEditFunction : function() {
		var boxElement = this.getElement(0);
		var textElement = this.getElement(1);

		var instance = this;
		var openTextAreaFunction = function(){
			var text = textElement.object.attr("text");
			var textArea = $('<textarea>' + text + '</textarea>');
			$(instance._paper.canvas).after(textArea);

			// 枠線に重ねるようにテキストエリアを移動。
			var boxOffset = $(boxElement.object.node).offset();
			textArea.offset(boxOffset);

			var boxWidth = boxElement.object.getBBox()["width"];
			var boxHeight = boxElement.object.getBBox()["height"];
			textArea.width(boxWidth);
			textArea.height(boxHeight);
			textArea.focus();

			// テキストエリアからフォーカスをはずした際に入力内容を自身に適用する。
			textArea.on("focusout", function(event){
				var inputText = textArea.val();
				var elementAttrList_ = instance.model.get("elementAttrList");

				var textAttr = elementAttrList_[1];
				textAttr["text"] = inputText;

				instance.model.set({
					elementAttrList : elementAttrList_
				},{
					silent : true
				});
				instance.setAttributes(elementAttrList_);
				textArea.remove();
				instance.alignText();
			});
		};

		boxElement.object.dblclick(openTextAreaFunction);
		textElement.object.dblclick(openTextAreaFunction);
	},
	setFrame : function(){
		this.elementList_[0].setFrame();
	},
	hideFrame : function(){
		this.elementList_[0].hideFrame();
	},
	moveElement : function(moveX, moveY){
		this.elementList_[0].moveElement(moveX, moveY);
		this.updateModelPosition();
		this.alignText();
	},
	resize : function(ratioX, ratioY, ellipsePosition){
		this.elementList_[0].resize(ratioX, ratioY, ellipsePosition);
		this.updateModelPosition();
		this.alignText();
	}
});