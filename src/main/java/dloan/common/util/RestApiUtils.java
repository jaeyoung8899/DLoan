package dloan.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RestApiUtils {

	private int    naverApiIdx;
	private int    naverApiCnt;
	private String naverApiKeyList[];
	private String naverApiSecretList[];
	
	@Value("#{conf['naver_api_url']}")
	private String naverApiUrl;
	
	private int    aladinApiIdx;
	private String aladinApiTtbkeyList[];
	
	@Value("#{conf['aladin_api_url']}")
	private String aladinApiUrl;
	
	public RestApiUtils(
			@Value("#{conf['aladin_api_ttbkey']}") String aladinApiTtbkey
			) {
		String naverApiKey = System.getProperty("naver_api_key");
		String naverApiSecret = System.getProperty("naver_api_secret");

		this.naverApiIdx        = -1;
		this.naverApiKeyList    = naverApiKey.split(",");
		this.naverApiSecretList = naverApiSecret.split(",");
		this.naverApiCnt        = this.naverApiKeyList.length;
		
		this.aladinApiIdx        = -1;
		this.aladinApiTtbkeyList = aladinApiTtbkey.split(",");
	}
	
	public String getNaverApiUrl() {
		return this.naverApiUrl;
	}
	
	public int getNaverApiCnt() {
		return this.naverApiCnt;
	}
	
	public String[] getNaverApiKey() {
		this.naverApiIdx++;
		if (this.naverApiIdx >= this.naverApiKeyList.length) {
			this.naverApiIdx = 0;
		}
		return new String[] { this.naverApiKeyList[this.naverApiIdx], this.naverApiSecretList[this.naverApiIdx] };
	}
	
	public String getAladinApiUrl() {
		return this.aladinApiUrl;
	}
	
	public String getAladinApiTtbkey() {
		this.aladinApiIdx++;
		if (this.aladinApiIdx >= this.aladinApiTtbkeyList.length-1) {
			this.aladinApiIdx = 0;
		}
		return this.aladinApiTtbkeyList[this.aladinApiIdx];
	}
}
