ENS.jvnFileDwonloadView = wgp.AbstractView.extend({
	initialize : function(argument) {
		this.render();
	},
	render : function() {
		var $downloadDiv = $("<div id='jvnFileDownload'>Javelin Log File Download</div>");
		var $startDatePicker = $("<input/>");
		$startDatePicker.attr("name", "start");
		$startDatePicker.attr("type", "text");
		$startDatePicker.attr("title", "From date");
		
		$startDatePicker.datetimepicker({
			dateFormat : "yy-mm-dd",
			timeFormat : "HH:mm"
		});

		var $endDatePicker = $("<input/>");
		$endDatePicker.attr("name", "end");
		$endDatePicker.attr("type", "text");
		$endDatePicker.attr("title", "To date");

		$endDatePicker.datetimepicker({
			dateFormat : "yy-mm-dd",
			timeFormat : "HH:mm"
		});

		var $downloadButton = $("<button/>");
		$downloadButton.attr("name", "download");
		$downloadButton.attr("type", "button");
		$downloadButton.html("get Javelin Logs");
		$downloadButton.css("margin-left", "100px");

		// ファイルダウンロード用のフォーム
		var $downloadForm = $("<form id='jvnFileDownloadForm'></form>");
		$downloadForm.attr("action", '/ENdoSnipe/jvnFileDownload/download');
		$downloadForm.attr("target", "resultForm")
		$downloadForm.attr("method", "POST");
		
		var $br = $("<br/>");
		$downloadForm.append("<label>From</label>").append($br.clone(true));
		$downloadForm.append($startDatePicker).append($br.clone(true));
		$downloadForm.append("<label>To</label>").append($br.clone(true));
		$downloadForm.append($endDatePicker).append($br.clone(true)).append($br.clone(true));
		$downloadForm.append($downloadButton);

		$downloadDiv.append($downloadForm);
		$("#" + this.id).append($downloadDiv);

		var $iframe= $("<iframe/>");
		$iframe.hide();
		$("#" + this.id).append($iframe);

		$downloadButton.on("click", function(event){
			var document = $iframe[0].contentDocument;
			var $iframeBody = $("body", document);

			var iframeForm = $downloadForm.clone(true);
			$iframeBody.append(iframeForm);
			iframeForm.submit();
		});

	},
	onAdd : function(element) {
		// Do nothing
	},
	onChange : function(element) {
		// Do nothing
	},
	onRemove : function(element) {
		// Do nothing
	},
	onComplete : function(element) {
		// Do nothing
	}
});