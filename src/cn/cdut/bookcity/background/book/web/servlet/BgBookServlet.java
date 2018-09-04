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
 * 后台的图书模块的控制层
 * @author huangyong
 * @date 2018年5月14日 下午10:09:14
 */
public class BgBookServlet extends BaseServlet{

	private static BookServiceImpl bookService = new BookServiceImpl();
	private static CategoryServiceImpl categoryService = new CategoryServiceImpl();
	
	/**
	 * 在图书管理的左侧显示所有分类
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String showCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<CategoryBean> parents = categoryService.findAllCategories();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/left.jsp";
	}
	
	/**
	 * 得到当前页数,默认为第1页
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
	 * 得到当前的URL,用于保持搜索条件不变 
	 * @param req
	 * @return String   返回类型
	 */
	private String getUrl(HttpServletRequest req) {
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
	 * 按目录查询 
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
		pageBean.setUrl(url);
		req.setAttribute("pageBean", pageBean);
		return "f:/adminjsps/admin/book/list.jsp";
	}
	
	/**
	 * 加载图书详细信息和分类信息,可用于编辑图书
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid = req.getParameter("bid");
		//加载图书信息
		BookBean book = bookService.findByBookId(bid);
		req.setAttribute("book", book);
		//加载所有一级分类
		List<CategoryBean> parents = categoryService.findFirstLevel();
		//加载图书所属的二级分类
		CategoryBean bookCategory = categoryService.load(book.getCategory().getCid());
		req.setAttribute("bookCategory", bookCategory);
		//加载图书所属的一级分类下的所有二级分类
		List<CategoryBean> children = categoryService.findByParentId(bookCategory.getParent().getCid());
		req.setAttribute("parents", parents);
		req.setAttribute("children", children);
		return "f:/adminjsps/admin/book/desc.jsp";
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
		return "f:/adminjsps/admin/book/list.jsp";
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
		return "f:/adminjsps/admin/book/list.jsp";
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
	 * 准备添加图书
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String addBookPre(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		List<CategoryBean> parents = categoryService.findFirstLevel();
		//用于在添加图书页面显示一级分类
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/add.jsp";
	}

	/**
	 * 编辑修改图书功能 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String editBook(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		Map<String, String[]> map = req.getParameterMap();
		BookBean book = CommonUtils.map2Bean(map, BookBean.class);
		CategoryBean category = CommonUtils.map2Bean(map, CategoryBean.class);
		book.setCategory(category);
		bookService.updateBook(book);
		req.setAttribute("msg", "修改成功!");
		return "f:/adminjsps/msg.jsp";
	}
	
	/**
	 * 删除图书
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String deleteBook(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		try {
			String bid = req.getParameter("bid");
			//加载图书,得到其大小图路径,删除大小图
			BookBean book = bookService.findByBookId(bid);
			String image_w = book.getImage_w();
			String image_b = book.getImage_b();
			URL url = this.getServletContext().getResource("/");
			URI uri = url.toURI();
			new File(new File(uri), image_w).delete();
			new File(new File(uri), image_b).delete();
			
			bookService.deleteBook(bid);
			req.setAttribute("msg", book.getBname() + " 删除成功!");
			return "f:/adminjsps/msg.jsp";
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}
