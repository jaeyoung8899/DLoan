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
	input.price[id$='_total']{border:0;background-color:inherit;}
	.used{display:none;}
	.result_list_wrapper {width:100%;overflow-x:auto;}
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
						</select>
						<label for="deadline">회기말</label>
						<input type="text" class="input_100" id="deadline" name="deadline" title="회기말" value="${spendingInfo.deadline}" rules="required">
						<label for="budget">금액</label>
						<input type="text" class="input_100 price"  id="budget" name="budget" title="금액" value="${spendingInfo.budget}" readonly>
					</section>
					<div class="btn_wrap"></div>
					
					<div class="btn_wrap" style="margin-bottom: 10px;">
						<c:if test="${spendingInfo.fixed eq 'N'}">
							<input type="button" class="btn_add" value="사용 예산 보기" id="btnUsedShow" style="width:120px;">
							<input type="button" class="btn_add" value="일괄 입력" id="btnAllInput">
							<input type="button" class="btn_add" value="반영" id="btnApply">
						</c:if>
					</div>
					<!-- 월별 -->
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
								<c:set var="half1_used_total" value="0"/>
								<c:set var="half2_used_total" value="0"/>
								<c:if test="${fn:length(storeList) > 0}">
									<c:forEach var="row" items="${storeList}" varStatus="status">
										<tr>
											<td>
												${row.storeName}
												<input type="hidden" name="recKey" value="${row.recKey}">
												<input type="hidden" name="storeId" value="${row.storeId}">
											</td>
											<td>
												<input type="text" class="input_120 price" name="half1"   value="${row.jan}">
												<span class="used"><fmt:formatNumber value="${row.janUsed + row.febUsed + row.marUsed + row.aprUsed + row.mayUsed + row.junUsed}" pattern="#,###" /></span>
											</td>
											<td>
												<input type="text" class="input_120 price" name="half2"   value="${row.jul}">
												<span class="used"><fmt:formatNumber value="${row.julUsed + row.augUsed + row.sepUsed + row.octUsed + row.novUsed + row.decUsed}" pattern="#,###" /></span>
											</td>
											<td>
												<input type="text" class="input_120 price" id="v0_row${status.index}_total" readonly style="font-weight:bold">
												<span class="used" style="font-weight:bold"><fmt:formatNumber value="${row.totalUsed}" pattern="#,###" /></span>
											</td>
										</tr>
										<c:set var="half1_used_total" value="${half1_used_total + row.janUsed + row.febUsed + row.marUsed + row.aprUsed + row.mayUsed + row.junUsed}"/>
										<c:set var="half2_used_total" value="${half2_used_total + row.julUsed + row.augUsed + row.sepUsed + row.octUsed + row.novUsed + row.decUsed}"/>
									</c:forEach>
								</c:if>
							</tbody>
							<tfoot>
								<tr>
									<td>Total</td>
									<td>
										<input type="text" class="input_120 price" id="half1_total" readonly>
										<span class="used" style="font-weight:bold"><fmt:formatNumber value="${half1_used_total}" pattern="#,###" /></span>
									</td>
									<td>
										<input type="text" class="input_120 price" id="half2_total" readonly>
										<span class="used" style="font-weight:bold"><fmt:formatNumber value="${half2_used_total}" pattern="#,###" /></span>
									</td>
									<td>
										<input type="text" class="input_120 price" id="v0_total" readonly>
										<span class="used" style="font-weight:bold"><fmt:formatNumber value="${half1_used_total + half2_used_total}" pattern="#,###" /></span>
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
								<c:set var="q1_used_total" value="0"/>
								<c:set var="q2_used_total" value="0"/>
								<c:set var="q3_used_total" value="0"/>
								<c:set var="q4_used_total" value="0"/>
								<c:if test="${fn:length(storeList) > 0}">
									<c:forEach var="row" items="${storeList}" varStatus="status">
										<tr>
											<td>
												${row.storeName}
												<input type="hidden" name="recKey" value="${row.recKey}">
												<input type="hidden" name="storeId" value="${row.storeId}">
											</td>
											<td>
												<input type="text" class="input_120 price" name="q1" value="${row.jan}">
												<span class="used"><fmt:formatNumber value="${row.janUsed + row.febUsed + row.marUsed}" pattern="#,###" /></span>
											</td>
											<td>
												<input type="text" class="input_120 price" name="q2" value="${row.apr}">
												<span class="used"><fmt:formatNumber value="${row.aprUsed + row.mayUsed + row.junUsed}" pattern="#,###" /></span>
											</td>
											<td>
												<input type="text" class="input_120 price" name="q3" value="${row.jul}">
												<span class="used"><fmt:formatNumber value="${row.julUsed + row.augUsed + row.sepUsed}" pattern="#,###" /></span>
											</td>
											<td>
												<input type="text" class="input_120 price" name="q4" value="${row.oct}">
												<span class="used"><fmt:formatNumber value="${row.octUsed + row.novUsed + row.decUsed}" pattern="#,###" /></span>
											</td>
											<td>
												<input type="text" class="input_120 price" id="v1_row${status.index}_total" readonly style="font-weight:bold">
												<span class="used" style="font-weight:bold"><fmt:formatNumber value="${row.totalUsed}" pattern="#,###" /></span>
											</td>
										</tr>
										<c:set var="q1_used_total" value="${q1_used_total + row.janUsed + row.febUsed + row.marUsed}"/>
										<c:set var="q2_used_total" value="${q2_used_total + row.aprUsed + row.mayUsed + row.junUsed}"/>
										<c:set var="q3_used_total" value="${q3_used_total + row.julUsed + row.augUsed + row.sepUsed}"/>
										<c:set var="q4_used_total" value="${q4_used_total + row.octUsed + row.novUsed + row.decUsed}"/>
									</c:forEach>
								</c:if>
							</tbody>
							<tfoot>
								<tr>
									<td>Total</td>
									<td>
										<input type="text" class="input_120 price" id="q1_total" readonly>
										<span class="used" style="font-weight:bold"><fmt:formatNumber value="${q1_used_total}" pattern="#,###" /></span>
									</td>
									<td>
										<input type="text" class="input_120 price" id="q2_total" readonly>
										<span class="used" style="font-weight:bold"><fmt:formatNumber value="${q2_used_total}" pattern="#,###" /></span>
									</td>
									<td>
										<input type="text" class="input_120 price" id="q3_total" readonly>
										<span class="used" style="font-weight:bold"><fmt:formatNumber value="${q3_used_total}" pattern="#,###" /></span>
									</td>
									<td>
										<input type="text" class="input_120 price" id="q4_total" readonly>
										<span class="used" style="font-weight:bold"><fmt:formatNumber value="${q4_used_total}" pattern="#,###" /></span>
									</td>
									<td>
										<input type="text" class="input_120 price" id="v1_total" readonly>
										<span class="used" style="font-weight:bold"><fmt:formatNumber value="${q1_used_total + q2_used_total + q3_used_total + q4_used_total}" pattern="#,###" /></span>
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
									<c:set var="jan_used_total" value="0"/>
									<c:set var="feb_used_total" value="0"/>
									<c:set var="mar_used_total" value="0"/>
									<c:set var="apr_used_total" value="0"/>
									<c:set var="may_used_total" value="0"/>
									<c:set var="jun_used_total" value="0"/>
									<c:set var="jul_used_total" value="0"/>
									<c:set var="aug_used_total" value="0"/>
									<c:set var="sep_used_total" value="0"/>
									<c:set var="oct_used_total" value="0"/>
									<c:set var="nov_used_total" value="0"/>
									<c:set var="dec_used_total" value="0"/>
									<c:if test="${fn:length(storeList) > 0}">
										<c:forEach var="row" items="${storeList}" varStatus="status">
											<tr>
												<td>
													${row.storeName}
													<input type="hidden" name="recKey" value="${row.recKey}">
													<input type="hidden" name="storeId" value="${row.storeId}">
												</td>
												<td>
													<input type="text" class="input_80 price" name="jan" value="${row.jan}">
													<span class="used"><fmt:formatNumber value="${row.janUsed}" pattern="#,###" /></span>
												</td>
												<td>
													<input type="text" class="input_80 price" name="feb" value="${row.feb}">
													<span class="used"><fmt:formatNumber value="${row.febUsed}" pattern="#,###" /></span>
												</td>
												<td>
													<input type="text" class="input_80 price" name="mar" value="${row.mar}">
													<span class="used"><fmt:formatNumber value="${row.marUsed}" pattern="#,###" /></span>
												</td>
												<td>
													<input type="text" class="input_80 price" name="apr" value="${row.apr}">
													<span class="used"><fmt:formatNumber value="${row.aprUsed}" pattern="#,###" /></span>
												</td>
												<td>
													<input type="text" class="input_80 price" name="may" value="${row.may}">
													<span class="used"><fmt:formatNumber value="${row.mayUsed}" pattern="#,###" /></span>
												</td>
												<td>
													<input type="text" class="input_80 price" name="jun" value="${row.jun}">
													<span class="used"><fmt:formatNumber value="${row.junUsed}" pattern="#,###" /></span>
												</td>
												<td>
													<input type="text" class="input_80 price" name="jul" value="${row.jul}">
													<span class="used"><fmt:formatNumber value="${row.julUsed}" pattern="#,###" /></span>
												</td>
												<td>
													<input type="text" class="input_80 price" name="aug" value="${row.aug}">
													<span class="used"><fmt:formatNumber value="${row.augUsed}" pattern="#,###" /></span>
												</td>
												<td>
													<input type="text" class="input_80 price" name="sep" value="${row.sep}">
													<span class="used"><fmt:formatNumber value="${row.sepUsed}" pattern="#,###" /></span>
												</td>
												<td>
													<input type="text" class="input_80 price" name="oct" value="${row.oct}">
													<span class="used"><fmt:formatNumber value="${row.octUsed}" pattern="#,###" /></span>
												</td>
												<td>
													<input type="text" class="input_80 price" name="nov" value="${row.nov}">
													<span class="used"><fmt:formatNumber value="${row.novUsed}" pattern="#,###" /></span>
												</td>
												<td>
													<input type="text" class="input_80 price" name="dec" value="${row.dec}">
													<span class="used"><fmt:formatNumber value="${row.decUsed}" pattern="#,###" /></span>
												</td>
												<td>
													<input type="text" class="input_80 price" id="v2_row${status.index}_total" readonly style="font-weight:bold">
													<span class="used" style="font-weight:bold"><fmt:formatNumber value="${row.totalUsed}" pattern="#,###" /></span>
												</td>
											</tr>
											<c:set var="jan_used_total" value="${jan_used_total + row.janUsed}"/>
											<c:set var="feb_used_total" value="${feb_used_total + row.febUsed}"/>
											<c:set var="mar_used_total" value="${mar_used_total + row.marUsed}"/>
											<c:set var="apr_used_total" value="${apr_used_total + row.aprUsed}"/>
											<c:set var="may_used_total" value="${may_used_total + row.mayUsed}"/>
											<c:set var="jun_used_total" value="${jun_used_total + row.junUsed}"/>
											<c:set var="jul_used_total" value="${jul_used_total + row.julUsed}"/>
											<c:set var="aug_used_total" value="${aug_used_total + row.augUsed}"/>
											<c:set var="sep_used_total" value="${sep_used_total + row.sepUsed}"/>
											<c:set var="oct_used_total" value="${oct_used_total + row.octUsed}"/>
											<c:set var="nov_used_total" value="${nov_used_total + row.novUsed}"/>
											<c:set var="dec_used_total" value="${dec_used_total + row.decUsed}"/>
										</c:forEach>
									</c:if>
								</tbody>
								<tfoot>
									<tr>
										<td>Total</td>
										<td>
											<input type="text" class="input_80 price" id="jan_total" readonly>
											<span class="used" style="font-weight:bold"><fmt:formatNumber value="${jan_used_total}" pattern="#,###" /></span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="feb_total" readonly>
											<span class="used" style="font-weight:bold"><fmt:formatNumber value="${feb_used_total}" pattern="#,###" /></span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="mar_total" readonly>
											<span class="used" style="font-weight:bold"><fmt:formatNumber value="${mar_used_total}" pattern="#,###" /></span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="apr_total" readonly>
											<span class="used" style="font-weight:bold"><fmt:formatNumber value="${apr_used_total}" pattern="#,###" /></span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="may_total" readonly>
											<span class="used" style="font-weight:bold"><fmt:formatNumber value="${may_used_total}" pattern="#,###" /></span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="jun_total" readonly>
											<span class="used" style="font-weight:bold"><fmt:formatNumber value="${jun_used_total}" pattern="#,###" /></span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="jul_total" readonly>
											<span class="used" style="font-weight:bold"><fmt:formatNumber value="${jul_used_total}" pattern="#,###" /></span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="aug_total" readonly>
											<span class="used" style="font-weight:bold"><fmt:formatNumber value="${aug_used_total}" pattern="#,###" /></span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="sep_total" readonly>
											<span class="used" style="font-weight:bold"><fmt:formatNumber value="${sep_used_total}" pattern="#,###" /></span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="oct_total" readonly>
											<span class="used" style="font-weight:bold"><fmt:formatNumber value="${oct_used_total}" pattern="#,###" /></span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="nov_total" readonly>
											<span class="used" style="font-weight:bold"><fmt:formatNumber value="${nov_used_total}" pattern="#,###" /></span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="dec_total" readonly>
											<span class="used" style="font-weight:bold"><fmt:formatNumber value="${dec_used_total}" pattern="#,###" /></span>
										</td>
										<td>
											<input type="text" class="input_80 price" id="v2_total"  readonly>
											<span class="used" style="font-weight:bold">
												<fmt:formatNumber value="${jan_used_total + feb_used_total + mar_used_total + apr_used_total
																			+ may_used_total + jun_used_total + jul_used_total + aug_used_total
																			+ sep_used_total + oct_used_total + nov_used_total + dec_used_total}" pattern="#,###" />
											</span>
										</td>
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