
/**
 * desc : 납품요청 상세
 * auth : shon
 * storeResponse.js
 */

var storeResponseDetail = {
		
	deleteKey : [],
	
	init : function () {
		this.events();
		
		comm.sort.initSort('detailInfo');
	},
	
	events : function () {
		
		// 조회버튼
		$("#btnSearch").on('click', function (event) {
			storeResponseDetail.search(1);
		});
		
		// 추가
		$("#btnAdd").on('click', function (event) {
			// 
			$('#from_reqDate').val('');
			$('#to_reqDate')  .val('');
			$('#userNo')      .val('');
			$('#title')       .val('');
			$('#author')      .val('');
			$('#publisher')   .val('');
			$('#popOrder')    .val('');
			$('#popSortCol')  .val('');
			$('#selReqLib')   .val($('#selLib').val());
		
			$('#reqInfo > tbody').empty().append('<tr><td colspan="8">조회된 결과가 없습니다.</td></tr>');
			
			$('#reqAddInfo').show();
		});
		
		// 삭제
		$("#btnDel").on('click', function (event) {
			var isChkLength = $("#detailInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
			
			if (isChkLength == 0) {
				alert('선택된 데이터가 없습니다.');
				return false;
			}
			
			$("#detailInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
				if ($(el).is(":checked")) {
					if (!$.isEmpty($(el).parent().parent().find('input[name=tResKey]').val())) {
						storeResponseDetail.deleteKey.push($(el).parent().parent().find('input[name=recKey]').val());
					}
					
					$(el).parent().parent().remove();
				}
			});
			
			$("#detailInfo > tbody > tr").each(function(i, el) {
				$(el).find('td:eq(0)').text((i+1));
			});
			
		});
		
		// 저장
		$("#btnSave").on('click', function (event) {
			storeResponseDetail.saveStoreResponse();
		});
		
		// 목록
		$("#btnList").on('click', function (event) {
			document.location.href = _ctx + '/store/storeResponseInfo'
		});
		
		// 엑셀다운
		$("#btnExcelDown").on('click', function (event) {
			storeResponseDetail.excelDown();
		});
	},
	
		
	/**
	 * 조회
	 */
	search : function(pageNo) {
		
		//vlaidatiton
		if (!commFormValid('requestInfoForm')) {
			return false;
		}
		
		var form = new commSubmit('searchInfoForm');
		form.setUrl( _ctx + "/store/stroeResponseInfoDetail");
		form.submit();
	},
	
	
	saveStoreResponse : function () {
		if (!commFormValid('searchInfoForm')) {
			return false;
		}
		
		var isChk = true;
		var ischkLength = $("#detailInfo > tbody > tr").length;
		
		if (ischkLength == 0) {
			alert('저장 할 데이터가 없습니다.');
			return false;
		}
		
		var arRecKey = new Array();
		$("#detailInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
			arRecKey.push($(el).parent().parent().find('input[name=recKey]').val());
		});

		if (!isChk) {
			return false;
		} 
		
		if (confirm('저장 하시겠습니까?')) {
			var params = {
				ltRecKey      : arRecKey,
				resKey        : $('#resKey').val(),
				resTitle      : $('#resTitle').val(),
				libManageCode : $('#selLib').val(),
				resStatus     : $('#selStatus').val(),
				ltDelKey      : storeResponseDetail.deleteKey
			};
			
			var option = {
				url   : _ctx + '/store/saveStoreResponse',
				param : $.param(params, true)
			};
			 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					alert('저장되었습니다.');
					$('#resKey').val(result.resKey);
					storeResponseDetail.search(1);
					resKey
				} else {
					alert(result.resultMessage);
				}
			});
		}
	},
	
	/**
	 * 엑셀 다운로드
	 */
	excelDown : function() {
		if(confirm("엑셀파일을 다운로드 하시겠습니까?")) {
			var form = new commSubmit('searchInfoForm');
			form.setUrl( _ctx + "/store/storeResInfoDetailToExcel");
			comm.loading.hide();
			form.submit();
		}
	},
	
	checkedAll :  function ($this, tabId) {
		var isChk = $($this).is(":checked");
		var name = $($this).data('name');
		$('#' + tabId + " > tbody > tr > td > input:checkbox[name='"+name+"']").each(function(i, el) {
			$(el).prop("checked", isChk)
		});
	},
	
};

