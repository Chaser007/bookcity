package cn.cdut.bookcity.background.book.web.servlet;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cdut.bookcity.book.domain.BookBean;
import cn.cdut.bookcity.book.domain.BookQueryForm;
import cn.cdut.bookcity.book.service.BookServiceImpl;
import cn.cdut.bookcity.category.domain.CategoryBean;
import cn.cdut.bookcity.category.service.CategoryServiceImpl;
import cn.cdut.bookcity.pager.PageBean;
import cn.cdut.servlet.BaseServlet;
import cn.cdut.util.common.CommonUtils;

/**
 * ��̨��ͼ��ģ��Ŀ��Ʋ�
 * @author huangyong
 * @date 2018��5��14�� ����10:09:14
 */
public class BgBookServlet extends BaseServlet{

	private static BookServiceImpl bookService = new BookServiceImpl();
	private static CategoryServiceImpl categoryService = new CategoryServiceImpl();
	
	/**
	 * ��ͼ�����������ʾ���з���
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String showCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<CategoryBean> parents = categoryService.findAllCategories();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/left.jsp";
	}
	
	/**
	 * �õ���ǰҳ��,Ĭ��Ϊ��1ҳ
	 * @return int   ��������
	 */
	private int getPageCode(HttpServletRequest req){
		int pc = 1;
		String param = req.getParameter("pageCode");
		if(param != null && !param.trim().isEmpty()){
			try{
				//Integer��parseInt�������׳�NumberFormatException����ʱ�쳣��
				//���䲶��ʲô������
				pc = Integer.parseInt(param);
			}catch(RuntimeException e){}
		}
		return pc;
	}
	
	/**
	 * �õ���ǰ��URL,���ڱ��������������� 
	 * @param req
	 * @return String   ��������
	 */
	private String getUrl(HttpServletRequest req) {
		/*
		 * req.getRequestURI()�õ�/bookcity/BookServlet
		 * req.getQueryString()�õ�'?'����Ĳ������ֲ�����'?'
		 */
		String url = req.getRequestURI() + "?" + req.getQueryString();
		//url�����п��ܴ���pageCode����,���ȡ��
		int index = url.indexOf("&pageCode");
		if(index != -1){
			url = url.substring(0, index);
		}
		return url;
 	}
	
	/**
	 * ��Ŀ¼��ѯ 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String findByCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		int pc = getPageCode(req);
		String url = getUrl(req);
		PageBean<BookBean> pageBean = bookService.findByCategory(cid, pc);
		pageBean.setUrl(url);
		req.setAttribute("pageBean", pageBean);
		return "f:/adminjsps/admin/book/list.jsp";
	}
	
	/**
	 * ����ͼ����ϸ��Ϣ�ͷ�����Ϣ,�����ڱ༭ͼ��
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid = req.getParameter("bid");
		//����ͼ����Ϣ
		BookBean book = bookService.findByBookId(bid);
		req.setAttribute("book", book);
		//��������һ������
		List<CategoryBean> parents = categoryService.findFirstLevel();
		//����ͼ�������Ķ�������
		CategoryBean bookCategory = categoryService.load(book.getCategory().getCid());
		req.setAttribute("bookCategory", bookCategory);
		//����ͼ��������һ�������µ����ж�������
		List<CategoryBean> children = categoryService.findByParentId(bookCategory.getParent().getCid());
		req.setAttribute("parents", parents);
		req.setAttribute("children", children);
		return "f:/adminjsps/admin/book/desc.jsp";
	}
	
	/**
	 * ͨ����������ͼ��
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String findByAuthor(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		String author = req.getParameter("author");
		int pc = getPageCode(req);
		String url = getUrl(req);
		PageBean<BookBean> pageBean = bookService.findByAuthor(author, pc);
		pageBean.setUrl(url);
		req.setAttribute("pageBean", pageBean);
		return "f:/adminjsps/admin/book/list.jsp";
	}
	
	/**
	 * ͨ������������ͼ�� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String findByPress(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		String press = req.getParameter("press");
		int pc = getPageCode(req);
		String url = getUrl(req);
		PageBean<BookBean> pageBean = bookService.findByPress(press, pc);
		pageBean.setUrl(url);
		req.setAttribute("pageBean", pageBean);
		return "f:/adminjsps/admin/book/list.jsp";
	}
	
	/**
	 * �߼���������,��ϲ�ѯ 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String findByCombination(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		int pc = getPageCode(req);
		String url = getUrl(req);
		BookQueryForm form = CommonUtils.map2Bean(req.getParameterMap(), BookQueryForm.class);
		PageBean<BookBean> pageBean = bookService.findByCombination(form, pc);
		pageBean.setUrl(url);
		req.setAttribute("pageBean", pageBean);
		return "f:/adminjsps/admin/book/list.jsp";
	}
	
	/**
	 * ׼�����ͼ��
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String addBookPre(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		List<CategoryBean> parents = categoryService.findFirstLevel();
		//���������ͼ��ҳ����ʾһ������
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/add.jsp";
	}

	/**
	 * �༭�޸�ͼ�鹦�� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String editBook(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		Map<String, String[]> map = req.getParameterMap();
		BookBean book = CommonUtils.map2Bean(map, BookBean.class);
		CategoryBean category = CommonUtils.map2Bean(map, CategoryBean.class);
		book.setCategory(category);
		bookService.updateBook(book);
		req.setAttribute("msg", "�޸ĳɹ�!");
		return "f:/adminjsps/msg.jsp";
	}
	
	/**
	 * ɾ��ͼ��
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String deleteBook(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		try {
			String bid = req.getParameter("bid");
			//����ͼ��,�õ����Сͼ·��,ɾ����Сͼ
			BookBean book = bookService.findByBookId(bid);
			String image_w = book.getImage_w();
			String image_b = book.getImage_b();
			URL url = this.getServletContext().getResource("/");
			URI uri = url.toURI();
			new File(new File(uri), image_w).delete();
			new File(new File(uri), image_b).delete();
			
			bookService.deleteBook(bid);
			req.setAttribute("msg", book.getBname() + " ɾ���ɹ�!");
			return "f:/adminjsps/msg.jsp";
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}
