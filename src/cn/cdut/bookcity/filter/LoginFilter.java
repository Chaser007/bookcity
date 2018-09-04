package cn.cdut.bookcity.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * �û���¼������
 * @author huangyong
 * @date 2018��5��11�� ����9:21:22
 */
public class LoginFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		
		Object user = request.getSession().getAttribute("user");
		//��û��¼,��ת����msg.jsp
		if(user == null){
			request.setAttribute("code", "error");
			request.setAttribute("msg", "���¼���ܷ���!");
			request.getRequestDispatcher("/jsps/msg.jsp").forward(request, resp);
			return;
		}
		//��¼�����
		
		filterChain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
