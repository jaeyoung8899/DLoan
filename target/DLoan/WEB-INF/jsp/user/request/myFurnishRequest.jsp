<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dloan.common.util.SessionUtils" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta name="viewport" content="width=device-width, user-scalable=no">
	<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
	<link rel="stylesheet" href="<c:url value="/resources/user/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
	<link rel="stylesheet" href="<c:url value="/resources/user/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
	<link rel="stylesheet" href="<c:url value="/resources/user/css/03_content.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
</head>
<body>
<div id="wrap">
<section class="body">
	<div id="body_contents" class="body_contents">
		<c:if test="${tab ne null}">
			<ul class="tab_wrap">
				<a href="${ctx}/request"><li>희망도서 서비스 신청</li></a>
				<%if(SessionUtils.getUserId() != null) { %>
					<a href="${ctx}/myRequestInfo" class="on"><li>신청현황</li></a>
				<%} %>
				<a href="${ctx}/requestHelp"><li>상세이용안내2</li></a>
			</ul>
		</c:if>
		<div class="tab_button_wrap">
			<button type="button"  class="sel_option_other sel_on_other" name="selectObject" value="library">도서관 신청현황</button>
			<button type="button"  class="sel_option_other" name="selectObject" value="bookstore">서점 신청현황</button>
		</div>
		<div class="search_form_03" style="margin-top:0;">
			<form name="requestForm" id="requestForm">
				<input type="hidden" id="sortCol" name="sortCol" value="${sortCol }" />
				<input type="hidden" id="order" name="order"   value="${order }" />
				<input type="hidden" id="start"  name="start" value="${start}" />
				<input type="hidden" name="display" value="10" />
				<input type="hidden" name="tab" value="${tab}" />
				<fieldset>
					<legend class="hidden">신청정보</legend>
					<div class="input_wrap">
						<label for="from_reqDate">신청일</label>
						<input type="text" class="input_100" id="from_reqDate" name="from_reqDate" title="신청시작일" value="${from_reqDate }" rules="date"> ~ <input type="text" class="input_100" id="to_reqDate" name="to_reqDate" title="신청종료일" value="${to_reqDate }" rules="date">
					</div>
					<div class="input_wrap">
						<label for="from_loanDate">대출일</label>
						<input type="text" class="input_100" id="from_loanDate" name="from_loanDate" title="대출시작일" value="${from_loanDate }" rules="date"> ~ <input type="text" class="input_100" id="to_loanDate" name="to_loanDate" title="대출종료일" value="${to_loanDate }" rules="date">
					</div>
					<div class="input_wrap">
						<label for="from_returnPlanDate">반납예정일</label>
						<input type="text" class="input_100" id="from_returnPlanDate" name="from_returnPlanDate" title="반납예정시작일" value="${from_returnPlanDate }" rules="date"> ~ <input type="text" class="input_100" id="to_returnPlanDate" name="to_returnPlanDate" title="반납예정종료일" value="${to_returnPlanDate }" rules="date">
					</div>
					<div class="input_wrap">
						<label for="from_returnDate">반납일</label>
						<input type="text" class="input_100" id="from_returnDate" name="from_returnDate" title="반납시작일" value="${from_returnDate }" rules="date"> ~ <input type="text" class="input_100" id="to_returnDate" name="to_returnDate" title="반납종료일" value="${to_returnDate }" rules="date">
					</div>
					<div class="input_wrap">
						<label for="selStatus">진행상태</label>
						<select class="select_130" id="selStatus" name="reqStatus" >
							<option value="" <c:if test="${reqStatus eq ''}">selected="selected"</c:if>>전체</option>
							<option value="1" <c:if test="${reqStatus eq '1'}">selected="selected"</c:if>>신청중</option>
							<option value="2" <c:if test="${reqStatus eq '2'}">selected="selected"</c:if>>처리중</option>
							<option value="3" <c:if test="${reqStatus eq '3'}">selected="selected"</c:if>>비치상태</option>
							<option value="4" <c:if test="${reqStatus eq '4'}">selected="selected"</c:if>>취소됨</option>
						</select>
					</div>
					<div class="input_wrap">
						<label>검색</label>
						<input type="button" class="btn_search ml_166" id="btnSearch">
					</div>
				</fieldset>
			</form>
		</div>
		<table class="result_table furnish" id="myRequestInfo">
			<caption>검색건수 : <span class="count">${pageInfo.totalRecordCount}</span>건</caption>
			<thead>
				<tr>
					<th>번호</th>
					<th>
						<input type="checkbox" id="check_all" class="check" data-name="chkNm" onchange="myRequest.checkedAll(this, 'myRequestInfo', '1', true);">
						<label for="check_all" class="check_wrap" ></label>
					</th>
					<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, myRequest.search);" data-sort="LIB_NAME">신청도서관</a></th>
					<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, myRequest.search);" data-sort="TITLE">도서명</a></th>
					<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, myRequest.search);" data-sort="AUTHOR">저자</a></th>
					<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, myRequest.search);" data-sort="PUBLISHER">출판사</a></th>
					<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, myRequest.search);" data-sort="REQ_DATE">신청일</a></th>
					<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, myRequest.search);" data-sort="LOAN_WAIT_DATE">대출만기일</a></th>
					<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, myRequest.search);" data-sort="LOAN_DATE">대출일</a></th>
					<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, myRequest.search);" data-sort="RETURN_PLAN_DATE">반납예정일</a></th>
					<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, myRequest.search);" data-sort="RETURN_DATE">반납일</a></th>
					<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, myRequest.search);" data-sort="REQ_STATUS_NAME">진행상태</a></th>
					<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, myRequest.search);" data-sort="CANCEL_REASON">거절사유</a></th>
				</tr>
			</thead>
			<tbody> 
				<c:choose>
					<c:when test="${fn:length(resultList) > 0}">
						<c:forEach var="row" items="${resultList}" varStatus="status">
							<tr>
								<td><input type="hidden" name="recKey" value="${row.recKey }"/>${row.rnum}</td>
								<td>
									<input type="checkbox" name="chkNm" class="check" id="check_${row.rnum}" value="${row.reqStatus }">
									<label for="check_${row.rnum}" class="check_wrap"></label>
								</td>
								<td>${row.libName}</td>
								<td style="text-align:left;">
									<c:choose>
										<c:when test="${row.reqStatus eq '4' and row.cancelReason ne null }">
											<a onclick="comm.alert('거절사유 : ' + '${row.cancelReason}')" style="cursor:pointer;">${row.title}</a>
										</c:when>
										<c:otherwise>${row.title }</c:otherwise>
									</c:choose>
								</td>
								<td>${row.author}</td>
								<td>${row.publisher}</td>
								<td><fmt:formatDate value="${row.reqDate}" pattern="yyyy MM-dd"/></td>
								<td><fmt:formatDate value="${row.loanWaitDate}" pattern="yyyy MM-dd"/></td>
								<td><fmt:formatDate value="${row.loanDate}" pattern="yyyy MM-dd"/></td>
								<td><fmt:formatDate value="${row.returnPlanDate}" pattern="yyyy MM-dd"/></td>
								<td><fmt:formatDate value="${row.returnDate}" pattern="yyyy MM-dd"/></td>
								<td>${row.reqStatusName}</td>
								<td>${row.cancelReason}</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="13">신청 내역이 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		<input type="button" class="btn_cancel" value="신청취소" id="btnReqCancel">
		<c:if test="${not empty pageInfo}">
		<div class="col-lg-12" style="text-align: center;">
			<kaitUi:paging pageInfo='${pageInfo}' jsFunction="myRequest.search" pagingType="Home" />
		</div>
		</c:if>
	</div>
	<script type="text/javascript" src="<c:url value="/resources/user/js/request/myFurnishRequest.js?v=${version }" />" ></script>
</section>
</div>
</body>
</html>