package com.yummy.controller.user;

import com.yummy.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@Slf4j
@RequestMapping("/user/shop")
@Api("Shop APIs-User ")
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;
    private static final String KEY = "SHOP_STATUS";

    /**
     * Get shop status
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("Get shop status")
    public Result<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("Get shop status: {}", status == 0 ? "Opening" : "Closed");
        return Result.success(status);
    }
}
