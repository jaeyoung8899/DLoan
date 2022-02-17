<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/04_setting.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<style type="text/css">
.ui-state-default { border : 0 }
</style>
</head>
<body>
<body>
	<%@ include file="/WEB-INF/jsp/common/libHeader.jsp"%>
	<div class="content">
		<section class="content_main">
			<%@ include file="/WEB-INF/jsp/library/envset/envsetMenu.jsp"%>
			<section class="setting_cont">
				<table class="setting_table" id="store">
				<colgroup>
					<col width="90px">
					<col width="400px">
				</colgroup>
				<thead>
				<tr>
					<th>번호</th>
					<th>서점명</th>
				</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${fn:length(libList) > 0}">
							<c:forEach var="row" items="${libList}" varStatus="status">
								<tr>
									<input type="hidden" name="storeId" value="${row.storeId }"  />
									<td>${row.rnum}</td>
									<td><a href="javascript:;" onclick="libOrdered.initLibSearch('${row.storeId }', '${row.storeName}');" >${row.storeName}</a></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="2">조회된 결과가 없습니다.</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
				</table>
				<input type="hidden" id="storeId">
				<div style="position:relative; top:-30px; display:inline-block">
					<h4 style="margin-left:20px"><span class="red">● </span> <span id="libTitle"></span></h4>
					<ul id="sortable" class="sortable"></ul>
					<div style="position:relative;">
						<h4 style="margin-left:20px"><span class="red">●</span> <span id="libTitle2">미배분도서관</span></h4>
						<ul id="nonSortable"  class="sortable"></ul>
					</div>
				</div>
				<div class="btn_wrap" style="width: 60%">
					<input type="button" class="btn_search" value="저장" id="btnSave">
				</div>
			</section>
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	<script type="text/javascript" src="<c:url value="/resources/library/js/envset/libOrdered.js?v=${version }" />"></script>
</body>
</html>