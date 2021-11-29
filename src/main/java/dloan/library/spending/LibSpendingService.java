package dloan.library.spending;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import dloan.common.dao.CommonDao;
import dloan.common.util.SessionUtils;
import dloan.common.util.RestCall;
import dloan.common.util.ValidUtils;

@Service
public class LibSpendingService {
	
	private static final String NAME_SPACE = "lib.spending.";

	@Autowired
	protected CommonDao commonDao;
	
	// 서점 목록
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> storeInfo(Map<String, Object> params) {
		return (List<Map<String, String>>) commonDao.selectList(NAME_SPACE.concat("storeInfo"), params);
	}
	
	/**
	 * 서점 목록 반환 서비스
	 * 
	 * @param params
	 * @return
	 */
	public Map<String, Object> getStore(Map<String, Object> params) {
		Map<String, Object> retMap = ValidUtils.resultSuccessMap();
		retMap.put("storeList", this.storeInfo(params));
		return retMap;
	}
	
	// 지출결의 등록
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = {Exception.class, Error.class})
	public Map<String, Object> regPrice(Map<String, String> params) throws Exception {
		Map<String, Object> returnMap = null;
		
		try {
			// 필수 체크 
			Map<String, Object> retMap = ValidUtils.requiredMap(params, 
					new String[] {"libManageCode", "class", "fiscalYear", "budget", "deadline"}, 
					new String[] {"관리구분", "구분", "회계연도", "금액", "회기말"});
			if (!retMap.isEmpty()) {
				return retMap;
			}
			// 세션 체크
			if(SessionUtils.getLibId() == null) {
				return ValidUtils.resultErrorMap("로그인 도서관 정보를 찾을 수 없습니다.");
			} else {
				params.put("libId", SessionUtils.getLibId());
			}
			
			// 회계연도로 중복등록된 지출결의 데이터가 존재하는지 체크
			Map<String, Object> duplicatedSpendingInfo = (Map<String, Object>) commonDao.selectOne("getDuplicatedSpendingCount", params);
			if(duplicatedSpendingInfo != null && duplicatedSpendingInfo.get("cnt") != null && Integer.parseInt((String) duplicatedSpendingInfo.get("cnt")) > 0) {
				return ValidUtils.resultErrorMap(params.get("fiscalYear") + "년도 지출결의는 이미 등록되어 있습니다.");
			}
			
			// 지출결의 등록
			commonDao.insert(NAME_SPACE.concat("insertSpending"), params);
			
			if(!params.containsKey("rKey") || params.get("rKey") == null) {
				return ValidUtils.resultErrorMap("지출결의 데이터 입력 중 오류가 발생했습니다.");
			}
			
			int rKey = Integer.parseInt(params.get("rKey"));
			
			String storeInfoStr = (String) params.get("storeInfo");
			if(!StringUtils.isEmpty(storeInfoStr)) {
				JSONArray jArray = new JSONArray(storeInfoStr);
				
				for(int i = 0;i < jArray.length();i++) {
					// json to Map
					Map<String, Object> reqMap = RestCall.jsonStringToMap(jArray.get(i).toString());
					// 도서관 사용자 ID 입력
					reqMap.put("libId", SessionUtils.getLibId());
					// 지출결의 키 입력
					reqMap.put("parentKey", rKey);
					// 등록
					commonDao.update(NAME_SPACE.concat("insertSpendingInfo"), reqMap);
				}
			}
			
			returnMap = ValidUtils.resultSuccessMap();
		} catch (Exception e) {
			// 트랜잭션에 의해 롤백될 수 있도록 예외발생시킴
			throw new Exception();
		}
		
		return returnMap;
	}
	
	// 지출결의 리스트 조회
	public Map<String, Object> selectLibSpendingInfo(Map<String, String> params) {
		return (Map<String, Object>) commonDao.selectPagingList(NAME_SPACE.concat("selectLibSpendingInfo"), params);
	}
	
	// 반품도서 조회
	public Map<String, Object> selectLibRefundBookInfo(Map<String, String> params) {
		return (Map<String, Object>) commonDao.selectPagingList(NAME_SPACE.concat("getRefundBookInfo"), params);
	}
	
	// 지출결의 상세정보 조회
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectLibSpendingDetailInfo(Map<String, String> params) {
		// 필수 체크
		Map<String, Object> retMap = ValidUtils.requiredMap(params, 
				new String[] {"recKey"}, 
				new String[] {"지출결의 키"});
		if (!retMap.isEmpty()) {
			return retMap;
		}
		
		Map<String, Object> spendingMap = (Map<String, Object>) commonDao.selectOne(NAME_SPACE.concat("getSpendingDetailInfo"), params);
		if(spendingMap == null) {
			retMap = ValidUtils.resultErrorMap("지출결의 데이터를 조회하지 못했습니다.");
			return retMap;
		}
		
		params.put("parentKey", params.get("recKey"));
		params.put("libManageCode", (String) spendingMap.get("libManageCode"));
		
		retMap.put("spendingInfo", spendingMap);
		
		String classValue = (String) spendingMap.get("class");
		
		// 서점정보
		List<Map<String, Object>> storeList = (List<Map<String, Object>>) commonDao.selectList(NAME_SPACE.concat("storeInfo"), params);
		for(Map<String, Object> store : storeList) {
			String storeId = (String) store.get("storeId");
			params.put("storeId", storeId);
			
			List<Map<String, Object>> spendingList = (List<Map<String, Object>>) commonDao.selectList(NAME_SPACE.concat("getStoreSpending"), params);
				
			int classNum = Integer.parseInt(classValue);
			String[] keyList = {"half", "q", "month"};
			if(classNum < 3) {
				for(int i = 0;i < spendingList.size();i++) {
					store.put(keyList[classNum] + (i + 1) + "Key", spendingList.get(i).get("recKey"));
					store.put(keyList[classNum] + (i + 1), spendingList.get(i).get("price"));
				}
			}
			// 사용 예산 조회
			store.put("spendingInfo", commonDao.selectOne(NAME_SPACE.concat("getUsedPrice"), params));
		}
		
		if(classValue.equals("3")) {
			Map<String, Object> reqMap = new HashMap<>();
			reqMap.put("parentKey", params.get("recKey"));
			// 기간별 조회
			retMap.put("spendingList", commonDao.selectList(NAME_SPACE.concat("getStoreSpending"), reqMap));
		}
		retMap.put("storeList", storeList);
		return retMap;
	}
	
	// 지출결의 저장
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = {Exception.class, Error.class})
	public Map<String, Object> savePrice(Map<String, Object> params) throws Exception {
		Map<String, Object> returnMap = null;
		
		try {
			// 필수 체크 
			Map<String, Object> retMap = ValidUtils.requiredMap(params, 
					new String[] {"recKey", "libManageCode", "class", "fiscalYear", "budget", "deadline"}, 
					new String[] {"지출결의 키", "관리구분", "구분", "회계연도", "금액", "회기말"});
			if (!retMap.isEmpty()) {
				return retMap;
			}
			// 세션 체크
			if(SessionUtils.getLibId() == null) {
				return ValidUtils.resultErrorMap("로그인 도서관 정보를 찾을 수 없습니다.");
			} else {
				params.put("libId", SessionUtils.getLibId());
			}
			
			// 회계연도로 중복등록된 지출결의 데이터가 존재하는지 체크
			Map<String, Object> duplicatedSpendingInfo = (Map<String, Object>) commonDao.selectOne("getDuplicatedSpendingCount", params);
			if(duplicatedSpendingInfo != null && duplicatedSpendingInfo.get("cnt") != null && Integer.parseInt((String) duplicatedSpendingInfo.get("cnt")) > 0) {
				return ValidUtils.resultErrorMap(params.get("fiscalYear") + "년도 지출결의는 이미 등록되어 있습니다.");
			}
			
			// 지출결의 업데이트
			commonDao.update(NAME_SPACE.concat("updateSpending"), params);
			
			String orgClass = params.get("orgClass") != null ? (String) params.get("orgClass") : "";
			String currClass = params.get("class") != null ? (String) params.get("class") : "";
			
			boolean isOtherClass = false;
			if(!StringUtils.isEmpty(orgClass) && !StringUtils.isEmpty(currClass)) {
				// 구분값이 변경된 경우 이전의 서점 지출결의 데이터들은 삭제
				if(!orgClass.equals(currClass)) {
					commonDao.delete(NAME_SPACE.concat("deleteSpendingInfo"), params);
					isOtherClass = true;
				}
			} else {
				return ValidUtils.resultErrorMap("구분값이 입력되지 않았습니다.");
			}
			
			String storeInfoStr = (String) params.get("storeInfo");
			if(!StringUtils.isEmpty(storeInfoStr)) {
				JSONArray jArray = new JSONArray(storeInfoStr);
				
				for(int i = 0;i < jArray.length();i++) {
					// json to Map
					Map<String, Object> reqMap = RestCall.jsonStringToMap(jArray.get(i).toString());
					// 도서관 사용자 ID 입력
					reqMap.put("libId", SessionUtils.getLibId());
					reqMap.put("parentKey", params.get("recKey"));
					
					if(isOtherClass) {
						// 구분값 변경된 경우 입력
						commonDao.insert(NAME_SPACE.concat("insertSpendingInfo"), reqMap);
					} else {
						// 기간설정 - 신규입력 항목(recKey 없음)은 입력
						if(currClass.equals("3") && StringUtils.isEmpty(reqMap.get("recKey"))) {
							commonDao.insert(NAME_SPACE.concat("insertSpendingInfo"), reqMap);
						} else {
							// 구분값이 동일한 경우 수정
							commonDao.update(NAME_SPACE.concat("updateSpendingInfo"), reqMap);
						}
					}
				}
			}
			
			// 기간설정 삭제 데이터
			String deleteInfoStr = (String) params.get("deleteInfo");
			if(currClass.equals("3")) {
				// 삭제키가 존재하는 행 삭제
				if(!StringUtils.isEmpty(deleteInfoStr)) {
					JSONArray deleteInfoArray = new JSONArray(deleteInfoStr);
					for(int j = 0;j < deleteInfoArray.length();j++) {
						Map<String, Object> reqMap = new HashMap<>();
						reqMap.put("recKey", deleteInfoArray.get(j).toString());
						commonDao.delete(NAME_SPACE.concat("deleteSpendingInfoByKey"), reqMap);
					}
				}
			}
			
			returnMap = ValidUtils.resultSuccessMap();
		} catch (Exception e) {
			// 트랜잭션에 의해 롤백될 수 있도록 예외발생시킴
			throw new Exception();
		}
		
		return returnMap;
	}
	
	// 지출결의 삭제
	@Transactional(rollbackFor = {Exception.class, Error.class})
	public Map<String, Object> deleteSpending(Map<String, Object> params) throws Exception {
		Map<String, Object> returnMap = null;
		
		try {
			// 필수 체크 
			Map<String, Object> retMap = ValidUtils.requiredMap(params, new String[] {"recKey"}, new String[] {"지출결의 키"});
			if (!retMap.isEmpty()) {
				return retMap;
			}
			
			// 세션 체크
			if(!SessionUtils.isLibSession()) {
				return ValidUtils.resultErrorMap("로그인 도서관 정보를 찾을 수 없습니다.");
			}
			
			// 지출결의 서점 데이터 삭제
			commonDao.delete(NAME_SPACE.concat("deleteSpendingInfo"), params);
			// 지출결의 데이터 삭제
			commonDao.delete(NAME_SPACE.concat("deleteSpending"), params);
			
			returnMap = ValidUtils.resultSuccessMap();
		} catch (Exception e) {
			// 트랜잭션에 의해 롤백될 수 있도록 예외발생시킴
			throw new Exception();
		}
		
		return returnMap;
	}
}