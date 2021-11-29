package dloan.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dloan.common.CommonService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dloan.common.dao.CommonDao;

@Service("dloan.task.DLoanTaskService")
public class DLoanTaskService {
	
	private static final String NAME_SPACE = "task.";

	@Autowired
	protected CommonDao commonDao;

	@Autowired
	private CommonService commonService;

	/**
	 * 대출대기자료 중에서 대출대기만료일이 지난자료 '미대출취소' 상태변경
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void taskExpiredLoanWait() {
		/**
		 * @author Shin WonBoo
		 * 1. 미대출취소 관련 이용자 신청제한 옵션 조회
		 * 2. 미대출취소자료 건수 조회
		 * 3. 미대출취소자료 조회
		 *  3-1. 대출대기자료 중에서 대출대기만료일이 지난자료 '미대출취소' 상태변경이력 입력
		 *  3-2. 대출대기자료 중에서 대출대기만료일이 지난자료 '미대출취소' 상태변경
		 *  3-3. 신청제한이력 입력
		 */
		
		// 1. 미대출취소 관련 이용자 신청제한 옵션 조회
		String userPenalty = (String) this.commonDao.selectOne(NAME_SPACE.concat("selectUserLimit"), "USER_PENALTY");
		// 미대출취소 이용자 신청제한 적용 여부
		if(!"Y".equals(userPenalty)) {
			userPenalty = "N";
		}
		
		// 2. 미대출취소자료 건수 조회
		Integer limitDataCount = (Integer) this.commonDao.selectOne(NAME_SPACE.concat("noneLoanCount"));
		
		// 미대출 처리 작업건수
		int taskResult = 0;
		if(limitDataCount.intValue() > 0) {
			// 3. 미대출취소자료 조회
			List<Map<String, Object>> limitDataList = (List<Map<String, Object>>) this.commonDao.selectList(NAME_SPACE.concat("noneLoanData"));
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			for(Map<String, Object> limitData : limitDataList) {
				if(limitData.get("userKey") == null || limitData.get("userKey").equals("")) {
					continue;
				}
				paramMap.clear();
				paramMap.put("userKey"     , limitData.get("userKey"));
				paramMap.put("reqKey"      , limitData.get("reqKey"));
				
				// 3.1. 대출대기자료 중에서 대출대기만료일이 지난자료 '미대출취소' 상태변경이력 입력
				this.commonDao.insert(NAME_SPACE.concat("insertReqStatusHis"), paramMap);
				// 3-2. 대출대기자료 중에서 대출대기만료일이 지난자료 '미대출취소' 상태변경
				this.commonDao.update(NAME_SPACE.concat("updateReqStatus"), paramMap);
				
				if("Y".equals(userPenalty)) {
					// 3-3. 신청제한이력 입력
					this.commonDao.insert(NAME_SPACE.concat("insertReqUserLimit"), paramMap);
				}
				taskResult++;
			}
		}
		
