package dloan.store.cancel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dloan.common.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dloan.common.util.ExcelDown;
import dloan.library.envset.LibraryManageService;
import dloan.library.envset.StoreManageService;

@Controller
@RequestMapping(value="/store")
public class CancelInfoController {
	
	@Autowired
	private CancelInfoService cancelInfoService;
	
	@Autowired
	private LibraryManageService libraryManageService;
	
	@Autowired
	private StoreManageService storeManageService;
	
	/**
	 * 신청거절내역 조회
	 * 
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/storeCancelInfo")
	public ModelAndView storeCancelInfo(@RequestParam Map<String, String> params) throws Exception {
		ModelAndView mv = new ModelAndView("store/cancel/cancelInfo");

		mv.addAllObjects(params);
		mv.addAllObjects(cancelInfoService.selectCancelInfo(params));
		mv.addObject("storeYn", SessionUtils.getStoreYn());
		mv.addObject("libList"   , this.libraryManageService.selectLibrary());
		mv.addObject("storeList" , this.storeManageService.selectStoreMng());
		
		return mv;
	}
	
	/**
	 * 신청거절내역 - 엑셀 다운로드
	 * 
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/storeCancelInfoToExcel")
	public void storeCancelInfoToExcel(HttpServletRequest req, HttpServletResponse res,
			@RequestParam Map<String, String> params) throws Exception {
		
		params.put("start", "1");
		params.put("display", "999999");
		
		Map<String, Object> data = cancelInfoService.selectCancelInfo(params);
		Map<String, Object> beans = new HashMap<String, Object>();
		
		beans.put("dataList", (List<Map<String, Object>>) data.get("resultList"));
		
		ExcelDown ed = new ExcelDown();
		ed.download(req, res, beans, ed.getFilename("신청거절내역"), "storeCancel.xlsx");
	}
}
