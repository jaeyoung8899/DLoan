
/**
 * desc : 기타설정관리
 * auth : shon
 * confMng.js
 */

var confMng = {
	
	init : function () {
		this.events();
	},
	
	events : function () {
		// 저장 버튼
		$("#btnSave").on('click', function (event) {
			confMng.updateConfMng();
		});
	},
	
	/**
	 * 조회
	 */
	search : function() {
		var form = new commSubmit();
		form.setUrl( _ctx + "/lib/envset/confMng");
		form.submit();
	},
	
	/**
	 * 저장
	 */
	updateConfMng : function() {
		
		var isChk       = false;
		var ltConfId    = new Array();
		var ltConfValue = new Array();
		
		$("#confInfo > tbody > tr").each(function(i, el) {

			var confId    = $(this).find('input[name="confId"]'   ).val();
			var confDesc  = $(this).find('input[name="confDesc"]' ).val();
			var confValue = $(this).find('[name="confValue"]').val();
			
			if ($.isEmpty(confValue)) {
				alert(confDesc + ' 설정값을 입력해주세요.');
				$(this).find('input[name="confValue"]').focus();
				isChk = true;
				return false;
			}
			
			if (confId == "MONTH") {
				// 희망도서 서비스 월 신청권수
				if (!confMng.check.isNumeric(confValue)) {
					alert(confDesc + ' 설정값을 숫자만 입력 해주세요.');
					$(this).find('input[name="confValue"]').focus();
					isChk = true;
					return false;
				}
			}
			
			ltConfId   .push(confId);
			ltConfValue.push(confValue);
		});
		
		if (isChk) {
			return false;
		}
		
		if (confirm('저장 하시겠습니까?')) {

			var params = {
				ltConfId    : ltConfId,
				ltConfValue : ltConfValue
			};

			var option = {
				url   : _ctx + '/lib/envset/updateConfMng',
				param : $.param(params, true)
			};
			
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					alert('저장 되었습니다.');
					confMng.search()
				} else {
					alert(result.resultMessage);
				}
			});
		}
	},
	
	check : {
		isNumeric : function (sNum) {
			var regNum =  /^[0-9]+$/;
			return regNum.test(sNum);
		}
	}
};

// onload 
$(document).ready(function() {
	confMng.init();
});