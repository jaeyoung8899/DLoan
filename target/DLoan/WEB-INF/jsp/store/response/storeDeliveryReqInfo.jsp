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

	<c:choose>
		<c:when test="${storeYn eq 'Y'}">
			<%@ include file="/WEB-INF/jsp/common/storeHeaderReturn.jsp"%>
		</c:when>
		<c:otherwise>
			<%@ include file="/WEB-INF/jsp/common/storeHeader.jsp"%>
		</c:otherwise>
	</c:choose>

	<div class="content">

		<section class="content_main">
			<img src="<c:url value="/resources/store/images/bookstore-box-subtitle.png" />" alt="" class="h2_square">
			
			<h2 class="h2">납품요청</h2>

			<!-- 검색 조건 -->
			<form class="form-horizontal" name="searchInfoForm" id="searchInfoForm">
				<input type="hidden"  id="resKey"  name="resKey" value="" />
				<section class="search_form_01">
					<div class="search_form_field">
						<label for="selLib" class="search_form_label">납품도서관</label>
						<select class="select_140" id="selLib" name="libManageCode"  title="납품도서관" rules="required"  onchange="javascript:storeDeliveryReqInfo.search();">
							<kaitUi:codeList list="${libList }" select="${libManageCode }" />
						</select>
					</div>

					<div class="search_form_field" style="width: 50%">
						<label class="search_form_label" for="resTitle">납품명</label>
						<input type="text" class="input_415" id="resTitle" name="resTitle" value="${resTitle }" title="납품명" rules="required">
					</div>

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
						<col width="45px">
						<col width="45px">
						<col width="120px">
						<col width="120px;">
						<col width="80px">
						<col width="230px">
						<col width="160px">
						<col width="100px">
						<col width="100px">
						<col width="100px">
					</colgroup>
					<thead>
						<tr>
							<th>번호</th>
							<th>
								<input type="checkbox" id="check_all" class="check" data-name="chkNm" onchange="storeDeliveryReqInfo.checkedAll(this, 'detailInfo');">
								<label for="check_all" class="check_wrap" ></label>
							</th>
							<th>납품도서관</th>
							<th>대출자번호</th>
							<th>이용자명</th>
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
										<td>${row.libManageName}</td>
										<td>${row.userName}</td>
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
									<td colspan="10" style="text-align: center;">조회된 결과가 없습니다.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
				<div class="btn_extra" style="top:5px">
					<input type="button" class="btn_approve" id="btnWindowPrint" value="인쇄">
					<input type="button" class="btn_sms" id="btnExcelDown" value="엑셀다운로드">
				</div>
			</div>
			<div class="btn_wrap" style="margin-bottom:20px">
				<input type="button" class="btn_regist"  id="btnSave" value="저장">
			</div>

		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/storeFooter.jsp"%>
	
	<script type="text/javascript" src="<c:url value="/resources/store/js/response/storeDeliveryReqInfo.js?v=${version}"/>"></script>
	
	
</body>
</html>