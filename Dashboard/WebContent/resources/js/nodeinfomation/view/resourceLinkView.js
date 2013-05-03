ENS.ResourceLinkElementModel = wgp.MapElement.extend({
	defaults:{
		text : null,
		fontSize : 8,
		test: "initText",
		textAnchor : "start",
		linkUrl : "",
		linkType : "basicURL"
	}
});

ENS.ResourceLinkElementView = wgp.MapElementView.extend({
	// 本クラスはテキストエリアを描画する。
	render : function(model){

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

		// 運用時のイベントを設定
		this.setOperationFunction();
		
		// 編集時のイベントを設定
		this.setEditFunction();
		return this;
	},
	createPositionArray : function(model){

		// ポジションのリスト
		var positionArray = [];
		// 左上ポジション
		var firstPosition = this.createPosition(model.get("pointX"),
				model.get("pointY"));
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
	adjustPosition : function(model){
		var fontSize = model.get("fontSize");
		var textAnchor = model.get("textAnchor");
		//TODO 揃えに応じたポジション設定
	},
	setOperationFunction : function(){

		var instance = this;
		this.object.dblclick(
			// ダブルクリック時の画面遷移処理
			function(event){

				var linkUrl = instance.model.get("linkUrl");
				var linkType = instance.model.get("linkType");

				// マップリンクの場合はリンク先が存在する場合に遷移を行う。
				if(linkType == "mapLinkURL"){
					var targetLink = $("#" + resourceMapListView.$el.attr("id"))
						.find("A#" + linkUrl);

					// ツリー要素をクリックしてイベントを発生させる。
					if(targetLink.length > 0){
						$(targetLink[0]).mousedown();
					}
				}
			}
		);
	},
	setEditFunction : function(){

		var instance = this;
		this.object.drag(
			// マウスムーヴ時の処理
			function(dx, dy, x, y, e){
				this.attr({
					x : this.data("x") + dx,
					y : this.data("y") + dy
				});
			},
			// ドラッグ開始時の処理
			function(x, y, e){
				this.data( "x", this.attr("x") );
				this.data( "y", this.attr("y") );
			},
			// ドラッグ終了時の処理
			function( e ){				
				instance.model.set("pointX", this.attr("x"), {silent:true});
				instance.model.set("pointY", this.attr("y"), {silent:true});
			}
			
		);
	}
});