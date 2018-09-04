package cn.cdut.bookcity.background.category.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cdut.bookcity.book.service.BookServiceImpl;
import cn.cdut.bookcity.category.domain.CategoryBean;
import cn.cdut.bookcity.category.service.CategoryServiceImpl;
import cn.cdut.servlet.BaseServlet;
import cn.cdut.util.common.CommonUtils;

/**
 * 后台的目录模块的控制层
 * @author huangyong
 * @date 2018年5月13日 下午4:38:09
 */
public class BgCategoryServlet extends BaseServlet {

	private static CategoryServiceImpl categoryService = new CategoryServiceImpl();
	private static BookServiceImpl bookService = new BookServiceImpl();
	
	/**
	 * 显示所有分类 
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
		return "f:/adminjsps/admin/category/list.jsp";
	}
	
	/**
	 * 添加一级分类 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String addFirstLevel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		CategoryBean parent = CommonUtils.map2Bean(req.getParameterMap(), CategoryBean.class);
		parent.setCid(CommonUtils.uuid());
		parent.setParent(null);
		categoryService.addCategory(parent);
		return showCategory(req, resp);
	}
	
	/**
	 * 准备添加二级分类 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String addSecondLevelPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pid = req.getParameter("cid");
		List<CategoryBean> parents =  categoryService.findFirstLevel();
		req.setAttribute("parents", parents);
		req.setAttribute("pid", pid);
		return "f:/adminjsps/admin/category/add2.jsp";
	}
	
	/**
	 * 添加二级分类 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String addSecondLevel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		CategoryBean child = CommonUtils.map2Bean(req.getParameterMap(), CategoryBean.class);
		CategoryBean parent = new CategoryBean();
		parent.setCid((String)req.getParameter("pid"));
		child.setCid(CommonUtils.uuid());
		child.setParent(parent);
		categoryService.addCategory(child);
		return showCategory(req, resp);
	}
	
	/**
	 * 准备修改一级分类 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String editFirstLevelPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		CategoryBean parent = categoryService.load(cid);
		req.setAttribute("parent", parent);
		return "f:/adminjsps/admin/category/edit.jsp";
	}
	
	/**
	 * 修改一级分类 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String editFirstLevel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		CategoryBean parent = CommonUtils.map2Bean(req.getParameterMap(), CategoryBean.class);
		parent.setParent(null);
		categoryService.modifyCategory(parent);
		return showCategory(req, resp);
	}
	
	/**
	 * 准备修改二级分类 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String editSecondLevelPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 获取当前二级分类cid,查询之并存入request
		 * 3. 查询所有一级分类,并存入request
		 */
		String cid = req.getParameter("cid");
		CategoryBean child = categoryService.load(cid);
		req.setAttribute("child", child);
		
		List<CategoryBean> parents = categoryService.findFirstLevel();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/category/edit2.jsp";
	}
	
	/**
	 * 修改二级分类 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String editSecondLevel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		CategoryBean child = CommonUtils.map2Bean(req.getParameterMap(), CategoryBean.class);
		CategoryBean parent = new CategoryBean();
		parent.setCid(req.getParameter("pid"));
		child.setParent(parent);
		
		categoryService.modifyCategory(child);
		return showCategory(req, resp);
	}
	
	/**
	 * 删除一级分类 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String deleteFirstLevel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		//查询该分类下是否有子分类,若有则不能删除
		int count = categoryService.findChildNumByPid(cid);
		if(count > 0){
			req.setAttribute("msg", "该分类下存在子分类,不能直接删除!");
			return "f:/adminjsps/admin/msg.jsp";
		}
		categoryService.deleteCategory(cid);
		return showCategory(req, resp);
	}
	
	/**
	 * 删除二级分类 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String deleteSecondLevel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		//查询该分类下是否有书籍,若有则不能删除
		int count = bookService.findBookNumByCid(cid);
		if(count > 0){
			req.setAttribute("msg", "该分类下存在书籍,不能直接删除!");
			return "f:/adminjsps/admin/msg.jsp";
		}
		categoryService.deleteCategory(cid);
		return showCategory(req, resp);
	}
}
