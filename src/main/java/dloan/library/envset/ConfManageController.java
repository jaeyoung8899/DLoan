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
public class ConfManageController {
	
	@Autowired
	private ConfManageService confManageService;
	
	/**
	 * 기타설정관리 페이지
	 * 
	 * @param req
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/confMng")
	public ModelAndView libMng(HttpServletResponse res) throws IOException {
		if (!StringUtils.isEmpty(SessionUtils.getLibMngCd())) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} else {
			ModelAndView mav = new ModelAndView("library/envset/confMng");
			mav.addObject("conf", this.confManageService.selectConfMng());
			return mav;
		}
	}
	
	/**
	 * 기타설정수정
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/updateConfMng")
	public @ResponseBody Map<String, Object> updateConfMng(
			@RequestParam(value="ltConfId")    List<String> ltConfId,
			@RequestParam(value="ltConfValue") List<String> ltConfValue
			) {
		return confManageService.updateStoreMng(ltConfId, ltConfValue);
	}
}
	                                              