package cn.cdut.bookcity.category.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cdut.bookcity.category.domain.CategoryBean;
import cn.cdut.bookcity.category.service.CategoryServiceImpl;
import cn.cdut.servlet.BaseServlet;

/**
 * 处理分类模块的servlet
 * @author huangyong
 * @date 2018年4月25日 上午11:49:55
 */
@SuppressWarnings("serial")
public class CategoryServlet extends BaseServlet {
	
	private static CategoryServiceImpl categoryService = new CategoryServiceImpl();
	
	/**
	 * 得到所有的分类,以键为categories存在request中 
	 * @param request
	 * @param response
	 * @throws ServletException 
	 * @return String   返回类型
	 */
	public String getAllCategories(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException{
		List<CategoryBean> categories = categoryService.findAllCategories();
		request.setAttribute("categories", categories);
		return "f:/jsps/left.jsp";
	}
	
	/**
	 * ajax请求,根据父分类获取其所含子分类
	 * @param request
	 * @param response
	 * @throws ServletException 
	 * @return String   返回类型
	 * @throws IOException 
	 */
	public String ajaxGetChildrenByParent(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String pid = request.getParameter("pid");
		List<CategoryBean> children = categoryService.findByParentId(pid);
		String json = categoryListToJson(children);
		response.getWriter().print(json);
		return null;
	}

	/**
	 * 将List<CategoryBean>类型的集合转化为json对象数组 
	 * @param children
	 * @return String   返回类型
	 */
	private String categoryListToJson(List<CategoryBean> categoryList) {
		/*
		 * json数组格式:  [{},{},...]
		 * 大括号{}整体表示json对象,中括号[]整体表示json对象数组
		 */
		StringBuilder sb = new StringBuilder("[");
		for(int i=0; i<categoryList.size(); i++) {
			sb.append(categoryToJson(categoryList.get(i)));
			if(i < categoryList.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 将CategoryBean对象转换为json对象
	 * @param categoryBean
	 * @return String   返回类型
	 */
	private String categoryToJson(CategoryBean categoryBean) {
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"cid\": ").append("\"").append(categoryBean.getCid()).append("\"");
		sb.append(", ");
		sb.append("\"cname\": ").append("\"").append(categoryBean.getCname()).append("\"");
		sb.append("}");
		return sb.toString();
	}
}
