<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/03_approver_delivery.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/05_popup.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<style type="text/css">
.search_form_01>label {
	margin-left:15px;
}
</style>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/libHeader.jsp"%>
	<div class="content">
		<section class="content_main">
			<img src="<c:url value="/resources/library/images/library-box-subtitle.png" />" alt="" class="h2_square">
			<h2 class="h2">신청승인</h2>
			<form class="form-horizontal" name="requestInfoForm" id="requestInfoForm">
				<input type="hidden"  id="sortCol" name="sortCol" value="${sortCol }" />
				<input type="hidden"  id="order" name="order"   value="${order }" />
				<input type="hidden"  id="start"  name="start" value="${start}" />
				<input type="hidden"  name="display" value="10" />
				<section class="search_form_01">
					<label for="selLibManageCode" class="first_label">배분도서관</label>
					<select class="select_140" id="selLibManageCode" name="reqLibManageCode" style="width:130px">
						<option value="" <c:if test="${reqLibManageCode eq ''}">selected="selected"</c:if>>전체</option>
						<c:forEach var="row" items="${libList}" varStatus="status">
							<c:if test="${row.libManageCode ne null}">
								<option value="${row.libManageCode}" <c:if test="${reqLibManageCode eq row.libManageCode}">selected="selected"</c:if>>${row.libName}</option>
							</c:if>
						</c:forEach>
					</select>
					<label for="selStoreId">신청서점</label>
					<select class="select_140" id="selStoreId" name="reqStoreId" style="width:155px">
						<option value="" <c:if test="${reqStoreId eq ''}">selected="selected"</c:if>>전체</option>
						<c:forEach var="row" items="${storeList}" varStatus="status">
							<c:if test="${row.storeId ne null}">
								<option value="${row.storeId}" <c:if test="${reqStoreId eq row.storeId}">selected="selected"</c:if>>${row.storeName}</option>
							</c:if>
						</c:forEach>
					</select>
					<label for="selStatus">신청진행상태</label>
					<select class="select_140" id="selStatus" name="reqStatus" style="width:120px">
						<option value="" <c:if test="${reqStatus eq ''}">selected="selected"</c:if>>전체</option>
						<option value="U01" <c:if test="${reqStatus eq 'U01'}">selected="selected"</c:if>>신청중</option>
						<option value="U02" <c:if test="${reqStatus eq 'U02'}">selected="selected"</c:if>>신청취소</option>
						<option value="S02" <c:if test="${reqStatus eq 'S02'}">selected="selected"</c:if>>서점신청거절</option>
						<option value="S03" <c:if test="${reqStatus eq 'S03'}">selected="selected"</c:if>>도서관확인요청</option>
						<option value="L01" <c:if test="${reqStatus eq 'L01'}">selected="selected"</c:if>>도서관승인</option>
						<option value="L02" <c:if test="${reqStatus eq 'L02'}">selected="selected"</c:if>>도서관신청거절</option>
						<option value="S04" <c:if test="${reqStatus eq 'S04'}">selected="selected"</c:if>>도서준비</option>
						<option value="S05" <c:if test="${reqStatus eq 'S05'}">selected="selected"</c:if>>대출대기</option>
						<option value="S06" <c:if test="${reqStatus eq 'S06'}">selected="selected"</c:if>>대출</option>
						<option value="S07" <c:if test="${reqStatus eq 'S07'}">selected="selected"</c:if>>반납</option>
						<option value="S08" <c:if test="${reqStatus eq 'S08'}">selected="selected"</c:if>>미대출취소</option>
						<option value="S09" <c:if test="${reqStatus eq 'S09'}">selected="selected"</c:if>>환불불가</option>
					</select>
					<label for="from_reqDate">신청일</label>
					<input type="text" class="input_120" id="from_reqDate" name="from_reqDate" title="신청시작일" value="${from_reqDate }" rules="date" style="width:90px">
					~
					<input type="text" class="input_120" id="to_reqDate" name="to_reqDate" title="신청종료일" value="${to_reqDate }" rules="date" style="width:90px">
					<br>
					<label for="userNo" class="first_label">대출자번호</label>
					<input type="text" class="input_120" id="userNo" name="userNo" value="${userNo }" style="width:130px">
					<label for="title">서명</label>
					<input type="text" class="input_120" id="title" name="title" value="${title}" style="width:310px">
					<label for="author">저자</label>
					<input type="text" class="input_120" id="author" name="author" value="${author }">
					<label for="publisher">출판사</label>
					<input type="text" class="input_120" id="publisher" name="publisher" value="${publisher }">
				</section>
				<div class="btn_wrap">
					<input type="button" class="btn_search" value="검색" id="btnSearch">
					<input type="button" class="btn_search" value="초기화" id="btnClear">
				</div>
			</form>
			<div class="result_list">
				<div style="width:1000px;overflow-x:auto;">
					<table class="result_table" id="requestInfo" style="width:1480px;">
						<colgroup>
							<col width="35px">
							<col width="35px">
							<col width="75px">
							<col width="75px">
							<col width="90px">
							<col width="180px">
							<col width="80px">
							<col width="80px">
							<col width="65px">
							<col width="50px">
							<col width="50px">
							<col width="50px">
							<col width="50px">
							<col width="50px">
							<col width="90px">
							<col width="90px">
							<col width="80px">
							<col width="70px">
							<col width="70px">
							<col width="150px">
							<col width="150px">
						</colgroup>
						<thead>
							<tr>
								<th>번호</th>
								<th>
									<input type="checkbox" id="check_all" class="check" data-name="chkNm" onchange="libRequest.checkedAll(this, 'requestInfo');">
									<label for="check_all" class="check_wrap" ></label>
								</th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="USER_NO">대출자 번호</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="STORE_NAME">신청 서점</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="LIB_MANAGE_NAME">배분 도서관</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="TITLE">도서명</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="AUTHOR">저자명</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="PUBLISHER">출판사</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="PUB_DATE">출판년도</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="PRICE">정가</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="REQ_DATE">신청일</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="LOAN_WAIT_DATE">대출<br>만기일</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="LOAN_DATE">대출일</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="RETURN_DATE">반납일</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="REQ_STATUS_NAME">진행상태</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="RES_STATUS_NAME">납품상태</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="USER_NO">대출자번호</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="USER_NAME">대출자명</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="SMS_YN">SMS수신<br>여부</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="CANCEL_REASON">거절사유</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRequest.search);" data-sort="CONFIRM_MESSAGE">확인요청<br>사유</a></th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${fn:length(resultList) > 0}">
									<c:forEach var="row" items="${resultList}" varStatus="status">
										<tr>
											<input type="hidden" name="recKey" value="${row.recKey }"  />
											<input type="hidden" name="userNo" value="${row.userNo }"  />
											<td>${row.rnum}</td>
											<td>
												<input type="checkbox" name="chkNm" class="check" id="check_${row.rnum}" value="${row.reqStatus }">
												<label for="check_${row.rnum}" class="check_wrap"></label>
											</td>
											<td>${row.userNo}</td>
											<td class="storeName">${row.storeName}</td>
											<td class="libManageName">${row.libManageName}</td>
											<td class="title" style="text-align:left;">${row.title}</td>
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
											<td>${row.resStatusName}</td>
											<td class="userNo">${row.userNo}</td>
											<td class="userName">${row.userName}</td>
											<td>${row.smsYn}</td>
											<td style="text-align:left;">${row.cancelReason}</td>
											<td style="text-align:left;">${row.confirmMessage}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="18">조회된 결과가 없습니다.</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
				<c:if test="${not empty pageInfo}">
				<div style="text-align: center;">
					<kaitUi:paging pageInfo='${pageInfo}' jsFunction="libRequest.search" />
				</div>
				</c:if>
				<div class="btn_wrap_ab">
					<input type="button" class="btn_approve" value="도서관승인" id="btnReqStatus">
					<input type="button" class="btn_refuse" value="도서관신청거절" id="btnLibReqCancel">
					<input type="button" class="btn_refuse" value="배분도서관수정" id="btnLibChange">
					<input type="button" class="btn_refuse" value="취소" id="btnCancel">
				</div>
				<div class="btn_extra">
					<input type="button" class="btn_refuse" value="제외도서등록" id="btnLimitBook">
					<input type="button" class="btn_approve" value="엑셀다운로드" id="btnReqExcel">
				</div>
			</div>
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	<!-- SMS발송 & 서점신청거절 레이어 -->
	<div class="back_black" id="smsSendPop" style="display:none">
		<div class="approver_popup_wrap">
			<form class="" name="sendSmsForm" id="sendSmsForm">
				<div class="popup_form_01">
					<input type="radio" class="radio" name="formRadio" id="formRadio1" value="select" checked="checked">
					<label class="up" for="formRadio1">양식선택</label>
					<select class="select_420" id="selReason" name="selReason" >
						<option value="">거절사유를 선택하세요.</option>
						<c:if test="${fn:length(reasonList) > 0}">
							<c:forEach var="row" items="${reasonList}" varStatus="status">
								<option value="${row.cancelReason}">${row.cancelReason }</option>
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
					<input type="button" class="btn_rejection" value="거절" id="btnSend">
				</div>
				<input type="button" class="btn_pop_close" id="btnModalClose">
			</form>
		</div>
	</div>
	
	<!-- 배분도서관수정 레이어 -->
	<div class="back_black" id="libChangePop" style="display:none">
		<div class="approver_popup_wrap">
			<form class="" name="libChangeForm" id="libChangeForm">
				<div class="popup_form_01">
					<label class="up" for="formRadio1">배분도서관선택</label>
					<select class="select_420" id="libChange" name="libChange">
						<option value="">배분할 도서관을 선택하세요.</option>
						<c:forEach var="row" items="${libList}" varStatus="status">
						<c:if test="${row.libManageCode ne null}">
							<option value="${row.libManageCode}">${row.libName}</option>
						</c:if>
						</c:forEach>
					</select>
					
					<div style="height:300px; overflow:auto;">
					<table class="result_table" id="changeInfo" style="width:530px;">
						<colgroup>
							<col width="80px">
							<col width="100px">
							<col width="150px">
							<col width="80px">
							<col width="70px">
						</colgroup>
						<thead>
							<tr>
								<th><a href="javascript:;" data-sort="STORE_NAME">신청 서점</a></th>
								<th><a href="javascript:;" data-sort="LIB_MANAGE_NAME">현재 배분 도서관</a></th>
								<th><a href="javascript:;" data-sort="TITLE">도서명</a></th>
								<th><a href="javascript:;" data-sort="USER_NO">대출자번호</a></th>
								<th><a href="javascript:;" data-sort="USER_NAME">대출자명</a></th>
							</tr>
						</thead>
						<tbody class="change_tbody">
							
							<!-- 데이터 불러오기 -->
							
						</tbody>
					</table>
					</div>
					<input type="button" class="btn_send" value="수정" id="btnChange">
				</div>
				<input type="button" class="btn_pop_close" id="btnChangeModalClose">
			</form>
		</div>
	</div>
	<script type="text/javascript" src="<c:url value="/resources/library/js/request/libRequest.js?v=${version }" />"></script>
</body>
</html>