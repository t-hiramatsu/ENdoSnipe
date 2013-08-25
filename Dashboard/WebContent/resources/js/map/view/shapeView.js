ENS.ShapeElementView = wgp.MapElementView.extend({
	initialize : function(argument){

		this.mapView  = argument.mapView;
		this.elementList_ = [];

		// 継承元のinitialzeメソッド実行
		wgp.MapElementView.prototype.initialize.apply(this, [ argument ]);
	},
	render : function(model){

		// 継承元のrenderメソッド実行
		wgp.MapElementView.prototype.render.apply(this, [ model ]);

		var shapeName = model.get("shapeName");
		var shapeType = model.get("shapeType");
		var elementAttrList_ = model.get("elementAttrList");
		var elementProperty = model.attributes;

		// 図形を描画する。
		var element =
			this.mapView.mapManager.createElementFunction(
				shapeName,
				shapeType,
				elementProperty);
		element.object.node.setAttribute("cid", model.cid);
		this.addElement(element);
		this.setAttributes(elementAttrList_);
		return this;
	},
	setEditFunction : function(){

	},
	addElement : function(element){
		this.elementList_.push(element);
	},
	getElement : function(index){
		return this.elementList_[index];
	},
	getAttributes : function(){
		return _.extend({}, this.model.get("elementAttrList"));
	},
	setAttributes : function(elementAttributeList){

		var instance = this;
		var elementAttrList_ = instance.model.get("elementAttrList");

		_.each(elementAttributeList, function(attributes, index){
			var element = instance.getElement(index);

			_.each(attributes, function(value, key){
				element.object.attr(ENS.svg.attribute[key].name, value);
			});

			elementAttrList_[index] = attributes;
		});

		instance.model.set({
			elementAttrList : elementAttrList_
		},{
			silent : true
		})
	},
	setFrame : function(){
		_.each(this.elementList_, function(element, index){
			element.setFrame();
		});
	},
	hideFrame : function(){
		_.each(this.elementList_, function(element, index){
			element.hideFrame();
		});
	},
	moveElement : function(moveX, moveY){
		_.each(this.elementList_, function(element, index){
			element.moveElement(moveX, moveY);
		});
		this.updateModelPosition();
	},
	resize : function(ratioX, ratioY, ellipsePosition){

		// リサイズ可能なビューのみリサイズ対象とする。
		if(this.isResizable()){
			_.each(this.elementList_, function(element, index){
				element.resize(ratioX, ratioY, ellipsePosition);
			});
			this.updateModelPosition();
		}
	},
	remove : function(){
		_.each(this.elementList_, function(element, index){
			element.hideFrame();
			element.object.remove();
		});
		var objectId = this.getObjectId();
		this.mapView.collection.remove(objectId);
	},
	getWidth : function(){
		return this.elementList_[0].width;
	},
	getHeight : function(){
		return this.elementList_[0].height;
	},
	setModelPosition : function(property){
		var element = this.elementList_[0];
		var x = property.pointX - element.x;
		var y = property.pointY - element.y;
		var ratioX = property.width / element.width;
		var ratioY = property.height / element.height;
		this.moveElement(x, y);
		this.resize(ratioX , ratioY ,raphaelMapConstants.RIGHT_UNDER);
	},
	updateModelPosition : function(){
		var element = this.elementList_[0];
		this.model.set({
			pointX : element.x,
			pointY : element.y,
			width  : element.width,
			height : element.height
		},{
			silent : true
		});

		// マップ領域の拡張
		this.mapView.enlargeMapArea(
			element.x,
			element.y,
			element.width,
			element.height);
	},
	isResizable : function(){
		return true;
	}
});