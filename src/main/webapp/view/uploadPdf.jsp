<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<title>日报上传</title>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="assets/css/bootstrap.css" rel="stylesheet" media="screen">
<link rel="stylesheet" href="assets/layer/theme/default/layer.css">
<link rel="stylesheet" href="assets/plugins/weui/style/weui.css" />
<link rel="stylesheet" href="assets/plugins/weui/style/example.css" />
<link rel="stylesheet" href="assets/layui/css/layui.css">
<link rel="stylesheet" href="assets/style/is.css" />
<style>
#request-proxy .dialog-img-code .weui-cell {
	padding: 5px 0
}

#request-proxy .dialog-img-code .weui-dialog__bd:first-child {
	padding: 0
}

#request-proxy .dialog-img-code .weui-input {
	padding-left: 10px
}

#request-proxy .weui-vcode-btn {
	margin-left: 0;
	width: 9rem
}

.loginLink {
	text-align: center;
	margin-top: 0.8rem;
}

.loginLink_text {
	display: inline-block;
}

.loginLink_text a {
	font-size: 0.92rem;
	color: #009b63;
}

.loginLink_text a:last-child {
	color: #333;
}

.loginLink_text span {
	margin: 0 0.3rem;
	height: 0.9rem;
}
</style>
<title>Insert title here</title>
</head>
<body>
	<div class="container" id="container" style="width: 500px;">
		<div class="page js_show" id="request-proxy">
			<div class="page__bd">
				<form id="uploadForm" method="post" accept="multiple/form-data">
					<div class="weui-cells weui-cells_form">
						<div class="weui-cell">
							<div class="weui-cell__bd  is-text-center">上传表单</div>
						</div>
						<!--登入数据start  -->
						<div class="weui-cell">
							<div class="weui-cell__hd">
								<label class="weui-label">文件</label>
							</div>
							<div class="weui-cell__bd">
								<input type="file" name="file" class="weui-input" />
							</div>
						</div>
						<div class="weui-cell">
							<div class="weui-cell__hd">
								<label class="weui-label">日期</label>
							</div>
							<div class="weui-cell__bd">
								<input type="text" name="file_date" id="file_date"
									class="layui-input layer-date">
							</div>
						</div>
					</div>
					<div class="weui-btn-area">
						<button class="weui-btn weui-btn_primary" type="button"
							onclick="sumbitBtn()">上传</button>
					</div>
					<div class="weui-btn-area">
						<button class="weui-btn" type="button"
							onclick="senderEmail()">人工发送</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="assets/js/jquery-3.3.1.min.js"></script>
	<script type="text/javascript" src="assets/js/jquery.form.js"></script>
	<script type="text/javascript" src="assets/layer/layer.js"></script>
	<script type="text/javascript" src="assets/layui/layui.js"></script>
	<script>
		layui.use('laydate', function() {
			var laydate = layui.laydate;

			laydate.render({
				elem : '#file_date',
				type : 'date',
				value : new Date(),
				format : 'yyyy-M-d',
				isInitValue : true
			});

		});
		function sumbitBtn() {
			$("#uploadForm").ajaxSubmit({
				url : 'main.do?method=upload',
				dataType : 'json',
				success : function(result) {
					layer.msg(result.msg);
				},
				error : function() {
					layer.msg("服务器未响应");
				}

			})
		}
		function senderEmail() {
			$.ajax({
				url : 'main.do?method=sender',
				type : 'get',
				dataType : 'json',
				success : function(result) {
					layer.msg(result.msg);
				},
				error : function() {
					layer.msg("服务器未响应");
				}
			})
		}
	</script>
</body>
</html>