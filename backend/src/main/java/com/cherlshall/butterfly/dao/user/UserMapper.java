package com.cherlshall.butterfly.dao.user;

import com.cherlshall.butterfly.entity.user.User;
import com.cherlshall.butterfly.entity.user.UserDetail;
import com.cherlshall.butterfly.entity.user.UserTag;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 根据用户名和密码查询用户id和权限 一般用于登陆
     */
    @Select("select id, authority from user where user_name = #{userName} and password = #{password}")
    User getIdAndAuthority(@Param("userName") String userName, @Param("password") String password);

    /**
     * 根据用户id查询用户所有标签
     */
    @Select("select id, label from user_tag where user_id = #{userId}")
    List<UserTag> getTagsByUserId(int userId);

    /**
     * 根据用户id查询用户详细信息
     */
    @Select("select * from user where id = #{id}")
    @Results({
            @Result(column = "id", property = "tags", javaType = List.class,
            many = @Many(select = "com.cherlshall.butterfly.dao.user.UserMapper.getTagsByUserId"))
    })
    UserDetail getUserDetailById(int id);
}
