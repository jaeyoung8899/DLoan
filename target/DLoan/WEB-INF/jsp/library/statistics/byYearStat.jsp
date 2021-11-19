<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/04_setting.css" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/05_popup.css" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/libHeader.jsp"%>
	<div class="content">
		<section class="content_main">
			<%@ include file="/WEB-INF/jsp/library/statistics/statMenu.jsp"%>
			<section class="setting_cont">
				<form class="form-horizontal" name="yearStatForm" id="yearStatForm">
					<input type="hidden"  id="viewYear" name="viewYear" value="${viewYear }" />
					<input type="hidden"  id="viewMonth" name="viewMonth" value="${viewMonth }" />
					<input type="hidden"  id="sortCol" name="sortCol" value="${sortCol }" />
					<input type="hidden"  id="order"   name="order"   value="${order }" />
					<input type="hidden"  id="start"   name="start" value="${start }" />
					<section class="search_form_01">
						<label for="selStoreId">신청서점</label>
						<select class="select_140" id="selStoreId" name="reqStoreId" style="width:155px">
						<option value="" <c:if test="${reqStoreId eq ''}">selected="selected"</c:if>>전체</option>
						<c:forEach var="row" items="${storeList}" varStatus="status">
							<c:if test="${row.storeId ne null}">
								<option value="${row.storeId}" <c:if test="${reqStoreId eq row.storeId}">selected="selected"</c:if>>${row.storeName}</option>
							</c:if>
						</c:forEach>
						</select>
						<label for="selYear">년도</label>
						<select class="select_140" id="selYear" name="reqYear" style="width:120px">
						</select>
					</section>
					<div class="btn_wrap">
						<input type="button" class="btn_search" value="검색" id="btnSearch">
					</div>
					<%@ include file="/WEB-INF/jsp/library/statistics/yearMenu.jsp"%>
					<div class="btn_wrap_1000 excel">
						
					</div>
					<!-- 전체 -->
					<table class="result_table" id="totalInfo" style="margin-bottom:20px;">
					<colgroup>
						<col width="50px">
						<col width="150px">
						<col width="70px">
						<col width="70px">
						<col width="70px">
						<col width="70px">
						<col width="70px">
						<col width="70px">
						<col width="70px">
						<col width="70px">
						<col width="70px">
						<col width="70px">
						<col width="70px">
						<col width="70px">
						<col width="70px">
					</colgroup>
					<thead>
					<tr>
						<th>번호</th>
						<th><a href="javascript:;" onclick="byYearStat.sortOrder(this, 'NAME');" data-sort="TITLE">이용자명</a></th>
						<th><a href="javascript:;" onclick="byYearStat.sortOrder(this, 'COUNT');"  data-sort="AUTHOR">Total</a></th>
						<th><a href="javascript:;">1월</a></th>
						<th><a href="javascript:;">2월</a></th>
						<th><a href="javascript:;">3월</a></th>
						<th><a href="javascript:;">4월</a></th>
						<th><a href="javascript:;">5월</a></th>
						<th><a href="javascript:;">6월</a></th>
						<th><a href="javascript:;">7월</a></th>
						<th><a href="javascript:;">8월</a></th>
						<th><a href="javascript:;">9월</a></th>
						<th><a href="javascript:;">10월</a></th>
						<th><a href="javascript:;">11월</a></th>
						<th><a href="javascript:;">12월</a></th>
					</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(resultList) > 0}">
							<c:set var="check" value="2000"/>
							<c:set var="countAll" value="0"/>
							<c:set var="count1" value="0"/>
							<c:set var="count2" value="0"/>
							<c:set var="count3" value="0"/>
							<c:set var="count4" value="0"/>
							<c:set var="count5" value="0"/>
							<c:set var="count6" value="0"/>
							<c:set var="count7" value="0"/>
							<c:set var="count8" value="0"/>
							<c:set var="count9" value="0"/>
							<c:set var="count10" value="0"/>
							<c:set var="count11" value="0"/>
							<c:set var="count12" value="0"/>
							
								<c:forEach var="row" items="${resultList}" varStatus="status">
									<tr>
										<td>
										${status.index+1}</td>
										<td>${row.name}</td>
										<td>${row.count}</td>
										<td>${row.one}</td>
										<td>${row.two}</td>
										<td>${row.three}</td>
										<td>${row.four}</td>
										<td>${row.five}</td>
										<td>${row.six}</td>
										<td>${row.seven}</td>
										<td>${row.eight}</td>
										<td>${row.nine}</td>
										<td>${row.ten}</td>
										<td>${row.eleven}</td>
										<td>${row.twelve}</td>
									</tr>
									<c:set var="countAll" value="${countAll+row.count}"/>
									<c:set var="count1" value="${count1+row.one}"/>
									<c:set var="count2" value="${count2+row.two}"/>
									<c:set var="count3" value="${count3+row.three}"/>
									<c:set var="count4" value="${count4+row.four}"/>
									<c:set var="count5" value="${count5+row.five}"/>
									<c:set var="count6" value="${count6+row.six}"/>
									<c:set var="count7" value="${count7+row.seven}"/>
									<c:set var="count8" value="${count8+row.eight}"/>
									<c:set var="count9" value="${count9+row.nine}"/>
									<c:set var="count10" value="${count10+row.ten}"/>
									<c:set var="count11" value="${count11+row.eleven}"/>
									<c:set var="count12" value="${count12+row.twelve}"/>
								</c:forEach>
								<tr style="font-weight:bold;">
									<td>Total</td>
									<td>
									<input type="hidden" id="total_sum" value="${fn:length(resultList)}">
									${fn:length(resultList)}</td>
									<td>${countAll}</td>
									<td>${count1}</td>
									<td>${count2}</td>
									<td>${count3}</td>
									<td>${count4}</td>
									<td>${count5}</td>
									<td>${count6}</td>
									<td>${count7}</td>
									<td>${count8}</td>
									<td>${count9}</td>
									<td>${count10}</td>
									<td>${count11}</td>
									<td>${count12}</td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr style="font-weight:bold;">
									<td>Total</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
					</table>
					
					<c:choose>
					<c:when test="${fn:length(monthsList) > 0}">
					<c:set var="num" value="1" />
					<c:forEach var="month" items="${monthsList}" varStatus="status">
					<!-- 월별 -->
					<table class="result_table" id="monthInfo_${num}" style="display:none; margin-bottom:20px;">
					<colgroup>
						<col width="50px">
						<col width="150px">
						<col width="70px">
						<col width="730px">
					</colgroup>
					<thead>
					<tr>
						<th>번호</th>
						<th><a href="javascript:;" onclick="byYearStat.sortOrder(this, 'NAME',${num});" data-sort="NAME">이용자명</a></th>
						<th><a href="javascript:;" onclick="byYearStat.sortOrder(this, 'COUNT',${num});"  data-sort="COUNT">Total</a></th>
						<th><a href="javascript:;">서명</a></th>
					</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(month) > 0}">
								<c:set var="count" value="0"/>
								<c:forEach var="row" items="${month}" varStatus="status">
									<tr>
										<td>${row.rownum}</td>
										<td>${row.name}</td>
										<td>${row.count}</td>
										<td style="text-align:left; line-height:150%; padding:5px 0px;">
										${row.title }
										</td>
									</tr>
									<c:set var="count" value="${count+row.count }"/>
								</c:forEach>
								<tr style="font-weight:bold;">
									<td>Total</td>
									<td>${fn:length(month)}</td>
									<td>${count}</td>
									<td></td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr style="font-weight:bold;">
									<td>Total</td>
									<td>0</td>
									<td>0</td>
									<td></td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
					</table>
					<c:set var="num" value="${num + 1}" />
					</c:forEach>
					</c:when>
					<c:otherwise>
					<c:forEach var="i" begin="1" end ="12" step="1">
					<table class="result_table" id="monthInfo_${i}" style="display:none; margin-bottom:20px;">
					<colgroup>
						<col width="50px">
						<col width="150px">
						<col width="70px">
						<col width="730px">
					</colgroup>
					<thead>
					<tr>
						<th>번호</th>
						<th><a href="javascript:;">이용자명</a></th>
						<th><a href="javascript:;">Total</a></th>
						<th><a href="javascript:;">서명</a></th>
					</tr>
					</thead>
					<tbody>
						<tr style="font-weight:bold;">
							<td>Total</td>
							<td>0</td>
							<td>0</td>
							<td></td>
						</tr>
					</tbody>
					</table>
					</c:forEach>
					</c:otherwise>
					</c:choose>
				</form>
			</section>
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	<script type="text/javascript" src="<c:url value="/resources/library/js/statistics/byYearStat.js?v=${version }" />"></script>
</body>
</html>