package com.cherlshall.butterfly.m2.dao;

import com.cherlshall.butterfly.m2.entity.dto.ProtocolName;
import com.cherlshall.butterfly.m2.entity.po.Protocol;
import com.cherlshall.butterfly.m2.entity.vo.ProtocolVO;
import com.cherlshall.butterfly.sql.driver.CommonSelectLangDriver;
import com.cherlshall.butterfly.sql.driver.CommonUpdateLangDriver;
import com.cherlshall.butterfly.sql.driver.SimpleInsertLangDriver;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author hu.tengfei
 * @date 2020/1/7
 */
@Mapper
public interface ProtocolDao {

    @Insert("insert into protocol (#{protocol})")
    @Lang(SimpleInsertLangDriver.class)
    int insert(Protocol protocol);

    @Delete("delete from protocol where id = #{id}")
    int delete(Integer id);

    @Update("update protocol (#{protocolVO})")
    @Lang(CommonUpdateLangDriver.class)
    int update(ProtocolVO protocolVO);

    @Select("select * from protocol where id = #{id}")
    Protocol findById(Integer id);

    @Select("select * from protocol (#{protocolVO})")
    @Lang(CommonSelectLangDriver.class)
    List<Protocol> listByPage(ProtocolVO protocolVO);

    @Select("select * from protocol (#{protocolVO})")
    @Lang(CommonSelectLangDriver.class)
    @Results({
            @Result(column = "protocol_id", property = "protocolId"),
            @Result(column = "protocol_id", property = "protocolEnName",
                    one = @One(select = "com.cherlshall.butterfly.m2.dao.ProtocolDao.getEnNameById"))
    })
    List<Protocol> listWithProtocolNameByPage(ProtocolVO protocolVO);

    @Select("select count(1) from protocol (#{protocolVO})")
    @Lang(CommonSelectLangDriver.class)
    Long count(ProtocolVO protocolVO);

    @Select("select id, en_name from protocol where category = #{category}")
    List<ProtocolName> namesByCategory(Integer category);

    @Select("select id, en_name from protocol where category = #{category} and protocol_id = #{protocolId}")
    List<ProtocolName> namesByCategoryAndProtocolId(Integer category, Integer protocolId);

    @Select("select en_name from protocol where id = #{id}")
    String getEnNameById(Integer id);

    @Update("update protocol set active = #{active} where id = #{id}")
    int updateActive(@Param("active") Integer active, @Param("id") Integer id);
}
