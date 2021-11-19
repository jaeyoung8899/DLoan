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
				<table class="result_table" id="confInfo">
				<colgroup>
					<col width="150px">
					<col width="100px">
					<col width="400px">
				</colgroup>
				<thead>
					<tr>
						<th>설정</th>
						<th>설정값</th>
						<th>설정설명</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${fn:length(conf) > 0}">
							<c:forEach var="row" items="${conf}" varStatus="status">
								<tr>
									<input type="hidden" name="confId" value="${row.confId }" />
									<input type="hidden" name="confDesc" value="${row.confDesc }" />
									<td style="text-align:left; padding-left :10px;">${row.confDesc}</td>
									<td style="text-align:left; padding-right:10px;">
										<c:choose>
											<c:when test="${row.confId eq 'AUTO_LIMIT' or row.confId eq 'USER_PENALTY'}">
												<select name="confValue" style="width:100%;height:30px;">
													<option value="Y" <c:if test="${row.confValue eq 'Y'}">selected="selected"</c:if>>사용</option>
													<option value="N" <c:if test="${row.confValue eq null or row.confValue eq 'N'}">selected="selected"</c:if>>사용안함</option>
												</select>
											</c:when>
											<c:otherwise>
												<input type="text" name="confValue" maxlength="11" value="${row.confValue}" style="width:100%; height:30px">
											</c:otherwise>
										</c:choose>
									</td>
									<td style="text-align:left;">${row.confDetail }</td>
								</tr>
							</c:forEach>
						</c:when>
					</c:choose>
				</tbody>
				</table>
			</section>
			<div class="btn_wrap">
				<input type="button" class="btn_search" value="저장" id="btnSave">
			</div>
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	<script type="text/javascript" src="<c:url value="/resources/library/js/envset/confMng.js?v=${version }" />"></script>
</body>
</html>