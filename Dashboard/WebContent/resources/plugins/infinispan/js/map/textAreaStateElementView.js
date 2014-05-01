infinispan.TextAreaStateElementView = Backbone.View
		.extend({
			initialize : function(argument) {
				_.bindAll();
				this._paper = argument.paper;
				if (this._paper == null) {
					alert("paper is not exist");
					return;
				}
				this.id = this.model.get("objectId");
				this.render();
			},
			render : function() {
				var color = this.getStateColor();
				this.model.set({
					"attributes" : {
						"stroke-width" : 0
					}
				}, {
					silent : true
				});
				this.element = new textArea(this.model.attributes, this._paper);
				this.element.textObject.attr({
					stroke : "#FFFFFF"
				});
			},
			update : function(model) {
				var instance = this;
				var color = this.getStateColor();
				this.model.set({
					"fill" : color
				}, {
					silent : true
				});
				this.element.setAttributes(model);
			},
			remove : function(property) {
				this.element.hide();
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