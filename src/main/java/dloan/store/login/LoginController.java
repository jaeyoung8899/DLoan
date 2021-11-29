package dloan.store.login;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dloan.common.handler.DLoanEnvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dloan.common.util.SessionUtils;

@Controller("dloan.store.login.LoginController")
@RequestMapping(value="/store")
public class LoginController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private DLoanEnvService dLoanEnvService;

	/**
	 * login 이동
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value={"", "/", "/login"})
	public ModelAndView loginPage(HttpServletRequest req, @RequestParam Map<String, String> params) {
		ModelAndView mv = new ModelAndView("store/login/storeLogin");
		mv.addObject("viewInfoList",dLoanEnvService.getEtcMap());
		System.out.println(dLoanEnvService.getEtcMap());
		mv.addAllObjects(params);
		return mv;
	}
	

	/**
	 * login
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/loginProc")
	public @ResponseBody Map<String, Object> login (@RequestParam Map<String, Object> params, HttpSession session) {
		Map<String, Object> rtnMap = this.loginService.login(params);
		
		return rtnMap;
	}
	
	/**
	 * 서점 로그아웃
	 * 
	 * @throws IOException 
	 */
	@RequestMapping(value="/logout")
	public void logout(HttpServletRequest req, HttpServletResponse res) throws IOException {
		SessionUtils.sessionStoreRemove();
		res.sendRedirect(req.getContextPath() + "/store/login");
	}
	

	/**
	 * 비밀번호 변경
	 * @param req
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/storePasswordChange")
	public ModelAndView libPasswordChange(HttpServletRequest req, @RequestParam Map<String, String> params) {

		ModelAndView mv = new ModelAndView("store/login/storePasswordChange");
		mv.addObject("storeYn", SessionUtils.getStoreYn());
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
}
