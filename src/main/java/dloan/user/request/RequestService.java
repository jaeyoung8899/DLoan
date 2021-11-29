package dloan.user.request;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dloan.common.handler.DLoanEnvService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dloan.common.CommonService;
import dloan.common.dao.CommonDao;
import dloan.common.util.IndexUtil;
import dloan.common.util.RestApiUtils;
import dloan.common.util.RestCall;
import dloan.common.util.SessionUtils;
import dloan.common.util.ValidUtils;

@Service
public class RequestService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestService.class);
	
	private static final String USER_SPACE = "user.request.";
	private static final String STORE_SPACE = "store.request.";
	
	// 서점신청 - 출판년도 제한(미만)
	private static int storeRequestPubDatelimit = 0;
	// 도서관신청 - 출판년도 제한(미만)
	private static int libRequestPubDatelimit = 0;
	
	@Autowired
	protected CommonDao commonDao;
	
	@Autowired
	private RestApiUtils restApiUtils;
	
	@Autowired
	private CommonService commonService;

	@Autowired
	private DLoanEnvService dLoanEnvService;

	public Map<String, Object> selectRequestInfo(Map<String, String> params) {	
		return (Map<String, Object>) commonDao.selectPagingList(USER_SPACE.concat("selectRequestInfo"), params);
	}
	
	/**
	 * 바로대출신청
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "unchecked", })
	public Map<String, Object> insertRequest(final Map<String, String> params) {
		
		/**********************************************************
		 * NO. 1 필수 체크 로직
		 *    1.1 : 연락처 일치여부 확인
		 *    1.2 : 단일권 체크
		 *    1.3 : 이용자 신청 제한 여부 조회
		 *    1.4 : 사용자별 월 신청권수 확인
		 *    1.5 : 5만원 초과 도서인지 체크 >> 3만원 초과 도서인지 체크
		 *    1.6 : 출판년도가 1년 미만인 도서인지 체크
		 *    1.7 : 제한 카테고리 체크 (알라딘)
		 *    1.8 : 제한도서 체크 
		 *    1.9 : 신청 가능한 도서관 체크 및 금액 체크
		 **********************************************************/
		// 필수 체크 
		Map<String, Object> retMap = ValidUtils.requiredMap(params, 
				new String[] {"storeId", "isbn", "smsYn", "phone", "newPhone"}, 
				new String[] {"신청서점", "ISBN", "SMS 수신여부", "연락처", "연락처확인"});
		if (!retMap.isEmpty()) {
			return retMap;
		}
		Map<String,Object> configMap = dLoanEnvService.getConfTblMap();
		System.out.println(configMap.get("STORE_PUB_LIMIT_YEAR"));
		storeRequestPubDatelimit = Integer.parseInt(configMap.get("STORE_PUB_LIMIT_YEAR").toString());

		/**********************************************************
		 *  지역 변수 선언
		 **********************************************************/
		params.put("userNo", SessionUtils.getUserNo());
		params.put("name", SessionUtils.getUserNm());
		
		Map<String, Object> tmpMap = null;
		
		String   fullIsbn      = params.get("isbn");
		String[] arIsbn        = fullIsbn.split(" ");
		String   title         = params.get("title");
		String   libManageCode = "";
		String   libName       = "";
		
		// 1.1 : 연락처 일치여부 확인
		if (!params.get("phone").equals(params.get("newPhone"))) {
			return ValidUtils.resultErrorMap("연락처가 다릅니다. 연락처를 확인해 주세요");
		}
		
		/* 
		 * 신청제한되지 않아야할 예외 ISBN 조회
		 * 1. 단일권인지 체크하지 않음
		 * 2. 알라딘 카테고리 조회하지 않음.
		 */
		Integer reqLimitAvoidCount = (Integer) this.commonDao.selectOne(USER_SPACE.concat("getReqLimitAvoidCount"), params);
		
		// 1.2 : 단일권 체크
		String[] bookSet = {"세트", "시리즈", "전집"};
		if(reqLimitAvoidCount == 0) {
			for (String set : bookSet) {
				if (title.indexOf(set) > -1) {
					return ValidUtils.resultErrorMap("단일권으로 선택하세요.");
				}
			}
		}
		
		// 1.3 : 이용자 신청 제한 여부 조회
		// 1.3.1 : 이용자 신청제한 사용여부 조회

