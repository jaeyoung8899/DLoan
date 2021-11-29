package dloan.common;

import javax.servlet.http.HttpServletRequest;

import dloan.common.handler.DLoanEnvService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value={"/common"})
public class CommonController {
	
	@SuppressWarnings("unused")
	private static final Logger logger =  Logger.getLogger(CommonController.class);

	@Autowired
	private CommonService commonService;

	@Autowired
	private  DLoanEnvService dLoanEnvService;

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

	/**
	 * 로컬스토리지용 환경설정 가져오기
	 */
	@RequestMapping(value = "/getConfig")
	public @ResponseBody List<Map<String,Object>> getConfig() {
		dLoanEnvService.makeEnv();
		return commonService.getViewOptionList();
	}

}
