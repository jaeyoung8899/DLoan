
/**
 * desc : 도서관신청관리
 * auth : shin
 * libReqMng.js
 */

var libReqMng = {
	
	init : function () {
		this.events();
	},
	
	events : function () {
		// datepicker
		$("input[id^=reqLimit").datepicker();
		
		// 저장 버튼
		$("#btnSave").on('click', function() {
			libReqMng.updateLibraryLimit();
		});
		
		// 신청제한, 목록표시제한 전체 선택
		$("#limitYN, #listYN").on('click', function() {
			libReqMng.allCheck($(this));
		});
	},
	
	updateLibraryLimit : function () {
		var libManageCodeArr = new Array(),
			limitYNArr       = new Array(),
			listYNArr        = new Array(),
			limitDate1Arr    = new Array(),
			limitDate2Arr    = new Array(),
			limitReasonArr   = new Array();
		
		var libManageCode, limitYN, listYN, limitDate1, limitDate2, limitReason;
		var isChk = true;
		
		$("#libInfo > tbody > tr").each(function(i, el) {
			libManageCode = $(this).find('input[name=libManageCode]').val();
			limitYN       = $(this).find('input[name=limitYN]').is(":checked") ? "Y" : "N";
			listYN        = $(this).find('input[name=listYN]').is(":checked") ? "Y" : "N";
			limitDate1    = $(this).find('input[name=limitDate1]').val();
			limitDate2    = $(this).find('input[name=limitDate2]').val();
			limitReason   = $(this).find('input[name=limitReason]').val();
			
			libManageCodeArr.push(libManageCode);
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
					libManageCode : libManageCodeArr.join(","),
					limitYN       : limitYNArr.join(","),
					listYN        : listYNArr.join(","),
					limitDate1    : limitDate1Arr.join(","),
					limitDate2    : limitDate2Arr.join(","),
					limitReason   : limitReasonArr.join(",")
			};
			
			var option = {
					url      :  _ctx + '/lib/envset/updateLibraryLimit',
					param    : $.param(params, true) 
			};
			
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					alert('저장 되었습니다.');
					location.href = "/lib/envset/libReqMng";
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
	libReqMng.init();
});