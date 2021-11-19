
/**
 * desc : 대출 반납
 * auth : shon
 * returnLoan.js
 */

var returnLoan = {
	init : function () {
		this.events();
		
		$("#from_reqDate,#to_reqDate").datepicker({
			
		});

		comm.sort.initSort('returnLoanInfo');
	},
	
	events : function () {
		
		$('#userNo, #author, #publisher, #title, #isbn').on('keydown', function(e) {
			var keyCode = e.keyCode || e.which;
			if (keyCode == 13) {
				returnLoan.search(1);
			}
		});
		
		// 조회버튼
		$("#btnSearch").on('click', function (event) {
			returnLoan.search(1);
		});
		
		// 초기화버튼
		$("#btnClear").on('click', function (event) {
			$("#returnLoanInfoForm select").val("");
			$("#returnLoanInfoForm input[type=text]").val("");
		});
		
		// 발송
		$("#btnSMSSend").on('click', function (event) {

			var btnId = this.id;
			var isChk = true;
			var ischkLength = $("#returnLoanInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
			
			if (ischkLength == 0) {
				alert('선택된 데이터가 없습니다.');
				return false;
			}
			
			var arStatus = new Array();
			$("#returnLoanInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
				if ($(el).is(":checked")) {
					
					
					arStatus.push($(this).val());
					
					if ($(this).parent().siblings('td:eq(11)').text() == 'N') {
						alert('SMS수신여부를 확인바랍니다.');
						
						isChk = false;
						return false;
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
			
			smsSendPop.smsPop();
		});
		
		// 상태변경 버튼
		$("#btnReqStatus").on('click', function (event) {
			returnLoan.updateStoreReqStatus("ST03");
		});
		
		// 상태변경 버튼 >> 대출대기
		$("#btnWaitStatus").on('click', function (event) {
			returnLoan.updateWaitReqStatus();
		});
		
		// 상태변경 버튼 >> 대출
		$("#btnLoanStatus").on('click', function (event) {
			returnLoan.updateLoanReqStatus();
		});
		
		// 엑셀다운
		$("#btnExcelDown").on('click', function (event) {
			returnLoan.excelDown();
		});
	},
	
	/**
	 * 조회
	 */
	search : function(pageNo) {
		
		//vlaidatiton
		if (!commFormValid('returnLoanInfoForm')) {
			return false;
		}
		
		if (!comm.date.compare($('#from_reqDate').val(), $('#to_reqDate').val(), 'yyyy-MM-dd')) {
			alert('신청시작일 보다 신청종료일은 작을수는 없습니다.');
			return false;
		}
		
		$("#start").val(pageNo);

		var form = new commSubmit('returnLoanInfoForm');
		form.setUrl( _ctx + "/store/returnLoanInfo");
		form.submit();
	},
	
	updateStoreReqStatus : function (type) {
		var isChk = true;
		var arRecKey    = new Array();
		var arReqStatus = new Array();
		
		var ischkLength = $("#returnLoanInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
		
		if (ischkLength == 0) {
			alert('선택된 데이터가 없습니다.');
			return false;
		}
		
		/*U01 : 신청중
		U02 : 신청취소
		S02 : 서점신청거절 (신청거절)
		S03 : 도서관확인요청 (신청중)
		S04 : 도서준비
		S05 : 대출대기
		S06 : 대출
		S07 : 반납
		S08 : 미대출취소
		L01 : 도서관승인 (도서준비)
		L02 : 도서관신청거절 (신청거절)*/
		
		var reqStatus = '';
		$("#returnLoanInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
			if ($(el).is(":checked")) {
				if ($(this).val() == 'S05') {
					reqStatus = 'S04';
					arRecKey.push($(el).parent().parent().find('input[name=recKey]').val());
				} else {
					alert('대출대기 자료의 경우만 도서준비로 변경가능합니다.');
					isChk = false;
					return false;
				}
			}
		});
		
		if (!isChk) {
			return false;
		}
		
		if (confirm('선택된 데이터를 도서준비로 상태변경 하시겠습니까?')) {
			var params = {
				ltRecKey  : arRecKey,
				reqStatus : reqStatus,
			};
			
			var option = {
				url   : _ctx + '/store/updateStoreLoanReqStatus',
				param : $.param(params, true)
			};
			 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					returnLoan.search(1);
				} else {
					alert(result.resultMessage);
				}
			});
		}
	},
	
	updateWaitReqStatus : function () {
		var isChk = true;
		var arRecKey    = new Array();
		var arReqStatus = new Array();
		
		var ischkLength = $("#returnLoanInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
		
		if (ischkLength == 0) {
			alert('선택된 데이터가 없습니다.');
			return false;
		}

		
		var reqStatus = '';
		$("#returnLoanInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
			if ($(el).is(":checked")) {
				if ($(this).val() == 'S06') {
					reqStatus = 'S05';
					arRecKey.push($(el).parent().parent().find('input[name=recKey]').val());
				} else {
					alert('대출 자료의 경우만 대출대기로 변경가능합니다.');
					isChk = false;
					return false;
				}
			}
		});
		
		if (!isChk) {
			return false;
		}
		
		if (confirm('선택된 데이터를 대출대기로 상태변경 하시겠습니까?')) {
			var params = {
				ltRecKey  : arRecKey,
				reqStatus : reqStatus,
			};
			
			var option = {
				url   : _ctx + '/store/updateWaitReqStatus',
				param : $.param(params, true)
			};
			 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					returnLoan.search(1);
				} else {
					alert(result.resultMessage);
				}
			});
		}
	},
	
	updateLoanReqStatus : function () {
		var isChk = true;
		var arRecKey    = new Array();
		var arReqStatus = new Array();
		
		var ischkLength = $("#returnLoanInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
		
		if (ischkLength == 0) {
			alert('선택된 데이터가 없습니다.');
			return false;
		}

		
		var reqStatus = '';
		$("#returnLoanInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
			if ($(el).is(":checked")) {
				if ($(this).val() == 'S07') {
					reqStatus = 'S06';
					arRecKey.push($(el).parent().parent().find('input[name=recKey]').val());
				} else {
					alert('반납 자료의 경우만 대출로 변경가능합니다.');
					isChk = false;
					return false;
				}
			}
		});
		
		if (!isChk) {
			return false;
		}
		
		if (confirm('선택된 데이터를 대출로 상태변경 하시겠습니까?')) {
			var params = {
				ltRecKey  : arRecKey,
				reqStatus : reqStatus,
			};
			
			var option = {
				url   : _ctx + '/store/updateLoanReqStatus',
				param : $.param(params, true)
			};
			 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					returnLoan.search(1);
				} else {
					alert(result.resultMessage);
				}
			});
		}
	},
	
	/**
	 * 대출 버튼 클릭
	 */
	storeLoan : function($this) {
		/* 카드비밀번호 사용하지 않음
		cardPasswdPop.open(
			$($this).parent().parent().find('input[name=recKey]').val(),
			$($this).parent().parent().find('input[name=userNo]').val()
		);
		*/
		loanPop.open(
				$($this).parent().parent().find('input[name=recKey]').val(),
				$($this).parent().parent().find('input[name=userNo]').val(),
				$($this).parent().parent().find('input[name=title]').val()
			);
	},
	
	/**
	 * 반납 버튼 클릭
	 */
	storeReturn : function($this) {
		
		if (confirm('반납 하시겠습니까?')) {
			
			var option = {
				url   : _ctx + '/store/updateStoreReturn',
				param : {
					recKey : $($this).parent().parent().find('input[name=recKey]').val()
				}
			};
			 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					if (result.appendixYn == "Y") {
						alert("부록이 존재하는 자료입니다.\n부록을 확인해주세요.");
					}
					returnLoan.search(1);
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
			var form = new commSubmit('returnLoanInfoForm');
			form.setUrl(_ctx + "/store/returnLoanInfoToExcel");
			comm.loading.hide();
			form.submit();
		}
	}
};

