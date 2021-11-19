package dloan.common.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Rest Api 호출
 */
public class RestCall {
	private static final Logger LOGGER = LoggerFactory.getLogger(RestCall.class);
	
	// time 설정
	private final static Integer READ_TIME = 5;
	private final static Integer TIME_OUT  = 5;
	
	/**
	 * Rest ful 호출 
	 * @param apiUrl
	 * @param httpMmethod
	 * @param header
	 * @param param
	 * @return
	 */
	public static String APICall(String apiUrl, String httpMmethod, Map<String, String> header, Map<String, String> param) {
		
		String ret = null;
		// 2019.05.08 소스코드 보안취약점 조치
		BufferedReader br = null;
		
		try {
			URL url = null;
			if ("GET".equalsIgnoreCase(httpMmethod)) {
				url = new URL(RestCall.getMapToString(apiUrl, param));
			} else {
				url = new URL(apiUrl);
			}
			
			LOGGER.debug("# apiUrl openConnection: " + apiUrl);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setConnectTimeout(TIME_OUT * 1000);
			con.setReadTimeout(READ_TIME * 1000);
			con.setRequestMethod(httpMmethod);
			
			// header
			if (header != null) {
				for (String key : header.keySet()) {
					con.setRequestProperty(key, (String) header.get(key));
				}
			}
			
			// 2019.05.07 소스코드 보안취약점 조치 : 불필요 코드 제거(모든 rest api는 get방식으로 사용)
			// post
			/*
			if (!"GET".equalsIgnoreCase(httpMmethod)) {
				con.setDoOutput(true);
				
				String temp = RestCall.getMapToString(StringUtils.EMPTY, param);
				if (!StringUtils.isEmpty(temp)) {
					temp = temp.substring(1);
				}
				
				if (!StringUtils.isEmpty(temp)) {
					OutputStream os = con.getOutputStream();
					os.write(temp.getBytes("UTF-8"));
					os.flush();
					os.close();
				}
			}
			*/
			
			// response
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			} else {  // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
			}
			
			StringBuffer response = new StringBuffer();
			
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			
			ret = response.toString();
		} catch (MalformedURLException e) {
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			if(br != null)
				try {
					br.close();
				} catch (IOException e) {
					LOGGER.error("buffer reader close error");
				}
		}
		LOGGER.debug("# ret ");
		return ret;
	}

	/**
	 * String json을 Map으로 변경
	 * @param jsonData
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonStringToMap(String jsonData) throws Exception {
		return new ObjectMapper().readValue(jsonData, Map.class);
	}
	
	/**
	 * map key value로 url로 변경
	 * @param url
	 * @param params
	 * @return
	 */
	public static String getMapToString(String url, Map<String, String> params) {
		String retVal = url;
		
		for (String key : params.keySet()){
			try {
				if (retVal.indexOf("?") == -1) {
					retVal += "?" + key + "=" + URLEncoder.encode(String.valueOf(params.get(key)), "UTF-8").replace("+", "%20");
				} else {
					retVal += "&" + key + "=" + URLEncoder.encode(String.valueOf(params.get(key)), "UTF-8").replace("+", "%20");
				}
			} catch (UnsupportedEncodingException e) {
				// 2019.05.07 소스코드 보안취약점 조치
				//e.printStackTrace();
				LOGGER.error("map to string error");
			}
		}
		System.out.println("retVal : "+retVal);
		return retVal;
	}
}
