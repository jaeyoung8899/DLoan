
/**
 * desc : libOrdered
 * auth : shon
 * libOrdered.js
 */

var libOrdered = {
		
	init : function () {
		this.events();
		
		$("#sortable").sortable();
		$("#sortable").disableSelection();
		
		if ($('#store tbody > tr').length == 0) {
			alert('도서관 배분관리 기초데이터를 확인해주세요.');
			$("#btnSave").attr('disabled', true);
		} else {
			this.initLibSearch($('#store tbody > tr:eq(0) input').val(), $('#store tbody > tr:eq(0) td:eq(1)').text());
		}
	},
	
	
	events : function () {
		// 저장버튼
		$("#btnSave").on('click', function (event) {
			//
			libOrdered.libOrderSave();
		});
	},
		
	/**
	 * 조회
	 */
	initLibSearch : function(storeId, storeNm) {

		$('#libTitle').text('[' + storeNm + '] 도서관 배분 우선순위');
		$('#storeId').val(storeId)
		
		
		var params = {
				storeId : storeId,
			};
			
			var option = {
				url   : _ctx + '/lib/envset/selectLibraryOrderInfo',
				param : $.param(params, true)
			};
			 
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					libOrdered.libDraw(result.storeList);
				} else {
					alert(result.resultMessage);
				}
			});
	},
	
	/**
	 * 저장
	 */
	libOrderSave : function() {

		var params = {
			lib     : $("#sortable" ).sortable("toArray"),
			storeId : $('#storeId').val()
		};
		
		var option = {
			url   : _ctx + '/lib/envset/saveLibOrdered',
			param : $.param(params, true)
		};
		 
		$.commAjax (option, function (result) {
			if (result.resultCode == "Y") {
				alert('저장되었습니다.')
			} else {
				alert(result.resultMessage);
			}
		});
	},
	
	libDraw : function(libList) {
		
		$('#sortable').empty();
		
		$.each(libList, function(i, el) {
			$('#sortable').append(
				'<li class="ui-state-default" id="'+el.libManageCode+'"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span>'+el.libName+" ("+el.libManageCode+")"+'</li>'
			)
		});
	}
};


// onload 
$(document).ready(function() {
	libOrdered.init();
});