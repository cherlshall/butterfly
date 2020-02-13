package com.cherlshall.butterfly.m2.service;

import com.cherlshall.butterfly.m2.entity.po.Type;

import java.util.List;

public interface TypeService {

    void insert(Type type);
    void changeActive(Integer id, Integer active);
    void changeOrder(Integer id, Integer displayOrder);
    void update(Type type);
    List<Type> list();
    List<Type> listActive();
}
