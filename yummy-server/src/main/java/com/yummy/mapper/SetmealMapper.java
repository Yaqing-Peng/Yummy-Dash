package com.yummy.mapper;

import com.github.pagehelper.Page;
import com.yummy.annotation.AutoFill;
import com.yummy.enumeration.OperationType;
import com.yummy.dto.SetmealPageQueryDTO;
import com.yummy.entity.Setmeal;
import com.yummy.vo.DishItemVO;
import com.yummy.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {

    /**
     * dynamic query setmeal
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);
	
	/**
     * get dish list by setmeal id
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

}
