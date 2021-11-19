package dloan.common.util;

import java.util.Date;
import java.util.Map;

import dloan.common.handler.SessionHandler;

public class SessionUtils {
	
	// 사용자
	public static final String USER_NO     = "USER_NO";
	public static final String USER_ID     = "USER_ID";
	public static final String USER_NM     = "USER_NM";
	public static final String HANDPHONE   = "HANDPHONE";

	// 서점
	public static final String STORE_ID    = "STORE_ID";
	public static final String STORE_NM    = "STORE_NM";
	public static final String STORE_TEL   = "STORE_TEL";
	
	// 도서관
	public static final String LIB_ID      = "LIB_ID";
	public static final String LIB_NM      = "LIB_NM";
	public static final String LIB_MNG_CD  = "LIB_MNG_CD";
	public static final String LIB_TEL     = "LIB_TEL";
	public static final String LIB_EXF_TIME= "LIB_EXF_TIME";
	
	//
	public static String getUserNo() {
		return (String) SessionHandler.getAttribute(USER_NO);
	}
	
	public static void setUserNo(String userNo) {
		SessionHandler.setAttribute(USER_NO, userNo);
	}
	
	public static String getUserId() {
		return (String) SessionHandler.getAttribute(USER_ID);
	}

	public static void setUserId(String userId) {
		SessionHandler.setAttribute(USER_ID, userId);
	}
	
	public static String getUserNm() {
		return (String) SessionHandler.getAttribute(USER_NM);
	}
	
	public static void setUserNm(String userNm) {
		SessionHandler.setAttribute(USER_NM, userNm);
	}
	
	public static String getPhone() {
		return (String) SessionHandler.getAttribute(HANDPHONE);
	}
	
	public static void setPhone(String phone) {
		SessionHandler.setAttribute(HANDPHONE, phone);
	}
	
	/**
	 * 사용자 세션
	 * @param userInfo
	 */
	public static void setUserInfo(Map<String, ?> userInfo) {
		SessionUtils.setUserNo ((String)userInfo.get("userNo"));
		/*20210728 제주 한라도서관 홈페이지 업체가 ID를 따로 관리합니다.*/
		SessionUtils.setUserId ((String)userInfo.get("userNo"));
		SessionUtils.setUserNm ((String)userInfo.get("name"));
		SessionUtils.setPhone  ((String)userInfo.get("handphone"));
	}
	/**
	 * 사용자 세션 삭제
	 */
	public static void sessionUserRemove() {
		//
		SessionHandler.removeAttribute(USER_NO);
		SessionHandler.removeAttribute(USER_ID);
		SessionHandler.removeAttribute(USER_NM);
		SessionHandler.removeAttribute(HANDPHONE);
	}
	
	public static String getStoreId() {
		return (String) SessionHandler.getAttribute(STORE_ID);
	}

	public static void setStoreId(String storeId) {
		SessionHandler.setAttribute(STORE_ID, storeId);
	}
	
	public static String getStoreNm() {
		return (String) SessionHandler.getAttribute(STORE_NM);
	}
	
	public static void setStoreNm(String storeNm) {
		SessionHandler.setAttribute(STORE_NM, storeNm);
	}
	
	public static String getStoreTel() {
		return (String) SessionHandler.getAttribute(STORE_TEL);
	}
	
	public static void setStoreTel(String storeTel) {
		SessionHandler.setAttribute(STORE_TEL, storeTel);
	}

	/**
	 * 서점 세션
	 * @param storeInfo
	 */
	public static void setStoreInfo(Map<String, Object> storeInfo) {
		SessionUtils.setStoreId ((String)storeInfo.get("storeId"));
		SessionUtils.setStoreNm ((String)storeInfo.get("storeName"));
		SessionUtils.setStoreTel((String)storeInfo.get("storePhone"));
	}
	
	/**
	 * 서점 세션 삭제
	 */
	public static void sessionStoreRemove() {
		SessionHandler.removeAttribute(STORE_ID);
		SessionHandler.removeAttribute(STORE_NM);
		SessionHandler.removeAttribute(STORE_TEL);
	}
	
	public static String getLibMngCd() {
		return (String) SessionHandler.getAttribute(LIB_MNG_CD);
	}

	public static void setLibLibMngCd(String libMngCd) {
		SessionHandler.setAttribute(LIB_MNG_CD, libMngCd);
	}
	public static String getLibId() {
		return (String) SessionHandler.getAttribute(LIB_ID);
	}
	
	public static void setLibId(String libId) {
		SessionHandler.setAttribute(LIB_ID, libId);
	}
	
	public static String getLibNm() {
		return (String) SessionHandler.getAttribute(LIB_NM);
	}
	
	public static void setLibNm(String libNm) {
		SessionHandler.setAttribute(LIB_NM, libNm);
	}
	
	public static String getLibTel() {
		return (String) SessionHandler.getAttribute(LIB_TEL);
	}
	
	public static void setLibTel(String libTel) {
		SessionHandler.setAttribute(LIB_TEL, libTel);
	}
	
	public static Date getLibExfTime() {
		return (Date) SessionHandler.getAttribute(LIB_EXF_TIME);
	}
	
	public static void setLibExfTime(Date libExfTime) {
		SessionHandler.setAttribute(LIB_EXF_TIME, libExfTime);
	}
	
	public static boolean isLibSession() {
		Date libExfTime = SessionUtils.getLibExfTime();
		if (libExfTime == null || libExfTime.getTime() < new Date().getTime()) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 도서관 세션
	 * @param libInfo
	 */
	public static void setLibInfo(Map<String, Object> libInfo) {
		SessionUtils.setLibLibMngCd((String)libInfo.get("libManageCode"));
		SessionUtils.setLibId ((String)libInfo.get("libId"));
		SessionUtils.setLibNm ((String)libInfo.get("libName"));
		SessionUtils.setPhone ((String)libInfo.get("handphone"));
		SessionUtils.setLibTel((String)libInfo.get("libPhone"));
	}
	
	/**
	 * 도서관 세션 삭제
	 */
	public static void sessionLibRemove() {
		SessionHandler.removeAttribute(LIB_MNG_CD);
		SessionHandler.removeAttribute(LIB_ID);
		SessionHandler.removeAttribute(LIB_NM);
		SessionHandler.removeAttribute(HANDPHONE);
		SessionHandler.removeAttribute(LIB_EXF_TIME);
	}
}
