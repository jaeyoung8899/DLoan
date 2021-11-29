<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<link rel="stylesheet" href="<c:url value="/resources/store/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/store/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/store/css/02_login.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/store/css/03_content.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
</head>
<body>

	<c:if test="${fn:length(viewInfoList) > 0}">
		<c:forEach var="row" items="${viewInfoList}" varStatus="status">
			<c:if test="${row.CLASS_CODE eq '004'}">
				<c:choose>
					<c:when test="${row.VALUE eq 'Y'}">
						<%@ include file="/WEB-INF/jsp/common/storeHeaderReturn.jsp"%>
					</c:when>
					<c:otherwise>
						<%@ include file="/WEB-INF/jsp/common/storeHeader.jsp"%>
					</c:otherwise>
				</c:choose>
			</c:if>
		</c:forEach>
	</c:if>

	<div class="content">
		<section class="content_login">
			<img src="<c:url value="/resources/store/images/bookstore-box-subtitle.png" />" alt="" class="h2_square">
			<h2 class="h2">로그인</h2>
			<section class="login_form" id="loginSection" style="height: 160px;">
				<label for="inputId">아이디</label> 
				<input type="text" class="input_350"  id="inputId" value="">
				<br>
				<label for="inputPassword">비밀번호</label> 
				<input type="password" class="input_350" id="inputPassword"  value=""> 
				<br>
				<div id="saveInfo">
					<input type="checkbox" class="check" id="save_id" value="">
					<label for="save_id" class="check_wrap" style="margin: 0 10px 30px 324px; text-align: center; line-height: normal; width: 80px; font-size: 14px; font-weight: bold">아이디 저장</label>
					<input type="checkbox" class="check" id="save_pw" value="">
					<label for="save_pw" class="check_wrap" style="margin: 0 10px 30px 32px; text-align: center; line-height: normal; width: 100px; font-size: 14px; font-weight: bold">비밀번호 저장</label>
				</div>
			</section>
			<input type="button" class="btn_login" id="btnSubmit" value="로그인">
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/storeFooter.jsp"%>
	<script type="text/javascript" src="<c:url value="/resources/store/js/login/storeLogin.js?v=${version}"/>"></script>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		$("#inputId").focus();
	});
</script>
</html>