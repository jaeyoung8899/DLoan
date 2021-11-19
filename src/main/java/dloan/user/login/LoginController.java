package dloan.user.login;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dloan.common.util.SessionUtils;

@Controller("dloan.user.login.LoginController")
public class LoginController {

	@Autowired
	private LoginService loginService;

	/**
	 * 로그인 페이지 이동
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/login")
	public ModelAndView loginPage(HttpServletRequest req, @RequestParam Map<String, String> params) {

		ModelAndView mv = new ModelAndView("user/login/login");	
		
		String retUrl  = "/request";
		String referer = req.getHeader("Referer");
		String [] uri  = {"request", "myRequestInfo"};
		String doamin  = req.getRequestURL().toString().replace(req.getRequestURI(), "");
		
		if (referer != null) {
			for (String sUri : uri) {
				if (referer.equals(doamin + "/" + sUri)) {
					retUrl = "/" + sUri;
					break;
				}
			}
		}
		
		mv.addObject("retUrl", retUrl);
		mv.addAllObjects(params);
		return mv;
	}
	
	
	/**
	 * 로그인 처리
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/loginProc")
	public @ResponseBody Map<String, Object> login (
			@RequestParam Map<String, String> params) {
		return loginService.login(params);
	}
	
	/**
	 * 사용자 로그아웃
	 * 
	 * @throws IOException 
	 */
	@RequestMapping(value="/logout")
	public void logout(HttpServletRequest req, HttpServletResponse res) throws IOException {
		SessionUtils.sessionUserRemove();
		res.sendRedirect(req.getContextPath() + "/login");
	}
	
	/**
	 * 외부연동 로그인
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/externalLogin")
	public void loginExternal(@RequestParam Map<String, String> params, HttpServletResponse res) throws IOException {
		
		// 1. 반환주소 확인
		String returnUrl = params.get("returnUrl");
		if (StringUtils.isEmpty(returnUrl)) {
			throw new IOException("반환주소가 없습니다.");
		}
		
		// 2. 외부연동 로그인
		// 로그인이 실패해도 자체 로그인 페이지로 이동되기 때문에 무조건 성공처리
		this.loginService.loginExternal(params);
		
		// 3. 반환페이지로 이동
		res.sendRedirect(returnUrl);
	}
	
	/**
	 * 외부연동 로그인
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/externalLoginUserNo")
	public void loginExternalUserNo(@RequestParam Map<String, String> params, HttpServletResponse res) throws IOException {
		
		// 1. 반환주소 확인
		String returnUrl = params.get("returnUrl");
		if (StringUtils.isEmpty(returnUrl)) {
			throw new IOException("반환주소가 없습니다.");
		}
		
		// 2. 외부연동 로그인
		// 로그인이 실패해도 자체 로그인 페이지로 이동되기 때문에 무조건 성공처리
		this.loginService.loginExternalUserNo(params);
		
		// 3. 반환페이지로 이동
		res.sendRedirect(returnUrl);
	}
	
	/**
	 * 외부연동 로그아웃
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/externalLogout")
	public void logoutExternal(@RequestParam Map<String, String> params, HttpServletResponse res) throws IOException {
		
		// 1. 반환주소 확인
		String returnUrl = params.get("returnUrl");
		if (StringUtils.isEmpty(returnUrl)) {
			throw new IOException("반환주소가 없습니다.");
		}
		
		// 2. 로그아웃
		SessionUtils.sessionUserRemove();
		
		// 3. 반환페이지로 이동
		res.sendRedirect(returnUrl);
	}
}
