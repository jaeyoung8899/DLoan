
/**
 * desc : 서점신청관리
 * auth : shin
 * storeReqMng.js
 */

var storeReqMng = {
	
	init : function () {
		this.events();
	},
	
	events : function () {
		// datepicker
		$("input[id^=reqLimit").datepicker();
		
		// 저장 버튼
		$("#btnSave").on('click', function() {
			storeReqMng.updateStoreLimit();
		});
		
		// 신청제한, 목록표시제한 전체 선택
		$("#limitYN, #listYN").on('click', function() {
			storeReqMng.allCheck($(this));
		});
	},
	
	updateStoreLimit : function () {
		var storeIdArr     = new Array(),
			limitYNArr     = new Array(),
			listYNArr      = new Array(),
			limitDate1Arr  = new Array(),
			limitDate2Arr  = new Array(),
			limitReasonArr = new Array();
		
		var storeId, limitYN, listYN, limitDate1, limitDate2, limitReason;
		var isChk = true;
		
		$("#storeInfo > tbody > tr").each(function(i, el) {
			storeId     = $(this).find('input[name=storeId]').val();
			limitYN     = $(this).find('input[name=limitYN]').is(":checked") ? "Y" : "N";
			listYN      = $(this).find('input[name=listYN]').is(":checked") ? "Y" : "N";
			limitDate1  = $(this).find('input[name=limitDate1]').val();
			limitDate2  = $(this).find('input[name=limitDate2]').val();
			limitReason = $(this).find('input[name=limitReason]').val();
			
			storeIdArr.push(storeId);
			limitYNArr.push(limitYN);
			listYNArr.push(listYN);
			if(limitReason == undefined || limitReason == "") {
				if(limitYN == "Y") {
					alert('신청제한사유는 필수 입력 항목입니다.');
					$(this).find('input[name=limitReason]').focus();
					isChk = false;
					return false;
				} else {
					limitReasonArr.push("NO MSG");
				}
			} else {
				limitReasonArr.push(limitReason);
			}
			if(comm.date.isDate(limitDate1, 'yyyy-MM-dd')) {
				limitDate1Arr.push(limitDate1);
			} else {
				limitDate1Arr.push("NO DATE");
			}
			if(comm.date.isDate(limitDate2, 'yyyy-MM-dd')) {
				limitDate2Arr.push(limitDate2);
			} else {
				limitDate2Arr.push("NO DATE");
			}
		});
		
		if(isChk && confirm('저장하시겠습니까?')) {
			var params = {
					storeId     : storeIdArr.join(","),
					limitYN     : limitYNArr.join(","),
					listYN      : listYNArr.join(","),
					limitDate1  : limitDate1Arr.join(","),
					limitDate2  : limitDate2Arr.join(","),
					limitReason : limitReasonArr.join(",")
			};
			
			var option = {
					url      :  _ctx + '/lib/envset/updateStoreLimit',
					param    : $.param(params, true) 
			};
			
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					alert('저장 되었습니다.');
					location.href = "/lib/envset/storeReqMng";
				} else {
					alert(result.resultMessage);
				}
			});
		}
	},
	
	allCheck : function(obj) {
		$("input[name=" + obj.attr('id') + "]").prop('checked', obj.is(":checked"));
	}
};

// onload 
$(document).ready(function() {
	storeReqMng.init();
});