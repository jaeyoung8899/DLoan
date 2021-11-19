package dloan.store.returnloan;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dloan.common.CommonService;
import dloan.common.dao.CommonDao;
import dloan.common.handler.SessionHandler;
import dloan.common.util.SessionUtils;
import dloan.common.util.ValidUtils;

@Service
public class ReturnLoanService {

	private static final String NAME_SPACE     = "store.returnloan.";
	private static final String STORE_REQEUST = "store.request.";
	private static final String USER_REQEUST  = "user.request.";

	@Autowired
	protected CommonDao commonDao;
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * 대출반납
	 * @param params
	 * @return 대출반납정보
	 */
	public Map<String, Object> selectReturnLoanInfo(Map<String, String> params) {
		return (Map<String, Object>) commonDao.selectPagingList(NAME_SPACE.concat("selectReturnLoanInfo"), params);
	}
	
	/*
	 * 대출
	 */
	@SuppressWarnings({ "unchecked", })
	public Map<String, Object> updateStoreLoan(Map<String, String> params) {
		// 카드비밀번호 확인
		// 2019.11.27 논산 카드비밀번호 체크 사용 x
		/*
		String cardPassword = (String) this.commonDao.selectOne(NAME_SPACE.concat("getCardPasswd"), params);
		if (cardPassword == null || !cardPassword.equals(params.get("passWd"))) {
			return ValidUtils.resultErrorMap("카드비밀번호를 확인해주세요.");
		}
		*/
		
		// 요청 정보 조회
		Map<String, Object> reqInfo = (Map<String, Object>) commonDao.selectOne(STORE_REQEUST.concat("selectStoreRequest"), params.get("recKey"));
		
		String libName = (String)reqInfo.get("libManageName");
		String reqStatus = (String)reqInfo.get("reqStatus");
		if (!"S05".equals(reqStatus)) {
			return ValidUtils.resultErrorMap("대출대기 자료의 경우만 대출가능합니다.\n[" +libName + "]");
		}
		
		// 사용자 정보 조회
		Map<String, String> userInfo = (Map<String, String>) commonDao.selectOne(STORE_REQEUST.concat("getStoreReqUserInfo"), reqInfo.get("userNo").toString());

		Map<String, String> tmpMap = null;
		
		tmpMap = new HashMap<String, String>();
		tmpMap.put("libManageCode", reqInfo.get("libManageCode").toString());
		tmpMap.put("userNo",        reqInfo.get("userNo").toString());
		
		/**********************************************************
		 * NO. 1 이용자 대출가능여부
		 *    1.1 : 회원 상태 확인 (1 대출 정지, 2 제적, 3 탈퇴)
		 *    1.2 : 회원 구분 확인 (1 비회원, 2. 준회원)
		 *    1.3 : 대출 정지 확인 
		 *    1.4 : 회원증 분실시 대출 가능 체크
		 *    1.5 : 연체료미납시 대출 가능 체크
		 **********************************************************/
		
		String lostYn  = (String)commonDao.selectOne(USER_REQEUST.concat("getMemberCardLostByLoanYn"), tmpMap);
		String delayYn = (String)commonDao.selectOne(USER_REQEUST.concat("getDelayLoanYn"), tmpMap);
		Map<String, String> loanInfo = (Map<String, String>)commonDao.selectOne(USER_REQEUST.concat("getMemberLoanInfo"), tmpMap);
		
		// 1.1 : 회원 상태 확인 (1 대출 정지, 2 제적, 3 탈퇴)
		if (!"0".equals(loanInfo.get("userClass"))) {
			if ("1".equals(loanInfo.get("userClass"))) {
				return ValidUtils.resultErrorMap("대출정지 상태라 대출이 불가능합니다.\n[" +libName + "]");
			} else if ("2".equals(loanInfo.get("userClass"))) {
				return ValidUtils.resultErrorMap("제적 상태라 대출이 불가능합니다.\n[" +libName + "]");
			} else if ("3".equals(loanInfo.get("userClass"))) {
				return ValidUtils.resultErrorMap("탈퇴 상태라 대출이 불가능합니다.\n[" +libName + "]");
			}
		}
		
		// 1.2 : 회원 구분 확인 (1 비회원, 2. 준회원)
		if (!"0".equals(loanInfo.get("memberClass"))) {
			if ("1".equals(loanInfo.get("memberClass"))) {
				return ValidUtils.resultErrorMap("비회원이라 대출이 불가능합니다.\n[" +libName + "]");
			} else if ("2".equals(loanInfo.get("memberClass"))) {
				return ValidUtils.resultErrorMap("준회원이라 대출이 불가능합니다.\n[" +libName + "]");
			}
		}
		
		// 1.3 : 대출 정지 확인 
		if ("Y".equals(loanInfo.get("loanStopYn"))) {
			return ValidUtils.resultErrorMap("대출정지 상태라 대출이 불가능합니다.\n[" +libName + "]");
		}
		
		// 1.4 : 회원증 분실시 대출 가능 체크
		if ("Y".equals(lostYn)) {
			if ("Y".equals(loanInfo.get("lostUserCard"))) {
				return ValidUtils.resultErrorMap("회원증을 분실하여 대출이 불가능합니다.\n[" +libName + "]");
			}
		}
		
		// 1.5 : 연체료미납시 대출 가능 체크
		if ("Y".equals(delayYn)) {
			if (StringUtils.isNotEmpty(loanInfo.get("arrear"))) {
				return ValidUtils.resultErrorMap("연체료가 존재하여 대출이 불가능합니다.\n[" +libName + "]");
			}
		}

		/**********************************************************
		 * NO. 2 대출가능 권수확인
		 **********************************************************/
		Map<String, String> loanBook = (Map<String, String>)commonDao.selectOne(USER_REQEUST.concat("getMemberLoanBookCount"), tmpMap);
		if (Integer.parseInt(loanBook.get("configLocalLoanCount")) < (Integer.parseInt(loanBook.get("localLoanCount")) + 1)) {
			return ValidUtils.resultErrorMap("대출가능 권수를 초과하여 대출이 불가능합니다.\n[" +libName + "]");
		}
		
		/**********************************************************
		 * NO. 3 반납예정일 계산
		 **********************************************************/
		// 대출기간 조회
		Integer loanPeriod = (Integer) this.commonDao.selectOne(NAME_SPACE.concat("getLoanPeriod"), tmpMap);
		
		// 공휴일계산
		List<String> holidayList = (List<String>) this.commonDao.selectList(NAME_SPACE.concat("selectHolidayInfo"));
		for (int i = 0; i <= loanPeriod; i++) {
			for (String holiday : holidayList) {
				int compareTo = this.getAddDay(i).compareTo(holiday);
				if (compareTo == 0) {
					loanPeriod++;
					break;
				}
				if (compareTo < 0) {
					break;
				}
			}
		}
		
		reqInfo.put("returnPlanDate", this.getAddDay(loanPeriod));
		reqInfo.put("ipAddr",         SessionHandler.getClientIpAddr());
		
		/**********************************************************
		 * NO. 4 책정보 입력
		 **********************************************************/
		commonDao.insert(NAME_SPACE.concat("insertBook"), reqInfo);
		
		/**********************************************************
		 * NO. 5 대출정보 입력
		 **********************************************************/
		reqInfo.put("storeName",        SessionUtils.getStoreNm());
		reqInfo.put("userReckey",       userInfo.get("recKey"));
		reqInfo.put("userPositionCode", userInfo.get("userPositionCode"));
		reqInfo.put("userClassCode",    userInfo.get("userClassCode"));
		reqInfo.put("memberClass",      userInfo.get("memberClass"));
		
		commonDao.insert(NAME_SPACE.concat("insertLoan"), reqInfo);

		/**********************************************************
		 * NO. 6 대출정보 수정
		 **********************************************************/
		reqInfo.put("reqStatus" ,      "S06");
		reqInfo.put("appendixYn",      params.get("appendixYn"));
		commonDao.update(NAME_SPACE.concat("updateRequestLoan"), reqInfo);
		
		/**********************************************************
		 * NO. 7 사용자 정보 수정(대출 count)
		 **********************************************************/
		commonDao.update(NAME_SPACE.concat("updateUserLoanCount"), reqInfo);
		
		/**********************************************************
		 * NO. 8 신청정보 상태 history 등록
		 **********************************************************/
		Map <String, Object> hisMap = new HashMap<String, Object>();
		hisMap.put("recKey",    params.get("recKey"));
		hisMap.put("reqStatus", "S06");
		hisMap.put("type",      "S");
		hisMap.put("userId",    SessionUtils.getStoreId());
		
		commonDao.insert("common.insertRequestStatusHistory", hisMap);
		
		/// sms 내용
		List<Map<String, Object>> smsList = new ArrayList<Map<String, Object>>(); 
		Map<String, Object> smsMap = null;
		
		Map<String, String> smsParam = new HashMap<String, String>();
		smsParam.put("name", "반납예정일안내 (자동발송)");
		smsParam.put("autoYn", "Y");

		String smsMsg = (String) commonDao.selectOne("common.getSmsContents", smsParam);
		
		// 수정된 대출 만료일 재조회
		Map<String, Object> reqInfo2 = (Map<String, Object>) commonDao.selectOne(STORE_REQEUST.concat("selectStoreRequest"), params.get("recKey"));

		Map<String, String> convData = new HashMap<String, String>();
		convData.put("userName"      , userInfo.get("name"));
		convData.put("storeName"     , (String) reqInfo2.get("storeName"));
		convData.put("title"         , (String) reqInfo2.get("title"));
		convData.put("loanWaitDate"  , (String) reqInfo2.get("loanWaitDt"));
		convData.put("returnPlanDate", (String) reqInfo2.get("returnPlanDt"));
		convData.put("enterPlanDate" , (String) reqInfo2.get("enterPlanDt"));
		String tmpMsg = this.commonService.convSmsMsg(smsMsg, convData);

		if ("Y".equals((String)reqInfo2.get("smsYn"))) {
			smsMap = new HashMap<String, Object>();

			smsMap.put("userName",      userInfo.get("name"));
			smsMap.put("recever",       userInfo.get("handphone"));
			smsMap.put("userKey",       userInfo.get("recKey"));
			smsMap.put("sender",        SessionUtils.getStoreTel());
			/* smsMap.put("msg", tmpMsg); */
			smsMap.put("msg"          , "{\"type\":\"SM\",\"msg\":\""+tmpMsg+"\"}");
			smsMap.put("libManageCode", reqInfo2.get("libManageCode"));
			smsMap.put("worker",        "DLOAN-STORE");

			if (smsMap.get("recever") != null) {
				smsList.add(smsMap);
			}
		}
		Map <String, Object> retMap = ValidUtils.resultSuccessMap();
		retMap.put("smsList", smsList);
		
		return retMap;
	}
	

