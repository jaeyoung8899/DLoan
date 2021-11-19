<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
			<img src="<c:url value="/resources/library/images/library-box-subtitle.png" />" alt="" class="h2_square">
			<h2 class="h2">환경설정</h2>
			<div class="h3_tab_wrap">
				<h3><a href="${ctx }/lib/envset/libOrdered"   <c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/lib/envset/libOrdered'  }">class="tab_on"</c:if>>서점</a></h3>
				<h3><a href="${ctx }/lib/envset/smsMng"       <c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/lib/envset/smsMng'      }">class="tab_on"</c:if>>SMS설정</a></h3>
				<h3><a href="${ctx }/lib/envset/limitBook"    <c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/lib/envset/limitBook'   }">class="tab_on"</c:if>>신청제외도서</a></h3>
				<h3><a href="${ctx }/lib/envset/cancelReason" <c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/lib/envset/cancelReason'}">class="tab_on"</c:if>>거절사유</a></h3>
				<c:if test="${sessionScope.LIB_MNG_CD eq null}">
				<h3><a href="${ctx }/lib/envset/libMng"       <c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/lib/envset/libMng'      }">class="tab_on"</c:if>>도서관관리</a></h3>
				<h3><a href="${ctx }/lib/envset/storeMng"     <c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/lib/envset/storeMng'    }">class="tab_on"</c:if>>서점관리</a></h3>
				<h3><a href="${ctx }/lib/envset/confMng"      <c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/lib/envset/confMng'     }">class="tab_on"</c:if>>기타설정관리</a></h3>
				</c:if>
			</div>