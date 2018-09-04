package cn.cdut.bookcity.expression;

/**
 * 表达式类,用于组合查询sql语句的拼接
 * @author huangyong
 * @date 2018年4月27日 下午6:08:14
 */
public class Expression {
	private String name;
	private String operator;
	private String value;

	public Expression(){}
	
	/**
	 * 这个构造函数用于 IS NULL表达式 
	 * @param name
	 * @param operator
	 */
	public Expression(String name, String operator) {
		this(name, operator, null);
	}

	public Expression(String name, String operator, String value) {
		this.name = name;
		this.operator = operator;
		this.value = value;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
