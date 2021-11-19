package dloan.user.request;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dloan.common.CommonService;
import dloan.common.tags.paging.PageInfo;
import dloan.common.util.MapComparator;
import dloan.common.util.RestApiUtils;
import dloan.common.util.RestCall;
import dloan.common.util.SessionUtils;
import dloan.user.login.LoginService;


@Controller
public class RequestController {
	Logger log = LoggerFactory.getLogger(RequestController.class);
	
	@Autowired
	private RequestService requestService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private RestApiUtils restApiUtils;
	
	@Autowired
	private MapComparator mapComparator;
	
	/**
	 * 책 조회
	 * 
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/request")
	public ModelAndView bookSearch(HttpServletRequest req, @RequestParam Map<String, String> params) throws Exception {
		if (SessionUtils.getUserId() == null) {
			// 세션이 없을 경우
			ModelAndView mv = new ModelAndView("user/request/request");
			mv.addObject("monthLimit", this.requestService.getMonthLimit());
			return mv;
		}
		
		Map<String, Object> retMap = null;
		
		if (StringUtils.isNotEmpty(params.get("d_titl")) ||
			StringUtils.isNotEmpty(params.get("d_auth")) ||
			StringUtils.isNotEmpty(params.get("d_publ"))) {
			
			if (params.containsKey("pageNo") == false || StringUtils.isEmpty((String) params.get("pageNo")) == true) {
				params.put("pageNo", "1");
				params.put("start" , "1");
			} else {
				// 2019.05.08 소스코드 보인취약점 조치
				try {
					params.put("start", String.valueOf((Integer.valueOf(params.get("pageNo"))-1) * 10 + 1));
				} catch (Exception e) {
					params.put("start" , "1");
				}
			}
			
			// 네이버 검색 --> 에러코드 429 : API키당 조회건 25000이 넘은 경우, 다른 키로 재검색
			int retry = 0;
			while(this.restApiUtils.getNaverApiCnt() > retry) {
				
				String[] naverApiKey = this.restApiUtils.getNaverApiKey();
				
				Map<String, String> header = new HashMap<String, String>();
				header.put("X-Naver-Client-Id",     naverApiKey[0]);
				header.put("X-Naver-Client-Secret", naverApiKey[1]);
				
				retMap = this.naverBookCall(header, params);
				if (!retMap.containsKey("errorCode")) {
					// 검색성공
					break;
				}
				if(retMap.get("errorCode") != null && !retMap.get("errorCode").equals("") && !"010".equals(retMap.get("errorCode"))){
					//throw new Exception(retMap.get("errorMessage").toString());
					break;
				}
				retry++;
			}
		}
		
		SessionUtils.setUserInfo(this.loginService.getUserInfo(SessionUtils.getUserId()));
		
		// 도서관 목록 조회
		List<Map<String, Object>> libraryList = this.commonService.selectLibrary(null);
		// 오름차순 정렬
		Collections.sort(libraryList, mapComparator);
		
		/**
		 * 2018-08-27 요청
		 * 영통, 북수원 도서관 비치희망서비스 신청제한(2018-09-01 ~)
		 * 2018-10-18 요청
		 * 호매실 도서관 비치희망서비스 신청제한(2018-10-22 ~)
		 * 2018-10-24 요청
		 * 광교홍재 도서관 비치희망서비스 신청제한(2018-10-29 ~)
		 * 일월 도서관 비치희망서비스 신청제한(2018-11-01 ~)
		 * 2018-10-29 요청
		 * 태장마루 도서관 비치희망서비스 신청제한(2018-11-01 ~)
		 * 2018-11-02 요청
		 * 대추골 도서관 비치희망서비스 신청제한(2018-11-03 ~)
		 * 2018-11-07 요청
		 * 광교푸른숲 도서관 비치희망서비스 신청제한(2018-11-08 ~)
		 * 2018-11-22 요청
		 * 매여울 도서관 비치희망서비스 신청제한(2018-11-22 ~)
		 * 2018-11-27 요청
		 * 한림 도서관 비치희망서비스 신청제한(2018-12-01 ~)
		 * 2018-11-28 요청
		 * 서수원 도서관 비치희망서비스 신청제한(2018-12-01 ~)
		 * 2018-11-30 요청
		 * 버드내 도서관 비치희망서비스 신청제한(2018-12-01 ~)
		 * 2018-12-04 요청
		 * 선경, 중앙, 창룡, 화서다산 도서관 비치희망서비스 신청제한(2018-12-04 ~)
		 * 2018-12-05 요청
		 * 한아름 도서관 비치희망서비스 신청제한(2018-12-06 ~)
		 * 2018-12-07 요청
		 * 슬기샘, 지혜샘, 바른샘 도서관 비치희망서비스 신청제한(2018-12-07 ~)
		 * 2018-12-20 요청
		 * 희망샘 도서관 도서관 비치희망서비스 신청제한(2018-12-20 ~)
		 * 2019-01-07 요청
		 * 슬기샘, 지혜샘, 바른샘, 한아름, 희망샘 도서관을 제외한 모든 공공도서관 비치희망서비스 오픈(2019-01-07 ~)
		 * 2019-01-17 요청
		 * 희망샘 도서관 비치희망서비스 오픈
		 * 2019-01-31 요청
		 * 지혜샘, 슬기샘, 바른샘 도서관 비치희망서비스 오픈
		 */
		
