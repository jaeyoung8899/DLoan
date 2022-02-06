
/**
 * desc : 지출결의 수정
 * auth : wb
 * libSpendingModify.js
 */

// 지출결의 수정
var libSpendingModify = {
	// 서점리스트
	storeList : new Array(),
	// 삭제할 키
	deleteList : new Array(),
	
	init : function () {
		// 현재 화면 가격 계산
		this.calc();
		
		// 회기말 캘린더
		$("#deadline").datepicker({minDate:0});
		
		// 기간설정 - 시작일, 종료일 캘린더
		$("input[name=startDate], input[name=endDate]").datepicker({yearRange:$("#fiscalYear").val() + ':+99', minDate:new Date($("#fiscalYear").val(), 0, 1)});
		
		// 회기말 이후 인지 체크
		var fixed = $("#fixed").val();
		if(fixed != undefined && fixed == 'Y') {
			$("#libManageCode, #fiscalYear, #class, #deadline").attr('disabled', true);
			$(".price").attr('readonly', true);
		}
		
		// sortable
		$("#view3").find("tbody").sortable().disableSelection();
		
		// 서점 목록 조회
		libSpendingModify.getStore($("select[name=libManageCode]").val());
		
		// events
		this.events();
	},
	
	// 화면 이벤트
	events : function() {
		// 가격 입력항목 이벤트
		$(document).on('keyup', ".price, #inputPrice", function() {
			this.value = comm.string.comma(this.value.replace(/,/gi, ""));
		});
		
		// 사용 예산 보기 버튼
		$("#btnUsedShow").on('click', function() {
			libSpendingModify.showUsedPrice($(this).data('showUsedPrice'));
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
			// 사용 예산 숨기기
			libSpendingModify.showUsedPrice(false);
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
			// 행 추가 버튼
			if(this.value == "3") {
				$("#btnAddRow").show();
			} else {
				$("#btnAddRow").hide();
			}
		});
		
		// 기간설정 - 추가하기 버튼
		$("#btnAddRow").on('click', function() {
			libSpendingModify.addCustomRow();
		});
		
		// 기간설정 - 행 삭제 버튼
		$(document).on('click', '.btnDeleteRow', function() {
			libSpendingModify.deleteCustomRow(this);
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
			var storeId = $("#view" + classValue).find("input[name=storeId]");
			var half1 = $("#view" + classValue).find("input[name=half1]");
			var half2 = $("#view" + classValue).find("input[name=half2]");
			
			// 분기별 합계
			var half1Sum = 0;
			var half2Sum = 0;
			
			// 전체 합계
			allTotal = 0;
			
			$.each(storeId, function(i, d){
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
			var storeId = $("#view" + classValue).find("input[name=storeId]");
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
			
			$.each(storeId, function(i, d){
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
		} else if(classValue == 2) {
			var storeId = $("#view" + classValue).find("input[name=storeId]");
			var month1  = $("#view" + classValue).find("input[name=month1]");
			var month2  = $("#view" + classValue).find("input[name=month2]");
			var month3  = $("#view" + classValue).find("input[name=month3]");
			var month4  = $("#view" + classValue).find("input[name=month4]");
			var month5  = $("#view" + classValue).find("input[name=month5]");
			var month6  = $("#view" + classValue).find("input[name=month6]");
			var month7  = $("#view" + classValue).find("input[name=month7]");
			var month8  = $("#view" + classValue).find("input[name=month8]");
			var month9  = $("#view" + classValue).find("input[name=month9]");
			var month10 = $("#view" + classValue).find("input[name=month10]");
			var month11 = $("#view" + classValue).find("input[name=month11]");
			var month12 = $("#view" + classValue).find("input[name=month12]");
			
			// 월별 합계
			var month1Sum = 0;
			var month2Sum = 0;
			var month3Sum = 0;
			var month4Sum = 0;
			var month5Sum = 0;
			var month6Sum = 0;
			var month7Sum = 0;
			var month8Sum = 0;
			var month9Sum = 0;
			var month10Sum = 0;
			var month11Sum = 0;
			var month12Sum = 0;
			
			// 전체 합계
			allTotal = 0;
			
			$.each(storeId, function(i, d){
				var month1Value  = Number(month1[i].value.replace(/,/gi, "")) || 0;
				var month2Value  = Number(month2[i].value.replace(/,/gi, "")) || 0;
				var month3Value  = Number(month3[i].value.replace(/,/gi, "")) || 0;
				var month4Value  = Number(month4[i].value.replace(/,/gi, "")) || 0;
				var month5Value  = Number(month5[i].value.replace(/,/gi, "")) || 0;
				var month6Value  = Number(month6[i].value.replace(/,/gi, "")) || 0;
				var month7Value  = Number(month7[i].value.replace(/,/gi, "")) || 0;
				var month8Value  = Number(month8[i].value.replace(/,/gi, "")) || 0;
				var month9Value  = Number(month9[i].value.replace(/,/gi, "")) || 0;
				var month10Value = Number(month10[i].value.replace(/,/gi, "")) || 0;
				var month11Value = Number(month11[i].value.replace(/,/gi, "")) || 0;
				var month12Value = Number(month12[i].value.replace(/,/gi, "")) || 0;
				
				// 열 집계
				month1Sum  += month1Value;
				month2Sum  += month2Value;
				month3Sum  += month3Value;
				month4Sum  += month4Value;
				month5Sum  += month5Value;
				month6Sum  += month6Value;
				month7Sum  += month7Value;
				month8Sum  += month8Value;
				month9Sum  += month9Value;
				month10Sum += month10Value;
				month11Sum += month11Value;
				month12Sum += month12Value;
				
				// 열 데이터 세팅
				$("#month1_total" ).val(month1Sum);
				$("#month2_total" ).val(month2Sum);
				$("#month3_total" ).val(month3Sum);
				$("#month4_total" ).val(month4Sum);
				$("#month5_total" ).val(month5Sum);
				$("#month6_total" ).val(month6Sum);
				$("#month7_total" ).val(month7Sum);
				$("#month8_total" ).val(month8Sum);
				$("#month9_total" ).val(month9Sum);
				$("#month10_total").val(month10Sum);
				$("#month11_total").val(month11Sum);
				$("#month12_total").val(month12Sum);
				
				// 행 집계
				var rowTotal = month1Value + month2Value + month3Value + month4Value + month5Value + month6Value
					+ month7Value + month8Value + month9Value + month10Value + month11Value + month12Value;
					
				// 행 데이터 세팅
				$("#v" + classValue + "_row" + i + "_total").val(rowTotal);
					
				// 전체 합계
				allTotal += rowTotal;
			});
		} else if(classValue == 3) {
			var term = $("#view" + classValue).find("input[name=term]");
			
			$.each(term, function(i, d){
				var termValue = Number(d.value.replace(/,/gi, "")) || 0;
				// 전체 합계
				allTotal += termValue;
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
			
			// 총액 계산 먼저 화면에 출력
			libSpendingModify.calc();
			
			var currClass = $("#class").val();
			var fiscalYear = $("#fiscalYear").val();
			var $view = $("#view" + currClass);
			
			var params = {
				recKey : $("input[name=spendingKey]").val(),
				libManageCode : $("#libManageCode").val(),
				class : currClass,
				orgClass : $("#orgClass").val(),
				fiscalYear : fiscalYear,
				budget : $("#budget").val().replace(/,/gi, ""),
				deadline : $("#deadline").val()
			};
			
			var paramArray = new Array();
			var isValid = true;
			$view.find("tbody tr").each(function(i, d) {
				var storeId = $(d).find("input[name=storeId]").val();
				var recKeys = $(d).find("input[name=recKey]");
				
				if(currClass == 0) {
					for(var i = 1;i <= recKeys.length;i++) {
						var param = {
							recKey : recKeys[i - 1].value,
							storeId : storeId,
							idx : i,
							price : $(d).find("input[name=half" + i + "]").val().replace(/,/gi, "") || 0,
							startDate : comm.date.formatDateToStr(new Date(fiscalYear, 6 * (i - 1), 1)),
							endDate : comm.date.formatDateToStr(new Date(fiscalYear, 6 * i, 0))
						};
						paramArray.push(param);
					}
				} else if(currClass == 1) {
					for(var i = 1;i <= recKeys.length;i++) {
						var param = {
							recKey : recKeys[i - 1].value,
							storeId : storeId,
							idx : i,
							price : $(d).find("input[name=q" + i + "]").val().replace(/,/gi, "") || 0,
							startDate : comm.date.formatDateToStr(new Date(fiscalYear, 3 * (i - 1), 1)),
							endDate : comm.date.formatDateToStr(new Date(fiscalYear, 3 * i, 0))
						};
						paramArray.push(param);
					}
				} else if(currClass == 2) {
					for(var i = 1;i <= recKeys.length;i++) {
						var param = {
							recKey : recKeys[i - 1].value,
							storeId : storeId,
							idx : i,
							price : $(d).find("input[name=month" + i + "]").val().replace(/,/gi, "") || 0,
							startDate : comm.date.formatDateToStr(new Date(fiscalYear, i - 1, 1)),
							endDate : comm.date.formatDateToStr(new Date(fiscalYear, i, 0))
						};
						paramArray.push(param);
					}
				} else if(currClass == 3) {
					storeId = $(d).find("select[name=storeId]");
					if(storeId.val() == "") {
						storeId.focus();
						alert('서점이 선택되지 않았습니다.');
						isValid = false;
						return false;
					}
					
					var startDate = $(d).find("input[name=startDate]");
					var endDate = $(d).find("input[name=endDate]");
					
					// 날짜 형식 체크
					if(startDate.val() < (fiscalYear + '-01-01') || startDate.val() > (fiscalYear + '-12-31')) {
						startDate.val('').focus();
						alert('해당 시작일은 회계연도에 속하지 않습니다.');
						isValid = false;
						return false;
					} else if(endDate.val() < (fiscalYear + '-01-01') || endDate.val() > (fiscalYear + '-12-31')) {
						endDate.val('').focus();
						alert('해당 종료일은 회계연도에 속하지 않습니다.');
						isValid = false;
						return false;
					} else if(!comm.date.compare(startDate.val(), endDate.val(), 'yyyy-MM-dd')) {
						startDate.focus();
						alert('시작일 보다 종료일이 작을 수 없습니다.');
						isValid = false;
						return false;
					} else if(!comm.date.isDate(startDate.val(), 'yyyy-MM-dd')) {
						startDate.focus();
						alert('날짜 형식이 올바르지 않습니다.');
						isValid = false;
						return false;
					} else if(!comm.date.isDate(endDate.val(), 'yyyy-MM-dd')) {
						endDate.focus();
						alert('날짜 형식이 올바르지 않습니다.');
						isValid = false;
						return false;
					}
					
					// 시작~종료 체크
					if(!comm.date.compare(startDate.val(), endDate.val(), 'yyyy-MM-dd')) {
						alert('시작일 보다 종료일이 작을 수 없습니다.');
						startDate.focus();
						isValid = false;
						return false;
					}
					
					var param = {
						recKey : recKeys[0].value || '',
						storeId : storeId.val(),
						idx : i + 1,
						price : $(d).find("input[name=term]").val().replace(/,/gi, "") || 0,
						startDate : comm.date.formatDateToStr(new Date(startDate.val())),
						endDate : comm.date.formatDateToStr(new Date(endDate.val()))
					};
					paramArray.push(param);
				} else {
					return false;
				}
			});
			
			if(!isValid) {
				return false;
			}
			
			params.storeInfo = JSON.stringify(paramArray);
			
			if(currClass == 3 && libSpendingModify.deleteList.length > 0) {
				params.deleteInfo = JSON.stringify(libSpendingModify.deleteList);
			}
			
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
				$view.find("input[name^=month]").val(price);
			} else if(currClass == 3) {
				$view.find("input[name=term]").val(price);
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
	},
	
	// 서점 목록 조회
	getStore : function(libManageCode) {
		var params = {
			libManageCode : libManageCode
		};
		
		var option = {
			url   : _ctx + '/lib/spending/getStore',
			param : $.param(params, true)
		};
		
		$.commAjax (option, function (result) {
			// 서점 목록 초기화
			libSpendingModify.storeList = new Array();
			
			if (result.resultCode == "Y") {
				var storeList = result.storeList;
				
				if(storeList != undefined) {
					if(storeList.length > 0) {
						$.each(storeList, function(i, d){
							// 서점목록 추가
							var store = {
								storeName : d.storeName,
								storeId : d.storeId
							};
							libSpendingModify.storeList.push(store);
						});
					} else {
						alert('신청 서점 목록이 없습니다.');
					}
				} else {
					alert('서점 목록을 불러오는데 실패했습니다.');
				}
			} else {
				alert(result.resultMessage);
			}
		});
	},
	
	// 기간설정 - 추가하기 버튼
	addCustomRow : function() {
		var fiscalYear = $("#fiscalYear");
		if(fiscalYear.val() == '') {
			fiscalYear.focus();
			alert('회계연도를 먼저 입력해주세요.');
			return false;
		}
		
		// 사용 예산 숨기기
		libSpendingModify.showUsedPrice(false);
		
		var tbody = $("#view3").find("tbody");
		var code = "<tr>";
		code += "<td><input type=\"hidden\" name=\"recKey\"><select name=\"storeId\" class=\"select_180\">";
		code += "<option value=\"\">미선택</option>";
		$.each(this.storeList, function(i, d) {
			code += "<option value=\"" + d.storeId + "\">" + d.storeName + "</option>";
		});
		code += "</td>";
		code += "<td><input type=\"text\" name=\"startDate\" class=\"input_120\"></td>";
		code += "<td><input type=\"text\" name=\"endDate\" class=\"input_120\"></td>";
		// 사용 예산 보기 활성화 체크
		code += "<td><input type=\"text\" name=\"term\" class=\"input_120 price\"></td>";
		code += "<td><input type=\"button\" class=\"btnDeleteRow\" ></td>";
		code += "</tr>";
		
		// add
		tbody.append(code);
		
		// add datepicker
		tbody.find("input[name=startDate], input[name=endDate]").removeClass('hasDatepicker').datepicker({yearRange:fiscalYear.val() + ':+0', minDate:new Date(fiscalYear.val(), 0, 1)});
	},
	
	// 기간설정 - 행 삭제버튼 
	deleteCustomRow : function(obj) {
		var parentElement = $(obj).parent().parent('tr');
		if(parentElement.find("input[name$=Date], input[name=term]").val() != "") {
			if(!confirm('행을 삭제하시겠습니까?')) {
				return false;
			}
		}
		// 삭제된 행 key
		if(parentElement.find("input[name=recKey]").length > 0) {
			libSpendingModify.deleteList.push(parentElement.find("input[name=recKey]").val());
		}
		
		// 행 삭제
		$(obj).parent().parent('tr').remove();
		
		// 계산
		libSpendingModify.calc();
	},
	
	// 사용 예산 보기
	showUsedPrice : function(isUsedPriceShow) {
		comm.loading.show();
		if(isUsedPriceShow) {
			// 보기
			$(".used").show();
			$(".result_table .price").hide();
			$("#btnUsedShow").val('사용 예산 숨기기').data('showUsedPrice', false);
		} else {
			// 숨기기
			$(".used").hide();
			$(".result_table .price").show();
			$("#btnUsedShow").val('사용 예산 보기').data('showUsedPrice', true);
		}
		comm.loading.hide();
	}
};

// onload 
$(document).ready(function() {
	// 지줄결의 수정
	libSpendingModify.init();
});