<html>
<head>
<#include "/inc/resource.ftl">
<script src="/assets/colpick/colpick.js" type="text/javascript"></script>
<link rel="stylesheet" href="/assets/colpick/colpick.css" type="text/css"/>
<style type="text/css">
.content{
	width:100%;
	height:300px;
	margin-bottom: 20px;
    font-size: 14px;
}
.title{
	text-align: center;
    font-size: 27px;
    height: 60px;
    line-height: 60px;
}
.color-selector{
	width: 23px;
    height: 23px;
    display: inline-block;
    background: #629755;
    vertical-align: middle;
}
.result-info-item{
	margin-left: 38%;
    text-align: left;
}
.input-item{
	width: 200px;
    display: inline-block;
}
.layui-form-select{
	width: 200px;
    display: inline-block;
}
.input-item-label{
	width: 100px;
    display: inline-block;
}

#result{
	max-width: 300px;
}
</style>
</head>
<body style="">
<div class="title">多彩码生成演示</div>
<br/>

<table style="width:90%;margin-left:5%;">
<tr>
	<td style="width:50%">
		<form class="layui-form">
		<textarea id="content" name="content" placeholder="请输入需要生成多彩码的内容" class="content">请输入需要生成多彩码的内容</textarea>
		<div>
			<span class="input-item-label">放大倍数：</span>
			<select name="multiple"  class="input-item">
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
				<option value="6">6</option>
				<option value="7">7</option>
				<option value="8">8</option>
			</select>
		</div>
		<div >
			<span class="input-item-label">定位块颜色：</span>
			<input class="layui-input input-item" name="positionColor" id="positionColor" value="0x629755" />
			<div class="color-selector" onclick="openColorPick()"></div>
			<div id="picker"></div>
		</div>
		<div>
			<span class="input-item-label">容错率：</span>
			<select name="errorCorrection"  class="input-item">
				<option value="H">H</option>
				<option value="H">Q</option>
				<option value="M">M</option>
				<option value="L">L</option>
			</select>
		</div>
		<div>
			<span class="input-item-label">形态：</span>
		</div>
		<div>
			<button class="layui-btn layui-btn-normal" lay-submit>生成</button>
		</div>
		</form>
	</td>
	
	<td style="vertical-align: top;text-align:center;width: 50%;">
	<img id="result" src="/assets/images/default.gif"></img>
	<div class="result-info-item">
		<span>version:</span><span id="version"></span>
	</div>
	<div class="result-info-item">
		<span>原始像素:</span><span id="original"></span>
	</div>
	<div class="result-info-item">
		<span>实际像素:</span><span id="actual"></span>
	</div>
	</td>
</tr>
</table>
	
<script type="text/javascript">
layui.config({
      base: '../../assets/layuiadmin/'
  }).extend({
      index: 'lib/index'
  }).use(['index', 'form','upload'], function () {
      var form = layui.form;

      form.on("submit", function (data) {
          //data.field.deliverFormImage = $('#deliverFormImage').val();
          sajax({
              type: "POST",
              url: "/doEncode",
              data: data.field,
              dataType: 'json'
          }).done(function (data) {
              if (data.code == -1) {
                layer.msg(data.desc);
              } else {
              	//更新图片
              	$('#result').attr('src','data:image/jpg;base64,'+data.data.base64Image);
              	$('#version').text(data.data.version);
              	$('#original').text(data.data.originalWidth+"*"+data.data.originalHeight);
              	$('#actual').text(data.data.width+"*"+data.data.height);
              }
          });
          return false;
      });
  });

function openColorPick(){
	var defaultColor = $('#positionColor').val();
	$('#picker').colpick({
		flat:true,
		layout:'hex',
		colorScheme:'white',
		color:defaultColor,
		onSubmit:function(hsb,hex,rgb,el) {
			//alert(hex);
			$('#positionColor').val('0x'+hex);
			$('.color-selector').css('background','#'+hex);
			$(el).colpickHide();
			
		}
	});
}
</script>
</body>
</html>