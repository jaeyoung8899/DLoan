package dloan.store.request;

import java.text.DateFormat;
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
import dloan.common.util.IndexUtil;
import dloan.common.util.SessionUtils;
import dloan.common.util.ValidUtils;

@Service
public class StoreRequestService {
	
	private static final String NAME_SPACE = "store.request.";
	
	@Autowired
	private CommonService commonService;

	@Autowired
	protected CommonDao commonDao;
	
	/**
	 * 신청승인
	 * @param params
	 * @return
	 */
	public Map<String, Object> selectStoreRequestInfo(Map<String, String> params) {
		params.put("storeYn",SessionUtils.getStoreYn());
		return (Map<String, Object>) commonDao.selectPagingList(NAME_SPACE.concat("selectStoreRequestInfo"), params);
	}
	
	/**
	 * 상태변경
	 *  - 도서관확인요청(ST02)	: 신청중 -> 도서관확인요청
	 *  - 도서준비 (ST03)		: 신청중,도서관승인 -> 도서준비
	 * 
	 * @param type
	 * @param reqStatus
	 * @param ltRecKey
	 * @param enterPlanDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> updateStoreReqStatus(String type, String reqStatus, List<String> ltRecKey, String enterPlanDate, String confirmMessage) {

		// SMS자동발송 내역
		List<Map<String, Object>> smsList = new ArrayList<Map<String, Object>>(); 
		
		
		/* 업데이트 전에 정말 다 신청중인지 확인 */
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ltRecKey", ltRecKey);
		List<Map<String, Object>> nowInfo = (List<Map<String, Object>>) this.commonDao.selectList(NAME_SPACE.concat("selectNowStatus"),params);    
		
		
		for (Map<String, Object> now : nowInfo) {
			if(type.equals("ST02")) {
				if (!now.get("reqStatus").equals("U01")) {
					Map<String, Object> smsMap = new HashMap<String, Object>();
					smsMap.put("notU01", "Y");
					smsList.add(smsMap);
					return smsList;
				}
			}else if(type.equals("ST03")) {
				if (!now.get("reqStatus").equals("U01")&&!now.get("reqStatus").equals("L01")) {
					Map<String, Object> smsMap = new HashMap<String, Object>();
					smsMap.put("notU01", "Y");
					smsList.add(smsMap);
					return smsList;
				}
			}
			
		}
		
		
		for (String recKey : ltRecKey) {
			// 상태변경
			Map <String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("recKey"       , recKey);
			reqMap.put("reqStatus"    , reqStatus);
			reqMap.put("type"         , type);
			reqMap.put("enterPlanDate", enterPlanDate);
			reqMap.put("confirmMessage", confirmMessage);
			this.commonDao.update(NAME_SPACE.concat("updateStoreReqStatus"), reqMap);
			
			// 상태변경이력
			reqMap.put("type",      "S");
			reqMap.put("userId",    SessionUtils.getStoreId());
			this.commonDao.insert("common.insertRequestStatusHistory", reqMap);
			
			// 신청정보조회
			Map<String, Object> reqInfo = (Map<String, Object>) this.commonDao.selectOne(NAME_SPACE.concat("selectStoreRequest"), recKey);
			
			// SMS수신여부확인
			// 2019.04.30 간담회 개선 : 도서준비 SMS미발송으로 변경, 도서관확인요청만 SMS 발송
			// if ("ST02".equals(type) || "Y".equals((String)reqInfo.get("smsYn"))) {
			if ("Y".equals((String)reqInfo.get("smsYn"))) {
				// SMS양식조회
				Map<String, String> smsParam = new HashMap<>();
				smsParam.put("autoYn", "Y");

				if ("ST02".equals(type)) {
					smsParam.put("smsType", "DLN04");
				} else {
					if (StringUtils.isEmpty(enterPlanDate)) {
						smsParam.put("smsType", "DLN05");
					} else {
						smsParam.put("smsType", "DLN08");
					}
				}

				String smsMsg = (String) this.commonDao.selectOne("common.getSmsContents", smsParam);
				if (!StringUtils.isEmpty(smsMsg)) {
					// 신청자정보조회
					Map<String, String> user = (Map<String, String>) this.commonDao.selectOne(NAME_SPACE.concat("getStoreReqUserInfo"), reqInfo.get("userNo"));
					
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
					if ("ST02".equals(type)) {
						smsMap.put("userName", reqInfo.get("libManageName"));
						smsMap.put("recever",  reqInfo.get("libHandphone"));
					} else {
						smsMap.put("userName", user.get("name"));
						smsMap.put("recever",  user.get("handphone"));
					}
					smsMap.put("userKey",       user.get("recKey"));
					smsMap.put("sender",        SessionUtils.getStoreTel());
					/* smsMap.put("msg", tmpMsg); */
					smsMap.put("msg"          , "{\"type\":\"SM\",\"msg\":\""+tmpMsg+"\"}");
					smsMap.put("libManageCode", reqInfo.get("libManageCode"));
					smsMap.put("worker",        "DLOAN-STORE");

					String alimMsg = commonService.convAlimMsg(smsParam,convData);
					if(!alimMsg.equals("")){
						smsMap.put("alimMsg",alimMsg);
					}
					
					if (smsMap.get("recever") != null) {
						smsList.add(smsMap);
					}
				}
			}
		}

