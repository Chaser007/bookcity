package cn.cdut.bookcity.book.domain;

/**
 * 对应高级搜索图书的表单类
 * @author huangyong
 * @date 2018年4月29日 下午11:22:32
 */
public class BookQueryForm {
	
	private String bname;
	private String author;
	private String press;
	
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPress() {
		return press;
	}
	public void setPress(String press) {
		this.press = press;
	}
}
