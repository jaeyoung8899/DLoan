<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<link rel="stylesheet" href="<c:url value="/resources/library/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/04_setting.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
<link rel="stylesheet" href="<c:url value="/resources/library/css/05_popup.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/libHeader.jsp"%>
	<div class="content">
		<section class="content_main">
			<%@ include file="/WEB-INF/jsp/library/envset/envsetMenu.jsp"%>
			<section class="setting_cont">
				<table class="result_table" id="storeInfo">
				<colgroup>
					<col width="150px">
					<col width="100px">
					<col width="90px">
					<col width="90px">
					<col width="120px">
					<col width="100px">
					<col width="100px">
					<col width="120px">
				</colgroup>
				<thead>
				<tr>
					<th>서점명</th>
					<th>서점ID</th>
					<th>전화번호</th>
					<th>담당자SMS연락처</th>
					<th>금액</th>
					<th>서점MAC</th>
					<th>비밀번호</th>
					<th>로그인차단</th>
				</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${fn:length(store) > 0}">
							<c:forEach var="row" items="${store}" varStatus="status">
								<tr>
									<input type="hidden" name="storeId" value="${row.storeId }" />
									
									<td style="text-align:left; padding-left: 10px;">${row.storeName}</td>
									<td>${row.storeId}</td>
									
									<td><input type="text" name="storePhone" maxlength="11" value="${row.storePhone}" style="width:100%; height:30px"></td>
									<td><input type="button" class="btn_add" style="width:100px" value="전화번호변경" onclick="storeMng.popChgPhone('${row.storeId}')"></td>
									<td><input type="text" name="limitPrice" maxlength="11" value="${row.limitPrice}" style="width:100%; height:30px"></td>
									<td><input type="button" class="btn_add" style="width:100px" value="서점MAC변경" onclick="storeMng.popChgMAC('${row.storeId}')"></td>
									<td><input type="button" class="btn_add" style="width:100px" value="비밀번호변경" onclick="storeMng.popChgPasswd('${row.storeId}')"></td>
									<td>
										<c:if test="${row.loginFailedCnt >= 5}">
											<input type="button" class="btn_delete" style="width:90px" value="차단해제" onclick="javascript:storeMng.failCountReset('${row.storeId}');">
										</c:if>
									</td>
								</tr>
							</c:forEach>
						</c:when>
						<%-- <c:otherwise>
							<tr>
								<td colspan="8">조회된 결과가 없습니다.</td>
							</tr>
						</c:otherwise> --%>
					</c:choose>
				</tbody>
				</table>
			</section>
			<div class="btn_wrap">
				<input type="button" class="btn_search" value="저장" id="btnSave">
			</div>
		</section>
	</div>
	<%@ include file="/WEB-INF/jsp/common/libFooter.jsp"%>
	<!-- 추가 레이어 -->
	<div class="back_black" id="passwdChange" style="display:none">
		<div class="setting_popup_wrap" style="height:230px">
			<form class="" name="changePasswd" id="changePasswd">
				<input type="hidden" id="popStoreId" value="">
				<div class="popup_form_02">
					<label for="chgPasswd" style="width:100px">변경 비밀번호</label>
					<input type="password" class="input_280" id="chgPasswd" title="변경 비밀번호" rules="required"><br>
					<label for="cfmPasswd" style="width:100px">비밀번호 확인</label>
					<input type="password" class="input_280" id="cfmPasswd" title="비밀번호 확인" rules="required"><br>
					<input type="button" class="btn_input" style="margin-left:265px" value="비밀번호변경" id="btnChgPasswd">
				</div>
				<input type="button" class="btn_pop_close" id="btnModalClose">
			</form>
		</div>
	</div>
	<!-- 서점MAC변경 레이어 -->
	<div class="back_black" id="storeMACChange" style="display:none">
		<div class="setting_popup_wrap" style="height:330px; width:480px">
			<form class="" name="changeMAC" id="changeMAC">
				<input type="hidden" id="macStoreId" value="">
				<div class="popup_form_02">
					<label for="allowMAC11" style="width:80px">서점MAC 1.</label>
					<input type="text" id="allowMAC11" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC12" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC13" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC14" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC15" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC16" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()"><br>
					
					<label for="allowMAC21" style="width:80px">서점MAC 2.</label>
					<input type="text" id="allowMAC21" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC22" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC23" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC24" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC25" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC26" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()"><br>
					
					<label for="allowMAC31" style="width:80px">서점MAC 3.</label>
					<input type="text" id="allowMAC31" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC32" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC33" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC34" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC35" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC36" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()"><br>
					
					<label for="allowMAC41" style="width:80px">서점MAC 4.</label>
					<input type="text" id="allowMAC41" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC42" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC43" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC44" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC45" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC46" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()"><br>
					
					<label for="allowMAC51" style="width:80px">서점MAC 5.</label>
					<input type="text" id="allowMAC51" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC52" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC53" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC54" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC55" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()">&nbsp;-&nbsp;
					<input type="text" id="allowMAC56" maxlength="2" style="width:30px; height:30px" onblur="this.value = this.value.toUpperCase()"><br>
					
					<input type="button" class="btn_input" style="margin-left:240px" value="서점MAC변경" id="btnChgMAC">
				</div>
				<input type="button" class="btn_pop_close">
			</form>
		</div>
	</div>

	<div class="back_black" id="phoneChange" style="display:none">
		<div class="setting_popup_wrap" style="height:230px; width:380px">
			<form class="" name="changePhone" id="changePhone">
				<input type="hidden" id="phoneStoreId" value="">
				<div class="popup_form_02">
					<label for="phone0" style="width:100px">전화번호1.</label>
					<input type="text" class="input_160" maxlength="11" id="phone0" title=""/><br>
					<label for="phone1" style="width:100px">전화번호2.</label>
					<input type="text" class="input_160" maxlength="11" id="phone1" title=""><br>
					<label for="phone2" style="width:100px">전화번호3.</label>
					<input type="text" class="input_160" maxlength="11" id="phone2" title=""><br>
					<input type="button" class="btn_input" style="margin-left:185px" value="전화번호변경" id="btnChgPhone">
				</div>
				<input type="button" class="btn_pop_close">
			</form>
		</div>
	</div>

	<script type="text/javascript" src="<c:url value="/resources/library/js/envset/storeMng.js?v=${version }" />"></script>
</body>
</html>