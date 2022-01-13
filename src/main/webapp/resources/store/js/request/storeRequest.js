
/**
 * desc : 신청 승인
 * auth : shon
 * storeRequest.js
 */

var storeRequest = {
	init : function () {
		this.events();
		
		$("#from_reqDate,#to_reqDate").datepicker({
			
		});

		comm.sort.initSort('requestInfo');
	},
	
	events : function () {
		
		
		$('#userNo, #author, #publisher, #title, #isbn').on('keydown', function(e) {
			var keyCode = e.keyCode || e.which;
			if (keyCode == 13) {
				storeRequest.search(1);
			}
		});
		
		// 조회버튼
		$("#btnSearch").on('click', function (event) {
			storeRequest.search(1);
		});
		
		// 초기화버튼
		$("#btnClear").on('click', function (event) {
			$("#requestInfoForm select").val("");
			$("#requestInfoForm input[type=text]").val("");
		});
		
		// SMS발송, 서점신청거절
		$("#btnSMSSend, #btnStoreReqCancel").on('click', function (event) {

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
					
					if (btnId == 'btnStoreReqCancel') {
						if ($(el).val() != 'U01' && $(el).val() != 'S04') {
							alert('신청중, 도서준비중인 도서만 서점신청거절 상태로 변경가능합니다.');
							isChk = false;
							return false;
						}
					}
					
					arStatus.push($(this).val());
					
					if (btnId == 'btnSMSSend') {
						if ($(this).parent().siblings('td:eq(9)').text() == 'N') {
							alert('SMS수신여부를 확인바랍니다.');
							isChk = false;
							return false;
						}
					}
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
			
			smsSendPop.smsPop(this.id);
		});
		
		// 상태변경 버튼
		$(":button[id*=btnReqStatus]").on('click', function (event) {
			var type = 'ST0' + new String(this.id).replace(/[^0-9]/g, "");
			
			storeRequest.updateStoreReqStatus(type);
		});
		
		// 엑셀다운
		$("#btnExcelDown").on('click', function (event) {
			storeRequest.excelDown();
		});
		
		// 단계 되돌리기 2020/04/06 AGK 추가
		$("#btnPrevStatus").on('click', function (event) {
			storeRequest.prevStatus();
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
		form.setUrl( _ctx + "/store/storeRequestInfo");
		console.log(form);
		form.submit();
	},
	
	/**
	 * 상태변경
	 */
	updateStoreReqStatus : function (type) {
		
		var ischkLength = $("#requestInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
		if (ischkLength == 0) {
			alert('선택된 데이터가 없습니다.');
			return false;
		}
		
		/*
		U01 : 신청중
		U02 : 신청취소
		S02 : 서점신청거절 (신청거절)
		S03 : 도서관확인요청 (신청중)
		S04 : 도서준비
		S05 : 대출대기
		S06 : 대출
		S07 : 반납
		S08 : 미대출취소
		L01 : 도서관승인 (도서준비)
		L02 : 도서관신청거절 (신청거절)
		*/
		
		var isChk       = true;
		var arRecKey    = new Array();
		var reqStatus   = '';
		var confirmMsg  = '';
		
		$("#requestInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
			
			if ($(el).is(":checked")) {
				
				// 도서관확인요청
				if (type == 'ST02') {
					if ($(this).val() == 'U01') {
						reqStatus = 'S03';
						arRecKey.push($(el).parent().parent().find('input[name=recKey]').val());
					} else {
						alert('신청중인 도서만 도서관확인요청으로 변경가능합니다.');
						isChk = false;
						return false;
					}
				}
				// 도서준비
				else if (type == 'ST03') {
					if ($(this).val() == 'U01' || $(this).val() == 'L01') {
						reqStatus = 'S04';
						arRecKey.push($(el).parent().parent().find('input[name=recKey]').val());
					} else {
						alert('신청중, 도서관승인 자료만 도서준비로 변경가능합니다.');
						isChk = false;
						return false;
					}
				}
				// 대출대기
				else if (type == 'ST04') {
					if ($(this).val() == 'U01' || $(this).val() == 'L01' || $(this).val() == 'S04' || $(this).val() == 'S08') {
						reqStatus = 'S05';
						arRecKey.push($(el).parent().parent().find('input[name=recKey]').val());
					} else {
						alert('신청중, 도서관승인, 도서준비,미대출 취소 자료만 대출대기로 변경가능합니다.');
						isChk = false;
						return false;
					}
				}
			}
		});
		
		if (!isChk) {
			return false;
		}

		if (type == 'ST02') {
			// 도서관 확인요청
			libConfirmPop.open(type, reqStatus, arRecKey);
		} else if (type == 'ST03') {
			// 도서 준비
			loanWaitPop.open(type, reqStatus, arRecKey);
		} else {
			// 대출대기
			if (confirm('선택된 데이터를 대출대기로 상태변경 하시겠습니까?')) {
				storeRequest.updateStoreReqStatusProc(type, reqStatus, arRecKey);
			}
		}
	},
	
	/**
	 * 상태변경
	 */
	updateStoreReqStatusProc : function(type, reqStatus, arRecKey, enterPlanDate, confirmMessage) {
		
		var params = {
			ltRecKey      : arRecKey,
			reqStatus     : reqStatus,
			type          : type,
			enterPlanDate : enterPlanDate,
			confirmMessage: confirmMessage
		};
		
		var option = {
			url   : _ctx + '/store/updateStoreReqStatus',
			param : $.param(params, true)
		};
		 
		$.commAjax (option, function (result) {
			if (result.resultCode == "Y") {
				if (type == 'ST03') {
					loanWaitPop.close();
				}
				storeRequest.search(1);
			} else {
				alert(result.resultMessage);
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
			form.setUrl(_ctx + "/store/storeRequestInfoToExcel");
			comm.loading.hide();
			form.submit();
		}
	},
	
	/*2020/04/06 AGK 추가 이전상태로 진행상태 변경하기*/
	prevStatus : function() {
		var isChk       = true;
		var ischkLength = $("#requestInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
		if (ischkLength == 0) {
			alert('선택된 데이터가 없습니다.');
			return false;
		}
		
		var arRecKey    = new Array();
		var confirmMsg  = '';
		
		$("#requestInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
			if ($(el).is(":checked")) {
				if ($(this).val() == 'U02' || $(this).val() == 'S04' ) {
					arRecKey.push($(el).parent().parent().find('input[name=recKey]').val());
				}else{
					alert('신청취소, 도서준비중 자료만 신청중으로 변경 가능합니다.');
					isChk = false;
					return false;
				}
			}
		});
		
		if (!isChk) {
			return false;
		}

		// 상태 되돌리기
		if (confirm('선택된 데이터의 진행상태를 신청중으로 되돌리시겠습니까?')) {
				storeRequest.updatePrevReqStatusProc(arRecKey);
		}
	},
	
	/**
	 * 이전상태변경
	 */
	updatePrevReqStatusProc : function(arRecKey) {
		
		var params = {
			ltRecKey      : arRecKey
		};
		
		var option = {
			url   : _ctx + '/store/updatePrevReqStatus',
			param : $.param(params, true)
		};
		 
		$.commAjax (option, function (result) {
			if (result.resultCode == "Y") {
				storeRequest.search(1);
			} else {
				alert(result.resultMessage);
			}
		});
	},
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
		
		// sms 변경
		$("#selSms").on('change', function (event) {
			if (!$.isEmpty($(this).val())) {
				
				var contents = $(this).children('option:selected').data('contents');
				$('#textArea').val(contents);
			}
		});
		
		// 
		$(":radio[name=formRadio]").on('change', function (event) {
			if ($(this).val() == 'self') {
				$('#selReason').attr('readonly', true).val('');
				$('#selSms')   .attr('readonly', true).val('');
				$('#textArea') .attr('readonly', false).val('');
			} else {
				$('#selReason').attr('readonly', false);
				$('#selSms')   .attr('readonly', false);
				$('#textArea') .attr('readonly', true);
			}
		});
		
		// 발송 
		$('#btnSend').on('click', function(event) {
			smsSendPop.smsSend();
		});
	},
	
	smsPop : function(id) {
		
		$('#selReason') .val('');
		$('#selSms')    .val('');
		$('#textArea')  .val('');
		$('#senderInfo').val('');
		$('#selSms')    .hide();
		$('#selReason') .hide();
		
		if (id == 'btnSMSSend') {
			$('#btnSend').removeClass('btn_rejection').addClass('btn_send').val('발송');
			$('#selSms').show();
		} else {
			$('#btnSend').removeClass('btn_send').addClass('btn_rejection').val('거절');
			$('#selReason').show();
		}
		
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
		
		var isCancel = $('#btnSend').val() == '거절' ? true : false;
		if (confirm(isCancel ? '서점신청거절 하시겠습니까?' : 'SMS를 발송 하시겠습니까?')) {
			var params = {
				msgType  : $(":radio[name=formRadio]").val(),
				msg      : $('#textArea').val(),
				ltRecKey : arRecKey,
			};
			
			var uri = _ctx + '/store/storeReqSmsSend';
			
			if ($('#btnSend').val() == '거절') {
				uri = _ctx + '/store/updateCancelReason';
			}
			
			var option = {
				url   : uri,
				param : $.param(params, true)
			};
			 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					storeRequest.search(1);
				} else {
					alert(result.resultMessage);
				}
			});
		}
	}
};

