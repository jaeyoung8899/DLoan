<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/04_setting.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/05_popup.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/libHeader.jsp"%>
	<div class="content">
		<section class="content_main">
			<%@ include file="/WEB-INF/jsp/library/envset/envsetMenu.jsp"%>
			<section class="setting_cont">
				<form name="envForm">
					<table class="result_table" id="storeInfo">
						<colgroup>
							<col width="170px">
							<col width="110px">
							<col width="90px">
							<col width="120px">
							<col width="240px">
							<col>
						</colgroup>
						<thead>
						<tr>
							<th>서점명</th>
							<th>서점ID</th>
							<th>
								신청제한
								<input type="checkbox" class="check" id="limitYN"/>
								<label for="limitYN" class="check_wrap"></label>
							</th>
							<th>
								목록표시제한
								<input type="checkbox" class="check" id="listYN"/>
								<label for="listYN" class="check_wrap"></label>
							</th>
							<th>신청제한일</th>
							<th>신청제한사유</th>
						</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${fn:length(store) > 0}">
									<c:forEach var="row" items="${store}" varStatus="status">
										<tr>
											<td style="text-align:left; padding-left: 10px;">${row.storeName}</td>
											<td>${row.storeId}</td>
											<td>
												<input type="checkbox" name="limitYN" class="check" id="limitYN_${row.storeId}"
													<c:if test="${row.limitYn eq 'Y' }">checked</c:if> value="Y"/>
												<label for="limitYN_${row.storeId}" class="check_wrap"></label>
											</td>
											<td>
												<input type="checkbox" name="listYN" class="check" id="listYN_${row.storeId}"
													<c:if test="${row.listYn eq 'Y' }">checked</c:if> value="Y"/>
												<label for="listYN_${row.storeId}" class="check_wrap"></label>
											</td>
											<td>
												<input type="text" name="limitDate1" class="input_100" id="reqLimitFrom_${row.storeId}" value="${row.limitDate1}">
												~
												<input type="text" name="limitDate2" class="input_100" id="reqLimitTo_${row.storeId}" value="${row.limitDate2}">
											</td>
											<td>
												<input type="text" name="limitReason" class="input_280" value="${row.limitReason}">
												<input type="hidden" name="storeId" value="${row.storeId}" />
											</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="6">조회 결과가 없습니다.</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</form>
			</section>
			<div class="btn_wrap">
				<input type="button" class="btn_search" value="저장" id="btnSave">
			</div>
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	<script type="text/javascript" src="<c:url value="/resources/library/js/envset/storeReqMng.js?v=${version }" />"></script>
</body>
</html>