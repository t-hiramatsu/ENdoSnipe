function raphaelMapPasteCommand(targetList, argument){
	this.targetList = targetList;
	this.argument = argument;
};
raphaelMapPasteCommand.prototype = new command();

raphaelMapPasteCommand.prototype.execute = function(){
	var targetList = this.targetList;
	var type = this.argument.type;
	if (targetList.length == 0) {
		return;
	}

	var paper = null;
	$.each(targetList, function(index, target){
		paper = target.object.paper;
		return false;
	});

	var manager = this.argument.manager;
	$.each(targetList, function(index, target){
		var property = target.getProperty();
	});
};