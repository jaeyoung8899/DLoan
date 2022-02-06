
/**
 * desc : 납품요청 상세
 * auth : shon
 * storeResponse.js
 */

var storeDeliveryReqInfo = {
		
	deleteKey : [],
	
	init : function () {
		this.events();
		
	},
	
	events : function () {
		
		// 저장
		$("#btnSave").on('click', function (event) {
			storeDeliveryReqInfo.saveStoreResponse();
		});
		
		// 엑셀다운
		$("#btnExcelDown").on('click', function (event) {
			storeDeliveryReqInfo.excelDown();
		});
	},
	
	/**
	 * 조회
	 */
	search : function(pageNo) {
		
		var form = new commSubmit('searchInfoForm');
		form.setUrl( _ctx + "/store/storeDeliveryReqInfo");
		form.submit();
	},
	
	
	saveStoreResponse : function () {
		if (!commFormValid('searchInfoForm')) {
			return false;
		}
		
		var isChk = true;
		var ischkLength = $("#detailInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
		
		if (ischkLength == 0) {
			alert('선택된 데이터가 없습니다.');
			return false;
		}
		
		var arRecKey = new Array();
		$("#detailInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").each(function(i, el) {
			arRecKey.push($(el).parent().parent().find('input[name=recKey]').val());
		});

		if (!isChk) {
			return false;
		} 
		
		if (confirm('저장 하시겠습니까?')) {
			var params = {
				ltRecKey      : arRecKey,
				resKey        : $('#resKey').val(),
				resTitle      : $('#resTitle').val(),
				libManageCode : $('#selLib').val(),
				resStatus     : $('#selStatus').val(),
				ltDelKey      : []
			};
			
			var option = {
				url   : _ctx + '/store/saveStoreResponse',
				param : $.param(params, true)
			};
			 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					alert('저장되었습니다.');
					document.location.href = _ctx + '/store/storeResponseInfo';
					resKey
				} else {
					alert(result.resultMessage);
				}
			});
		}
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
			var form = new commSubmit('searchInfoForm');
			form.setUrl(_ctx + "/store/storeDeliveryReqInfoToExcel");
			comm.loading.hide();
			form.submit();
		}
	}
};

// onload 
$(document).ready(function() {
	// 신청승인
	storeDeliveryReqInfo.init();
	// 서점 공통
	storecomm.init();
});