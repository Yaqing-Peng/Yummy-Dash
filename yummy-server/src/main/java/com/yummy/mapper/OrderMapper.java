package com.yummy.mapper;

import com.github.pagehelper.Page;
import com.yummy.dto.GoodsSalesDTO;
import com.yummy.dto.OrdersPageQueryDTO;
import com.yummy.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    void insert(Orders order);


    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);


    void update(Orders orders);

    @Select("select * from orders where status = #{status} and order_time < #{time}")
    List<Orders> getByStatusAndTimeLT(Integer status, LocalDateTime time);
}
