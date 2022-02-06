
/**
 * desc : 지출결의
 * auth : jy
 * libSpending.js
 */

//지출결의 탭
var libSpending = {
	init : function () {
		this.events();
		
		comm.sort.initSort('spendingInfo');
	},
	
	events : function () {
		
		// 검색조건 엔터키 이벤트
		$("#fiscal_year").on('keydown', function(e) {
			var keyCode = e.keyCode || e.which;
			if (keyCode == 13) {
				libSpending.search(1);
			}
		});
		
		// 검색버튼
		$("#btnSearch").on('click', function (event) {
			libSpending.search(1);
		});	
		
		// 등록버튼
		$("#btnRegister").on('click', function (event) {
			location.href = '/lib/spending/register';
		});
	},

	//조회 
	search : function(pageNo) {
		
		pageNo = pageNo || 1;
		$("#start").val(pageNo);
		
		var form = new commSubmit('requestInfoForm');
		form.setUrl( _ctx + "/lib/spending");
		form.submit();
	},

	initSort : function () {
		
		var mask = "";
			
		if ($('#order').val() == 'ASC') {
			mask = "▲";
		} else if ($('#order').val() == 'DESC') {
			mask = "▼";
		}
			
		$('#responseInfo > thead > tr > th > a').each(function(i, el) {
			if ($(el).data('sort') == $('#sortCol').val()) {
				$(el).html($(el).html() + " " + mask);
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
		libSpending.search();
	},
	
};

// 반품도서 탭
var libRefund = {
	init : function () {
		this.events();
		
		$("#from_resDate,#to_resDate").datepicker({});

		comm.sort.initSort('libRefundInfo');
	},
	
	events : function () {
		
		// 조회버튼
		$("#btnRefundSearch").on('click', function (event) {
			libRefund.search(1);
		});
		
		// 초기화버튼
		$("#btnClear").on('click', function (event) {
			$("#requestInfoForm select").val("");
			$("#requestInfoForm input[type=text]").val("");
		});
		
		/**
		 * 2019/04/23 엑셀다운로드 버튼 추가
		 */
		$("#btnRefundExcel").on('click', function() {
			libRefund.excelDownload();
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
		
		if (!comm.date.compare($('#from_resDate').val(), $('#to_resDate').val(), 'yyyy-MM-dd')) {
			alert('납품요청시작일 보다 납품요청종료일은 작을수 없습니다.');
			return false;
		}
		
		pageNo = pageNo || 1;
		$("#start").val(pageNo);

		var form = new commSubmit('requestInfoForm');
		form.setUrl( _ctx + "/lib/spending/refundBookInfo");
		form.submit();
	},
	
	/**
	 * 엑셀 다운로드
	 */
	excelDownload : function() {
		if(confirm("엑셀파일을 다운로드 하시겠습니까?")) {
			var form = new commSubmit('requestInfoForm');
			form.setUrl(_ctx + "/lib/spending/refundBookInfoToExcel");
			form.submit();
		}
	}
};

// onload 
$(document).ready(function() {
	// 지줄결의
	libSpending.init();
	// 반품도서
	libRefund.init();
});