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
public class LibraryReqManageController {
	
	@Autowired
	private LibraryReqManageService libraryReqManageService;
	
	/**
	 * 도서관 신청관리 페이지
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(value="/libReqMng")
	public ModelAndView libMng() {
		ModelAndView mav = new ModelAndView("library/envset/libReqMng");
		mav.addObject("lib", this.libraryReqManageService.selectLibrary());
		return mav;
	}
	
	/**
	 * 도서관 신청제한 업데이트 
	 * 
	 * @param params
	 * @return 업데이트 결과
	 */
	@RequestMapping("/updateLibraryLimit")
	@ResponseBody
	public Map<String, Object> updateLibraryLimit(@RequestParam Map<String, Object> params) {
		return libraryReqManageService.updateLibraryLimit(params);
	}
}