package cn.cdut.bookcity.background.order.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cdut.bookcity.order.domain.Order;
import cn.cdut.bookcity.order.service.OrderServiceImpl;
import cn.cdut.bookcity.pager.PageBean;
import cn.cdut.servlet.BaseServlet;

/**
 * ��̨�Ķ�������ģ��Ŀ��Ʋ�
 * @author huangyong
 * @date 2018��5��17�� ����10:40:51
 */
public class BgOrderServlet extends BaseServlet {
	
	private static OrderServiceImpl orderService = new OrderServiceImpl();
	
	/**
	 * �õ���ǰ��ѯ��ҳ�� 
	 * @return int   ��������
	 */
	private int getPageCode(HttpServletRequest req) {
		int pc = 1;
		String param = req.getParameter("pageCode");
		if(param != null && !param.trim().equals("")) {
			try {
				//Integer��pareseInt()���׳�NumberFormatException����ʱ�쳣
				pc = Integer.parseInt(param);
			} catch (NumberFormatException e) {
				//�׳��쳣ʱʲôҲ������,pc�����1
			}
		}
		return pc;
	}

	/**
	 * �õ�ǰ�˵�Url,���ڱ��������������� 
	 * @return String   ��������
	 */
	private String getUrl(HttpServletRequest req) {
		//�õ������url
		String url = req.getRequestURI() + "?" + req.getQueryString();
		//���url�к���pageCode��ǰҳ����,��ȥ��
		int index = url.indexOf("&pageCode");
		if(index != -1) {
			url = url.substring(0, index);
		}
		return url;
	}
	
	/**
	 * ��ʾ���ж��� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String showAllOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int pc = getPageCode(req);
		String url = getUrl(req);
		//�õ���ҳ��,��ȫ�������
		PageBean<Order> pageBean = orderService.findAll(pc);
		pageBean.setUrl(url);
		req.setAttribute("pageBean", pageBean);
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	/**
	 * ����ͬ��״̬��ʾ���� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String showOrderByStatus(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int pc = getPageCode(req);
		String url = getUrl(req);
		String status = req.getParameter("status");
		//�õ���ҳ��,��ȫ�������
		PageBean<Order> pageBean = orderService.findByStatus(Integer.parseInt(status), pc);
		pageBean.setUrl(url);
		req.setAttribute("pageBean", pageBean);
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	/**
	 * ���ض�����ϸ��Ϣ
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		//�����û����µİ�ť����
		String btn = req.getParameter("btn");
		Order order = orderService.loadOrder(oid);
		req.setAttribute("btn", btn);
		req.setAttribute("order", order);
		return "f:/adminjsps/admin/order/desc.jsp";
	}
	
	/**
	 * ȡ������ 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String cancel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		//������status=5������״̬Ϊȡ��
		orderService.updateStatus(oid, 5);
		//���ض�������Ϣ,���Ը��û�
		Order order = orderService.loadOrder(oid);
		req.setAttribute("order", order);
		return "f:/adminjsps/admin/order/desc.jsp";
	}
	
	/**
	 * ��������, �����޸�Ϊ�ѷ���
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String deliver(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		//������status=3������״̬Ϊȡ��
		orderService.updateStatus(oid, 3);
		//���ض�������Ϣ,���Ը��û�
		Order order = orderService.loadOrder(oid);
		req.setAttribute("order", order);
		return "f:/adminjsps/admin/order/desc.jsp";
	}
}
