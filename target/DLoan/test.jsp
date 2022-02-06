<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>시립도서관 도서검색 - 선경도서관</title>
<meta name="title" content="선경도서관"/>
<meta name="author" content="선경도서관"/>
<meta name="keywords" content="선경도서관, 수원시, 도서관사업소"/>


<!-- css -->
<link rel="stylesheet" type="text/css" href="http://www.suwonlib.go.kr/ssc/sub_all.css" />

<!-- js -->
<script type="text/javascript" src="http://www.suwonlib.go.kr/P_js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="http://www.suwonlib.go.kr/P_js/jquery.easing.1.3.js"></script>
<script type="text/javascript" src="http://www.suwonlib.go.kr/P_js/gnb.js"></script>
<script type="text/javascript" src="http://www.suwonlib.go.kr/P_js/js.js"></script>

<script type="text/javascript" src="http://www.suwonlib.go.kr/P_js/mt_navi/modernizr.custom.js"></script>
<script type="text/javascript" src="http://www.suwonlib.go.kr/P_js/mt_navi/pushy.min.js"></script> 

<!-- IE9 미만에서 HTML5 엘리먼트 지원 -->
<!--[if lt IE 9]>
    <script type="text/javascript" src="http://www.suwonlib.go.kr/P_js/respond.min.js"></script>
    <script type="text/javascript" src="http://www.suwonlib.go.kr/P_js/IE9.js"></script>
    <![endif]-->

<link rel="stylesheet" type="text/css" href="http://www.suwonlib.go.kr/A-LibM/style.css" />

</head>

<body id="section1">


  <div id="container"> 
  <!--skipnavigation Start-->

<dl id="skipnavigation">
  <dt><strong class="skip">바로가기 메뉴</strong></dt>
  <dd><a href="#body_contents" class="skip_navi">본문 바로가기</a></dd>
</dl>
<noscript>
*  본 브라우저는 스크립트를 지원하지 않습니다. 홈페이지가 정상적으로 보이지 않을때는 최신 브라우저를 설치하여 사용하시길 권장합니다.
</noscript>
<!--//#skipnavigation End-->


