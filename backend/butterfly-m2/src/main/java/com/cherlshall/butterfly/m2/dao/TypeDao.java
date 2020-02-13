package com.cherlshall.butterfly.m2.dao;

import com.cherlshall.butterfly.m2.entity.po.Type;
import com.cherlshall.butterfly.sql.driver.CommonUpdateLangDriver;
import com.cherlshall.butterfly.sql.driver.SimpleInsertLangDriver;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TypeDao {

    @Insert("insert into type (#{type})")
    @Lang(SimpleInsertLangDriver.class)
    int insert(Type type);

    @Update("update type set active = #{active} where id = #{id}")
    int changeActive(@Param("id") Integer id, @Param("active") Integer active);

    @Update("update type set display_order = #{displayOrder} where id = #{id}")
    int changeOrder(@Param("id") Integer id, @Param("displayOrder") Integer displayOrder);

    @Update("update type (#{type})")
    @Lang(CommonUpdateLangDriver.class)
    int update(Type type);

    @Select("select * from type order by display_order")
    List<Type> list();

    @Select("select * from type where active = #{active} order by display_order")
    List<Type> listByActive(Integer active);

    @Select("select display_order from type order by display_order desc limit 1")
    Integer findMaxDisplayOrder();

    @Select("select * from type where id = #{id}")
    Type findById(Integer id);

    @Select("select count(1) from type where cn_name = #{cnName}")
    int findByCnName(String cnName);

    @Select("select count(1) from type where enName = #{enName}")
    int findByEnName(String enName);
}
