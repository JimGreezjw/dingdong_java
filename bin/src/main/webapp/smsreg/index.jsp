<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	String msg = (String) request.getAttribute("msg");
	if (msg == null || "".equals(msg)) {
		msg = "";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>ID生成</title>
<link type="text/css" rel="stylesheet"
	href="jquery.mobile-1.4.5/jquery.mobile-1.4.5.min.css" />
<script type="text/javascript"
	src="jquery.mobile-1.4.5/jquery-1.11.3.js"></script>
<script type="text/javascript"
	src="jquery.mobile-1.4.5/jquery.mobile-1.4.5.js"></script>

</head>
<body>
	<div id="idcodepage" data-role="page">
		<div data-role="header">手机验证码生成</div>
		<div data-role="content" class="content">
			<fieldset data-role="fieldcontain" id="inputArea">
				<table id="mainTable">
					<tr>
						<td><input type="tel" id="mobno" colSpan="2" placeholder='请输入手机号码……' data-clear-btn="true"></input></td>
						<td><input type="button" id="btn1" value="获取验证码" /></td>
					</tr>
					<tr>
						<td><input type="tel" id="serverno" placeholder='手机验证码'></input></td>
					</tr>
					<tr>
						<td><input type="tel" id="clientno" placeholder='图像验证码'></input></td>
						<td>
							<img id="serverimg" style="cursor:pointer;" onClick="getServerValidateCode();"
								src="<%=basePath%>imgCodeServlet" />
					</td>
					</tr>
					<tr>
						<td><input type="button" id="btn2" value="立即注册" /></td>
					</tr>
				</table>
			</fieldset>
		</div>
		<div data-role="footer">手机验证码生成测试</div>
	</div>

	<script type="text/javascript">
		var waitSec = -1;
		// 设置初始的验证码
		// getServerValidateCode();
		
		function getServerValidateCode() {
			var $_img = $("#serverimg");
			$_img.src = $_img.src + "?";
		}

		$("#btn1").on("click", function() {
			var mobno = $("#mobno").val();
			var reg = /^1[0-9]{10}/;

			if (!reg.test(mobno)) {
				alert("请输入正确的手机号码");
			} else {
				getCodeFromServer();
			}
		});
		
		$("#btn2").on("click", function () {
			// 手机号码的校验
			var mobno = $("#mobno").val();
			var reg = /^1[0-9]{10}/;

			if (!reg.test(mobno)) {
				alert("请输入正确的手机号码");
				return;
			} else {
				// getCodeFromServer();
			}
			
			// 服务端验证码
			var serverno = $("#serverno").val();
			if (serverno) {
				
			}
			else {
				alert("请输入手机验证码！");
				return;
			}
			
			// 图像验证码
			var clientno = $("#clientno").val();
			if (clientno) {
				
			}
			else {
				alert("请输入图像验证码！");
				return;
			}
			
			$.ajax({
				url : "/jmproject/FinishValiServlet",
				type : "post",
				cache : false,
				data : {
					mobileNo : mobno,
					serverNo : serverno,
					clientNo : clientno
				},
				success : function(result) {
					var isOk = result.ok;
					var msg = result.msg;
					if (isOk == 1) {
						alert(msg);	
					} else {
						// var msg = result.mag;
						alert(msg);
					}
				},
				error : function() {
					// alert(arguments[1]);
				},
			});
		});
		
		function getCodeFromServer() {
			$.ajax({
				url : "/jmproject/MessageServlet",
				type : "post",
				cache : false,
				data : {
					beanName : "messageService",
					methodName : "sendSmsMessage",
					mobileNo : "" + $("#mobno").val(),
				},
				success : function(result) {
					var isOk = result.ok;
					if (isOk == 1) {
						// alert("成功");	
						waitSec = 60;
						clockFunc();
					} else {
						var msg = result.mag;
						alert(msg);
					}
				},
				error : function() {
					alert(arguments[1]);
				},
			});
		}

		function clockFunc() {
			if (waitSec > 0) {
				waitSec--;
				$("#btn1").attr("disabled", true);
				$("#btn1").val("验证码已经发送，" + waitSec + "后可以再次发送").button(
						"refresh");
				setTimeout("clockFunc()", 1000);
			} else {
				$("#btn1").removeAttr("disabled");
				$("#btn1").val("获取验证码").button("refresh");
			}
		}

		
	</script>
</body>
</html>
