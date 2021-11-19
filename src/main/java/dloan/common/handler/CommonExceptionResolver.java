package dloan.common.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import dloan.common.util.ValidUtils;

public class CommonExceptionResolver extends SimpleMappingExceptionResolver {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonExceptionResolver.class);
	
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		if (this.isAjax(request)) {
			// 2019.05.07 소스코드 보안취약점 조치
			//ex.printStackTrace();
			LOGGER.error("commonExceptionResolver");
			return new ModelAndView("jsonView", ValidUtils.resultErrorMap("시스템 장애가 발생하였습니다. 관리자에게 문의하세요."));
		} else {
			return super.doResolveException(request, response, handler, ex);
		}
	}
	
	private boolean isAjax(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}
}
