package dloan.library.sms;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dloan.common.dao.CommonDao;

/**
 * SMS이력조회 서비스
 * @author WB
 */
@Service
public class SmsService {
	
	private static final String NAME_SPACE = "library.sms.";
	
	@Autowired
	private CommonDao commonDao;
	
	public Map<String, Object> smsList(Map<String, String> params) {
		return commonDao.selectPagingList(NAME_SPACE.concat("selectSms"), params);
	}
}