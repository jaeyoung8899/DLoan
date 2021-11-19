package dloan.common.tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("serial")
public class CodeListTag extends TagSupport {
	
	private List<Map<String, Object>> list; 
	private String select; 
	private String type; 
	
	public int doEndTag() throws JspException {
		try {
			
			List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
			
			if (list == null) {
				return SKIP_BODY;
			}
			
			if (list.size() > 0) {
				retList = list;
			}

			String codeContents = randererCode(retList, type, select);

			JspWriter out = pageContext.getOut();

			// 랜더
			out.println(codeContents);

			return SKIP_BODY;

		} catch (IOException e) {
			throw new JspException();
		}
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String randererCode(List<Map<String, Object>> codeList, String type, String select) {
		
		StringBuffer strBuff = new StringBuffer();
		if ("A".equals(type)) {
			strBuff.append("<option value=''>전체</option>\n");
		} else if ("S".equals(type)) {
			strBuff.append("<option value=''>선택</option>\n");
		}
		
		String selCode = null;
		String code    = null;
		String value   = null;
		
		for (Map<String, Object> codeMap : codeList) {
			
			if (codeMap.get("code") == null) {
				code = "";
			} else {
				code    = String.valueOf(codeMap.get("code"));
				selCode = String.valueOf(codeMap.get("code"));
			}
			if (codeMap.get("value") == null) {
				value = "";
			} else {
				value = String.valueOf(codeMap.get("value"));
			}
			
			if (StringUtils.isNotEmpty(select)) {
				if (select.equals(selCode)) {
					strBuff.append("<option value="+code+" selected>"+value+"</option>\n");
				} else {
					strBuff.append("<option value="+code+">"+value+"</option>\n");
				}
			} else {
				strBuff.append("<option value="+code+">"+value+"</option>\n");
			}
		} 
		
		return strBuff.toString();
	}

}