<header id="headerWrap" class="topbg_sunkyung">
  <div id="header"> 
    <!--글로벌Start-->
    <div class="global">
      <h2 class="skip">글로벌메뉴</h2>
		<a href="javascript:;">홈</a>
	
		<a href="javascript:;">로그인</a>
		<a href="javascript:;">회원가입</a>
	
		<a href="javascript:;">사이트맵</a>
		<a class="last last_sunkyung" href="javascript:;" target="_blank" title="새창열림"><span>인트로바로가기</span></a>
	</div>
	
    <!--글로벌End --> 
  </div>
  

  <nav id="gnbDiv">
    <div class="gnbDiv" style="height:92px"> <!--//활성화시 350px/ 비활성화시 92px-->
      <div class="gnbWrap">
        <h1><a href="javascript:;"><img src="http://www.suwonlib.go.kr/images/cni/logo_sunkyung.gif" alt="선경도서관" /></a></h1>
        <!-- SideBtn -->
		
        <button class="SideBtn">SideNavi</button>
        <button class="homeBtn">Home</button>

		
        
        <!-- gnb -->
        <div class="gnb">
          <ul>
            <li class="one"> <a class="oneDep disB" href="javascript:;">이용안내</a>
              <div class="guideBg">
                <p class="tit">이용안내</p>
                <p class="txt">도서관 이용안내에 대한 정보를 제공합니다.</p>
                <p class="img"><img src="http://www.suwonlib.go.kr/images/gmi/cni/lnb_01.jpg" alt=""/></p>
              </div>
              <p class="twoDep">
				<a class="disB off" href="javascript:;">이용 및 회원안내</a>
				
				<!-- 창룡 > 열람실이용 메뉴 없음 -->
				<!-- 버드내 > 열람실이용 메뉴 없음 -->
				<!-- 호매실 > 열람실이용 메뉴 없음 -->
				<!-- 한림 > 열람실이용 메뉴 없음 -->
				<!-- 대추골 > 열람실이용 메뉴 없음 -->
				<!-- 일월 > 열람실이용 메뉴 없음 -->
				<a class="disB off" href="javascript:;">열람실이용</a>
				
				<a class="disB off" href="javascript:;">대출회원가입</a>
				<a class="disB off" href="javascript:;">도서관서비스</a>
				<a class="disB off" href="javascript:;">모바일 앱 이용안내</a> 
				<a class="disB off" href="javascript:;">FAQ</a>
			 </p>
            </li>
            <li class="two"> <a class="oneDep disB" href="javascript:;">자료찾기</a>
              <div class="guideBg">
                <p class="tit">자료찾기</p>
                <p class="txt">도서관 자료를 찾으실 수 있습니다.</p>
                <p class="img"><img src="http://www.suwonlib.go.kr/images/gmi/cni/lnb_02.jpg" alt=""/></p>
              </div>
              <p class="twoDep">
				<a class="disB off" href="javascript:;">시립도서관 도서검색</a>
				<a class="disB off" href="javascript:;">지역도서관 통합검색</a>
				<a class="disB off" href="javascript:;">추천도서</a>
				<a class="disB off" href="javascript:;">신간안내</a>
				<a class="disB off" href="javascript:;">베스트대출</a>
				<a class="disB off" href="javascript:;">특화자료(수원학)</a>
				
				

			  </p>
            </li>
            <li class="three"> <a class="oneDep disB" href="javascript:;">문화행사</a>
              <div class="guideBg">
                <p class="tit">문화행사</p>
                <p class="txt">문화행사와 관련된 정보를 제공합니다.</p>
                <p class="img"><img src="http://www.suwonlib.go.kr/images/gmi/cni/lnb_03.jpg" alt=""/></p>
              </div>
              <p class="twoDep">
				<a class="disB off le-15" href="javascript:;">독서문화프로그램 안내</a>
				
				<a class="disB off" href="javascript:;">동아리커뮤니티</a>
				
			  </p>
            </li>
            <li class="four"> <a  class="oneDep disB" href="javascript:;">참여마당</a>
              <div class="guideBg">
                <p class="tit">참여마당</p>
                <p class="txt">다양한 프로그램에 참여하실 수 있습니다.</p>
                <p class="img"><img src="http://www.suwonlib.go.kr/images/gmi/cni/lnb_04.jpg" alt=""/></p>
              </div>
              <p class="twoDep"> 
				  <a class="disB off" href="javascript:;">도서관에 물어보세요</a> 
				  <a class="disB off" href="javascript:;" title="새창열림" target="_blank">칭찬합니다 <img src="http://www.suwonlib.go.kr/images/np.png" alt="새창열림" /></a> 
				  <a class="disB off" href="javascript:;">온라인신청</a> 
			  </p>
            </li>
            <li class="five"> <a href="javascript:;" class="oneDep disB">도서관소식</a>
              <div class="guideBg">
                <p class="tit">도서관소식</p>
                <p class="txt">도서관에 대한 소식을 전해 드립니다.</p>
                <p class="img"><img src="http://www.suwonlib.go.kr/images/gmi/cni/lnb_05.jpg" alt=""/></p>
              </div>
              <p class="twoDep"> 
			  <a class="disB off" href="javascript:;">공지사항</a> 
			  <a class="disB off" href="javascript:;">도서관갤러리</a> 
			  <a class="disB off" href="javascript:;">공개자료실</a> 
			  
			  </p>
            </li>
            <li class="six"> <a class="oneDep disB" href="javascript:;">도서관소개</a>
              <div class="guideBg">
                <p class="tit">도서관소개</p>
                <p class="txt">도서관에 대하여 소개합니다.</p>
                <p class="img"><img src="http://www.suwonlib.go.kr/images/gmi/cni/lnb_06.jpg" alt=""/></p>
              </div>
              <p class="twoDep"> 
			  <a class="disB off" href="javascript:;">연혁</a> 
			  <a class="disB off" href="javascript:;">행정서비스헌장</a> 
			  <a class="disB off" href="javascript:;">조직도</a> 
			  <a class="disB off" href="javascript:;">현황안내</a> 
			  <a class="disB off" href="javascript:;">상징물</a> 
			  <a class="disB off" href="javascript:;">오시는길</a> 
			  </p>
            </li>
          </ul>
        </div>
      </div>
      <div class="twoDbg"></div>
      <!--//gnb활성화시내려옴 --> 
    </div>
    <!--//gnbDiv--> 
  </nav>
