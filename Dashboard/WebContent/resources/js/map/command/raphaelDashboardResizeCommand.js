function raphaelDashboardResizeCommand(targetList, argument) {
	this.targetList = targetList;
	this.argument = argument;
};
raphaelDashboardResizeCommand.prototype = new command();

raphaelDashboardResizeCommand.prototype.execute = function() {
	var ratioX = this.argument["ratioX"];
	var ratioY = this.argument["ratioY"];
	var ellipsePosition = this.argument["ellipsePosition"];
	$.each(this.targetList, function(index, object) {
		object.resize(ratioX, ratioY, ellipsePosition);
	});
};