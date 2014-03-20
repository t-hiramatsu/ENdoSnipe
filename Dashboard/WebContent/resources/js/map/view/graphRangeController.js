/**
 * グラフの時間帯調整UIクラス
 */
ENS.graphRangeController = function(id) {
	this.ID_RANGE = "range_controller_range";
	this.ID_DATEPICKER = "range_controller_datepicker";
	this.ID_PREV_BUTTON = "range_controller_prev";
	this.ID_PLAY_BUTTON = "range_controller_play";
	this.ID_NEXT_BUTTON = "range_controller_next";
	this.ID_SEARCH_BUTTON = "range_controller_search";
	this.ID_SPAN = "range_controller_span";
	this.TIMER_INTERVAL = 1000;

	this.searchListener = null;
	this.isPlaying = true;
	this.timerId = null;

	this._create(id);
};

/**
 * 検索ボタンが押された時のイベントリスナを設定する
 */
ENS.graphRangeController.prototype.setSearchListener = function(func) {
	this.searchListener = func;
};

/**
 * UIを作成する
 */
ENS.graphRangeController.prototype._create = function(id) {
	// 時間の範囲
	var $rangeLabel = $("<span/>").html("Range: ");
	var $range = this._createRange();

	// 日時
	var $datepicker = this._createDatetimePicker();

	// 15分前ボタン
	var $prev = this._createPrevButton();

	// リアルタイム再生ボタン
	var $play = this._createPlayButton();

	// 15分後ボタン
	var $next = this._createNextButton();

	// 検索ボタン
	var $search = this._createSearchButton();

	// 期間表示
	// var $span = this._createSpan();

	// コンテナ
	var $container = $("#" + id);

	// スタイルの変更
	$search.css("margin-left", "3px");
	$container.css("font-size", "0.8em");
	$container.css("margin-bottom", "0px");
	$datepicker.css("margin-left", "7px");
	$prev.css("margin-left", "8px");
	$next.css("margin-left", "3px");
	$play.css("margin-left", "3px");
	// $span.css("margin-left", "16px");

	// コンテナに追加
	$container.append($rangeLabel);
	$container.append($range);
	$container.append($datepicker);
	$container.append($search);
	$container.append($prev);
	$container.append($play);
	$container.append($next);
	// $container.append($span);

	// 初期表示では現在時刻をセット
	var date = new Date();
	this._setDate(date);
	this._updateSpan(date);
	this._updateTooltip();
};

/**
 * ドロップダウンリストのオプションを生成する
 */
ENS.graphRangeController.prototype._createOption = function(value) {
	var $option = $("<option/>");
	$option.html(value + " h");
	$option.attr("val", value);
	return $option;
};

/**
 * 範囲時間のドロップダウンリストを生成する
 */
ENS.graphRangeController.prototype._createRange = function() {
	var $range = $("<select/>");
	$range.attr("id", this.ID_RANGE);
	$range.append(this._createOption(0.5));
	$range.append(this._createOption(1).prop("selected", true));
	$range.append(this._createOption(2));
	$range.append(this._createOption(4));
	$range.append(this._createOption(8));
	$range.append(this._createOption(16));
	$range.append(this._createOption(24));

	var instance = this;
	$range.change(function() {
		if (!instance.isPlaying) {
			return;
		}
		var rangeMs = instance._getRangeMs();
		instance._updateGraph(rangeMs, 0);
		instance._updateTooltip();
	});
	return $range;
};

/**
 * 日付時刻選択UIを生成する
 */
ENS.graphRangeController.prototype._createDatetimePicker = function() {
	var $datepicker = $("<input/>");
	$datepicker.attr("id", this.ID_DATEPICKER);
	$datepicker.attr("type", "text");
	$datepicker.attr("title", "To date");
	$datepicker.datetimepicker({
		dateFormat : "yy-mm-dd",
		timeFormat : "HH:mm"
	});
	return $datepicker;
};

/**
 * 日付時刻選択UIにDate型で指定した時刻を表示させる
 */