//		String userPenalty = (String) this.commonDao.selectOne("task.".concat("selectUserLimit"), "USER_PENALTY");
		String userPenalty = configMap.get("USER_PENALTY") != null ? configMap.get("USER_PENALTY").toString() : "";
		// 1.3.2 : 이용자 신청제한 이력 조회
		if(userPenalty.equals("Y")) {
			String nextMonth = configMap.get("USER_PENALTY_DATE") != null ? configMap.get("USER_PENALTY_DATE").toString() : "";
			if (nextMonth.equals("Y")){
				params.put("userPenalty","Y");
			}
			Map<String, Object> userLimit = (Map<String, Object>) this.commonDao.selectOne(USER_SPACE.concat("getUserLimit"), params);
			if(userLimit != null && userLimit.containsKey("limitDate")) {
				return ValidUtils.resultErrorMap("희망도서 미대출로 인해 "+ userLimit.get("limitDate") + "까지 신청제한됩니다.");
			}
		}
		
		// 1.4 : 사용자별 월 신청권수 확인
		// dloanMonthCount : 서점 신청권수
		// furnishMonthCount : 비치희망 신청권수
		int dloanMonthCount = (Integer) this.commonDao.selectOne(USER_SPACE.concat("getUserRequestConuntYn"), params);
		int monthCount = 0;
		if(configMap.get("MONTH_COUNT").equals("Y")){
			int furnishMonthCount = (Integer) this.commonDao.selectOne(USER_SPACE.concat("getUserFurnishRequestConuntYn"), params);
			monthCount = dloanMonthCount + furnishMonthCount;
		}else{
			monthCount = dloanMonthCount;
		}


		int monthLimit = Integer.parseInt(configMap.get("MONTH").toString());
		if (monthCount >= monthLimit) {
			return ValidUtils.resultErrorMap("희망도서 서비스 신청은 월 " + monthLimit + "권만 신청 가능합니다.");
		}
		
		// 1.5 : 5만원 초과 도서인지 체크
		int curPrice = 0;
		int limitBookPrice = Integer.parseInt(configMap.get("BOOK_PRICE").toString());
		if (StringUtils.isNumeric(params.get("price"))) {
			curPrice = Integer.parseInt(params.get("price"));
		} else {
			params.put("price", "");
		}
		if (curPrice > limitBookPrice) {
			return ValidUtils.resultErrorMap("절판 및 품절도서, 고가도서 ("+limitBookPrice+"원 초과도서)는 신청 제외 대상 입니다.");
		}
		
		// 1.6 : 출판년도가 n년 미만인 도서인지 체크
		String pubDate = params.get("pubDate");
		if(StringUtils.isEmpty(pubDate)) {
			return ValidUtils.resultErrorMap("신청도서의 출판년도를 알 수 없습니다.");
		} else {
			pubDate = pubDate.substring(0, 4);
			// 출판년도 n년 미만 체크
			if(!isPubLimitRequestYn(pubDate, storeRequestPubDatelimit)) {
				return ValidUtils.resultErrorMap("출판년도가 " + storeRequestPubDatelimit + "년 이상인 도서는 신청 제외 대상 입니다.");
			}
		}
		
		// 1.7 : 제한 카테고리 체크 (알라딘)
		if(reqLimitAvoidCount == 0) {
			String isbn = arIsbn[arIsbn.length - 1];
			Integer categoryId = this.getAladinCategoryId(isbn);
			System.out.println("categoryId : " + categoryId);
			if (categoryId != null) {
				if (categoryId == -1000) {
					// 절판인 경우
					return ValidUtils.resultErrorMap("절판 및 품절도서, 고가도서 (5만원 초과도서)는 신청 제외 대상 입니다.");
				} else {
					Map<String, String> categoryMap = (Map<String, String>) this.commonDao.selectOne(USER_SPACE.concat("getAladinCategory"), categoryId);
					if(categoryMap != null) {
						String categoryLimit = categoryMap.get("categoryLimit");
						if (!StringUtils.isEmpty(categoryLimit)) {
							return ValidUtils.resultErrorMap(categoryLimit);
						}
					}
				}
			}
		}
		
		// 1.8 : 제한도서 체크 
		for (String isbn : arIsbn) {
			tmpMap = new HashMap<String, Object>();
			tmpMap.put("isbn", isbn);
			
			String limitReason = (String) this.commonDao.selectOne(USER_SPACE.concat("getLimitBookCount"), tmpMap);
			if (!StringUtils.isEmpty(limitReason)) {
				return ValidUtils.resultErrorMap("신청이 제한된 도서 입니다.\n(제한사유 : " + limitReason + ")");
			}
		}
		
		// 1.9 : 신청 가능한 도서관 체크 및 금액 체크
		Boolean isFull     = true;
		Boolean isExcess   = false;
		int price      = 0;
		int limitPrice = 0;
		
		tmpMap = new HashMap<String, Object>();
		tmpMap.put("storeId", params.get("storeId"));
		tmpMap.put("arIsbn",  Arrays.asList(arIsbn));

		if(configMap.get("LOANABLE_CHECK") != null && configMap.get("LOANABLE_CHECK").equals("Y")){
			// 1.9.1 : 대출가능 도서 유무 체크
			Map<String, Object> libBookLoanYn = this.selectLoanYnList(tmpMap);
			if(libBookLoanYn == null || libBookLoanYn.get("resultList") == null) {
				return ValidUtils.resultErrorMap("대출가능여부 조회 중 오류가 발생했습니다.");
			} else {
				List<Map<String, Object>> libBookList = (List<Map<String, Object>>) libBookLoanYn.get("resultList");
				// 대출 가능 도서관 리스트
				ArrayList<String> loanPossibleLibList = new ArrayList<>();
				for(Map<String, Object> libBook : libBookList) {
					String loanYn = libBook.get("loanLimitDesc").toString();
					String bookLibName = libBook.get("libName").toString();

					if("O".equals(loanYn)) {
						if(!loanPossibleLibList.contains(bookLibName)) {
							loanPossibleLibList.add(StringUtils.remove(bookLibName, "도서관"));
						}
					}
				}
				// 에러메시지
				String errorMsg = "";
				for(String loanPossibleLib : loanPossibleLibList) {
					if(StringUtils.isEmpty(errorMsg)) {
						errorMsg = loanPossibleLib;
					} else {
						if(!errorMsg.contains(loanPossibleLib)) {
							errorMsg += ", " + loanPossibleLib;
						}
					}
				}

				if(loanPossibleLibList != null && loanPossibleLibList.size() > 0) {
					return ValidUtils.resultErrorMap(errorMsg + "도서관에 대출 가능한 책이 있습니다.\n해당도서관을 이용해주세요.");
				}
			}
		}

		// 1.9.2 : 도서관별 책수
		List<Map<String, String>> libBookList = (List<Map<String, String>>) commonDao.selectList(USER_SPACE.concat("selectLibBookCount"), tmpMap);

		int reqLimitCnt = Integer.parseInt(configMap.get("REQ_LIMIT_COUNT").toString());

		for (Map<String, String> libBook : libBookList) {
			if (Integer.parseInt(libBook.get("cnt")) < reqLimitCnt) {
				libManageCode = libBook.get("libManageCode");
				libName       = libBook.get("libName");
				
				// 도서관별 금액 체크
				Map<String, String> storePrice = (Map<String, String>) this.commonDao.selectOne(USER_SPACE.concat("getMonthLibPrice"), libBook);
				price      = Integer.parseInt(storePrice.get("price"));
				limitPrice = Integer.parseInt(storePrice.get("limitPrice"));
				if ((price + curPrice) > limitPrice) {
					isExcess = true;
				}
				
				//서점별 금액 체크
				Map<String, String> storelimitPrice = (Map<String, String>) this.commonDao.selectOne(USER_SPACE.concat("getMonthStorePrice"), tmpMap);
				price      = Integer.parseInt(storelimitPrice.get("price"));
				limitPrice = Integer.parseInt(storelimitPrice.get("limitPrice"));
				
				if ((price + curPrice) > limitPrice) {
					isExcess = true;
				}
				
				if (!isExcess) {
					isFull = false;
					break;
				}
				
				isExcess = false;
			}
		}

		if (isFull) {

			// 2019.05.17 REQ.029 - 모든 도서관에서 납품중일 경우에만 납품중으로, 나머지는 소장중으로 팝업 문구
			// 2019.06.21 KOLASIII에서 구입대상자료의 경우도 구매 진행중인 도서 신청제한 메시지 추가
			int dloanBookCnt = 0;
			for (Map<String, String> libBook : libBookList) {
				if (Integer.parseInt(libBook.get("cnt")) >= 1 && Integer.parseInt(libBook.get("shelfCnt")) == 0) {
					dloanBookCnt = dloanBookCnt+1;
				}
			}
			
			if (libBookList.size() == dloanBookCnt) {
				// 2019.03.20 REQ.023 - 안내 문구 수정
				//return ValidUtils.resultErrorMap("도서관에서 소장중인 중복도서가 1권 초과인경우 신청이 제한 됩니다.");
				return ValidUtils.resultErrorMap("도서관에서 구매 진행 중인 도서는 신청이 제한 됩니다.");
			}
			
			StringBuffer tempLibName1 = new StringBuffer();
			
			for (Map<String, String> libBook : libBookList) {
				if (Integer.parseInt(libBook.get("cnt")) >= reqLimitCnt) {
					if (tempLibName1.length() == 0) {
						tempLibName1.append(libBook.get("libName"));
					} else {
						tempLibName1.append(", ").append(libBook.get("libName"));
					}
				}
			}
			
			if (tempLibName1.length() > 1) {
				// 2019.03.20 REQ.023 - 안내 문구 수정
				//return ValidUtils.resultErrorMap("도서관에서 소장중인 중복도서가 1권 초과인경우 신청이 제한 됩니다.");
				return ValidUtils.resultErrorMap("도서관에서 이미 소장중인 도서는 신청이 제한 됩니다.");
			}

			return ValidUtils.resultErrorMap("이번 달 희망도서 서비스 신청이 마감되었습니다.");
		}
		
		params.put("libManageCode", libManageCode);
		
		/**********************************************************
		 * NO. 2 KOLASIII 신청가능여부
		 *   2.1 : 개인정보보호 적용여부
		 *   2.2 : 미실명인증자 대출가능여부 체크
		 *   2.3 : 연체자료 체크
		 **********************************************************/
		
		tmpMap = new HashMap<String, Object>();
		tmpMap.put("libManageCode", libManageCode);
		tmpMap.put("userNo",        SessionUtils.getUserNo());
		
		// 2.1 개인정보보호 적용여부
		// 이미 N인 이용자 진행 했던부분으로 진행 할 수 있도록
		// String personYn = (String)commonDao.selectOne(USER_SPACE.concat("getPersonInfoYn"), tmpMap);
		
		Map<String, String> userAgree = (Map<String, String>)commonDao.selectOne(USER_SPACE.concat("getUserAgreeInfo"), tmpMap);
		
		//2.1 개인정보보호 적용여부
		/* 이미 N인 이용자 진행 했던부분으로 진행 할 수 있도록
		if ("Y".equals(personYn)) {
			if ("Y".equals(userAgree.get("privacyConfirmYn"))) {
				return ValidUtils.resultErrorMap("주요 개인정보 확인처리 후 신청이 가능합니다.\n[" +libName + "]");
			}
		}
		*/
		
		// 2.2 : 미실명인증자 대출가능여부 체크
		String unknownYn = (String)commonDao.selectOne(USER_SPACE.concat("getUnknownLoanYn"), tmpMap);
		if ("Y".equals(unknownYn)) {
			if (!"Y".equals(userAgree.get("agreeYn"))) {
				return ValidUtils.resultErrorMap("개인정보동의를 하지 않아 신청이 불가능합니다.\n[" +libName + "]");
			}
			if (StringUtils.isEmpty(userAgree.get("ipinHash"))) {
				return ValidUtils.resultErrorMap("실명인증을 하지 않아 신청이 불가능합니다.\n[" +libName + "]");
			}
		}
		
		// 2.3 : 연체자료 체크
		Integer delayCnt = Integer.parseInt(userAgree.get("delayCnt"));
		if (delayCnt > 0) {
			return ValidUtils.resultErrorMap("연체중인 자료가 있어  신청이 불가능합니다.\n[" +libName + "]");
		}
		
		/**********************************************************
		 * NO. 3 이용자 대출가능여부
		 *    3.1 : 회원 상태 확인 (1 대출 정지, 2 제적, 3 탈퇴)
		 *    3.2 : 회원 구분 확인 (1 비회원, 2. 준회원)
		 *    3.3 : 대출 정지 확인 
		 *    3.4 : 회원증 분실시 대출 가능 체크
		 *    3.5 : 연체료미납시 대출 가능 체크
		 **********************************************************/
		
		String lostYn  = (String)commonDao.selectOne(USER_SPACE.concat("getMemberCardLostByLoanYn"), tmpMap);
		String delayYn = (String)commonDao.selectOne(USER_SPACE.concat("getDelayLoanYn"), tmpMap);
		Map<String, String> loanInfo = (Map<String, String>)commonDao.selectOne(USER_SPACE.concat("getMemberLoanInfo"), tmpMap);
		
		// 3.1 : 회원 상태 확인 (1 대출 정지, 2 제적, 3 탈퇴)
		if (!"0".equals(loanInfo.get("userClass"))) {
			if ("1".equals(loanInfo.get("userClass"))) {
				return ValidUtils.resultErrorMap("대출정지 상태라 신청이 불가능합니다.\n[" +libName + "]");
			} else if ("2".equals(loanInfo.get("userClass"))) {
				return ValidUtils.resultErrorMap("제적 상태라 신청이 불가능합니다.\n[" +libName + "]");
			} else if ("3".equals(loanInfo.get("userClass"))) {
				return ValidUtils.resultErrorMap("탈퇴 상태라 신청이 불가능합니다.\n[" +libName + "]");
			}
		}
		
		// 3.2 : 회원 구분 확인 (1 비회원, 2. 준회원)
		if (!"0".equals(loanInfo.get("memberClass"))) {
			if ("1".equals(loanInfo.get("memberClass"))) {
				return ValidUtils.resultErrorMap("비회원은 신청이 불가능합니다.\n[" +libName + "]");
			} else if ("2".equals(loanInfo.get("memberClass"))) {
				return ValidUtils.resultErrorMap("준회원은 신청이 불가능합니다.\n[" +libName + "]");
			}
		}
		
		// 3.3 : 대출 정지 확인 
		if ("Y".equals(loanInfo.get("loanStopYn"))) {
			return ValidUtils.resultErrorMap("대출정지 상태라 신청이 불가능합니다.\n[" +libName + "]");
		}
		
		// 3.4 : 회원증 분실시 대출 가능 체크
		if ("Y".equals(lostYn)) {
			if ("Y".equals(loanInfo.get("lostUserCard"))) {
				return ValidUtils.resultErrorMap("회원증을 분실하여 신청이 불가능합니다.\n[" +libName + "]");
			}
		}
		
		// 3.5 : 연체료미납시 대출 가능 체크
		if ("Y".equals(delayYn)) {
			if (StringUtils.isNotEmpty(loanInfo.get("arrear"))) {
				return ValidUtils.resultErrorMap("연체료가 존재하여 신청이 불가능합니다.\n[" +libName + "]");
			}
		}

		//통합대출건수 사용
		/*List<Map<String,Object>> libList = new ArrayList<Map<String,Object>> ();
		libList = (List<Map<String, Object>>) commonDao.selectList(USER_SPACE.concat("getTotalLibInfo"),tmpMap);

		int totalLoanCnt=0;
		for(int i=0; i<libList.size(); i++) {
			Map<String, Object> tmpMapIn = new HashMap<String,Object> ();
			tmpMapIn.put("userNo",        SessionUtils.getUserNo());
			tmpMapIn.put("libManageCode", libList.get(i).get("manageCode"));
			Map<String, String> loanTotalBook = (Map<String, String>)commonDao.selectOne(USER_SPACE.concat("getMemberLoanBookCount"), tmpMapIn);
			totalLoanCnt+=Integer.parseInt(loanTotalBook.get("localLoanCount"));
		}
		int totalCount = (int) commonDao.selectOne(USER_SPACE.concat("getTotalLoanCnt"));
		if (totalCount < (totalLoanCnt + 1)) {
			return ValidUtils.resultErrorMap("통합대출가능 권수를 초과하여 신청이 불가능합니다.");
		}*/
		
		// 신청정보 등록
		commonDao.insert(USER_SPACE.concat("insertRequest"), params);
		
		// 신청정보 상태 history 등록
		Map <String, Object> hisMap = new HashMap<String, Object>();
		hisMap.put("recKey",    params.get("recKey"));
		hisMap.put("reqStatus", "U01");
		hisMap.put("type",      "U");
		hisMap.put("userId",    SessionUtils.getUserNo());
		
		commonDao.insert("common.insertRequestStatusHistory", hisMap);
		
		Map<String, Object> rtnMap = ValidUtils.resultSuccessMap();
		
		// SMS양식조회
		Map<String, String> smsParam = new HashMap<String, String>();
		smsParam.put("smsType", "DLN01");
		smsParam.put("autoYn", "Y");

		String smsMsg = (String) this.commonDao.selectOne("common.getSmsContents", smsParam);
		if (!StringUtils.isEmpty(smsMsg)) {
			// 신청정보조회
			Map<String, Object> reqInfo = (Map<String, Object>) this.commonDao.selectOne(STORE_SPACE.concat("selectStoreRequest"), params.get("recKey"));
			// 신청자정보 조회
			Map<String, String> user = (Map<String, String>) this.commonDao.selectOne(STORE_SPACE.concat("getStoreReqUserInfo"), params.get("userNo"));
			
			// SMS발송 메지시정리
			Map<String, String> convData = new HashMap<String, String>();
			convData.put("userName"      , user.get("name"));
			convData.put("storeName"     , (String) reqInfo.get("storeName"));
			convData.put("title"         , (String) reqInfo.get("title"));
			convData.put("loanWaitDate"  , (String) reqInfo.get("loanWaitDt"));
			convData.put("returnPlanDate", (String) reqInfo.get("returnPlanDt"));
			convData.put("enterPlanDate" , (String) reqInfo.get("enterPlanDt"));
			String tmpMsg = this.commonService.convSmsMsg(smsMsg, convData);
			
			// SMS자동발송 내역
			Map<String, Object> smsMap = new HashMap<String, Object>();
			smsMap.put("userName",      reqInfo.get("storeName"));
			smsMap.put("recever",       reqInfo.get("storeHandphone"));
			smsMap.put("userKey",       user.get("recKey"));
			smsMap.put("sender",        reqInfo.get("libPhone"));	// 신청자 핸드폰번호를 서점에 알려주지 않기위해서 발신자 번호에 도서관 전화번호를 입력
			/* smsMap.put("msg", tmpMsg); */
			smsMap.put("msg"          , "{\"type\":\"SM\",\"msg\":\""+tmpMsg+"\"}");
			smsMap.put("libManageCode", reqInfo.get("libManageCode"));
			smsMap.put("worker",        "DLOAN-USER");

			String alimMsg = commonService.convAlimMsg(smsParam,convData);
			if(!alimMsg.equals("")){
				smsMap.put("alimMsg",alimMsg);
			}

			// 00/00 00:00:00 ~ 08/59 00:00:00 = 09:00 00:00:00
			// 18/00 00:00:00 ~ 23/59 59:59:59 = 09:00 00:00:00
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH24:mm:ss");
			SimpleDateFormat formatYYYYMMDD = new SimpleDateFormat("yyyy/MM/dd");
			
			Date nowDate = new Date();
			Date sendDate = new Date();
			
			String afterDate = "";
			format.format(nowDate);
			
			int nowhour = nowDate.getHours();
			
			if(nowhour < 9){
				afterDate = formatYYYYMMDD.format(sendDate)+" 09:00:00";
			}else if(nowhour > 18){
				sendDate.setTime(nowDate.getTime() + (1000*60*60*24)*1);
				afterDate = formatYYYYMMDD.format(sendDate)+" 09:00:00";
			}else{
				afterDate = "";
			}
			
			// YYYY/MM/DD HH24:MI:SS
						
			smsMap.put("sendDate", afterDate);
			
			rtnMap.put("smsInfo", smsMap);
		}
		
		return rtnMap;
	}
	
	/**
	 * 비치희망신청
	 * 
	 * @param params
	 * @return map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> insertFurnishRequest(Map<String, String> params) {
		
		/**********************************************************
		 * NO. 1 필수 체크 로직
		 *    1.1 : 연락처 일치여부 확인
		 *    1.2 : 단일권 체크
		 *    1.3 : 사용자별 월 신청권수 확인
		 *    1.4 : 5만원 초과 도서인지 체크
		 *    1.5 : 출판년도가 1년 미만인 도서인지 체크
		 *    1.6 : 제한 카테고리 체크 (알라딘)
		 *    1.7 : 제한도서 체크 
		 *    1.8 : 도서관 대출가능 여부 체크
		 **********************************************************/
		// 필수 체크 
		Map<String, Object> retMap = ValidUtils.requiredMap(params, 
				new String[] {"libManageCode", "isbn", "phone", "newPhone"}, 
				new String[] {"신청도서관", "ISBN", "연락처", "연락처확인"});
		if (!retMap.isEmpty()) {
			return retMap;
		}

		Map<String,Object> configMap = dLoanEnvService.getConfTblMap();

		storeRequestPubDatelimit = (int) configMap.get("LIB_REQ_LIMIT_YEAR");

		/**********************************************************
		 *  지역 변수 선언
		 **********************************************************/
		params.put("userNo", SessionUtils.getUserNo());
		
		String   fullIsbn      = params.get("isbn");
		String[] arIsbn        = fullIsbn.split(" ");
		String   title         = params.get("title");
		String   libManageCode = params.get("libManageCode");
		String   libName       = "";
		
		Map<String, Object> tmpMap = null;
		
		// 1.1 : 연락처 일치여부 확인
		if (!params.get("phone").equals(params.get("newPhone"))) {
			return ValidUtils.resultErrorMap("연락처가 다릅니다. 연락처를 확인해 주세요");
		}
		
		/* 
		 * 신청이 제한되지 않아야할 예외 ISBN 조회
		 * 1. 단일권인지 체크하지 않음
		 * 2. 알라딘 카테고리 조회하지 않음.
		 */
		Integer reqLimitAvoidCount = (Integer) this.commonDao.selectOne(USER_SPACE.concat("getReqLimitAvoidCount"), params);
		
		// 1.2 : 단일권 체크
		String[] bookSet = {"세트", "시리즈", "전집"};
		if(reqLimitAvoidCount == 0) {
			for (String set : bookSet) {
				if (title.indexOf(set) > -1) {
					return ValidUtils.resultErrorMap("단일권으로 선택하세요.");
				}
			}
		}
		
		// 1.3 : 사용자별 월 신청권수 확인
		// dloanMonthCount : 서점 신청권수
		// furnishMonthCount : 비치희망 신청권수
		int dloanMonthCount = (Integer) this.commonDao.selectOne(USER_SPACE.concat("getUserRequestConuntYn"), params);
		int furnishMonthCount = (Integer) this.commonDao.selectOne(USER_SPACE.concat("getUserFurnishRequestConuntYn"), params);
		int monthCount = dloanMonthCount + furnishMonthCount;
		int monthLimit = (int) configMap.get("MONTH");
		if (monthCount >= monthLimit) {
			return ValidUtils.resultErrorMap("희망도서 서비스 신청은 월 " + monthLimit + "권만 신청 가능합니다.");
		}
		
		// 1.4 : 5만원 초과 도서인지 체크
		Integer curPrice = 0;
		if (StringUtils.isNumeric(params.get("price"))) {
			curPrice = Integer.parseInt(params.get("price"));
		} else {
			params.put("price", "");
		}
		if (curPrice > 50000) {
			return ValidUtils.resultErrorMap("절판 및 품절도서, 고가도서 (5만원 초과도서)는 신청 제외 대상 입니다.");
		}
		
		// 1.5 : 출판년도가 5년 미만인 도서인지 체크
		String pubDate = params.get("pubDate");
		if(StringUtils.isEmpty(pubDate)) {
			return ValidUtils.resultErrorMap("신청도서의 출판년도를 알 수 없습니다.");
		} else {
			pubDate = pubDate.substring(0, 4);
			// 출판년도 5년 미만 체크
			if(!isPubLimitRequestYn(pubDate, libRequestPubDatelimit)) {
				return ValidUtils.resultErrorMap("출판년도가 " + libRequestPubDatelimit + "년 이상인 도서는 신청 제외 대상 입니다.");
			}
		}
		
		// 1.6 : 제한 카테고리 체크 (알라딘)
		// 카테고리 제한 해제 요청한 ISBN
		if(reqLimitAvoidCount == 0) {
			// 10자리 13자리 isbn 중 뒤 13자리 isbn으로만 카테고리 제한 체크
			String isbn = arIsbn[arIsbn.length - 1];
			Integer categoryId = this.getAladinCategoryId(isbn);
			if (categoryId != null) {
				if (categoryId == -1000) {
					// 절판인 경우
					return ValidUtils.resultErrorMap("절판 및 품절도서, 고가도서 (5만원 초과도서)는 신청 제외 대상 입니다.");
				} else {
					Map<String, String> categoryMap = (Map<String, String>) this.commonDao.selectOne(USER_SPACE.concat("getAladinCategory"), categoryId);
					if(categoryMap != null) {
						String categoryMall = categoryMap.get("categoryMall");
						String categoryLimit = categoryMap.get("categoryLimit");
						if (!StringUtils.isEmpty(categoryLimit)) {
							if("MC".equals(libManageCode) && "외국도서".equals(categoryMall)) {
								// 영통도서관은 원서 신청이 가능함
							} else {
								return ValidUtils.resultErrorMap(categoryLimit);
							}
						}
					}
				}
			}
		}
		
		// 1.7 : 제한도서 체크 
		for (String isbn : arIsbn) {
			tmpMap = new HashMap<String, Object>();
			tmpMap.put("isbn", isbn);
			
			String limitReason = (String) this.commonDao.selectOne(USER_SPACE.concat("getLimitBookCount"), tmpMap);
			if (!StringUtils.isEmpty(limitReason)) {
				return ValidUtils.resultErrorMap("신청이 제한된 도서 입니다.\n(제한사유 : " + limitReason + ")");
			}
		}
		
		// 1.8 : 도서관 대출가능 여부 체크
		tmpMap.put("libManageCode", libManageCode);
		tmpMap.put("userNo",        SessionUtils.getUserNo());
		tmpMap.put("isbn", fullIsbn);
		tmpMap.put("arIsbn", arIsbn);
		
		// 도서관 정보
		Map<String, String> libInfo = (Map<String, String>) this.commonDao.selectOne(USER_SPACE.concat("getLibraryInfo"), params);
		libName = libInfo.get("libName");
		
		// 1.8.1 : 대출가능 도서 유무 체크
		Map<String, Object> libBookLoanYn = this.selectLoanYnList(tmpMap);
		if(libBookLoanYn == null || libBookLoanYn.get("resultList") == null) {
			return ValidUtils.resultErrorMap("대출가능여부 조회 중 오류가 발생했습니다.");
		} else {
			List<Map<String, Object>> libBookList = (List<Map<String, Object>>) libBookLoanYn.get("resultList");
			// 대출 가능 도서관 리스트
			ArrayList<String> loanPossibleLibList = new ArrayList<>(); 
			for(Map<String, Object> libBook : libBookList) {
				String loanYn = libBook.get("loanLimitDesc").toString();
				String bookLibName = libBook.get("libName").toString();
				
				if("O".equals(loanYn)) {
					if(!loanPossibleLibList.contains(bookLibName)) {
						loanPossibleLibList.add(StringUtils.remove(bookLibName, "도서관"));
					}
				}
			}
			// 에러메시지
			String errorMsg = "";
			for(String loanPossibleLib : loanPossibleLibList) {
				if(StringUtils.isEmpty(errorMsg)) {
					errorMsg = loanPossibleLib;
				} else {
					if(!errorMsg.contains(loanPossibleLib)) {
						errorMsg += ", " + loanPossibleLib;
					}
				}
			}
			
			if(loanPossibleLibList != null && loanPossibleLibList.size() > 0) {
				return ValidUtils.resultErrorMap(errorMsg + "도서관에 대출 가능한 책이 있습니다.\n해당도서관을 이용해주세요.");
			}
		}
		
		// 1.8.2 : 해당 도서관에 비치중인 도서 수 체크
		Map<String, Object> selectLibBookList = (Map<String, Object>) commonDao.selectOne(USER_SPACE.concat("selectLibBookCountForFurnish"), tmpMap);  
		if(selectLibBookList == null) {
			return ValidUtils.resultErrorMap("대출가능여부 조회 중 오류가 발생했습니다.");
		} else {
			String cnt = selectLibBookList.get("cnt").toString();
			if(Integer.parseInt(cnt) >= 1) {
				// 2019.02.15 안내 문구 수정
				// return ValidUtils.resultErrorMap("도서관에 비치된 도서입니다. 중복도서가 1권 초과인경우 신청이 제한 됩니다.");
				// 2019.03.20 REQ.023 : 안내 문구 수정
				return ValidUtils.resultErrorMap("도서관에서 이미 소장중인 도서는 신청이 제한 됩니다.");
			}
		}
		
		/**********************************************************
		 * NO. 2 KOLASIII 신청가능여부
		 *   2.1 : 개인정보보호 적용여부
		 *   2.2 : 미실명인증자 대출가능여부 체크
		 *   2.3 : 연체자료 체크
		 **********************************************************/
		
		// 2.1 개인정보보호 적용여부
		// 이미 N인 이용자 진행 했던부분으로 진행 할 수 있도록
		// String personYn = (String)commonDao.selectOne(USER_SPACE.concat("getPersonInfoYn"), tmpMap);
		
		Map<String, String> userAgree = (Map<String, String>)commonDao.selectOne(USER_SPACE.concat("getUserAgreeInfo"), tmpMap);
		
		//2.1 개인정보보호 적용여부
		/* N인 이용자도 본인확인해서 대출하는 이용자는 신청
		if ("Y".equals(personYn)) {
			if ("Y".equals(userAgree.get("privacyConfirmYn"))) {
				return ValidUtils.resultErrorMap("주요 개인정보 확인처리 후 신청이 가능합니다.\n[" +libName + "]");
			}
		}
		*/
		
		// 2.2 : 미실명인증자 대출가능여부 체크
		String unknownYn = (String)commonDao.selectOne(USER_SPACE.concat("getUnknownLoanYn"), tmpMap);
		if ("Y".equals(unknownYn)) {
			
			if (!"Y".equals(userAgree.get("agreeYn"))) {
				return ValidUtils.resultErrorMap("개인정보동의를 하지 않아 신청이 불가능합니다.\n[" +libName + "]");
			}
			
			if (StringUtils.isEmpty(userAgree.get("ipinHash"))) {
				return ValidUtils.resultErrorMap("실명인증을 하지 않아 신청이 불가능합니다.\n[" +libName + "]");
			}
		}
		
		// 2.3 : 연체자료 체크
		Integer delayCnt = Integer.parseInt(userAgree.get("delayCnt"));
		if (delayCnt > 0) {
			return ValidUtils.resultErrorMap("연체중인 자료가 있어 신청이 불가능합니다.\n[" +libName + "]");
		}
		
		/**********************************************************
		 * NO. 3 이용자 대출가능여부
		 *    3.1 : 회원 상태 확인 (1 대출 정지, 2 제적, 3 탈퇴)
		 *    3.2 : 회원 구분 확인 (1 비회원, 2. 준회원)
		 *    3.3 : 대출 정지 확인 
		 *    3.4 : 회원증 분실시 대출 가능 체크
		 *    3.5 : 연체료미납시 대출 가능 체크
		 **********************************************************/
		
		String lostYn  = (String)commonDao.selectOne(USER_SPACE.concat("getMemberCardLostByLoanYn"), tmpMap);
		String delayYn = (String)commonDao.selectOne(USER_SPACE.concat("getDelayLoanYn"), tmpMap);
		Map<String, String> loanInfo = (Map<String, String>)commonDao.selectOne(USER_SPACE.concat("getMemberLoanInfo"), tmpMap);
		
		// 3.1 : 회원 상태 확인 (1 대출 정지, 2 제적, 3 탈퇴)
		if (!"0".equals(loanInfo.get("userClass"))) {
			if ("1".equals(loanInfo.get("userClass"))) {
				return ValidUtils.resultErrorMap("대출정지 상태라 신청이 불가능합니다.\n[" +libName + "]");
			} else if ("2".equals(loanInfo.get("userClass"))) {
				return ValidUtils.resultErrorMap("제적 상태라 신청이 불가능합니다.\n[" +libName + "]");
			} else if ("3".equals(loanInfo.get("userClass"))) {
				return ValidUtils.resultErrorMap("탈퇴 상태라 신청이 불가능합니다.\n[" +libName + "]");
			}
		}
		
		// 3.2 : 회원 구분 확인 (1 비회원, 2. 준회원)
		if (!"0".equals(loanInfo.get("memberClass"))) {
			if ("1".equals(loanInfo.get("memberClass"))) {
				return ValidUtils.resultErrorMap("비회원은 신청이 불가능합니다.\n[" +libName + "]");
			} else if ("2".equals(loanInfo.get("memberClass"))) {
				return ValidUtils.resultErrorMap("준회원은 신청이 불가능합니다.\n[" +libName + "]");
			}
		}
		
		// 3.3 : 대출 정지 확인 
		if ("Y".equals(loanInfo.get("loanStopYn"))) {
			return ValidUtils.resultErrorMap("대출정지 상태라 신청이 불가능합니다.\n[" +libName + "]");
		}
		
		// 3.4 : 회원증 분실시 대출 가능 체크
		if ("Y".equals(lostYn)) {
			if ("Y".equals(loanInfo.get("lostUserCard"))) {
				return ValidUtils.resultErrorMap("회원증을 분실하여 신청이 불가능합니다.\n[" +libName + "]");
			}
		}
		
		// 3.5 : 연체료미납시 대출 가능 체크
		if ("Y".equals(delayYn)) {
			if (StringUtils.isNotEmpty(loanInfo.get("arrear"))) {
				return ValidUtils.resultErrorMap("연체료가 존재하여 신청이 불가능합니다.\n[" +libName + "]");
			}
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = new Date();
		
		String firstWork = format.format(today) + " @ 바로대출_비치희망도서 신청";
		
		// 파라미터 구성
		params.put("libManageCode", libManageCode);
		params.put("userName", loanInfo.get("userName"));
		params.put("zipCode", loanInfo.get("zipCode"));
		params.put("addr", loanInfo.get("addr"));
		params.put("tel", loanInfo.get("tel"));
		params.put("mail", loanInfo.get("mail"));
		
		params.put("pubDate", pubDate);
		params.put("idxTitle"    , IndexUtil.makeIdxForColumnSize(1000, IndexUtil.readyToMakeIdx2(title)));
		params.put("idxAuthor"   , IndexUtil.makeIdxForColumnSize(1000, IndexUtil.readyToMakeIdx2(params.get("author"))));
		params.put("idxPublisher", IndexUtil.makeIdxForColumnSize(1000, IndexUtil.readyToMakeIdx2(params.get("publisher"))));
		params.put("idxApplicantName", IndexUtil.makeIdxForColumnSize(100, IndexUtil.readyToMakeIdx2(loanInfo.get("userName"))));
		params.put("firstWork", firstWork);
		
		/**********************************************************
		 * NO. 4 대출가능 권수확인
		 * 2018/02/26 패치로 대출가능 권수와 상관없이 신청 가능
		 **********************************************************/
		/*
		Map<String, String> loanBook = (Map<String, String>)commonDao.selectOne(USER_SPACE.concat("getMemberLoanBookCount"), tmpMap);
		if (Integer.parseInt(loanBook.get("configLocalLoanCount")) < (Integer.parseInt(loanBook.get("localLoanCount")) + 1)) {
			return ValidUtils.resultErrorMap("대출가능 권수를 초과하여 신청이 불가능합니다.\n[" +libName + "]");
		}
		*/
		
		//  비치희망 신청 정보 등록
		commonDao.insert(USER_SPACE.concat("insertFurnishRequest"), params);
		
		Map<String, Object> rtnMap = ValidUtils.resultSuccessMap();
		
		return rtnMap;
	}
	
	/**
	 * 사용자 신청 취소
	 * @param ltRecKey
	 * @return
	 */
	public Map<String, Object> userRequestCancel(List<String> ltRecKey) {
		
		for (String reckey : ltRecKey) {
			
			Map <String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("recKey",    reckey);
			reqMap.put("reqStatus", "U02");
			
			commonDao.update(USER_SPACE.concat("updateReqStatus"), reqMap);
			
			// 2. 신청정보 상태 history 등록;
			reqMap.put("reqStatus", "U02");
			reqMap.put("type",      "U");
			reqMap.put("userId",    SessionUtils.getUserNo());
			
			commonDao.insert("common.insertRequestStatusHistory", reqMap);
		}

		return ValidUtils.resultSuccessMap();
	}
	
	/**
	 * 신청자료 진행상태 조회
	 * 
	 * @param recKey
	 * @return
	 */
	public String getRequestStatus(String recKey) {
		return (String) this.commonDao.selectOne(USER_SPACE.concat("getRequestStatus"), recKey);
	}
	
	/**
	 * 알라딘 카테고리 검색
	 * 
	 * @param isbn
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Integer getAladinCategoryId(String isbn) {
		
		try {
			// 파라미터 정리
			Map<String, String> param = new HashMap<String, String>();
			param.put("output"    , "js");
			param.put("version"   , "20131101");
			param.put("ttbkey"    , this.restApiUtils.getAladinApiTtbkey());
			param.put("ItemId"    , isbn);
			if (!StringUtils.isEmpty(isbn) && isbn.length() == 13) {
				param.put("itemIdType", "ISBN13");
			} else {
				param.put("itemIdType", "ISBN");
			}
			
			// 자료검색
			Map<String, Object> retMap = RestCall.jsonStringToMap(RestCall.APICall(this.restApiUtils.getAladinApiUrl(), "GET", null, param));
			
			// totalResults
			Object totalResults = retMap.get("totalResults");
			if (totalResults != null && totalResults instanceof Integer) {
				if (((Integer) totalResults) == 1) {
					
					// item
					Object item = retMap.get("item");
					if (item != null && item instanceof List) {
						if (((List<Map<String, Object>>) item).size() == 1) {
							
							// item 0
							Object item0 = ((List<Map<String, Object>>) item).get(0);
							if (item0 != null && item0 instanceof Map) {
								
								// stockStatus
								Object stockStatus = ((Map<String, Object>) item0).get("stockStatus");
								if (stockStatus != null && stockStatus instanceof String) {
									if(isbn.equals("9788968330889")){
										
									}else{
										if ("절판".equals((String) stockStatus)) {
											return -1000;
										}
									}
								}
								
								// categoryId
								Object categoryId = ((Map<String, Object>) item0).get("categoryId");
								if (categoryId != null && categoryId instanceof Integer) {
									return (Integer) categoryId;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// 2019.05.07 소스코드 보안취약점 조치
			//e.printStackTrace();
			LOGGER.error("알라딘 카테고리 검색 오류");
		}
	
		return null;
	}
	
	/**
	 * 대출가능 도서
	 * 
	 * @param params {isbn, libManageCode}
	 * @param isbn의 경우 89123456 978123456456 의 경우 공백을 기준으로 or 검색
	 * @return
	 * RETURN 'X(타관책)';
	 * RETURN 'X(예약중)';
	 * RETURN 'X(이용제한)';
	 * RETURN 'X(별치기호제한)';
	 * RETURN 'X(등록구분제한)';
	 * RETURN 'X(자료실제한)';
	 * RETURN 'O';
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectLoanYnList(Map<String, Object> params) {
		if(params.containsKey("arIsbn") == false) {
			String[] arIsbn        = params.get("isbn").toString().split(" ");
			params.put("arIsbn",  Arrays.asList(arIsbn));
		}
		Map<String, Object> retMap     = ValidUtils.resultSuccessMap();
		List<Map<String, Object>> list = (List<Map<String, Object>>) commonDao.selectList(USER_SPACE.concat("selectLoanYnList"), params);
		
		retMap.put("resultList", list);
		
		return retMap;
	}

	/**
	 * 비치희망 신청 내역 조회
	 * 
	 * @param params
	 * @return map
	 */
	public Map<String, Object> selectFurnishRequestInfo(Map<String, String> params) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) commonDao.selectPagingList(USER_SPACE.concat("selectFurnishRequestInfo"), params);
	}

	/**
	 * 비치희망 신청 취소
	 * @param ltRecKey
	 * @return
	 */
	public Map<String, Object> userFurnishRequestCancel(List<String> ltRecKey) {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = new Date();
		
		String lastWork = format.format(today) + "@바로대출-이용자 비치희망 취소";
		for (String reckey : ltRecKey) {
			Map <String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("recKey", reckey);
			reqMap.put("lastWork", lastWork);
			reqMap.put("reqStatus", "4");
			
			commonDao.update(USER_SPACE.concat("updateFurnishReqStatus"), reqMap);
		}

		return ValidUtils.resultSuccessMap();
	}
	
	/**
	 * 서점 혹은 도서관의 출판년도 제한 여부
	 * @param  pubDate      : 신청도서의 출판년도
	 * @param  limitPubDate : 신청제한 기준(~년 미만)
	 * @return true         : 신청가능
	 * @return false        : 신청제한
	 */
	public boolean isPubLimitRequestYn(String pubDate, int limitPubDate) {
		try {
			Date today = new Date();
			Calendar cal = Calendar.getInstance();
			
			cal.setTime(today);
			
			// 출판년도
			int pubYear = Integer.parseInt(pubDate.substring(0, 4));
			// 신청제한 출판년도
			int currYear = cal.get(Calendar.YEAR);
			if(currYear - pubYear > limitPubDate) {
				// 신청 불가능
				return false;
			} else {
				// 신청 가능
				return true;
			}
		} catch (NumberFormatException e) {
			// 2019.05.08 소스코드 보안취약점 조치 
			LOGGER.error("신청도서의 출판년도 변환 오류");
			return false;
		} catch (Exception e) {
			// 2019.05.08 소스코드 보안취약점 조치
			LOGGER.error("출판년도 비교 오류");
			return false;
		}
	}
}
