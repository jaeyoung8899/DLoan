package dloan.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class ValidUtils {

	// email 체크
	public static boolean isValidEmail(String email) {
		
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
	}
	
	/**
	 * 비밀번호 유효성 검사
	 * 
	 * @param passwd		비밀번호
	 * @param isSpecial		특수문자 존재확인
	 * @param isEnglish		영어 존재확인
	 * @param isNumber		숫자 존재확인
	 * @param minLength		최소 자릿수
	 * @param maxLength		최대 자릿수
	 * @return
	 */
	public static boolean isValidPassword(final String passwd, boolean isSpecial, boolean isEnglish, boolean isNumber, int minLength, int maxLength) {         
		
		StringBuffer buff = new StringBuffer("(");
		
		// 영어존재
		// 2018-01-05, 영어는 소문자만
		if (isEnglish) {
			buff.append("(?=.*[a-zA-Z])");
			//buff.append("(?=.*[a-z])");
		}

		// 특수문자존재
		if (isSpecial) {
			buff.append("(?=.*[~!@#$%^*+=-])");
		}
		
		// 숫자존재
		if (isNumber) {
			buff.append("(?=.*[0-9])");
		}
		
		// 길이
		buff.append(".{" + minLength + "," + maxLength + "})");
		// 확인
		return Pattern.compile(buff.toString()).matcher(passwd).matches();	
	}
	
	public static boolean isDate (String date) {
		return ValidUtils.isDate (date, null);
	}
	
	public static boolean isDate (String date, String format) {
		
		try {
			
			if (StringUtils.isEmpty(format)) {
				format = "yyyyMMdd";
			}
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setLenient(false);
			sdf.parse(date);
			return true;
		} catch (ParseException pe) {
			return false;
		}
	}
	
	public static Map<String, Object> requiredMap (Map<String, ?> params, String[] keys, String[] msg) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		if (params == null) {
			if (keys.length > 0) {
				retMap.put("resultMessage", msg[0] + "을(를) 입력하세요.");
			} else {
				retMap.put("resultMessage", "입력 된 값이 없습니다.");
			}
			retMap.put("resultCode",    "N");
			return retMap;
		}
		
		if (keys.length == msg.length) {
			for (int i = 0; i < keys.length; i++) {
				
				Object value = params.get(keys[i]);
				
				if (value == null) {
					retMap.put("resultMessage", msg[i] + "을(를) 입력하세요.");
					retMap.put("resultCode",    "N");
				} else {
					
					if (value instanceof String) {
						if (StringUtils.isEmpty((String)value)) {
							retMap.put("resultMessage", msg[i] + "을(를) 입력하세요.");
							retMap.put("resultCode",    "N");
						}
					}
				}
			}
		}
		
		return retMap;
	}
	
	public static void requiredMapExc (Map<String, String> params, String[] keys, String[] msg) throws Exception {
		
		String message = null;
		
		if (params == null) {
			if (keys.length > 0) {
				message = msg[0] + "을(를) 입력하세요.";
			} else {
				message = "입력 된 값이 없습니다.";
			}
			throw new Exception(message);
		}
		
		if (keys.length == msg.length) {
			for (int i = 0; i < keys.length; i++) {
				
				Object value = params.get(keys[i]);
				
				if (value == null) {
					message = msg[i] + "을(를) 입력하세요.";
					throw new Exception(message);
				} else {
					
					if (value instanceof String) {
						if (StringUtils.isEmpty((String)value)) {
							message = msg[i] + "을(를) 입력하세요.";
							throw new Exception(message);
						}
					}
				}
			}
		}
	}
	
	public static Map<String, Object> resultErrorMap(String resultMessage) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultMessage", resultMessage);
		resultMap.put("resultCode",    "N");
		return resultMap;
	}
	
	public static Map<String, Object> resultSuccessMap() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultMessage", "success");
		resultMap.put("resultCode",    "Y");
		return resultMap;
	}
}
