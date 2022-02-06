<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/03_approver_delivery.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/libHeader.jsp"%>
	<div class="content">
		<section class="content_main">
			<img src="<c:url value="/resources/library/images/library-box-subtitle.png" />" alt="" class="h2_square">
			<h2 class="h2">SMS이력조회</h2>
			<form class="form-horizontal" name="requestInfoForm" id="requestInfoForm">
				<input type="hidden"  id="sortCol" name="sortCol" value="${sortCol }" />
				<input type="hidden"  id="order" name="order"   value="${order }" />
				<input type="hidden"  id="start"  name="start" value="${start}" />
				<input type="hidden"  name="display" value="10" />
				<section class="search_form_01">
					<div class="search_form_field">
						<label for="selLibManageCode" class="search_form_label">배분도서관</label>
						<select class="select_140" id="selLibManageCode" name="selLibManageCode" style="width:130px">
							<c:if test="${sessionScope.LIB_MNG_CD eq null}">
								<option value="" <c:if test="${selLibManageCode eq null}">selected="selected"</c:if>>전체</option>
							</c:if>
							<c:forEach var="row" items="${libList}" varStatus="status">
								<c:if test="${row.code ne null}">
									<option value="${row.code}" <c:if test="${selLibManageCode eq row.code}">selected="selected"</c:if>>${row.value}</option>
								</c:if>
							</c:forEach>
						</select>
					</div>
					<div class="search_form_field">
						<label for="from_reqDate" class="search_form_label">발송일</label>
						<input type="text" class="input_100" id="from_reqDate" name="from_reqDate" title="발송시작일" value="${from_reqDate }" rules="date">
						~
						<input type="text" class="input_100" id="to_reqDate" name="to_reqDate" title="발송종료일" value="${to_reqDate }" rules="date">
					</div>
					<p/>
					<div class="search_form_field">
						<label for="userNo" class="search_form_label">대출자번호</label>
						<input type="text" class="input_140" id="userNo" name="userNo" value="${userNo }">
					</div>
					<div class="search_form_field">
						<label for="title" class="search_form_label">이용자명</label>
						<input type="text" class="input_140" id="userName" name="userName" value="${userName}">
					</div>
					<div class="search_form_field">
						<label for="author" class="search_form_label">휴대폰번호</label>
						<input type="text" class="input_140" id="sendPhone" name="sendPhone" value="${sendPhone}">
					</div>
					<p/>
					<div class="search_form_field" style="min-width:50%">
						<label for="sendMessage" class="search_form_label">문자내용</label>
						<input type="text" class="input_140" id="sendMessage" name="sendMessage" value="${sendMessage}" style="width:300px">
					</div>
				</section>
				<div class="btn_wrap">
					<input type="button" class="btn_search" value="검색" id="btnSearch">
					<input type="button" class="btn_search" value="초기화" id="btnClear">
				</div>
			</form>
			<div class="result_list">
					<table class="result_table" id="requestInfo" style="min-width:1000px;">
						<colgroup>
							<col width="50px">
							<col width="80px">
							<col width="100px">
							<col width="120px">
							<col width="490px">
							<col width="170px">
							<col width="90px">
						</colgroup>
						<thead>
							<tr>
								<th>번호</th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, sms.search);" data-sort="USER_NAME">수신자명</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, sms.search);" data-sort="USER_NO">대출자번호</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, sms.search);" data-sort="SEND_PHONE">수신번호</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, sms.search);" data-sort="MSG">문자내역</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, sms.search);" data-sort="SEND_TIME">전송시간</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, sms.search);" data-sort="SUCCESS_YN">전송결과</a></th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${fn:length(resultList) > 0}">
									<c:forEach var="row" items="${resultList}" varStatus="status">
										<tr>
											<td>${row.rnum}</td>
											<td>${row.name}</td>
											<td>${row.userNo}</td>
											<td>${row.sendPhone}</td>
											<td style="text-align:left;">${row.sendMessage}</td>
											<td>${row.sendTime}</td>
											<td>${row.sendSuccessYn}</td>
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
				</div>
				<c:if test="${not empty pageInfo}">
					<div style="text-align: center;">
						<kaitUi:paging pageInfo='${pageInfo}' jsFunction="sms.search" />
					</div>
				</c:if>
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	<script type="text/javascript" src="<c:url value="/resources/library/js/sms/sms.js?v=${version }" />"></script>
</body>
</html>