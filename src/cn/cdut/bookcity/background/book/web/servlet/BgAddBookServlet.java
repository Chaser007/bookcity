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
 * ��servlet�������ͼ�鹦��:
 *  ��Ϊ���ͼ�鹦����Ҫ�ϴ�ͼƬ,����form��������enctype=multipart/form-data;
 *  ��ǰ�˴������������Ƕ�����,����HttpServletRequest��getParameter()��������ʹ��,
 *  Ҳ�Ͳ��ܲ��ü̳��Լ���BaseServlet������.�����õ�commons-fileupload.jar��commons-io.jar
 * 
 * @author huangyong
 * @date 2018��5��15�� ����4:34:39
 */
public class BgAddBookServlet extends HttpServlet {
	
	private static BookServiceImpl bookService = new BookServiceImpl();

	/**
	 * ��form��������enctype=multipart/form-dataʱ,������
	 * post����,��Ϊget��������������
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		//����һ���ļ�����,����ָ���仺������С���ļ�������ʱĿ¼
		DiskFileItemFactory factory = new DiskFileItemFactory();
		
		//���������ϴ��ļ�����
		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		//�����ϴ��ļ����ı���
		fileUpload.setHeaderEncoding("UTF-8");
		//�����ϴ������ļ���С����
		fileUpload.setFileSizeMax(100*1024);
		//�����ϴ����ļ���С����
		fileUpload.setSizeMax(1000 * 1024);
		
		List<FileItem> fileItemList = null;
		try {
			//����request�õ�FileItem����
			fileItemList = fileUpload.parseRequest(request);
		} catch (FileUploadException e) {
			goError("�ϴ��ļ�����", request, response);
			return;
		}
		
		//����Map��������������еı��ֶ�
		Map<String, Object> map = new HashMap<String, Object>();
		for(FileItem fileItem : fileItemList) {
			//�ж��Ǳ��ֶ����װ��map��
			if(fileItem.isFormField()) {
				map.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
			}
			//����,�����ϴ��ļ�
			else {
				//��ñ�input���name����ֵ
				String fieldName = fileItem.getFieldName();
				//����ϴ��ļ���
				String fileName = fileItem.getName();
				
				//��ȡ�ļ���,��Ϊ�������ͬ,�ϴ����ļ��������Ǿ���·��
				int index = fileName.lastIndexOf("\\");
				if(index != -1) {
					fileName = fileName.substring(index + 1);
				}
				//�����Ͳ���.jpg���͵�ͼƬ�򷵻ش�����Ϣ
				if(! fileName.toLowerCase().endsWith(".jpg")) {
					goError("���ϴ�jpg���͵�ͼƬ", request, response);
					return;
				}
				//���ļ����Ӹ�uuidǰ׺��ֹ�������ظ���������
				fileName = CommonUtils.uuid() + "_" + fileName;
				
				//ʹ��ServletContext��getResource()�����õ���Դ·��
				URL url = request.getServletContext().getResource("/book_img");
				try {
					URI uri = url.toURI();
					// ����ͼƬ�ļ�
					File imageFile = new File(new File(uri), fileName);
					//���ϴ��ļ����������
					fileItem.write(imageFile);
					//����ͼƬ·����map��
					map.put(fieldName, "book_img/" + fileName);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		//��map�����е�����ӳ�䵽BookBean������,����ȫ����
		BookBean newBook = CommonUtils.map2Bean(map, BookBean.class);
		newBook.setBid(CommonUtils.uuid());
		CategoryBean category = new CategoryBean();
		category.setCid((String) map.get("cid"));
		newBook.setCategory(category);
		
		//����service���book
		bookService.addBook(newBook);
		request.setAttribute("msg", "�ɹ��ϼ�ͼ��");
		request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request, response);
	}
	
	/**
	 * ������/adminjsps/admin/book/add.jspҳ����ʾ������Ϣ�ķ���
	 * @param msg
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException 
	 * @return void   ��������
	 */
	private void goError(String msg, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("msg", msg);
		/*
		 * ����һ�����ൽrequest��,���������б�ѡ��,
		 * �κ�Ҫת����/adminjsps/admin/book/add.jspҳ��ı��뱣��һ�����ൽrequest��.
		 */
		request.setAttribute("parents", new CategoryServiceImpl().findFirstLevel());
		request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
	}

}
