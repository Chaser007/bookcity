function changeVerifyCode() {
	$("#vCode").attr("src", getContextPath() + "/VerifyCodeServlet?" + new Date().getTime());
}