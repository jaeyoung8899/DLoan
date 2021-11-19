<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/04_setting.css" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/05_popup.css" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/libHeader.jsp"%>
	<div class="content">
		<section class="content_main">
			<%@ include file="/WEB-INF/jsp/library/envset/envsetMenu.jsp"%>
			<section class="setting_cont">
				<form class="form-horizontal" name="limitBookForm" id="limitBookForm">
					<input type="hidden"  name="display" value="10" />
					<input type="hidden"  id="sortCol" name="sortCol" value="${sortCol }" />
					<input type="hidden"  id="order"   name="order"   value="${order }" />
					<input type="hidden"  id="start"   name="start" value="${start }" />
					<section class="search_form_01">
						<label for="title">도서명</label>
						<input type="text" class="input_280" id="title" name="title" value="${title }">
						<label for="author">저자</label>
						<input type="text" class="input_120" id="author" name="author" value="${author }">
						<label for="publisher">출판사</label>
						<input type="text" class="input_120" id="publisher" name="publisher" value="${publisher }">
						<label for="isbn">ISBN</label>
						<input type="text" class="input_120" id="isbn" name="isbn" value="${isbn }">
					</section>
					<div class="btn_wrap">
						<input type="button" class="btn_search" value="검색" id="btnSearch">
						<input type="button" class="btn_search" value="초기화" id="btnClear">
					</div>
					<div class="btn_wrap_1000">
						<input type="button" class="btn_add" value="추가" id="btnAdd">
						<input type="button" class="btn_delete" value="삭제" id="btnDel">
					</div>
					<table class="result_table" id="limitBookInfo">
					<colgroup>
						<col width="50px">
						<col width="50px">
						<col width="200px">
						<col width="150px">
						<col width="150px">
						<col width="120px">
						<col width="280px">
					</colgroup>
					<thead>
					<tr>
						<th>번호</th>
						<th>
							<input type="checkbox" id="check_all" class="check" data-name="chkNm" onchange="limitBook.checkedAll(this, 'limitBookInfo');">
							<label for="check_all" class="check_wrap" ></label>
						</th>
						<th><a href="javascript:;" onclick="limitBook.sortOrder(this, 'TITLE');" data-sort="TITLE">도서명</a></th>
						<th><a href="javascript:;" onclick="limitBook.sortOrder(this, 'AUTHOR');"  data-sort="AUTHOR">저자</a></th>
						<th><a href="javascript:;" onclick="limitBook.sortOrder(this, 'PUBLISHER');"  data-sort="PUBLISHER">출판사</a></th>
						<th><a href="javascript:;" onclick="limitBook.sortOrder(this, 'ISBN');"  data-sort="ISBN">ISBN</a></th>
						<th><a href="javascript:;" onclick="limitBook.sortOrder(this, 'LIMIT_REASON');"  data-sort="LIMIT_REASON">제한사유</a></th>
					</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(resultList) > 0}">
								<c:forEach var="row" items="${resultList}" varStatus="status">
									<tr>
										<td>${status.count}</td>
										<td>
											<input type="checkbox" name="chkNm" class="check" id="check_${row.rnum}" value="${row.reqStatus }">
											<label for="check_${row.rnum}" class="check_wrap"></label>
										</td>
										<td style="text-align:left;">${row.title}</td>
										<td>${row.author}</td>
										<td>${row.publisher}</td>
										<td>${row.isbn}</td>
										<td>${row.limitReason}</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="7">조회된 결과가 없습니다.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
					</table>
					<c:if test="${not empty pageInfo}">
					<div style="text-align: center;">
						<kaitUi:paging pageInfo='${pageInfo}'  jsFunction="limitBook.search" />
					</div>
					</c:if>
				</form>
			</section>
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	<!-- 추가 레이어 -->
	<div class="back_black" id="limitBookInput" style="display:none">
		<div class="setting_popup_wrap">
			<form class="" name="saveLimitBook" id="saveLimitBook">
				<div class="popup_form_02">
					<label for="in_title" style="width:60px">도서명</label>
					<input type="text" class="input_280" id="in_title" title="도서명" rules="required"><br>
					<label for="in_author" style="width:60px">저자</label>
					<input type="text" class="input_280" id="in_author" title="저자" rules="required"><br>
					<label for="in_publisher" style="width:60px">출판사</label>
					<input type="text" class="input_280" id="in_publisher" title="출판사" rules="required"><br>
					<label for="in_isbn" style="width:60px">ISBN</label>
					<input type="text" class="input_280" id="in_isbn" title="ISBN" rules="required"><br>
					<label for="in_limitReason" style="width:60px">제한사유</label>
					<input type="text" class="input_280" id="in_limitReason" title="제한사유" rules="required"><br>
					<input type="button" class="btn_input" value="입력" id="btnInsert">
				</div>
				<input type="button" class="btn_pop_close" id="btnModalClose">
			</form>
		</div>
	</div>
	<script type="text/javascript" src="<c:url value="/resources/library/js/envset/limitBook.js?v=${version }" />"></script>
</body>
</html>