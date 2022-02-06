
/**
 * desc : 신청 승인
 * auth : shon
 * libRequest.js
 */

var libRequest = {
	init : function () {
		this.events();
		
		$("#from_reqDate,#to_reqDate").datepicker({
			
		});

		comm.sort.initSort('requestInfo');
		libcomm.headerCheck();
		libRequest.useBtnYn();
	},
	
	events : function () {
		// event
		$('#userNo, #author, #publisher, #title').on('keydown', function(e) {
			var keyCode = e.keyCode || e.which;
			if (keyCode == 13) {
				libRequest.search(1);
			}
		});
		
		// 조회버튼
		$("#btnSearch").on('click', function (event) {
			libRequest.search(1);
		});
		
		// 초기화버튼
		$("#btnClear").on('click', function (event) {
			$("#requestInfoForm select").val("");
			$("#requestInfoForm input[type=text]").val("");
		});
		
		// 신청거절
		$("#btnLibReqCancel").on('click', function (event) {
			var btnId = this.id;
			var isChk = true;
			var ischkLength = $("#requestInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
			
			if (ischkLength == 0) {
				alert('선택된 데이터가 없습니다.');
				return false;
			}
			
			var arStatus = new Array();
			$("#requestInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
				if ($(el).is(":checked")) {
					if ($(el).val() != 'U01' &&
						$(el).val() != 'S03' &&
						$(el).val() != 'S04' &&
						$(el).val() != 'S05') {
						alert('신청중, 도서관확인요청, 도서준비, 대출대기인 도서만 도서관신청거절 변경가능합니다.');
						isChk = false;
						return false;
					}
					arStatus.push($(this).val());
				}
			});

			if (!isChk) {
				return false;
			}
			
			var sLength = arStatus.length;
			
			if (sLength > 1) {
				var re = new RegExp(arStatus[0], "g");
				var status = arStatus.join('').replace(re, "");
				
				if (!$.isEmpty(status)) {
					alert('진행상태가 동일한 자료만 선택 가능합니다.');
					return false;
				}
			}
			
			smsSendPop.smsPop();
		});
		
		// 상태변경 버튼
		$("#btnReqStatus").on('click', function (event) {
			libRequest.updateLibReqStatus();
		});
		
		// 제외도서등록
		$("#btnLimitBook").on('click', function (event) {
			libRequest.updateLimitBook();
		});
		
		// 2019.04.23 엑셀다운로드 버튼 추가
		$("#btnReqExcel").on('click', function() {
			libRequest.excelDownload();
		});
		
		// 2019.04.25 취소 버튼 추가
		$("#btnCancel").on('click', function() {
			libRequest.updateLibReqCancel();
		});
		
		//2020.01.30 배분도서관수정 추가
		$("#btnLibChange").on('click', function() {
			libRequest.libChangePop();
		});
		
		// 닫기 버튼
		$("#btnChangeModalClose").on('click', function (event) {
			libRequest.changePopClose();
		});
		
		$("#btnChange").on('click', function (event) {
			libRequest.changeLib();
		});
	},
	
	/**
	 * 조회
	 */
	search : function(pageNo) {
		if (!commFormValid('requestInfoForm')) {
			return false;
		}
		
		if (!comm.date.compare($('#from_reqDate').val(), $('#to_reqDate').val(), 'yyyy-MM-dd')) {
			alert('신청시작일 보다 신청종료일은 작을수는 없습니다.');
			return false;
		}
		
		$("#start").val(pageNo);

		var form = new commSubmit('requestInfoForm');
		form.setUrl( _ctx + "/lib/libRequestInfo");
		form.submit();
	},
	
	/**
	 * 도서관 승인
	 */
	updateLibReqStatus : function () {
		var isChk = true;
		var arRecKey    = new Array();
		var arReqStatus = new Array();
		
		var ischkLength = $("#requestInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
		
		if (ischkLength == 0) {
			alert('선택된 데이터가 없습니다.');
			return false;
		}
		
		/*
		 * U01 : 신청중
		 * U02 : 신청취소
		 * S02 : 서점신청거절 (신청거절)
		 * S03 : 도서관확인요청 (신청중)
		 * S04 : 도서준비
		 * S05 : 대출대기
		 * S06 : 대출
		 * S07 : 반납
		 * S08 : 미대출취소
		 * L01 : 도서관승인 (도서준비)
		 * L02 : 도서관신청거절 (신청거절)
		 */
		var reqStatus = '';
		$("#requestInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
			if ($(el).is(":checked")) {
				if ($(this).val() == 'S03') {
					reqStatus = 'L01';
					arRecKey.push($(el).parent().parent().find('input[name=recKey]').val());
				} else {
					alert('도서관확인요청 자료의 경우만 도서관승인으로 변경가능합니다.');
					isChk = false;
					return false;
				}
			}
		});
		
		if (!isChk) {
			return false;
		}
		
		if (confirm('선택된 데이터를 도서관승인으로 상태변경 하시겠습니까?')) {
			var params = {
				ltRecKey  : arRecKey,
				reqStatus : reqStatus,
			};
			
			var option = {
				url   : _ctx + '/lib/updateLibReqStatus',
				param : $.param(params, true)
			};
			 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					libRequest.search(1);
				} else {
					alert(result.resultMessage);
				}
			});
		}
	},
	
	/**
	 * 제외도서등록
	 */
	updateLimitBook : function () {
		
		var ischkLength = $("#requestInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
		if (ischkLength == 0) {
			alert('선택된 데이터가 없습니다.');
			return false;
		}
		
		var isChk    = true;
		var arRecKey = new Array();
		
		$("#requestInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").each(function(i, el) {
			if ($(this).val() == 'S02' || $(this).val() == 'L02') {
				arRecKey.push($(el).parent().parent().find('input[name=recKey]').val());
			} else {
				alert('신청거절 자료의 경우만 제외도서로 등록이 가능합니다.');
				isChk = false;
				return false;
			}
		});
		
		if (!isChk) {
			return false;
		}
		
		if (confirm('선택된 자료를 제외도서로 등록 하시겠습니까?')) {
			
			var option = {
				url   : _ctx + '/lib/updateLimitBook',
				param : $.param({
					ltRecKey : arRecKey
				}, true)
			};
			
			$.commAjax (option, function (result) {
				alert("제외도서로 등록하였습니다.");
			});
		}
	},
	
	/**
	 * 체크박스 모두 체크
	 */
	checkedAll :  function ($this, tabId) {
		var isChk = $($this).is(":checked");
		var name = $($this).data('name');
		$('#' + tabId + " > tbody > tr > td > input:checkbox[name='"+name+"']").each(function(i, el) {
			$(el).prop("checked", isChk)
		});
	},
	
	/**
	 * 2019.04.24 
	 * 엑셀 다운로드 추가
	 */
	excelDownload : function() {
		if(confirm("엑셀파일을 다운로드 하시겠습니까?")) {
			var form = new commSubmit('requestInfoForm');
			form.setUrl(_ctx + "/lib/libRequestInfoToExcel");
			form.submit();
		}
	},
	
	/**
	 * 2019.04.25
	 * 도서관 승인, 신청거절 > 도서관 확인요청 기능 추가
	 */
	updateLibReqCancel : function () {
		var isChk = true;
		var arRecKey    = new Array();
		var arReqStatus = new Array();
		
		var ischkLength = $("#requestInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
		
		if (ischkLength == 0) {
			alert('선택된 데이터가 없습니다.');
			return false;
		}
		
		var reqStatus = '';
		$("#requestInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
			if ($(el).is(":checked")) {
				if ($(this).val() == 'L01' || $(this).val() == 'L02') {
					reqStatus = 'S03';
					arRecKey.push($(el).parent().parent().find('input[name=recKey]').val());
				} else {
					alert('도서관 승인,신청거절 자료만 도서관 확인요청 상태로 변경할 수 있습니다.');
					isChk = false;
					return false;
				}
			}
		});
		
		if (!isChk) {
			return false;
		}
		
		if (confirm('선택된 데이터를 도서관 확인요청 상태로 변경하시겠습니까?')) {
			var params = {
				ltRecKey  : arRecKey,
				reqStatus : reqStatus,
			};
			
			var option = {
				url   : _ctx + '/lib/updateLibReqStatus',
				param : $.param(params, true)
			};
			 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					libRequest.search(1);
				} else {
					alert(result.resultMessage);
				}
			});
		}
	},
	
	//2020.01.30 배분도서관 수정 추가
	libChangePop : function() {
		
		
		var arAll    = new Array();

		var ischkLength = $("#requestInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
		
		if (ischkLength == 0) {
			alert('선택된 데이터가 없습니다.');
			return false;
		}
		
		$("#requestInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
			if ($(el).is(":checked")) {
				var arRecKey  =  {};
				arRecKey["recKey"]=$(el).parent().parent().find('input[name=recKey]').val();
				arRecKey["userNo"]=$(el).parent().parent().find('input[name=userNo]').val();
				arRecKey["title"]=$(el).parent().siblings('td.title').text();
				arRecKey["libManageName"]=$(el).parent().siblings('td.libManageName').text();
				arRecKey["userName"]=$(el).parent().siblings('td.userName').text();
				arRecKey["storeName"]=$(el).parent().siblings('td.storeName').text();
				console.log(arRecKey);
				arAll.push(arRecKey);
			}
		});
		
		var appendList = "";
		for(var i=0; i<arAll.length; i++){
			appendList +='<tr>';
			appendList +='<input type="hidden" name="recKey" value="'+arAll[i].recKey+'"  />';
			appendList +='<input type="hidden" name="userNo" value="'+arAll[i].userNo+'"  />';
			appendList +='<td class="storeName">'+arAll[i].storeName+'</td>'
			appendList +='<td class=libManageName>'+arAll[i].libManageName+'</td>';
			appendList +='<td class="title" style="text-align:left;">'+arAll[i].title+'</td>';
			appendList +='<td class="userNo">'+arAll[i].userNo+'</td>';
			appendList +='<td class="userName">'+arAll[i].userName+'</td>';
			appendList +='</tr>';
		}
		$(".change_tbody").html(appendList);
		$("#libChangePop").show();
	},
	
	//2020.01.30 배분도서관 수정 추가
	changeLib : function () {
		var arRecKey    = new Array();
		
		$("#requestInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
			if ($(el).is(":checked")) {
				arRecKey.push($(el).parent().parent().find('input[name=recKey]').val());
			}
		});
		
		if ($.isEmpty($('#libChange option:selected').val())) {
			alert('배분도서관을 선택하세요.');
			$('#libChange').focus();
			return false;
		}
		
		if (confirm('배분 도서관 수정을 하시겠습니까?')) {
			var params = {
				changeLib : $('#libChange option:selected').val(), 
				ltRecKey : arRecKey
			};
			
			var option = {
				url   : _ctx + '/lib/updateLibrary',
				param : $.param(params, true)
			};
			 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					$("#libChangePop").hide();
					libRequest.search(1);
				} else {
					alert(result.resultMessage);
				}
			});
		}
	},
	
	changePopClose : function() {
		$("#libChangePop").hide();
	},

	useBtnYn : function (){
		if(comm.getViewOptionData_value('007','ALL') === 'Y'){
			$('#btnReqReturn').show();
		}

		if(comm.getViewOptionData_value('008','ALL') === 'Y'){
			$('#btnReqStatusS04').show();
		}

	}
};

