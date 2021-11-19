
/**
 * desc : 거절사유
 * auth : shon
 * cancelReason.js
 */

var cancelReason = {

	deleteRecKey : [],
	
	init : function () {
		this.events();
		
	},
	
	events : function () {
		// 저장버튼
		$("#btnSave").on('click', function (event) {
			//
			cancelReason.saveCancelReason();
		});
		
		$("#btnAdd").on('click', function (event) {
			cancelReason.reasonAdd();
		});
		
		$("#btnDel").on('click', function (event) {
			cancelReason.deleteCancelReason();
		});
	},
	
	/**
	 * 조회
	 */
	search : function(pageNo) {
		
		var form = new commSubmit();
		form.setUrl( _ctx + "/lib/envset/cancelReason");
		form.submit();
	},
	
	
	/**
	 *  저장
	 */
	saveCancelReason : function() {
		
		var arKey    = new Array();
		var arReason = new Array();
		var trReason = '';
		var isValid  = true;
		$('#reasonInfo > tbody > tr').each(function(i, el) {
			var trReason = $(el).children('td:eq(2)').find('input[name=cancelReason]').val()
			if ($.isEmpty(trReason)) {
				alert('거절사유를 입력하시오.');
				$(el).children('td:eq(2)').find('input[name=cancelReason]').focus();
				isValid    = false;
				return false;
			}
			
			arReason.push($(el).children('input').val() + "#@" +$(el).children('td:eq(2)').find('input[name=cancelReason]').val());
		});
		
		if (!isValid) {
			return false;
		}

		var params = {
			delList  : cancelReason.deleteRecKey,
			ltReason : arReason,
		};
		
		var option = {
			url   : _ctx + '/lib/envset/saveCancelReason',
			param : $.param(params, true)
		};
		 
		$.commAjax (option, function (result) {
			if (result.resultCode == "Y") {
				alert('저장되었습니다.');
				cancelReason.search();
			} else {
				
				alert(result.resultMessage);
			}
		});
	},
	
	deleteCancelReason : function() {
		var ltReckey    = new Array();
		
		var ischkLength = $("#reasonInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
		
		if (ischkLength == 0) {
			alert('선택된 데이터가 없습니다.');
			return false;
		}
		
		
		$('#reasonInfo > tbody > tr').each(function(i, el) {
			
			var isChk = $(el).find("input:checkbox[name='chkNm']").is(":checked");
			var recKey = $(el).find("input[name='recKey']").val();
			
			if (isChk) {
				if (!$.isEmpty(recKey)) {
					cancelReason.deleteRecKey.push(recKey);
				}
				$(this).remove();
			}
		})
		
		$('#reasonInfo > tbody > tr').each(function(i, el) {
			$(el).children('td:eq(0)').text((i+1));
		})
	},
	
	reasonAdd : function() {
		
		var cnt = $('#reasonInfo > tbody > tr').length;
		
		var content = '';
		content += '<tr>';
		content += '	<input type="hidden" name="reckey" value=""  />';
		content += '	<td>'+(cnt+1)+'</td>';
		content += '	<td><input type="checkbox" name="chkNm" class="check" id="check_'+(cnt+1)+'" value=""><label for="check_'+(cnt+1)+'" class="check_wrap"></label></td>';
		content += '	<td><input type="text" name="cancelReason" maxlength=1300 value="" style="width:100%; height:30px"></td>';
		content += '</tr>';
		$('#reasonInfo > tbody').append(content);
		
		$('#reasonInfo > tbody > tr:eq('+cnt+')').find('input').focus();
	
	},
	
	checkedAll :  function ($this, tabId) {
		var isChk = $($this).is(":checked");
		var name = $($this).data('name');
		
		$('#' + tabId + " > tbody > tr > td > input:checkbox[name='"+name+"']").each(function(i, el) {
			$(el).prop("checked", isChk)
		});
	},
	
};


// onload 
$(document).ready(function() {
	cancelReason.init();
});