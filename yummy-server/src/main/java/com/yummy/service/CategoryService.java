package com.yummy.service;

import com.yummy.dto.CategoryDTO;
import com.yummy.dto.CategoryPageQueryDTO;
import com.yummy.entity.Category;
import com.yummy.result.PageResult;
import java.util.List;

public interface CategoryService {

    /**
     * Add a new category
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     * Category page query
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * Delete a category by id
     * @param id
     */
    void deleteById(Long id);

    /**
     * Update a category
     * @param categoryDTO
     */
    void update(CategoryDTO categoryDTO);

    /**
     * Start/Stop category
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * Get category list by id
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
