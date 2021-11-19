package dloan.library.login;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dloan.common.util.SessionUtils;
import dloan.common.util.ValidUtils;

@Controller("dloan.library.login.LoginController")
@RequestMapping(value="/lib")
public class LoginController {

	@Value("#{conf['lib_session_timeout']}")
	private int libSessionTimeout;
	
	@Autowired
	private LoginService loginService;
	
	/**
	 * login 이동
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value={"", "/", "/login"})
	public ModelAndView loginPage(HttpServletRequest req, @RequestParam Map<String, String> params) {
		ModelAndView mv = new ModelAndView("library/login/libLogin");
		mv.addAllObjects(params);
		return mv;
	}
	

	/**
	 * 도서관 login
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/loginProc")
	public @ResponseBody Map<String, Object> login (@RequestParam Map<String, Object> params, HttpSession session) {
		
		Map<String, Object> rtnMap = this.loginService.login(params);
		if ("Y".equals(rtnMap.get("resultCode"))) {
			SessionUtils.setLibExfTime(new Date(new Date().getTime() + (this.libSessionTimeout*1000)));
			session.setMaxInactiveInterval(this.libSessionTimeout);
		}
		
		return rtnMap;
	}
	
	
	/**
	 * 비밀번호 변경
	 * @param req
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/libPasswordChange")
	public ModelAndView libPasswordChange(HttpServletRequest req, @RequestParam Map<String, String> params) {

		ModelAndView mv = new ModelAndView("library/login/libPasswordChange");
		mv.addAllObjects(params);
		return mv;
	}
	
	
	/**
	 * 비밀번호 변경
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/passwordChangeProc")
	public @ResponseBody Map<String, Object> passwordChangeProc (
			@RequestParam Map<String, String> params) {
		
		return loginService.passwordChange(params);
	}
	
	
	/**
	 *  도서관 로그아웃
	 * 
	 * @throws IOException 
	 */
	@RequestMapping(value="/logout")
	public void logout(HttpServletRequest req, HttpServletResponse res) throws IOException {
		SessionUtils.sessionLibRemove();
		res.sendRedirect(req.getContextPath() + "/lib/login");
	}
	
	/**
	 * 도서관 로그인 상태확인
	 * 
	 * @return
	 */
	@RequestMapping(value="/loginCheck")
	public @ResponseBody Map<String, Object> loginCheck() {
		if (!SessionUtils.isLibSession()) {
			return ValidUtils.resultErrorMap("로그인만료");
		} else {
			return ValidUtils.resultSuccessMap();
		}
	}
}
