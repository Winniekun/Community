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
     *
     * @param userId 用户id
     * @param offset 起始位置
     * @param limit 每一页显示多少条数据
     * @return
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    /**
     *
     * @param userId
     * @return
     */
    // param 用于给参数取别名
    // 如果只有一个参数，并且在<if>里使用，则必须使用param
    int selectDiscussPostrows(@Param("userId") int userId);



}
