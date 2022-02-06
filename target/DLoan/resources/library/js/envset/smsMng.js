
/**
 * desc : sms 메세지 관리
 * auth : shon
 * smsMng.js
 */

var smsMng = {
	
	deleteRecKey : [],
	
	init : function () {
		this.events();
	},
	
	
	events : function () {
		// 저장버튼
		$("#btnSave").on('click', function (event) {
			//
			smsMng.saveSmsMng();
		});
		
		$("#btnAdd").on('click', function (event) {
			smsMng.smsAdd();
		});
		
		$("#btnDel").on('click', function (event) {
			
			var ltReckey    = new Array();
			
			var ischkLength = $("#smsmngTable > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
			
			if (ischkLength == 0) {
				alert('선택된 데이터가 없습니다.');
				return false;
			}
			
			$('#smsmngTable > tbody > tr').each(function(i, el) {
				
				var isChk = $(el).find("input:checkbox[name='chkNm']").is(":checked");
				var recKey = $(el).find("input[name='recKey']").val();
				
				if (isChk) {
					if (!$.isEmpty(recKey)) {
						smsMng.deleteRecKey.push(recKey);
					}
					$(this).remove();
				}
			})
			
			$('#smsmngTable > tbody > tr').each(function(i, el) {
				$(el).children('td:eq(0)').text((i+1));
			})
				
		});
	},
	
	/**
	 * 조회
	 */
	search : function() {
		var form = new commSubmit();
		form.setUrl( _ctx + "/lib/envset/smsMng");
		form.submit();
	},
	
	/**
	 *  저장
	 */
	saveSmsMng : function() {
		
		var arKey      = new Array();
		var arName     = new Array();
		var arContents = new Array();
		var trContents = ''
		var trName     = '';
		var isValid    = true;
		
		$('#smsmngTable > tbody > tr').each(function(i, el) {
			
			var trContents = $(el).find('input[name=contents]').val();
			var trName     = $(el).find('input[name=smsName]').val();
			var trAutoYn   = $(el).find('input[name=autoYn]').val();
			
			if (trAutoYn != "Y" && $.isEmpty(trName)) {
				alert('SMS 설정명을 입력하세요.');
				$(el).find('input[name=smsName]').focus();
				isValid    = false;
				return false;
			}
			if ($.isEmpty(trContents)) {
				alert('SMS 내용을 입력하세요.');
				$(el).find('input[name=contents]').focus();
				isValid    = false;
				return false;
			} else {
				if (!smsMng.smsMngVaild($(el).find('input[name=contents]'))) {
					isValid    = false;
					return false;
				} 
			}
			
			arKey.push($(el).find('input[name=recKey]').val());
			arName.push(trName);
			arContents.push(trContents);
		});
		
		if (!isValid) {
			return false;
		}

		if (confirm('저장하시겠습니까?')) {
			var params = {
				deleteRecKey : smsMng.deleteRecKey,
				recKey       : arKey,
				name         : arName,
				contents     : arContents,
			};
			
			var option = {
				url   : _ctx + '/lib/envset/saveSmsMng',
				param : $.param(params, true)
			};
			 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					alert('저장되었습니다.', function() {
						smsMng.search();
					});
				} else {
					alert(result.resultMessage);
				}
			});
		}
		
	},
	
	smsAdd : function() {
		
		var cnt = $('#smsmngTable > tbody > tr').length;
		
		var content = '';
		content += '<tr>';
		content += '	<input type="hidden" name="reckey" value=""  />';
		content += '	<td>'+(cnt+1)+'</td>';
		content += '	<td><input type="checkbox" name="chkNm" class="check" id="check_'+(cnt+1)+'" value=""><label for="check_'+(cnt+1)+'" class="check_wrap"></label></td>';
		content += '	<td><input type="text" name="smsName" maxlength=33 value="" style="width:140px; height:30px;"></td>';
		content += '	<td><input type="text" name="contents" value="" style="width:490px; height:30px;"></textarea></td>';
		content += '</tr>';
		$('#smsmngTable > tbody').append(content);
		$('#smsmngTable > tbody > tr:eq('+cnt+')').find('input').focus();
	},
	
	checkedAll :  function ($this, tabId) {
		var isChk = $($this).is(":checked");
		var name = $($this).data('name');
		
		$('#' + tabId + " > tbody > tr > td > input:checkbox[name='"+name+"']").each(function(i, el) {
			$(el).prop("checked", isChk)
		});
	},
	
	smsMngVaild : function($content) {
		
		var content = $content.val();
		var arIdx = new Array();
		var arCon = new Array();
		var cnt = 1; 
		for (var i = 0; i < content.length; i++) {
			
			if (content.charAt(i) == '$') {
				arIdx.push(content.indexOf('$', i));
			}
		}
		
		if ((arIdx.length % 2) != 0) {
			alert('$갯수가 짝수여야 합니다.');
			$content.focus();
			return false;
		}
		
		$('#smsInfo > tbody > tr').each(function(i, el) {
			arCon.push($(this).find('td:eq(0)').text());
		});
		
		var temp    = '';
		var arTemp  = new Array();
		var isExists = false;
		for (var j = 0; j < arIdx.length; j++) {
			if ((j % 2) == 1) {
				isExists = false; 
				temp = content.substring(arIdx[j-1], arIdx[j]) + "$";
				
				for (var k = 0; k < arCon.length; k++) {
					if (temp == arCon[k]) {
						isExists = true; 
						break;
					}
				}
				if (!isExists) {
					arTemp.push(temp);
				}
			}
		}
		
		if (arTemp.length > 0) {
			alert(arTemp.join(' ,') + '은(는) 값은 사용할 수 없습니다.');
			$content.focus();
			return false;
		}
		
		return true;
	}
};


// onload 
$(document).ready(function() {
	smsMng.init();
});