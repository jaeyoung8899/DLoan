package dloan.library.statistics;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dloan.common.util.ExcelDown;
import dloan.common.util.SessionUtils;
import dloan.library.envset.LibraryManageService;
import dloan.library.envset.StoreManageService;
import oracle.sql.CLOB;

@Controller
@RequestMapping(value="/lib/statistics")
public class YearStatController {
	
	@Autowired
	private YearStatService yearStatService;
	
	@Autowired
	private StoreManageService storeManageService;
	
	@Autowired
	private LibraryManageService libraryManageService;
	
	/**
	 * 년도별 도서 통계
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/byYearStat")
	public ModelAndView byYearStat(HttpServletRequest req, @RequestParam Map<String, String> params) {

		ModelAndView mv = new ModelAndView("library/statistics/byYearStat");
		List<Map<String, String>> storeList=this.storeManageService.selectStoreMng();

		mv.addObject    ("storeList" , storeList);
		mv.addObject    ("reqStoreId" , storeList.get(0).get("storeId"));
		mv.addObject    ("viewMonth" , "0");
		return mv;
	}
	
	@RequestMapping(value="/byYearStatInfo")
	public ModelAndView byYearStatInfo(HttpServletRequest req, @RequestParam Map<String, String> params) {

		ModelAndView mv = new ModelAndView("library/statistics/byYearStat");

		List<List<Map<String, Object>>> monthList = new ArrayList<List<Map<String, Object>>>();
				
		int next = Integer.parseInt(params.get("reqYear"))+1;
		params.put("next_year", Integer.toString(next));
		mv.addObject    ("resultList" , yearStatService.selectYearStat(params));
		mv.addObject    ("viewMonth" , params.get("viewMonth"));
		
		for(int i=1; i<13; i++) {
			params.put("viewMonth", ""+i);
			monthList.add    (yearStatService.selectMonthStat(params));
		}
		
		mv.addObject    ("monthsList" , monthList);
		mv.addObject    ("storeList" , this.storeManageService.selectStoreMng());
		mv.addObject    ("sortCol" , params.get("sortCol"));
		mv.addObject    ("order" , params.get("order"));
		mv.addObject    ("viewYear" , params.get("reqYear"));
		mv.addObject    ("reqStoreId" , params.get("reqStoreId"));
		
		return mv;
	}
	
	/**
	 * 신청 조회 - 엑셀 다운로드
	 * @param request
	 * @param response
	 * @param params
	 * @throws Exception
	 */
	@RequestMapping(value = "/yearStatInfoToExcel")
	public void yearStatInfoToExcel(HttpServletRequest request, HttpServletResponse response
			, @RequestParam Map<String, String> params) throws Exception {
		params.put("start", "1");
		params.put("display", "999999");
		
		int next = Integer.parseInt(params.get("reqYear"))+1;
		params.put("next_year", Integer.toString(next));
		
		List<Map<String, Object>> data = this.yearStatService.selectYearStat(params);
		int user=0;
		int count=0;
		int one=0;
		int two=0;
		int three=0;
		int four=0;
		int five=0;
		int six=0;
		int seven=0;
		int eight=0;
		int nine=0;
		int ten=0;
		int eleven=0;
		int twelve=0;
		for(int j=0; j<data.size(); j++) {
			user=user+1;
			count+=Integer.parseInt((String) data.get(j).get("count"));
			one+=Integer.parseInt((String) data.get(j).get("one"));
			two+=Integer.parseInt((String) data.get(j).get("two"));
			three+=Integer.parseInt((String) data.get(j).get("three"));
			four+=Integer.parseInt((String) data.get(j).get("four"));
			five+=Integer.parseInt((String) data.get(j).get("five"));
			six+=Integer.parseInt((String) data.get(j).get("six"));
			seven+=Integer.parseInt((String) data.get(j).get("seven"));
			eight+=Integer.parseInt((String) data.get(j).get("eight"));
			nine+=Integer.parseInt((String) data.get(j).get("nine"));
			ten+=Integer.parseInt((String) data.get(j).get("ten"));
			eleven+=Integer.parseInt((String) data.get(j).get("eleven"));
			twelve+=Integer.parseInt((String) data.get(j).get("twelve"));
		}
		Map<String, Object> dataIn = new HashMap<String,Object>();
		dataIn.put("rownum","Total");
		dataIn.put("name",user);
		dataIn.put("count",count);
		dataIn.put("one",one);
		dataIn.put("two",two);
		dataIn.put("three",three);
		dataIn.put("four",four);
		dataIn.put("five",five);
		dataIn.put("six",six);
		dataIn.put("seven",seven);
		dataIn.put("eight",eight);
		dataIn.put("nine",nine);
		dataIn.put("ten",ten);
		dataIn.put("eleven",eleven);
		dataIn.put("twelve",twelve);
		data.add(dataIn);
		
		Map<String, Object> beans = new HashMap<String, Object>();
		
		beans.put("dataList", data);
		
		
		
		for(int i=1; i<13; i++) {
			int user2=0;
			int count2=0;
			params.put("viewMonth", ""+i);
			List<Map<String, Object>> data2 = (yearStatService.selectMonthStat(params));
			Map<String, Object> dataIn2 = new HashMap<String,Object>();
			for(int j=0; j<data2.size(); j++) {
				user2=user2+1;
				count2+=Integer.parseInt((String) data2.get(j).get("count"));
			}
			
			dataIn2.put("rownum","Total");
			dataIn2.put("name",user2);
			dataIn2.put("count",count2);
			data2.add(dataIn2);
			
			beans.put("monthList"+i, data2);
		}
		
		
		  ExcelDown ed = new ExcelDown(); ed.download(request, response, beans,
		  ed.getFilename("바로대출신청통계내역"), "libStat.xlsx");
		 
	}
	
	
	
