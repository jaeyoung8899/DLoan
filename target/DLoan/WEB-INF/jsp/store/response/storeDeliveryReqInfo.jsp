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
			
			<h2 class="h2">납품요청</h2>

			<!-- 검색 조건 -->
			<form class="form-horizontal" name="searchInfoForm" id="searchInfoForm">
				<input type="hidden"  id="resKey"  name="resKey" value="" />
				<section class="search_form_01">
				
					<label class="first_label" for="resTitle">납품명</label> 
					<input type="text" class="input_415" id="resTitle" name="resTitle" value="${resTitle }" title="납품명" rules="required">
					
					
					<label for="selLib">납품도서관</label> 
					<select class="select_140" id="selLib" name="libManageCode"  title="납품도서관" rules="required"  onchange="javascript:storeDeliveryReqInfo.search();">
						<kaitUi:codeList list="${libList }" select="${libManageCode }" />
					</select> <br> 
						
					<label class="first_label" for="res_Date">납품요청일</label> 
					<jsp:useBean id="toDay" class="java.util.Date" />
					<input type="text" class="input_140"  id="res_Date" name="res_Date" value="<fmt:formatDate value="${toDay}" pattern="yyyy-MM-dd" />" disabled="disabled"> 
					
					
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
				
					<p class="table_name">납품도서목록</p>
					
					<table class="result_table" id="detailInfo" >
					<colgroup>
						<col width="90px">
						<col width="90px">
						<col width="180px;">
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
								<input type="checkbox" id="check_all" class="check" data-name="chkNm" onchange="storeDeliveryReqInfo.checkedAll(this, 'detailInfo');">
								<label for="check_all" class="check_wrap" ></label>
							</th>
							<th>대출자번호</th>
							<th>도서명</th>
							<th>저자명</th>
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
										<td>${row.reqDate}</td>
										<td>${row.reqStatusName}</td>
										<td>${row.resStatusName}</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="8" style="text-align: center;">조회된 결과가 없습니다.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
				<div class="btn_extra" style="top:5px">
					<input type="button" class="btn_approve" id="btnWindowPrint" value="인쇄">
					<input type="button" class="btn_sms" id="btnExcelDown" value="엑셀다운로드">
				</div>
				<div class="btn_wrap" style="margin-bottom:20px">
					<input type="button" class="btn_regist"  id="btnSave" value="저장">
				</div>
			</div>
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/storeFooter.jsp"%>
	
	<script type="text/javascript" src="<c:url value="/resources/store/js/response/storeDeliveryReqInfo.js?v=${version}"/>"></script>
	
	
</body>
</html>