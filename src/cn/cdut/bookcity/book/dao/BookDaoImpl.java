package cn.cdut.bookcity.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.cdut.bookcity.book.domain.BookBean;
import cn.cdut.bookcity.book.domain.BookQueryForm;
import cn.cdut.bookcity.category.domain.CategoryBean;
import cn.cdut.bookcity.expression.Expression;
import cn.cdut.bookcity.pager.PageBean;
import cn.cdut.bookcity.pager.PageConstants;
import cn.cdut.util.common.CommonUtils;
import cn.cdut.util.jdbc.TxQueryRunner;

/**
 * Book模块持久层
 * @author huangyong
 * @date 2018年4月26日 下午11:02:31
 */
public class BookDaoImpl {
	
	private static TxQueryRunner qr = new TxQueryRunner();
	
	/**
	 * 将Map集合中的数据封装到BookBean中 
	 * @param map
	 * @return BookBean   返回类型
	 */
	private BookBean mapToBookBean(Map<String, Object> map) {
		if(map == null || map.size() == 0) {
			return null;
		}
		BookBean book = CommonUtils.map2Bean(map, BookBean.class);
		CategoryBean category = new CategoryBean();
		category.setCid((String) map.get("cid"));
		book.setCategory(category);
		return book;
	}
	
	/**
	 * 通过书id进行查询 
	 * @param bid 书id
	 * @param pc 当前页数
	 * @throws SQLException 
	 * @return PageBean<BookBean>   返回类型
	 */
	public BookBean findByBookId(String bid) throws SQLException{
		String sql = "SELECT * FROM t_book WHERE bid=?";
		Map<String, Object> map = qr.query(sql, new MapHandler(), bid);
		return mapToBookBean(map);
	}
	
	/**
	 * 通用查询方法,多条件组合查询，所有条件都需满足
	 * @param criterion 表达式的集合
	 * @param pc 当前需要查询的页数
	 * @return PageBean<Book>   返回类型
	 * @throws SQLException 
	 */
	private PageBean<BookBean> queryByCriteria(List<Expression> criterion, int pc) throws SQLException{
		/*
		 * 1、设置分页大小
		 * 2、根据条件查询得到记录数
		 * 3、根据条件查询得到List<Book>
		 * 4、参数pc即为当前页
		 */
		//设置分页大小
		int pageSize = PageConstants.BOOK_PAGE_SIZE;
		
		/*
		 * where子句拼接,例如：
		 *  WHERE 1=1 AND id = ? AND name = ? AND age is not null
		 */
		StringBuilder whereSql = new StringBuilder(" WHERE 1=1");
		List<Object> params = new ArrayList<Object>();
		for(Expression exp : criterion){
			whereSql.append(" ").append("AND").append(" ").append(exp.getName()).
					 append(" ").append(exp.getOperator());
			if(exp.getValue() != null && exp.getValue().trim() != "" ){
				whereSql.append(" ").append("?");
				params.add(exp.getValue());
			}
		}
		
		//查询所有记录数
		String sql = "SELECT count(*) FROM t_book" + whereSql.toString();
		Number number = (Number) qr.query(sql, new ScalarHandler(), params.toArray());
		int totalRecords = number.intValue();
		
		//按条件查询
		sql = "SELECT * FROM t_book" + whereSql.toString() + " ORDER BY orderBy LIMIT ?,?";
		//从第(pc - 1) * pageSize记录开始查，共查pageSize条记录
		//mysql中第一条记录是从0开始
		params.add((pc - 1) * pageSize);
		params.add(pageSize);
		List<BookBean> bookList = qr.query(sql, new BeanListHandler<BookBean>(BookBean.class), params.toArray());
		
		//创建PageBean设置参数,其中url参数交给控制层设置
		PageBean<BookBean> pageBean =  new PageBean<BookBean>();
		pageBean.setPageCode(pc);
		pageBean.setPageSize(pageSize);
		pageBean.setTotalRecords(totalRecords);
		pageBean.setBeanList(bookList);
		
		return pageBean;
	}
	
	/**
	 * 根据所属分类cid查询 
	 * @param cid 分类id
	 * @param pc 当前页数
	 * @throws SQLException 
	 * @return PageBean<Book>   返回类型
	 */
	public PageBean<BookBean> findByCategory(String cid, int pc) throws SQLException{
		List<Expression> criterion = new ArrayList<Expression>();
		criterion.add(new Expression("cid", "=", cid));
		return queryByCriteria(criterion, pc);
	}
	