		/*List<Map<String, Object>> tempLibraryList = new ArrayList<Map<String, Object>>();
		
		for(Map<String, Object> libraray : libraryList) {
			String code = (String) libraray.get("code");
			
			if(StringUtils.isEmpty(code)) {
			
			} else if(code.equals("MD") || code.equals("MF") || code.equals("ME")) {
				
			} else {
				tempLibraryList.add(libraray);
			}
		}
		if(tempLibraryList.size() > 0) {
			libraryList = new ArrayList<Map<String, Object>>();
			libraryList.addAll(tempLibraryList);
		}*/
		
		ModelAndView mv = new ModelAndView("user/request/request");
		mv.addObject("storeList" , this.commonService.selectStore("randomOrder"));
		mv.addObject("libraryList" , libraryList);
		mv.addObject("monthLimit", this.requestService.getMonthLimit());
		mv.addAllObjects(retMap);
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> naverBookCall(Map<String, String> header, Map<String, String> params) {
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		String jsonData = RestCall.APICall(this.restApiUtils.getNaverApiUrl(), "GET", header, params);
		jsonData = jsonData.replaceAll("<b>", "").replaceAll("</b>", "");
		
		try {
			retMap.putAll( RestCall.jsonStringToMap(jsonData));
			
			retMap.put("bookList", (List<Map<String, Object>>)retMap.get("items"));
		} catch (Exception e) {
			// 2019.05.08 소스코드 보안취약점 조치
			//e.printStackTrace();
			log.error("naver 상품 검색 api 오류");
		}
		
		PageInfo pageInfo = new PageInfo();
		
		// 현재 페이지
		if (params.containsKey("pageNo") == false || StringUtils.isEmpty((String) params.get("pageNo")) == true) {
			pageInfo.setCurrentPageNo(1);
		} else {
			pageInfo.setCurrentPageNo(Integer.parseInt(params.get("pageNo").toString()));
		}
		
		// 페이지당 레코드 갯수
		if (params.containsKey("display") == false || StringUtils.isEmpty((String) params.get("display")) == true) {
			pageInfo.setRecordCountPerPage(10);
		} else {
			pageInfo.setRecordCountPerPage(Integer.parseInt(params.get("display").toString()));
		}
		
		// 보여줄 페이지 사이즈
		pageInfo.setPageSize(10);
		
		if (retMap.containsKey("total")) {
			pageInfo.setTotalRecordCount(Integer.parseInt(retMap.get("total").toString()));
		}
		
		retMap.put("pageNo",    params.get("pageNo"));
		retMap.put("start",     params.get("start"));
		retMap.put("d_titl",    params.get("d_titl"));
		retMap.put("d_auth",    params.get("d_auth"));
		retMap.put("d_publ",    params.get("d_publ"));
		//retMap.put("d_isbn",    params.get("d_isbn"));
		retMap.put("newPhone1", params.get("newPhone1"));
		retMap.put("newPhone2", params.get("newPhone2"));
		retMap.put("newPhone3", params.get("newPhone3"));
		retMap.put("reqType",   params.get("reqType"));
		retMap.put("bookstore", params.get("bookstore"));
		retMap.put("library",   params.get("library"));
		retMap.put("smsYn",     params.get("smsYn"));
		retMap.put("addOnsYn",  params.get("addOnsYn"));
		retMap.put("pageInfo",  pageInfo);
		
		return retMap;
	}
	