</header>



 

  <div id="wrap"> 
    
    <!--//sidemenu-->
	<div id="sidemenu" class="sidebg_sunkyung">
      <h2 class="bg2">자료찾기</h2>
      <ul id="side2m">
        <li id="side2m1" class=""><a href="javascript:;">시립도서관 도서검색</a></li>
		<li id="side2m7" class=""><a href="javascript:;">지역도서관 통합검색</a></li>
		<li id="side2m2" class=""><a href="javascript:;">추천도서</a></li>
        <li id="side2m3" class=""><a href="javascript:;">신간안내</a></li>
        <li id="side2m4" class=""><a href="javascript:;">베스트대출</a></li>
        <li id="side2m5" class=""><a href="javascript:;">특화자료(수원학)</a></li>
        <li id="side2m6" class="on"><a href="javascript:;" onclick="fn_callDLoan('request')">바로대출 신청</a></li>
        <li id="side2m8" class=""><a href="javascript:;" onclick="fn_callDLoan('myRequestInfo')">바로대출 신청내역</a></li>
      </ul>
    </div>  
    
    <!--//body-->
    <section class="body">
      
	  <!-- body_head start -->
	  <div id="body_head">
        <div id="location">
          <div class="loca_text"><img src="http://www.suwonlib.go.kr/images/gmi/inc/loc_home.gif" alt="Home" /> > 자료찾기 > <span>시립도서관 도서검색</span></div>
        </div>
        <div id="body_title">
          <h2>시립도서관 도서검색</h2>
        </div>
      </div>
	  <!-- body_head end -->

	<script type="text/javascript">
	function fn_callDLoan(url) {
		
		if (url == "request") {
			$("#body_title").html("<h2>바로대출 신청</h2>");
			$("#side2m6").addClass("on");
			$("#side2m8").removeClass("on");
		} else {
			$("#body_title").html("<h2>바로대출 신청내역</h2>");
			$("#side2m8").addClass("on");
			$("#side2m6").removeClass("on");
		}
		
		var dloanUrl = "http://localhost:8080/";
		$("#dloan_iframe").attr('src', dloanUrl+url);
	}
	</script>

      <!--body_contents-컨텐츠Start-->
	  <div id="body_contents" style="height:1200px"> 
	  	<iframe id="dloan_iframe" src="http://localhost:8080/request" onload="" width="100%" height="100%" style="border:0" on></iframe>
      </div>
	  <!--//body_contents-컨텐츠End--> 

    </section>
    <!--#//body--> 
    
    
    <!--#quick_menu-->
    <script type="text/javascript" src="http://www.suwonlib.go.kr/P_js/Floating.js"></script> 
<div id="quick_menu">
      <div id="quick">
        <h2><img src="http://www.suwonlib.go.kr/images/gmi/cni/quick_h2.gif" alt="quickmenu"  /></h2>
        <ul>
		
          <li class="Bgimg1"><a href="javascript:;">디지털<br />
            자료실예약</a></li>
          <li class="Bgimg2"><a href="javascript:;">희망도서<br />
            신청</a></li>
          <li class="Bgimg3"><a href="javascript:;">독서문화<br />
            프로그램</a></li>
          <li class="Bgimg4"><a href="javascript:;">도서관<br />
            체험교실</a></li>
          <li class="Bgimg5"><a href="javascript:;">청소년<br />
            자원봉사</a></li>
          <li class="Bgimg6"><a href="javascript:;">북스타트</a></li>


		
		</ul>
      </div>
      <script type="text/javascript">initMoving(document.getElementById("quick_menu"), 130, 80, 225);</script> 
    </div> 
    
  </div>
</div>

<!--#footer S--> 
  <footer id="footer_outer">
  <div class="footer"> 
    
    <script type="text/javascript" src="http://www.suwonlib.go.kr/P_js/scroll.js"></script> 
