package com.cherlshall.butterfly.m2.service;

import com.cherlshall.butterfly.common.vo.PageData;
import com.cherlshall.butterfly.m2.entity.po.Field;
import com.cherlshall.butterfly.m2.entity.vo.FieldVO;

import java.util.List;

/**
 * @author hu.tengfei
 * @date 2020/1/7
 */
public interface FieldService {

    void insert(Field field);

    void delete(Integer id);

    void update(FieldVO fieldVO);

    PageData<Field> listByPage(FieldVO fieldVO);

    void changeActive(Integer active, Integer id);
}