	/*
	 * 반납
	 */
	@SuppressWarnings({ "unchecked", })
	public Map<String, Object> updateStoreReturn(Map<String, String> params) {
		// 요청 정보 조회
		Map<String, Object> reqInfo  = (Map<String, Object>) commonDao.selectOne(STORE_REQEUST.concat("selectStoreRequest"), params.get("recKey"));
		
		String reqStatus = (String)reqInfo.get("reqStatus");
		String libName = reqInfo.get("libManageName").toString();
		
		if (!"S06".equals(reqStatus)) {
			return ValidUtils.resultErrorMap("대출중 자료의 경우만 반납이 가능합니다.\n[" +libName + "]");
		}
		
		reqInfo.put("ipAddr", SessionHandler.getClientIpAddr());
		
		// 연체 여부
		String overYn = (String)commonDao.selectOne(NAME_SPACE.concat("getReturPlanDateOverYn"), params);
		reqInfo.put("overYn", overYn);
		
		if ("Y".equals(overYn)) {
			Date loanStop = (Date)commonDao.selectOne(NAME_SPACE.concat("getLoanStopDatefunc"), reqInfo);
			
			if (loanStop == null) {
				return ValidUtils.resultErrorMap("대출 정지일 부여에 실패하였습니다.");
			}
			
			reqInfo.put("loanStopDate", loanStop);
		}
		
		/**********************************************************
		 * NO. 1 이용자 대출정보 수정 (대출 count)
		 **********************************************************/
		commonDao.update(NAME_SPACE.concat("updateUserReturnLoan"), reqInfo);
		
		/**********************************************************
		 * NO. 2 대출정보 수정
		 **********************************************************/
		commonDao.update(NAME_SPACE.concat("updateLoanInfo"), reqInfo);
		
		/**********************************************************
		 * NO. 3 책정보 수정
		 **********************************************************/
		commonDao.update(NAME_SPACE.concat("updateBookInfo"), reqInfo);
		
		/**********************************************************
		 * NO. 4 대출정보 수정
		 **********************************************************/
		reqInfo.put("reqStatus", "S07");
		
		commonDao.update(NAME_SPACE.concat("updateRequestReturn"), reqInfo);
		
		/**********************************************************
		 * NO. 5 신청정보 상태 history 등록
		 **********************************************************/
		Map <String, Object> hisMap = new HashMap<String, Object>();
		hisMap.put("recKey", params.get("recKey"));
		hisMap.put("reqStatus", "S07");
		hisMap.put("type", "S");
		hisMap.put("userId", SessionUtils.getStoreId());
		
		commonDao.insert("common.insertRequestStatusHistory", hisMap);
		
		// 부록존재여부
		Map<String, Object> rtnMap = ValidUtils.resultSuccessMap();
		rtnMap.put("appendixYn", reqInfo.get("appendixYn"));
		return rtnMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> updateStoreLoanReqStatus(String reqStatus, List<String> ltRecKey) {
		
		/* 업데이트 전에 정말 다 신청중인지 확인 */
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ltRecKey", ltRecKey);
		List<Map<String, Object>> nowInfo = (List<Map<String, Object>>) this.commonDao.selectList(NAME_SPACE.concat("selectNowStatus"),params);    
		
		
		for (Map<String, Object> now : nowInfo) {
			if (!now.get("reqStatus").equals("S05")) {
				return ValidUtils.resultErrorMap("대출대기중이 아닌 도서가 있습니다. 페이지 새로고침을 진행 해주세요.");
			}
		}
		
		for (String recKey : ltRecKey) {
			
			Map<String, Object> reqInfo  = (Map<String, Object>) commonDao.selectOne(STORE_REQEUST.concat("selectStoreRequest"), recKey);

			commonDao.delete(NAME_SPACE.concat("deleteSpeciesInfo"), reqInfo);
			commonDao.delete(NAME_SPACE.concat("deleteBookInfo"), reqInfo);
			commonDao.delete(NAME_SPACE.concat("deleteIndexInfo"), reqInfo);
			commonDao.delete(NAME_SPACE.concat("deletePurchaseInfo"), reqInfo);

			Map <String, Object> param = new HashMap<String, Object>();
			param.put("recKey",    recKey);
			param.put("reqStatus", reqStatus);
			
			commonDao.update(NAME_SPACE.concat("updateBookWaitReqStatus"), param);
			
			/**********************************************************
			 * NO. 5 신청정보 상태 history 등록
			 **********************************************************/
			Map <String, Object> hisMap = new HashMap<String, Object>();
			hisMap.put("recKey",    recKey);
			hisMap.put("reqStatus", reqStatus);
			hisMap.put("type",      "S");
			hisMap.put("userId",    SessionUtils.getStoreId());
			
			commonDao.insert("common.insertRequestStatusHistory", hisMap);
		}

		return ValidUtils.resultSuccessMap();
	}
	
	/* 대출>>대출대기중 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> updateWaitReqStatus(String reqStatus, List<String> ltRecKey) {
		
		for (String recKey : ltRecKey) {
			
			Map<String, Object> reqInfo  = (Map<String, Object>) commonDao.selectOne(STORE_REQEUST.concat("selectStoreRequest"), recKey);
			reqInfo.put("ipAddr",         SessionHandler.getClientIpAddr());
			commonDao.delete(NAME_SPACE.concat("deleteDBBookInfo"), reqInfo);
			commonDao.delete(NAME_SPACE.concat("deleteLsWorkInfo"), reqInfo);
			commonDao.update(NAME_SPACE.concat("updateUserLoanCount2"), reqInfo);

			Map <String, Object> param = new HashMap<String, Object>();
			param.put("recKey",    recKey);
			param.put("reqStatus", reqStatus);
			
			commonDao.update(NAME_SPACE.concat("updateWaitReqStatus"), param);
			
			/**********************************************************
			 * NO. 5 신청정보 상태 history 등록
			 **********************************************************/
			Map <String, Object> hisMap = new HashMap<String, Object>();
			hisMap.put("recKey",    recKey);
			hisMap.put("reqStatus", reqStatus);
			hisMap.put("type",      "S");
			hisMap.put("userId",    SessionUtils.getStoreId());
			
			commonDao.insert("common.insertRequestStatusHistory", hisMap);
		}

