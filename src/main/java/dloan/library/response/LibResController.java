package dloan.library.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dloan.common.CommonService;
import dloan.common.util.ExcelDown;
import dloan.common.util.SessionUtils;
import dloan.common.util.pdf.PdfDownload;


@Controller
@RequestMapping(value="/lib")
public class LibResController {
	Logger log = LoggerFactory.getLogger(LibResController.class);
	

	@Autowired
	private LibResService libResService;
	
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * 납품 조회
	 * 
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/libResponseInfo")
	public ModelAndView stroeResponseInfo(HttpServletRequest req, @RequestParam Map<String, String> params) throws Exception {
		ModelAndView mv = new ModelAndView("library/response/libResponse");

		// 2019/02/13 배분도서관 디폴트 -> 로그인도서관 
		if (params.get("libManageCode") == null) {
			params.put("libManageCode", SessionUtils.getLibMngCd());
		}
		
		mv.addAllObjects(params);
		
		mv.addObject("libList", commonService.selectLibrary(null));
		mv.addAllObjects(libResService.selectLibResponseInfo(params));

		return mv;
	}
	
	/**
	 * 납품 조회 - 엑셀 다운로드
	 * @param request
	 * @param response
	 * @param params
	 * @throws Exception
	 */
	@RequestMapping(value = "/libResponseInfoToExcel")
	public void libResponseInfoToExcel(HttpServletRequest request, HttpServletResponse response
			, @RequestParam Map<String, String> params) throws Exception {
		params.put("start", "1");
		params.put("display", "999999");
		
		Map<String, Object> data = this.libResService.selectLibResponseInfo(params);
		Map<String, Object> beans = new HashMap<String, Object>();
		
		beans.put("dataList", data.get("resultList"));
		
		ExcelDown ed = new ExcelDown();
		ed.download(request, response, beans, ed.getFilename("납품조회내역"), "libRes.xlsx");
	}
	
	/**
	 * 납품 요청 상세
	 * 
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/libResponseInfoDetail")
	public ModelAndView stroeResponseInfoDetail(HttpServletRequest req, @RequestParam Map<String, String> params) throws Exception {
		ModelAndView mv = new ModelAndView("library/response/libResponseDetail");

		
		mv.addAllObjects(params);
		
		mv.addObject("libList", commonService.selectLibrary(null));
		
		if (StringUtils.isNotEmpty(params.get("resKey"))) {
			mv.addAllObjects(libResService.selectReqeustList(params));
			mv.addAllObjects(libResService.getResopnseInfo(params));
		}

		return mv;
	}
	
	/**
	 * 납품 승인.
	 * @param ltRecKey
	 * @param ltResStatus
	 * @param resKey
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveLibApprove")
	public @ResponseBody Map<String, Object> saveLibApprove(
			@RequestParam(name="ltRecKey",    required=false)  List<String> ltRecKey,
			@RequestParam(name="ltResStatus", required=false)  List<String> ltResStatus,
			@RequestParam(name="ltReturnBookReason",required=false)  List<String>  ltReturnBookReason,
			@RequestParam(name="resKey",      required=false)  String resKey
			) throws Exception {
		
		return libResService.saveLibApprove(ltRecKey, ltResStatus, resKey,ltReturnBookReason);
	}
	
	/**
	 * 납품 요청.
	 * @param resKey
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveLibApproveBk")
	public @ResponseBody Map<String, Object> saveLibApproveBk(
			@RequestParam(name="resKey",      required=false)  String resKey
			) throws Exception {
		
		return libResService.saveLibApproveBk(resKey);
	}
	
	/**
	 * exceldown
	 * 
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/libResExcelDown")
	public void boardExcelDown(HttpServletRequest req,
			HttpServletResponse res,
			@RequestParam Map<String, String> params) throws Exception {
		
		// 데이터
		Map<String, Object> data   = (Map<String, Object>) libResService.selectReqeustList(params);
		Map<String , Object> beans = new HashMap<String , Object>();
		beans.put("dataList" , (List<Map<String, Object>>)data.get("resultList") );

		ExcelDown ed = new ExcelDown();
		ed.download(req, res, beans, ed.getFilename("납품요청내역"), "libResDetail.xlsx");
	}
	
	/**
	 * exceldown
	 * 
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/libResExcelDownload")
	public void responseExcelDownload(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> params) throws Exception {
		List<String> sheetInfoList = Arrays.asList(new String[] {"견   적   서", "납   품   서", "거 래 명 세 서"});
		List<String> sheetNameList = Arrays.asList(new String[] {"견적서", "납품서", "거래명세서"});
		
		List<Map<String, Object>> sheetDataList = new ArrayList<>();
		// 도서 목록
		List<Map<String, Object>> libResDataList = libResService.getLibResponseInfo(params);
		// 가격 셀서식이 적용안되어 정수형으로 변환
		for(Map<String, Object> libResData : libResDataList) {
			String price = (String) libResData.get("price");
			String realPrice = (String) libResData.get("realPrice");
			String cnt = (String) libResData.get("cnt");
			if(StringUtils.isNotEmpty(price)) {
				libResData.put("price", Integer.parseInt(price));
			}
			if(StringUtils.isNotEmpty(realPrice)) {
				libResData.put("realPrice", Integer.parseInt(realPrice));
			}
			if(StringUtils.isNotEmpty(cnt)) {
				libResData.put("cnt", Integer.parseInt(cnt));
			}
		}
		
		// 시트별로 입력할 데이터 추가
		Calendar calendar = Calendar.getInstance();
		for(String title : sheetInfoList) {
			Map<String, Object> sheetInfo = new HashMap<>();
			sheetInfo.put("mainTitle", title);
			sheetInfo.put("subTitle", title.replace(" ", ""));
			sheetInfo.put("year", calendar.get(Calendar.YEAR));
			sheetInfo.put("month", calendar.get(Calendar.MONTH) + 1);
			sheetInfo.put("day", calendar.get(Calendar.DAY_OF_MONTH));
			// res book data list
			sheetInfo.put("dataList", libResDataList);
			
			sheetDataList.add(sheetInfo);
		}
		// 서점정보 조회
		Map<String, Object> libResInfo = libResService.getResopnseInfo(params);
		String storeId = (String) libResInfo.get("storeId");
		String templateFileName = "libResDetail";
		// 서점별 템플릿 적용
		if(StringUtils.isNotEmpty(storeId)) {
			// nd_store >> Nd
			// libResDetail + Nd
			templateFileName += storeId.substring(0, 1).toUpperCase() + storeId.split("_")[0].substring(1);
		}
		
		ExcelDown ed = new ExcelDown();
		ed.multiSheetDownload("halla/".concat(templateFileName).concat(".xlsx"), sheetNameList, sheetDataList, request, response);
	}
	
	/**
	 * 거래명세서 PDF
	 * @param request
	 * @param response
	 * @param params
	 * @throws Exception
	 */
	@RequestMapping(value = "/libResPdfDownload")
	public void responsePdfDownload(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> params) throws Exception {
		// 페이지 title
		List<String> pageTitleList = Arrays.asList(new String[] {"견    적    서", "납    품    서", "거  래  명  세  서"});
		// 도서목록
		List<Map<String, Object>> libResDataList = libResService.getLibResponseInfo(params);
		
		PdfDownload pd = new PdfDownload();
		pd.createPdf(request, response, pageTitleList, libResDataList);
	}
	
}
