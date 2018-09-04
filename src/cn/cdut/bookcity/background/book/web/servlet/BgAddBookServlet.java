package cn.cdut.bookcity.background.book.web.servlet;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.cdut.bookcity.book.domain.BookBean;
import cn.cdut.bookcity.book.service.BookServiceImpl;
import cn.cdut.bookcity.category.domain.CategoryBean;
import cn.cdut.bookcity.category.service.CategoryServiceImpl;
import cn.cdut.util.common.CommonUtils;

/**
 * 该servlet用于添加图书功能:
 *  因为添加图书功能中要上传图片,所以form表单的属性enctype=multipart/form-data;
 *  从前端传过来的数据是二进制,所以HttpServletRequest的getParameter()方法则不能使用,
 *  也就不能采用继承自己的BaseServlet来处理.这里用到commons-fileupload.jar和commons-io.jar
 * 
 * @author huangyong
 * @date 2018年5月15日 下午4:34:39
 */
public class BgAddBookServlet extends HttpServlet {
	
	private static BookServiceImpl bookService = new BookServiceImpl();

	/**
	 * 当form表单的属性enctype=multipart/form-data时,均采用
	 * post请求,因为get请求有数据限制
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		//创建一个文件工厂,可以指定其缓冲区大小和文件保存临时目录
		DiskFileItemFactory factory = new DiskFileItemFactory();
		
		//创建解析上传文件的类
		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		//设置上传文件名的编码
		fileUpload.setHeaderEncoding("UTF-8");
		//设置上传单个文件大小限制
		fileUpload.setFileSizeMax(100*1024);
		//设置上传总文件大小限制
		fileUpload.setSizeMax(1000 * 1024);
		
		List<FileItem> fileItemList = null;
		try {
			//解析request得到FileItem集合
			fileItemList = fileUpload.parseRequest(request);
		} catch (FileUploadException e) {
			goError("上传文件过大", request, response);
			return;
		}
		
		//创建Map集合用来保存表单中的表单字段
		Map<String, Object> map = new HashMap<String, Object>();
		for(FileItem fileItem : fileItemList) {
			//判断是表单字段则封装到map中
			if(fileItem.isFormField()) {
				map.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
			}
			//否则,则是上传文件
			else {
				//获得表单input框的name属性值
				String fieldName = fileItem.getFieldName();
				//获得上传文件名
				String fileName = fileItem.getName();
				
				//截取文件名,因为浏览器不同,上传的文件名可能是绝对路径
				int index = fileName.lastIndexOf("\\");
				if(index != -1) {
					fileName = fileName.substring(index + 1);
				}
				//若类型不是.jpg类型的图片则返回错误信息
				if(! fileName.toLowerCase().endsWith(".jpg")) {
					goError("请上传jpg类型的图片", request, response);
					return;
				}
				//给文件名加个uuid前缀防止因名称重复产生覆盖
				fileName = CommonUtils.uuid() + "_" + fileName;
				
				//使用ServletContext的getResource()方法得到资源路径
				URL url = request.getServletContext().getResource("/book_img");
				try {
					URI uri = url.toURI();
					// 创建图片文件
					File imageFile = new File(new File(uri), fileName);
					//将上传文件输出到本地
					fileItem.write(imageFile);
					//保存图片路径到map中
					map.put(fieldName, "book_img/" + fileName);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		//将map集合中的内容映射到BookBean对象中,并补全属性
		BookBean newBook = CommonUtils.map2Bean(map, BookBean.class);
		newBook.setBid(CommonUtils.uuid());
		CategoryBean category = new CategoryBean();
		category.setCid((String) map.get("cid"));
		newBook.setCategory(category);
		
		//调用service添加book
		bookService.addBook(newBook);
		request.setAttribute("msg", "成功上架图书");
		request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request, response);
	}
	
	/**
	 * 处理在/adminjsps/admin/book/add.jsp页面显示错误信息的方法
	 * @param msg
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException 
	 * @return void   返回类型
	 */
	private void goError(String msg, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("msg", msg);
		/*
		 * 保存一级分类到request中,用于下拉列表选择,
		 * 任何要转发到/adminjsps/admin/book/add.jsp页面的必须保存一级分类到request中.
		 */
		request.setAttribute("parents", new CategoryServiceImpl().findFirstLevel());
		request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
	}

}
