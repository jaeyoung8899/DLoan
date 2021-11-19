<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/02_login.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/04_setting.css?v=${cssver}" />" type="text/css" title="no title" charset="utf-8"/>
</head>
<body>
	
	<%@ include file="/WEB-INF/jsp/common/libHeader.jsp"%>

	<div class="content">
		<section class="content_login"> 
			<img src="<c:url value="/resources/library/images/library-box-subtitle.png" />" alt="" class="h2_square">
			<h2 class="h2">로그인</h2>
		
			<section class="login_form"> 
				<label for="inputId">아이디</label> 
				<input type="text" class="input_350"  id="inputId" value="">
				<br>
				<label for="inputPassword">비밀번호</label> 
				<input type="password" class="input_350" id="inputPassword"  value="">
				<br>
				<input type="checkbox" class="check" id="save_id" value="">
				<label for="save_id" class="check_wrap" style="margin: 0 10px 30px 324px; text-align: center; line-height: normal; width: 80px;">아이디 저장</label>
				<input type="checkbox" class="check" id="save_pw" value="">
				<label for="save_pw" class="check_wrap" style="margin: 0 10px 30px 32px; text-align: center; line-height: normal; width: 100px;">비밀번호 저장</label>
			</section>
			<input type="button" class="btn_login" id="btnSubmit" value="로그인">
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	
	<script type="text/javascript" src="<c:url value="/resources/library/js/login/libLogin.js?v=${version }" />"> </script>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		$("#inputId").focus();
	});
</script>
</html>