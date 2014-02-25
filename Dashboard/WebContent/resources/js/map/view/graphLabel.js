ENS.graphLabel = {};

// グラフラベルの横幅
ENS.graphLabel.LABEL_WIDTH = 200;

// カーソルとグラフラベルのオフセット
ENS.graphLabel.LABEL_OFFSET = 50;

// IDを指定してグラフラベルのDIV要素を作成する
ENS.graphLabel.create = function(id) {
	var $label = $("<div/>");
	$label.attr("id", id);
	ENS.graphLabel._setAttr($label);
	return $label;
};

// 引数で与えたDIV要素をグラフラベル化する
ENS.graphLabel.modify = function($div) {
	ENS.graphLabel._setAttr($div);
};

ENS.graphLabel._setAttr = function($label) {
	$label.addClass("ensLabel");
	$label.css("width", ENS.graphLabel.LABEL_WIDTH + "px");
	$label.hide();
};

// マウスイベントのリスナを設定する
ENS.graphLabel.setEventListener = function($labelDiv, $graphDiv) {
	$graphDiv
			.mousemove(function(e) {
				var $contents = $("#contents_area");
				var cLeft = $contents.offset().left;
				var cWidth = $contents.width();
				var cScrollX = $contents.scrollLeft();
				var graphPos = $(this).position();
				var labelPos = {};
				labelPos.top = graphPos.top;
				labelPos.left = e.pageX - cLeft + ENS.graphLabel.LABEL_OFFSET
						+ cScrollX;

				$labelDiv.show();

				// マップ画面の場合は位置を修正する
				if (graphPos.left === 0 && graphPos.top === 0) {
					labelPos.left = e.pageX - $(this).offset().left
							+ ENS.graphLabel.LABEL_OFFSET;
				}

				// ラベルが画面右端に到達している場合、ラベルをカーソルの左側に移動させる
				if (cLeft + cWidth - ENS.graphLabel.LABEL_WIDTH < e.pageX) {
					labelPos.left = labelPos.left
							- (ENS.graphLabel.LABEL_OFFSET * 2 + ENS.graphLabel.LABEL_WIDTH);
				}

				// ラベルの位置を設定する
				$labelDiv.css("left", labelPos.left);
				$labelDiv.css("top", labelPos.top);

				// マウスカーソルがデータの存在しない場所にあるときは、"NO DATA"と表示
				if ($labelDiv.html().length <= 0) {
					$labelDiv.html("NO DATA");
				}

				// 改行を追加する
				var text = $labelDiv.html();
				text = text.replace(/<b>/g, "<br/><b>");
				$labelDiv.html(text);
			});
	$graphDiv.mouseout(function(e) {
		$labelDiv.hide();
	});
	$graphDiv.focusout(function(e) {
		$labelDiv.hide();
	});
}