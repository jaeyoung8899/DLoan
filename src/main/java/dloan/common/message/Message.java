package dloan.common.message;

import java.util.Locale;

import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * 메시지 객체
 * [src/message_ko.properties]
 * 
 */
public class Message {
	/** 스프링 관련 Resource 객체 */
	private ResourceBundleMessageSource messageSource;

	/** 
	 * 스프링 관련 Resource 객체
	 * @param messageSource 스프링 관련 Resource 객체
	 */
	public void setMessageSource(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	/** 
	 * Returns [파일:message_ko.properties]key에 해당하는 Value
	 * @param  key message_ko.properties파일의 키값
	 * @return key에 해당하는 Value
	 */
	public String getMessage(String key){
		String temp = null;
		temp = messageSource.getMessage(key, null, null, Locale.KOREAN);
		// 2019.05.08 소스코드 보안취약점 조치
		return temp == null ? "" : temp.trim();
	}
}