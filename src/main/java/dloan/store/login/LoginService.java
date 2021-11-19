package dloan.store.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dloan.common.dao.CommonDao;
import dloan.common.handler.SessionHandler;
import dloan.common.util.CipherUtil;
import dloan.common.util.SessionUtils;
import dloan.common.util.ValidUtils;

@Service("dloan.store.login.LoginService")
public class LoginService {
	
	private static final String NAME_SPACE = "store.login.";

	@Autowired
	protected CommonDao commonDao;
	
	/**
	 * 회원정보 조회
	 * 
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getStoreInfo(Map<String, Object> params) {
		return (Map<String, Object>) commonDao.selectOne(NAME_SPACE.concat("getStoreInfo"), params);
	}

	/**
	 * 로그인
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> login(Map<String, Object> params) {
		
		Map<String, Object> retMap = ValidUtils.requiredMap(params, 
				new String[] {"storeId", "storePassword"},
				new String[] {"아이디", "비밀번호"});
		if (!retMap.isEmpty()) {
			return retMap;
		}
		
		// 서점정보 조회
		Map<String, Object> storeInfo = (Map<String, Object>) this.commonDao.selectOne(NAME_SPACE.concat("getStoreInfo"), params);
		if (storeInfo == null) {
			return ValidUtils.resultErrorMap("아이디와 비밀번호를 확인해주세요.");
		}
		
		// 비밀번호 실패 횟수 체크
		Integer failCount = Integer.valueOf((String)storeInfo.get("loginFailedCnt"));
		if (failCount >= 5) {
			return ValidUtils.resultErrorMap("비밀번호 오류횟수(5회)를 초과했습니다.\n  관리자에 문의하세요.");
		}
		
		// 비밀번호 실패 횟수 초기화
		params.put("failCount", 0);
		this.commonDao.update(NAME_SPACE.concat("updatePasswordFaliCount"), params);
		
		// 서점MAC정보 조회
		List<String> storeMacList = (List<String>) this.commonDao.selectList(NAME_SPACE.concat("selectStoreMacInfo"), params.get("storeId"));
		if (storeMacList.size() > 0) {
			
			if (StringUtils.isEmpty((String)params.get("storeMac"))) {
				return ValidUtils.resultErrorMap("등록되지 않은 PC입니다.");
			}
			
			// 동일한 MAC 찾기
			boolean isSearch = false;
			
			// 네트웍 MAC이 여러개인 PC가 존재해서 전부 받아서 확인함
			String[] paramStoreMacList = ((String)params.get("storeMac")).split(",");
			for (String paramStoreMac : paramStoreMacList) {
				
				for (String storeMac : storeMacList) {
					if (paramStoreMac.equals(CipherUtil.sha256Encode(storeMac))) {
						isSearch = true;
						break;
					}
				}
				
				if (isSearch) {
					break;
				}
			}
			
			if (!isSearch) {
				return ValidUtils.resultErrorMap("등록되지 않은 PC입니다.");
			}
		}
		
		// 비밀번호 확인
		String userPassword = CipherUtil.sha256Encode((String)params.get("storePassword"));
		if (!storeInfo.get("password").equals(userPassword)) {
			params.put("failCount", Integer.valueOf((String)storeInfo.get("loginFailedCnt")) +1);
			this.commonDao.update(NAME_SPACE.concat("updatePasswordFaliCount"), params);
			return ValidUtils.resultErrorMap("아이디와 비밀번호를 확인해주세요.");
		}
		
		// 개인정보 접속기록 보관
		params.put("recIp", SessionHandler.getClientIpAddr());
		this.commonDao.insert("getStoreInfoConnectionHistoryLog", params);
		
		// 세션 생성.
		SessionUtils.setStoreInfo(storeInfo);
		retMap.putAll(ValidUtils.resultSuccessMap());
		// 비밀번호 변경일 체크 6개월(180일)
		if (Integer.valueOf((String)storeInfo.get("changePwDay")) > 180) {
			retMap.put("isChangePw", true);
		} else {
			retMap.put("isChangePw", false);
		}
		
		return retMap;
		
	}
	

	/**
	 * 비밀번호 변경
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> passwordChange(Map<String, String> params) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		params.put("storeId", SessionUtils.getStoreId());

		// 다음에 변경
		if ("next".equals(params.get("flag"))) {
			commonDao.update(NAME_SPACE.concat("updatePasswordChange"), params);
			retMap.putAll(  ValidUtils.resultSuccessMap());
		} else {
			retMap = ValidUtils.requiredMap(params, 
					new String[] {"curPw", "changePw", "confirmPw"}, 
					new String[] {"현재 비밀번호", "변경 비밀번호", "변경 비밀번호"});
			
			if (!retMap.isEmpty()) {
				return retMap;
			}
	
			Map<String, Object> libInfo = (Map<String, Object>) commonDao.selectOne(NAME_SPACE.concat("getStoreInfo"), params);
			
			if (libInfo != null) {
				
				String userPassword = CipherUtil.sha256Encode(params.get("curPw"));
				
				if (params.get("curPw").equals(params.get("changePw"))) {
					retMap.putAll(ValidUtils.resultErrorMap("현재 비밀번호와 변경된 비밀번호가 동일합니다."));
				} else {
					
					// 2. 비밀번호 확인
					if (!libInfo.get("password").equals(userPassword)) {
						retMap.putAll(ValidUtils.resultErrorMap("현재 비밀번호를 확인해주세요."));
					} else {
						
						if (params.get("changePw").equals(params.get("confirmPw"))) {
							
							if (ValidUtils.isValidPassword(params.get("changePw"), true, true, true, 8, 20)) {
								
								params.put("password", CipherUtil.sha256Encode(params.get("changePw")));
								// 3. 비밀번호 변경
								commonDao.update(NAME_SPACE.concat("updatePasswordChange"), params);
								commonDao.insert(NAME_SPACE.concat("updatePasswordChangeAccessRightsLog"), params);
								
								retMap.putAll(ValidUtils.resultSuccessMap());
							} else {
								retMap.putAll(ValidUtils.resultErrorMap("비밀번호를 영어 소문자, 특수문자, 숫자를 조합하여 8~20자 이내로 입력하세요."));
							}
							
						} else {
							retMap.putAll(ValidUtils.resultErrorMap("비밀번호와 비밀번호 확인이 일치하지 않습니다"));
						}
					}
				}
			}
		}
	
		return retMap;
	}
}
