
/**
 * desc : 지출결의
 * auth : jy
 * libSpending.js
 */

// 등록페이지
var libSpendingRegister = {
	// 서점리스트
	storeList : new Array(),
	
	init : function () {
		// 회기말 캘린더
		$("#deadline").datepicker({minDate:0});
		// sortable
		$("#view3").find("tbody").sortable().disableSelection();
		// 도서관 계정인 경우 서점 목록 조회
		if($("input[name=libManageCode]").length != 0) {
			libSpendingRegister.getStore($("input[name=libManageCode]").val());
		}
		// 이벤트 등록
		this.events();
	},
	
	events : function () {
		// 가격 입력항목 이벤트, 동적으로 생성된 태그에도 이벤트 적용
		$(document).on('keyup', '.price, #inputPrice', function() {
			this.value = comm.string.comma(this.value.replace(/,/gi, ""));
		});
		
		// 구분
		$("#class").on('change', function() {
			$("div[id^=view]").hide();
			$("div[id=view" + this.value + "]").show();
			// 행 추가 버튼
			if(this.value == "3") {
				$("#btnAddRow").show();
				$("#btnStoreInput").hide();
			} else {
				$("#btnAddRow").hide();
				$("#btnStoreInput").show();
			}
		});
		
		// 일괄 입력 레이어 open
		$("#btnAllInput").on('click', function() {
			libSpendingRegister.openPop();
		});
		
		// 일괄 입력 레이어 close
		$("#btnModalClose").on('click', function() {
			libSpendingRegister.closePop();
		});
		
		// 일괄 입력 레이어 입력 버튼
		$("#btnInput").on('click', function() {
			libSpendingRegister.allInput();
		});
		
		// 일괄 입력 레이어 금액 키 이벤트 등록(enter)
		$("#inputPrice").on('keyup', function(e) {
			if(e.keyCode == 13) {
				libSpendingRegister.allInput();
			}
		});
		
		// 반영 버튼
		$("#btnApply").on('click', function() {
			libSpendingRegister.calc();
		});
		
		// 등록버튼
		$("#btnRegister").on('click', function (event) {
			libSpendingRegister.regPrice();
		});
		
		// 도서관 콤보박스 이벤트
		$("#libManageCode").on('change', function() {
			libSpendingRegister.getStore(this.value);
		});
		
		// 기간설정 - 추가하기 버튼
		$("#btnAddRow").on('click', function() {
			libSpendingRegister.addCustomRow();
		});
		
		// 기간설정 - 행 삭제 버튼
		$(document).on('click', '.btnDeleteRow', function() {
			libSpendingRegister.deleteCustomRow(this);
		});
		
		// 서점별 일괄 입력 레이어 open
		$("#btnStoreInput").on('click', function() {
			libSpendingRegister.openStorePop();
		});
		
		// 서점별 일괄 입력 레이어 close
		$("#btnStoreModalClose").on('click', function() {
			libSpendingRegister.closePop();
		});
		
		// 서점별 일괄 입력 레이어 입력 버튼
		$("#btnSInput").on('click', function() {
			libSpendingRegister.allStoreInput();
		});
		
		// 서점별 일괄 입력 레이어 금액 키 이벤트 등록(enter)
		$("#inputStorePrice").on('keyup', function(e) {
			if(e.keyCode == 13) {
				libSpendingRegister.allStoreInput();
			}
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
				$("#v" + classValue + "_r" + i + "_total").val(rowTotal);
					
				// 전체 합계
				allTotal += rowTotal;
			});
		} else if(classValue == 1) {
			var storeId = $("#view" + classValue).find("input[name=storeId]");
			var q1 = $("#view" + classValue).find("input[name=quarter1]");
			var q2 = $("#view" + classValue).find("input[name=quarter2]");
			var q3 = $("#view" + classValue).find("input[name=quarter3]");
			var q4 = $("#view" + classValue).find("input[name=quarter4]");
			
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
				$("#quarter1_total").val(q1Sum);
				$("#quarter2_total").val(q2Sum);
				$("#quarter3_total").val(q3Sum);
				$("#quarter4_total").val(q4Sum);
				
				// 행 집계
				var rowTotal = q1Value + q2Value + q3Value + q4Value;
				// 행 데이터 세팅
				$("#v" + classValue + "_r" + i + "_total").val(rowTotal);
					
				// 전체 합계
				allTotal += rowTotal;
			});
		} else if(classValue == 2) {
			var storeId = $("#view" + classValue).find("input[name=storeId]");
			var month1 = $("#view" + classValue).find("input[name=month1]");
			var month2 = $("#view" + classValue).find("input[name=month2]");
			var month3 = $("#view" + classValue).find("input[name=month3]");
			var month4 = $("#view" + classValue).find("input[name=month4]");
			var month5 = $("#view" + classValue).find("input[name=month5]");
			var month6 = $("#view" + classValue).find("input[name=month6]");
			var month7 = $("#view" + classValue).find("input[name=month7]");
			var month8 = $("#view" + classValue).find("input[name=month8]");
			var month9 = $("#view" + classValue).find("input[name=month9]");
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
				$("#month1_total").val(month1Sum);
				$("#month2_total").val(month2Sum);
				$("#month3_total").val(month3Sum);
				$("#month4_total").val(month4Sum);
				$("#month5_total").val(month5Sum);
				$("#month6_total").val(month6Sum);
				$("#month7_total").val(month7Sum);
				$("#month8_total").val(month8Sum);
				$("#month9_total").val(month9Sum);
				$("#month10_total").val(month10Sum);
				$("#month11_total").val(month11Sum);
				$("#month12_total").val(month12Sum);
				
				// 행 집계
				var rowTotal = month1Value + month2Value + month3Value + month4Value + month5Value + month6Value
					+ month7Value + month8Value + month9Value + month10Value + month11Value + month12Value;
				// 행 데이터 세팅
				$("#v" + classValue + "_r" + i + "_total").val(rowTotal);
					
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
	
	// 화면의 가격 입력항목 콤마 세팅(price 클래스 요소)
	setComma : function() {
		$.each($(".price"), function(i, d) {
			d.value = comm.string.comma(d.value);
		});
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
				$view.find("input[name^=quarter]").val(price);
			} else if(currClass == 2) {
				$view.find("input[name^=month]").val(price);
			} else if(currClass == 3) {
				$view.find("input[name=term]").val(price);
			}
			libSpendingRegister.calc();
		}
		// 레이어 닫기
		libSpendingRegister.closePop();
	},
	
	// 서점별 일괄 입력 레이어 입력 버튼
	allStoreInput : function() {
		var price = $("#inputStorePrice").val() || 0;
		var store = $("select[name=storeManageCode]").val();
		var currClass = $("#class").val();
		var $view = $("#view" + currClass);
		
		if($view) {
			if(currClass==0){
				var $store = $view.find("input[value="+store+"]");
				$store.parent().parent().find("input[name^=half]").val(price);
				libSpendingRegister.calc();
			}else if(currClass == 1) {
				var $store = $view.find("input[value="+store+"]");
				$store.parent().parent().find("input[name^=quarter]").val(price);
				libSpendingRegister.calc();
			}else if(currClass == 2) {
				var $store = $view.find("input[value="+store+"]");
				$store.parent().parent().find("input[name^=month]").val(price);
				libSpendingRegister.calc();
			}
		}
		// 레이어 닫기
		libSpendingRegister.closePop();
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
				$view.find("input[name^=quarter]").val(price);
			} else if(currClass == 2) {
				$view.find("input[name^=month]").val(price);
			} else if(currClass == 3) {
				$view.find("input[name=term]").val(price);
			}
			libSpendingRegister.calc();
		}
		// 레이어 닫기
		libSpendingRegister.closePop();
	},
	
	// 일괄 입력 레이어 open
	openPop : function() {
		$("#pricePop").show();
		$("#inputPrice").focus();
	},
	
	// 서점별 일괄 입력 레이어 open
	openStorePop : function() {
		$("#priceStorePop").show();
		$("#inputStorePrice").focus();
	},
	
	// 일괄 입력 레이어 close
	closePop : function() {
		$("#priceStorePop").hide();
		$("#inputStorePrice").val("");
		$("#pricePop").hide();
		$("#inputPrice").val("");
	},
	
	// 등록
	regPrice : function() {
		var orgClass = $("#orgClass").val();
		var currClass = $("#class").val();
		
		if(confirm('등록하시겠습니까?')) {
			// validatiton
			if (!commFormValid('requestInfoForm')) {
				return false;
			}
			
			// 회기말 형식 체크
			if(!comm.date.isDate($("#deadline").val(), 'yyyy-MM-dd')) {
				$("#deadline").focus();
				alert('날짜 형식이 올바르지 않습니다.');
				return false;
			}
			
			// 총액 계산 먼저 화면에 출력
			libSpendingRegister.calc();
			
			var currClass = $("#class").val();
			var fiscalYear = $("#fiscalYear").val();
			var $view = $("#view" + currClass);
			
			var params = {
				libManageCode : $("#libManageCode").val(),
				class : currClass,
				fiscalYear : fiscalYear,
				budget : $("#budget").val().replace(/,/gi, ""),
				deadline : $("#deadline").val()
			};
			
			var paramArray = new Array();
			var isValid = true;
			$view.find("tbody tr").each(function(i, d) {
				var storeId = $(d).find("input[name=storeId]").val();
				
				if(currClass == 0) {
					for(var i = 1;i <= 2;i++) {
						var param = {
							storeId : storeId,
							idx : i,
							price : $(d).find("input[name=half" + i + "]").val().replace(/,/gi, "") || 0,
							startDate : comm.date.formatDateToStr(new Date(fiscalYear, 6 * (i - 1), 1)),
							endDate : comm.date.formatDateToStr(new Date(fiscalYear, 6 * i, 0))
						};
						paramArray.push(param);
					}
				} else if(currClass == 1) {
					for(var i = 1;i <= 4;i++) {
						var param = {
							storeId : storeId,
							idx : i,
							price : $(d).find("input[name=quarter" + i + "]").val().replace(/,/gi, "") || 0,
							startDate : comm.date.formatDateToStr(new Date(fiscalYear, 3 * (i - 1), 1)),
							endDate : comm.date.formatDateToStr(new Date(fiscalYear, 3 * i, 0))
						};
						paramArray.push(param);
					}
				} else if(currClass == 2) {
					for(var i = 1;i <= 12;i++) {
						var param = {
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
						startDate.focus();
						alert('시작일 보다 종료일이 작을 수 없습니다.');
						isValid = false;
						return false;
					}
					
					var param = {
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
			
			var option = {
				url   : _ctx + '/lib/spending/regPrice',
				param : $.param(params, true)
			};
			
			$.commAjax (option, function (result) {
				if (result.resultCode == "Y") {
					alert('등록되었습니다.');
					location.replace('/lib/spending');
				} else {
					alert(result.resultMessage);
				}
			});
		}
	},
	
	// 서점 목록 조회
	getStore : function(libManageCode) {
		// 그리드 삭제
		$("div[id^=view] tbody").empty();
		
		// 납품도서관 전체 선택시
		if(libManageCode == '') {
			comm.loading.show();
			return;
			comm.loading.hide();
		}
		
		var params = {
			libManageCode : libManageCode
		};
		
		var option = {
			url   : _ctx + '/lib/spending/getStore',
			param : $.param(params, true)
		};
		
		$.commAjax (option, function (result) {
			// 서점 목록 초기화
			libSpendingRegister.storeList = new Array();
			
			if (result.resultCode == "Y") {
				var storeList = result.storeList;
				
				if(storeList != undefined) {
					if(storeList.length > 0) {
						var $view0 = $("#view0");
						var $view1 = $("#view1");
						var $view2 = $("#view2");
						var $view3 = $("#view3");
						
						var halfList = ['half1', 'half2'];
						var quarterList = ['quarter1', 'quarter2', 'quarter3', 'quarter4'];
						var monthList = ['month1','month2','month3','month4','month5','month6','month7','month8','month9','month10','month11','month12'];
						var option="";
						
						$.each(storeList, function(i, d){
							var code = "<tr>";
							code += "<td>" + d.storeName + "<input type='hidden' name='storeId' value='" + d.storeId + "'/></td>";
							for(var j in halfList) {
								code += "<td><input type='text' class='input_120 price' name='" + halfList[j] + "' value='0'/></td>";
							}
							code += "<td><input type='text' class='input_120 price' id='v0_r" + i + "_total' readonly/></td>";
							code += "</tr>";
							// 추가
							$view0.find("tbody").append(code);
							
							code = "<tr>";
							code += "<td>" + d.storeName + "<input type='hidden' name='storeId' value='" + d.storeId + "'/></td>";
							for(var j in quarterList) {
								code += "<td><input type='text' class='input_80 price' name='" + quarterList[j] + "' value='0'/></td>";
							}
							code += "<td><input type='text' class='input_80 price' id='v1_r" + i + "_total' readonly/></td>";
							code += "</tr>";
							// 추가
							$view1.find("tbody").append(code);
							
							code = "<tr>";
							code += "<td>" + d.storeName + "<input type='hidden' name='storeId' value='" + d.storeId + "'/></td>";
							for(var j in monthList) {
								code += "<td><input type='text' class='input_80 price' name='" + monthList[j] + "' value='0'/></td>";
							}
							code += "<td><input type='text' class='input_80 price' id='v2_r" + i + "_total' readonly/></td>";
							code += "</tr>";
							// 추가
							$view2.find("tbody").append(code);
							
							option += "<option value="+d.storeId+">" + d.storeName + "</option>";
							
							// 서점목록 추가
							var store = {
								storeName : d.storeName,
								storeId : d.storeId
							};
							libSpendingRegister.storeList.push(store);
						});
						
						$("#storeManageCode").html(option);
					} else {
						alert('신청 서점 목록이 없습니다.');
					}
				} else {
					alert('서점 목록을 불러오는데 실패했습니다.');
				}
			} else {
				alert(result.resultMessage);
			}
			
			// 열 집계 초기화
			$("input[id$=total]").val('');
			// 계산
			libSpendingRegister.calc();
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
		
		var tbody = $("#view3").find("tbody");
		
		var code = "<tr>";
		code += "<td><select name=\"storeId\" class=\"select_180\">";
		code += "<option value=\"\">미선택</option>";
		$.each(this.storeList, function(i, d) {
			code += "<option value=\"" + d.storeId + "\">" + d.storeName + "</option>";
		});
		code += "</td>";
		code += "<td><input type=\"text\" name=\"startDate\" class=\"input_120\"></td>";
		code += "<td><input type=\"text\" name=\"endDate\" class=\"input_120\"></td>";
		code += "<td><input type=\"text\" name=\"term\" class=\"input_120 price\"></td>";
		code += "<td><input type=\"button\" class=\"btnDeleteRow\"></td>";
		code += "</tr>";
		
		// add
		tbody.append(code);
		
		// add datepicker
		tbody.find("input[name=startDate], input[name=endDate]").removeClass('hasDatepicker').datepicker({yearRange:fiscalYear.val() + ':+99', minDate:new Date(fiscalYear.val(), 0, 1)});
	},
	
	// 기간설정 - 행 삭제버튼 
	deleteCustomRow : function(obj) {
		var parentElement = $(obj).parent().parent('tr');
		if(parentElement.find("input[name$=Date], input[name=term]").val() != "") {
			if(!confirm('행을 삭제하시겠습니까?')) {
				return false;
			}
		}
		
		// 행 삭제
		$(obj).parent().parent('tr').remove();
		
		// 계산
		libSpendingRegister.calc();
	}
};

// onload 
$(document).ready(function() {
	// 지출결의 상세
	libSpendingRegister.init();
});