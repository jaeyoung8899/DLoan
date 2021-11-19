<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>

<link rel="stylesheet" href="<c:url value="/resources/store/css/00_reset.css?v=${cssver}" />" type="text/css" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/store/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/store/css/03_content.css?v=${cssver}" />" type="text/css" title="no title" charset="utf-8"/>
</head>
<body>

	<%@ include file="/WEB-INF/jsp/common/storeHeader.jsp"%>

	<div class="content">

		<section class="content_main">
			<img src="<c:url value="/resources/store/images/bookstore-box-subtitle.png" />" alt="" class="h2_square">
			
			<h2 class="h2">납품요청내역</h2>
			
			<!-- 검색 조건 -->
			<form name="requestInfoForm" id="requestInfoForm">
				<input type="hidden"  id="sortCol" name="sortCol" value="${sortCol }" />
				<input type="hidden"  id="order" name="order"   value="${order }" />
				<input type="hidden"  id="start"  name="start" value="${start}" />
				<input type="hidden"  name="display" value="10" />
				<section class="search_form_01"> 
					<label for="selStatus" class="first_label">납품진행상태</label> 
					
					<select class="select_140" id="selStatus" name="resStatus" >
						<option value="" <c:if test="${resStatus eq ''}">selected="selected"</c:if>>전체</option>
						<option value="S01" <c:if test="${resStatus eq 'S01'}">selected="selected"</c:if>>납품요청</option>
						<option value="L01" <c:if test="${resStatus eq 'L01'}">selected="selected"</c:if>>납품승인</option>
						<option value="S02" <c:if test="${resStatus eq 'S02'}">selected="selected"</c:if>>납품완료</option>
					</select> 
					<label for="selLib">납품도서관</label> 
					<select class="select_140" id="selLib" name="libManageCode" >
						<option value=""<c:if test="${libManageCode eq ''}">selected="selected"</c:if>>전체</option>
						<kaitUi:codeList list="${libList }" select="${libManageCode }" />
					</select> 
					<label for="from_resDate">납품요청일</label> 
					<input type="text" class="input_120"  id="from_resDate" name="from_resDate" title="신청시작일" value="${from_resDate }" rules="date">
					~
					<input type="text" class="input_120"  id="to_resDate" name="to_resDate" title="신청종료일" value="${to_resDate }"  rules="date">
				</section>
				<div class="btn_wrap">
					<input type="button" class="btn_search"  id="btnSearch" value="검색">
					<input type="button" class="btn_search"  id="btnClear" value="초기화">
				</div>
			</form>
		
		<!-- 목록 -->
			<div class="result_list_del">
				<table class="result_table" id="responseInfo">
					<colgroup>
						<col width="90px">
						<col width="90px">
						<col width="180px;">
						<col width="190px">
						<col width="180px">
						<col width="145px">
						<col width="125px">
					</colgroup>
					<thead>
						<tr>
							<th style="text-align: center;">No.</th>
							<th style="text-align: center;">
								<input type="checkbox" id="check_all" class="check" data-name="chkNm" onchange="storeResponse.checkedAll(this, 'responseInfo');">
								<label for="check_all" class="check_wrap" ></label>
							</th>
							<th style="text-align: center;"><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeResponse.search);" data-sort="LIB_MANAGE_NAME">납품<br>도서관</a></th>
							<th style="text-align: center;"><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeResponse.search);" data-sort="RES_TITLE">납품명</a></th>
							<th style="text-align: center;"><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeResponse.search);" data-sort="RES_DATE">납품요청일</a></th>
							<th style="text-align: center;"><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeResponse.search);" data-sort="BOOK_COUNT">납품도서수</a></th>
							
							<th style="text-align: center;"><a href="javascript:;" onclick="comm.sort.sortOrder(this, storeResponse.search);" data-sort="RES_STATUS_NAME">진행상태</a></th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(resultList) > 0}">
								<c:forEach var="row" items="${resultList}" varStatus="status">
									<tr>
										<input type="hidden" name="recKey" value="${row.recKey }"  />
										<td>${row.rnum}</td>
										<td>
											<input type="checkbox" id="check_${row.rnum}"  name="chkNm" class="check" value="${row.resStatus }">
											<label for="check_${row.rnum}" class="check_wrap"></label>
										</td>
										<td>${row.libManageName}</td>
										<td style="text-align:left;"><a href="javascript:;" onclick="javascript:document.location.href='${ctx}/store/stroeResponseInfoDetail?resKey=${row.recKey}';">${row.resTitle}</a></td>
										<td><fmt:formatDate value="${row.resDate}" pattern="yyyy-MM-dd"/></td>
										<td>${row.bookCount}</td>
										<td>${row.resStatusName}</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="7" style="text-align: center;">조회된 결과가 없습니다.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
				<div class="btn_extra">
					<input type="button" class="btn_approve" id="btnWindowPrint" value="인쇄">
					<input type="button" class="btn_sms" id="btnExcelDown" value="엑셀다운로드">
				</div>
				<div class="btn_wrap">
					<input type="button" class="btn_complete" id="btnComplate" value="납품완료">
				</div>
				<c:if test="${not empty pageInfo}">
				<div style="text-align: center;">
					<kaitUi:paging pageInfo='${pageInfo}'  jsFunction="storeResponse.search" />
				</div>
				</c:if>
				
			</div>
		</section>
	</div>
	
	<%@ include file="/WEB-INF/jsp/common/storeFooter.jsp"%>
	
	<script type="text/javascript" src="<c:url value="/resources/store/js/response/storeResponse.js?v=${version}"/>"></script>
	
</body>
</html>