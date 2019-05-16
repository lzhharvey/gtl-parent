<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" charset="utf-8" src="/js/jquery.cookie.js"></script>
<table class="easyui-datagrid" id="orderItemList" title="订单商品详情列表"
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/orderItem/list',method:'get',pageSize:30,toolbar:toolbar">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
            <th data-options="field:'id',width:100">订单商品详情ID</th>
            <th data-options="field:'itemId',width:200">商品ID</th>
            <th data-options="field:'orderId',width:100">订单ID</th>
            <th data-options="field:'num',width:100">数量</th>
            <th data-options="field:'title',width:400">标题</th>
            <th data-options="field:'price',width:100">单价</th>
            <th data-options="field:'totalFee',width:100">总价</th>
            <th data-options="field:'picPath',width:400">图片路径</th>
        </tr>
    </thead>
</table>
<div id="orderItemEditWindow" class="easyui-window" title="编辑订单商品详情" data-options="modal:true,closed:true,iconCls:'icon-save',href:'/orderItem-edit'" style="width:80%;height:80%;padding:10px;">
</div>

<script>

    function getSelectionsIds(){
    	var orderItemList = $("#orderItemList");
    	var sels = orderItemList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
    }
    
    var toolbar = [
    //     {
    //     text:'新增',
    //     iconCls:'icon-add',
    //     handler:function(){
    //     	$(".tree-title:contains('新增订单商品详情')").parent().click();
    //     }
    // },
        {
        text:'编辑',
        iconCls:'icon-edit',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','必须选择一个订单商品详情才能编辑!');
        		return ;
        	}
        	if(ids.indexOf(',') > 0){
        		$.messager.alert('提示','只能选择一个订单商品详情!');
        		return ;
        	}
        	
        	$("#orderItemEditWindow").window({
        		onLoad :function(){
        			//回显数据
        			var data = $("#orderItemList").datagrid("getSelections")[0];
        			data.priceView = TAOTAO.formatPrice(data.price);
        			$("#orderItemEditForm").form("load",data);
        			
        			// 加载订单商品详情描述
        			// $.getJSON('/orderItem/desc/'+data.id,function(_data){
        			// 	if(_data.status == 200){
        			// 		//UM.getEditor('orderItemeEditDescEditor').setContent(_data.data.orderItemDesc, false);
        			// 		orderItemEditEditor.html(_data.data.orderItemDesc);
        			// 	}
        			// });
        			
        			//加载订单商品详情规格
        			// $.getJSON('/rest/orderItem/param/orderItem/query/'+data.id,function(_data){
        			// 	if(_data && _data.status == 200 && _data.data && _data.data.paramData){
        			// 		$("#orderItemEditForm .params").show();
        			// 		$("#orderItemEditForm [name=orderItemParams]").val(_data.data.paramData);
        			// 		$("#orderItemEditForm [name=orderItemParamId]").val(_data.data.id);
        			//
        			// 		//回显订单商品详情规格
        			// 		 var paramData = JSON.parse(_data.data.paramData);
        			//
        			// 		 var html = "<ul>";
        			// 		 for(var i in paramData){
        			// 			 var pd = paramData[i];
        			// 			 html+="<li><table>";
        			// 			 html+="<tr><td colspan=\"2\" class=\"group\">"+pd.group+"</td></tr>";
        			//
        			// 			 for(var j in pd.params){
        			// 				 var ps = pd.params[j];
        			// 				 html+="<tr><td class=\"param\"><span>"+ps.k+"</span>: </td><td><input autocomplete=\"off\" type=\"text\" value='"+ps.v+"'/></td></tr>";
        			// 			 }
        			//
        			// 			 html+="</li></table>";
        			// 		 }
        			// 		 html+= "</ul>";
        			// 		 $("#orderItemEditForm .params td").eq(1).html(html);
        			// 	}
        			// });
        			
        			TAOTAO.init({
        				"pics" : data.image,
        				"cid" : data.cid,
        				fun:function(node){
        					TAOTAO.changeItemParam(node, "orderItemEditForm");
        				}
        			});
        		}
        	}).window("open");
        }
    },{
        text:'删除',
        iconCls:'icon-cancel',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','未选中订单商品详情!');
        		return ;
        	}
        	$.messager.confirm('确认','确定删除ID为'+ids+'的订单商品详情吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/orderItem/delete",params, function(data){
                        if(data.status == 200){
            				$.messager.alert('提示','删除订单商品详情成功!',undefined,function(){
            					$("#orderItemList").datagrid("reload");
            				});
            			}
            		});
        	    }
        	});
        }
     },'-',
        // {
        //     text:'查询',
        //     iconCls:'icon-search',
        //     handler:function(){
        //         // alert($.cookie('search_orderItem_cookie'));
        //         $("#orderItemSearchWindow").window({
        //
        //     }).window("open");
        //     }
        // }
    //     {
    //     text:'上架',
    //     iconCls:'icon-remove',
    //     handler:function(){
    //     	var ids = getSelectionsIds();
    //     	if(ids.length == 0){
    //     		$.messager.alert('提示','未选中订单商品详情!');
    //     		return ;
    //     	}
    //     	$.messager.confirm('确认','确定上架ID为 '+ids+' 的订单商品详情吗？',function(r){
    //     	    if (r){
    //     	    	var params = {"ids":ids};
    //             	$.post("/rest/orderItem/reshelf",params, function(data){
    //         			if(data.status == 200){
    //         				$.messager.alert('提示','上架订单商品详情成功!',undefined,function(){
    //         					$("#orderItemList").datagrid("reload");
    //         				});
    //         			}
    //         		});
    //     	    }
    //     	});
    //     }
    // }

    ];
</script>