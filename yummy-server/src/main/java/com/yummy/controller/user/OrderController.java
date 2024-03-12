package com.yummy.controller.user;

import com.yummy.dto.OrdersPaymentDTO;
import com.yummy.dto.OrdersSubmitDTO;
import com.yummy.result.Result;
import com.yummy.service.OrderService;
import com.yummy.vo.OrderPaymentVO;
import com.yummy.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@Api(tags = "User-order APIs")
@RequestMapping("/user/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * User submit orders
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("User submit orders")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("User submit orders: {}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * User order payment
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("User order payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("User order payment：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("Generate prepay order：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }
}
