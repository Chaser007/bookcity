package cn.cdut.bookcity.book.service;

import java.sql.SQLException;

import cn.cdut.bookcity.book.dao.BookDaoImpl;
import cn.cdut.bookcity.book.domain.BookBean;
import cn.cdut.bookcity.book.domain.BookQueryForm;
import cn.cdut.bookcity.pager.PageBean;

/**
 * Book模块服务层
 * @author huangyong
 * @date 2018年4月26日 下午11:03:36
 */
public class BookServiceImpl {
	private static BookDaoImpl bookDao  = new BookDaoImpl();
	
	/**
	 * 通过分类查询服务
	 * @param cid 分类id
	 * @param pc 当前页数
	 * @return PageBean<Book>   返回类型
	 */
	public PageBean<BookBean> findByCategory(String cid, int pc){
		try {
			return bookDao.findByCategory(cid, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过书名模糊查询
	 * @param bname 书名
	 * @param pc 当前页数
	 * @return PageBean<Book>   返回类型
	 */
	public PageBean<BookBean> findByBookname(String bname, int pc){
		try {
			return bookDao.findByBookname(bname, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 通过作者查找图书 
	 * @param author
	 * @param pc
	 * @return PageBean<BookBean>   返回类型
	 */
	public PageBean<BookBean> findByAuthor(String author, int pc){
		try{
			return bookDao.findByAuthor(author, pc);
		} catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 通过出版社查询 
	 * @param press
	 * @param pc
	 * @return PageBean<BookBean>   返回类型
	 */
	public PageBean<BookBean> findByPress(String press, int pc){
		try {
			return bookDao.findByPress(press, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 高级搜索,组合查询 
	 * @param form
	 * @param pc
	 * @return PageBean<BookBean>   返回类型
	 */
	public PageBean<BookBean> findByCombination(BookQueryForm form , int pc){
		try {
			return bookDao.findByCombination(form, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过id查询 
	 * @param bid
	 * @param pc
	 * @return BookBean   返回类型
	 */
	public BookBean findByBookId(String bid){
		try {
			return bookDao.findByBookId(bid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 按分类id查询其含有的书个数 
	 * @param cid
	 * @return int   返回类型
	 */
	public int findBookNumByCid(String cid) {
		try {
			return bookDao.findBookNumByCid(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 添加图书
	 * @param newBook 
	 * @return void   返回类型
	 */
	public void addBook(BookBean newBook) {
		try {
			bookDao.addBook(newBook);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改图书
	 * @param book 
	 * @return void   返回类型
	 */
	public void updateBook(BookBean book) {
		try {
			bookDao.updateBook(book);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 删除图书
	 * @param bid 
	 * @return void   返回类型
	 */
	public void deleteBook(String bid) {
		try {
			bookDao.deleteBook(bid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
