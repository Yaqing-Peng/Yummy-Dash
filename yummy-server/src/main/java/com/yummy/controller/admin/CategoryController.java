package com.yummy.controller.admin;

import com.yummy.dto.CategoryDTO;
import com.yummy.dto.CategoryPageQueryDTO;
import com.yummy.entity.Category;
import com.yummy.result.PageResult;
import com.yummy.result.Result;
import com.yummy.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * category management
 */
@RestController
@RequestMapping("/admin/category")
@Api(tags = "Category related APIs")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Add a new category
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("Add a new category")
    public Result<String> save(@RequestBody CategoryDTO categoryDTO){
        log.info("Add a new category：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * Category page query
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("Category page query")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("Category page query：{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Delete a category by id
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("Delete a category by id")
    public Result<String> deleteById(Long id){
        log.info("Delete a category by id：{}", id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * Update a category
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Update a category")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * Start/Stop category
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("Start/Stop category")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id){
        categoryService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * Get category list by id
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Get category list by id")
    public Result<List<Category>> list(Integer type){
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
