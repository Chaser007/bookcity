package cn.cdut.bookcity.cart.web.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cdut.bookcity.book.domain.BookBean;
import cn.cdut.bookcity.cart.domain.CartItem;
import cn.cdut.bookcity.cart.service.CartServiceImpl;
import cn.cdut.bookcity.user.domain.UserBean;
import cn.cdut.servlet.BaseServlet;
import cn.cdut.util.common.CommonUtils;

/**
 * ���ﳵģ����Ʋ�
 * @author huangyong
 * @date 2018��5��4�� ����9:03:42
 */
public class CartServlet extends BaseServlet {
	
	private static CartServiceImpl cartService = new CartServiceImpl();
	
	/**
	 * ��ʾ��ǰ�û��Ĺ��ﳵ���� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String showMyCart(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		UserBean user = (UserBean) req.getSession().getAttribute("user");
		List<CartItem> cartItemList = cartService.findByUser(user.getUid());
		req.setAttribute("cartItemList", cartItemList);
		return "f:/jsps/cart/list.jsp";
	}
	
	/**
	 * ���û����ﳵ�������Ŀ 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String addCartItem(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		Map map = req.getParameterMap();
		//ӳ��bid
		BookBean book = CommonUtils.map2Bean(map, BookBean.class);
		//ӳ��quantity
		CartItem cartItem = CommonUtils.map2Bean(map, CartItem.class);
		UserBean user = (UserBean) req.getSession().getAttribute("user");
		cartItem.setBook(book);
		cartItem.setUser(user);
		cartService.addCartItem(cartItem);
		return showMyCart(req, resp);
	}
	
	/**
	 * ���ض�����ﳵ��Ŀ,�������ɶ��� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String loadCartItems(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String cartItemIds = req.getParameter("cartItemIds");
		String total = req.getParameter("total");
		List<CartItem> cartItemList = cartService.loadCartItems(cartItemIds);
		
		req.setAttribute("cartItemIds", cartItemIds);
		req.setAttribute("cartItemList", cartItemList);
		req.setAttribute("total", total);
		return "/jsps/cart/showitem.jsp";
	}
	
	/**
	 * �����򵥸�ɾ�����ﳵ��Ŀ 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String batchDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String cartItemIds = req.getParameter("cartItemIds");
		cartService.batchDelete(cartItemIds);
		return showMyCart(req, resp);
	}
	
	/**
	 * ajax�����޸��û����ﳵ��Ŀ����
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String ajaxUpdateQuantity(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String cartItemId = req.getParameter("cartItemId");
		int quantity = Integer.parseInt(req.getParameter("quantity"));
		CartItem cartItem = cartService.updateQuantity(cartItemId, quantity);
		/*
		 * �������ص�json���� 
		 */
		StringBuilder jsonData = new StringBuilder("{");
		jsonData.append("\"quantity\":").append(cartItem.getQuantity()).append(",");
		jsonData.append("\"subTotal\":").append(cartItem.getSubTotal());
		jsonData.append("}");
		
		resp.getWriter().print(jsonData.toString());
		return null;
	}
}
