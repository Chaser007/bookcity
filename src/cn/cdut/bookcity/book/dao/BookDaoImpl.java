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
 * Bookģ��־ò�
 * @author huangyong
 * @date 2018��4��26�� ����11:02:31
 */
public class BookDaoImpl {
	
	private static TxQueryRunner qr = new TxQueryRunner();
	
	/**
	 * ��Map�����е����ݷ�װ��BookBean�� 
	 * @param map
	 * @return BookBean   ��������
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
	 * ͨ����id���в�ѯ 
	 * @param bid ��id
	 * @param pc ��ǰҳ��
	 * @throws SQLException 
	 * @return PageBean<BookBean>   ��������
	 */
	public BookBean findByBookId(String bid) throws SQLException{
		String sql = "SELECT * FROM t_book WHERE bid=?";
		Map<String, Object> map = qr.query(sql, new MapHandler(), bid);
		return mapToBookBean(map);
	}
	
	/**
	 * ͨ�ò�ѯ����,��������ϲ�ѯ������������������
	 * @param criterion ���ʽ�ļ���
	 * @param pc ��ǰ��Ҫ��ѯ��ҳ��
	 * @return PageBean<Book>   ��������
	 * @throws SQLException 
	 */
	private PageBean<BookBean> queryByCriteria(List<Expression> criterion, int pc) throws SQLException{
		/*
		 * 1�����÷�ҳ��С
		 * 2������������ѯ�õ���¼��
		 * 3������������ѯ�õ�List<Book>
		 * 4������pc��Ϊ��ǰҳ
		 */
		//���÷�ҳ��С
		int pageSize = PageConstants.BOOK_PAGE_SIZE;
		
		/*
		 * where�Ӿ�ƴ��,���磺
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
		
		//��ѯ���м�¼��
		String sql = "SELECT count(*) FROM t_book" + whereSql.toString();
		Number number = (Number) qr.query(sql, new ScalarHandler(), params.toArray());
		int totalRecords = number.intValue();
		
		//��������ѯ
		sql = "SELECT * FROM t_book" + whereSql.toString() + " ORDER BY orderBy LIMIT ?,?";
		//�ӵ�(pc - 1) * pageSize��¼��ʼ�飬����pageSize����¼
		//mysql�е�һ����¼�Ǵ�0��ʼ
		params.add((pc - 1) * pageSize);
		params.add(pageSize);
		List<BookBean> bookList = qr.query(sql, new BeanListHandler<BookBean>(BookBean.class), params.toArray());
		
		//����PageBean���ò���,����url�����������Ʋ�����
		PageBean<BookBean> pageBean =  new PageBean<BookBean>();
		pageBean.setPageCode(pc);
		pageBean.setPageSize(pageSize);
		pageBean.setTotalRecords(totalRecords);
		pageBean.setBeanList(bookList);
		
		return pageBean;
	}
	
	/**
	 * ������������cid��ѯ 
	 * @param cid ����id
	 * @param pc ��ǰҳ��
	 * @throws SQLException 
	 * @return PageBean<Book>   ��������
	 */
	public PageBean<BookBean> findByCategory(String cid, int pc) throws SQLException{
		List<Expression> criterion = new ArrayList<Expression>();
		criterion.add(new Expression("cid", "=", cid));
		return queryByCriteria(criterion, pc);
	}
	
	/**
	 * ͨ��������ģ����ѯ
	 * @param press ��������
	 * @param pc ��ǰҳ��
	 * @throws SQLException 
	 * @return PageBean<BookBean>   ��������
	 */
	public PageBean<BookBean> findByPress(String press, int pc) throws SQLException{
		List<Expression> criterion = new ArrayList<Expression>();
		criterion.add(new Expression("press", "LIKE", "%" + press + "%"));
		return queryByCriteria(criterion, pc);
	}
	
	/**
	 * ͨ�����߽���ģ����ѯ 
	 * @param author
	 * @param pc
	 * @return PageBean<BookBean>   ��������
	 * @throws SQLException 
	 */
	public PageBean<BookBean> findByAuthor(String author, int pc) throws SQLException{
		List<Expression> criterion = new ArrayList<Expression>();
		criterion.add(new Expression("author", "LIKE", "%" + author + "%"));
		return queryByCriteria(criterion, pc);
	}
	
	/**
	 * ͨ������ģ����ѯ
	 * @param bname ����
	 * @param pc ��ǰҳ��
	 * @throws SQLException 
	 * @return PageBean<Book>   ��������
	 */
	public PageBean<BookBean> findByBookname(String bname, int pc) throws SQLException{
		List<Expression> criterion = new ArrayList<Expression>();
		criterion.add(new Expression("bname", "LIKE", "%" + bname + "%"));
		return queryByCriteria(criterion, pc);
	}
	
	/**
	 * ͨ�����������ߡ������� ���ģ����ѯ
	 * @param form ��װ���������ߡ�������ı���
	 * @param pc ��ǰҳ��
	 * @throws SQLException 
	 * @return PageBean<BookBean>   ��������
	 */
	public PageBean<BookBean> findByCombination(BookQueryForm form, int pc) throws SQLException{
		List<Expression> criterion = new ArrayList<Expression>();
		criterion.add(new Expression("bname", "LIKE", "%" + form.getBname() + "%"));
		criterion.add(new Expression("author", "LIKE", "%" + form.getAuthor() + "%"));
		criterion.add(new Expression("press", "LIKE", "%" + form.getPress() + "%"));
		return queryByCriteria(criterion, pc);
	}

	/**
	 * ������id��ѯ������������ 
	 * @param cid
	 * @return int   ��������
	 * @throws SQLException 
	 */
	public int findBookNumByCid(String cid) throws SQLException {
		String sql = "SELECT count(*) FROM t_book WHERE cid=?";
		Number count = (Number) qr.query(sql, new ScalarHandler(), cid);
		return count.intValue();
 	}

	/**
	 * ���ͼ��
	 * @param newBook 
	 * @return void   ��������
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
	 * �޸�ͼ��
	 * @param book 
	 * @return void   ��������
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
	 * ɾ��ͼ��
	 * @param bid 
	 * @return void   ��������
	 * @throws SQLException 
	 */
	public void deleteBook(String bid) throws SQLException {
		String sql = "DELETE FROM t_book WHERE bid=?";
		qr.update(sql, bid);
	}
}
