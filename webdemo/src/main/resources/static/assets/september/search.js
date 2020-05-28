var pager = null;
var pageSize = 15;
var currentPage = 1;
var searchUrl = '';
var sortField = '';
var sortOrder = '';
var doUpdateStatusUrl = '';
function doSearch(curPage){
	currentPage = curPage;
	pager = null;
	var params = prepareParams();
	listData(params);
}

function doSearchCurrentPage(){
	var params = prepareParams();
	listData(params);
}

function prepareParams(){
	var params = {};
	params.sortOrder = sortOrder;
	params.sortField = sortField;
	var inputs = $('.layui-form-item input');
	for(var i=0;i<inputs.length;i++){
		if(inputs[i].name){
			params[inputs[i].name] = inputs[i].value;
		}
	}
	var inputs = $('.layui-form-item select');
	for(var i=0;i<inputs.length;i++){
		if(inputs[i].name){
			params[inputs[i].name] = inputs[i].value;
		}
	}
	params.currentPage = currentPage;
	params.pageSize = pageSize;
	return params;
}
function doPage(){
	var params = prepareParams();
	listData(params);
}

function listData(params) {
	sajax({
		type : "POST",
		url : searchUrl,
		data : params,
		dataType : 'json',
		success : function(page) {
			if(page.result){
				buildHtmlWithJsonArray('data_row', page.result,false, false);
			}
			buildPager(page);
			if(typeof callBackPage === "function") 
				callBackPage(page);
		}
	});
}

function doUpdateStatus(id,name,value){
        sajax({
        	type : "POST",
        	url : doUpdateStatusUrl,
        	data : "id="+id+"&"+name+"="+value
//        	dataType : 'json'
        }).done(function (data) {
            	if (data.code == -1) {
            	    if(data.desc)
            		layer.msg(data.desc);
            	    else
            		layer.msg("系统异常");
	       	 } else {
	       	    layer.msg('修改成功');
		 }
	    });;
}

function buildPager(pageResult){
	if(pager==null){
		layui.laypage.render({
		     elem: 'page'
		     ,count: pageResult.totalResult
		     ,limit: pageSize
		     ,limits: [pageSize, 30, 50]
		     ,layout: ['count', 'prev', 'page', 'next', 'limit', 'skip']
			 ,jump: function(obj){
		         pageSize=obj.limit;
		         currentPage = obj.curr;
		         if(pager==null){
		        	 pager = layui.laypage;
		         }else{
		        	 doPage();	 
		         }
		     }
	   	});
	}
}

function setDesc(field){
	sortField = field;
	sortOrder = 'desc';
	$('.layui-table-sort').removeAttr('lay-sort');
	$('#'+field+'_sort').attr('lay-sort','desc');
	doSearch(1);
}
function setAsc(field){
	sortField = field;
	sortOrder = 'asc';
	$('.layui-table-sort').removeAttr('lay-sort');
	$('#'+field+'_sort').attr('lay-sort','asc');
	doSearch(1);
}

function listDataUrl(url,clazz,archiveId) {
	sajax({
		type : "POST",
		url : url,
		data : {archiveId : archiveId},
		dataType : 'json',
		success : function(page) {
			if(page.result){
				buildHtmlWithJsonArray(clazz, page.result,false, false);
			}
			buildPager(page);
			if(typeof callBackPage === "function")
				callBackPage(page);
		}
	});
}
