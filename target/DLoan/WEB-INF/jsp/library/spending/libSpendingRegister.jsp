<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>

<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" />
<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" />
<link rel="stylesheet" href="<c:url value="/resources/library/css/04_setting.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/05_popup.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<style type="text/css">
	.search_form_01, .btn_wrap, .result_table {width:100%}
	.result_table input{text-align:center;}
	.result_table tfoot td, .result_table tfoot input {font-weight:bold;}
	input.price[id$='_total']{border:0;background-color:transparent;}
	.result_list_wrapper {width:100%;overflow-x:auto;}
	.btnDeleteRow{width: 25px;height: 25px;background: url(/resources/library/images/button-x-off.png);}
	input.disabled {background-color:gainsboro;}
</style>
</head>
<body>

	<%@ include file="/WEB-INF/jsp/common/libHeader.jsp"%>

	<div class="content">
		<section class="content_main">
			<%@ include file="/WEB-INF/jsp/library/spending/spendingMenu.jsp"%>
			<section class="setting_cont">
				<!-- 검색 조건 -->
				<form name="requestInfoForm" id="requestInfoForm">
					<section class="search_form_01">
						<c:choose>
							<c:when test="${sessionScope.LIB_MNG_CD eq null or sessionScope.LIB_MNG_CD eq ''}">
								<c:if test="${fn:length(libList) > 0}">
									<label for="lib">납품도서관</label>
									<select class="select_180" id="libManageCode" name="libManageCode" title="납품도서관" rules="required">
										<option value="">전체</option>
										<kaitUi:codeList list="${libList}"></kaitUi:codeList>
									</select>
								</c:if>
							</c:when>
							<c:otherwise>
								<input type="hidden" id="libManageCode" name="libManageCode" value="${sessionScope.LIB_MNG_CD}" title="납품도서관" rules="required">
							</c:otherwise>
						</c:choose>
						<label for="fiscalYear">회계연도</label>
						<input type="text" class="input_100" id="fiscalYear" name="fiscalYear" title="회계연도" rules="required">
						<label for="class">구분</label>
						<select class="select_140" id="class" name="class" rules="required">
							<option value="0">반기별</option>
							<option value="1">분기별</option>
							<option value="2">월별</option>
							<option value="3">기간설정</option>
						</select>
						<label for="deadline">회기말</label>
						<input type="text" class="input_100" id="deadline" name="deadline" title="회기말" rules="required">
						<label for="budget">금액</label>
						<input type="text" class="input_100 price" id="budget" name="budget" title="금액" readonly rules="required">
					</section>
					<div class="btn_wrap"></div>
					
					<!-- 구분별 테이블 -->
					<div class="btn_wrap" style="margin-bottom: 10px;">
						<input type="button" class="btn_add" value="행 추가" id="btnAddRow" style="display:none;">
						<input type="button" class="btn_add" value="서점별 입력" id="btnStoreInput">
						<input type="button" class="btn_add" value="일괄 입력" id="btnAllInput">
						<input type="button" class="btn_add" value="반영" id="btnApply">
					</div>
					
					<!-- 반기별 -->
					<div class="result_list" id="view0">
						<table class="result_table">
							<colgroup>
								<col width="250px">
								<col width="250px">
								<col width="250px">
								<col width="250px">
							</colgroup>
							<thead>
								<tr>
									<th>서점</th>
									<th>상반기</th>
									<th>하반기</th>
									<th>Total</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
							<tfoot>
								<tr>
									<td>Total</td>
									<td><input type="text" class="input_120 price" id="half1_total" readonly></td>
									<td><input type="text" class="input_120 price" id="half2_total" readonly></td>
									<td><input type="text" class="input_120 price" id="v0_total" readonly></td>
								</tr>
							</tfoot>
						</table>
					</div>
					
					<!-- 분기별 -->
					<div class="result_list" id="view1" style="display:none;">
						<table class="result_table">
							<colgroup>
								<col width="160px">
								<col width="170px">
								<col width="170px">
								<col width="170px">
								<col width="170px">
								<col width="160px">
							</colgroup>	
							<thead>
								<tr>
									<th>서점</th>
									<th>1분기</th>
									<th>2분기</th>
									<th>3분기</th>
									<th>4분기</th>
									<th>Total</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
							<tfoot>
								<tr>
									<td>Total</td>
									<td><input type="text" class="input_120 price" id="quarter1_total" readonly></td>
									<td><input type="text" class="input_120 price" id="quarter2_total" readonly></td>
									<td><input type="text" class="input_120 price" id="quarter3_total" readonly></td>
									<td><input type="text" class="input_120 price" id="quarter4_total" readonly></td>
									<td><input type="text" class="input_120 price" id="v1_total" readonly></td>
								</tr>
							</tfoot>
						</table>
					</div>
					
					<!-- 월별 -->
					<div class="result_list"  id="view2" style="display:none;">
						<div class="result_list_wrapper">
							<table class="result_table" style="min-width:1400px">
								<colgroup>
									<col width="165px">
									<col width="95px">
									<col width="95px">
									<col width="95px">
									<col width="95px">
									<col width="95px">
									<col width="95px">
									<col width="95px">
									<col width="95px">
									<col width="95px">
									<col width="95px">
									<col width="95px">
									<col width="95px">
									<col width="95px">
								</colgroup>
								<thead>
									<tr>
										<th>서점</th>
										<th>1월</th>
										<th>2월</th>
										<th>3월</th>
										<th>4월</th>
										<th>5월</th>
										<th>6월</th>
										<th>7월</th>
										<th>8월</th>
										<th>9월</th>
										<th>10월</th>
										<th>11월</th>
										<th>12월</th>
										<th>Total</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
								<tfoot>
									<tr>
										<td>Total</td>
										<td><input type="text" class="input_80 price" id="month1_total" readonly></td>
										<td><input type="text" class="input_80 price" id="month2_total" readonly></td>
										<td><input type="text" class="input_80 price" id="month3_total" readonly></td>
										<td><input type="text" class="input_80 price" id="month4_total" readonly></td>
										<td><input type="text" class="input_80 price" id="month5_total" readonly></td>
										<td><input type="text" class="input_80 price" id="month6_total" readonly></td>
										<td><input type="text" class="input_80 price" id="month7_total" readonly></td>
										<td><input type="text" class="input_80 price" id="month8_total" readonly></td>
										<td><input type="text" class="input_80 price" id="month9_total" readonly></td>
										<td><input type="text" class="input_80 price" id="month10_total" readonly></td>
										<td><input type="text" class="input_80 price" id="month11_total" readonly></td>
										<td><input type="text" class="input_80 price" id="month12_total" readonly></td>
										<td><input type="text" class="input_80 price" id="v2_total"  readonly></td>
									</tr>
								</tfoot>
							</table>
						</div>
					</div>
					
					<!-- 기간설정 -->
					<div class="result_list"  id="view3" style="display:none;">
						<div class="result_list_wrapper">
							<table class="result_table" style="min-width:800px">
								<colgroup>
									<col width="200px">
									<col width="200px">
									<col width="200px">
									<col width="150px">
									<col width="50px">
								</colgroup>
								<thead>
									<tr>
										<th>서점</th>
										<th>시작일</th>
										<th>종료일</th>
										<th>금액</th>
										<th></th>
									</tr>
								</thead>
								<tbody></tbody>
								<tfoot>
									<tr>
										<td>Total</td>
										<td colspan="2"></td>
										<td><input type="text" class="input_80 price" id="v3_total" readonly></td>
										<td></td>
									</tr>
								</tfoot>
							</table>
						</div>
					</div>
				</form>
				<div class="btn_wrap" style="margin-top:20px">
					<input type="button" class="btn_add" value="등록" id="btnRegister">
				</div>
			</section>
		</section>
	</div>
	<!-- 일괄 입력 레이어 -->
	<div class="back_black" id="pricePop" style="display:none;">
		<div class="setting_popup_wrap" style="margin:350px auto;width:350px;height:170px;">
			<form class="" name="" id="" onsubmit="return false">
				<div class="popup_form_02">
					<label for="in_title" style="width:60px">금액</label>
					<input type="text" class="input_160" id="inputPrice" rules="required">
					<input type="button" class="btn_input" value="입력" id="btnInput" style="margin-left:105px;">
				</div>
				<input type="button" class="btn_pop_close" id="btnModalClose">
			</form>
		</div>
	</div>
	
	<!-- 서점별 일괄 입력 레이어 -->
	<div class="back_black" id="priceStorePop" style="display:none;">
		<div class="setting_popup_wrap" style="margin:350px auto;width:350px;height:190px;">
			<form class="" name="" id="" onsubmit="return false">
				<div class="popup_form_02">
					<label for="in_title" style="width:60px">서점</label>
					<select class="select_180" id="storeManageCode" name="storeManageCode" title="도서관별" rules="required">
					</select>
					<label for="in_title" style="width:60px">금액</label>
					<input type="text" class="input_160" id="inputStorePrice" rules="required">
					<input type="button" class="btn_input" value="입력" id="btnSInput" style="margin-left:105px;">
				</div>
				<input type="button" class="btn_pop_close" id="btnStoreModalClose">
			</form>
		</div>
	</div>
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	
	<script type="text/javascript" src="<c:url value="/resources/library/js/spending/libSpendingRegister.js?v=${version}"/>"></script>
</body>
</html>