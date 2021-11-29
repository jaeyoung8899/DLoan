<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<img src="<c:url value="/resources/library/images/library-box-subtitle.png" />" alt="" class="h2_square">
<h2 class="h2">지출결의</h2>
<div class="h3_tab_wrap">
	<h3><a href="${ctx }/lib/spending" <c:if test="${tab eq 'spending'}">class="tab_on"</c:if>>지출결의</a></h3>
	<h3><a href="${ctx }/lib/spending/refundBookInfo" <c:if test="${tab eq 'refund'}">class="tab_on"</c:if>>반품도서</a></h3>
</div>