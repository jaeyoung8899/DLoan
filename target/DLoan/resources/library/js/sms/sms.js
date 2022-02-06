
/**
 * desc : sms 이력조회
 * auth : wb
 * sms.js
 */

var sms = {
	init : function () {
		this.events();
		
		$("#from_reqDate, #to_reqDate").datepicker({
			
		});

		comm.sort.initSort('requestInfo');
	},
	
	events : function () {
		// event
		$('#userNo, #userName, #sendPhone, #sendMessage').on('keydown', function(e) {
			var keyCode = e.keyCode || e.which;
			if (keyCode == 13) {
				sms.search(1);
			}
		});
		
		// 조회버튼
		$("#btnSearch").on('click', function (event) {
			sms.search(1);
		});
		
		// 초기화버튼
		$("#btnClear").on('click', function (event) {
			$("#requestInfoForm select").val("");
			$("#requestInfoForm input[type=text]").val("");
		});
	},
	
	/**
	 * 조회
	 */
	search : function(pageNo) {
		if (!commFormValid('requestInfoForm')) {
			return false;
		}
		
		if (!comm.date.compare($('#from_reqDate').val(), $('#to_reqDate').val(), 'yyyy-MM-dd')) {
			alert('발송시작일 보다 발송종료일은 작을수는 없습니다.');
			return false;
		}
		
		$("#start").val(pageNo);

		var form = new commSubmit('requestInfoForm');
		form.setUrl( _ctx + "/lib/sms");
		form.submit();
	},
};

// onload 
$(document).ready(function() {
	// sms
	sms.init();
});