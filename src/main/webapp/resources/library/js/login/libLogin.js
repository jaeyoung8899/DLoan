
/**
 * desc : login
 * auth : shon
 * login.js
 */

var libLogin = {
	init : function () {
		this.events();
	},
	
	events : function () {
		
	    var userInputId = libLogin.getCookie("userInputId");
	    var setIdCookieYN = libLogin.getCookie("setIdCookieYN");
	    var userInputPw = libLogin.getCookie("userInputPw");
	    var setPwCookieYN = libLogin.getCookie("setPwCookieYN");

		if(setIdCookieYN == 'Y') {
	        $("#inputId").val(userInputId);
	        $("#save_id").prop("checked", true);
	    } else {
			$("#inputId").val('');
	        $("#save_id").prop("checked", false);
	    }

		if(setPwCookieYN == 'Y') {
	        $("#inputPassword").val(userInputPw);
	        $("#save_pw").prop("checked", true);
	    } else {
			$("#inputPassword").val('');
	        $("#save_pw").prop("checked", false);
	    }
		
		$('#inputId').on('keydown', function(e) {
			var keyCode = e.keyCode || e.which;
			if (keyCode == 13) {
				
				if ($.isEmpty($('#inputPassword').val())) {
					$('#inputPassword').focus()
				} else {
					libLogin.login();
				}
				
			}
		});
		
		$('#inputPassword').on('keydown', function(e) {
			var keyCode = e.keyCode || e.which;
			if (keyCode == 13) {
				libLogin.login();
			}
		});
		// 로그인 버튼
		$("#btnSubmit").on('click', function (event) {
			libLogin.login();
		});
	},
		
	/**
	 * 로그인
	 */
	login : function() {
		
		if (!libLogin.valid()) {
			return false;
		}
		
		
		
		var params = {
			libId       : $('#inputId').val(),
			libPassword : $('#inputPassword').val(),
		};
		
		var option = {
			url      :  _ctx + '/lib/loginProc',
			param    : $.param(params, true)
		};
		
		// 
		$.commAjax (option, function (result) {
			if (result.resultCode == "Y") {
				
				if($('#save_id').is(':checked')) {
			        libLogin.setCookie("userInputId",  $('#inputId').val(), 60); 
			        libLogin.setCookie("setIdCookieYN", "Y", 60);
				}else{
					libLogin.deleteCookie("userInputId");
            		libLogin.deleteCookie("setIdCookieYN");
				}
				
				if($('#save_pw').is(':checked')) {
			        libLogin.setCookie("userInputPw",  $('#inputPassword').val(), 60); 
			        libLogin.setCookie("setPwCookieYN", "Y", 60);
				}else{
					libLogin.deleteCookie("userInputPw");
            		libLogin.deleteCookie("setPwCookieYN");
				}
				
				if (result.isChangePw) {
					location.href = _ctx + "/lib/libPasswordChange?isNext=true";
				} else {
					location.href = _ctx + "/lib/libRequestInfo";
				}
			} else {
				
				alert(result.resultMessage);
				$('#inputId')      .val('');
				$('#inputPassword').val('');
				$('#inputId')      .focus();
			}
		});
	},
	
	valid : function () {
		if ($.isEmpty($('#inputId').val())) {
			
			alert("아이디를 입력하세요.");
			$('#inputId').focus();
			return false;
		}
		
		if ($.isEmpty($('#inputPassword').val())) {
			alert("비밀번호를 입력하세요.");
			$('#inputPassword').focus();
			return false;
		}
		
		return true;
	},
	
	//쿠키값 set
	setCookie : function(cookieName, value, exdays) {
		var exdate = new Date();
		exdate.setDate(exdate.getDate() + exdays);
		var cookieValue = escape(value) + ((exdate == null) ? '' : '; expires=' + exdate.toGMTString());
		document.cookie  = cookieName + '=' + cookieValue;
	},
	
	//쿠키값 삭제
	deleteCookie : function(cookieName) {
		var expireDate = new Date();
		expireDate.setDate(expireDate.getDate() - 1);
		document.cookie = cookieName + '= ' + '; expires=' + expireDate.toGMTString();		
	},
	
	getCookie : function(cookieName) {
		var x, y;
		var val = document.cookie.split(';');
		
		for(var i = 0; i < val.length; i++) {
			x = val[i].substr(0, val[i].indexOf('='));
			y = val[i].substr(val[i].indexOf('=')+1);
			x = x.replace(/^\s+|\s+$/g, ''); // 앞과 뒤의 공백 제거하기
			
			if(x == cookieName) {
				return unescape(y);
			}
		}
	}
	
};


// onload 
$(document).ready(function() {
	libLogin.init();
});