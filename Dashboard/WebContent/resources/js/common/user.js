/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
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
ENS.nodeInfoParentView = {
	viewClassName : "ENS.NodeInfoParentView",
	viewAttribute : {
		ids : {
			dualSliderArea : "sliderArea",
			graphArea : "graphArea"
		},
		// %グラフのY軸の最大値
		percentGraphMaxYValue : 105,
		// グラフの上限を決める際の、グラフのY値の最大に対する係数
		yValueMagnification : 1.1,
		// 最大化グラフの横のマージン
		maxGraphSideMargin : 30,
		// 最大化グラフの縦のマージン
		maxGraphVerticalMargin : 310,
		// グラフタイトル横のボタン用スペースの大きさ
		titleButtonSpace : 88
	}

};
ENS.nodeInfoField = {
	viewClassName : "wgp.MultiAreaView",
	rootView : appView,
	tabTitle : "Graph",
	collection : [ ENS.nodeInfoParentView ]
};

ENS.nodePerfDoctorParentElem = {
	viewClassName : "ENS.perfDoctorView",
	viewAttribute : {
		term : 1800,
		maxLineNum : 20
	}
};

ENS.nodePerfDoctorParentView = {
	viewClassName : "wgp.MultiAreaView",
	rootView : appView,
	tabTitle : "Performance Doctor",
	collection : [ ENS.nodePerfDoctorParentElem ]
};

ENS.nodeTabView = {
	viewClassName : "wgp.TabView",
	rootView : appView,
	collection : [ENS.nodeInfoField, ENS.nodePerfDoctorParentView]
};

ENS.reportParentElem = {
	viewClassName : "ENS.reportView",
	viewAttribute : {
		term : 1800,
		maxLineNum : 20
	}
};

ENS.reportParentView = {
	viewClassName : "wgp.MultiAreaView",
	rootView : appView,
	collection : [ ENS.reportParentElem ]
};

ENS.ResourceMapField = {
	viewClassName : "ENS.ResourceMapView",
	rootView : appView,
	collection : []
};

if (!wgp.constants.VIEW_SETTINGS) {
	wgp.constants.VIEW_SETTINGS = {};
}
wgp.constants.VIEW_SETTINGS = $.extend(wgp.constants.VIEW_SETTINGS, {
	"reportNode-" : ENS.reportParentView,
	"default" : ENS.nodeTabView,
	"ENS.ResourceMapView" : ENS.ResourceMapField
});