ENS.graphLabel = {};

ENS.graphLabel.LABEL_WIDTH = 200;
ENS.graphLabel.LABEL_OFFSET = 50;

ENS.graphLabel.create = function(id) {
	var $label = $("<div/>");
	$label.attr("id", id);
	ENS.graphLabel._setAttr($label);
	return $label;
};

ENS.graphLabel.modify = function($div){
	ENS.graphLabel._setAttr($div);
};

ENS.graphLabel._setAttr = function($label){
	$label.addClass("ensLabel");
	$label.css("width", ENS.graphLabel.LABEL_WIDTH+"px");
	$label.hide();
};

ENS.graphLabel.setEventListener = function(labeldiv, graphdiv) {
	graphdiv
			.mousemove(function(e) {
				labeldiv.show();
				var graphPos = $(this).position();
				var contentsLeft = $("#contents_area").offset().left;
				var contentsWidth = $("#contents_area").width();
				var labelPos = {};
				labelPos.top = graphPos.top;
				labelPos.left = e.pageX - contentsLeft
						+ ENS.graphLabel.LABEL_OFFSET;

				if (contentsLeft + contentsWidth - ENS.graphLabel.LABEL_WIDTH
						* 1.5 < e.pageX) {
					labelPos.left = e.pageX
							- contentsLeft
							- (ENS.graphLabel.LABEL_OFFSET + ENS.graphLabel.LABEL_WIDTH);
				}

				labeldiv.css("left", labelPos.left);
				labeldiv.css("top", labelPos.top);
				if(labeldiv.html().length <= 0){
					labeldiv.html("NO DATA");
				}
			});
	graphdiv.mouseout(function(e) {
		labeldiv.hide();
	});
	graphdiv.focusout(function(e) {
		labeldiv.hide();
	});
}