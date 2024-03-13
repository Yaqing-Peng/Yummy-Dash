package com.yummy.task;

import com.yummy.entity.Orders;
import com.yummy.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * Scheduled to process timeout orders every one minute
     */
    //@Scheduled(cron = "0 * * * * ? ")
    @Scheduled(cron = "0/10 * * * * ?")
    public void processTimeOutOrders() {
        log.info("Scheduled to process timeout orders: {}", LocalDateTime.now());
        //process orders in previous 15 minutes
        LocalDateTime time = LocalDateTime.now().minusMinutes(15);
        List<Orders> ordersList = orderMapper.getByStatusAndTimeLT(Orders.PENDING_PAYMENT, time);

        if(ordersList != null && ordersList.size() > 0){
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("Timeout");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /**
     * Scheduled to process delivered orders every day at 1am
     */
    //@Scheduled(cron = "0 0 1 * * ? ")
    @Scheduled(cron = "1/10 * * * * ?")
    public void processDeliveredOrder() {
        log.info("Scheduled to process delivered orders: {}", LocalDateTime.now());
        //process orders in previous day: 1am - 1h
        LocalDateTime time = LocalDateTime.now().minusMinutes(60);
        List<Orders> ordersList = orderMapper.getByStatusAndTimeLT(Orders.DELIVERY_IN_PROGRESS, time);

        if(ordersList != null && ordersList.size() > 0){
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
