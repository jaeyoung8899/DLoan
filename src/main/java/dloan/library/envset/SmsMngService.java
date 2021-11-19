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
public class SmsMngService {
	
	private static final String NAME_SPACE = "library.smsmng.";

	@Autowired
	protected CommonDao commonDao;
	
	/**
	 * smsmng 목록
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectSmsMng(Map<String, String> params) {
		return (List<Map<String, String>>) commonDao.selectList(NAME_SPACE.concat("selectSmsMng"), params);
	}
	

	
	/**
	 * 저장
	 * @param delRecKey
	 * @param reckey
	 * @param name
	 * @param contents
	 * @return
	 */
	public Map<String, Object> saveSmsMng (List<String> delRecKey, List<String> reckey,
			List<String> name, List<String> contents) {
		
		// 삭제
		Map<String, Object> params = null;
		if (delRecKey != null) {
			for (int i = 0; i < delRecKey.size(); i++) {
				params = new HashMap<String, Object>();
				params.put("recKey",  delRecKey.get(i));

				commonDao.delete(NAME_SPACE.concat("deleteSmsMng"), params);
			}
		}

		// 등록/ 수정
		for (int j = 0; j < reckey.size(); j++) {
			params = new HashMap<String, Object>();
			params.put("userId",   SessionUtils.getLibId());
			params.put("name",     name.get(j));
			params.put("contents", contents.get(j));
			params.put("recKey",   reckey.get(j));
			commonDao.update(NAME_SPACE.concat("saveSmsMng"), params);
		}
		
		return ValidUtils.resultSuccessMap();
	}
}
