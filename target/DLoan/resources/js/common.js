/**
 * comm global function
 */
var comm = {
	
	// date Util
	date : {
		
		/**
		 * 날짜
		 */
		isDate : function(sDate, sFormat) {
			sFormat = sFormat || "yyyyMMdd";
			try {
				if (sDate === comm.date.formatDateToStr(comm.date.formatStrToDate(sDate, sFormat), sFormat)) {
					return true;
				}
			} catch (e) {
			}
			return false;
		},
		
		addDate : function(sDate, nOffset, sFormat) {
			sFormat = sFormat || "yyyyMMdd";
			var oDate = comm.date.formatStrToDate(sDate, sFormat);
			
			if (oDate== null) {
				return null;
			}
			oDate.setDate(oDate.getDate() + nOffset);
			return comm.date.formatDateToStr(oDate, sFormat);
		},
		
		addMonth :function(sDate, nOffset, sFormat) {
			sFormat = sFormat || "yyyyMMdd";
			
			var oDate = comm.date.formatStrToDate(sDate, sFormat);
			
			if (oDate== null) {
				return null;
			}
			oDate.setMonth(oDate.getMonth() + nOffset);
			return comm.date.formatDateToStr(oDate, sFormat);
		},
		
		addYear :function(sDate, nOffset, sFormat) {
			sFormat = sFormat || "yyyyMMdd";
			
			var oDate = comm.date.formatStrToDate(sDate, sFormat);
			
			if (oDate== null) {
				return null;
			}
			oDate.setFullYear(oDate.getFullYear() + nOffset);
			
			return comm.date.formatDateToStr(oDate, sFormat);
		},
		
		
		formatDateToStr : function (oDate, sFormat) {
			sFormat = sFormat || "yyyyMMdd";
			return sFormat.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
				switch ($1) {
					case "yyyy": return comm.string.lpad(String(oDate.getFullYear()), 4, '0');
					case "yy":   return comm.string.lpad(String(oDate.getFullYear() % 100), 2, '0');
					case "MM":   return comm.string.lpad(String(oDate.getMonth() + 1), 2, '0');
					case "dd":   return comm.string.lpad(String(oDate.getDate()), 2, '0');
					case "HH":   return comm.string.lpad(String(oDate.getHours()), 2, '0');
					case "hh":   return comm.string.lpad(String((oDate.getHours() % 12) ? (oDate.getHours() % 12) : 12), 2, '0');
					case "mm":   return comm.string.lpad(String(oDate.getMinutes()), 2, '0');
					case "ss":   return comm.string.lpad(String(oDate.getSeconds()), 2, '0');
					default: return $1;
				}
			});
		},
		
		formatStrToDate : function (sDate, sFormat) {
			sFormat = sFormat || "yyyyMMdd";
			var retDate = new Date(1900, 0, 1, 0, 0, 0);
			var sTransDate = sFormat.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1, $2, nIdx) {
				var sValue = sDate.substr(nIdx, $1.length);
				var nValue = Number(sValue);
				switch ($1) {
					case "yyyy": retDate.setFullYear(nValue); break;
					case "MM":   retDate.setMonth(nValue -1); break;
					case "dd":   retDate.setDate(nValue);     break;
					case "HH":   retDate.setHours(nValue);    break;
					case "mm":   retDate.setMinutes(nValue);  break;
					case "ss":   retDate.setSeconds(nValue);  break;
					default : return $1;
				}
				return sValue;
			});
			
			if (sDate === sTransDate && sDate === comm.date.formatDateToStr(retDate, sFormat)) {
				return retDate;
			}
			return null;
		},
		
		compare : function(startDate, endDate, format) {
			
			if ($.isEmpty(startDate) || $.isEmpty(endDate)) {
				return true;
			}
			
			if (comm.date.formatStrToDate(startDate, format) > comm.date.formatStrToDate(endDate, format)) {
				return false;
			}
			return true;
		}
	},
	
	string : {
		lpad : function(s, n, c) {    
			if (! s || ! c || s.length >= n) {
				return s;
			}
			var max = (n - s.length)/c.length;
			for (var i = 0; i < max; i++) {
				s = c + s;
			}
			return s;
		},
		 
		// 
		rpad : function(s, n, c) {  
			if (! s || ! c || s.length >= n) {
				return s;
			}
			var max = (n - s.length)/c.length;
			for (var i = 0; i < max; i++) {
				s += c;
			}
			return s;
		},
		
		// email check
		isValidEmail : function (email) {
			
			var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
			
			if (email.length == 0) {
				return false;
			}
			
			return re.test(email);
		},
		
		isValidPassword : function(reg_pwd) {
			var pw = reg_pwd;
			var num = pw.search(/[0-9]/g);
			var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);
			var lowercase = pw.search(/[a-z]/g);
			var uppercase = pw.search(/[A-Z]/g);

			// 1. 길이 체크
			if(pw.length < 8 || pw.length > 20){
				alert("비밀번호는 8 ~ 20자리 이내로 입력해주세요.");
				return false;
			}
			// 2. 공백여부
			else if(pw.search(/₩s/) != -1){
				alert("비밀번호는 공백없이 입력해주세요.");
				return false;
			} 
			// 3. 숫자 + 특수문자 + 영어 소문자로 구성
			else if(num < 0 || spe < 0 || lowercase < 0 || uppercase >= 0){
				alert("비밀번호는 영어 소문자, 숫자, 특수문자를 혼합하여 입력해주세요.");
				return false;
			}

			return true;
			/*
			if (!/^(?=.*[a-z])(?=.*[~!@#$%^*+=-])(?=.*[0-9]).{8,20}$/.test(reg_pwd)) {
				alert("비밀번호를 영어 소문자, 숫자, 특수문자를 조합하여 8~20자 이내로 입력하세요.");
				return false;
			} else {
				return true;
			}
			*/
		},
		
		comma : function(x) {
			return String(x).replace(/\B(?=(?:\d{3})+(?!\d))/g, ",");
		},
		
		isNull : function (str) {
			if (str == null) {
				return true;
			} 
			if (str == "NaN") {
				return true;
			}
			if (new String(str).valueOf() == "undefined") {
				return true;
			}
			var chkStr = new String(str);

			if (chkStr.valueOf() == "undefined" ) {
				return true;
			}
			if (chkStr == null) {
				return true;   
			}
			if (chkStr.toString().length == 0 ) {
				return true;  
			}
			return false;
		},
	},
	
	
	/**
	 * 공통 로딩바 처리
	 */
	loading : {
		show : function () {
			HoldOn.open({theme : "sk-fading-circle", backgroundColor : "#FFF"});
		},
		
		hide : function () {
			HoldOn.close();
		}
	},
	
	sort : {
		initSort : function(tableId) {
			var mask = "";
			
			if ($('#order').val() == 'ASC') {
				mask = "▲";
			} else if ($('#order').val() == 'DESC') {
				mask = "▼";
			}
				
			$('#'+tableId+' > thead > tr > th > a').each(function(i, el) {
				if ($(el).data('sort') == $('#sortCol').val()) {
					$(el).html($(el).html() + " " + mask);
					return false;
				}
			});
		},
		
		sortOrder : function($this, searchFunc) {
			var mask = '';
			var sortCol = $($this).data('sort');
		
			if (sortCol == $('#sortCol').val()) {
				
				if (!$.isEmpty($('#order').val())) {
					if ($('#order').val() == 'ASC') {
						mask = "▼";
					} else {
						mask = "▲";
					}
				} else {
					// default
					mask = "▲";
				}
			} else {
				// default
				mask = "▲";
			}
			
			if (mask == "▲") {
				$('#order').val("ASC")
			} else {
				$('#order').val("DESC")
			}
			
			$('#sortCol').val(sortCol);
			
			// header 표시
			var colNm = $($this).html();
			
			colNm = colNm.replace(" ▲", "").replace(" ▼", "");
			$($this).html(colNm + " " + mask);
			
			// 조회 
			
			if (typeof searchFunc === 'function') {
				searchFunc(1);
			}
		},
	},
	
	alert : function(message) {
		if(message != undefined && message != "") {
			alert(message);
		}
	},
	
	help : function() {
		console.log('1. 바로대출 상태값 보기 : comm.status()');
	},
	
	status : function() {
		console.log('U01 : 신청중\nU02 : 신청취소\nS01 : 서점승인 (도서준비중)\nS02 : 서점신청거절 (신청거절)\nS03 : 도서관확인요청 (신청중)\nS04 : 도서준비중\nS05 : 대출대기중\nS06 : 대출\n'
				+ 'S07 : 반납\nS08 : 미대출취소\nL01 : 도서관승인 (도서준비중)\nL02 : 도서관신청거절 (신청거절)');
	},

	getViewOptionData : function(classCode, manageCode,storeId) {

		var viewOptionInfo = localStorage.getItem('viewOptionInfo');
		if(viewOptionInfo === null || viewOptionInfo === undefined || viewOptionInfo === 'undefined' || viewOptionInfo === '')
		{
			return "";
		}
		else
		{
			var jsonData_viewOptionInfo = JSON.parse(viewOptionInfo);
			console.log(jsonData_viewOptionInfo)
			var result = $.grep(jsonData_viewOptionInfo, function(e)
			{
				return (e.CLASS_CODE == classCode) && (e.MANAGE_CODE == manageCode ||  e.MANAGE_CODE == 'ALL');
			});

			console.log(result)

			if ( result == null || result.length <= 0 )
				return "";
			else
			{


				if(result.length > 1)
				{
					var return_array = [];
					// 자관 정보가 있는경우 자관꺼만 전달
					for(var index in result)
					{
						var temp_one = result[index];
						if(temp_one.MANAGE_CODE === manageCode)
						{
							return_array.push(temp_one);

						}
					}

					// 여기까지 값이 없이 왔다면 그냥 모두 전달
					if(return_array.length == 1)
					{
						return return_array;
					}
					else
					{
						//중복된 데이터가 있는 경우도 있어서 배열로  return
						return result;
					}
				}
				else
				{
					//중복된 데이터가 있는 경우도 있어서 배열로  return
					return result;
				}


			}
		}
	},
	// ViewOptionInfo의 정보를 추출한다.
	getViewOptionData_value: function (classCode, manageCode,storeId){

		var getValue = comm.getViewOptionData(classCode, manageCode);
		var return_data = "";

		if(getValue.length > 0)
		{
			return_data = getValue[0].VALUE;
		}
		else
		{
			return_data = '';
		}

		return return_data;
	},

	//conf_tbl 다시조회
	setConfTbl : function () {
		var option = {
			url   : _ctx + '/common/getConfig'
		};

		$.commAjax (option, function (result) {
			console.log('설정 재조회')
		});
	}

};


