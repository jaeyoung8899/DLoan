
/**
 * desc : 신청거절내역
 * auth : Shin WonBoo
 * storeCancel.js
 */

var storeCancel = {
	init : function () {
		this.events();
		
		$("#from_reqDate,#to_reqDate").datepicker({
			
		});

		comm.sort.initSort('requestInfo');
	},
	
	events : function () {
		// 검색 필드
		$('#author, #publisher, #title, #isbn').on('keydown', function(e) {
			var keyCode = e.keyCode || e.which;
			if (keyCode == 13) {
				storeCancel.search(1);
			}
		});
		
		// 조회버튼
		$("#btnSearch").on('click', function (event) {
			storeCancel.search(1);
		});
		
		// 초기화버튼
		$("#btnClear").on('click', function (event) {
			$("#requestInfoForm select").val("");
			$("#requestInfoForm input[type=text]").val("");
		});
		
		// 엑셀다운
		$("#btnExcelDown").on('click', function (event) {
			storeCancel.excelDown();
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
			alert('신청시작일 보다 신청종료일은 작을수는 없습니다.');
			return false;
		}

		pageNo = pageNo || 1;
		$("#start").val(pageNo);

		var form = new commSubmit('requestInfoForm');
		form.setUrl( _ctx + "/store/storeCancelInfo");
		form.submit();
	},
	
	checkedAll :  function ($this, tabId) {
		var isChk = $($this).is(":checked");
		var name = $($this).data('name');
		$('#' + tabId + " > tbody > tr > td > input:checkbox[name='"+name+"']").each(function(i, el) {
			$(el).prop("checked", isChk)
		});
	},
	
	/**
	 * 엑셀 다운로드
	 */
	excelDown : function() {
		if(confirm("엑셀파일을 다운로드 하시겠습니까?")) {
			var form = new commSubmit('requestInfoForm');
			form.setUrl(_ctx + "/store/storeCancelInfoToExcel");
			form.submit();
		}
	}
};

// onload 
$(document).ready(function() {
	// 신청거절내역
	storeCancel.init();
	// 서점 공통
	storecomm.init();
});