		return ValidUtils.resultSuccessMap();
	}
	
	
	/* 반납>>대출 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> updateLoanReqStatus(String reqStatus, List<String> ltRecKey) {
		
		
		/* 업데이트 전에 연체 자료가 아닌지 확인 */
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ltRecKey", ltRecKey);
		List<Map<String, Object>> nowInfo = (List<Map<String, Object>>) this.commonDao.selectList(NAME_SPACE.concat("selectReturnPlanDate"),params);    
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
		Date time = new Date();
		
		for (Map<String, Object> now : nowInfo) {
			Date time2 = null;
			try {
				time2 = format1.parse((String) now.get("returnPlanDate"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int compare = time.compareTo(time2);
			
			if (compare > 0) {
				return ValidUtils.resultErrorMap("연체된 자료는 대출로 변경 불가능합니다.");
			}
		}
		
		
		for (String recKey : ltRecKey) {
			
			Map<String, Object> reqInfo  = (Map<String, Object>) commonDao.selectOne(STORE_REQEUST.concat("selectStoreRequest"), recKey);
			reqInfo.put("ipAddr",         SessionHandler.getClientIpAddr());
			
			Map<String, Object> lsInfo = (Map<String,Object>) this.commonDao.selectOne(NAME_SPACE.concat("selectLsRequest"), reqInfo);
			
			if(lsInfo==null) {
				/* co에있는걸 ls로 넣어주는 쿼리 */
				commonDao.insert(NAME_SPACE.concat("insertCoLoanToLs"), reqInfo);
				commonDao.delete(NAME_SPACE.concat("deleteCoLoanToLs"), reqInfo);
			}
			
			commonDao.update(NAME_SPACE.concat("updateUserLoanCount3"), reqInfo);
			commonDao.update(NAME_SPACE.concat("updateLsWorkInfo"), reqInfo);
			commonDao.update(NAME_SPACE.concat("updateDbBookInfo"), reqInfo);
			
			Map <String, Object> param = new HashMap<String, Object>();
			param.put("recKey",    recKey);
			param.put("reqStatus", reqStatus);
			
			commonDao.update(NAME_SPACE.concat("updateLoanReqStatus"), param);
			
			/**********************************************************
			 * NO. 5 신청정보 상태 history 등록
			 **********************************************************/
			Map <String, Object> hisMap = new HashMap<String, Object>();
			hisMap.put("recKey",    recKey);
			hisMap.put("reqStatus", reqStatus);
			hisMap.put("type",      "S");
			hisMap.put("userId",    SessionUtils.getStoreId());
			
			commonDao.insert("common.insertRequestStatusHistory", hisMap);
		}

		return ValidUtils.resultSuccessMap();
	}
	
	
	private String getAddDay(int addDay) {
		
		DecimalFormat df = new DecimalFormat("00");
		
		Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.add(Calendar.DATE, addDay);
		
		return Integer.toString(currentCalendar.get(Calendar.YEAR))+ "/"+ df.format(currentCalendar.get(Calendar.MONTH)+1)+ "/"+ df.format(currentCalendar.get(Calendar.DATE));
    }
}
