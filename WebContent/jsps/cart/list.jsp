<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>cartlist.jsp</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link rel="stylesheet" type="text/css" href="<c:url value='/css/cart/list.css'/>">

<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/basePath.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/round.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/round.js'/>"></script>

<script type="text/javascript">
	$(function(){
		showTotal();
		
		$("#selectAll").click(function(){
			var checked = $(this).attr("checked");
			selectAll(checked);
			enableJiesuan(checked);
			showTotal();
		});
		
		$("input:checkbox[name='checkboxBtn']").click(function(){
			var checkedCount = $("input:checkbox[name='checkboxBtn'][checked='true']").length;
			var totalCount = $("input:checkbox[name='checkboxBtn']").length;
			//如果被选中的复选框等于总数量则全选打勾,并使结算按钮有效
			if(checkedCount == totalCount){
				$("#selectAll").attr("checked",true);
				enableJiesuan(true);
			}
			//如果被选中的复选框的数量等于0全选不打勾,并使结算按钮无效
			else if(checkedCount == 0){
				$("#selectAll").attr("checked",false);
				enableJiesuan(false);
			}
			//否则即小于总数量全选不打勾,结算按钮有效
			else{
				$("#selectAll").attr("checked",false);
				enableJiesuan(true);
			}
			showTotal();
		});
		
		$(".jia").click(function(){
			//获得当前条目的id
			var id = $(this).attr("id").substring(0,32);
			//获得当前条目的数量
			var quantity = Number($("#" + id + "Quantity").val());
			//发送更新数量请求
			sendUpdataQuantity(id, quantity + 1);
		});
		
		$(".jian").click(function(){
			var id = $(this).attr("id").substring(0,32);
			var quantity = Number($("#" + id + "Quantity").val());
			
			//若数量为1时再减就为删除该条目
			if(quantity == 1){
				if(confirm("确定要删除该条目?")){
					location = getContextPath() + "/CartServlet?method=batchDelete&cartItemIds=" + id;
				}	
			}
			
			sendUpdataQuantity(id, quantity - 1);
		});
	});
	
	
	/*
	 * checkbox的全选函数,
	 * bool参数为true则全选,
	 * 为false则全不选
	 */
	function selectAll(bool){
		$("input:checkbox[name='checkboxBtn']").attr("checked",bool);
	}
	
	/*
 	 * 使结算按钮是否有效
	 * bool：true时有效,false无效
	 */
	function enableJiesuan(bool){
		if(bool){
			$("#jiesuan").removeClass("kill");
			$("#jiesuan").addClass("jiesuan");
		}else{
			$("#jiesuan").removeClass("jiesuan");
			$("#jiesuan").addClass("kill");
		}
	}
	
	/*
     *	显示总计的函数
	 */
	function showTotal(){
		var total = 0;
		$("input:checkbox[name='checkboxBtn'][checked='true']").each(function(){
			var cartItemId = $(this).val();	
			var subTotal = $("#" + cartItemId + "Subtotal").text();
			//Number()为js的强制转换,将字符创强转为数字以便相加
			total += Number(subTotal);
		});
		$("#total").text(round(total,2));
	}
	
	/*
	 * 批量删除购物车条目
	 */
	function batchDelete(){
		var cartItemIdArray = new Array();
		$("input:checkbox[name='checkboxBtn'][checked='true']").each(function(){
			cartItemIdArray.push($(this).val());
		});
		if(cartItemIdArray.length == 0){
			return false;
		}
		var contextPath = getContextPath();
		var path = "/CartServlet?method=batchDelete&cartItemIds=" + cartItemIdArray.toString();
		//相当于为a标签指定href
		location = contextPath + path;
	} 
	
	/*
	 * ajax发送更改条目数量的函数
	 */
	function sendUpdataQuantity(id, quantity){
		$.ajax({
			url: getContextPath() + "/CartServlet",
			type: "post",
			data: {method: "ajaxUpdateQuantity", cartItemId: id, quantity: quantity},
			async: false,
			cache: false,
			dataType: "json",
			success: function(result){
				$("#" + id + "Quantity").val(result.quantity);
				$("#" + id + "Subtotal").text(result.subTotal);
				showTotal();
			}
		});
	}
	
	/*
	 * 结算按钮触发函数提交表单form1
	 */
	function submitForm(){
		var cartItemIdArray = new Array();
		//循环拿到打了勾的条目id
		$("input:checkbox[name='checkboxBtn'][checked='true']").each(function(){
			cartItemIdArray.push($(this).val());
		});
		
		//给表单中的input的val属性赋值
		$("#cartItemIds").val(cartItemIdArray.toString());
		$("#hiddenTotal").val($("#total").text());
		//提交form1表单
		$("#form1").submit();
	}
	
	