ENS.graphRangeController.prototype._setDate = function(date) {
	var dateStr = this._getDateStr(date);
	var $datepicker = $("#" + this.ID_DATEPICKER);
	$datepicker.val(dateStr);
};

/**
 * yyyy/mm/dd hh:mm 形式の文字列を取得する
 */
ENS.graphRangeController.prototype._getDateStr = function(date) {
	var year = date.getFullYear();
	var month = ('00' + (date.getMonth() + 1)).slice(-2);
	var day = ('00' + date.getDate()).slice(-2);
	var hour = ('00' + date.getHours()).slice(-2);
	var min = ('00' + date.getMinutes()).slice(-2);
	var result = year + "-" + month + "-" + day + " " + hour + ":" + min;
	return result;
};

/**
 * 15分前ボタンを生成する
 */
ENS.graphRangeController.prototype._createPrevButton = function() {
	var $prev = $("<button/>");
	$prev.attr("id", this.ID_PREV_BUTTON);
	$prev.addClass("range_config");
	$prev.html("15 minutes ago");
	$prev.button({
		icons : {
			primary : "ui-icon-seek-prev"
		},
		text : false
	});
	var instance = this;
	$prev.click(function() {
		if (instance.isPlaying) {
			instance._stop();
		}
		instance._shiftTime(-instance._getRangeMs() * 0.5);
	});
	return $prev;
};

/**
 * 再生ボタンを生成する
 */
ENS.graphRangeController.prototype._createPlayButton = function() {
	var $play = $("<button/>");
	$play.attr("id", this.ID_PLAY_BUTTON);
	$play.addClass("range_config");
	$play.html("Current time");
	var instance = this;
	$play.button({
		icons : {
			primary : "ui-icon-stop"
		},
		text : false
	});
	$play.click(function(event) {
		if (instance.isPlaying) {
			instance._stop();
			var range = instance._getFromToMs();
			instance._updateGraph(range.from, range.to);
		} else {
			instance._play();
		}

	});
	return $play;
};

/**
 * 15分後ボタンを生成する
 */
ENS.graphRangeController.prototype._createNextButton = function() {
	var $next = $("<button/>");
	$next.attr("id", this.ID_NEXT_BUTTON);
	$next.addClass("range_config");
	$next.html("15 minutes after");
	$next.button({
		icons : {
			secondary : "ui-icon-seek-next"
		},
		text : false
	});
	var instance = this;
	$next.click(function() {
		if (instance.isPlaying) {
			instance._stop();
		}
		instance._shiftTime(instance._getRangeMs() * 0.5);
	});
	return $next;
};

/**
 * 検索ボタンを生成する
 */
ENS.graphRangeController.prototype._createSearchButton = function() {
	var $search = $("<button/>");
	$search.attr("id", this.ID_SEARCH_BUTTON);
	$search.attr("title", "search");
	$search.addClass("range_config");
	$search.html("search");
	$search.button({
		icons : {
			secondary : "ui-icon-search"
		},
		text : false
	});
	var instance = this;
	$search.click(function() {
		if (instance.isPlaying) {
			instance._stop();
		}
		instance._search();
	});
	return $search;
};

/**
 * 期間表示領域を作成する
 */
ENS.graphRangeController.prototype._createSpan = function() {
	var $span = $("<span/>");
	$span.attr("id", this.ID_SPAN);
	return $span;
};

/**
 * 設定に合わせて期間表示を更新する
 */
ENS.graphRangeController.prototype._updateSpan = function(date) {
	var to = date;
	if (to === undefined) {
		to = this._getDate();
	}
	var rangeMs = this._getRangeMs();
	if (to === null) {
		return;
	}
	var from = new Date(to.getTime() - rangeMs);
	var str = "Term: ";
	str += this._getDateStr(from) + " 〜 " + this._getDateStr(to);
	if (this.isPlaying) {
		str += "[Realtime]";
	}
	$("#" + this.ID_SPAN).html(str);
};

