<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/04_setting.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/libHeader.jsp"%>
	<div class="content">
		<section class="content_main">
			<%@ include file="/WEB-INF/jsp/library/envset/envsetMenu.jsp"%>
			<section class="setting_cont">
				<div class="btn_wrap_490" style="width:700px">
					<input type="button" class="btn_add" value="추가" id="btnAdd">
					<input type="button" class="btn_delete" value="삭제" id="btnDel">
				</div>
				<table class="setting_table" id="smsmngTable" style="width:700px">
				<colgroup>
					<col width="40px">
					<col width="30px">
					<col width="150px">
					<col width="480px">
				</colgroup>
				<thead>
				<tr>
					<th>번호</th>
					<th>
						<input type="checkbox" id="check_all" class="check" data-name="chkNm" onchange="smsMng.checkedAll(this, 'smsmngTable');">
						<label for="check_all" class="check_wrap" ></label>
					</th>
					<th>SMS설정명</th>
					<th>설정정보</th>
				</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${fn:length(smsList) > 0}">
							<c:forEach var="row" items="${smsList}" varStatus="status">
								<tr>
									<input type="hidden" name="recKey" value="${row.recKey }"  />
									<input type="hidden" name="autoYn" value="${row.autoYn }"  />
									<td>${row.rnum}</td>
									<td>
										<c:if test="${row.autoYn ne 'Y'}">
											<input type="checkbox" name="chkNm" class="check" id="check_${row.rnum}" value="${row.reqStatus }">
											<label for="check_${row.rnum}" class="check_wrap"></label>
										</c:if>
									</td>
									<td style="text-align:left">
										<c:choose>
											<c:when test="${row.autoYn eq 'Y'}">
											${row.name}
											</c:when>
											<c:otherwise>
											<input type="text" name="smsName" maxlength=33 value="${row.name}" style="width:140px; height:30px;">
											</c:otherwise>
										</c:choose>
									</td>
									<td><input type="text" name="contents" value="${row.contents}" style="width:490px; height:30px;"></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="3">조회된 결과가 없습니다.</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
				</table>
				<div class="sms_setting">
					<div class="sms_setting_cont" style="width:280px">
						<h5><span class="red">● </span> 발송내용 작성방법</h5>
						<p class="sms_p">자동으로 신청자명, 도서관명을 입력하는<br>경우 아래의 키워드를 사용하면 SMS발송 시<br>자동으로 입력됩니다.</p>
						<table class="sms_table" style="width:260px" id="smsInfo">
						<tr>
							<th>키워드</th>
							<th>내용</th>
						</tr>
						<tr>
							<td>$신청자$</td>
							<td>신청자명</td>
						</tr>
						<tr>
							<td>$서점$</td>
							<td>서점명</td>
						</tr>
						<tr>
							<td>$서명$</td>
							<td>서명</td>
						</tr>
						<tr>
							<td>$대출만료일$</td>
							<td>대출만료일</td>
						</tr>
						<tr>
							<td>$반납예정일$</td>
							<td>반납예정일</td>
						</tr>
						<tr>
							<td>$입고예정일$</td>
							<td>입고예정일</td>
						</tr>
						<tr>
							<td>$도서관$</td>
							<td>도서관명</td>
						</tr>
						</table>
					</div>
				</div>
				<div class="btn_wrap" style="width: 60%">
					<input type="button" class="btn_search" value="저장" id="btnSave">
				</div>
			</section>
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	<script type="text/javascript" src="<c:url value="/resources/library/js/envset/smsMng.js?v=${version }" />"></script>
</body>
</html>