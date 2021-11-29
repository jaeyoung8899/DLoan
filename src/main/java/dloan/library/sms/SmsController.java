package dloan.library.sms;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dloan.common.CommonService;
import dloan.common.util.SessionUtils;

/**
 * SMS 이력조회
 * @author WB
 */
@Controller
@RequestMapping(value="/lib")
public class SmsController {
	
	@Autowired
	private SmsService smsService;
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping(value = "/sms")
	public ModelAndView sms(@RequestParam Map<String, String> params) {
		ModelAndView mv = new ModelAndView("/library/sms/sms");
		
		if(SessionUtils.isLibSession() && SessionUtils.getLibMngCd() == null) {
			mv.addObject("libList", commonService.selectLibrary(null));
		} else if(SessionUtils.isLibSession() && StringUtils.isEmpty(SessionUtils.getLibMngCd()) == false) {
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put("libManageCode", SessionUtils.getLibMngCd());
			mv.addObject("libList", commonService.selectLibrary(paramMap));
		}
		
		if(params.isEmpty() == false) {
			mv.addAllObjects(smsService.smsList(params));
		}
		
		mv.addAllObjects(params);
		
		return mv;
	}
}