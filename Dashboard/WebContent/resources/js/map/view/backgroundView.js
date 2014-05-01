ENS.BackgroundElementView = ENS.ShapeElementView.extend({
	setFrame : function() {
	},
	hideFrame : function() {
	},
	moveElement : function(moveX, moveY) {
	},
	isResizable : function() {
		return true;
	},
	setAttributes : function(elementAttributeList) {
		var instance = this;

		// オブジェクト種別によって生成する背景オブジェクトを決定する。
		var objectType = elementAttributeList[0]["objectType"];

		var shapeType;
		var shapeName;
		var elementProperty = this.model.attributes;

		// 背景画像指定
		if (raphaelMapConstants.IMAGE_TYPE_NAME == objectType) {
			shapeType = raphaelMapConstants.IMAGE_TYPE_NAME;
			shapeName = raphaelMapConstants.IMAGE_ELEMENT_NAME_ELEMENT_NAME;

			var element = this.dashboardView.dashboardManager
					.createElementFunction(shapeName, shapeType,
							elementProperty);
			element.object.node.setAttribute("cid", this.model.cid);

			var href = elementAttributeList[0]["src"];
			element.object.attr(ENS.svg.attribute["src"].name, wgp.common
					.getContextPath()
					+ "/" + href);

			this.elementList_[0].object.remove();
			this.elementList_[0] = element;

			// 背景色指定
		} else {
			shapeType = raphaelMapConstants.POLYGON_TYPE_NAME;
			shapeName = raphaelMapConstants.RECTANGLE_ELEMENT_NAME;

			var element = this.dashboardView.dashboardManager
					.createElementFunction(shapeName, shapeType,
							elementProperty);
			element.object.node.setAttribute("cid", this.model.cid);

			var fill = elementAttributeList[0]["fill"];
			element.object.attr(ENS.svg.attribute["fill"].name, fill);

			this.elementList_[0].object.remove();
			this.elementList_[0] = element;
		}

		// 最奥に配置
		$(this.dashboardView.paper.canvas).prepend(element.object.node);

		instance.model.set({
			elementAttrList : elementAttributeList
		}, {
			silent : true
		});

		// SVGの描画処理が走らない場合があるため、下記の遅延処理によって
		// 確実に走るようにする。
		var dashboardCanvas = this.dashboardView.paper.canvas;
		setTimeout(function(){
			$(dashboardCanvas).show();
		}, 100);
	},
	updateModelPosition : function() {
		var element = this.elementList_[0];
		this.model.set({
			pointX : element.x,
			pointY : element.y,
			width : element.width,
			height : element.height
		}, {
			silent : true
		});

		this.dashboardView.setDashboardSize(element.width, element.height);
	}
});