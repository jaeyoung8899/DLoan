package dloan.library.envset;

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

@Service
public class LibraryManageService {
	
	private static final String NAME_SPACE = "library.manage.";
	private static final String LOGIN_NAME_SPACE = "library.login.";

	@Autowired
	protected CommonDao commonDao;
	
	/**
	 * 도서관목록
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectLibrary() {
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		// 개인정보 접속기록 보관
		params.put("recIp", SessionHandler.getClientIpAddr());
		params.put("libId", SessionUtils.getLibId());
		commonDao.insert(NAME_SPACE.concat("selectLibraryConnectionHistoryLog"), params);
		
		return (List<Map<String, String>>) this.commonDao.selectList(NAME_SPACE.concat("selectLibrary"));
	}

	/**
	 * 비밀번호 오류횟수 초기화
	 * @param params
	 * @return
	 */
	public Map<String, Object> pwFailCountReset(Map<String, String> params) {
		Map<String, Object> retMap = ValidUtils.requiredMap(params, 
				new String[] {"libId"}, 
				new String[] {"아이디"});
		
		if (!retMap.isEmpty()) {
			return retMap;
		}
		
		params.put("failCount", "0");
		commonDao.update(LOGIN_NAME_SPACE.concat("updatePasswordFaliCount"), params);

		retMap.putAll(ValidUtils.resultSuccessMap());
		
		return retMap;
	}
	
	
	/**
	 * 비밀번호 변경
	 * @param params
	 * @return
	 */
	public Map<String, Object> passwordChange(Map<String, String> params) {

		Map<String, Object> retMap = ValidUtils.requiredMap(params, 
				new String[] {"libId",  "changePw",      "confirmPw"}, 
				new String[] {"아이디", "변경 비밀번호", "변경 비밀번호"});

		if (!retMap.isEmpty()) {
			return retMap;
		}

		if (params.get("changePw").equals(params.get("confirmPw"))) {

			if (ValidUtils.isValidPassword(params.get("changePw"), true, true, true, 8, 20)) {

				params.put("password", CipherUtil.sha256Encode(params.get("changePw")));
				// 3. 비밀번호 변경
				commonDao.update(LOGIN_NAME_SPACE.concat("updatePasswordChange"), params);
				commonDao.insert(LOGIN_NAME_SPACE.concat("updatePasswordChangeAccessRightsLog"), params);
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
	 * 도서관 수정
	 * @param params
	 * @return
	 */
	public Map<String, Object> updateLibrary(
			List<String> ltLibId,
			List<String> ltLibPhone,
			List<String> ltHandphone,
			List<String> ltName,
			List<String> ltLimitPrice) {
		
		Map<String, String> params = null;
		for (int i = 0; i < ltLibId.size(); i++) {
			params = new HashMap<String, String>();
			params.put("libId",     ltLibId.get(i));
			params.put("libPhone",  ltLibPhone.get(i));
			params.put("handphone", ltHandphone.get(i));
			params.put("name",      ltName.get(i));
			params.put("limitPrice",ltLimitPrice.get(i));
			
			params.put("userId", SessionUtils.getLibId());
			params.put("recIp", SessionHandler.getClientIpAddr());
			
			this.commonDao.update(NAME_SPACE.concat("updateLibrary"), params);
			this.commonDao.insert(NAME_SPACE.concat("updateLibraryAccessRightsLog"), params);
			this.commonDao.insert(NAME_SPACE.concat("updateLibraryConnectionHistoryLog"), params);
		}
		return ValidUtils.resultSuccessMap();
	}
	
	/**
	 * 도서관IP 조회
	 * 
	 * @param libId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> selectLibAllowIP(String libId) {
		return (List<String>) this.commonDao.selectList(NAME_SPACE.concat("selectLibAllowIP"), libId);
	}
	
	/**
	 * 도서관IP 변경
	 * 
	 * @param libId
	 * @param ltAllowIp
	 * @return
	 */
	public Map<String, Object> updateLibAllowIP(String libId, List<String> ltAllowIp) {
		
		// IP 삭제
		this.commonDao.delete(NAME_SPACE.concat("deleteLibAllowIP"), libId);
		this.commonDao.insert(NAME_SPACE.concat("deleteLibAllowIPAccessRightsLog"), libId);
		// IP 입력
		Map<String, String> params = null;
		for (int i = 0; i < ltAllowIp.size(); i++) {
			params = new HashMap<String, String>();
			params.put("libId"  , libId);
			params.put("allowIp", ltAllowIp.get(i));
			this.commonDao.insert(NAME_SPACE.concat("insertLibAllowIP"), params);
			this.commonDao.insert(NAME_SPACE.concat("insertLibAllowIPAccessRightsLog"), params);
		}
		
		return ValidUtils.resultSuccessMap();
	}
}
