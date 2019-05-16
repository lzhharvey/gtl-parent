<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" charset="utf-8" src="/js/jquery.cookie.js"></script>
<table class="easyui-datagrid" id="userList" title="用户列表" 
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/user/list',method:'get',pageSize:30,toolbar:toolbar">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th data-options="field:'id',width:60">用户ID</th>
            <th data-options="field:'username',width:100">用户姓名</th>
            <th data-options="field:'password',width:250">密码</th>
            <th data-options="field:'phone',width:130">手机</th>
            <th data-options="field:'email',width:130">邮箱</th>
            <th data-options="field:'created',width:130,align:'center',formatter:TAOTAO.formatDateTime">创建日期</th>
            <th data-options="field:'updated',width:130,align:'center',formatter:TAOTAO.formatDateTime">更新日期</th>
        </tr>
    </thead>
</table>
<div id="userEditWindow" class="easyui-window" title="编辑用户" data-options="modal:true,closed:true,iconCls:'icon-save',href:'/user-edit'" style="width:80%;height:80%;padding:10px;">
</div>

<script>

    function getSelectionsIds(){
    	var userList = $("#userList");
    	var sels = userList.datagrid("getSelections");
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
    //     	$(".tree-title:contains('新增用户')").parent().click();
    //     }
    // },
        {
        text:'编辑',
        iconCls:'icon-edit',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','必须选择一个用户才能编辑!');
        		return ;
        	}
        	if(ids.indexOf(',') > 0){
        		$.messager.alert('提示','只能选择一个用户!');
        		return ;
        	}
        	
        	$("#userEditWindow").window({
        		onLoad :function(){
        			//回显数据
        			var data = $("#userList").datagrid("getSelections")[0];
        			data.priceView = TAOTAO.formatPrice(data.price);
        			$("#userEditForm").form("load",data);
        			
        			// 加载用户描述
        			// $.getJSON('/user/desc/'+data.id,function(_data){
        			// 	if(_data.status == 200){
        			// 		//UM.getEditor('usereEditDescEditor').setContent(_data.data.userDesc, false);
        			// 		userEditEditor.html(_data.data.userDesc);
        			// 	}
        			// });
        			
        			//加载用户规格
        			// $.getJSON('/rest/user/param/user/query/'+data.id,function(_data){
        			// 	if(_data && _data.status == 200 && _data.data && _data.data.paramData){
        			// 		$("#userEditForm .params").show();
        			// 		$("#userEditForm [name=userParams]").val(_data.data.paramData);
        			// 		$("#userEditForm [name=userParamId]").val(_data.data.id);
        			//
        			// 		//回显用户规格
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
        			// 		 $("#userEditForm .params td").eq(1).html(html);
        			// 	}
        			// });
        			
        			TAOTAO.init({
        				"pics" : data.image,
        				"cid" : data.cid,
        				fun:function(node){
        					TAOTAO.changeItemParam(node, "userEditForm");
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
        		$.messager.alert('提示','未选中用户!');
        		return ;
        	}
        	$.messager.confirm('确认','确定删除ID为 '+ids+' 的用户吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/user/delete",params, function(data){
                        if(data.status == 200){
            				$.messager.alert('提示','删除用户成功!',undefined,function(){
            					$("#userList").datagrid("reload");
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
        //         // alert($.cookie('search_user_cookie'));
        //         $("#userSearchWindow").window({
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
    //     		$.messager.alert('提示','未选中用户!');
    //     		return ;
    //     	}
    //     	$.messager.confirm('确认','确定上架ID为 '+ids+' 的用户吗？',function(r){
    //     	    if (r){
    //     	    	var params = {"ids":ids};
    //             	$.post("/rest/user/reshelf",params, function(data){
    //         			if(data.status == 200){
    //         				$.messager.alert('提示','上架用户成功!',undefined,function(){
    //         					$("#userList").datagrid("reload");
    //         				});
    //         			}
    //         		});
    //     	    }
    //     	});
    //     }
    // }

    ];
</script>