	@RequestMapping(value="/byUserStat")
	public ModelAndView byUserStat(HttpServletRequest req, @RequestParam Map<String, String> params) {

		ModelAndView mv = new ModelAndView("library/statistics/byUserStat");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date time = new Date();
		
		String now = format.format(time);
		String[] first = now.split("-");
		
		mv.addObject    ("from_reqDate" , first[0]+"-"+first[1]+"-01");
		mv.addObject    ("to_reqDate" , now);
		
		mv.addObject    ("storeList" , this.storeManageService.selectStoreMng());
		mv.addObject    ("libList"   , this.libraryManageService.selectLibrary());
		mv.addObject    ("reqLibManageCode" , SessionUtils.getLibMngCd());
		
		return mv;
	}
	
	@RequestMapping(value="/byUserStatInfo")
	public ModelAndView byUserStatInfo(HttpServletRequest req, @RequestParam Map<String, String> params) throws Exception {

		ModelAndView mv = new ModelAndView("library/statistics/byUserStat");

		mv.addObject    ("resultList" , yearStatService.selectUserStat(params));
		
		mv.addObject    ("storeList" , this.storeManageService.selectStoreMng());
		mv.addObject    ("libList"   , this.libraryManageService.selectLibrary());
		
		mv.addObject    ("sortCol" , params.get("sortCol"));
		mv.addObject    ("order" , params.get("order"));
		mv.addObject    ("reqStoreId" , params.get("reqStoreId"));
		mv.addObject    ("from_reqDate" , params.get("from_reqDate"));
		mv.addObject    ("to_reqDate" , params.get("to_reqDate"));
		mv.addObject    ("reqLibManageCode" , params.get("reqLibManageCode"));
		
		return mv;
	}
	
	
	@RequestMapping(value = "/userStatInfoToExcel")
	public void userStatInfoToExcel(HttpServletRequest request, HttpServletResponse response
			, @RequestParam Map<String, String> params) throws Exception {
		params.put("start", "1");
		params.put("display", "999999");
		
		List<Map<String, Object>> data = this.yearStatService.selectUserStat(params);
		int user=0;
		int count=0;
		for(int j=0; j<data.size(); j++) {
			user=user+1;
			count+=Integer.parseInt((String) data.get(j).get("count"));
		}
		Map<String, Object> dataIn = new HashMap<String,Object>();
		dataIn.put("rownum","Total");
		dataIn.put("name",user);
		dataIn.put("count",count);
		data.add(dataIn);
		
		Map<String, Object> beans = new HashMap<String, Object>();
		
		beans.put("dataList", data);
		
		  ExcelDown ed = new ExcelDown(); ed.download(request, response, beans,
		  ed.getFilename("바로대출신청이용자별통계내역"), "libUserStat.xlsx");
		 
	}
	
	
	
	@RequestMapping(value="/byBookStat")
	public ModelAndView byBookStat(HttpServletRequest req, @RequestParam Map<String, String> params) {

		ModelAndView mv = new ModelAndView("library/statistics/byBookStat");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date time = new Date();
		
		String now = format.format(time);
		String[] first = now.split("-");
		
		mv.addObject    ("from_reqDate" , first[0]+"-"+first[1]+"-01");
		mv.addObject    ("to_reqDate" , now);
		
		mv.addObject    ("storeList" , this.storeManageService.selectStoreMng());
		mv.addObject    ("libList"   , this.libraryManageService.selectLibrary());
		mv.addObject    ("reqLibManageCode" , SessionUtils.getLibMngCd());
		return mv;
	}
	
	@RequestMapping(value="/byBookStatInfo")
	public ModelAndView byBookStatInfo(HttpServletRequest req, @RequestParam Map<String, String> params) {

		ModelAndView mv = new ModelAndView("library/statistics/byBookStat");

		mv.addObject    ("resultList" , yearStatService.selectBookStat(params));
		
		mv.addObject    ("storeList" , this.storeManageService.selectStoreMng());
		mv.addObject    ("libList"   , this.libraryManageService.selectLibrary());
		
		mv.addObject    ("sortCol" , params.get("sortCol"));
		mv.addObject    ("order" , params.get("order"));
		mv.addObject    ("reqStoreId" , params.get("reqStoreId"));
		mv.addObject    ("from_reqDate" , params.get("from_reqDate"));
		mv.addObject    ("to_reqDate" , params.get("to_reqDate"));
		mv.addObject    ("reqLibManageCode" , params.get("reqLibManageCode"));
		
		return mv;
	}
	
	
	@RequestMapping(value = "/bookStatInfoToExcel")
	public void bookStatInfoToExcel(HttpServletRequest request, HttpServletResponse response
			, @RequestParam Map<String, String> params) throws Exception {
		params.put("start", "1");
		params.put("display", "999999");
		
		List<Map<String, Object>> data = this.yearStatService.selectBookStat(params);
		int book=0;
		int count=0;
		for(int j=0; j<data.size(); j++) {
			book=book+1;
			count+=Integer.parseInt((String) data.get(j).get("count"));
		}
		Map<String, Object> dataIn = new HashMap<String,Object>();
		dataIn.put("rownum","Total");
		dataIn.put("title",book);
		dataIn.put("count",count);
		data.add(dataIn);
		
		Map<String, Object> beans = new HashMap<String, Object>();
		
		beans.put("dataList", data);
		
		  ExcelDown ed = new ExcelDown(); ed.download(request, response, beans,
		  ed.getFilename("바로대출신청도서별통계내역"), "libBookStat.xlsx");
		 
	}
	
	
	
}