<!-- 배너 -->
    <div class="banner">
      <h2>배너모음</h2>
      <ul class="banner_control">
        <li class="prev_banner"><a href="#n"><img src="http://www.suwonlib.go.kr/images/gmi/cni/banner_btn_up.gif" alt="이전배너보기"  /></a></li>
        <li class="pause_banner"><a href="#n"><img src="http://www.suwonlib.go.kr/images/gmi/cni/banner_btn_stop.gif" alt="배너정지" /></a></li>
        <li class="next_banner"><a href="#n"><img src="http://www.suwonlib.go.kr/images/gmi/cni/banner_btn_down.gif" alt="다음배너보기" /></a></li>
		<li><a href="javascript:;"><img src="http://www.suwonlib.go.kr/images/gmi/cni/banner_btn_all.gif" alt="배너전체보기" /></a></li>
      </ul>
      
      <ul class="banner_img clearfix">
		
		<li><a href="javascript:;" title="새창열림"><img src="http://www.suwonlib.go.kr/images/gmi/main/banner16.gif" alt="사서에게 물어보세요" width="120" height="33" /></a></li> 
		
        <li><a href="javascript:;" title="새창열림" target="_blank"><img src="http://www.suwonlib.go.kr/images/gmi/main/banner01.gif" alt="수원시청" width="120" height="33" /></a></li>
        <li><a href="javascript:;" title="새창열림" target="_blank"><img src="http://www.suwonlib.go.kr/images/gmi/main/banner02.gif" alt="e수원뉴스" width="120" height="33" /></a></li>
        <li><a href="javascript:;" title="새창열림" target="_blank" class="last"><img src="http://www.suwonlib.go.kr/images/gmi/main/banner03.gif" alt="수원iTV" width="120" height="33" /></a></li>
        <li><a href="javascript:;" title="새창열림" target="_blank"><img src="http://www.suwonlib.go.kr/images/gmi/main/banner04.gif" alt="수원시문화관광" width="120" height="33" /></a></li>
        <li><a href="javascript:;" title="새창열림" target="_blank"><img src="http://www.suwonlib.go.kr/images/gmi/main/banner05.gif" alt="국립중앙도서관" width="120" height="33" /></a></li>
        <li><a href="javascript:;" title="새창열림" target="_blank" class="last"><img src="http://www.suwonlib.go.kr/images/gmi/main/banner06.gif" alt="경기도사이버도서관" width="120" height="33" /></a></li>
        <li><a href="javascript:;" title="새창열림" target="_blank"><img src="http://www.suwonlib.go.kr/images/gmi/main/banner07.gif" alt="민원24" width="120" height="33" /></a></li>
        <li><a href="javascript:;" title="새창열림" target="_blank"><img src="http://www.suwonlib.go.kr/images/gmi/main/banner08.gif" alt="다문화정보" width="120" height="33" /></a></li>
        <li><a href="javascript:;" title="새창열림" target="_blank" class="last"><img src="http://www.suwonlib.go.kr/images/gmi/main/banner09.gif" alt="그린카드" width="120" height="33" /></a></li>
        <li><a href="javascript:;" title="새창열림" target="_blank"><img src="http://www.suwonlib.go.kr/images/gmi/main/banner11.gif" alt="국립어린이청소년도서관" width="120" height="33" /></a></li>
        <li><a href="javascript:;" title="새창열림" target="_blank"><img src="http://www.suwonlib.go.kr/images/gmi/main/banner12.gif" alt="수원시 휴먼콜센터" width="120" height="33" /></a></li>
        <li><a href="javascript:;" title="새창열림" target="_blank"><img src="http://www.suwonlib.go.kr/images/gmi/main/banner13.gif" alt="공공 I-PIN 재인증" width="120" height="33" /></a></li>
        <li><a href="javascript:;" title="새창열림" target="_blank"><img src="http://www.suwonlib.go.kr/images/gmi/main/banner14.gif" alt="수원시e-book" width="120" height="33" /></a></li>
        <li><a href="javascript:;" title="새창열림" target="_blank"><img src="http://www.suwonlib.go.kr/images/gmi/main/banner15.gif" alt="다문화 동화구연" width="120" height="33" /></a></li>
      </ul>

<script type="text/javascript">
function link_ni()
{
   document.form_ni.action = "http://www.nl.go.kr/ask/";
    document.form_ni.lib_id.value = "141025";
    document.form_ni.lib_name.value = "선경도서관";
    document.form_ni.target="_blank";
    document.form_ni.submit();
}
</script>

