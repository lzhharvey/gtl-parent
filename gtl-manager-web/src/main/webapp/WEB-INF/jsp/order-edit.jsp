<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div style="padding:10px 10px 10px 10px">
	<form id="orderEditForm" class="orderForm" method="post">
		<input type="hidden" name="orderId"/>
	    <table cellpadding="5">
			<tr>
				<td>邮费:</td>
				<td><input class="easyui-numberbox" type="text" name="postFee" data-options="min:0,max:99999999,required:true" />
				</td>
			</tr>
	        <%--<tr>--%>
	            <%--<td>you:</td>--%>
	            <%--<td><input class="easyui-textbox" type="text" name="ordername" data-options="required:true" style="width: 280px;"></input></td>--%>
	        <%--</tr>--%>
	        <%--<tr>--%>
	            <%--<td>密码:</td>--%>
	            <%--<td><input class="easyui-textbox" name="password" data-options="multiline:true,validType:'length[0,150]'" style="height:60px;width: 280px;"></input></td>--%>
	        <%--</tr>--%>
	        <%--<tr>--%>
	            <%--<td>手机:</td>--%>
	            <%--<td><input class="easyui-numberbox" type="text" name="phone" data-options="min:1,max:99999999999,required:true" />--%>
	            <%--</td>--%>
	        <%--</tr>--%>
			<%--<tr>--%>
				<%--<td>邮箱:</td>--%>
				<%--<td><input class="easyui-textbox" type="text" name="email" data-options="min:1,max:99999999,precision:2,required:true" />--%>
				<%--</td>--%>
			<%--</tr>--%>
	    </table>
	    <input type="hidden" name="orderParams"/>
	    <input type="hidden" name="orderParamId"/>
	</form>
	<div style="padding:5px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">提交</a>
	</div>
</div>
<script type="text/javascript">
	var orderEditEditor ;
	$(function(){
		//实例化编辑器
		orderEditEditor = TAOTAO.createEditor("#orderEditForm [name=desc]");
	});
	
	function submitForm(){

        if(!$('#orderEditForm').form('validate')){
			$.messager.alert('提示','表单还未填写完成!');
			return ;
		}

        // $("#orderEditForm [name=price]").val(eval($("#orderEditForm [name=priceView]").val()) * 1000);
		// orderEditEditor.sync();

        var paramJson = [];
		$("#orderEditForm .params li").each(function(i,e){
			var trs = $(e).find("tr");
			var group = trs.eq(0).text();
			var ps = [];
			for(var i = 1;i<trs.length;i++){
				var tr = trs.eq(i);
				ps.push({
					"k" : $.trim(tr.find("td").eq(0).find("span").text()),
					"v" : $.trim(tr.find("input").val())
				});
			}
			paramJson.push({
				"group" : group,
				"params": ps
			});
		});
		paramJson = JSON.stringify(paramJson);

        $("#orderEditForm [name=orderParams]").val(paramJson);
        $.post("/order/update",$("#orderEditForm").serialize(), function(data){
			if(data.status == 200){
                $.messager.alert('提示','修改订单成功!','info',function(){
					$("#orderEditWindow").window('close');
					$("#orderList").datagrid("reload");
				});
			}
		});
	}
</script>
