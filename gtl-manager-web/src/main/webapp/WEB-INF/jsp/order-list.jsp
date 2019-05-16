<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" charset="utf-8" src="/js/jquery.cookie.js"></script>
<table class="easyui-datagrid" id="orderList" title="订单列表"
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/order/list',method:'get',pageSize:30,toolbar:toolbar">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
            <th data-options="field:'orderId',width:100">订单ID</th>
            <th data-options="field:'payment',width:100">订单价</th>
            <th data-options="field:'postFee',width:40">邮费</th>
            <th data-options="field:'createTime',width:130,align:'center',formatter:TAOTAO.formatDateTime">下单时间</th>
            <th data-options="field:'updateTime',width:130,align:'center',formatter:TAOTAO.formatDateTime">订单修改时间</th>
            <th data-options="field:'userId',width:60">用户ID</th>
        </tr>
    </thead>
</table>
<div id="orderEditWindow" class="easyui-window" title="编辑订单" data-options="modal:true,closed:true,iconCls:'icon-save',href:'/order-edit'" style="width:80%;height:80%;padding:10px;">
</div>

<script>

    function getSelectionsIds(){
    	var orderList = $("#orderList");
    	var sels = orderList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].orderId);
    	}
    	ids = ids.join(",");
    	return ids;
    }
    
    var toolbar = [
    //     {
    //     text:'新增',
    //     iconCls:'icon-add',
    //     handler:function(){
    //     	$(".tree-title:contains('新增订单')").parent().click();
    //     }
    // },
        {
        text:'编辑',
        iconCls:'icon-edit',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','必须选择一个订单才能编辑!');
        		return ;
        	}
        	if(ids.indexOf(',') > 0){
        		$.messager.alert('提示','只能选择一个订单!');
        		return ;
        	}
        	
        	$("#orderEditWindow").window({
        		onLoad :function(){
        			//回显数据
        			var data = $("#orderList").datagrid("getSelections")[0];
        			data.priceView = TAOTAO.formatPrice(data.price);
        			$("#orderEditForm").form("load",data);
        			
        			// 加载订单描述
        			// $.getJSON('/order/desc/'+data.id,function(_data){
        			// 	if(_data.status == 200){
        			// 		//UM.getEditor('ordereEditDescEditor').setContent(_data.data.orderDesc, false);
        			// 		orderEditEditor.html(_data.data.orderDesc);
        			// 	}
        			// });
        			
        			//加载订单规格
        			// $.getJSON('/rest/order/param/order/query/'+data.id,function(_data){
        			// 	if(_data && _data.status == 200 && _data.data && _data.data.paramData){
        			// 		$("#orderEditForm .params").show();
        			// 		$("#orderEditForm [name=orderParams]").val(_data.data.paramData);
        			// 		$("#orderEditForm [name=orderParamId]").val(_data.data.id);
        			//
        			// 		//回显订单规格
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
        			// 		 $("#orderEditForm .params td").eq(1).html(html);
        			// 	}
        			// });
        			
        			TAOTAO.init({
        				"pics" : data.image,
        				"cid" : data.cid,
        				fun:function(node){
        					TAOTAO.changeItemParam(node, "orderEditForm");
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
        		$.messager.alert('提示','未选中订单!');
        		return ;
        	}
        	$.messager.confirm('确认','确定删除ID为'+ids+'的订单吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/order/delete",params, function(data){
                        if(data.status == 200){
            				$.messager.alert('提示','删除订单成功!',undefined,function(){
            					$("#orderList").datagrid("reload");
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
        //         // alert($.cookie('search_order_cookie'));
        //         $("#orderSearchWindow").window({
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
    //     		$.messager.alert('提示','未选中订单!');
    //     		return ;
    //     	}
    //     	$.messager.confirm('确认','确定上架ID为 '+ids+' 的订单吗？',function(r){
    //     	    if (r){
    //     	    	var params = {"ids":ids};
    //             	$.post("/rest/order/reshelf",params, function(data){
    //         			if(data.status == 200){
    //         				$.messager.alert('提示','上架订单成功!',undefined,function(){
    //         					$("#orderList").datagrid("reload");
    //         				});
    //         			}
    //         		});
    //     	    }
    //     	});
    //     }
    // }

    ];
</script>