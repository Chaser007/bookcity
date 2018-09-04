package cn.cdut.bookcity.expression;

/**
 * ���ʽ��,������ϲ�ѯsql����ƴ��
 * @author huangyong
 * @date 2018��4��27�� ����6:08:14
 */
public class Expression {
	private String name;
	private String operator;
	private String value;

	public Expression(){}
	
	/**
	 * ������캯������ IS NULL���ʽ 
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
