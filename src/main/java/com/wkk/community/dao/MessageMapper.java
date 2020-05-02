package com.wkk.community.dao;

import com.wkk.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Time: 2020/5/1下午9:43
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Mapper
public interface MessageMapper {
    // 查询当前用户的会话列表, 针对每个会话值返回最新私信
    List<Message> selectConversations(int userId, int offset, int limit);

    // 查询当前用户的会话数量
    int selectConversationCount(int userId);

    // 查询某个会话包含的私信列表
    List<Message> selectLetters(String conversationId, int offset, int limit);

    // 查询某个会话所包含的私信数量
    int selectLetterCount(String conversationId);

    // 查询未读私信数量
    int selectLetterUnReadCount(int userId,  String conversationId);

    // 新增消息
    int insertMessage(Message message);

    // 修改消息状态
    int updateStatus(List<Integer> ids, int status);


}
