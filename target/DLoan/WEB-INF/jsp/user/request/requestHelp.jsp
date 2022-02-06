<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dloan.common.util.SessionUtils" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta name="viewport" content="width=device-width, user-scalable=no">
	<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
	<link rel="stylesheet" href="<c:url value="/resources/user/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
	<link rel="stylesheet" href="<c:url value="/resources/user/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
	<link rel="stylesheet" href="<c:url value="/resources/user/css/03_content.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
</head>
<body>
<div id="wrap">
	<section class="body">
		<div id="body_contents">
			<ul class="tab_wrap">
				<a href="${ctx}/request"><li>희망도서 서비스 신청</li></a>
				<%if (SessionUtils.getUserId() != null) { %>
					<a href="${ctx}/myRequestInfo"><li>신청현황</li></a>
				<%}%> 
				<a href="${ctx}/requestHelp" class="on"><li>상세이용안내</li></a>
			</ul>
			<div class="tab_button_wrap">
				
			</div>
			<div id="bookstore_info">
				<h3 class="baro_h3"><img src="${ctx}/resources/user/images/icon-blue.png" alt=""> 이용대상</h3>
				<ul class="info_list">
					<li>논산시 도서관 정회원 (연체자는 신청불가)<li>
				</ul>
				<h3 class="baro_h3"><img src="${ctx}/resources/user/images/icon-blue.png" alt=""> 대출기간</h3>
				<ul class="info_list">
					<li>14일 (연장불가)<li>
				</ul>
				<h3 class="baro_h3"><img src="${ctx}/resources/user/images/icon-blue.png" alt=""> 이용안내</h3>
				<table class="info_table">
					<caption class="hidden">희망도서 서비스 이용방법</caption>
					<colgroup>
						<col style="width:16%;">
						<col style="width:auto;">
					</colgroup>
					<tr>
						<th>구 분</th>
						<th>이용안내</th>
					</tr>
					<tr>
						<th rowspan="5">도 서<br>대출방법</th>
						<td style="position:relative;"><div style="display:inline-block; position:absolute; top:13px;">1. 도서관 홈페이지 및 논산시 도서관 앱(</div>
						<div style="display:inline-block; position:absolute; left:230px; top:0px;"><a href="http://lib.nonsan.go.kr/lib/sub02_04.do" target="_blank"><img src="${ctx}/resources/user/images/Libropia.png" alt="Libropia" width=40px; height=40px;></a></div>
						<div style="display:inline-block; position:absolute; top:13px; left:270px;">)에서 도서 신청</div></td>
					</tr>
					<tr>
						<td>2. 서점에 비치된 도서일 경우 : 서점에서 승인 <br>&nbsp;&nbsp; 서점에 미비치된 도서일 경우 : 서점에서 도서준비 -> 승인</td>
					</tr>
					<tr>
						<td>3. 이용자는 대출 가능 SMS 수신 후 7일이내 도서 대출 (회원증+대출비밀번호)<br>&nbsp;&nbsp;※ 서점 보유 도서는 바로 대출가능</td>
					</tr>
					<tr>
						<td>4. 대출 후 2주간 도서 이용</td>
					</tr>
					<tr>
						<td>5. 서점에 도서 반납<br>&nbsp;&nbsp;※ 도서 연체/미반납 시 대출정지, 오·훼손 시 변상, <span style="font-weight: bold;">미대출시 다음 1달간 『서점 바로대출 서비스』 신청 불가</span></td>
					</tr>
				</table>
				<h3 class="baro_h3"><img src="${ctx}/resources/user/images/icon-blue.png" alt=""> 대출서점(협약서점)</h3>
				<table class="info_table02">
					<caption class="hidden">두루두루 서비스 이용대상, 도서대출 기간 및 대출권수, 대출반납 방법, 회원가입 방법, 도서대출/반납, 문의처 정보를 제공합니다.</caption>
					<colgroup>
						<col style="width:22%;">
						<col style="width:58%;">
						<col style="width:12%">
						<col style="width:8%;">
					</colgroup>
					<tr>
						<th>서점명</th>
						<th>주소 및 운영시간</th>
						<th><span class="medi_block">전화</span>번호</th>
						<th>위치</th>
					</tr>
					<tr>
						<th>논산서점</th>
						<td class="lh_20">충청남도 논산시 시민로 415<!-- <br><span class="operationTime">평일, 주말, 공휴일 9시 ~ 22시 </span>--></td>
						<td>734-3565</td>
						<td><a href="https://map.naver.com/?mapmode=0&lng=defa4693cb3c3437e26d36e452882054&pinId=17331461&pinType=site&lat=5a707fa9674fbd677ac2dde1a658725f&dlevel=11&enc=b64" target="_blank"><img src="${ctx}/resources/user/images/map.png" alt="지도보기"></a></td>
					</tr>
					<tr>
						<th>이화서점</th>
						<td class="lh_20">충청남도 논산시 시민로 416<!-- <br><span class="operationTime">평일, 주말, 공휴일 9시 ~ 22시 </span>--></td>
						<td>732-1549</td>
						<td><a href="https://map.naver.com/?mapmode=0&lng=8d5d76087716033c3349ca0a7d7b514d49bf51c2bceffade43d1b5bb6034d0c7&pinId=17326108&lat=255abee7c82bb36ae4ac0477f390f8e0&dlevel=11&enc=b64&pinType=site" target="_blank"><img src="${ctx}/resources/user/images/map.png" alt="지도보기"></a></td>
					</tr>
					
				</table>
				<h3 class="baro_h3"><img src="${ctx}/resources/user/images/icon-blue.png" alt=""> 유의사항</h3>
				<ul class="info_list">
					<li>서점 방문시 회원증을 꼭 지참하시기 바랍니다. <li>
					<li>서점에서 대출한 자료는 도서관 대출 권수에 포함됩니다. </li>
					<li>도서관 회원의 등록은 도서관 홈페이지 회원가입 후, 도서관을 직접 방문하여 신분증 등을 통한 본인확인 절차를 완료한 경우 가능합니다.</li>
					<li>신청하신 도서가 해당도서관에서 이미 소장중인 경우 신청이 제한될 수 있으니, 그 점 유념하시기 바람니다.</li>
					<li>서점에 원하시는 책이 없을 수 있으므로, 서점 방문 전 홈페이지나 앱을 통해 미리 신청하여 주세요! 서점에서 확인 후 문자 드리오니, 그때 방문하시면 불편 없이 이용하실 수 있습니다.</li>
				</ul>
				<h3 class="baro_h3"><img src="${ctx}/resources/user/images/icon-blue.png" alt=""> 신청제외도서</h3>
				<ul class="info_list">
					<li>동일도서가 해당도서관에 소장중인 경우(논산시 도서관 상호대차 서비스로 대출가능도서 신청 제외)</li>
					<li>각종 수험서, 자격증 도서, 문제집, 교과서, 심화단계 전문서, 만화책, 그래픽노블, 라이트노벨, 게임서</li>
					<li>판타지, 무협지, 로맨스, 인터넷 소설 등의 장르문학은 문단에서 인정받은 작품만 일부 허용(예:수상작)
						<%--<br>&nbsp;&nbsp;※ 어린이 학습만화는 도서관으로만 신청 가능, 서점에서 대출 불가--%></li>
					<li>자료의 형태가 도서관 소장도서로 부적합한 도서<br>
						&nbsp;&nbsp;※ 스프링 도서, 50쪽 미만의 소책자, 쓰기익힘책, 워크북, 퍼즐북, 스티커북, 팝업북, 컬러링북, 필사책, 커팅북, 달력, 가계부 등</li>
					<li>원서, 고가의 도서(5만원 이상), 정기간행물, 논문, 전집류, 잡지, 사전류, 지도류, 비도서, 오디오북, 전자책</li>
					<li>출판년도가 오래된 도서(발행 후 1년 이상 경과한 도서), 중고도서, 청소년 정서에 바람직하지 않은 영향을 줄 수 있는 도서 등</li>
				</ul>
				<h3 class="baro_h3"><img src="${ctx}/resources/user/images/icon-blue.png" alt=""> 희망도서 바로대출 신청조회 안내</h3>
				<ul class="info_list">
					<li>신청한 도서의 진행상황 및 처리결과는 ‘희망도서 바로대출’ -> ‘신청현황’ 탭의 진행상태에서 확인가능합니다.<li>
				</ul>
				<table class="info_table">
					<caption class="hidden">희망도서 서비스 신청조회 안내</caption>
					<colgroup>
						<col style="width:16%;">
						<col style="width:auto;">
					</colgroup>
					<tr>
						<th>구 분</th>
						<th>상 태 설 명</th>
					</tr>
					<tr>
						<th>신청중</th>
						<td>이용자가 신청중인 상태</td>
					</tr>
					<tr>
						<th>신청취소</th>
						<td>이용자가 신청을 취소한 상태</td>
					</tr>
					<tr>
						<th>도서준비</th>
						<td>서점에서 도서를 준비하는 상태</td>
					</tr>
					<tr>
						<th>신청거절</th>
						<td>이용자가 신청한 도서를 서점/도서관에서 거절한 상태</td>
					</tr>
					<tr>
						<th>대출대기</th>
						<td>서점에서 이용자에게 도서를 대출하기 위해 대기하는 상태</td>
					</tr>
					<tr>
						<th>대출</th>
						<td>서점에서 이용자에게 도서를 대출해준 상태</td>
					</tr>
					<tr>
						<th>반납</th>
						<td>이용자가 도서를 서점에 반납한 상태</td>
					</tr>
					<tr>
						<th>미대출취소</th>
						<td>대출만기일까지 이용자가 대출을 하지 않은 상태</td>
					</tr>
				</table>
				<h3 class="baro_h3"><img src="${ctx}/resources/user/images/icon-blue.png" alt=""> 문의</h3>
				<ul class="info_list">
					<li>논산시 도서관</li>
				</ul>
				<table class="info_table02">
					<colgroup>
						<col style="width:16.66%">
						<col style="width:16.66%">
						<col style="width:16.66%">
						<col style="width:16.66%">
						<col style="width:16.66%">
						<col style="width:16.66%">
					</colgroup>
					<tbody>
						<tr>
							<th>열린</th>
							<td>746-5990~2</td>
							<th>강경</th>
							<td>746-8980~2</td>
							<th>연무</th>
							<td>746-8990-2</td>
						</tr>
						<!-- <tr>
							<th>화서다산</th>
							<td>228-3547</td>
							<th>호매실</th>
							<td>228-4658</td>
							<th>서수원</th>
							<td>228-4749</td>
						</tr>
						<tr>
							<th>한림</th>
							<td>228-4855</td>
							<th>버드내</th>
							<td>228-4866</td>
							<th>북수원</th>
							<td>228-4778</td>
						</tr>
						<tr>
							<th>대추골</th>
							<td>228-4543</td>
							<th>일월</th>
							<td>228-4890</td>
							<th>광교홍재</th>
							<td>228-4639</td>
						</tr>
						<tr>
							<th>영통</th>
							<td>228-4756</td>
							<th>태장마루</th>
							<td>228-4833</td>
							<th>광교푸른숲</th>
							<td>228-3537</td>
						</tr>
						<tr>
							<th>매여울</th>
							<td>228-3571</td>
							<th>망포글빛</th>
							<td>228-4293</td>
							<th>시스템문의</th>
							<td>228-4732</td>
						</tr> -->
					</tbody>
				</table>
			</div>
		</div>
	</section>
	<script type="text/javascript" src="<c:url value="/resources/user/js/request/requestHelp.js?v=${version }" />" ></script>
</div>
</body>
</html>