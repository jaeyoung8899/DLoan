<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta name="viewport" content="width=device-width, user-scalable=no">
	<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
	<link rel="stylesheet" href="<c:url value="/resources/user/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
	<link rel="stylesheet" href="<c:url value="/resources/user/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
	<link rel="stylesheet" href="<c:url value="/resources/user/css/02_login.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
</head>
<body>
	<div id="body_contents">
		<div class="login-form">
			<form name="login">
				<input type="hidden" id="retUrl" value="${retUrl}">
				<fieldset>
					<legend class="hidden">아이디/비밀번호 로그인</legend>
					<ul class="lg_left">
						<li class="mfloat">
							<dl>
								<dd class="idtxt">
									<label for="inputId">아이디</label>
									<input id="inputId" type="text" class="inputText" title="아이디입력">
								</dd>
								<dd>
									<label for="inputPassword">비밀번호</label>
									<input id="inputPassword" type="password" class="inputText" title="비밀번호입력">
								</dd>
								<dd class="mbtnfloat"><input type="button" class="btn_login" value="로그인" id="btnSubmit"></dd>
							</dl>
						</li>
					</ul>
				</fieldset>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="<c:url value="/resources/user/js/login/login.js?v=${version }" />" ></script>
</body>
</html>