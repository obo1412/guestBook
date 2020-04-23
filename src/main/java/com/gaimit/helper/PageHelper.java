package com.gaimit.helper;

public class PageHelper {
	/***** GET?ŒŒ?¼ë¯¸í„°ë¡? ì²˜ë¦¬?•  ê°? *****/
    private int page = 1; 			// ?˜„?¬ ?˜?´ì§? ë²ˆí˜¸

    /***** DB?—?„œ ì¡°íšŒ?•œ ê²°ê³¼ ê°? *****/
    private int totalCount = 0;		// ? „ì²? ê¸? ê°œìˆ˜ ì¡°íšŒ

    /***** ê°œë°œ?ê°? ? •?˜?•´?•¼ ?•˜?Š” ê°? *****/
    private int listCount = 10;		// ?•œ ?˜?´ì§??— ë³´ì—¬ì§? ê¸??˜ ëª©ë¡ ?ˆ˜
    private int groupCount = 5;		// ?•œ ê·¸ë£¹?— ?‘œ?‹œ?•  ?˜?´ì§?ë²ˆí˜¸ ê°œìˆ˜

	/***** ?—°?‚°ì²˜ë¦¬ê°? ?•„?š”?•œ ê°? *****/
	private int totalPage = 0;		// ? „ì²? ?˜?´ì§? ?ˆ˜
    private int startPage = 0;		// ?˜„?¬ ê·¸ë£¹?˜ ?‹œ?‘ ?˜?´ì§? 
    private int endPage = 0;		// ?˜„?¬ ê·¸ë£¹?˜ ë§ˆì?ë§? ?˜?´ì§?
    private int prevPage = 0;		// ?´? „ ê·¸ë£¹?˜ ë§ˆì?ë§? ?˜?´ì§?
    private int nextPage = 0;		// ?´? „ ê·¸ë£¹?˜ ì²? ?˜?´ì§?
    private int limitStart = 0;		// MySQL?˜ Limit ?‹œ?‘ ?œ„ì¹?
    
    //ë¦¬ìŠ¤?Š¸ ?˜¸ì¶œì‹œ ê·? ë¦¬ìŠ¤?Š¸?˜ index ê°?
    private int indexStart = 0;
    private int indexLast = 0;
	
	/** ?˜?´ì§? êµ¬í˜„?— ?•„?š”?•œ ê³„ì‚°?‹?„ ì²˜ë¦¬?•˜?Š” ë©”ì„œ?“œ */
	public void pageProcess(int page, int totalCount, int listCount, int groupCount) {
		this.page = page;
		this.totalCount = totalCount;
		this.listCount = listCount;
		this.groupCount = groupCount;
		
		indexStart = ((page - 1)*listCount)+1;
		indexLast = totalCount-((page -1)*listCount);

		// ? „ì²? ?˜?´ì§? ?ˆ˜
	    totalPage = ((totalCount-1)/listCount)+1;

	    // ?˜„?¬ ?˜?´ì§??— ???•œ ?˜¤ì°? ì¡°ì ˆ
	    if (page < 0) {
	    	page = 1;
	    }
	    
	    if (page > totalPage) {
	    	page = totalPage;
	    }

	    // ?˜„?¬ ?˜?´ì§? ê·¸ë£¹?˜ ?‹œ?‘ ?˜?´ì§? ë²ˆí˜¸
	    startPage = ((page - 1) / groupCount) * groupCount + 1;

	    // ?˜„?¬ ?˜?´ì§? ê·¸ë£¹?˜ ? ?˜?´ì§? ë²ˆí˜¸
	    endPage = (((startPage - 1) + groupCount) / groupCount) * groupCount;

	    // ? ?˜?´ì§? ë²ˆí˜¸ê°? ? „ì²? ?˜?´ì§??ˆ˜ë¥? ì´ˆê³¼?•˜ë©? ?˜¤ì°¨ë²”?œ„ ì¡°ì ˆ
	    if (endPage > totalPage) {
	    	endPage = totalPage;
	    }

	    // ?´? „ ê·¸ë£¹?˜ ë§ˆì?ë§? ?˜?´ì§?
	    if (startPage > groupCount) {
	        prevPage = startPage - 1;
	    } else {
	    	prevPage = 0;
	    }

	    // ?‹¤?Œ ê·¸ë£¹?˜ ì²? ?˜?´ì§?
	    if (endPage < totalPage) {
	        nextPage = endPage + 1;
	    } else {
	    	nextPage = 0;
	    }

	    // ê²??ƒ‰ ë²”ìœ„?˜ ?‹œ?‘ ?œ„ì¹?
	    limitStart = (page-1) * listCount;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getListCount() {
		return listCount;
	}

	public void setListCount(int listCount) {
		this.listCount = listCount;
	}

	public int getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getPrevPage() {
		return prevPage;
	}

	public void setPrevPage(int prevPage) {
		this.prevPage = prevPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public int getLimitStart() {
		return limitStart;
	}

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	public int getIndexStart() {
		return indexStart;
	}

	public void setIndexStart(int indexStart) {
		this.indexStart = indexStart;
	}

	public int getIndexLast() {
		return indexLast;
	}

	public void setIndexLast(int indexLast) {
		this.indexLast = indexLast;
	}

	@Override
	public String toString() {
		return "PageHelper [page=" + page + ", totalCount=" + totalCount + ", listCount=" + listCount + ", groupCount="
				+ groupCount + ", totalPage=" + totalPage + ", startPage=" + startPage + ", endPage=" + endPage
				+ ", prevPage=" + prevPage + ", nextPage=" + nextPage + ", limitStart=" + limitStart + ", indexStart="
				+ indexStart + ", indexLast=" + indexLast + "]";
	}

	
}
