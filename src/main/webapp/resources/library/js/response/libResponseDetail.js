
/**
 * desc : 납품요청 상세
 * auth : shon
 * libResponseDetail.js
 */

var libResponseDetail = {
		
	deleteKey : [],
	
	init : function () {
		this.events();
	},
	
	events : function () {
		
		// 조회버튼
		$("#btnSearch").on('click', function (event) {
			libResponseDetail.search(1);
		});
		
		// 저장
		$("#btnSave").on('click', function (event) {
			libResponseDetail.saveLibApprove();
		});
		
		// 요청
		$("#btnSaveBk").on('click', function (event) {
			libResponseDetail.saveLibApproveBk();
		});
		
		// 목록
		$("#btnList").on('click', function (event) {
			document.location.href = _ctx + '/lib/libResponseInfo'
		});
		
		// 엑셀다운
		$("#btnExcelDown").on('click', function (event) {
			libResponseDetail.excelDown();
		});
		
		// 2021.09.28 거래명세서 EXCEL
		$("#btnExcelDown2").on('click', function() {
			libResponseDetail.excelDownload();
		});
		
		// 2021.09.28 거래명세서 PDF
		$("#btnPDFDown2").on('click', function (event) {
			libResponseDetail.pdfDownload();
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
		
		var form = new commSubmit('searchInfoForm');
		form.setUrl( _ctx + "/lib/libResponseInfoDetail");
		form.submit();
	},
	
	/**
	 * 납품 승인
	 */
	saveLibApprove : function () {
		
		var isChk = true;
		var ischkLength = $("#detailInfo > tbody > tr").length;
		
		if (ischkLength == 0) {
			alert('저장 할 데이터가 없습니다.');
			return false;
		}
		
		var arRecKey    = new Array();
		var arResStatus = new Array();
		$("#detailInfo > tbody > tr").each(function(i, el) {
			arRecKey.push($(el).find('input[name=recKey]').val());
			arResStatus.push($(el).find(':radio[name*="choice_"]:checked').val());
		});
		
		
		if (confirm('저장 하시겠습니까?')) {
			var params = {
				ltRecKey    : arRecKey,
				resKey      : $('#resKey').val(),
				ltResStatus : arResStatus,
			};
			
			var option = {
				url   : _ctx + '/lib/saveLibApprove',
				param : $.param(params, true)
			};
			 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					alert('저장되었습니다.');
					libResponseDetail.search(1);
					resKey
				} else {
					alert(result.resultMessage);
				}
			});
		}
	},
	
	/**
	 * 요청
	 */
	saveLibApproveBk : function () {
		
		if (confirm('변경 하시겠습니까?')) {
			var params = {
				resKey      : $('#resKey').val(),
			};
			
			var option = {
				url   : _ctx + '/lib/saveLibApproveBk',
				param : $.param(params, true)
			};
			 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					alert('변경되었습니다.');
					libResponseDetail.search(1);
					resKey
				} else {
					alert(result.resultMessage);
				}
			});
		}
	},
	radioAll :  function (type) {
		$("#detailInfo > tbody > tr > td > :radio[name*='choice_']").each(function(i, el) {
			
			var isDisabled = $(el).parent().find(':radio').attr('disabled');
			if (isDisabled == 'disabled' || isDisabled == true) {
				return false;
			}
			
			var name = $(el).parent().find(':radio').attr('name');
			$(el).parent().find(":radio[name='"+name+"']:radio[value='"+type+"']").prop('checked', true);
		});
	},
	
	excelDown : function() {
		if(confirm("엑셀 다운로드 하시겠습니까?")) {
			var form = new commSubmit('searchInfoForm');
			form.setUrl( _ctx + "/lib/libResExcelDown");
			form.submit();
		}
	},
		
	/**
	 * 2021.09.28
	 * 템플릿을 활용한 엑셀 다운로드
	 */
	excelDownload : function() {
		if(confirm("거래명세서를 엑셀로 다운로드 하시겠습니까?")) {
			var form = new commSubmit('searchInfoForm');
			form.setUrl( _ctx + "/lib/libResExcelDownload");
			form.submit();
		}
	},
	
	/**
	 * 2021.09.28
	 * PDF 다운로드
	 */
	pdfDownload : function() {
		if(confirm("거래명세서 PDF를 다운로드 하시겠습니까?")) {
			var form = new commSubmit('searchInfoForm');
			form.setUrl( _ctx + "/lib/libResPdfDownload");
			form.submit();
		}
	}
	
};

// onload 
$(document).ready(function() {
	// 신청승인
	libResponseDetail.init();
});