Date.prototype.format = function(f) {
	if (!this.valueOf()) return " ";

	var weekName = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
	var d = this;

	return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
		switch ($1) {
			case "yyyy": return d.getFullYear();
			case "yy":   return (d.getFullYear() % 1000).zf(2);
			case "MM":   return (d.getMonth() + 1).zf(2);
			case "dd":   return d.getDate().zf(2);
			case "E":    return weekName[d.getDay()];
			case "HH":   return d.getHours().zf(2);
			case "hh":   return ((h = d.getHours() % 12) ? h : 12).zf(2);
			case "mm":   return d.getMinutes().zf(2);
			case "ss":   return d.getSeconds().zf(2);
			case "a/p":  return d.getHours() < 12 ? '오전' : '오후';
			default: return $1;
		}
	});
};
 
String.prototype.string = function(len) {var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf     = function(len) {return "0".string(len - this.length) + this;};
Number.prototype.zf     = function(len) {return this.toString().zf(len);};

 
// jquery handle
(function ($) {
	
	/**
	 * var option = {
	 *    url     : _ctx + '/sample/sample.do', -- 필수
	 *    param   : {},    -- option default {},
	 *    type    : '',    -- option default 'POST',
	 *    isAsync : false, -- option default true,
	 *    isLdBar : false, -- option default true,
	 *    dataType : 'json' ,
	 * };
	 * 
	 * $.commAjax (option, function(result) {
	 *     callBack;
	 * });
	 */
	$.commAjax = function (option, sucFunc) {
		
		if (option.isLdBar == undefined || option.isLdBar == true) {
			comm.loading.show();
		}
		
		var _params = {};
		if (option.param != undefined && option.param != '') {
			_params = option.param;
		}
		
		var ajsxOpt = {
				type        : option.type || 'POST',
				url         : option.url,
				data        : _params,
				async       : option.isAsync == false ? false : true,
				success     : sucFunc,
				error       : $.commAjaxError,
				complete : function () {
					comm.loading.hide();
				}	
		};
		
		// body에 쓸때는 데이타 및 type 변환
		if (option.dataType != undefined && option.dataType != '') {
			ajsxOpt.data        = JSON.stringify(_params);
			ajsxOpt.dataType    = 'json';
			ajsxOpt.contentType = "application/json; charset=UTF-8";
		}
		
		$.ajax(ajsxOpt);
	},
	
	/**
	 * Ajax 에러처리
	 */
	$.commAjaxError = function(jqxhr, status, error) {
		comm.loading.hide();
		if (jqxhr.status == 403) {
			if (_path == "user") {
				// 변경 : 로그인 페이지가 아니고 신청페이지로 이동 (신청페이지는 세션이 없어도 보여지도록 변경)
				//document.location.href = _ctx + "/login";
				document.location.href = _ctx + "/request";
			} else {
				document.location.href = _ctx + "/" + _path + "/login";
			}
		} else {
			document.location.href = _ctx + "/common/error?path=" + _path;
		}
	},
	
	/**
	 * $.isEmpty(str)
	 */
	$.isEmpty = function (str) {
		if (str == null || str == undefined || str.length == 0) {
			return true;
		} else {
			return false;
		}
	};
	
	/**
	 * $.isTrimEmpty()
	 */
	$.isTrimEmpty = function(str) {
		if ($.isEmpty(str)) {
			return true;
		} else {
			if ($.trim(str).length == 0) {
				return true;
			} else {
				return false;
			}
		}
	};

	$.fn.extend({
		/**
		 * form 에 있는 값들을 Json 형식 변환
		 *  $('form name OR form Id').serializeObject();
		 */
		serializeObject : function() {
			var obj = null;
			try {
				if (this[0].tagName && this[0].tagName.toUpperCase() == "FORM") {
					var arr = this.serializeArray();
					if (arr) {
						obj = {};
						$.each(arr, function() {
							obj[this.name] = this.value;
						});
					}
				}
			} catch (e) {
				console.log(e.message);
			}
			return obj;
		},
	});
	
	$.datepicker.regional['ko'] = {
		closeText         : '닫기',
		currentText       : '오늘',
		prevText          : '이전 달',
		nextText          : '다음 달',
//		monthNames        : ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		monthNamesShort   : ['1','2','3','4','5','6','7','8','9','10','11','12'],
		dayNames          : ['일','월','화','수','목','금','토'],
//		dayNamesShort     : ['일','월','화','수','목','금','토'],
		dayNamesMin       : ['일','월','화','수','목','금','토'],
		weekHeader        : 'Wk',
		dateFormat        : 'yy-mm-dd',
		showMonthAfterYear: true,
		changeMonth       : true,
	    changeYear        : true,
	    yearRange         : 'c-99:c+99',
	    /*showOn            : "both",
	    buttonImage       : _ctx+"/resources/store/images/icon-calendar.png",
	    buttonImageOnly   : false,*/
	    showButtonPanel   : true,
	    beforeShow        : function(args0) { addDatepickerCustomButton(args0); },
	    onChangeMonthYear : function(args0, args1, args2) { addDatepickerCustomButton(args2.input[0]); }
	};
	
	function addDatepickerCustomButton(input) {
		setTimeout(function () {
    		var buttonPane = $(input).datepicker("widget").find(".ui-datepicker-buttonpane");
            var btnElement = $('<button class="ui-datepicker-current ui-state-default ui-priority-secondary ui-corner-all" type="button">지우기</button>');
            btnElement.unbind("click").bind("click", function () {
            	$.datepicker._clearDate(input);
                $(input).datepicker("hide");
            });
            btnElement.appendTo(buttonPane);
        }, 1);
	}
	
	/*
	if (_path == "lib" || _path == "store") {
		$.datepicker.regional['ko'].showOn = "both";
		$.datepicker.regional['ko'].buttonImage = _ctx+"/resources/store/images/icon-calendar.png";
		$.datepicker.regional['ko'].buttonImageOnly = false;
	}
	*/
	
	$.datepicker.setDefaults($.datepicker.regional['ko']);
	$.datepicker._gotoToday = function(id) { 
	    $(id).datepicker('setDate', new Date()).datepicker('hide').blur(); 
	};
	
})(jQuery);


/**
 * form submit을 위한 
 * param objForm 필수 아님. 
 * ex) var form = new commSubmit('formId'); 
 *     form.setUrl('url/url'); 필수
 *     form.addParam("currentPageNo", 1);
 *     form.submit();
 */
var commSubmit = function (objForm) {
	
	comm.loading.show();
	
	this.formId = comm.string.isNull(objForm) == true ? "_commonForm_" : objForm;
	this.url = "";

	if (this.formId == "_commonForm_") {
		
		if ($('form[name=_commonForm_]').length != 0) {
			$('form[name=_commonForm_]').remove();
		}
		
		$("body").append("<form name='_commonForm_' id='_commonForm_'></form>");
	}

	// url
	this.setUrl  = function setUrl(url) {
		this.url = url;
	};

	// form에 hidden 생성.
	this.addParam = function addParam(key, value) {
		$("#"+this.formId).append($("<input type='hidden' name='"+key+"' id='"+key+"' value='"+value+"' >"));
	};

	// 기본 post로 넘김.
	this.submit = function submit() {
		console.log($("#"+this.formId))
		console.log($("#"+this.formId)[0])
		var sForm    = $("#"+this.formId)[0];
		sForm.action = this.url;
		sForm.method = "POST";
		sForm.submit();
	};

	//submit에서는 hide 시점을 찾을 수 없음
	//comm.loading.hide();
};

/**
 * form validate
 */
var commFormValid = function (objForm) {

	var isChk = true;
	var formId = comm.string.isNull(objForm) == true ? "" : objForm;
	
	var messages = {
		required     : '$$을(를) 입력하세요',
		required2    : '$$을(를) 선택하세요',
		alpha        : '$$은(는) 영문만 입력가능합니다.',
		alphaNumeric : '$$은(는) 숫자/영문만 입력가능합니다.',
		numeric      : '$$은(는) 정수형 숫자만 입력가능합니다.',
		email        : '$$은(는) 이메일 형식이 아닙니다.',
		integer      : '$$은(는) 숫자만 입력가능합니다.',
		decimal      : '$$은(는) 숫자(소숫점 포함) 형식이 아닙니다.',
		date         : '$$은(는) 날짜 형식이 아닙니다.'
	};
	
	var	regex = {
		alphaRegex        : /^[a-z]+$/i,
		alphaNumericRegex : /^[a-z0-9]+$/i,
		numericRegex      : /^[0-9]+$/,
		emailRegex        : /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/,
		integerRegex      : /^\-?[0-9]+$/,
		decimalRegex      : /^\-?[0-9]*\.?[0-9]+$/,
		dateRegex         : /([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]))/,
	};
	
	var ruleCheck = {
		required     : function (field) {
			
			if ((field.type === 'checkbox') || (field.type === 'radio')) {
				
				var element = document.getElementsByName(field.name);
				for (var i = 0; i < element.length; i++) {
					if (element[i].checked) {
						return true;
					}
				}
				return false;
			}
			return (field.value !== null && field.value !== '');
		},
		required2    : function (field) {
			return (field.value !== null && field.value !== '');
		},
		alpha        : function (field) {
			return regex.alphaRegex.test(field.value);
		},
		alphaNumeric : function (field) {
			return regex.alphaNumericRegex.test(field.value);
		}, 
		numeric      : function (field) {
			return regex.numericRegex.test(field.value);
		}, 
		email        : function (field) {
			return regex.emailRegex.test(field.value);
		}, 
		integer      : function (field) {
			return regex.integerRegex.test(field.value);
		},
		decimal      : function (field) {
			return regex.decimalRegex.test(field.value);
		},
		date         : function (field) {
			return regex.dateRegex.test(field.value);
		},
	};

	
	$($('#'+formId).prop('elements')).each(function() {
		
		if (this.type == 'button' || this.type == '') {
			return true;
		}
		
		var dummyRule = $(this).attr('rules');
		
		if (dummyRule !== undefined) {
			
			var rules   = dummyRule.split('|');
			var isEmpty = (!this.value || this.value === '' || this.value === undefined);
			
			//if (!isEmpty) {
			for (var i = 0; i < rules.length; i++) {
				var rule = rules[i];
				
				if (rule != 'required' && rule != 'required2') {
					if (isEmpty) {
						continue;
					}
				}
				
				if (typeof ruleCheck[rule] === 'function') {
					
					if (!ruleCheck[rule].apply(this, [this])) {
						var title = '';
						if (this.title === undefined) {
							title = this.name;
						} else {
							title = this.title;
						}
						
						var msg = messages[rule].replace('$$', title);
						isChk = false;
						alert(msg);
						this.focus();
						return false;
					}
				}
			}
		}
	});
	
	return isChk;
};

/**
 * 도서관 공통
 */
var libcomm = {
	LOGIN_CHECK_TIME : 5000,
		
	/**
	 * 로그인확인
	 */
	loginCheck : function() {
		$.commAjax({
			url     : _ctx + '/lib/loginCheck',
			isLdBar : false
		}, function (result) {
			if (result.resultCode == "Y") {
				setTimeout(libcomm.loginCheck, libcomm.LOGIN_CHECK_TIME);
			} else {
				document.location.href = _ctx + "/lib/logout";
			}
		});
	},
	headerCheck : function () {
		var headerCheckYn = comm.getViewOptionData_value('006','ALL');
		if(headerCheckYn === 'Y'){
			$('#spendingYn').show();
		}
	}
}

/**
 * 서점 공통
 */ 
var storecomm = {
	init : function () {
		this.events();
	},
	
	events : function() {
		// 인쇄 버튼
		$("#btnWindowPrint").on('click', function() {
			window.print();
		});
	},
}