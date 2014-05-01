/*******************************************************************************
 * WGP 1.0B - Web Graphical Platform (https://sourceforge.net/projects/wgp/)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2012 Acroquest Technology Co.,Ltd.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
function triangle(elementProperty, paper) {

	// 設定が取得できない場合は処理を終了する。
	if (!elementProperty) {
		return this;
	}

	// 数値に直す。
	elementProperty.pointX = parseFloat(elementProperty.pointX);
	elementProperty.pointY = parseFloat(elementProperty.pointY);
	elementProperty.width = parseFloat(elementProperty.width);
	elementProperty.height = parseFloat(elementProperty.height);

	var positionArray = this.createPositionArray(elementProperty);
	this.createMapElement(positionArray, paper);
	this.object.attr("fill", "white");
	this.setAttributes(elementProperty);

	if (elementProperty.color == null) {
		elementProperty.color = "#FFFFFF";
	}
	if (!elementProperty.lineType) {
		elementProperty.lineType = "";
	}

	this.object.node.setAttribute(raphaelMapConstants.OBJECT_ID_NAME,
			elementProperty.objectId);
	this.object.node.setAttribute('class',
			raphaelMapConstants.CLASS_MAP_ELEMENT);

	this.objectId = elementProperty.objectId;
	this.objectType_ = raphaelMapConstants.POLYGON_TYPE_NAME;
	this.elementName_ = raphaelMapConstants.TRIANGLE_ELEMENT_NAME;

	this.x = elementProperty.pointX;
	this.y = elementProperty.pointY;
	this.width = elementProperty.width;
	this.height = elementProperty.height;

	return this;
}
triangle.prototype = new mapElement();

triangle.prototype.createPositionArray = function(elementProperty) {

	// ポジションのリスト
	var positionArray =[];

	var firstPosition = new Position(elementProperty.pointX
			+ (elementProperty.width / 2), elementProperty.pointY);
	positionArray.push(firstPosition);

	var secondPosition = new Position(-(elementProperty.width / 2),
			elementProperty.height);
	positionArray.push(secondPosition);

	var thirdPosition = new Position(elementProperty.width, 0);
	positionArray.push(thirdPosition);

	return positionArray;
};

triangle.prototype.createMapElement = function(positionArray, paper) {
	// パス情報
	var path = this.createPathString(positionArray);
	// オブジェクト生成
	this.object = paper.path(path);
	this.object.parentObject_ = this;

};