<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<link rel="stylesheet" href="<c:url value="/resources/store/css/00_reset.css?v=${cssver}" />" type="text/css" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/store/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/store/css/03_content.css?v=${cssver}" />" type="text/css" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/store/css/04_popup.css?v=${cssver}" />" type="text/css" title="no title" charset="utf-8"/>
</head>
<body>
	<c:choose>
		<c:when test="${storeYn eq 'Y'}">
			<%@ include file="/WEB-INF/jsp/common/storeHeaderReturn.jsp"%>
		</c:when>
		<c:otherwise>
			<%@ include file="/WEB-INF/jsp/common/storeHeader.jsp"%>
		</c:otherwise>
	</c:choose>

	<div class="content">
		<section class="content_main">
			<img src="<c:url value="/resources/store/images/bookstore-box-subtitle.png" />" alt="" class="h2_square">
			<h2 class="h2">신청거절내역</h2>
			<form class="form-horizontal" name="requestInfoForm" id="requestInfoForm">
				<input type="hidden" id="sortCol" name="sortCol" value="${sortCol }" />
				<input type="hidden" id="order" name="order" value="${order }" />
				<input type="hidden" id="start" name="start" value="${start}" />
				<input type="hidden" name="display" value="10" />
				<section class="search_form_01">
					<div class="search_form_field">
						<label for="selStatus" class="search_form_label">신청상태</label>
						<select class="select_140" id="selStatus" name="reqStatus">
							<option value="">전체</option>
							<option value="S02">서점신청거절</option>
							<option value="L02">도서관신청거절</option>
						</select>
					</div>
					<div class="search_form_field">
						<label for="from_reqDate" class="search_form_label">신청일</label>
						<input type="text" class="input_100 hasDatepicker" id="from_reqDate" name="from_reqDate" title="신청시작일" value="" rules="date">
						-
						<input type="text" class="input_100 hasDatepicker" id="to_reqDate" name="to_reqDate" title="신청종료일" value="" rules="date"><br>
					</div>
					<p></p>
					<div class="search_form_field">
						<label for="title" class="search_form_label">서명</label>
						<input type="text" class="input_200" id="title" name="title" value="">
					</div>
					<div class="search_form_field">
						<label for="author" class="search_form_label">저자</label>
						<input type="text" class="input_160" id="author" name="author" value="">
					</div>
					<div class="search_form_field">
						<label for="publisher" class="search_form_label">출판사</label>
						<input type="text" class="input_160" id="publisher" name="publisher" value="">
					</div>
					<div class="search_form_field">
						<label for="isbn" class="search_form_label">ISBN</label>
						<input type="text" class="input_160" id="isbn" name="isbn" value="">
					</div>
				</section>
				<div class="btn_wrap">
					<input type="button" class="btn_search" value="검색" id="btnSearch">
					<input type="button" class="btn_search"  id="btnClear" value="초기화">
				</div>
			</form>
			<div class="result_list">
				<table class="result_table" id="requestInfo">
					<colgroup>
						<col width="35px">
						<col width="100px">
						<col width="300px">
						<col width="85px">
						<col width="90px">
						<col width="100px">
						<col width="50px">
						<col width="50px">
						<col width="90px">
						<col>
					</colgroup>
					<thead>
						<tr>
							<th>번호</th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeCancel.search);" data-sort="STORE_NAME">신청서점</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeCancel.search);" data-sort="TITLE">도서명</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeCancel.search);" data-sort="AUTHOR">저자</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeCancel.search);" data-sort="PUBLISHER">출판사</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeCancel.search);" data-sort="REQ_STATUS_NAME">진행상태</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeCancel.search);" data-sort="REQ_DATE">신청일</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeCancel.search);" data-sort="CANCEL_DATE">신청<br>거절일</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeCancel.search);" data-sort="CANCEL_USER_NAME">거절기관</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeCancel.search);" data-sort="CANCEL_REASON">거절사유</a></th>
						</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${fn:length(resultList) > 0}">
							<c:forEach var="row" items="${resultList}" varStatus="status">
								<tr>
									<td>${row.rnum}</td>
									<td>${row.storeName}</td>
									<td style="text-align:left;">${row.title}</td>
									<td>${row.author}</td>
									<td>${row.publisher}</td>
									<td>${row.reqStatusName}</td>
									<td><fmt:formatDate value="${row.reqDate}" pattern="yyyy MM-dd"/></td>
									<td><fmt:formatDate value="${row.cancelDate}" pattern="yyyy MM-dd"/></td>
									<td>${row.cancelUserName}</td>
									<td style="text-align:left;">${row.cancelReason}</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="10">조회된 결과가 없습니다.</td>
							</tr>
						</c:otherwise>
					</c:choose>
					</tbody>
				</table>
				<c:if test="${not empty pageInfo}">
				<div style="text-align: center;">
					<kaitUi:paging pageInfo='${pageInfo}' jsFunction="storeCancel.search" />
				</div>
				</c:if>
				<div class="btn_extra">
					<input type="button" class="btn_approve" id="btnWindowPrint" value="인쇄">
					<input type="button" class="btn_sms" id="btnExcelDown" value="엑셀다운로드">
				</div>
			</div>
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/storeFooter.jsp"%>
	<script type="text/javascript" src="<c:url value="/resources/store/js/cancel/storeCancel.js?v=${version}"/>"></script>
</body>
</html>