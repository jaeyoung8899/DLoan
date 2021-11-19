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
				<table class="result_table" id="libInfo">
				<colgroup>
					<col width="170px">
					<col width="100px">
					<col width="100px">
					<col width="90px">
					<col width="120px">
					<col width="120px">
					<col width="100px">
					<col width="100px">
					<col width="100px">
				</colgroup>
				<thead>
				<tr>
					<th>도서관명</th>
					<th>도서관ID</th>
					<th>전화번호</th>
					<th>담당자</th>
					<th>담당자SMS연락처</th>
					<th>금액</th>
					<th>도서관IP</th>
					<th>비밀번호</th>
					<th>로그인차단</th>
				</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${fn:length(lib) > 0}">
							<c:forEach var="row" items="${lib}" varStatus="status">
								<tr>
									<input type="hidden" name="libId" value="${row.libId }" />
									<input type="hidden" name="libManageCode" value="${row.libManageCode}" />
									
									<td style="text-align:left; padding-left: 10px;">${row.libName}<c:if test="${row.libManageCode ne null}"> (${row.libManageCode})</c:if></td>
									<td>${row.libId}</td>
									<c:choose>
										<c:when test="${row.libManageCode ne null}">
									<td><input type="text" name="libPhone" maxlength="11" value="${row.libPhone}" style="width:100%; height:30px"></td>
									<td><input type="text" name="name"     maxlength="11" value="${row.name}" style="width:100%; height:30px"></td>
									<td><input type="text" name="handphone" maxlength="11" value="${row.handphone}" style="width:100%; height:30px"></td>
									<td><input type="text" name="limitPrice" maxlength="11" value="${row.limitPrice}" style="width:100%; height:30px"></td>
										</c:when>
										<c:otherwise>
									<td></td>
									<td><input type="text" name="name"     maxlength="11" value="${row.name}" style="width:100%; height:30px"></td>
									<td></td>
									<td></td>
										</c:otherwise>
									</c:choose>
									<td><input type="button" class="btn_add" style="width:90px" value="도서관IP변경" onclick="libMng.popChgIP('${row.libId}')"></td>
									<td><input type="button" class="btn_add" style="width:90px" value="비밀번호변경" onclick="libMng.popChgPasswd('${row.libId}')"></td>
									<td>
										<c:if test="${row.loginFailedCnt >= 5}">
											<input type="button" class="btn_delete" style="width:90px" value="차단해제" onclick="javascript:libMng.failCountReset('${row.libId}');">
										</c:if>
									</td>
								</tr>
							</c:forEach>
						</c:when>
						<%-- <c:otherwise>
							<tr>
								<td colspan="8">조회된 결과가 없습니다.</td>
							</tr>
						</c:otherwise> --%>
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
	<!-- 비밀번호변경 레이어 -->
	<div class="back_black" id="passwdChange" style="display:none">
		<div class="setting_popup_wrap" style="height:230px">
			<form class="" name="changePasswd" id="changePasswd">
				<input type="hidden" id="popLibId" value="">
				<div class="popup_form_02">
					<label for="chgPasswd" style="width:100px">변경 비밀번호</label>
					<input type="password" class="input_280" id="chgPasswd" title="변경 비밀번호" rules="required"><br>
					<label for="cfmPasswd" style="width:100px">비밀번호 확인</label>
					<input type="password" class="input_280" id="cfmPasswd" title="비밀번호 확인" rules="required"><br>
					<input type="button" class="btn_input" style="margin-left:265px" value="비밀번호변경" id="btnChgPasswd">
				</div>
				<input type="button" class="btn_pop_close">
			</form>
		</div>
	</div>
	<!-- 도서관IP변경 레이어 -->
	<div class="back_black" id="libIPChange" style="display:none">
		<div class="setting_popup_wrap" style="height:330px; width:300px">
			<form class="" name="changeAllowIP" id="changeAllowIP">
				<input type="hidden" id="ipLibId" value="">
				<div class="popup_form_02">
					<label for="allowIp1" style="width:70px">도서관IP 1.</label>
					<input type="text" id="allowIp1" class="input_120" title="도서관IP" maxlength="15"><br>
					<label for="allowIp2" style="width:70px">도서관IP 2.</label>
					<input type="text" id="allowIp2" class="input_120" title="도서관IP" maxlength="15"><br>
					<label for="allowIp3" style="width:70px">도서관IP 3.</label>
					<input type="text" id="allowIp3" class="input_120" title="도서관IP" maxlength="15"><br>
					<label for="allowIp4" style="width:70px">도서관IP 4.</label>
					<input type="text" id="allowIp4" class="input_120" title="도서관IP" maxlength="15"><br>
					<label for="allowIp5" style="width:70px">도서관IP 5.</label>
					<input type="text" id="allowIp5" class="input_120" title="도서관IP" maxlength="15"><br>
					<input type="button" class="btn_input" style="margin-left:80px" value="도서관IP변경" id="btnChgIP">
				</div>
				<input type="button" class="btn_pop_close">
			</form>
		</div>
	</div>
	<script type="text/javascript" src="<c:url value="/resources/library/js/envset/libMng.js?v=${version }" />"></script>
</body>
</html>