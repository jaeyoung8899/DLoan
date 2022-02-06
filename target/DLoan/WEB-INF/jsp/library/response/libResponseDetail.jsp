<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>

<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css?v=${cssver}" />"   type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/03_approver_delivery.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
	<link rel="stylesheet" href="<c:url value="/resources/library/css/05_popup.css?v=${cssver}" />" type="text/css" title="no title" charset="utf-8"/>
</head>
<body>

	<%@ include file="/WEB-INF/jsp/common/libHeader.jsp"%>

	<div class="content">
		<section class="content_main">
			<img src="<c:url value="/resources/library/images/library-box-subtitle.png" />" alt="" class="h2_square">
			<h2 class="h2">납품도서목록</h2>
			<!-- 검색 조건 -->
			<form class="form-horizontal" name="searchInfoForm" id="searchInfoForm">
				<input type="hidden"  id="resKey"  name="resKey" value="${resKey }" />
				<section class="search_form_01">
					<div class="search_form_field" style="width:48.3%">
						<label for="resTitle" class="search_form_label">납품명</label>
						<input type="text" class="input_350" id="resTitle" name="resTitle" value="${resTitle }" title="납품명" rules="required" disabled="disabled">
					</div>
					<div class="search_form_field">
						<label for="selLib" class="search_form_label">납품도서관</label>
						<select class="select_180" id="selLib" name="libManageCode"  title="납품도서관" rules="required" disabled="disabled">
							<kaitUi:codeList list="${libList }" select="${libManageCode }" />
						</select>
					</div>
					<p></p>
					<div class="search_form_field">
						<label for="res_Date" class="search_form_label">납품요청일</label>
						<input type="text" class="input_140"  id="res_Date" name="res_Date" value="<fmt:formatDate value="${resDate}" pattern="yyyy-MM-dd" />" disabled="disabled">
					</div>
					<div class="search_form_field">
						<label for ="selStatus" class="search_form_label">진행상태</label>
						<select class="select_140" id="selStatus" name="resStatus" disabled="disabled">
							<option value="S01" <c:if test="${resStatus eq 'S01'}">selected="selected"</c:if>>납품요청</option>
							<option value="L01" <c:if test="${resStatus eq 'L01'}">selected="selected"</c:if>>납품승인</option>
							<option value="S02" <c:if test="${resStatus eq 'S02'}">selected="selected"</c:if>>납품완료</option>
						</select>
					</div>
					<div class="search_form_field">
						<label for="storeName" class="search_form_label">납품서점</label>
						<input type="text" class="input_140"  id="storeName" name="storeName" value="${storeName }" disabled="disabled">
					</div>
				</section>
			</form>
		
			<!-- 목록 -->
			<div class="result_list_del">
				<div class="result_list_wrapper">
					<table class="result_table" id="detailInfo" style="min-width:1000px">
						<colgroup>
							<col width="40px">
							<col width="120px">
							<col width="80px">
							<col >
							<col width="150px">
							<col width="130px">
							<col width="70px">
							<col width="70px">
							<col width="70px">
							<col width="80px">
							<col width="90px">
							<col width="150px">
						</colgroup>
						<thead>
						<tr>
							<th>번호</th>
							<th>대출자번호</th>
							<th>이름</th>
							<th>도서명</th>
							<th>저자명</th>
							<th>출판사</th>
							<th>출판년도</th>
							<th>정가[가격]</th>
							<th>납품금액</th>
							<th>신청일</th>
							<th>진행상태</th>
							<th><a href="javascript:;" onclick="javascript:libResponseDetail.radioAll('L01');">구입</a>/<a href="javascript:;" onclick="javascript:libResponseDetail.radioAll('L02');">반품</a></th>
						</tr>
						</thead>
						<tbody>
						<c:choose>
							<c:when test="${fn:length(resultList) > 0}">
								<!-- 2020.07.29합계 -->
								<c:set var="price_sum" value ="0"/>

								<c:forEach var="row" items="${resultList}" varStatus="status">

									<c:if test="${row.resStatus eq 'L01' && resStatus ne 'S01'}">
										<c:set var="price_sum" value ="${price_sum + row.realPrice}"/>
									</c:if>
									<tr>
										<input type="hidden" name="recKey" value="${row.recKey }"  />
										<input type="hidden" name="tResKey" value="${row.resKey }"  />
										<input type="hidden" name ="returnReason" value ="${row.resRemark}">
										<td>${row.rnum}</td>
										<td>${row.userNo}</td>
										<td>${row.userName}</td>
										<td style="text-align:left;" id = "tbTitle_${row.rnum}">${row.title}</td>
										<td>${row.author}</td>
										<td>${row.publisher}</td>
										<td>
											<c:if test="${row.pubDate ne null}">
												${fn:substring(row.pubDate, 0, 4)}
											</c:if>
										</td>
										<td>${row.price}</td>
										<td>${row.realPrice}</td>
										<td>${row.reqDate}</td>
										<td>${row.reqStatusName}</td>
										<td>
											<c:choose>
												<c:when test="${resStatus eq 'S01'}">
													<c:choose>
														<c:when test="${row.resStatus eq 'L01'}">
															<input type="radio" name="choice_${row.rnum}" id="pur_${row.rnum}" class="radio" value="L01" checked="checked" onclick="javascript:libResponseDetail.purClick('${row.rnum}')">
															<label class="up" for="pur_${row.rnum}" >구입</label>
															<input type="radio" name="choice_${row.rnum}" id="ret_${row.rnum}" class="radio" value="L02" onclick="javascript:libResponseDetail.retClick('${row.rnum}')">
															<label class="up" for="ret_${row.rnum}" >반품</label>
														</c:when>
														<c:when test="${row.resStatus eq 'L02'}">
															<input type="radio" name="choice_${row.rnum}" id="pur_${row.rnum}" class="radio"  value="L01" onclick="javascript:libResponseDetail.purClick('${row.rnum}')">
															<label class="up" for="pur_${row.rnum}" >구입</label>
															<input type="radio" name="choice_${row.rnum}" id="ret_${row.rnum}" class="radio" value="L02" checked="checked" onclick="javascript:libResponseDetail.retClick('${row.rnum}')">
															<label class="up" for="ret_${row.rnum}" >반품</label>
														</c:when>
														<c:otherwise>
															<input type="radio" name="choice_${row.rnum}" id="pur_${row.rnum}" class="radio"  value="L01" checked="checked" onclick="javascript:libResponseDetail.purClick('${row.rnum}')">
															<label class="up" for="pur_${row.rnum}" >구입</label>
															<input type="radio" name="choice_${row.rnum}" id="ret_${row.rnum}" class="radio" value="L02" onclick="javascript:libResponseDetail.retClick('${row.rnum}')">
															<label class="up" for="ret_${row.rnum}" >반품</label>
														</c:otherwise>
													</c:choose>
													<input type="hidden" id ="prevRadioValue_${row.rnum}" value ="${row.resStatus}">
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${row.resStatus eq 'L02'}">
															<%-- <span style="color: red;">${row.resStatusName}</span> --%>
															<span style="color: red;cursor:pointer" onclick="javascript:libResponseDetail.retPopup('${row.rnum}')">${row.resStatusName}</span>
														</c:when>
														<c:otherwise>
															${row.resStatusName}
														</c:otherwise>
													</c:choose>
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</c:forEach>
								<c:if test="${resStatus ne 'S01'}">
									<tr style="font-weight: bold;">
										<td colspan="10" >* 구입자료만 합산됩니다.</td>
										<td>가격합계</td>
										<td class="font_gulim">\<fmt:formatNumber value="${price_sum}" pattern="###,###,###"/></td>
									</tr>
								</c:if>
							</c:when>
						</c:choose>
						</tbody>
					</table>
				</div>
				<div class="btn_extra">
					<!-- <input type="button" class="btn_approve" id="btnPDFDown" value="PDF다운로드"> -->
					<input type="button" class="btn_approve" id="btnExcelDown" value="엑셀다운로드">
					<c:if test="${resStatus eq 'L01' or resStatus eq 'S02'}">
						<div id="pdfYn" style="display: none;">
							<input type="button" class="btn_approve" id="btnExcelDown2" value="거래명세서">
							<input type="button" class="btn_approve" id="btnPDFDown2" value="거래명세서 PDF" style="background-color:#f57878">
						</div>
					</c:if>
				</div>
			</div>
			<div class="btn_wrap">
				<c:if test="${resStatus eq 'S01'}">
					<input type="button" class="btn_del_approve" id="btnSave" value="납품승인">
				</c:if>
				<c:if test="${resStatus eq 'L01'}">
					<input type="button" class="btn_del_approve" id="btnSaveBk" value="납품요청">
				</c:if>
				<!--
				<c:if test="${resStatus ne 'S01'}">
					<input type="button" class="btn_del_approve" id="btnStatement1" value="대금명세서">
					<input type="button" class="btn_del_approve" id="btnStatement2" value="거래명세서">
				</c:if>
				 -->
				<input type="button" class="btn_list" id="btnList" value="목록">
			</div>
		</section>
	</div>

	<!-- 반품사유 팝업 -->
	<div class="back_black" id="returnReasonPop" style="display:none">
		<div class="approver_popup_wrap" style="width:570px; height:310px;">
			<form class="" name="returnReasonForm" id="returnReasonForm">
				<div class="popup_form_01" style ="margin-top: 10px;">
					<label class="up" id="txtBookTitle"> </label>
					<textarea class="textarea_520" id="txtReturnReason" rules="required" ></textarea>
					<div style ="text-align:right">
						<c:choose>
							<c:when test="${resStatus eq 'S01'}">
								<input type="button" class="btn_search" value="확인" id="btnApply">
								<input type="button" class="btn_del_approve" value="취소" id="btnCancel">
							</c:when>
							<c:otherwise>
								<input type="button" class="btn_del_approve" value="닫기" id="btnClose">
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<input type="button" class="btn_pop_close" id="btnModalClose">
			</form>
		</div>
	</div>
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	
	<script type="text/javascript" src="<c:url value="/resources/library/js/response/libResponseDetail.js?v=${version}"/>"></script>
</body>
</html>