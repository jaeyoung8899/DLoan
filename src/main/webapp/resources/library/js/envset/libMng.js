
/**
 * desc : 도서관관리
 * auth : shon
 * libMng.js
 */

var libMng = {
	
	init : function () {
		this.events();
	},
	
	events : function () {
		
		// 저장 버튼
		$("#btnSave").on('click', function (event) {
			libMng.updateLibrary();
		});
		
		// 레이어 닫기 버튼
		$(".btn_pop_close").on('click', function (event) {
			$("#passwdChange,#libIPChange").hide();
		});
		
		// 비밀번호 변경
		$("#btnChgPasswd").on('click', function (event) {
			libMng.passwordReset();
		});
		
		// 도서관IP 변경
		$("#btnChgIP").on('click', function (event) {
			libMng.changeLibIP();
		});
	},
	
	/**
	 * 조회
	 */
	search : function(pageNo) {
		var form = new commSubmit();
		form.setUrl( _ctx + "/lib/envset/libMng");
		form.submit();
	},
	
	
	updateLibrary : function() {
		
		var isChk = false;
		
		var ltLibId     = new Array();
		var ltLibPhone  = new Array();
		var ltHandphone = new Array();
		var ltName      = new Array();
		var ltLimitPrice= new Array();
		
		var libMngCode  = '';
		var libId       = '';
		var libPhone    = '';
		var handphone   = '';
		var name        = '';
		var limitPrice  = '';
		
		$("#libInfo > tbody > tr").each(function(i, el) {

			libMngCode = $(this).find('input[name="libManageCode"]').val();
			libId      = $(this).find('input[name="libId"]').val();
			libPhone   = $(this).find('input[name="libPhone"]').val();
			handphone  = $(this).find('input[name="handphone"]').val();
			name       = $(this).find('input[name="name"]').val();
			limitPrice = $(this).find('input[name="limitPrice"]').val();
			
			if (!libMng.check.isNumeric(libPhone) && !$.isEmpty(libMngCode)) {
				alert('숫자만 입력 해주세요.');
				$(this).find('input[name="libPhone"]').focus();
				isChk = true;
				return false;
			}
			if (!libMng.check.isNumeric(handphone) && !$.isEmpty(libMngCode)) {
				alert('숫자만 입력 해주세요.');
				$(this).find('input[name="handphone"]').focus();
				isChk = true;
				return false;
			}
			if (!libMng.check.isNumeric(limitPrice) && !$.isEmpty(libMngCode)) {
				alert('숫자만 입력 해주세요.');
				$(this).find('input[name="limitPrice"]').focus();
				isChk = true;
				return false;
			}
			
			if ($.isEmpty(name)) {
				alert('담당자를 입력해주세요.');
				$(this).find('input[name="handphone"]').focus();
				isChk = true;
				return false;
			}
			
			if ($.isEmpty(libMngCode)) {
				ltLibPhone  .push('');
				ltHandphone .push('');
				ltLimitPrice.push('');
			} else {
				ltLibPhone  .push($(this).find('input[name=libPhone]').val());
				ltHandphone .push($(this).find('input[name=handphone]').val());
				ltLimitPrice.push($(this).find('input[name=limitPrice]').val());
			}
			ltLibId.push($(this).find('input[name=libId]').val());
			ltName .push($(this).find('input[name=name]').val());
			
		});
		
		if (isChk) {
			return false;
		}
		
		if (confirm('저장 하시겠습니까?')) {

			var params = {
				ltLibId     : ltLibId,
				ltLibPhone  : ltLibPhone,
				ltHandphone : ltHandphone,
				ltName      : ltName,
				ltLimitPrice: ltLimitPrice
			};

			var option = {
				url      :  _ctx + '/lib/envset/updateLibrary',
				param    : $.param(params, true)
			};
			
			// 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					alert('저장 되었습니다.');
					libMng.search()
				} else {
					alert(result.resultMessage);
				}
			});
		}
	},
	
	/**
	 * 비밀번호변경 레이어 오픈
	 */
	popChgPasswd : function(libId) {
		
		$("#popLibId")    .val(libId);
		$("#chgPasswd")   .val('');
		$("#cfmPasswd")   .val('');
		$("#passwdChange").show();
		$("#chgPasswd")   .focus();
	},
	
	/**
	 * 
	 */
	failCountReset : function(libId) {
		if (confirm('비밀번호 오류횟수를 초기화 하시겠습니까?')) {

			var params = {
					libId  : libId,
			};
			
			var option = {
					url      :  _ctx + '/lib/envset/pwFailCountReset',
					param    : $.param(params, true)
			};
			
			// 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					alert('비밀번호 오류횟수가 초기화 되었습니다.');
					libMng.search()
				} else {
					alert(result.resultMessage);
					$('#chgPasswd').val('');
					$('#cfmPasswd').val('');
				}
			});
		}
	},
	
	/**
	 * 비밀번호 변경
	 */
	passwordReset : function() {
		
		if (!libMng.valid()) {
			return false;
		}
		
		if (confirm('비밀번호를 변경 하시겠습니까?')) {
			
			var params = {
					libId     : $('#popLibId').val(),
					changePw  : $('#chgPasswd').val(),
					confirmPw : $('#cfmPasswd').val(),
			};
			
			var option = {
					url      :  _ctx + '/lib/envset/passwordReset',
					param    : $.param(params, true)
			};
			
			// 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					alert('비밀번호가 변경 되었습니다.');
					$("#passwdChange").hide();
				} else {
					alert(result.resultMessage);
					$('#chgPasswd').val('');
					$('#cfmPasswd').val('');
				}
			});
		}
	},
	
	valid : function () {
		
		if ($.isEmpty($('#chgPasswd').val())) {
			alert("변경할 비밀번호를 입력하세요.");
			$('#chgPasswd').focus();
			return false;
		}
		if ($.isEmpty($('#cfmPasswd').val())) {
			alert("변경할 비밀번호를 입력하세요.");
			$('#cfmPasswd').focus();
			return false;
		}
		
		if ($('#cfmPasswd').val() != $('#chgPasswd').val()) {
			alert("비밀번호와 비밀번호확인이 일치하지 않습니다.");
			$('#cfmPasswd').focus();
			return false;
		}
		
		if (!comm.string.isValidPassword($('#chgPasswd').val())) {
			$('#chgPasswd').focus();
			return false;
		}
		
		return true;
	},
	
	check : {
		isCheckIP : function (sIP) {
			var regIP = /^(1|2)?\d?\d([.](1|2)?\d?\d){3}$/;

		    return regIP.test(sIP);
		},
		
		isNumeric : function (sNum) {
			var regNum =  /^[0-9]+$/;
			return regNum.test(sNum);
			
		}
	},
	
	/**
	 * 도서관IP변경 레이어 오픈
	 */
	popChgIP : function(libId) {
		
		$("#ipLibId").val(libId);
		
		var option = {
			url      :  _ctx + '/lib/envset/selectLibAllowIP',
			param    : $.param({
				libId : libId
			}, true)
		};
		
		$.commAjax (option, function (result) {
			
			$("#changeAllowIP input[type=text]").val("");
			for (var i = 0; i < result.length; i++) {
				$("#allowIp"+(i+1)).val(result[i]);
			}
			
			$("#libIPChange").show();
			$("#allowIp1").focus();
		});
	},
	
	changeLibIP : function() {
	
		var ltAllowIp = new Array();
		
		for (var i = 1; i <= 5; i++) {
			
			var allowIp = $("#allowIp"+i).val();
			if ($.isEmpty(allowIp)) {
				continue;
			}
			
			if (!libMng.check.isCheckIP(allowIp)) {
				alert('IP 형식이 아닙니다.');
				$("#allowIp"+i).focus();
				return false;
			}
			
			ltAllowIp.push(allowIp);
		}
		
		if (confirm('도서관IP를 변경 하시겠습니까?')) {
			
			var option = {
				url   :  _ctx + '/lib/envset/updateLibAllowIP',
				param : $.param({
					libId     : $('#ipLibId').val(),
					ltAllowIp : ltAllowIp
				}, true)
			};
			
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					alert('도서관IP가 변경 되었습니다.');
					$("#libIPChange").hide();
				} else {
					alert(result.resultMessage);
				}
			});
		}
	}
};

// onload 
$(document).ready(function() {
	libMng.init();
});