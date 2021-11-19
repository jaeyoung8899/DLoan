package dloan.library.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dloan.common.CommonService;
import dloan.common.dao.CommonDao;
import dloan.common.util.SessionUtils;
import dloan.common.util.ValidUtils;

@Service
public class LibRequestService {
	
	private static final String NAME_SPACE    = "lib.request.";
	private static final String STORE_REQEUST = "store.request.";
	private static final String STORE_RETURN  = "store.returnloan.";
	
	@Autowired
	protected CommonDao commonDao;
	
	@Autowired
	private CommonService commonService;
	
	public Map<String, Object> selectLibRequestInfo(Map<String, String> params) {	
		return (Map<String, Object>) commonDao.selectPagingList(NAME_SPACE.concat("selectLibRequestInfo"), params);
	}
	
	/**
	 * 상태 변경
	 * @param type
	 * @param reqStatus
	 * @param ltRecKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> updateLibReqStatus(String reqStatus, List<String> ltRecKey) {
		// 2019-04-11 신청거절자료 자동등록 옵션조회
		String autoLimitYn = (String) commonDao.selectOne("getAutoLimitYn");
		if(autoLimitYn == null || autoLimitYn.equals("")) {
			autoLimitYn = "N";
		}
		
		List<Map<String, Object>> smsList = new ArrayList<Map<String, Object>>();
		
		for (String recKey : ltRecKey) {
			// 1. 상태변경
			Map <String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("recKey"   , recKey);
			reqMap.put("reqStatus", reqStatus);
			this.commonDao.update(NAME_SPACE.concat("updateLibReqStatus"), reqMap);
			
			// 2. 상태변경이력
			reqMap.put("type"  , "L");
			reqMap.put("userId", SessionUtils.getLibId());
			this.commonDao.insert("common.insertRequestStatusHistory", reqMap);
			
			// 3. 도서관 승인거절 > 도서관 확인요청 변경시, 신청거절자료가 신청제외도서에 자동등록되는지에 따라 신청제외도서에서도 삭제
			if("Y".equals(autoLimitYn) && reqStatus.equals("S03")) {
				// 3-1. 신청정보 조회
				Map<String, Object> reqInfo = (Map<String, Object>) this.commonDao.selectOne(STORE_REQEUST.concat("selectStoreRequest"), recKey);
				
				String isbnStr = reqInfo.get("isbn") == null ? "" : (String) reqInfo.get("isbn");
				String[] isbnArr = isbnStr.split(" ");
				// 3-2. isbn으로 신청제외도서 삭제
				for(String isbn : isbnArr) {
					reqMap.clear();
					reqMap.put("isbn", isbn);
					this.commonDao.delete("library.limitbook.deleteLimitBook", reqMap);
				}
			}
			
			// SMS양식조회
			// 2019.04.30 도서관 승인 SMS 발송하지 않도록 변경
			/*
			Map<String, String> smsParam = new HashMap<String, String>();
			smsParam.put("autoYn", "Y");
			smsParam.put("name"  , "도서관승인 (자동발송)");
			
			String smsMsg = (String) this.commonDao.selectOne("common.getSmsContents", smsParam);
			if (!StringUtils.isEmpty(smsMsg)) {
				
				// 신청정보조회
				Map<String, Object> reqInfo = (Map<String, Object>) this.commonDao.selectOne(STORE_REQEUST.concat("selectStoreRequest"), recKey);
				
				// 신청자정보조회
				Map<String, String> user = (Map<String, String>) this.commonDao.selectOne(STORE_REQEUST.concat("getStoreReqUserInfo"), reqInfo.get("userNo"));
				
				// SMS발송 메지시정리
				Map<String, String> convData = new HashMap<String, String>();
				convData.put("userName"      , user.get("name"));
				convData.put("storeName"     , (String) reqInfo.get("storeName"));
				convData.put("title"         , (String) reqInfo.get("title"));
				convData.put("loanWaitDate"  , (String) reqInfo.get("loanWaitDt"));
				convData.put("returnPlanDate", (String) reqInfo.get("returnPlanDt"));
				convData.put("enterPlanDate" , (String) reqInfo.get("enterPlanDt"));
				String tmpMsg = this.commonService.convSmsMsg(smsMsg, convData);
				
				// SMS자동발송 내역 추가
				Map<String, Object> smsMap = new HashMap<String, Object>();
				smsMap.put("userKey"      , user.get("recKey"));
				smsMap.put("sender"       , reqInfo.get("libPhone"));
				smsMap.put("userName"     , reqInfo.get("storeName"));
				smsMap.put("recever"      , reqInfo.get("storeHandphone"));
				smsMap.put("msg"          , tmpMsg);
				smsMap.put("libManageCode", reqInfo.get("libManageCode"));
				smsMap.put("worker"       , "DLOAN-LIB");
				
				if (smsMap.get("recever") != null) {
					smsList.add(smsMap);
				}
			}
			*/
		}

		return smsList;
	}
	
	/**
	 * 도서관신청거절
	 * 
	 * @param msgType
	 * @param msg
	 * @param ltRecKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> updateCancelReason(String msgType, String msg, List<String> ltRecKey) {
		// 2019-04-11 신청거절자료 자동등록 옵션조회
		String autoLimitYn = (String) commonDao.selectOne("getAutoLimitYn");
		if(autoLimitYn == null || autoLimitYn.equals("")) {
			autoLimitYn = "N";
		}
		
		List<Map<String, Object>> smsList = new ArrayList<Map<String, Object>>();
		
		// SMS양식조회
		Map<String, String> smsParam = new HashMap<String, String>();
		smsParam.put("name"  , "도서관신청거절 (자동발송)");
		smsParam.put("autoYn", "Y");

		String smsMsg = (String) this.commonDao.selectOne("common.getSmsContents", smsParam);
		
		// 도서관신청거절
		for (String recKey : ltRecKey) {
			// 신청정보 조회
			Map<String, Object> reqInfo = (Map<String, Object>) this.commonDao.selectOne(STORE_REQEUST.concat("selectStoreRequest"), recKey);
			
			// 신청중, 도서관확인요청의 경우에만 SMS발송
			if ("U01".equals(reqInfo.get("reqStatus")) || "S03".equals(reqInfo.get("reqStatus"))) {
				// 발송할 SMS정리
				if (!StringUtils.isEmpty(smsMsg)) {
					// 신청자정보 조회
					Map<String, String> user = (Map<String, String>) this.commonDao.selectOne(STORE_REQEUST.concat("getStoreReqUserInfo"), reqInfo.get("userNo"));
					if (user != null && !StringUtils.isEmpty(user.get("handphone")) && !StringUtils.isEmpty(user.get("name")) && "Y".equals((String)reqInfo.get("smsYn"))) {
						// SMS발송 메지시정리
						Map<String, String> convData = new HashMap<String, String>();
						convData.put("userName"      , user.get("name"));
						convData.put("storeName"     , (String) reqInfo.get("storeName"));
						convData.put("title"         , (String) reqInfo.get("title"));
						convData.put("loanWaitDate"  , (String) reqInfo.get("loanWaitDt"));
						convData.put("returnPlanDate", (String) reqInfo.get("returnPlanDt"));
						convData.put("enterPlanDate" , (String) reqInfo.get("enterPlanDt"));
						String tmpMsg = this.commonService.convSmsMsg(smsMsg, convData);
					
						Map<String, Object> smsMap = new HashMap<String, Object>();
						smsMap.put("userKey"      , user.get("recKey"));
						smsMap.put("sender"       , reqInfo.get("libPhone"));
						smsMap.put("recever"      , user.get("handphone"));
						smsMap.put("msg"          , "{\"type\":\"SM\",\"msg\":\""+tmpMsg+"\"}");
						smsMap.put("libManageCode", reqInfo.get("libManageCode"));
						smsMap.put("worker"       , "DLOAN-LIB");
						smsMap.put("userName"     , user.get("name"));
						smsMap.put("reqStatus"	  , "S04");
						smsList.add(smsMap);
						
					}
				}
			}
			
			// 1. 대출대기의 경우 KOLAS에 입력한 정보 삭제 
			if ("S05".equals(reqInfo.get("reqStatus"))) {
				// 종,책,색인,구입희망 정보 삭제
				this.commonDao.delete(STORE_RETURN.concat("deleteSpeciesInfo" ), reqInfo);
				this.commonDao.delete(STORE_RETURN.concat("deleteBookInfo"    ), reqInfo);
				this.commonDao.delete(STORE_RETURN.concat("deleteIndexInfo"   ), reqInfo);
				this.commonDao.delete(STORE_RETURN.concat("deletePurchaseInfo"), reqInfo);
			}
			
			// 2. 도서관신청거절 상태로 변경
			Map<String, String> reqMap = new HashMap<String, String>();
			reqMap.put("recKey"      , recKey);
			reqMap.put("reqStatus"   , "L02");
			reqMap.put("cancelReason", msg);
			reqMap.put("userId"      , SessionUtils.getLibId());
			this.commonDao.update(NAME_SPACE.concat("updateCancelReasonNStatus"), reqMap);
			
			// 3. 진행상태 변경이력 입력
			reqMap.put("type" , "L");
			this.commonDao.insert("common.insertRequestStatusHistory", reqMap);
			
			// 4. 거절자료 신청제외도서 등록
			if(autoLimitYn.equals("Y")) {
				reqMap.clear();
				reqMap.put("title", (String) reqInfo.get("title"));
				reqMap.put("author", (String) reqInfo.get("author"));
				reqMap.put("publisher", (String) reqInfo.get("publisher"));
				reqMap.put("limitReason", msg);
				reqMap.put("userId", SessionUtils.getLibId());
				
				String reqIsbn = reqInfo.get("isbn") == null ? "" : (String) reqInfo.get("isbn");
				String[] isbnArr = reqIsbn.split(" ");
				for(String isbn : isbnArr) {
					reqMap.put("isbn", isbn);
					// 신청제외도서 등록
					commonDao.insert("library.limitbook.saveLimitBook", reqMap);
				}
			}
		}
		
		return smsList;
	}
	
	/**
	 * 제외도서로 등록
	 * 
	 * @param ltRecKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> updateLimitBook(List<String> ltRecKey) {
		
		// 신청정보 조회
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("arRecKey", ltRecKey);
		List<Map<String, String>> reqList = (List<Map<String, String>>) this.commonDao.selectList(NAME_SPACE.concat("selectRequestInfo"), param);
		
		// 제외도서 등록
		for (Map<String, String> req : reqList) {
			
			String[] arIsbn = req.get("isbn").split(" ");
			for (String isbn : arIsbn) {
				
				Map<String, String> limitParam = new HashMap<String, String>();
				limitParam.put("isbn", isbn);
				limitParam.put("title", req.get("title"));
				limitParam.put("author", req.get("author"));
				limitParam.put("publisher", req.get("publisher"));
				limitParam.put("limitReason", req.get("cancelReason"));
				limitParam.put("userId", SessionUtils.getLibId());
				this.commonDao.update("library.limitbook.saveLimitBook", limitParam);
			}
		}
		
		return ValidUtils.resultSuccessMap();
	}
	
	/**
	 * 배분도서관수정
	 * 
	 * @param changeLib
	 * @param ltRecKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> updateLibrary(String changeLib, List<String> ltRecKey) {
		
		/* 2020.07.07 배분도서관 수정 시, sms 나가도록 수정 */
		/* 2021.04.23 배분도서관 상태값에 따라 다른 테이블도 보정되도록 수정 */
		List<Map<String, Object>> smsList = new ArrayList<Map<String, Object>>();
		
		for (String recKey : ltRecKey) {
			Map<String, Object> reqInfo1 = (Map<String, Object>) this.commonDao.selectOne(STORE_REQEUST.concat("selectStoreRequest"), recKey);
			
			if(reqInfo1.get("resStatus")!=null) { //납품진행된 책은 배분도서관 수정 불가
				
				smsList.add(ValidUtils.resultErrorMap("납품 진행된 도서는 배분 도서관 수정이 불가합니다."));
				return smsList;
				
			}
		}
		
		for (String recKey : ltRecKey) {
			
		// 바뀌기 이전 신청정보 조회
		Map<String, Object> reqInfo1 = (Map<String, Object>) this.commonDao.selectOne(STORE_REQEUST.concat("selectStoreRequest"), recKey);
		
		Map<String, Object> libParam = new HashMap<String, Object>();
		libParam.put("libManageCode", reqInfo1.get("libManageCode"));
		Map<String, Object> libInfo = (Map<String, Object>) this.commonDao.selectOne(NAME_SPACE.concat("selectLibInfo"), libParam);
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("recKey", recKey);
		param.put("changeLib", changeLib);
			
			this.commonDao.update("library.limitbook.changeLibrary", param);
			
			if(reqInfo1.get("reqStatus").equals("S05")) { //대출대기
				
				this.commonDao.update(NAME_SPACE.concat("updateBookInfo"), param);
				this.commonDao.update(NAME_SPACE.concat("updateIdxInfo"), param);
				this.commonDao.update(NAME_SPACE.concat("updatePurchaseInfo"), param);
				this.commonDao.update(NAME_SPACE.concat("updateSpeicesInfo"), param);
				
			}else if(reqInfo1.get("reqStatus").equals("S06") || reqInfo1.get("reqStatus").equals("S07") || reqInfo1.get("reqStatus").equals("S08")) { //대출,반납,미대출취소
				
				this.commonDao.update(NAME_SPACE.concat("updateBookInfo"), param);
				this.commonDao.update(NAME_SPACE.concat("updateIdxInfo"), param);
				this.commonDao.update(NAME_SPACE.concat("updatePurchaseInfo"), param);
				this.commonDao.update(NAME_SPACE.concat("updateSpeicesInfo"), param);
				
				this.commonDao.update(NAME_SPACE.concat("updateDBBookInfo"), param);
				
				int cnt = (int) this.commonDao.selectOne(NAME_SPACE.concat("selectLSCnt"), param);
				if(cnt==0) {
					this.commonDao.update(NAME_SPACE.concat("updateCLInfo"), param);
				}else {
					this.commonDao.update(NAME_SPACE.concat("updateLSInfo"), param);
				}
				
			}
			
		// 바뀐 후 신청정보 조회
		Map<String, Object> reqInfo = (Map<String, Object>) this.commonDao.selectOne(STORE_REQEUST.concat("selectStoreRequest"), recKey);
		
		
		// SMS양식조회
		Map<String, String> smsParam = new HashMap<String, String>();
		smsParam.put("name"  , "배분도서관수정(자동발송)");
		smsParam.put("autoYn", "Y");

		String smsMsg = (String) this.commonDao.selectOne("common.getSmsContents", smsParam);
		
		// 신청자정보조회
		Map<String, String> user = (Map<String, String>) this.commonDao.selectOne(STORE_REQEUST.concat("getStoreReqUserInfo"), reqInfo.get("userNo"));
		
		// SMS발송 메지시정리
		Map<String, String> convData = new HashMap<String, String>();
		convData.put("libName"      , SessionUtils.getLibNm());
		convData.put("title"         ,(String) reqInfo.get("title"));
		String tmpMsg = this.commonService.convSmsMsg(smsMsg, convData);
	
		Map<String, Object> smsMap = new HashMap<String, Object>();
		smsMap.put("userKey"      , user.get("recKey"));
		smsMap.put("sender"       , libInfo.get("libPhone"));
		smsMap.put("recever"      , reqInfo.get("libHandphone"));
		smsMap.put("msg"          , "{\"type\":\"SM\",\"msg\":\""+tmpMsg+"\"}");
		smsMap.put("libManageCode", reqInfo.get("libManageCode"));
		smsMap.put("worker"       , "DLOAN-LIB");
		smsMap.put("userName"     , reqInfo.get("libManageName"));
		smsMap.put("reqStatus"	  , reqInfo.get("reqStatus"));
		smsList.add(smsMap);
		
		}
		
		return smsList;
	}
}
