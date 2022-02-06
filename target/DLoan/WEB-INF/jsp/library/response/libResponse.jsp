<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>

<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" />
<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" />
<link rel="stylesheet" href="<c:url value="/resources/library/css/03_approver_delivery.css?v=${cssver}" />" type="text/css" media="screen" title="no title" />
</head>
<body>

	<%@ include file="/WEB-INF/jsp/common/libHeader.jsp"%>

	<div class="content">

		<section class="content_main">
			<img src="<c:url value="/resources/library/images/library-box-subtitle.png" />" alt="" class="h2_square">
			
			<h2 class="h2">납품요청</h2>
			
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
				<div class="result_list_wrapper">
					<table class="result_table" id="responseInfo">
						<colgroup>
							<col width="90px">
							<col width="130px;">
							<col width="150px;">
							<col width="190px">
							<col width="180px">
							<col width="145px">
							<col width="125px">
						</colgroup>
						<thead>
							<tr>
								<th>No.</th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libResponse.search);" data-sort="LIB_MANAGE_NAME">납품<br>도서관</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libResponse.search);" data-sort="STORE_NAME">납품서점</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libResponse.search);" data-sort="RES_TITLE">납품명</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libResponse.search);" data-sort="RES_DATE">납품요청일</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libResponse.search);" data-sort="BOOK_COUNT">납품도서수</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libResponse.search);" data-sort="RES_STATUS">진행상태</a></th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${fn:length(resultList) > 0}">
									<c:forEach var="row" items="${resultList}" varStatus="status">
										<tr>
											<input type="hidden" name="recKey" value="${row.recKey }" />
											<td>${row.rnum}</td>
											<td>${row.libManageName}</td>
											<td>${row.storeName}</td>
											<td style="text-align:left;"><a href="javascript:;" onclick="javascript:document.location.href='${ctx}/lib/libResponseInfoDetail?resKey=${row.recKey}';">${row.resTitle}</a></td>
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
				</div>
				<c:if test="${not empty pageInfo}">
					<div style="text-align: center;">
						<kaitUi:paging pageInfo='${pageInfo}'  jsFunction="libResponse.search" />
					</div>
				</c:if>
				<div class="btn_extra">
					<input type="button" class="btn_approve" value="엑셀다운로드" id="btnReqExcel">
				</div>
			</div>
		</section>
	</div>
	
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	
	<script type="text/javascript" src="<c:url value="/resources/library/js/response/libResponse.js?v=${version}"/>"></script>
	
</body>
</html>