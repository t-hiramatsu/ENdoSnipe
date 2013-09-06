function raphaelMapResizeCommand(targetList, argument) {
	this.targetList = targetList;
	this.argument = argument;
};
raphaelMapResizeCommand.prototype = new command();

raphaelMapResizeCommand.prototype.execute = function() {
	var ratioX = this.argument["ratioX"];
	var ratioY = this.argument["ratioY"];
	var ellipsePosition = this.argument["ellipsePosition"];
	$.each(this.targetList, function(index, object) {
		object.resize(ratioX, ratioY, ellipsePosition);
	});
};