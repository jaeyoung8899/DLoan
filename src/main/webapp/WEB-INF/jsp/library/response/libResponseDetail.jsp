<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>

<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css?v=${cssver}" />"   type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/03_approver_delivery.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
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
					<label class="first_label" for="resTitle">납품명</label> 
					<input type="text" class="input_415" id="resTitle" name="resTitle" value="${resTitle }" title="납품명" rules="required" disabled="disabled">
					
					
					<label for="selLib">납품도서관</label> 
					<select class="select_140" id="selLib" name="libManageCode"  title="납품도서관" rules="required" disabled="disabled">
						<kaitUi:codeList list="${libList }" select="${libManageCode }" />
					</select> <br> 
						
					<label class="first_label" for="res_Date">납품요청일</label> 
					<input type="text" class="input_140"  id="res_Date" name="res_Date" value="<fmt:formatDate value="${resDate}" pattern="yyyy-MM-dd" />" disabled="disabled">
					
					<label class="ml_69" for ="selStatus">진행상태</label>
					<select class="select_140" id="selStatus" name="resStatus" disabled="disabled">
						<option value="S01" <c:if test="${resStatus eq 'S01'}">selected="selected"</c:if>>납품요청</option>
						<option value="L01" <c:if test="${resStatus eq 'L01'}">selected="selected"</c:if>>납품승인</option>
						<option value="S02" <c:if test="${resStatus eq 'S02'}">selected="selected"</c:if>>납품완료</option>
					</select> 
					<label class="first_label" for="storeName">납품서점</label> 
					<input type="text" class="input_140"  id="storeName" name="storeName" value="${storeName }" disabled="disabled">
					
				</section>
			</form>
		
			<!-- 목록 -->
			<div class="result_list_del">
				<!-- <p class="table_name">납품도서목록</p> -->
				<table class="result_table" id="detailInfo" >
					<colgroup>
						<col width="40px">
						<col width="155px;">
						<col width="150px">
						<col width="150px">
						<col width="60px">
						<col width="70px">
						<col width="80px">
						<col width="125px">
						<col width="75px">
						<col width="145px">
					</colgroup>
					<thead>
						<tr>
							<th>번호</th>
							<th>대출자번호</th>
							<th>도서명</th>
							<th>저자명</th>
							<th>출판사</th>
							<th>출판년도</th>
							<th>정가[가격]</th>
							<th>신청일</th>
							<th>진행상태</th>
							<th><a href="javascript:;" onclick="javascript:libResponseDetail.radioAll('L01');">구입</a>/<a href="javascript:;" onclick="javascript:libResponseDetail.radioAll('L02');">반품</a></th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(resultList) > 0}">
								<c:forEach var="row" items="${resultList}" varStatus="status">
									<tr>
										<input type="hidden" name="recKey" value="${row.recKey }"  />
										<input type="hidden" name="tResKey" value="${row.resKey }"  />
										
										<td>${row.rnum}</td>
										<td>${row.userNo}</td>
										<td style="text-align:left;">${row.title}</td>
										<td>${row.author}</td>
										<td>${row.publisher}</td>
										<td>
											<c:if test="${row.pubDate ne null}">
												${fn:substring(row.pubDate, 0, 4)}
											</c:if>
										</td>
										<td>${row.price}</td>
										<td>${row.reqDate}</td>
										<td>${row.reqStatusName}</td>
										<td>
											<c:choose>
												<c:when test="${resStatus eq 'S01'}">
												
												<c:choose>
													<c:when test="${row.resStatus eq 'L01'}">
														<input type="radio" name="choice_${row.rnum}" id="pur_${row.rnum}" class="radio" value="L01" checked="checked">
														<label class="up" for="pur_${row.rnum}" >구입</label>
														<input type="radio" name="choice_${row.rnum}" id="ret_${row.rnum}" class="radio" value="L02">
														<label class="up" for="ret_${row.rnum}" >반품</label>
													</c:when>
													<c:when test="${row.resStatus eq 'L02'}">
														<input type="radio" name="choice_${row.rnum}" id="pur_${row.rnum}" class="radio"  value="L01">
														<label class="up" for="pur_${row.rnum}" >구입</label>
														<input type="radio" name="choice_${row.rnum}" id="ret_${row.rnum}" class="radio" value="L02" checked="checked">
														<label class="up" for="ret_${row.rnum}" >반품</label>
													</c:when>
													<c:otherwise>
														<input type="radio" name="choice_${row.rnum}" id="pur_${row.rnum}" class="radio"  value="L01" checked="checked">
														<label class="up" for="pur_${row.rnum}" >구입</label>
														<input type="radio" name="choice_${row.rnum}" id="ret_${row.rnum}" class="radio" value="L02" >
														<label class="up" for="ret_${row.rnum}" >반품</label>
													</c:otherwise>
												</c:choose>
												</c:when>
												<c:otherwise>
												<c:choose>
													<c:when test="${row.resStatus eq 'L02'}">
														<span style="color: red;">${row.resStatusName}</span>
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
							</c:when>
							
						</c:choose>
					</tbody>
				</table>
				<div class="btn_wrap" style="margin-bottom:20px">
					<c:if test="${resStatus eq 'S01'}">
						<input type="button" class="btn_del_approve" id="btnSave" value="납품승인">
					</c:if>
					<c:if test="${resStatus eq 'L01'}">
						<input type="button" class="btn_del_approve" id="btnSaveBk" value="납품요청">
					</c:if>
					<input type="button" class="btn_list" id="btnList" value="목록">
				</div>
				<div class="btn_extra">
					<input type="button" class="btn_approve" id="btnExcelDown" value="엑셀다운로드">
					<c:if test="${resStatus eq 'L01' or resStatus eq 'S02'}">
						<input type="button" class="btn_approve" id="btnExcelDown2" value="거래명세서">
						<input type="button" class="btn_approve" id="btnPDFDown2" value="거래명세서 PDF" style="background-color:#f57878">
					</c:if>
				</div>
			</div>
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	
	<script type="text/javascript" src="<c:url value="/resources/library/js/response/libResponseDetail.js?v=${version}"/>"></script>
</body>
</html>