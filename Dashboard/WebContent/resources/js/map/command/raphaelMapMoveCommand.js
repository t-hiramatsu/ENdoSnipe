function raphaelMapMoveCommand(targetList, argument) {
	this.targetList = targetList;
	this.argument = argument;
};
raphaelMapMoveCommand.prototype = new command();

raphaelMapMoveCommand.prototype.execute = function() {
	var moveX = this.argument["moveX"];
	var moveY = this.argument["moveY"];
	var targetList = this.targetList;
	$.each(targetList, function(index, object) {
		
		// X軸方向移動制限: imageかtextareaのより幅がある要素を抽出
		var widthArray = [];
		$.each(object.elementList_, function(index, element){
			widthArray.push(element.width);
		});

		var widthIndex = widthArray.indexOf(Math.max.apply(null,widthArray));
		var widthElement = object.elementList_[widthIndex];
		var x = widthElement.x;
		var positionLeft = x + moveX;
		if(positionLeft < widthElement.width/2 + 5)
		{
			moveX = - x;
			//テキストエリアの場合は、要素の幅を追加
			if((widthIndex == 1)&&(widthElement.objectType_ === "TEXTAREA")){
				moveX = moveX + widthElement.width;
			}
		}

		// Y軸方向移動制限: image要素を抽出
		var heightElement = object.elementList_[0];
		var y = heightElement.y;
		var positionTop = y + moveY;
		if(positionTop < 0)
		{
			moveY = - y;
			
			//Linkの場合はheight分を追加
			if(heightElement.objectType_ === "TEXTAREA"){
				moveY = moveY + heightElement.height;
			}
		}
		
		object.moveElement(moveX, moveY);
	});
};