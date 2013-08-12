function command(target, argument) {
	this.target = target;
	this.argument = argument;
};

command.prototype.execute = function() {
};

command.prototype.redo = function() {

};

command.prototype.undo = function() {

};