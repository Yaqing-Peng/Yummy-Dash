package com.yummy.service;

import com.yummy.dto.*;
import com.yummy.vo.*;

public interface OrderService {


    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);


    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;


    void paySuccess(String outTradeNo);

    void reminder(Long id);
}
