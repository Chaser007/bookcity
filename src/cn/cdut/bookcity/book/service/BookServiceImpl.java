package cn.cdut.bookcity.book.service;

import java.sql.SQLException;

import cn.cdut.bookcity.book.dao.BookDaoImpl;
import cn.cdut.bookcity.book.domain.BookBean;
import cn.cdut.bookcity.book.domain.BookQueryForm;
import cn.cdut.bookcity.pager.PageBean;

/**
 * Bookģ������
 * @author huangyong
 * @date 2018��4��26�� ����11:03:36
 */
public class BookServiceImpl {
	private static BookDaoImpl bookDao  = new BookDaoImpl();
	
	/**
	 * ͨ�������ѯ����
	 * @param cid ����id
	 * @param pc ��ǰҳ��
	 * @return PageBean<Book>   ��������
	 */
	public PageBean<BookBean> findByCategory(String cid, int pc){
		try {
			return bookDao.findByCategory(cid, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ͨ������ģ����ѯ
	 * @param bname ����
	 * @param pc ��ǰҳ��
	 * @return PageBean<Book>   ��������
	 */
	public PageBean<BookBean> findByBookname(String bname, int pc){
		try {
			return bookDao.findByBookname(bname, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * ͨ�����߲���ͼ�� 
	 * @param author
	 * @param pc
	 * @return PageBean<BookBean>   ��������
	 */
	public PageBean<BookBean> findByAuthor(String author, int pc){
		try{
			return bookDao.findByAuthor(author, pc);
		} catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * ͨ���������ѯ 
	 * @param press
	 * @param pc
	 * @return PageBean<BookBean>   ��������
	 */
	public PageBean<BookBean> findByPress(String press, int pc){
		try {
			return bookDao.findByPress(press, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * �߼�����,��ϲ�ѯ 
	 * @param form
	 * @param pc
	 * @return PageBean<BookBean>   ��������
	 */
	public PageBean<BookBean> findByCombination(BookQueryForm form , int pc){
		try {
			return bookDao.findByCombination(form, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ͨ��id��ѯ 
	 * @param bid
	 * @param pc
	 * @return BookBean   ��������
	 */
	public BookBean findByBookId(String bid){
		try {
			return bookDao.findByBookId(bid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ������id��ѯ�京�е������ 
	 * @param cid
	 * @return int   ��������
	 */
	public int findBookNumByCid(String cid) {
		try {
			return bookDao.findBookNumByCid(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ���ͼ��
	 * @param newBook 
	 * @return void   ��������
	 */
	public void addBook(BookBean newBook) {
		try {
			bookDao.addBook(newBook);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * �޸�ͼ��
	 * @param book 
	 * @return void   ��������
	 */
	public void updateBook(BookBean book) {
		try {
			bookDao.updateBook(book);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ɾ��ͼ��
	 * @param bid 
	 * @return void   ��������
	 */
	public void deleteBook(String bid) {
		try {
			bookDao.deleteBook(bid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
