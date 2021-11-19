package dloan.library.envset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dloan.common.dao.CommonDao;
import dloan.common.util.ValidUtils;

@Service
public class ConfManageService {
	
	private static final String NAME_SPACE = "library.confmng.";

	@Autowired
	protected CommonDao commonDao;
	
	/**
	 * 기타설정목록
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectConfMng() {
		return (List<Map<String, String>>) this.commonDao.selectList(NAME_SPACE.concat("selectConfMng"));
	}
	
	/**
	 * 기타설정수정
	 * 
	 * @param params
	 * @return
	 */
	public Map<String, Object> updateStoreMng(List<String> ltConfId, List<String> ltConfValue) {
		
		Map<String, String> params = null;
		for (int i = 0; i < ltConfId.size(); i++) {
			params = new HashMap<String, String>();
			params.put("confId",     ltConfId.get(i));
			params.put("confValue",  ltConfValue.get(i));
			this.commonDao.update(NAME_SPACE.concat("updateConfMng"), params);
		}
		return ValidUtils.resultSuccessMap();
	}
	
}
