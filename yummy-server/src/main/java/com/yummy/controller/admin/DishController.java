package com.yummy.controller.admin;

import com.yummy.dto.DishDTO;
import com.yummy.mapper.DishMapper;
import com.yummy.result.Result;
import com.yummy.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PrivateKey;

@RestController
@Slf4j
@Api(tags = "Dish API")
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("Add a new dish")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("Add a new dish");
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }
}
