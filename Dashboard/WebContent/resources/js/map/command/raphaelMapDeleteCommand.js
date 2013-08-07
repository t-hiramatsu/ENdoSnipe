function raphaelMapDeleteCommand(targetList, argument){
	this.targetList = targetList;
	this.argument = argument;
};
raphaelMapDeleteCommand.prototype = new command();

raphaelMapDeleteCommand.prototype.execute = function(){
	$.each(this.targetList, function(index, target){
		target.hide();
	});
};