/**
 * SMS발송 레이어
 */
var smsSendPop = {
	init : function () {
		this.events();
	},

	events : function () {

		// 닫기 버튼
		$("#btnModalClose").on('click', function (event) {
			smsSendPop.smsPopClose();
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
				$('#selSms')   .attr('readonly', true).val('');
				$('#textArea') .attr('readonly', false).val('');
			} else {
				$('#selSms')   .attr('readonly', false);
				$('#textArea') .attr('readonly', true);
			}
		});
		
		// 발송 
		$('#btnSend').on('click', function(event) {
			smsSendPop.smsSend();
		});
	},
	
	smsPop : function() {
		
		$('#selSms')    .val('');
		$('#textArea')  .val('');
		$('#senderInfo').val('');
		
		this.senderInfo();
		
		$("#smsSendPop").show();
	},
	
	senderInfo : function() {
		
		var isChkLength   = $("#returnLoanInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
		
		var userNo = '';
		$("#returnLoanInfo > tbody > tr").each(function(i, el) {
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
		
		$("#returnLoanInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
			if ($(el).is(":checked")) {
				arRecKey.push($(el).parent().parent().find('input[name=recKey]').val());
			}
		});
		
		if ($.isEmpty($('#textArea').val())) {
			alert('메세지 내용을 입력하세요');
			$('#textArea').focus();
			return false;
		}
		
		if (confirm('SMS를 발송 하시겠습니까?')) {
			var params = {
				msgType  : $(":radio[name=formRadio]").val(),
				msg      : $('#textArea').val(),
				ltRecKey : arRecKey,
			};
			
			var uri = _ctx + '/store/storeReqSmsSend';
			
			var option = {
				url   : uri,
				param : $.param(params, true)
			};
			 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					returnLoan.search(1);
				} else {
					alert(result.resultMessage);
				}
			});
		}
	}
};