<form name="form_ni" method="post" action="http://www.nl.go.kr/ask/">
<input type="hidden" name="lib_name" />
<input type="hidden" name="lib_id" />
</form>

    </div>
    <!-- //배너 -->


    
    
    <ul class="foot_link">
      <li><a href="javascript:;">도서관연혁</a></li>
      <li><a href="javascript:;"><span class="fbtn_sunkyung">개인정보처리방침</span></a></li>
      <li><a href="javascript:;" class="last">홈페이지 이용약관</a></li>
    </ul>
    <div class="foot_logo"><img src="http://www.suwonlib.go.kr/images/cni/foot_logo.gif" alt="사람이 반갑습니다. 휴먼시티 수원" /></div>
    <div class="foot_add">
      <address>
      [16258] 경기도 수원시 팔달구 신풍로23번길 68(신풍동) <br />
      전화 : 031-228-4728 / 1899-3300  팩스 : 031-228-3746
      </address>
      <p>Copyright by 수원시 도서관사업소 All Right Reserved.</p>
    </div>
    
    <!--//관련사이트바로가기-->
    
    <div id="selectbox"> 
         <div id="select_depart004"> 
        <!--//#select_box Start-->
        <h2><a href="#selectbox_depart004" onclick="displayOn('selectbox_depart004'); return false;">도서관 바로가기</a></h2>
        <div id="selectbox_depart004" class="sbx" style="display:none;">
          <div class="wrap">
            <h3>시립도서관(직영)</h3>
            <ul>
              <li><a href="javascript:;" target="_blank" title="새창열림">선경도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">중앙도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">창룡도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">화서다산도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">호매실도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">서수원도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">한림도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">버드내도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">북수원도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">대추골도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">일월도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">광교홍재도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">영통도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">태장마루도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">인도래작은도서관</a></li>
			</ul>
          </div>
          <div class="wrap">
            <h3>시립도서관(위탁)</h3>
            <ul>
              <li><a href="javascript:;" target="_blank" title="새창열림">슬기샘어린이도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">지혜샘도어린이서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">바른샘어린이도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">한아름도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">반달어린이도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">사랑샘도서관</a></li>
              <li><a href="javascript:;" target="_blank" title="새창열림">희망샘도서관</a></li>
			</ul>
          </div>
          <div class="wrap">
            <h3>사립도서관</h3>
            <ul>
              <li><a href="javascript:;" target="_blank" title="새창열림">화홍어린이도서관</a></li>
            </ul>
          </div>
          <a href="#con_right" onclick="displayOff('selectbox_depart004'); obj=document.getElementById('select_depart004').getElementsByTagName('a')[0];obj.focus(); return false;" class="close"><img src="http://www.suwonlib.go.kr/images/gmi/cni/select_close.gif" alt="목록 닫기" /></a> </div>
        <!--//#select_box End--> 
      </div>
    </div>
    
    
	<!-- 방문자 start -->
    <div class="visitor">
      <h2 class="skip">방문자</h2>
      <dl class="today">
        <dt>오늘</dt>
        <dd>646</dd>
      </dl>
      <dl class="total">
        <dt>전체</dt>
        <dd>1,781,322</dd>
      </dl>
    </div>
    <!-- 방문자 end --> 
    
  </div>
</footer>
 


  <!--#header_m--> 
<!-- mobile_header -->
<script type="text/javascript">

$(document).ready(function(){
	
	// m_navi toggle
	$depth_one=$(".m_navi ul li span.depth_01");
	$depth_one.click(function(){
		$depth_one.removeClass("active").parents(".m_navi ul li").children("ul").slideUp("fast");
		$(this).addClass("active").next().slideDown("fast");
	});
	
	// m_navi toggle
	$depth_two=$(".m_navi ul li ul span.depth_02");
	$depth_two.click(function(){
		$depth_two.removeClass("active").parents(".m_navi ul li ul li").children("ul").slideUp("fast");
		$(this).addClass("active").next().slideDown("fast");
	});

});
</script>
<!-- mobile_header end -->

