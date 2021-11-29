
/**
 * desc : 바로대출 신청
 * auth : shon
 * request.js
 */
var first=0;
var request = {
	
	// 초기 
	init : function () {
		this.events();
	},
	
	events : function () {
		
		first = $(window).height();
		if($(window).width()<=700){
			$('#d_titl, #d_auth, #d_publ').on('focus', function() {
				location.href = '#' + $(this).attr('id');
			});
			$('#d_titl').bind("touchstart",function(e){
				location.href = '#' + $(this).attr('id');
			});
			$('#d_auth').bind("touchstart",function(e){
				location.href = '#' + $(this).attr('id');
			});
			$('#d_publ').bind("touchstart",function(e){
				location.href = '#' + $(this).attr('id');
			});
		}
		
		$('#d_titl, #d_auth, #d_publ').on('keydown', function(e) {
			var keyCode = e.keyCode || e.which;
			if (keyCode == 13) {
				request.search(1);
			}
		});
		
		// 조회버튼
		$("#btnSearch").on('click', function (event) {
			request.search(1);
		});
		
		// 신청 서점, 도서관 선택
		$(":button[name='selectObject']").on('click', function() {
			if($(this).val() == 'bookstore') {
				$("input[name='reqType']").val("bookstore");
				$("div[data-group='bookstore']").show();
				$("div[data-group='library']").hide();
			} else if($(this).val() == 'library') {
				$("input[name='reqType']").val("library");
				$("div[data-group='bookstore']").hide();
				$("div[data-group='library']").show();
			}
			$(".sel_option").removeClass("sel_on");
			$(this).addClass("sel_on");
		});
		
		// 부가기능 팝업
		$("#addOnsY").on('change', function() {
			if($(this).is(":checked") == false) {
				alert('SMS수신 여부 동의하지 않으면 서비스에 제한이 있습니다');
			}
		});
		
		// SMS 수신 팝업
		$("#smsY").on('change', function() {
			if($(this).is(":checked") == false) {
				alert('SMS수신 여부 동의하지 않으면 서비스에 제한이 있습니다');
			}
		});

		//헤더 표시여부
		// var headerYn = comm.getViewOptionData_value('002','ALL');
		// if(headerYn === 'Y') {
		// 	$('#reqHeader').show();
		// }

	},

	/**
	 * 조회
	 */
	search : function(pageNo) {
		
		//vlaidatiton
		if ($.isEmpty($("#d_titl").val()) && $.isEmpty($("#d_auth").val()) && $.isEmpty($("#d_publ").val())) {
			alert('검색조건 중 한가지 이상은 입력하십시오.');
			$("#d_titl").focus();
			return false;
		}	
		
		$("#pageNo").val(pageNo);

		var form = new commSubmit('bookSearchForm');
		form.addParam('newPhone1' , $('#newPhone1').val());
		form.addParam('newPhone2' , $('#newPhone2').val());
		form.addParam('newPhone3' , $('#newPhone3').val());
		form.addParam('bookstore' , $('#selStore').val());
		form.addParam('library'   , $('#selLib').val());
		form.addParam('reqType'   , $('#reqType').val());
		form.addParam('addOnsYn'  , $('#addOnsY').is(":checked") ? "Y" : "N");
		form.addParam('smsYn'     , $('#smsY').is(":checked") ? "Y" : "N");
		form.setUrl( _ctx + "/request");
		form.submit();
	},
	
	/**
	 * 신청
	 */
	userRequest : function($this) {


		/*var today = comm.date.formatDateToStr(new Date(), 'yyyy/MM/dd HH:mm:ss');
		var LimitStartDay = '2019/12/11 00:00:01';
		var LimitEndDay = '2019/12/16 00:00:01';
		if(today >= LimitStartDay && today <= LimitEndDay) {
				alert('12월 16일 부터 서비스를 시작합니다.');
				return false;
		}*/

		//vlaidatiton
		if (!commFormValid('requestForm')) {
			return false;
		}
		// ISBN
		var isbn_chk = $($this).parent().children('input[name=isbn]').val();
		
		// 신청유형
		var type = $('#reqType').val();
				
		if (confirm('서점바로대출 서비스를 신청하시겠습니까?')) {
			var params = {
				phone         : $('#phone1').val() + $('#phone2').val() + $('#phone3').val(),
				newPhone      : $('#newPhone1').val() + $('#newPhone2').val() + $('#newPhone3').val(),
				smsYn         : $(':checkbox[name="smsYn"]').is(":checked") ? "Y" : "N",
				addOnsYn      : $(':checkbox[name="addOnsYn"]').is(":checked") ? "Y" : "N",
				type          : type,
				storeId       : $('#selStore').val(), 
				libManageCode : $('#selLib').val(), 
				title         : $($this).parent().children('input[name=title]').val(),
				imgUrl        : $($this).parent().children('input[name=imgUrl]').val(),
				isbn          : isbn_chk,
				author        : $($this).parent().children('input[name=author]').val(),
				publisher     : $($this).parent().children('input[name=publisher]').val(),
				price         : $($this).parent().children('input[name=price]').val(), 
				pubDate       : $($this).parent().children('input[name=pubdate]').val(), 
			};
			
			var option = {
				url   : _ctx + '/insertBookRequest',
				param : $.param(params, true)
			};
			
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					if($('#Chuseok').val() == "Y"){
						alert('신청되었습니다. 신청현황에서 확인해주세요. 추석연휴기간에는 도서준비가 늦춰질 수 있습니다. 양해 부탁드립니다.');
					}else{
						alert('신청되었습니다. 신청현황에서 확인해주세요.');
					}
				} else {
					
					alert(result.resultMessage);
				}
			});
		}
	},
};

//휴대폰 번호 자리수가 찰 경우 다음 칸으로 이동
$('#phone1').keyup(function(e){if($('#phone1').val().length >= 3 ){$('#phone2').focus();}});
$('#phone2').keyup(function(e){if($('#phone2').val().length >= 4 ){$('#phone3').focus();}});
$('#newPhone1').keyup(function(e){if($('#newPhone1').val().length >= 3 ){$('#newPhone2').focus();}});
$('#newPhone2').keyup(function(e){if($('#newPhone2').val().length >= 4 ){$('#newPhone3').focus();}});

// onload 
$(document).ready(function() {
	request.init();
});

