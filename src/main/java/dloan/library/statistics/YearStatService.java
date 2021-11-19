package dloan.library.statistics;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dloan.common.dao.CommonDao;
import dloan.common.util.SessionUtils;
import dloan.common.util.ValidUtils;
import oracle.sql.CLOB;

@Service
public class YearStatService {

	private static final String NAME_SPACE = "library.statistics.";

	@Autowired
	protected CommonDao commonDao;

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectYearStat(Map<String, String> params) {
		return (List<Map<String, Object>>) commonDao.selectList(NAME_SPACE.concat("selectYearStatInfo"), params);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectMonthStat(Map<String, String> params) {
		
		if(Integer.parseInt(params.get("viewMonth"))<10) {
			params.put("date", params.get("reqYear")+"0"+params.get("viewMonth"));
		}else {
			params.put("date", params.get("reqYear")+params.get("viewMonth"));
		}
		
		return (List<Map<String, Object>>) commonDao.selectList(NAME_SPACE.concat("selectMonthStatInfo"), params);
	}

	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectUserStat(Map<String, String> params) throws Exception {
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		resultList = (List<Map<String, Object>>) commonDao.selectList(NAME_SPACE.concat("selectUserStatInfo"), params);
		
		for(int i=0; i<resultList.size(); i++) {
			String title = this.clobToString(resultList.get(i).get("title"));
			title=title.substring(1);
			Object title2=title;
			resultList.get(i).put("title",title2);
		}
				
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectBookStat(Map<String, String> params) {
		return (List<Map<String, Object>>) commonDao.selectList(NAME_SPACE.concat("selectBookStatInfo"), params);
	}
	
	@SuppressWarnings("deprecation")
	public String clobToString(Object object) throws Exception{
		StringBuffer s = new StringBuffer();
		BufferedReader br = new BufferedReader(((CLOB) object).getCharacterStream());
		String ts = "";
		while((ts=br.readLine())!=null) {
			s.append(ts+"\n");
		}
		br.close();
		return s.toString();
	}
}
