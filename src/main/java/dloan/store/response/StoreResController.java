package dloan.store.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dloan.common.CommonService;
import dloan.common.util.ExcelDown;
import dloan.common.util.SessionUtils;

@Controller
@RequestMapping(value="/store")
public class StoreResController {

	@Autowired
	private StoreResService storeResService;
	
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * 납품 조회
	 * 
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/storeResponseInfo")
	public ModelAndView stroeResponseInfo(HttpServletRequest req, @RequestParam Map<String, String> params) throws Exception {
		ModelAndView mv = new ModelAndView("store/response/storeResponse");

		params.put("storeId", SessionUtils.getStoreId());
		mv.addAllObjects(params);
		mv.addObject("storeYn", SessionUtils.getStoreYn());
		mv.addObject("libList", commonService.selectLibrary(params));
		mv.addAllObjects(storeResService.selectStoreResponseInfo(params));

		return mv;
	}
	
	/**
	 * 납품 요청 상세
	 * 
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/stroeResponseInfoDetail")
	public ModelAndView stroeResponseInfoDetail(HttpServletRequest req, @RequestParam Map<String, String> params) throws Exception {
		ModelAndView mv = new ModelAndView("store/response/storeResponseDetail");

		params.put("storeId", SessionUtils.getStoreId());
		mv.addAllObjects(params);
		
		mv.addObject("libList", commonService.selectLibrary(null));
		mv.addObject("storeYn", SessionUtils.getStoreYn());
		if (StringUtils.isNotEmpty(params.get("resKey"))) {
			mv.addAllObjects(storeResService.selectReqeustList(params));
			mv.addAllObjects(storeResService.getResopnseInfo(params));
		}

		return mv;
	}
	
	/**
	 * 요청 정보
	 * 
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/selectRequestList")
	public @ResponseBody Map<String, Object> selectRequestList(HttpServletRequest req, @RequestParam Map<String, String> params) throws Exception {

		params.put("storeId", SessionUtils.getStoreId());
		return storeResService.selectReqeustList(params);
	}
	
	/**
	 * 납품 요청
	 * @param ltRecKey
	 * @param resKey
	 * @param resTitle
	 * @param libManageCode
	 * @param resStatus
	 * @param ltDelKey
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveStoreResponse")
	public @ResponseBody Map<String, Object> saveStoreResponse(
			@RequestParam(name="ltRecKey",      required=false)  List<String> ltRecKey,
			@RequestParam(name="resKey",        required=false)  String resKey,
			@RequestParam(name="resTitle",      required=false)  String resTitle,
			@RequestParam(name="libManageCode", required=false)  String libManageCode,
			@RequestParam(name="resStatus",     required=false)  String resStatus,
			@RequestParam(name="ltDelKey",      required=false)  List<String> ltDelKey 
			) throws Exception {
		
		return storeResService.saveStoreResponse(ltRecKey, resKey, resTitle, libManageCode, resStatus, ltDelKey);
	}
	
	
	/**
	 * 납품 완료
	 * @param ltRecKey
	 * @param ltReqStatus
	 * @return
	 */
	@RequestMapping(value="/updateStoreResStatus")
	public @ResponseBody Map<String, Object> updateStoreResStatus (
			@RequestParam(name="resStatus") String resStatus,
			@RequestParam(name="ltRecKey")  List<String> ltRecKey) {
		
		return storeResService.updateStoreResStatusComplate(resStatus, ltRecKey);
	}
	
	/**
	 * 납품 요청 내역
	 * 
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/storeDeliveryReqInfo")
	public ModelAndView stroeResponseInfoList(HttpServletRequest req, @RequestParam Map<String, String> params) throws Exception {
		ModelAndView mv = new ModelAndView("store/response/storeDeliveryReqInfo");

		params.put("storeId", SessionUtils.getStoreId());
		mv.addAllObjects(params);
		
		List<Map<String, Object>> libList = (List<Map<String, Object>>)commonService.selectLibrary(params);

		mv.addObject("libList", libList);
		
		if (params.containsKey("libManageCode") == false || StringUtils.isEmpty((String) params.get("libManageCode"))) {
			if (libList.size() > 0) {
				params.put("libManageCode", (String)libList.get(0).get("code"));
			}
		}
		mv.addObject("storeYn", SessionUtils.getStoreYn());
		mv.addAllObjects(storeResService.selectReqeustList(params));

		return mv;
	}
	
	/**
	 * 납품요청내역 - 상세정보 - 엑셀 다운로드
	 * 
	 * @param req
	 * @param res
	 * @param params
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/storeResInfoDetailToExcel")
	public void boardExcelDown(HttpServletRequest req,
			HttpServletResponse res,
			@RequestParam Map<String, String> params) throws Exception {

		params.put("storeId", SessionUtils.getStoreId());
		// 데이터
		Map<String, Object> data   = (Map<String, Object>) storeResService.selectReqeustList(params);
		Map<String, Object> beans = new HashMap<String , Object>();
		beans.put("dataList" , (List<Map<String, Object>>)data.get("resultList") );

		ExcelDown ed = new ExcelDown();
		ed.download(req, res, beans, ed.getFilename("납품요청내역_상세"), "storeResDetail.xlsx");
	}
	
	/**
	 * 납품요청내역 - 엑셀 다운로드
	 * 
	 * @param request
	 * @param response
	 * @param params
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/storeResponseInfoToExcel")
	public void responseInfoToExcel(HttpServletRequest req, HttpServletResponse res,
			@RequestParam Map<String, String> params) throws Exception {
		
		params.put("storeId", SessionUtils.getStoreId());
		params.put("start", "1");
		params.put("display", "999999");
		
		Map<String, Object> beans = new HashMap<String, Object>();
		Map<String, Object> data = storeResService.selectStoreResponseInfo(params);
		beans.put("dataList", (List<Map<String, Object>>) data.get("resultList"));
		
		ExcelDown ed = new ExcelDown();
		ed.download(req, res, beans, ed.getFilename("납품요청내역"), "storeResList.xlsx");
	}
	
	/**
	 * 납품요청 - 엑셀 다운로드
	 * @param request 
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/storeDeliveryReqInfoToExcel")
	public void stroeResponseInfoListToExcel(HttpServletRequest req, HttpServletResponse res,
			@RequestParam Map<String, String> params) throws Exception {
		
		params.put("storeId", SessionUtils.getStoreId());
		
		List<Map<String, Object>> libList = (List<Map<String, Object>>)commonService.selectLibrary(params);

		if(params.containsKey("libManageCode") == false || StringUtils.isEmpty((String) params.get("libManageCode"))) {
			if (libList.size() > 0) {
				params.put("libManageCode", (String)libList.get(0).get("code"));
			}
		}
		
		Map<String, Object> data = storeResService.selectReqeustList(params);
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("dataList", (List<Map<String, Object>>) data.get("resultList"));
		
		ExcelDown ed = new ExcelDown();
		ed.download(req, res, beans, ed.getFilename("납품요청"), "storeDeliveryInfo.xlsx");
	}
}