package com.fung.jdbc.common;

import java.util.List;

/**
 * 说明：实现通用分页查询实体类
 * @author fung
 * @version 1.0
 */
public class PageBean<T> {
	private int totalRows; // 总记录数
	private int totalPages; // 总页数
	private int curPage; // 当前页码
	private int prePage; // 上一页页码
	private int nextPage; // 下一页页码
	private int rowsPerPage; // 每页显示的记录数 
	private List<T> pageList; // 当前页的数据集
	
    /**
     * 初始化分页bean对象
     * @param totalRows 总记录数
     * @param rowsPerPage 每页显示的记录数
     */
	public void initPageBean(int totalRows, int rowsPerPage){
		this.rowsPerPage = rowsPerPage;
		this.totalRows = totalRows;
		this.totalPages = (this.totalRows-1)/this.rowsPerPage + 1;
		// 设置上一页
		if(this.curPage == 1){
			this.setPrePage(1);
		}else{
			this.setPrePage(this.curPage-1);
		}
		// 设置下一页
		if(this.curPage == this.totalPages){
			this.setNextPage(this.totalPages);
		}else{
			this.setNextPage(this.curPage + 1);
		}
	}
	
	/**
	 * 生成SQLServer的分页查询语句
	 * @param sql 原始sql语句
	 * @param curPage 第几页
	 * @param rowsPerPage 每页多少行 
	 */
	public String getPageSQLServer(String sql, int curPage, int rowsPerPage){
		String afterFrom = sql.toLowerCase().substring(sql.indexOf("from"));
		String pageSql = null;
		if(afterFrom.indexOf("where") == -1){
			 pageSql = "select top "+ rowsPerPage + " * "+afterFrom
			+" where id not in(select top "+rowsPerPage*(curPage-1)+" id "
			+afterFrom+" order by id desc)"+"order by id desc";
		}else{
			pageSql = "select top "+ rowsPerPage + " * "+afterFrom
			+" and id not in(select top "+rowsPerPage*(curPage-1)+" id "
			+afterFrom+" order by id desc)"+"order by id desc";
		}
		return pageSql;
	}
	
	/**
	 * 生成MySql分页sql语句
	 * @param sql 原始sql语句
	 * @param curPage 第几页
	 * @param rowsPerPage 每页多少行 
	 * @return 返回分页SQL语句
	 */
	public String getPageMySQL(String sql, int curPage, int rowsPerPage){
		String pageSql = sql+" limit "+ (curPage-1)*rowsPerPage+","+rowsPerPage;
		return pageSql;
	}
	
	/**
	 * 生成Oracle分页查询语句
	 * @param sql 原始sql语句
	 * @return 返回分页SQL语句
	 */
	public String getOrclPageSql(String sql){
		int begin = (curPage - 1) * rowsPerPage;
		int end = begin + rowsPerPage;
		StringBuffer pagingSelect = new StringBuffer(300);
		pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) row_ where rownum <= "+end+") where rownum_ > "+begin);
		return pagingSelect.toString();
	}
	
	public List<T> getPageList() {
		return pageList;
	}

	public void setPageList(List<T> pageList) {
		this.pageList = pageList;
	}
	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPrePage() {
		return prePage;
	}

	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public int getRowsPerPage() {
		return rowsPerPage;
	}

	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}
}