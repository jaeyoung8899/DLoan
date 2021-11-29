package dloan.store.returnloan;

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
import dloan.library.envset.SmsMngService;

@Controller
@RequestMapping(value="/store")
public class ReturnLoanController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnLoanController.class);
	
	@Autowired
	private SmsMngService smsMngService;
	
	@Autowired
	private ReturnLoanService returnLoanService;

	@Autowired
	private CommonService commonService;
	
	/**
	 * 대출 반납
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/returnLoanInfo")
	public ModelAndView storeRequestInfo(@RequestParam Map<String, String> params) throws Exception {

		String pageName = "";
		String storeYn = SessionUtils.getStoreYn();
		if(storeYn !=null && storeYn.equals("Y")) {
			pageName = "store/returnloan/returnLoanReturn";
		}else{
			pageName = "store/returnloan/returnLoan";
		}

		ModelAndView mv = new ModelAndView(pageName);

		params.put("storeId", SessionUtils.getStoreId());
		mv.addAllObjects(params);
		mv.addAllObjects(returnLoanService.selectReturnLoanInfo(params));
		mv.addObject("smsList",    smsMngService.selectSmsMng(params));
		
		return mv;
	}
	
	/**
	 * 대출
	 * @param ltRecKey
	 * @param ltReqStatus
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/updateStoreLoan")
	public @ResponseBody Map<String, Object> updateStoreLoan (
			@RequestParam Map<String, String> params) {
		
		Map <String, Object> retMap = returnLoanService.updateStoreLoan(params);
		
		List<Map<String, Object>> smsList = null;
		
		if (retMap.containsKey("smsList")) {
			smsList = (List<Map<String, Object>>)retMap.get("smsList");
		}
		
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
			LOGGER.error("서점 관리자페이지 대출처리 오류");
		}
		
		return retMap;
	}
	
	/**
	 * 반납
	 * @param ltRecKey
	 * @param ltReqStatus
	 * @return
	 */
	@RequestMapping(value="/updateStoreReturn")
	public @ResponseBody Map<String, Object> updateStoreReturn (
			@RequestParam Map<String, String> params) {
		return returnLoanService.updateStoreReturn(params);
	}
	
	/**
	 * 대출대기 = > 도서준비 변경
	 * @param ltRecKey
	 * @param ltReqStatus
	 * @return
	 */
	@RequestMapping(value="/updateStoreLoanReqStatus")
	public @ResponseBody Map<String, Object> updateStoreLoanReqStatus (
			@RequestParam(name="reqStatus") String reqStatus,
			@RequestParam(name="ltRecKey")  List<String> ltRecKey) {
		return returnLoanService.updateStoreLoanReqStatus(reqStatus, ltRecKey);
	}
	
	/**
	 * 대출 = > 대출대기 변경
	 * @param ltRecKey
	 * @param ltReqStatus
	 * @return
	 */
	@RequestMapping(value="/updateWaitReqStatus")
	public @ResponseBody Map<String, Object> updateWaitReqStatus (
			@RequestParam(name="reqStatus") String reqStatus,
			@RequestParam(name="ltRecKey")  List<String> ltRecKey) {
		return returnLoanService.updateWaitReqStatus(reqStatus, ltRecKey);
	}
	
	/**
	 * 반납 = > 대출 변경
	 * @param ltRecKey
	 * @param ltReqStatus
	 * @return
	 */
	@RequestMapping(value="/updateLoanReqStatus")
	public @ResponseBody Map<String, Object> updateLoanReqStatus (
			@RequestParam(name="reqStatus") String reqStatus,
			@RequestParam(name="ltRecKey")  List<String> ltRecKey) {
		return returnLoanService.updateLoanReqStatus(reqStatus, ltRecKey);
	}
	
	/**
	 * 대출 반납 - 엑셀 다운로드
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/returnLoanInfoToExcel")
	public void storeRequestInfoToExcel(HttpServletRequest req, HttpServletResponse res,
			@RequestParam Map<String, String> params) throws Exception {

		params.put("storeId", SessionUtils.getStoreId());
		params.put("start", "1");
		params.put("display", "999999");
		
		Map<String, Object> data = returnLoanService.selectReturnLoanInfo(params);
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("dataList", (List<Map<String, Object>>) data.get("resultList"));
		
		ExcelDown ed = new ExcelDown();
		ed.download(req, res, beans, ed.getFilename("대출반납"), "storeLoan.xlsx");
	}
}
