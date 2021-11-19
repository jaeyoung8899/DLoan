
/**
 * desc : 서점관리
 * auth : shon
 * storeMng.js
 */

var storeMng = {
	
	init : function () {
		this.events();
	},
	
	events : function () {
		
		// 저장 버튼
		$("#btnSave").on('click', function (event) {
			storeMng.updateStoreMng();
		});
		
		// 레이어 닫기 버튼
		$(".btn_pop_close").on('click', function (event) {
			$("#passwdChange,#storeMACChange,#phoneChange").hide();
		});
		
		// 비밀번호 변경
		$("#btnChgPasswd").on('click', function (event) {
			storeMng.passwordReset();
		});
		
		// 서점MAC 변경
		$("#btnChgMAC").on('click', function (event) {
			storeMng.changeStoreMAC();
		});

		//서점전화번호 변경
		$('#btnChgPhone').click(function (){
			storeMng.btnChgPhone();
		})
	},
	
	/**
	 * 조회
	 */
	search : function(pageNo) {
		var form = new commSubmit();
		form.setUrl( _ctx + "/lib/envset/storeMng");
		form.submit();
	},
	
	
	updateStoreMng : function() {
		
		var isChk = false;
		
		var ltStoreId    = new Array();
		var ltStorePhone = new Array();
		var ltHandphone  = new Array();
		var ltLimitPrice = new Array();
		
		var storeId    = '';
		var storePhone = '';
		var handphone  = '';
		var limitPrice = '';
		
		$("#storeInfo > tbody > tr").each(function(i, el) {

			storeId    = $(this).find('input[name="storeId"]').val();
			storePhone = $(this).find('input[name="storePhone"]').val();
			handphone  = $(this).find('input[name="handphone"]').val();
			limitPrice = $(this).find('input[name="limitPrice"]').val();
			
			if ($.isEmpty(storePhone)) {
				alert('전화번호를 입력해주세요.');
				$(this).find('input[name="storePhone"]').focus();
				isChk = true;
				return false;
			}
			if (!storeMng.check.isNumeric(storePhone)) {
				alert('전화번호를 숫자만 입력 해주세요.');
				$(this).find('input[name="storePhone"]').focus();
				isChk = true;
				return false;
			}
			if ($.isEmpty(handphone)) {
				alert('담당자SMS연락처를 입력해주세요.');
				$(this).find('input[name="handphone"]').focus();
				isChk = true;
				return false;
			}
			if (!storeMng.check.isNumeric(handphone)) {
				alert('담당자SMS연락처를 숫자만 입력 해주세요.');
				$(this).find('input[name="handphone"]').focus();
				isChk = true;
				return false;
			}
			if ($.isEmpty(limitPrice)) {
				alert('제한금액을 입력해주세요.');
				$(this).find('input[name="limitPrice"]').focus();
				isChk = true;
				return false;
			}
			if (!storeMng.check.isNumeric(limitPrice)) {
				alert('제한금액은 숫자만 입력 해주세요.');
				$(this).find('input[name="limitPrice"]').focus();
				isChk = true;
				return false;
			}

			ltStoreId   .push($(this).find('input[name=storeId]').val());
			ltStorePhone.push($(this).find('input[name=storePhone]').val());
			ltHandphone .push($(this).find('input[name=handphone]').val());
			ltLimitPrice.push($(this).find('input[name=limitPrice]').val());
		});
		
		if (isChk) {
			return false;
		}
		if (confirm('저장 하시겠습니까?')) {

			var params = {
				ltStoreId    : ltStoreId,
				ltStorePhone : ltStorePhone,
				ltHandphone  : ltHandphone,
				ltLimitPrice : ltLimitPrice
			};

			var option = {
				url      :  _ctx + '/lib/envset/updateStoreMng',
				param    : $.param(params, true)
			};
			
			// 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					alert('저장 되었습니다.');
					storeMng.search()
				} else {
					alert(result.resultMessage);
				}
			});
		}
	},
	
	/**
	 * 비밀번호변경 레이어 오픈
	 */
	popChgPasswd : function(storeId) {
		
		$("#popStoreId")  .val(storeId);
		$("#chgPasswd")   .val('');
		$("#cfmPasswd")   .val('');
		$("#passwdChange").show();
		$("#chgPasswd")   .focus();
	},
	
	
	
	/**
	 * 비밀번호 변경
	 */
	passwordReset : function() {
		
		if (!storeMng.valid()) {
			return false;
		}
		
		if (confirm('비밀번호를 변경 하시겠습니까?')) {
			
			var params = {
					storeId   : $('#popStoreId').val(),
					changePw  : $('#chgPasswd').val(),
					confirmPw : $('#cfmPasswd').val(),
			};
			
			var option = {
					url      :  _ctx + '/lib/envset/updateStorePasswordReset',
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
	
	/**
	 * 
	 */
	failCountReset : function(storeId) {
		if (confirm('비밀번호 오류횟수를 초기화 하시겠습니까?')) {

			var params = {
					storeId  : storeId,
			};
			
			var option = {
					url      :  _ctx + '/lib/envset/pwFailCountResetStore',
					param    : $.param(params, true)
			};
			
			// 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					alert('비밀번호 오류횟수가 초기화 되었습니다.');
					storeMng.search()
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
		
		isNumeric : function (sNum) {
			var regNum =  /^[0-9]+$/;
			return regNum.test(sNum);
		},
		isMac : function (mac) {
			var regex = /^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$/;
			return regex.test(mac.toUpperCase())
		}
	},
	
	/**
	 * 도서관IP변경 레이어 오픈
	 */
	popChgMAC : function(storeId) {
		
		$("#macStoreId").val(storeId);
		
		var option = {
			url      :  _ctx + '/lib/envset/selectStoreMAC',
			param    : $.param({
				storeId : storeId
			}, true)
		};
		
		$.commAjax (option, function (result) {
			
			console.log(result);
			
			$("#changeMAC input[type=text]").val("");
			for (var i = 0; i < result.length; i++) {
				console.log("#allowMAC"+(i+1)+"[0]" + ":" + result[i].substring(0,2));
				$("#allowMAC"+(i+1)+"1").val(result[i].substring( 0, 2));
				$("#allowMAC"+(i+1)+"2").val(result[i].substring( 2, 4));
				$("#allowMAC"+(i+1)+"3").val(result[i].substring( 4, 6));
				$("#allowMAC"+(i+1)+"4").val(result[i].substring( 6, 8));
				$("#allowMAC"+(i+1)+"5").val(result[i].substring( 8,10));
				$("#allowMAC"+(i+1)+"6").val(result[i].substring(10,12));
			}
			
			$("#storeMACChange").show();
			$("#allowMAC11").focus();
		});
	},
	
	changeStoreMAC : function() {
		
		var ltMac = new Array();
		
		for (var i = 1; i <= 5; i++) {
			
			var mac = new Array();
			for (var j = 1; j <= 6; j++) {
				mac.push($("#allowMAC"+i+""+j).val());
			}
			
			if ($.isEmpty(mac.join(""))) {
				continue;
			}
			
			if (!storeMng.check.isMac(mac.join("-"))) {
				alert('Mac Address형식이 아닙니다.');
				$("#allowMAC"+i+"1").focus();
				return false;
			}
			
			ltMac.push(mac.join("").toUpperCase());
		}
		
		if (confirm('서점MAC을 변경 하시겠습니까?')) {
			
			var option = {
				url   :  _ctx + '/lib/envset/updateStoreMAC',
				param : $.param({
					storeId    : $('#macStoreId').val(),
					ltStoreMac : ltMac
				}, true)
			};
			
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					alert('서점MAC이 변경 되었습니다.');
					$("#storeMACChange").hide();
				} else {
					alert(result.resultMessage);
				}
			});
		}
	},
	/**
	 * 서점 전화번호 레이어 오픈
	 */
	popChgPhone : function(storeId) {

		$("#phoneStoreId").val(storeId);


		var option = {
			url      :  _ctx + '/lib/envset/selectStorePhone',
			param    : $.param({
				storeId : storeId
			}, true)
		};

		$.commAjax (option, function (result) {

			$("#phone0").val("");
			$("#phone1").val("");
			$("#phone2").val("");
			for (var i = 0; i < result.length; i++) {
				$('#phone'+i).val(result[i])
			}

			$("#phoneChange").show();
		});
	},
	/**
	 * 서점전화번호 저장
	 */
	btnChgPhone : function () {

		var phoneList = [];

		for(var i = 0; i < 3 ; i++) {
			if($('#phone'+i).val() !== ''){
				phoneList.push($('#phone'+i).val());
			}
		}
		if(phoneList.length < 1) {
			alert('핸드폰번호를 하나 이상 입력해주세요.');
			return ;
		}

		if (confirm('서점 전화번호를 변경 하시겠습니까?')) {

			var option = {
				url   :  _ctx + '/lib/envset/updateStorePhone',
				param : $.param({
					storeId    : $('#phoneStoreId').val(),
					storePhoneList : phoneList
				}, true)
			};

			$.commAjax (option, function (result) {
				if (result.resultCode === "Y") {
					alert('서점 전화번호가 변경 되었습니다.');
					$("#phoneStoreId").hide();
				} else {
					alert(result.resultMessage);
				}
			});
		}
	}
};

// onload 
$(document).ready(function() {
	storeMng.init();
});