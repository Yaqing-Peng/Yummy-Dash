package com.yummy.mapper;

import com.yummy.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    void insert(Orders orders);
}
