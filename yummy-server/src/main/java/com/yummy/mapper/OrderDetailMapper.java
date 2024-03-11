package com.yummy.mapper;

import com.yummy.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.ManagedBean;
import java.util.List;

@Mapper
public interface OrderDetailMapper {
    void insertBatch(List<OrderDetail> orderDetailList);
}