		this.commonDao.insert("insertLog", limitDataCount.intValue() + "건 의 미대출취소자료 중 " + taskResult + "건 상태변경 됨");
	}
	
	/**
	 * 논산연무도서관 선결제로 인한 환불불가 상태 추가
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void taskNoRefund() {
		/**
		 * 1. 환불불가 대상 조회
		 * 2. update  dloan_req_tbl set req_status='S09' where rec_key= 바로대출
		 * 3. delete ls_work_t01 where rec_key= 대출
		 * 4. delete co_non_db_book_tbl where rec_key= 긴급대출책정보
		 * 5. delete from bo_purchaseinfo_tbl where rec_key= 긍;ㅂ
		 * 6. delete From idx_bo_tbl where rec_key= 섹ㅇ;ㄴ
		 * 7. delete From bo_species_tbl where rec_key= 종
		 * 7. delete From bo_book_tbl where species_key= 책
		 */
		
		
		// 환불불가 처리 작업건수
		int taskResult = 0;
		// 1. 환불불가 대상 조회
		List<Map<String, Object>> noRefundDataList = (List<Map<String, Object>>) this.commonDao.selectList(NAME_SPACE.concat("noRefundData"));
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		for(Map<String, Object> noRefundData : noRefundDataList) {
			if(noRefundData.get("userKey") == null || noRefundData.get("userKey").equals("")) {
				continue;
			}
			paramMap.clear();
			paramMap.put("reqKey"      , noRefundData.get("reqKey"));
			paramMap.put("userKey"     , noRefundData.get("userKey"));
			paramMap.put("loanKey"     , noRefundData.get("loanKey"));
			paramMap.put("bookKey"     , noRefundData.get("bookKey"));
			paramMap.put("speciesKey"     , noRefundData.get("speciesKey"));
			paramMap.put("purchaseKey"     , noRefundData.get("purchaseKey"));

			 //2. UPDATE  DLOAN_REQ_TBL SET REQ_STATUS='S09' WHERE REC_KEY= 바로대출
			this.commonDao.update(NAME_SPACE.concat("noRefundDloanReqTbl"), paramMap);
			 //3. DELETE LS_WORK_T01 WHERE REC_KEY= 대출
			this.commonDao.delete(NAME_SPACE.concat("noRefundLsWorkT01"), paramMap);
			 //4. DELETE CO_NON_DB_BOOK_TBL WHERE REC_KEY= 긴급대출책정보
			this.commonDao.delete(NAME_SPACE.concat("noRefundCoNonDbBookTbl"), paramMap);
			 //5. DELETE FROM BO_PURCHASEINFO_TBL WHERE REC_KEY= 구입
			this.commonDao.delete(NAME_SPACE.concat("noRefundBoPurchaseinfoTbl"), paramMap);
			 //6. DELETE fROM IDX_BO_TBL WHERE REC_KEY= 색인
			this.commonDao.delete(NAME_SPACE.concat("noRefundIdxBoTbl"), paramMap);
			 //7. DELETE fROM BO_SPECIES_TBL WHERE REC_KEY= 종
			this.commonDao.delete(NAME_SPACE.concat("noRefundBoSpeciesTbl"), paramMap);
			 //8. DELETE fROM BO_BOOK_TBL WHERE SPECIES_KEY= 책
			this.commonDao.delete(NAME_SPACE.concat("noRefundBoBookTbl"), paramMap);

			taskResult++;
		}
		
		this.commonDao.insert("insertLog", noRefundDataList.size() + "건 의 환불불가 중 " + taskResult + "건 상태변경 됨");
	}

	/**
	 * 청주시립도서관 반환일이 지나면 반환 비활성화 로직 추가
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void taskNoReturn() {

		// 미반환취소 처리 작업건수
		int taskResult = 0;
		// 1. 미반환취소 대상 조회
		List<Map<String, Object>> noReturnDataList = (List<Map<String, Object>>) this.commonDao.selectList(NAME_SPACE.concat("noReturnData"));

		for(Map<String, Object> noReturnData : noReturnDataList) {
			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("recKey"      , noReturnData.get("recKey"));
			paramMap.put("purchaseKey" , noReturnData.get("purchaseKey"));

			//5. DELETE FROM BO_PURCHASEINFO_TBL WHERE REC_KEY= 구입
			this.commonDao.delete(NAME_SPACE.concat("noReturnBoPurchaseinfoTbl"), paramMap);
			//6. DELETE fROM IDX_BO_TBL WHERE REC_KEY= 색인
			this.commonDao.delete(NAME_SPACE.concat("noReturnIdxBoTbl"), paramMap);
			//7. DELETE fROM BO_SPECIES_TBL WHERE REC_KEY= 종
			this.commonDao.delete(NAME_SPACE.concat("noReturnBoSpeciesTbl"), paramMap);
			//8. DELETE fROM BO_BOOK_TBL WHERE SPECIES_KEY= 책
			this.commonDao.delete(NAME_SPACE.concat("noReturnBoBookTbl"), paramMap);
			//2. UPDATE  DLOAN_REQ_TBL SET REQ_STATUS='S09' WHERE REC_KEY= 바로대출
			this.commonDao.update(NAME_SPACE.concat("noReturnDloanReqTbl"), paramMap);

			// 상태변경이력
			paramMap.put("type"  , "T");
			paramMap.put("userId", "TASK");
			paramMap.put("reqStatus", "S09");
			this.commonDao.insert("common.insertRequestStatusHistory", paramMap);

			taskResult++;
		}

		this.commonDao.insert("insertLog", noReturnDataList.size() + "건 의 미반환취소 중 " + taskResult + "건 상태변경 됨");
	}

	//반납독촉안내 문자
	public void loanScheduler(){
		Map<String,Object> param = new HashMap<>();

		//n일전 전송일 조회
		int smsDate = Integer.parseInt((String) this.commonDao.selectOne(NAME_SPACE.concat("selectUserLimit"), "LOAN_SMS_DATE"));
		//전송 간격일 조회
		int smsPeriod = Integer.parseInt((String) this.commonDao.selectOne(NAME_SPACE.concat("selectUserLimit"), "LOAN_SMS_PERIOD"));
		//전송 횟수 조회
		int smsCount  = Integer.parseInt((String) this.commonDao.selectOne(NAME_SPACE.concat("selectUserLimit"), "LOAN_SMS_COUNT"));
		//최대 전송일수
		int maxSendDate = smsPeriod * smsCount;
		param.put("smsDate",smsDate);
		param.put("smsPeriod",smsPeriod);
		param.put("maxSendDate",maxSendDate);

		int noneCnt = (int) commonDao.selectOne(NAME_SPACE.concat("noneReturnUserCount"),param);

		if(noneCnt > 0) {

			List<Map<String,Object>> noneReturnUser = (List<Map<String, Object>>) commonDao.selectList(NAME_SPACE.concat("noneReturnUser"),param);

			for (Map<String, Object> stringObjectMap : noneReturnUser) {
				if (stringObjectMap.get("smsYn").equals("Y")) {
					// SMS양식조회
					Map<String, String> smsParam = new HashMap<>();
					smsParam.put("smsType", "DLN09");
					smsParam.put("autoYn", "Y");

					//sms내용 조회
					String smsMsg = (String) this.commonDao.selectOne("common.getSmsInfo", smsParam);

					if (!StringUtils.isEmpty(smsMsg)) {
						// 강서구 KOLAS 사용안함 신청자정보 조회
						//Map<String, String> user = (Map<String, String>) commonDao.selectOne(NAME_SPACE.concat("getStoreReqUserInfo"), reqInfo.get("userNo"));

						// SMS발송 메지시정리

						Map<String, String> convData = new HashMap<String, String>();
						if(stringObjectMap.get("userName").toString().equals("")){
							convData.put("userName", (String) stringObjectMap.get("userNo"));
							smsParam.put("userName", (String) stringObjectMap.get("userNo"));
						}else{
							convData.put("userName", (String) stringObjectMap.get("userName"));
							smsParam.put("userName", (String) stringObjectMap.get("userName"));
						}
						convData.put("storeName", (String) stringObjectMap.get("storeName"));
						convData.put("title", (String) stringObjectMap.get("title"));
						convData.put("returnPlanDate", (String) stringObjectMap.get("returnPlanDt"));
						String tmpMsg = this.commonService.convSmsMsg(smsMsg, convData);

						// SMS자동발송 내역 추가
						Map<String, Object> smsMap = new HashMap<String, Object>();
						smsMap.put("recever", stringObjectMap.get("handphone"));
						smsMap.put("userKey", stringObjectMap.get("userNo"));
						smsMap.put("sender", stringObjectMap.get("storePhone"));
						/* smsMap.put("msg", tmpMsg); */
						smsMap.put("msg", tmpMsg);
						smsMap.put("libManageCode", stringObjectMap.get("libManageCode"));
						smsMap.put("worker", "DLOAN-STORE");
						String alimMsg = commonService.convAlimMsg(smsParam, convData);
						if (!alimMsg.equals("")) {
							smsMap.put("alimMsg", alimMsg);
						}

						commonService.smsSend(smsMap);

					}

				}
			}

		}

	}
	
	
}
