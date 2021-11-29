<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" />
<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" />
<link rel="stylesheet" href="<c:url value="/resources/library/css/04_setting.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<style type="text/css">
.search_form_01, .btn_wrap, .result_table {width:100%}
</style>
</head>
<body>

	<%@ include file="/WEB-INF/jsp/common/libHeader.jsp"%>

	<div class="content">

		<section class="content_main">
			<%@ include file="/WEB-INF/jsp/library/spending/spendingMenu.jsp"%>
			<section class="setting_cont">
				<!-- 검색 조건 -->
				<form name="requestInfoForm" id="requestInfoForm">
					<input type="hidden"  id="sortCol" name="sortCol" value="${sortCol }" />
					<input type="hidden"  id="order" name="order"   value="${order }" />
					<input type="hidden"  id="start"  name="start" value="${start}" />
					<input type="hidden"  name="display" value="10" />
					<section class="search_form_01"> 
						<label for="selLib">신청 서점</label> 
						<select class="select_140" id="selStore" name="storeId" >
							<option value=""<c:if test="${storeId eq ''}">selected="selected"</c:if>>전체</option>
							<kaitUi:codeList list="${storeList }" select="${storeId }" />
						</select> 
						<label for="selLib">납품 도서관</label> 
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
						<input type="button" class="btn_search"  id="btnRefundSearch" value="검색">
						<input type="button" class="btn_search"  id="btnClear" value="초기화">
					</div>
				</form>
		
				<!-- 목록 -->
				<div class="result_list">
					<div class="btn_wrap" style="margin-bottom:10px">
						<input type="button" class="btn_add" value="엑셀다운로드" id="btnRefundExcel" style="width:110px">
					</div>
					<table class="result_table" id="libRefundInfo">
						<colgroup>
							<col width="50px">
							<col width="110px">
							<col width="110px">
							<col width="200px">
							<col width="120px">
							<col width="130px">
							<col width="100px">
							<col width="180px">
						</colgroup>
						<thead>
							<tr>
								<th>No.</th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRefund.search);" data-sort="STORE_NAME">신청서점</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRefund.search);" data-sort="LIB_NAME">납품도서관</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRefund.search);" data-sort="TITLE">서명</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRefund.search);" data-sort="AUTHOR">저자명</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRefund.search);" data-sort="PUBLISHER">출판사</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libRefund.search);" data-sort="RES_DATE">납품요청일</a></th>
								<th>반품사유</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${fn:length(resultList) > 0}">
									<c:forEach var="row" items="${resultList}" varStatus="status">
										<tr>
											<td>${row.rnum}</td>
											<td>${row.storeName}</td>
											<td>${row.libName}</td>
											<td>${row.title}</td>
											<td>${row.author}</td>
											<td>${row.publisher}</td>
											<td><fmt:formatDate value="${row.resDate}" pattern="yyyy MM-dd"/></td>
											<td>${row.resRemark}</td>
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
					<c:if test="${not empty pageInfo}">
						<div style="text-align: center;">
							<kaitUi:paging pageInfo='${pageInfo}'  jsFunction="libRefund.search" />
						</div>
					</c:if>
				</div>
			</section>
		</section>
	</div>
	
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	<script type="text/javascript" src="<c:url value="/resources/library/js/spending/libSpending.js?v=${version}"/>"></script>
	
</body>
</html>