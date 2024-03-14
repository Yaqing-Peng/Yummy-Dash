package com.yummy.mapper;

import com.yummy.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import javax.annotation.ManagedBean;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderDetailMapper {
    void insertBatch(List<OrderDetail> orderDetailList);


    @Select("select name from order_detail group by name order by count(*) desc")
    List<String> getNameRank(LocalDateTime beginTime, LocalDateTime endTime);

    @Select("select Count(*) from order_detail group by name order by count(*) desc")
    List<Integer> getCountRank(LocalDateTime beginTime, LocalDateTime endTime);
}
