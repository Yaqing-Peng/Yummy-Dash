package com.yummy.service;

import com.yummy.vo.BusinessDataVO;
import com.yummy.vo.DishOverViewVO;
import com.yummy.vo.OrderOverViewVO;
import com.yummy.vo.SetmealOverViewVO;
import java.time.LocalDateTime;

public interface WorkspaceService {


    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);


    OrderOverViewVO getOrderOverView();


    DishOverViewVO getDishOverView();


    SetmealOverViewVO getSetmealOverView();

}
