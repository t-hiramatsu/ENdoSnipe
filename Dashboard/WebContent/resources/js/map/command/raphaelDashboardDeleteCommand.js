function raphaelDashboardDeleteCommand(targetList, argument){
	this.targetList = targetList;
	this.argument = argument;
};
raphaelDashboardDeleteCommand.prototype = new command();

raphaelDashboardDeleteCommand.prototype.execute = function(){
	$.each(this.targetList, function(index, target){
		target.hide();
	});
};