package com.wkk.community.dao;

import com.wkk.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Time: 2020/4/28下午10:24
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Mapper
public interface DiscussPostMapper {
    /**
     * 查询帖子
     * @param userId 用户id
     * @param offset 起始位置
     * @param limit 每一页显示多少条数据
     * @return
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    /**
     * 查询帖子
     * @param userId
     * @return
     */
    // param 用于给参数取别名
    // 如果只有一个参数，并且在<if>里使用，则必须使用param
    int selectDiscussPostrows(@Param("userId") int userId);

    /**
     *
     * @param id discuss的主键id
     * @return
     */
    DiscussPost selectDiscussPostById(int id);


    /**
     * 增加帖子
     */

    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 更新帖子的评论量
     */
    int updateCommentCount(int id, int commentCount);

    /**
     * 置顶
     */
    int updateType(int id, int type);

    /**
     * 加精
     */
    int updateStatus(int id, int status);
    

}
