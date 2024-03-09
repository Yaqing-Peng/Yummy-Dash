package com.yummy.controller.admin;

import com.yummy.dto.DishDTO;
import com.yummy.dto.DishPageQueryDTO;
import com.yummy.entity.Dish;
import com.yummy.mapper.DishMapper;
import com.yummy.result.PageResult;
import com.yummy.result.Result;
import com.yummy.service.DishService;
import com.yummy.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@Slf4j
@Api(tags = "Dish API")
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 1.Add a new dish with flavor
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("Add a new dish with flavor")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("Add a new dish with flavor");
        dishService.saveWithFlavor(dishDTO);
        //clean redis cache
        String key = String.valueOf(dishDTO.getCategoryId());
        cleanCache(key);
        return Result.success();
    }

    /**
     * 2.Dish page query
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("Dish page query")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("Dish page query: {}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 3.Batch delete dish by ids
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("Batch delete dish by ids")
    public Result delete(@RequestParam List<Long> ids){
        log.info("Batch delete dish by ids: {}", ids);
        dishService.deleteBatch(ids);
        //clean redis cache
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 4.Get dish with flavor by id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("Get dish with flavor by id")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("Get dish with flavor by id :{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 5.Update dish info
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Update dish info")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("Update dish info: {}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        //clean redis cache
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * set dish status
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("Set dish status")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        dishService.startOrStop(status, id);

        //clean redis cache
        cleanCache("dish_*");

        return Result.success();
    }

    /**
     * Get dish list by category id
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Get dish list by category id")
    public Result<List<Dish>> list(Long categoryId) {
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    /**
     * clean redis cache
     * @param pattern
     */
    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

}
