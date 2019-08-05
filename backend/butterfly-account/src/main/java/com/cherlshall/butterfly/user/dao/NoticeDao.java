package com.cherlshall.butterfly.user.dao;

import com.cherlshall.butterfly.user.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NoticeDao {

    /**
     * 查询用户的通知信息
     */
    @Select("select * from notice where user_id = #{userId}")
    List<Notice> getNoticesByUserId(int userId);
}
