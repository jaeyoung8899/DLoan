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
public class LimitBookService {
	
	private static final String NAME_SPACE = "library.limitbook.";

	@Autowired
	protected CommonDao commonDao;
	
	/**
	 * limit book 목록
	 * @param params
	 * @return
	 */
	public Map<String, Object> selectLimitBook(Map<String, String> params) {
		return (Map<String, Object>) commonDao.selectPagingList(NAME_SPACE.concat("selectLimitBook"), params);
	}
	
	/**
	 * 제외도서 삭제.
	 * @param delList
	 * @return
	 */
	public Map<String, Object> deleteLimitBook(List<String> delList) {
		Map<String, Object> param = null;
		if (delList != null) {
			for (int i = 0; i < delList.size(); i++) {
				param = new HashMap<String, Object>();
				param.put("isbn",  delList.get(i));

				commonDao.delete(NAME_SPACE.concat("deleteLimitBook"), param);
			}
		}
		return ValidUtils.resultSuccessMap();
	}

	
	/**
	 * 저장
	 * @param params
	 * @return
	 */
	public Map<String, Object> saveLimitBook (Map<String, Object> params) {
		Map<String, Object> retMap = ValidUtils.requiredMap(params, 
				new String[] {"isbn"}, 
				new String[] {"ISBN"});

		if (!retMap.isEmpty()) {
			return retMap;
		}
		
		params.put("userId",   SessionUtils.getLibId());
		commonDao.update(NAME_SPACE.concat("saveLimitBook"), params);
		
		return ValidUtils.resultSuccessMap();
	}
	
	/**
	 * isbn check
	 * @param params
	 * @return
	 */
	public Map<String, Object> getISBNCount (Map<String, Object> params) {
		
		Map<String, Object> retMap = ValidUtils.requiredMap(params, 
				new String[] {"isbn"}, 
				new String[] {"ISBN"});

		if (!retMap.isEmpty()) {
			return retMap;
		}
		
		int isbnCnt = (Integer)commonDao.selectOne(NAME_SPACE.concat("getISBNCount"), params);
		
		retMap.put("isbnCnt", isbnCnt);
		retMap.putAll(ValidUtils.resultSuccessMap());
		
		return retMap;
	}
}
