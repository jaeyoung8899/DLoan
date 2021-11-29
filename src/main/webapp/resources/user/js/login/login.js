
/**
 * desc : login
 * auth : shon
 * login.js
 */

var login = {
	init : function () {
		this.events();
	},
	
	events : function () {
		
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
	},
		
	/**
	 * 로그인
	 */
	login : function() {
		
		if (!login.valid()) {
			return false;
		}
		
		var params = {
			userId       : $('#inputId').val(),
			userPassword : $('#inputPassword').val(),
		};
		
		var option = {
			url      :  _ctx + '/loginProc',
			param    : $.param(params, true)
		};
		
		// 
		$.commAjax (option, function (result) {
			if (result.resultCode == "Y") {
				localStorage.setItem("viewOptionInfo",JSON.stringify(result.viewOptionInfo));
				var viewCode = comm.getViewOptionData_value('001','ALL');
				location.href = _ctx +$('#retUrl').val()+'?viewCode='+viewCode;
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
	}
};


// onload 
$(document).ready(function() {
	login.init();
});