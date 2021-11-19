<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
			<img src="<c:url value="/resources/library/images/library-box-subtitle.png" />" alt="" class="h2_square">
			<h2 class="h2">통계</h2>
			<div class="h3_tab_wrap2">
				<h3><a href="${ctx }/lib/statistics/byYearStat"   <c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/lib/statistics/byYearStat' || requestScope['javax.servlet.forward.request_uri'] eq '/lib/statistics/byYearStatInfo'}">class="tab_on"</c:if>>연도별</a></h3>
				<h3><a href="${ctx }/lib/statistics/byUserStat"       <c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/lib/statistics/byUserStat' ||  requestScope['javax.servlet.forward.request_uri'] eq '/lib/statistics/byUserStatInfo'   }">class="tab_on"</c:if>>이용자별</a></h3>
				<h3><a href="${ctx }/lib/statistics/byBookStat"    <c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/lib/statistics/byBookStat'  || requestScope['javax.servlet.forward.request_uri'] eq '/lib/statistics/byBookStatInfo' }">class="tab_on"</c:if>>도서별</a></h3>
			</div>