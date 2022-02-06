<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/04_setting.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/libHeader.jsp"%>
	<div class="content">
		<section class="content_main">
			<%@ include file="/WEB-INF/jsp/library/envset/envsetMenu.jsp"%>
			<section class="setting_cont">
				<div class="btn_wrap_1000">
					<input type="button" class="btn_add" value="추가" id="btnAdd">
					<input type="button" class="btn_delete" value="삭제" id="btnDel">
				</div>
				<table class="result_table" id="reasonInfo">
				<colgroup>
					<col width="50px">
					<col width="50px">
					<col width="800px">
				</colgroup>
				<thead>
				<tr>
					<th>번호</th>
					<th>
						<input type="checkbox" id="check_all" class="check" data-name="chkNm" onchange="cancelReason.checkedAll(this, 'reasonInfo');">
						<label for="check_all" class="check_wrap" ></label>
					</th>
					<th>거절사유</th>
				</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${fn:length(reasonList) > 0}">
							<c:forEach var="row" items="${reasonList}" varStatus="status">
								<tr>
									<input type="hidden" name="recKey" value="${row.recKey }"  />
									<td>${status.count}</td>
									<td>
										<input type="checkbox" name="chkNm" class="check" id="check_${row.rnum}" value="${row.reqStatus }">
										<label for="check_${row.rnum}" class="check_wrap"></label>
									</td>
									<td><input type="text" name="cancelReason" maxlength=1300 value="${row.cancelReason}" style="width:100%; height:30px"></td>
								</tr>
							</c:forEach>
						</c:when>
						<%-- <c:otherwise>
							<tr>
								<td colspan="3">조회된 결과가 없습니다.</td>
							</tr>
						</c:otherwise> --%>
					</c:choose>
				</tbody>
				</table>
			</section>
			<div class="btn_wrap" style="width: 97%">
				<input type="button" class="btn_search" value="저장" id="btnSave">
			</div>
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	<script type="text/javascript" src="<c:url value="/resources/library/js/envset/cancelReason.js?v=${version }" />"></script>
</body>
</html>