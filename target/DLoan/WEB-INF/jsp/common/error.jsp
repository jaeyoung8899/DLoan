<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<c:if test="${param.path ne null}">
	<c:set var="path" value="${param.path }" />
</c:if>

<c:choose>
	<c:when test="${path eq 'lib'}">
		<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
		<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
		<link rel="stylesheet" href="<c:url value="/resources/library/css/error_library.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
	</c:when>
	<c:when test="${path eq 'store'}">
		<link rel="stylesheet" href="<c:url value="/resources/store/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
		<link rel="stylesheet" href="<c:url value="/resources/store/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
		<link rel="stylesheet" href="<c:url value="/resources/store/css/error_bookstore.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
	</c:when>
	<c:otherwise>
		<link rel="stylesheet" href="<c:url value="/resources/user/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
		<link rel="stylesheet" href="<c:url value="/resources/user/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
		<link rel="stylesheet" href="<c:url value="/resources/user/css/error_user.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
	</c:otherwise>
</c:choose>

</head>
<body>
	<div class="error_page">
		<div class="error_msg">
			<c:choose>
				<c:when test="${path eq 'lib'}">
					<img src="<c:url value="/resources/library/images/notice2.png" />" alt="시스템오류이미지">
				</c:when>
				<c:when test="${path eq 'store'}">
					<img src="<c:url value="/resources/store/images/notice1.png" />" alt="시스템오류이미지">
				</c:when>
				<c:otherwise>
					<img src="<c:url value="/resources/user/images/notice1.png" />" alt="시스템오류이미지">
				</c:otherwise>
			</c:choose>
<%	if (response.getStatus() == HttpServletResponse.SC_NOT_FOUND) { %>
			<h1>찾을 수 없는 페이지입니다.</h1>
<%	} else { %>
			<h1>시스템오류 입니다. 관리자에게 문의하시기 바랍니다.</h1>
<%	} %>
			<input type="button" class="btn_prev" value="이전페이지" onclick="history.back();">
			<c:choose>
				<c:when test="${path eq 'lib'}">
					<input type="button" class="btn_home" value="홈으로이동" onclick="document.location.href='${ctx}/lib/libRequestInfo';">
				</c:when>
				<c:when test="${path eq 'store'}">
					<input type="button" class="btn_home" value="홈으로이동" onclick="document.location.href='${ctx}/store/storeRequestInfo';">
				</c:when>
			</c:choose>
		</div>
	</div>
</body>
</html>
