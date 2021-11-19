package dloan.library.envset;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/lib/envset")
public class CancelReasonController {
	
	@Autowired
	private CancelReasonService cancelReasonService;
	/**
	 * 선정제외 도서관리 이동
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/cancelReason")
	public ModelAndView limitBook(HttpServletRequest req, @RequestParam Map<String, String> params) {

		ModelAndView mv = new ModelAndView("library/envset/cancelReason");
		mv.addAllObjects(params);
		mv.addObject("reasonList", cancelReasonService.selectCancelReason(params));
		return mv;
	}
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/saveCancelReason")
	public @ResponseBody Map<String, Object> saveLimitBook(
			@RequestParam(name="ltReason", required = false) List<String> ltReason,
			@RequestParam(name="delList",  required = false) List<String> delList) {
		
		return cancelReasonService.saveCancelReason(ltReason, delList);
	}
}
