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
public class SmsMngController {
	
	@Autowired
	private SmsMngService smsMngService;
	
	/**
	 * sms 설정 이동
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/smsMng")
	public ModelAndView smsMng(HttpServletRequest req, @RequestParam Map<String, String> params) {

		ModelAndView mv = new ModelAndView("library/envset/smsMng");
		mv.addAllObjects(params);
		
		mv.addObject("smsList", smsMngService.selectSmsMng(params));
		
		return mv;
	}

	/**
	 *  저장
	 * @param deleteReckey
	 * @param recKey
	 * @param name
	 * @param contents
	 * @return
	 */
	@RequestMapping(value="/saveSmsMng")
	public @ResponseBody Map<String, Object> saveSmsMng(
			@RequestParam(name="deleteRecKey", required = false) List<String> deleteRecKey,
			@RequestParam(name="recKey",       required = false) List<String> recKey,
			@RequestParam(name="name",         required = false) List<String> name,
			@RequestParam(name="contents",     required = false) List<String> contents
			) {
		return smsMngService.saveSmsMng(deleteRecKey, recKey, name, contents);
	}
}
