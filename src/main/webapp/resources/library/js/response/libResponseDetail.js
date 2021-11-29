
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

		if(comm.getViewOptionData_value('009','ALL') === 'Y'){
			$('#pdfYn').css('display','inline');
		}

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
		var arReturnBookReason = new Array();
		$("#detailInfo > tbody > tr").each(function(i, el) {
			arRecKey.push($(el).find('input[name=recKey]').val());
			arResStatus.push($(el).find(':radio[name*="choice_"]:checked').val());

			if($(el).find('input[name=returnReason]').val()==""){
				arReturnBookReason.push(" ");
			}else{
				arReturnBookReason.push($(el).find('input[name=returnReason]').val());
			}
		});
		
		
		if (confirm('저장 하시겠습니까?')) {
			var params = {
				ltRecKey    : arRecKey,
				resKey      : $('#resKey').val(),
				ltResStatus : arResStatus,
				ltReturnBookReason : arReturnBookReason
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
		if( type == 'L02'){
			// 반품
			$('#txtReturnReason').val('');
			returnReasonPop.strType = "ALL";
			returnReasonPop.open();
		}else{
			//구입
			$("#detailInfo > tbody > tr > td > :radio[name*='choice_']").each(function(i, el) {
				var isDisabled = $(el).parent().find(':radio').attr('disabled');
				if (isDisabled == 'disabled' || isDisabled == true) {
					return false;
				}
				$(el).parent().find("[id*=prevRadioValue_]").val('L01');
				var name = $(el).parent().find(':radio').attr('name');
				$(el).parent().find(":radio[name='"+name+"']:radio[value='"+type+"']").prop('checked', true);
			});
		}
	},
	
	excelDown : function() {
		if(confirm("엑셀 다운로드 하시겠습니까?")) {
			var form = new commSubmit('searchInfoForm');
			form.setUrl( _ctx + "/lib/libResExcelDown");
			form.submit();
		}
	},

	/**
	 * 반품 버튼 클릭
	 */
	retClick : function (row) {

		returnReasonPop.vPrevVal = $("#prevRadioValue_"+row).val();

		returnReasonPop.nRow = row;
		returnReasonPop.open();
		$("#prevRadioValue_"+row).val('L02');
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
	},
	/**
	 * 반품 내용 확인 버튼
	 */
	retPopup : function(row) {
		returnReasonPop.nRow = row;
		returnReasonPop.open();
	},
	
};

//반품사유 팝업
var returnReasonPop = {
	nRow : 0 ,
	vPrevVal : "L01",
	strType : "",
	init : function () {
		this.events();
	},
	// 버튼 초기화
	events : function () {
		// 닫기 버튼 ()
		$("#btnModalClose").on('click', function (event) {
			returnReasonPop.close();
		});
		// 확인 버튼
		$('#btnApply').on('click', function(event) {
			returnReasonPop.apply();
		});
		// 취소 버튼
		$('#btnCancel').on('click', function(event) {
			returnReasonPop.close();
		});
		// 닫기 버튼 (그냥닫음)
		$("#btnClose").on('click', function (event) {
			returnReasonPop.strType = "ALL";
			returnReasonPop.close();
		});
	},

	// 팝업 띄울 때 작업
	open : function() {

		if(returnReasonPop.strType == "ALL"){
			$("#txtBookTitle").text("반품사유 (일괄수정) ");
			$("#returnReasonPop").show();
		}else{
			if(returnReasonPop.nRow < 1 ){
				alert("오류가 발생했습니다.");
				//$("input:radio[name='"+radioName+"']:radio[value='L01']").prop('checked', true);
				return;
			}


			var sTitle = $("#tbTitle_"+returnReasonPop.nRow).text();
			if( sTitle.length > 45 ) sTitle = sTitle.substr(0,45)+"...";

			$("#txtBookTitle").text("반품사유 ("+sTitle+") ");

			// 반품 사유 
			var v_returnReason = $('input[name=returnReason]')[returnReasonPop.nRow-1].value;
			$('#txtReturnReason').val(v_returnReason);
			$("#returnReasonPop").show();
		}
	},

	//, 취소버튼 클릭
	close : function() {

		if(returnReasonPop.strType == "ALL"){

			returnReasonPop.strType = "";
		}else{
			var prevCheck = returnReasonPop.vPrevVal;

			$("#prevRadioValue_"+returnReasonPop.nRow).val(prevCheck);
			//요청은 구입과 동일하게 
			if(prevCheck == 'S01') prevCheck = 'L01';
			// 이전으로 되돌리기
			$("input:radio[name='choice_"+returnReasonPop.nRow+"']:radio[value='"+prevCheck+"']").prop('checked', true);
		}
		$("#returnReasonPop").hide();
	},

	// 확인 버튼 클릭
	apply : function () {

		if(returnReasonPop.strType == "ALL"){
			$("#detailInfo > tbody > tr > td > :radio[name*='choice_']").each(function(i, el) {

				var isDisabled = $(el).parent().find(':radio').attr('disabled');
				if (isDisabled == 'disabled' || isDisabled == true) {
					return false;
				}

				// 반품사유
				$(el).parent().find("[name='returnReason']").val($('#txtReturnReason').val());
				// 라디오버튼 체크
				var name = $(el).parent().find(':radio').attr('name');
				$(el).parent().find(":radio[name='"+name+"']:radio[value='L02']").prop('checked', true);
				// 이전값 입력
				$(el).parent().find("[id*=prevRadioValue_]").val('L02');

			});

			returnReasonPop.strType = "";
			$("#returnReasonPop").hide();
		}else{
			if($("#txtReturnReason").val().length < 1){
				alert("반품사유를 입력해 주세요.");
				return;
			}
			$('input[name=returnReason]')[returnReasonPop.nRow-1].value = $('#txtReturnReason').val();
			$("#returnReasonPop").hide();
		}
	}
};


// onload 
$(document).ready(function() {
	// 신청승인
	libResponseDetail.init();

	returnReasonPop.init();
});