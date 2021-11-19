
/**
 * desc : 연도별 통계
 * auth : shon
 * byYearStat.js
 */

var byYearStat = {
		
	init : function () {
		
		this.events();
		this.initSort();
		
		var str="";
		var viewYear = $("#viewYear").val();
		for (var i =new Date().getFullYear(); i >=2019 ; i--) {
			if(viewYear==i){
				str += "<option value='" + i + "' selected>" + i + "</option> \n";
			}else{
				str += "<option value='" + i + "'>" + i + "</option> \n";
			}
        }
		$("#selYear").html(str);

		
			var viewMonth = $("#viewMonth").val();
			if(viewMonth=="0"||viewMonth==""){
				
				for(var i=1; i<13; i++){
					$('#monthInfo_'+i).css("display","none");
					$('.'+i+'month a').removeClass("tab_on");
				}
				
				$('#totalInfo').css("display","block");
				$('.totalYear a').addClass("tab_on");
				
			}else{
				
				for(var i=1; i<13; i++){
					$('#monthInfo_'+i).css("display","none");
					$('.'+i+'month a').removeClass("tab_on");
				}
				
				$('#monthInfo_'+viewMonth).css("display","block");
				$('#totalInfo').css("display","none");
				$('.'+viewMonth+'month a').addClass("tab_on");
			}
			comm.loading.hide();
			
			var str2="<input type=\"button\" class=\"btn_add\" value=\"엑셀다운로드\" id=\"btnReqExcel\" style=\"width:100px;\">";
			$(".btn_wrap_1000.excel").html(str2);
	},
	
	events : function () {
		
		// 검색조건 엔터키 이벤트
		$("#limitBookForm input[type=text]").on('keydown', function(e) {
			var keyCode = e.keyCode || e.which;
			if (keyCode == 13) {
				limitBook.search(1);
			}
		});
		
		// 조회버튼
		$("#btnSearch").on('click', function (e) {
			$("#viewMonth").val("0");
			byYearStat.viewSearch();
		});
		
		// 엑셀다운로드 버튼
		$(document).on("click", "#btnReqExcel",function(){
			byYearStat.excelDownload();
		});
	},
	
	viewMonth : function(month){

		$("#viewMonth").val(month);
		
		for(var i=1; i<13; i++){
			$('#monthInfo_'+i).css("display","none");
			$('.'+i+'month a').removeClass("tab_on");
		}
		$('#totalInfo').css("display","none");
		$('.totalYear a').removeClass("tab_on");
		
		$('#monthInfo_'+month).css("display","block");
		
		$('.'+month+'month a').addClass("tab_on");
		
	},
	
	viewTotal : function(){
		
		$("#viewMonth").val("0");
		
		for(var i=0; i<13; i++){
			$('#monthInfo_'+i).css("display","none");
			$('.'+i+'month a').removeClass("tab_on");
		}
		
		$('#totalInfo').css("display","block");
		$('.totalYear a').addClass("tab_on");
	},
	
	viewSearch : function(){
		var form = new commSubmit('yearStatForm');
		form.setUrl( _ctx + "/lib/statistics/byYearStatInfo");
		form.submit();
		comm.loading.show();
	},
	
	initSort : function () {
		
		var mask = "";
		if ($('#order').val() == 'ASC') {
			mask = "▲";
		} else if ($('#order').val() == 'DESC') {
			mask = "▼";
		}

		$('#limitBookInfo > thead > tr > th > a').each(function(i, el) {
			if ($(el).data('sort') == $('#sortCol').val()) {
				$(el).text($(el).text() + " " + mask);
				return false;
			}
		});
	},
		
	
	sortOrder : function($this, sortCol, month) {
		var mask = '';
	
		if (sortCol == $('#sortCol').val()) {
			
			if (!$.isEmpty($('#order').val())) {
				if ($('#order').val() == 'ASC') {
					mask = "▼";
				} else {
					mask = "▲";
				}
			} else {
				// default
				mask = "▲";
			}
		} else {
			// default
			mask = "▲";
		}
		
		if (mask == "▲") {
			$('#order').val("ASC")
		} else {
			$('#order').val("DESC")
		}
		
		$('#sortCol').val(sortCol);
		
		// header 표시
		var colNm = $($this).text();
		
		colNm = colNm.replace(" ▲", "").replace(" ▼", "");
		$($this).text(colNm + " " + mask);
		
		if(month!="0"||month!=null){
			$("#viewMonth").val(month);
		}
		// 조회
		byYearStat.viewSearch();
	},
	
	excelDownload : function() {
		
		var check=$("#total_sum").val();
		if(check>2000){
			if(confirm("검색 조건의 데이터가 많아 데이터 다운에 시간이 걸립니다. 엑셀파일을 다운로드 하시겠습니까?")){
				var form = new commSubmit('yearStatForm');
				form.setUrl(_ctx + "/lib/statistics/yearStatInfoToExcel");
				form.submit();
				comm.loading.show();
			}
			
		}else{
			if(confirm("엑셀파일을 다운로드 하시겠습니까?")) {
				var form = new commSubmit('yearStatForm');
				form.setUrl(_ctx + "/lib/statistics/yearStatInfoToExcel");
				form.submit();
				comm.loading.show();
			}
		}
	},
};


// onload 
$(document).ready(function() {
	comm.loading.show();
	byYearStat.init();
});