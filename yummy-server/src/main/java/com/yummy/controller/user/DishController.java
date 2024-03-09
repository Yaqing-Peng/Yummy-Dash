package com.yummy.controller.user;

import com.yummy.constant.StatusConstant;
import com.yummy.entity.Dish;
import com.yummy.result.Result;
import com.yummy.service.DishService;
import com.yummy.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "Customer-Dish APIs")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * get dish list by category id
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("get dish list by category id")
    public Result<List<DishVO>> list(Long categoryId) {
        //1.try get in redis
        String key = "dish_" + categoryId;
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if(list != null && list.size() > 0){
            return Result.success(list);
        }

        //2.if not in redis, get in mysql and write into redis
        //dynamic get dish list by cate id and status
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//get on sale dishes

        list = dishService.listWithFlavor(dish);
        redisTemplate.opsForValue().set(key, list);

        return Result.success(list);
    }

}
