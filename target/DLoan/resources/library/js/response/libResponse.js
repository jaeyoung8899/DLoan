
/**
 * desc : 납품요청
 * auth : shon
 * libResponse.js
 */

var libResponse = {
	init : function () {
		this.events();
		
		$("#from_resDate,#to_resDate").datepicker({});

		comm.sort.initSort('responseInfo');
	},
	
	events : function () {
		
		// 조회버튼
		$("#btnSearch").on('click', function (event) {
			libResponse.search(1);
		});
		
		// 초기화버튼
		$("#btnClear").on('click', function (event) {
			$("#requestInfoForm select").val("");
			$("#requestInfoForm input[type=text]").val("");
		});
		
		/**
		 * 2019/04/23 엑셀다운로드 버튼 추가
		 */
		$("#btnReqExcel").on('click', function() {
			libResponse.excelDownload();
		});
	},
	
	/**
	 * 조회
	 */
	search : function(pageNo) {
		
		//vlaidatiton
		if (!commFormValid('requestInfoForm')) {
			return false;
		}
		
		if (!comm.date.compare($('#from_reqDate').val(), $('#to_reqDate').val(), 'yyyy-MM-dd')) {
			alert('납품요청시작일 보다 납품요청종료일은 작을수는 없습니다.');
			return false;
		}
		
		pageNo = pageNo || 1;
		$("#start").val(pageNo);

		var form = new commSubmit('requestInfoForm');
		form.setUrl( _ctx + "/lib/libResponseInfo");
		form.submit();
	},
	
	/**
	 * 엑셀 다운로드
	 */
	excelDownload : function() {
		if(confirm("엑셀파일을 다운로드 하시겠습니까?")) {
			var form = new commSubmit('requestInfoForm');
			form.setUrl(_ctx + "/lib/libResponseInfoToExcel");
			form.submit();
		}
	}
};

// onload 
$(document).ready(function() {
	// 신청승인
	libResponse.init();
});