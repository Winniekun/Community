$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	// 获取标题和内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
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