	/**
	 * 책 신청(type)
	 * bookstore  - 서점
	 * library    - 도서관
	 * 
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/insertBookRequest")
	public @ResponseBody Map<String, Object> insertBookRequest(HttpServletRequest req, 
			@RequestParam Map<String, String> params) throws Exception {
		
		Map<String, Object> rtnMap = new HashMap<String, Object>(); 
		
		String type = params.get("type");
		if(StringUtils.isEmpty(type)) {
			rtnMap.put("resultMessage", "올바른 요청이 아닙니다.");
			rtnMap.put("resultCode",    "N");
			return rtnMap;
		} else if(!type.equals("bookstore") && !type.equals("library")) {
			rtnMap.put("resultMessage", "신청정보가 올바르지 않습니다.");
			rtnMap.put("resultCode",    "N");
			return rtnMap;
		}
		
		
			// 바로대출 신청
			rtnMap = this.requestService.insertRequest(params);
		
		if ("Y".equals(rtnMap.get("resultCode"))) {
			// SMS발송
			try {
				Map<String, Object> smsInfo = (Map<String, Object>) rtnMap.get("smsInfo");
				if (smsInfo != null) {
					//this.commonService.smsSend(smsInfo);
					this.commonService.smsSendRequest(smsInfo);
				}
			} catch (Exception e) {
				// 2019.05.07 소스코드 보안취약점 조치
				//e.printStackTrace();
				log.error("홈페이지 희망도서 신청 SMS 오류");
			}
		}
		
		return rtnMap;
	}
	
	/**
	 * 내 요청정보
	 * 
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/myRequestInfo")
	public ModelAndView myRequestInfo(HttpServletRequest req, @RequestParam Map<String, String> params) throws Exception {
		
		String viewName = "";
		ModelAndView mv = new ModelAndView();
		
		params.put("userNo", SessionUtils.getUserNo());
		
		if (params.containsKey("start") == false || StringUtils.isEmpty((String) params.get("start")) == true) {
			params.put("start", "1");
		}
		
		//String tab = StringUtils.isEmpty(params.get("tab")) ? "library" : params.get("tab");
		/*
		 * String tab = params.get("tab"); if(StringUtils.isEmpty(tab) ||
		 * (!tab.equals("library") && !tab.equals("bookstore"))) { tab = "library"; }
		 */
		
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		
//		if(tab.equals("bookstore")) {
			rtnMap = requestService.selectRequestInfo(params);
			viewName = "user/request/myRequest";
		/*} else if(tab.equals("library")) {
			rtnMap = requestService.selectFurnishRequestInfo(params);
			viewName = "user/request/myFurnishRequest";
		}
*/		
	//	mv.addObject("tab", tab);
		mv.addAllObjects(params);
		mv.addAllObjects(rtnMap);
		
		mv.setViewName(viewName);
		return mv;
	}
	
	/**
	 * 신청 취소
	 * 
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/userRequestCancel")
	public @ResponseBody Map<String, Object> userRequestCancel(HttpServletRequest req, 
			@RequestParam(name="ltRecKey") List<String> ltRecKey,
			@RequestParam(name="ltReqStatus") List<String> ltReqStaus) throws Exception {
		
		return requestService.userRequestCancel(ltRecKey);
	}

	/**
	 * 상세이용안내
	 * 
	 * @return
	 */
	@RequestMapping(value="/requestHelp")
	public ModelAndView requestHelp() {
		ModelAndView mav = new ModelAndView("user/request/requestHelp");
		mav.addObject("monthLimit", this.requestService.getMonthLimit());
		return mav;
	}
	
	/**
	 * 비치 희망 신청 취소
	 * 
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/userFurnishRequestCancel")
	public @ResponseBody Map<String, Object> userFurnishRequestCancel(HttpServletRequest req, 
			@RequestParam(name="ltRecKey") List<String> ltRecKey,
			@RequestParam(name="ltReqStatus") List<String> ltReqStaus) throws Exception {
		
		return requestService.userFurnishRequestCancel(ltRecKey);
	}
	
}