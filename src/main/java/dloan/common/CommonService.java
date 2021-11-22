package dloan.common;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dloan.common.dao.CommonDao;

@Service("commonService")
public class CommonService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonService.class);
	
	private static final String NAME_SPACE = "common.";

	@Autowired
	private CommonDao commonDao;
	
	@Value("#{conf['sms_title_byte']}")
	private int smsTitleByte;
	
	//@Transactional(isolation=Isolation.DEFAULT, propagation=Propagation.REQUIRES_NEW, rollbackFor={Exception.class, SQLException.class})
	public void smsSend(Map<String, Object> params) {
		String sender = (String) params.get("sender");
		if (!StringUtils.isEmpty(sender)) {
			params.put("sender", sender.replaceAll("-", ""));
		}
		
		String recever = (String) params.get("recever");
		if (!StringUtils.isEmpty(recever)) {
			params.put("recever", recever.replaceAll("-", ""));
		}
//		 if(params.containsKey("reqStatus")) { 
//			this.commonDao.update(NAME_SPACE.concat("smsSend2"), params); //잘못된 번호 발송으로 인해 수정해줌.
//		}else {
			this.commonDao.update(NAME_SPACE.concat("smsSend"), params);
//		}
	}
	
	//@Transactional(isolation=Isolation.DEFAULT, propagation=Propagation.REQUIRES_NEW, rollbackFor={Exception.class, SQLException.class})
		public void alimTalkSend(Map<String, Object> params) {
			String sender = (String) params.get("sender");
			if (!StringUtils.isEmpty(sender)) {
				params.put("sender", sender.replaceAll("-", ""));
			}
			
			String recever = (String) params.get("recever");
			if (!StringUtils.isEmpty(recever)) {
				params.put("recever", recever.replaceAll("-", ""));
			}
//			 if(params.containsKey("reqStatus")) { 
//				this.commonDao.update(NAME_SPACE.concat("smsSend2"), params); //잘못된 번호 발송으로 인해 수정해줌.
//			}else {
				this.commonDao.update(NAME_SPACE.concat("alimTalkSend"), params);
//			}
		}
	
	//@Transactional(isolation=Isolation.DEFAULT, propagation=Propagation.REQUIRES_NEW, rollbackFor={Exception.class, SQLException.class})
	public void smsSendRequest(Map<String, Object> params) {
		String sender = (String) params.get("sender");
		if (!StringUtils.isEmpty(sender)) {
			params.put("sender", sender.replaceAll("-", ""));
		}
		
		String recever = (String) params.get("recever");
		if (!StringUtils.isEmpty(recever)) {
			params.put("recever", recever.replaceAll("-", ""));
		}
		
		//this.commonDao.update(NAME_SPACE.concat("smsSendDloan"), params);
		this.commonDao.update(NAME_SPACE.concat("smsSend"), params);
	}
	/**
	 * SMS 메시지 변환
	 * <pre>
	 * <b>[매핑정보]</b>
	 * $신청자$	: userName
	 * $서점$		: storeName
	 * $서명$		: title
	 * $대출만료일$	: loanWaitDate
	 * $반납예정일$	: returnPlanDate
	 * $입고예정일$	: enterPlanDate
	 * </pre>
	 * @param msg
	 * @param convData
	 * @return
	 */
	public String convSmsMsg(String msg, Map<String, String> convData) {
		
		if (StringUtils.isEmpty(msg) || convData == null || convData.isEmpty()) {
			return msg;
		}
		
		// 서명 바이트로 자르기
		convData.put("title", this.convTitle(convData.get("title")));

		// 입고예정일 변환 (X일내로)
		String enterPlanDate = convData.get("enterPlanDate");
		if (!StringUtils.isEmpty(enterPlanDate)) {
			
			try {
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				long ldiff = TimeUnit.MILLISECONDS.toDays(
					sdf.parse(enterPlanDate).getTime() - sdf.parse(sdf.format(new Date())).getTime()
				);
				
				enterPlanDate = String.valueOf(ldiff < 0 ? 0 : ldiff);
				
			} catch (ParseException e) {
				// 2019.05.07 소스코드 보안취약점 조치
				LOGGER.error("SMS 메세지 변환 오류");
			}
		}
		convData.put("enterPlanDate", enterPlanDate);
		
		//
		msg = msg.replace("$신청자$"		, StringUtils.defaultString(convData.get("userName")));
		msg = msg.replace("$서점$"		, StringUtils.defaultString(convData.get("storeName")));
		msg = msg.replace("$서명$"		, StringUtils.defaultString(convData.get("title")));
		msg = msg.replace("$대출만료일$"	, StringUtils.defaultString(convData.get("loanWaitDate")));
		msg = msg.replace("$반납예정일$"	, StringUtils.defaultString(convData.get("returnPlanDate")));
		msg = msg.replace("$입고예정일$"	, StringUtils.defaultString(convData.get("enterPlanDate")));
		msg = msg.replace("$도서관$"		, StringUtils.defaultString(convData.get("libName")));
		return msg;
	}

	/**
	 * 서점 조회 combo
	 * @param storeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectStore(String storeId) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(storeId)) {
			params.put("storeId", storeId);
		}
		return (List<Map<String, Object>>) commonDao.selectList(NAME_SPACE.concat("selectStore"), params);
	}
	
	/**
	 * 도서관 조회 combo
	 * @param libManageCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectLibrary(Map<String, String> params) {
		return (List<Map<String, Object>>) commonDao.selectList(NAME_SPACE.concat("selectLibrary"), params);
	}
	
	/**
	 * 서명 바이트로 자르기
	 *  - 0보다 클때만 적용
	 *  - 2바이트는 '..'을 기록하는데 사용
	 * 
	 * @param title
	 * @return
	 */
	private String convTitle(String title) {
		try {
			if (!StringUtils.isEmpty(title) && title.getBytes("EUC-KR").length > this.smsTitleByte) {
				int    offset = 0;
				int    maxlen = this.smsTitleByte-2;
				byte[] buffer = new byte[this.smsTitleByte];
					
				for (int i = 0; i < title.length(); i++) {
					byte[] temp = title.substring(i, i+1).getBytes("EUC-KR");
					if (offset+temp.length <= maxlen) {
						for (int j = 0; j < temp.length; j++) {
							buffer[offset++] = temp[j];
						}
					} else {
						buffer[offset++] = (byte)'.';
						buffer[offset++] = (byte)'.';
						return new String(buffer, 0, offset, "EUC-KR");
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			// 2019.05.07 소스코드 보안취약점 조치
			LOGGER.error("SMS 서명 바이트 변환 오류");
		}
		
		return title;
	}
	
	
	/**
	 * 알림톡 메시지 변환
	 * <pre>
	 * <b>[매핑정보]</b>
	 * #{이용자명}	: userName
	 * #{서점명}	: storeName
	 * #{서명}		: title
	 * #{대출만료일}	: loanWaitDate
	 * </pre>
	 * @param msg
	 * @param convData
	 * @return
	 */
	public String convAlimMsg(String msg, Map<String, String> convData) {
		
		if (StringUtils.isEmpty(msg) || convData == null || convData.isEmpty()) {
			return msg;
		}
		
		// 서명 바이트로 자르기
//		convData.put("title", this.convTitle(convData.get("title")));

		// 입고예정일 변환 (X일내로)
		String enterPlanDate = convData.get("enterPlanDate");
		if (!StringUtils.isEmpty(enterPlanDate)) {
			
			try {
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				long ldiff = TimeUnit.MILLISECONDS.toDays(
					sdf.parse(enterPlanDate).getTime() - sdf.parse(sdf.format(new Date())).getTime()
				);
				
				enterPlanDate = String.valueOf(ldiff < 0 ? 0 : ldiff);
				
			} catch (ParseException e) {
				// 2019.05.07 소스코드 보안취약점 조치
				LOGGER.error("SMS 메세지 변환 오류");
			}
		}
		convData.put("enterPlanDate", enterPlanDate);
		
		//
		msg = msg.replace("#{이용자명}"		, StringUtils.defaultString(convData.get("userName")));
		msg = msg.replace("#{서점명}"		, StringUtils.defaultString(convData.get("storeName")));
		msg = msg.replace("#{서명}"		, StringUtils.defaultString(convData.get("title")));
		msg = msg.replace("#{대출만료일}"	, StringUtils.defaultString(convData.get("loanWaitDate")));
		msg = msg.replace("#{업무일자}"	, StringUtils.defaultString(convData.get("loanWaitDate")));
		msg = msg.replace("#{예약만기일}"	, StringUtils.defaultString(convData.get("loanWaitDate")));
		msg = msg.replace("#{반납예정일}"	, StringUtils.defaultString(convData.get("returnPlanDate")));
		msg = msg.replace("#{입고예정일}"	, StringUtils.defaultString(convData.get("enterPlanDate")));
		msg = msg.replace("#{도서관명}"		, StringUtils.defaultString(convData.get("libManageName")));
		msg = msg.replace("#{발신번호}"		, StringUtils.defaultString(convData.get("storePhone")));
		return msg;
	}

	/**
	 * 설정조회
	 *
	 */
	public List<Map<String,Object>> getViewOptionList() {
		//화면제어
		List<Map<String,Object>> viewList = (List<Map<String, Object>>) commonDao.selectList("common.env.getViewOption");

		return viewList;
	}
}
