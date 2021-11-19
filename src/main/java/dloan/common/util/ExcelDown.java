package dloan.common.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

public class ExcelDown {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelDown.class);

	public ExcelDown() {
	}

	public String getFilename() {
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddmmmm");
		return ft.format(new Date());
	}

	public String getFilename(String pre) {
		return pre.concat("_").concat(getFilename());
	}

	
	/**
	 * Make Excel & Download.
	 * WEB-INF/template/ 경로에 있는 파일정보를 읽어서 바인딩한다.
	 * @throws Exception 
	 */
	public void download(HttpServletRequest req, HttpServletResponse res, Map<String, Object> beans, String fileName, String templateFile) {
		
		String tempPath = req.getSession().getServletContext().getRealPath("/WEB-INF/template");
		LOGGER.debug(tempPath);
		String userAgent = req.getHeader("User-Agent");
		LOGGER.debug(userAgent);
		InputStream is = null;
		OutputStream os = null;
		Workbook resultWorkbook = null;
		try {
			if (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("Chrome")) {
				fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
			} else {
				fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
			LOGGER.debug(fileName);
			LOGGER.debug(templateFile);
			// 2019.05.08 소스코드 보안취약점 조치
			is = new BufferedInputStream(new FileInputStream(tempPath + "/" + templateFile));
			XLSTransformer transformer = new XLSTransformer();
			resultWorkbook = transformer.transformXLS(is, beans);
			res.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".xlsx\"");
			os = res.getOutputStream();
			resultWorkbook.write(os);
			/*
			InputStream is = new BufferedInputStream(new FileInputStream(tempPath + "\\" + templateFile));
			InputStream is = new BufferedInputStream(new FileInputStream(tempPath + "\\" + templateFile));
			XLSTransformer transformer = new XLSTransformer();
			Workbook resultWorkbook = transformer.transformXLS(is, beans);
			res.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".xlsx\"");
			OutputStream os = res.getOutputStream();
			resultWorkbook.write(os);
			*/
		} catch (ParsePropertyException | InvalidFormatException | IOException ex) {
			LOGGER.error("MakeExcel");
		} finally {
			try {
				if(is != null)	is.close();
				if(resultWorkbook != null)	resultWorkbook.close();
				if(os != null)	os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOGGER.error("input stream close error");
			}
		}
	}
	
	/**
	 * Excel Multi Sheet Download
	 * @param response
	 * @param dataInfo
	 * @param templateFileName
	 */
	public void multiSheetDownload(String templateFileName, List<String> sheetNames, List<Map<String, Object>> dataList
			, HttpServletRequest request, HttpServletResponse response) {
		
		OutputStream os = null;
		InputStream is = null;

		String tempPath = request.getSession().getServletContext().getRealPath("/WEB-INF/template");
		
		try {
			String fileName = this.getFilename("거래명세서");
			
			try {
				is = new BufferedInputStream(new FileInputStream(tempPath + "/" + templateFileName));
			} catch (FileNotFoundException e) {
				LOGGER.debug("파일 찾을 수 없음");
				is = new BufferedInputStream(new FileInputStream(tempPath + "/halla/libResDetail.xlsx"));
			}
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" +  URLEncoder.encode(fileName, "UTF-8") + ".xlsx");//한글은 인코딩 필요

			os = response.getOutputStream();

			XLSTransformer transformer = new XLSTransformer();
			
			 Workbook excel = transformer.transformXLS(is, dataList.get(0));
			//Workbook excel = transformer.transformMultipleSheetsList(is, dataList, sheetNames, "data", new HashMap<>(), 0);
			
			excel.write(os);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
