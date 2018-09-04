package cn.cdut.bookcity.book.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cdut.bookcity.book.domain.BookBean;
import cn.cdut.bookcity.book.domain.BookQueryForm;
import cn.cdut.bookcity.book.service.BookServiceImpl;
import cn.cdut.bookcity.pager.PageBean;
import cn.cdut.servlet.BaseServlet;
import cn.cdut.util.common.CommonUtils;

/**
 * Bookģ����Ʋ�
 * @author huangyong
 * @date 2018��4��26�� ����10:54:02
 */
public class BookServlet extends BaseServlet {
	private static BookServiceImpl bookService = new BookServiceImpl();

	/**
	 * ��ҳ��õ���ǰҳ��,��û��Ĭ��Ϊ��һҳ
	 * @param req
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
	 * ��ȡ��ǰ��URL��ַ,���ڱ�����������
	 * @param req
	 * @return String   ��������
	 */
	private String getUrl(HttpServletRequest req){
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
	 * ����ͼ����ϸ��Ϣ
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid = req.getParameter("bid");
		BookBean book = bookService.findByBookId(bid);
		req.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
	}
	
	/**
	 * ͨ����������ͼ�鹦�� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String findByBookname(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		int pc = getPageCode(req);
		String url = getUrl(req);
		String bname = req.getParameter("bname");
		PageBean<BookBean> pageBean = bookService.findByBookname(bname, pc);
		pageBean.setUrl(url);
		req.setAttribute("pageBean", pageBean);
		return "f:/jsps/book/list.jsp";
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
			throws ServletException, IOException{
		int pc = getPageCode(req);
		String url = getUrl(req);
		BookQueryForm form = CommonUtils.map2Bean(req.getParameterMap(), BookQueryForm.class);
		PageBean<BookBean> pageBean = bookService.findByCombination(form, pc);
		pageBean.setUrl(url);
		req.setAttribute("pageBean", pageBean);
		return "f:/jsps/book/list.jsp";
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
		return "f:/jsps/book/list.jsp";
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
		return "f:/jsps/book/list.jsp";
	}
	
	/**
	 * ͨ�������ѯ 
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
		//��������pageBean
		pageBean.setUrl(url);
		req.setAttribute("pageBean", pageBean);
		return "f:/jsps/book/list.jsp";
	}
	

}
