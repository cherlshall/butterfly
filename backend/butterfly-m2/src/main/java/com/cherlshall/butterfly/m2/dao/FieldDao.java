package com.cherlshall.butterfly.m2.dao;

import com.cherlshall.butterfly.m2.entity.po.Field;
import com.cherlshall.butterfly.m2.entity.vo.FieldVO;
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
public interface FieldDao {

    @Insert("insert into field (#{field})")
    @Lang(SimpleInsertLangDriver.class)
    int insert(Field field);

    @Delete("delete from field where id = #{id}")
    int delete(Integer id);

    @Update("update field (#{FieldVO})")
    @Lang(CommonUpdateLangDriver.class)
    int update(FieldVO FieldVO);

    @Select("select * from field where id = #{id}")
    Field findById(Integer id);

    @Select("select * from field (#{fieldVO})")
    @Lang(CommonSelectLangDriver.class)
    @Results({
            @Result(column = "link", property = "link"),
            @Result(column = "link", property = "linkEnName",
                    one = @One(select = "com.cherlshall.butterfly.m2.dao.ProtocolDao.getEnNameById"))
    })
    List<Field> listByPage(FieldVO fieldVO);

    @Select("select count(1) from field (#{fieldVO})")
    @Lang(CommonSelectLangDriver.class)
    Long count(FieldVO fieldVO);

    @Select("select sum(size) from field where protocol_id = #{protocolId}")
    Integer sumSizeByProtocolId(Integer protocolId);

    @Update("update field set active = #{active} where id = #{id}")
    int updateActive(@Param("active") Integer active, @Param("id") Integer id);
}
