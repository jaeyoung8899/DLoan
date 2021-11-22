package dloan.common.handler;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import dloan.common.util.SessionUtils;

/**
 * 세션관리 인터셉터
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 도서관 세션유지시간 (초단위)
	 */
	@Value("#{conf['lib_session_timeout']}")
	private int libSessionTimeout;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	throws Exception {

		String uri = request.getRequestURI();
		
		if (uri.startsWith("/lib")) {
			// ----------------------------------------------------------------------------------------------------
			// 도서관
			// ----------------------------------------------------------------------------------------------------
			
			// 세선확인
			if (!SessionUtils.isLibSession()) {
				// 로그인 페이지로 이동
				if (this.isAjax(request)) {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					return false;
				} else {
					response.sendRedirect(request.getContextPath() + "/lib/login");
					return false;
				}
			}
			
			// 도서관 세션유지시간 초기화
			SessionUtils.setLibExfTime(new Date(new Date().getTime() + (this.libSessionTimeout*1000)));
			
		} else if (uri.startsWith("/store")) {
			// ----------------------------------------------------------------------------------------------------
			// 서점
			// ----------------------------------------------------------------------------------------------------
			
			// 세선확인
			if (SessionUtils.getStoreId() == null) {
				// 로그인 페이지로 이동
				if (this.isAjax(request)) {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					return false;
				} else {
					response.sendRedirect(request.getContextPath() + "/store/login");
					return false;
				}
			}
						
		} else {
			// ----------------------------------------------------------------------------------------------------
			// 이용자
			// ----------------------------------------------------------------------------------------------------
			
			// 세선확인
			if (SessionUtils.getUserId() == null) {
				// 로그인 페이지로 이동
				// 변경 : 로그인 페이지가 아니고 신청페이지로 이동 (신청페이지는 세션이 없어도 보여지도록 변경)
				if (this.isAjax(request)) {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					return false;
				} else {
					//response.sendRedirect(request.getContextPath() + "/login");
					response.sendRedirect(request.getContextPath() + "/request");
					return false;
				}
			}
		}
		
		return super.preHandle(request, response, handler);
	}
	
	/**
	 * AJAX 호출인지 확인
	 * 
	 * @param request
	 * @return
	 */
	private boolean isAjax(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}
}