		return smsList;
	}
	
	
	@SuppressWarnings("unchecked") 
	public Map<String, Object> updatePrevReqStatus(List<String> ltRecKey) {

		
		for (String recKey : ltRecKey) {
			// 상태변경
			Map <String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("recKey"       , recKey);
			
			Map<String, Object> reqInfo = (Map<String, Object>) this.commonDao.selectOne(NAME_SPACE.concat("selectStoreRequest"), recKey);
			
			String reqStatus = (String) reqInfo.get("reqStatus");
			
			if(reqStatus.equals("U02") || reqStatus.equals("S04")) {
				reqMap.put("type", reqStatus);
				reqMap.put("reqStatus", "U01");
				this.commonDao.update(NAME_SPACE.concat("updatePrevReqStatus"), reqMap);
			}
			
			// 상태변경이력
			reqMap.put("type",      "S");
			reqMap.put("userId",    SessionUtils.getStoreId());
			this.commonDao.insert("common.insertRequestStatusHistory", reqMap);
			
		}

		return ValidUtils.resultSuccessMap();
	}

	/**
	 * 상태변경
	 *  - 대출대기(ST04) : 신청중,도서관승인,도서준비 -> 대출대기
	 * 
	 * @param type
	 * @param reqStatus
	 * @param ltRecKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> updateStoreReqStatusLoanWait(String type, String reqStatus, List<String> ltRecKey) {

		// SMS자동발송 내역
		List<Map<String, Object>> smsList = new ArrayList<Map<String, Object>>(); 
		
		for (String recKey : ltRecKey) {
			// 신청정보조회
			Map<String, Object> reqInfo = (Map<String, Object>) commonDao.selectOne(NAME_SPACE.concat("selectStoreRequest"), recKey);
			reqInfo.put("ipAddr", SessionHandler.getClientIpAddr());
			reqInfo.put("year"  , (String) this.commonDao.selectOne(NAME_SPACE.concat("getYear")));

			// 차수정보 존재확인
			Integer seqCnt = (Integer) this.commonDao.selectOne(NAME_SPACE.concat("getSeqCount"), reqInfo);
			if (seqCnt == 0) {
				// 차수정보가 존재하지 않을 경우 입력
				this.commonDao.insert(NAME_SPACE.concat("insertPurchaseSeq"), reqInfo);
			}
			
			// 종정보 입력
			this.commonDao.insert(NAME_SPACE.concat("insertSpeciesInfo"), reqInfo);
			
			// 책정보 입력
			this.commonDao.insert(NAME_SPACE.concat("insertBookInfo"), reqInfo);
			
			// 색인정보 입력
			String idxAll = "";
			
			String idxTitle = (String) reqInfo.get("title");
			if (!StringUtils.isEmpty(idxTitle)) {
				idxTitle = IndexUtil.readyToMakeIdx2(idxTitle);
				idxAll  += idxTitle;
			}
			
			String idxAuthor = (String) reqInfo.get("author");
			if (!StringUtils.isEmpty(idxAuthor)) {
				idxAuthor = IndexUtil.readyToMakeIdx2(idxAuthor);
				idxAll  += (" " + idxAuthor);
			}
			
			String idxpublisher = (String) reqInfo.get("publisher");
			if (!StringUtils.isEmpty(idxpublisher)) {
				idxpublisher = IndexUtil.readyToMakeIdx2(idxpublisher);
				idxAll  += (" " + idxpublisher);
			}

			reqInfo.put("idxTitle",     IndexUtil.makeIdxForColumnSize(3000, idxTitle));
			reqInfo.put("idxAuthor",    IndexUtil.makeIdxForColumnSize(4000, idxAuthor));
			reqInfo.put("idxPublisher", IndexUtil.makeIdxForColumnSize(2000, idxpublisher));
			reqInfo.put("idxAll",       IndexUtil.makeIdxForColumnSize(4000, idxAll));
			
			// 색인데이터 입력
			this.commonDao.insert(NAME_SPACE.concat("insertIndexInfo"), reqInfo);
			
			// 구입정보 입력
			this.commonDao.insert(NAME_SPACE.concat("insertPurchaseInfo"), reqInfo);
			
			// 상태변경
			Map <String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("recKey",      recKey);
			reqMap.put("reqStatus",   reqStatus);
			reqMap.put("type",        type);
			reqMap.put("userNo",      reqInfo.get("userNo"));
			reqMap.put("specieskKey", reqInfo.get("specieskKey"));
			this.commonDao.update(NAME_SPACE.concat("updateStoreReqStatus"), reqMap);
			
			// 상태변경이력
			reqMap.put("type",   "S");
			reqMap.put("userId", SessionUtils.getStoreId());
			this.commonDao.insert("common.insertRequestStatusHistory", reqMap);
			
			// 수정된 대출 만료일 재조회
			Map<String, Object> reqInfo2 = (Map<String, Object>) this.commonDao.selectOne(NAME_SPACE.concat("selectStoreRequest"), recKey);

			// SMS양식조회
			Map<String, String> smsParam = new HashMap<String, String>();
			smsParam.put("smsType",   "DLN06");
			smsParam.put("autoYn", "Y");

			// SMS수신여부확인
			if ("Y".equals((String)reqInfo2.get("smsYn"))) {
				
				Calendar cal = Calendar.getInstance();
		        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        Date date = new Date();
		        cal.setTime(date);
		        cal.add(Calendar.DATE, 7);
		        String loanWaitDate = df.format(cal.getTime());



				// SMS자동발송 내역 추가
				Map<String, Object> smsMap = new HashMap<String, Object>();
				
				// 신청자정보 조회
				Map<String, String> user = (Map<String, String>) commonDao.selectOne(NAME_SPACE.concat("getStoreReqUserInfo"), reqInfo.get("userNo"));
				
				// SMS발송 메지시정리
				Map<String, String> convData = new HashMap<String, String>();
				convData.put("userName"      , user.get("name"));
				convData.put("storeName"     , (String) reqInfo2.get("storeName"));
				convData.put("title"         , (String) reqInfo2.get("title"));
				convData.put("loanWaitDate"  , loanWaitDate);
				convData.put("returnPlanDate", (String) reqInfo2.get("returnPlanDt"));
				convData.put("enterPlanDate" , (String) reqInfo2.get("enterPlanDt"));
				convData.put("storePhone" , (String) reqInfo2.get("storePhone"));
				convData.put("libManageName" , (String) reqInfo2.get("libManageName"));
				String tmpMsg = "";


				String smsMsg = (String) this.commonDao.selectOne("common.getSmsContents", smsParam);
				if (!StringUtils.isEmpty(smsMsg)) {
					tmpMsg = this.commonService.convSmsMsg(smsMsg, convData);
				}
				
				smsMap.put("userName",      user.get("name"));
				smsMap.put("recever",       user.get("handphone"));
				smsMap.put("userKey",       user.get("recKey"));
				smsMap.put("sender",        SessionUtils.getStoreTel());
				/* smsMap.put("msg", tmpMsg); */
				smsMap.put("msg"          , "{\"type\":\"SM\",\"msg\":\""+tmpMsg+"\"}");
				smsMap.put("libManageCode", reqInfo2.get("libManageCode"));
				smsMap.put("worker",        "DLOAN-STORE");
				smsMap.put("smsType",       "DLN06");

				String alimMsg = commonService.convAlimMsg(smsParam,convData);
				if(!alimMsg.equals("")){
					smsMap.put("alimMsg",alimMsg);
				}

				if (smsMap.get("recever") != null) {
					smsList.add(smsMap);
				}
			}
		}

		return smsList;
	}
	
	/**
	 * SMS 발송
	 * @param msgType
	 * @param msg
	 * @param ltRecKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> storeReqSmsSend(String msgType, String msg, List<String> ltRecKey) {
		
		Map<String, Object> smsMap = null;
		Map<String, Object> reqInfo    = null;
		Map<String, String> user   = null;
		String tmpMsg = "";
		for (String recKey : ltRecKey) {
			
			reqInfo = (Map<String, Object>) commonDao.selectOne(NAME_SPACE.concat("selectStoreRequest"), recKey);
			user    = (Map<String, String>) commonDao.selectOne(NAME_SPACE.concat("getStoreReqUserInfo"), reqInfo.get("userNo"));
			
			if ("self".equals(msgType)) {
				tmpMsg = msg;
			} else {
				
				Map<String, String> convData = new HashMap<String, String>();
				convData.put("userName"      , user.get("name"));
				convData.put("storeName"     , (String) reqInfo.get("storeName"));
				convData.put("title"         , (String) reqInfo.get("title"));
				convData.put("loanWaitDate"  , (String) reqInfo.get("loanWaitDt"));
				convData.put("returnPlanDate", (String) reqInfo.get("returnPlanDt"));
				convData.put("enterPlanDate" , (String) reqInfo.get("enterPlanDt"));
				tmpMsg = this.commonService.convSmsMsg(msg, convData);
			}
			
			if (user != null && user.get("handphone") != null && user.get("name") != null) {
				
				smsMap = new HashMap<String, Object>();
				smsMap.put("userKey",       user.get("recKey"));
				smsMap.put("sender",        SessionUtils.getStoreTel());
				smsMap.put("recever",       user.get("handphone"));
				/* smsMap.put("msg", tmpMsg); */
				smsMap.put("msg"          , "{\"type\":\"SM\",\"msg\":\""+tmpMsg+"\"}");
				smsMap.put("libManageCode", reqInfo.get("libManageCode"));
				smsMap.put("worker",        "DLOAN-STORE");
				smsMap.put("userName",      user.get("name"));
				
				commonService.smsSend(smsMap);
			}
		}
		return ValidUtils.resultSuccessMap();
	}

	/**
	 * 거절
	 * @param msgType
	 * @param msg
	 * @param ltRecKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> updateCancelReason(String msgType, String msg, List<String> ltRecKey) {
		// 2019-04-11 서점거절자료 자동등록 옵션조회
		String autoLimitYn = (String) commonDao.selectOne("getAutoLimitYn");
		if(autoLimitYn == null || autoLimitYn.equals("")) {
			autoLimitYn = "N";
		}
		
		List<Map<String, Object>> smsList = new ArrayList<Map<String, Object>>(); 
		Map<String, Object> smsMap  = null;
		Map<String, Object> reqInfo = null;
		Map<String, String> user    = null;
		Map<String, String> reqMap  = null;
		for (String recKey : ltRecKey) {
			reqInfo = (Map<String, Object>) commonDao.selectOne(NAME_SPACE.concat("selectStoreRequest"), recKey);
			user    = (Map<String, String>) commonDao.selectOne(NAME_SPACE.concat("getStoreReqUserInfo"), reqInfo.get("userNo"));
		
			if (user != null && user.get("handphone") != null && user.get("name") != null) {
				if ("Y".equals((String)reqInfo.get("smsYn"))) {
					// SMS양식조회
					Map<String, String> smsParam = new HashMap<String, String>();
					smsParam.put("smsType",   "DLN02");
					smsParam.put("autoYn", "Y");

					String smsMsg = (String) this.commonDao.selectOne("common.getSmsContents", smsParam);
					if (!StringUtils.isEmpty(smsMsg)) {
						// SMS발송 메지시정리
						Map<String, String> convData = new HashMap<String, String>();
						convData.put("userName"      , user.get("name"));
						convData.put("storeName"     , (String) reqInfo.get("storeName"));
						convData.put("title"         , (String) reqInfo.get("title"));
						convData.put("loanWaitDate"  , (String) reqInfo.get("loanWaitDt"));
						convData.put("returnPlanDate", (String) reqInfo.get("returnPlanDt"));
						convData.put("enterPlanDate" , (String) reqInfo.get("enterPlanDt"));
						String tmpMsg = this.commonService.convSmsMsg(smsMsg, convData);
						
						//
						smsMap = new HashMap<String, Object>();
						smsMap.put("userKey",       user.get("recKey"));
						smsMap.put("sender",        SessionUtils.getStoreTel());
						smsMap.put("recever",       user.get("handphone"));
						/* smsMap.put("msg", tmpMsg); */
						smsMap.put("msg"          , "{\"type\":\"SM\",\"msg\":\""+tmpMsg+"\"}");
						smsMap.put("libManageCode", reqInfo.get("libManageCode"));
						smsMap.put("worker",        "DLOAN-STORE");
						smsMap.put("userName",      user.get("name"));
						smsMap.put("smsType"  , "DLN02");

						String alimMsg = commonService.convAlimMsg(smsParam,convData);
						if(!alimMsg.equals("")){
							smsMap.put("alimMsg",alimMsg);
						}
						smsList.add(smsMap);
					}
				}
			}
			
			// 1. 신청정보 update
			reqMap = new HashMap<String, String>();
			reqMap.put("recKey",       recKey);
			reqMap.put("reqStatus",    "S02");
			reqMap.put("cancelReason", msg);
			reqMap.put("userId",       SessionUtils.getStoreId());
			commonDao.update(NAME_SPACE.concat("updateCancelReasonNStatus"), reqMap);

			// 2. 신청정보 상태 history 등록;
			reqMap.put("type", "S");
			commonDao.insert("common.insertRequestStatusHistory", reqMap);
			
			// 3. 거절자료 신청제외도서 등록
			if(autoLimitYn.equals("Y")) {
				reqMap.clear();
				reqMap.put("title", (String) reqInfo.get("title"));
				reqMap.put("author", (String) reqInfo.get("author"));
				reqMap.put("publisher", (String) reqInfo.get("publisher"));
				reqMap.put("limitReason", msg);
				reqMap.put("userId", SessionUtils.getStoreId());
				
				String reqIsbn = (String) reqInfo.get("isbn");
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
}
