
/**
 * desc : 비밀번호 변경
 * auth : shon
 * storePasswordChange.js
 */

var storePasswordChange = {
	init : function () {
		this.events();
	},
	
	events : function () {
		
		// 다음에 변경
		$("#btnNext").on('click', function (event) {
			storePasswordChange.passwordChange('next');
		});
		
		// 비밀번호 변경
		$("#btnChange").on('click', function (event) {
			storePasswordChange.passwordChange();
		});
	},
		
	/**
	 * 비밀번호 변경
	 */
	passwordChange : function(flag) {
		
		if (flag == 'change') {
			if (!storePasswordChange.valid()) {
				return false;
			}
		}
		
		var params = {
			curPw     : $('#curPw').val(),
			changePw  : $('#changePw').val(),
			confirmPw : $('#confirmPw').val(),
			flag      : flag
		};
		
		var option = {
			url      :  _ctx + '/store/passwordChangeProc',
			param    : $.param(params, true)
		};
		
		// 
		$.commAjax (option, function (result) {
			if (result.resultCode == "Y") {
				if (flag == 'change') {
				alert('비밀번호가 변경 되었습니다.');
				}
				location.href = _ctx + "/store/storeRequestInfo";
			} else {
				alert(result.resultMessage);
				$('#changePw') .val('');
				$('#confirmPw').val('');
			}
		});
	},
	
	valid : function () {
		if ($.isEmpty($('#curPw').val())) {
			
			alert("현재 비밀번호를 입력하세요.");
			$('#curPw').focus();
			return false;
		}
		
		if ($.isEmpty($('#changePw').val())) {
			alert("변경할 비밀번호를 입력하세요.");
			$('#changePw').focus();
			return false;
		}
		
		if ($.isEmpty($('#confirmPw').val())) {
			alert("변경할 비밀번호를 입력하세요.");
			$('#confirmPw').focus();
			return false;
		}
		
		if ($('#confirmPw').val() != $('#changePw').val()) {
			alert("비밀번호와 비밀번호확인이 일치하지 않습니다.");
			$('#confirmPw').focus();
			return false;
		}
		
		if ($('#curPw').val() == $('#changePw').val()) {
			alert("현재비밀번호와 변경된 비밀번호가 동일합니다.");
			$('#confirmPw').focus();
			return false;
		}
		
		if (!comm.string.isValidPassword($('#confirmPw').val())) {
			$('#changePw').focus();
			return false;
		}
		
		return true;
	}
};


// onload 
$(document).ready(function() {
	storePasswordChange.init();
});