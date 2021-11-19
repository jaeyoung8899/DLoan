<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="dloan.common.util.SessionUtils" %>
	<header>
		<div class="header_top">
			<span class="color_green">CMS</span> For library
			<%String name=SessionUtils.getLibNm();
				System.out.println("name은 "+name);
				if(name==null){
					name="";
				}%>
				<div style="display:inline-block; color:white;"><%=name %></div>
			<%	if (SessionUtils.isLibSession()) { %>
				<input type="button" class="btn_top_logout" value="로그아웃" onclick="javascript:document.location.href='${ctx}/lib/logout';">
				<c:if test="${requestScope['javax.servlet.forward.request_uri'] ne '/lib/libPasswordChange'  }">
					<input type="button" class="btn_top_pwreset" value="비밀번호변경"  onclick="javascript:document.location.href='${ctx}/lib/libPasswordChange?isNext=false';">
				</c:if>
			<%	} %>
		</div>
		<div class="header_main_wrap">
			<div class="header_main">
				<h1 class="h1_logo">
					<a href="${ctx}/lib/libRequestInfo"><img src= "<c:url value="/resources/library/images/logo-library.png" />" alt="메인로고"></a>
				</h1>
				<ul class="menu_nav">
					<li><a class="menu_tab01<c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/lib/libRequestInfo'}">_on</c:if>" href="${ctx}/lib/libRequestInfo" >신청승인</a></li>
					<li><a class="menu_tab02<c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/lib/libResponseInfo' or requestScope['javax.servlet.forward.request_uri'] eq '/lib/libResponseInfoDetail'}">_on</c:if>" href="${ctx }/lib/libResponseInfo">납품요청</a></li>
					<li><a class="menu_tab03<c:if test="${fn:indexOf(requestScope['javax.servlet.forward.request_uri'], '/lib/statistics/') != -1}">_on</c:if>" href="${ctx }/lib/statistics/byYearStat">통계</a></li>
					<li><a class="menu_tab04<c:if test="${fn:indexOf(requestScope['javax.servlet.forward.request_uri'], '/lib/envset/') != -1}">_on</c:if>" href="${ctx }/lib/envset/libOrdered">환경설정</a></li>
				</ul>
			</div>
		</div>
	</header>

<%	if (SessionUtils.isLibSession()) { %>
	<script type="text/javascript">
	$(document).ready(function() {
		setTimeout(libcomm.loginCheck, libcomm.LOGIN_CHECK_TIME);
	});
	</script>
<%	} %>
