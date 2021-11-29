package dloan.library.spending;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dloan.common.CommonService;
import dloan.common.util.ExcelDown;
import dloan.common.util.SessionUtils;
import dloan.library.envset.StoreManageService;


@Controller
@RequestMapping(value="/lib/spending")
public class LibSpendingController {
	Logger log = LoggerFactory.getLogger(LibSpendingController.class);
	

	@Autowired
	private LibSpendingService libSpendingService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private StoreManageService storeManageService;
	
	/**
	 * 지줄결의
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="")
	public ModelAndView spendingInfo(HttpServletRequest req, @RequestParam Map<String, String> params) throws Exception { 
		ModelAndView mv = new ModelAndView("library/spending/libSpending");
		
		Map<String, String> reqMap = new HashMap<String, String>();
		if(!StringUtils.isEmpty(SessionUtils.getLibMngCd())) {
			reqMap.put("libManageCode", SessionUtils.getLibMngCd());
			
			if(!params.containsKey("libManageCode")) {
				params.put("libManageCode", SessionUtils.getLibMngCd());
			}
		}
		
		mv.addAllObjects(params);
		mv.addObject("libList", commonService.selectLibrary(reqMap));
		mv.addObject("storeList" , this.storeManageService.selectStoreMng());
		mv.addAllObjects(libSpendingService.selectLibSpendingInfo(params));
		
		mv.addObject("tab", "spending");
		return mv;
	}
	
	/**
	 * 지출결의 등록페이지
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/register")
	public ModelAndView register(HttpServletRequest req, @RequestParam Map<String, Object> params) throws Exception {
		ModelAndView mv = new ModelAndView("library/spending/libSpendingRegister");
		
		if(!StringUtils.isEmpty(SessionUtils.getLibMngCd())) {
			if(!params.containsKey("libManageCode")) {
				params.put("libManageCode", SessionUtils.getLibMngCd());
				mv.addObject("storeList", libSpendingService.storeInfo(params));
			}
		} else if(SessionUtils.isLibSession() && StringUtils.isEmpty(SessionUtils.getLibMngCd())) {
			// 통합관리자 로그인
			mv.addObject("libList", commonService.selectLibrary(null));
		}
		
		mv.addAllObjects(params);
		
		mv.addObject("tab", "spending");
		
		return mv; 
	}
	
	/**
	 * 지출결의 입력
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/regPrice")
	@ResponseBody
	public Map<String, Object> regPrice(@RequestParam Map<String, String> params) throws Exception {
		return libSpendingService.regPrice(params);
	}
	
	/**
	 * 반품도서 조회
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/refundBookInfo")
	public ModelAndView refundInfo(@RequestParam Map<String, String> params) throws Exception {
		ModelAndView mv = new ModelAndView("library/spending/libRefundBook");
		
		if(params.get("libManageCode") == null) {
			params.put("libManageCode", SessionUtils.getLibMngCd());
		}
		
		mv.addAllObjects(params);
		mv.addObject("libList", commonService.selectLibrary(null));
		mv.addObject("storeList", commonService.selectStore(null));
		mv.addAllObjects(libSpendingService.selectLibRefundBookInfo(params));
		
		mv.addObject("tab", "refund");
		
		return mv;
	}
	
	/**
	 * 반품도서 - 엑셀 다운로드
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/refundBookInfoToExcel")
	public void refundBookInfoToExcel(HttpServletRequest req, HttpServletResponse res, @RequestParam Map<String, String> params) throws Exception {
		params.put("start", "1");
		params.put("display", "999999");
		
		Map<String, Object> data = libSpendingService.selectLibRefundBookInfo(params);
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("dataList", (List<Map<String, Object>>) data.get("resultList"));
		
		ExcelDown ed = new ExcelDown();
		ed.download(req, res, beans, ed.getFilename("반품도서"), "libRefundBook.xlsx");
	}
	
	/**
	 * 지출결의 조회
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/{key}")
	public ModelAndView refundInfo(@RequestParam Map<String, String> params, @PathVariable("key") String rKey) throws Exception {
		ModelAndView mv = new ModelAndView("library/spending/libSpendingModify");
		
		params.put("recKey", rKey);
		
		mv.addAllObjects(params);
		
		mv.addAllObjects(libSpendingService.selectLibSpendingDetailInfo(params));
		
		mv.addObject("tab", "spending");
		
		return mv;
	}
	
	/**
	 * 지출결의 금액 수정 서비스
	 * 
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/savePrice")
	@ResponseBody
	public Map<String, Object> savePrice(@RequestParam Map<String, Object> params) throws Exception {
		return libSpendingService.savePrice(params);
	}
	
	/**
	 * 서점 목록 조회
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/getStore")
	@ResponseBody
	public Map<String, Object> getStore(@RequestParam Map<String, Object> params) {
		return libSpendingService.getStore(params);
	}
	
	/**
	 * 지출결의 삭제
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Map<String, Object> deleteSpending(@RequestParam Map<String, Object> params) throws Exception {
		return libSpendingService.deleteSpending(params);
	}
}