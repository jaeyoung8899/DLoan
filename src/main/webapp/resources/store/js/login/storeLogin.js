
/**
 * desc : 서점 로그인
 * auth : shon
 * login.js
 */

var login = {
	init : function () {
		this.events();
	},
	
	events : function () {


		var userInputId = login.getCookie("userInputId");
	    var setIdCookieYN = login.getCookie("setIdCookieYN");
	    var userInputPw = login.getCookie("userInputPw");
	    var setPwCookieYN = login.getCookie("setPwCookieYN");

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
					login.login();
				}
				
			}
		});
		
		$('#inputPassword').on('keydown', function(e) {
			var keyCode = e.keyCode || e.which;
			if (keyCode == 13) {
				login.login();
			}
		});
		
		// 로그인 버튼
		$("#btnSubmit").on('click', function (event) {
			login.login();
		});

		//스토리지저장
		login.getViewInfo();

	},
		
	/**
	 * 로그인
	 */
	login : function() {
		
		if (!login.valid()) {
			return false;
		}

		//맥주소 체크
		var macUseYn = comm.getViewOptionData_value('003','ALL');
		if(macUseYn !=='undefined' && macUseYn !== undefined && macUseYn !== '' && macUseYn === 'Y') {
			login.getMac(login.loginProc);
		}else{
			login.loginProc();
		}

	},
	
	loginProc : function(mac) {
		var returnYn = comm.getViewOptionData_value('004','ALL');
		
		var option = {
			url      :  _ctx + '/store/loginProc',
			param    : $.param({
				storeId       : $('#inputId').val(),
				storePassword : $('#inputPassword').val(),
				storeMac      : mac,
				storeYn       : returnYn
			}, true),
			isLdBar  : false
		};
		
		$.commAjax (option, function (result) {
			if (result.resultCode == "Y") {
				
				if($('#save_id').is(':checked')) {
			        login.setCookie("userInputId",  $('#inputId').val(), 60); 
			        login.setCookie("setIdCookieYN", "Y", 60);
				}else{
					login.deleteCookie("userInputId");
            		login.deleteCookie("setIdCookieYN");
				}
				
				if($('#save_pw').is(':checked')) {
			        login.setCookie("userInputPw",  $('#inputPassword').val(), 60); 
			        login.setCookie("setPwCookieYN", "Y", 60);
				}else{
					login.deleteCookie("userInputPw");
            		login.deleteCookie("setPwCookieYN");
				}

				if (result.isChangePw) {
					location.href = _ctx + "/store/storePasswordChange?isNext=true";
				} else {
					location.href = _ctx + "/store/storeRequestInfo";
				}
			} else {
				alert(result.resultMessage);
				$('#inputId')      .val('');
				$('#inputPassword').val('');
				$('#inputId')      .focus();
				comm.loading.hide();
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
	
	getMac : function(callbackFunc) {
		comm.loading.show();
		$.ajax({
			crossOrigin: true,
			type       : "GET",
			url        : "http://localhost:20201",
			datatype   : "json",
			data       : {"printdata":"call"},
			success    : function(data) {
				callbackFunc(data.mac);
			},
			error      : function(data) {
				alert("PC정보를 확인하는데 실패하였습니다.");
				comm.loading.hide();
			}
		});
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
	},
	getViewInfo : function () {
		$.ajax({
			crossOrigin: true,
			type       : 'GET',
			url        : _ctx + '/common/getConfig',
			datatype   : 'json',
			success    : function(data) {
				localStorage.setItem("viewOptionInfo",JSON.stringify(data));
			},
			error      : function() {
				alert('데이터가져오기실패');
			}
		});
	}
	
};

// onload 
$(document).ready(function() {
	login.init();
});