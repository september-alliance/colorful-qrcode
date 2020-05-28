<meta charset="utf-8">
<title>登录</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link rel="shortcut icon" href="/assets/pages/logo.png" type="image/x-icon" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
<link rel="stylesheet" href="/assets/layuiadmin/layui/css/layui.css" media="all">
<script src="/assets/layuiadmin/layui/layui.js"></script> 
<script src="/assets/common/jquery-2.0.3.min.js"></script>
<script src="/assets/september/common.js"></script>
<script src="/assets/layuiadmin/layui/layui.js"></script>
<script src="/assets/pages/login/form.js"></script>
<style type="text/css">
.left img{
    width: 600px;
}
.top{
	height:100px;
}
.top .title{
	display: inline-block;
	vertical-align: top;
    height: 40px;
    line-height: 40px;
    font-size: 23px;
    margin-top: 10px;
    margin-left: 10px;
    color: #696dff;
}
.top img{
    margin-left: 40px;
	height: 40px;
	margin-top: 10px;
}
.left{
	float:left;
}
.login-wrap{
	float: left;
    height: 252px;
    width: 290px;
    background: white;
    margin-top: 64px;
    border-radius: 8px;
    margin-left: 107px;
}
.bottom{
	height: 50px;
    position: fixed;
    text-align: center;
    width: 100%;
    bottom: 28px;
	color: white;
}
.company-info{
	font-size:16px;
}
.center{
	width: 1100px;
    height: 334px;
    margin-left: auto;
    margin-right: auto;
}
.input-wrap{
	width: 80%;
    margin-left: auto;
    margin-right: auto;
    margin-top: 32px;
    text-align: center;
}
.account-info{
	height: 36px;
    line-height: 36px;
    width: 100%;
	margin-top: 9px;
	padding-left: 38px;
    /* placeholder字体大小  */
    font-size: 13px;
    border-radius: 21px;
    border: 1px solid #ccc;
}
.verifyCode{
	height: 36px;
    line-height: 36px;
    width: 167px;
    float: left;
}
.code-image{
	width: 70px;
    height: 36px;
	margin-top: 9px;
	cursor: pointer;
}
.login-btn{
	width: 100%;
    height: 35px;
    line-height: 35px;
    background: linear-gradient(to right,#1602ff , #00a9ff);
    margin-top: 25px;
    border: 1px solid rgba(77,105,246,1);
    color: white;
    font-size: 15px;
    font-weight: 400;
    border-radius: 21px;
}
.input-area{
	margin-top: 15px;
}
#username{
	background: url(/assets/images/login/account.png) no-repeat 12px;
   	background-size: 15px;
}
#password{
	background: url(/assets/images/login/pwd.png) no-repeat 12px;
   	background-size: 15px;
}
</style>

<body style="background:#1c2c59">
	<div class="top">
	    <img src="/assets/images/login/logo.png" /><div class="title">东鼎车辆超载测量与监控系统</div>
	</div>
	<div class="center">
		<div class="left">
		    <img src="/assets/images/login/left.png" />
		</div>
		<div class="login-wrap">
			<div class="input-wrap">
				<form id="form" class="layui-form">
				<div style="font-weight: 400;font-size: 15px;">管理系统登录</div>
				<div class="input-area">
					
					<div class="">
						<input class="account-info" id="username" lay-verify="username" type="text" placeholder="用户名" autocomplete="off" />
					</div>
					<div class="">
						<input class="account-info" id="password" lay-verify="password" type="password" placeholder="密码" autocomplete="off" />
					</div>
				</div>
				<button class="login-btn" lay-submit lay-filter="submit_button" >登录</button>
				</form>
			</div>
		</div>
	</div>
	<div class="bottom">
		<img src="/assets/images/login/bottom.png" style="width: 201px;height: 31px;margin-left: auto;display: inline-block;"/>
	</div>
</body>
<script>
	
	layui.config({
		base : '../../assets/layuiadmin/'
	}).extend({
		index : 'lib/index'
	}).use([ 'index', 'form' ], function() {
		var form = layui.form;
		/* 自定义验证规则 */
		//addVerify(form);

		form.on("submit", function(data) {
			var username = $("#username").val();
			var password = $("#password").val();
			
			sajax({
				type : "POST",
				url : '/doLogin',
				data : {
					username : username,
					password : password
				},
				success : function(data) {
					url = getCookie('_lastRequestUrl');
					if(url){
						window.location.href = url;
					}else{
						window.location.href = '/weightData/weightData';
					}
				}
			});
			return false;
		});
	});

	function refreshCode(img){
		$(img).attr('src','/imagecode/get?'+new Date().getTime());
	}
</script>
</html>