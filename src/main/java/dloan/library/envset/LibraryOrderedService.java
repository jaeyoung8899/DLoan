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
public class LibraryOrderedService {
	
	private static final String NAME_SPACE = "library.ordered.";

	@Autowired
	protected CommonDao commonDao;
	
	/**
	 * 서점 목록
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectStoreInfo(Map<String, String> params) {
		return (List<Map<String, String>>) commonDao.selectList(NAME_SPACE.concat("selectStoreInfo"), params);
	}
	
	/**
	 * 도서관 배분 조회
	 * 
	 * @param param
	 * @return
	 */
	public Map<String, Object> selectLibraryOrderInfo(Map<String, String> params) {
		
		// 필수 체크 
		Map<String, Object> retMap = ValidUtils.requiredMap(params, 
				new String[] {"storeId"}, 
				new String[] {"서점"});
		
		if (!retMap.isEmpty()) {
			return retMap;
		}
		
		retMap.put("storeList", commonDao.selectList(NAME_SPACE.concat("selectLibraryOrderInfo"), params));
		retMap.put("storeNonList", commonDao.selectList(NAME_SPACE.concat("selectNonLibraryOrderInfo"), params));
		retMap.putAll(ValidUtils.resultSuccessMap());
		
		return retMap;
	}
	
	/**
	 * 저장
	 * @param lib
	 * @param order
	 * @param store
	 */
	public void saveLibOrdered (List<String> lib, List<String> order, String storeId) {
		
		Map<String, Object> params = null;
		commonDao.delete(NAME_SPACE.concat("deleteLibOrdered"), storeId);
		
		for (int i = 0; i < lib.size(); i++) {
			params = new HashMap<String, Object>();
			params.put("storeId",       storeId);
			params.put("libManageCode", lib.get(i));
			params.put("orderPriority", (i+1));
			params.put("userId",        SessionUtils.getLibId());
			
			commonDao.update(NAME_SPACE.concat("saveLibOrdered"), params);
		}
	}
}
