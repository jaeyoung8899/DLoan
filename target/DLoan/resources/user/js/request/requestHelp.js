/**
 * desc : 상세이용안내
 * auth : shin
 * requestHelp.js
 */

var requestHelp = {
	
	// 초기 
	init : function () {
		this.events();
	},
	
	events : function () {
		// 도서관, 서점 선택
		$("button[name=selectObject]").on('click', function() {
			if($(this).val() == "library") {
				$("#library_info").show();
				$("#bookstore_info").hide();
			} else if($(this).val() == "bookstore") {
				$("#library_info").hide();
				$("#bookstore_info").show();
			}
			$(".sel_option_other").removeClass("sel_on_other");
			$(this).addClass("sel_on_other");
		});
	}
};

$(document).ready(function(){
	requestHelp.init();
});
