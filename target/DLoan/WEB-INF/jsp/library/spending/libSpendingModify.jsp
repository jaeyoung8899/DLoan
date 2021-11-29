<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>

<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" />
<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" />
<link rel="stylesheet" href="<c:url value="/resources/library/css/04_setting.css?v=${cssver}" />" type="text/css" media="screen" title="no title"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/05_popup.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<style type="text/css">
	.search_form_01, .btn_wrap, .result_table {width:100%}
	.result_table input{text-align:center;}
	.result_table tfoot td, .result_table tfoot input {font-weight:bold;}
	input.price[id$='_total']{border:0;background-color:transparent;}
	.result_list_wrapper {width:100%;overflow-x:auto;}
	.btnDeleteRow{width: 25px;height: 25px;background: url(/resources/library/images/button-x-off.png);}
	input.disabled {background-color:gainsboro;}
	.used{display:none;}
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
					<input type="hidden" id="orgClass" value="${spendingInfo['class']}">
					<input type="hidden" id="fixed" value="${spendingInfo.fixed }">
					
					<input type="hidden" name="spendingKey" value="${spendingInfo.recKey}">
					<section class="search_form_01">
						<label for="libManageCode">납품도서관</label>
						<select class="select_180" id="libManageCode" name="libManageCode" title="납품도서관" rules="required" readonly>
							<option value="${spendingInfo.libManageCode}">${spendingInfo.libName}</option>
						</select>
						<label for="fiscalYear">회계연도</label>
						<input type="text" class="input_100" id="fiscalYear" name="fiscalYear" title="회계연도" value="${spendingInfo.fiscalYear}" rules="required">
						<label for="class">구분</label>
						<select class="select_140" id="class" name="class">
							<option value="0" <c:if test="${spendingInfo['class'] eq '0'}">selected='selected'</c:if>>반기별</option>
							<option value="1" <c:if test="${spendingInfo['class'] eq '1'}">selected='selected'</c:if>>분기별</option>
							<option value="2" <c:if test="${spendingInfo['class'] eq '2'}">selected='selected'</c:if>>월별</option>
							<option value="3" <c:if test="${spendingInfo['class'] eq '3'}">selected='selected'</c:if>>기간설정</option>
						</select>
						<label for="deadline">회기말</label>
						<input type="text" class="input_100" id="deadline" name="deadline" title="회기말" value="${spendingInfo.deadline}" rules="required">
						<label for="budget">금액</label>
						<input type="text" class="input_100 price"  id="budget" name="budget" title="금액" value="${spendingInfo.budget}" readonly>
					</section>
					<div class="btn_wrap"></div>
					
					<div class="btn_wrap" style="margin-bottom: 10px;">
						<c:if test="${spendingInfo.fixed eq 'N'}">
							<input type="button" class="btn_add" value="사용 예산 보기" id="btnUsedShow" data-show-used-price="true" style="width:120px;">
							<input type="button" class="btn_add" value="행 추가" id="btnAddRow" <c:if test="${spendingInfo['class'] ne '3'}">style="display:none;"</c:if>>
							<input type="button" class="btn_add" value="일괄 입력" id="btnAllInput">
							<input type="button" class="btn_add" value="반영" id="btnApply">
						</c:if>
					</div>
					
					<!-- 반기별 -->
					<div class="result_list"  id="view0" <c:if test="${spendingInfo['class'] ne '0'}">style="display:none;"</c:if>>
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
								<c:set var="half1Used" value="0" />
								<c:set var="half2Used" value="0" />
								<c:if test="${fn:length(storeList) > 0}">
									<c:forEach var="row" items="${storeList}" varStatus="status">
										<c:set var="rowUsed" value="0" />
										<tr>
											<td>
												${row.storeName}
												<input type="hidden" name="storeId" value="${row.storeId}">
											</td>
											<td>
												<input type="hidden" name="recKey" value="${row.half1Key}">
												<input type="text" class="input_120 price" name="half1" value="${row.half1}">
												<span class="used">
													<fmt:formatNumber value="${row.spendingInfo.half1Used}" pattern="#,###" />
													<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.half1Used}" />
													<c:set var="half1Used" value="${half1Used + row.spendingInfo.half1Used}" />
												</span>
											</td>
											<td>
												<input type="hidden" name="recKey" value="${row.half2Key}">
												<input type="text" class="input_120 price" name="half2" value="${row.half2}">
												<span class="used">
													<fmt:formatNumber value="${row.spendingInfo.half2Used}" pattern="#,###" />
													<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.half2Used}" />
													<c:set var="half2Used" value="${half2Used + row.spendingInfo.half2Used}" />
												</span>
											</td>
											<td>
												<input type="text" class="input_120 price" id="v0_row${status.index}_total" readonly style="font-weight:bold">
												<span class="used " style="font-weight:bold">
													<fmt:formatNumber value="${rowUsed}" pattern="#,###" />
												</span>
											</td>
										</tr>
									</c:forEach>
								</c:if>
							</tbody>
							<tfoot>
								<tr>
									<td>Total</td>
									<td>
										<input type="text" class="input_120 price" id="half1_total" readonly>
										<span class="used" style="font-weight:bold">
											<fmt:formatNumber value="${half1Used}" pattern="#,###" />
										</span>
									</td>
									<td>
										<input type="text" class="input_120 price" id="half2_total" readonly>
										<span class="used" style="font-weight:bold">
											<fmt:formatNumber value="${half2Used}" pattern="#,###" />
										</span>
									</td>
									<td>
										<input type="text" class="input_120 price" id="v0_total" readonly>
										<span class="used" style="font-weight:bold">
											<fmt:formatNumber value="${half1Used + half2Used}" pattern="#,###" />
										</span>
									</td>
								</tr>
							</tfoot>
						</table>
					</div>
					
					<!-- 분기별 -->
					<div class="result_list"  id="view1" <c:if test="${spendingInfo['class'] ne '1' }">style="display:none;"</c:if>>
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
								<c:set var="q1Used" value="0" />
								<c:set var="q2Used" value="0" />
								<c:set var="q3Used" value="0" />
								<c:set var="q4Used" value="0" />
								<c:if test="${fn:length(storeList) > 0}">
									<c:forEach var="row" items="${storeList}" varStatus="status">
										<c:set var="rowUsed" value="0" />
										<tr>
											<td>
												${row.storeName}
												<input type="hidden" name="storeId" value="${row.storeId}">
											</td>
											<td>
												<input type="hidden" name="recKey" value="${row.q1Key}">
												<input type="text" class="input_120 price" name="q1" value="${row.q1}">
												<span class="used">
													<fmt:formatNumber value="${row.spendingInfo.q1Used}" pattern="#,###" />
													<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.q1Used}" />
													<c:set var="q1Used" value="${q1Used + row.spendingInfo.q1Used}" />
												</span>
											</td>
											<td>
												<input type="hidden" name="recKey" value="${row.q2Key}">
												<input type="text" class="input_120 price" name="q2" value="${row.q2}">
												<span class="used">
													<fmt:formatNumber value="${row.spendingInfo.q2Used}" pattern="#,###" />
													<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.q2Used}" />
													<c:set var="q2Used" value="${q2Used + row.spendingInfo.q2Used}" />
												</span>
											</td>
											<td>
												<input type="hidden" name="recKey" value="${row.q3Key}">
												<input type="text" class="input_120 price" name="q3" value="${row.q3}">
												<span class="used">
													<fmt:formatNumber value="${row.spendingInfo.q3Used}" pattern="#,###" />
													<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.q3Used}" />
													<c:set var="q3Used" value="${q3Used + row.spendingInfo.q3Used}" />
												</span>
											</td>
											<td>
												<input type="hidden" name="recKey" value="${row.q4Key}">
												<input type="text" class="input_120 price" name="q4" value="${row.q4}">
												<span class="used">
													<fmt:formatNumber value="${row.spendingInfo.q4Used}" pattern="#,###" />
													<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.q4Used}" />
													<c:set var="q4Used" value="${q4Used + row.spendingInfo.q4Used}" />
												</span>
											</td>
											<td>
												<input type="text" class="input_120 price" id="v1_row${status.index}_total" readonly style="font-weight:bold">
												<span class="used" style="font-weight:bold">
													<fmt:formatNumber value="${rowUsed}" pattern="#,###" />
												</span>
											</td>
										</tr>
									</c:forEach>
								</c:if>
							</tbody>
							<tfoot>
								<tr>
									<td>Total</td>
									<td>
										<input type="text" class="input_120 price" id="q1_total" readonly>
										<span class="used" style="font-weight:bold">
											<fmt:formatNumber value="${q1Used}" pattern="#,###" />
										</span>
									</td>
									<td>
										<input type="text" class="input_120 price" id="q2_total" readonly>
										<span class="used" style="font-weight:bold">
											<fmt:formatNumber value="${q2Used}" pattern="#,###" />
										</span>
									</td>
									<td>
										<input type="text" class="input_120 price" id="q3_total" readonly>
										<span class="used" style="font-weight:bold">
											<fmt:formatNumber value="${q3Used}" pattern="#,###" />
										</span>
									</td>
									<td>
										<input type="text" class="input_120 price" id="q4_total" readonly>
										<span class="used" style="font-weight:bold">
											<fmt:formatNumber value="${q4Used}" pattern="#,###" />
										</span>
									</td>
									<td>
										<input type="text" class="input_120 price" id="v1_total" readonly>
										<span class="used" style="font-weight:bold">
											<fmt:formatNumber value="${q1Used + q2Used + q3Used + q4Used}" pattern="#,###" />
										</span>
									</td>
								</tr>
							</tfoot>
						</table>
					</div>
					
					<!-- 월별 -->
					<div class="result_list"  id="view2" <c:if test="${spendingInfo['class'] ne '2' }">style="display:none;"</c:if>>
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
									<c:set var="month1Used" value="0" />
									<c:set var="month2Used" value="0" />
									<c:set var="month3Used" value="0" />
									<c:set var="month4Used" value="0" />
									<c:set var="month5Used" value="0" />
									<c:set var="month6Used" value="0" />
									<c:set var="month7Used" value="0" />
									<c:set var="month8Used" value="0" />
									<c:set var="month9Used" value="0" />
									<c:set var="month10Used" value="0" />
									<c:set var="month11Used" value="0" />
									<c:set var="month12Used" value="0" />
									<c:if test="${fn:length(storeList) > 0}">
										<c:forEach var="row" items="${storeList}" varStatus="status">
											<c:set var="rowUsed" value="0" />
											<tr>
												<td>
													${row.storeName}
													<input type="hidden" name="storeId" value="${row.storeId}">
												</td>
												<td>
													<input type="hidden" name="recKey" value="${row.month1Key}">
													<input type="text" class="input_80 price" name="month1" value="${row.month1}">
													<span class="used">
														<fmt:formatNumber value="${row.spendingInfo.month1Used}" pattern="#,###" />
														<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.month1Used}" />
														<c:set var="month1Used" value="${month1Used + row.spendingInfo.month1Used}" />
													</span>
												</td>
												<td>
													<input type="hidden" name="recKey" value="${row.month2Key}">
													<input type="text" class="input_80 price" name="month2" value="${row.month2}">
													<span class="used">
														<fmt:formatNumber value="${row.spendingInfo.month2Used}" pattern="#,###" />
														<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.month2Used}" />
														<c:set var="month2Used" value="${month2Used + row.spendingInfo.month2Used}" />
													</span>
												</td>
												<td>
													<input type="hidden" name="recKey" value="${row.month3Key}">
													<input type="text" class="input_80 price" name="month3" value="${row.month3}">
													<span class="used">
														<fmt:formatNumber value="${row.spendingInfo.month3Used}" pattern="#,###" />
														<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.month3Used}" />
														<c:set var="month3Used" value="${month3Used + row.spendingInfo.month3Used}" />
													</span>
												</td>
												<td>
													<input type="hidden" name="recKey" value="${row.month4Key}">
													<input type="text" class="input_80 price" name="month4" value="${row.month4}">
													<span class="used">
														<fmt:formatNumber value="${row.spendingInfo.month4Used}" pattern="#,###" />
														<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.month4Used}" />
														<c:set var="month4Used" value="${month4Used + row.spendingInfo.month4Used}" />
													</span>
												</td>
												<td>
													<input type="hidden" name="recKey" value="${row.month5Key}">
													<input type="text" class="input_80 price" name="month5" value="${row.month5}">
													<span class="used">
														<fmt:formatNumber value="${row.spendingInfo.month5Used}" pattern="#,###" />
														<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.month5Used}" />
														<c:set var="month5Used" value="${month5Used + row.spendingInfo.month5Used}" />
													</span>
												</td>
												<td>
													<input type="hidden" name="recKey" value="${row.month6Key}">
													<input type="text" class="input_80 price" name="month6" value="${row.month6}">
													<span class="used">
														<fmt:formatNumber value="${row.spendingInfo.month6Used}" pattern="#,###" />
														<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.month6Used}" />
														<c:set var="month6Used" value="${month6Used + row.spendingInfo.month6Used}" />
													</span>
												</td>
												<td>
													<input type="hidden" name="recKey" value="${row.month7Key}">
													<input type="text" class="input_80 price" name="month7" value="${row.month7}">
													<span class="used">
														<fmt:formatNumber value="${row.spendingInfo.month7Used}" pattern="#,###" />
														<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.month7Used}" />
														<c:set var="month7Used" value="${month7Used + row.spendingInfo.month7Used}" />
													</span>
												</td>
												<td>
													<input type="hidden" name="recKey" value="${row.month8Key}">
													<input type="text" class="input_80 price" name="month8" value="${row.month8}">
													<span class="used">
														<fmt:formatNumber value="${row.spendingInfo.month8Used}" pattern="#,###" />
														<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.month8Used}" />
														<c:set var="month8Used" value="${month8Used + row.spendingInfo.month8Used}" />
													</span>
												</td>
												<td>
													<input type="hidden" name="recKey" value="${row.month9Key}">
													<input type="text" class="input_80 price" name="month9" value="${row.month9}">
													<span class="used">
														<fmt:formatNumber value="${row.spendingInfo.month9Used}" pattern="#,###" />
														<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.month9Used}" />
														<c:set var="month9Used" value="${month9Used + row.spendingInfo.month9Used}" />
													</span>
												</td>
												<td>
													<input type="hidden" name="recKey" value="${row.month10Key}">
													<input type="text" class="input_80 price" name="month10" value="${row.month10}">
													<span class="used">
														<fmt:formatNumber value="${row.spendingInfo.month10Used}" pattern="#,###" />
														<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.month10Used}" />
														<c:set var="month10Used" value="${month10Used + row.spendingInfo.month10Used}" />
													</span>
												</td>
												<td>
													<input type="hidden" name="recKey" value="${row.month11Key}">
													<input type="text" class="input_80 price" name="month11" value="${row.month11}">
													<span class="used">
														<fmt:formatNumber value="${row.spendingInfo.month11Used}" pattern="#,###" />
														<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.month11Used}" />
														<c:set var="month11Used" value="${month11Used + row.spendingInfo.month11Used}" />
													</span>
												</td>
												<td>
													<input type="hidden" name="recKey" value="${row.month12Key}">
													<input type="text" class="input_80 price" name="month12" value="${row.month12}">
													<span class="used">
														<fmt:formatNumber value="${row.spendingInfo.month12Used}" pattern="#,###" />
														<c:set var="rowUsed" value="${rowUsed + row.spendingInfo.month12Used}" />
														<c:set var="month12Used" value="${month12Used + row.spendingInfo.month12Used}" />
													</span>
												</td>
												<td>
													<input type="text" class="input_80 price" id="v2_row${status.index}_total" readonly style="font-weight:bold">
													<span class="used" style="font-weight:bold">
														<fmt:formatNumber value="${rowUsed}" pattern="#,###" />
													</span>
												</td>
											</tr>
										</c:forEach>
									</c:if>
								</tbody>
								<tfoot>
									<tr>
										<td>Total</td>
										<td>
											<input type="text" class="input_80 price" id="month1_total" readonly>
											<span class="used" style="font-weight:bold">
												<fmt:formatNumber value="${month1Used}" pattern="#,###" />
											</span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="month2_total" readonly>
											<span class="used" style="font-weight:bold">
												<fmt:formatNumber value="${month2Used}" pattern="#,###" />
											</span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="month3_total" readonly>
											<span class="used" style="font-weight:bold">
												<fmt:formatNumber value="${month3Used}" pattern="#,###" />
											</span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="month4_total" readonly>
											<span class="used" style="font-weight:bold">
												<fmt:formatNumber value="${month4Used}" pattern="#,###" />
											</span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="month5_total" readonly>
											<span class="used" style="font-weight:bold">
												<fmt:formatNumber value="${month5Used}" pattern="#,###" />
											</span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="month6_total" readonly>
											<span class="used" style="font-weight:bold">
												<fmt:formatNumber value="${month6Used}" pattern="#,###" />
											</span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="month7_total" readonly>
											<span class="used" style="font-weight:bold">
												<fmt:formatNumber value="${month7Used}" pattern="#,###" />
											</span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="month8_total" readonly>
											<span class="used" style="font-weight:bold">
												<fmt:formatNumber value="${month8Used}" pattern="#,###" />
											</span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="month9_total" readonly>
											<span class="used" style="font-weight:bold">
												<fmt:formatNumber value="${month9Used}" pattern="#,###" />
											</span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="month10_total" readonly>
											<span class="used" style="font-weight:bold">
												<fmt:formatNumber value="${month10Used}" pattern="#,###" />
											</span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="month11_total" readonly>
											<span class="used" style="font-weight:bold">
												<fmt:formatNumber value="${month11Used}" pattern="#,###" />
											</span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="month12_total" readonly>
											<span class="used" style="font-weight:bold">
												<fmt:formatNumber value="${month12Used}" pattern="#,###" />
											</span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="v2_total"  readonly>
											<span class="used" style="font-weight:bold">
												<fmt:formatNumber value="${month1Used + month2Used + month3Used + month4Used + month5Used + month6Used
													+ month7Used + month8Used + month9Used + month10Used + month11Used + month12Used}" pattern="#,###" />
											</span>
										</td>
									</tr>
								</tfoot>
							</table>
						</div>
					</div>
					
					<!-- 기간설정 -->
					<div class="result_list"  id="view3" <c:if test="${spendingInfo['class'] ne '3' }">style="display:none;"</c:if>>
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
								<tbody>
									<c:set var="v3Used" value="0"/>
									<c:if test="${fn:length(spendingList) > 0}">
										<c:forEach var="row" items="${spendingList}" varStatus="status">
											<tr>
												<td>
													<input type="hidden" name="recKey" value="${row.recKey}">
													<select name="storeId" class="select_180">
														<option value="">미선택</option>
														<c:forEach var="store" items="${storeList}">
															<option value="${store.storeId}" <c:if test="${store.storeId eq row.storeId}">selected</c:if>>${store.storeName}</option>
														</c:forEach>
													</select>
												</td>
												<td><input type="text" name="startDate" class="input_120" value="${row.startDate}"></td>
												<td><input type="text" name="endDate" class="input_120" value="${row.endDate}"></td>
												<td>
													<input type="text" name="term" class="input_120 price" value="${row.price}">
													<span class="used">
														<fmt:formatNumber value="${row.usedPrice}" pattern="#,###" />
														<c:set var="v3Used" value="${v3Used + row.usedPrice}"/>
													</span>
												</td>
												<td><input type="button" class="btnDeleteRow"></td>
											</tr>
										</c:forEach>
									</c:if>
								</tbody>
								<tfoot>
									<tr>
										<td>Total</td>
										<td colspan="2"></td>
										<td>
											<input type="text" class="input_80 price" id="v3_total" readonly>
											<span class="used"><fmt:formatNumber value="${v3Used}" pattern="#,###" /></span>
										</td>
										<td></td>
									</tr>
								</tfoot>
							</table>
						</div>
					</div>
				</form>
				<div class="btn_wrap" style="margin-top:20px">
					<c:if test="${spendingInfo.fixed eq 'N'}">
						<input type="button" class="btn_add" value="저장" id="btnModify">
						<input type="button" class="btn_delete" value="삭제" id="btnDelete">
					</c:if>
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
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	
	<script type="text/javascript" src="<c:url value="/resources/library/js/spending/libSpendingModify.js?v=${version}"/>"></script>
</body>
</html>