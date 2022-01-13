package dloan.library.envset;

import dloan.common.dao.CommonDao;
import dloan.common.util.ValidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoreReqManageService {
	
	private static final String NAME_SPACE = "library.store.reqmanage.";

	@Autowired
	protected CommonDao commonDao;
	
	/**
	 * 서점목록
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectStore() {
		return (List<Map<String, String>>) this.commonDao.selectList(NAME_SPACE.concat("selectStore"));
	}
	
	/**
	 * 서점 신청제한 업데이트 서비스
	 * 
	 * @param params
	 * @return
	 */
	public Map<String, Object> updateStoreLimit(Map<String, Object> params) {
		String storeId     = (String) params.get("storeId");	// 도서관 관리구분
		String limitYN     = (String) params.get("limitYN");	// 신청제한여부(Y:제한, N:제한하지않음)
		String listYN      = (String) params.get("listYN");		// 목록표시제한여부(Y:목록에서표시하지않음, N:목록에서표시)
		String limitDate1  = (String) params.get("limitDate1");	// 신청제한 시작일
		String limitDate2  = (String) params.get("limitDate2");	// 신청제한 종료일
		String limitReason = (String) params.get("limitReason");// 신청제한 문구
		
		String[] storeIdArr     = storeId.split(",");
		String[] limitYNArr     = limitYN.split(",");
		String[] listYNArr      = listYN.split(",");
		String[] limitDate1Arr  = limitDate1.split(",");
		String[] limitDate2Arr  = limitDate2.split(",");
		String[] limitReasonArr = limitReason.split(",");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		for(int i = 0, max = storeIdArr.length;i < max;i++) {
			paramMap.clear();
			paramMap.put("storeId", storeIdArr[i]);
			paramMap.put("limitYN", limitYNArr[i]);
			paramMap.put("listYN", listYNArr[i]);
			paramMap.put("limitDate1", limitDate1Arr[i].equals("NO DATE") ? null : limitDate1Arr[i].trim());
			paramMap.put("limitDate2", limitDate2Arr[i].equals("NO DATE") ? null : limitDate2Arr[i].trim());
			paramMap.put("limitReason", limitReasonArr[i].equals("NO MSG") ? null : limitReasonArr[i].trim());
			// update
			this.commonDao.update(NAME_SPACE.concat("updateStoreLimit"), paramMap);
		}
		
		return ValidUtils.resultSuccessMap();
	}
}
