package dloan.user.login;

import java.util.List;
import java.util.Map;

import dloan.common.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dloan.common.dao.CommonDao;
import dloan.common.util.CipherUtil;
import dloan.common.util.SessionUtils;
import dloan.common.util.ValidUtils;

@Service("dloan.user.login.LoginService")
public class LoginService {
	
	private static final String NAME_SPACE = "user.login.";

	@Autowired
	protected CommonDao commonDao;

	@Autowired
	private CommonService commonService;

	
	/**
	 * 회원정보 조회
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserInfo(String userId) {
		//제주 아이디 사용안하여 대출자번호로 로그인
		return (Map<String, Object>) commonDao.selectOne(NAME_SPACE.concat("getUserInfoUserNo"), userId);
	}
	
	/**
	 *  사용자 login
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object>login(Map<String, String> params) {
		Map<String, Object> retMap = ValidUtils.requiredMap(params, 
				new String[] {"userId", "userPassword"}, 
				new String[] {"메일주소", "비밀번호"});
		
		if (!retMap.isEmpty()) {
			return retMap;
		}
		// 1.암호와 여부
		String encYn = (String) commonDao.selectOne(NAME_SPACE.concat("getEncryptYn"));
		
		// 2.사용자 정보 조회
		Map<String, Object> userInfo = (Map<String, Object>) commonDao.selectOne(NAME_SPACE.concat("getUserInfo"), params.get("userId"));
		if (userInfo != null) {
			
			// 3. 비밀번호 비교 
			String pass = null;
			if ("Y".equals(encYn)) {
				pass = CipherUtil.sha256Encode(params.get("userPassword"));
				if (!pass.equals((String)userInfo.get("password"))) {
					return ValidUtils.resultErrorMap("아이디와 비밀번호를 확인해주세요.");
				} else {
					// 세션 생성.
					SessionUtils.setUserInfo(userInfo);
				}
			} else {
				pass = (String)userInfo.get("password");
				if (!pass.equals(params.get("userPassword"))) {
					return ValidUtils.resultErrorMap("아이디와 비밀번호를 확인해주세요.");
				} else {
					// 세션 생성.
					SessionUtils.setUserInfo(userInfo);
				}
			}

		} else {
			return ValidUtils.resultErrorMap("아이디와 비밀번호를 확인해주세요.");
		}
		retMap.put("viewOptionInfo",commonService.getViewOptionList());
		retMap.putAll(ValidUtils.resultSuccessMap());

		return retMap;
	}
	
	/**
	 * 외부연동 로그인
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> loginExternal(Map<String, String> params) {
		
		// 1. 파라미터 확인
		Map<String, Object> retMap = ValidUtils.requiredMap(params,
				new String[] {"userId", "userPw"}, 
				new String[] {"아이디", "비밀번호"});
		if (!retMap.isEmpty()) {
			return retMap;
		}
		
		// 2. 사용자 정보 조회
		Map<String, Object> userInfo = (Map<String, Object>) this.commonDao.selectOne(NAME_SPACE.concat("getUserInfo"), params.get("userId"));
		if (userInfo == null) {
			return ValidUtils.resultErrorMap("아이디와 비밀번호를 확인해주세요.");
		}
		
		// 3. 암호와 여부
		String encYn = (String) this.commonDao.selectOne(NAME_SPACE.concat("getEncryptYn"));
		
		//홈페이지 업체에서는 암호화한 값으로 파라미터 password를 입력 하기에 비밀번호 암호화를 KOLASIII에서 사용할 경우 동일한 값을 비교하며, 암호화를 사용하지 않을 경우 KOLASIII의 비밀번호를 암호화 해준다.
		// 4. 비밀번호 확인
		if ("Y".equals(encYn)) {
			if (!params.get("userPw").equals((String) userInfo.get("password"))) {
				return ValidUtils.resultErrorMap("아이디와 비밀번호를 확인해주세요.");
			}
		} else {
			if (!params.get("userPw").equals(CipherUtil.sha256Encode((String) userInfo.get("password")))) {
				return ValidUtils.resultErrorMap("아이디와 비밀번호를 확인해주세요.");
			}
		}
		
		// 5. 세션 생성.
		SessionUtils.setUserInfo(userInfo);
		return ValidUtils.resultSuccessMap();
	}
	
	/**
	 * 외부연동 로그인
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> loginExternalUserNo(Map<String, String> params) {
		
		// 1. 파라미터 확인
		Map<String, Object> retMap = ValidUtils.requiredMap(params,
				new String[] {"userName", "userNo"}, 
				new String[] {"이름", "대출자번호"});
		if (!retMap.isEmpty()) {
			return retMap;
		}
		
		// 2. 사용자 정보 조회
		/*
		Map<String, Object> userInfo = (Map<String, Object>) this.commonDao.selectOne(NAME_SPACE.concat("getUserInfo"), params.get("userId"));
		if (userInfo == null) {
			return ValidUtils.resultErrorMap("아이디와 대출자번호를 확인해주세요.");
		}
		// 4. 대출자번호
		if (!params.get("userNo").equals((String) userInfo.get("userNo"))) {
			return ValidUtils.resultErrorMap("아이디와 대출자번호를 확인해주세요.");
		}
		*/
		// 2. 사용자 정보 조회 변경
		Map<String, Object> userInfo = (Map<String, Object>) this.commonDao.selectOne(NAME_SPACE.concat("getUserInfoUserNo"), params);
		if (userInfo == null) {
			return ValidUtils.resultErrorMap("이름과 대출자번호를 확인해주세요.");
		}
		// 4. 대출자번호
		if (!params.get("userNo").equals((String) userInfo.get("userNo"))) {
			return ValidUtils.resultErrorMap("이름과 대출자번호를 확인해주세요.");
		}
		
		// 5. 세션 생성.
		SessionUtils.setUserInfo(userInfo);
		return ValidUtils.resultSuccessMap();
	}
}
