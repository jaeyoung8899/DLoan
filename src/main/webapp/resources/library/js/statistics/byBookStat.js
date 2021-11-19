
/**
 * desc : 연도별 통계
 * auth : shon
 * byYearStat.js
 */

var byBookStat = {
		
	init : function () {
		
		$("#from_reqDate,#to_reqDate").datepicker({
			
		});
		
		
		this.events();
		this.initSort();
		comm.loading.hide();
		
		var str2="<input type=\"button\" class=\"btn_add\" value=\"엑셀다운로드\" id=\"btnReqExcel\" style=\"width:100px;\">";
		$(".btn_wrap_1000.excel").html(str2);
	},
	
	events : function () {
		
		// 검색조건 엔터키 이벤트
		$("#userStatForm input[type=text]").on('keydown', function(e) {
			var keyCode = e.keyCode || e.which;
			if (keyCode == 13) {
				byBookStat.viewSearch();
			}
		});
		
		// 조회버튼
		$("#btnSearch").on('click', function (e) {
			byBookStat.viewSearch();
		});
		
		// 엑셀다운로드 버튼
		$(document).on("click", "#btnReqExcel",function(){
			byBookStat.excelDownload();
		});
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
	
	
	viewSearch : function(){
		if($('#from_reqDate').val()==""||$('#from_reqDate').val()==null||$('#to_reqDate').val()==""||$('#to_reqDate').val()==null){
			alert("신청일을 입력해주세요.");
			return false;
		}
		
		if (!comm.date.compare($('#from_reqDate').val(), $('#to_reqDate').val(), 'yyyy-MM-dd')) {
			alert('신청시작일 보다 신청종료일은 작을수는 없습니다.');
			return false;
		}
		
		var form = new commSubmit('bookStatForm');
		form.setUrl( _ctx + "/lib/statistics/byBookStatInfo");
		form.submit();
		comm.loading.show();
	},
		
	
	sortOrder : function($this, sortCol) {
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
		
		// 조회
		byBookStat.viewSearch();
	},
	
	excelDownload : function() {
		if($('#from_reqDate').val()==""||$('#from_reqDate').val()==null||$('#to_reqDate').val()==""||$('#to_reqDate').val()==null){
			alert("신청일을 입력해주세요.");
			return false;
		}
		
		if (!comm.date.compare($('#from_reqDate').val(), $('#to_reqDate').val(), 'yyyy-MM-dd')) {
			alert('신청시작일 보다 신청종료일은 작을수는 없습니다.');
			return false;
		}
		
		if(confirm("엑셀파일을 다운로드 하시겠습니까?")) {
			var form = new commSubmit('bookStatForm');
			form.setUrl(_ctx + "/lib/statistics/bookStatInfoToExcel");
			form.submit();
			comm.loading.show();
		}
	},
};


// onload 
$(document).ready(function() {
	comm.loading.show();
	byBookStat.init();
});