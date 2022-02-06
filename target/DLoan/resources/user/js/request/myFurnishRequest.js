
/**
 * desc : myFurnishRequest
 * auth : shon
 * myFurnishRequest.js
 */

var myRequest = {
	init : function () {
		this.events();
		
		$("#from_reqDate,#to_reqDate, #from_loanDate, #to_loanDate, #from_returnPlanDate, #to_returnPlanDate, #from_returnDate, #to_returnDate" ).datepicker({
			
		});
		
		comm.sort.initSort('myRequestInfo');
	},
	
	
	events : function () {
		// 조회버튼
		$("#btnSearch").on('click', function (event) {
			//
			myRequest.search(1);
		});
		
		// 신청 취소
		$("#btnReqCancel").on('click', function (event) {
			myRequest.userRequestCancel();
		});
		
		// 서점, 도서관 신청현황
		$(":button[name='selectObject']").on('click', function() {
			var reqForm = document.requestForm;
			
			reqForm.from_reqDate.value = "";
			reqForm.to_reqDate.value = "";
			reqForm.from_loanDate.value = "";
			reqForm.to_loanDate.value = "";
			reqForm.from_returnDate.value = "";
			reqForm.to_returnDate.value = "";
			reqForm.from_returnPlanDate.value = "";
			reqForm.to_returnPlanDate.value = "";
			reqForm.reqStatus.value = "";
			reqForm.sortCol.value = "";
			reqForm.order.value = "";
			reqForm.start.value = 1;
			
			if($(this).val() == 'bookstore') {
				reqForm.tab.value = "bookstore";
			} else if($(this).val() == 'library') {
				reqForm.tab.value = "library";
			}
			
			var requestForm = new commSubmit('requestForm');
			requestForm.setUrl( _ctx + "/myRequestInfo");
			requestForm.submit();
		});
	},
		
	/**
	 * 조회
	 */
	search : function(pageNo) {
		
		//vlaidatiton

		if (!commFormValid('requestForm')) {
			return false;
		}
		
		if (!comm.date.compare($('#from_reqDate').val(), $('#to_reqDate').val(), 'yyyy-MM-dd')) {
			alert('신청시작일 보다 신청종료일 작을수는 없습니다.');
			return false;
		}
		if (!comm.date.compare($('#from_loanDate').val(), $('#to_loanDate').val(), 'yyyy-MM-dd')) {
			alert('대출시작일 보다 대출종료일 작을수는 없습니다.');
			return false;
		}
		if (!comm.date.compare($('#from_returnPlanDate').val(), $('#to_returnPlanDate').val(), 'yyyy-MM-dd')) {
			alert('반납예정시작일 보다 반납예정종료일 작을수는 없습니다.');
			return false;
		}
		if (!comm.date.compare($('#from_returnDate').val(), $('#to_returnDate').val(), 'yyyy-MM-dd')) {
			alert('반납시작일 보다 반납종료일 작을수는 없습니다.');
			return false;
		}
		
		
		$("#start").val(pageNo);
		
		var form = new commSubmit('requestForm');
		form.setUrl( _ctx + "/myRequestInfo");
		form.submit();
	},
	
	/**
	 * 신청 취소
	 */
	userRequestCancel : function() {

		var isChk = true;
		var arRecKey    = new Array();
		var arReqStatus = new Array();
		
		var ischkLength = $("#myRequestInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
		
		if (ischkLength == 0) {
			alert('선택된 데이터가 없습니다.');
			return false;
		}
		
		$("#myRequestInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
			if ($(el).is(":checked")) {
				
				if($(this).val() == '1') {
					arReqStatus.push($(this).val());
					arRecKey.push($(this).parent().parent().find('input').val());
				} else {
					alert('신청중인 도서만 신청취소 가능합니다.')
					isChk = false;
					return false;
				}
			}
		});
		
		if (!isChk) {
			return false;
		}
		
		if (confirm('선택된 데이터를 신청취소 하시겠습니까?')) {
			
			var params = {
				ltRecKey    : arRecKey,
				ltReqStatus : arReqStatus
			};
			
			var option = {
				url   : _ctx + '/userFurnishRequestCancel',
				param : $.param(params, true)
			};
			
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					myRequest.search(1);
				} else {
					alert(result.resultMessage);
				}
			});
		}
	},
	
	
	checkedAll : function ($this, tabId, checkVal, isInclude) {
		var isChk = $($this).is(":checked");
		var name = $($this).data('name');
		
		var ar = checkVal.split("|");
		
		$('#' + tabId + " > tbody > tr > td > input:checkbox[name='"+name+"']").each(function(i, el) {
			for (var i = 0; i < ar.length; i++) {
				if (isInclude) {
					if (ar[i] == $(el).val()) {
						 $(el).prop("checked", isChk);
					}
				} else {
					if (ar[i] != $(el).val()) {
						 $(el).prop("checked", isChk);
					}
				}
			}
		});
	},
};


// onload 
$(document).ready(function() {
	myRequest.init();
});