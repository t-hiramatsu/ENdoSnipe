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
		object.moveElement(moveX, moveY);
	});
};