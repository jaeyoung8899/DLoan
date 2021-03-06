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

		if(params.get("alimMsg") != null && !params.get("alimMsg").toString().equals("")){
			this.commonDao.update(NAME_SPACE.concat("alimTalkSend"),params);
		}else{
			this.commonDao.update(NAME_SPACE.concat("smsSend"), params);
		}
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
//				this.commonDao.update(NAME_SPACE.concat("smsSend2"), params); //????????? ?????? ???????????? ?????? ????????????.
//			}else {
			this.commonDao.update(NAME_SPACE.concat("alimTalkSend"), params);
//			}
	}

	/**
	 * SMS ????????? ??????
	 * <pre>
	 * <b>[????????????]</b>
	 * $?????????$	: userName
	 * $??????$		: storeName
	 * $??????$		: title
	 * $???????????????$	: loanWaitDate
	 * $???????????????$	: returnPlanDate
	 * $???????????????$	: enterPlanDate
	 * </pre>
	 * @param msg
	 * @param convData
	 * @return
	 */
	public String convSmsMsg(String msg, Map<String, String> convData) {
		
		if (StringUtils.isEmpty(msg) || convData == null || convData.isEmpty()) {
			return msg;
		}
		
		// ?????? ???????????? ?????????
		convData.put("title", this.convTitle(convData.get("title")));

		// ??????????????? ?????? (X?????????)
		String enterPlanDate = convData.get("enterPlanDate");
		if (!StringUtils.isEmpty(enterPlanDate)) {
			
			try {
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				long ldiff = TimeUnit.MILLISECONDS.toDays(
					sdf.parse(enterPlanDate).getTime() - sdf.parse(sdf.format(new Date())).getTime()
				);
				
				enterPlanDate = String.valueOf(ldiff < 0 ? 0 : ldiff);
				
			} catch (ParseException e) {
				// 2019.05.07 ???????????? ??????????????? ??????
				LOGGER.error("SMS ????????? ?????? ??????");
			}
		}
		convData.put("enterPlanDate", enterPlanDate);
		
		//
		msg = msg.replace("$?????????$"		, StringUtils.defaultString(convData.get("userName")));
		msg = msg.replace("$??????$"		, StringUtils.defaultString(convData.get("storeName")));
		msg = msg.replace("$??????$"		, StringUtils.defaultString(convData.get("title")));
		msg = msg.replace("$???????????????$"	, StringUtils.defaultString(convData.get("loanWaitDate")));
		msg = msg.replace("$???????????????$"	, StringUtils.defaultString(convData.get("returnPlanDate")));
		msg = msg.replace("$???????????????$"	, StringUtils.defaultString(convData.get("enterPlanDate")));
		msg = msg.replace("$?????????$"		, StringUtils.defaultString(convData.get("libName")));
		return msg;
	}

	/**
	 * ?????? ?????? combo
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
	 * ????????? ?????? combo
	 * @param libManageCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectLibrary(Map<String, String> params) {
		return (List<Map<String, Object>>) commonDao.selectList(NAME_SPACE.concat("selectLibrary"), params);
	}
	
	/**
	 * ?????? ???????????? ?????????
	 *  - 0?????? ????????? ??????
	 *  - 2???????????? '..'??? ??????????????? ??????
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
			// 2019.05.07 ???????????? ??????????????? ??????
			LOGGER.error("SMS ?????? ????????? ?????? ??????");
		}
		
		return title;
	}
	
	
	/**
	 * ????????? ????????? ??????
	 * <pre>
	 * <b>[????????????]</b>
	 * #{????????????}	: userName
	 * #{?????????}	: storeName
	 * #{??????}		: title
	 * #{???????????????}	: loanWaitDate
	 * </pre>
	 * @param msg
	 * @param convData
	 * @return
	 */
	public String convAlimMsg(Map<String,String> smsParam, Map<String, String> convData) {

		String msg = commonDao.selectOne(NAME_SPACE.concat("getAlimMsg"),smsParam)== null ? "" :(String) commonDao.selectOne(NAME_SPACE.concat("getAlimMsg"),smsParam);

		if (StringUtils.isEmpty(msg) || convData == null || convData.isEmpty()) {
			return msg;
		}
		
		// ?????? ???????????? ?????????
//		convData.put("title", this.convTitle(convData.get("title")));

		// ??????????????? ?????? (X?????????)
		String enterPlanDate = convData.get("enterPlanDate");
		if (!StringUtils.isEmpty(enterPlanDate)) {
			
			try {
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				long ldiff = TimeUnit.MILLISECONDS.toDays(
					sdf.parse(enterPlanDate).getTime() - sdf.parse(sdf.format(new Date())).getTime()
				);
				
				enterPlanDate = String.valueOf(ldiff < 0 ? 0 : ldiff);
				
			} catch (ParseException e) {
				// 2019.05.07 ???????????? ??????????????? ??????
				LOGGER.error("SMS ????????? ?????? ??????");
			}
		}
		convData.put("enterPlanDate", enterPlanDate);
		
		//
		msg = msg.replace("#{????????????}"		, StringUtils.defaultString(convData.get("userName")));
		msg = msg.replace("#{?????????}"		, StringUtils.defaultString(convData.get("storeName")));
		msg = msg.replace("#{??????}"		, StringUtils.defaultString(convData.get("title")));
		msg = msg.replace("#{???????????????}"	, StringUtils.defaultString(convData.get("loanWaitDate")));
		msg = msg.replace("#{????????????}"	, StringUtils.defaultString(convData.get("loanWaitDate")));
		msg = msg.replace("#{???????????????}"	, StringUtils.defaultString(convData.get("loanWaitDate")));
		msg = msg.replace("#{???????????????}"	, StringUtils.defaultString(convData.get("returnPlanDate")));
		msg = msg.replace("#{???????????????}"	, StringUtils.defaultString(convData.get("enterPlanDate")));
		msg = msg.replace("#{????????????}"		, StringUtils.defaultString(convData.get("libManageName")));
		msg = msg.replace("#{????????????}"		, StringUtils.defaultString(convData.get("storePhone")));
		return msg;
	}

	/**
	 * ????????????
	 *
	 */
	public List<Map<String,Object>> getViewOptionList() {
		//????????????
		List<Map<String,Object>> viewList = (List<Map<String, Object>>) commonDao.selectList("common.env.getViewOption");

		return viewList;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectLibraryLimit() {
		return (List<Map<String, Object>>) commonDao.selectList(NAME_SPACE.concat("selectLibraryLimit"));
	}
}
