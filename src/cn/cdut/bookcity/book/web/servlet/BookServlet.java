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
 * Book模块控制层
 * @author huangyong
 * @date 2018年4月26日 下午10:54:02
 */
public class BookServlet extends BaseServlet {
	private static BookServiceImpl bookService = new BookServiceImpl();

	/**
	 * 从页面得到当前页数,若没有默认为第一页
	 * @param req
	 * @return int   返回类型
	 */
	private int getPageCode(HttpServletRequest req){
		int pc = 1;
		String param = req.getParameter("pageCode");
		if(param != null && !param.trim().isEmpty()){
			try{
				//Integer的parseInt方法会抛出NumberFormatException运行时异常，
				//将其捕获什么都不做
				pc = Integer.parseInt(param);
			}catch(RuntimeException e){}
		}
		return pc;
	}
	
	/**
	 * 获取当前的URL地址,用于保持搜索条件
	 * @param req
	 * @return String   返回类型
	 */
	private String getUrl(HttpServletRequest req){
		/*
		 * req.getRequestURI()得到/bookcity/BookServlet
		 * req.getQueryString()得到'?'后面的参数部分不包含'?'
		 */
		String url = req.getRequestURI() + "?" + req.getQueryString();
		//url参数中可能带有pageCode参数,需截取掉
		int index = url.indexOf("&pageCode");
		if(index != -1){
			url = url.substring(0, index);
		}
		return url;
	}
	
	/**
	 * 加载图书详细信息
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid = req.getParameter("bid");
		BookBean book = bookService.findByBookId(bid);
		req.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
	}
	
	/**
	 * 通过书名搜索图书功能 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
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
	 * 高级搜索功能,组合查询 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
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
	 * 通过作者搜索图书
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
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
	 * 通过出版社搜索图书 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
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
	 * 通过分类查询 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String findByCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		int pc = getPageCode(req);
		String url = getUrl(req);
		PageBean<BookBean> pageBean = bookService.findByCategory(cid, pc);
		//补充完整pageBean
		pageBean.setUrl(url);
		req.setAttribute("pageBean", pageBean);
		return "f:/jsps/book/list.jsp";
	}
	

}
