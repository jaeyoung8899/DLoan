package dloan.common.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

public class DownloadView extends AbstractView {
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadView.class);
			
	public DownloadView() {
		setContentType("application/download; charset=utf-8");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model
			, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String _path    = (String) model.get("filePath");
		String _name    = (String) model.get("fileName");
		String _orgName = (String) model.get("orgFileName");

		if (_name == null || _name.equals("")) {
			return;
		}
		File file = new File(_path + "/" + _name);

		response.setContentType(getContentType());
		response.setContentLength((int) file.length());

		String userAgent = request.getHeader("User-Agent");
		//boolean ie = userAgent.indexOf("MSIE") > -1;
		String fileName = _orgName;

		// 파일 인코딩
		if (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("Chrome")) {
			fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		}

		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		OutputStream out = response.getOutputStream();

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, out);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException ioe) {
					LOGGER.error("파일 다운로드 오류");
				}
			}
		}
		out.flush();
	}
}
