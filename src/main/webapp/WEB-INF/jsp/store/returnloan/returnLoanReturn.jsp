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
	<%@ include file="/WEB-INF/jsp/common/storeHeaderReturn.jsp"%>
	<div class="content">
		<section class="content_main">
			<img src="<c:url value="/resources/store/images/bookstore-box-subtitle.png" />" alt="" class="h2_square">
			<h2 class="h2">구입반환</h2>
			<form class="form-horizontal" name="requestInfoForm" id="returnLoanInfoForm">
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
			</form>
			<div class="result_list">
				<div class="result_list_wrapper">
					<table class="result_table" id="returnLoanInfo">
						<colgroup>
							<col width="40px">
							<col width="40px">
							<col width="110px">
							<col width="70px">
							<col width="70px">
							<col width="80px">
							<col width="160px">
							<col width="70px">
							<col width="70px">
							<col width="70px">
							<col width="70px">
							<col width="70px">
							<col width="70px">
							<col width="70px">
							<col width="50px">
							<col width="50px">
						</colgroup>
						<thead>
						<tr>
							<th>번호</th>
							<th>
								<input type="checkbox" id="check_all" class="check" data-name="chkNm" onchange="returnLoan.checkedAll(this, 'returnLoanInfo');">
								<label for="check_all" class="check_wrap" ></label>
							</th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, returnLoan.search);" data-sort="USER_NO">대출자<br>번호</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, returnLoan.search);" data-sort="USER_NAME">구입자명</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, returnLoan.search);" data-sort="USER_NAME">핸드폰<br>번호</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, returnLoan.search);" data-sort="LIB_MANAGE_NAME">배분<br>도서관</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, returnLoan.search);" data-sort="TITLE">도서명</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, returnLoan.search);" data-sort="REQ_DATE">신청일</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, returnLoan.search);" data-sort="LOAN_WAIT_DATE">구입<br>만기일</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, returnLoan.search);" data-sort="LOAN_DATE">구입일</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, returnLoan.search);" data-sort="RETURN_PLAN_DATE">반환<br>예정일</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, returnLoan.search);" data-sort="RETURN_DATE">반환일</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, returnLoan.search);" data-sort="REQ_STATUS_NAME">진행상태</a></th>
							<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, returnLoan.search);" data-sort="SMS_YN">SMS수신<br>여부</a></th>
							<th>구입</th>
							<th>반환</th>
						</tr>
						</thead>
						<tbody>
						<c:choose>
							<c:when test="${fn:length(resultList) > 0}">
								<c:forEach var="row" items="${resultList}" varStatus="status">
									<tr>
										<input type="hidden" name="recKey" value="${row.recKey }"  />
										<input type="hidden" name="userNo" value="${row.userNo}"  />
										<input type="hidden" name="title" value="${row.title}"  />
										<td>${row.rnum}</td>
										<td>
											<input type="checkbox" name="chkNm" class="check" id="check_${row.rnum}" value="${row.reqStatus }">
											<label for="check_${row.rnum}" class="check_wrap"></label>
										</td>
										<td>${row.userNo}</td>
										<td>${row.name}</td>
										<td>${row.handphone}</td>
										<td>${row.libManageName}</td>
										<td style="text-align:left;">${row.title}</td>
										<td><fmt:formatDate value="${row.reqDate}" pattern="yyyy MM-dd"/></td>
										<td><fmt:formatDate value="${row.loanWaitDate}" pattern="yyyy MM-dd"/></td>
										<td><fmt:formatDate value="${row.loanDate}" pattern="yyyy MM-dd"/></td>
										<td><fmt:formatDate value="${row.returnPlanDate}" pattern="yyyy MM-dd"/></td>
										<td><fmt:formatDate value="${row.returnDate}" pattern="yyyy MM-dd"/></td>
										<td>${row.reqStatusName}</td>
										<td>${row.smsYn}</td>
										<td>
											<c:if test="${row.reqStatus eq 'S05'}">
												<input type="button" class="btn_borrow" value="구입" onclick="javascript:returnLoan.storeLoan(this);">
											</c:if>
										</td>
										<td>
											<c:if test="${row.reqStatus eq 'S06'}">
												<input type="button" class="btn_return" value="반환" onclick="javascript:returnLoan.storeReturn(this);">
											</c:if>
											<c:if test="${row.reqStatus eq 'S09'}">
												<input type="button" class="btn_no_return" value="반환" onclick="javascript:returnLoan.storeNoReturn(this);">
											</c:if>
										</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="15">조회된 결과가 없습니다.</td>
								</tr>
							</c:otherwise>
						</c:choose>
						</tbody>
					</table>
				</div>

				<c:if test="${not empty pageInfo}">
				<div style="text-align: center;">
					<kaitUi:paging pageInfo='${pageInfo}'  jsFunction="returnLoan.search" />
				</div>
				</c:if>
				<div class="btn_wrap_ab">
					<input type="button" class="btn_approve" value="도서준비" id="btnReqStatus">
				</div>
				<div class="btn_extra">
					<input type="button" class="btn_refuse" value="미구입취소복원" id="btnNoLoanStatus">
					<input type="button" class="btn_refuse" value="구입취소" id="btnWaitStatus">
					<input type="button" class="btn_refuse" value="반환취소" id="btnLoanStatus">
					
					<input type="button" class="btn_sms" value="SMS발송" id="btnSMSSend">
					<input type="button" class="btn_approve" value="인쇄" id="btnWindowPrint">
					<input type="button" class="btn_sms" id="btnExcelDown" value="엑셀다운로드">
				</div>
			</div>
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/storeFooter.jsp"%>
	<!-- SMS발송 레이어 -->
	<div class="back_black" id="smsSendPop" style="display:none">
		<div class="approver_popup_wrap">
			<form class="" name="sendSmsForm" id="sendSmsForm">
				<div class="popup_form_01">
					<input type="radio" class="radio" name="formRadio" id="formRadio1" value="select" checked="checked">
					<label class="up" for="formRadio1" >양식선택</label>
					<select class="select_420" id="selSms" name="selSms" >
						<option value="">SMS 발송 양식을 선택하세요.</option>
						<c:if test="${fn:length(smsList) > 0}">
							<c:forEach var="row" items="${smsList}" varStatus="status">
								<option value="${row.name}" data-contents="${row.contents}">${row.name}</option>
							</c:forEach>
						</c:if>
					</select>
					<input type="radio" name="formRadio" id="formRadio12" value="self" class="radio">
					<label class="up" for="formRadio12" >직접입력</label>
					<textarea class="textarea_520" id="textArea" rules="required" readonly="readonly"></textarea>
					<div class="user_num_box">
						회원번호 :
						<input type="text" class="input_user_num" id="senderInfo" readonly="readonly">
					</div>
					<input type="button" class="btn_send" id="btnSend" value="발송">
				</div>
				<input type="button" class="btn_pop_close" id="btnModalClose">
			</form>
		</div>
	</div>
	<!-- 카드비밀번호 -->
	<div class="back_black" id="loanCardPasswd" style="display:none">
		<div class="approver_popup_wrap" style="width:460px; height:160px; margin-top:400px">
			<form class="" name="loanCardPasswdForm" id="loanCardPasswdForm">
				<input type="hidden" id="loanCardPasswdRecKey" name="loanCardPasswdRecKey" />
				<input type="hidden" id="loanCardPasswdUserNo" name="loanCardPasswdUserNo" />
				<div class="popup_form_01" style="width:370px">
					<label class="up" for="cardPasswd" style="margin-right:5px">카드비밀번호</label>
					<input type="password" class="input_160" id="cardPasswd" title="카드비밀번호">
					<label class="up" for="appendix_yn" style="margin-left:5px">부록존재여부</label>
					<input type="checkbox" class="check" id="appendix_yn" value="Y">
					<label for="appendix_yn" class="check_wrap"></label>
					<input type="button" class="btn_send" id="btnConfirm" value="확인">
				</div>
				<input type="button" class="btn_pop_close" id="btnModalClose2">
			</form>
		</div>
	</div>
	<!-- 대출 레이어 -->
	<div class="back_black" id="loan" style="display:none">
		<div class="approver_popup_wrap" style="width:480px; height:230px; margin-top:400px">
			<form class="" name="loanForm" id="loanForm">
				<input type="hidden" id="loanRecKey" name="loanRecKey" />
				<!-- <input type="hidden" id="loanUserNo" name="loanUserNo" /> -->
				<div class="popup_form_01" style="width:370px">
					<label class="up" for="loanUserNo" style="margin-right:5px">대출자번호</label>
					<input type="text" class="input_280" id="loanUserNo" title="카드비밀번호" readonly>
					<div style="height:5px;"></div>
					<label class="up" for="loanTitle" style="margin-right:5px">구입도서명</label>
					<input type="text" class="input_280" id="loanTitle" readonly>
					<br>
					<div style="height:5px;"></div>
					<label class="up" for="appendix_yn1" style="margin-right:5px">부록존재여부</label>
					<input type="checkbox" class="check" id="appendix_yn1" value="Y" >
					<label for="appendix_yn1" class="check_wrap"></label>
					<br>
					<input type="button" class="btn_send" id="btnConfirm2" value="확인">
				</div>
				<input type="button" class="btn_pop_close" id="btnModalClose3">
			</form>
		</div>
	</div>
	<script type="text/javascript" src="<c:url value="/resources/store/js/returnloan/returnLoan.js?v=${version}"/>"></script>
</body>
</html>