<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>

<link rel="stylesheet" href="<c:url value="/resources/store/css/00_reset.css?v=${cssver}" />"   type="text/css" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/store/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/store/css/03_content.css?v=${cssver}" />" type="text/css" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/store/css/04_popup.css?v=${cssver}" />"   type="text/css" title="no title" charset="utf-8"/>
</head>
<body>

	<%@ include file="/WEB-INF/jsp/common/storeHeader.jsp"%>

	<div class="content">

		<section class="content_main">
			<img src="<c:url value="/resources/store/images/bookstore-box-subtitle.png" />" alt="" class="h2_square">
			
			<h2 class="h2">납품요청내역</h2>

			<!-- 검색 조건 -->
			<form class="form-horizontal" name="searchInfoForm" id="searchInfoForm">
				<input type="hidden"  id="resKey"  name="resKey" value="${resKey }" />
				<section class="search_form_01">
				
					<label class="first_label" for="resTitle">납품명</label> 
					<c:choose>
						<c:when test="${resKey ne null}">
							<c:choose>
								<c:when test="${resStatus eq 'S01' }">
									<input type="text" class="input_415" id="resTitle" name="resTitle" value="${resTitle }" title="납품명" rules="required">
								</c:when>
								<c:otherwise>
								<input type="text" class="input_415" id="resTitle" name="resTitle" value="${resTitle }" title="납품명" rules="required" disabled="disabled">
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
						<input type="text" class="input_415" id="resTitle" name="resTitle" value="${resTitle }" title="납품명" rules="required">
						</c:otherwise>
					</c:choose>
					
					<label for="selLib">납품도서관</label> 
					<select class="select_140" id="selLib" name="libManageCode"  title="납품도서관" rules="required" <c:if test="${resKey ne null}">disabled="disabled"</c:if>>
						<kaitUi:codeList list="${libList }" select="${libManageCode }" />
					</select> <br> 
						
					<label class="first_label" for="res_Date">납품요청일</label> 
					<c:choose>
						<c:when test="${resDate ne null}" >
					<input type="text" class="input_140"  id="res_Date" name="res_Date" value="${resDate}" disabled="disabled">
						</c:when>
						<c:otherwise>
						
					<jsp:useBean id="toDay" class="java.util.Date" />
					<input type="text" class="input_140"  id="res_Date" name="res_Date" value="<fmt:formatDate value="${toDay}" pattern="yyyy-MM-dd" />" disabled="disabled"> 
						</c:otherwise>
					</c:choose>
					
					<label class="ml_69" for ="selStatus">진행상태</label>
					<select class="select_140" id="selStatus" name="resStatus" disabled="disabled">
						<option value="S01" <c:if test="${resStatus eq 'S01'}">selected="selected"</c:if>>납품요청</option>
						<option value="L01" <c:if test="${resStatus eq 'L01'}">selected="selected"</c:if>>납품승인</option>
						<option value="S02" <c:if test="${resStatus eq 'S02'}">selected="selected"</c:if>>납품완료</option>
					</select> 
				</section>
				<!-- 
				<div class="btn_wrap">
					<input type="button" class="btn_search"  id="btnSearch" value="검색">
				</div> -->
			</form>
		
			<!-- 목록 -->
			<div class="result_list_del">
				
					<div class="btn_wrap_1000 btn_extra">
						<c:if test="${resStatus eq 'S01' or resKey eq null}">
							<input type="button" class="btn_add"     id="btnAdd"  value="추가"> 
							<input type="button" class="btn_delete"  id="btnDel"  value="삭제">
						</c:if>
						<input type="button" class="btn_approve" id="btnWindowPrint" value="인쇄">
						<input type="button" class="btn_sms" id="btnExcelDown"  value="엑셀다운로드">
					</div>
				
				<p class="table_name">납품도서목록</p>
					
				<table class="result_table" id="detailInfo" >
					<colgroup>
						<col width="40px">
						<col width="40px">
						<col width="140px;">
						<col width="180px">
						<col width="120px">
						<col width="130px">
						<col width="70px">
						<col width="80px">
						<col width="125px">
						<col width="100px">
						<col width="100px">
					</colgroup>
					<thead>
						<tr>
							<th>번호</th>
							<th>
								<input type="checkbox" id="check_all" class="check" data-name="chkNm" onchange="storeResponseDetail.checkedAll(this, 'detailInfo');">
								<label for="check_all" class="check_wrap" ></label>
							</th>
							<th>대출자번호</th>
							<th>도서명</th>
							<th>저자명</th>
							<th>출판사</th>
							<th>출판년도</th>
							<th>정가[가격]</th>
							<th>신청일</th>
							<th>진행상태</th>
							<th>납품상태</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(resultList) > 0}">
								<c:forEach var="row" items="${resultList}" varStatus="status">
									<tr>
										<input type="hidden" name="recKey" value="${row.recKey }"  />
										<input type="hidden" name="tResKey" value="${row.resKey }"  />
										
										<td>${row.rnum}</td>
										<td>
											<input type="checkbox" id="check_${row.rnum}"  name="chkNm" class="check" >
											<label for="check_${row.rnum}" class="check_wrap"></label>
										</td>
										<td>${row.userNo}</td>
										<td style="text-align:left;">${row.title}</td>
										<td>${row.author}</td>
										<td>${row.publisher}</td>
										<td>
											<c:if test="${row.pubDate ne null}">
												${fn:substring(row.pubDate, 0, 4)}
											</c:if>
										</td>
										<td>${row.price}</td>
										<td>${row.reqDate}</td>
										<td>${row.reqStatusName}</td>
										<c:choose>
											<c:when test="${row.resStatus eq 'L02'}">
										<td><span style="color: red;">${row.resStatusName}</span></td>
											</c:when>
											<c:otherwise>
										<td>${row.resStatusName}</td>
											</c:otherwise>
										</c:choose>
									</tr>
								</c:forEach>
							</c:when>
							<%-- <c:otherwise>
								<tr>
									<td colspan="7" style="text-align: center;">조회된 결과가 없습니다.</td>
								</tr>
							</c:otherwise> --%>
						</c:choose>
					</tbody>
				</table>
							
				<div class="btn_wrap" style="margin-bottom:20px">
					<c:if test="${resStatus eq 'S01' or resKey eq null}">
					<input type="button" class="btn_regist"  id="btnSave" value="저장">
					</c:if>
					<input type="button" class="btn_list"    id="btnList" value="목록">
				</div>
			</div>
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/storeFooter.jsp"%>
	
	<script type="text/javascript" src="<c:url value="/resources/store/js/response/storeResponseDetail.js?v=${version}"/>"></script>
	
	
	<!------------------------------------------pop start----------------------------------------------->
	<div class="back_black"  style="display: none;" id="reqAddInfo">
		<div class="delivery_popup_wrap">
			<form name="reqSearchInfo" id="reqSearchInfo">
			<input type="hidden"  id="popSortCol" name="popSortCol" value="" />
			<input type="hidden"  id="popOrder"  name="popOrder"   value="" />
			<div class="popup_form_02">
				<label class="pop_label_first" for="from_reqDate">신청일</label>
					<input type="text" class="input_140" id="from_reqDate" name="from_reqDate" title="신청시작일" value="" rules="date">
					-
					<input type="text" class="input_140" id="to_reqDate" name="to_reqDate" title="신청종료일" value=""  rules="date">
				<label for="userNo">대출자번호</label> 
				<input type="text" class="input_140"  id="userNo" name="userNo"  value="">
				 
				<label for="selReqLib">납품도서관</label>
				
				<select class="select_140" id="selReqLib" name="reqLibManageCode" rules="required" disabled="disabled">
					<kaitUi:codeList list="${libList }" />
				</select> <br> 
				
				<label class="pop_label_first" for="title">서명</label> 
				<input type="text" class="input_330" id="title" name="title"  value=""> 
				
				<label class="ml_35" for="author">저자</label>
				<input type="text" class="input_140" id="author" name="author"  value="">
				 
				<label for="publisher">출판사</label>
				<input type="text" class="input_140" id="publisher" name="publisher"  value="">
			</div>
			<div class="btn_wrap">
				<input type="button" class="btn_search" id="btnReqSearch" value="검색">
				<input type="button" class="btn_search" id="btnReqClear" value="초기화">
			</div>
			</form>
			<div class="result_list_del" style="height: 300px; overflow-x:hidden; overflow-y:auto;">
				<table class="result_table" id="reqInfo">
					<colgroup>
						<col width="90px">
						<col width="90px">
						<col width="180px">
						<col width="190px">
						<col width="180px">
						<col width="145px">
						<col width="125px">
						<col width="125px">
					</colgroup>
					<thead>
						<tr>
							<th>번호</th>
							<th>
								<input type="checkbox" id="check_popall" class="check" data-name="chkPopNm" onchange="resAddPop.checkedAll(this);">
								<label for="check_popall" class="check_wrap" ></label>
							</th>
							<th><a href="javascript:;" onclick="resAddPop.sortOrder(this);" data-sort="USER_NO">대출자번호</a></th>
							<th><a href="javascript:;" onclick="resAddPop.sortOrder(this);" data-sort="TITLE">도서명</a></th>
							<th><a href="javascript:;" onclick="resAddPop.sortOrder(this);" data-sort="AUTHOR">저자명</a></th>
							<th><a href="javascript:;" onclick="resAddPop.sortOrder(this);" data-sort="REQ_DATE">신청일</a></th>
							<th><a href="javascript:;" onclick="resAddPop.sortOrder(this);" data-sort="REQ_STATUS_NAME">진행상태</a></th>
							<th><a href="javascript:;" onclick="resAddPop.sortOrder(this);" data-sort="RES_STATUS_NAME">납품상태</a></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td colspan="8">조회된 결과가 없습니다.</td>
						</tr>
					</tbody>
				</table>
			</div>
				<div class="btn_wrap">
					<input type="button" class="btn_search" id="btnParentAdd" value="추가">
				</div>
			<input type="button" class="btn_pop_close" id="btnPopClse">
		</div>
	</div>
	<!------------------------------------------pop end----------------------------------------------->
	
</body>
</html>