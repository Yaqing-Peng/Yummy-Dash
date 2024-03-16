package com.yummy.service.impl;

import com.yummy.constant.StatusConstant;
import com.yummy.entity.Orders;
import com.yummy.mapper.DishMapper;
import com.yummy.mapper.OrderMapper;
import com.yummy.mapper.SetmealMapper;
import com.yummy.mapper.UserMapper;
import com.yummy.service.WorkspaceService;
import com.yummy.vo.BusinessDataVO;
import com.yummy.vo.DishOverViewVO;
import com.yummy.vo.OrderOverViewVO;
import com.yummy.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;


    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        Map map = new HashMap();
        map.put("begin",begin);
        map.put("end",end);

        //get total order count
        Integer totalOrderCount = orderMapper.getOrderCountByMap(map);

        map.put("status", Orders.COMPLETED);

        //get valid turnover
        Double turnover = orderMapper.getAmountSumByMap(map);
        turnover = turnover == null? 0.0 : turnover;

        //get valid order count
        Integer validOrderCount = orderMapper.getOrderCountByMap(map);

        Double unitPrice = 0.0;

        Double orderCompletionRate = 0.0;
        if(totalOrderCount != 0 && validOrderCount != 0){
            //order complete rate
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            //average order amount
            unitPrice = turnover / validOrderCount;
        }

        //new user count
        Integer newUsers = userMapper.getUserCount(map);

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }


    public OrderOverViewVO getOrderOverView() {
        Map map = new HashMap();
        map.put("begin", LocalDateTime.now().with(LocalTime.MIN));
        map.put("status", Orders.TO_BE_CONFIRMED);

        //get orders to be confirmed
        Integer waitingOrders = orderMapper.getOrderCountByMap(map);

        //get orders confirmed
        map.put("status", Orders.CONFIRMED);
        Integer deliveredOrders = orderMapper.getOrderCountByMap(map);

        //get orders completed
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.getOrderCountByMap(map);

        //get orders cancelled
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.getOrderCountByMap(map);

        //get total orders
        map.put("status", null);
        Integer allOrders = orderMapper.getOrderCountByMap(map);

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }


    public DishOverViewVO getDishOverView() {
        Map map = new HashMap();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = dishMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map);

        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }


    public SetmealOverViewVO getSetmealOverView() {
        Map map = new HashMap();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = setmealMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = setmealMapper.countByMap(map);

        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
