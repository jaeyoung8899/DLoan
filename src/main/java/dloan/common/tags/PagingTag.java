package dloan.common.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import dloan.common.tags.paging.PageInfo;
import dloan.common.tags.paging.PagingRendererHome;
import dloan.common.tags.paging.PagingRenderer;

@SuppressWarnings("serial")
public class PagingTag extends TagSupport {

	private Object pageInfo;
	private String jsFunction;
	private String pagingType;

	public int doEndTag() throws JspException {
		try {

			JspWriter out = pageContext.getOut();
			String contents = "";
			
			// 랜더
			if ("Home".equals(pagingType)) {
				
				PagingRendererHome pagingRenderer = new PagingRendererHome();
				PageInfo pInfo = null;
				if (pageInfo instanceof PageInfo) {
					pInfo = (PageInfo) pageInfo;
				}
				contents = pagingRenderer.renderPagination(pInfo, jsFunction);
			} else {

				PagingRenderer pagingRenderer = new PagingRenderer();
				PageInfo pInfo = null;
				if (pageInfo instanceof PageInfo) {
					pInfo = (PageInfo) pageInfo;
				}
				contents = pagingRenderer.renderPagination(pInfo, jsFunction);
			}
			
			out.println(contents);

			return EVAL_PAGE;

		} catch (IOException e) {
			throw new JspException();
		}
	}

	public void setPageInfo(Object pageInfo) {
		this.pageInfo = pageInfo;
	}

	public void setJsFunction(String jsFunction) {
		this.jsFunction = jsFunction;
	}
	
	public void setPagingType(String pagingType) {
		this.pagingType = pagingType;
	}
}
