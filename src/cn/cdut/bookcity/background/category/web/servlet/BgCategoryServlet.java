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
 * ��̨��Ŀ¼ģ��Ŀ��Ʋ�
 * @author huangyong
 * @date 2018��5��13�� ����4:38:09
 */
public class BgCategoryServlet extends BaseServlet {

	private static CategoryServiceImpl categoryService = new CategoryServiceImpl();
	private static BookServiceImpl bookService = new BookServiceImpl();
	
	/**
	 * ��ʾ���з��� 
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
		return "f:/adminjsps/admin/category/list.jsp";
	}
	
	/**
	 * ���һ������ 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
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
	 * ׼����Ӷ������� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
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
	 * ��Ӷ������� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
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
	 * ׼���޸�һ������ 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String editFirstLevelPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		CategoryBean parent = categoryService.load(cid);
		req.setAttribute("parent", parent);
		return "f:/adminjsps/admin/category/edit.jsp";
	}
	
	/**
	 * �޸�һ������ 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String editFirstLevel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		CategoryBean parent = CommonUtils.map2Bean(req.getParameterMap(), CategoryBean.class);
		parent.setParent(null);
		categoryService.modifyCategory(parent);
		return showCategory(req, resp);
	}
	
	/**
	 * ׼���޸Ķ������� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String editSecondLevelPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. ��ȡ��ǰ��������cid,��ѯ֮������request
		 * 3. ��ѯ����һ������,������request
		 */
		String cid = req.getParameter("cid");
		CategoryBean child = categoryService.load(cid);
		req.setAttribute("child", child);
		
		List<CategoryBean> parents = categoryService.findFirstLevel();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/category/edit2.jsp";
	}
	
	/**
	 * �޸Ķ������� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
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
	 * ɾ��һ������ 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String deleteFirstLevel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		//��ѯ�÷������Ƿ����ӷ���,��������ɾ��
		int count = categoryService.findChildNumByPid(cid);
		if(count > 0){
			req.setAttribute("msg", "�÷����´����ӷ���,����ֱ��ɾ��!");
			return "f:/adminjsps/admin/msg.jsp";
		}
		categoryService.deleteCategory(cid);
		return showCategory(req, resp);
	}
	
	/**
	 * ɾ���������� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String deleteSecondLevel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		//��ѯ�÷������Ƿ����鼮,��������ɾ��
		int count = bookService.findBookNumByCid(cid);
		if(count > 0){
			req.setAttribute("msg", "�÷����´����鼮,����ֱ��ɾ��!");
			return "f:/adminjsps/admin/msg.jsp";
		}
		categoryService.deleteCategory(cid);
		return showCategory(req, resp);
	}
}
