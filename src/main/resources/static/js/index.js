$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	// // 发送ajax请求之前，将CSRF令牌设置到请求头中
	// var token = $("meta[name= '_csrf']").attr("content");
	// var header = $("meta[name= '_csrf_header']").attr("content");
	// $(document).ajaxSend(function (e, xhr, options) {
	// 	xhr.setRequestHeader(header, token);
	// });
	// 发送异步post
	$.post(
		// url
		CONTEXT_PATH + "/discuss/add",
		// 内容
		{"title": title, "content": content},
		// feedback
		function (data) {
			data = $.parseJSON(data);
			// 在提示框中显示返回消息
			$("#hintBody").text(data.msg);
			// 显示提示框， 过两秒自动关闭
			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide");
				// 刷新页面
				if(data.code === 0){
					window.location.reload();
				}
			}, 2000);
		}
	);


}
