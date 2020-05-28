var serviceName="";

var common = {};
common.status = {};
common.status.yes = '1';
common.status.no = '0';

/**
 * 获取根路径
 * 
 * @returns
 */
function getRootName() {
	var pathName = window.document.location.pathname;
	var projectName = pathName
			.substring(0, pathName.substr(1).indexOf('/') + 1);
	return projectName;
}

/**
 * 获取跟路径URL
 * 
 * @returns
 */
function getRootPath() {
	var curWwwPath = window.document.location.href;
	var pathName = window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	var localhostPaht = curWwwPath.substring(0, pos);	
	return (localhostPaht + serviceName);
}

//重新加载第一页
function closeWindowAndRefreshParent(){
	setTimeout(function(){
		if(window.parent.listData && typeof(window.parent.doSearch)=="function"){
			window.parent.doSearch(); //刷新父页面
		}
		var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
		parent.layer.close(index);  // 关闭layer
	},1500);
}

//重新加载父页面
function closeWindowAndRefreshParentWithoutSearch(){
	window.parent.location.reload();
	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
	parent.layer.close(index);  // 关闭layer
}

//刷新当前页
function closeWindowAndRefreshParentCurrentPage(){
	setTimeout(function(){
		if(window.parent.listData && typeof(window.parent.doSearchCurrentPage)=="function"){
			window.parent.doSearchCurrentPage(); //刷新父页面
		}
		var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
		parent.layer.close(index);  // 关闭layer
	},1500);
}

function closeChildWindow(){
	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
	parent.layer.close(index);  // 关闭layer
}

function closeChildWindowAndDoSpecial(data){
	setTimeout(function(){
		var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
	parent.doSpecial(data);
	parent.layer.close(index);
	},1500);
}

function showErrorMsg(argErrors){
	var txt = "";
	for(var i=0;i<argErrors.length;i++){
		txt+= argErrors[i].message+"<br/>";
	}
	layer.msg(txt);
}
function bindInputEnterUp(){
	$("input").keyup(function(){
		if(event.keyCode ==13){
			if(typeof(doSearch)=="undefined"){
				return;
			}
			if(doSearch){
				doSearch();
			}
          }
    });
}

$(function(){
	bindInputEnterUp();
});

function sajax(options){
	var successHandler = options.success;
	if(!options.hiddenLoding){
		var loading;
		options.beforeSend=function(){
			loading = layer.load(1, {
			  shade: [0.1,'#fff'] //0.1透明度的白色背景
			});
		};
		options.complete=function(){
			layer.close(loading);
		};
	}
	options.success=function (result) {
		if (result.code != 0) {
			if(result.errorType=='args_not_valid'){
				
				for(var i=0;i<result.argErrors.length;i++){
					var error = result.argErrors[i];
					$('input[name='+error.field+']').addClass('red-border');
				}
				//layer.msg('数据格式不正确');
				showErrorMsg(result.argErrors);
			}else if(result.errorType=='unexpect_exception'){
				layer.msg('系统内部错误');
			}else{
				layer.msg(result.desc);
			}
			return;
		}else{
			if(successHandler){
				successHandler(result.data);
			}
		}
	};
	options.error=function(){
	    layer.msg('系统异常');
	}
	return $.ajax(options);
}

function validateForm(formId) {
	if (formId == null || formId == undefined || formId === '') {
		return true;
	}

	var msg = '';
	var elements = $('#' + formId + ' :input');

	$.each(elements, function(index, ele) {
		var text = '';
		var element = $(ele);
		text = element.val();

		if (hasAttr(element, 'data-null-msg')
				&& element.attr('data-null-msg') != '' && text == '') {
			msg += element.attr('data-null-msg') + '<br />';
		} else {
			if (hasAttr(element, 'data-min-length')
					&& element.attr('data-min-length') != ''
					&& element.attr('data-min-length').indexOf(',') > -1) {
				var minArray = element.attr('data-min-length').split(',', 2);
				var minLength = parseInt(minArray[0]);

				if (!isNaN(minLength) && text.length < minLength) {
					msg += minArray[1] + '<br />';
				}
			}
		}

		if (hasAttr(element, 'data-mobile-msg')
				&& element.attr('data-mobile-msg') != ''
				&& text != ''
				&& !/^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/.test(text)) {
			msg += element.attr('data-mobile-msg') + '<br />';
		}
	});

	if (msg != '') {
		layer.msg(msg);
		return false;
	}

	return true;
}

function hasAttr(ele, name) {
	return (ele.attr(name) != null && ele.attr(name) != undefined);
}

function confirmDelete(callback){
	layer.confirm('确定要删除该条数据吗？', {
		btn : [ '是', '否' ]
	//按钮
	}, function() {
		//yes
		if(callback){
			callback();
		}
	}, function() {
		//no
	});
}

function confirmClose(callback){
	layer.confirm('内容未提交，是否确定关闭？', {
		btn : [ '是', '否' ]
	//按钮
	}, function(index) {
		//yes
		layer.close(index);
		if(callback){
			callback();
		}
	}, function() {
		//no
	});
}

function prompt(promptText, callback){
	  layer.prompt({
		  title: promptText, formType: 2, btn : [ '是', '否' ]
	  }, function(text, index){
			//yes
		  layer.close(index);
			if(callback){
				callback(text);
			}
		}, function() {
			//no
		});
}

function confirm(confirmText, callback){
	layer.confirm(confirmText, {
		btn : [ '是', '否' ]
	//按钮
	}, function() {
		//yes
		if(callback){
			callback();
		}
	}, function() {
		//no
	});
}

function confirmMultiDelete(callback){
	layer.confirm('确定要删除选中的数据吗？', {
		btn : [ '是', '否' ]
	//按钮
	}, function() {
		//yes
		if(callback){
			callback();
		}
	}, function() {
		//no
	});
}

function confirmRelease(callback){
	layer.confirm('确定要发布该条数据吗？', {
		btn : [ '是', '否' ]
	//按钮
	}, function() {
		//yes
		if(callback){
			callback();
		}
	}, function() {
		//no
	});
}

function prepareFormData(data){
	var inputs = $('.layui-form-item input');
	var inputs2 = ($('.input-elem[type="checkbox"]'));
	for(var i=0;i<inputs.length;i++){
		if(inputs[i].name && inputs[i].type=='checkbox'){
			if(data.field[inputs[i].name]=='on'){
				data.field[inputs[i].name] = 1;
			}else{
				data.field[inputs[i].name] = 0;
			}
		}
	}
	for(var i=0;i<inputs2.length;i++){
		if(inputs2[i].name && inputs2[i].type=='checkbox'){
			if(data.field[inputs2[i].name]=='on'){
				data.field[inputs2[i].name] = 1;
			}else{
				data.field[inputs2[i].name] = 0;
			}
		}
	}
}
/**
 * 表单初始化时,input checkbox radio等元素赋值时使用
 */
function formInputInitOnOff(value){
    if(value == "0")
	return "";
    return value;
}

function getCookie(name)
{
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg)){
		return unescape(arr[2]);
	}else{
		return null;
	}
}

function setCookie(name , val , timeout)
{
	if(!timeout){
		// 默认一周
		timeout = 1000*3600*24*7;
	}
	var exp = new Date();
	exp.setTime(exp.getTime() + timeout);
	document.cookie = name + "="+ escape (val) + ";path=/;expires=" + exp.toGMTString();
}