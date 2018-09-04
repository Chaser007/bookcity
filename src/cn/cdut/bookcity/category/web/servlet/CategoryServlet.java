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
 * �������ģ���servlet
 * @author huangyong
 * @date 2018��4��25�� ����11:49:55
 */
@SuppressWarnings("serial")
public class CategoryServlet extends BaseServlet {
	
	private static CategoryServiceImpl categoryService = new CategoryServiceImpl();
	
	/**
	 * �õ����еķ���,�Լ�Ϊcategories����request�� 
	 * @param request
	 * @param response
	 * @throws ServletException 
	 * @return String   ��������
	 */
	public String getAllCategories(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException{
		List<CategoryBean> categories = categoryService.findAllCategories();
		request.setAttribute("categories", categories);
		return "f:/jsps/left.jsp";
	}
	
	/**
	 * ajax����,���ݸ������ȡ�������ӷ���
	 * @param request
	 * @param response
	 * @throws ServletException 
	 * @return String   ��������
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
	 * ��List<CategoryBean>���͵ļ���ת��Ϊjson�������� 
	 * @param children
	 * @return String   ��������
	 */
	private String categoryListToJson(List<CategoryBean> categoryList) {
		/*
		 * json�����ʽ:  [{},{},...]
		 * ������{}�����ʾjson����,������[]�����ʾjson��������
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
	 * ��CategoryBean����ת��Ϊjson����
	 * @param categoryBean
	 * @return String   ��������
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
