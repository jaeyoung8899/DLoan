package dloan.library.request;

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
import dloan.library.envset.LibraryManageService;
import dloan.library.envset.SmsMngService;
import dloan.library.envset.StoreManageService;


@Controller
@RequestMapping(value="/lib")
public class LibRequestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LibRequestController.class);

	@Autowired
	private LibRequestService libRequestService;

	@Autowired
	private CancelReasonService cancelReasonService;
	
	@Autowired
	private LibraryManageService libraryManageService;
	
	@Autowired
	private StoreManageService storeManageService;
	
	@Autowired
	private SmsMngService smsMngService;
	
	@Autowired
	private CommonService commonService;
	
	
	/**
	 * 신청 조회
	 * 
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/libRequestInfo")
	public ModelAndView storeRequestInfo(HttpServletRequest req, @RequestParam Map<String, String> params) throws Exception {
		
		// 신청진행상태 디폴트 -> 도서관확인요청(S03)
		if (params.get("reqStatus") == null) {
			params.put("reqStatus", "S03");
		}
		
		// 배분도서관 디폴트 -> 로그인도서관
		if (params.get("reqLibManageCode") == null) {
			params.put("reqLibManageCode", SessionUtils.getLibMngCd());
		}
		
		ModelAndView mv = new ModelAndView("library/request/libRequest");
		mv.addAllObjects(params);
		mv.addAllObjects(this.libRequestService.selectLibRequestInfo(params));
		mv.addObject    ("reasonList", this.cancelReasonService.selectCancelReason(params));
		mv.addObject    ("smsList"   , this.smsMngService.selectSmsMng(params));
		mv.addObject    ("libList"   , this.libraryManageService.selectLibrary());
		mv.addObject    ("storeList" , this.storeManageService.selectStoreMng());
		return mv;
	}
	
	/**
	 * 신청 조회 - 엑셀 다운로드
	 * @param request
	 * @param response
	 * @param params
	 * @throws Exception
	 */
	@RequestMapping(value = "/libRequestInfoToExcel")
	public void libRequestInfoToExcel(HttpServletRequest request, HttpServletResponse response
			, @RequestParam Map<String, String> params) throws Exception {
		params.put("start", "1");
		params.put("display", "999999");
		
		Map<String, Object> data = this.libRequestService.selectLibRequestInfo(params);
		Map<String, Object> beans = new HashMap<String, Object>();
		
		beans.put("dataList", data.get("resultList"));
		
		ExcelDown ed = new ExcelDown();
		ed.download(request, response, beans, ed.getFilename("신청승인내역"), "libReq.xlsx");
	}
	
	/**
	 * 상태변경
	 * @param ltRecKey
	 * @param ltReqStatus
	 * @return
	 */
	@RequestMapping(value="/updateLibReqStatus")
	public @ResponseBody Map<String, Object> updateLibReqStatus (
			@RequestParam(name="reqStatus") String reqStatus,
			@RequestParam(name="ltRecKey")  List<String> ltRecKey) {
		
		List<Map<String, Object>> smsList = this.libRequestService.updateLibReqStatus(reqStatus, ltRecKey);
		
		try {
			if (smsList != null && smsList.size() > 0) {
				for (Map<String, Object> smsMap : smsList) {
					commonService.smsSend(smsMap);
				}
			}
		} catch (Exception e) {
			// 2019.05.07 소스코드 보안취약점 조치
			//e.printStackTrace();
			LOGGER.error("도서관 관리자페이지 상태변경 오류");
		}
		
		return ValidUtils.resultSuccessMap();
	}
	
	/**
	 * 도서관 신청거절
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
		List<Map<String, Object>> smsList = libRequestService.updateCancelReason(msgType, msg, ltRecKey);
		
		try {
			if (smsList != null && smsList.size() > 0) {
				for (Map<String, Object> smsMap : smsList) {
					commonService.smsSend(smsMap);
				}
			}
		} catch (Exception e) {
			// 2019.05.07 소스코브 보안취약점 조치
			//e.printStackTrace();
			LOGGER.error("도서관 관리자페이지 신청거절 오류");
		}
		
		return ValidUtils.resultSuccessMap();
	}
	
	/**
	 * 제외도서로 등록
	 * 
	 * @param ltRecKey
	 * @return
	 */
	@RequestMapping(value="/updateLimitBook")
	public @ResponseBody Map<String, Object> updateLimitBook(@RequestParam(name="ltRecKey")  List<String> ltRecKey) {
		return this.libRequestService.updateLimitBook(ltRecKey);
	}
	
	/**
	 * 배분도서관 수정
	 * @param msgType
	 * @param msg
	 * @param ltRecKey
	 * @return
	 */
	@RequestMapping(value="/updateLibrary")
	public @ResponseBody Map<String, Object> updateLibrary (
			@RequestParam(name="changeLib",      required = true) String changeLib,
			@RequestParam(name="ltRecKey", required = true) List<String> ltRecKey) {
		
		// 트랜잭션 보장 못함.
		List<Map<String, Object>> smsList = libRequestService.updateLibrary(changeLib, ltRecKey);
		if(smsList.get(0).containsKey("resultCode")) {
			if(smsList.get(0).get("resultCode").equals("N")) {
				return ValidUtils.resultErrorMap((String) smsList.get(0).get("resultMessage"));
			}
		}
		try {
			if (smsList != null && smsList.size() > 0) {
				for (Map<String, Object> smsMap : smsList) {
					commonService.smsSend(smsMap);
				}
			}
		} catch (Exception e) {
			LOGGER.error("도서관 관리자페이지 신청거절 오류");
		}
				
		return ValidUtils.resultSuccessMap();
	}
}
