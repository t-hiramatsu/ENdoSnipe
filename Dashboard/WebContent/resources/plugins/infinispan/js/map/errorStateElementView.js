infinispan.plusMinus = function(num) {
	if (num > 0)
		return 1;
	else if (num < 0)
		return -1;
	else
		return 1;

};
infinispan.errorState = {};
infinispan.errorState.rate = 0.5;
infinispan.errorXOffset = 5;

infinispan.addMouseover = function(errorElement) {
	if ($.isArray(errorElement)) {
		for ( var i = 0; i < errorElement.length; i++) {
			errorElement[i].object.mouseover(function() {
			});
		}
	} else {
		errorElement.object.mouseover(function() {
		});
	}
};

infinispan.ErrorStateElementView = Backbone.View.extend({
	// /stateを渡す。NORMAL or ERROR or WARN
	initialize : function(argument) {
		_.bindAll();
		this.model.set({
			state : argument.state
		});
//		console.log(argument.state);

		this._paper = argument.paper;
		if (this._paper == null) {
			alert("paper is not exist");
			return;
		}
		this.taskInfo = argument.info;
		this.id = this.model.get("objectId");
		this.render();
//		console.log("errorState is called");
	},
	render : function() {
		var color = this.getStateColor();
		this.model.set({
			"attributes" : {
				fill : color,
				stroke : color
			}
		}, {
			silent : true
		});

		if (this.model.attributes.width == null) {
			this.model.attributes.width = 10;
		}
		if (this.model.attributes.height == null) {
			this.model.attributes.height = 10;
		}
		
		
		this.model.attributes.width = this.model.attributes.width * infinispan.errorState.rate;
		this.model.attributes.height = this.model.attributes.height * infinispan.errorState.rate;

		
		var lengthOfArrow = Math.sqrt(this.model.attributes.width
				* this.model.attributes.width + this.model.attributes.height
				* this.model.attributes.height);
		var rightDownData = new wgp.MapElement({
			objectId : 102,
			objectName : null,
			height : this.model.attributes.height
					* infinispan.plusMinus(this.model.attributes.height),
			width : -this.model.attributes.width
					* infinispan.plusMinus(this.model.attributes.width),
			pointX : this.model.attributes.pointX + this.model.attributes.width
					/ 2 + infinispan.errorXOffset,
			pointY : this.model.attributes.pointY
					- this.model.attributes.height / 2
		});
		var rightUpData = new wgp.MapElement({
			objectId : 103,
			objectName : null,
			height : -this.model.attributes.height
					* infinispan.plusMinus(this.model.attributes.height),
			width : -this.model.attributes.width
					* infinispan.plusMinus(this.model.attributes.width),
			pointX : this.model.attributes.pointX + this.model.attributes.width
					/ 2 + infinispan.errorXOffset,
			pointY : this.model.attributes.pointY
					+ this.model.attributes.height / 2
		});
		rightDownData.set({
			"attributes" : {
				fill : color,
				stroke : color,
				"stroke-width" : 10
			}
		}, {
			silent : true
		});
		rightUpData.set({
			"attributes" : {
				fill : color,
				stroke : color,
				"stroke-width" : 10
			}
		}, {
			silent : true
		});
		this.element = [];
		this.element[0] = new line(rightDownData.attributes, this._paper);
		this.element[1] = new line(rightUpData.attributes, this._paper);

		var anim2 = Raphael.animation({
			"stroke-opacity" : 1
		}, 1500, "easeIn",function(){this.animate(anim1);});
		var anim1 = Raphael.animation({
			"stroke-opacity" : 0
		}, 1500, "easeOut",function(){this.animate(anim2);});
		
		this.element[0].object.animate(anim1);
		this.element[1].object.animate(anim1);
		
		//this.element[1].object.animate(anim1);

		infinispan.addMouseover(this.element);
	},
	update : function(model) {
		var instance = this;
		var color = this.getStateColor();
		this.model.set({
			"fill" : color
		}, {
			silent : true
		});
		this.element[0].setAttributes(model);
		this.element[1].setAttributes(model);
	},
	remove : function(property) {
		this.element[0].hide();
		this.element[1].hide();
	},
	getStateColor : function() {
		var state = this.model.get("state");
		var color = infinispan.constants.STATE_COLOR[state];
		if (color == null) {
			color = infinispan.constants.STATE_COLOR[infinispan.constants.STATE.NORMAL];
		}
		return color;
	}
});