<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dloan.common.util.SessionUtils" %>
	<header>
		<div class="header_top">
			<span class="color_blue">CMS</span> For bookstore
			<c:if test="${sessionScope.STORE_ID ne null}">
				<input type="button" class="btn_top_logout" value="로그아웃" onclick="javascript:document.location.href='${ctx}/store/logout';">
				<c:if test="${requestScope['javax.servlet.forward.request_uri'] ne '/store/storePasswordChange'}">
					<input type="button" class="btn_top_pwreset" value="비밀번호변경" onclick="javascript:document.location.href='${ctx}/store/storePasswordChange';">
				</c:if>
			</c:if>
			<%String name=SessionUtils.getStoreNm();
				System.out.println("name은 "+name);
				if(name==null){
					name="";
				}%>

				<div style="color:white; display:inline-block;"><%=name%></div>

			<input type="button" class="btn_top_cs" value="원격지원" onclick="window.open('http://113366.com/eco', '_blank')"/>
		</div>
		<div class="header_main_wrap">
			<div class="header_main">
				<h1 class="h1_logo">
					<a href="${ctx }/store/storeRequestInfo"><img src= "<c:url value="/resources/store/images/logo-bookstore.png" />" alt="메인로고"></a>
				</h1>
				<ul class="menu_nav">
					<li><a class="menu_tab01<c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/store/storeRequestInfo'    }">_on</c:if>" href="${ctx }/store/storeRequestInfo">신청승인</a></li>
					<li><a class="menu_tab02<c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/store/returnLoanInfo'      }">_on</c:if>" href="${ctx }/store/returnLoanInfo">대출반납</a></li>
					<li><a class="menu_tab03<c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/store/storeDeliveryReqInfo'}">_on</c:if>" href="${ctx }/store/storeDeliveryReqInfo">납품요청</a></li>
					<li><a class="menu_tab04<c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/store/storeResponseInfo' or requestScope['javax.servlet.forward.request_uri'] eq '/store/stroeResponseInfoDetail'}">_on</c:if>" href="${ctx }/store/storeResponseInfo">납품요청내역</a></li>
					<li><a class="menu_tab05<c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/store/storeCancelInfo'     }">_on</c:if>" href="${ctx }/store/storeCancelInfo">신청거절내역</a></li>
				</ul>
			</div>
		</div>
	</header>