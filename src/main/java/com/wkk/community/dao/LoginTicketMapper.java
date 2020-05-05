package com.wkk.community.dao;

import com.wkk.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @Time: 2020/4/30下午12:37
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Mapper
@Deprecated
public interface LoginTicketMapper {
    //使用注解
    @Insert({
            "INSERT INTO  login_ticket(user_id,ticket,status,expired) ",
            "VALUES (#{userId},#{ticket},#{status},#{expired})"
    })
    // 自增id
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "SELECT id,user_id,ticket,status,expired ",
            "FROM login_ticket WHERE ticket=#{ticket} "
    })
    LoginTicket selectByTicket(String ticket);

    @Update({
            "<script>",
            "UPDATE login_ticket SET status=#{status} WHERE ticket=#{ticket} ",
            "<if test=\"ticket!=null\"> ",
            "and 1=1 ",
            "</if>",
            "</script>"
    })
    int updateStatus(String ticket, int status);

}
