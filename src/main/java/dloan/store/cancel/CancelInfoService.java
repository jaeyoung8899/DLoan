package dloan.store.cancel;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dloan.common.dao.CommonDao;

@Service
public class CancelInfoService {
	
	private static final String NAME_SPACE = "store.cancel.";
	
	@Autowired
	protected CommonDao commonDao;

	public Map<String, Object> selectCancelInfo(Map<String, String> params) {
		return (Map<String, Object>) commonDao.selectPagingList(NAME_SPACE.concat("selectCancelInfo"), params);
	}
}
