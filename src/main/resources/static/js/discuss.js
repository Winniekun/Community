$(function () {
  $("#topBtn").click(setTop);
  $("#wonderfulBtn").click(setWonderful);
  $("#deleteBtn").click(setDelete);

})

function like(btn, entityType, entityId, entityUserId, postId) {
    $.post(
        // url
        CONTEXT_PATH + "/like",
        // 给服务器的数据
        {"entityType": entityType, "entityId": entityId, "entityUserId": entityUserId, "postId": postId},
        // 回调函数
        function (data) {
            console.log(data);
            data = $.parseJSON(data);
            if(data.code === 0){
                $(btn).children('i').text(data.likeCount);
                $(btn).children('b').text(data.likeStatus === 1?'已赞':'赞');

            }else{
                alert(data.msg);
            }
        }
    )
}

// 置顶
function setTop() {
    $.post(
        // url
        CONTEXT_PATH + "/discuss/top",
        // 返回给服务器的数据
        {"id": $('#postId').val()},
        // 回调函数
        function (data) {
            data = $.parseJSON(data);
            if(data.code === 0){
                $("#topBtn").attr("disabled", "disabled")
            }else{
                alert(data.msg)
            }
        }
    );
}
// 加精
function setWonderful() {
    $.post(
        // url
        CONTEXT_PATH + "/discuss/wonderful",
        // 返回给服务器的数据
        {"id": $('#postId').val()},
        // 回调函数
        function (data) {
            data = $.parseJSON(data);
            if(data.code === 0){
                $("#wonderfulBtn").attr("disabled", "disabled")
            }else{
                alert(data.msg)
            }
        }
    );
}
// 删除
function setDelete() {
    $.post(
        // url
        CONTEXT_PATH + "/discuss/delete",
        // 返回给服务器的数据
        {"id": $('#postId').val()},
        // 回调函数
        function (data) {
            data = $.parseJSON(data);
            if(data.code === 0){
                location.href= CONTEXT_PATH + "/index";
            }else{
                alert(data.msg)
            }
        }
    );
}
