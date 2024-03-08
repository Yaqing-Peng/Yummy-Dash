package com.yummy.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yummy.constant.MessageConstant;
import com.yummy.constant.StatusConstant;
import com.yummy.dto.DishDTO;
import com.yummy.dto.DishPageQueryDTO;
import com.yummy.entity.Dish;
import com.yummy.entity.DishFlavor;
import com.yummy.entity.Setmeal;
import com.yummy.exception.DeletionNotAllowedException;
import com.yummy.mapper.DishFlavorMapper;
import com.yummy.mapper.DishMapper;
import com.yummy.mapper.SetmealDishMapper;
import com.yummy.mapper.SetmealMapper;
import com.yummy.result.PageResult;
import com.yummy.service.DishService;
import com.yummy.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * Add a new dish with flavor
     *
     * @param dishDTO
     */
    @Transactional//ensure dish and flavor consistency
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 1.insert dish
        dishMapper.insert(dish);

        // 2.get pk from the above insert
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //3.batch insert flavor data
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * Dish page query
     *
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * Batch delete dish by ids
     *
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //check the status of dish, if on sale, can't delete
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //check the linked set meal of dish, if is linked, can't delete
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //batch delete from dish table and flavor table
        for (Long id : ids) {
            dishMapper.deleteById(id);
            dishFlavorMapper.deleteByDishId(id);
        }
    }

    /**
     * Get dish with flavor by id
     *
     * @param id
     * @return
     */
    public DishVO getByIdWithFlavor(Long id) {
        //Get dish by id
        Dish dish = dishMapper.getById(id);

        //get flavor data by dish id
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);

        //encapsulate them in VO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    /**
     * Update dish info
     *
     * @param dishDTO
     */
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //Update dish info
        dishMapper.update(dish);

        //delete original flavor data by dish id
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        //get new flavor data
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            //insert new flavor data
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public void startOrStop(Integer status, Long id) {

    }

    /**
     * 菜品起售停售
     *
     * @param status
     * @param id
     */
    @Transactional
//    public void startOrStop(Integer status, Long id) {
//        Dish dish = Dish.builder()
//                .id(id)
//                .status(status)
//                .build();
//        dishMapper.update(dish);
//
//        if (status == StatusConstant.DISABLE) {
//            // 如果是停售操作，还需要将包含当前菜品的套餐也停售
//            List<Long> dishIds = new ArrayList<>();
//            dishIds.add(id);
//            // select setmeal_id from setmeal_dish where dish_id in (?,?,?)
//            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishIds);
//            if (setmealIds != null && setmealIds.size() > 0) {
//                for (Long setmealId : setmealIds) {
//                    Setmeal setmeal = Setmeal.builder()
//                            .id(setmealId)
//                            .status(StatusConstant.DISABLE)
//                            .build();
//                    setmealMapper.update(setmeal);
//                }
//            }
//        }
//    }

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //get flavors by dish id
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
