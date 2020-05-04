function like(btn, entityType, entityId) {
    $.post(
        // url
        CONTEXT_PATH + "/like",
        // 给服务器的数据
        {"entityType": entityType, "entityId": entityId},
        // 回调函数
        function (data) {
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
