package dloan.library.envset;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dloan.common.dao.CommonDao;
import dloan.common.handler.SessionHandler;
import dloan.common.util.CipherUtil;
import dloan.common.util.SessionUtils;
import dloan.common.util.ValidUtils;

@Service
public class StoreManageService {
	
	private static final String NAME_SPACE = "library.storemng.";

	@Autowired
	protected CommonDao commonDao;
	
	/**
	 * 서점목록
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectStoreMng() {
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		// 개인정보 접속기록 보관
		params.put("recIp", SessionHandler.getClientIpAddr());
		params.put("libId", SessionUtils.getLibId());
		commonDao.insert(NAME_SPACE.concat("selectStoreMngConnectionHistoryLog"), params);
		
		return (List<Map<String, String>>) this.commonDao.selectList(NAME_SPACE.concat("selectStoreMng"));
	}

	
	/**
	 * 비밀번호 오류횟수 초기화
	 * @param params
	 * @return
	 */
	public Map<String, Object> pwFailCountResetStore(Map<String, String> params) {
		Map<String, Object> retMap = ValidUtils.requiredMap(params, 
				new String[] {"storeId"}, 
				new String[] {"아이디"});
		
		if (!retMap.isEmpty()) {
			return retMap;
		}
		
		params.put("failCount", "0");
		commonDao.update(NAME_SPACE.concat("updatePasswordFaliCount"), params);

		retMap.putAll(ValidUtils.resultSuccessMap());
		
		return retMap;
	}
	
	/**
	 * 비밀번호 변경
	 * @param params
	 * @return
	 */
	public Map<String, Object> updateStorePassword(Map<String, String> params) {

		Map<String, Object> retMap = ValidUtils.requiredMap(params, 
				new String[] {"storeId",  "changePw",      "confirmPw"}, 
				new String[] {"아이디", "변경 비밀번호", "변경 비밀번호"});

		if (!retMap.isEmpty()) {
			return retMap;
		}

		if (params.get("changePw").equals(params.get("confirmPw"))) {

			if (ValidUtils.isValidPassword(params.get("changePw"), true, true, true, 8, 20)) {

				params.put("password", CipherUtil.sha256Encode(params.get("changePw")));
				// 3. 비밀번호 변경
				commonDao.update(NAME_SPACE.concat("updateStorePassword"), params);

				retMap.putAll(ValidUtils.resultSuccessMap());
			} else {
				retMap.putAll(ValidUtils.resultErrorMap("비밀번호를 영어 대문자, 소문자, 특수문자, 숫자를 조합하여 8~20자 이내로 입력하세요."));
			}

		} else {
			retMap.putAll(ValidUtils.resultErrorMap("비밀번호와 비밀번호확인이 일치하지 않습니다"));
		}

		return retMap;
	}
	
	/**
	 * 서점 수정
	 * @param params
	 * @return
	 */
	public Map<String, Object> updateStoreMng(
			List<String> ltStoreId,
			List<String> ltStorePhone,
			List<String> ltHandphone,
			List<String> ltLimitPrice) {
		
		Map<String, String> params = null;
		for (int i = 0; i < ltStoreId.size(); i++) {
			params = new HashMap<String, String>();
			params.put("storeId",     ltStoreId.get(i));
			params.put("storePhone",  ltStorePhone.get(i));
			params.put("handphone",   ltHandphone.get(i));
			params.put("limitPrice",  ltLimitPrice.get(i));
			
			params.put("userId", SessionUtils.getLibId());
			params.put("recIp", SessionHandler.getClientIpAddr());
			
			this.commonDao.update(NAME_SPACE.concat("updateStoreMng"), params);
			this.commonDao.insert(NAME_SPACE.concat("updateStoreMngAccessRightsLog"), params);
			this.commonDao.insert(NAME_SPACE.concat("updateStoreMngConnectionHistoryLog"), params);
		}
		return ValidUtils.resultSuccessMap();
	}
	
	/**
	 * 서점MAC 조회
	 * 
	 * @param storeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> selectStoreMAC(String storeId) {
		return (List<String>) this.commonDao.selectList(NAME_SPACE.concat("selectStoreMAC"), storeId);
	}
	
	/**
	 * 서점MAC 변경
	 * 
	 * @param storeId
	 * @param ltStoreMac
	 * @return
	 */
	public Map<String, Object> updateStoreMAC(String storeId, List<String> ltStoreMac) {
		
		// MAC 삭제
		this.commonDao.delete(NAME_SPACE.concat("deleteStoreMAC"), storeId);
		this.commonDao.insert(NAME_SPACE.concat("deleteStoreMACAccessRightsLog"), storeId);
		// MAC 입력
		Map<String, String> params = null;
		for (int i = 0; i < ltStoreMac.size(); i++) {
			params = new HashMap<String, String>();
			params.put("storeId" , storeId);
			params.put("storeMac", ltStoreMac.get(i));
			this.commonDao.insert(NAME_SPACE.concat("insertStoreMAC"), params);
			this.commonDao.insert(NAME_SPACE.concat("insertStoreMACAccessRightsLog"), params);
		}
		
		return ValidUtils.resultSuccessMap();
	}


	/**
	 * 서점 전화번호 조회
	 */
	public List<String> selectStorePhone(String storeId) {
		String phone = (String) commonDao.selectOne(NAME_SPACE.concat("selectStorePhone"),storeId);
		String [] phoneArray =phone.split(",");
		List<String> phoneList = new ArrayList<>();

		if(phoneArray.length > 1){
			phoneList.addAll(Arrays.asList(phoneArray));
		}else{
			phoneList.add(phoneArray[0]);
		}

		return phoneList;
	}

	/**
	 * 서점전화번호 저장
	 */
	public Map<String,Object> updateStorePhone(String storeId, List<String> storePhoneList) {
		StringBuffer bfPhoneList = new StringBuffer();
		String phoneList = "";
		Map<String,Object> param = new HashMap<>();
		if(storePhoneList.size() < 2) {
			phoneList = storePhoneList.get(0);
		}else{
			for (String s : storePhoneList) {
				bfPhoneList.append(s).append(",");
			}
			phoneList = bfPhoneList.toString();
			phoneList = phoneList.substring(0,phoneList.length()-1);
		}
		param.put("storeId",storeId);
		param.put("phoneList",phoneList);

		commonDao.update(NAME_SPACE.concat("updateStorePhone"),param);

		return ValidUtils.resultSuccessMap();
	}

}
