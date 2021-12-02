<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<link rel="stylesheet" href="<c:url value="/resources/store/css/00_reset.css?v=${cssver}" />" type="text/css" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/store/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/store/css/03_content.css?v=${cssver}" />" type="text/css" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/store/css/04_popup.css?v=${cssver}" />" type="text/css" title="no title" charset="utf-8"/>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/storeHeader.jsp"%>
	<div class="content">
		<section class="content_main">
			<img src="<c:url value="/resources/store/images/bookstore-box-subtitle.png" />" alt="" class="h2_square">
			<h2 class="h2">신청승인</h2>
			<%--<form class="form-horizontal" name="requestInfoForm" id="requestInfoForm">
				<input type="hidden"  id="sortCol" name="sortCol" value="${sortCol }" />
				<input type="hidden"  id="order" name="order"   value="${order }" />
				<input type="hidden"  id="start"  name="start" value="${start}" />
				<input type="hidden"  name="display" value="10" />
				<section class="search_form_01">
					<label for="selStatus" class="first_label">신청진행상태</label>
					<select class="select_140" id="selStatus" name="reqStatus">
						<option value="" <c:if test="${reqStatus eq ''}">selected="selected"</c:if>>전체</option>
						<option value="U01" <c:if test="${reqStatus eq 'U01'}">selected="selected"</c:if>>신청중</option>
						<option value="S02" <c:if test="${reqStatus eq 'S02'}">selected="selected"</c:if>>서점신청거절</option>
						<option value="S03" <c:if test="${reqStatus eq 'S03'}">selected="selected"</c:if>>도서관확인요청</option>
						<option value="L01" <c:if test="${reqStatus eq 'L01'}">selected="selected"</c:if>>도서관승인</option>
						<option value="L02" <c:if test="${reqStatus eq 'L02'}">selected="selected"</c:if>>도서관신청거절</option>
						<option value="S04" <c:if test="${reqStatus eq 'S04'}">selected="selected"</c:if>>도서준비</option>
						<option value="S05" <c:if test="${reqStatus eq 'S05'}">selected="selected"</c:if>>대출대기</option>
						<option value="S06" <c:if test="${reqStatus eq 'S06'}">selected="selected"</c:if>>대출</option>
						<option value="S07" <c:if test="${reqStatus eq 'S07'}">selected="selected"</c:if>>반납</option>
						<option value="U02" <c:if test="${reqStatus eq 'U02'}">selected="selected"</c:if>>신청취소</option>
						<option value="S08" <c:if test="${reqStatus eq 'S08'}">selected="selected"</c:if>>미대출취소</option>
						<option value="S09" <c:if test="${reqStatus eq 'S09'}">selected="selected"</c:if>>환불불가</option>
					</select>
					<label for="from_reqDate">신청일</label>
					<input type="text" class="input_120" id="from_reqDate" name="from_reqDate" title="신청시작일" value="${from_reqDate }" rules="date">
					-
					<input type="text" class="input_120" id="to_reqDate" name="to_reqDate" title="신청종료일" value="${to_reqDate }"  rules="date">
					<label for="userNo">대출자번호</label>
					<input type="text" class="input_120" id="userNo" name="userNo" value="${userNo }"><br>
					<label for="title" class="first_label">서명</label>
					&lt;%&ndash; <input type="text" class="input_350" id="title" name="title" value="${title}"> &ndash;%&gt;
					&lt;%&ndash; 2019 간담회 기능개선 &ndash;%&gt;
					<input type="text" class="input_200" id="title" name="title" value="${title}">
					<label for="author">저자</label>
					<input type="text" class="input_120" id="author" name="author" value="${author }">
					<label for="publisher">출판사</label>
					<input type="text" class="input_120" id="publisher" name="publisher"  value="${publisher }">
					<label for="isbn">ISBN</label>
					<input type="text" class="input_160" id="isbn" name="isbn" value="${isbn}">
				</section>
				<div class="btn_wrap">
					<input type="button" class="btn_search" value="검색" id="btnSearch">
					<input type="button" class="btn_search"  id="btnClear" value="초기화">
				</div>
			</form>--%>
			<input type="hidden"  id="sortCol" name="sortCol" value="${sortCol }" />
			<input type="hidden"  id="order" name="order"   value="${order }" />
			<input type="hidden"  id="start"  name="start" value="${start}" />
			<input type="hidden"  name="display" value="10" />
			<section class="search_form_01">
				<div class="search_form_field">
					<label for="selStatus" class="search_form_label">신청진행상태</label>
					<select class="select_140" id="selStatus" name="reqStatus">
						<option value="" <c:if test="${reqStatus eq ''}">selected="selected"</c:if>>전체</option>
						<option value="U01" <c:if test="${reqStatus eq 'U01'}">selected="selected"</c:if>>신청중</option>
						<option value="S02" <c:if test="${reqStatus eq 'S02'}">selected="selected"</c:if>>서점신청거절</option>
						<option value="S03" <c:if test="${reqStatus eq 'S03'}">selected="selected"</c:if>>도서관확인요청</option>
						<option value="L01" <c:if test="${reqStatus eq 'L01'}">selected="selected"</c:if>>도서관승인</option>
						<option value="L02" <c:if test="${reqStatus eq 'L02'}">selected="selected"</c:if>>도서관신청거절</option>
						<option value="S04" <c:if test="${reqStatus eq 'S04'}">selected="selected"</c:if>>도서준비</option>
						<option value="S05" <c:if test="${reqStatus eq 'S05'}">selected="selected"</c:if>>구입대기</option>
						<option value="S06" <c:if test="${reqStatus eq 'S06'}">selected="selected"</c:if>>구입</option>
						<option value="S07" <c:if test="${reqStatus eq 'S07'}">selected="selected"</c:if>>반환</option>
						<option value="U02" <c:if test="${reqStatus eq 'U02'}">selected="selected"</c:if>>신청취소</option>
						<option value="S08" <c:if test="${reqStatus eq 'S08'}">selected="selected"</c:if>>미구입취소</option>
						<option value="S09" <c:if test="${reqStatus eq 'S09'}">selected="selected"</c:if>>미반환취소</option>
					</select>
				</div>
				<div class="search_form_field">
					<label for="from_reqDate" class="search_form_label">신청일</label>
					<input type="text" class="input_100" id="from_reqDate" name="from_reqDate" title="신청시작일" value="${from_reqDate }" rules="date">
					-
					<input type="text" class="input_100" id="to_reqDate" name="to_reqDate" title="신청종료일" value="${to_reqDate }"  rules="date">
				</div>
				<div class="search_form_field">
					<label for="userNo" class="search_form_label">대출자번호</label>
					<input type="text" class="input_120" id="userNo" name="userNo" value="${userNo }"><br>
				</div>
				<div class="search_form_field">
					<label for="title" class="search_form_label">서명</label>
					<input type="text" class="input_200" id="title" name="title" value="${title}">
				</div>
				<div class="search_form_field">
					<label for="author" class="search_form_label">저자</label>
					<input type="text" class="input_120" id="author" name="author" value="${author }">
				</div>
				<div class="search_form_field">
					<label for="publisher" class="search_form_label">출판사</label>
					<input type="text" class="input_120" id="publisher" name="publisher"  value="${publisher }">
				</div>
				<div class="search_form_field">
					<label for="isbn" class="search_form_label">ISBN</label>
					<input type="text" class="input_160" id="isbn" name="isbn" value="${isbn}">
				</div>
				<div class="search_form_field">
					<label for="name" class="search_form_label">구입자명</label>
					<input type="text" class="input_120" id="name" name="name" value="${name}">
				</div>
			</section>
			<div class="btn_wrap">
				<input type="button" class="btn_search" value="검색" id="btnSearch">
				<input type="button" class="btn_search"  id="btnClear" value="초기화">
			</div>
			<div class="result_list">
				<div class="result_list_wrapper">
					<table class="result_table" id="requestInfo" style="width:1550px;">
						<colgroup>
							<col width="35px">
							<col width="35px">
							<col width="100px">
							<col width="170px">
							<col width="120px">
							<col width="120px">
							<col width="60px">
							<col width="70px">
							<col width="70px">
							<col width="50px">
							<col width="50px">
							<col width="50px">
							<col width="90px">
							<col width="120px">
							<col width="70px">
							<col width="60px">
							<col width="150px">
							<col width="150px">
						</colgroup>
						<thead>
							<tr>
								<th>번호</th>
								<th>
									<input type="checkbox" id="check_all" class="check" data-name="chkNm" onchange="storeRequest.checkedAll(this, 'requestInfo');">
									<label for="check_all" class="check_wrap" ></label>
								</th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeRequest.search);" data-sort="LIB_MANAGE_NAME">배분도서관</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeRequest.search);" data-sort="TITLE">도서명</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeRequest.search);" data-sort="AUTHOR">저자</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeRequest.search);" data-sort="PUBLISHER">출판사</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeRequest.search);" data-sort="PUB_DATE">출판년도</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeRequest.search);" data-sort="PRICE">정가[가격]</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeRequest.search);" data-sort="REQ_DATE">신청일</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeRequest.search);" data-sort="LOAN_WAIT_DATE">대출<br>만기일</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeRequest.search);" data-sort="LOAN_DATE">대출일</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeRequest.search);" data-sort="RETURN_DATE">반납일</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeRequest.search);" data-sort="REQ_STATUS_NAME">진행상태</a></th>								
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeRequest.search);" data-sort="USER_NO">대출자번호</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeRequest.search);" data-sort="USER_NAME">대출자명</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeRequest.search);" data-sort="SMS_YN">SMS수신<br>여부</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeRequest.search);" data-sort="CANCEL_REASON">거절사유</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeRequest.search);" data-sort="CONFIRM_MESSAGE">확인요청<br>사유</a></th>
							</tr>
						</thead>
						<tbody>
						<c:choose>
							<c:when test="${fn:length(resultList) > 0}">
								<c:forEach var="row" items="${resultList}" varStatus="status">
									<tr>
										<input type="hidden" name="recKey" value="${row.recKey }"  />
										<input type="hidden" name="userNo" value="${row.userNo}"  />
										<td>${row.rnum}</td>
										<td>
											<input type="checkbox" name="chkNm" class="check" id="check_${row.rnum}" value="${row.reqStatus }">
											<label for="check_${row.rnum}" class="check_wrap"></label>
										</td>
										<td>${row.libManageName}</td>
										<td style="text-align:left;">${row.title}</td>
										<td>${row.author}</td>
										<td>${row.publisher}</td>
										<td>
											<c:if test="${row.pubDate ne null}">
												${fn:substring(row.pubDate, 0, 4)}
											</c:if>
										</td>
										<td>${row.price}</td>
										<td><fmt:formatDate value="${row.reqDate}" pattern="yyyy-MM-dd"/></td>
										<td><fmt:formatDate value="${row.loanWaitDate}" pattern="yyyy-MM-dd"/></td>
										<td><fmt:formatDate value="${row.loanDate}" pattern="yyyy-MM-dd"/></td>
										<td><fmt:formatDate value="${row.returnDate}" pattern="yyyy-MM-dd"/></td>
										<td>${row.reqStatusName}</td>
										<td>${row.userNo}</td>
										<td>${row.name}</td>
										<td>${row.smsYn}</td>
										<td style="text-align:left;">${row.cancelReason}</td>
										<td style="text-align:left;">${row.confirmMessage}</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="17">조회된 결과가 없습니다.</td>
								</tr>
							</c:otherwise>
						</c:choose>
						</tbody>
					</table>
				</div>
				<c:if test="${not empty pageInfo}">
				<div style="text-align: center;">
					<kaitUi:paging pageInfo='${pageInfo}'  jsFunction="storeRequest.search" />
				</div>
				</c:if>
				<div class="btn_wrap_ab">
					<input type="button" class="btn_approve" id="btnReqStatus3" value="도서준비">
					<input type="button" class="btn_sms" id="btnReqStatus4" value="대출대기">
				</div>
				<div class="btn_extra">
					<!-- 2020/04/06 AGK 추가 -->
					<input type="button" class="btn_refuse" id="btnPrevStatus" value="신청중">
					<input type="button" class="btn_approve" id="btnReqStatus2" value="도서관확인요청">
					<input type="button" class="btn_sms" id="btnSMSSend" value="SMS발송">
					<input type="button" class="btn_refuse" id="btnStoreReqCancel" value="서점신청거절">
					<input type="button" class="btn_approve" id="btnWindowPrint" value="인쇄">
					<input type="button" class="btn_sms" id="btnExcelDown" value="엑셀다운로드">
				</div>
			</div>
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/storeFooter.jsp"%>
	<!-- SMS발송 & 서점신청거절 레이어 -->
	<div class="back_black" id="smsSendPop" style="display:none">
		<div class="approver_popup_wrap">
			<form class="" name="sendSmsForm" id="sendSmsForm">
				<div class="popup_form_01">
					<input type="radio" class="radio" name="formRadio" id="formRadio1" value="select" checked="checked">
					<label class="up" for="formRadio1">양식선택</label>
					<select class="select_420" id="selReason" name="selReason" style="display:none;">
						<option value="">거절사유를 선택하세요.</option>
						<c:if test="${fn:length(reasonList) > 0}">
							<c:forEach var="row" items="${reasonList}" varStatus="status">
								<option value="${row.cancelReason}">${row.cancelReason }</option>
							</c:forEach>
						</c:if>
					</select>
					<select class="select_420" id="selSms" name="selSms" style="display:none;">
						<option value="">SMS 발송 양식을 선택하세요.</option>
						<c:if test="${fn:length(smsList) > 0}">
							<c:forEach var="row" items="${smsList}" varStatus="status">
								<option value="${row.name}" data-contents="${row.contents}">${row.name}</option>
							</c:forEach>
						</c:if>
					</select>
					<input type="radio" class="radio" name="formRadio" id="formRadio12" value="self">
					<label class="up" for="formRadio12">직접선택</label>
					<textarea class="textarea_520" id="textArea" rules="required" readonly="readonly"></textarea>
					<div class="user_num_box">
						회원번호 :
						<input type="text" class="input_user_num" id="senderInfo" readonly="readonly">
					</div>
					<input type="button" class="btn_send" value="발송" id="btnSend">
				</div>
				<input type="button" class="btn_pop_close" id="btnModalClose">
			</form>
		</div>
	</div>
	<!-- 도서준비 -->
	<div class="back_black" id="loanWaitLayer" style="display:none">
		<div class="approver_popup_wrap" style="width:370px; height:150px; margin-top:400px">
			<form class="" name="loanWaitForm" id="loanWaitForm">
				<div class="popup_form_01" style="width:280px">
					<label class="up" for="enterPlanDate" style="margin-right:5px">입고예정일</label>
					<input type="text" class="input_120" id="enterPlanDate" title="입고예정일" rules=integer style="width:50px">
					<label class="up" for="enterPlanDate" style="margin-right:5px">일</label>
					<input type="button" class="btn_send" id="btnConfirm" style="margin-top:0" value="확인">
				</div>
				<input type="button" class="btn_pop_close" id="btnModalClose2">
			</form>
		</div>
	</div>
	<!-- 도서관 확인요청 -->
	<div class="back_black" id="libConfirmLayer" style="display:none">
		<div class="approver_popup_wrap" style="height:290px;margin-top:250px;">
			<form class="" name="libConfirmForm" id="libConfirmForm">
				<div class="popup_form_01">
					<label class="up" for="formRadio12">도서관 확인요청 메세지</label>
					<textarea class="textarea_520" id="libConfirmText" style="height:100px;"></textarea>
					<input type="button" class="btn_send" value="확인요청" id="btnConfirm2">
				</div>
				<input type="button" class="btn_pop_close" id="btnModalClose3">
			</form>
		</div>
	</div>
	<script type="text/javascript" src="<c:url value="/resources/store/js/request/storeRequest.js?v=${version}"/>"></script>
</body>
</html>