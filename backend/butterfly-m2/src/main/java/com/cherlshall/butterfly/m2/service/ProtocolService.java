package com.cherlshall.butterfly.m2.service;

import com.cherlshall.butterfly.common.vo.PageData;
import com.cherlshall.butterfly.m2.entity.dto.ProtocolName;
import com.cherlshall.butterfly.m2.entity.po.Protocol;
import com.cherlshall.butterfly.m2.entity.vo.ProtocolVO;

import java.util.List;

/**
 * @author hu.tengfei
 * @date 2020/1/7
 */
public interface ProtocolService {

    void insert(Protocol protocol);

    void delete(Integer id);

    void update(ProtocolVO protocolVO);

    Protocol findById(Integer id);

    PageData<Protocol> listByPage(ProtocolVO protocolVO);

    List<ProtocolName> listProtocolName(Integer category);

    List<ProtocolName> listProtocolName(Integer category, Integer protocolId);

    void changeActive(Integer active, Integer id);
}
