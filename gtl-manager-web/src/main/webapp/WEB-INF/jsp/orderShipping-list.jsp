<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" charset="utf-8" src="/js/jquery.cookie.js"></script>
<table class="easyui-datagrid" id="orderShippingList" title="订单物流信息列表"
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/orderShipping/list',method:'get',pageSize:30,toolbar:toolbar">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
            <th data-options="field:'orderId',width:100">订单ID</th>
            <th data-options="field:'receiverName',width:100">收货人</th>
            <th data-options="field:'receiverPhone',width:100">收货人电话</th>
            <th data-options="field:'receiverMobile',width:100">收货人手机</th>
            <th data-options="field:'receiverState',width:100">省份</th>
            <th data-options="field:'receiverCity',width:100">收货城市</th>
            <%--<th data-options="field:'receiverDistrict',width:100">收货区</th>--%>
            <th data-options="field:'receiverAddress',width:100">收货地址</th>
            <th data-options="field:'created',width:130,align:'center',formatter:TAOTAO.formatDateTime">创建日期</th>
            <th data-options="field:'updated',width:130,align:'center',formatter:TAOTAO.formatDateTime">更新日期</th>
        </tr>
    </thead>
</table>
<div id="orderShippingEditWindow" class="easyui-window" title="编辑订单物流信息" data-options="modal:true,closed:true,iconCls:'icon-save',href:'/orderShipping-edit'" style="width:80%;height:80%;padding:10px;">
</div>

<script>

    function getSelectionsIds(){
    	var orderShippingList = $("#orderShippingList");
    	var sels = orderShippingList.datagrid("getSelections");
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
    //     	$(".tree-title:contains('新增订单物流信息')").parent().click();
    //     }
    // },
        {
        text:'编辑',
        iconCls:'icon-edit',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','必须选择一个订单物流信息才能编辑!');
        		return ;
        	}
        	if(ids.indexOf(',') > 0){
        		$.messager.alert('提示','只能选择一个订单物流信息!');
        		return ;
        	}
        	
        	$("#orderShippingEditWindow").window({
        		onLoad :function(){
        			//回显数据
        			var data = $("#orderShippingList").datagrid("getSelections")[0];
        			data.priceView = TAOTAO.formatPrice(data.price);
        			$("#orderShippingEditForm").form("load",data);
        			
        			// 加载订单物流信息描述
        			// $.getJSON('/orderShipping/desc/'+data.id,function(_data){
        			// 	if(_data.status == 200){
        			// 		//UM.getEditor('orderShippingeEditDescEditor').setContent(_data.data.orderShippingDesc, false);
        			// 		orderShippingEditEditor.html(_data.data.orderShippingDesc);
        			// 	}
        			// });
        			
        			//加载订单物流信息规格
        			// $.getJSON('/rest/orderShipping/param/orderShipping/query/'+data.id,function(_data){
        			// 	if(_data && _data.status == 200 && _data.data && _data.data.paramData){
        			// 		$("#orderShippingEditForm .params").show();
        			// 		$("#orderShippingEditForm [name=orderShippingParams]").val(_data.data.paramData);
        			// 		$("#orderShippingEditForm [name=orderShippingParamId]").val(_data.data.id);
        			//
        			// 		//回显订单物流信息规格
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
        			// 		 $("#orderShippingEditForm .params td").eq(1).html(html);
        			// 	}
        			// });
        			
        			TAOTAO.init({
        				"pics" : data.image,
        				"cid" : data.cid,
        				fun:function(node){
        					TAOTAO.changeItemParam(node, "orderShippingEditForm");
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
        		$.messager.alert('提示','未选中订单物流信息!');
        		return ;
        	}
        	$.messager.confirm('确认','确定删除ID为'+ids+'的订单物流信息吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/orderShipping/delete",params, function(data){
                        if(data.status == 200){
            				$.messager.alert('提示','删除订单物流信息成功!',undefined,function(){
            					$("#orderShippingList").datagrid("reload");
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
        //         // alert($.cookie('search_orderShipping_cookie'));
        //         $("#orderShippingSearchWindow").window({
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
    //     		$.messager.alert('提示','未选中订单物流信息!');
    //     		return ;
    //     	}
    //     	$.messager.confirm('确认','确定上架ID为 '+ids+' 的订单物流信息吗？',function(r){
    //     	    if (r){
    //     	    	var params = {"ids":ids};
    //             	$.post("/rest/orderShipping/reshelf",params, function(data){
    //         			if(data.status == 200){
    //         				$.messager.alert('提示','上架订单物流信息成功!',undefined,function(){
    //         					$("#orderShippingList").datagrid("reload");
    //         				});
    //         			}
    //         		});
    //     	    }
    //     	});
    //     }
    // }

    ];
</script>