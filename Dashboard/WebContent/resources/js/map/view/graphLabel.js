ENS.graphLabel = {};

ENS.graphLabel.LABEL_WIDTH = 200;
ENS.graphLabel.LABEL_OFFSET = 50;

ENS.graphLabel.create = function(id) {
	var labeldiv = $("<div id='" + id + "' class='ensLabel' style= '"
			+ "position : absolute;" + "width: " + ENS.graphLabel.LABEL_WIDTH
			+ "px; overflow:visible;" + "word-break : break-word;"
			+ "background-color: rgba(220,220,220,0.8);"
			+ "border: 3px solid #24140e;" + "color: black;" + "padding: 10px;"
			+ "border-radius: 10px;"
			+ "z-index : 10'><div>");
	labeldiv.hide();
	return labeldiv;
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
			});
	graphdiv.mouseout(function(e) {
		labeldiv.hide();
	});
	graphdiv.focusout(function(e) {
		labeldiv.hide();
	});
}