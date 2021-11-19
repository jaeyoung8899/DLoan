package dloan.library.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dloan.common.dao.CommonDao;
import dloan.common.handler.SessionHandler;
import dloan.common.util.CipherUtil;
import dloan.common.util.SessionUtils;
import dloan.common.util.ValidUtils;

@Service("dloan.library.login.LoginService")
public class LoginService {
	
	private static final String NAME_SPACE = "library.login.";
	
	@Autowired
	protected CommonDao commonDao;
	
	/**
	 * 도서관 정보 조회
	 * 
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getLibraryInfo(Map<String, String> params) {
		return (Map<String, String>) commonDao.selectOne(NAME_SPACE.concat("getLibraryInfo"), params);
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> login(Map<String, Object> params) {
		
		Map<String, Object> retMap = ValidUtils.requiredMap(params, 
				new String[] {"libId", "libPassword"}, 
				new String[] {"아이디", "비밀번호"});
		if (!retMap.isEmpty()) {
			return retMap;
		}
		// 도서관정보 조회
		Map<String, Object> libInfo = (Map<String, Object>) this.commonDao.selectOne(NAME_SPACE.concat("getLibraryInfo"), params);
		if (libInfo == null) {
			return ValidUtils.resultErrorMap("아이디와 비밀번호를 확인해주세요.");
		}
		
		// 로그인 허용 IP 확인
		List<String> libAllowIP = (List<String>) this.commonDao.selectList(NAME_SPACE.concat("selectLibAllowIP"), params.get("libId"));
		if (libAllowIP.size() > 0) {
			boolean isSearch = false;
			for (String loginIp : libAllowIP) {
				if (loginIp.equals(SessionHandler.getClientIpAddr())) {
					isSearch = true;
					break;
				}
			}
			if (!isSearch) {
				return ValidUtils.resultErrorMap("등록되지 않은 PC입니다.");
			}
		}
		
		// 비밀번호 확인
		String userPassword = CipherUtil.sha256Encode((String)params.get("libPassword"));
		if (!libInfo.get("password").equals(userPassword)) {
			// 비밀번호 틀린 경우, 비밀번호 오류횟수 +1 추가
			params.put("failCount", Integer.valueOf((String)libInfo.get("loginFailedCnt")) +1);
			this.commonDao.update(NAME_SPACE.concat("updatePasswordFaliCount"), params);
			return ValidUtils.resultErrorMap("아이디와 비밀번호를 확인해주세요.");
		}
		
		// 비밀번호 실패 횟수 체크
		Integer failCount = Integer.valueOf((String)libInfo.get("loginFailedCnt"));
		if (failCount >= 5) {
			return ValidUtils.resultErrorMap("비밀번호 오류횟수(5회)를 초과했습니다.\n  관리자에 문의하세요.");
		}
		
		// 개인정보 접속기록 보관
		params.put("recIp", SessionHandler.getClientIpAddr());
		this.commonDao.insert("getLibraryInfoConnectionHistoryLog", params);
		
		// 비밀번호 실패 횟수 초기화
		params.put("failCount", 0);
		this.commonDao.update(NAME_SPACE.concat("updatePasswordFaliCount"), params);
		
		// 세션 생성.
		SessionUtils.setLibInfo(libInfo);
		retMap.putAll(ValidUtils.resultSuccessMap());
		
		// 비밀번호 변경일 체크 6개월(180일)
		if (Integer.valueOf((String)libInfo.get("changePwDay")) > 180) {
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
		
		params.put("libId", SessionUtils.getLibId());
		
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

			Map<String, Object> libInfo = (Map<String, Object>) commonDao.selectOne(NAME_SPACE.concat("getLibraryInfo"), params);
			
			if (libInfo != null) {
				if (params.get("curPw").equals(params.get("changePw"))) {
					retMap.putAll(ValidUtils.resultErrorMap("현재 비밀번호와 변경된 비밀번호가 동일합니다."));
				} else {
					String userPassword = CipherUtil.sha256Encode(params.get("curPw"));

					// 2. 비밀번호 확인
					if (!libInfo.get("password").equals(userPassword)) {
						retMap.putAll(ValidUtils.resultErrorMap("현재 비밀번호를 확인해주세요."));
					} else {

						if (params.get("changePw").equals(params.get("confirmPw"))) {

							if (ValidUtils.isValidPassword(params.get("changePw"), true, true, true, 8, 20)) {

								params.put("password", CipherUtil.sha256Encode(params.get("changePw")));
								//params.put("password", params.get("changePw"));
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
