package com.yummy.service;

import com.yummy.dto.OrdersSubmitDTO;
import com.yummy.vo.OrderSubmitVO;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
}
