<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div style="padding:10px 10px 10px 10px">
	<form id="orderShippingEditForm" class="orderShippingForm" method="post">
		<%--<input type="hidden" name="id"/>--%>
		<input type="hidden" name="orderId"/>
	    <table cellpadding="5">

	        <tr>
	            <td>收货人:</td>
	            <td><input class="easyui-textbox" type="text" name="receiverName" data-options="required:true" style="width: 280px;"></input></td>
	        </tr>
			<tr>
				<td>收货人电话:</td>
				<td><input class="easyui-numberbox" type="text" name="receiverPhone" data-options="min:0,max:99999999999,required:true" />
				</td>
			</tr>
	        <tr>
	            <td>收货人手机:</td>
	            <td><input class="easyui-numberbox" type="text" name="receiverMobile" data-options="min:0,max:99999999999,required:true"/></td>
	        </tr>
			<tr>
				<td>省份:</td>
				<td><input class="easyui-textbox" type="text" name="receiverState" data-options="min:0,max:99999999999,required:true" />
				</td>
			</tr>
	        <tr>
	            <td>收货城市:</td>
	            <td><input class="easyui-textbox" type="text" name="receiverCity" data-options="min:0,max:99999999999,required:true" />
	            </td>
	        </tr>
			<%--<tr>--%>
				<%--<td>收货区:</td>--%>
				<%--<td><input class="easyui-textbox" type="text" name="receiverDistrict" data-options="min:0,max:99999999999,required:true" />--%>
				<%--</td>--%>
			<%--</tr>--%>
			<tr>
				<td>收货地址:</td>
				<td><input class="easyui-textbox" type="text" name="receiverAddress" data-options="min:0,max:99999999999,required:true" />
				</td>
			</tr>
			<%--<tr>--%>
				<%--<td>邮箱:</td>--%>
				<%--<td><input class="easyui-textbox" type="text" name="email" data-options="min:1,max:99999999,precision:2,required:true" />--%>
				<%--</td>--%>
			<%--</tr>--%>
	    </table>
	    <input type="hidden" name="orderShippingParams"/>
	    <input type="hidden" name="orderShippingParamId"/>
	</form>
	<div style="padding:5px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">提交</a>
	</div>
</div>
<script type="text/javascript">
	var orderShippingEditEditor ;
	$(function(){
		//实例化编辑器
		orderShippingEditEditor = TAOTAO.createEditor("#orderShippingEditForm [name=desc]");
	});
	
	function submitForm(){

        if(!$('#orderShippingEditForm').form('validate')){
			$.messager.alert('提示','表单还未填写完成!');
			return ;
		}

        // $("#orderShippingEditForm [name=price]").val(eval($("#orderShippingEditForm [name=priceView]").val()) * 1000);
		// orderShippingEditEditor.sync();

        var paramJson = [];
		$("#orderShippingEditForm .params li").each(function(i,e){
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

        $("#orderShippingEditForm [name=orderShippingParams]").val(paramJson);
        $.post("/orderShipping/update",$("#orderShippingEditForm").serialize(), function(data){
			if(data.status == 200){
                $.messager.alert('提示','修改订单物流信息成功!','info',function(){
					$("#orderShippingEditWindow").window('close');
					$("#orderShippingList").datagrid("reload");
				});
			}
		});
	}
</script>