/**
 * 카드비밀번호 레이어
 */
var cardPasswdPop = {
	
	init : function () {
		this.events();
	},
	
	events : function () {
		
		// 카드비밀번호 엔터키 차단
		$("#cardPasswd").on('keydown', function(e) {
			var keyCode = e.keyCode || e.which;
			if (keyCode == 13) {
				return false;
			}
		});
		
		// 닫기 버튼
		$("#btnModalClose2").on('click', function (event) {
			cardPasswdPop.close();
		});
		
		// 확인 버튼
		$("#btnConfirm").on('click', function (event) {
			var cardPasswd = $("#cardPasswd").val();
			if ($.isEmpty(cardPasswd)) {
				alert("카드비밀번호를 입력하세요.");
				$("#cardPasswd").focus();
				return false;
			}
			
			cardPasswdPop.loan(
				$("#loanCardPasswdRecKey").val(),
				$("#loanCardPasswdUserNo").val(),
				cardPasswd,
				$("#appendix_yn").is(":checked") ? "Y" : "N"
			);
		});
	},
	
	/**
	 * 카드비밀번호 레이어 오픈
	 */
	open : function(recKey, userNo) {
		$("#loanCardPasswdRecKey").val(recKey);
		$("#loanCardPasswdUserNo").val(userNo);
		$("#loanCardPasswd").show();
		$("#cardPasswd").focus();
	},
	
	/**
	 * 카드비밀번호 레이어 닫기
	 */
	close : function() {
		$("#loanCardPasswdRecKey,#loanCardPasswdUserNo,#cardPasswd").val("");
		$("#loanCardPasswd").hide();
	},
	
	/**
	 * 대출
	 */
	loan : function(recKey, userNo, passWd, appendixYn) {
		
		if (confirm('희망도서 서비스를 신청하시겠습니까?')) {
			// 버튼 비활성화
			$("#btnConfirm").attr("disabled", true).css("opacity", 0.5).val("대출처리중");
			
			var option = {
				url   : _ctx + '/store/updateStoreLoan',
				param : {
					recKey     : recKey,
					userNo     : userNo,
					passWd     : passWd,
					appendixYn : appendixYn
				}
			};

			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					cardPasswdPop.close();
					returnLoan.search(1);
				} else {
					alert(result.resultMessage);
					$("#cardPasswd").val("").focus();
					// 버튼 활성화
					$("#btnConfirm").attr("disabled", false).css("opacity", 1).val("확인");
				}
			});
		}
	}
};

//대출 레이어 팝업
var loanPop = {
	init : function () {
		this.events();
	},
		
	events : function () {
		
		// 확인(대출)
		$("#btnConfirm2").on('click', function() {
			loanPop.loan();
		});
		
		// 닫기
		$("#btnModalClose3").on('click', function() {
			loanPop.close();
		});
	},
	
	open : function(recKey, userNo, title) {
		$("#loanRecKey").val(recKey);
		$("#loanUserNo").val(userNo);
		$("#loanTitle").val(title);
		$("#loan").show();
	},
	
	close : function() {
		document.loanForm.reset();
		$("#loan").hide();
	},
	
	loan : function() {
		if (confirm('희망도서 서비스를 신청하시겠습니까?')) {
			// 버튼 비활성화
			$("#btnConfirm2").attr("disabled", true).css("opacity", 0.5).val("대출처리중");
			
			var recKey = $("#loanRecKey").val();
			var userNo = $("#loanUserNo").val();
			var appendixYn = $("#appendix_yn").is(":checked") ? "Y" : "N";
			
			var option = {
				url   : _ctx + '/store/updateStoreLoan',
				param : {
					recKey     : recKey,
					userNo     : userNo,
					appendixYn : appendixYn
				}
			};

			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					loanPop.close();
					returnLoan.search(1);
				} else {
					alert(result.resultMessage);
					// 버튼 활성화
					$("#btnConfirm2").attr("disabled", false).css("opacity", 1).val("확인");
				}
			});
		}
	}
}

// onload 
$(document).ready(function() {
	// 신청승인
	returnLoan.init();
	// SMS발송 레이어
	smsSendPop.init();
	// 카드비밀번호 확인 레이어
	cardPasswdPop.init();
	// 서점 공통
	storecomm.init();
	// 대출 레이어
	loanPop.init();
});