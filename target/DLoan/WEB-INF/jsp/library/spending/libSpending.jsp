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
			<!-- 검색 조건 -->
			<section class="setting_cont">
				<form name="requestInfoForm" id="requestInfoForm">
					<input type="hidden"  id="sortCol" name="sortCol" value="${sortCol }" />
					<input type="hidden"  id="order" name="order"   value="${order }" />
					<input type="hidden"  id="start"  name="start" value="${start}" />
					<input type="hidden"  name="display" value="10" />
					<section class="search_form_01">
						<label for="selLib" class="search_form_label">납품도서관</label>
						<select class="select_180" id="selLib" name="libManageCode">
							<c:if test="${sessionScope.LIB_MNG_CD eq null or sessionScope.LIB_MNG_CD eq ''}">
								<option value="">전체</option>
							</c:if>
							<kaitUi:codeList list= "${libList}" select="${libManageCode}" /> 
						</select>
						<label for="fiscalYear" class="search_form_label">회계연도</label> 
						<input type="text" class="input_120"  id="fiscalYear" name="fiscalYear" title="회계연도" value="${fiscalYear}">
					</section>
					<div class="btn_wrap">
						<input type="button" class="btn_search"  id="btnSearch" value="검색" >
					</div>
				</form>
			</section>
		
			<!-- 목록 -->
			<section class="setting_cont">
				<div class="result_list">
					<div class="btn_wrap" style="margin-bottom:10px">
						<input type="button" class="btn_add" value="등록" id="btnRegister">
					</div>
					<table class="result_table" id="spendingInfo">
						<colgroup>
							<col width="30px">
							<col width="140px;">
							<col width="80px;">
							<col width="100px">
							<col width="150px;">
							<col width="160px">
							<col width="160px">
						</colgroup>
						<thead>
							<tr>		
								<th>No.</th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libSpending.search);" data-sort="LIB_NAME">납품도서관</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libSpending.search);" data-sort="CLASS">구분</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libSpending.search);" data-sort="FISCAL_YEAR">회계연도</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libSpending.search);" data-sort="BUDGET">금액</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libSpending.search);" data-sort="DEADLINE">회기말</a></th>
								<th><a href="javascript:;" onclick="comm.sort.sortOrder(this, libSpending.search);" data-sort="REG_DATE">등록일</a></th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${fn:length(resultList) > 0}">
									<c:forEach var="row" items="${resultList}" varStatus="status">
										<tr>
											<td>${row.rnum}</td>
											<td><a href="/lib/spending/${row.recKey }">${row.libName}</a></td>
											<td>${row.classDesc}</td>
											<td>${row.fiscalYear}</td>
											<td><fmt:formatNumber value="${row.budget}" pattern="###,###,###"/></td>
											<td>${row.deadline}</td>
											<td>${row.regDate}</td>
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
					<c:if test="${not empty pageInfo}">
						<div style="text-align: center;width:100%;min-width:1000px">
							<kaitUi:paging pageInfo='${pageInfo}'  jsFunction="libSpending.search" />
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