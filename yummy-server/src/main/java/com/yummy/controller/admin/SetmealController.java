package com.yummy.controller.admin;

import com.yummy.dto.SetmealDTO;
import com.yummy.dto.SetmealPageQueryDTO;
import com.yummy.result.PageResult;
import com.yummy.result.Result;
import com.yummy.service.SetmealService;
import com.yummy.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Setmeal management
 */
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "Setmeal APIs")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * Add Setmeal
     *
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("Add Setmeal")
    @CacheEvict(cacheNames = "setmealCache",key = "#setmealDTO.categoryId")//key: setmealCache::100
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * Setmeal page query
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("Setmeal page query")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Setmeal batch delete
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("Setmeal batch delete")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result delete(@RequestParam List<Long> ids) {
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * Get Setmeal by id
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("Get Setmeal by id")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }

    /**
     * Update setmeal
     *
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Update setmeal")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * Update setmeal status
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("Update setmeal status")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result startOrStop(@PathVariable Integer status, Long id) {
        setmealService.startOrStop(status, id);
        return Result.success();
    }
}
