package cn.cdut.bookcity.order.web.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cdut.bookcity.cart.domain.CartItem;
import cn.cdut.bookcity.cart.service.CartServiceImpl;
import cn.cdut.bookcity.order.domain.Order;
import cn.cdut.bookcity.order.domain.OrderItem;
import cn.cdut.bookcity.order.service.OrderServiceImpl;
import cn.cdut.bookcity.pager.PageBean;
import cn.cdut.bookcity.user.domain.UserBean;
import cn.cdut.servlet.BaseServlet;
import cn.cdut.util.common.CommonUtils;

/**
 * ����ģ����Ʋ�
 * @author huangyong
 * @date 2018��5��7�� ����9:06:36
 */
public class OrderServlet extends BaseServlet {

	private static OrderServiceImpl orderService = new OrderServiceImpl();
	
	private static CartServiceImpl cartService = new CartServiceImpl();
	
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
		 * req.getRequestURI()�õ�/bookcity/OrderServlet
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
	 * �ҵĶ������� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String showMyOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		UserBean user = (UserBean)req.getSession().getAttribute("user");
		int pc = getPageCode(req);
		String url = getUrl(req);
		PageBean<Order> pageBean = orderService.findByUser(user.getUid(), pc);
		pageBean.setUrl(url);
		req.setAttribute("pageBean", pageBean);
		return "f:/jsps/order/list.jsp";
	}
	
	/**
	 * ���ɶ��� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String createOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		//1����ȡcartItemIds,��ѯ������,Ϊ���湹��OrderItem��׼��
		String cartItemIds = req.getParameter("cartItemIds");
		List<CartItem> cartItemList = cartService.loadCartItems(cartItemIds);
		if(cartItemList.size() == 0) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "��û��ѡ��Ҫ�����ͼ�飬�����µ���");
			return "f:/jsps/msg.jsp";
		}
		
		//2������Order
		Order order = new Order();
		order.setOid(CommonUtils.uuid());
		//Ϊ�������ø�ʽ������
		order.setOrdertime(String.format("%tF %<tT", new Date()));
		order.setAddress(req.getParameter("address"));
		order.setStatus(1);
		UserBean owner = (UserBean)req.getSession().getAttribute("user");
		order.setOwner(owner);
		//�����ܼ�
		BigDecimal total = new BigDecimal("0");
		for(CartItem cartItem : cartItemList){
			String subTotal = cartItem.getSubTotal();
			total = total.add(new BigDecimal(subTotal));
		}
		order.setTotal(total.doubleValue());
		
		//����List<OrderItem>
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for(CartItem cartItem : cartItemList){
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setOrderItemId(CommonUtils.uuid());
			orderItem.setBook(cartItem.getBook());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setSubtotal(Double.parseDouble(cartItem.getSubTotal()));
			orderItemList.add(orderItem);
		}
		
		order.setOrderItemList(orderItemList);
		
		//����service��������
		orderService.createOrder(order);
		//ɾ�����ﳵ�е������Ŀ
		cartService.batchDelete(cartItemIds);
		
		req.setAttribute("order", order);
		return "f:/jsps/order/ordersucc.jsp";
	}
	
	/**
	 * ��ʾ������ϸ��Ϣ 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String loadOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String oid = req.getParameter("oid");
		// ���ڼ��ض�������ʾ�ĸ���ť(֧����ȡ����ȷ��)
		String btnType = req.getParameter("btn");
		Order order = orderService.loadOrder(oid);
		req.setAttribute("order", order);
		req.setAttribute("btn", btnType);
		return "f:/jsps/order/desc.jsp";
	}
	
	/**
	 * ȡ������,ֻ��ȡ��δ֧���Ķ��� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String cancelOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String oid = req.getParameter("oid");
		int status = orderService.findStatus(oid);
		if(status != 1){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "����״̬����!");
			return "f:/jsps/msg.jsp";
		}
		//5:����ȡ��״̬
		orderService.updateStatus(oid, 5);
		return showMyOrder(req, resp);
	}
	
	/**
	 * ֧��׼��,׼����֧�����,֧���Ķ��� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String payPrepare(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String oid = req.getParameter("oid");
		req.setAttribute("order", orderService.loadOrder(oid));
		return "f:/jsps/order/pay.jsp";
	}
	
	/**
	 * ֧������
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String pay(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * ʹ���ױ�֧��ƽ̨
		 * �ױ�֧��ƽ̨ͳһʹ��GBK/GB2312���뷽ʽ
		 * �����漰�����ĵĲ�����ע������ʽ
		 * 1������13����Ҫ�Ĳ���
		 * 2������Կ��13�������������㷨�õ�hmac����
		 * 3���ض����ױ���֧������
		 */
		//���������ļ�,�����ļ��к����̻���š���Կ��֧���ɹ���Ļص���ַ
		Properties pro = new Properties();
		pro.load(this.getClass().getClassLoader().getResourceAsStream("payment.properties"));
		
		String p0_Cmd = "Buy";	//ҵ������:�̶�ֵ"Buy"
		String p1_MerId = pro.getProperty("p1_MerId");	//�̻����:�˴��ò��Ա��
		String p2_Order = req.getParameter("oid");	//�̻�������:����֧���ɹ��������Լ��޸Ķ���״̬
		String p3_Amt = "0.01";	//֧�����:��Ϊ�ǲ����˺�,Ǯ�Ǵ���ױ���,������Ϊ��Сֵ
		String p4_Cur = "CNY";	//���ױ���:�̶�ֵ"CNY"
		String p5_Pid = "";	//��Ʒ����:������ʾ��,����Ϊ���ַ���,������Ϊ��
		String p6_Pcat = "";	//��Ʒ����:����Ʒ��������һ��
		String p7_Pdesc = "";	//��Ʒ����:����������һ��
		String p8_Url = pro.getProperty("p8_Url");	//�̻�����֧���ɹ����ݵĵ�ַ:�ɹ����践�صĵ�ַ
		String p9_SAF = "0";	//�ͻ���ַ�Ƿ������ױ�,"0"Ϊ������,"1"Ϊ����
		String pa_MP = "";	//�̻���չ��Ϣ
		String pd_FrpId = req.getParameter("yh");	//֧��ͨ������:���������ĸ�֧��ͨ����֧��ҳ��
		String pr_NeedResponse = "1";	//Ӧ�����:�̶�ֵΪ1,����ҪӦ�����
		
		//�������ļ��л�ȡ��Կ,��Կ����ǩ��
		String keyValue = pro.getProperty("keyValue");
		//�ü����㷨���ܵõ�hmac����
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);
		
		//������Ҫ�ض���ĵ�ַ,��get��ʽ����
		//�ױ�֧�����ص�ַ
		String url = "https://www.yeepay.com/app-merchant-proxy/node";
		url = url + "?p0_Cmd=" + p0_Cmd +
				"&p1_MerId=" + p1_MerId +
				"&p2_Order=" + p2_Order +
				"&p3_Amt=" + p3_Amt +
				"&p4_Cur=" + p4_Cur +
				"&p5_Pid=" + p5_Pid +
				"&p6_Pcat=" + p6_Pcat +
				"&p7_Pdesc=" + p7_Pdesc +
				"&p8_Url=" + p8_Url +
				"&p9_SAF=" + p9_SAF +
				"&pa_MP=" + pa_MP +
				"&pd_FrpId=" + pd_FrpId +
				"&pr_NeedResponse=" + pr_NeedResponse +
				"&hmac=" + hmac;
		
		//�ض����ױ�֧������
		resp.sendRedirect(url);
		
		return null;
	}
	
	/**
	 * ���ױ�֧���ɹ���Ļص����� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String payCallBack(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 1����ȡ�ױ�֧���ɹ��󷵻ص�11��������ǩ������hmac;
		 * 2������ͨ��11��������keyValue���м����㷨���hmac���ױ��ṩ��hmac����У��,
		 * ��һ��˵�����ݿ���;
		 */
		String p1_MerId = req.getParameter("p1_MerId");	//�̻����
		String r0_Cmd = req.getParameter("r0_Cmd");	//ҵ������:�̶�ֵ"Buy"
		String r1_Code = req.getParameter("r1_Code");	//֧�����:�̶�ֵ"1",����֧���ɹ�
		String r2_TrxId = req.getParameter("r2_TrxId");	//�ױ�֧��������ˮ��
		String r3_Amt = req.getParameter("r3_Amt");	//֧�����
		String r4_Cur = req.getParameter("r4_Cur");	//���ױ���:�̶�ֵ,������"RMB"
		String r5_Pid = req.getParameter("r5_Pid");	//��Ʒ����
		String r6_Order = req.getParameter("r6_Order");	//��Ʒ������:�ױ�֧�������̻�������
		String r7_Uid = req.getParameter("r7_Uid");	//�ױ�֧����ԱID
		String r8_MP = req.getParameter("r8_MP");	//��Ʒ��չ��Ϣ
		String r9_BType = req.getParameter("r9_BType");	//���׽����������: "1"Ϊ������ض���,"2"Ϊ��������Ե�ͨ��
		String hmac = req.getParameter("hmac");
		
		//�������ļ��л�ȡkeyValue��Կ
		Properties pro = new Properties();
		pro.load(this.getClass().getClassLoader().getResourceAsStream("payment.properties"));
		String keyValue = pro.getProperty("keyValue");
		
		//ͨ�������㷨���������Ƿ���Ч
		boolean isEffect = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
				r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
				r8_MP, r9_BType, keyValue);
		
		//��ǩ����һ��,�򷵻ش�����Ϣ
		if(!isEffect){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "֧��ʧ��,���ǻ���!");
			return "f:/jsps/msg.jsp";
		}
		
		//��֧���ɹ�,�϶���ɹ�,���ɹ��ױ�������ʴ˷���
		if("1".equals(r1_Code)){
			/*
			 * ������׽����������Ϊ������ض���
			 * 1��������״̬Ϊ1����Ķ�����״̬Ϊ2--��֧��δ����
			 * 2������ɹ���Ϣת����msg.jsp
			 */
			if("1".equals(r9_BType)){
				int status = orderService.findStatus(r6_Order);
				if(status == 1)
					orderService.updateStatus(r6_Order, 2);
				req.setAttribute("code", "success");
				req.setAttribute("msg", "��ϲ��,֧���ɹ�!");
				return "f:/jsps/msg.jsp";
			}
			/*
			 * ������׽����������Ϊ��������Ե�ͨ��
			 * ��Ҫ���ױ�����"success"
			 * 
			 * ֻ�й���IP��ַ�����н��׽����������Ϊ��������Ե�ͨ��
			 */
			else if("2".equals(r9_BType)){
				resp.getWriter().print("success");
			}
		}
		
		return null;
	}
}