</script>
</head>
<body>

	<c:choose>
	  <c:when test="${empty cartItemList }">

		<table width="95%" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td align="right"><img align="top"
					src="<c:url value='/images/icon_empty.png'/>" /></td>
				<td><span class="spanEmpty">您的购物车中暂时没有商品</span></td>
			</tr>
		</table>
		
   	  </c:when>
	  <c:otherwise>
	  
		<table width="95%" align="center" cellpadding="0" cellspacing="0">
			<tr align="center" bgcolor="#efeae5">
				<td align="left" width="50px">
					<input type="checkbox" id="selectAll" checked="checked" /><label for="selectAll">全选</label>
				</td>
				<td colspan="2">商品名称</td>
				<td>单价</td>
				<td>数量</td>
				<td>小计</td>
				<td>操作</td>
			</tr>


		  <c:forEach items="${cartItemList }" var="cartItem">
			<tr align="center">
				<td align="left">
					<input value="${cartItem.cartItemId }" type="checkbox" name="checkboxBtn" checked="checked" />
				</td>
				<td align="left" width="70px">
					<a class="linkImage" href="<c:url value='/BookServlet?method=load&bid=${cartItem.book.bid }'/>">
						<img border="0"	width="54" align="top" src="<c:url value='/${cartItem.book.image_b }'/>" />
					</a>
				</td>
				<td align="left" width="400px">
					<a href="<c:url value='/BookServlet?method=load&bid=${cartItem.book.bid }'/>"><span>${cartItem.book.bname }</span></a>
				</td>
				<td>
					<span>&yen;<span class="currPrice" id="${cartItem.cartItemId }CurrPrice">${cartItem.book.currPrice }</span></span>
				</td>
				<td>
					<a class="jian" id="${cartItem.cartItemId }Dec"></a>
					<input class="quantity"	readonly="readonly" id="${cartItem.cartItemId }Quantity" type="text" value="${cartItem.quantity }" />
					<a class="jia" id="${cartItem.cartItemId }Inc"></a>
				</td>
				<td width="100px">
					<span class="price_n">&yen;<span class="subTotal" id="${cartItem.cartItemId }Subtotal">${cartItem.subTotal }</span></span>
				</td>
				<td>
					<a href="<c:url value='/CartServlet?method=batchDelete&cartItemIds=${cartItem.cartItemId }'/>">删除</a>
				</td>
			</tr>
		  </c:forEach>


			<tr>
				<td colspan="4" class="tdBatchDelete">
					<a href="javascript: batchDelete();">批量删除</a></td>
				<td colspan="3" align="right" class="tdTotal">
					<span>总计：</span>
					<span class="price_t">&yen;<span id="total"></span></span>
				</td>
			</tr>
			<tr>
				<td colspan="7" align="right">
					<a href="javascript: submitForm();" id="jiesuan" class="jiesuan"></a>
				</td>
			</tr>
		</table>
		<form id="form1" action="<c:url value='/CartServlet'/>" method="post">
			<input type="hidden" name="cartItemIds" id="cartItemIds" /> 
			<input type="hidden" name="method" value="loadCartItems" />
			<input type="hidden" name="total" id="hiddenTotal"/>
		</form>
	  </c:otherwise>
	</c:choose>
</body>
</html>
