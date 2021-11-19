package dloan.store.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import dloan.common.dao.CommonDao;
import dloan.common.util.SessionUtils;
import dloan.common.util.ValidUtils;

@Service
public class StoreResService {

	private static final String NAME_SPACE = "store.response.";

	@Autowired
	protected CommonDao commonDao;
	
	public Map<String, Object> selectStoreResponseInfo(Map<String, String> params) {	
		return (Map<String, Object>) commonDao.selectPagingList(NAME_SPACE.concat("selectStoreResponseInfo"), params);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getResopnseInfo(Map<String, String> params) {	
		return (Map<String, Object>) commonDao.selectOne(NAME_SPACE.concat("getResopnseInfo"), params);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectReqeustList(Map<String, String> params) {	
		
		Map<String, Object> retMap     = ValidUtils.resultSuccessMap();
		List<Map<String, Object>> list = (List<Map<String, Object>>) commonDao.selectList(NAME_SPACE.concat("selectRequestList"), params);
		
		retMap.put("resultList", list);
		
		return retMap;
	}
	
	public Map<String, Object> saveStoreResponse(List<String> ltRecKey, String resKey, String resTitle, String libManageCode, String resStatus, List<String> ltDelKey) {
		
		Map<String, Object> retMap = ValidUtils.resultSuccessMap();
		
		Map<String, Object> params = new HashMap<String, Object>();
		if (ltDelKey != null && ltDelKey.size() > 0) {
			params.put("arRecKey",  ltDelKey);
			params.put("resStatus", null);
			params.put("resKey",    null);
			
			// 삭제
			commonDao.update(NAME_SPACE.concat("updateRequestResKey"), params);
			params.clear();
		}

		params.put("storeId",       SessionUtils.getStoreId());
		params.put("resTitle",      resTitle);
		params.put("libManageCode", libManageCode);
		
		if (StringUtils.isEmpty(resKey)) {
			commonDao.insert(NAME_SPACE.concat("insertResponseInfo"), params);
		} else {
			params.put("resKey", resKey);
			commonDao.update(NAME_SPACE.concat("updateResponseInfo"), params);
		}
		
		Map<String, Object> reqParam = new HashMap<String, Object>();
		reqParam.put("arRecKey",  ltRecKey);
		reqParam.put("resStatus", resStatus);
		reqParam.put("resKey",    params.get("resKey"));
		
		commonDao.update(NAME_SPACE.concat("updateRequestResKey"), reqParam);
		
		retMap.put("resKey", params.get("resKey"));
		
		return retMap;
	}
	
	/**
	 * 납품 완료
	 * @param reqStatus
	 * @param ltRecKey
	 * @return
	 */
	public Map<String, Object> updateStoreResStatusComplate(String resStatus, List<String> ltRecKey) {
		for (String recKey : ltRecKey) {
			Map <String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("recKey",    recKey);
			reqMap.put("resStatus", resStatus);
			
			commonDao.update(NAME_SPACE.concat("updateStoreResponsStatus"), reqMap);
		}
		return ValidUtils.resultSuccessMap();
	}
}
