package com.cherlshall.butterfly.dao.user;

import com.cherlshall.butterfly.entity.user.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NoticeMapper {

    /**
     * 查询用户的通知信息
     */
    @Select("select * from notice where user_id = #{userId}")
    List<Notice> getNoticesByUserId(int userId);
}