
/**
 * desc : 제외도서관리
 * auth : shon
 * limitBook.js
 */

var limitBook = {
		
	init : function () {
		$("#title").focus();
		this.events();
		this.initSort();
	},
	
	events : function () {
		
		// 검색조건 엔터키 이벤트
		$("#limitBookForm input[type=text]").on('keydown', function(e) {
			var keyCode = e.keyCode || e.which;
			if (keyCode == 13) {
				limitBook.search(1);
			}
		});
		
		// 조회버튼
		$("#btnSearch").on('click', function (e) {
			limitBook.search(1);
		});
		
		// 초기화버튼
		$("#btnClear").on('click', function (event) {
			$(".search_form_01 input[type=text]").val("");
		});
		
		// 추가버튼
		$("#btnAdd").on('click', function (e) {
			$("#limitBookInput").show();
			$("#in_title").focus();
		});
		
		// 삭제버튼
		$("#btnDel").on('click', function (e) {
			limitBook.deleteLimitBook();
		});
		
		// 닫기버튼
		$("#btnModalClose").on('click', function (e) {
			limitBook.limitBookClose();
		});
		
		// 입력버튼
		$("#btnInsert").on('click', function (e) {
			limitBook.getISBNCount();
		});
	},
	
	initSort : function () {
		
		var mask = "";
		if ($('#order').val() == 'ASC') {
			mask = "▲";
		} else if ($('#order').val() == 'DESC') {
			mask = "▼";
		}

		$('#limitBookInfo > thead > tr > th > a').each(function(i, el) {
			if ($(el).data('sort') == $('#sortCol').val()) {
				$(el).text($(el).text() + " " + mask);
				return false;
			}
		});
	},
		
	/**
	 * 조회
	 */
	search : function(pageNo) {
		$("#start").val(pageNo);
		var form = new commSubmit('limitBookForm');
		form.setUrl( _ctx + "/lib/envset/limitBook");
		form.submit();
	},
	
	/**
	 * 존재하는 ISBN인지 확인
	 */
	getISBNCount : function() {
		
		if (!commFormValid('saveLimitBook')) {
			return false;
		}
		
		var option = {
			url   : _ctx + '/lib/envset/getISBNCount',
			param : $.param({isbn : $('#in_isbn').val()}, true)
		};
		 
		$.commAjax (option, function (result) {
			if (result.resultCode == "Y") {
				if (result.isbnCnt > 0) {
					alert('이미 등록된 ISBN입니다.');
				} else {
					limitBook.saveLimitBook();
				}
			} else {
				alert(result.resultMessage);
			}
		});
	},
	
	saveLimitBook : function() {
		
		var params = {
			title       : $('#in_title').val(),
			author      : $('#in_author').val(),
			publisher   : $('#in_publisher').val(),
			isbn        : $('#in_isbn').val(),
			limitReason : $('#in_limitReason').val(),
		};
			
		var option = {
			url   : _ctx + '/lib/envset/saveLimitBook',
			param : $.param(params, true)
		};
		 
		$.commAjax (option, function (result) {
			if (result.resultCode == "Y") {
				alert('등록되었습니다.');
				limitBook.limitBookClose();
				limitBook.search(1);
			} else {
				alert(result.resultMessage);
			}
		});
	},
	
	deleteLimitBook : function () {
		
		var ischkLength = $("#limitBookInfo > tbody > tr > td > input:checkbox[name='chkNm']:checked").length;
		if (ischkLength == 0) {
			alert('선택된 데이터가 없습니다.');
			return false;
		}
		
		if (!confirm('선택된 데이터를 삭제하시겠습니까?')) {
			return false;
		}
			
		var arIsbn = new Array();
		$("#limitBookInfo > tbody > tr > td > input:checkbox[name='chkNm']").each(function(i, el) {
			if ($(el).is(":checked")) {
				arIsbn.push($(this).parent().siblings('td:eq(4)').text());
			}
		});
		
		var option = {
			url   : _ctx + '/lib/envset/deleteLimitBook',
			param : $.param({ delList : arIsbn }, true)
		};
			 
		$.commAjax (option, function (result) {
			if (result.resultCode == "Y") {
				alert('삭제되었습니다.');
				limitBook.search(1);
			} else {
				alert(result.resultMessage);
			}
		});
	},
	
	sortOrder : function($this, sortCol) {
		var mask = '';
	
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
		var colNm = $($this).text();
		
		colNm = colNm.replace(" ▲", "").replace(" ▼", "");
		$($this).text(colNm + " " + mask);
		
		// 조회
		limitBook.search(1);
	},
	
	/**
	 * 전체 선택
	 */
	checkedAll :  function ($this, tabId) {
		var isChk = $($this).is(":checked");
		var name  = $($this).data('name');
		$('#' + tabId + " > tbody > tr > td > input:checkbox[name='"+name+"']").each(function(i, el) {
			$(el).prop("checked", isChk)
		});
	},
	
	/**
	 * 추가 레이어 닫기
	 */
	limitBookClose : function() {
		$("#limitBookInput").hide();
		$("#saveLimitBook input[type=text]").val('');
	}
};


// onload 
$(document).ready(function() {
	limitBook.init();
});