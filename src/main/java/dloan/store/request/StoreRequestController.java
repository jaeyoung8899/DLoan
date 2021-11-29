package dloan.store.request;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dloan.common.CommonService;
import dloan.common.util.ExcelDown;
import dloan.common.util.SessionUtils;
import dloan.common.util.ValidUtils;
import dloan.library.envset.CancelReasonService;
import dloan.library.envset.SmsMngService;


@Controller
@RequestMapping(value="/store")
public class StoreRequestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(StoreRequestController.class);
	
	@Autowired
	private StoreRequestService storeRequestService;

	@Autowired
	private SmsMngService smsMngService;
	
	@Autowired
	private CancelReasonService cancelReasonService;
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * 신청 조회
	 * 
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/storeRequestInfo")
	public ModelAndView storeRequestInfo(@RequestParam Map<String, String> params) throws Exception {

		String pageName = "";
		String storeYn = SessionUtils.getStoreYn();
		if(storeYn != null &&storeYn.equals("Y")) {
			pageName = "store/request/storeRequestReturn";
		}else{
			pageName = "store/request/storeRequest";
		}

		ModelAndView mv = new ModelAndView(pageName);

		// 디폴트 검색조건 (현재일자)
		// 2019 간담회 개선 : 디폴트 검색조건 삭제
		/*
		if (params.get("from_reqDate") == null && params.get("to_reqDate") == null) {
			String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			params.put("from_reqDate", today);
			params.put("to_reqDate"  , today);
		}
		*/
		
		params.put("storeId", SessionUtils.getStoreId());
		mv.addAllObjects(params);
		mv.addAllObjects(storeRequestService.selectStoreRequestInfo(params));

		mv.addObject("reasonList", cancelReasonService.selectCancelReason(params));
		mv.addObject("smsList",    smsMngService.selectSmsMng(params));
		
		return mv;
	}
	
	/**
	 * 상태변경
	 * @param ltRecKey
	 * @param ltReqStatus
	 * @return
	 */
	@RequestMapping(value="/updateStoreReqStatus")
	public @ResponseBody Map<String, Object> updateStoreReqStatus (
			@RequestParam(name="type"         )  String type,
			@RequestParam(name="reqStatus"    )  String reqStatus,
			@RequestParam(name="ltRecKey"     )  List<String> ltRecKey,
			@RequestParam(name="enterPlanDate")  String enterPlanDate,
			@RequestParam(name="confirmMessage")  String confirmMessage) {
		
		List<Map<String, Object>> smsList = null;
		
		if ("ST04".equals(type)) {
			smsList = storeRequestService.updateStoreReqStatusLoanWait(type, reqStatus, ltRecKey);
		} else {
			smsList = storeRequestService.updateStoreReqStatus(type, reqStatus, ltRecKey, enterPlanDate, confirmMessage);
		}
		
		// sms 예외처리
		try {
			if (smsList != null && smsList.size() > 0) {
				for (Map<String, Object> smsMap : smsList) {
					for(String key : smsMap.keySet()) {
						if(key.equals("notU01")) {
							return ValidUtils.resultErrorMap("신청중이 아닌 도서가 있습니다. 페이지 새로고침을 진행 해주세요.");  
						}
					}
					commonService.smsSend(smsMap);
				}
			}
		} catch (Exception e) {
			// 2019.05.07 소스코드 보안취약점 조치
			//e.printStackTrace();
			LOGGER.error("서점 관리자페이지 상태변경 오류");
		}
		
		return ValidUtils.resultSuccessMap();
	}
	
	
	/**
	 * 상태변경
	 * @param ltRecKey
	 * @param ltReqStatus
	 * @return
	 */
	@RequestMapping(value="/updatePrevReqStatus")
	public @ResponseBody Map<String, Object> updatePrevReqStatus (
			@RequestParam(name="ltRecKey"     )  List<String> ltRecKey) {
		

		return storeRequestService.updatePrevReqStatus(ltRecKey);
	}
	
	/**
	 * sms 발송
	 * @param msgType
	 * @param msg
	 * @param ltRecKey
	 * @return
	 */
	@RequestMapping(value="/storeReqSmsSend")
	public @ResponseBody Map<String, Object> storeReqSmsSend (
			@RequestParam(name="msgType",  required = true) String msgType,
			@RequestParam(name="msg",      required = true) String msg,
			@RequestParam(name="ltRecKey", required = true) List<String> ltRecKey) {
		
		return storeRequestService.storeReqSmsSend(msgType, msg, ltRecKey);
	}
	
	/**
	 * 거절
	 * @param msgType
	 * @param msg
	 * @param ltRecKey
	 * @return
	 */
	@RequestMapping(value="/updateCancelReason")
	public @ResponseBody Map<String, Object> updateCancelReason (
			@RequestParam(name="msgType",  required = true) String msgType,
			@RequestParam(name="msg",      required = true) String msg,
			@RequestParam(name="ltRecKey", required = true) List<String> ltRecKey) {
		
		// 트랜잭션 보장 못함.
		List<Map<String, Object>> smsList = storeRequestService.updateCancelReason(msgType, msg, ltRecKey);
		
		// sms 예외처리
		try {
			if (smsList != null && smsList.size() > 0) {
				for (Map<String, Object> smsMap : smsList) {
					commonService.smsSend(smsMap);
				}
			}
		} catch (Exception e) {
			// 2019.05.07 소스코드 보안취약점 조치
			//e.printStackTrace();
			LOGGER.error("서점 관리자페이지 신청거절 오류");
		}
		return ValidUtils.resultSuccessMap();
	}
	
	/**
	 * 신청 조회 - 엑셀 다운로드
	 * 
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/storeRequestInfoToExcel")
	public void storeRequestInfoToExcel(HttpServletRequest req, HttpServletResponse res,
			@RequestParam Map<String, String> params) throws Exception {
		
		// 디폴트 검색조건 (현재일자)
		if (params.get("from_reqDate") == null && params.get("to_reqDate") == null) {
			String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			params.put("from_reqDate", today);
			params.put("to_reqDate"  , today);
		}
		
		params.put("storeId", SessionUtils.getStoreId());
		params.put("start", "1");
		params.put("display", "999999");
		
		Map<String, Object> data = storeRequestService.selectStoreRequestInfo(params);
		Map<String, Object> beans = new HashMap<String, Object>();
		
		beans.put("dataList", (List<Map<String, Object>>) data.get("resultList"));
		
		ExcelDown ed = new ExcelDown();
		ed.download(req, res, beans, ed.getFilename("신청승인내역"), "storeReq.xlsx");
	}
}
