package dloan.common.tags.paging;

import java.text.MessageFormat;

public class PagingRendererHome {

	public String firstUl;
	public String firstPageLabel;
	public String previousPageLabel;
	public String currentPageLabel;
	public String otherPageLabel;
	public String nextPageLabel;
	public String lastPageLabel;
	public String lastUl;
	
	// 생성자.
	public PagingRendererHome() {
		firstUl           = "<div class=\"board_navi\">";
		firstPageLabel    = "<a href=\"javascript:;\" onclick=\"{0}({1}); return false;\"><img src=\"../resources/user/images/btn_page_first.gif\" alt=\"첫페이지으로\"></a>";
		previousPageLabel = "<a href=\"javascript:;\" onclick=\"{0}({1}); return false;\"><img src=\"../resources/user//images/btn_page_prev.gif\" alt=\"이전페이지로\"></a>";
		currentPageLabel  = "<a href=\"javascript:;\" class=\"b\">{0}</a>";
		otherPageLabel    = "<a href=\"javascript:;\" onclick=\"{0}({1}); return false;\">{2}</a>";
		nextPageLabel     = "<a href=\"javascript:;\" onclick=\"{0}({1}); return false;\"><img src=\"../resources/user//images/btn_page_next.gif\" alt=\"다음페이지로\"></a>";
		lastPageLabel     = "<a href=\"javascript:;\" onclick=\"{0}({1}); return false;\"><img src=\"../resources/user//images/btn_page_last.gif\" alt=\"마지막페이지으로\"></a>";
		lastUl            = "</div>";
	}
	

	public String renderPagination(PageInfo	 pageInfo, String jsFunction) {
		
		if (pageInfo == null) {
			return null;
		}

		StringBuffer strBuff = new StringBuffer();
		strBuff.append(firstUl);
		
		int firstPageNo           = pageInfo.getFirstPageNo();
		int firstPageNoOnPageList = pageInfo.getFirstPageNoOnPageList();
		int totalPageCount        = pageInfo.getTotalPageCount();
		int pageSize              = pageInfo.getPageSize();
		int lastPageNoOnPageList  = pageInfo.getLastPageNoOnPageList();
		int currentPageNo         = pageInfo.getCurrentPageNo();
		int lastPageNo            = pageInfo.getLastPageNo();
		
		if (totalPageCount > pageSize) {
			if (firstPageNoOnPageList > pageSize) {
				strBuff.append(MessageFormat.format(firstPageLabel,    new Object[]{jsFunction,Integer.toString(firstPageNo)}));
				strBuff.append(MessageFormat.format(previousPageLabel, new Object[]{jsFunction,Integer.toString(firstPageNoOnPageList-1)}));
			} else {
				strBuff.append(MessageFormat.format(firstPageLabel,    new Object[]{jsFunction,Integer.toString(firstPageNo)}));
				strBuff.append(MessageFormat.format(previousPageLabel ,new Object[]{jsFunction,Integer.toString(firstPageNo)}));
			}
		}
		
		for (int i = firstPageNoOnPageList; i <= lastPageNoOnPageList; i++) {
			if (i == currentPageNo) {
				strBuff.append(MessageFormat.format(currentPageLabel, new Object[]{Integer.toString(i)}));
			} else {
				strBuff.append(MessageFormat.format(otherPageLabel,   new Object[]{jsFunction,Integer.toString(i), Integer.toString(i)}));
			}
		}

		if (totalPageCount > pageSize) {
			if (lastPageNoOnPageList < totalPageCount) {
				strBuff.append(MessageFormat.format(nextPageLabel, new Object[]{jsFunction, Integer.toString(firstPageNoOnPageList+pageSize)}));
				strBuff.append(MessageFormat.format(lastPageLabel, new Object[]{jsFunction, Integer.toString(lastPageNo)}));
			} else {
				strBuff.append(MessageFormat.format(nextPageLabel, new Object[]{jsFunction, Integer.toString(lastPageNo)}));
				strBuff.append(MessageFormat.format(lastPageLabel, new Object[]{jsFunction, Integer.toString(lastPageNo)}));
			}
		}
		strBuff.append(lastUl);
		return strBuff.toString();
	}
}
