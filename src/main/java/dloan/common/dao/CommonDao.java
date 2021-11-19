package dloan.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.google.gson.GsonBuilder;

import dloan.common.tags.paging.PageInfo;

@Repository("commonDao")
public class CommonDao {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonDao.class);
	
	@Resource(name="sqlSession")
    protected SqlSession sqlSession;
	
	public Object selectOne(String sqlid) {
		this.printSqlId(sqlid);
		return this.sqlSession.selectOne(sqlid);
	}
	
	public Object selectOne(String sqlid, Object param) {
		this.printSqlId(sqlid, param);
		return this.sqlSession.selectOne(sqlid, param);
	}
	
	public List<?> selectList(String sqlid) {
		this.printSqlId(sqlid);
		return this.sqlSession.selectList(sqlid);
	}
	
	public List<?> selectList(String sqlid, Object param) {
		this.printSqlId(sqlid, param);
		return this.sqlSession.selectList(sqlid, param);
	}
	
	public int insert(String sqlid) {
		this.printSqlId(sqlid);
		return this.sqlSession.insert(sqlid);
	}
	
	public int insert(String sqlid, Object param) {
		this.printSqlId(sqlid, param);
		return this.sqlSession.insert(sqlid, param);
	}
	
	public int update(String sqlid) {
		this.printSqlId(sqlid);
		return this.sqlSession.update(sqlid);
	}
	
	public int update(String sqlid, Object param) {
		this.printSqlId(sqlid, param);
		return this.sqlSession.update(sqlid, param);
	}
	
	public int delete(String sqlid) {
		this.printSqlId(sqlid);
		return this.sqlSession.delete(sqlid);
	}
	
	public int delete(String sqlid, Object param) {
		this.printSqlId(sqlid, param);
		return this.sqlSession.delete(sqlid, param);
	}
	
	private void printSqlId(String sqlid) {
		this.printSqlId(sqlid, null);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectPagingList(String sqlid, Object param) {
		
		this.printSqlId(sqlid, param);

		Map<String, Object> params = (Map<String, Object>)param;
		
		PageInfo pageInfo = new PageInfo();
		
		// 현재 페이지
		if (params.containsKey("start") == false || StringUtils.isEmpty((String) params.get("start")) == true) {
			pageInfo.setCurrentPageNo(1);
		} else {
			pageInfo.setCurrentPageNo(Integer.parseInt(params.get("start").toString()));
		}

		// 페이지당 레코드 갯수
		if (params.containsKey("display") == false || StringUtils.isEmpty((String) params.get("display")) == true) {
			pageInfo.setRecordCountPerPage(10);
		} else {
			pageInfo.setRecordCountPerPage(Integer.parseInt(params.get("display").toString()));
		}

		// 보여줄 페이지 사이즈
		pageInfo.setPageSize(10);
		
		int start = pageInfo.getFirstRecordIndex();
		int end   = start + pageInfo.getRecordCountPerPage();
		
		params.put("start", start + 1);
		params.put("end",   end);
		
		// 조회
		List<Map<String, Object>> list = sqlSession.selectList(sqlid, params);
		

		if (list.size() == 0) {
			pageInfo.setTotalRecordCount(0);
		} else {
			pageInfo.setTotalRecordCount(Integer.parseInt(list.get(0).get("totalCount").toString()));
		}
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("currentPage",    pageInfo.getCurrentPageNo());
		returnMap.put("recordsPerPage", pageInfo.getRecordCountPerPage());
		returnMap.put("resultList",     list);
		returnMap.put("pageInfo",       pageInfo);
		
		return returnMap;
	}
	
	private void printSqlId(String sqlid, Object param) {
		logger.debug("## SQL   ID    : " + sqlid);
		logger.debug("## Param Class : " + (param != null ? param.getClass().getName() : "null"));
		logger.debug("## Param Data  : " + (param != null ? new GsonBuilder().serializeNulls().create().toJson(param) : "null"));
	}
}