var resAddPop = {
	init : function () {
		this.events();
		
		$("#from_reqDate,#to_reqDate").datepicker({});
	},
	
	events : function () {
		
		// 조회버튼
		$("#btnReqSearch").on('click', function (event) {
			resAddPop.reqSearch(1);
		});
		
		// 초기화버튼
		$("#btnReqClear").on('click', function (event) {
			$("#reqSearchInfo input[type=text]").val("");
		});
		
		$("#btnPopClse").on('click', function (event) {
			$('#reqAddInfo').hide();
		});
		
		// 추가	
		$("#btnParentAdd").on('click', function (event) {
			var isChkLength = $("#reqInfo > tbody > tr > td > input:checkbox[name='chkPopNm']:checked").length;
			
			if (isChkLength == 0) {
				alert('선택된 데이터가 없습니다.');
				return false;
			}
			
			var content  = '';
			var cnt      = $('#detailInfo > tbody > tr').length;
			var arRecKey = new Array();
			var isChk    = true;
			$("#reqInfo > tbody > tr > td > input:checkbox[name='chkPopNm']").each(function(i, el) {
				if ($(el).is(":checked")) {
					arRecKey.push($(el).parent().parent().find('input[name=popRecKey]').val());
					content +='<tr>';
					content +='<input type="hidden" name="recKey" value="'+$(el).parent().parent().find('input[name=popRecKey]').val()+'"  />';
					content +='<input type="hidden" name="tResKey" value="" />';
					content +='<td>'+(++cnt)+'</td>';
					content +='<td>';
					content +='<input type="checkbox" id="check_'+cnt+'" name="chkNm" class="check">';
					content +='<label for="check_'+cnt+'" class="check_wrap"></label></td>';
					content +='<td>'+$(el).parent().parent().find('td:eq(2)').text()+'</td>';
					content +='<td style="text-align:left;">'+$(el).parent().parent().find('td:eq(3)').text()+'</td>';
					content +='<td>'+$(el).parent().parent().find('td:eq(4)').text()+'</td>';
					content +='<td>'+$(el).parent().parent().find('td:eq(5)').text()+'</td>';
					content +='<td>'+$(el).parent().parent().find('td:eq(6)').text()+'</td>';
					content +='<td>'+$(el).parent().parent().find('td:eq(7)').text()+'</td>';
					content +='</tr>';
				}
			});
			
			$("#detailInfo > tbody > tr").each(function(i, el) {
				var recKey = $(el).find('input[name=recKey]').val();
				
				for (var idx = 0; idx < arRecKey.length; idx++) {
					if (recKey == arRecKey[idx]) {
						isChk = false;
						break;
					}
				}
				if (!isChk) {
					return false;
				}
			});
			
			if (!isChk) {
				alert('중복된 자료가 존재합니다.');
				return false;
			}
			
			$('#detailInfo > tbody').append(content);
			
			$('#reqAddInfo').hide();
		});
	},
	
	reqSearch : function() {
		//vlaidatiton
		if (!commFormValid('reqSearchInfo')) {
			return false;
		}
		if (!comm.date.compare($('#from_reqDate').val(), $('#to_reqDate').val(), 'yyyy-MM-dd')) {
			alert('신청시작일 보다 신청종료일은 작을수는 없습니다.');
			return false;
		}
		
		var params = {
			libManageCode : $('#selReqLib').val(),
			from_reqDate  : $('#from_reqDate').val(),
			to_reqDate    : $('#to_reqDate').val(),
			userNo        : $('#userNo').val(),
			title         : $('#title').val(),
			author        : $('#author').val(),
			publisher     : $('#publisher').val(),
			order         : $('#popOrder').val(),
			sortCol       : $('#popSortCol').val(),
		};
		
		var option = {
			url   : _ctx + '/store/selectRequestList',
			param : $.param(params, true)
		};
		 
		$.commAjax (option, function (result) {
			if (result.resultCode == "Y") {

				$('#reqInfo > tbody').empty()
				if (result.resultList.length > 0) {
					
					var content = ''
						$.each(result.resultList, function(i, el) {
							
							content = '<tr>';
							content +='<input type="hidden" name="popRecKey" value="'+el.recKey+'"  />';
							content +='<td>'+el.rnum+'</td>';
							content +='<td>';
							content +='<input type="checkbox" id="popCheck_'+el.rnum+'" name="chkPopNm" class="check">';
							content +='<label for="popCheck_'+el.rnum+'" class="check_wrap"></label></td>';
							content +='<td>'+el.userNo+'</td>';
							content +='<td style="text-align:left;">'+el.title+'</td>';
							content +='<td>'+el.author+'</td>';
							content +='<td>'+el.reqDate+'</td>';
							content +='<td>'+el.reqStatusName+'</td>';
							content +='<td>'+el.resStatusName+'</td>';
							content +='</tr>';
							
							$('#reqInfo > tbody').append(content)
						});
				} else{
					$('#reqInfo > tbody').append('<tr><td colspan="8">조회된 결과가 없습니다.</td></tr>')
				}
				
			} else {
				alert(result.resultMessage);
			}
		});
	},
	
	sortOrder : function($this) {
		
		
		$('#reqInfo > thead > tr > th > a').each(function(i, el) {
			var colName = $(el).html();
			colName = colName.replace(" ▲", "").replace(" ▼", "");
			$(el).html(colName);
		});
		
		var mask = '';
		var sortCol = $($this).data('sort');
	
		if (sortCol == $('#popSortCol').val()) {
			
			if (!$.isEmpty($('#popOrder').val())) {
				if ($('#popOrder').val() == 'ASC') {
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
			$('#popOrder').val("ASC")
		} else {
			$('#popOrder').val("DESC")
		}
		
		$('#popSortCol').val(sortCol);
		
		// header 표시
		var colNm = $($this).html();
		
		colNm = colNm.replace(" ▲", "").replace(" ▼", "");
		$($this).html(colNm + " " + mask);
		
		// 조회 
		resAddPop.reqSearch();
	},
	
	checkedAll : function ($this) {
		var isChk = $($this).is(":checked");
		var name  = $($this).data('name');
		$("#reqInfo > tbody > tr > td > input:checkbox[name='"+name+"']").each(function(i, el) {
			$(el).prop("checked", isChk)
		});
	}
};

// onload 
$(document).ready(function() {
	// 신청승인
	storeResponseDetail.init();
	//
	resAddPop.init();
	// 서점 공통
	storecomm.init();
});