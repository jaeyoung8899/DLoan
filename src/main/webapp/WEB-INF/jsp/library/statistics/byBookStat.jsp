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
			<%@ include file="/WEB-INF/jsp/library/statistics/statMenu.jsp"%>
			<section class="setting_cont">
				<form class="form-horizontal" name="bookStatForm" id="bookStatForm">
					<input type="hidden"  id="sortCol" name="sortCol" value="${sortCol }" />
					<input type="hidden"  id="order"   name="order"   value="${order }" />
					<input type="hidden"  id="start"   name="start" value="${start }" />
					<section class="search_form_01">
						<label for="selStoreId">신청서점</label>
						<select class="select_140" id="selStoreId" name="reqStoreId" style="width:155px">
						<option value="" <c:if test="${reqStoreId eq ''}">selected="selected"</c:if>>전체</option>
						<c:forEach var="row" items="${storeList}" varStatus="status">
							<c:if test="${row.storeId ne null}">
								<option value="${row.storeId}" <c:if test="${reqStoreId eq row.storeId}">selected="selected"</c:if>>${row.storeName}</option>
							</c:if>
						</c:forEach>
						</select>
						
						<label for="selLibManageCode" class="first_label">배분도서관</label>
						<select class="select_140" id="selLibManageCode" name="reqLibManageCode" style="width:130px">
						<option value="" <c:if test="${reqLibManageCode eq ''}">selected="selected"</c:if>>전체</option>
							<c:forEach var="row" items="${libList}" varStatus="status">
							<c:if test="${row.libManageCode ne null}">
								<option value="${row.libManageCode}" <c:if test="${reqLibManageCode eq row.libManageCode}">selected="selected"</c:if>>${row.libName}</option>
							</c:if>
							</c:forEach>
						</select>
						
						<label for="from_reqDate">신청일</label>
						<input type="text" class="input_120" id="from_reqDate" name="from_reqDate" title="신청시작일" value="${from_reqDate }" rules="date" style="width:90px">
						~
						<input type="text" class="input_120" id="to_reqDate" name="to_reqDate" title="신청종료일" value="${to_reqDate }" rules="date" style="width:90px">
					</section>
					<div class="btn_wrap">
						<input type="button" class="btn_search" value="검색" id="btnSearch">
					</div>
					<div class="btn_wrap_1000">
						
					</div>
					
					
					<table class="result_table" id="userInfo" style="margin-bottom:20px;">
					<colgroup>
						<col width="50px">
						<col width="300px">
						<col width="70px">
					</colgroup>
					<thead>
					<tr>
						<th>번호</th>
						<th><a href="javascript:;" onclick="byBookStat.sortOrder(this, 'TITLE');" data-sort="NAME">서명</a></th>
						<th><a href="javascript:;" onclick="byBookStat.sortOrder(this, 'COUNT');"  data-sort="COUNT">Count</a></th>
					</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(resultList) > 0}">
								<c:set var="count" value="0"/>
								<c:forEach var="row" items="${resultList}" varStatus="status">
									<tr>
										<td>${row.rownum}</td>
										<td style="text-align:left;">${row.title}</td>
										<td>${row.count}권</td>
									</tr>
									<c:set var="count" value="${count+row.count }"/>
								</c:forEach>
								<tr style="font-weight:bold;">
									<td>Total</td>
									<td style="text-align:left;">${fn:length(resultList)}</td>
									<td>${count}권</td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="3">조회된 결과가 없습니다.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
					</table>
				</form>
			</section>
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	<script type="text/javascript" src="<c:url value="/resources/library/js/statistics/byBookStat.js?v=${version }" />"></script>
</body>
</html>