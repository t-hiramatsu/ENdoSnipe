function textField(elementProperty, paper){

	// 設定が取得できない場合は処理を終了する。
	if (!elementProperty) {
		return this;
	}

	// 位置情報リスト
	var positionArray = [];

	// テキストエリアに値が設定されていない場合は初期値を設定する。
	if (!elementProperty.text) {
		elementProperty.text = "TEXT";
	}

	// intに直す。
	elementProperty.pointX = parseFloat(elementProperty.pointX);
	elementProperty.pointY = parseFloat(elementProperty.pointY);

	// テキストオブジェクトを挿入する。
	var textPosition = {
		x : elementProperty.pointX,
		y : elementProperty.pointY,
		initValue : elementProperty.text
	};
	positionArray.push(textPosition);
	this.createMapElement(positionArray, paper);
	this.object.node.setAttribute('objectId', elementProperty.objectId);
	this.object.node.setAttribute('class',
			raphaelMapConstants.CLASS_MAP_ELEMENT);

	var tspan = $(this.object.node).find("tspan");
	tspan.attr("objectId", elementProperty.objectId);

	this.objectId = elementProperty.objectId;
	this.objectType_ = raphaelMapConstants.TEXTAREA_TYPE_NAME;
	this.x = elementProperty.pointX;
	this.y = elementProperty.pointY;

	if(elementProperty.width){
		elementProperty.width = parseFloat(elementProperty.width);
	}else{
		elementProperty.width = this.object.getBBox()["width"];
	}

	if(elementProperty.height){
		elementProperty.height = parseFloat(elementProperty.height);
	}else{
		elementProperty.height = this.object.getBBox()["height"];
	}


	this.width = elementProperty.width;
	this.height = elementProperty.height;
	this.setTextValue(elementProperty.text);
	return this;
}
textField.prototype = new mapElement();

textField.prototype.createPositionArray = function(elementProperty){

	// ポジションのリスト
	var positionArray = [];
	// 左上ポジション
	var firstPosition = new Position(elementProperty.pointX,
			elementProperty.pointY);
	positionArray.push(firstPosition);
	// 右上ポジション
	var secondPosition = new Position(elementProperty.width, 0);
	positionArray.push(secondPosition);
	// 左下ポジション
	var thirdPosition = new Position(0, elementProperty.height);
	positionArray.push(thirdPosition);
	// 右下ポジション
	var forthPosition = new Position(-1 * elementProperty.width, 0);
	positionArray.push(forthPosition);

	return positionArray;
};

/**
 * オブジェクトの生成。
 *
 * @private
 * @param {Map}
 *            positionArray
 * @param {raphael}
 *            paper
 *
 */
textField.prototype.createMapElement = function(positionArray, paper) {
	if (positionArray) {
		var position = positionArray[0];
		this.object = paper
				.text(position.x, position.y, position.initValue);
		return this;
	}
};

/**
 * テキストの位置を調整する処理。
 *
 * @private
 */
textField.prototype.adjustPosition = function() {
	var fontSize = this.fontSize;
	var alignment = this.textAnchor;
	// 揃えによって実装を変える。
	if (alignment == 'start') {
		this.adjustPositionLeft(fontSize);
	} else if (alignment == 'end') {
		this.adjustPositionRight(fontSize);
	} else if (alignment == 'middle') {
		this.adjustPositionCenter(fontSize);
	} else {
		this.adjustPositionLeft(fontSize);
	}
};

/**
 * テキストのみ位置調整が必要なため、修正する。
 *
 * @private
 *
 */
textField.prototype.adjustPositionLeft = function(fontSize) {
	var pointX = this.x;
	var pointY = this.y + this.height / 2;

	this.object.attr("y", pointY);
	this.object.attr("x", pointX);
	this.y = pointY;
	this.x = pointX;
};

/**
 * テキストのみ位置調整が必要なため、修正する。
 *
 * @private
 *
 */
