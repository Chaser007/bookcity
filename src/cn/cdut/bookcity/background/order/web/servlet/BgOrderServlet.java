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
 * 后台的订单管理模块的控制层
 * @author huangyong
 * @date 2018年5月17日 下午10:40:51
 */
public class BgOrderServlet extends BaseServlet {
	
	private static OrderServiceImpl orderService = new OrderServiceImpl();
	
	/**
	 * 得到当前查询的页数 
	 * @return int   返回类型
	 */
	private int getPageCode(HttpServletRequest req) {
		int pc = 1;
		String param = req.getParameter("pageCode");
		if(param != null && !param.trim().equals("")) {
			try {
				//Integer的pareseInt()会抛出NumberFormatException运行时异常
				pc = Integer.parseInt(param);
			} catch (NumberFormatException e) {
				//抛出异常时什么也不用做,pc则等于1
			}
		}
		return pc;
	}

	/**
	 * 得到前端的Url,用于保持搜索条件不变 
	 * @return String   返回类型
	 */
	private String getUrl(HttpServletRequest req) {
		//得到请求的url
		String url = req.getRequestURI() + "?" + req.getQueryString();
		//如果url中含有pageCode当前页参数,则去掉
		int index = url.indexOf("&pageCode");
		if(index != -1) {
			url = url.substring(0, index);
		}
		return url;
	}
	
	/**
	 * 显示所有订单 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String showAllOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int pc = getPageCode(req);
		String url = getUrl(req);
		//得到分页类,补全类的属性
		PageBean<Order> pageBean = orderService.findAll(pc);
		pageBean.setUrl(url);
		req.setAttribute("pageBean", pageBean);
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	/**
	 * 按不同的状态显示订单 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String showOrderByStatus(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int pc = getPageCode(req);
		String url = getUrl(req);
		String status = req.getParameter("status");
		//得到分页类,补全类的属性
		PageBean<Order> pageBean = orderService.findByStatus(Integer.parseInt(status), pc);
		pageBean.setUrl(url);
		req.setAttribute("pageBean", pageBean);
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	/**
	 * 加载订单详细信息
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		//接受用户按下的按钮类型
		String btn = req.getParameter("btn");
		Order order = orderService.loadOrder(oid);
		req.setAttribute("btn", btn);
		req.setAttribute("order", order);
		return "f:/adminjsps/admin/order/desc.jsp";
	}
	
	/**
	 * 取消订单 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String cancel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		//订单的status=5代表订单状态为取消
		orderService.updateStatus(oid, 5);
		//加载订单的信息,回显给用户
		Order order = orderService.loadOrder(oid);
		req.setAttribute("order", order);
		return "f:/adminjsps/admin/order/desc.jsp";
	}
	
	/**
	 * 发货功能, 订单修改为已发货
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String deliver(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		//订单的status=3代表订单状态为取消
		orderService.updateStatus(oid, 3);
		//加载订单的信息,回显给用户
		Order order = orderService.loadOrder(oid);
		req.setAttribute("order", order);
		return "f:/adminjsps/admin/order/desc.jsp";
	}
}
