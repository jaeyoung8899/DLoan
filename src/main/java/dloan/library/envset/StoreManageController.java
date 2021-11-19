package dloan.library.envset;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dloan.common.util.SessionUtils;

@Controller
@RequestMapping(value="/lib/envset")
public class StoreManageController {
	
	@Autowired
	private StoreManageService storeManageService;
	
	/**
	 * 서점관리 페이지
	 * 
	 * @param res
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/storeMng")
	public ModelAndView libMng(HttpServletResponse res) throws IOException {
		if (!StringUtils.isEmpty(SessionUtils.getLibMngCd())) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} else {
			ModelAndView mav = new ModelAndView("library/envset/storeMng");
			mav.addObject("store", this.storeManageService.selectStoreMng());
			return mav;
		}
	}
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/pwFailCountResetStore")
	public @ResponseBody Map<String, Object> pwFailCountResetStore(
			@RequestParam Map<String, String> params) {
		
		return storeManageService.pwFailCountResetStore(params);
	}
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/updateStorePasswordReset")
	public @ResponseBody Map<String, Object> passwordChange(
			@RequestParam Map<String, String> params) {
		
		return storeManageService.updateStorePassword(params);
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value="/updateStoreMng")
	public @ResponseBody Map<String, Object> updateLibrary(
			@RequestParam(value="ltStoreId")    List<String> ltStoreId,
			@RequestParam(value="ltStorePhone") List<String> ltStorePhone,
			@RequestParam(value="ltHandphone")  List<String> ltHandphone,
			@RequestParam(value="ltLimitPrice") List<String> ltLimitPrice
			) {
		return storeManageService.updateStoreMng(ltStoreId, ltStorePhone, ltHandphone, ltLimitPrice);
	}
	
	/**
	 * 서점MAC 조회
	 * 
	 * @param storeId
	 * @return
	 */
	@RequestMapping(value="/selectStoreMAC")
	public @ResponseBody List<String> selectStoreMAC(@RequestParam(value="storeId") String storeId) {
		return this.storeManageService.selectStoreMAC(storeId);
	}
	
	/**
	 * 서점MAC 변경
	 * 
	 * @param storeId
	 * @param ltStoreMac
	 * @return
	 */
	@RequestMapping(value="/updateStoreMAC")
	public @ResponseBody Map<String, Object> updateStoreMAC(
			@RequestParam(value="storeId")    String storeId,
			@RequestParam(value="ltStoreMac") List<String> ltStoreMac
			) {
		return storeManageService.updateStoreMAC(storeId, ltStoreMac);
	}

	/**
	 * 서점 전화번호 조회
	 */
	@RequestMapping(value = "/selectStorePhone")
	public @ResponseBody List<String> selectStorePhone(@RequestParam(value = "storeId") String storeId) {
		return storeManageService.selectStorePhone(storeId);
	}

	/**
	 * 서점전화번호 저장
	 */
	@RequestMapping(value = "/updateStorePhone")
	public @ResponseBody Map<String,Object> updateStorePhone(
			@RequestParam(value = "storeId") String storeId,
			@RequestParam(value = "storePhoneList") List<String> storePhoneList) {
		return storeManageService.updateStorePhone(storeId,storePhoneList);
	}
}
	                                              