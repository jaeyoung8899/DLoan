package dloan.library.envset;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dloan.common.util.SessionUtils;

@Controller
@RequestMapping(value="/lib/envset")
public class LibraryManageController {
	
	@Autowired
	private LibraryManageService libraryManageService;
	
	/**
	 * 도서관관리 페이지
	 * 
	 * @param req
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/libMng")
	public ModelAndView libMng(HttpServletResponse res) throws IOException {
		if (!StringUtils.isEmpty(SessionUtils.getLibMngCd())) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} else {
			ModelAndView mav = new ModelAndView("library/envset/libMng");
			mav.addObject("lib", this.libraryManageService.selectLibrary());
			return mav;
		}
	}
	
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/pwFailCountReset")
	public @ResponseBody Map<String, Object> pwFailCountReset(
			@RequestParam Map<String, String> params) {
		
		return libraryManageService.pwFailCountReset(params);
	}
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/passwordReset")
	public @ResponseBody Map<String, Object> passwordChange(
			@RequestParam Map<String, String> params) {
		
		return libraryManageService.passwordChange(params);
	}
	
	/**
	 * 도서관정보 수정
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/updateLibrary")
	public @ResponseBody Map<String, Object> updateLibrary(
			@RequestParam(value="ltLibId")     List<String> ltLibId,
			@RequestParam(value="ltLibPhone")  List<String> ltLibPhone,
			@RequestParam(value="ltHandphone") List<String> ltHandphone,
			@RequestParam(value="ltName")      List<String> ltName,
			@RequestParam(value="ltLimitPrice")List<String> ltLimitPrice
			) {
		return libraryManageService.updateLibrary(ltLibId,ltLibPhone,ltHandphone, ltName, ltLimitPrice);
	}

	/**
	 * 도서관IP 조회
	 * 
	 * @param libId
	 * @return
	 */
	@RequestMapping(value="/selectLibAllowIP")
	public @ResponseBody List<String> selectLibAllowIP(@RequestParam(value="libId") String libId) {
		return this.libraryManageService.selectLibAllowIP(libId);
	}
	
	/**
	 * 도서관IP 변경
	 * 
	 * @param libId
	 * @param ltAllowIp
	 * @return
	 */
	@RequestMapping(value="/updateLibAllowIP")
	public @ResponseBody Map<String, Object> updateLibAllowIP(
			@RequestParam(value="libId")     String libId,
			@RequestParam(value="ltAllowIp") List<String> ltAllowIp
			) {
		return libraryManageService.updateLibAllowIP(libId, ltAllowIp);
	}
}
	                                              