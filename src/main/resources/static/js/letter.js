$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
	$("#sendModal").modal("hide");
	var toUser = $('#recipient-name').val();
	var content = $('#message-text').val();
	$.post(
		// url
		CONTEXT_PATH + "/letter/send",
		// 发送给后端的数据
		{'toName': toUser, 'content': content},
		// 回调函数
		function (data) {
			// 接受后端传递的数据
			data = $.parseJSON(data);
			if(data.code === 0){
				$('#hintBody').text("发送成功!");
			}else{
				$('#hintBody').text(data.msg);
			}
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

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}
