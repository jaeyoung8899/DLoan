<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dloan.common.util.SessionUtils" %>
<%@ page import="java.util.Date, java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta name="viewport" content="width=device-width, user-scalable=no">
	<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
	<link rel="stylesheet" href="<c:url value="/resources/user/css/00_reset.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
	<link rel="stylesheet" href="<c:url value="/resources/user/css/01_header_footer_common.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
	<link rel="stylesheet" href="<c:url value="/resources/user/css/03_content.css?v=${cssver}" />" type="text/css" media="screen" title="no title" charset="utf-8"/>
	<c:if test="${pageNo eq null}">
		<script>
			$(function(){
				var filter = "win16|win32|win64|mac";
				if(navigator.platform){
					if(0 > filter.indexOf(navigator.platform.toLowerCase())){
						// mobile
						//alert('msg');
					} else {
						// pc
					}
				}
			});
		</script>
	</c:if>
</head>
<body>
<div id="wrap">
	<section class="body">
	<div id="body_contents">
		
		<%if (SessionUtils.getUserId() != null) { %>
		
		<h3 class="baro_h3 exception">
			신청 제외대상
		</h3>
		<div class="search_form_01">
		<ul class="info_list">
			<li>도서관 소장자료 또는 구입중인 도서</li>
			<li>수험서, 문제집, 참고서, 전문서적(교재) 등 개인 학습을 위한 자료</li>
			<li>판타지, 무협지, 로맨스, 전집류</li>
			<li>만화류(학습만화 포함), 라이트노벨, 그래픽노블, POD(주문제작도서), 게임서적 등 각종 오락용 출판물, 화보집, 사진집 </li>
			<li>미출간도서(예약판매도서 등), 품절 및 절판도서</li>
			<li>당해연도 기준으로 출판년도가 2년 이상 경과한 도서</li> 
			<li>정가 5만원 이상의 고가도서</li>
			<li>서지정보가 불명확한 도서</li>
			<li>자료의 형태가 도서관 자료로 부적합한 도서<br>&nbsp;&nbsp; ※ 스프링도서, 사운드북, 퍼즐북, 스티커북, 컬러링북, 팝업북, 병풍책, 워크북, 50쪽 미만의 소책자, 가계부 등</li>
			<li>비도서(전자책, 오디오북, DVD 등), 원서(해외 주문도서 포함), 참고 도서(사전, 연감, 백서 등), 간행물, 논문 등</li>
			<li>영리를 목적으로 신청하는 경우(본인이 저자 등), 개인적인 성향이 강한  종교자료</li>
			<li>청소년 정서에 바람직하지 않은 영향을 줄 수 있는 도서 등<br>&nbsp;&nbsp; ※ 간행물 윤리 위원회에서 선정한 유해도서</li>
			<li>기타 도서관 자료로서 부적합하다고 판단되는 도서</li>
		</ul>
	</div>
		
		<h3 class="baro_h3">
			서비스 신청
		</h3>
		<div class="search_form_01">
			<form name="requestForm" id="requestForm">
				<fieldset>
					<legend class="hidden">신청자정보</legend>
					<input type="hidden" id="reqType" name="reqType" 
						value="<c:choose><c:when test="${reqType eq null }">library</c:when><c:otherwise>${reqType }</c:otherwise></c:choose>"/>
					<div class="input_wrap">
						<label for="userNm">신청자명</label>
						<input type="text" class="input_170" id="userNm" name="userNm"  value="${sessionScope.USER_NM }" readonly="readonly">
					</div>
					<div class="input_wrap">
						<label for="userNo">대출자번호</label>
						<input type="text" class="input_170" id="userNo" name="userNo" value="${sessionScope.USER_NO }" readonly="readonly">
					</div>
					<div class="input_wrap">
						<label for="phone1">연락처</label>
						<c:set var="tel" value="${fn:split(sessionScope.HANDPHONE, '-')}" />
						<c:choose>
							<c:when test="${not empty tel}">
								<input type="number" class="input_50" id="phone1" name="phone1" value="${fn:split(sessionScope.HANDPHONE,'-')[0]}" maxlength=4 style="display: inline-block;" autocomplete="off">
								<input type="number" class="input_50" id="phone2" name="phone2" value="${fn:split(sessionScope.HANDPHONE,'-')[1]}" maxlength=4 style="display: inline-block;" autocomplete="off" title="연락처 두번째입력란">
								<input type="number" class="input_50" id="phone3" name="phone3" value="${fn:split(sessionScope.HANDPHONE,'-')[2]}" maxlength=4 style="display: inline-block;" autocomplete="off" title="연락처 세번째입력란">
							</c:when>
							<c:otherwise>
								<input type="number" class="input_50" id="phone1" name="phone1" value="" maxlength=3 style="display: inline-block;" autocomplete="off">
								<input type="number" class="input_50" id="phone2" name="phone2" value="" maxlength=4 style="display: inline-block;" autocomplete="off" title="연락처 두번째입력란">
								<input type="number" class="input_50" id="phone3" name="phone3" value="" maxlength=4 style="display: inline-block;" autocomplete="off" title="연락처 세번째입력란">
							</c:otherwise>
						</c:choose>
					</div>
					<div class="input_wrap">
						<label for="newPhone1" class="orange">연락처 확인</label>
						<input type="number" class="input_50" id="newPhone1" name="newPhone1" title="연락처확인 첫번째입력란" value="${newPhone1 }" maxlength=3 rules="required|numeric" style="display: inline-block;" autocomplete="off">
						<input type="number" class="input_50" id="newPhone2" name="newPhone2" title="연락처확인 두번째입력란" value="${newPhone2 }" maxlength=4 rules="required|numeric" style="display: inline-block;" autocomplete="off">
						<input type="number" class="input_50" id="newPhone3" name="newPhone3" title="연락처확인 세번째입력란" value="${newPhone3 }" maxlength=4 rules="required|numeric" style="display: inline-block;" autocomplete="off">
					</div>
				</fieldset>
			</form>
		</div>
		<div>
			<!-- <h3 class="baro_h3 n_h3">
				신청장소
			</h3> -->
		</div>
	
		<div class="search_form_01" data-group="bookstore" style="margin-top:10px;">
			<div class="input_wrap">
				<label for="newStore">신청서점</label>
				<select class="select_200" id="selStore" name="bookstore" rules="required2" title="신청서점">
						<option value="" <c:if test="${bookstore eq null}">selected="selected"</c:if>>서점 신청</option>
						<kaitUi:codeList list="${storeList }" select="${bookstore }" />
				</select>
			</div>
			<div class="input_wrap">
				<label for="smsY">SMS수신</label>
				<input type="checkbox" name="smsYn" id="smsY" class="check" title="SMS수신여부" <c:if test="${smsYn eq 'Y' || smsYn eq null}">checked</c:if>>
				<label for="smsY" class="check_wrap" style="vertical-align: text-bottom;" ></label>
				<label for="smsY" class="up">수신</label>
			</div>
		</div>
		<div class="search_form_02" id="input_form2">
			<form name="bookSearchForm" id="bookSearchForm">
				<input type="hidden"  name="display" value="10" />
				<input type="hidden"  id="pageNo" name="pageNo" value="${pageNo}" />
				<div class="input_wrap">
					<label for="d_titl">도서명</label>
					<input type="text" class="input_120" id="d_titl" name="d_titl" value="${d_titl }">
				</div>
				<div class="input_wrap">
					<label for="d_auth">저자</label>
					<input type="text" class="input_120" id="d_auth" name="d_auth" value="${d_auth }">
				</div>
				<div class="input_wrap">
					<label for="d_publ">출판사</label>
					<input type="text" class="input_120" id="d_publ" name="d_publ" value="${d_publ }">
				</div>
				<div class="input_wrap">
					<input type="button" class="btn_search" id="btnSearch">
				</div>
			</form>
		</div>
		<div class="book_list_wrap" id="book_list_wrap">
			<c:if test="${not empty pageInfo}">
			<c:choose>
				<c:when test="${not empty pageInfo}">
					<p>검색건수 : <span class="count">${pageInfo.totalRecordCount}</span>건</p>
				</c:when>
				<c:otherwise>
					<p>검색건수 : <span class="count">0</span>건</p>
				</c:otherwise>
			</c:choose>
			<ul class="book_list">
				<c:choose>
					<c:when test="${fn:length(bookList) > 0}">
						<c:forEach var="row" items="${bookList}" varStatus="status">
							<li class="book_info">
								<input type="hidden" name="title"     value="${row.title}" />
								<input type="hidden" name="imgUrl"    value="${row.image}" />
								<input type="hidden" name="isbn"      value="${row.isbn}" />
								<input type="hidden" name="author"    value="${row.author}" />
								<input type="hidden" name="publisher" value="${row.publisher}" />
								<input type="hidden" name="price"     value="${row.price}" />
								<input type="hidden" name="pubdate"   value="${row.pubdate}" />
								<input type="hidden" name="link"   value="${row.link}" />
								<a href="${row.link}" target="_blank"><img src="${row.image}" alt="책표지"></a>
								<dl class="">
									<dt>${row.title}</dt>
									<dd>${row.author}</dd>
									<dd>${row.publisher}</dd>
									<dd>${row.isbn}</dd>
									<dd class="price_hidden"><fmt:formatNumber value="${row.price}" /></dd>
									<dd class="year_hidden">${fn:substring(row.pubdate,0,4)}년</dd>
								</dl>
					<%	if ("ECO1"   .equals(SessionUtils.getUserId()) ||
							"ECO2"   .equals(SessionUtils.getUserId()) ||
							"ECO3"   .equals(SessionUtils.getUserId()) ||
							"ECO4"   .equals(SessionUtils.getUserId()) ||
							"ECO5"   .equals(SessionUtils.getUserId()) ||
							"ECO6"   .equals(SessionUtils.getUserId()) ||
							"ECO7"   .equals(SessionUtils.getUserId()) ||
							"ECO8"   .equals(SessionUtils.getUserId()) ||
							"ECO9"   .equals(SessionUtils.getUserId()) ||
							"ECO10"  .equals(SessionUtils.getUserId()) ||
							"ECO11"  .equals(SessionUtils.getUserId()) ||
							"ECO12"  .equals(SessionUtils.getUserId()) ||
							"ECO13"  .equals(SessionUtils.getUserId()) ||
							"ECO14"  .equals(SessionUtils.getUserId()) ||
							"shy0629".equals(SessionUtils.getUserId()) ) { %>
								<input type="button" class="btn_apply" value="바로대출 신청" onclick="request.userRequest(this);">
					<%	} else {
							if (new Date().compareTo(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2017/08/01 00:00:00")) < 0) { %>
								<input type="button" class="btn_apply" value="바로대출 신청" onclick="alert('8월 1일부터 서비스 시작합니다.');">
					<%		} else {
								if (new Date().compareTo(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2017/08/01 09:00:00")) < 0) { %>
								<input type="button" class="btn_apply" value="바로대출 신청" onclick="alert('8월 1일 오전9시 부터 신청 가능합니다.');">
					<%			} else { %>
								<input type="button" class="btn_apply" value="바로대출 신청" onclick="request.userRequest(this);">
					<%			}
							}
						} %>
							</li>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<li class="book_info">
							<dl style="text-align:center; width:100%; padding-top:40px">
							<c:if test="${errorMessage eq null}"><dt>조회된 결과가 없습니다.</dt></c:if>
							<c:if test="${errorMessage ne null && errorCode eq 'SE03'}">
								<dt>검색결과가 1000건 이상으로 데이터가 너무 많습니다. 검색조건을 더 많이 입력해주시기 바랍니다.</dt>
							</c:if>
							<c:if test="${errorMessage ne null && errorCode ne 'SE03'}">
								<dt>${errorMessage} 다시 검색해 주시기 바랍니다.</dt>
							</c:if>
							</dl>
						</li>
					</c:otherwise>
				</c:choose>
			</ul>
			</c:if>
			<c:if test="${not empty pageInfo}">
			<div class="col-lg-12" style="text-align: center;">
				<kaitUi:paging pageInfo='${pageInfo}' jsFunction="request.search" pagingType="Home" />
			</div>
			</c:if>
		</div>
<%		} else { %>
		<h3 class="baro_" style="text-align:center">
			<a href="javascript:;" style="font-size:12px; background-color:#0490cb; color:white; padding:5px 15px; border-radius:5px" onclick="alert('도서관 정회원으로 로그인 후 이용 해 주시기 바랍니다.')">서점 바로대출 서비스 신청하기</a>
		</h3>
<%		} %>
	</div>
	<form name="time_check_form" id="time_check_form">
	<%if (new Date().compareTo(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2017/09/30 00:00:00")) > 0 && new Date().compareTo(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2017/10/09 00:00:00")) < 0) { %>
		<input type="hidden" name="Chuseok" value="Y" id="Chuseok">
	<%}else{ %>
		<input type="hidden" name="Chuseok" value="N" id="Chuseok">
	<%} %>
	</form>
	<script type="text/javascript" src="<c:url value="/resources/user/js/request/request.js?v=${version }" />" ></script>
<c:if test="${not empty pageInfo}">
<script>
location.href = "#book_list_wrap";
</script>
</c:if>
<iframe id="inneriframe" width="0" height="0"></iframe>
<script type="text/javascript">          
function rsize() {       
        var iframe = document.getElementById("inneriframe");
        var wrapper = document.getElementById("wrap");
        var innerC = document.body;            
        var height = wrapper.offsetHeight;
        console.log(height);
        iframe.src  = "https://lib.nonsan.go.kr/iframeAutoResize.htm?height="+height;
}   
window.onload = rsize;
</script>
</section>
</div>
</body>
</html>