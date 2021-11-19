package dloan.library.envset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dloan.common.dao.CommonDao;
import dloan.common.util.SessionUtils;
import dloan.common.util.ValidUtils;

@Service
public class CancelReasonService {
	
	private static final String NAME_SPACE = "library.cancelreason.";

	@Autowired
	protected CommonDao commonDao;
	
	/**
	 * 거절사유 목록
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectCancelReason(Map<String, String> params) {
		return (List<Map<String, Object>>) commonDao.selectList(NAME_SPACE.concat("selectCancelReason"), params);
	}

	
	/**
	 * 저장
	 * @param params
	 * @return
	 */
	public Map<String, Object> saveCancelReason (List<String> ltReason, List<String> delList) {
		
		Map<String, Object> param = null;
		if (delList != null) {
			for (String recKey : delList) {
				param = new HashMap<String, Object>();
				param.put("recKey",  recKey);
				commonDao.delete(NAME_SPACE.concat("deleteCancelReason"), param);
			}
		}

		Map<String, Object> params = null;
		String[] keyVal = null;
		
		for (String reason : ltReason) {
			params = new HashMap<String, Object>();
			keyVal = reason.split("#@");
			params.put("userId",       SessionUtils.getLibId());
			params.put("recKey",       keyVal[0]);
			params.put("cancelReason", keyVal[1]);
			commonDao.update(NAME_SPACE.concat("saveCancelReason"), params);
		}

		return ValidUtils.resultSuccessMap();
	}
}
