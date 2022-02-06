
/**
 * desc : 지출결의 수정
 * auth : wb
 * libSpendingModify.js
 */

// 지출결의 수정
var libSpendingModify = {
	
	init : function () {
		// 현재 화면 가격 계산
		this.calc();
		
		// 회기말 캘린더
		$("#deadline").datepicker({minDate:0});
		
		// 회기말 이후 인지 체크
		var fixed = $("#fixed").val();
		if(fixed != undefined && fixed == 'Y') {
			$("#libManageCode, #fiscalYear, #class, #deadline").attr('disabled', true);
			$(".price").attr('readonly', true);
		}
		
		
		
		// events
		this.events();
	},
	
	// 화면 이벤트
	events : function() {
		// 가격 입력항목 이벤트
		$(".price, #inputPrice").on('keyup', function() {
			this.value = comm.string.comma(this.value.replace(/,/gi, ""));
		});
		
		// 사용 예산 보기 버튼
		$("#btnUsedShow").on('click', function() {
			$('.used, .result_table .price').toggle();
		});
		
		// 일괄 입력 레이어 open
		$("#btnAllInput").on('click', function() {
			libSpendingModify.openPop();
		});
		
		// 일괄 입력 레이어 close
		$("#btnModalClose").on('click', function() {
			libSpendingModify.closePop();
		});
		
		// 일괄 입력 레이어 입력 버튼
		$("#btnInput").on('click', function() {
			libSpendingModify.allInput();
		});
		
		// 일괄 입력 레이어 금액 키 이벤트 등록(enter)
		$("#inputPrice").on('keyup', function(e) {
			if(e.keyCode == 13) {
				libSpendingModify.allInput();
			}
		});
		
		// 반영 버튼
		$("#btnApply").on('click', function() {
			libSpendingModify.calc();
		});
		
		// 저장 버튼
		$("#btnModify").on('click', function() {
			libSpendingModify.save();
		});
		
		// 삭제 버튼
		$("#btnDelete").on('click', function() {
			libSpendingModify.delete();
		});
		
		// 구분 콤보박스 이벤트
		$("#class").on('change', function() {
			var orgClass = $("#orgClass").val();
			var currClass = this.value;
			
			// 기존 구분과 다른 값으로 변경하는 경우
			if(orgClass != currClass) {
				// 화면 변경
				$("div[id^=view]").hide();
				$("div[id=view" + this.value + "]").show();
				// 0으로 초기화
				libSpendingModify.calc(0);
			} else if(orgClass == currClass) {
				// 기존 구분으로 변경하는 경우
				$("div[id^=view]").hide();
				$("div[id=view" + this.value + "]").show();
				
				libSpendingModify.calc();
			}
		});
	}, 
	
	// 화면의 가격 입력항목 콤마 세팅(price 클래스 요소)
	setComma : function() {
		$.each($(".price"), function(i, d) {
			d.value = comm.string.comma(d.value);
		});
	},
	
	// 전체 합계 계산
	calc : function(initPrice) {
		comm.loading.show();
		
		var classValue = $("#class").val();
		
		// 초기화 가격이 있는 경우 
		if(initPrice != undefined) {
			$("#view" + classValue).find(".price").val(initPrice);
		}
		
		var allTotal = 0;	// 전체 합계
		
		if(classValue == 0) {
			var rKey = $("#view" + classValue).find("input[name=recKey]");
			var half1 = $("#view" + classValue).find("input[name=half1]");
			var half2 = $("#view" + classValue).find("input[name=half2]");
			
			// 분기별 합계
			var half1Sum = 0;
			var half2Sum = 0;
			
			// 전체 합계
			allTotal = 0;
			
			$.each(rKey, function(i, d){
				var half1Value = Number(half1[i].value.replace(/,/gi, "")) || 0;
				var half2Value = Number(half2[i].value.replace(/,/gi, "")) || 0;
				
				// 열 집계
				half1Sum += half1Value;
				half2Sum += half2Value;
				
				// 열 데이터 세팅
				$("#half1_total").val(half1Sum);
				$("#half2_total").val(half2Sum);
				
				// 행 집계
				var rowTotal = half1Value + half2Value;
				// 행 데이터 세팅
				$("#v" + classValue + "_row" + i + "_total").val(rowTotal);
					
				// 전체 합계
				allTotal += rowTotal;
			});
		} else if(classValue == 1) {
			var rKey = $("#view" + classValue).find("input[name=recKey]");
			var q1 = $("#view" + classValue).find("input[name=q1]");
			var q2 = $("#view" + classValue).find("input[name=q2]");
			var q3 = $("#view" + classValue).find("input[name=q3]");
			var q4 = $("#view" + classValue).find("input[name=q4]");
			
			// 분기별 합계
			var q1Sum = 0;
			var q2Sum = 0;
			var q3Sum = 0;
			var q4Sum = 0;
			
			// 전체 합계
			allTotal = 0;
			
			$.each(rKey, function(i, d){
				var q1Value = Number(q1[i].value.replace(/,/gi, "")) || 0;
				var q2Value = Number(q2[i].value.replace(/,/gi, "")) || 0;
				var q3Value = Number(q3[i].value.replace(/,/gi, "")) || 0;
				var q4Value = Number(q4[i].value.replace(/,/gi, "")) || 0;
				
				// 열 집계
				q1Sum += q1Value;
				q2Sum += q2Value;
				q3Sum += q3Value;
				q4Sum += q4Value;
				
				// 열 데이터 세팅
				$("#q1_total").val(q1Sum);
				$("#q2_total").val(q2Sum);
				$("#q3_total").val(q3Sum);
				$("#q4_total").val(q4Sum);
				
				// 행 집계
				var rowTotal = q1Value + q2Value + q3Value + q4Value;
				// 행 데이터 세팅
				$("#v" + classValue + "_row" + i + "_total").val(rowTotal);
					
				// 전체 합계
				allTotal += rowTotal;
			});
		} else {
			var rKey = $("#view" + classValue).find("input[name=recKey]");
			var jan = $("#view" + classValue).find("input[name=jan]");
			var feb = $("#view" + classValue).find("input[name=feb]");
			var mar = $("#view" + classValue).find("input[name=mar]");
			var apr = $("#view" + classValue).find("input[name=apr]");
			var may = $("#view" + classValue).find("input[name=may]");
			var jun = $("#view" + classValue).find("input[name=jun]");
			var jul = $("#view" + classValue).find("input[name=jul]");
			var aug = $("#view" + classValue).find("input[name=aug]");
			var sep = $("#view" + classValue).find("input[name=sep]");
			var oct = $("#view" + classValue).find("input[name=oct]");
			var nov = $("#view" + classValue).find("input[name=nov]");
			var dec = $("#view" + classValue).find("input[name=dec]");
			
			// 월별 합계
			var janSum = 0;
			var febSum = 0;
			var marSum = 0;
			var aprSum = 0;
			var maySum = 0;
			var junSum = 0;
			var julSum = 0;
			var augSum = 0;
			var sepSum = 0;
			var octSum = 0;
			var novSum = 0;
			var decSum = 0;
			
			// 전체 합계
			allTotal = 0;
			
			$.each(rKey, function(i, d){
				var janValue = Number(jan[i].value.replace(/,/gi, "")) || 0;
				var febValue = Number(feb[i].value.replace(/,/gi, "")) || 0;
				var marValue = Number(mar[i].value.replace(/,/gi, "")) || 0;
				var aprValue = Number(apr[i].value.replace(/,/gi, "")) || 0;
				var mayValue = Number(may[i].value.replace(/,/gi, "")) || 0;
				var junValue = Number(jun[i].value.replace(/,/gi, "")) || 0;
				var julValue = Number(jul[i].value.replace(/,/gi, "")) || 0;
				var augValue = Number(aug[i].value.replace(/,/gi, "")) || 0;
				var sepValue = Number(sep[i].value.replace(/,/gi, "")) || 0;
				var octValue = Number(oct[i].value.replace(/,/gi, "")) || 0;
				var novValue = Number(nov[i].value.replace(/,/gi, "")) || 0;
				var decValue = Number(dec[i].value.replace(/,/gi, "")) || 0;
				
				// 열 집계
				janSum += janValue;
				febSum += febValue;
				marSum += marValue;
				aprSum += aprValue;
				maySum += mayValue;
				junSum += junValue;
				julSum += julValue;
				augSum += augValue;
				sepSum += sepValue;
				octSum += octValue;
				novSum += novValue;
				decSum += decValue;
				
				// 열 데이터 세팅
				$("#jan_total").val(janSum);
				$("#feb_total").val(febSum);
				$("#mar_total").val(marSum);
				$("#apr_total").val(aprSum);
				$("#may_total").val(maySum);
				$("#jun_total").val(junSum);
				$("#jul_total").val(julSum);
				$("#aug_total").val(augSum);
				$("#sep_total").val(sepSum);
				$("#oct_total").val(octSum);
				$("#nov_total").val(novSum);
				$("#dec_total").val(decSum);
				
				// 행 집계
				var rowTotal = janValue + febValue + marValue + aprValue + mayValue + junValue
					+ julValue + augValue + sepValue + octValue + novValue + decValue;
				// 행 데이터 세팅
				$("#v" + classValue + "_row" + i + "_total").val(rowTotal);
					
				// 전체 합계
				allTotal += rowTotal;
			});
		}
		
		// 전체 합계 세팅
		$("#v" + classValue + "_total").val(allTotal);
		$("#budget").val(allTotal);
		
		// 콤마 변환
		this.setComma();
		
		comm.loading.hide();
	},
	
	// 저장
	save : function() {
		var orgClass = $("#orgClass").val();
		var currClass = $("#class").val();
		
		var msg = '저장하시겠습니까?';
		if(orgClass != currClass) {
			msg = '이전에 입력한 데이터는 모두 삭제되고 새로 입력됩니다.\n그래도 ' + msg;
		}
		
		if(confirm(msg)) {
			//vlaidatiton
			if (!commFormValid('requestInfoForm')) {
				return false;
			}
			
			libSpendingModify.calc();
			
			var currClass = $("#class").val();
			var $view = $("#view" + currClass);
			
			var params = {
				recKey : $("input[name=spendingKey]").val(),
				libManageCode : $("#libManageCode").val(),
				class : currClass,
				fiscalYear : $("#fiscalYear").val(),
				budget : $("#budget").val().replace(/,/gi, ""),
				deadline : $("#deadline").val()
			};
			
			var paramArray = new Array();
			
			$view.find("tbody tr").each(function(i, d) {
				var param = {
					rKey : $(d).find("input[name=recKey]").val(),
					storeId : $(d).find("input[name=storeId]").val(),
					jan : 0,
					feb : 0,
					mar : 0,
					apr : 0,
					may : 0,
					jun : 0,
					jul : 0,
					aug : 0,
					sep : 0,
					oct : 0,
					nov : 0,
					dec : 0
				};
				
				if(currClass == 0) {
					param.jan = $(d).find("input[name=half1]").val().replace(/,/gi, "") || 0;
					param.jul = $(d).find("input[name=half2]").val().replace(/,/gi, "") || 0;
				} else if(currClass == 1) {
					param.jan = $(d).find("input[name=q1]").val().replace(/,/gi, "") || 0;
					param.apr = $(d).find("input[name=q2]").val().replace(/,/gi, "") || 0;
					param.jul = $(d).find("input[name=q3]").val().replace(/,/gi, "") || 0;
					param.oct = $(d).find("input[name=q4]").val().replace(/,/gi, "") || 0;
				} else if(currClass == 2) {
					param.jan = $(d).find("input[name=jan]").val().replace(/,/gi, "") || 0;
					param.feb = $(d).find("input[name=feb]").val().replace(/,/gi, "") || 0;
					param.mar = $(d).find("input[name=mar]").val().replace(/,/gi, "") || 0;
					param.apr = $(d).find("input[name=apr]").val().replace(/,/gi, "") || 0;
					param.may = $(d).find("input[name=may]").val().replace(/,/gi, "") || 0;
					param.jun = $(d).find("input[name=jun]").val().replace(/,/gi, "") || 0;
					param.jul = $(d).find("input[name=jul]").val().replace(/,/gi, "") || 0;
					param.aug = $(d).find("input[name=aug]").val().replace(/,/gi, "") || 0;
					param.sep = $(d).find("input[name=sep]").val().replace(/,/gi, "") || 0;
					param.oct = $(d).find("input[name=oct]").val().replace(/,/gi, "") || 0;
					param.nov = $(d).find("input[name=nov]").val().replace(/,/gi, "") || 0;
					param.dec = $(d).find("input[name=dec]").val().replace(/,/gi, "") || 0;
				} else {
					return false;
				}
				
				paramArray.push(param);
			});
			
			params.storeInfo = JSON.stringify(paramArray);
			
			var option = {
				url   : _ctx + '/lib/spending/savePrice',
				param : $.param(params, true)
			};
			
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					alert('저장되었습니다.');
					location.reload();
				} else {
					alert(result.resultMessage);
				}
			});
		}
	},
	
	// 일괄 입력 레이어 입력 버튼
	allInput : function() {
		var price = $("#inputPrice").val() || 0;
		
		var currClass = $("#class").val();
		var $view = $("#view" + currClass);
		
		if($view) {
			if(currClass == 0) {
				$view.find("input[name^=half]").val(price);
			} else if(currClass == 1) {
				$view.find("input[name^=q]").val(price);
			} else if(currClass == 2) {
				$view.find("input[name=jan],input[name=feb],input[name=mar],input[name=apr],input[name=may]"
							+ ",input[name=jun],input[name=jul],input[name=aug],input[name=sep],input[name=oct]"
							+ ",input[name=nov],input[name=dec]").val(price);
			}
			libSpendingModify.calc();
		}
		// 레이어 닫기
		libSpendingModify.closePop();
	},
	
	// 일괄 입력 레이어 open
	openPop : function() {
		$("#pricePop").show();
		$("#inputPrice").focus();
	},
	
	// 일괄 입력 레이어 close
	closePop : function() {
		$("#pricePop").hide();
		$("#inputPrice").val("");
	},
	
	// 지출결의 삭제
	delete : function() {
		var recKey = document.requestInfoForm.spendingKey.value;
		
		if(!confirm('해당연도 지출결의를 삭제하시겠습니까?')) {
			return;
		}
		
		var params = {
			recKey : recKey
		};
		
		var option = {
			url   : _ctx + '/lib/spending/delete',
			param : $.param(params, true)
		};
		
		$.commAjax (option, function (result) {
			if (result.resultCode == "Y") {
				alert('삭제되었습니다.');
				location.replace('/lib/spending');
			} else {
				alert(result.resultMessage);
			}
		});
	}
};

// onload 
$(document).ready(function() {
	// 지줄결의 수정
	libSpendingModify.init();
});