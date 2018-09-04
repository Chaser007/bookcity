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
 * 订单模块控制层
 * @author huangyong
 * @date 2018年5月7日 下午9:06:36
 */
public class OrderServlet extends BaseServlet {

	private static OrderServiceImpl orderService = new OrderServiceImpl();
	
	private static CartServiceImpl cartService = new CartServiceImpl();
	
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
		 * req.getRequestURI()得到/bookcity/OrderServlet
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
	 * 我的订单功能 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
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
	 * 生成订单 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String createOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		//1、获取cartItemIds,查询出其结果,为后面构建OrderItem做准备
		String cartItemIds = req.getParameter("cartItemIds");
		List<CartItem> cartItemList = cartService.loadCartItems(cartItemIds);
		if(cartItemList.size() == 0) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "您没有选择要购买的图书，不能下单！");
			return "f:/jsps/msg.jsp";
		}
		
		//2、构建Order
		Order order = new Order();
		order.setOid(CommonUtils.uuid());
		//为订单设置格式化日期
		order.setOrdertime(String.format("%tF %<tT", new Date()));
		order.setAddress(req.getParameter("address"));
		order.setStatus(1);
		UserBean owner = (UserBean)req.getSession().getAttribute("user");
		order.setOwner(owner);
		//计算总计
		BigDecimal total = new BigDecimal("0");
		for(CartItem cartItem : cartItemList){
			String subTotal = cartItem.getSubTotal();
			total = total.add(new BigDecimal(subTotal));
		}
		order.setTotal(total.doubleValue());
		
		//构建List<OrderItem>
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
		
		//调用service创建订单
		orderService.createOrder(order);
		//删除购物车中的相关条目
		cartService.batchDelete(cartItemIds);
		
		req.setAttribute("order", order);
		return "f:/jsps/order/ordersucc.jsp";
	}
	
	/**
	 * 显示订单详细信息 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String loadOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String oid = req.getParameter("oid");
		// 用于加载订单后显示哪个按钮(支付、取消、确认)
		String btnType = req.getParameter("btn");
		Order order = orderService.loadOrder(oid);
		req.setAttribute("order", order);
		req.setAttribute("btn", btnType);
		return "f:/jsps/order/desc.jsp";
	}
	
	/**
	 * 取消订单,只能取消未支付的订单 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String cancelOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String oid = req.getParameter("oid");
		int status = orderService.findStatus(oid);
		if(status != 1){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "订单状态不对!");
			return "f:/jsps/msg.jsp";
		}
		//5:代表取消状态
		orderService.updateStatus(oid, 5);
		return showMyOrder(req, resp);
	}
	
	/**
	 * 支付准备,准备好支付金额,支付的订单 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String payPrepare(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String oid = req.getParameter("oid");
		req.setAttribute("order", orderService.loadOrder(oid));
		return "f:/jsps/order/pay.jsp";
	}
	
	/**
	 * 支付功能
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String pay(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 使用易宝支付平台
		 * 易宝支付平台统一使用GBK/GB2312编码方式
		 * 所以涉及到中文的参数需注意编码格式
		 * 1、构建13个需要的参数
		 * 2、拿密钥和13个参数做加密算法得到hmac参数
		 * 3、重定向到易宝的支付网关
		 */
		//加载配置文件,配置文件中含有商户编号、密钥、支付成功后的回调地址
		Properties pro = new Properties();
		pro.load(this.getClass().getClassLoader().getResourceAsStream("payment.properties"));
		
		String p0_Cmd = "Buy";	//业务类型:固定值"Buy"
		String p1_MerId = pro.getProperty("p1_MerId");	//商户编号:此处用测试编号
		String p2_Order = req.getParameter("oid");	//商户订单号:用于支付成功后我们自己修改订单状态
		String p3_Amt = "0.01";	//支付金额:因为是测试账号,钱是打给易宝的,所以设为最小值
		String p4_Cur = "CNY";	//交易币种:固定值"CNY"
		String p5_Pid = "";	//商品名称:用于显示的,可以为空字符串,但不能为空
		String p6_Pcat = "";	//商品种类:和商品名称作用一致
		String p7_Pdesc = "";	//商品描述:和上面作用一样
		String p8_Url = pro.getProperty("p8_Url");	//商户接受支付成功数据的地址:成功后需返回的地址
		String p9_SAF = "0";	//送货地址是否保留在易宝,"0"为不保留,"1"为保留
		String pa_MP = "";	//商户扩展信息
		String pd_FrpId = req.getParameter("yh");	//支付通道编码:即需跳到哪个支付通道的支付页面
		String pr_NeedResponse = "1";	//应答机制:固定值为1,即需要应答机制
		
		//从配置文件中获取密钥,密钥参与签名
		String keyValue = pro.getProperty("keyValue");
		//用加密算法加密得到hmac参数
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);
		
		//构建需要重定向的地址,以get方式请求
		//易宝支付网关地址
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
		
		//重定向到易宝支付网关
		resp.sendRedirect(url);
		
		return null;
	}
	
	/**
	 * 在易宝支付成功后的回调方法 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String payCallBack(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 1、获取易宝支付成功后返回的11个参数和签名数据hmac;
		 * 2、本地通过11个参数和keyValue进行加密算法获得hmac与易宝提供的hmac进行校验,
		 * 若一致说明数据可信;
		 */
		String p1_MerId = req.getParameter("p1_MerId");	//商户编号
		String r0_Cmd = req.getParameter("r0_Cmd");	//业务类型:固定值"Buy"
		String r1_Code = req.getParameter("r1_Code");	//支付结果:固定值"1",代表支付成功
		String r2_TrxId = req.getParameter("r2_TrxId");	//易宝支付交易流水号
		String r3_Amt = req.getParameter("r3_Amt");	//支付金额
		String r4_Cur = req.getParameter("r4_Cur");	//交易币种:固定值,返回是"RMB"
		String r5_Pid = req.getParameter("r5_Pid");	//商品名称
		String r6_Order = req.getParameter("r6_Order");	//商品订单号:易宝支付返回商户订单号
		String r7_Uid = req.getParameter("r7_Uid");	//易宝支付会员ID
		String r8_MP = req.getParameter("r8_MP");	//商品扩展信息
		String r9_BType = req.getParameter("r9_BType");	//交易结果返回类型: "1"为浏览器重定向,"2"为服务器点对点通信
		String hmac = req.getParameter("hmac");
		
		//从配置文件中获取keyValue密钥
		Properties pro = new Properties();
		pro.load(this.getClass().getClassLoader().getResourceAsStream("payment.properties"));
		String keyValue = pro.getProperty("keyValue");
		
		//通过加密算法检验数据是否有效
		boolean isEffect = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
				r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
				r8_MP, r9_BType, keyValue);
		
		//若签名不一致,则返回错误信息
		if(!isEffect){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "支付失败,你是坏人!");
			return "f:/jsps/msg.jsp";
		}
		
		//若支付成功,肯定会成功,不成功易宝不会访问此方法
		if("1".equals(r1_Code)){
			/*
			 * 如果交易结果返回类型为浏览器重定向
			 * 1、若订单状态为1则更改订单的状态为2--已支付未发货
			 * 2、保存成功信息转发到msg.jsp
			 */
			if("1".equals(r9_BType)){
				int status = orderService.findStatus(r6_Order);
				if(status == 1)
					orderService.updateStatus(r6_Order, 2);
				req.setAttribute("code", "success");
				req.setAttribute("msg", "恭喜你,支付成功!");
				return "f:/jsps/msg.jsp";
			}
			/*
			 * 如果交易结果返回类型为服务器点对点通信
			 * 需要向易宝返回"success"
			 * 
			 * 只有公网IP地址才能有交易结果返回类型为服务器点对点通信
			 */
			else if("2".equals(r9_BType)){
				resp.getWriter().print("success");
			}
		}
		
		return null;
	}
}
