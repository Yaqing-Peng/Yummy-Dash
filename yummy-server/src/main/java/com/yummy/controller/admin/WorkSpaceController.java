package com.yummy.controller.admin;

import com.yummy.result.Result;
import com.yummy.service.WorkspaceService;
import com.yummy.vo.BusinessDataVO;
import com.yummy.vo.DishOverViewVO;
import com.yummy.vo.OrderOverViewVO;
import com.yummy.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Working space
 */
@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "Working space APIs")
public class WorkSpaceController {

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * get business Data of cur date
     * @return
     */
    @GetMapping("/businessData")
    @ApiOperation("Get business Data of cur date")
    public Result<BusinessDataVO> businessData(){

        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);

        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);

        BusinessDataVO businessDataVO = workspaceService.getBusinessData(begin, end);
        return Result.success(businessDataVO);
    }

    /**
     * Get order overview
     * @return
     */
    @GetMapping("/overviewOrders")
    @ApiOperation("Get order overview")
    public Result<OrderOverViewVO> orderOverView(){
        return Result.success(workspaceService.getOrderOverView());
    }

    /**
     * Get dish overview
     * @return
     */
    @GetMapping("/overviewDishes")
    @ApiOperation("Get order overview")
    public Result<DishOverViewVO> dishOverView(){
        return Result.success(workspaceService.getDishOverView());
    }

    /**
     * Get setmeal overview
     * @return
     */
    @GetMapping("/overviewSetmeals")
    @ApiOperation("Get setmeal overview")
    public Result<SetmealOverViewVO> setmealOverView(){
        return Result.success(workspaceService.getSetmealOverView());
    }
}
