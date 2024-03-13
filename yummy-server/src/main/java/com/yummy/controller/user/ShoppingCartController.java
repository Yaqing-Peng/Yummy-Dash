package com.yummy.controller.user;

import com.yummy.dto.ShoppingCartDTO;
import com.yummy.entity.ShoppingCart;
import com.yummy.result.Result;
import com.yummy.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Api(tags = "Shopping cart APIs")
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * Add shopping cart items
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("Add shopping cart items")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("Add shopping cart items :{}", shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * Get shopping cart item list
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Get shopping cart item list")
    public Result<List<ShoppingCart>> list(){
        log.info("Get shopping cart item list");
        List<ShoppingCart> shoppingCarts = shoppingCartService.shoppingCartList();
        return Result.success(shoppingCarts);
    }

    /**
     * Clean all items in shopping cart
     * @return
     */
    @DeleteMapping("/clean")
    @ApiOperation("Clean all items in shopping cart")
    public Result clean(){
        log.info("Clean all items in shopping cart");
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }

    /**
     * minus one item at shopping cart
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/sub")
    @ApiOperation("Minus one item at shopping cart")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("minus one item at shopping cart：{}", shoppingCartDTO);
        shoppingCartService.subShoppingCart(shoppingCartDTO);
        return Result.success();
    }
}