	/**
	 * 通过出版社模糊查询
	 * @param press 出版社名
	 * @param pc 当前页数
	 * @throws SQLException 
	 * @return PageBean<BookBean>   返回类型
	 */
	public PageBean<BookBean> findByPress(String press, int pc) throws SQLException{
		List<Expression> criterion = new ArrayList<Expression>();
		criterion.add(new Expression("press", "LIKE", "%" + press + "%"));
		return queryByCriteria(criterion, pc);
	}
	
	/**
	 * 通过作者进行模糊查询 
	 * @param author
	 * @param pc
	 * @return PageBean<BookBean>   返回类型
	 * @throws SQLException 
	 */
	public PageBean<BookBean> findByAuthor(String author, int pc) throws SQLException{
		List<Expression> criterion = new ArrayList<Expression>();
		criterion.add(new Expression("author", "LIKE", "%" + author + "%"));
		return queryByCriteria(criterion, pc);
	}
	
	/**
	 * 通过书名模糊查询
	 * @param bname 书名
	 * @param pc 当前页数
	 * @throws SQLException 
	 * @return PageBean<Book>   返回类型
	 */
	public PageBean<BookBean> findByBookname(String bname, int pc) throws SQLException{
		List<Expression> criterion = new ArrayList<Expression>();
		criterion.add(new Expression("bname", "LIKE", "%" + bname + "%"));
		return queryByCriteria(criterion, pc);
	}
	
	/**
	 * 通过书名、作者、出版社 组合模糊查询
	 * @param form 封装书名、作者、出版社的表单类
	 * @param pc 当前页数
	 * @throws SQLException 
	 * @return PageBean<BookBean>   返回类型
	 */
	public PageBean<BookBean> findByCombination(BookQueryForm form, int pc) throws SQLException{
		List<Expression> criterion = new ArrayList<Expression>();
		criterion.add(new Expression("bname", "LIKE", "%" + form.getBname() + "%"));
		criterion.add(new Expression("author", "LIKE", "%" + form.getAuthor() + "%"));
		criterion.add(new Expression("press", "LIKE", "%" + form.getPress() + "%"));
		return queryByCriteria(criterion, pc);
	}

	/**
	 * 按分类id查询其包含的书个数 
	 * @param cid
	 * @return int   返回类型
	 * @throws SQLException 
	 */
	public int findBookNumByCid(String cid) throws SQLException {
		String sql = "SELECT count(*) FROM t_book WHERE cid=?";
		Number count = (Number) qr.query(sql, new ScalarHandler(), cid);
		return count.intValue();
 	}

	/**
	 * 添加图书
	 * @param newBook 
	 * @return void   返回类型
	 * @throws SQLException 
	 */
	public void addBook(BookBean newBook) throws SQLException {
		String sql = "INSERT INTO t_book(bid,bname,author,price,currPrice,discount,"
				+ "press,publishtime,edition,pageNum,wordNum,printtime,booksize,"
				+ "paper,cid,image_w,image_b) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = {newBook.getBid(), newBook.getBname(), newBook.getAuthor(),
				newBook.getPrice(), newBook.getCurrPrice(), newBook.getDiscount(),
				newBook.getPress(), newBook.getPublishtime(), newBook.getEdition(),
				newBook.getPageNum(), newBook.getWordNum(), newBook.getPrinttime(),
				newBook.getBooksize(), newBook.getPaper(), newBook.getCategory().getCid(),
				newBook.getImage_w(), newBook.getImage_b()};
		qr.update(sql, params);
	}

	/**
	 * 修改图书
	 * @param book 
	 * @return void   返回类型
	 * @throws SQLException 
	 */
	public void updateBook(BookBean book) throws SQLException {
		String sql = "UPDATE t_book SET bname=?,author=?,price=?,currPrice=?,"
				+ "discount=?,press=?,publishtime=?,edition=?,pageNum=?,"
				+ "wordNum=?,printtime=?,booksize=?,paper=?,cid=? WHERE bid=?";
		Object[] params = {book.getBname(), book.getAuthor(),book.getPrice(), 
				book.getCurrPrice(), book.getDiscount(), book.getPress(), 
				book.getPublishtime(), book.getEdition(), book.getPageNum(),
				book.getWordNum(), book.getPrinttime(),	book.getBooksize(),
				book.getPaper(), book.getCategory().getCid(), book.getBid()};
		qr.update(sql,params);
	}

	/**
	 * 删除图书
	 * @param bid 
	 * @return void   返回类型
	 * @throws SQLException 
	 */
	public void deleteBook(String bid) throws SQLException {
		String sql = "DELETE FROM t_book WHERE bid=?";
		qr.update(sql, bid);
	}
}
