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

import dloan.common.util.ValidUtils;

@Controller
@RequestMapping(value="/lib/envset")
public class LibraryOrderedController {
	
	@Autowired
	private LibraryOrderedService libraryOrderedService;
	
	/**
	 * 도서관 배분 이동
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/libOrdered")
	public ModelAndView libOrdered(HttpServletRequest req, @RequestParam Map<String, String> params) {

		ModelAndView mv = new ModelAndView("library/envset/libOrdered");
		mv.addAllObjects(params);
		
		mv.addObject("libList", libraryOrderedService.selectStoreInfo(params));
		
		return mv;
	}
	
	/**
	 * 
	 * @param lib
	 * @param storeId
	 * @return
	 */
	@RequestMapping(value="/selectLibraryOrderInfo")
	public @ResponseBody Map<String, Object> selectLibraryOrderInfo(@RequestParam Map<String, String> params) {
		
		return libraryOrderedService.selectLibraryOrderInfo(params);
	}
	
	/**
	 * 
	 * @param lib
	 * @param storeId
	 * @return
	 */
	@RequestMapping(value="/saveLibOrdered")
	public @ResponseBody Map<String, Object> saveLibOrdered(@RequestParam(value="lib") List<String> lib	, 
			@RequestParam(value="storeId") String storeId
			) {
		libraryOrderedService.saveLibOrdered(lib, null, storeId);
		return ValidUtils.resultSuccessMap();
	}

}