/**
 * リアルタイム表示を開始する
 */
ENS.graphRangeController.prototype._play = function() {
	$("#" + this.ID_PLAY_BUTTON).button({
		icons : {
			primary : "ui-icon-stop"
		}
	});
	this.isPlaying = true;
	var rangeMs = this._getRangeMs();
	this._updateGraph(rangeMs, 0);
	var instance = this;
	var date = new Date();
	this._setDate(date);
	this.timerId = setInterval(function() {
		var date = new Date();
		instance._updateSpan(date);
	}, this.TIMER_INTERVAL);
};

/**
 * リアルタイム表示を終了する
 */
ENS.graphRangeController.prototype._stop = function() {
	$("#" + this.ID_PLAY_BUTTON).button({
		icons : {
			primary : "ui-icon-play"
		}
	});
	this.isPlaying = false;
	clearInterval(this.timerId);
};

/**
 * 日付時刻指定UIからDateを取得する
 */
ENS.graphRangeController.prototype._getDate = function() {
	// Date型に変換可能なフォーマットに変換する
	var dateStr = $("#" + this.ID_DATEPICKER).val().replace(/-/g, "/");
	var date = new Date(dateStr);
	// フォーマット異常の場合はアラートを表示する
	if (isNaN(date.getTime())) {
		return null;
	}
	return date;
};

/**
 * ドロップダウンリストの値を元に範囲時間のミリ秒を取得する
 */
ENS.graphRangeController.prototype._getRangeMs = function() {
	var rangeMs = this._getRangeHour() * 60 * 60 * 1000;
	return rangeMs;
};

/**
 * ドロップダウンリストの値を元に範囲時間の時間を取得する
 */
ENS.graphRangeController.prototype._getRangeHour = function() {
	var rangeStr = $("#" + this.ID_RANGE).val();
	var range = rangeStr.replace(" h", "");
	return range;
};

/**
 * 検索ボタンが押下された時の処理
 */
ENS.graphRangeController.prototype._search = function() {
	var range = this._getFromToMs();
	if (range === null) {
		alert("Invalid date format.");
		return;
	}
	this._updateGraph(range.from, range.to);
};

/**
 * 時間帯をシフトする
 */
ENS.graphRangeController.prototype._shiftTime = function(time) {
	var date = this._getDate();
	if (date === null) {
		return;
	}
	date.setTime(date.getTime() + time);
	this._setDate(date);

	var range = this._getFromToMs();
	if (range === null) {
		return;
	}
	this._updateGraph(range.from, range.to);
};

/**
 * 表示対象の時刻を取得する。フォーマット異常の場合nullを返す。
 */
ENS.graphRangeController.prototype._getFromToMs = function() {
	var date = this._getDate();
	if (date === null) {
		return null;
	}
	var rangeMs = this._getRangeMs();

	// 検索対象のミリ秒を求める
	var currentDate = new Date();
	var now = currentDate.getTime();
	var to = now - date.getTime();
	var from = now - date.getTime() + rangeMs;

	return {
		from : from,
		to : to
	};
};

/**
 * グラフ描画を更新する
 */
ENS.graphRangeController.prototype._updateGraph = function(from, to) {
	this.searchListener(from, to);
	var date = new Date();
	date.setTime(date.getTime() - to);
	this._updateSpan(date);
	this._updateTooltip();
};

/**
 * ツールチップをアップデートする
 */
ENS.graphRangeController.prototype._updateTooltip = function() {
	$("#" + this.ID_PREV_BUTTON).attr("title",
			(this._getRangeHour() / 2) + " hours ago");
	$("#" + this.ID_NEXT_BUTTON).attr("title",
			(this._getRangeHour() / 2) + " hours after");
	if (!this.isPlaying) {
		$("#" + this.ID_PLAY_BUTTON).attr("title", "Start realtime tracking.");
	} else {
		$("#" + this.ID_PLAY_BUTTON).attr("title", "Stop realtime tracking.");
	}
};