textField.prototype.adjustPositionCenter = function(fontSize) {
	var pointX = this.x + this.width / 2;
	var pointY = this.y + this.height / 2 - this.object.getBBox().height
			/ 2;

	this.object.attr("y", pointY);
	this.object.attr("x", pointX);
	this.y = pointY;
	this.x = pointX;
};

/**
 * テキストのみ位置調整が必要なため、修正する。
 *
 * @private
 *
 */
textField.prototype.adjustPositionRight = function(fontSize) {
	var text = this.text;
	var textArray = text.split(wgp.common.getLineFeedCode());
	var pointX = this.x + this.width;
	var pointY = this.y + this.height / 2 - (textArray.length * fontSize) / 2;

	this.object.attr("y", pointY);
	this.object.attr("x", pointX);
	this.y = pointY;
	this.x = pointX;
};

/**
 * テキストオブジェクトにテキストを設定する。
 *
 * @param {String}
 *            text テキストオブジェクトに設定する文字列
 *
 * @private
 */
textField.prototype.setTextValue = function(text) {
	this.text = text;
	return;
	var lineFeedCode = wgp.common.getLineFeedCode();

	var arrayText = String.split(text, lineFeedCode);
	var replaceText = [];
	var tempText;
	$.each(arrayText, function(index, textValue) {
		if (textValue === "" || textValue === "&nbsp;") {
			if (tempText != "&nbsp;") {
				replaceText.push("&nbsp;" + lineFeedCode);
			} else {
				replaceText.push(lineFeedCode);
			}
			tempText = "";
			return true;
		}
		var arraySpace = textValue.replace(/ /g, "");
		if (arraySpace === "") {
			tempText = "";
		} else {
			tempText = textValue;
		}
		replaceText.push(textValue + lineFeedCode);
	});

	var resultText = replaceText.join("");
	resultText = resultText.replace(/ /g, "&nbsp;");

	this.object.attr("text", resultText);
};

/**
 * テキストオブジェクトにフレームを設定する。
 */
textField.prototype.setFrame = function() {

	var bbox = this.object.getBBox();
	var x = bbox["x"];
	var y = bbox["y"];
	var width = bbox["width"];
	var height = bbox["height"];

	if (!this.frame) {
		var paper = this.object.paper;
		var RADIUS_SIZE = raphaelMapConstants.RADIUS_SIZE;
		var frameLeftUpper = new ellipseSmall(x - (RADIUS_SIZE / 2),
				y - (RADIUS_SIZE / 2), RADIUS_SIZE, RADIUS_SIZE, paper);
		this._setFrameId(frameLeftUpper, raphaelMapConstants.LEFT_UPPER);

		var frameLeftBottom = new ellipseSmall(x - (RADIUS_SIZE / 2),
				y + height - (RADIUS_SIZE / 2), RADIUS_SIZE,
				RADIUS_SIZE, paper);
		this._setFrameId(frameLeftBottom, raphaelMapConstants.LEFT_UNDER);

		var frameRightUpper = new ellipseSmall(x + width
				- (RADIUS_SIZE / 2), y - (RADIUS_SIZE / 2), RADIUS_SIZE,
				RADIUS_SIZE, paper);
		this._setFrameId(frameRightUpper, raphaelMapConstants.RIGHT_UPPER);

		var frameRightBottom = new ellipseSmall(x + width
				- (RADIUS_SIZE / 2), y + height - (RADIUS_SIZE / 2),
				RADIUS_SIZE, RADIUS_SIZE, paper);
		this._setFrameId(frameRightBottom, raphaelMapConstants.RIGHT_UNDER);

		this.frame = {};
		this.frame[raphaelMapConstants.LEFT_UPPER] = frameLeftUpper;
		this.frame[raphaelMapConstants.LEFT_UNDER] = frameLeftBottom;
		this.frame[raphaelMapConstants.RIGHT_UPPER] = frameRightUpper;
		this.frame[raphaelMapConstants.RIGHT_UNDER] = frameRightBottom;
	} else {
		$.each(this.frame, function(index, frameObject) {
			frameObject.object.show();
		});
	}
};