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
 * 用户登录过滤器
 * @author huangyong
 * @date 2018年5月11日 下午9:21:22
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
		//若没登录,则转发到msg.jsp
		if(user == null){
			request.setAttribute("code", "error");
			request.setAttribute("msg", "需登录才能访问!");
			request.getRequestDispatcher("/jsps/msg.jsp").forward(request, resp);
			return;
		}
		//登录则放行
		
		filterChain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
