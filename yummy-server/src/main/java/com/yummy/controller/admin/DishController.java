package com.yummy.controller.admin;

import com.yummy.dto.DishDTO;
import com.yummy.dto.DishPageQueryDTO;
import com.yummy.mapper.DishMapper;
import com.yummy.result.PageResult;
import com.yummy.result.Result;
import com.yummy.service.DishService;
import com.yummy.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@Api(tags = "Dish API")
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("Add a new dish with flavor")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("Add a new dish with flavor");
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("Dish page query")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("Dish page query: {}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("Batch delete dish by ids")
    public Result delete(@RequestParam List<Long> ids){
        log.info("Batch delete dish by ids: {}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * Get dish with flavor by id
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
     * Update dish info
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Update dish info")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("Update dish info: {}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }
}
