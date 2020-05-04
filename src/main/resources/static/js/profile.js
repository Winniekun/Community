$(function () {
    $(".follow-btn").click(follow);
});

function follow() {
    var btn = this;
    if ($(btn).hasClass("btn-info")) {
        // 关注TA
        $.post(
            // url
            CONTEXT_PATH + "/follow",
            // 发送给后端数据
            {"entityType": 3, "entityId": $(btn).prev().val()},
            // 回调函数
            function (data) {
                data = $.parseJSON(data);
                if (data.code === 0) {
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            }
        );
        // $(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
    } else {
        // 取消TA
        $.post(
            // url
            CONTEXT_PATH + "/unfollow",
            // 发送给后端数据
            {"entityType": 3, "entityId": $(btn).prev().val()},
            console.log("测试"),
            // 回调函数
            function (data) {
                data = $.parseJSON(data);
                if (data.code === 0) {
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            });
        // 取消关注
        // $(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
    }

}
