package dloan.library.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dloan.common.dao.CommonDao;
import dloan.common.util.ValidUtils;

@Service
public class LibResService {
	
	private static final String NAME_SPACE = "lib.response.";

	@Autowired
	protected CommonDao commonDao;
	
	public Map<String, Object> selectLibResponseInfo(Map<String, String> params) {	
		return (Map<String, Object>) commonDao.selectPagingList(NAME_SPACE.concat("selectLibResponseInfo"), params);
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
	
	

	public Map<String, Object> saveLibApprove(List<String> ltRecKey,
			List<String> ltResStatus, String resKey, List<String>  ltReturnBookReason) {
		
		Map<String, Object> retMap = ValidUtils.resultSuccessMap();
		
		for (int i = 0; i < ltResStatus.size(); i++) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("recKey",    ltRecKey.get(i));
			params.put("resStatus", ltResStatus.get(i));
			params.put("resRemark", ltReturnBookReason.get(i));
			commonDao.update(NAME_SPACE.concat("updateLibReqResponsStatus"), params);
		}

		Map<String, Object> resParam = new HashMap<String, Object>();
		resParam.put("recKey",    resKey);
		resParam.put("resStatus", "L01");
		commonDao.update(NAME_SPACE.concat("updateLibResponsStatus"), resParam);
		
		return retMap;
	}
	
	public Map<String, Object> saveLibApproveBk(String resKey) {
		
		Map<String, Object> retMap = ValidUtils.resultSuccessMap();
		
		Map<String, Object> resParam = new HashMap<String, Object>();
		resParam.put("recKey",    resKey);
		resParam.put("resStatus", "S01");
		commonDao.update(NAME_SPACE.concat("updateLibResponsStatus"), resParam);
		
		return retMap;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getLibResponseInfo(Map<String, String> params) {
		return (List<Map<String, Object>>) commonDao.selectList(NAME_SPACE.concat("getLibResponseInfo"), params);
	}
	
}
