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
	<%@ include file="/WEB-INF/jsp/common/storeHeader.jsp"%>
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
					<%--
					<label for="selStoreId" class="first_label">신청서점</label>
					<select class="select_120" id="selStoreId" name="reqStoreId">
						<option value="" <c:if test="${reqStoreId eq ''}">selected="selected"</c:if>>전체</option>
						<c:forEach var="row" items="${storeList}" varStatus="status">
							<c:if test="${row.storeId ne null}">
								<option value="${row.storeId}" <c:if test="${reqStoreId eq row.storeId}">selected="selected"</c:if>>${row.storeName}</option>
							</c:if>
						</c:forEach>
					</select>
					<label for="selLibManageCode" style="margin-left:20px;">배분도서관</label>
					<select class="select_120" id="selLibManageCode" name="reqLibManageCode">
						<option value="" <c:if test="${reqLibManageCode eq ''}">selected="selected"</c:if>>전체</option>
						<c:forEach var="row" items="${libList}" varStatus="status">
							<c:if test="${row.libManageCode ne null}">
								<option value="${row.libManageCode}" <c:if test="${reqLibManageCode eq row.libManageCode}">selected="selected"</c:if>>${row.libName}</option>
							</c:if>
						</c:forEach>
					</select>
					 --%>
					<label for="selStatus" class="first_label">신청상태</label>
					<select class="select_140" id="selStatus" name="reqStatus">
						<option value="" <c:if test="${reqStatus eq ''}">selected="selected"</c:if>>전체</option>
						<option value="S02" <c:if test="${reqStatus eq 'S02'}">selected="selected"</c:if>>서점신청거절</option>
						<option value="L02" <c:if test="${reqStatus eq 'L02'}">selected="selected"</c:if>>도서관신청거절</option>
					</select>
					<label for="from_reqDate" style="margin-left:20px;">신청일</label>
					<input type="text" class="input_120" id="from_reqDate" name="from_reqDate" title="신청시작일" value="${from_reqDate }" rules="date">
					-
					<input type="text" class="input_120" id="to_reqDate" name="to_reqDate" title="신청종료일" value="${to_reqDate }" rules="date"><br>
					<label for="title" class="first_label">서명</label>
					<input type="text" class="input_200" id="title" name="title" value="${title}">
					<label for="author">저자</label>
					<input type="text" class="input_120" id="author" name="author" value="${author }">
					<label for="publisher">출판사</label>
					<input type="text" class="input_120" id="publisher" name="publisher"  value="${publisher }">
					<label for="isbn">ISBN</label>
					<input type="text" class="input_160" id="isbn" name="isbn" value="${isbn}">
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
						<col width="75px">
						<col width="200px">
						<col width="85px">
						<col width="90px">
						<col width="100px">
						<col width="50px">
						<col width="50px">
						<col width="80px">
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