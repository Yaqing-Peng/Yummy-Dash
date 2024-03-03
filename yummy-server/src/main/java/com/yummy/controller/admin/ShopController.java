package com.yummy.controller.admin;

import com.yummy.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


@RestController("adminShopController")
@Slf4j
@RequestMapping("/admin/shop")
@Api("Shop APIs-Admin")
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;
    private static final String KEY = "SHOP_STATUS";

    /**
     * Set shop status
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("Set shop status")
    public Result setStatus(@PathVariable Integer status){
        log.info("Set shop status: {}", status == 0 ? "Opening" : "Closed");
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

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
