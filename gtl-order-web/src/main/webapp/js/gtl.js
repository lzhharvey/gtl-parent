var TT = gtl = {
	checkLogin : function(){
		var _ticket = $.cookie("TT_TOKEN");
		if(!_ticket){
			return ;
		}
        $.ajax({
            url : "http://localhost:8088/page/token/" + _ticket,
            dataType : "jsonp",
            type : "GET",
            success : function(data){
                if(data.status == 200){
                    var username = data.data.username;
                    var userid=data.data.id;
                    var html = username + "，欢迎来到淘淘商城！<a class=\"link-logout\" id=\"logout\" >[退出]</a>";
                    $("#loginbar").html(html);
                    $("#userId").val(userid);
                    $("#logout").click(function () {
                        $.ajax({
                            url : "http://localhost:8088/page/logout/" + _ticket,
                            dataType : "jsonp",
                            type : "GET",
                            error: function(request) {  //失败
                                alert("登出失败！");
                            },
                            success: function(data) {  //成功
                                if (data.status == 200){
                                    alert("登出成功！");
                                    window.location.href="http://localhost:8082"
                                }
                            }
                        });
                    });
                }
            }
        });
	}
}
$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	TT.checkLogin();
});