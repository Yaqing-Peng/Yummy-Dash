package com.yummy.mapper;

import com.github.pagehelper.Page;
import com.yummy.annotation.AutoFill;
import com.yummy.dto.DishPageQueryDTO;
import com.yummy.entity.Dish;
import com.yummy.enumeration.OperationType;
import com.yummy.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {


    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);


    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);


    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);


    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);


    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    /**
     * dynamically update dish by id
     *
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     * dynamically get dish by conditions
     *
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    /**
     * get dishes by setmeal id
     * @param setmealId
     * @return
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);

    /**
     * count dishes by time and status
     * @param map
     * @return
     */
    Integer countByMap(Map map);

}
