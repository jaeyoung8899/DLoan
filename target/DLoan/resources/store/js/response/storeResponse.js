
/**
 * desc : 납품요청
 * auth : shon
 * storeResponse.js
 */

var storeResponse = {
	init : function () {
		this.events();
		
		$("#from_resDate,#to_resDate").datepicker({});

		comm.sort.initSort('responseInfo');
	},
	
	events : function () {
		
		// 조회버튼
		$("#btnSearch").on('click', function (event) {
			storeResponse.search(1);
		});
		
		// 초기화버튼
		$("#btnClear").on('click', function (event) {
			$("#requestInfoForm select").val("");
			$("#requestInfoForm input[type=text]").val("");
		});
		
		// 납품완료
		$("#btnComplate").on('click', function (event) {
			storeResponse.updateStoreResStatus();
		});
		
		// 엑셀 다운로드
		$("#btnExcelDown").on('click', function (event) {
			storeResponse.excelDown();
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
		form.setUrl( _ctx + "/store/storeResponseInfo");
		form.submit();
	},
	
	
	updateStoreResStatus : function () {

		var isChk = true;
		var ischkLength = $("#responseInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
		
		if (ischkLength == 0) {
			alert('선택된 데이터가 없습니다.');
			return false;
		}
		
		var arResKey = new Array();
		
		$("#responseInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
			if ($(el).is(":checked")) {
				
				if ($(el).val() != 'L01') {
					alert('납품승인 도서만 납품완료 변경가능합니다.');
					isChk = false;
					return false;
				}

				arResKey.push($(el).parent().parent().find('input[name=recKey]').val());
			}
		});

		if (!isChk) {
			return false;
		} 
		
		if (confirm('선택된 데이터를 납품완료로 상태변경하시겠습니까?')) {
			var params = {
				ltRecKey  : arResKey,
				resStatus : 'S02',
			};
			
			var option = {
				url   : _ctx + '/store/updateStoreResStatus',
				param : $.param(params, true)
			};
			 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					storeResponse.search(1);
				} else {
					alert(result.resultMessage);
				}
			});
		}
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
			form.setUrl(_ctx + "/store/storeResponseInfoToExcel");
			comm.loading.hide();
			form.submit();
		}
	}
};

// onload 
$(document).ready(function() {
	// 신청승인
	storeResponse.init();
	// 서점 공통
	storecomm.init();
});