var smsSendPop = {
	init : function () {
		this.events();
	},

	events : function () {

		// 닫기 버튼
		$("#btnModalClose").on('click', function (event) {
			smsSendPop.smsPopClose();
		});
		
		// 거절사유 변경
		$("#selReason").on('change', function (event) {
			if (!$.isEmpty($(this).val())) {
				$('#textArea').val($(this).val());
			}
		});
		
		// 
		$(":radio[name=formRadio]").on('change', function (event) {
			if ($(this).val() == 'self') {
				$('#selReason').attr('readonly', true).val('');
				$('#textArea') .attr('readonly', false).val('');
			} else {
				$('#selReason').attr('readonly', false);
				$('#textArea') .attr('readonly', true);
			}
		});
		
		// 발송 
		$('#btnSend').on('click', function(event) {
			smsSendPop.smsSend();
		});
	},
	
	smsPop : function() {
		
		$('#selReason') .val('');
		$('#textArea')  .val('');
		$('#senderInfo').val('');
		this.senderInfo();
		
		$("#smsSendPop").show();
	},
	
	senderInfo : function() {
		
		var isChkLength   = $("#requestInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
		
		var userNo = '';
		$("#requestInfo > tbody > tr").each(function(i, el) {
			var chk = $(this).find("input:checkbox[name='chkNm']").is(":checked");
			if (chk) {
				userNo = $(this).find("input[name='userNo']").val();
				return false;
			}
		});
		
		if (isChkLength == 0) {
			$('#senderInfo').val('');
		} else if (isChkLength == 1) {
			$('#senderInfo').val(userNo);
		} else {
			$('#senderInfo').val(userNo + ' 외' + (isChkLength -1) + '명');
		}
	},
	
	smsPopClose : function() {
		//$("#smsSendPop").dialog("close");
		$("#smsSendPop").hide();
	}, 
	
	smsSend : function () {
		var arRecKey    = new Array();
		
		$("#requestInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
			if ($(el).is(":checked")) {
				arRecKey.push($(el).parent().parent().find('input[name=recKey]').val());
			}
		});
		
		if ($.isEmpty($('#textArea').val())) {
			alert('메세지 내용을 입력하세요');
			$('#textArea').focus();
			return false;
		}
		
		if (confirm('도서관신청거절 하시겠습니까?')) {
			var params = {
				msgType  : $(":radio[name=formRadio]").val(),
				msg      : $('#textArea').val(),
				ltRecKey : arRecKey,
			};
			
			var option = {
				url   : _ctx + '/lib/updateCancelReason',
				param : $.param(params, true)
			};
			 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					libRequest.search(1);
				} else {
					alert(result.resultMessage);
				}
			});
		}
	}
};

// onload 
$(document).ready(function() {
	// 신청승인
	libRequest.init();
	
	// sms send popup
	smsSendPop.init();
});