<!-- side m navi -->
<div class="pushy pushy-left">

	<!-- account -->
    <div class="m_global mgbg_sunkyung">
		<ul>
		
			<li><a href="javascript:;">로그인</a></li>
            <li><a href="javascript:;">회원가입</a></li>
		
        	
            <li><a href="javascript:;">사이트맵</a></li>
            <li><a href="javascript:;" target="_blank" title="새창열림">인트로 바로가기</a></li>
        </ul>
    </div>  
    
    <!-- navi -->
    <div class="m_navi">
	<ul>
		<li><span class="depth_01">이용안내</span> 
			<ul> 
            	<li><a href="javascript:;">이용 및 회원안내</a></li>   
				
            	<li><a href="javascript:;">열림실이용</a></li>
				
            	<li><a href="javascript:;">대출회원가입</a></li>
            	<li><span>도서관서비스</span>
				<ul class="dep3">
                     <li><a href="javascript:;">책나루(무인)도서관</a></li>
                     <li><a href="javascript:;">관내상호대차(수원시)</a></li>
                     <li><a href="javascript:;">책바다서비스(전국)</a></li>
                     <li><a href="javascript:;">책이음(통합도서) 서비스</a></li>
                     <li><a href="javascript:;">책배달서비스</a></li>
					 <li><a href="javascript:;">메트로땅콩도서관</a></li>
                     <li><a href="javascript:;">전자도서관</a></li>
					 <li><a href="javascript:;">협약대학 도서관</a></li>
                     <li><a href="javascript:;">북스타트</a></li>
				</ul>  
				</li>
				<li><a href="javascript:;">모바일 앱 이용안내</a></li>
				<li><a href="javascript:;">FAQ</a></li>
			</ul>
		</li>
        <li><span class="depth_01">자료찾기</span>
			<ul class="depth_01">
				<li><a href="javascript:;">시립도서관 도서검색</a></li>
            	<li><a href="javascript:;">지역도서관 통합검색</a></li>
				<li><a href="javascript:;">추천도서</a></li>
            	<li><a href="javascript:;">신간안내</a></li>
            	<li><a href="javascript:;">베스트대출</a></li>
            	<li><span>특화자료(수원학)</span>
				<ul class="dep3">
                     <li><a href="javascript:;">기본안내</a></li>
					 <li><a href="javascript:;">선경갤러리</a></li>
					 <li><a href="javascript:;">성곽자료</a></li>					 
				</ul>  
				</li>
				
			</ul>
		</li>
        <li><span class="depth_01">문화행사</span>
			<ul class="depth_01">
            	<li><a href="javascript:;">독서문화프로그램 안내</a></li>

				
				<li><a href="javascript:;">동아리커뮤니티</a></li>
				
				
			</ul>
		</li>
        <li><span class="depth_01">참여마당</span>
			<ul class="depth_01">
            	<li><a href="javascript:;">도서관에 물어보세요</a></li>
            	<li><a href="javascript:;" title="새창열림" target="_blank">칭찬합니다 <img src="http://www.suwonlib.go.kr/images/np2.png" alt="새창열림" /></a></li>
            	<li><span>온라인신청</span>
				<ul class="dep3">
                     <li><a href="javascript:;">청소년자원봉사</a></li>
					 <li><a href="javascript:;">디지털자료실예약</a></li>
					 <li><a href="javascript:;">비치희망도서신청</a></li>
					 <li><a href="javascript:;">도서관체험교실</a></li>
				</ul>  
				</li>
            </ul>
		</li>
        <li><span class="depth_01">도서관소식</span>
			<ul class="depth_01">
            	<li><span>공지사항</span>
				<ul class="dep3">
                     <li><a href="javascript:;">공지사항</a>
					 <li><a href="javascript:;">타도서관소식</a>
				</ul> 
				</li>
            	<li><a href="javascript:;">도서관갤러리</a></li>
            	<li><a href="javascript:;">공개자료실</a></li>
				
				
            </ul>
		</li>
        <li><span class="depth_01">도서관소개</span>
			<ul class="depth_01">
            	<li><a href="javascript:;">연혁</a></li>
            	<li><a href="javascript:;">행정서비스헌장</a></li>
            	<li><a href="javascript:;">조직도</a></li>
            	<li><span>현황안내</span>
				<ul class="dep3">
                     <li><a href="javascript:;">장서현황</a></li>
					 <li><a href="javascript:;">시설현황</a></li>
					 <li><a href="javascript:;">비상대피도</a></li>
					 
				</ul> 
				</li>
				<li><a href="javascript:;">상징물</a></li>
            	<li><a href="javascript:;">오시는길</a></li>
            </ul>
		</li>
		<li><span class="depth_01">회원정보</span>
			<ul class="depth_01">
            	<li><a href="javascript:;">회원로그인</a></li>
            	<li><a href="javascript:;">회원가입</a></li>
            	<li><a href="javascript:;">ID/PW 찾기</a></li>
            	<li><a href="javascript:;">이용약관</a></li>
            	<li><a href="javascript:;">개인정보처리방침</a></li>
            	<li><a href="javascript:;">회원정보수정</a></li>
            	<li><a href="javascript:;">회원탈퇴</a></li>
            	<li><a href="javascript:;">대출내역조회</a></li>
            	<li><a href="javascript:;">배너모음</a></li>
            </ul>
		</li>
	</ul>
	</div>

</div>

<!-- 3번째 메뉴 열었다 펼침( 이건 적당히 body 맨아래로 옮겨두됨 )-->
				<script type="text/javascript">
					var dep2 =  $(".m_navi > ul > li > ul > li > span");
					dep2.click(function(){
						dep2.removeClass("active");
						$(this).addClass("active");
						if($("+.dep3",this).css("display")=="none"){
							$(".dep3").slideUp("nomal");
							$("+.dep3",this).slideDown("nomal");		
						}else{
							$(".dep3").slideUp("nomal");
						}
					});
				</script>

<div class="site-overlay"></div>
 

</body>
</html>