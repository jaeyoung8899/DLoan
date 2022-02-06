<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/02_login.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
</head>
<body>
	
	<%@ include file="/WEB-INF/jsp/common/libHeader.jsp"%>

	<div class="content">
		<section class="content_login" > 
			<img src="<c:url value="/resources/library/images/library-box-subtitle.png" />" alt="" class="h2_square">
			<h2 class="h2">비밀번호 변경</h2>
		
			<section class="login_form" style="height: 190px;"> 
				<label for="curPw" style="width: 100px;">현재 비밀번호</label> 
				<input type="password" class="input_280"  id="curPw" value="">
				<br>
				<label for="changePw" style="width: 100px;">변경 비밀번호</label> 
				<input type="password" class="input_280" id="changePw"  value=""> 
				<label for="confirmPw" style="width: 100px;">비밀번호확인</label> 
				<input type="password" class="input_280" id="confirmPw"  value=""> 
			</section> 
			<div style="text-align: center;">
			<c:if test="${isNext}">
				<input type="button" class="btn_next"    id="btnNext"   value="다음에변경">
			</c:if>
				<input type="button" class="btn_change"  id="btnChange" value="비밀번호변경">
			</div>
		</section>

	</div>
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	
	<script type="text/javascript" src="<c:url value="/resources/library/js/login/libPasswordChange.js?v=${version }" />"> </script>
</body>
</html>