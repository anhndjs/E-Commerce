package com.molla.admin.repository;

import com.molla.common.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository <Category, Integer> {
    @Query("select  c from Category c where c.parent.id is Null")
    public List<Category> findRootCategories();

    public Category findByName(String name);
    public Category findByAlias(String name);
}
