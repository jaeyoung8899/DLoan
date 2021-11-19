package dloan.common.tags.paging;

import java.text.MessageFormat;

public class PagingRenderer {

	public String firstUl;
	public String firstPageLabel;
	public String previousPageLabel;
	public String currentPageLabel;
	public String otherPageLabel;
	public String nextPageLabel;
	public String lastPageLabel;
	public String lastUl;
	
	// 생성자.
	public PagingRenderer() {
		firstUl           = "<ul class=\"pagination\">";
		firstPageLabel    = "<li><a href=\"javascript:;\" onclick=\"{0}({1}); return false;\">&lt;&lt;</a></li>";
		previousPageLabel = "<li><a href=\"javascript:;\" onclick=\"{0}({1}); return false;\">&lt;</a></li>";
		currentPageLabel  = "<li class=\"active\"><a href=\"javascript:;\">{0}</a></li>";
		otherPageLabel    = "<li><a href=\"javascript:;\" onclick=\"{0}({1}); return false;\">{2}</a></li>";
		nextPageLabel     = "<li><a href=\"javascript:;\" onclick=\"{0}({1}); return false;\">&gt;</a></li>";
		lastPageLabel     = "<li><a href=\"javascript:;\" onclick=\"{0}({1}); return false;\">&gt;&gt;</a></li>";
		lastUl            = "</ul>";
	}

	public String renderPagination(PageInfo pageInfo, String jsFunction) {
		
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
