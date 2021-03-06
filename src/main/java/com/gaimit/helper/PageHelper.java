package com.gaimit.helper;

public class PageHelper {
	/***** GET??Όλ―Έν°λ‘? μ²λ¦¬?  κ°? *****/
    private int page = 1; 			// ??¬ ??΄μ§? λ²νΈ

    /***** DB?? μ‘°ν? κ²°κ³Ό κ°? *****/
    private int totalCount = 0;		// ? μ²? κΈ? κ°μ μ‘°ν

    /***** κ°λ°?κ°? ? ??΄?Ό ?? κ°? *****/
    private int listCount = 10;		// ? ??΄μ§?? λ³΄μ¬μ§? κΈ?? λͺ©λ‘ ?
    private int groupCount = 5;		// ? κ·Έλ£Ή? ???  ??΄μ§?λ²νΈ κ°μ

	/***** ?°?°μ²λ¦¬κ°? ??? κ°? *****/
	private int totalPage = 0;		// ? μ²? ??΄μ§? ?
    private int startPage = 0;		// ??¬ κ·Έλ£Ή? ?? ??΄μ§? 
    private int endPage = 0;		// ??¬ κ·Έλ£Ή? λ§μ?λ§? ??΄μ§?
    private int prevPage = 0;		// ?΄?  κ·Έλ£Ή? λ§μ?λ§? ??΄μ§?
    private int nextPage = 0;		// ?΄?  κ·Έλ£Ή? μ²? ??΄μ§?
    private int limitStart = 0;		// MySQL? Limit ?? ?μΉ?
    
    //λ¦¬μ€?Έ ?ΈμΆμ κ·? λ¦¬μ€?Έ? index κ°?
    private int indexStart = 0;
    private int indexLast = 0;
	
	/** ??΄μ§? κ΅¬ν? ??? κ³μ°?? μ²λ¦¬?? λ©μ? */
	public void pageProcess(int page, int totalCount, int listCount, int groupCount) {
		this.page = page;
		this.totalCount = totalCount;
		this.listCount = listCount;
		this.groupCount = groupCount;
		
		indexStart = ((page - 1)*listCount)+1;
		indexLast = totalCount-((page -1)*listCount);

		// ? μ²? ??΄μ§? ?
	    totalPage = ((totalCount-1)/listCount)+1;

	    // ??¬ ??΄μ§?? ??? ?€μ°? μ‘°μ 
	    if (page < 0) {
	    	page = 1;
	    }
	    
	    if (page > totalPage) {
	    	page = totalPage;
	    }

	    // ??¬ ??΄μ§? κ·Έλ£Ή? ?? ??΄μ§? λ²νΈ
	    startPage = ((page - 1) / groupCount) * groupCount + 1;

	    // ??¬ ??΄μ§? κ·Έλ£Ή? ? ??΄μ§? λ²νΈ
	    endPage = (((startPage - 1) + groupCount) / groupCount) * groupCount;

	    // ? ??΄μ§? λ²νΈκ°? ? μ²? ??΄μ§??λ₯? μ΄κ³Ό?λ©? ?€μ°¨λ²? μ‘°μ 
	    if (endPage > totalPage) {
	    	endPage = totalPage;
	    }

	    // ?΄?  κ·Έλ£Ή? λ§μ?λ§? ??΄μ§?
	    if (startPage > groupCount) {
	        prevPage = startPage - 1;
	    } else {
	    	prevPage = 0;
	    }

	    // ?€? κ·Έλ£Ή? μ²? ??΄μ§?
	    if (endPage < totalPage) {
	        nextPage = endPage + 1;
	    } else {
	    	nextPage = 0;
	    }

	    // κ²?? λ²μ? ?? ?μΉ?
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
