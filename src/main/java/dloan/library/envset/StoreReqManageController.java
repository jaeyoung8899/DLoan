package dloan.library.envset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping(value="/lib/envset")
public class StoreReqManageController {
	
	@Autowired
	private StoreReqManageService storeReqManageService;
	
	/**
	 * 서점 신청관리 페이지
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(value="/storeReqMng")
	public ModelAndView libMng() {
		ModelAndView mav = new ModelAndView("library/envset/storeReqMng");
		mav.addObject("store", this.storeReqManageService.selectStore());
		return mav;
	}
	
	/**
	 * 서점 신청제한 업데이트 
	 * 
	 * @param params
	 * @return 업데이트 결과
	 */
	@RequestMapping("/updateStoreLimit")
	@ResponseBody
	public Map<String, Object> updateStoreLimit(@RequestParam Map<String, Object> params) {
		return this.storeReqManageService.updateStoreLimit(params);
	}
}