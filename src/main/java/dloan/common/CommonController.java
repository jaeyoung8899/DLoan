package dloan.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/common"})
public class CommonController {
	
	@SuppressWarnings("unused")
	private static final Logger logger =  Logger.getLogger(CommonController.class);
	
	/**
	 * 에러페이지 이동
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/error")
	public String error(HttpServletRequest req) {
		return "common/error";
	}
}