var loanWaitPop = {
	type      : null,
	reqStatus : null,
	arRecKey  : null,
		
	init : function () {
		this.events();
	},

	events : function () {
		// 닫기 버튼
		$("#btnModalClose2").on('click', function (event) {
			loanWaitPop.close();
		});
		
		// 확인 버튼
		$("#btnConfirm").on('click', function (event) {
			if (!commFormValid('loanWaitForm')) {
				return false;
			}
			if (confirm('선택된 데이터를 도서준비로 상태변경 하시겠습니까?')) {
				storeRequest.updateStoreReqStatusProc(loanWaitPop.type, loanWaitPop.reqStatus, loanWaitPop.arRecKey, $("#enterPlanDate").val());
			}
		});
	},
	
	open : function(type, reqStatus, arRecKey) {
		loanWaitPop.type      = type;
		loanWaitPop.reqStatus = reqStatus;
		loanWaitPop.arRecKey  = arRecKey;
		$("#loanWaitLayer").show();
	},
	
	close : function() {
		$("#loanWaitLayer").hide();
	}
};

var libConfirmPop = {
	type			: null,
	reqStatus		: null,
	arRecKey		: null,
	confirmMessage	: null,
	
	init : function () {
		this.events();
	},
	
	events : function() {
		// 닫기 버튼
		$("#btnModalClose3").on('click', function() {
			libConfirmPop.close();
		});
		
		// 도서관 확인요청 버튼
		$("#btnConfirm2").on('click', function() {
			if (!commFormValid('libConfirmForm')) {
				return false;
			}
			if (confirm('선택된 데이터를 도서관확인요청 상태로 변경 하시겠습니까?')) {
				libConfirmPop.confirmMessage = $("#libConfirmText").val();
				storeRequest.updateStoreReqStatusProc(libConfirmPop.type, libConfirmPop.reqStatus, libConfirmPop.arRecKey, "", libConfirmPop.confirmMessage);
			}
		});
	},
	
	open : function(type, reqStatus, arRecKey) {
		libConfirmPop.type		= type;
		libConfirmPop.reqStatus	= reqStatus;
		libConfirmPop.arRecKey	= arRecKey;
		$("#libConfirmLayer").show();
		$("#libConfirmText").focus();
	},
	
	close : function() {
		$("#libConfirmLayer").hide();
	}
};

// onload 
$(document).ready(function() {
	// 신청승인
	storeRequest.init();
	// sms 팝업
	smsSendPop.init();
	// 입고예정일
	loanWaitPop.init();
	// 도서관확인요청
	libConfirmPop.init();
	// 서점 공통
	storecomm.init();
});