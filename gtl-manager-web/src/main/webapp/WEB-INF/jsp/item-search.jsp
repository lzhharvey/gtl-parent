<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/jquery.cookie.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div style="padding:10px 10px 10px 10px">
	<form id="itemeSearchForm" class="itemForm" method="post">
		<input type="hidden" name="id"/>
	    <table cellpadding="5">
			<%--<tr>--%>
				<%--<td>商品ID:</td>--%>
				<%--<td><input class="easyui-numberbox" type="text" name="idView" data-options="min:1,max:99999999,precision:2,required:true" />--%>
					<%--<input type="hidden" name="id"/>--%>
				<%--</td>--%>
			<%--</tr>--%>
	        <tr>
	            <td>商品标题:</td>
	            <td><input class="easyui-textbox" type="text" name="title" data-options="required:true" style="width: 280px;"></input></td>
	        </tr>

	        <%--<tr>--%>
	            <%--<td>商品价格:</td>--%>
	            <%--<td><input class="easyui-numberbox" type="text" name="priceView" data-options="min:1,max:99999999,precision:2,required:true" />--%>
	            	<%--<input type="hidden" name="price"/>--%>
	            <%--</td>--%>
	        <%--</tr>--%>
	        <%--<tr>--%>
	            <%--<td>库存数量:</td>--%>
	            <%--<td><input class="easyui-numberbox" type="text" name="num" data-options="min:1,max:99999999,precision:0,required:true" /></td>--%>
	        <%--</tr>--%>
	    </table>
	    <input type="hidden" name="itemParams"/>
	    <input type="hidden" name="itemParamId"/>
	</form>
	<div style="padding:5px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">搜索</a>
	</div>
</div>
<script type="text/javascript">

	function submitForm(){
	    //获取表单值
		// if ($("#itemeSearchForm [name=idView]").val()!=null && $("#itemeSearchForm [name=idView]").val().length!=0) {
        //     $("#itemeSearchForm [name=id]").val(eval($("#itemeSearchForm [name=idView]").val()));
        // }
        if ($("#itemeSearchForm [name=title]").val()!=null&&("#itemeSearchForm [name=title]").val().length!=0) {
            $("#itemeSearchForm [name=title]").val(eval($("#itemeSearchForm [name=title]").val()));
        }
        // $("#itemeSearchForm [name=price]").val(eval($("#itemeSearchForm [name=priceView]").val()) * 1000);
        // $("#itemeSearchForm [name=num]").val(eval($("#itemeSearchForm [name=num]").val()) );
        // var title=$("#itemeSearchForm [name=title]").val().replace(" ","%20");
		//
        // $.messager.alert('提示','搜索商品成功!','info',function(){
        //     $("#itemSearchWindow").window('close');
        //     $("#itemList").datagrid("reload");
            //改变itemList的data-options里的url
            // $("#itemList").attr("data-options","singleSelect:false,collapsible:true,pagination:true," +
            //     "url:'/item/getItem?title="+title+"',method:'get',pageSize:30,toolbar:toolbar");
        // });

		 $.post(
		    "/item/getItem",
			$("#itemeSearchForm").serialize(),
		 	function(data){
                 alert($("#itemList").attr("data-options",""));
				    $.messager.alert('提示','搜索商品成功!','info',function(){
					$("#itemSearchWindow").window('close');
					$("#itemList").datagrid("reload");
				});
                $("#itemList").attr("data-options","singleSelect:false,collapsible:true,pagination:true," +
                    "url:'/item/getItem?title="+title+"',method:'get',pageSize:30,toolbar:toolbar");
		});
	}
</script>
