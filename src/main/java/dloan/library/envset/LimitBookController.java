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
public class LimitBookController {
	
	@Autowired
	private LimitBookService limitBookService;
	/**
	 * 선정제외 도서관리 이동
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/limitBook")
	public ModelAndView limitBook(HttpServletRequest req, @RequestParam Map<String, String> params) {

		ModelAndView mv = new ModelAndView("library/envset/limitBook");
		mv.addAllObjects(params);
		mv.addAllObjects(limitBookService.selectLimitBook(params));
		return mv;
	}
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/getISBNCount")
	public @ResponseBody Map<String, Object> selectLibraryOrderInfo(@RequestParam Map<String, Object> params) {
		
		return limitBookService.getISBNCount(params);
	}
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/saveLimitBook")
	public @ResponseBody Map<String, Object> saveLimitBook(@RequestParam Map<String, Object> params) {
		
		return limitBookService.saveLimitBook(params);
	}
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/deleteLimitBook")
	public @ResponseBody Map<String, Object> deleteLimitBook(@RequestParam(name="delList", required = true) List<String> delList) {
		
		return limitBookService.deleteLimitBook(delList);
	}
	

}
